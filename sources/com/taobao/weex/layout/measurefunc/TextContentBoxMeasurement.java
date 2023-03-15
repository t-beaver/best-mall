package com.taobao.weex.layout.measurefunc;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Looper;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.TextDecorationSpan;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXCustomStyleSpan;
import com.taobao.weex.dom.WXLineHeightSpan;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.layout.MeasureMode;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXTextDecoration;
import com.taobao.weex.utils.WXDomUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TextContentBoxMeasurement extends ContentBoxMeasurement {
    private static final Canvas DUMMY_CANVAS = new Canvas();
    private static final String ELLIPSIS = "…";
    /* access modifiers changed from: private */
    public AtomicReference<Layout> atomicReference = new AtomicReference<>();
    private boolean hasBeenMeasured = false;
    private Layout layout;
    protected Layout.Alignment mAlignment;
    protected int mColor;
    protected String mFontFamily = null;
    protected int mFontSize = -1;
    protected int mFontStyle = -1;
    protected int mFontWeight = -1;
    protected boolean mIsColorSet = false;
    protected int mLineHeight = -1;
    private int mNumberOfLines = -1;
    private String mText = null;
    protected WXTextDecoration mTextDecoration = WXTextDecoration.NONE;
    protected TextPaint mTextPaint;
    private float previousWidth = Float.NaN;
    private Spanned spanned;
    private TextUtils.TruncateAt textOverflow;

    public TextContentBoxMeasurement(WXComponent wXComponent) {
        super(wXComponent);
    }

    class SetSpanOperation {
        protected final int end;
        protected final int flag;
        protected final int start;
        protected final Object what;

        SetSpanOperation(TextContentBoxMeasurement textContentBoxMeasurement, int i, int i2, Object obj) {
            this(i, i2, obj, 17);
        }

        SetSpanOperation(int i, int i2, Object obj, int i3) {
            this.start = i;
            this.end = i2;
            this.what = obj;
            this.flag = i3;
        }

        public void execute(Spannable spannable) {
            spannable.setSpan(this.what, this.start, this.end, this.flag);
        }
    }

    public void layoutBefore() {
        this.mTextPaint = new TextPaint(1);
        this.hasBeenMeasured = false;
        updateStyleAndText();
        this.spanned = createSpanned(this.mText);
    }

    public void measureInternal(float f, float f2, int i, int i2) {
        boolean z = true;
        this.hasBeenMeasured = true;
        TextPaint textPaint = this.mTextPaint;
        if (i != MeasureMode.EXACTLY) {
            z = false;
        }
        float textWidth = getTextWidth(textPaint, f, z);
        if (textWidth <= 0.0f || this.spanned == null) {
            if (i == MeasureMode.UNSPECIFIED) {
                f = 0.0f;
            }
            if (i2 == MeasureMode.UNSPECIFIED) {
                f2 = 0.0f;
            }
        } else {
            Layout createLayout = createLayout(textWidth, (Layout) null);
            this.layout = createLayout;
            this.previousWidth = (float) createLayout.getWidth();
            if (Float.isNaN(f)) {
                r4 = (float) this.layout.getWidth();
            } else {
                r4 = Math.min((float) this.layout.getWidth(), f);
            }
            if (Float.isNaN(f2)) {
                f2 = (float) this.layout.getHeight();
            }
        }
        this.mMeasureWidth = r4;
        this.mMeasureHeight = f2;
    }

    public void layoutAfter(float f, float f2) {
        if (this.mComponent != null) {
            if (!this.hasBeenMeasured) {
                updateStyleAndText();
                recalculateLayout(f);
            } else if (!(this.layout == null || WXDomUtils.getContentWidth(this.mComponent.getPadding(), this.mComponent.getBorder(), f) == this.previousWidth)) {
                recalculateLayout(f);
            }
            this.hasBeenMeasured = false;
            Layout layout2 = this.layout;
            if (layout2 != null && !layout2.equals(this.atomicReference.get()) && Build.VERSION.SDK_INT >= 19 && Thread.currentThread() != Looper.getMainLooper().getThread()) {
                warmUpTextLayoutCache(this.layout);
            }
            swap();
            WXSDKManager.getInstance().getWXRenderManager().postOnUiThread((Runnable) new Runnable() {
                public void run() {
                    if (TextContentBoxMeasurement.this.mComponent != null) {
                        TextContentBoxMeasurement.this.mComponent.updateExtra(TextContentBoxMeasurement.this.atomicReference.get());
                    }
                }
            }, this.mComponent.getInstanceId());
        }
    }

    private void updateStyleAndText() {
        updateStyleImp(this.mComponent.getStyles());
        this.mText = WXAttr.getValue(this.mComponent.getAttrs());
    }

    public void forceRelayout() {
        layoutBefore();
        measure(this.previousWidth, Float.NaN, MeasureMode.EXACTLY, MeasureMode.UNSPECIFIED);
        layoutAfter(this.previousWidth, Float.NaN);
    }

    private void updateStyleImp(Map<String, Object> map) {
        if (map != null) {
            if (map.containsKey(Constants.Name.LINES)) {
                int lines = WXStyle.getLines(map);
                if (lines <= 0) {
                    lines = -1;
                }
                this.mNumberOfLines = lines;
            }
            if (map.containsKey(Constants.Name.FONT_SIZE)) {
                this.mFontSize = WXStyle.getFontSize(map, this.mComponent.getInstance().getDefaultFontSize(), this.mComponent.getViewPortWidthForFloat());
            }
            if (map.containsKey(Constants.Name.FONT_WEIGHT)) {
                this.mFontWeight = WXStyle.getFontWeight(map);
            }
            if (map.containsKey(Constants.Name.FONT_STYLE)) {
                this.mFontStyle = WXStyle.getFontStyle(map);
            }
            if (map.containsKey("color")) {
                int color = WXResourceUtils.getColor(WXStyle.getTextColor(map));
                this.mColor = color;
                this.mIsColorSet = color != Integer.MIN_VALUE;
            }
            if (map.containsKey(Constants.Name.TEXT_DECORATION)) {
                this.mTextDecoration = WXStyle.getTextDecoration(map);
            }
            if (map.containsKey(Constants.Name.FONT_FAMILY)) {
                this.mFontFamily = WXStyle.getFontFamily(map);
            }
            this.mAlignment = WXStyle.getTextAlignment(map, this.mComponent.isLayoutRTL());
            this.textOverflow = WXStyle.getTextOverflow(map);
            int lineHeight = WXStyle.getLineHeight(map, this.mComponent.getViewPortWidthForFloat());
            if (lineHeight != -1) {
                this.mLineHeight = lineHeight;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Spanned createSpanned(String str) {
        if (TextUtils.isEmpty(str)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(str);
        updateSpannable(spannableString, 17);
        return spannableString;
    }

    /* access modifiers changed from: protected */
    public void updateSpannable(Spannable spannable, int i) {
        int length = spannable.length();
        int i2 = this.mFontSize;
        if (i2 == -1) {
            this.mTextPaint.setTextSize((float) this.mComponent.getInstance().getDefaultFontSize());
        } else {
            this.mTextPaint.setTextSize((float) i2);
        }
        int i3 = this.mLineHeight;
        if (i3 != -1) {
            setSpan(spannable, new WXLineHeightSpan(i3), 0, length, i);
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

    /* access modifiers changed from: protected */
    public void setSpan(Spannable spannable, Object obj, int i, int i2, int i3) {
        spannable.setSpan(obj, i, i2, i3);
    }

    private float getTextWidth(TextPaint textPaint, float f, boolean z) {
        if (this.mText == null) {
            if (z) {
                return f;
            }
            return 0.0f;
        } else if (z) {
            return f;
        } else {
            float desiredWidth = Layout.getDesiredWidth(this.spanned, textPaint);
            return (WXUtils.isUndefined(f) || desiredWidth < f) ? desiredWidth : f;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v1, resolved type: android.text.StaticLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v9, resolved type: android.text.StaticLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.text.StaticLayout} */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002f, code lost:
        if (r0 <= 1) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0031, code lost:
        r0 = r14.getLineStart(r0 - 1);
        r2 = r14.getLineEnd(r12.mNumberOfLines - 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003d, code lost:
        if (r0 >= r2) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003f, code lost:
        if (r0 <= 0) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        r14 = new android.text.SpannableStringBuilder(r12.spanned.subSequence(0, r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004d, code lost:
        r14 = new android.text.SpannableStringBuilder();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0052, code lost:
        r2 = (double) r13;
        r14.append(truncate(new android.text.SpannableStringBuilder(r12.spanned.subSequence(r0, r2)), r12.mTextPaint, (int) java.lang.Math.ceil(r2), r12.textOverflow));
        adjustSpansRange(r12.spanned, r14);
        r12.spanned = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x008a, code lost:
        return new android.text.StaticLayout(r12.spanned, r12.mTextPaint, (int) java.lang.Math.ceil(r2), android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x008b, code lost:
        r13 = (int) java.lang.Math.ceil((double) r13);
        r14 = r12.spanned;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00af, code lost:
        return android.text.StaticLayout.Builder.obtain(r14, 0, r14.length(), r12.mTextPaint, r13).setMaxLines(1).setEllipsize(android.text.TextUtils.TruncateAt.END).setEllipsizedWidth(r13).build();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00b0, code lost:
        return r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        if (r14 == null) goto L_0x0008;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        r1 = new android.text.StaticLayout(r12.spanned, r12.mTextPaint, (int) java.lang.Math.ceil((double) r13), android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001e, code lost:
        r0 = r12.mNumberOfLines;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0021, code lost:
        if (r0 == -1) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0023, code lost:
        if (r0 <= 0) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0029, code lost:
        if (r0 >= r14.getLineCount()) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002b, code lost:
        r0 = r12.mNumberOfLines;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.text.Layout createLayout(float r13, android.text.Layout r14) {
        /*
            r12 = this;
            float r0 = r12.previousWidth
            int r0 = (r0 > r13 ? 1 : (r0 == r13 ? 0 : -1))
            if (r0 != 0) goto L_0x0008
            if (r14 != 0) goto L_0x001e
        L_0x0008:
            android.text.StaticLayout r14 = new android.text.StaticLayout
            android.text.Spanned r2 = r12.spanned
            android.text.TextPaint r3 = r12.mTextPaint
            double r0 = (double) r13
            double r0 = java.lang.Math.ceil(r0)
            int r4 = (int) r0
            android.text.Layout$Alignment r5 = android.text.Layout.Alignment.ALIGN_NORMAL
            r6 = 1065353216(0x3f800000, float:1.0)
            r7 = 0
            r8 = 0
            r1 = r14
            r1.<init>(r2, r3, r4, r5, r6, r7, r8)
        L_0x001e:
            int r0 = r12.mNumberOfLines
            r1 = -1
            if (r0 == r1) goto L_0x00b0
            if (r0 <= 0) goto L_0x00b0
            int r1 = r14.getLineCount()
            if (r0 >= r1) goto L_0x00b0
            int r0 = r12.mNumberOfLines
            r1 = 0
            r2 = 1
            if (r0 <= r2) goto L_0x008b
            int r0 = r0 - r2
            int r0 = r14.getLineStart(r0)
            int r3 = r12.mNumberOfLines
            int r3 = r3 - r2
            int r2 = r14.getLineEnd(r3)
            if (r0 >= r2) goto L_0x00b0
            if (r0 <= 0) goto L_0x004d
            android.text.SpannableStringBuilder r14 = new android.text.SpannableStringBuilder
            android.text.Spanned r3 = r12.spanned
            java.lang.CharSequence r1 = r3.subSequence(r1, r0)
            r14.<init>(r1)
            goto L_0x0052
        L_0x004d:
            android.text.SpannableStringBuilder r14 = new android.text.SpannableStringBuilder
            r14.<init>()
        L_0x0052:
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            android.text.Spanned r3 = r12.spanned
            java.lang.CharSequence r0 = r3.subSequence(r0, r2)
            r1.<init>(r0)
            android.text.TextPaint r0 = r12.mTextPaint
            double r2 = (double) r13
            double r4 = java.lang.Math.ceil(r2)
            int r13 = (int) r4
            android.text.TextUtils$TruncateAt r4 = r12.textOverflow
            android.text.Spanned r13 = r12.truncate(r1, r0, r13, r4)
            r14.append(r13)
            android.text.Spanned r13 = r12.spanned
            r12.adjustSpansRange(r13, r14)
            r12.spanned = r14
            android.text.StaticLayout r13 = new android.text.StaticLayout
            android.text.Spanned r5 = r12.spanned
            android.text.TextPaint r6 = r12.mTextPaint
            double r0 = java.lang.Math.ceil(r2)
            int r7 = (int) r0
            android.text.Layout$Alignment r8 = android.text.Layout.Alignment.ALIGN_NORMAL
            r9 = 1065353216(0x3f800000, float:1.0)
            r10 = 0
            r11 = 0
            r4 = r13
            r4.<init>(r5, r6, r7, r8, r9, r10, r11)
            return r13
        L_0x008b:
            double r13 = (double) r13
            double r13 = java.lang.Math.ceil(r13)
            int r13 = (int) r13
            android.text.Spanned r14 = r12.spanned
            int r0 = r14.length()
            android.text.TextPaint r3 = r12.mTextPaint
            android.text.StaticLayout$Builder r14 = android.text.StaticLayout.Builder.obtain(r14, r1, r0, r3, r13)
            android.text.StaticLayout$Builder r14 = r14.setMaxLines(r2)
            android.text.TextUtils$TruncateAt r0 = android.text.TextUtils.TruncateAt.END
            android.text.StaticLayout$Builder r14 = r14.setEllipsize(r0)
            android.text.StaticLayout$Builder r13 = r14.setEllipsizedWidth(r13)
            android.text.StaticLayout r13 = r13.build()
            return r13
        L_0x00b0:
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.layout.measurefunc.TextContentBoxMeasurement.createLayout(float, android.text.Layout):android.text.Layout");
    }

    private Spanned truncate(Editable editable, TextPaint textPaint, int i, TextUtils.TruncateAt truncateAt) {
        Editable editable2 = editable;
        SpannedString spannedString = new SpannedString("");
        if (!TextUtils.isEmpty(editable) && editable.length() > 0) {
            if (truncateAt != null) {
                editable.append(ELLIPSIS);
                for (Object obj : editable.getSpans(0, editable.length(), Object.class)) {
                    int spanStart = editable.getSpanStart(obj);
                    int spanEnd = editable.getSpanEnd(obj);
                    if (spanStart == 0 && spanEnd == editable.length() - 1) {
                        editable.removeSpan(obj);
                        editable.setSpan(obj, 0, editable.length(), editable.getSpanFlags(obj));
                    }
                }
            }
            while (editable.length() > 1) {
                int length = editable.length() - 1;
                if (truncateAt != null) {
                    length--;
                }
                editable.delete(length, length + 1);
                if (new StaticLayout(editable, textPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineCount() <= 1) {
                    return editable2;
                }
            }
        }
        return spannedString;
    }

    private void adjustSpansRange(Spanned spanned2, Spannable spannable) {
        for (Object obj : spanned2.getSpans(0, spanned2.length(), Object.class)) {
            int spanStart = spanned2.getSpanStart(obj);
            int spanEnd = spanned2.getSpanEnd(obj);
            if (spanStart == 0 && spanEnd == spanned2.length()) {
                spannable.removeSpan(obj);
                spannable.setSpan(obj, 0, spannable.length(), spanned2.getSpanFlags(obj));
            }
        }
    }

    private void recalculateLayout(float f) {
        float contentWidth = WXDomUtils.getContentWidth(this.mComponent.getPadding(), this.mComponent.getBorder(), f);
        if (contentWidth > 0.0f) {
            Spanned createSpanned = createSpanned(this.mText);
            this.spanned = createSpanned;
            if (createSpanned != null) {
                Layout createLayout = createLayout(contentWidth, this.layout);
                this.layout = createLayout;
                this.previousWidth = (float) createLayout.getWidth();
                return;
            }
            this.previousWidth = 0.0f;
        }
    }

    private boolean warmUpTextLayoutCache(Layout layout2) {
        try {
            layout2.draw(DUMMY_CANVAS);
            return true;
        } catch (Exception e) {
            WXLogUtils.eTag("TextWarmUp", e);
            return false;
        }
    }

    private void swap() {
        Layout layout2 = this.layout;
        if (layout2 != null) {
            this.atomicReference.set(layout2);
            this.layout = null;
        }
        this.hasBeenMeasured = false;
    }
}
