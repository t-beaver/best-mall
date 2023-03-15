package io.dcloud.common.adapter.ui;

import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.RecordView;
import java.util.regex.Pattern;

public class CustomeizedInputConnection extends InputConnectionWrapper {
    static Pattern patternEmail = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
    static Pattern patternID15 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
    static Pattern patternID18 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
    static Pattern patternPhone = Pattern.compile("^1[0-9]{10}$");
    public static int sDefaultInputType = -1;
    String composingText = null;
    int mCursorPos = -1;
    StringBuffer mInputString = new StringBuffer();
    int mInputType = -1;
    CustomeizedInputConnection mLastCustomeizedInputConnection = null;
    InputConnection mTarget;
    IWebview mWebview = null;

    public CustomeizedInputConnection(IWebview iWebview, InputConnection inputConnection, EditorInfo editorInfo, CustomeizedInputConnection customeizedInputConnection) {
        super(inputConnection, false);
        this.mTarget = inputConnection;
        this.mWebview = iWebview;
        this.mLastCustomeizedInputConnection = customeizedInputConnection;
        checkLastInputConnection();
        recordInputType(editorInfo);
        recordText();
        clearComposingText();
    }

    private void clearComposingText() {
        this.composingText = null;
    }

    private void log(String str, String str2) {
    }

    private boolean needRecord(int i, String str) {
        if (i == 1) {
            return patternPhone.matcher(str).find();
        }
        if (i == 2) {
            return patternEmail.matcher(str).find();
        }
        if (i != 7) {
            return RecordView.Utils.needRecord(i);
        }
        if (str.length() == 18 && patternID18.matcher(str).find()) {
            return true;
        }
        if (str.length() != 15 || !patternID15.matcher(str).find()) {
            return false;
        }
        return true;
    }

    private void recordInputType(EditorInfo editorInfo) {
        int i = sDefaultInputType;
        this.mInputType = i;
        int i2 = editorInfo.inputType;
        int i3 = i2 & 15;
        if (i3 == 1) {
            int i4 = i2 & 4080;
            if (i4 == 32 || i4 == 208) {
                this.mInputType = 2;
            }
        } else if (i3 != 3) {
            this.mInputType = i;
        } else {
            this.mInputType = 1;
        }
        log("AssistantInput", "recordInputType mInputType =" + this.mInputType + ";typeClass=" + i3);
        RecordView recordView = AdaWebview.mRecordView;
        if (recordView != null && recordView.mShowed) {
            showRecordView(recordView.mAnchorY, this.mInputType);
        }
    }

