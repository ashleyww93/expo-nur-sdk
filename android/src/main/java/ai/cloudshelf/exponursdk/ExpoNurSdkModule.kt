package ai.cloudshelf.exponursdk


import androidx.core.os.bundleOf

import ai.cloudshelf.exponursdk.RFIDTag;

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoNurSdkModule : Module() {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoNurSdk')` in JavaScript.
    Name("ExpoNurSdk")

    // Defines event names that the module can send to JavaScript.
    Events("onDeviceScanFinished", "onDeviceConnectionChanged", "onTagsFoundChanged")

    // Function("fireEventDevice") {
    //   this@ExpoNurSdkModule.sendEvent("onDeviceConnectionChanged", bundleOf("isConnected" to true))
    // }

    //Real functions
    Function("initialize") { deviceSpec: String? ->
      val activity = appContext.currentActivity;

      if (activity == null) {
        return@Function false
      } else {
        return@Function Helper.getInstance().initialize(deviceSpec, activity, object : EventCallbacks {
          override fun onDeviceScanComplete(deviceList: List<NurDeviceSpec>) {
            val processedDeviceList = deviceList.map { device ->
              device.getSpec()
            }
          
            this@ExpoNurSdkModule.sendEvent("onDeviceScanFinished", bundleOf("foundDevices" to processedDeviceList))
          }

          override fun onConnectionStatusChanged(isConnected: Boolean) {
            this@ExpoNurSdkModule.sendEvent("onDeviceConnectionChanged", bundleOf("isConnected" to isConnected))
          }

          override fun onTagsDiscovered(tagsList: kotlin.collections.List<RFIDTag>) {
            val processedTagsList = tagsList.map { tag ->
              kotlin.collections.mapOf(
                "isGS1Encoded" to tag.getIsGS1Encoded(),
                "gS1String" to tag.getGS1String(),
                "epc" to tag.getEpc(),
                "rssi" to tag.getRssi().toString(),
                "tid" to tag.getTid(),
                "usr" to tag.getUsr(),
                "usrSupported" to tag.getUsrSupported()
              )
            }

            this@ExpoNurSdkModule.sendEvent("onTagsFoundChanged", bundleOf("tags" to processedTagsList))
          }
        })
      } 
    }

 
    Function("provideSettings") { allowedCompanyPrefixes: ArrayList<String>, readUsr: Boolean ->
      return@Function Helper.getInstance().provideSettings(ArrayList(allowedCompanyPrefixes.toMutableList()), readUsr);
    }

    Function("terminate") {
      return@Function Helper.getInstance().terminate();
    }

    Function("scanDevices") {
      return@Function Helper.getInstance().scanDevices();
    }

    Function("connect") { deviceSpec: String ->
      return@Function Helper.getInstance().connect(deviceSpec);
    }

    Function("disconnect") {
      return@Function Helper.getInstance().disconnect();
    }

    Function("traceContinuousStart") {
      return@Function Helper.getInstance().traceContinuousStart();
    }

    Function("traceContinuousStop") {
      return@Function Helper.getInstance().traceContinuousStop();
    }

    Function("traceOnce") { clearPreviousTags: Boolean ->
      return@Function Helper.getInstance().traceOnce(clearPreviousTags);
    }

    Function("resetSeenTags") {
      return@Function Helper.getInstance().clearSeenTags();
    }
    
    Function("connectionStatus") {
      return@Function Helper.getInstance().connectionStatus();
    }
  }
}