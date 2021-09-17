package com.solion.tapdaqflutter

import android.content.Context
import android.src.main.kotlin.com.solion.tapdaqflutter.TapdaqNative
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.flutter.plugin.common.PluginRegistry.Registrar

class TapdaqNativeFactory(private val registrar: Registrar): PlatformViewFactory(StandardMessageCodec.INSTANCE) {
  override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
    return TapdaqNative(context, registrar, viewId, args as HashMap<*, *>?)
  }
}