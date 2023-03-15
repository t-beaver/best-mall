package com.taobao.weex.ui.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.taobao.weex.ui.view.WXEditText;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConfirmBar {
    private static ConfirmBar instance;
    /* access modifiers changed from: private */
    public List<WXComponent> editText = new ArrayList();
    private int height;
    /* access modifiers changed from: private */
    public AtomicReference<ISysEventListener> listener = null;
    /* access modifiers changed from: private */
    public ViewGroup rootView;
    /* access modifiers changed from: private */
    public RelativeLayout rtl;

    static ConfirmBar getInstance() {
        if (instance == null) {
            synchronized (ConfirmBar.class) {
                if (instance == null) {
                    instance = new ConfirmBar();
                }
            }
        }
        return instance;
    }

    /* access modifiers changed from: private */
    public void showConfirm(boolean z, int i) {
        try {
            if (this.rtl != null) {
                View view = null;
                ViewGroup viewGroup = this.rootView;
                if (viewGroup != null) {
                    view = viewGroup.findViewWithTag("AppRootView");
                }
                if (z) {
                    if (this.rootView != null) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.bottomMargin = this.height;
                        view.setLayoutParams(layoutParams);
                    }
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.rtl.getLayoutParams();
                    layoutParams2.topMargin = i;
                    this.rtl.setLayoutParams(layoutParams2);
                    this.rtl.setVisibility(0);
                    this.rtl.bringToFront();
                    return;
                }
                this.rtl.setVisibility(4);
                if (view != null) {
                    FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layoutParams3.bottomMargin = 0;
                    view.setLayoutParams(layoutParams3);
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void addComponent(WXComponent wXComponent) {
        try {
            this.editText.add(wXComponent);
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void removeComponent(WXComponent wXComponent) {
        try {
            this.editText.remove(wXComponent);
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void createConfirmBar(final Context context, IApp iApp) {
        try {
            if (this.listener == null) {
                AtomicReference<ISysEventListener> atomicReference = new AtomicReference<>(new ISysEventListener() {
                    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                        if (sysEventType == ISysEventListener.SysEventType.onSizeChanged) {
                            int[] iArr = (int[]) obj;
                            int i = context.getResources().getDisplayMetrics().heightPixels;
                            int i2 = context.getResources().getDisplayMetrics().heightPixels / 4;
                            if (Math.abs(iArr[1] - i) > i2 || Math.abs(iArr[1] - iArr[3]) > i2) {
                                if (iArr[1] <= iArr[3] || Math.abs(iArr[1] - iArr[3]) <= i2) {
                                    Iterator it = ConfirmBar.this.editText.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        WXComponent wXComponent = (WXComponent) it.next();
                                        if (wXComponent.getHostView().hasFocus() && (wXComponent instanceof DCTextArea) && ((DCTextArea) wXComponent).isShowConfirm) {
                                            ConfirmBar.getInstance().showConfirm(true, iArr[1]);
                                            break;
                                        }
                                        ConfirmBar.getInstance().showConfirm(false, iArr[1]);
                                    }
                                } else {
                                    ConfirmBar.getInstance().showConfirm(false, iArr[1]);
                                }
                            }
                        } else if (sysEventType == ISysEventListener.SysEventType.onWebAppStop) {
                            AtomicReference unused = ConfirmBar.this.listener = null;
                            RelativeLayout unused2 = ConfirmBar.this.rtl = null;
                            ViewGroup unused3 = ConfirmBar.this.rootView = null;
                        }
                        return false;
                    }
                });
                this.listener = atomicReference;
                iApp.registerSysEventListener(atomicReference.get(), ISysEventListener.SysEventType.onSizeChanged);
                iApp.registerSysEventListener(this.listener.get(), ISysEventListener.SysEventType.onWebAppStop);
            }
            this.height = (int) TypedValue.applyDimension(1, 44.0f, context.getResources().getDisplayMetrics());
            if (this.rtl == null) {
                this.rtl = new RelativeLayout(context);
                ViewGroup viewGroup = (ViewGroup) iApp.obtainWebAppRootView().obtainMainView().getParent();
                this.rootView = viewGroup;
                if (viewGroup != null) {
                    this.rootView.addView(this.rtl, new FrameLayout.LayoutParams(-1, this.height));
                }
                Button button = new Button(context);
                button.setText(17039370);
                button.setGravity(17);
                button.setTextColor(Color.argb(255, 50, 205, 50));
                button.setTextSize(TypedValue.applyDimension(2, 6.0f, context.getResources().getDisplayMetrics()));
                button.setBackground((Drawable) null);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ConfirmBar.this.editText.size() > 0) {
                            DCTextArea dCTextArea = null;
                            Iterator it = ConfirmBar.this.editText.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                WXComponent wXComponent = (WXComponent) it.next();
                                if ((wXComponent.getHostView() instanceof WXEditText) && wXComponent.getHostView().hasFocus()) {
                                    dCTextArea = wXComponent;
                                    break;
                                }
                            }
                            if (dCTextArea != null) {
                                HashMap hashMap = new HashMap(1);
                                HashMap hashMap2 = new HashMap(1);
                                hashMap2.put("value", ((WXEditText) dCTextArea.getHostView()).getText().toString());
                                hashMap.put("detail", hashMap2);
                                dCTextArea.fireEvent("confirm", hashMap);
                                if (dCTextArea.getParent() != null) {
                                    dCTextArea.getParent().interceptFocus();
                                }
                                dCTextArea.getHostView().clearFocus();
                                dCTextArea.hideSoftKeyboard();
                            }
                        }
                    }
                });
                this.rtl.addView(button, new RelativeLayout.LayoutParams(-2, -1));
                this.rtl.setBackgroundColor(Color.argb(255, 220, 220, 220));
                this.rtl.setTag("ConfirmBar");
                this.rtl.setVisibility(4);
            }
        } catch (Exception unused) {
        }
    }
}
