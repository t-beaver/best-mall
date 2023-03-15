package io.dcloud.feature.nativeObj.richtext;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.nativeObj.NativeTypefaceFactory;
import io.dcloud.feature.nativeObj.richtext.dom.DomElement;
import io.dcloud.feature.nativeObj.richtext.dom.HrDomElement;
import io.dcloud.feature.nativeObj.richtext.dom.ImgDomElement;
import io.dcloud.feature.nativeObj.richtext.dom.TextDomElement;
import io.dcloud.feature.nativeObj.richtext.span.AHrefSpan;
import io.dcloud.feature.nativeObj.richtext.span.ClickSpanAble;
import io.dcloud.feature.nativeObj.richtext.span.ImgSpan;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class RichTextParser {

    public static class LinkMovementMethodExt extends LinkMovementMethod {
        ClickSpanAble[] downLinks = null;
        long downTime = 0;
        float downX;
        float downY;
        IAssets mAssets = null;
        ICallBack mObserver = null;
        IWebview mWebview = null;

        LinkMovementMethodExt(IWebview iWebview, ICallBack iCallBack, IAssets iAssets) {
            this.mWebview = iWebview;
            this.mObserver = iCallBack;
            this.mAssets = iAssets;
        }

        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            ImgSpan[] imgSpanArr;
            boolean z;
            String str;
            String str2;
            ImgSpan imgSpan;
            String str3;
            ClickSpanAble clickSpanAble;
            TextView textView2 = textView;
            Spannable spannable2 = spannable;
            int action = motionEvent.getAction();
            int x = ((int) motionEvent.getX()) - textView.getTotalPaddingLeft();
            int y = ((int) motionEvent.getY()) - textView.getTotalPaddingTop();
            int scrollX = x + textView.getScrollX();
            int scrollY = y + textView.getScrollY();
            Layout layout = textView.getLayout();
            int lineForVertical = layout.getLineForVertical(scrollY);
            int offsetForHorizontal = layout.getOffsetForHorizontal(lineForVertical, (float) scrollX);
            Rect rect = new Rect((int) layout.getLineLeft(lineForVertical), (int) ((float) layout.getLineTop(lineForVertical)), (int) layout.getLineRight(lineForVertical), (int) ((float) layout.getLineBottom(lineForVertical)));
            textView2.setTag(AbsoluteConst.FALSE);
            boolean z2 = true;
            if (rect.contains(scrollX, scrollY)) {
                ClickSpanAble[] clickSpanAbleArr = (ClickSpanAble[]) spannable2.getSpans(offsetForHorizontal, offsetForHorizontal, ClickSpanAble.class);
                if (!(clickSpanAbleArr == null || clickSpanAbleArr.length == 0)) {
                    if (action == 1) {
                        ClickSpanAble[] clickSpanAbleArr2 = this.downLinks;
                        if (clickSpanAbleArr2 != null && clickSpanAbleArr2.length >= clickSpanAbleArr.length && !this.mAssets.isClick()) {
                            int i = 0;
                            while (true) {
                                if (i >= clickSpanAbleArr.length) {
                                    break;
                                }
                                clickSpanAble = clickSpanAbleArr[i];
                                if (clickSpanAble == this.downLinks[i]) {
                                    int spanFlags = spannable2.getSpanFlags(clickSpanAble);
                                    int spanStart = spannable2.getSpanStart(clickSpanAble);
                                    int spanEnd = spannable2.getSpanEnd(clickSpanAble);
                                    if (spanStart == 0 || spanEnd == spannable.length() || clickSpanAbleArr.length == 1) {
                                        spanFlags = 18;
                                    }
                                    if ((spanFlags == 17 && offsetForHorizontal >= spanStart && offsetForHorizontal < spanEnd) || ((spanFlags == 18 && offsetForHorizontal >= spanStart && offsetForHorizontal <= spanEnd) || ((spanFlags == 33 && offsetForHorizontal > spanStart && offsetForHorizontal < spanEnd) || (spanFlags == 34 && offsetForHorizontal > spanStart && offsetForHorizontal <= spanEnd)))) {
                                        clickSpanAble.onClick(textView2, this.mWebview);
                                        ICallBack iCallBack = this.mObserver;
                                    }
                                }
                                i++;
                            }
                            clickSpanAble.onClick(textView2, this.mWebview);
                            ICallBack iCallBack2 = this.mObserver;
                            if (iCallBack2 != null) {
                                iCallBack2.onCallBack(0, clickSpanAble);
                            }
                        }
                        this.downLinks = null;
                    } else if (action == 0) {
                        this.downLinks = clickSpanAbleArr;
                        this.downX = motionEvent.getX();
                        this.downY = motionEvent.getY();
                    } else if (action == 2 && (Math.abs(motionEvent.getX() - this.downX) > 20.0f || Math.abs(motionEvent.getY() - this.downY) > 20.0f)) {
                        this.downLinks = null;
                    }
                    for (ClickSpanAble hasClickEvent : clickSpanAbleArr) {
                        if (hasClickEvent.hasClickEvent()) {
                            textView2.setTag(AbsoluteConst.TRUE);
                            imgSpanArr = clickSpanAbleArr;
                            z = true;
                            break;
                        }
                    }
                }
                imgSpanArr = clickSpanAbleArr;
            } else {
                imgSpanArr = null;
            }
            z = false;
            if (this.mAssets.isClick()) {
                textView2.setTag(AbsoluteConst.TRUE);
                if (action == 0) {
                    this.downTime = System.currentTimeMillis();
                } else if ((action == 1 || action == 3) && System.currentTimeMillis() - this.downTime < 800) {
                    String str4 = "";
                    if (imgSpanArr == null || imgSpanArr.length <= 0 || (imgSpan = imgSpanArr[0]) == null) {
                        str2 = str4;
                        str = str2;
                    } else {
                        str = imgSpan.getHref();
                        if (imgSpan instanceof ImgSpan) {
                            str4 = imgSpan.getSrc();
                            str3 = WXBasicComponentType.IMG;
                        } else if (imgSpan instanceof AHrefSpan) {
                            str3 = "a";
                        } else {
                            str2 = str4;
                        }
                        String str5 = str3;
                        str2 = str4;
                        str4 = str5;
                    }
                    String str6 = "{\"tagName\":\"" + str4 + "\",\"href\":\"" + str + "\",\"src\":\"" + str2 + "\"}";
                    Deprecated_JSUtil.execCallback(this.mWebview, this.mAssets.getOnClickCallBackId(), str6, JSUtil.OK, true, true);
                    if (this.mWebview.getOpener() != null) {
                        Deprecated_JSUtil.execCallback(this.mWebview.getOpener(), this.mAssets.getOnClickCallBackId(), str6, JSUtil.OK, true, true);
                    }
                }
            } else {
                z2 = z;
            }
            return z2 ? z2 : super.onTouchEvent(textView, spannable, motionEvent);
        }
    }

    private static void handleEndTag(IAssets iAssets, TextView textView, SpannableStringBuilder spannableStringBuilder, DomElement domElement) {
        domElement.makeSpan(iAssets, textView, spannableStringBuilder);
    }

    private static DomElement handleStartTag(XmlPullParser xmlPullParser) {
        DomElement domElement;
        String name = xmlPullParser.getName();
        if (name.equalsIgnoreCase("script") || name.equalsIgnoreCase(AbsURIAdapter.LINK) || name.equalsIgnoreCase("iframe") || name.equalsIgnoreCase("style") || name.equalsIgnoreCase("meta")) {
            return null;
        }
        if (WXBasicComponentType.IMG.equalsIgnoreCase(name)) {
            domElement = new ImgDomElement();
        } else if ("hr".equalsIgnoreCase(name)) {
            domElement = new HrDomElement();
        } else if ("br".equalsIgnoreCase(name)) {
            domElement = new DomElement();
        } else if ("a".equalsIgnoreCase(name) || AbsURIAdapter.FONT.equalsIgnoreCase(name) || ContextChain.TAG_PRODUCT.equalsIgnoreCase(name)) {
            domElement = new TextDomElement();
        } else {
            domElement = new TextDomElement();
        }
        domElement.parseDomElement(xmlPullParser);
        return domElement;
    }

    static SpannableStringBuilder makeSpannableStringBuilder(IAssets iAssets, TextView textView, String str) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(new StringReader(str));
            int eventType = newPullParser.getEventType();
            DomElement domElement = null;
            DomElement domElement2 = null;
            while (eventType != 1) {
                if (eventType == 0) {
                    try {
                        System.out.print("Start document");
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    if (eventType == 2) {
                        System.out.println("start-tag=<" + newPullParser.getName() + Operators.G);
                        domElement = handleStartTag(newPullParser);
                        if (domElement != null) {
                            domElement.parentDomElement = domElement2;
                        }
                    } else if (eventType == 3) {
                        System.out.println("end-tag=</" + newPullParser.getName() + Operators.G);
                        if (domElement != null) {
                            if (!(domElement instanceof TextDomElement) || ((domElement instanceof TextDomElement) && TextUtils.isEmpty(((TextDomElement) domElement).text))) {
                                handleEndTag(iAssets, textView, spannableStringBuilder, domElement);
                            }
                            domElement = domElement.parentDomElement;
                        }
                    } else if (eventType == 4) {
                        System.out.println("[Text:" + newPullParser.getText() + Operators.ARRAY_END_STR);
                        if (domElement != null && (domElement instanceof TextDomElement)) {
                            ((TextDomElement) domElement).text = newPullParser.getText();
                            if (!TextUtils.isEmpty(((TextDomElement) domElement).text)) {
                                handleEndTag(iAssets, textView, spannableStringBuilder, domElement);
                            }
                        }
                    }
                    domElement2 = domElement;
                }
                eventType = newPullParser.next();
            }
        } catch (XmlPullParserException e3) {
            e3.printStackTrace();
        }
        return spannableStringBuilder;
    }

    static void updateFromHTML(IAssets iAssets, IWebview iWebview, TextView textView, String str, JSONObject jSONObject) {
        updateFromHTML(iAssets, iWebview, textView, str, jSONObject, (ICallBack) null);
    }

    static void updateFromHTML(IAssets iAssets, IWebview iWebview, TextView textView, String str, JSONObject jSONObject, ICallBack iCallBack) {
        String str2;
        Typeface create;
        if (jSONObject != null) {
            if (jSONObject.has("family") && (create = Typeface.create(jSONObject.optString("family"), 0)) != null) {
                textView.setTypeface(create);
            }
            if (jSONObject.has("__onClickCallBackId__") && !PdrUtil.isEmpty(jSONObject.optString("__onClickCallBackId__"))) {
                iAssets.setClick(true);
                iAssets.setOnClickCallBackId(jSONObject.optString("__onClickCallBackId__"));
            }
            if (jSONObject.has("fontSrc")) {
                String optString = jSONObject.optString("fontSrc", "");
                if (optString.contains("__wap2app.ttf")) {
                    str2 = BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/__wap2app.ttf";
                    if (!new File(str2).exists()) {
                        str2 = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), optString);
                    }
                } else {
                    str2 = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), optString);
                }
                Typeface typeface = NativeTypefaceFactory.getTypeface(iWebview.obtainApp(), str2);
                if (typeface != null) {
                    textView.setTypeface(typeface);
                }
            }
            if (jSONObject.has(AbsoluteConst.JSON_KEY_ALIGN)) {
                String optString2 = jSONObject.optString(AbsoluteConst.JSON_KEY_ALIGN, "");
                if ("center".equalsIgnoreCase(optString2)) {
                    textView.setGravity(1);
                } else if ("right".equalsIgnoreCase(optString2)) {
                    textView.setGravity(5);
                } else {
                    textView.setGravity(3);
                }
            }
        }
        textView.setText(makeSpannableStringBuilder(iAssets, textView, str.replaceAll("&nbsp", Operators.SPACE_STR)));
        textView.setMovementMethod(new LinkMovementMethodExt(iWebview, iCallBack, iAssets));
    }
}
