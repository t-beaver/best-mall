package com.taobao.weex.ui.component;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSONObject;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.WXEditText;
import com.taobao.weex.utils.FontDO;
import com.taobao.weex.utils.TypefaceUtil;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.core.ui.DCKeyboardManager;
import io.dcloud.common.core.ui.keyboard.DCEditText;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.utils.UniUtils;
import io.dcloud.feature.uniapp.utils.UniViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DCWXInput extends WXComponent<WXEditText> {
    private static final int MAX_TEXT_FORMAT_REPEAT = 3;
    final String ADJUST_POSITION = "adjustPosition";
    final String PASSWORD = Constants.Value.PASSWORD;
    private int cursor = -1;
    boolean isConfirmHold = false;
    /* access modifiers changed from: private */
    public AtomicBoolean isLayoutFinished = new AtomicBoolean(false);
    public boolean isNeedConfirm = true;
    /* access modifiers changed from: private */
    public boolean isPassword = false;
    float keyboardHeight = 0.0f;
    /* access modifiers changed from: private */
    public String mBeforeText = "";
    /* access modifiers changed from: private */
    public int mEditorAction = 6;
    /* access modifiers changed from: private */
    public List<TextView.OnEditorActionListener> mEditorActionListeners;
    /* access modifiers changed from: private */
    public String mFontFamily;
    /* access modifiers changed from: private */
    public int mFormatRepeatCount = 0;
    /* access modifiers changed from: private */
    public TextFormatter mFormatter = null;
    /* access modifiers changed from: private */
    public WXSDKInstance.FrameViewEventListener mFrameViewEventListener;
    /* access modifiers changed from: private */
    public boolean mIgnoreNextOnInputEvent = false;
    /* access modifiers changed from: private */
    public final InputMethodManager mInputMethodManager;
    private int mLineHeight = -1;
    /* access modifiers changed from: private */
    public boolean mListeningConfirm = false;
    private WXComponent.OnFocusChangeListener mOnFocusChangeListener = new WXComponent.OnFocusChangeListener() {
        int count = 0;

        public void onFocusChange(boolean z) {
            TextView textView = (TextView) DCWXInput.this.getHostView();
            if (textView != null) {
                HashMap hashMap = new HashMap(1);
                HashMap hashMap2 = new HashMap(1);
                hashMap2.put("value", textView.getText().toString());
                if (!z) {
                    hashMap.put("detail", hashMap2);
                    DCWXInput.this.fireEvent(Constants.Event.BLUR, hashMap);
                } else if (DCWXInput.this.keyboardHeight == 0.0f) {
                    fireEventForFocus(textView);
                } else {
                    hashMap2.put("height", Float.valueOf(DCWXInput.this.keyboardHeight));
                    hashMap2.put("value", textView.getText().toString());
                    hashMap.put("detail", hashMap2);
                    DCWXInput.this.fireEvent(Constants.Event.FOCUS, hashMap);
                }
            }
        }

        /* access modifiers changed from: private */
        public void fireEventForFocus(final TextView textView) {
            ((WXEditText) DCWXInput.this.getHostView()).postDelayed(new Runnable() {
                public void run() {
                    if (DCWXInput.this.keyboardHeight == 0.0f) {
                        AnonymousClass6.this.count++;
                        if (AnonymousClass6.this.count > 3) {
                            HashMap hashMap = new HashMap(1);
                            HashMap hashMap2 = new HashMap(1);
                            hashMap2.put("value", textView.getText().toString());
                            hashMap2.put("height", Float.valueOf(DCWXInput.this.keyboardHeight));
                            hashMap.put("detail", hashMap2);
                            DCWXInput.this.fireEvent(Constants.Event.FOCUS, hashMap);
                            return;
                        }
                        AnonymousClass6.this.fireEventForFocus(textView);
                        return;
                    }
                    AnonymousClass6.this.count = 0;
                    HashMap hashMap3 = new HashMap(1);
                    HashMap hashMap4 = new HashMap(1);
                    hashMap4.put("value", textView.getText().toString());
                    hashMap4.put("height", Float.valueOf(DCWXInput.this.keyboardHeight));
                    hashMap3.put("detail", hashMap4);
                    DCWXInput.this.fireEvent(Constants.Event.FOCUS, hashMap3);
                }
            }, 200);
        }
    };
    private TextPaint mPaint = new TextPaint();
    private String mReturnKeyType = null;
    private TextWatcher mTextChangedEventDispatcher;
    /* access modifiers changed from: private */
    public List<TextWatcher> mTextChangedListeners;
    protected String mType = "text";
    private BroadcastReceiver mTypefaceObserver;
    /* access modifiers changed from: private */
    public float measureHeight = -1.0f;
    /* access modifiers changed from: private */
    public float measureWidht = -1.0f;
    private JSONObject placeholderStyle = new JSONObject();
    /* access modifiers changed from: private */
    public String placeholderTextAlign;
    /* access modifiers changed from: private */
    public int selectionEnd = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public int selectionStart = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public String textAlign = "left";

    private interface ReturnTypes {
        public static final String DEFAULT = "default";
        public static final String DONE = "done";
        public static final String GO = "go";
        public static final String NEXT = "next";
        public static final String SEARCH = "search";
        public static final String SEND = "send";
    }

    /* access modifiers changed from: protected */
    public int getVerticalGravity() {
        return 16;
    }

    public DCWXInput(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        interceptFocusAndBlurEvent();
        this.mInputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                if (DCWXInput.this.getBasicComponentData().getStyles() == null || DCWXInput.this.getBasicComponentData().getStyles().size() == 0) {
                    float unused = DCWXInput.this.measureWidht = f;
                }
                if (CSSConstants.isUndefined(f2)) {
                    this.mMeasureHeight = WXViewUtils.getRealPxByWidth(((float) DCWXInput.this.getInstance().getDefaultFontSize()) * 1.4f, DCWXInput.this.getInstance().getInstanceViewPortWidthWithFloat());
                    float unused2 = DCWXInput.this.measureHeight = this.mMeasureHeight;
                }
            }

            public void layoutBefore() {
                DCWXInput.this.updateStyleAndAttrs();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(WXEditText wXEditText, int i, int i2, int i3, int i4, int i5, int i6) {
        super.setHostLayoutParams(wXEditText, i, i2, i3, i4, i5, i6);
    }

    /* access modifiers changed from: protected */
    public void setLayoutSize(GraphicSize graphicSize) {
        super.setLayoutSize(graphicSize);
    }

    /* access modifiers changed from: protected */
    public void layoutDirectionDidChanged(boolean z) {
        int textAlign2 = getTextAlign((String) getStyles().get(Constants.Name.TEXT_ALIGN));
        if (textAlign2 <= 0) {
            textAlign2 = GravityCompat.START;
        }
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setGravity(textAlign2 | getVerticalGravity());
        }
    }

    /* access modifiers changed from: package-private */
    public final float getMeasuredLineHeight() {
        int i = this.mLineHeight;
        return (i == -1 || i <= 0) ? this.mPaint.getFontMetrics((Paint.FontMetrics) null) : (float) i;
    }

    /* access modifiers changed from: protected */
    public float getMeasureHeight() {
        float f = 50.0f;
        if (getMeasuredLineHeight() >= 50.0f) {
            f = getMeasureHeight();
        }
        return WXViewUtils.getRealPxByWidth(f, getInstance().getInstanceViewPortWidthWithFloat());
    }

    /* access modifiers changed from: private */
    public void updateStyleAndAttrs() {
        if (getStyles().size() > 0) {
            String str = null;
            int fontSize = getStyles().containsKey(Constants.Name.FONT_SIZE) ? WXStyle.getFontSize(getStyles(), getInstance().getDefaultFontSize(), (float) getViewPortWidth()) : -1;
            if (getStyles().containsKey(Constants.Name.FONT_FAMILY)) {
                str = WXStyle.getFontFamily(getStyles());
            }
            int fontStyle = getStyles().containsKey(Constants.Name.FONT_STYLE) ? WXStyle.getFontStyle(getStyles()) : -1;
            int fontWeight = getStyles().containsKey(Constants.Name.FONT_WEIGHT) ? WXStyle.getFontWeight(getStyles()) : -1;
            int lineHeight = WXStyle.getLineHeight(getStyles(), (float) getViewPortWidth());
            if (lineHeight != -1) {
                this.mLineHeight = lineHeight;
            }
            if (fontSize != -1) {
                this.mPaint.setTextSize((float) fontSize);
            }
            if (str != null) {
                TypefaceUtil.applyFontStyle(this.mPaint, fontStyle, fontWeight, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public WXEditText initComponentHostView(Context context) {
        WXEditText wXEditText = new WXEditText(context, getInstanceId());
        appleStyleAfterCreated(wXEditText);
        wXEditText.setImeOptions(6);
        return wXEditText;
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(WXEditText wXEditText) {
        super.onHostViewInitialized(wXEditText);
        if (this.measureWidht > 0.0f) {
            WXBridgeManager.getInstance().setStyleWidth(getInstanceId(), getRef(), this.measureWidht);
        }
        if (this.measureHeight > 0.0f) {
            WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), this.measureHeight);
        }
        addFocusChangeListener(new WXComponent.OnFocusChangeListener() {
            public void onFocusChange(boolean z) {
                if (!z) {
                    DCWXInput.this.decideSoftKeyboard();
                }
                DCWXInput.this.setPseudoClassStatus(Constants.PSEUDO.FOCUS, z);
                if (z) {
                    DCWXInput dCWXInput = DCWXInput.this;
                    dCWXInput.setTextAlign(dCWXInput.textAlign);
                } else if (DCWXInput.this.getHostView() != null && PdrUtil.isEmpty(((WXEditText) DCWXInput.this.getHostView()).getText().toString())) {
                    DCWXInput dCWXInput2 = DCWXInput.this;
                    int access$600 = dCWXInput2.getTextAlign(dCWXInput2.placeholderTextAlign == null ? DCWXInput.this.textAlign : DCWXInput.this.placeholderTextAlign);
                    if (access$600 > 0) {
                        ((WXEditText) DCWXInput.this.getHostView()).setGravity(access$600 | DCWXInput.this.getVerticalGravity());
                    }
                }
            }
        });
        addKeyboardListener(wXEditText);
        if (this.isNeedConfirm) {
            addEditorChangeListener();
        }
    }

    private void addEditorChangeListener() {
        addEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (!DCWXInput.this.mListeningConfirm || i != DCWXInput.this.mEditorAction) {
                    return DCWXInput.this.isConfirmHold;
                }
                HashMap hashMap = new HashMap(1);
                HashMap hashMap2 = new HashMap(1);
                hashMap2.put("value", textView.getText().toString());
                hashMap.put("detail", hashMap2);
                DCWXInput.this.fireEvent("confirm", hashMap);
                if (!DCWXInput.this.isConfirmHold) {
                    DCWXInput.this.blur();
                }
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isConsumeTouch() {
        return !isDisabled();
    }

    /* access modifiers changed from: protected */
    public void appleStyleAfterCreated(final WXEditText wXEditText) {
        int textAlign2 = getTextAlign((String) getStyles().get(Constants.Name.TEXT_ALIGN));
        if (textAlign2 <= 0) {
            textAlign2 = GravityCompat.START;
        }
        wXEditText.setGravity(textAlign2 | getVerticalGravity());
        int color = WXResourceUtils.getColor("#999999");
        if (color != Integer.MIN_VALUE) {
            wXEditText.setHintTextColor(color);
        }
        AnonymousClass4 r0 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (DCWXInput.this.mTextChangedListeners != null) {
                    for (TextWatcher beforeTextChanged : DCWXInput.this.mTextChangedListeners) {
                        beforeTextChanged.beforeTextChanged(charSequence, i, i2, i3);
                    }
                }
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (DCWXInput.this.mFormatter != null) {
                    String format = DCWXInput.this.mFormatter.format(DCWXInput.this.mFormatter.recover(charSequence.toString()));
                    if (format.equals(charSequence.toString()) || DCWXInput.this.mFormatRepeatCount >= 3) {
                        int unused = DCWXInput.this.mFormatRepeatCount = 0;
                    } else {
                        DCWXInput dCWXInput = DCWXInput.this;
                        int unused2 = dCWXInput.mFormatRepeatCount = dCWXInput.mFormatRepeatCount + 1;
                        int length = DCWXInput.this.mFormatter.format(DCWXInput.this.mFormatter.recover(charSequence.subSequence(0, wXEditText.getSelectionStart()).toString())).length();
                        wXEditText.setText(format);
                        wXEditText.setSelection(length);
                        return;
                    }
                }
                if (DCWXInput.this.mTextChangedListeners != null) {
                    for (TextWatcher onTextChanged : DCWXInput.this.mTextChangedListeners) {
                        onTextChanged.onTextChanged(charSequence, i, i2, i3);
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                if (DCWXInput.this.mTextChangedListeners != null) {
                    for (TextWatcher afterTextChanged : DCWXInput.this.mTextChangedListeners) {
                        afterTextChanged.afterTextChanged(editable);
                    }
                }
            }
        };
        this.mTextChangedEventDispatcher = r0;
        wXEditText.addTextChangedListener(r0);
        wXEditText.setTextSize(0, (float) WXStyle.getFontSize(getStyles(), getInstance().getDefaultFontSize(), getInstance().getInstanceViewPortWidthWithFloat()));
        wXEditText.setSingleLine(true);
    }

    public void addEvent(String str) {
        if (getHostView() != null && !TextUtils.isEmpty(str)) {
            if (str.equals("input")) {
                addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable editable) {
                    }

                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (DCWXInput.this.mIgnoreNextOnInputEvent) {
                            boolean unused = DCWXInput.this.mIgnoreNextOnInputEvent = false;
                            String unused2 = DCWXInput.this.mBeforeText = charSequence.toString();
                            return;
                        }
                        HashMap hashMap = new HashMap(1);
                        HashMap hashMap2 = new HashMap(3);
                        hashMap2.put("value", charSequence.toString());
                        hashMap2.put("cursor", Integer.valueOf(((WXEditText) DCWXInput.this.getHostView()).getSelectionEnd()));
                        if (i2 == 0 && i3 != 0) {
                            try {
                                String charSequence2 = charSequence.subSequence(i, i3 + i).toString();
                                hashMap2.put("keyCode", Integer.valueOf(Character.codePointAt(charSequence2, charSequence2.length() - 1)));
                            } catch (Exception unused3) {
                                hashMap2.put("keyCode", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
                            }
                        } else if (i2 != 0 && i3 == 0) {
                            String charSequence3 = DCWXInput.this.mBeforeText.subSequence(i, i2 + i).toString();
                            hashMap2.put("keyCode", Integer.valueOf(Character.codePointAt(charSequence3, charSequence3.length() - 1)));
                        }
                        String unused4 = DCWXInput.this.mBeforeText = charSequence.toString();
                        hashMap.put("detail", hashMap2);
                        DCWXInput.this.fireEvent("input", hashMap);
                    }
                });
            }
            if ("confirm".equals(str)) {
                this.mListeningConfirm = true;
            }
            if (Constants.Event.FOCUS.equals(str) || Constants.Event.BLUR.equals(str)) {
                setFocusAndBlur();
            }
            super.addEvent(str);
        }
    }

    /* access modifiers changed from: protected */
    public void setFocusAndBlur() {
        if (!ismHasFocusChangeListener(this.mOnFocusChangeListener)) {
            addFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x01dd, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x02a7, code lost:
        return super.setProperty(r6, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0128, code lost:
        if (r6.equals(com.taobao.weex.common.Constants.Name.SELECTION_START) == false) goto L_0x0011;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0166, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r6, java.lang.Object r7) {
        /*
            r5 = this;
            r6.hashCode()
            int r0 = r6.hashCode()
            r1 = 0
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r1)
            r3 = 1
            r4 = -1
            switch(r0) {
                case -2137753034: goto L_0x0122;
                case -1898657397: goto L_0x0116;
                case -1629051985: goto L_0x010a;
                case -1576785488: goto L_0x00fe;
                case -1349119146: goto L_0x00f2;
                case -1224696685: goto L_0x00e6;
                case -1065511464: goto L_0x00da;
                case -791400086: goto L_0x00ce;
                case 107876: goto L_0x00c1;
                case 108114: goto L_0x00b3;
                case 94842723: goto L_0x00a5;
                case 97604824: goto L_0x0097;
                case 102977279: goto L_0x0089;
                case 124732746: goto L_0x007c;
                case 270940796: goto L_0x006f;
                case 344059807: goto L_0x0062;
                case 365601008: goto L_0x0055;
                case 598246771: goto L_0x0048;
                case 914346044: goto L_0x003b;
                case 947486441: goto L_0x002e;
                case 1625554645: goto L_0x0021;
                case 1638055017: goto L_0x0014;
                default: goto L_0x0011;
            }
        L_0x0011:
            r1 = -1
            goto L_0x012c
        L_0x0014:
            java.lang.String r0 = "autoFocus"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x001d
            goto L_0x0011
        L_0x001d:
            r1 = 21
            goto L_0x012c
        L_0x0021:
            java.lang.String r0 = "allowCopyPaste"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x002a
            goto L_0x0011
        L_0x002a:
            r1 = 20
            goto L_0x012c
        L_0x002e:
            java.lang.String r0 = "returnKeyType"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0037
            goto L_0x0011
        L_0x0037:
            r1 = 19
            goto L_0x012c
        L_0x003b:
            java.lang.String r0 = "singleline"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0044
            goto L_0x0011
        L_0x0044:
            r1 = 18
            goto L_0x012c
        L_0x0048:
            java.lang.String r0 = "placeholder"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0051
            goto L_0x0011
        L_0x0051:
            r1 = 17
            goto L_0x012c
        L_0x0055:
            java.lang.String r0 = "fontSize"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x005e
            goto L_0x0011
        L_0x005e:
            r1 = 16
            goto L_0x012c
        L_0x0062:
            java.lang.String r0 = "confirmHold"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x006b
            goto L_0x0011
        L_0x006b:
            r1 = 15
            goto L_0x012c
        L_0x006f:
            java.lang.String r0 = "disabled"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0078
            goto L_0x0011
        L_0x0078:
            r1 = 14
            goto L_0x012c
        L_0x007c:
            java.lang.String r0 = "maxlength"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0085
            goto L_0x0011
        L_0x0085:
            r1 = 13
            goto L_0x012c
        L_0x0089:
            java.lang.String r0 = "lines"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0093
            goto L_0x0011
        L_0x0093:
            r1 = 12
            goto L_0x012c
        L_0x0097:
            java.lang.String r0 = "focus"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00a1
            goto L_0x0011
        L_0x00a1:
            r1 = 11
            goto L_0x012c
        L_0x00a5:
            java.lang.String r0 = "color"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00af
            goto L_0x0011
        L_0x00af:
            r1 = 10
            goto L_0x012c
        L_0x00b3:
            java.lang.String r0 = "min"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00bd
            goto L_0x0011
        L_0x00bd:
            r1 = 9
            goto L_0x012c
        L_0x00c1:
            java.lang.String r0 = "max"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00cb
            goto L_0x0011
        L_0x00cb:
            r1 = 8
            goto L_0x012c
        L_0x00ce:
            java.lang.String r0 = "maxLength"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00d8
            goto L_0x0011
        L_0x00d8:
            r1 = 7
            goto L_0x012c
        L_0x00da:
            java.lang.String r0 = "textAlign"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00e4
            goto L_0x0011
        L_0x00e4:
            r1 = 6
            goto L_0x012c
        L_0x00e6:
            java.lang.String r0 = "fontFamily"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00f0
            goto L_0x0011
        L_0x00f0:
            r1 = 5
            goto L_0x012c
        L_0x00f2:
            java.lang.String r0 = "cursor"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x00fc
            goto L_0x0011
        L_0x00fc:
            r1 = 4
            goto L_0x012c
        L_0x00fe:
            java.lang.String r0 = "placeholderColor"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0108
            goto L_0x0011
        L_0x0108:
            r1 = 3
            goto L_0x012c
        L_0x010a:
            java.lang.String r0 = "selectionEnd"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0114
            goto L_0x0011
        L_0x0114:
            r1 = 2
            goto L_0x012c
        L_0x0116:
            java.lang.String r0 = "keepSelectionIndex"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0120
            goto L_0x0011
        L_0x0120:
            r1 = 1
            goto L_0x012c
        L_0x0122:
            java.lang.String r0 = "selectionStart"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x012c
            goto L_0x0011
        L_0x012c:
            r0 = 2147483647(0x7fffffff, float:NaN)
            r4 = 0
            switch(r1) {
                case 0: goto L_0x0290;
                case 1: goto L_0x028f;
                case 2: goto L_0x027b;
                case 3: goto L_0x0271;
                case 4: goto L_0x0229;
                case 5: goto L_0x01f6;
                case 6: goto L_0x01ec;
                case 7: goto L_0x01de;
                case 8: goto L_0x01dd;
                case 9: goto L_0x01dd;
                case 10: goto L_0x01d4;
                case 11: goto L_0x01c8;
                case 12: goto L_0x01ba;
                case 13: goto L_0x01ac;
                case 14: goto L_0x017d;
                case 15: goto L_0x0171;
                case 16: goto L_0x0167;
                case 17: goto L_0x0166;
                case 18: goto L_0x0159;
                case 19: goto L_0x0151;
                case 20: goto L_0x0135;
                case 21: goto L_0x01c8;
                default: goto L_0x0133;
            }
        L_0x0133:
            goto L_0x02a3
        L_0x0135:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r6)
            boolean r6 = r6.booleanValue()
            android.view.View r7 = r5.getHostView()
            if (r7 == 0) goto L_0x0150
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            r7.setAllowCopyPaste(r6)
        L_0x0150:
            return r3
        L_0x0151:
            java.lang.String r6 = java.lang.String.valueOf(r7)
            r5.setReturnKeyType(r6)
            return r3
        L_0x0159:
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r4)
            if (r6 == 0) goto L_0x0166
            boolean r6 = r6.booleanValue()
            r5.setSingleLine(r6)
        L_0x0166:
            return r3
        L_0x0167:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x0170
            r5.setFontSize(r6)
        L_0x0170:
            return r3
        L_0x0171:
            java.lang.Boolean r0 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r2)
            boolean r0 = r0.booleanValue()
            r5.isConfirmHold = r0
            goto L_0x02a3
        L_0x017d:
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r2)
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            boolean r0 = r6.booleanValue()
            r0 = r0 ^ r3
            r7.setFocusable(r0)
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            boolean r0 = r6.booleanValue()
            r0 = r0 ^ r3
            r7.setFocusableInTouchMode(r0)
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            boolean r6 = r6.booleanValue()
            r6 = r6 ^ r3
            r7.setCursorVisible(r6)
            return r3
        L_0x01ac:
            java.lang.Integer r6 = com.taobao.weex.utils.WXUtils.getInteger(r7, r4)
            if (r6 == 0) goto L_0x01b9
            int r6 = r6.intValue()
            r5.setMaxLength(r6)
        L_0x01b9:
            return r3
        L_0x01ba:
            java.lang.Integer r6 = com.taobao.weex.utils.WXUtils.getInteger(r7, r4)
            if (r6 == 0) goto L_0x01c7
            int r6 = r6.intValue()
            r5.setLines(r6)
        L_0x01c7:
            return r3
        L_0x01c8:
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r2)
            boolean r6 = r6.booleanValue()
            r5.setAutofocus(r6)
            return r3
        L_0x01d4:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x01dd
            r5.setColor(r6)
        L_0x01dd:
            return r3
        L_0x01de:
            java.lang.Integer r6 = com.taobao.weex.utils.WXUtils.getInteger(r7, r4)
            if (r6 == 0) goto L_0x01eb
            int r6 = r6.intValue()
            r5.setMaxLength(r6)
        L_0x01eb:
            return r3
        L_0x01ec:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x01f5
            r5.setTextAlign(r6)
        L_0x01f5:
            return r3
        L_0x01f6:
            if (r7 == 0) goto L_0x0228
            java.lang.String r6 = r7.toString()     // Catch:{ Exception -> 0x0224 }
            com.taobao.weex.utils.FontDO r6 = com.taobao.weex.utils.TypefaceUtil.getFontDO(r6)     // Catch:{ Exception -> 0x0224 }
            if (r6 == 0) goto L_0x021c
            android.graphics.Typeface r0 = r6.getTypeface()     // Catch:{ Exception -> 0x0224 }
            if (r0 == 0) goto L_0x021c
            android.view.View r0 = r5.getHostView()     // Catch:{ Exception -> 0x0224 }
            if (r0 == 0) goto L_0x021c
            android.view.View r7 = r5.getHostView()     // Catch:{ Exception -> 0x0224 }
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7     // Catch:{ Exception -> 0x0224 }
            android.graphics.Typeface r6 = r6.getTypeface()     // Catch:{ Exception -> 0x0224 }
            r7.setTypeface(r6)     // Catch:{ Exception -> 0x0224 }
            goto L_0x0228
        L_0x021c:
            java.lang.String r6 = r7.toString()     // Catch:{ Exception -> 0x0224 }
            r5.registerTypefaceObserver(r6)     // Catch:{ Exception -> 0x0224 }
            goto L_0x0228
        L_0x0224:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0228:
            return r3
        L_0x0229:
            int r6 = com.taobao.weex.utils.WXUtils.getInt(r7)
            if (r6 <= 0) goto L_0x0249
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            android.text.Editable r7 = r7.getText()
            int r7 = r7.length()
            if (r6 > r7) goto L_0x0249
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            r7.setSelection(r6)
            goto L_0x0270
        L_0x0249:
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            android.text.Editable r7 = r7.getText()
            int r7 = r7.length()
            if (r6 <= r7) goto L_0x0270
            android.view.View r6 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r6 = (com.taobao.weex.ui.view.WXEditText) r6
            android.view.View r7 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r7 = (com.taobao.weex.ui.view.WXEditText) r7
            android.text.Editable r7 = r7.getText()
            int r7 = r7.length()
            r6.setSelection(r7)
        L_0x0270:
            return r3
        L_0x0271:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x027a
            r5.setPlaceholderColor(r6)
        L_0x027a:
            return r3
        L_0x027b:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.Integer r0 = com.taobao.weex.utils.WXUtils.getInteger(r7, r0)
            int r0 = r0.intValue()
            r5.selectionEnd = r0
            int r1 = r5.selectionStart
            r5.setSelectionRange(r1, r0)
            goto L_0x02a3
        L_0x028f:
            return r3
        L_0x0290:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            java.lang.Integer r0 = com.taobao.weex.utils.WXUtils.getInteger(r7, r0)
            int r0 = r0.intValue()
            r5.selectionStart = r0
            int r1 = r5.selectionEnd
            r5.setSelectionRange(r0, r1)
        L_0x02a3:
            boolean r6 = super.setProperty(r6, r7)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.DCWXInput.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    private void registerTypefaceObserver(String str) {
        if (WXEnvironment.getApplication() == null) {
            WXLogUtils.w("WXText", "ApplicationContent is null on register typeface observer");
            return;
        }
        this.mFontFamily = str;
        if (this.mTypefaceObserver == null) {
            this.mTypefaceObserver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    FontDO fontDO;
                    String stringExtra = intent.getStringExtra(Constants.Name.FONT_FAMILY);
                    if (DCWXInput.this.mFontFamily.equals(stringExtra) && (fontDO = TypefaceUtil.getFontDO(stringExtra)) != null && fontDO.getTypeface() != null && DCWXInput.this.getHostView() != null) {
                        ((WXEditText) DCWXInput.this.getHostView()).setTypeface(fontDO.getTypeface());
                    }
                }
            };
            LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).registerReceiver(this.mTypefaceObserver, new IntentFilter(TypefaceUtil.ACTION_TYPE_FACE_AVAILABLE));
        }
    }

    @WXComponentProp(name = "returnKeyType")
    public void setReturnKeyType(String str) {
        if (getHostView() != null && !str.equals(this.mReturnKeyType)) {
            this.mReturnKeyType = str;
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -906336856:
                    if (str.equals("search")) {
                        c = 0;
                        break;
                    }
                    break;
                case 3304:
                    if (str.equals("go")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3089282:
                    if (str.equals("done")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3377907:
                    if (str.equals("next")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3526536:
                    if (str.equals("send")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1544803905:
                    if (str.equals("default")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.mEditorAction = 3;
                    break;
                case 1:
                    this.mEditorAction = 2;
                    break;
                case 2:
                    this.mEditorAction = 6;
                    break;
                case 3:
                    this.mEditorAction = 5;
                    break;
                case 4:
                    this.mEditorAction = 4;
                    break;
                case 5:
                    this.mEditorAction = 0;
                    break;
            }
            blur();
            ((WXEditText) getHostView()).setImeOptions(this.mEditorAction);
        }
    }

    public void setPlaceholder(String str) {
        if (str != null && getHostView() != null) {
            ((WXEditText) getHostView()).setHint(str);
        }
    }

    @WXComponentProp(name = "placeholderColor")
    public void setPlaceholderColor(String str) {
        int color;
        if (getHostView() != null && !TextUtils.isEmpty(str) && (color = WXResourceUtils.getColor(str)) != Integer.MIN_VALUE) {
            ((WXEditText) getHostView()).setHintTextColor(color);
        }
    }

    public void setType(String str) {
        Log.e("weex", "setType=" + str);
        if (str != null && getHostView() != null && !this.mType.equals(str)) {
            this.mType = str;
            ((EditText) getHostView()).setInputType(getInputType(this.mType));
        }
    }

    @WXComponentProp(name = "autofocus")
    public void setAutofocus(final boolean z) {
        if (getHostView() != null) {
            final EditText editText = (EditText) getHostView();
            if (this.isLayoutFinished.get()) {
                if (z) {
                    hostViewFocus(editText);
                } else {
                    hideSoftKeyboard();
                }
                editText.clearFocus();
            } else if (getInstance().isFrameViewShow()) {
                ((WXEditText) getHostView()).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (DCWXInput.this.getInstance() != null) {
                            if (z) {
                                DCWXInput.this.isLayoutFinished.set(true);
                                DCWXInput.this.hostViewFocus(editText);
                                DCWXInput dCWXInput = DCWXInput.this;
                                dCWXInput.setSelectionRange(dCWXInput.selectionStart, DCWXInput.this.selectionEnd);
                            } else {
                                editText.clearFocus();
                            }
                            ((WXEditText) DCWXInput.this.getHostView()).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            } else {
                if (this.mFrameViewEventListener != null) {
                    getInstance().removeFrameViewEventListener(this.mFrameViewEventListener);
                }
                this.mFrameViewEventListener = new WXSDKInstance.FrameViewEventListener() {
                    public void onShowAnimationEnd() {
                        if (DCWXInput.this.getInstance() != null) {
                            if (z) {
                                DCWXInput.this.isLayoutFinished.set(true);
                                DCWXInput.this.hostViewFocus(editText);
                                DCWXInput dCWXInput = DCWXInput.this;
                                dCWXInput.setSelectionRange(dCWXInput.selectionStart, DCWXInput.this.selectionEnd);
                            } else {
                                editText.clearFocus();
                            }
                            DCWXInput.this.getInstance().removeFrameViewEventListener(this);
                            WXSDKInstance.FrameViewEventListener unused = DCWXInput.this.mFrameViewEventListener = null;
                        }
                    }
                };
                getInstance().addFrameViewEventListener(this.mFrameViewEventListener);
            }
        }
    }

    @WXComponentProp(name = "adjustPosition")
    public void setAdjustPosition(Object obj) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setInputSoftMode(WXUtils.getBoolean(obj, true).booleanValue() ? DCKeyboardManager.SOFT_INPUT_MODE_ADJUST_PAN : DCKeyboardManager.SOFT_INPUT_MODE_ADJUST_NOTHING);
        }
    }

    /* access modifiers changed from: private */
    public void hostViewFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
        showSoftKeyboard();
    }

    @WXComponentProp(name = "value")
    public void setText(String str) {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && !TextUtils.equals(wXEditText.getText(), str)) {
            this.mIgnoreNextOnInputEvent = true;
            wXEditText.setText(str);
            int i = this.cursor;
            if (i <= 0) {
                i = str.length();
            }
            if (str == null) {
                i = 0;
            }
            wXEditText.setSelection(i);
        }
    }

    @WXComponentProp(name = "color")
    public void setColor(String str) {
        int color;
        if (getHostView() != null && !TextUtils.isEmpty(str) && (color = WXResourceUtils.getColor(str)) != Integer.MIN_VALUE) {
            ((WXEditText) getHostView()).setTextColor(color);
        }
    }

    @WXComponentProp(name = "fontSize")
    public void setFontSize(String str) {
        if (getHostView() != null && str != null) {
            HashMap hashMap = new HashMap(1);
            hashMap.put(Constants.Name.FONT_SIZE, str);
            ((WXEditText) getHostView()).setTextSize(0, (float) WXStyle.getFontSize(hashMap, getInstance().getDefaultFontSize(), getInstance().getInstanceViewPortWidthWithFloat()));
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    @com.taobao.weex.ui.component.WXComponentProp(name = "placeholderStyle")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setPlaceholderStyle(com.alibaba.fastjson.JSONObject r18) {
        /*
            r17 = this;
            r0 = r17
            com.alibaba.fastjson.JSONObject r1 = r0.placeholderStyle
            if (r1 == 0) goto L_0x022c
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L_0x000e
            goto L_0x022c
        L_0x000e:
            android.view.View r1 = r17.getHostView()
            com.taobao.weex.ui.view.WXEditText r1 = (com.taobao.weex.ui.view.WXEditText) r1
            java.lang.CharSequence r1 = r1.getHint()
            java.lang.String r2 = ""
            if (r1 != 0) goto L_0x0034
            com.taobao.weex.dom.WXAttr r3 = r17.getAttrs()
            java.lang.String r4 = "placeholder"
            boolean r3 = r3.containsKey(r4)
            if (r3 == 0) goto L_0x0034
            com.taobao.weex.dom.WXAttr r1 = r17.getAttrs()
            java.lang.Object r1 = r1.get(r4)
            java.lang.String r1 = com.taobao.weex.utils.WXUtils.getString(r1, r2)
        L_0x0034:
            android.text.SpannableString r3 = new android.text.SpannableString
            r3.<init>(r1)
            android.text.style.AbsoluteSizeSpan r1 = new android.text.style.AbsoluteSizeSpan
            android.view.View r4 = r17.getHostView()
            com.taobao.weex.ui.view.WXEditText r4 = (com.taobao.weex.ui.view.WXEditText) r4
            float r4 = r4.getTextSize()
            int r4 = (int) r4
            r1.<init>(r4)
            android.text.style.ForegroundColorSpan r4 = new android.text.style.ForegroundColorSpan
            android.view.View r5 = r17.getHostView()
            com.taobao.weex.ui.view.WXEditText r5 = (com.taobao.weex.ui.view.WXEditText) r5
            int r5 = r5.getCurrentHintTextColor()
            r4.<init>(r5)
            com.alibaba.fastjson.JSONObject r5 = r0.placeholderStyle
            java.util.Set r5 = r5.entrySet()
            java.util.Iterator r5 = r5.iterator()
            r6 = 0
            r7 = r6
        L_0x0064:
            boolean r8 = r5.hasNext()
            r9 = 0
            if (r8 == 0) goto L_0x0201
            java.lang.Object r8 = r5.next()
            java.util.Map$Entry r8 = (java.util.Map.Entry) r8
            java.lang.Object r10 = r8.getKey()
            java.lang.String r10 = (java.lang.String) r10
            r10.hashCode()
            int r11 = r10.hashCode()
            java.lang.String r13 = "fontSize"
            r14 = 3
            r15 = 2
            r16 = -1
            r12 = 1
            switch(r11) {
                case -1586082113: goto L_0x00e2;
                case -1065511464: goto L_0x00d7;
                case -734428249: goto L_0x00cc;
                case 94842723: goto L_0x00c1;
                case 365601008: goto L_0x00b8;
                case 598800822: goto L_0x00ad;
                case 605322756: goto L_0x00a2;
                case 746232421: goto L_0x0097;
                case 1287124693: goto L_0x008b;
                default: goto L_0x0088;
            }
        L_0x0088:
            r10 = -1
            goto L_0x00ec
        L_0x008b:
            java.lang.String r11 = "backgroundColor"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x0094
            goto L_0x0088
        L_0x0094:
            r10 = 8
            goto L_0x00ec
        L_0x0097:
            java.lang.String r11 = "text-align"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00a0
            goto L_0x0088
        L_0x00a0:
            r10 = 7
            goto L_0x00ec
        L_0x00a2:
            java.lang.String r11 = "background-color"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00ab
            goto L_0x0088
        L_0x00ab:
            r10 = 6
            goto L_0x00ec
        L_0x00ad:
            java.lang.String r11 = "font-weight"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00b6
            goto L_0x0088
        L_0x00b6:
            r10 = 5
            goto L_0x00ec
        L_0x00b8:
            boolean r10 = r10.equals(r13)
            if (r10 != 0) goto L_0x00bf
            goto L_0x0088
        L_0x00bf:
            r10 = 4
            goto L_0x00ec
        L_0x00c1:
            java.lang.String r11 = "color"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00ca
            goto L_0x0088
        L_0x00ca:
            r10 = 3
            goto L_0x00ec
        L_0x00cc:
            java.lang.String r11 = "fontWeight"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00d5
            goto L_0x0088
        L_0x00d5:
            r10 = 2
            goto L_0x00ec
        L_0x00d7:
            java.lang.String r11 = "textAlign"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00e0
            goto L_0x0088
        L_0x00e0:
            r10 = 1
            goto L_0x00ec
        L_0x00e2:
            java.lang.String r11 = "font-size"
            boolean r10 = r10.equals(r11)
            if (r10 != 0) goto L_0x00eb
            goto L_0x0088
        L_0x00eb:
            r10 = 0
        L_0x00ec:
            r11 = -2147483648(0xffffffff80000000, float:-0.0)
            switch(r10) {
                case 0: goto L_0x01a4;
                case 1: goto L_0x017c;
                case 2: goto L_0x011d;
                case 3: goto L_0x0108;
                case 4: goto L_0x01a4;
                case 5: goto L_0x011d;
                case 6: goto L_0x00f3;
                case 7: goto L_0x017c;
                case 8: goto L_0x00f3;
                default: goto L_0x00f1;
            }
        L_0x00f1:
            goto L_0x0064
        L_0x00f3:
            java.lang.Object r8 = r8.getValue()
            java.lang.String r8 = java.lang.String.valueOf(r8)
            int r8 = com.taobao.weex.utils.WXResourceUtils.getColor(r8)
            if (r8 == r11) goto L_0x0064
            android.text.style.BackgroundColorSpan r6 = new android.text.style.BackgroundColorSpan
            r6.<init>(r8)
            goto L_0x0064
        L_0x0108:
            java.lang.Object r8 = r8.getValue()
            java.lang.String r8 = java.lang.String.valueOf(r8)
            int r8 = com.taobao.weex.utils.WXResourceUtils.getColor(r8)
            if (r8 == r11) goto L_0x0064
            android.text.style.ForegroundColorSpan r4 = new android.text.style.ForegroundColorSpan
            r4.<init>(r8)
            goto L_0x0064
        L_0x011d:
            java.lang.Object r7 = r8.getValue()
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r7.hashCode()
            int r8 = r7.hashCode()
            switch(r8) {
                case 53430: goto L_0x0160;
                case 54391: goto L_0x0154;
                case 55352: goto L_0x0148;
                case 56313: goto L_0x013c;
                case 3029637: goto L_0x0130;
                default: goto L_0x012f;
            }
        L_0x012f:
            goto L_0x016b
        L_0x0130:
            java.lang.String r8 = "bold"
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x0139
            goto L_0x016b
        L_0x0139:
            r16 = 4
            goto L_0x016b
        L_0x013c:
            java.lang.String r8 = "900"
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x0145
            goto L_0x016b
        L_0x0145:
            r16 = 3
            goto L_0x016b
        L_0x0148:
            java.lang.String r8 = "800"
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x0151
            goto L_0x016b
        L_0x0151:
            r16 = 2
            goto L_0x016b
        L_0x0154:
            java.lang.String r8 = "700"
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x015d
            goto L_0x016b
        L_0x015d:
            r16 = 1
            goto L_0x016b
        L_0x0160:
            java.lang.String r8 = "600"
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x0169
            goto L_0x016b
        L_0x0169:
            r16 = 0
        L_0x016b:
            switch(r16) {
                case 0: goto L_0x0175;
                case 1: goto L_0x0175;
                case 2: goto L_0x0175;
                case 3: goto L_0x0175;
                case 4: goto L_0x0175;
                default: goto L_0x016e;
            }
        L_0x016e:
            android.text.style.StyleSpan r7 = new android.text.style.StyleSpan
            r7.<init>(r9)
            goto L_0x0064
        L_0x0175:
            android.text.style.StyleSpan r7 = new android.text.style.StyleSpan
            r7.<init>(r12)
            goto L_0x0064
        L_0x017c:
            java.lang.Object r9 = r8.getValue()
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r0.placeholderTextAlign = r9
            java.lang.Object r8 = r8.getValue()
            java.lang.String r8 = java.lang.String.valueOf(r8)
            int r8 = r0.getTextAlign(r8)
            if (r8 <= 0) goto L_0x0064
            android.view.View r9 = r17.getHostView()
            com.taobao.weex.ui.view.WXEditText r9 = (com.taobao.weex.ui.view.WXEditText) r9
            int r10 = r17.getVerticalGravity()
            r8 = r8 | r10
            r9.setGravity(r8)
            goto L_0x0064
        L_0x01a4:
            java.lang.Object r1 = r8.getValue()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r8 = "px"
            boolean r9 = r1.endsWith(r8)
            if (r9 == 0) goto L_0x01b9
            java.lang.String r1 = r1.replaceAll(r8, r2)
            goto L_0x01c6
        L_0x01b9:
            java.lang.String r8 = "wx"
            boolean r9 = r1.endsWith(r8)
            if (r9 == 0) goto L_0x01c6
            java.lang.String r1 = r1.replaceAll(r8, r2)
        L_0x01c6:
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>(r12)
            r8.put(r13, r1)
            android.text.style.AbsoluteSizeSpan r1 = new android.text.style.AbsoluteSizeSpan
            com.taobao.weex.WXSDKInstance r9 = r17.getInstance()
            int r9 = r9.getDefaultFontSize()
            com.taobao.weex.WXSDKInstance r10 = r17.getInstance()
            float r10 = r10.getInstanceViewPortWidthWithFloat()
            int r8 = com.taobao.weex.dom.WXStyle.getFontSize(r8, r9, r10)
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            com.taobao.weex.WXSDKInstance r9 = r17.getInstance()
            int r9 = r9.getDefaultFontSize()
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Integer r8 = com.taobao.weex.utils.WXUtils.getInteger(r8, r9)
            int r8 = r8.intValue()
            r1.<init>(r8)
            goto L_0x0064
        L_0x0201:
            int r2 = r3.length()
            r5 = 33
            r3.setSpan(r1, r9, r2, r5)
            if (r6 == 0) goto L_0x0213
            int r1 = r3.length()
            r3.setSpan(r6, r9, r1, r5)
        L_0x0213:
            if (r7 == 0) goto L_0x021c
            int r1 = r3.length()
            r3.setSpan(r7, r9, r1, r5)
        L_0x021c:
            int r1 = r3.length()
            r3.setSpan(r4, r9, r1, r5)
            android.view.View r1 = r17.getHostView()
            com.taobao.weex.ui.view.WXEditText r1 = (com.taobao.weex.ui.view.WXEditText) r1
            r1.setHint(r3)
        L_0x022c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.DCWXInput.setPlaceholderStyle(com.alibaba.fastjson.JSONObject):void");
    }

    @WXComponentProp(name = "placeholderClass")
    public void setPlaceholderClass(JSONObject jSONObject) {
        setPlaceholderStyle(jSONObject);
    }

    @WXComponentProp(name = "textAlign")
    public void setTextAlign(String str) {
        this.textAlign = str;
        int textAlign2 = getTextAlign(str);
        if (textAlign2 > 0) {
            ((WXEditText) getHostView()).setGravity(textAlign2 | getVerticalGravity());
        }
    }

    @WXComponentProp(name = "singleline")
    public void setSingleLine(boolean z) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setSingleLine(z);
        }
    }

    @WXComponentProp(name = "lines")
    public void setLines(int i) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setLines(i);
        }
    }

    @WXComponentProp(name = "maxLength")
    public void setMaxLength(int i) {
        if (getHostView() != null) {
            if (i == -1) {
                i = Integer.MAX_VALUE;
            }
            ((WXEditText) getHostView()).setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
        }
    }

    @WXComponentProp(name = "maxlength")
    @Deprecated
    public void setMaxlength(int i) {
        setMaxLength(i);
    }

    private int getInputType(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1034364087:
                if (str.equals("number")) {
                    c = 0;
                    break;
                }
                break;
            case 114715:
                if (str.equals(Constants.Value.TEL)) {
                    c = 1;
                    break;
                }
                break;
            case 116079:
                if (str.equals("url")) {
                    c = 2;
                    break;
                }
                break;
            case 3076014:
                if (str.equals("date")) {
                    c = 3;
                    break;
                }
                break;
            case 3556653:
                if (str.equals("text")) {
                    c = 4;
                    break;
                }
                break;
            case 3560141:
                if (str.equals(Constants.Value.TIME)) {
                    c = 5;
                    break;
                }
                break;
            case 95582509:
                if (str.equals("digit")) {
                    c = 6;
                    break;
                }
                break;
            case 96619420:
                if (str.equals("email")) {
                    c = 7;
                    break;
                }
                break;
            case 1216985755:
                if (str.equals(Constants.Value.PASSWORD)) {
                    c = 8;
                    break;
                }
                break;
            case 1793702779:
                if (str.equals(Constants.Value.DATETIME)) {
                    c = 9;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 17;
            case 3:
                ((WXEditText) getHostView()).setFocusable(false);
                break;
            case 5:
                if (getHostView() != null) {
                    ((WXEditText) getHostView()).setFocusable(false);
                    break;
                }
                break;
            case 6:
                return 8194;
            case 7:
                return 33;
            case 8:
                if (getHostView() == null) {
                    return 129;
                }
                ((WXEditText) getHostView()).setTransformationMethod(PasswordTransformationMethod.getInstance());
                return 129;
            case 9:
                return 4;
            default:
                return 1;
        }
        return 0;
    }

    @WXComponentProp(name = "cursorSpacing")
    public void setCursorSpacing(String str) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setCursorSpacing(UniViewUtils.getRealPxByWidth(UniUtils.getFloat(str), getInstance().getInstanceViewPortWidthWithFloat()));
        }
    }

    private void showSoftKeyboard() {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    DCWXInput.this.mInputMethodManager.showSoftInput(DCWXInput.this.getHostView(), 1);
                }
            }), 100);
        }
    }

    /* access modifiers changed from: package-private */
    public void hideSoftKeyboard() {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    DCWXInput.this.mInputMethodManager.hideSoftInputFromWindow(((WXEditText) DCWXInput.this.getHostView()).getWindowToken(), 0);
                }
            }), 16);
        }
    }

    /* access modifiers changed from: private */
    public int getTextAlign(String str) {
        int i = isLayoutRTL() ? GravityCompat.END : GravityCompat.START;
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1364013995:
                if (str.equals("center")) {
                    c = 0;
                    break;
                }
                break;
            case 3317767:
                if (str.equals("left")) {
                    c = 1;
                    break;
                }
                break;
            case 108511772:
                if (str.equals("right")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 17;
            case 1:
                return GravityCompat.START;
            case 2:
                return GravityCompat.END;
            default:
                return i;
        }
    }

    @JSMethod
    public void blur() {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && wXEditText.hasFocus()) {
            if (getParent() != null) {
                getParent().interceptFocus();
            }
            wXEditText.clearFocus();
            hideSoftKeyboard();
        }
    }

    public String getValue() {
        return ((WXEditText) getHostView()).getText().toString();
    }

    @JSMethod
    public void setValue(String str) {
        ((WXEditText) getHostView()).setText(str);
    }

    @JSMethod
    public void focus() {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && !wXEditText.hasFocus()) {
            if (getParent() != null) {
                getParent().ignoreFocus();
            }
            wXEditText.requestFocus();
            wXEditText.setFocusable(true);
            wXEditText.setFocusableInTouchMode(true);
            showSoftKeyboard();
        }
    }

    /* access modifiers changed from: protected */
    public Object convertEmptyProperty(String str, Object obj) {
        str.hashCode();
        if (str.equals("color")) {
            return "black";
        }
        if (!str.equals(Constants.Name.FONT_SIZE)) {
            return super.convertEmptyProperty(str, obj);
        }
        return Integer.valueOf(getInstance().getDefaultFontSize());
    }

    /* access modifiers changed from: private */
    public void decideSoftKeyboard() {
        View hostView = getHostView();
        if (hostView != null) {
            final Context context = getContext();
            if (context instanceof Activity) {
                hostView.postDelayed(WXThread.secure((Runnable) new Runnable() {
                    public void run() {
                        View currentFocus = ((Activity) context).getCurrentFocus();
                        if (currentFocus != null && !(currentFocus instanceof EditText) && !currentFocus.isFocused()) {
                            DCWXInput.this.mInputMethodManager.hideSoftInputFromWindow(((WXEditText) DCWXInput.this.getHostView()).getWindowToken(), 0);
                        }
                    }
                }), 16);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setSelectionRange(int i, int i2) {
        EditText editText;
        if (i2 != Integer.MAX_VALUE && i != Integer.MAX_VALUE && (editText = (EditText) getHostView()) != null) {
            int length = ((WXEditText) getHostView()).length();
            if (i <= i2) {
                int i3 = 0;
                if (i < 0) {
                    i = 0;
                }
                if (i2 > length) {
                    i2 = length;
                }
                if (i2 >= 0) {
                    i3 = i2;
                }
                editText.setSelection(i, i3);
            }
        }
    }

    @UniJSMethod
    public void getSelectionRange(String str) {
        HashMap hashMap = new HashMap(2);
        EditText editText = (EditText) getHostView();
        if (editText != null) {
            int selectionStart2 = editText.getSelectionStart();
            int selectionEnd2 = editText.getSelectionEnd();
            if (!editText.hasFocus()) {
                selectionStart2 = 0;
                selectionEnd2 = 0;
            }
            hashMap.put(Constants.Name.SELECTION_START, Integer.valueOf(selectionStart2));
            hashMap.put(Constants.Name.SELECTION_END, Integer.valueOf(selectionEnd2));
        }
        WXBridgeManager.getInstance().callback(getInstanceId(), str, hashMap, false);
    }

    @UniJSMethod
    public void getCursor(JSCallback jSCallback) {
        HashMap hashMap = new HashMap(1);
        if (getHostView() == null || !((WXEditText) getHostView()).isFocused()) {
            hashMap.put("cursor", 0);
        } else {
            hashMap.put("cursor", Integer.valueOf(((WXEditText) getHostView()).getSelectionEnd()));
        }
        jSCallback.invoke(hashMap);
    }

    @JSMethod
    public void setTextFormatter(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("formatRule");
            String string2 = jSONObject.getString("formatReplace");
            String string3 = jSONObject.getString("recoverRule");
            String string4 = jSONObject.getString("recoverReplace");
            PatternWrapper parseToPattern = parseToPattern(string, string2);
            PatternWrapper parseToPattern2 = parseToPattern(string3, string4);
            if (parseToPattern != null && parseToPattern2 != null) {
                this.mFormatter = new TextFormatter(parseToPattern, parseToPattern2);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private final void addEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        TextView textView;
        if (onEditorActionListener != null && (textView = (TextView) getHostView()) != null) {
            if (this.mEditorActionListeners == null) {
                this.mEditorActionListeners = new ArrayList();
                textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    private boolean handled = true;

                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        for (TextView.OnEditorActionListener onEditorActionListener : DCWXInput.this.mEditorActionListeners) {
                            if (onEditorActionListener != null) {
                                this.handled = onEditorActionListener.onEditorAction(textView, i, keyEvent) & this.handled;
                            }
                        }
                        return this.handled;
                    }
                });
            }
            this.mEditorActionListeners.add(onEditorActionListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void addTextChangedListener(TextWatcher textWatcher) {
        if (this.mTextChangedListeners == null) {
            this.mTextChangedListeners = new ArrayList();
        }
        this.mTextChangedListeners.add(textWatcher);
    }

    private void addKeyboardListener(final WXEditText wXEditText) {
        if (wXEditText != null && (wXEditText.getContext() instanceof Activity)) {
            ((WXEditText) getHostView()).setkeyBoardHeightChangeListener(new DCEditText.OnKeyboardHeightChangeListener() {
                public void onChange(boolean z, int i) {
                    if (DCWXInput.this.getInstance() != null && !DCWXInput.this.getInstance().isDestroy() && wXEditText != null) {
                        DCWXInput dCWXInput = DCWXInput.this;
                        dCWXInput.keyboardHeight = (float) ((int) WXViewUtils.getWebPxByWidth((float) i, dCWXInput.getInstance().getInstanceViewPortWidthWithFloat()));
                        HashMap hashMap = new HashMap(2);
                        hashMap.put("height", Float.valueOf(DCWXInput.this.keyboardHeight));
                        hashMap.put("duration", 0);
                        HashMap hashMap2 = new HashMap(1);
                        hashMap2.put("detail", hashMap);
                        DCWXInput.this.fireEvent("keyboardheightchange", hashMap2);
                        if (wXEditText.isFocused()) {
                            String str = DCWXInput.this.isPassword ? Constants.Value.PASSWORD : DCWXInput.this.mType;
                            if (!z && !DCWXInput.this.isConfirmHold && !str.equalsIgnoreCase(Constants.Value.PASSWORD) && !DCKeyboardManager.getInstance().getFrontInputType().equals(Constants.Value.PASSWORD)) {
                                DCWXInput.this.blur();
                            }
                            if (z) {
                                DCKeyboardManager.getInstance().setFrontInputType(str);
                            }
                        }
                        if (z) {
                            DCWXInput dCWXInput2 = DCWXInput.this;
                            dCWXInput2.keyboardHeight = dCWXInput2.keyboardHeight;
                        }
                    }
                }
            });
        }
    }

    public void updateProperties(Map<String, Object> map) {
        String str;
        if (map != null && map.size() > 0) {
            setType(map.containsKey("type") ? String.valueOf(map.get("type")) : this.mType);
            String str2 = this.mType;
            if (str2 != null && str2.equals("text") && map.containsKey("confirmType")) {
                if (map.containsKey("confirmType")) {
                    str = String.valueOf(map.get("confirmType"));
                } else {
                    str = this.mReturnKeyType;
                    if (str == null) {
                        str = "done";
                    }
                }
                setReturnKeyType(str);
            }
            WXAttr attrs = getAttrs();
            String str3 = Constants.Value.PASSWORD;
            if (attrs.containsKey(str3)) {
                boolean booleanValue = WXUtils.getBoolean(getAttrs().get(str3), false).booleanValue();
                this.isPassword = booleanValue;
                if (!booleanValue) {
                    str3 = this.mType;
                }
                if (str3 != null && getHostView() != null) {
                    ((EditText) getHostView()).setInputType(getInputType(str3));
                } else {
                    return;
                }
            }
            if (map.containsKey("cursor")) {
                this.cursor = WXUtils.getInteger(map.get("cursor"), 0).intValue();
            }
            if (map.containsKey(Constants.Name.SELECTION_START)) {
                this.selectionStart = WXUtils.getInteger(map.get(Constants.Name.SELECTION_START), Integer.MAX_VALUE).intValue();
            }
            if (map.containsKey(Constants.Name.SELECTION_END)) {
                this.selectionEnd = WXUtils.getInteger(map.get(Constants.Name.SELECTION_END), Integer.MAX_VALUE).intValue();
            }
            if (map.containsKey(Constants.Name.PLACEHOLDER)) {
                setPlaceholder(WXUtils.getString(map.get(Constants.Name.PLACEHOLDER), ""));
            }
            if (map.containsKey("placeholderClass")) {
                this.placeholderStyle.putAll((JSONObject) JSONObject.parse(WXUtils.getString(map.get("placeholderClass"), "{}")));
            }
            if (map.containsKey("placeholderStyle")) {
                this.placeholderStyle.putAll((JSONObject) JSONObject.parse(WXUtils.getString(map.get("placeholderStyle"), "{}")));
            }
            if (map.containsKey("adjustPosition")) {
                setAdjustPosition(map.get("adjustPosition"));
            }
        }
        super.updateProperties(map);
    }

    public void destroy() {
        super.destroy();
        List<TextView.OnEditorActionListener> list = this.mEditorActionListeners;
        if (list != null) {
            list.clear();
        }
        List<TextWatcher> list2 = this.mTextChangedListeners;
        if (list2 != null) {
            list2.clear();
        }
        if (this.mTextChangedEventDispatcher != null) {
            this.mTextChangedEventDispatcher = null;
        }
        if (getHostView() != null) {
            ((WXEditText) getHostView()).destroy();
        }
    }

    private PatternWrapper parseToPattern(String str, String str2) {
        Pattern pattern;
        if (str == null || str2 == null) {
            return null;
        }
        if (!Pattern.compile("/[\\S]+/[i]?[m]?[g]?").matcher(str).matches()) {
            WXLogUtils.w("WXInput", "Illegal js pattern syntax: " + str);
            return null;
        }
        int i = 0;
        String substring = str.substring(str.lastIndexOf("/") + 1);
        String substring2 = str.substring(str.indexOf("/") + 1, str.lastIndexOf("/"));
        if (substring.contains(ContextChain.TAG_INFRA)) {
            i = 2;
        }
        if (substring.contains(WXComponent.PROP_FS_MATCH_PARENT)) {
            i |= 32;
        }
        boolean contains = substring.contains("g");
        try {
            pattern = Pattern.compile(substring2, i);
        } catch (PatternSyntaxException unused) {
            WXLogUtils.w("WXInput", "Pattern syntax error: " + substring2);
            pattern = null;
        }
        if (pattern == null) {
            return null;
        }
        PatternWrapper patternWrapper = new PatternWrapper();
        boolean unused2 = patternWrapper.global = contains;
        Pattern unused3 = patternWrapper.matcher = pattern;
        String unused4 = patternWrapper.replace = str2;
        return patternWrapper;
    }

    private static class PatternWrapper {
        /* access modifiers changed from: private */
        public boolean global;
        /* access modifiers changed from: private */
        public Pattern matcher;
        /* access modifiers changed from: private */
        public String replace;

        private PatternWrapper() {
            this.global = false;
        }
    }

    public static class TextFormatter {
        private PatternWrapper format;
        private PatternWrapper recover;

        private TextFormatter(PatternWrapper patternWrapper, PatternWrapper patternWrapper2) {
            this.format = patternWrapper;
            this.recover = patternWrapper2;
        }

        /* access modifiers changed from: package-private */
        public String format(String str) {
            try {
                PatternWrapper patternWrapper = this.format;
                if (patternWrapper != null) {
                    if (patternWrapper.global) {
                        return this.format.matcher.matcher(str).replaceAll(this.format.replace);
                    }
                    return this.format.matcher.matcher(str).replaceFirst(this.format.replace);
                }
            } catch (Throwable th) {
                WXLogUtils.w("WXInput", "[format] " + th.getMessage());
            }
            return str;
        }

        /* access modifiers changed from: package-private */
        public String recover(String str) {
            try {
                PatternWrapper patternWrapper = this.recover;
                if (patternWrapper != null) {
                    if (patternWrapper.global) {
                        return this.recover.matcher.matcher(str).replaceAll(this.recover.replace);
                    }
                    return this.recover.matcher.matcher(str).replaceFirst(this.recover.replace);
                }
            } catch (Throwable th) {
                WXLogUtils.w("WXInput", "[formatted] " + th.getMessage());
            }
            return str;
        }
    }
}
