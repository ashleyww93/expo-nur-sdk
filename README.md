# expo-nur-sdk

An Expo plugin to bring Nordic SDK to the managed workflow

### Note
This module only supports android.

### Configure for Android
Ensure your application has the following permissions:
 - ACCESS_COARSE_LOCATION
 - ACCESS_FINE_LOCATION
 - BLUETOOTH_ADMIN
 - BLUETOOTH_ADVERTISE
 - BLUETOOTH_CONNECT
 - BLUETOOTH_SCAN
 - READ_EXTERNAL_STORAGE
 - WRITE_EXTERNAL_STORAGE


 ### Available Functions
  - addDeviceConnectionEventListener
  - addDeviceScanStatusUpdateListener
  - addTagsFoundEventListener
  - initialize
  - provideSettings
  - terminate
  - scanDevices
  - connect
  - traceContinuousStart
  - traceContinuousStop
  - traceOnce
  - resetSeenTags
  - connectionStatus