import { Button, FlatList, StyleSheet, Text, TouchableOpacity, View } from 'react-native';

import * as ExpoNurSdk from 'expo-nur-sdk';
import { useEffect, useState } from 'react';

export default function App() {
  const [expoNurSdkInitalized,setExpoNurSdkInitalized] = useState<boolean>(false);
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const [availableDevices, setAvailableDevices] = useState<string[]>([]);
  const [deviceToUse, setDeviceToUse] = useState<string | undefined>(undefined);
  const [tagsFound, setTagsFound] = useState<ExpoNurSdk.RFIDTag[]>([]);

  useEffect(() => {
    const sub = ExpoNurSdk.addDeviceConnectionEventListener((event) => {
      console.log('device connection event', event);
      if (event.isConnected !== isConnected) {
        setIsConnected(event.isConnected);
      }
    });

    const sub2 = ExpoNurSdk.addDeviceScanFinishedListener((event) => {
      console.log('device scan event', event);
      if (JSON.stringify(event.foundDevices) !== JSON.stringify(availableDevices)) {
        setAvailableDevices(event.foundDevices);
      }
    })

    const sub3 = ExpoNurSdk.addTagsFoundEventListener((event) => {
      console.log('tag event', event);
      setTagsFound(event.tags);
    })

    if(!expoNurSdkInitalized) {
      setExpoNurSdkInitalized(ExpoNurSdk.initialize(null));
    }

    return () => {
      sub.remove();
      sub2.remove();
      sub3.remove();
    }
  }, [expoNurSdkInitalized]);

  const handleDeviceChange = (device: string) => {
    if(ExpoNurSdk.connectionStatus() === 'connected') {
      ExpoNurSdk.disconnect();
    }
    setDeviceToUse(device);
    ExpoNurSdk.connect(device);
  }

  if(!expoNurSdkInitalized) {
    return <View style={styles.container}>
      <Text>Unable to initialize Expo Nur SDK</Text>
    </View>
  }

  return (
    <View style={styles.container}>
      <Button
        title="Terminate"
        onPress={() => {
          ExpoNurSdk.terminate();
        }}
      />
      <Text>{isConnected ? "CONNECTED" : "NOT CONNECTED"}</Text>
      <Button
        title="Scan Devices"
        onPress={() => {
          ExpoNurSdk.scanDevices();
        }}
      />
      

      <View style={{ maxHeight: 200 }}>
        <FlatList
          data={availableDevices}
          keyExtractor={(item) => item}
          ListEmptyComponent={() => (
            <Text style={{ padding: 10 }}>No devices </Text>
          )}
          renderItem={({ item }) => (
            <TouchableOpacity onPress={() => handleDeviceChange(item)}>
              <Text style={{ 
                padding: 10, 
                backgroundColor: item === deviceToUse ? 'lightblue' : 'white' 
              }}>
                {item}
              </Text>
            </TouchableOpacity>
          )}
        />
      </View>
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

<Button
        title="START Read Continuous"
        onPress={() => {
          ExpoNurSdk.traceContinuousStart();
        }}
      />
      <Button
        title="STOP Read Continuous"
        onPress={() => {
          ExpoNurSdk.traceContinuousStop();
        }}
      />
            <Button
        title="Read Continuous - CLEAR"
        onPress={() => {
          ExpoNurSdk.resetSeenTags();
        }}
      />
        <View style={{ maxHeight: 200 }}>
        <FlatList
          data={tagsFound}
          keyExtractor={(item) => item.epc}
          ListEmptyComponent={() => (
            <Text style={{ padding: 10 }}>No tags</Text>
          )}
          renderItem={({ item }) => (
            <TouchableOpacity onPress={() => {}}>
              <Text style={{ 
                padding: 10, 
                }}>
                EPC: {item.epc}{"\n"}
                RSSI: {item.rssi}{"\n"}
                TID: {item.tid}{"\n"}
                USR: {item.usrSupported && item.usr ? item.usr : 'Not Supported'}
              </Text>
            </TouchableOpacity>
          )}
        />
      </View>
      
      {/* <Text>Helper Test: {helperTestValue}</Text>
      <Button
        title="Size"
        onPress={() => {
          setHelperTestValue(ExpoNurSdk.helperTest());
        }}
      />
       <Button
        title="add"
        onPress={() => {
          setHelperTestValue(ExpoNurSdk.add());
        }}
      /> */}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
