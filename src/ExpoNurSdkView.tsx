import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoNurSdkViewProps } from './ExpoNurSdk.types';

const NativeView: React.ComponentType<ExpoNurSdkViewProps> =
  requireNativeViewManager('ExpoNurSdk');

export default function ExpoNurSdkView(props: ExpoNurSdkViewProps) {
  return <NativeView {...props} />;
}
