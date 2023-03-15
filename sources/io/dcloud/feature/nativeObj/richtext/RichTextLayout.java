package io.dcloud.feature.nativeObj.richtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.nativeObj.INativeViewChildView;
import io.dcloud.feature.nativeObj.NativeView;
import io.dcloud.feature.nativeObj.richtext.dom.ImgDomElement;
import java.io.InputStream;
import org.json.JSONObject;

public class RichTextLayout {

    static class DefaultAssets implements IAssets {
        boolean isClick;
        String mCallBackId;
        NativeView mNativeView;
        IWebview mWebview;

        DefaultAssets(IWebview iWebview) {
            this(iWebview, (NativeView) null);
        }

        public InputStream convert2InputStream(String str) {
            return this.mWebview.obtainApp().obtainResInStream(this.mWebview.obtainFullUrl(), str);
        }

        public float convertHeight(String str, float f) {
            NativeView nativeView = this.mNativeView;
            if (nativeView != null) {
                return PdrUtil.parseFloat(str, (float) nativeView.mInnerHeight, f, getScale());
            }
            return PdrUtil.parseFloat(str, (float) this.mWebview.obtainApp().getInt(1), f, getScale());
        }

        public float convertWidth(String str, float f) {
            NativeView nativeView = this.mNativeView;
            if (nativeView != null) {
                return PdrUtil.parseFloat(str, (float) nativeView.mInnerWidth, f, getScale());
            }
            return PdrUtil.parseFloat(str, (float) this.mWebview.obtainApp().getInt(0), f, getScale());
        }

        public int getDefaultColor(boolean z) {
            return z ? -16776961 : -16777216;
        }

        public String getOnClickCallBackId() {
            return this.mCallBackId;
        }

        public float getScale() {
            NativeView nativeView = this.mNativeView;
            if (nativeView != null) {
                return nativeView.mCreateScale;
            }
            return this.mWebview.getScale();
        }

        public boolean isClick() {
            return this.isClick;
        }

        public void loadResource(final ImgDomElement.AsycLoader asycLoader) {
            ImageLoaderL.getInstance().loadImage(asycLoader.url, new ImageLoadingListener() {
                public void onLoadingCancelled(String str, View view) {
                }

                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    asycLoader.onComplete(bitmap);
                }

                public void onLoadingFailed(String str, View view, FailReason failReason) {
                }

                public void onLoadingStarted(String str, View view) {
                }
            });
        }

        public void setClick(boolean z) {
            this.isClick = z;
        }

        public void setOnClickCallBackId(String str) {
            this.mCallBackId = str;
        }

        public int stringToColor(String str) {
            return PdrUtil.stringToColor(str);
        }

