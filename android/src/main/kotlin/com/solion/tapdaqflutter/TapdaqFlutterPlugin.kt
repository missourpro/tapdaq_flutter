package com.solion.tapdaqflutter

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.content.Context

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.listeners.*;

class TapdaqFlutterPlugin(private val activity: Activity, private val channel: MethodChannel) : MethodCallHandler{
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val defaultChannel = MethodChannel(registrar.messenger(), "tapdaq_flutter")
      defaultChannel.setMethodCallHandler(TapdaqFlutterPlugin(registrar.activity(), defaultChannel))

        val interstitialChannel = MethodChannel(registrar.messenger(), "tapdaq_flutter/interstitial")
        interstitialChannel.setMethodCallHandler(TapdaqInterstitial(registrar, interstitialChannel))

        val videoChannel = MethodChannel(registrar.messenger(), "tapdaq_flutter/video")
        videoChannel.setMethodCallHandler(TapdaqInterstitial(registrar, videoChannel))

        val rewardChannel = MethodChannel(registrar.messenger(), "tapdaq_flutter/rewarded")
        rewardChannel.setMethodCallHandler(TapdaqRewarded(registrar, rewardChannel))

        registrar.platformViewRegistry().registerViewFactory("tapdaq_flutter/banner",
                TapdaqBannerFactory(registrar))
        registrar.platformViewRegistry().registerViewFactory("tapdaq_flutter/native",
            TapdaqNativeFactory(registrar))


    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
      when (call.method) {
          "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
          "initialize" -> {
              val GDPR = if (call.argument<Boolean>("GDPR") == true) STATUS.TRUE else STATUS.FALSE
              val ConsentStatus = if (call.argument<Boolean>("ConsentStatus") == true) STATUS.TRUE else STATUS.FALSE
              val Restricted = if (call.argument<Boolean>("Restricted") == true) STATUS.TRUE else STATUS.FALSE
              val APP_ID = call.argument<String>("AppId")
              val CLIENT_KEY = call.argument<String>("ClientKey")

              val config = TapdaqConfig()
              config.setUserSubjectToGDPR(GDPR)
              config.setConsentStatus(ConsentStatus)
              config.setAgeRestrictedUserStatus(Restricted)

              Tapdaq.getInstance().initialize(
                      activity,
                      APP_ID,
                      CLIENT_KEY,
                      config,
                      TapdaqInitListener(channel)
              )
          }
          "testMediation" -> Tapdaq.getInstance().startTestActivity(activity);
          else -> result.notImplemented()
      }
  }
}
