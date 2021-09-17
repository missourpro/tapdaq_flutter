package android.src.main.kotlin.com.solion.tapdaqflutter

import TapdaqNativeListener
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.solion.tapdaqflutter.R
import com.tapdaq.sdk.Tapdaq
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAdImage
import com.tapdaq.sdk.network.HttpClientBase.ResponseImageHandler
import com.tapdaq.sdk.network.TClient


class TMNativeAdView : RelativeLayout {
  private var mInflater: LayoutInflater
  private var mAdview: ViewGroup? = null
  private var mTitleView: TextView? = null
  private var mSubtitleTextView: TextView? = null
  private var mBodyTextView: TextView? = null
  private var mCaptionTextView: TextView? = null
  private var mImageView: ImageView? = null
  private var mButton: Button? = null
  private var mAdChoicesView: ViewGroup? = null
  private var mIconView: ViewGroup? = null
  private var mPriceTextView: TextView? = null
  private var mStoreTextView: TextView? = null
  private var mStarRating: TextView? = null
  private var mMediaView: ViewGroup? = null

  constructor(context: Context?) : super(context) {
    mInflater = LayoutInflater.from(context)
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    mInflater = LayoutInflater.from(context)
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  ) {
    mInflater = LayoutInflater.from(context)
    init()
  }

  private fun init() {
    val v: View = mInflater.inflate(R.layout.tapdaq_native_layout, this, true)
    mTitleView = v.findViewById(R.id.title_textview)
    mSubtitleTextView = v.findViewById(R.id.subtitle_textview)
    mBodyTextView = v.findViewById(R.id.body_textview)
    mCaptionTextView = v.findViewById(R.id.caption_textview)
    mImageView = v.findViewById(R.id.image_view)
    mButton = v.findViewById(R.id.cta_button)
    mAdChoicesView = v.findViewById(R.id.adchoices_view)
    mIconView = v.findViewById(R.id.icon_image_view)
    mPriceTextView = v.findViewById(R.id.price_textview)
    mStoreTextView = v.findViewById(R.id.store_textview)
    mStarRating = v.findViewById(R.id.star_rating_textview)
    mMediaView = v.findViewById(R.id.media_view)
    mAdview = v.findViewById(R.id.ad_view)
  }

  fun clear() {
    mTitleView!!.text = ""
    mSubtitleTextView!!.text = ""
    mBodyTextView!!.text = ""
    mCaptionTextView!!.text = ""
    mButton!!.text = ""
    mPriceTextView!!.text = ""
    mStoreTextView!!.text = ""
    mStarRating!!.text = ""
    mAdview!!.removeAllViews()
    mMediaView!!.removeAllViews()
    mAdChoicesView!!.removeAllViews()
    mImageView!!.setImageBitmap(null)
    mIconView!!.removeAllViews()
  }

  fun populate(ad: TDMediatedNativeAd?) {
    clear()
    if (ad != null) {
      if (ad.adView != null) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mAdview!!.addView(ad.adView, params)
      }
      if (ad.mediaView != null) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.addRule(CENTER_IN_PARENT)
        mMediaView!!.addView(ad.mediaView, params)
      }
      mTitleView!!.text = ad.title
      if (ad.subtitle != null) mSubtitleTextView!!.text = ad.subtitle
      mBodyTextView!!.text = ad.body
      if (ad.caption != null) mCaptionTextView!!.text = ad.caption
      mButton!!.text = ad.callToAction
      if (ad.price != null) mPriceTextView!!.text = ad.price
      mStarRating!!.text = java.lang.Double.toString(ad.starRating)
      if (ad.store != null) mStoreTextView!!.text = ad.store
      if (ad.images != null) {
        val image = ad.images[0]
        if (image.drawable != null) {
          mImageView!!.setImageDrawable(ad.images[0].drawable)
        } else if (image.url != null) {
          TClient().executeImageGET(context, image.url, 0, 0, object : ResponseImageHandler {
            override fun onSuccess(response: Bitmap) {
              mImageView!!.setImageBitmap(response)
            }

            override fun onError(e: Exception) {}
          })
        }
        //                removeView(mImageView);
//                addView(mImageView, 1);
      }
      if (ad.appIconView != null) {
        mIconView!!.addView(ad.appIconView)
      }
      if (ad.adChoiceView != null) {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mAdChoicesView!!.addView(ad.adChoiceView, params)
      }
      ad.registerView(mButton)
      if (ad.videoController != null && ad.videoController.hasVideoContent()) {
        ad.videoController.play()
      }
      ad.trackImpression()
    }
  }

  fun load(activity: Activity, adTag: String, tapdaqNativeListener: TapdaqNativeListener) {
    Tapdaq.getInstance().loadMediatedNativeAd(activity, adTag,  tapdaqNativeListener);
  }
}