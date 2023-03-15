package com.taobao.weex.ui.component;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import com.alibaba.fastjson.JSONObject;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.layout.MeasureMode;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.helper.SoftKeyboardDetector;
import com.taobao.weex.ui.component.helper.WXTimeInputHelper;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.WXEditText;
import com.taobao.weex.utils.TypefaceUtil;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class AbstractEditComponent extends WXComponent<WXEditText> {
    private static final int MAX_TEXT_FORMAT_REPEAT = 3;
    private boolean mAutoFocus;
    /* access modifiers changed from: private */
    public String mBeforeText = "";
    /* access modifiers changed from: private */
    public int mEditorAction = 6;
    /* access modifiers changed from: private */
    public List<TextView.OnEditorActionListener> mEditorActionListeners;
    /* access modifiers changed from: private */
    public int mFormatRepeatCount = 0;
    /* access modifiers changed from: private */
    public TextFormatter mFormatter = null;
    /* access modifiers changed from: private */
    public boolean mIgnoreNextOnInputEvent = false;
    /* access modifiers changed from: private */
    public final InputMethodManager mInputMethodManager = ((InputMethodManager) getContext().getSystemService("input_method"));
    private boolean mKeepSelectionIndex = false;
    /* access modifiers changed from: private */
    public String mLastValue = "";
    private int mLineHeight = -1;
    /* access modifiers changed from: private */
    public boolean mListeningKeyboard = false;
    /* access modifiers changed from: private */
    public String mMax = null;
    /* access modifiers changed from: private */
    public String mMin = null;
    private WXComponent.OnClickListener mOnClickListener = new WXComponent.OnClickListener() {
        public void onHostViewClick() {
            String access$100 = AbstractEditComponent.this.mType;
            access$100.hashCode();
            if (access$100.equals("date")) {
                AbstractEditComponent.this.hideSoftKeyboard();
                if (AbstractEditComponent.this.getParent() != null) {
                    AbstractEditComponent.this.getParent().interceptFocus();
                }
                WXTimeInputHelper.pickDate(AbstractEditComponent.this.mMax, AbstractEditComponent.this.mMin, AbstractEditComponent.this);
            } else if (access$100.equals(Constants.Value.TIME)) {
                AbstractEditComponent.this.hideSoftKeyboard();
                if (AbstractEditComponent.this.getParent() != null) {
                    AbstractEditComponent.this.getParent().interceptFocus();
                }
                WXTimeInputHelper.pickTime(AbstractEditComponent.this);
            }
        }
    };
    private TextPaint mPaint = new TextPaint();
    /* access modifiers changed from: private */
    public String mReturnKeyType = null;
    private TextWatcher mTextChangedEventDispatcher;
    /* access modifiers changed from: private */
    public List<TextWatcher> mTextChangedListeners;
    /* access modifiers changed from: private */
    public String mType = "text";
    private SoftKeyboardDetector.Unregister mUnregister;

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

    public AbstractEditComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                if (CSSConstants.isUndefined(f) || i == MeasureMode.UNSPECIFIED) {
                    f = 0.0f;
                }
                this.mMeasureWidth = f;
                this.mMeasureHeight = AbstractEditComponent.this.getMeasureHeight();
            }

            public void layoutBefore() {
                AbstractEditComponent.this.updateStyleAndAttrs();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void layoutDirectionDidChanged(boolean z) {
        int textAlign = getTextAlign((String) getStyles().get(Constants.Name.TEXT_ALIGN));
        if (textAlign <= 0) {
            textAlign = GravityCompat.START;
        }
        if (getHostView() instanceof WXEditText) {
            ((WXEditText) getHostView()).setGravity(textAlign | getVerticalGravity());
        }
    }

    /* access modifiers changed from: protected */
    public final float getMeasuredLineHeight() {
        int i = this.mLineHeight;
        return (i == -1 || i <= 0) ? this.mPaint.getFontMetrics((Paint.FontMetrics) null) : (float) i;
    }

    /* access modifiers changed from: protected */
    public float getMeasureHeight() {
        return getMeasuredLineHeight();
    }

    /* access modifiers changed from: protected */
    public void updateStyleAndAttrs() {
        if (getStyles().size() > 0) {
            String str = null;
            int fontSize = getStyles().containsKey(Constants.Name.FONT_SIZE) ? WXStyle.getFontSize(getStyles(), getInstance().getDefaultFontSize(), getViewPortWidthForFloat()) : -1;
            if (getStyles().containsKey(Constants.Name.FONT_FAMILY)) {
                str = WXStyle.getFontFamily(getStyles());
            }
            int fontStyle = getStyles().containsKey(Constants.Name.FONT_STYLE) ? WXStyle.getFontStyle(getStyles()) : -1;
            int fontWeight = getStyles().containsKey(Constants.Name.FONT_WEIGHT) ? WXStyle.getFontWeight(getStyles()) : -1;
            int lineHeight = WXStyle.getLineHeight(getStyles(), getViewPortWidthForFloat());
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
        return wXEditText;
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(WXEditText wXEditText) {
        super.onHostViewInitialized(wXEditText);
        addFocusChangeListener(new WXComponent.OnFocusChangeListener() {
            public void onFocusChange(boolean z) {
                if (!z) {
                    AbstractEditComponent.this.decideSoftKeyboard();
                }
                AbstractEditComponent.this.setPseudoClassStatus(Constants.PSEUDO.FOCUS, z);
            }
        });
        addKeyboardListener(wXEditText);
    }

    /* access modifiers changed from: protected */
    public boolean isConsumeTouch() {
        return !isDisabled();
    }

    private void applyOnClickListener() {
        addClickListener(this.mOnClickListener);
    }

    /* access modifiers changed from: protected */
    public void appleStyleAfterCreated(final WXEditText wXEditText) {
        int textAlign = getTextAlign((String) getStyles().get(Constants.Name.TEXT_ALIGN));
        if (textAlign <= 0) {
            textAlign = GravityCompat.START;
        }
        wXEditText.setGravity(textAlign | getVerticalGravity());
        int color = WXResourceUtils.getColor("#999999");
        if (color != Integer.MIN_VALUE) {
            wXEditText.setHintTextColor(color);
        }
        AnonymousClass4 r0 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AbstractEditComponent.this.mTextChangedListeners != null) {
                    for (TextWatcher beforeTextChanged : AbstractEditComponent.this.mTextChangedListeners) {
                        beforeTextChanged.beforeTextChanged(charSequence, i, i2, i3);
                    }
                }
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AbstractEditComponent.this.mFormatter != null) {
                    String access$800 = AbstractEditComponent.this.mFormatter.format(AbstractEditComponent.this.mFormatter.recover(charSequence.toString()));
                    if (access$800.equals(charSequence.toString()) || AbstractEditComponent.this.mFormatRepeatCount >= 3) {
                        int unused = AbstractEditComponent.this.mFormatRepeatCount = 0;
                    } else {
                        AbstractEditComponent abstractEditComponent = AbstractEditComponent.this;
                        int unused2 = abstractEditComponent.mFormatRepeatCount = abstractEditComponent.mFormatRepeatCount + 1;
                        int length = AbstractEditComponent.this.mFormatter.format(AbstractEditComponent.this.mFormatter.recover(charSequence.subSequence(0, wXEditText.getSelectionStart()).toString())).length();
                        wXEditText.setText(access$800);
                        wXEditText.setSelection(length);
                        return;
                    }
                }
                if (AbstractEditComponent.this.mTextChangedListeners != null) {
                    for (TextWatcher onTextChanged : AbstractEditComponent.this.mTextChangedListeners) {
                        onTextChanged.onTextChanged(charSequence, i, i2, i3);
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                if (AbstractEditComponent.this.mTextChangedListeners != null) {
                    for (TextWatcher afterTextChanged : AbstractEditComponent.this.mTextChangedListeners) {
                        afterTextChanged.afterTextChanged(editable);
                    }
                }
            }
        };
        this.mTextChangedEventDispatcher = r0;
        wXEditText.addTextChangedListener(r0);
        wXEditText.setTextSize(0, (float) WXStyle.getFontSize(getStyles(), getInstance().getDefaultFontSize(), getInstance().getInstanceViewPortWidthWithFloat()));
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (getHostView() != null && !TextUtils.isEmpty(str)) {
            final TextView textView = (TextView) getHostView();
            if (str.equals(Constants.Event.CHANGE)) {
                addFocusChangeListener(new WXComponent.OnFocusChangeListener() {
                    public void onFocusChange(boolean z) {
                        if (z) {
                            String unused = AbstractEditComponent.this.mLastValue = textView.getText().toString();
                            return;
                        }
                        CharSequence text = textView.getText();
                        if (text == null) {
                            text = "";
                        }
                        if (!text.toString().equals(AbstractEditComponent.this.mLastValue)) {
                            AbstractEditComponent.this.fireEvent(Constants.Event.CHANGE, text.toString());
                            String unused2 = AbstractEditComponent.this.mLastValue = textView.getText().toString();
                        }
                    }
                });
                addEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i != AbstractEditComponent.this.mEditorAction) {
                            return false;
                        }
                        CharSequence text = textView.getText();
                        if (text == null) {
                            text = "";
                        }
                        if (!text.toString().equals(AbstractEditComponent.this.mLastValue)) {
                            AbstractEditComponent.this.fireEvent(Constants.Event.CHANGE, text.toString());
                            String unused = AbstractEditComponent.this.mLastValue = textView.getText().toString();
                        }
                        if (AbstractEditComponent.this.getParent() != null) {
                            AbstractEditComponent.this.getParent().interceptFocus();
                        }
                        AbstractEditComponent.this.hideSoftKeyboard();
                        return true;
                    }
                });
            } else if (str.equals("input")) {
                addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable editable) {
                    }

                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (AbstractEditComponent.this.mIgnoreNextOnInputEvent) {
                            boolean unused = AbstractEditComponent.this.mIgnoreNextOnInputEvent = false;
                            String unused2 = AbstractEditComponent.this.mBeforeText = charSequence.toString();
                        } else if (!AbstractEditComponent.this.mBeforeText.equals(charSequence.toString())) {
                            String unused3 = AbstractEditComponent.this.mBeforeText = charSequence.toString();
                            AbstractEditComponent.this.fireEvent("input", charSequence.toString());
                        }
                    }
                });
            }
            if (Constants.Event.RETURN.equals(str)) {
                addEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i != AbstractEditComponent.this.mEditorAction) {
                            return false;
                        }
                        HashMap hashMap = new HashMap(2);
                        hashMap.put(Constants.Name.RETURN_KEY_TYPE, AbstractEditComponent.this.mReturnKeyType);
                        hashMap.put("value", textView.getText().toString());
                        AbstractEditComponent.this.fireEvent(Constants.Event.RETURN, hashMap);
                        return true;
                    }
                });
            }
            if (Constants.Event.KEYBOARD.equals(str)) {
                this.mListeningKeyboard = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void fireEvent(String str, String str2) {
        if (str != null) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("value", str2);
            hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            hashMap3.put("value", str2);
            hashMap2.put(TemplateDom.KEY_ATTRS, hashMap3);
            WXSDKManager.getInstance().fireEvent(getInstanceId(), getRef(), str, hashMap, hashMap2);
        }
    }

    public void performOnChange(String str) {
        if (getEvents() != null) {
            WXEvent events = getEvents();
            String str2 = Constants.Event.CHANGE;
            if (!events.contains(str2)) {
                str2 = null;
            }
            fireEvent(str2, str);
        }
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1898657397:
                if (str.equals(Constants.Name.KEEP_SELECTION_INDEX)) {
                    c = 0;
                    break;
                }
                break;
            case -1576785488:
                if (str.equals(Constants.Name.PLACEHOLDER_COLOR)) {
                    c = 1;
                    break;
                }
                break;
            case -1065511464:
                if (str.equals(Constants.Name.TEXT_ALIGN)) {
                    c = 2;
                    break;
                }
                break;
            case -791400086:
                if (str.equals(Constants.Name.MAX_LENGTH)) {
                    c = 3;
                    break;
                }
                break;
            case 107876:
                if (str.equals("max")) {
                    c = 4;
                    break;
                }
                break;
            case 108114:
                if (str.equals(Constants.Name.MIN)) {
                    c = 5;
                    break;
                }
                break;
            case 3575610:
                if (str.equals("type")) {
                    c = 6;
                    break;
                }
                break;
            case 94842723:
                if (str.equals("color")) {
                    c = 7;
                    break;
                }
                break;
            case 102977279:
                if (str.equals(Constants.Name.LINES)) {
                    c = 8;
                    break;
                }
                break;
            case 124732746:
                if (str.equals(Constants.Name.MAXLENGTH)) {
                    c = 9;
                    break;
                }
                break;
            case 270940796:
                if (str.equals(Constants.Name.DISABLED)) {
                    c = 10;
                    break;
                }
                break;
            case 365601008:
                if (str.equals(Constants.Name.FONT_SIZE)) {
                    c = 11;
                    break;
                }
                break;
            case 598246771:
                if (str.equals(Constants.Name.PLACEHOLDER)) {
                    c = 12;
                    break;
                }
                break;
            case 914346044:
                if (str.equals(Constants.Name.SINGLELINE)) {
                    c = 13;
                    break;
                }
                break;
            case 947486441:
                if (str.equals(Constants.Name.RETURN_KEY_TYPE)) {
                    c = 14;
                    break;
                }
                break;
            case 1625554645:
                if (str.equals(Constants.Name.ALLOW_COPY_PASTE)) {
                    c = 15;
                    break;
                }
                break;
            case 1667607689:
                if (str.equals(Constants.Name.AUTOFOCUS)) {
                    c = 16;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.mKeepSelectionIndex = WXUtils.getBoolean(obj, false).booleanValue();
                return true;
            case 1:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setPlaceholderColor(string);
                }
                return true;
            case 2:
                String string2 = WXUtils.getString(obj, (String) null);
                if (string2 != null) {
                    setTextAlign(string2);
                }
                return true;
            case 3:
                Integer integer = WXUtils.getInteger(obj, (Integer) null);
                if (integer != null) {
                    setMaxLength(integer.intValue());
                }
                return true;
            case 4:
                setMax(String.valueOf(obj));
                return true;
            case 5:
                setMin(String.valueOf(obj));
                return true;
            case 6:
                String string3 = WXUtils.getString(obj, (String) null);
                if (string3 != null) {
                    setType(string3);
                }
                return true;
            case 7:
                String string4 = WXUtils.getString(obj, (String) null);
                if (string4 != null) {
                    setColor(string4);
                }
                return true;
            case 8:
                Integer integer2 = WXUtils.getInteger(obj, (Integer) null);
                if (integer2 != null) {
                    setLines(integer2.intValue());
                }
                return true;
            case 9:
                Integer integer3 = WXUtils.getInteger(obj, (Integer) null);
                if (integer3 != null) {
                    setMaxLength(integer3.intValue());
                }
                return true;
            case 10:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (!(bool == null || this.mHost == null)) {
                    if (bool.booleanValue()) {
                        ((WXEditText) this.mHost).setFocusable(false);
                        ((WXEditText) this.mHost).setFocusableInTouchMode(false);
                    } else {
                        ((WXEditText) this.mHost).setFocusableInTouchMode(true);
                        ((WXEditText) this.mHost).setFocusable(true);
                    }
                }
                return true;
            case 11:
                String string5 = WXUtils.getString(obj, (String) null);
                if (string5 != null) {
                    setFontSize(string5);
                }
                return true;
            case 12:
                String string6 = WXUtils.getString(obj, (String) null);
                if (string6 != null) {
                    setPlaceholder(string6);
                }
                return true;
            case 13:
                Boolean bool2 = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool2 != null) {
                    setSingleLine(bool2.booleanValue());
                }
                return true;
            case 14:
                setReturnKeyType(String.valueOf(obj));
                return true;
            case 15:
                boolean booleanValue = WXUtils.getBoolean(obj, true).booleanValue();
                if (getHostView() != null) {
                    ((WXEditText) getHostView()).setAllowCopyPaste(booleanValue);
                }
                return true;
            case 16:
                Boolean bool3 = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool3 != null) {
                    setAutofocus(bool3.booleanValue());
                }
                return true;
            default:
                return super.setProperty(str, obj);
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

    @WXComponentProp(name = "placeholder")
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

    @WXComponentProp(name = "type")
    public void setType(String str) {
        Log.e("weex", "setType=" + str);
        if (str != null && getHostView() != null && !this.mType.equals(str)) {
            this.mType = str;
            ((EditText) getHostView()).setInputType(getInputType(this.mType));
            String str2 = this.mType;
            str2.hashCode();
            if (str2.equals("date") || str2.equals(Constants.Value.TIME)) {
                applyOnClickListener();
            }
        }
    }

    @WXComponentProp(name = "autofocus")
    public void setAutofocus(boolean z) {
        if (getHostView() != null) {
            this.mAutoFocus = z;
            EditText editText = (EditText) getHostView();
            if (this.mAutoFocus) {
                editText.setFocusable(true);
                editText.requestFocus();
                editText.setFocusableInTouchMode(true);
                showSoftKeyboard();
                return;
            }
            hideSoftKeyboard();
        }
    }

    @WXComponentProp(name = "value")
    public void setValue(String str) {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && !TextUtils.equals(wXEditText.getText(), str)) {
            this.mIgnoreNextOnInputEvent = true;
            int selectionStart = wXEditText.getSelectionStart();
            wXEditText.setText(str);
            if (!this.mKeepSelectionIndex) {
                selectionStart = str.length();
            }
            if (str == null) {
                selectionStart = 0;
            }
            wXEditText.setSelection(selectionStart);
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

    @WXComponentProp(name = "textAlign")
    public void setTextAlign(String str) {
        int textAlign = getTextAlign(str);
        if (textAlign > 0) {
            ((WXEditText) getHostView()).setGravity(textAlign | getVerticalGravity());
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
            case 96619420:
                if (str.equals("email")) {
                    c = 6;
                    break;
                }
                break;
            case 1216985755:
                if (str.equals(Constants.Value.PASSWORD)) {
                    c = 7;
                    break;
                }
                break;
            case 1793702779:
                if (str.equals(Constants.Value.DATETIME)) {
                    c = 8;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 8194;
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
                return 33;
            case 7:
                if (getHostView() == null) {
                    return 129;
                }
                ((WXEditText) getHostView()).setTransformationMethod(PasswordTransformationMethod.getInstance());
                return 129;
            case 8:
                return 4;
            default:
                return 1;
        }
        return 0;
    }

    @WXComponentProp(name = "max")
    public void setMax(String str) {
        this.mMax = str;
    }

    @WXComponentProp(name = "min")
    public void setMin(String str) {
        this.mMin = str;
    }

    private boolean showSoftKeyboard() {
        if (getHostView() == null) {
            return false;
        }
        ((WXEditText) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
            public void run() {
                AbstractEditComponent.this.mInputMethodManager.showSoftInput(AbstractEditComponent.this.getHostView(), 1);
            }
        }), 100);
        return true;
    }

    /* access modifiers changed from: private */
    public void hideSoftKeyboard() {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    AbstractEditComponent.this.mInputMethodManager.hideSoftInputFromWindow(((WXEditText) AbstractEditComponent.this.getHostView()).getWindowToken(), 0);
                }
            }), 16);
        }
    }

    private int getTextAlign(String str) {
        int i = isLayoutRTL() ? GravityCompat.END : GravityCompat.START;
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        if (str.equals("left")) {
            return GravityCompat.START;
        }
        if (str.equals("center")) {
            return 17;
        }
        if (str.equals("right")) {
            return GravityCompat.END;
        }
        return i;
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
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = getContext();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void decideSoftKeyboard() {
        /*
            r4 = this;
            android.view.View r0 = r4.getHostView()
            if (r0 == 0) goto L_0x001e
            android.content.Context r1 = r4.getContext()
            if (r1 == 0) goto L_0x001e
            boolean r2 = r1 instanceof android.app.Activity
            if (r2 == 0) goto L_0x001e
            com.taobao.weex.ui.component.AbstractEditComponent$11 r2 = new com.taobao.weex.ui.component.AbstractEditComponent$11
            r2.<init>(r1)
            java.lang.Runnable r1 = com.taobao.weex.common.WXThread.secure((java.lang.Runnable) r2)
            r2 = 16
            r0.postDelayed(r1, r2)
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.decideSoftKeyboard():void");
    }

    @JSMethod
    public void setSelectionRange(int i, int i2) {
        int length;
        EditText editText = (EditText) getHostView();
        if (editText != null && i <= (length = ((WXEditText) getHostView()).length()) && i2 <= length) {
            focus();
            editText.setSelection(i, i2);
        }
    }

    @JSMethod
    public void getSelectionRange(String str) {
        HashMap hashMap = new HashMap(2);
        EditText editText = (EditText) getHostView();
        if (editText != null) {
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            if (!editText.hasFocus()) {
                selectionStart = 0;
                selectionEnd = 0;
            }
            hashMap.put(Constants.Name.SELECTION_START, Integer.valueOf(selectionStart));
            hashMap.put(Constants.Name.SELECTION_END, Integer.valueOf(selectionEnd));
        }
        WXBridgeManager.getInstance().callback(getInstanceId(), str, hashMap, false);
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

    /* access modifiers changed from: protected */
    public final void addEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        TextView textView;
        if (onEditorActionListener != null && (textView = (TextView) getHostView()) != null) {
            if (this.mEditorActionListeners == null) {
                this.mEditorActionListeners = new ArrayList();
                textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    private boolean handled = true;

                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        for (TextView.OnEditorActionListener onEditorActionListener : AbstractEditComponent.this.mEditorActionListeners) {
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

    public final void addTextChangedListener(TextWatcher textWatcher) {
        if (this.mTextChangedListeners == null) {
            this.mTextChangedListeners = new ArrayList();
        }
        this.mTextChangedListeners.add(textWatcher);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
        r3 = r3.getContext();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addKeyboardListener(com.taobao.weex.ui.view.WXEditText r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L_0x0003
            return
        L_0x0003:
            android.content.Context r3 = r3.getContext()
            if (r3 == 0) goto L_0x001b
            boolean r0 = r3 instanceof android.app.Activity
            if (r0 == 0) goto L_0x001b
            r0 = r3
            android.app.Activity r0 = (android.app.Activity) r0
            com.taobao.weex.ui.component.AbstractEditComponent$13 r1 = new com.taobao.weex.ui.component.AbstractEditComponent$13
            r1.<init>(r3)
            com.taobao.weex.ui.component.helper.SoftKeyboardDetector$Unregister r3 = com.taobao.weex.ui.component.helper.SoftKeyboardDetector.registerKeyboardEventListener(r0, r1)
            r2.mUnregister = r3
        L_0x001b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.addKeyboardListener(com.taobao.weex.ui.view.WXEditText):void");
    }

    public void destroy() {
        super.destroy();
        if (getHostView() != null) {
            ((WXEditText) getHostView()).destroy();
        }
        SoftKeyboardDetector.Unregister unregister = this.mUnregister;
        if (unregister != null) {
            try {
                unregister.execute();
                this.mUnregister = null;
            } catch (Throwable th) {
                WXLogUtils.w("Unregister throw ", th);
            }
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

    private static class TextFormatter {
        private PatternWrapper format;
        private PatternWrapper recover;

        private TextFormatter(PatternWrapper patternWrapper, PatternWrapper patternWrapper2) {
            this.format = patternWrapper;
            this.recover = patternWrapper2;
        }

        /* access modifiers changed from: private */
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

        /* access modifiers changed from: private */
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