        DefaultAssets(IWebview iWebview, NativeView nativeView) {
            this.mWebview = null;
            this.mNativeView = null;
            this.isClick = false;
            this.mCallBackId = null;
            this.mWebview = iWebview;
            this.mNativeView = nativeView;
        }
    }

    public static class RichTextLayoutHolder extends LinearLayout implements IAssets, INativeViewChildView {
        public boolean isClick = false;
        String mCallBackId = null;
        DefaultAssets mDefaultAssets = null;
        String mItemId = null;
        public TextView mMainView = null;
        NativeView mNativeView = null;
        int mNativeViewHeight = -2;
        IWebview mWebView = null;

        public RichTextLayoutHolder(Context context, IWebview iWebview, NativeView nativeView, String str) {
            super(context);
            this.mWebView = iWebview;
            this.mNativeView = nativeView;
            this.mItemId = str;
            AnonymousClass1 r6 = new TextView(context) {
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    boolean onTouchEvent = super.onTouchEvent(motionEvent);
                    Object tag = getTag();
                    if (!(tag instanceof String) || !Boolean.parseBoolean((String) tag)) {
                        return false;
                    }
                    return onTouchEvent;
                }
            };
            this.mMainView = r6;
            addView(r6);
            this.mDefaultAssets = new DefaultAssets(iWebview, nativeView);
        }

        public InputStream convert2InputStream(String str) {
            return this.mDefaultAssets.convert2InputStream(str);
        }

        public float convertHeight(String str, float f) {
            return this.mDefaultAssets.convertHeight(str, f);
        }

        public float convertWidth(String str, float f) {
            return this.mDefaultAssets.convertWidth(str, f);
        }

        public int getDefaultColor(boolean z) {
            return this.mDefaultAssets.getDefaultColor(z);
        }

        /* access modifiers changed from: package-private */
        public IWebview getIWebview() {
            return this.mWebView;
        }

        public String getOnClickCallBackId() {
            return this.mCallBackId;
        }

        public float getScale() {
            return this.mNativeView.mCreateScale;
        }

        public boolean isClick() {
            return this.isClick;
        }

        public void loadResource(ImgDomElement.AsycLoader asycLoader) {
            this.mDefaultAssets.loadResource(asycLoader);
        }

        public View obtainMainView() {
            return this.mMainView;
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            int i3 = layoutParams.width;
            if (!(i3 == -2 || i3 == -1)) {
                i = View.MeasureSpec.makeMeasureSpec(Math.min(this.mNativeView.mInnerWidth, i3), View.MeasureSpec.getMode(i));
            }
            int i4 = layoutParams.height;
            if (!(i4 == -2 || i4 == -1)) {
                i2 = View.MeasureSpec.makeMeasureSpec(Math.min(this.mNativeViewHeight, i4), View.MeasureSpec.getMode(i2));
            }
            super.onMeasure(i, i2);
        }

        public void setClick(boolean z) {
            this.isClick = z;
        }

        public void setOnClickCallBackId(String str) {
            this.mCallBackId = str;
        }

        public int stringToColor(String str) {
            return this.mDefaultAssets.stringToColor(str);
        }

        public void updateLayout() {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new FrameLayout.LayoutParams(this.mNativeView.mInnerWidth, this.mNativeViewHeight);
            }
            NativeView nativeView = this.mNativeView;
            layoutParams.topMargin = nativeView.mInnerTop + (nativeView.isStatusBar() ? DeviceInfo.sStatusBarHeight : 0);
            NativeView nativeView2 = this.mNativeView;
            layoutParams.leftMargin = nativeView2.mInnerLeft;
            layoutParams.width = nativeView2.mInnerWidth;
            int i = nativeView2.mInnerHeight;
            this.mNativeViewHeight = i;
            if (i == 0 && TextUtils.equals("wrap_content", nativeView2.mStyle.optString("height"))) {
                this.mNativeViewHeight = -2;
            }
            layoutParams.height = this.mNativeViewHeight;
            setLayoutParams(layoutParams);
        }
    }

    public static RichTextLayoutHolder makeRichText(Context context, IWebview iWebview, NativeView nativeView, String str, JSONObject jSONObject, JSONObject jSONObject2, String str2) {
        return makeRichText(new RichTextLayoutHolder(context, iWebview, nativeView, str2), str, jSONObject, jSONObject2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0102 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0121 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static io.dcloud.feature.nativeObj.richtext.RichTextLayout.RichTextLayoutHolder makeRichText(io.dcloud.feature.nativeObj.richtext.RichTextLayout.RichTextLayoutHolder r19, java.lang.String r20, org.json.JSONObject r21, org.json.JSONObject r22) {
        /*
            r0 = r19
            r1 = r21
            io.dcloud.common.DHInterface.IWebview r2 = r0.mWebView
            android.widget.TextView r3 = r0.mMainView
            r4 = r20
            r5 = r22
            io.dcloud.feature.nativeObj.richtext.RichTextParser.updateFromHTML(r0, r2, r3, r4, r5)
            r19.updateLayout()
            io.dcloud.feature.nativeObj.NativeView r2 = r0.mNativeView
            int r3 = r2.mInnerWidth
            int r4 = r2.mInnerHeight
            java.lang.String r5 = "left"
            boolean r6 = r1.isNull(r5)
            r6 = r6 ^ 1
            java.lang.String r7 = "right"
            boolean r8 = r1.isNull(r7)
            r8 = r8 ^ 1
            java.lang.String r9 = "top"
            boolean r10 = r1.isNull(r9)
            r10 = r10 ^ 1
            java.lang.String r11 = "width"
            boolean r12 = r1.isNull(r11)
            r12 = r12 ^ 1
            java.lang.String r13 = "height"
            boolean r14 = r1.isNull(r13)
            r14 = r14 ^ 1
            java.lang.String r15 = "bottom"
            boolean r16 = r1.isNull(r15)
            r16 = r16 ^ 1
            r20 = r15
            int r15 = r2.mInnerLeft
            r22 = r15
            int r15 = r2.mInnerTop
            r17 = 0
            if (r14 == 0) goto L_0x0082
            java.lang.String r13 = r1.optString(r13)
            boolean r18 = android.text.TextUtils.isEmpty(r13)
            if (r18 != 0) goto L_0x007d
            java.lang.String r2 = "wrap_content"
            boolean r2 = r2.equals(r13)
            if (r2 == 0) goto L_0x006b
            r2 = -2
            r14 = 0
            goto L_0x0085
        L_0x006b:
            int r2 = r0.mNativeViewHeight
            float r2 = (float) r2
            r18 = r14
            float r14 = (float) r4
            float r13 = r0.convertHeight(r13, r14)
            float r2 = java.lang.Math.min(r2, r13)
            int r2 = (int) r2
            r14 = r18
            goto L_0x0085
        L_0x007d:
            r18 = r14
            int r2 = r2.mInnerHeight
            goto L_0x0085
        L_0x0082:
            r18 = r14
            r2 = r4
        L_0x0085:
            if (r12 == 0) goto L_0x0098
            java.lang.String r11 = r1.optString(r11)
            boolean r13 = android.text.TextUtils.isEmpty(r11)
            if (r13 != 0) goto L_0x0098
            float r13 = (float) r3
            float r11 = r0.convertWidth(r11, r13)
            int r11 = (int) r11
            goto L_0x0099
        L_0x0098:
            r11 = r3
        L_0x0099:
            if (r10 == 0) goto L_0x00ab
            java.lang.String r9 = r1.optString(r9)
            boolean r13 = android.text.TextUtils.isEmpty(r9)
            if (r13 != 0) goto L_0x00ab
            float r13 = (float) r4
            float r9 = r0.convertHeight(r9, r13)
            int r15 = (int) r9
        L_0x00ab:
            if (r6 == 0) goto L_0x00be
            java.lang.String r5 = r1.optString(r5)
            boolean r9 = android.text.TextUtils.isEmpty(r5)
            if (r9 != 0) goto L_0x00be
            float r9 = (float) r3
            float r5 = r0.convertWidth(r5, r9)
            int r5 = (int) r5
            goto L_0x00c0
        L_0x00be:
            r5 = r22
        L_0x00c0:
            if (r8 == 0) goto L_0x00d3
            java.lang.String r7 = r1.optString(r7)
            boolean r9 = android.text.TextUtils.isEmpty(r7)
            if (r9 != 0) goto L_0x00d3
            float r9 = (float) r3
            float r7 = r0.convertWidth(r7, r9)
            int r7 = (int) r7
            goto L_0x00d4
        L_0x00d3:
            r7 = 0
        L_0x00d4:
            if (r16 == 0) goto L_0x00e9
            r9 = r20
            java.lang.String r1 = r1.optString(r9)
            boolean r9 = android.text.TextUtils.isEmpty(r1)
            if (r9 != 0) goto L_0x00e9
            float r9 = (float) r4
            float r1 = r0.convertHeight(r1, r9)
            int r1 = (int) r1
            goto L_0x00ea
        L_0x00e9:
            r1 = 0
        L_0x00ea:
            if (r6 != 0) goto L_0x0102
            if (r12 != 0) goto L_0x00f2
            if (r8 == 0) goto L_0x00f2
            int r5 = -r7
            goto L_0x0109
        L_0x00f2:
            if (r12 == 0) goto L_0x00fa
            if (r8 != 0) goto L_0x00fa
            int r3 = r3 - r11
            int r5 = r3 / 2
            goto L_0x0109
        L_0x00fa:
            if (r12 == 0) goto L_0x0109
            if (r8 == 0) goto L_0x0109
            int r3 = r3 - r11
            int r5 = r3 - r7
            goto L_0x0109
        L_0x0102:
            if (r12 != 0) goto L_0x0109
            if (r8 == 0) goto L_0x0109
            int r3 = r3 - r5
            int r11 = r3 - r7
        L_0x0109:
            if (r10 != 0) goto L_0x0121
            if (r14 != 0) goto L_0x0111
            if (r16 == 0) goto L_0x0111
            int r15 = -r1
            goto L_0x0128
        L_0x0111:
            if (r14 == 0) goto L_0x0119
            if (r16 != 0) goto L_0x0119
            int r4 = r4 - r2
            int r15 = r4 / 2
            goto L_0x0128
        L_0x0119:
            if (r14 == 0) goto L_0x0128
            if (r16 == 0) goto L_0x0128
            int r4 = r4 - r2
            int r15 = r4 - r1
            goto L_0x0128
        L_0x0121:
            if (r14 != 0) goto L_0x0128
            if (r16 == 0) goto L_0x0128
            int r4 = r4 - r15
            int r2 = r4 - r1
        L_0x0128:
            android.widget.LinearLayout$LayoutParams r3 = new android.widget.LinearLayout$LayoutParams
            r3.<init>(r11, r2)
            r3.leftMargin = r5
            r3.rightMargin = r7
            r3.bottomMargin = r1
            r3.topMargin = r15
            android.widget.TextView r1 = r0.mMainView
            r1.setLayoutParams(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.richtext.RichTextLayout.makeRichText(io.dcloud.feature.nativeObj.richtext.RichTextLayout$RichTextLayoutHolder, java.lang.String, org.json.JSONObject, org.json.JSONObject):io.dcloud.feature.nativeObj.richtext.RichTextLayout$RichTextLayoutHolder");
    }

    public static TextView makeRichText(Object[] objArr) {
        IWebview obtainWebView = objArr[0].obtainWebView();
        TextView textView = new TextView(obtainWebView.getContext());
        RichTextParser.updateFromHTML(new DefaultAssets(obtainWebView) {
            public int getDefaultColor(boolean z) {
                return z ? -16776961 : -1;
            }
        }, obtainWebView, textView, objArr[1], objArr[2], objArr[3]);
        return textView;
    }
}
