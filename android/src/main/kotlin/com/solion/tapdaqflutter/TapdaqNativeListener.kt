import android.src.main.kotlin.com.solion.tapdaqflutter.TMNativeAdView
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd
import com.tapdaq.sdk.common.TMAdError
import com.tapdaq.sdk.listeners.TMAdListener
import io.flutter.plugin.common.MethodChannel

class TapdaqNativeListener(private val channel: MethodChannel, private  val  adView: TMNativeAdView) :TMAdListener(){
    override fun didLoad(ad: TDMediatedNativeAd?) {
        adView.populate(ad)
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
