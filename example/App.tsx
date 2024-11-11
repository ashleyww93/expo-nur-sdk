import { StyleSheet, Text, View } from 'react-native';

import * as ExpoNurSdk from 'expo-nur-sdk';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoNurSdk.hello()}</Text>
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
