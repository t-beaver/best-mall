package io.dcloud.common.core.ui.keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.EditText;
import io.dcloud.common.core.ui.DCKeyboardManager;

public class DCEditText extends EditText {
    float mCursorSpacing = 0.0f;
    String mInputMode = DCKeyboardManager.SOFT_INPUT_MODE_ADJUST_PAN;
    String mInstanceId;
    OnKeyboardHeightChangeListener mKeyboardHeightChangeListener;

    public interface OnKeyboardHeightChangeListener {
        void onChange(boolean z, int i);
    }

    public DCEditText(Context context, String str) {
        super(context);
        this.mInstanceId = str;
    }

    public void destroy() {
        clearFocus();
        if (DCKeyboardManager.getInstance().getNativeInput() == this) {
            DCKeyboardManager.getInstance().setNativeInput((View) null, 0.0f);
        }
    }

    public OnKeyboardHeightChangeListener getKeyboardHeightChangeListener() {
        return this.mKeyboardHeightChangeListener;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        DCKeyboardManager.getInstance().nativeEditTextFocus(this.mInstanceId, this, z, this.mInputMode, this.mCursorSpacing);
    }

    public void setCursorSpacing(float f) {
        this.mCursorSpacing = f;
    }

    public void setInputSoftMode(String str) {
        this.mInputMode = str;
    }

    public void setkeyBoardHeightChangeListener(OnKeyboardHeightChangeListener onKeyboardHeightChangeListener) {
        this.mKeyboardHeightChangeListener = onKeyboardHeightChangeListener;
    }
}
