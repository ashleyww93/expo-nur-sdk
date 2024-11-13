package ai.cloudshelf.exponursdk;

import com.nordicid.nurapi.NurApiListener;
import com.nordicid.nurapi.NurEventTriggeredRead;
import com.nordicid.nurapi.NurEventTraceTag;
import com.nordicid.nurapi.NurEventProgrammingProgress;
import com.nordicid.nurapi.NurEventNxpAlarm;
import com.nordicid.nurapi.NurEventInventory;
import com.nordicid.nurapi.NurEventFrequencyHop;
import com.nordicid.nurapi.NurEventEpcEnum;
import com.nordicid.nurapi.NurEventDeviceInfo;
import com.nordicid.nurapi.NurEventClientInfo;
import com.nordicid.nurapi.NurEventIOChange;
import com.nordicid.nurapi.NurEventAutotune;
import com.nordicid.nurapi.NurEventTagTrackingChange;
import com.nordicid.nurapi.NurEventTagTrackingData;



public class NurApiListenerExtended implements NurApiListener  {
    
    public NurApiListenerExtended() {
        super();
    }

    @Override
    public void triggeredReadEvent(NurEventTriggeredRead event) {
    }

    @Override
    public void traceTagEvent(NurEventTraceTag event) {
    }

    @Override
    public void programmingProgressEvent(NurEventProgrammingProgress event) {
    }

    @Override
    public void nxpEasAlarmEvent(NurEventNxpAlarm event) {
    }

    @Override
    public void logEvent(int level, String txt) {
    }

    @Override
    public void inventoryStreamEvent(NurEventInventory event) {
    }

    @Override
    public void inventoryExtendedStreamEvent(NurEventInventory event) {
    }

    @Override
    public void frequencyHopEvent(NurEventFrequencyHop event) {
    }

    @Override
    public void epcEnumEvent(NurEventEpcEnum event) {
    }

    @Override
    public void disconnectedEvent() {
        Helper.getInstance().sendOnConnectionStatusChanged(false);
    }

    @Override
    public void deviceSearchEvent(NurEventDeviceInfo event) {
    }

    @Override
    public void debugMessageEvent(String event) {
    }

    @Override
    public void connectedEvent() {
        Helper.getInstance().sendOnConnectionStatusChanged(true);
    }

    @Override
    public void clientDisconnectedEvent(NurEventClientInfo event) {
    }

    @Override
    public void clientConnectedEvent(NurEventClientInfo event) {
    }

    @Override
    public void bootEvent(String event) {
    }

    @Override
    public void IOChangeEvent(NurEventIOChange event) {
    }

    @Override
    public void autotuneEvent(NurEventAutotune event) {
    }

    @Override
    public void tagTrackingScanEvent(NurEventTagTrackingData event) {
    }

    @Override
    public void tagTrackingChangeEvent(NurEventTagTrackingChange event) {
    }
}
