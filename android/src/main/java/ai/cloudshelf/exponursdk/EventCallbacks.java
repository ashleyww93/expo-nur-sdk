package ai.cloudshelf.exponursdk;

import java.util.List;

import ai.cloudshelf.exponursdk.RFIDTag;
import ai.cloudshelf.exponursdk.NurDeviceScanner;

public interface EventCallbacks {
    void onDeviceScanComplete(List<NurDeviceSpec> deviceList);
    void onConnectionStatusChanged(boolean isConnected);
    void onTagsDiscovered(List<RFIDTag> tagsList);
}