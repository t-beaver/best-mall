package io.dcloud.feature.weex.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.taobao.weex.adapter.DrawableStrategy;
import com.taobao.weex.adapter.IDrawableLoader;
import io.dcloud.feature.uniapp.utils.bitmap.BitmapLoadCallback;

public class FrescoDrawableLoader implements IDrawableLoader {
    /* access modifiers changed from: private */
    public Context mContext;

    public FrescoDrawableLoader(Context context) {
        this.mContext = context;
    }

    public void setDrawable(String str, final IDrawableLoader.DrawableTarget drawableTarget, DrawableStrategy drawableStrategy) {
        FrescoLoadUtil.getInstance().loadImageBitmap(this.mContext, str, drawableStrategy.width, drawableStrategy.height, new BitmapLoadCallback<Bitmap>() {
            public void onFailure(String str, Throwable th) {
            }

            public void onSuccess(String str, Bitmap bitmap) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(FrescoDrawableLoader.this.mContext.getResources(), bitmap);
                bitmapDrawable.setGravity(119);
                drawableTarget.setDrawable(bitmapDrawable, true);
            }
        });
    }
}
