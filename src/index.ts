import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';
import ExpoNurSdkModule from './ExpoNurSdkModule';

const emitter = new EventEmitter(ExpoNurSdkModule ?? NativeModulesProxy.ExpoNurSdk);

export interface DeviceConnectionPayload {
  isConnected: boolean;
}

// export interface RFIDTag {
//   epc: string,
//   rssi: string,
// }

// export interface FoundTagsPayload {
//   tags: RFIDTag[],
// }

/*Events*/
export function addDeviceConnectionEventListener(listener: (event: DeviceConnectionPayload) => void): Subscription {
  return emitter.addListener<DeviceConnectionPayload>('onDeviceConnectionChanged', listener);
}

// export function addTagsFoundEventListener(listener: (event: FoundTagsPayload) => void): Subscription {
//   return emitter.addListener<FoundTagsPayload>('onTagsFoundChanged', listener);
// }


export function hello(): string {
  return ExpoNurSdkModule.hello();
}


export function fireEventDevice(): string {
  return ExpoNurSdkModule.fireEventDevice();
}


export function add(): number {
  return ExpoNurSdkModule.add();
}


export function helperTest(): number {
  return ExpoNurSdkModule.helperTest();
}


