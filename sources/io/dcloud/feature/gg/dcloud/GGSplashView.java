package io.dcloud.feature.gg.dcloud;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.taobao.weex.common.Constants;
import io.dcloud.PdrR;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.feature.internal.splash.ISplash;
import io.dcloud.h.c.d.a;

public class GGSplashView extends FrameLayout implements ISplash {
    String appid;
    View bottomIcon;
    FrameLayout container;
    Handler handler = new Handler(Looper.getMainLooper());
    ICallBack mCallBack;
    private long pullTime;

    public GGSplashView(Activity activity) {
        super(activity);
        initView(activity);
    }

    private void initView(Activity activity) {
        try {
            View inflate = LayoutInflater.from(activity).inflate(PdrR.getInt(activity, Constants.Name.LAYOUT, PdrUtil.dealString("ilWlkdg}lW{xdi{`")), (ViewGroup) null);
            inflate.setBackgroundColor(getBgColor());
            addView(inflate);
            this.container = (FrameLayout) inflate.findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lW{xdi{`Wkgf|iafmz")));
            Drawable icon = getIcon();
            this.bottomIcon = inflate.findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lW{xdi{`Wjg||geWjiz")));
            ImageView imageView = (ImageView) findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lWakgfW{afodm")));
            if (icon == null) {
                imageView.setVisibility(8);
                ((ImageView) findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lWakgf")))).setImageDrawable(AdSplashUtil.getApplicationIcon(activity));
                ((TextView) findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lWfiem")))).setText(AdSplashUtil.getApplicationName(activity));
                return;
            }
            imageView.setVisibility(0);
            imageView.setImageDrawable(icon);
            findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lWfiem"))).setVisibility(8);
            findViewById(PdrR.getInt(activity, "id", PdrUtil.dealString("ilWlkdg}lWakgf"))).setVisibility(8);
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public String get(String str) {
        return SP.getBundleData(ADHandler.AdTag, str);
    }

    public int getBgColor() {
        int stringToColor = PdrUtil.stringToColor(get("bg"));
        if (stringToColor != -1) {
            return stringToColor;
        }
        return -1;
    }

    public View getBottomIcon() {
        return this.bottomIcon;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0036 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable getIcon() {
        /*
            r3 = this;
            java.lang.String r0 = "img"
            java.lang.String r0 = r3.get(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 0
            if (r1 != 0) goto L_0x002d
            boolean r1 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r0)
            if (r1 == 0) goto L_0x0023
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r1 = r1.exists()
            if (r1 == 0) goto L_0x002d
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeFile(r0)
            goto L_0x002e
        L_0x0023:
            r1 = 0
            java.io.InputStream r0 = io.dcloud.common.adapter.util.PlatformUtil.getInputStream(r0, r1)
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r0)
            goto L_0x002e
        L_0x002d:
            r0 = r2
        L_0x002e:
            if (r0 == 0) goto L_0x0036
            android.graphics.drawable.BitmapDrawable r1 = new android.graphics.drawable.BitmapDrawable
            r1.<init>(r0)
            return r1
        L_0x0036:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gg.dcloud.GGSplashView.getIcon():android.graphics.drawable.Drawable");
    }

    public FrameLayout getImgContainer() {
        return this.container;
    }

    public void onFinishShow() {
        ICallBack iCallBack = this.mCallBack;
        if (iCallBack != null) {
            iCallBack.onCallBack(1, this.appid);
            this.mCallBack = null;
        }
        FrameLayout frameLayout = this.container;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
    }

    public void onWillCloseSplash() {
        if (this.container.getChildCount() == 0) {
            onFinishShow();
        }
    }

    public void setAppid(String str) {
        this.appid = str;
    }

    public void setCallBack(ICallBack iCallBack) {
        this.mCallBack = iCallBack;
    }

    public void setImageBitmap(Bitmap bitmap) {
    }

    public void setNameText(String str) {
    }

    public void setPullTime(long j) {
        this.pullTime = j;
    }

    public void showAd(final a aVar) {
        if (getParent() != null) {
            aVar.a((ViewGroup) this.container);
        } else {
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    if (GGSplashView.this.getParent() != null) {
                        aVar.a((ViewGroup) GGSplashView.this.container);
                    } else {
                        GGSplashView.this.handler.postDelayed(this, 20);
                    }
                }
            }, 20);
        }
    }
}
