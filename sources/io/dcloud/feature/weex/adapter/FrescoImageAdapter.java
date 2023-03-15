package io.dcloud.feature.weex.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.weex.adapter.Fresco.DCGenericDraweeHierarchy;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FrescoImageAdapter implements IWXImgLoaderAdapter {
    public void setImage(String str, ImageView imageView, WXImageQuality wXImageQuality, WXImageStrategy wXImageStrategy) {
        if (PdrUtil.isEmpty(str)) {
            if (imageView != null) {
                imageView.setImageBitmap((Bitmap) null);
            }
        } else if (wXImageStrategy == null || wXImageStrategy.placeHolder == null) {
            final String str2 = str;
            final ImageView imageView2 = imageView;
            final WXImageQuality wXImageQuality2 = wXImageQuality;
            final WXImageStrategy wXImageStrategy2 = wXImageStrategy;
            WXSDKManager.getInstance().postOnUiThread(new Runnable() {
                public void run() {
                    FrescoImageAdapter.setImage(str2, imageView2, wXImageQuality2, wXImageStrategy2, (Drawable) null);
                }
            }, 0);
        } else {
            final String str3 = str;
            final WXImageStrategy wXImageStrategy3 = wXImageStrategy;
            final ImageView imageView3 = imageView;
            final WXImageQuality wXImageQuality3 = wXImageQuality;
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    Bitmap bitmap;
                    Logger.d("FrescoImage", "Thread_setImage--" + str3);
                    try {
                        if (wXImageStrategy3.placeHolder.startsWith("file")) {
                            bitmap = BitmapFactory.decodeFile(wXImageStrategy3.placeHolder.replaceFirst(DeviceInfo.FILE_PROTOCOL, ""));
                        } else {
                            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(wXImageStrategy3.placeHolder).openConnection();
                            httpURLConnection.connect();
                            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        bitmap = null;
                    }
                    final BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    WXSDKManager.getInstance().postOnUiThread(new Runnable() {
                        public void run() {
                            FrescoImageAdapter.setImage(str3, imageView3, wXImageQuality3, wXImageStrategy3, bitmapDrawable);
                        }
                    }, 0);
                }
            }, true);
        }
    }

    /* access modifiers changed from: private */
    public static void setImage(final String str, final ImageView imageView, WXImageQuality wXImageQuality, final WXImageStrategy wXImageStrategy, Drawable drawable) {
        String str2;
        if (imageView != null && imageView.getLayoutParams() != null) {
            if (TextUtils.isEmpty(str)) {
                imageView.setImageBitmap((Bitmap) null);
                return;
            }
            if (str.startsWith("//")) {
                str2 = "http:" + str;
            } else {
                str2 = str;
            }
            ImageRequestBuilder progressiveRenderingEnabled = ImageRequestBuilder.newBuilderWithSource(Uri.parse(str2)).setImageDecodeOptions(ImageDecodeOptions.newBuilder().build()).setAutoRotateEnabled(true).setLocalThumbnailPreviewsEnabled(true).setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH).setProgressiveRenderingEnabled(false);
            if (!wXImageStrategy.isAutoCompression()) {
                progressiveRenderingEnabled.setResizeOptions(new ResizeOptions(Integer.MAX_VALUE, Integer.MAX_VALUE));
            }
            ImageRequest build = progressiveRenderingEnabled.build();
            if (imageView instanceof DraweeView) {
                AbstractDraweeController build2 = ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true)).setControllerListener(new BaseControllerListener<ImageInfo>() {
                    public void onFinalImageSet(String str, ImageInfo imageInfo, Animatable animatable) {
                        WXImageStrategy wXImageStrategy;
                        if (imageInfo != null && (wXImageStrategy = WXImageStrategy.this) != null && wXImageStrategy.getImageListener() != null) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("width", Integer.valueOf(imageInfo.getWidth()));
                            hashMap.put("height", Integer.valueOf(imageInfo.getHeight()));
                            if (imageInfo.getWidth() > 0) {
                                WXImageStrategy.this.getImageListener().onImageFinish(str, imageView, true, hashMap);
                            } else {
                                WXImageStrategy.this.getImageListener().onImageFinish(str, imageView, false, hashMap);
                            }
                        }
                    }

                    public void onIntermediateImageSet(String str, ImageInfo imageInfo) {
                        FLog.d("", "Intermediate image received");
                    }

                    public void onFailure(String str, Throwable th) {
                        FLog.e(getClass(), th, "Error loading %s", str);
                        WXImageStrategy wXImageStrategy = WXImageStrategy.this;
                        if (wXImageStrategy != null && wXImageStrategy.getImageListener() != null) {
                            WXImageStrategy.this.getImageListener().onImageFinish(str, imageView, false, (Map) null);
                        }
                    }
                })).setImageRequest(build)).build();
                if (drawable != null) {
                    ((DCGenericDraweeHierarchy) ((FrescoImageView) imageView).getHierarchy()).setPlaceholderImage(drawable);
                }
                ((DraweeView) imageView).setController(build2);
                return;
            }
            Fresco.getImagePipeline().fetchDecodedImage(build, new Object()).subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    CloseableReference result = dataSource.getResult();
                    if (result != null) {
                        try {
                            Preconditions.checkState(CloseableReference.isValid(result));
                            CloseableImage closeableImage = (CloseableImage) result.get();
                            if (closeableImage instanceof CloseableStaticBitmap) {
                                imageView.setImageBitmap(((CloseableStaticBitmap) closeableImage).getUnderlyingBitmap());
                            }
                            WXImageStrategy wXImageStrategy = wXImageStrategy;
                            if (!(wXImageStrategy == null || wXImageStrategy.getImageListener() == null)) {
                                wXImageStrategy.getImageListener().onImageFinish(str, imageView, true, (Map) null);
                            }
                        } finally {
                            result.close();
                        }
                    }
                }

                public void onFailureImpl(DataSource dataSource) {
                    WXImageStrategy wXImageStrategy = wXImageStrategy;
                    if (wXImageStrategy != null && wXImageStrategy.getImageListener() != null) {
                        wXImageStrategy.getImageListener().onImageFinish(str, imageView, false, (Map) null);
                    }
                }
            }, UiThreadImmediateExecutorService.getInstance());
        }
    }
}
