import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoNurSdk.web.ts
// and on native platforms to ExpoNurSdk.ts
import ExpoNurSdkModule from './ExpoNurSdkModule';
import ExpoNurSdkView from './ExpoNurSdkView';
import { ChangeEventPayload, ExpoNurSdkViewProps } from './ExpoNurSdk.types';

// Get the native constant value.
export const PI = ExpoNurSdkModule.PI;

export function hello(): string {
  return ExpoNurSdkModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoNurSdkModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoNurSdkModule ?? NativeModulesProxy.ExpoNurSdk);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoNurSdkView, ExpoNurSdkViewProps, ChangeEventPayload };
