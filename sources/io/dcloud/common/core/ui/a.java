package io.dcloud.common.core.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.dcloud.android.graphics.Region;
import com.dcloud.android.widget.AbsoluteLayout;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.FeatureMessageDispatcher;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaContainerFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.DHImageView;
import io.dcloud.common.adapter.ui.RecordView;
import io.dcloud.common.adapter.ui.WebParentView;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.core.permission.PermissionControler;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;

class a extends AdaContainerFrameItem implements ISysEventListener, IWebAppRootView {
    protected String A;
    protected String B;
    private DHImageView C;
    ICallBack a = null;
    /* access modifiers changed from: private */
    public Stack<b> b = null;
    /* access modifiers changed from: private */
    public ArrayList<b> c = null;
    b d = null;
    b e = null;
    b f = null;
    b g = null;
    boolean h;
    String i;
    IApp j;
    public boolean k;
    private final int l;
    IActivityHandler m;
    k n;
    ICallBack o;
    long p;
    boolean q;
    boolean r;
    /* access modifiers changed from: private */
    public boolean s;
    int t;
    private boolean u;
    /* access modifiers changed from: private */
    public ArrayList<ICallBack> v;
    m w;
    private n x;
    private o y;
    protected byte z;

    /* renamed from: io.dcloud.common.core.ui.a$a  reason: collision with other inner class name */
    static /* synthetic */ class C0016a {
        static final /* synthetic */ int[] a;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|26) */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType[] r0 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                a = r0
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onPause     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onResume     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onSimStateChanged     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onDeviceNetChanged     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onNewIntent     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0049 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onConfigurationChanged     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0054 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onKeyboardShow     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0060 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onKeyboardHide     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x006c }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onWebAppBackground     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0078 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onWebAppForeground     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0084 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onWebAppTrimMemory     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0090 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onSplashclosed     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.a.C0016a.<clinit>():void");
        }
    }

    class b implements ICallBack {
        b() {
        }

        public Object onCallBack(int i, Object obj) {
            b i2;
            String valueOf = String.valueOf(obj);
            if (TextUtils.isEmpty(valueOf) || !valueOf.equals("com.huawei.intent.action.CLICK_STATUSBAR") || a.this.obtainMainView().getParent() == null || (i2 = a.this.i()) == null) {
                return null;
            }
            i2.c();
            return null;
        }
    }

    class c implements MessageHandler.IMessages {
        final /* synthetic */ b a;

        c(b bVar) {
            this.a = bVar;
        }

        public void execute(Object obj) {
            if (a.this.b != null && this.a != null) {
                Logger.d("DHAppRootView.popFrameView frame" + this.a);
                a.this.b.remove(this.a);
                a.this.a(this.a);
            }
        }
    }

    class d implements MessageHandler.IMessages {
        final /* synthetic */ ArrayList a;

        d(ArrayList arrayList) {
            this.a = arrayList;
        }

        public void execute(Object obj) {
            try {
                if (a.this.b != null) {
                    for (int size = a.this.b.size() - 1; size >= 0; size--) {
                        b bVar = (b) a.this.b.get(size);
                        if (!this.a.contains(bVar)) {
                            l lVar = bVar.mWindowMgr;
                            if (lVar != null) {
                                lVar.processEvent(IMgr.MgrType.WindowMgr, 22, bVar);
                            }
                            bVar.m = true;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.w("DHAppRootView onConfigurationChanged", e);
            }
        }
    }

    class e implements Runnable {
        e() {
        }

        public void run() {
            IActivityHandler iActivityHandler = a.this.m;
            if (iActivityHandler != null) {
                iActivityHandler.showSplashWaiting();
            }
        }
    }

    class f implements MessageHandler.IMessages {
        final /* synthetic */ boolean a;
        final /* synthetic */ a b;
        final /* synthetic */ int c;

        f(boolean z, a aVar, int i) {
            this.a = z;
            this.b = aVar;
            this.c = i;
        }

        public void execute(Object obj) {
            Logger.d("approotview", "closeSplashScreen1;autoClose=" + this.a + ";mAppid" + a.this.i);
            a.this.a(this.b, this.c);
        }
    }

    class g implements Runnable {
        final /* synthetic */ a a;
        final /* synthetic */ int b;

        g(a aVar, int i) {
            this.a = aVar;
            this.b = i;
        }

        public void run() {
            boolean unused = a.this.s = false;
            a.this.a(this.a, this.b);
        }
    }

    class h implements ICallBack {
        h() {
        }

        public Object onCallBack(int i, Object obj) {
            IApp iApp;
            a.this.j.onSplashClosed();
            a aVar = a.this;
            if (!aVar.q || (iApp = aVar.j) == null) {
                aVar.r = true;
                return null;
            }
            iApp.callSysEventListener(ISysEventListener.SysEventType.onSplashclosed, this);
            return null;
        }
    }

    class i implements ViewTreeObserver.OnGlobalLayoutListener {
        i() {
        }

        public void onGlobalLayout() {
            View obtainMainView = a.this.obtainMainView();
            a.this.onRootViewGlobalLayout(obtainMainView);
            if (obtainMainView != null && DeviceInfo.sDeviceSdkVer >= 16 && obtainMainView.getViewTreeObserver() != null) {
                obtainMainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                obtainMainView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        }
    }

    class j implements MessageHandler.IMessages {
        j() {
        }

        public void execute(Object obj) {
            Iterator it = a.this.v.iterator();
            while (it.hasNext()) {
                ((ICallBack) it.next()).onCallBack(-1, (Object) null);
            }
            a.this.v.clear();
        }
    }

    class l extends k {
        c A = new c();
        Paint u = new Paint();
        int v;
        int w;
        int x;
        int y;
        String z = "";

        /* renamed from: io.dcloud.common.core.ui.a$l$a  reason: collision with other inner class name */
        class C0017a implements ICallBack {
            C0017a() {
            }

            public Object onCallBack(int i, Object obj) {
                l.this.m.c();
                a.this.a = null;
                return null;
            }
        }

        class b implements Runnable {
            b() {
            }

            public void run() {
                if (a.this.c != null) {
                    Iterator it = a.this.c.iterator();
                    while (it.hasNext()) {
                        b bVar = (b) it.next();
                        if (!bVar.isChildOfFrameView) {
                            bVar.resize();
                        }
                    }
                }
            }
        }

        class c implements Runnable {
            int a = 0;
            boolean b = false;

            c() {
            }

            public void run() {
                if (this.b) {
                    this.a++;
                    l.this.invalidate();
                    this.a %= 4;
                    l.this.postDelayed(this, 500);
                    return;
                }
                this.a = 0;
            }
        }

        public l(Context context, a aVar) {
            super(context, aVar);
            double d = (double) (DeviceInfo.DEFAULT_FONT_SIZE * DeviceInfo.sDensity);
            Double.isNaN(d);
            this.u.setColor(-13421773);
            this.u.setTextSize((float) ((int) (d * 1.2d)));
            setTag("AppRootView");
            String string = context.getString(R.string.dcloud_common_in_the_buffer);
            this.z = string;
            this.v = (int) this.u.measureText(string);
            this.w = (int) this.u.measureText("...");
        }

        public void dispatchConfigurationChanged(Configuration configuration) {
            super.dispatchConfigurationChanged(configuration);
            if (BaseInfo.sDoingAnimation) {
                a.this.a = new C0017a();
                return;
            }
            this.m.c();
        }

        /* access modifiers changed from: protected */
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (a.this.j.getMaskLayerCount() > 0) {
                c cVar = this.A;
                if (!cVar.b) {
                    cVar.b = true;
                    cVar.run();
                }
                canvas.drawColor(-2013265920);
                canvas.drawText(this.z, (float) this.x, (float) this.y, this.u);
                int i = this.A.a;
                if (i == 1) {
                    canvas.drawText(Operators.DOT_STR, (float) (this.x + this.v), (float) this.y, this.u);
                } else if (i == 2) {
                    canvas.drawText(PdrUtil.FILE_PATH_ENTRY_BACK, (float) (this.x + this.v), (float) this.y, this.u);
                } else if (i == 3) {
                    canvas.drawText("...", (float) (this.x + this.v), (float) this.y, this.u);
                }
            } else {
                this.A.b = false;
            }
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (a.this.j.getMaskLayerCount() > 0) {
                return true;
            }
            return super.dispatchTouchEvent(motionEvent);
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            a.this.mViewOptions.onScreenChanged();
            PlatformUtil.RESET_H_W();
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z2, int i, int i2, int i3, int i4) {
            super.onLayout(z2, i, i2, i3, i4);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.x = ((a.this.j.getInt(0) - this.v) - this.w) / 2;
            double d = (double) a.this.j.getInt(2);
            Double.isNaN(d);
            this.y = (int) (d * 0.8d);
        }

        /* access modifiers changed from: protected */
        public void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            if (!(AdaWebview.sCustomeizedInputConnection == null || i2 == 0 || i4 == 0)) {
                int i5 = getResources().getDisplayMetrics().heightPixels;
                int i6 = getResources().getDisplayMetrics().heightPixels / 4;
                if (Math.abs(i2 - i5) > i6 || Math.abs(i2 - i4) > i6) {
                    if (i2 <= i4 || Math.abs(i2 - i4) <= i6) {
                        AdaWebview.sCustomeizedInputConnection.showRecordView(i2, true);
                    } else {
                        AdaWebview.sCustomeizedInputConnection.closeRecordView();
                    }
                }
            }
            a.this.j.updateScreenInfo(6);
            a.this.j.callSysEventListener(ISysEventListener.SysEventType.onSizeChanged, new int[]{i, i2, i3, i4});
            a.this.mViewOptions.onScreenChanged(i, i2);
            post(new b());
        }
    }

    class m implements Runnable {
    }

    class n implements Comparator<b> {
        n() {
        }

        /* renamed from: a */
        public int compare(b bVar, b bVar2) {
            if (bVar.getFrameType() == 3) {
                return 1;
            }
            if (bVar2.getFrameType() == 3) {
                return -1;
            }
            int i = bVar.mZIndex - bVar2.mZIndex;
            if (i != 0) {
                return i;
            }
            if (bVar.lastShowTime > bVar2.lastShowTime) {
                return 1;
            }
            return -1;
        }
    }

    class o implements Comparator<AdaFrameItem> {
        o() {
        }

        /* renamed from: a */
        public int compare(AdaFrameItem adaFrameItem, AdaFrameItem adaFrameItem2) {
            boolean z = adaFrameItem instanceof IFrameView;
            if (z && ((IFrameView) adaFrameItem).getFrameType() == 3) {
                return 1;
            }
            if (z && ((IFrameView) adaFrameItem).getFrameType() == 3) {
                return -1;
            }
            int i = adaFrameItem.mZIndex - adaFrameItem2.mZIndex;
            if (i != 0) {
                return i;
            }
            if (adaFrameItem.lastShowTime > adaFrameItem2.lastShowTime) {
                return 1;
            }
            return -1;
        }
    }

    public a(Context context, IApp iApp, b bVar) {
        super(context);
        boolean z2 = true;
        this.h = true;
        this.i = null;
        this.j = null;
        this.k = false;
        this.l = 2;
        this.n = new k();
        this.o = new b();
        this.p = System.currentTimeMillis();
        this.q = false;
        this.r = false;
        this.s = false;
        this.t = 0;
        this.u = true;
        this.v = new ArrayList<>();
        this.x = new n();
        this.y = new o();
        this.C = null;
        this.q = BaseInfo.sRuntimeMode == null ? false : z2;
        this.j = iApp;
        this.m = DCloudAdapterUtil.getIActivityHandler(iApp.getActivity());
        this.i = iApp.obtainAppId();
        setMainView(new l(context, this));
        this.b = new Stack<>();
        this.c = new ArrayList<>();
        iApp.setWebAppRootView(this);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onPause);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onResume);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onDeviceNetChanged);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onNewIntent);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onConfigurationChanged);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onSimStateChanged);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyboardShow);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppBackground);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppForeground);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyboardHide);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppTrimMemory);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onSplashclosed);
        if (PermissionControler.checkPermission(this.i, IFeature.F_DEVICE.toLowerCase(Locale.ENGLISH))) {
            String bundleData = SP.getBundleData(context, BaseInfo.PDR, "last_notify_net_type");
            String netWorkType = DeviceInfo.getNetWorkType();
            if (!PdrUtil.isEquals(bundleData, netWorkType)) {
                Logger.d("NetCheckReceiver", "netchange last_net_type:" + bundleData + ";cur_net_type:" + netWorkType);
                SP.setBundleData(context, BaseInfo.PDR, "last_notify_net_type", netWorkType);
            }
        }
        this.m.addClickStatusbarCallBack(this.o);
    }

    private void a(View view) {
    }

    public boolean didCloseSplash() {
        return this.q;
    }

    public synchronized void dispose() {
        b();
        this.b = null;
        this.c = null;
        if (this.w == null) {
            DHImageView dHImageView = this.C;
            if (dHImageView != null) {
                dHImageView.setImageBitmap((Bitmap) null);
                this.C = null;
            }
            super.dispose();
            this.m.removeClickStatusbarCallBack(this.o);
            RecordView recordView = AdaWebview.mRecordView;
            if (recordView != null) {
                recordView.dispose();
            }
            AdaWebview.mRecordView = null;
        } else {
            throw null;
        }
    }

    /* access modifiers changed from: package-private */
    public b f() {
        return a(2);
    }

    public IFrameView findFrameViewB(IFrameView iFrameView) {
        if (!this.c.contains(iFrameView)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        a(iFrameView, (ArrayList<b>) arrayList);
        a((ArrayList<b>) arrayList);
        if (arrayList.size() <= 1 && arrayList.size() == 1) {
            return (IFrameView) arrayList.get(0);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void g(b bVar) {
        MessageHandler.sendMessage(new c(bVar), (Object) null);
    }

    public void goHome(IFrameView iFrameView) {
        if (iFrameView instanceof b) {
            b bVar = (b) iFrameView;
            bVar.getAnimOptions().mOption = 1;
            bVar.getAnimOptions().mAnimType = "none";
            bVar.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 2, iFrameView);
        }
    }

    public void h() {
        if (AndroidResources.sIMEAlive && this.v.size() > 0) {
            int i2 = 500;
            if (BaseInfo.isUniAppAppid(this.j)) {
                i2 = 50;
            }
            MessageHandler.sendMessage(new j(), (long) i2, (Object) null);
        }
    }

    public b i() {
        Stack<b> stack = this.b;
        b bVar = null;
        if (stack != null && !stack.isEmpty()) {
            for (int size = this.b.size() - 1; size >= 0; size--) {
                bVar = (b) this.b.get(size);
                if (bVar.obtainMainView().getVisibility() == 0 && !bVar.isChildOfFrameView) {
                    break;
                }
            }
        }
        return bVar;
    }

    /* access modifiers changed from: package-private */
    public void j() {
        Stack<b> stack = this.b;
        if (stack != null && !stack.isEmpty()) {
            Boolean bool = Boolean.FALSE;
            for (int size = this.b.size() - 1; size >= 0; size--) {
                b bVar = (b) this.b.get(size);
                View obtainMainView = bVar.obtainMainView();
                if (!bVar.isChildOfFrameView) {
                    if (bool.booleanValue() || obtainMainView.getVisibility() != 0) {
                        obtainMainView.setImportantForAccessibility(4);
                    } else {
                        obtainMainView.setImportantForAccessibility(0);
                        bool = Boolean.TRUE;
                    }
                }
            }
        }
    }

    public void k() {
        if (obtainMainView() instanceof k) {
            ((k) obtainMainView()).c();
        }
    }

    /* access modifiers changed from: package-private */
    public void l() {
        Collections.sort(this.c, this.x);
    }

    /* access modifiers changed from: package-private */
    public void m() {
        Collections.sort(this.b, this.x);
        int size = this.b.size();
        for (int i2 = 0; i2 < size; i2++) {
            ((b) this.b.get(i2)).obtainMainView().bringToFront();
        }
    }

    public void onAppActive(IApp iApp) {
        b(iApp);
        BaseInfo.sCurrentAppOriginalAppid = iApp.obtainOriginalAppId();
        iApp.getActivity();
        if (this.m != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            if (iApp.obtainStatusBarMgr().isTemporaryFullScreen && !iApp.obtainStatusBarMgr().isImmersive && !iApp.isFullScreen() && !this.q) {
                layoutParams.topMargin = DeviceInfo.sStatusBarHeight;
            }
            this.m.setViewAsContentView(obtainMainView(), layoutParams);
        }
        Logger.d(Logger.MAIN_TAG, iApp.obtainAppId() + " onAppActive setContentView");
        a(obtainMainView());
        FeatureMessageDispatcher.dispatchMessage("app_open", 1);
    }

    public void onAppStart(IApp iApp) {
        this.q = false;
        this.r = false;
        if (iApp != null) {
            a(iApp);
        }
        obtainMainView().getViewTreeObserver().addOnGlobalLayoutListener(new i());
        obtainMainView().setBackgroundColor(-1);
        onAppActive(iApp);
    }

    public void onAppStop(IApp iApp) {
        onAppUnActive(iApp);
    }

    public void onAppUnActive(IApp iApp) {
        if (this.w != null) {
            throw null;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: io.dcloud.common.adapter.util.EventActionInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v11, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r6v0 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: type inference failed for: r6v15 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* JADX WARNING: type inference failed for: r6v18 */
    /* JADX WARNING: type inference failed for: r6v19 */
    /* JADX WARNING: type inference failed for: r6v20 */
    /* JADX WARNING: type inference failed for: r6v21 */
    /* JADX WARNING: type inference failed for: r6v22 */
    /* JADX WARNING: type inference failed for: r6v23 */
    /* JADX WARNING: type inference failed for: r6v24 */
    /* JADX WARNING: type inference failed for: r6v25 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onExecute(io.dcloud.common.DHInterface.ISysEventListener.SysEventType r13, java.lang.Object r14) {
        /*
            r12 = this;
            int[] r0 = io.dcloud.common.core.ui.a.C0016a.a
            int r13 = r13.ordinal()
            r13 = r0[r13]
            java.lang.String r0 = "p.runtime.launcher = '%s';"
            java.lang.String r1 = "p.runtime.arguments = %s;"
            java.lang.String r2 = "plus.device.imsi = ['%s'];"
            java.lang.String r3 = "Device"
            r4 = 2
            java.lang.String r5 = "javascript:"
            r6 = 0
            java.lang.String r7 = "try{if((window.__html5plus__&&__html5plus__.isReady?__html5plus__:navigator.plus&&navigator.plus.isReady?navigator.plus:window.plus).runtime)var p=window.__html5plus__&&__html5plus__.isReady?__html5plus__:navigator.plus&&navigator.plus.isReady?navigator.plus:window.plus; %s }catch(_){}"
            java.lang.String r8 = "javascript:(function(){if(!((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus)){window.__load__plus__&&window.__load__plus__();}var e = document.createEvent('HTMLEvents');var evt = '%s';e.initEvent(evt, false, true);/*console.log('dispatch ' + evt + ' event');*/document.dispatchEvent(e);})();"
            r9 = 0
            r10 = 1
            java.lang.String r11 = ""
            switch(r13) {
                case 1: goto L_0x038c;
                case 2: goto L_0x032c;
                case 3: goto L_0x02e4;
                case 4: goto L_0x0266;
                case 5: goto L_0x017f;
                case 6: goto L_0x0120;
                case 7: goto L_0x010f;
                case 8: goto L_0x00fe;
                case 9: goto L_0x00ed;
                case 10: goto L_0x0052;
                case 11: goto L_0x0041;
                case 12: goto L_0x0021;
                default: goto L_0x001f;
            }
        L_0x001f:
            goto L_0x03a0
        L_0x0021:
            io.dcloud.common.ui.blur.AppEventForBlurManager.onSplashclosed()
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "splashclosed"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            io.dcloud.common.adapter.util.EventActionInfo r13 = new io.dcloud.common.adapter.util.EventActionInfo
            r13.<init>(r14)
            io.dcloud.common.DHInterface.IApp r14 = r12.j
            android.app.Activity r14 = r14.getActivity()
            java.lang.String r0 = "onSplashclosed"
            io.dcloud.a.a(r14, r6, r0, r6)
        L_0x003e:
            r6 = r13
            goto L_0x03a0
        L_0x0041:
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "trimmemory"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>(r14)
            goto L_0x03a0
        L_0x0052:
            io.dcloud.common.DHInterface.IApp r13 = r12.j
            java.lang.String r13 = r13.obtainAppId()
            java.lang.String r13 = io.dcloud.common.util.BaseInfo.getLauncherData(r13)
            io.dcloud.common.DHInterface.IApp r2 = r12.j
            android.content.Intent r2 = r2.obtainWebAppIntent()
            java.lang.String r3 = "__webapp_reply__"
            boolean r2 = r2.getBooleanExtra(r3, r9)
            if (r2 == 0) goto L_0x006d
            java.lang.String r13 = "default"
            goto L_0x00bd
        L_0x006d:
            java.lang.String r14 = java.lang.String.valueOf(r14)
            io.dcloud.common.DHInterface.IApp r2 = r12.j
            r2.setRuntimeArgs(r14)
            java.lang.Object[] r14 = new java.lang.Object[r10]
            io.dcloud.common.DHInterface.IApp r2 = r12.j
            java.lang.String r2 = r2.obtainRuntimeArgs(r10)
            r14[r9] = r2
            java.lang.String r14 = io.dcloud.common.util.StringUtil.format(r1, r14)
            java.lang.Object[] r1 = new java.lang.Object[r10]
            r1[r9] = r13
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r0, r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r5)
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r9] = r14
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r7, r2)
            r1.append(r2)
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r9] = r0
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r7, r2)
            r1.append(r2)
            java.lang.String r11 = r1.toString()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r14)
            r1.append(r0)
            java.lang.String r6 = r1.toString()
        L_0x00bd:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r11)
            java.lang.Object[] r0 = new java.lang.Object[r4]
            java.lang.String r1 = "foreground"
            r0[r9] = r1
            r0[r10] = r13
            java.lang.String r1 = "javascript:(function(){if(!((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus)){window.__load__plus__&&window.__load__plus__();}var e = document.createEvent('HTMLEvents');var evt = '%s';e.initEvent(evt, false, true); e.active = '%s';/*console.log('dispatch ' + evt + ' event');*/document.dispatchEvent(e);})();"
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r1, r0)
            r14.append(r0)
            java.lang.String r11 = r14.toString()
            java.util.HashMap r14 = new java.util.HashMap
            r14.<init>()
            java.lang.String r0 = "active"
            r14.put(r0, r13)
            io.dcloud.common.adapter.util.EventActionInfo r13 = new io.dcloud.common.adapter.util.EventActionInfo
            java.lang.String r0 = "foreground"
            r13.<init>(r0, r6, r14)
            goto L_0x003e
        L_0x00ed:
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "background"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>(r14)
            goto L_0x03a0
        L_0x00fe:
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "keyboardhide"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>(r14)
            goto L_0x03a0
        L_0x010f:
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "keyboardshow"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>(r14)
            goto L_0x03a0
        L_0x0120:
            io.dcloud.common.DHInterface.IApp r13 = r12.j
            r13.updateScreenInfo(r10)
            android.content.Context r13 = r12.getContext()
            android.app.Activity r13 = (android.app.Activity) r13
            java.lang.Boolean r13 = io.dcloud.common.adapter.util.DeviceInfo.isSystemNightMode(r13)
            android.content.Context r14 = r12.getContext()
            java.lang.String r0 = "dc_dark_mode_"
            java.lang.String r1 = "dark_mode"
            java.lang.String r14 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r14, (java.lang.String) r0, (java.lang.String) r1)
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r14)
            if (r0 == 0) goto L_0x0147
            java.lang.String r14 = "DCLOUD_DARK_MODE"
            java.lang.String r14 = io.dcloud.common.adapter.util.AndroidResources.getMetaValue(r14)
        L_0x0147:
            java.lang.String r0 = "auto"
            boolean r14 = io.dcloud.common.util.PdrUtil.isEquals(r0, r14)
            if (r14 == 0) goto L_0x017e
            boolean r13 = r13.booleanValue()
            if (r13 == 0) goto L_0x0158
            java.lang.String r13 = "dark"
            goto L_0x015a
        L_0x0158:
            java.lang.String r13 = "light"
        L_0x015a:
            java.lang.Object[] r14 = new java.lang.Object[r4]
            java.lang.String r0 = "uistylechange"
            r14[r9] = r0
            r14[r10] = r13
            java.lang.String r0 = "javascript:!function(){(window.__html5plus__&&__html5plus__.isReady?__html5plus__:navigator.plus&&navigator.plus.isReady?navigator.plus:window.plus)||window.__load__plus__&&window.__load__plus__();var _=document.createEvent(\"HTMLEvents\");_.initEvent(\"%s\",!1,!0),_.uistyle=\"%s\",document.dispatchEvent(_)}();"
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r0, r14)
            java.util.HashMap r14 = new java.util.HashMap
            r14.<init>()
            java.lang.String r0 = "uistyle"
            r14.put(r0, r13)
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            java.lang.String r13 = "uistylechange"
            r6.<init>((java.lang.String) r13, (java.util.Map<java.lang.String, java.lang.Object>) r14)
            io.dcloud.common.util.AppRuntime.switchAllWebViewDarkMode()
            goto L_0x03a0
        L_0x017e:
            return r9
        L_0x017f:
            java.lang.String r13 = java.lang.String.valueOf(r14)
            io.dcloud.common.DHInterface.IApp r14 = r12.j
            r14.setRuntimeArgs(r13)
            android.app.Activity r13 = r12.getActivity()
            android.content.Intent r13 = r13.getIntent()
            java.lang.String r14 = "unimp_run_extra_info"
            java.lang.String r13 = io.dcloud.common.constant.IntentConst.obtainIntentStringExtra(r13, r14, r10)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r13)
            if (r2 != 0) goto L_0x01a1
            io.dcloud.common.DHInterface.IApp r2 = r12.j
            r2.setConfigProperty(r14, r13)
        L_0x01a1:
            android.app.Activity r13 = r12.getActivity()
            android.content.Intent r13 = r13.getIntent()
            java.lang.String r13 = io.dcloud.common.util.BaseInfo.getLaunchType(r13)
            io.dcloud.common.DHInterface.IApp r14 = r12.j
            java.lang.String r14 = r14.obtainAppId()
            io.dcloud.common.util.BaseInfo.putLauncherData(r14, r13)
            android.content.Context r14 = r12.getContext()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            io.dcloud.common.DHInterface.IApp r3 = r12.j
            java.lang.String r3 = r3.obtainAppId()
            r2.append(r3)
            java.lang.String r3 = "LAUNCHTYPE"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "pdr"
            java.lang.String r14 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r14, (java.lang.String) r3, (java.lang.String) r2)
            boolean r2 = android.text.TextUtils.isEmpty(r14)
            if (r2 == 0) goto L_0x01df
            java.lang.String r14 = "default"
        L_0x01df:
            java.lang.Object[] r2 = new java.lang.Object[r10]
            io.dcloud.common.DHInterface.IApp r3 = r12.j
            java.lang.String r3 = r3.obtainRuntimeArgs(r10)
            r2[r9] = r3
            java.lang.String r1 = io.dcloud.common.util.StringUtil.format(r1, r2)
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r9] = r13
            java.lang.String r13 = io.dcloud.common.util.StringUtil.format(r0, r2)
            java.lang.Object[] r0 = new java.lang.Object[r10]
            r0[r9] = r14
            java.lang.String r14 = "p.runtime.origin = '%s';"
            java.lang.String r14 = io.dcloud.common.util.StringUtil.format(r14, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r9] = r1
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r7, r2)
            r0.append(r2)
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r9] = r13
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r7, r2)
            r0.append(r2)
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r9] = r14
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r7, r2)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r10]
            java.lang.String r3 = "newintent"
            r0[r9] = r3
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r8, r0)
            r2.append(r0)
            java.lang.String r11 = r2.toString()
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "var p = plus;"
            r0.append(r2)
            r0.append(r1)
            r0.append(r13)
            r0.append(r14)
            java.lang.String r13 = r0.toString()
            java.lang.String r14 = "newintent"
            r6.<init>((java.lang.String) r14, (java.lang.String) r13)
            goto L_0x03a0
        L_0x0266:
            java.lang.String r13 = r12.i
            java.util.Locale r14 = java.util.Locale.ENGLISH
            java.lang.String r14 = r3.toLowerCase(r14)
            boolean r13 = io.dcloud.common.core.permission.PermissionControler.checkPermission((java.lang.String) r13, (java.lang.String) r14)
            if (r13 != 0) goto L_0x0275
            return r9
        L_0x0275:
            android.content.Context r13 = r12.getContext()
            java.lang.String r14 = io.dcloud.common.util.BaseInfo.PDR
            java.lang.String r0 = "last_notify_net_type"
            java.lang.String r13 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r13, (java.lang.String) r14, (java.lang.String) r0)
            java.lang.String r14 = io.dcloud.common.adapter.util.DeviceInfo.getNetWorkType()
            boolean r1 = io.dcloud.common.util.PdrUtil.isEquals(r13, r14)
            if (r1 != 0) goto L_0x02e3
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "netchange last_net_type:"
            r1.append(r3)
            r1.append(r13)
            java.lang.String r13 = ";cur_net_type:"
            r1.append(r13)
            r1.append(r14)
            java.lang.String r13 = r1.toString()
            java.lang.String r1 = "NetCheckReceiver"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r1, (java.lang.String) r13)
            android.content.Context r13 = r12.getContext()
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.PDR
            io.dcloud.common.adapter.util.SP.setBundleData(r13, r1, r0, r14)
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "netchange"
            r13[r9] = r14
            java.lang.String r13 = io.dcloud.common.util.StringUtil.format(r8, r13)
            java.lang.Object[] r14 = new java.lang.Object[r10]
            java.lang.String r0 = io.dcloud.common.adapter.util.DeviceInfo.getUpdateIMSI()
            r14[r9] = r0
            java.lang.String r14 = io.dcloud.common.util.StringUtil.format(r2, r14)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            r0.append(r14)
            r0.append(r13)
            java.lang.String r11 = r0.toString()
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            java.lang.String r13 = "netchange"
            r6.<init>((java.lang.String) r13, (java.lang.String) r14)
            goto L_0x03a0
        L_0x02e3:
            return r9
        L_0x02e4:
            java.lang.String r13 = r12.i
            java.util.Locale r14 = java.util.Locale.ENGLISH
            java.lang.String r14 = r3.toLowerCase(r14)
            boolean r13 = io.dcloud.common.core.permission.PermissionControler.checkPermission((java.lang.String) r13, (java.lang.String) r14)
            if (r13 == 0) goto L_0x03a0
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = io.dcloud.common.adapter.util.DeviceInfo.getUpdateIMSI()
            r13[r9] = r14
            java.lang.String r13 = io.dcloud.common.util.StringUtil.format(r2, r13)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r5)
            r14.append(r13)
            java.lang.String r14 = r14.toString()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r14)
            java.lang.Object[] r14 = new java.lang.Object[r10]
            java.lang.String r1 = "imsichange"
            r14[r9] = r1
            java.lang.String r14 = io.dcloud.common.util.StringUtil.format(r8, r14)
            r0.append(r14)
            java.lang.String r11 = r0.toString()
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>((java.lang.String) r1, (java.lang.String) r13)
            goto L_0x03a0
        L_0x032c:
            java.lang.String r13 = r12.i
            io.dcloud.common.util.AppStatus.setAppStatus(r13, r10)
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "resume"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            boolean r13 = io.dcloud.common.adapter.util.AndroidResources.sIMEAlive
            if (r13 != 0) goto L_0x0346
            io.dcloud.common.adapter.ui.CustomeizedInputConnection r13 = io.dcloud.common.adapter.ui.AdaWebview.sCustomeizedInputConnection
            if (r13 == 0) goto L_0x0346
            r13.closeRecordView()
        L_0x0346:
            java.util.Calendar r13 = java.util.Calendar.getInstance()
            java.util.Date r0 = new java.util.Date
            long r1 = r12.p
            r0.<init>(r1)
            r13.setTime(r0)
            java.util.Calendar r0 = java.util.Calendar.getInstance()
            java.util.Date r1 = new java.util.Date
            long r2 = java.lang.System.currentTimeMillis()
            r1.<init>(r2)
            r0.setTime(r1)
            r1 = 5
            int r13 = r13.get(r1)
            int r1 = r0.get(r1)
            if (r13 == r1) goto L_0x0386
            r1 = 0
            io.dcloud.common.util.BaseInfo.run5appEndTime = r1
            io.dcloud.common.DHInterface.IApp r13 = r12.j
            long r1 = r12.p
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r2 = "commit"
            r13.setConfigProperty(r2, r1)
            long r0 = r0.getTimeInMillis()
            r12.p = r0
        L_0x0386:
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>(r14)
            goto L_0x03a0
        L_0x038c:
            java.lang.String r13 = r12.i
            io.dcloud.common.util.AppStatus.setAppStatus(r13, r4)
            java.lang.Object[] r13 = new java.lang.Object[r10]
            java.lang.String r14 = "pause"
            r13[r9] = r14
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r8, r13)
            io.dcloud.common.adapter.util.EventActionInfo r6 = new io.dcloud.common.adapter.util.EventActionInfo
            r6.<init>(r14)
        L_0x03a0:
            java.util.ArrayList<io.dcloud.common.core.ui.b> r13 = r12.c
            if (r13 == 0) goto L_0x03d3
            boolean r13 = android.text.TextUtils.isEmpty(r11)
            if (r13 != 0) goto L_0x03d3
            java.util.ArrayList<io.dcloud.common.core.ui.b> r13 = r12.c
            int r13 = r13.size()
            int r13 = r13 - r10
        L_0x03b1:
            if (r13 < 0) goto L_0x03d3
            java.util.ArrayList<io.dcloud.common.core.ui.b> r14 = r12.c
            java.lang.Object r14 = r14.get(r13)
            io.dcloud.common.core.ui.b r14 = (io.dcloud.common.core.ui.b) r14
            io.dcloud.common.DHInterface.IWebview r14 = r14.obtainWebView()
            if (r14 == 0) goto L_0x03d0
            boolean r0 = r14 instanceof io.dcloud.common.adapter.ui.AdaUniWebView
            if (r0 == 0) goto L_0x03cd
            if (r6 == 0) goto L_0x03cd
            io.dcloud.common.adapter.ui.AdaUniWebView r14 = (io.dcloud.common.adapter.ui.AdaUniWebView) r14
            r14.fireEvent(r6)
            goto L_0x03d0
        L_0x03cd:
            r14.loadUrl(r11)
        L_0x03d0:
            int r13 = r13 + -1
            goto L_0x03b1
        L_0x03d3:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.a.onExecute(io.dcloud.common.DHInterface.ISysEventListener$SysEventType, java.lang.Object):boolean");
    }

    public void onRootViewGlobalLayout(View view) {
        if (!isDisposed()) {
            if (AdaWebview.ScreemOrientationChangedNeedLayout) {
                AdaWebview.ScreemOrientationChangedNeedLayout = false;
                this.j.updateScreenInfo(3);
            }
            int width = view.getWidth();
            int height = view.getHeight();
            if (Math.abs(width - this.j.getInt(0)) <= 100) {
                int i2 = height - this.j.getInt(1);
                if (!this.j.isVerticalScreen()) {
                    i2 = width - this.j.getInt(0);
                }
                if (i2 != 0) {
                    this.j.updateScreenInfo(3);
                }
                if (view.getHeight() != this.t && view.getHeight() == this.j.getInt(1)) {
                    PlatformUtil.RESET_H_W();
                    if (!this.u) {
                        BaseInfo.sFullScreenChanged = true;
                    }
                    this.u = false;
                }
                this.t = view.getHeight();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void reload(boolean z2) {
        int size = this.c.size() - 1;
        while (size >= 0) {
            IWebview obtainWebView = this.c.get(size).obtainWebView();
            if (obtainWebView != null) {
                obtainWebView.reload();
            }
            if (z2) {
                size--;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void b(b bVar) {
        int i2;
        b bVar2;
        b bVar3 = bVar;
        byte b2 = bVar.getAnimOptions().mOption;
        byte b3 = 4;
        if ((!bVar3.i && !bVar3.inStack && bVar.obtainFrameOptions().hasTransparentValue()) || bVar.obtainMainView().getVisibility() != 0) {
            if (b2 == 3 || b2 == 1) {
                ArrayList<b> arrayList = new ArrayList<>();
                a(arrayList, bVar3);
                bVar3.h = arrayList;
                if (b2 != 1) {
                    return;
                }
            } else if (b2 != 2) {
                if (b2 == 4 || b2 == 0) {
                    ArrayList<b> arrayList2 = new ArrayList<>();
                    b(arrayList2, bVar3);
                    bVar3.g = arrayList2;
                    return;
                }
            } else {
                return;
            }
        }
        ArrayList<b> arrayList3 = new ArrayList<>();
        ArrayList<b> arrayList4 = new ArrayList<>();
        Region region = new Region(2);
        int size = this.c.size() - 1;
        while (size >= 0) {
            b bVar4 = this.c.get(size);
            if (bVar4.obtainMainView().getVisibility() == 0) {
                if (!bVar4.isChildOfFrameView) {
                    bVar4.i();
                    ViewOptions obtainFrameOptions = bVar4.obtainFrameOptions();
                    if (b2 == b3 || b2 == 0) {
                        b bVar5 = bVar4;
                        i2 = size;
                        if ((!a(region) && region.getFillScreenCounter() <= 2) || bVar3 == bVar5) {
                            if (!obtainFrameOptions.hasTransparentValue()) {
                                if (a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                    if (a(arrayList3, bVar5)) {
                                        break;
                                    }
                                }
                            }
                            b(arrayList4, bVar5);
                        } else if (a(arrayList3, bVar5)) {
                            break;
                        }
                    } else if (b2 == 2) {
                        ViewOptions obtainFrameOptions_Animate = bVar4.obtainFrameOptions_Animate();
                        if (bVar4 == bVar3 && obtainFrameOptions_Animate != null) {
                            obtainFrameOptions = obtainFrameOptions_Animate;
                        }
                        if (a(region)) {
                            if (a(arrayList3, bVar4)) {
                                break;
                            }
                        } else {
                            if (!obtainFrameOptions.hasTransparentValue()) {
                                bVar2 = bVar4;
                                i2 = size;
                                if (a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                    if (a(arrayList3, bVar2)) {
                                        break;
                                    }
                                }
                            } else {
                                bVar2 = bVar4;
                                i2 = size;
                            }
                            b(arrayList4, bVar2);
                        }
                    } else {
                        b bVar6 = bVar4;
                        i2 = size;
                        if (b2 == 3 || b2 == 1) {
                            if (bVar6 == bVar3) {
                                a(arrayList3, bVar6);
                            } else if (a(region)) {
                                a(arrayList3, bVar6);
                            } else {
                                if (!obtainFrameOptions.hasTransparentValue()) {
                                    if (a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                        if (a(arrayList3, bVar6)) {
                                            break;
                                        }
                                    }
                                }
                                b(arrayList4, bVar6);
                            }
                        }
                    }
                }
                i2 = size;
            } else {
                i2 = size;
                if (a(arrayList3, bVar4)) {
                    break;
                }
            }
            size = i2 - 1;
            b3 = 4;
        }
        bVar3.h = arrayList3;
        bVar3.g = arrayList4;
    }

    /* access modifiers changed from: package-private */
    public int c(b bVar) {
        return this.b.indexOf(bVar);
    }

    /* access modifiers changed from: package-private */
    public boolean d(b bVar) {
        Iterator<b> it = this.c.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (bVar != next && !next.isChildOfFrameView && next.obtainMainView().getVisibility() == 0) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Stack<b> e() {
        return this.b;
    }

    public void f(b bVar) {
        AnimOptions animOptions = bVar.getAnimOptions();
        animOptions.mOption = this.z;
        animOptions.mAnimType = this.A;
        animOptions.mAnimType_close = this.B;
    }

    /* access modifiers changed from: package-private */
    public b g() {
        return a(4);
    }

    private void a(b bVar, int i2, int i3) {
        Logger.d("DHAppRootView.pushFrameView" + bVar);
        this.b.insertElementAt(bVar, i2);
        addFrameItem((AdaFrameItem) bVar, i3);
    }

    public void c() {
        Logger.d(Logger.ANIMATION_TAG, "AppRootView dispatchConfigurationChanged(横竖屏切换、全屏非全屏切换、虚拟返回键栏隐藏显示) 引发调整栈窗口");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        a((ArrayList<b>) arrayList2, (ArrayList<b>) arrayList);
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            b bVar = (b) it.next();
            boolean contains = this.b.contains(bVar);
            l lVar = bVar.mWindowMgr;
            if (lVar != null) {
                lVar.processEvent(IMgr.MgrType.WindowMgr, 8, bVar);
            }
            bVar.n = !contains;
        }
        MessageHandler.sendMessage(new d(arrayList2), (Object) null);
    }

    /* access modifiers changed from: package-private */
    public int e(b bVar) {
        int i2;
        int i3;
        int i4;
        int indexOf = this.c.indexOf(bVar);
        int size = this.b.size() - 1;
        while (true) {
            if (size < 0) {
                i3 = 0;
                break;
            }
            b bVar2 = (b) this.b.get(size);
            int indexOf2 = this.c.indexOf(bVar2);
            if (indexOf2 >= 0 && indexOf > indexOf2 && bVar2.getFrameType() != 3 && !bVar2.isTabItem()) {
                i3 = size + 1;
                break;
            }
            size--;
        }
        if (i3 != 0) {
            ViewGroup obtainMainViewGroup = obtainMainViewGroup();
            int childCount = obtainMainViewGroup.getChildCount();
            i4 = i3;
            int i5 = 0;
            for (i2 = 0; i2 < childCount; i2++) {
                View childAt = obtainMainViewGroup.getChildAt(i2);
                if ((childAt instanceof AbsoluteLayout) || (childAt instanceof io.dcloud.common.ui.d)) {
                    i5++;
                } else {
                    i4++;
                }
                if (i5 >= i3) {
                    break;
                }
            }
        } else {
            i4 = i3;
        }
        a(bVar, i3, i4);
        if (BaseInfo.isUniAppAppid(bVar.obtainApp())) {
            if (bVar.obtainWebView() != null) {
                bVar.obtainWebView().setIWebViewFocusable(true);
            }
            bVar.changeWebParentViewRect();
        }
        return i3;
    }

    /* access modifiers changed from: package-private */
    public ArrayList<b> d() {
        return this.c;
    }

    /* access modifiers changed from: package-private */
    public void i(b bVar) {
        if (bVar.getParentFrameItem() != null) {
            bVar.getParentFrameItem().sortNativeViewBringToFront();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reload(java.lang.String r19) {
        /*
            r18 = this;
            r0 = r18
            java.lang.String r1 = "\\|"
            r2 = r19
            java.lang.String[] r1 = r2.split(r1)
            int r2 = r1.length
            r3 = 10
            r4 = 3
            r5 = 2
            java.lang.String r6 = "weex,io.dcloud.feature.weex.WeexFeature"
            r7 = 4
            r8 = 0
            r9 = 1
            if (r2 <= 0) goto L_0x0045
            boolean r2 = io.dcloud.common.util.BaseInfo.isUniNViewBackgroud()
            if (r2 == 0) goto L_0x0045
            java.util.ArrayList<io.dcloud.common.core.ui.b> r2 = r0.c
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x0045
            java.util.ArrayList<io.dcloud.common.core.ui.b> r2 = r0.c
            java.lang.Object r2 = r2.get(r8)
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2
            io.dcloud.common.core.ui.l r10 = r2.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r11 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            java.lang.Object[] r12 = new java.lang.Object[r7]
            io.dcloud.common.DHInterface.IApp r2 = r2.obtainApp()
            r12[r8] = r2
            r12[r9] = r6
            java.lang.String r2 = "updateServiceReload"
            r12[r5] = r2
            r2 = 0
            r12[r4] = r2
            r10.processEvent(r11, r3, r12)
        L_0x0045:
            java.util.ArrayList<io.dcloud.common.core.ui.b> r2 = r0.c
            int r2 = r2.size()
            int r2 = r2 - r9
        L_0x004c:
            if (r2 < 0) goto L_0x00cc
            java.util.ArrayList<io.dcloud.common.core.ui.b> r10 = r0.c
            java.lang.Object r10 = r10.get(r2)
            io.dcloud.common.core.ui.b r10 = (io.dcloud.common.core.ui.b) r10
            io.dcloud.common.DHInterface.IWebview r11 = r10.obtainWebView()
            io.dcloud.common.adapter.util.ViewOptions r12 = r10.obtainFrameOptions()
            org.json.JSONObject r12 = r12.mUniNViewJson
            if (r12 == 0) goto L_0x0064
            r12 = 1
            goto L_0x0065
        L_0x0064:
            r12 = 0
        L_0x0065:
            if (r11 == 0) goto L_0x00c2
            r13 = 0
        L_0x0068:
            int r14 = r1.length
            if (r13 >= r14) goto L_0x00c2
            r14 = r1[r13]
            if (r12 == 0) goto L_0x009c
            java.lang.String r15 = ".js"
            boolean r15 = r14.endsWith(r15)
            if (r15 == 0) goto L_0x0097
            java.lang.Object[] r15 = new java.lang.Object[r9]
            r15[r8] = r14
            io.dcloud.common.core.ui.l r14 = r10.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            java.lang.Object[] r4 = new java.lang.Object[r7]
            io.dcloud.common.DHInterface.IApp r17 = r10.obtainApp()
            r4[r8] = r17
            r4[r9] = r6
            java.lang.String r17 = "updateReload"
            r4[r5] = r17
            r16 = 3
            r4[r16] = r15
            r15 = 10
            r14.processEvent(r3, r15, r4)
            goto L_0x00bc
        L_0x0097:
            r15 = 10
            r16 = 3
            goto L_0x00bc
        L_0x009c:
            r15 = 10
            r16 = 3
            java.lang.String r3 = r11.obtainUrl()
            boolean r3 = r3.startsWith(r14)
            if (r3 == 0) goto L_0x00ae
            r11.reload()
            goto L_0x00c6
        L_0x00ae:
            boolean r3 = r11 instanceof io.dcloud.common.core.ui.TabBarWebview
            if (r3 == 0) goto L_0x00bc
            r3 = r11
            io.dcloud.common.core.ui.TabBarWebview r3 = (io.dcloud.common.core.ui.TabBarWebview) r3
            boolean r3 = r3.checkUrlToReload(r14)
            if (r3 == 0) goto L_0x00bc
            goto L_0x00c6
        L_0x00bc:
            int r13 = r13 + 1
            r3 = 10
            r4 = 3
            goto L_0x0068
        L_0x00c2:
            r15 = 10
            r16 = 3
        L_0x00c6:
            int r2 = r2 + -1
            r3 = 10
            r4 = 3
            goto L_0x004c
        L_0x00cc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.a.reload(java.lang.String):void");
    }

    class k {
        int a = 0;
        Vector<b> b = new Vector<>();
        private boolean c = false;

        k() {
        }

        /* access modifiers changed from: package-private */
        public void a(b bVar) {
            this.b.add(bVar);
            int i = this.a + 1;
            this.a = i;
            if (i > 1) {
                this.c = true;
                return;
            }
            this.a = 1;
            this.c = false;
        }

        /* access modifiers changed from: package-private */
        public void b(b bVar) {
            this.b.remove(bVar);
            this.a--;
        }

        /* access modifiers changed from: package-private */
        public int a() {
            return this.a;
        }
    }

    private void a(ArrayList<b> arrayList, ArrayList<b> arrayList2) {
        Region region = new Region(2);
        for (int size = this.c.size() - 1; size >= 0; size--) {
            b bVar = this.c.get(size);
            if (bVar.obtainMainView().getVisibility() == 0) {
                ViewOptions obtainFrameOptions = bVar.obtainFrameOptions();
                if (bVar.isChildOfFrameView) {
                    continue;
                } else if (bVar.f) {
                    a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height);
                    b(arrayList, bVar);
                } else {
                    bVar.i();
                    if (!a(region)) {
                        if (!obtainFrameOptions.hasTransparentValue()) {
                            if (a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height)) {
                                if (a(arrayList2, bVar)) {
                                    return;
                                }
                            }
                        }
                        b(arrayList, bVar);
                    } else if (a(arrayList2, bVar)) {
                        return;
                    }
                }
            } else if (a(arrayList2, bVar)) {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void h(b bVar) {
        ArrayList<AdaFrameItem> arrayList = bVar.getParentFrameItem().mChildArrayList;
        if (arrayList.size() > 1) {
            Collections.sort(arrayList, this.y);
            ArrayList arrayList2 = new ArrayList();
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                AdaFrameItem next = it.next();
                if (next.obtainMainView() == null) {
                    arrayList2.add(next);
                } else if (!(next.obtainMainView() instanceof WebParentView)) {
                    next.obtainMainView().bringToFront();
                }
            }
            if (arrayList2.size() > 0) {
                arrayList.removeAll(arrayList2);
            }
        }
        i(bVar);
    }

    /* access modifiers changed from: package-private */
    public boolean a(Region region) {
        boolean quickContains = region.quickContains(0, 0, this.j.getInt(0), this.j.getInt(1));
        if (region.fillWholeScreen()) {
            return quickContains;
        }
        if (!quickContains) {
            return quickContains;
        }
        region.setEmpty();
        region.count();
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean a(ArrayList<b> arrayList, b bVar) {
        arrayList.add(bVar);
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean a(Region region, int i2, int i3, int i4, int i5) {
        int i6 = i2 + i4;
        int i7 = i3 + i5;
        boolean quickContains = region.quickContains(i2, i3, i6, i7);
        if (!quickContains) {
            region.op(i2, i3, i6, i7, Region.Op.UNION);
        }
        return quickContains;
    }

    /* access modifiers changed from: package-private */
    public void a(b bVar) {
        Logger.d("DHAppRootView.closeFrameView pFrameView=" + bVar);
        bVar.onDestroy();
        removeFrameItem(bVar);
        System.gc();
    }

    /* access modifiers changed from: package-private */
    public void a(IApp iApp) {
        if (Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_WAITING))) {
            obtainMainView().postDelayed(new e(), 100);
        }
    }

    /* access modifiers changed from: package-private */
    public b a(int i2) {
        ArrayList<b> arrayList = this.c;
        if (arrayList != null) {
            Iterator<b> it = arrayList.iterator();
            while (it.hasNext()) {
                b next = it.next();
                if (next.getFrameType() == i2) {
                    return next;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void a(a aVar, b bVar, int i2, boolean z2, int i3) {
        if (bVar != null) {
            Logger.d("approotview", "closeSplashScreen0 delay=" + i2 + ";autoClose=" + z2 + ";mAppid" + this.i);
            if (bVar.obtainMainView() != null) {
                MessageHandler.sendMessage(new f(z2, aVar, i3), (long) Math.max(i2, 150), bVar);
                return;
            }
            Logger.d("approotview", "closeSplashScreen2;autoClose;mAppid" + this.i);
            a(aVar, i3);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(a aVar, int i2) {
        Log.e("Html5Plus-SplashClosed", System.currentTimeMillis() + "");
        boolean z2 = i2 > 10;
        Log.e(Logger.MAIN_TAG, "closeSplashScreen0 appid=" + this.i + ";" + z2 + ";closeSplashDid=" + this.q);
        if (aVar != null && !this.q) {
            if (!this.s) {
                IActivityHandler iActivityHandler = this.m;
                boolean z3 = iActivityHandler != null ? !iActivityHandler.hasAdService() : true;
                IActivityHandler iActivityHandler2 = this.m;
                if (iActivityHandler2 != null) {
                    if (!iActivityHandler2.hasAdService()) {
                        Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.feature.gg.dcloud.ADHandler", "SplashAdIsEnable", (Object) null, new Class[]{Context.class}, new Object[]{aVar.getContext()});
                        if ((invokeMethod instanceof Boolean) && ((Boolean) invokeMethod).booleanValue()) {
                            long abs = Math.abs(System.currentTimeMillis() - BaseInfo.splashCreateTime);
                            if (abs < 2500) {
                                this.s = true;
                                MessageHandler.postDelayed(new g(aVar, i2), 2500 - abs);
                                return;
                            }
                        }
                    }
                    if (z3) {
                        this.m.closeAppStreamSplash(this.i);
                    }
                    BaseInfo.setLoadingLaunchePage(false, "closeSplashScreen0");
                    try {
                        ((FrameLayout.LayoutParams) obtainMainView().getLayoutParams()).topMargin = 0;
                    } catch (Exception e2) {
                        Logger.e("Exception", "e.getMessage()==" + e2.getMessage());
                    }
                    if (z3) {
                        this.j.onSplashClosed();
                    } else {
                        this.m.setSplashCloseListener(this.i, new h());
                    }
                } else {
                    IApp iApp = this.j;
                    if (iApp != null) {
                        iApp.diyStatusBarState();
                    }
                }
                BaseInfo.run5appEndTime = TestUtil.getUseTime(AbsoluteConst.RUN_5AP_TIME_KEY, "");
                TestUtil.delete(AbsoluteConst.RUN_5AP_TIME_KEY);
                this.j.setConfigProperty("commit", String.valueOf(this.p));
                IOnCreateSplashView onCreateSplashView = this.j.getOnCreateSplashView();
                if (onCreateSplashView != null) {
                    onCreateSplashView.onCloseSplash();
                }
                DCKeyboardManager.getInstance().dhAppRootIsReady(this);
                BaseInfo.splashCloseTime = System.currentTimeMillis();
                IApp iApp2 = this.j;
                if ((iApp2 != null && z3) || this.r) {
                    iApp2.callSysEventListener(ISysEventListener.SysEventType.onSplashclosed, this);
                }
            } else {
                return;
            }
        }
        this.q = true;
    }

    public Object a(View view, ICallBack iCallBack) {
        if (!AndroidResources.sIMEAlive) {
            return iCallBack.onCallBack(-1, (Object) null);
        }
        DeviceInfo.hideIME(obtainMainView());
        this.v.add(iCallBack);
        return null;
    }

    /* access modifiers changed from: package-private */
    public void b(ArrayList<b> arrayList, b bVar) {
        arrayList.add(bVar);
    }

    private void a(ArrayList<b> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i2 = this.j.getInt(0);
        Iterator<b> it = arrayList.iterator();
        while (it.hasNext()) {
            b next = it.next();
            ViewOptions obtainFrameOptions = next.obtainFrameOptions();
            int i3 = obtainFrameOptions.width;
            if (i3 == -1) {
                i3 = i2;
            }
            int i4 = obtainFrameOptions.left;
            if (i4 + i3 <= 0 || i4 >= i2 || obtainFrameOptions.right + i3 <= 0) {
                arrayList2.add(next);
            }
        }
        arrayList.removeAll(arrayList2);
    }

    public void b() {
        Logger.d(this.i + " clearFrameView");
        ArrayList<b> arrayList = this.c;
        if (arrayList != null) {
            int size = arrayList.size();
            b[] bVarArr = new b[size];
            this.c.toArray(bVarArr);
            for (int i2 = 0; i2 < size; i2++) {
                try {
                    bVarArr[i2].onDestroy();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            this.c.clear();
        }
        clearView();
        Stack<b> stack = this.b;
        if (stack != null) {
            stack.clear();
        }
    }

    public void a(IFrameView iFrameView, ArrayList<b> arrayList) {
        if (this.c.contains(iFrameView)) {
            int indexOf = this.c.indexOf(iFrameView);
            if (this.c != null) {
                com.dcloud.android.graphics.Region region = new com.dcloud.android.graphics.Region();
                int i2 = indexOf - 1;
                while (i2 >= 0) {
                    b bVar = this.c.get(i2);
                    if (!bVar.isChildOfFrameView && bVar.obtainMainView().getVisibility() == 0) {
                        arrayList.add(bVar);
                        ViewOptions obtainFrameOptions = bVar.obtainFrameOptions();
                        a(region, obtainFrameOptions.left, obtainFrameOptions.top, obtainFrameOptions.width, obtainFrameOptions.height);
                    }
                    if (!a(region)) {
                        i2--;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private void b(IApp iApp) {
        iApp.setFullScreen(PdrUtil.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_FULLSCREEN), false, false));
    }

    public void a(b bVar, b bVar2) {
        AnimOptions animOptions = bVar.getAnimOptions();
        this.z = animOptions.mOption;
        animOptions.mOption = bVar2.getAnimOptions().mOption;
        this.A = animOptions.mAnimType;
        animOptions.mAnimType = bVar2.getAnimOptions().mAnimType;
        this.B = animOptions.mAnimType_close;
        animOptions.mAnimType_close = bVar2.getAnimOptions().mAnimType_close;
    }

    public DHImageView a(b bVar, int i2, boolean z2) {
        boolean z3;
        DHImageView dHImageView;
        long currentTimeMillis = System.currentTimeMillis();
        l lVar = (l) obtainMainView();
        if (bVar.mNativeView != null) {
            if (this.C == null) {
                this.C = lVar.getLeftImageView();
            }
            if (bVar.mNativeView.isAnimate()) {
                this.C.removeNativeView();
                bVar.mNativeView = null;
            } else {
                if (this.C.getParent() != lVar) {
                    if (this.C.getParent() != null) {
                        ((ViewGroup) this.C.getParent()).removeView(this.C);
                    }
                    lVar.addView(this.C);
                }
                this.C.addNativeView(bVar, bVar.mNativeView);
                this.C.setImageBitmap((Bitmap) null);
                this.C.bringToFront();
                this.C.setVisibility(0);
                return this.C;
            }
        }
        if (a((ViewGroup) lVar)) {
            DHImageView dHImageView2 = this.C;
            if (dHImageView2 != null) {
                dHImageView2.clear();
                this.C = null;
            }
            return null;
        }
        Bitmap bitmap = bVar.mSnapshot;
        if (bitmap != null) {
            z3 = false;
        } else if (1 != i2 || (dHImageView = this.C) == null || dHImageView.getBitmap() == null || this.C.getTag() == null || bVar.hashCode() != ((Integer) this.C.getTag()).intValue()) {
            bitmap = PlatformUtil.captureView(bVar.obtainMainView());
            z3 = true;
        } else {
            if (this.C.getParent() != lVar) {
                if (this.C.getParent() != null) {
                    ((ViewGroup) this.C.getParent()).removeView(this.C);
                }
                lVar.addView(this.C);
            }
            this.C.removeNativeView();
            this.C.bringToFront();
            this.C.setVisibility(0);
            return this.C;
        }
        if (bitmap == null || PlatformUtil.isWhiteBitmap(bitmap)) {
            DHImageView dHImageView3 = this.C;
            if (dHImageView3 != null) {
                dHImageView3.clear();
                this.C = null;
            }
        } else {
            if (this.C == null) {
                this.C = lVar.getLeftImageView();
            }
            if (this.C.getParent() != lVar) {
                if (this.C.getParent() != null) {
                    ((ViewGroup) this.C.getParent()).removeView(this.C);
                }
                lVar.addView(this.C);
            }
            this.C.bringToFront();
            this.C.setImageBitmap(bitmap);
            this.C.removeNativeView();
            this.C.setVisibility(0);
        }
        DHImageView dHImageView4 = this.C;
        if (dHImageView4 != null) {
            if (dHImageView4.isSlipping()) {
                return null;
            }
            this.C.refreshImagerView();
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("==============B截图耗时=");
        long j2 = currentTimeMillis2 - currentTimeMillis;
        sb.append(j2);
        Logger.i("mabo", sb.toString());
        if (j2 >= ((long) BaseInfo.sTimeoutCapture)) {
            int i3 = BaseInfo.sTimeOutCount + 1;
            BaseInfo.sTimeOutCount = i3;
            if (i3 > BaseInfo.sTimeOutMax) {
                BaseInfo.sAnimationCaptureB = false;
            }
        } else if (z3) {
            BaseInfo.sTimeOutCount = 0;
        }
        return this.C;
    }

    public boolean a(ViewGroup viewGroup) {
        DHImageView dHImageView = this.C;
        return (dHImageView == null || dHImageView.mBitmapHeight <= 0 || ((long) viewGroup.getHeight()) == this.C.mBitmapHeight) ? false : true;
    }
}
