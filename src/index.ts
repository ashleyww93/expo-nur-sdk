import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';
import ExpoNurSdkModule from './ExpoNurSdkModule';

const emitter = new EventEmitter(ExpoNurSdkModule ?? NativeModulesProxy.ExpoNurSdk);

export interface DeviceConnectionPayload {
  isConnected: boolean;
}

export interface DeviceScanFinishedPayload {
  foundDevices: string[]
}

export interface RFIDTag {
  isGS1Encoded: boolean;
  gS1String: string | null;
  epc: string;
  rssi: string,
  tid: string | null;
  usr: string | null,
  usrSupported: boolean,
} 

export interface FoundTagsPayload {
  tags: RFIDTag[],
}

/*Events*/
export function addDeviceConnectionEventListener(listener: (event: DeviceConnectionPayload) => void): Subscription {
  return emitter.addListener<DeviceConnectionPayload>('onDeviceConnectionChanged', listener);
}

export function addDeviceScanFinishedListener(listener: (event: DeviceScanFinishedPayload) => void): Subscription {
  return emitter.addListener<DeviceScanFinishedPayload>('onDeviceScanFinished', listener);
}

export function addTagsFoundEventListener(listener: (event: FoundTagsPayload) => void): Subscription {
  return emitter.addListener<FoundTagsPayload>('onTagsFoundChanged', listener);
}


export function hello(): string {
  return ExpoNurSdkModule.hello();
}



export function fireEventDevice(): string {
  return ExpoNurSdkModule.fireEventDevice();
}


export function initialize(deviceSpec: string | null): boolean {
  return ExpoNurSdkModule.initialize(deviceSpec);
}

export function provideSettings(allowedCompanyPrefixes: string[], readUsr: boolean): boolean {
  return ExpoNurSdkModule.provideSettings(allowedCompanyPrefixes, readUsr);
}

export function terminate(): boolean {
  return ExpoNurSdkModule.terminate();
}

export function scanDevices(): boolean {
  return ExpoNurSdkModule.scanDevices();
}

export function connect(deviceSpec: string): boolean {
  return ExpoNurSdkModule.connect(deviceSpec);
}

export function disconnect(): boolean {
  return ExpoNurSdkModule.disconnect();
}

export function traceContinuousStart(): boolean {
  return ExpoNurSdkModule.traceContinuousStart();
}

export function traceContinuousStop(): boolean {
  return ExpoNurSdkModule.traceContinuousStop();
}

export function traceOnce(clearPreviousTags: boolean): boolean {
  return ExpoNurSdkModule.traceOnce(clearPreviousTags);
}

export function resetSeenTags(): boolean {
  return ExpoNurSdkModule.resetSeenTags();
}

export function connectionStatus(): 'connected' | 'disconnected' {
  return ExpoNurSdkModule.connectionStatus();
}



