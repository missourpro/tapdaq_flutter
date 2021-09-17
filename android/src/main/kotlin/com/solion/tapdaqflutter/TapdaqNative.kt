package android.src.main.kotlin.com.solion.tapdaqflutter

import TapdaqNativeListener
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
import io.flutter.plugin.common.PluginRegistry.Registrar

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;
import com.tapdaq.sdk.model.TMAdSize;

class TapdaqNative(context: Context, registrar: Registrar, id: Int, args: HashMap<*, *>?) :
        PlatformView, MethodCallHandler{

  private val channel: MethodChannel = MethodChannel(registrar.messenger(), "tapdaq_flutter/native_$id")
  private val adView: TMNativeAdView = TMNativeAdView(registrar.activity())
  private val activity: Activity = registrar.activity()

  init {
    channel.setMethodCallHandler(this)

    val adTag = args?.get("adTag") as String

    adView.load(activity, adTag, TapdaqNativeListener(channel, adView))
  }


  override fun getView(): View {
    return adView
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method) {
      "dispose" -> dispose()
      else -> result.notImplemented()
    }
  }

  override fun dispose() {
    adView.visibility = View.GONE
    //adView.destroy()
    channel.setMethodCallHandler(null)
  }
}