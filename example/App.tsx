import { Button, StyleSheet, Text, View } from 'react-native';

import * as ExpoNurSdk from 'expo-nur-sdk';
import { useEffect, useState } from 'react';

export default function App() {

  const [isConnected, setIsConnected] = useState<boolean>(false);
  const [helperTestValue, setHelperTestValue] = useState<number>(1337);

useEffect(() => {
  const sub = ExpoNurSdk.addDeviceConnectionEventListener((event) => {
    console.log('event fired', event);
    setIsConnected(event.isConnected);
  });

  return () => {
    sub.remove();
  }
})


  return (
    <View style={styles.container}>
      <Text>{ExpoNurSdk.hello()}</Text>
      <Text>{isConnected ? "CONNECTED" : "NOT CONNECTED"}</Text>
      <Button
        title="Fire Event"
        onPress={() => {
          ExpoNurSdk.fireEventDevice();
        }}
      />
      <Text>Helper Test: {helperTestValue}</Text>
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
      />
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
