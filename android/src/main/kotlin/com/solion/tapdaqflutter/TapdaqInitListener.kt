package com.solion.tapdaqflutter

import android.app.Activity;
import android.content.Context
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;

class TapdaqInitListener(private val channel: MethodChannel) : TMInitListener() {

  override fun didInitialise() {
    super.didInitialise()
    channel.invokeMethod("didInit", null)
  }

  override fun didFailToInitialise(error: TMAdError) {
    super.didFailToInitialise(error)
    channel.invokeMethod("FailToInit", null)
  }
}
