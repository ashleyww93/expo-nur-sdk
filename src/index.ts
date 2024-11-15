import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';
import ExpoNurSdkModule from './ExpoNurSdkModule';

const emitter = new EventEmitter(ExpoNurSdkModule ?? NativeModulesProxy.ExpoNurSdk);

export interface DeviceConnectionPayload {
  isConnected: boolean;
}

export interface DeviceScanStatusUpdatePayload {
  foundDevices: string[]
  isScanning: boolean,
}
export interface RFIDTagGS1Data {
  fullGS1String: string;
  companyPrefix: string;
  itemReference: string;
  serialNumber: string;
}

export interface RFIDTag {
  gs1Data: RFIDTagGS1Data | null;
  epc: string;
  rssi: string;
  tid: string | null;
  usr: string | null;
} 

export interface FoundTagsPayload {
  tags: RFIDTag[],
}

/*Events*/
export function addDeviceConnectionEventListener(listener: (event: DeviceConnectionPayload) => void): Subscription {
  return emitter.addListener<DeviceConnectionPayload>('onDeviceConnectionChanged', listener);
}

export function addDeviceScanStatusUpdateListener(listener: (event: DeviceScanStatusUpdatePayload) => void): Subscription {
  return emitter.addListener<DeviceScanStatusUpdatePayload>('onDeviceScanStatusUpdate', listener);
}

export function addTagsFoundEventListener(listener: (event: FoundTagsPayload) => void): Subscription {
  return emitter.addListener<FoundTagsPayload>('onTagsFoundChanged', listener);
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



