package ai.cloudshelf.exponursdk

import androidx.core.os.bundleOf
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
    Events("onDeviceConnectionChanged")

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    Function("hello") {
      "Hello world! 👋"
    }

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    Function("fireEventDevice") {
      this@ExpoNurSdkModule.sendEvent("onDeviceConnectionChanged", bundleOf("isConnected" to true))
    }

    Function("add") {
      return@Function Helper.getInstance().add();
    }

    Function("helperTest") {
      return@Function Helper.getInstance().size();
    }
    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    // Function("fireEventTags") {
    //   this@ExpoNurSdkModule.sendEvent("onTagsFoundChanged", bundleOf("isConnected" to true))
    // }
  }
}
