package com.solion.tapdaqflutter

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.flutter.plugin.common.PluginRegistry.Registrar

class TapdaqBannerFactory(private val registrar: Registrar): PlatformViewFactory(StandardMessageCodec.INSTANCE) {
  override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
    return TapdaqBanner(context, registrar, viewId, args as HashMap<*, *>?)
  }
}