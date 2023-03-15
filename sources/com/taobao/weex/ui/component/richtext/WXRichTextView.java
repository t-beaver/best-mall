package com.taobao.weex.ui.component.richtext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import com.taobao.weex.ui.component.richtext.span.ImgSpan;
import com.taobao.weex.ui.view.WXTextView;

public class WXRichTextView extends WXTextView {
    public WXRichTextView(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        if (((!isEnabled() || getTextLayout() == null || !(getText() instanceof Spannable)) ? false : updateSelection(motionEvent, (Spannable) getText())) || onTouchEvent) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        super.verifyDrawable(drawable);
        return true;
    }

    public void setTextLayout(Layout layout) {
        super.setTextLayout(layout);
        if (layout.getText() instanceof Spanned) {
            Spanned spanned = (Spanned) layout.getText();
            ImgSpan[] imgSpanArr = (ImgSpan[]) spanned.getSpans(0, spanned.length(), ImgSpan.class);
            if (imgSpanArr != null) {
                for (ImgSpan view : imgSpanArr) {
                    view.setView(this);
                }
            }
        }
    }

    private boolean updateSelection(MotionEvent motionEvent, Spannable spannable) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 0) {
            int x = ((int) motionEvent.getX()) - getPaddingLeft();
            int y = ((int) motionEvent.getY()) - getPaddingTop();
            int scrollX = x + getScrollX();
            int scrollY = y + getScrollY();
            Layout textLayout = getTextLayout();
            int offsetForHorizontal = textLayout.getOffsetForHorizontal(textLayout.getLineForVertical(scrollY), (float) scrollX);
            ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannable.getSpans(offsetForHorizontal, offsetForHorizontal, ClickableSpan.class);
            if (clickableSpanArr.length != 0) {
                if (actionMasked == 1) {
                    clickableSpanArr[0].onClick(this);
                } else {
                    Selection.setSelection(spannable, spannable.getSpanStart(clickableSpanArr[0]), spannable.getSpanEnd(clickableSpanArr[0]));
                }
                return true;
            }
            Selection.removeSelection(spannable);
        }
        return false;
    }
}
