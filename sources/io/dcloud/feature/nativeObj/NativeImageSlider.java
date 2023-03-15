package io.dcloud.feature.nativeObj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.nativeObj.BannerLayout;
import io.dcloud.feature.nativeObj.NativeView;
import io.dcloud.feature.nativeObj.data.NativeImageDataItem;
import io.dcloud.feature.nativeObj.photoview.PhotoActivity;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class NativeImageSlider extends NativeView {
    static final String TF = "{clientX:%d,clientY:%d,pageX:%d,pageY:%d,screenX:%d,screenY:%d,currentImageIndex:%d}";
    DisplayImageOptions defaultOptions;
    boolean isAutoplay = false;
    boolean isFullscreen = true;
    boolean isLoop = false;
    View mBackgroundView;
    BannerLayout mBannerLayout;
    int mFistBitmapHeight = 0;
    int mFistBitmapWidth = 0;
    String mIndicator = "default";
    int mInterval = PathInterpolatorCompat.MAX_NUM_POINTS;
    int mSliderHeight = 0;
    int measureTop = 0;

    public NativeImageSlider(Context context, IWebview iWebview, String str, String str2, JSONObject jSONObject) {
        super(context, iWebview, str, str2, jSONObject);
        addBannerView(iWebview);
        this.mIntercept = false;
    }

    private ArrayList<NativeImageDataItem> toArrayList(IWebview iWebview, JSONArray jSONArray) {
        ArrayList<NativeImageDataItem> arrayList = new ArrayList<>();
        if (jSONArray != null && jSONArray.length() > 0) {
            for (int i = 0; i < jSONArray.length(); i++) {
                NativeImageDataItem nativeImageDataItem = new NativeImageDataItem();
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                String convertAppPath = PdrUtil.convertAppPath(iWebview, optJSONObject.optString("src"));
                ImageLoaderUtil.addNetIconDownloadUrl(convertAppPath);
                nativeImageDataItem.setUrl(convertAppPath);
                if (optJSONObject.has(AbsoluteConst.JSON_KEY_ALIGN)) {
                    nativeImageDataItem.align = optJSONObject.optString(AbsoluteConst.JSON_KEY_ALIGN);
                }
                if (optJSONObject.has(AbsoluteConst.JSON_KEY_VERTICAL_ALIGN)) {
                    nativeImageDataItem.verticalAlign = optJSONObject.optString(AbsoluteConst.JSON_KEY_VERTICAL_ALIGN);
                }
                if (optJSONObject.has("height")) {
                    nativeImageDataItem.height = optJSONObject.optString("height");
                }
                if (optJSONObject.has("width")) {
                    nativeImageDataItem.width = optJSONObject.optString("width");
                }
                arrayList.add(nativeImageDataItem);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void updateSliderHeight(int i, int i2) {
        int i3 = i2 * (this.mInnerWidth / i);
        if (this.mBannerLayout != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.mInnerWidth, i3);
            layoutParams.topMargin = this.mInnerTop + this.measureTop;
            layoutParams.bottomMargin = this.mInnerBottom;
            this.mBackgroundView.setLayoutParams(layoutParams);
            this.mBannerLayout.setLayoutParams(layoutParams);
            this.mSliderHeight = i3 + this.mInnerTop;
        }
    }

    public void addBannerView(IWebview iWebview) {
        JSONObject jSONObject = this.mStyle;
        JSONArray jSONArray = null;
        if (jSONObject != null) {
            if (jSONObject.has("images")) {
                jSONArray = this.mStyle.optJSONArray("images");
            }
            if (this.mStyle.has("loop")) {
                this.isLoop = this.mStyle.optBoolean("loop");
            }
            if (this.mStyle.has(IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
                this.isFullscreen = this.mStyle.optBoolean(IApp.ConfigProperty.CONFIG_FULLSCREEN);
            }
            if (this.mStyle.has(Constants.Name.AUTOPLAY)) {
                this.isAutoplay = this.mStyle.optBoolean(Constants.Name.AUTOPLAY);
                if (this.mStyle.has("interval")) {
                    this.mInterval = this.mStyle.optInt("interval", this.mInterval);
                }
            }
            if (this.mStyle.has(WXBasicComponentType.INDICATOR)) {
                this.mIndicator = this.mStyle.optString(WXBasicComponentType.INDICATOR);
            }
        }
        this.mWebView = iWebview;
        int i = 0;
        BannerLayout bannerLayout = new BannerLayout(getContext(), this.isLoop, false);
        this.mBannerLayout = bannerLayout;
        boolean z = this.isAutoplay;
        if (z) {
            bannerLayout.setAutoPlay(z, this.mInterval);
        }
        this.defaultOptions = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading((Drawable) new ColorDrawable(0)).build();
        this.mBannerLayout.setImageLoader(new BannerLayout.ImageLoader() {
            public void displayImage(Context context, String str, final View view, final int i) {
                ImageLoaderL.getInstance().loadImage(str, NativeImageSlider.this.defaultOptions, (ImageLoadingListener) new ImageLoadingListener() {
                    long startTime;

                    public void onLoadingCancelled(String str, View view) {
                    }

                    public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                        NativeImageSlider nativeImageSlider = NativeImageSlider.this;
                        if (nativeImageSlider.isLayoutAdapt && i == 0) {
                            nativeImageSlider.mFistBitmapWidth = bitmap.getWidth();
                            NativeImageSlider.this.mFistBitmapHeight = bitmap.getHeight();
                            NativeImageSlider nativeImageSlider2 = NativeImageSlider.this;
                            nativeImageSlider2.updateSliderHeight(nativeImageSlider2.mFistBitmapWidth, nativeImageSlider2.mFistBitmapHeight);
                            NativeImageSlider.this.mCanvasView.requestLayout();
                        }
                        try {
                            if (!URLUtil.isNetworkUrl(str) || System.currentTimeMillis() - this.startTime <= 500) {
                                ((ImageView) view).setImageBitmap(bitmap);
                                return;
                            }
                            ((ImageView) view).setImageBitmap(bitmap);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                            alphaAnimation.setDuration(500);
                            view.startAnimation(alphaAnimation);
                        } catch (Exception unused) {
                        }
                    }

                    public void onLoadingFailed(String str, View view, FailReason failReason) {
                    }

                    public void onLoadingStarted(String str, View view) {
                        this.startTime = System.currentTimeMillis();
                    }
                });
            }
        });
        this.mBannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            public void onItemClick(int i) {
                NativeImageSlider nativeImageSlider = NativeImageSlider.this;
                if (nativeImageSlider.isFullscreen) {
                    Intent intent = new Intent(NativeImageSlider.this.getContext(), PhotoActivity.class);
                    intent.putParcelableArrayListExtra(PhotoActivity.IMAGE_URLS_KEY, NativeImageSlider.this.mBannerLayout.getUrls());
                    intent.putExtra(PhotoActivity.IMAGE_CURRENT_INDEX_KEY, i);
                    intent.putExtra(PhotoActivity.IMAGE_LOOP_KEY, true);
                    intent.putExtra(PhotoActivity.IMAGE_PHOTO_KEY, true);
                    intent.putExtra(PhotoActivity.IMAGE_PHOTO_TOP, NativeImageSlider.this.getTop());
                    NativeImageSlider.this.getContext().startActivity(intent);
                    Context context = NativeImageSlider.this.getContext();
                    if (context instanceof Activity) {
                        ((Activity) context).overridePendingTransition(17432576, 17432577);
                    }
                } else if (nativeImageSlider.mCanvasView.listenClick()) {
                    NativeImageSlider.this.mCanvasView.doTypeEvent(Constants.Event.CLICK);
                }
            }

            public void onItemLongClick(int i) {
            }
        });
        this.mBannerLayout.setIndicatorContainerData(BannerLayout.Position.centerBottom, 20, 10, 18, this.mIndicator);
        ArrayList<NativeImageDataItem> arrayList = toArrayList(iWebview, jSONArray);
        if (arrayList.size() > 0) {
            this.mBannerLayout.setViewUrls(arrayList, 0);
        }
        int i2 = this.mInnerWidth;
        if (!this.isLayoutAdapt) {
            i = this.mInnerHeight;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i2, i);
        layoutParams.topMargin = this.mInnerTop + this.measureTop;
        layoutParams.bottomMargin = this.mInnerBottom;
        if (this.mBackgroundView == null) {
            this.mBackgroundView = new View(getContext());
        }
        addView(this.mBackgroundView, layoutParams);
        addView(this.mBannerLayout, layoutParams);
        super.attachCanvasView();
    }

    public void addImages(IWebview iWebview, JSONArray jSONArray) {
        if (this.mBannerLayout != null) {
            this.mBannerLayout.addViewUrls(toArrayList(iWebview, jSONArray), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void attachCanvasView() {
    }

    public void clearNativeViewData() {
        super.clearNativeViewData();
        this.mFistBitmapHeight = 0;
        this.mFistBitmapWidth = 0;
        this.mSliderHeight = 0;
        BannerLayout bannerLayout = this.mBannerLayout;
        if (bannerLayout != null) {
            bannerLayout.clearBannerData();
        }
    }

    /* access modifiers changed from: protected */
    public void configurationCange() {
        super.configurationCange();
        if (this.mBannerLayout == null) {
            return;
        }
        if (this.isLayoutAdapt) {
            int i = this.mFistBitmapHeight;
            if (i != 0) {
                updateSliderHeight(this.mFistBitmapWidth, i);
                return;
            }
            return;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.mInnerWidth, this.mInnerHeight);
        layoutParams.topMargin = this.mInnerTop + this.measureTop;
        layoutParams.bottomMargin = this.mInnerBottom;
        this.mBackgroundView.setLayoutParams(layoutParams);
        this.mBannerLayout.setLayoutParams(layoutParams);
        this.mBannerLayout.requestLayout();
    }

    public Object doForFeature(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.equals("setAllowImageDownload")) {
            return super.doForFeature(str, obj);
        }
        Object[] objArr = (Object[]) obj;
        boolean booleanValue = ((Boolean) objArr[0]).booleanValue();
        boolean booleanValue2 = ((Boolean) objArr[1]).booleanValue();
        BannerLayout bannerLayout = this.mBannerLayout;
        if (bannerLayout == null) {
            return null;
        }
        bannerLayout.setAllowImageDownload(booleanValue, booleanValue2);
        return null;
    }

    public int getCurrentImageIndex() {
        BannerLayout bannerLayout = this.mBannerLayout;
        if (bannerLayout != null) {
            return bannerLayout.getCurrentPosition();
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public String getEventJSON() {
        return StringUtil.format(TF, Integer.valueOf((int) ((this.mTouchX - ((float) this.mInnerLeft)) / this.mCreateScale)), Integer.valueOf((int) ((this.mTouchY - ((float) this.mInnerTop)) / this.mCreateScale)), Integer.valueOf((int) (this.mTouchX / this.mCreateScale)), Integer.valueOf((int) (this.mTouchY / this.mCreateScale)), Integer.valueOf((int) (this.mTouchX / this.mCreateScale)), Integer.valueOf((int) (this.mTouchY / this.mCreateScale)), Integer.valueOf(getCurrentImageIndex()));
    }

    /* access modifiers changed from: protected */
    public int getNViewContentHeight() {
        ArrayList<NativeView.Overlay> arrayList = this.mOverlays;
        if (arrayList == null) {
            return this.mAppScreenHeight;
        }
        int i = 0;
        this.mInnerHeight = this.mAppScreenHeight;
        Iterator<NativeView.Overlay> it = arrayList.iterator();
        while (it.hasNext()) {
            NativeView.Overlay next = it.next();
            int i2 = makeRect(this, next.mDestJson, next).bottom;
            if (i2 > i) {
                i = i2;
            }
        }
        int i3 = this.mSliderHeight;
        return i3 > i ? i3 : i;
    }

    public String getViewType() {
        return AbsoluteConst.NATIVE_IMAGESLIDER;
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        if (this.mBackgroundView == null) {
            this.mBackgroundView = new View(getContext());
        }
        this.mBackgroundView.setBackgroundColor(this.mBackGroundColor);
    }

    /* access modifiers changed from: package-private */
    public void interceptTouchEvent(boolean z) {
        this.mIntercept = false;
    }

    /* access modifiers changed from: protected */
    public void measureChildViewToTop(int i) {
        super.measureChildViewToTop(i);
        this.measureTop = i;
        if (this.mBannerLayout != null) {
            configurationCange();
        }
    }

    public void setImages(IWebview iWebview, JSONArray jSONArray) {
        if (this.mBannerLayout != null) {
            this.mBannerLayout.setViewUrls(toArrayList(iWebview, jSONArray), 0);
        }
    }

    public void setStyle(JSONObject jSONObject, boolean z) {
        super.setStyle(jSONObject, z);
        if (this.mBannerLayout != null) {
            JSONArray jSONArray = null;
            JSONObject jSONObject2 = this.mStyle;
            if (jSONObject2 != null) {
                if (jSONObject2.has("images")) {
                    jSONArray = this.mStyle.optJSONArray("images");
                }
                if (this.mStyle.has("loop")) {
                    this.isLoop = this.mStyle.optBoolean("loop");
                }
                if (this.mStyle.has(IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
                    this.isFullscreen = this.mStyle.optBoolean(IApp.ConfigProperty.CONFIG_FULLSCREEN);
                }
                if (this.mStyle.has(WXBasicComponentType.INDICATOR)) {
                    this.mIndicator = this.mStyle.optString(WXBasicComponentType.INDICATOR);
                }
            }
            ArrayList<NativeImageDataItem> arrayList = toArrayList(this.mWebView, jSONArray);
            this.mBannerLayout.setImageLoop(Boolean.valueOf(this.isLoop));
            this.mBannerLayout.setIndicatorType(this.mIndicator);
            this.mBannerLayout.setViewUrls(arrayList, 0);
        }
    }

    public void setStyleBackgroundColor(int i) {
        super.setStyleBackgroundColor(i);
        this.mBackgroundView.setBackgroundColor(this.mBackGroundColor);
    }
}
