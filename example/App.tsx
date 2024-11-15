import { Button, FlatList, StyleSheet, Text, TouchableOpacity, View } from 'react-native';

import * as ExpoNurSdk from 'expo-nur-sdk';
import { useEffect, useState } from 'react';
import React from 'react';

export default function App() {
  const [expoNurSdkInitalized,setExpoNurSdkInitalized] = useState<boolean>(false);
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const [lastConnectedEvent, setLastConnectedEvent]= useState<Date | undefined>(undefined);
  const [availableDevices, setAvailableDevices] = useState<string[]>([]);
  const [lastAvailableDevicesEvent, setLastAvailableDevicesEvent] = useState<Date | undefined>(undefined);
  const [isScanningDevices, setIsScanningDevices] = useState<boolean>(false);
  const [deviceToUse, setDeviceToUse] = useState<string | undefined>(undefined);
  const [tagsFound, setTagsFound] = useState<ExpoNurSdk.RFIDTag[]>([]);
  const [lastTagsEvent, setLastTagsEvent] = useState<Date | undefined>(undefined);

  useEffect(() => {
    const sub = ExpoNurSdk.addDeviceConnectionEventListener((event) => {
      console.log('device connection event', event);
      if (event.isConnected !== isConnected) {
        setIsConnected(event.isConnected);
        setLastConnectedEvent(new Date());
      }
    });

    const sub2 = ExpoNurSdk.addDeviceScanStatusUpdateListener((event) => {
      console.log('device scan event', event);
      if (JSON.stringify(event.foundDevices) !== JSON.stringify(availableDevices)) {
        setAvailableDevices(event.foundDevices);
        setLastAvailableDevicesEvent(new Date());
      }

      if(event.isScanning !== isScanningDevices) {
        setIsScanningDevices(event.isScanning)
      }
    })

    const sub3 = ExpoNurSdk.addTagsFoundEventListener((event) => {
      console.log('tag event', event);
      setTagsFound(event.tags);
      setLastTagsEvent(new Date());
    })

    if(!expoNurSdkInitalized) {
      const init = ExpoNurSdk.initialize(null);
      if(init) {
        //0867360217
        ExpoNurSdk.provideSettings(['0867360217'], true);
      }
      setExpoNurSdkInitalized(init);
    }

    return () => {
      sub.remove();
      sub2.remove();
      sub3.remove();
    }
  }, [expoNurSdkInitalized, isConnected, availableDevices]);

  const handleDeviceChange = (device: string) => {
    if(ExpoNurSdk.connectionStatus() === 'connected') {
      ExpoNurSdk.disconnect();
    }
    setDeviceToUse(device);
    ExpoNurSdk.connect(device);
  }  


  return (
    <View style={styles.container}>
      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 10, justifyContent: 'space-between', width: '100%' }}>
        <Text>Expo SDK Initialized: {expoNurSdkInitalized ? "Yes" : "No"}</Text>
        <Button
          title="Terminate SDK"
          onPress={() => {
            ExpoNurSdk.terminate();
            setExpoNurSdkInitalized(false);
          }}
        />
      </View>
      {expoNurSdkInitalized && (
        <>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 10, gap: 10,  justifyContent: 'space-between', width: '100%' }}>
            <Text>
              Device Connection Status: {isConnected ? "Connected" : "Disconnected"}
              {"\n"}
              Last Connection Event: {lastConnectedEvent ? lastConnectedEvent.toLocaleString() : 'No event recorded'}
              </Text>
            <Button
              title="Scan for Devices"
              onPress={() => {
                ExpoNurSdk.scanDevices();
              }}
            />
          </View>
          <View style={{ maxHeight: 200, borderWidth: 1, borderColor: 'gray', marginBottom: 10, width: '100%' }}>
            <Text style={{ padding: 10, fontWeight: 'bold', textAlign:'center' }}>
              Available Devices {isScanningDevices && "(Scanning for devices)"}
              {"\n"}
              Last Device Event: {lastAvailableDevicesEvent ? lastAvailableDevicesEvent.toLocaleString() : 'No event recorded'}
              </Text>
            <FlatList
              data={availableDevices}
              keyExtractor={(item) => item}
              ListEmptyComponent={() => (
                <Text style={{ padding: 10, textAlign: 'center' }}>
                  No devices 
                </Text>
              )}
              renderItem={({ item }) => (
                <TouchableOpacity onPress={() => handleDeviceChange(item)} style={{ borderWidth: 1, borderColor: 'gray', borderRadius: 5, margin: 5 }}>
                  <Text style={{ 
                    padding: 10, 
                    backgroundColor: item === deviceToUse ? 'lightblue' : 'white',
                    textAlign: 'center'
                  }}>
                    {item}
                  </Text>
                </TouchableOpacity>
              )}
              style={{ flexGrow: 0 }}
            />
          </View>
          <View style={{ flexDirection: 'column', alignItems: 'center', marginBottom: 10, gap: 10, width: '100%'  }}>
            <Text style={{ fontWeight: 'bold', textAlign:'center' }}>Controls</Text>
            <View style={{ flexDirection: 'row', alignItems: 'center',  gap: 10 }}>
              <Button
                title="Read Single Tag"
                onPress={() => {
                  ExpoNurSdk.traceOnce(false);
                }}
              />
              <Button
                title="Read Single Tag (clear previous)"
                onPress={() => {
                  ExpoNurSdk.traceOnce(true);
                }}
              />
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 10 }}>
              <Button
                title="Start Continuous Tag Reading"
                onPress={() => {
                  ExpoNurSdk.traceContinuousStart();
                }}
              />
              <Button
                title="Stop Continuous Tag Reading"
                onPress={() => {
                  ExpoNurSdk.traceContinuousStop();
                }}
              />
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 10 }}>
              <Button
                title="Clear Previously Seen Tags"
                onPress={() => {
                  ExpoNurSdk.resetSeenTags();
                }}
              />
            </View>
            <View style={{ borderWidth: 1, borderColor: 'gray',  marginBottom: 10, width: '100%', maxHeight: 300 }}>
             <Text style={{ padding: 10, fontWeight: 'bold', textAlign:'center' }}>
              Found Tags ({tagsFound.length})
              {"\n"}
              Last Tags Event: {lastTagsEvent ? lastTagsEvent.toLocaleString() : 'No event recorded'}
              </Text>
             <FlatList
              data={tagsFound}
              keyExtractor={(item) => item.epc}
              ListEmptyComponent={() => (
                <Text style={{ padding: 10, textAlign:'center' }}>No tags discovered</Text>
              )}
              renderItem={({ item }) => (
                <TouchableOpacity onPress={() => {}} style={{ borderWidth: 1, borderColor: 'gray', borderRadius: 5, margin: 5 }}>
                  <Text style={{ 
                    padding: 10, 
                    }}>
                      GS1: {item.gs1Data ? `${item.gs1Data.fullGS1String} (${item.gs1Data.companyPrefix}) - (${item.gs1Data.itemReference}) - (${item.gs1Data.serialNumber})` : 'Not GS1 Encoded'}{"\n"}
                    EPC: {item.epc}{"\n"}
                    RSSI: {item.rssi}{"\n"}
                    TID: {item.tid}{"\n"}
                    USR: {item.usr && item.usr ? item.usr : 'Not Supported'}
                  </Text>
                </TouchableOpacity>
              )}
             
            />
            </View>
          </View>
        </>
      )}
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    margin: 10,
    maxHeight: '100%',
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'flex-start',
  },
});
