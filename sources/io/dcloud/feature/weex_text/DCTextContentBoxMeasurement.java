package io.dcloud.feature.weex_text;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import com.taobao.weex.dom.TextDecorationSpan;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXCustomStyleSpan;
import com.taobao.weex.dom.WXLineHeightSpan;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.layout.measurefunc.TextContentBoxMeasurement;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXTextDecoration;
import com.taobao.weex.utils.WXViewUtils;

public class DCTextContentBoxMeasurement extends TextContentBoxMeasurement {
    private boolean decode = false;
    private String space = "";

    public DCTextContentBoxMeasurement(WXComponent wXComponent) {
        super(wXComponent);
    }

    public void layoutBefore() {
        WXAttr attrs = this.mComponent.getAttrs();
        this.space = (String) attrs.get("space");
        this.decode = Boolean.valueOf(attrs.containsKey("decode") ? attrs.get("decode").toString() : AbsoluteConst.FALSE).booleanValue();
        super.layoutBefore();
    }

    /* access modifiers changed from: protected */
    public Spanned createSpanned(String str) {
        String str2 = this.space;
        if (str2 != null) {
            if (str2.equals("ensp")) {
                str = Html.fromHtml(str.replaceAll(Operators.SPACE_STR, "&ensp;")).toString();
            } else if (this.space.equals("emsp")) {
                str = Html.fromHtml(str.replaceAll(Operators.SPACE_STR, "&emsp;")).toString();
            }
        }
        return super.createSpanned(str);
    }

    /* access modifiers changed from: protected */
    public void updateSpannable(Spannable spannable, int i) {
        if (this.mComponent != null && this.mComponent.getInstance() != null) {
            int length = spannable.length();
            if (this.mFontSize == -1) {
                this.mTextPaint.setTextSize(WXViewUtils.getRealPxByWidth((float) this.mComponent.getInstance().getDefaultFontSize(), this.mComponent.getInstance().getInstanceViewPortWidthWithFloat()));
            } else {
                this.mTextPaint.setTextSize((float) this.mFontSize);
            }
            if (this.mLineHeight != -1) {
                setSpan(spannable, new WXLineHeightSpan(this.mLineHeight), 0, length, i);
            }
            setSpan(spannable, new AlignmentSpan.Standard(this.mAlignment), 0, length, i);
            if (!(this.mFontStyle == -1 && this.mFontWeight == -1 && this.mFontFamily == null)) {
                setSpan(spannable, new WXCustomStyleSpan(this.mFontStyle, this.mFontWeight, this.mFontFamily), 0, length, i);
            }
            if (this.mIsColorSet) {
                this.mTextPaint.setColor(this.mColor);
            }
            if (this.mTextDecoration == WXTextDecoration.UNDERLINE || this.mTextDecoration == WXTextDecoration.LINETHROUGH) {
                setSpan(spannable, new TextDecorationSpan(this.mTextDecoration), 0, length, i);
            }
        }
    }
}
