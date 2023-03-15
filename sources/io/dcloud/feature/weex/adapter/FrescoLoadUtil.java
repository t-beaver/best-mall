package io.dcloud.feature.weex.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.uniapp.adapter.UniImageLoadAdapter;
import io.dcloud.feature.uniapp.utils.bitmap.BitmapLoadCallback;
import java.lang.ref.SoftReference;

public class FrescoLoadUtil implements UniImageLoadAdapter {
    private static FrescoLoadUtil inst;
    Handler mHandler = new Handler(Looper.getMainLooper());

    public static UniImageLoadAdapter getInstance() {
        if (inst == null) {
            inst = new FrescoLoadUtil();
        }
        return inst;
    }

    public final void loadImageBitmap(Context context, String str, BitmapLoadCallback<Bitmap> bitmapLoadCallback) {
        loadImageBitmap(context, str, 0, 0, bitmapLoadCallback);
    }

    public final void loadImageBitmap(Context context, String str, int i, int i2, BitmapLoadCallback<Bitmap> bitmapLoadCallback) {
        String str2;
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith("//")) {
                str2 = "http:" + str;
            } else {
                str2 = str;
            }
            try {
                fetch(context, Uri.parse(str2), i, i2, bitmapLoadCallback);
            } catch (Exception e) {
                e.printStackTrace();
                bitmapLoadCallback.onFailure(str, e);
            }
        }
    }

    private void fetch(Context context, Uri uri, int i, int i2, BitmapLoadCallback<Bitmap> bitmapLoadCallback) {
        final Uri uri2 = uri;
        final int i3 = i2;
        final int i4 = i;
        final Context context2 = context;
        final BitmapLoadCallback<Bitmap> bitmapLoadCallback2 = bitmapLoadCallback;
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                ImageRequestBuilder progressiveRenderingEnabled = ImageRequestBuilder.newBuilderWithSource(uri2).setProgressiveRenderingEnabled(false);
                progressiveRenderingEnabled.setPostprocessor(new Postprocessor() {
                    public String getName() {
                        return null;
                    }

                    public CacheKey getPostprocessorCacheKey() {
                        return null;
                    }

                    public CloseableReference<Bitmap> process(Bitmap bitmap, PlatformBitmapFactory platformBitmapFactory) {
                        if (i3 <= 0 || i3 <= 0) {
                            return platformBitmapFactory.createBitmap(bitmap);
                        }
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        Matrix matrix = new Matrix();
                        matrix.postScale(((float) i4) / ((float) width), ((float) i3) / ((float) height));
                        return platformBitmapFactory.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    }
                });
                Fresco.getImagePipeline().fetchDecodedImage(progressiveRenderingEnabled.build(), context2).subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                    /* access modifiers changed from: protected */
                    public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        CloseableReference result;
                        if (dataSource.isFinished() && (result = dataSource.getResult()) != null) {
                            Bitmap bitmap = null;
                            try {
                                if (result.get() instanceof CloseableBitmap) {
                                    bitmap = ((CloseableBitmap) result.get()).getUnderlyingBitmap();
                                }
                                if (bitmap != null && !bitmap.isRecycled()) {
                                    SoftReference softReference = new SoftReference(Bitmap.createBitmap(bitmap));
                                    if (bitmapLoadCallback2 != null) {
                                        bitmapLoadCallback2.onSuccess(uri2.toString(), (Bitmap) softReference.get());
                                    }
                                }
                            } finally {
                                result.close();
                                dataSource.close();
                            }
                        }
                    }

                    /* access modifiers changed from: protected */
                    public void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        if (bitmapLoadCallback2 != null) {
                            bitmapLoadCallback2.onSuccess(uri2.toString(), null);
                        }
                    }
                }, UiThreadImmediateExecutorService.getInstance());
            }
        }, true);
    }
}
