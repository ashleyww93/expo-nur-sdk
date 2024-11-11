import * as React from 'react';

import { ExpoNurSdkViewProps } from './ExpoNurSdk.types';

export default function ExpoNurSdkView(props: ExpoNurSdkViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
