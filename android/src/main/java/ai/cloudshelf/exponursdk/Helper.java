package ai.cloudshelf.exponursdk;

import android.app.Activity;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurTag;
import com.nordicid.nurapi.NurTagStorage;
import com.nordicid.nurapi.NurApiErrors;
import com.nordicid.nurapi.NurApiException;
import com.nordicid.tdt.EPCTagEngine;

import ai.cloudshelf.exponursdk.BleScanner;
import ai.cloudshelf.exponursdk.NurDeviceScanner;
import ai.cloudshelf.exponursdk.EventCallbacks;
import ai.cloudshelf.exponursdk.NurApiAutoConnectTransport;

import static com.nordicid.nurapi.NurApi.BANK_TID;
import static com.nordicid.nurapi.NurApi.BANK_USER;

public class Helper implements NurDeviceScanner.NurDeviceScannerListener {
    private static Helper instance;

    private Helper() {}

    public static Helper getInstance() {
        if (instance == null)
            instance = new Helper();
        return instance;
    }
    private NurApiListenerExtended mNurApiListener;
    private NurApi mNurApi;
    private NurDeviceScanner mDeviceScanner;
    private NurApiAutoConnectTransport hAcTr;
    private Activity context;
    
    //device scanning
    private List<NurDeviceSpec> mDeviceList;
    private boolean mDeviceScanning = false;
    private List<String> allowedCompanyPrefixes = new ArrayList<>();
    private boolean mShouldReadUsr = false;

    //callbacks
    private EventCallbacks mCallbacks;

    private NurTagStorage mSeenTagStorage;
    private List<RFIDTag> mSeenRFIDTagStorage;

