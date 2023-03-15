package io.dcloud.feature.weex.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import java.util.HashMap;
import java.util.Map;

public class GlideImageAdapter {
    public static void setImage(final String str, final ImageView imageView, WXImageQuality wXImageQuality, final WXImageStrategy wXImageStrategy) {
        AnonymousClass1 r4 = new Runnable() {
            public void run() {
                ImageView imageView = imageView;
                if (imageView != null && imageView.getLayoutParams() != null) {
                    if (TextUtils.isEmpty(str)) {
                        imageView.setImageBitmap((Bitmap) null);
                        return;
                    }
                    String str = str;
                    if (str.startsWith("//")) {
                        str = "http:" + str;
                    }
                    if (imageView.getLayoutParams().width > 0) {
                        RequestOptions requestOptions = new RequestOptions();
                        DisplayMetrics displayMetrics = imageView.getResources().getDisplayMetrics();
                        requestOptions.override(displayMetrics.widthPixels, displayMetrics.heightPixels);
                        Context context = imageView.getContext();
                        if (str.contains(".gif")) {
                            Glide.with(context).asGif().load(str).apply((BaseRequestOptions<?>) requestOptions).listener(new RequestListener<GifDrawable>() {
                                public boolean onLoadFailed(GlideException glideException, Object obj, Target<GifDrawable> target, boolean z) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("errorMessage", glideException.getMessage());
                                    GlideImageAdapter.onImageFinish(wXImageStrategy, str, imageView, false, hashMap);
                                    return false;
                                }

                                public boolean onResourceReady(GifDrawable gifDrawable, Object obj, Target<GifDrawable> target, DataSource dataSource, boolean z) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("width", Integer.valueOf(gifDrawable.getIntrinsicWidth()));
                                    hashMap.put("height", Integer.valueOf(gifDrawable.getIntrinsicHeight()));
                                    GlideImageAdapter.onImageFinish(wXImageStrategy, str, imageView, true, hashMap);
                                    return false;
                                }
                            }).into(imageView);
                        } else {
                            Glide.with(context).load(str).apply((BaseRequestOptions<?>) requestOptions).listener(new RequestListener<Drawable>() {
                                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("errorMessage", glideException.getMessage());
                                    GlideImageAdapter.onImageFinish(wXImageStrategy, str, imageView, false, hashMap);
                                    return false;
                                }

                                public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("width", Integer.valueOf(drawable.getIntrinsicWidth()));
                                    hashMap.put("height", Integer.valueOf(drawable.getIntrinsicHeight()));
                                    GlideImageAdapter.onImageFinish(wXImageStrategy, str, imageView, true, hashMap);
                                    return false;
                                }
                            }).into(imageView);
                        }
                    }
                }
            }
        };
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            r4.run();
        } else {
            WXSDKManager.getInstance().postOnUiThread(r4, 0);
        }
    }

    /* access modifiers changed from: private */
    public static void onImageFinish(WXImageStrategy wXImageStrategy, String str, ImageView imageView, boolean z, Map map) {
        if (wXImageStrategy != null && wXImageStrategy.getImageListener() != null) {
            wXImageStrategy.getImageListener().onImageFinish(str, imageView, z, map);
        }
    }
}
