package com.solion.tapdaqflutter

import android.content.Context
import android.view.View
import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;

class TapdaqBannerListener(private val channel: MethodChannel) : TMAdListener() {

  override fun didLoad() {
    channel.invokeMethod("didLoad", null)
  }

  override fun didFailToLoad(error: TMAdError) {
    val arguments: MutableMap<String, Any> = HashMap()
    arguments.put("errorCode", error.getErrorCode())
    arguments.put("errorMessage", error.getErrorMessage())

    channel.invokeMethod("didFailToLoad", arguments)
  }

  override fun didRefresh() {
    channel.invokeMethod("didRefresh", null)
  }

  override fun didFailToRefresh(error: TMAdError) {
    val arguments: MutableMap<String, Any> = HashMap()
    arguments.put("errorCode", error.getErrorCode())
    arguments.put("errorMessage", error.getErrorMessage())

    channel.invokeMethod("didFailToRefresh", arguments)
  }

  override fun didClick() {
    channel.invokeMethod("didClick", null)
  }
}