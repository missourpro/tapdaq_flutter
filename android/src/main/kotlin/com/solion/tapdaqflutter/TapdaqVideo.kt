package com.solion.tapdaqflutter

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.content.Context

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;

class TapdaqVideo(private val registrar: PluginRegistry.Registrar,
                         private val channel: MethodChannel): MethodChannel.MethodCallHandler {

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when(call.method) {
      "load" -> {
        val adTag = call.argument<String>("adTag")
        Tapdaq.getInstance().loadVideo(registrar.activity(), adTag,
                TapdaqInterstitialListener(channel))
        result.success(null)
      }
      "isLoaded" -> {
        val adTag = call.argument<String>("adTag")
        result.success(Tapdaq.getInstance().isVideoReady(
                registrar.activity(), adTag))
      }
      "show" -> {
        val adTag = call.argument<String>("adTag")
        Tapdaq.getInstance().showVideo(registrar.activity(), adTag,
                TapdaqInterstitialListener(channel));
        result.success(null)
      }
      else -> result.notImplemented()
    }
  }
}
