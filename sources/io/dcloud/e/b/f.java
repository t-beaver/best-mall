package io.dcloud.e.b;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import io.dcloud.common.DHInterface.IAppInfo;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.util.AppStatusBarManager;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;

public class f implements IAppInfo {
    protected Activity a = null;
    protected IWebAppRootView b = null;
    private IOnCreateSplashView c = null;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    public int g = 0;
    public int h = 0;
    protected boolean i = false;
    private boolean j = false;
    private int k = 0;
    ViewRect l = new ViewRect();
    AppStatusBarManager m;
    public String n = "none";
    final String o = "Q&2U*0E^1S#600T7";
    public String p = null;
    protected boolean q = false;

    class a implements MessageHandler.IMessages {
        final /* synthetic */ String a;

        a(String str) {
            this.a = str;
        }

        public void execute(Object obj) {
            if ("landscape".equals(this.a)) {
                f.this.a.setRequestedOrientation(6);
            } else if ("landscape-primary".equals(this.a)) {
                f.this.a.setRequestedOrientation(0);
            } else if ("landscape-secondary".equals(this.a)) {
                f.this.a.setRequestedOrientation(8);
            } else if ("portrait".equals(this.a)) {
                f.this.a.setRequestedOrientation(7);
            } else if ("portrait-primary".equals(this.a)) {
                f.this.a.setRequestedOrientation(1);
            } else if ("portrait-secondary".equals(this.a)) {
                f.this.a.setRequestedOrientation(9);
            } else {
                f.this.a.setRequestedOrientation(4);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(Activity activity) {
        if (this.m == null) {
            this.m = new AppStatusBarManager(activity, this);
        }
        this.a = activity;
    }

    public int checkSelfPermission(String str, String str2) {
        return PermissionUtil.checkSelfPermission(this.a, str, str2);
    }

    public void clearMaskLayerCount() {
        this.k = 0;
    }

    public Activity getActivity() {
        return this.a;
    }

    public ViewRect getAppViewRect() {
        return this.l;
    }

    public int getInt(int i2) {
        if (i2 == 0) {
            return this.d;
        }
        if (i2 == 1) {
            return this.h;
        }
        if (i2 != 2) {
            return -1;
        }
        return this.e;
    }

    public int getMaskLayerCount() {
        return this.k;
    }

    public IOnCreateSplashView getOnCreateSplashView() {
        return this.c;
    }

    public int getRequestedOrientation() {
        return this.a.getRequestedOrientation();
    }

    public boolean isFullScreen() {
        return this.i;
    }

    public boolean isVerticalScreen() {
        return this.a.getResources().getConfiguration().orientation == 1;
    }

    public IWebAppRootView obtainWebAppRootView() {
        return this.b;
    }

    public void requestPermissions(String[] strArr, int i2) {
        PermissionUtil.requestPermissions(this.a, strArr, i2);
    }

    public void setFullScreen(boolean z) {
        if (BaseInfo.sGlobalFullScreen != z) {
            this.i = z;
            AppStatusBarManager appStatusBarManager = this.m;
            if (appStatusBarManager != null) {
                appStatusBarManager.setFullScreen(getActivity(), z);
            }
            updateScreenInfo(this.i ? 2 : 3);
        }
        BaseInfo.sGlobalFullScreen = z;
    }

    public void setMaskLayer(boolean z) {
        this.j = z;
        if (z) {
            this.k++;
            return;
        }
        int i2 = this.k - 1;
        this.k = i2;
        if (i2 < 0) {
            this.k = 0;
        }
    }

    public void setOnCreateSplashView(IOnCreateSplashView iOnCreateSplashView) {
        this.c = iOnCreateSplashView;
    }

    public void setRequestedOrientation(String str) {
        try {
            MessageHandler.sendMessage(new a(str), 48, str);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void setWebAppRootView(IWebAppRootView iWebAppRootView) {
        this.b = iWebAppRootView;
    }

    public void updateScreenInfo(int i2) {
        if (!this.i && this.f == 0) {
            Rect rect = new Rect();
            this.a.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int i3 = rect.top;
            this.f = i3;
            if (i3 > 0) {
                SP.setBundleData(getActivity(), BaseInfo.PDR, "StatusBarHeight", String.valueOf(this.f));
            }
        }
        DisplayMetrics displayMetrics = this.a.getResources().getDisplayMetrics();
        this.a.getResources().getDisplayMetrics();
        int i4 = displayMetrics.widthPixels;
        int i5 = displayMetrics.heightPixels;
        boolean isAllScreenDevice = PdrUtil.isAllScreenDevice(this.a);
        int i6 = 0;
        if (isAllScreenDevice) {
            this.a.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            int i7 = displayMetrics.heightPixels;
            int i8 = displayMetrics.widthPixels;
            AppStatusBarManager appStatusBarManager = this.m;
            if (appStatusBarManager != null && !appStatusBarManager.isFullScreenOrImmersive()) {
                i7 -= this.f;
            }
            i5 = i7;
            boolean z = PdrUtil.isNavigationBarExist(this.a) && !this.q;
            int navigationBarHeight = PdrUtil.getNavigationBarHeight(this.a);
            if (this.a.getResources().getConfiguration().orientation == 1) {
                if (z) {
                    i5 -= navigationBarHeight;
                }
            } else if (z) {
                i4 = i8 - navigationBarHeight;
            }
            i4 = i8;
        }
        this.e = i5;
        if (i2 == 2) {
            this.d = i4;
            this.h = i5;
        } else if (i2 == 1) {
            this.d = i4;
            if (isAllScreenDevice) {
                this.h = i5;
            } else {
                if (!this.m.isFullScreenOrImmersive()) {
                    i6 = this.f;
                }
                this.h = i5 - i6;
            }
        } else {
            IWebAppRootView iWebAppRootView = this.b;
            if (iWebAppRootView != null) {
                this.d = iWebAppRootView.obtainMainView().getWidth();
                this.h = this.b.obtainMainView().getHeight();
            } else {
                this.d = i4;
                this.h = i5;
            }
        }
        int i9 = this.e;
        int i10 = this.h;
        if (i9 < i10) {
            this.e = i10;
        }
        this.l.onScreenChanged(this.d, i10);
    }

    public void setRequestedOrientation(int i2) {
        this.a.setRequestedOrientation(i2);
    }
}