    private void recordText() {
        CharSequence textBeforeCursor = super.getTextBeforeCursor(255, 0);
        if (textBeforeCursor != null && textBeforeCursor.length() > 0) {
            StringBuffer stringBuffer = this.mInputString;
            stringBuffer.delete(0, stringBuffer.length());
            CharSequence textAfterCursor = super.getTextAfterCursor(255, 0);
            this.mCursorPos = textBeforeCursor.length();
            StringBuffer stringBuffer2 = this.mInputString;
            stringBuffer2.append(textBeforeCursor);
            stringBuffer2.append(textAfterCursor);
            log("AssistantInput", "recordText mCursorPos=" + this.mCursorPos + ";mInputString=" + this.mInputString);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkLastInputConnection() {
        CustomeizedInputConnection customeizedInputConnection = this.mLastCustomeizedInputConnection;
        if (customeizedInputConnection != null && Build.VERSION.SDK_INT >= 24) {
            customeizedInputConnection.finishComposingText();
        }
    }

    public void closeRecordView() {
        RecordView recordView = AdaWebview.mRecordView;
        if (recordView != null) {
            recordView.update(recordView.mAnchorY, -1);
            AdaWebview.mRecordView.conceal();
            AdaWebview.mRecordView.mShowed = false;
        }
    }

    public boolean commitText(CharSequence charSequence, int i) {
        recordText();
        int i2 = this.mCursorPos;
        if (i2 < 0 || i2 >= this.mInputString.length()) {
            this.mInputString.append(charSequence);
        } else {
            this.mInputString.insert(this.mCursorPos, charSequence);
        }
        log("AssistantInput", "commitText mCursorPos=" + this.mCursorPos + ";msg=" + this.mInputString);
        return super.commitText(charSequence, i);
    }

    public boolean finishComposingText() {
        String stringBuffer = this.mInputString.toString();
        if (!TextUtils.isEmpty(stringBuffer)) {
            if (!TextUtils.isEmpty(this.composingText) && stringBuffer.contains(this.composingText)) {
                int indexOf = stringBuffer.indexOf(this.composingText);
                stringBuffer = stringBuffer.substring(0, indexOf) + stringBuffer.substring(indexOf + this.composingText.length());
            }
            String trim = stringBuffer.trim();
            log("AssistantInput", "finishComposingText mInputType=" + this.mInputType + ";msg=" + trim);
            if (AdaWebview.mRecordView != null && needRecord(this.mInputType, trim)) {
                AdaWebview.mRecordView.record(trim, this.mInputType);
                int i = this.mInputType;
                if (i != -1 && i == AdaWebview.sCustomeizedInputConnection.mInputType) {
                    RecordView recordView = AdaWebview.mRecordView;
                    recordView.update(recordView.mAnchorY, i);
                }
                StringBuffer stringBuffer2 = this.mInputString;
                stringBuffer2.delete(0, stringBuffer2.length());
            }
        }
        InputConnection inputConnection = this.mTarget;
        if (inputConnection != null) {
            return inputConnection.finishComposingText();
        }
        return true;
    }

    public CharSequence getTextBeforeCursor(int i, int i2) {
        log("AssistantInput", "getTextBeforeCursor mCursorPos=" + this.mCursorPos + ";n=" + i);
        recordText();
        clearComposingText();
        return super.getTextBeforeCursor(i, i2);
    }

    public boolean setComposingText(CharSequence charSequence, int i) {
        this.composingText = charSequence.toString();
        log("AssistantInput", "setComposingText mCursorPos=" + this.mCursorPos + ";msg=" + charSequence);
        return super.setComposingText(charSequence, i);
    }

    public boolean setSelection(int i, int i2) {
        if (i == i2) {
            this.mCursorPos = i;
        }
        return super.setSelection(i, i2);
    }

    public void setText(String str) {
        clearComposingText();
        if (TextUtils.equals(Build.MODEL, "ZTE B880")) {
            StringBuffer stringBuffer = this.mInputString;
            stringBuffer.delete(0, stringBuffer.length());
            this.mCursorPos = str.length();
            this.mInputString.append(str);
            deleteSurroundingText(-2147483647, Integer.MAX_VALUE);
            super.setSelection(0, Integer.MAX_VALUE);
            super.commitText(str, str.length());
            return;
        }
        recordText();
        deleteSurroundingText(-2147483647, Integer.MAX_VALUE);
        super.setSelection(0, Integer.MAX_VALUE);
        super.commitText(str, str.length());
    }

    public void showRecordView(int i, boolean z) {
        showRecordView(i, !z ? -1 : this.mInputType);
    }

    public void update(int i) {
        super.sendKeyEvent(new KeyEvent(0, i > 0 ? 22 : 21));
    }

    /* access modifiers changed from: package-private */
    public void updateInputType(int i) {
        this.mInputType = i;
    }

    public void showRecordView(int i, int i2) {
        if (this.mWebview.obtainApp() != null) {
            if (AdaWebview.mRecordView == null) {
                AdaWebview.mRecordView = new RecordView(this.mWebview.getContext(), (ViewGroup) this.mWebview.obtainApp().obtainWebAppRootView().obtainMainView(), this.mWebview.obtainApp().obtainAppId());
            }
            AdaWebview.mRecordView.update(i, i2);
            AdaWebview.mRecordView.mShowed = true;
        }
    }
}
