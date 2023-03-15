package io.dcloud.feature.nativeObj.photoview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.EventDispatchManager;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.nativeObj.BannerLayout;
import io.dcloud.feature.nativeObj.data.NativeImageDataItem;
import io.dcloud.feature.nativeObj.photoview.subscaleview.ImageSource;
import io.dcloud.feature.nativeObj.photoview.subscaleview.SubsamplingScaleImageView;
import io.src.dcloud.adapter.DCloudBaseActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoActivity extends DCloudBaseActivity {
    public static String IMAGE_BG_KEY = "image_backgroud_color";
    public static String IMAGE_CURRENT_INDEX_KEY = "image_current_index";
    public static String IMAGE_INDICATOR_KEY = "image_indicator";
    public static String IMAGE_LOOP_KEY = "image_loop";
    public static String IMAGE_PHOTO_KEY = "image_photo";
    public static String IMAGE_PHOTO_TOP = "image_photo_top";
    public static String IMAGE_URLLIST_KEY = "image_urlList";
    public static String IMAGE_URLS_KEY = "image_urls";
    private String callbackId;
    public boolean isBack = false;
    /* access modifiers changed from: private */
    public String[] localImageUrls;
    int mCurrentItmeIndex = 0;
    private DisplayImageOptions mDefOps;
    ArrayList<NativeImageDataItem> mImageUrls;
    public String mIndicatorType = "default";
    /* access modifiers changed from: private */
    public ArrayList originalImageUrls;
    private BannerLayout photoLayout;

    private void fullScreen(Activity activity) {
        int i = Build.VERSION.SDK_INT;
        if (i < 19) {
            return;
        }
        if (i >= 21) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            return;
        }
        Window window2 = activity.getWindow();
        WindowManager.LayoutParams attributes = window2.getAttributes();
        attributes.flags |= 67108864;
        window2.setAttributes(attributes);
    }

    private DisplayImageOptions getIconDisplayOptions() {
        return new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.ARGB_8888).showImageOnLoading((Drawable) new ColorDrawable(0)).build();
    }

    public ArrayList<NativeImageDataItem> listToNativeDataItems(ArrayList<String> arrayList) {
        if (arrayList == null) {
            return null;
        }
        ArrayList<NativeImageDataItem> arrayList2 = new ArrayList<>();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            NativeImageDataItem nativeImageDataItem = new NativeImageDataItem();
            nativeImageDataItem.setUrl(it.next());
            arrayList2.add(nativeImageDataItem);
        }
        return arrayList2;
    }

    public void onBackPressed() {
        if (!EventDispatchManager.getInstance().dispatchEvent(ISysEventListener.SysEventType.onKeyUp, new Object[]{4, null})) {
            if (this.callbackId != null) {
                LongClickEventManager.getInstance().removeOnLongClickListener(this.callbackId);
            }
            RuningAcitvityUtil.removeRuningActivity(getComponentName().getClassName());
            finish();
            overridePendingTransition(17432576, 17432577);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        RuningAcitvityUtil.putRuningActivity(this);
        this.mDefOps = getIconDisplayOptions();
        Intent intent = getIntent();
        if (intent.hasExtra(IMAGE_URLLIST_KEY)) {
            this.mImageUrls = listToNativeDataItems(intent.getStringArrayListExtra(IMAGE_URLLIST_KEY));
        } else {
            this.mImageUrls = intent.getParcelableArrayListExtra(IMAGE_URLS_KEY);
        }
        if (this.mImageUrls == null) {
            onBackPressed();
            return;
        }
        int intExtra = intent.getIntExtra(IMAGE_CURRENT_INDEX_KEY, this.mCurrentItmeIndex);
        this.mCurrentItmeIndex = intExtra;
        if (intExtra > this.mImageUrls.size()) {
            this.mCurrentItmeIndex = 0;
        }
        if (intent.hasExtra("preview_callback")) {
            this.callbackId = intent.getStringExtra("preview_callback");
        }
        if (intent.hasExtra("original_image_urlArray")) {
            this.originalImageUrls = intent.getStringArrayListExtra("original_image_urlArray");
        }
        if (intent.hasExtra("screen_orientation")) {
            setRequestedOrientation(intent.getIntExtra("screen_orientation", 2));
        }
        boolean booleanExtra = intent.getBooleanExtra(IMAGE_LOOP_KEY, false);
        boolean booleanExtra2 = intent.getBooleanExtra(IMAGE_PHOTO_KEY, false);
        if (intent.hasExtra(IMAGE_INDICATOR_KEY)) {
            this.mIndicatorType = intent.getStringExtra(IMAGE_INDICATOR_KEY);
        }
        RelativeLayout relativeLayout = new RelativeLayout(this.that);
        relativeLayout.setBackgroundColor(intent.getIntExtra(IMAGE_BG_KEY, -16777216));
        relativeLayout.postDelayed(new Runnable() {
            public void run() {
                PhotoActivity.this.isBack = true;
            }
        }, 1000);
        if (this.mImageUrls.size() > 0) {
            this.localImageUrls = new String[((!booleanExtra || this.mImageUrls.size() != 2) ? this.mImageUrls.size() : this.mImageUrls.size() + 2)];
            BannerLayout bannerLayout = new BannerLayout(this.that, booleanExtra, booleanExtra2);
            this.photoLayout = bannerLayout;
            bannerLayout.setImageLoader(new BannerLayout.ImageLoader() {
                public void displayImage(Context context, final String str, final View view, final int i) {
                    Uri imageFileUri;
                    Object tag = view.getTag();
                    view.setTag((Object) null);
                    if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        str = str.replace(DeviceInfo.FILE_PROTOCOL, "");
                    }
                    if (!PdrUtil.isDeviceRootDir(str) && !PdrUtil.isNetPath(str) && !PdrUtil.isBase64ImagePath(str)) {
                        if (str.startsWith("assets://")) {
                            str = str.replace("assets://", SDK.ANDROID_ASSET);
                        } else if (str.startsWith("/")) {
                            str = "file:///android_asset" + str;
                        } else {
                            str = SDK.ANDROID_ASSET + str;
                        }
                    }
                    if (!FileUtil.checkPrivatePath(context, str) && (imageFileUri = FileUtil.getImageFileUri(context, str)) != null) {
                        str = imageFileUri.toString();
                    }
                    Glide.with((FragmentActivity) PhotoActivity.this).download(str).into(new SimpleTarget<File>() {
                        private ProgressBar bar;
                        private SubsamplingScaleImageView subImageview;

                        public void onLoadStarted(Drawable drawable) {
                            View view = view;
                            if (view instanceof RelativeLayout) {
                                try {
                                    this.subImageview = (SubsamplingScaleImageView) ((RelativeLayout) view).getChildAt(0);
                                    this.bar = (ProgressBar) ((RelativeLayout) view).getChildAt(1);
                                } catch (Exception unused) {
                                }
                            }
                        }

                        public boolean typeOf(File file, String str) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            if (options.outMimeType.toLowerCase(Locale.ENGLISH).contains(str)) {
                                return true;
                            }
                            return false;
                        }

                        public void onResourceReady(File file, Transition<? super File> transition) {
                            ImageSource imageSource;
                            View view = view;
                            if (view instanceof RelativeLayout) {
                                try {
                                    if (!typeOf(file, "gif")) {
                                        if (typeOf(file, "bmp")) {
                                            imageSource = ImageSource.bitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                                        } else {
                                            imageSource = ImageSource.uri(file.getAbsolutePath());
                                        }
                                        this.subImageview.setImage(imageSource);
                                    } else {
                                        this.subImageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                        this.subImageview.setImageURI(Uri.fromFile(file));
                                    }
                                    this.bar.setVisibility(8);
                                } catch (Exception unused) {
                                }
                            } else {
                                ((ImageView) view).setImageURI(Uri.fromFile(file));
                            }
                            if (PdrUtil.isNetPath(str) && PhotoActivity.this.localImageUrls != null && i < PhotoActivity.this.localImageUrls.length) {
                                PhotoActivity.this.localImageUrls[i] = file.getAbsolutePath();
                            }
                        }
                    });
                    view.setTag(tag);
                }
            });
            this.photoLayout.setIndicatorContainerData((BannerLayout.Position) null, 20, 10, 18, this.mIndicatorType);
            this.photoLayout.setScrollDuration(500);
            this.photoLayout.setViewUrls(this.mImageUrls, this.mCurrentItmeIndex);
            if (this.mImageUrls.size() == 2 && booleanExtra && this.photoLayout.getPager() != null) {
                this.photoLayout.getPager().setOffscreenPageLimit(2);
            }
            this.photoLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                public void onItemClick(int i) {
                    PhotoActivity photoActivity = PhotoActivity.this;
                    if (photoActivity.isBack) {
                        photoActivity.onBackPressed();
                    }
                }

                public void onItemLongClick(int i) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("index", i);
                        if (!(PhotoActivity.this.originalImageUrls == null || PhotoActivity.this.originalImageUrls.get(i) == null)) {
                            jSONObject.put("url", PhotoActivity.this.originalImageUrls.get(i));
                        }
                        if (PhotoActivity.this.localImageUrls[i] != null) {
                            jSONObject.put(AbsoluteConst.XML_PATH, PhotoActivity.this.localImageUrls[i]);
                        }
                    } catch (JSONException unused) {
                    }
                    LongClickEventManager.getInstance().fireEvent(jSONObject);
                }
            });
            relativeLayout.addView(this.photoLayout, new RelativeLayout.LayoutParams(-1, -1));
        }
        setContentView(relativeLayout, new ViewGroup.LayoutParams(-1, -1));
        fullScreen(this);
    }
}
