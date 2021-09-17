package com.solion.tapdaqflutter

import android.app.Activity;
import android.content.Context
import android.text.TextUtils;

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;

import java.util.HashMap;
import java.util.Map;

class TapdaqInterstitialListener(private val channel: MethodChannel) : TMAdListener() {

  override fun didLoad() {
    channel.invokeMethod("didLoad", null)
  }

  override fun didFailToLoad(error: TMAdError) {
    val arguments: MutableMap<String, Any> = HashMap()
    arguments.put("errorCode", error.getErrorCode())
    arguments.put("errorMessage", error.getErrorMessage())

    channel.invokeMethod("didFailToLoad", arguments)
  }

  override fun willDisplay() {
    channel.invokeMethod("willDisplay", null)
  }

  override fun didDisplay() {
    channel.invokeMethod("didDisplay", null)
  }

  override fun didFailToDisplay(error: TMAdError) {
    val arguments: MutableMap<String, Any> = HashMap()
    arguments.put("errorCode", error.getErrorCode())
    arguments.put("errorMessage", error.getErrorMessage())
    channel.invokeMethod("didFailToDisplay", arguments)
  }

  override fun didClose() {
    channel.invokeMethod("didClose", null)
  }

  override fun didClick() {
    channel.invokeMethod("didClick", null)
  }

}