    //Continuious
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    private void debugToast(String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendOnDeviceScanStatusUpdate() {
        this.mCallbacks.onDeviceScanStatusUpdate(this.mDeviceList, this.mDeviceScanning);
    }

    public void sendOnConnectionStatusChanged(boolean isConnected) {
        this.mCallbacks.onConnectionStatusChanged(isConnected);
    }

    public void sendOnTagsDiscovered() {
        this.mCallbacks.onTagsDiscovered(this.mSeenRFIDTagStorage);
    }

    public boolean initialize(String deviceSpecAutoConnect, Activity context, EventCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
        this.mDeviceList = new ArrayList<>();
        this.mDeviceScanning = false;
        this.mNurApiListener = new NurApiListenerExtended();
        this.mSeenTagStorage = new NurTagStorage();
        this.mSeenRFIDTagStorage = new ArrayList<>();

        mNurApi = new NurApi();
        mNurApi.setListener(mNurApiListener);
    
        BleScanner.init(context);

        int requestedDevices = context.getIntent().getIntExtra("TYPE_LIST", NurDeviceScanner.ALL_DEVICES);
        this.mDeviceScanner = new NurDeviceScanner(context, requestedDevices, this, mNurApi);

        if(deviceSpecAutoConnect != null) {
            this.connect(deviceSpecAutoConnect);
        }
        return true;
    }

    public boolean terminate() {

        this.traceContinuousStop();
        this.disconnect();
        return true;
    }

    public boolean provideSettings(ArrayList<String> allowedCompanyPrefixes, Boolean readUsr) {
        this.allowedCompanyPrefixes = allowedCompanyPrefixes;
        this.mShouldReadUsr = readUsr;
        return true;
    }

    public boolean scanDevices() {
        try {
            this.mDeviceScanner.scanDevices();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean connect(String deviceSpec) {
        try {
            NurDeviceSpec spec = new NurDeviceSpec(deviceSpec);
            String strAddress;
            this.hAcTr = NurDeviceSpec.createAutoConnectTransport(context, mNurApi, spec);
            strAddress = spec.getAddress();
            this.hAcTr.setAddress(strAddress);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public boolean disconnect() {
        if (this.hAcTr != null) {
            this.hAcTr.onDestroy();
            this.hAcTr = null;
        }
        return true;
    }

    public boolean traceContinuousStart() {
        //stop the old one first if needed
        this.traceContinuousStop();

        this.scheduler = Executors.newScheduledThreadPool(1);

                // Define a task
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            traceOnce(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                int initialDelay = 0;          // Initial delay before the first execution
                int delayBetweenRuns = 250;      // Delay between task executions in milliseconds
        
                // Schedule the task with a fixed delay
                this.scheduledFuture = scheduler.scheduleWithFixedDelay(
                        task, initialDelay, delayBetweenRuns, TimeUnit.MILLISECONDS);

        return true;
    }

    public boolean traceContinuousStop() {

        stopScheduledTask();
        // Shut down the scheduler to release resources
        if (this.scheduler != null && !this.scheduler.isShutdown()) {
            this.scheduler.shutdown();
            Log.d("ScheduledTask", "Scheduler has been shut down.");
        }
        return true;
    }

    public boolean traceOnce(Boolean clearPreviousTags) throws Exception {
        if(!this.mNurApi.isConnected()) {
            return false;
        }

        if (this.mNurApi.getSetupSelectedAntenna() != NurApi.ANTENNAID_AUTOSELECT) {
            this.mNurApi.setSetupSelectedAntenna(NurApi.ANTENNAID_AUTOSELECT);
        }

        if(clearPreviousTags) {
            clearSeenTags();
        }

         try {
             // Perform inventory
             this.mNurApi.inventory();
             // Fetch tags from NUR
             this.mNurApi.fetchTags();
         } catch (NurApiException ex) {
             if (ex.error == NurApiErrors.NO_TAG)
             {
                Log.e("Helper", "No tags found.");
                return true;
             }
             throw ex;
         }

         // Handle inventoried tags
         handleInventoryResult();

        return true;
    }

    public String connectionStatus() {
        if (this.mNurApi == null || !this.mNurApi.isConnected()) {
            return "disconnected";
        }
        return "connected";
    }


    //device listener
    public void onScanStarted(){
        this.mDeviceList.clear();
        this.mDeviceScanning = true;
        this.sendOnDeviceScanStatusUpdate();
    }

    public void onDeviceFound(NurDeviceSpec device){
        this.mDeviceList.add(device);
        this.sendOnDeviceScanStatusUpdate();
    }

    public void onScanFinished(){
        this.mDeviceScanning = false;
        this.sendOnDeviceScanStatusUpdate();
    }

    public void clearSeenTags() {
        this.mNurApi.getStorage().clear();
        this.mSeenTagStorage.clear();
        this.mSeenRFIDTagStorage.clear();
    }


    private void handleInventoryResult() throws Exception {
        synchronized (this.mNurApi.getStorage()) {

            Boolean gs1Only = !this.allowedCompanyPrefixes.isEmpty();

            HashMap<String, String> tmp;
            NurTagStorage tagStorage = this.mNurApi.getStorage();
            List<RFIDTag> oldRFIDTagStorage = new ArrayList<>(this.mSeenRFIDTagStorage);

            
            for (int i = 0; i < tagStorage.size(); i++) {
                NurTag t = tagStorage.get(i);

                if (mSeenTagStorage.addTag(t)) {
                    //This only fires if we havent seen it before
                    RFIDTag mappedTag = mappedTag = new RFIDTag(t.getEpcString(), t.getRssi());
                  
                    try {
                        //Check if tag is GS1 coded. Exception fired if not and plain EPC shown.
                        //This is TDT (TagDataTranslation) library feature.
                        EPCTagEngine engine = new EPCTagEngine(t.getEpcString());
                        //Looks like it is GS1 coded, show pure Identity URI
                    
                        String gs = engine.buildPureIdentityURI();
                        String companyPrefix = engine.getSegment(0).toString();
                        String itemRef = engine.getSegment(1).toString();
                        String serialNumber = engine.getSegment(2).toString();

                        RFIDTagGS1Data gs1Data = new RFIDTagGS1Data(gs, companyPrefix, itemRef, serialNumber);
                        mappedTag.setGS1Data(gs1Data);
                        
                        if (!this.allowedCompanyPrefixes.isEmpty() && !this.allowedCompanyPrefixes.contains(companyPrefix)) {
                            // if prefix is not known then skip over as we dont want to continue;
                            continue;
                        }
                    } catch (Exception ex) {
                        if(gs1Only) {
                            // If it errored, then its GS1 encoded, and if we only want GS1 then we skip over...
                            continue;
                        }
                    }
    
                    try {
                        byte[] tidBank1 = this.mNurApi.readTagByEpc(t.getEpc(), t.getEpc().length, BANK_TID, 0, 4);
                        String tid = NurApi.byteArrayToHexString(tidBank1);       
                        mappedTag.setTid(tid);
                    } catch(NurApiException e) {
                        Log.e("Helper", "Error reading TID bank: " + e.getMessage());
                    }
    
                    if(this.mShouldReadUsr) {
                        try {
                            byte[] usrBank1 = this.mNurApi.readTagByEpc(t.getEpc(), t.getEpc().length, BANK_USER, 0, 4);
                            String usr = NurApi.byteArrayToHexString(usrBank1);
                            mappedTag.setUsr(usr);
                        } catch(NurApiException e) {
                            Log.e("Helper", "Error reading USR bank: " + e.getMessage());
                        }
                    }
    
                    this.mSeenRFIDTagStorage.add(mappedTag);
                }
            }

            tagStorage.clear();
            
            if (!this.mSeenRFIDTagStorage.equals(oldRFIDTagStorage)) {
                this.sendOnTagsDiscovered();
            }
        }
    }

    private void stopScheduledTask() {
        if (this.scheduledFuture != null && !this.scheduledFuture.isCancelled()) {
            this.scheduledFuture.cancel(true);  // Cancel the task immediately
            Log.d("ScheduledTask", "Task has been cancelled.");
        }
    }
}
