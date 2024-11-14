package ai.cloudshelf.exponursdk;

import java.util.List;

import ai.cloudshelf.exponursdk.RFIDTag;
import ai.cloudshelf.exponursdk.NurDeviceScanner;

public interface EventCallbacks {
    void onDeviceScanStatusUpdate(List<NurDeviceSpec> deviceList, boolean isScanning);
    void onConnectionStatusChanged(boolean isConnected);
    void onTagsDiscovered(List<RFIDTag> tagsList);
}