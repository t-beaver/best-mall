package io.dcloud.feature.weex;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import com.taobao.weex.WXSDKInstance;

public final class WXAnalyzerDelegate {
    private static boolean ENABLE = false;
    private Object mWXAnalyzer;

    public WXAnalyzerDelegate(Context context) {
        if (ENABLE && context != null) {
            try {
                this.mWXAnalyzer = Class.forName("com.taobao.weex.analyzer.WeexDevOptions").getDeclaredConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate() {
        Object obj = this.mWXAnalyzer;
        if (obj != null) {
            try {
                obj.getClass().getDeclaredMethod("onCreate", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onStart() {
        Object obj = this.mWXAnalyzer;
        if (obj != null) {
            try {
                obj.getClass().getDeclaredMethod("onStart", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        Object obj = this.mWXAnalyzer;
        if (obj != null) {
            try {
                obj.getClass().getDeclaredMethod("onResume", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onPause() {
        Object obj = this.mWXAnalyzer;
        if (obj != null) {
            try {
                obj.getClass().getDeclaredMethod("onPause", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onStop() {
        Object obj = this.mWXAnalyzer;
        if (obj != null) {
            try {
                obj.getClass().getDeclaredMethod("onStop", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        Object obj = this.mWXAnalyzer;
        if (obj != null) {
            try {
                obj.getClass().getDeclaredMethod("onDestroy", new Class[0]).invoke(this.mWXAnalyzer, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onWeexRenderSuccess(WXSDKInstance wXSDKInstance) {
        Object obj = this.mWXAnalyzer;
        if (obj != null && wXSDKInstance != null) {
            try {
                obj.getClass().getDeclaredMethod("onWeexRenderSuccess", new Class[]{WXSDKInstance.class}).invoke(this.mWXAnalyzer, new Object[]{wXSDKInstance});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        Object obj = this.mWXAnalyzer;
        if (obj == null) {
            return false;
        }
        try {
            return ((Boolean) obj.getClass().getDeclaredMethod("onKeyUp", new Class[]{Integer.TYPE, KeyEvent.class}).invoke(this.mWXAnalyzer, new Object[]{Integer.valueOf(i), keyEvent})).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (this.mWXAnalyzer != null) {
            if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
                try {
                    this.mWXAnalyzer.getClass().getDeclaredMethod("onException", new Class[]{WXSDKInstance.class, String.class, String.class}).invoke(this.mWXAnalyzer, new Object[]{wXSDKInstance, str, str2});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public View onWeexViewCreated(WXSDKInstance wXSDKInstance, View view) {
        Object obj = this.mWXAnalyzer;
        if (obj == null || wXSDKInstance == null || view == null) {
            return null;
        }
        try {
            return (View) obj.getClass().getDeclaredMethod("onWeexViewCreated", new Class[]{WXSDKInstance.class, View.class}).invoke(this.mWXAnalyzer, new Object[]{wXSDKInstance, view});
        } catch (Exception e) {
            e.printStackTrace();
            return view;
        }
    }
}
