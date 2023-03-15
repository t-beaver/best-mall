package io.dcloud.common.util;

import android.graphics.Color;
import android.text.TextUtils;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ITitleNView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.core.ui.l;
import org.json.JSONArray;
import org.json.JSONObject;

public class TitleNViewUtil {
    public static final String TRANSPARENT_BUTTON_BACKGROUND_COLOR = "#7F333333";
    public static final String TRANSPARENT_BUTTON_TEXT_COLOR = "#FFFFFF";

    /* JADX WARNING: Removed duplicated region for block: B:75:0x00cb A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean backButtonIsChanged(org.json.JSONObject r6, org.json.JSONObject r7) {
        /*
            java.lang.String r0 = "titleColor"
            r1 = 0
            java.lang.String r2 = "undefined"
            java.lang.String r3 = "autoBackButton"
            r4 = 1
            java.lang.String r5 = "backButton"
            if (r6 != 0) goto L_0x002f
            if (r7 == 0) goto L_0x002f
            boolean r6 = r7.has(r3)
            if (r6 == 0) goto L_0x0015
            return r4
        L_0x0015:
            boolean r6 = r7.has(r5)
            if (r6 == 0) goto L_0x0028
            org.json.JSONObject r6 = r7.optJSONObject(r5)
            if (r6 != 0) goto L_0x0027
            java.lang.String r6 = r7.optString(r5)
            if (r2 == r6) goto L_0x0028
        L_0x0027:
            return r4
        L_0x0028:
            boolean r6 = r7.has(r0)
            if (r6 == 0) goto L_0x00cb
            return r4
        L_0x002f:
            if (r6 == 0) goto L_0x00cb
            if (r7 == 0) goto L_0x00cb
            boolean r0 = r7.has(r0)
            if (r0 == 0) goto L_0x003a
            return r4
        L_0x003a:
            boolean r0 = r6.has(r3)
            if (r0 != 0) goto L_0x0060
            boolean r0 = r6.has(r5)
            if (r0 != 0) goto L_0x0060
            boolean r6 = r7.has(r3)
            if (r6 == 0) goto L_0x0053
            boolean r6 = r7.optBoolean(r3)
            if (r6 == 0) goto L_0x0053
            return r4
        L_0x0053:
            boolean r6 = r7.has(r5)
            if (r6 == 0) goto L_0x00cb
            org.json.JSONObject r6 = r7.optJSONObject(r5)
            if (r6 == 0) goto L_0x00cb
            return r4
        L_0x0060:
            boolean r0 = r6.has(r3)
            if (r0 == 0) goto L_0x0093
            boolean r6 = r6.optBoolean(r3)
            boolean r0 = r7.has(r3)
            if (r0 == 0) goto L_0x0077
            boolean r7 = r7.optBoolean(r3)
            if (r6 == r7) goto L_0x00cb
            return r4
        L_0x0077:
            boolean r0 = r7.has(r5)
            if (r0 == 0) goto L_0x00cb
            if (r6 == 0) goto L_0x008c
            org.json.JSONObject r6 = r7.optJSONObject(r5)
            if (r6 == 0) goto L_0x008b
            java.lang.String r6 = r7.optString(r5)
            if (r2 != r6) goto L_0x008c
        L_0x008b:
            return r4
        L_0x008c:
            org.json.JSONObject r6 = r7.optJSONObject(r5)
            if (r6 == 0) goto L_0x00cb
            return r4
        L_0x0093:
            boolean r0 = r6.has(r5)
            if (r0 == 0) goto L_0x00cb
            org.json.JSONObject r6 = r6.optJSONObject(r5)
            if (r6 == 0) goto L_0x00a1
            r6 = 1
            goto L_0x00a2
        L_0x00a1:
            r6 = 0
        L_0x00a2:
            boolean r0 = r7.has(r3)
            if (r0 == 0) goto L_0x00af
            boolean r7 = r7.optBoolean(r3)
            if (r6 == r7) goto L_0x00cb
            return r4
        L_0x00af:
            boolean r0 = r7.has(r5)
            if (r0 == 0) goto L_0x00cb
            if (r6 == 0) goto L_0x00c4
            org.json.JSONObject r6 = r7.optJSONObject(r5)
            if (r6 == 0) goto L_0x00c3
            java.lang.String r6 = r7.optString(r5)
            if (r2 != r6) goto L_0x00c4
        L_0x00c3:
            return r4
        L_0x00c4:
            org.json.JSONObject r6 = r7.optJSONObject(r5)
            if (r6 == 0) goto L_0x00cb
            return r4
        L_0x00cb:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.backButtonIsChanged(org.json.JSONObject, org.json.JSONObject):boolean");
    }

    public static boolean backgroundImageIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 == null || !jSONObject2.has(Constants.Name.BACKGROUND_IMAGE)) {
            return false;
        }
        if (jSONObject == null) {
            return jSONObject2.has(Constants.Name.BACKGROUND_IMAGE);
        }
        String optString = jSONObject.optString(Constants.Name.BACKGROUND_IMAGE);
        String optString2 = jSONObject2.optString(Constants.Name.BACKGROUND_IMAGE);
        if (!jSONObject.has(Constants.Name.BACKGROUND_IMAGE) && jSONObject2.has(Constants.Name.BACKGROUND_IMAGE)) {
            return true;
        }
        if (!jSONObject.has(Constants.Name.BACKGROUND_IMAGE) || !jSONObject2.has(Constants.Name.BACKGROUND_IMAGE) || optString.equals(optString2)) {
            return false;
        }
        return true;
    }

    public static boolean backgroundIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 != null && jSONObject2.has("backgroundColor")) {
            if (jSONObject == null) {
                if (!TextUtils.isEmpty(jSONObject2.optString("backgroundColor"))) {
                    return true;
                }
            } else if (jSONObject != null) {
                String optString = jSONObject.optString("backgroundColor");
                String optString2 = jSONObject2.optString("backgroundColor");
                if (!TextUtils.isEmpty(optString) || TextUtils.isEmpty(optString2)) {
                    return !TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2) && !optString.equals(optString2);
                }
                return true;
            }
        }
    }

    public static int changeColorAlpha(int i, float f) {
        if (0.0f > f || f > 1.0f) {
            return -1;
        }
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }

    private static boolean checkKeyValueIsModify(String str, JSONObject jSONObject, JSONObject jSONObject2) {
        if (!jSONObject2.has(str)) {
            return false;
        }
        if (jSONObject == null || !jSONObject.has(str)) {
            return true;
        }
        return !jSONObject.optString(str).equals(jSONObject2.optString(str));
    }

    public static String color2Color(String str, String str2, float f) {
        try {
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || 0.0f > f || f > 1.0f) {
                return null;
            }
            if (str.startsWith("#") && str2.startsWith("#")) {
                String substring = str.substring(1, str.length());
                String substring2 = str2.substring(1, str2.length());
                if (substring.length() == 3) {
                    str = "#" + substring.charAt(0) + substring.charAt(0) + substring.charAt(1) + substring.charAt(1) + substring.charAt(2) + substring.charAt(2);
                }
                if (substring2.length() == 3) {
                    str2 = "#" + substring2.charAt(0) + substring2.charAt(0) + substring2.charAt(1) + substring2.charAt(1) + substring2.charAt(2) + substring2.charAt(2);
                }
                int parseColor = Color.parseColor(str);
                int red = Color.red(parseColor);
                int green = Color.green(parseColor);
                int blue = Color.blue(parseColor);
                int parseColor2 = Color.parseColor(str2);
                int red2 = Color.red(parseColor2);
                int green2 = Color.green(parseColor2);
                int blue2 = Color.blue(parseColor2);
                String hexString = Integer.toHexString(Color.alpha(parseColor));
                if (1 == hexString.length()) {
                    hexString = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString;
                }
                float f2 = (float) red;
                if (red != red2) {
                    f2 = red > red2 ? f2 - (((float) (red - red2)) * f) : f2 + (((float) (red - red2)) * f);
                }
                String hexString2 = Integer.toHexString((int) f2);
                if (1 == hexString2.length()) {
                    hexString2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString2;
                }
                float f3 = (float) green;
                if (green != green2) {
                    f3 = green > green2 ? f3 - (((float) (green - green2)) * f) : f3 + (((float) (green2 - green)) * f);
                }
                String hexString3 = Integer.toHexString((int) f3);
                if (1 == hexString3.length()) {
                    hexString3 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString3;
                }
                float f4 = (float) blue;
                if (blue != blue2) {
                    f4 = blue > blue2 ? f4 - (((float) (blue - blue2)) * f) : f4 + (((float) (blue2 - blue)) * f);
                }
                String hexString4 = Integer.toHexString((int) f4);
                if (1 == hexString4.length()) {
                    hexString4 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString4;
                }
                return "#" + hexString + hexString2 + hexString3 + hexString4;
            } else if (!str.startsWith("rgba") || !str.contains(Operators.BRACKET_START_STR) || !str.contains(Operators.BRACKET_END_STR) || !str.contains(",") || !str2.startsWith("rgba") || !str2.contains(Operators.BRACKET_START_STR) || !str2.contains(Operators.BRACKET_END_STR) || !str2.contains(",")) {
                return null;
            } else {
                String substring3 = str.substring(str.indexOf(Operators.BRACKET_START_STR) + 1, str.indexOf(Operators.BRACKET_END_STR));
                String substring4 = str2.substring(str2.indexOf(Operators.BRACKET_START_STR) + 1, str2.indexOf(Operators.BRACKET_END_STR));
                String[] stringSplit = PdrUtil.stringSplit(substring3, ",");
                String[] stringSplit2 = PdrUtil.stringSplit(substring4, ",");
                if (stringSplit == null || 4 != stringSplit.length || stringSplit2 == null || 4 != stringSplit2.length) {
                    return null;
                }
                int intValue = Integer.valueOf(stringSplit[0]).intValue();
                int intValue2 = Integer.valueOf(stringSplit[1]).intValue();
                int intValue3 = Integer.valueOf(stringSplit[2]).intValue();
                int intValue4 = Integer.valueOf(stringSplit2[0]).intValue();
                int intValue5 = Integer.valueOf(stringSplit2[1]).intValue();
                int intValue6 = Integer.valueOf(stringSplit2[2]).intValue();
                String str3 = stringSplit[3];
                float f5 = (float) intValue;
                if (intValue != intValue4) {
                    f5 = intValue > intValue4 ? f5 - (((float) (intValue - intValue4)) * f) : f5 + (((float) (intValue - intValue4)) * f);
                }
                float f6 = (float) intValue2;
                if (intValue2 != intValue5) {
                    f6 = intValue2 > intValue5 ? f6 - (((float) (intValue2 - intValue5)) * f) : f6 + (((float) (intValue5 - intValue2)) * f);
                }
                float f7 = (float) intValue3;
                if (intValue3 != intValue6) {
                    f7 = intValue3 > intValue6 ? f7 - (((float) (intValue3 - intValue6)) * f) : f7 + (((float) (intValue6 - intValue3)) * f);
                }
                return "rgba(" + ((int) f5) + "," + ((int) f6) + "," + ((int) f7) + "," + str3 + Operators.BRACKET_END_STR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean compareColor(String str, String str2) {
        try {
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (str.startsWith("#") && str2.startsWith("#")) {
                    String substring = str.substring(1, str.length());
                    String substring2 = str2.substring(1, str2.length());
                    if (substring.length() == 3) {
                        str = "#" + substring.charAt(0) + substring.charAt(0) + substring.charAt(1) + substring.charAt(1) + substring.charAt(2) + substring.charAt(2);
                    }
                    if (substring2.length() == 3) {
                        str2 = "#" + substring2.charAt(0) + substring2.charAt(0) + substring2.charAt(1) + substring2.charAt(1) + substring2.charAt(2) + substring2.charAt(2);
                    }
                    int parseColor = Color.parseColor(str);
                    int parseColor2 = Color.parseColor(str2);
                    if (Color.rgb(Color.red(parseColor), Color.green(parseColor), Color.blue(parseColor)) == Color.rgb(Color.red(parseColor2), Color.green(parseColor2), Color.blue(parseColor2))) {
                        return true;
                    }
                    return false;
                } else if (str.startsWith("rgba") && str.contains(Operators.BRACKET_START_STR) && str.contains(Operators.BRACKET_END_STR) && str.contains(",") && str2.startsWith("rgba") && str2.contains(Operators.BRACKET_START_STR) && str2.contains(Operators.BRACKET_END_STR) && str2.contains(",")) {
                    String substring3 = str.substring(str.indexOf(Operators.BRACKET_START_STR) + 1, str.indexOf(Operators.BRACKET_END_STR));
                    String substring4 = str2.substring(str2.indexOf(Operators.BRACKET_START_STR) + 1, str2.indexOf(Operators.BRACKET_END_STR));
                    String[] stringSplit = PdrUtil.stringSplit(substring3, ",");
                    String[] stringSplit2 = PdrUtil.stringSplit(substring4, ",");
                    if (stringSplit != null && 4 == stringSplit.length && stringSplit2 != null && 4 == stringSplit2.length && Color.rgb(Integer.valueOf(stringSplit[0]).intValue(), Integer.valueOf(stringSplit[1]).intValue(), Integer.valueOf(stringSplit[2]).intValue()) == Color.rgb(Integer.valueOf(stringSplit2[0]).intValue(), Integer.valueOf(stringSplit2[1]).intValue(), Integer.valueOf(stringSplit2[2]).intValue())) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void drawTitle(IFrameView iFrameView, ITitleNView iTitleNView, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12) {
        if (iFrameView != null && iTitleNView != null) {
            if (!BaseInfo.sDoingAnimation) {
                iTitleNView.setTitle(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12);
            } else if (iFrameView.obtainWebView().obtainFrameView() instanceof AdaFrameView) {
                AbsMgr obtainWindowMgr = ((AdaFrameView) iFrameView.obtainWebView().obtainFrameView()).obtainWindowMgr();
                IMgr.MgrType mgrType = IMgr.MgrType.WindowMgr;
                final ITitleNView iTitleNView2 = iTitleNView;
                final String str13 = str;
                final String str14 = str2;
                final String str15 = str3;
                final String str16 = str4;
                final String str17 = str5;
                final String str18 = str6;
                final String str19 = str7;
                final String str20 = str8;
                final String str21 = str9;
                final String str22 = str10;
                final String str23 = str11;
                AbsMgr absMgr = obtainWindowMgr;
                AnonymousClass1 r14 = r0;
                final String str24 = str12;
                AnonymousClass1 r0 = new l.m() {
                    public void onAnimationEnd() {
                        ITitleNView.this.setTitle(str13, str14, str15, str16, str17, str18, str19, str20, str21, str22, str23, str24);
                    }
                };
                absMgr.processEvent(mgrType, 71, r14);
            }
        }
    }

    private static int getColorAlpha(String str) {
        int i;
        try {
            i = Color.parseColor(str);
        } catch (Exception unused) {
            i = PdrUtil.stringToColor(str);
        }
        return Color.alpha(i);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object getTitleNView(io.dcloud.common.DHInterface.AbsMgr r5, io.dcloud.common.DHInterface.IWebview r6, io.dcloud.common.DHInterface.IFrameView r7, java.lang.String r8) {
        /*
            if (r5 == 0) goto L_0x002e
            if (r6 == 0) goto L_0x002e
            if (r7 == 0) goto L_0x002e
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            if (r0 != 0) goto L_0x002e
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r1 = 4
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = 0
            r1[r2] = r6
            java.lang.String r6 = "nativeobj"
            r3 = 1
            r1[r3] = r6
            java.lang.String r6 = "getNativeView"
            r4 = 2
            r1[r4] = r6
            java.lang.Object[] r6 = new java.lang.Object[r4]
            r6[r2] = r7
            r6[r3] = r8
            r7 = 3
            r1[r7] = r6
            r6 = 10
            java.lang.Object r5 = r5.processEvent(r0, r6, r1)
            return r5
        L_0x002e:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.getTitleNView(io.dcloud.common.DHInterface.AbsMgr, io.dcloud.common.DHInterface.IWebview, io.dcloud.common.DHInterface.IFrameView, java.lang.String):java.lang.Object");
    }

    public static String getTitleNViewId(IFrameView iFrameView) {
        if (iFrameView == null || iFrameView.obtainWebView() == null || iFrameView.obtainWebView().obtainWindowView() == null) {
            return null;
        }
        if (2 == iFrameView.getFrameType() || 4 == iFrameView.getFrameType() || 5 == iFrameView.getFrameType()) {
            return String.valueOf(iFrameView.obtainWebView().obtainWindowView().hashCode());
        }
        return iFrameView.obtainWebView().getWebviewUUID();
    }

    public static String getTitleNViewSearchInputText(ITitleNView iTitleNView) {
        return iTitleNView.getTitleNViewSearchInputText();
    }

    public static String intColor2String(int i, boolean z) {
        StringBuilder sb = new StringBuilder();
        String hexString = Integer.toHexString(Color.alpha(i));
        String hexString2 = Integer.toHexString(Color.red(i));
        String hexString3 = Integer.toHexString(Color.green(i));
        String hexString4 = Integer.toHexString(Color.blue(i));
        if (hexString.length() == 1) {
            hexString = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString;
        }
        if (hexString2.length() == 1) {
            hexString2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString2;
        }
        if (hexString3.length() == 1) {
            hexString3 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString3;
        }
        if (hexString4.length() == 1) {
            hexString4 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString4;
        }
        sb.append("#");
        if (z) {
            sb.append(hexString);
        }
        sb.append(hexString2);
        sb.append(hexString3);
        sb.append(hexString4);
        return sb.toString();
    }

    public static boolean isButtonsIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        return jSONObject2.has("buttons");
    }

    public static boolean isSearchInputChange(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 != null && jSONObject2.has("searchInput")) {
            JSONObject jSONObject3 = null;
            if (jSONObject != null) {
                jSONObject3 = jSONObject.optJSONObject("searchInput");
            }
            JSONObject optJSONObject = jSONObject2.optJSONObject("searchInput");
            if (jSONObject3 == null && optJSONObject != null) {
                return true;
            }
            if (jSONObject3 != null && optJSONObject == null) {
                return true;
            }
            if (jSONObject3 == null || optJSONObject.length() <= 0 || (jSONObject3.optString(AbsoluteConst.JSON_KEY_ALIGN).equalsIgnoreCase(optJSONObject.optString(AbsoluteConst.JSON_KEY_ALIGN)) && jSONObject3.optString("backgroundColor").equalsIgnoreCase(optJSONObject.optString("backgroundColor")) && jSONObject3.optString(Constants.Name.BORDER_RADIUS).equalsIgnoreCase(optJSONObject.optString(Constants.Name.BORDER_RADIUS)) && jSONObject3.optString(Constants.Name.PLACEHOLDER).equalsIgnoreCase(optJSONObject.optString(Constants.Name.PLACEHOLDER)) && jSONObject3.optString(Constants.Name.PLACEHOLDER_COLOR).equalsIgnoreCase(optJSONObject.optString(Constants.Name.PLACEHOLDER_COLOR)) && jSONObject3.optString("color").equalsIgnoreCase(optJSONObject.optString("color")) && jSONObject3.optBoolean(Constants.Name.DISABLED) != optJSONObject.optBoolean(Constants.Name.DISABLED) && jSONObject3.optBoolean("autoFocus") != optJSONObject.optBoolean("autoFocus"))) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isShadowChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        JSONObject optJSONObject;
        if (jSONObject2 == null || !jSONObject2.has("shadow")) {
            return false;
        }
        JSONObject optJSONObject2 = jSONObject2.optJSONObject("shadow");
        if (jSONObject == null && optJSONObject2 != null && optJSONObject2.has("color")) {
            return true;
        }
        if (jSONObject == null) {
            return false;
        }
        if (!jSONObject.has("shadow")) {
            if (optJSONObject2 != null) {
                return !TextUtils.isEmpty(optJSONObject2.optString("color"));
            }
            return false;
        } else if (!jSONObject.has("shadow") || (optJSONObject = jSONObject.optJSONObject("shadow")) == null || optJSONObject2 == null || optJSONObject2.optString("color").equals(optJSONObject.optString("color"))) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isTitleTypeForDef(JSONObject jSONObject) {
        if (jSONObject != null) {
            return !jSONObject.has("type") || "default".equals(jSONObject.opt("type"));
        }
        return false;
    }

    private void jsonCompare(JSONObject jSONObject, JSONObject jSONObject2) {
        jSONObject.keys();
    }

    public static boolean paddingIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2.has("padding")) {
            return checkKeyValueIsModify("padding", jSONObject, jSONObject2);
        }
        if (jSONObject2.has(Constants.Name.PADDING_RIGHT)) {
            return checkKeyValueIsModify(Constants.Name.PADDING_RIGHT, jSONObject, jSONObject2);
        }
        if (jSONObject2.has(Constants.Name.PADDING_LEFT)) {
            return checkKeyValueIsModify(Constants.Name.PADDING_LEFT, jSONObject, jSONObject2);
        }
        if (jSONObject2.has(AbsoluteConst.JSON_KEY_PADDING_LEFT)) {
            return checkKeyValueIsModify(AbsoluteConst.JSON_KEY_PADDING_LEFT, jSONObject, jSONObject2);
        }
        if (jSONObject2.has(AbsoluteConst.JSON_KEY_PADDING_RIGHT)) {
            return checkKeyValueIsModify(AbsoluteConst.JSON_KEY_PADDING_RIGHT, jSONObject, jSONObject2);
        }
        return false;
    }

    public static boolean progressIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (!(jSONObject2 == null || !jSONObject2.has("progress") || jSONObject == null)) {
            if (jSONObject.has("progress") || !jSONObject2.has("progress")) {
                if (jSONObject.has("progress") && jSONObject2.has("progress")) {
                    if ((jSONObject.optJSONObject("progress") == null || Constants.Name.UNDEFINED.equals(jSONObject.optString("progress"))) && jSONObject2.optJSONObject("progress") != null && !Constants.Name.UNDEFINED.equals(jSONObject2.optString("progress"))) {
                        return true;
                    }
                    if (jSONObject.optJSONObject("progress") != null && !Constants.Name.UNDEFINED.equals(jSONObject.optString("progress")) && (jSONObject2.optJSONObject("progress") == null || Constants.Name.UNDEFINED.equals(jSONObject2.optString("progress")))) {
                        return true;
                    }
                    if (jSONObject.optJSONObject("progress") != null && !Constants.Name.UNDEFINED.equals(jSONObject.optString("progress"))) {
                        if ((!Constants.Name.UNDEFINED.equals(jSONObject2.optString("progress"))) && (jSONObject2.optJSONObject("progress") != null)) {
                            String optString = jSONObject.optString("height");
                            String optString2 = jSONObject.optString("color");
                            String optString3 = jSONObject2.optString("height");
                            String optString4 = jSONObject2.optString("color");
                            if (!optString.equals(optString3) || !optString2.equals(optString4)) {
                                return true;
                            }
                        }
                    }
                }
            } else if (jSONObject2.optJSONObject("progress") == null || !Constants.Name.UNDEFINED.equals(jSONObject2.optString("progress"))) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean redDotChange(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 != null && jSONObject2.has("redDotColor")) {
            if (jSONObject == null) {
                if (!TextUtils.isEmpty(jSONObject2.optString("redDotColor"))) {
                    return true;
                }
            } else if (jSONObject != null) {
                String optString = jSONObject.optString("redDotColor");
                String optString2 = jSONObject2.optString("redDotColor");
                if (!TextUtils.isEmpty(optString) || TextUtils.isEmpty(optString2)) {
                    return !TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2) && !optString.equals(optString2);
                }
                return true;
            }
        }
    }

    public static void setBackButton(ITitleNView iTitleNView, JSONObject jSONObject, int i) {
        String str;
        if (iTitleNView != null) {
            JSONObject jSONObject2 = null;
            if (!jSONObject.has("autoBackButton")) {
                iTitleNView.removeBackButton();
            } else if (jSONObject.optBoolean("autoBackButton")) {
                if (jSONObject.has("backButton") && jSONObject.optJSONObject("backButton") != null && !Constants.Name.UNDEFINED.equalsIgnoreCase(jSONObject.optString("backButton"))) {
                    jSONObject2 = jSONObject.optJSONObject("backButton");
                }
                String optString = jSONObject.optString("titlecolor");
                if (jSONObject2 == null || !jSONObject2.has("color")) {
                    if (TextUtils.isEmpty(optString)) {
                        optString = jSONObject.optString("titleColor");
                    }
                    if (!TextUtils.isEmpty(optString) && optString.startsWith("#") && optString.length() == 9) {
                        optString = "#" + optString.substring(3, optString.length());
                    }
                } else {
                    optString = jSONObject2.optString("color");
                }
                if (!TextUtils.isEmpty(optString) || optString.startsWith("#")) {
                    if (jSONObject2 == null || !jSONObject2.has("colorPressed")) {
                        str = changeColorAlpha(optString, 0.3f);
                    } else {
                        str = jSONObject2.optString("colorPressed");
                    }
                    iTitleNView.addBackButton(optString, str, jSONObject.optString("type"), jSONObject2);
                }
            } else {
                iTitleNView.removeBackButton();
            }
        }
    }

    public static void setBackgroundImage(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (jSONObject != null) {
            iTitleNView.setBackgroundImage(jSONObject.optString(Constants.Name.BACKGROUND_IMAGE));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0134  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setButtons(io.dcloud.common.DHInterface.ITitleNView r26, org.json.JSONObject r27, io.dcloud.common.DHInterface.IWebview r28) {
        /*
            r0 = r27
            if (r26 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.String r1 = "buttons"
            boolean r2 = r0.has(r1)
            if (r2 == 0) goto L_0x0189
            java.lang.String r2 = "titleColor"
            java.lang.String r2 = r0.optString(r2)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x001f
            java.lang.String r2 = "titlecolor"
            java.lang.String r2 = r0.optString(r2)
        L_0x001f:
            r3 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r15 = changeColorAlpha((java.lang.String) r2, (float) r3)
            org.json.JSONArray r14 = r0.optJSONArray(r1)
            java.lang.String r13 = "type"
            java.lang.String r12 = r0.optString(r13)
            if (r14 == 0) goto L_0x0189
            r10 = 0
        L_0x0032:
            int r0 = r14.length()
            if (r10 >= r0) goto L_0x0189
            org.json.JSONObject r0 = r14.optJSONObject(r10)
            java.lang.String r1 = "none"
            java.lang.String r8 = r0.optString(r13, r1)
            java.lang.String r1 = "color"
            java.lang.String r1 = r0.optString(r1)
            java.lang.String r2 = "colorPressed"
            java.lang.String r2 = r0.optString(r2)
            java.lang.String r3 = "width"
            java.lang.String r9 = r0.optString(r3)
            java.lang.String r3 = "background"
            java.lang.String r4 = "#7F333333"
            java.lang.String r18 = r0.optString(r3, r4)
            java.lang.String r3 = "transparent"
            boolean r3 = r3.equals(r12)
            r4 = 1050253722(0x3e99999a, float:0.3)
            if (r3 == 0) goto L_0x0071
            java.lang.String r1 = "#FFFFFF"
            java.lang.String r2 = changeColorAlpha((java.lang.String) r1, (float) r4)
        L_0x006e:
            r3 = r1
            r4 = r2
            goto L_0x0089
        L_0x0071:
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 == 0) goto L_0x007e
            java.lang.String r1 = changeColorAlpha((java.lang.String) r15, (float) r4)
            r4 = r1
            r3 = r15
            goto L_0x0089
        L_0x007e:
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x006e
            java.lang.String r2 = changeColorAlpha((java.lang.String) r1, (float) r4)
            goto L_0x006e
        L_0x0089:
            java.lang.String r1 = "__cb__"
            boolean r2 = r0.has(r1)
            if (r2 == 0) goto L_0x00a3
            org.json.JSONObject r1 = r0.optJSONObject(r1)
            java.lang.String r2 = "id"
            java.lang.String r2 = r1.optString(r2)
            java.lang.String r6 = "htmlId"
            r1.optString(r6)
            r19 = r2
            goto L_0x00c1
        L_0x00a3:
            java.lang.String r1 = "onClick"
            boolean r2 = r0.has(r1)
            if (r2 == 0) goto L_0x00b2
            java.lang.String r1 = r0.optString(r1)
        L_0x00af:
            r19 = r1
            goto L_0x00c1
        L_0x00b2:
            java.lang.String r1 = "onclick"
            boolean r2 = r0.has(r1)
            if (r2 == 0) goto L_0x00bf
            java.lang.String r1 = r0.optString(r1)
            goto L_0x00af
        L_0x00bf:
            r19 = 0
        L_0x00c1:
            java.lang.String r1 = "float"
            java.lang.String r1 = r0.optString(r1)
            java.lang.String r2 = "left"
            boolean r1 = r2.equals(r1)
            java.lang.String r2 = "maxWidth"
            java.lang.String r6 = "select"
            java.lang.String r7 = "redDot"
            java.lang.String r5 = "fontSrc"
            java.lang.String r11 = "fontSize"
            r21 = r10
            java.lang.String r10 = "fontWeight"
            r22 = r12
            java.lang.String r12 = "title"
            r23 = r13
            java.lang.String r13 = "text"
            r24 = r14
            java.lang.String r14 = "badgeText"
            if (r1 == 0) goto L_0x0134
            java.lang.String r1 = r0.optString(r13)
            java.lang.String r12 = r0.optString(r12)
            java.lang.String r10 = r0.optString(r10)
            java.lang.String r11 = r0.optString(r11)
            java.lang.String r13 = r0.optString(r5)
            boolean r25 = r0.optBoolean(r7)
            boolean r5 = r0.has(r14)
            if (r5 == 0) goto L_0x010f
            java.lang.String r5 = r0.optString(r14)
            r20 = r5
            r14 = 0
            goto L_0x0112
        L_0x010f:
            r14 = 0
            r20 = 0
        L_0x0112:
            boolean r16 = r0.optBoolean(r6, r14)
            java.lang.String r17 = r0.optString(r2)
            r0 = r26
            r2 = r12
            r5 = r10
            r6 = r11
            r7 = r13
            r10 = r19
            r11 = r28
            r12 = r22
            r13 = r18
            r14 = r25
            r25 = r15
            r15 = r20
            r0.addLeftButton(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)
            r18 = 0
            goto L_0x017d
        L_0x0134:
            r25 = r15
            java.lang.String r1 = r0.optString(r13)
            java.lang.String r12 = r0.optString(r12)
            java.lang.String r10 = r0.optString(r10)
            java.lang.String r11 = r0.optString(r11)
            java.lang.String r13 = r0.optString(r5)
            boolean r15 = r0.optBoolean(r7)
            boolean r5 = r0.has(r14)
            if (r5 == 0) goto L_0x015c
            java.lang.String r5 = r0.optString(r14)
            r20 = r5
            r14 = 0
            goto L_0x015f
        L_0x015c:
            r14 = 0
            r20 = 0
        L_0x015f:
            boolean r16 = r0.optBoolean(r6, r14)
            java.lang.String r17 = r0.optString(r2)
            r0 = r26
            r2 = r12
            r5 = r10
            r6 = r11
            r7 = r13
            r10 = r19
            r11 = r28
            r12 = r22
            r13 = r18
            r18 = 0
            r14 = r15
            r15 = r20
            r0.addRightButton(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)
        L_0x017d:
            int r10 = r21 + 1
            r12 = r22
            r13 = r23
            r14 = r24
            r15 = r25
            goto L_0x0032
        L_0x0189:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.setButtons(io.dcloud.common.DHInterface.ITitleNView, org.json.JSONObject, io.dcloud.common.DHInterface.IWebview):void");
    }

    public static void setCapsuleButtonStyle(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (iTitleNView != null && jSONObject != null && jSONObject.has("capsuleButtonStyle")) {
            iTitleNView.setCapsuleButtonStyle(jSONObject.optJSONObject("capsuleButtonStyle"));
        }
    }

    public static void setHomeButton(ITitleNView iTitleNView, JSONObject jSONObject, int i) {
        if (iTitleNView != null) {
            boolean z = false;
            boolean z2 = true;
            if (i == 5) {
                z = true;
            }
            if (!jSONObject.has("homeButton") || !jSONObject.optBoolean("homeButton")) {
                z2 = z;
            }
            if (z2) {
                String optString = jSONObject.optString("titlecolor");
                if (TextUtils.isEmpty(optString)) {
                    optString = jSONObject.optString("titleColor");
                }
                if (!TextUtils.isEmpty(optString) && optString.startsWith("#") && optString.length() == 9) {
                    optString = "#" + optString.substring(3, optString.length());
                }
                if (!TextUtils.isEmpty(optString) || optString.startsWith("#")) {
                    iTitleNView.addHomeButton(optString, changeColorAlpha(optString, 0.3f), jSONObject.optString("type"));
                    return;
                }
                return;
            }
            iTitleNView.removeHomeButton();
        }
    }

    public static void setProgress(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (iTitleNView == null || !jSONObject.has("progress")) {
            return;
        }
        if (jSONObject.optJSONObject("progress") == null || Constants.Name.UNDEFINED.equalsIgnoreCase(jSONObject.optString("progress"))) {
            iTitleNView.setProgress("", "");
            return;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject("progress");
        String optString = optJSONObject.optString("height");
        if (!optJSONObject.has("height") || "".equals(optString)) {
            optString = "2px";
        }
        String optString2 = optJSONObject.optString("color");
        if (!optJSONObject.has("color") || "".equals(optString2)) {
            optString2 = "#00FF00";
        }
        iTitleNView.setProgress(optString, optString2);
    }

    public static void setRedDotColor(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (jSONObject != null) {
            iTitleNView.setRedDotColor(PdrUtil.stringToColor(jSONObject.optString("redDotColor")));
        }
    }

    public static void setSearchInput(ITitleNView iTitleNView, JSONObject jSONObject, IWebview iWebview) {
        if (iTitleNView != null) {
            if (jSONObject.has("searchInput")) {
                JSONObject optJSONObject = jSONObject.optJSONObject("searchInput");
                iTitleNView.addSearchInput(optJSONObject.optString(AbsoluteConst.JSON_KEY_ALIGN), optJSONObject.optString("backgroundColor"), optJSONObject.optString(Constants.Name.BORDER_RADIUS, "0px"), optJSONObject.optString(Constants.Name.PLACEHOLDER), optJSONObject.optString(Constants.Name.PLACEHOLDER_COLOR, "#CCCCCC"), optJSONObject.optString("color", "#000000"), optJSONObject.optBoolean(Constants.Name.DISABLED, false), optJSONObject.optBoolean("autoFocus", false), iWebview);
                return;
            }
            iTitleNView.clearSearchInput();
        }
    }

    public static void setShadow(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (iTitleNView != null) {
            JSONObject optJSONObject = jSONObject.optJSONObject("shadow");
            if (optJSONObject != null) {
                iTitleNView.setShadow(optJSONObject);
            } else {
                iTitleNView.setShadow(new JSONObject());
            }
        }
    }

    public static void setSplitLine(ITitleNView iTitleNView, IWebview iWebview, JSONObject jSONObject, JSONObject jSONObject2, boolean z, String str) {
        if (iTitleNView == null) {
            return;
        }
        if (jSONObject2 != null) {
            JSONObject combinJSONObject = JSONUtil.combinJSONObject(jSONObject, jSONObject2);
            String optString = combinJSONObject.optString("color");
            if ((combinJSONObject.has("color") && "".equals(optString)) || !combinJSONObject.has("color")) {
                optString = "#CCCCCC";
            }
            if (z && "transparent".equals(str)) {
                optString = changeColorAlpha(optString, 0.0f);
            }
            try {
                if ("transparent".equals(str) && iWebview.obtainWindowView().getScrollY() == 0) {
                    optString = changeColorAlpha(optString, 0.0f);
                }
            } catch (Exception unused) {
            }
            String optString2 = combinJSONObject.optString("height");
            if (!TextUtils.isEmpty(optString)) {
                if (TextUtils.isEmpty(optString2)) {
                    optString2 = "1px";
                }
                iTitleNView.setSplitLine(optString2, optString);
            }
        } else if (jSONObject != null) {
            iTitleNView.removeSplitLine();
        }
    }

    public static void setSubTitleIcon(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (jSONObject != null) {
            String optString = jSONObject.optString("titleColor");
            if (TextUtils.isEmpty(optString)) {
                optString = jSONObject.optString("titlecolor");
            }
            ITitleNView iTitleNView2 = iTitleNView;
            iTitleNView2.setIconSubTitleStyle(jSONObject.optString("titleIcon"), jSONObject.optString("titleIconRadius"), jSONObject.optString("subtitleText"), jSONObject.optString("subtitleColor"), jSONObject.optString("subtitleSize"), jSONObject.optString("subtitleOverflow"), optString, jSONObject.optString("titleAlign"), jSONObject.optString("titleIconWidth"));
        }
    }

    public static void setTitleAlign(ITitleNView iTitleNView, JSONObject jSONObject) {
        if (jSONObject != null) {
            iTitleNView.setTitleAlign(jSONObject.optString("titleAlign"));
        }
    }

    public static void setTitleNViewButtonStyle(ITitleNView iTitleNView, String str, JSONObject jSONObject, IFrameView iFrameView) {
        int i;
        String str2;
        JSONObject optJSONObject;
        String optString;
        JSONObject jSONObject2 = jSONObject;
        try {
            i = Integer.parseInt(str);
        } catch (Exception unused) {
            i = -1;
        }
        if (jSONObject2 != null) {
            String str3 = "";
            String optString2 = jSONObject2.optString("type", str3);
            String optString3 = jSONObject2.optString("color");
            String optString4 = jSONObject2.optString("colorPressed");
            String optString5 = jSONObject2.optString("width");
            String optString6 = jSONObject2.optString("background");
            if (!PdrUtil.isEmpty(optString3) && PdrUtil.isEmpty(optString4)) {
                optString4 = changeColorAlpha(optString3, 0.3f);
            }
            String str4 = optString4;
            if (jSONObject2.has("__cb__")) {
                JSONObject optJSONObject2 = jSONObject2.optJSONObject("__cb__");
                String optString7 = optJSONObject2.optString("id");
                optJSONObject2.optString("htmlId");
                str2 = optString7;
            } else {
                if (jSONObject2.has("onClick")) {
                    optString = jSONObject2.optString("onClick");
                } else if (jSONObject2.has("onclick")) {
                    optString = jSONObject2.optString("onclick");
                } else {
                    str2 = null;
                }
                str2 = optString;
            }
            AdaFrameItem adaFrameItem = (AdaFrameItem) iFrameView;
            JSONObject jSONObject3 = adaFrameItem.obtainFrameOptions().titleNView;
            JSONArray optJSONArray = jSONObject3.optJSONArray("buttons");
            if (optJSONArray != null && i < optJSONArray.length() && i >= 0 && (optJSONObject = optJSONArray.optJSONObject(i)) != null) {
                str3 = optJSONObject.optString("width");
            }
            String str5 = str3;
            String optString8 = jSONObject2.optString("text");
            String optString9 = jSONObject2.optString(AbsoluteConst.JSON_KEY_TITLE);
            String optString10 = jSONObject2.optString(Constants.Name.FONT_WEIGHT);
            String optString11 = jSONObject2.optString(Constants.Name.FONT_SIZE);
            String optString12 = jSONObject2.optString("fontSrc");
            IWebview obtainWebView = iFrameView.obtainWebView();
            String optString13 = jSONObject2.optString("redDot");
            String optString14 = jSONObject2.has("badgeText") ? jSONObject2.optString("badgeText") : null;
            String optString15 = jSONObject2.optString("select");
            String optString16 = jSONObject3.optString("type");
            String optString17 = jSONObject2.optString(Constants.Name.MAX_WIDTH);
            JSONArray jSONArray = optJSONArray;
            JSONObject jSONObject4 = jSONObject3;
            if (iTitleNView.setTitleNViewButtonStyle(i, optString8, optString9, optString3, str4, optString10, optString11, optString12, optString2, str2, obtainWebView, optString6, optString13, optString14, optString15, optString5, optString16, optString17, str5) && jSONArray != null && i < jSONArray.length() && i >= 0) {
                JSONUtil.combinJSONObject(jSONArray.optJSONObject(i), jSONObject);
                adaFrameItem.obtainFrameOptions().setTitleNView(jSONObject4, iFrameView.obtainWebView());
            }
        }
    }

    public static void setTitleNViewPadding(ITitleNView iTitleNView, IWebview iWebview, JSONObject jSONObject) {
        int i;
        int i2;
        if (jSONObject.has("padding")) {
            i = PdrUtil.convertToScreenInt(jSONObject.optString("padding"), 0, 0, iWebview.getScale());
            i2 = PdrUtil.convertToScreenInt(jSONObject.optString("padding"), 0, 0, iWebview.getScale());
        } else {
            i2 = 0;
            i = 0;
        }
        if (jSONObject.has(Constants.Name.PADDING_LEFT)) {
            i = PdrUtil.convertToScreenInt(jSONObject.optString(Constants.Name.PADDING_LEFT), 0, 0, iWebview.getScale());
        } else if (jSONObject.has(AbsoluteConst.JSON_KEY_PADDING_LEFT)) {
            i = PdrUtil.convertToScreenInt(jSONObject.optString(AbsoluteConst.JSON_KEY_PADDING_LEFT), 0, 0, iWebview.getScale());
        }
        if (jSONObject.has(Constants.Name.PADDING_RIGHT)) {
            i2 = PdrUtil.convertToScreenInt(jSONObject.optString(Constants.Name.PADDING_RIGHT), 0, 0, iWebview.getScale());
        } else if (jSONObject.has(AbsoluteConst.JSON_KEY_PADDING_RIGHT)) {
            i2 = PdrUtil.convertToScreenInt(jSONObject.optString(AbsoluteConst.JSON_KEY_PADDING_RIGHT), 0, 0, iWebview.getScale());
        }
        iTitleNView.setTitleNViewPadding(i, 0, i2, 0);
    }

    public static void setTitleNViewSearchInputFocus(ITitleNView iTitleNView, String str) {
        iTitleNView.setSearchInputFocus(Boolean.parseBoolean(str));
    }

    public static void setTitleNViewSearchInputText(ITitleNView iTitleNView, String str) {
        iTitleNView.setTitleNViewSearchInputText(str);
    }

    public static boolean splitLineIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 != null && jSONObject2.has("splitLine")) {
            JSONObject jSONObject3 = null;
            if (jSONObject != null) {
                jSONObject3 = jSONObject.optJSONObject("splitLine");
            }
            JSONObject optJSONObject = jSONObject2.optJSONObject("splitLine");
            if (jSONObject3 == null && optJSONObject != null) {
                return true;
            }
            if (jSONObject3 != null && optJSONObject == null) {
                return true;
            }
            if (!(jSONObject3 == null || optJSONObject == null)) {
                String optString = jSONObject3.optString("color");
                String optString2 = optJSONObject.optString("color");
                if (optString == null && optString2 != null) {
                    return true;
                }
                if (optString != null && optString2 != null && !optString.equals(optString2)) {
                    return true;
                }
                String optString3 = jSONObject3.optString("height");
                String optString4 = optJSONObject.optString("height");
                if (optString3 == null && optString4 != null) {
                    return true;
                }
                if (optString3 == null || optString4 == null || optString3.equals(optString4)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static void startProcess(ITitleNView iTitleNView) {
        if (iTitleNView != null) {
            iTitleNView.startProgress();
        }
    }

    public static void stopProcess(ITitleNView iTitleNView) {
        if (iTitleNView != null) {
            iTitleNView.stopProgress();
        }
    }

    public static boolean subTitleIconChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 == null) {
            return false;
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        if (jSONObject2.optString("titleIcon").equals(jSONObject.optString("titleIcon")) && jSONObject2.optString("titleIconRadius").equals(jSONObject.optString("titleIconRadius")) && jSONObject2.optString("subtitleText").equals(jSONObject.optString("subtitleText")) && jSONObject2.optString("subtitleColor").equals(jSONObject.optString("subtitleColor")) && jSONObject2.optString("subtitleSize").equals(jSONObject.optString("subtitleSize")) && jSONObject2.optString("subtitleOverflow").equals(jSONObject.optString("subtitleOverflow"))) {
            return false;
        }
        return true;
    }

    public static boolean titleAlignIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 != null && jSONObject2.has("titleAlign")) {
            if (jSONObject == null) {
                return !TextUtils.isEmpty(jSONObject2.optString("titleAlign"));
            }
            String optString = jSONObject.optString("titleAlign");
            String optString2 = jSONObject2.optString("titleAlign");
            if (!TextUtils.isEmpty(optString) || TextUtils.isEmpty(optString2)) {
                return !TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2) && !optString.equals(optString2);
            }
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:78:0x00f7 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean titleColorIsChanged(org.json.JSONObject r5, org.json.JSONObject r6) {
        /*
            r0 = 0
            if (r6 == 0) goto L_0x00f7
            java.lang.String r1 = "titleColor"
            boolean r2 = r6.has(r1)
            java.lang.String r3 = "titlecolor"
            if (r2 != 0) goto L_0x0015
            boolean r2 = r6.has(r3)
            if (r2 != 0) goto L_0x0015
            goto L_0x00f7
        L_0x0015:
            r2 = 1
            if (r5 != 0) goto L_0x0039
            boolean r4 = r6.has(r1)
            if (r4 == 0) goto L_0x0028
            java.lang.String r4 = r6.optString(r1)
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x0038
        L_0x0028:
            boolean r4 = r6.has(r3)
            if (r4 == 0) goto L_0x0039
            java.lang.String r4 = r6.optString(r3)
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0039
        L_0x0038:
            return r2
        L_0x0039:
            if (r5 == 0) goto L_0x00f7
            boolean r4 = r5.has(r1)
            if (r4 == 0) goto L_0x0099
            java.lang.String r5 = r5.optString(r1)
            boolean r4 = r6.has(r1)
            if (r4 == 0) goto L_0x006f
            java.lang.String r6 = r6.optString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x005c
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x005c
            return r2
        L_0x005c:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x006f:
            boolean r1 = r6.has(r3)
            if (r1 == 0) goto L_0x00f7
            java.lang.String r6 = r6.optString(r3)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x0086
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x0086
            return r2
        L_0x0086:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x0099:
            boolean r4 = r5.has(r3)
            if (r4 == 0) goto L_0x00f7
            java.lang.String r5 = r5.optString(r3)
            boolean r4 = r6.has(r1)
            if (r4 == 0) goto L_0x00cd
            java.lang.String r6 = r6.optString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x00ba
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00ba
            return r2
        L_0x00ba:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x00cd:
            boolean r1 = r6.has(r3)
            if (r1 == 0) goto L_0x00f7
            java.lang.String r6 = r6.optString(r3)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x00e4
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00e4
            return r2
        L_0x00e4:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x00f7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.titleColorIsChanged(org.json.JSONObject, org.json.JSONObject):boolean");
    }

    public static boolean titleIsChanged(JSONObject jSONObject, JSONObject jSONObject2) {
        if (jSONObject2 != null && (jSONObject2.has("titleText") || jSONObject2.has("titletext"))) {
            if (jSONObject == null && ((jSONObject2.has("titleText") && !TextUtils.isEmpty(jSONObject2.optString("titleText"))) || (jSONObject2.has("titletext") && !TextUtils.isEmpty(jSONObject2.optString("titletext"))))) {
                return true;
            }
            if (jSONObject != null) {
                if (jSONObject.has("titleText")) {
                    String optString = jSONObject.optString("titleText");
                    if (jSONObject2.has("titleText")) {
                        String optString2 = jSONObject2.optString("titleText");
                        if ((!TextUtils.isEmpty(optString) || TextUtils.isEmpty(optString2)) && !TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2) && !optString.equals(optString2)) {
                            return true;
                        }
                    } else if (jSONObject2.has("titletext")) {
                        String optString3 = jSONObject2.optString("titletext");
                        if ((!TextUtils.isEmpty(optString) || TextUtils.isEmpty(optString3)) && !TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString3) && !optString.equals(optString3)) {
                            return true;
                        }
                    }
                } else if (jSONObject.has("titletext")) {
                    String optString4 = jSONObject.optString("titletext");
                    if (jSONObject2.has("titleText")) {
                        String optString5 = jSONObject2.optString("titleText");
                        if ((!TextUtils.isEmpty(optString4) || TextUtils.isEmpty(optString5)) && !TextUtils.isEmpty(optString4) && !TextUtils.isEmpty(optString5) && !optString4.equals(optString5)) {
                            return true;
                        }
                    } else if (jSONObject2.has("titletext")) {
                        String optString6 = jSONObject2.optString("titletext");
                        if ((!TextUtils.isEmpty(optString4) || TextUtils.isEmpty(optString6)) && !TextUtils.isEmpty(optString4) && !TextUtils.isEmpty(optString6) && !optString4.equals(optString6)) {
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void titleNViewButtonBadge(ITitleNView iTitleNView, JSONObject jSONObject, boolean z) {
        iTitleNView.setBadgeText(jSONObject, z);
    }

    public static void titleNViewButtonRedDot(ITitleNView iTitleNView, JSONObject jSONObject, boolean z) {
        iTitleNView.setRedDot(jSONObject, z);
    }

    public static boolean titleNViewStyleNoTitle(JSONObject jSONObject) {
        if (jSONObject == null) {
            return true;
        }
        if (!jSONObject.has("titleText") && !jSONObject.has("titletext")) {
            return true;
        }
        if (jSONObject.has("titleText")) {
            if (TextUtils.isEmpty(jSONObject.optString("titleText"))) {
                return true;
            }
            return false;
        } else if (!jSONObject.has("titletext") || !TextUtils.isEmpty(jSONObject.optString("titletext"))) {
            return false;
        } else {
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x006d A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean titleOverflowIsChanged(org.json.JSONObject r4, org.json.JSONObject r5) {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x006d
            java.lang.String r1 = "titleOverflow"
            boolean r2 = r5.has(r1)
            if (r2 != 0) goto L_0x000c
            goto L_0x006d
        L_0x000c:
            r2 = 1
            if (r4 != 0) goto L_0x0020
            boolean r3 = r5.has(r1)
            if (r3 == 0) goto L_0x0020
            java.lang.String r3 = r5.optString(r1)
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x0020
            return r2
        L_0x0020:
            if (r4 == 0) goto L_0x006d
            boolean r3 = r4.has(r1)
            if (r3 != 0) goto L_0x0039
            boolean r4 = r5.has(r1)
            if (r4 == 0) goto L_0x006d
            java.lang.String r4 = r5.optString(r1)
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x006d
            return r2
        L_0x0039:
            boolean r3 = r4.has(r1)
            if (r3 == 0) goto L_0x006d
            java.lang.String r4 = r4.optString(r1)
            boolean r3 = r5.has(r1)
            if (r3 == 0) goto L_0x006d
            java.lang.String r5 = r5.optString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r4)
            if (r1 == 0) goto L_0x005a
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x005a
            return r2
        L_0x005a:
            boolean r1 = android.text.TextUtils.isEmpty(r4)
            if (r1 != 0) goto L_0x006d
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x006d
            boolean r4 = r4.equals(r5)
            if (r4 != 0) goto L_0x006d
            return r2
        L_0x006d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.titleOverflowIsChanged(org.json.JSONObject, org.json.JSONObject):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:78:0x00f7 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean titleSizeIsChanged(org.json.JSONObject r5, org.json.JSONObject r6) {
        /*
            r0 = 0
            if (r6 == 0) goto L_0x00f7
            java.lang.String r1 = "titleSize"
            boolean r2 = r6.has(r1)
            java.lang.String r3 = "titlesize"
            if (r2 != 0) goto L_0x0015
            boolean r2 = r6.has(r3)
            if (r2 != 0) goto L_0x0015
            goto L_0x00f7
        L_0x0015:
            r2 = 1
            if (r5 != 0) goto L_0x0039
            boolean r4 = r6.has(r1)
            if (r4 == 0) goto L_0x0028
            java.lang.String r4 = r6.optString(r1)
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x0038
        L_0x0028:
            boolean r4 = r6.has(r3)
            if (r4 == 0) goto L_0x0039
            java.lang.String r4 = r6.optString(r3)
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0039
        L_0x0038:
            return r2
        L_0x0039:
            if (r5 == 0) goto L_0x00f7
            boolean r4 = r5.has(r1)
            if (r4 == 0) goto L_0x0099
            java.lang.String r5 = r5.optString(r1)
            boolean r4 = r6.has(r1)
            if (r4 == 0) goto L_0x006f
            java.lang.String r6 = r6.optString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x005c
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x005c
            return r2
        L_0x005c:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x006f:
            boolean r1 = r6.has(r3)
            if (r1 == 0) goto L_0x00f7
            java.lang.String r6 = r6.optString(r3)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x0086
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x0086
            return r2
        L_0x0086:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x0099:
            boolean r4 = r5.has(r3)
            if (r4 == 0) goto L_0x00f7
            java.lang.String r5 = r5.optString(r3)
            boolean r4 = r6.has(r1)
            if (r4 == 0) goto L_0x00cd
            java.lang.String r6 = r6.optString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x00ba
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00ba
            return r2
        L_0x00ba:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x00cd:
            boolean r1 = r6.has(r3)
            if (r1 == 0) goto L_0x00f7
            java.lang.String r6 = r6.optString(r3)
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 == 0) goto L_0x00e4
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00e4
            return r2
        L_0x00e4:
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x00f7
            boolean r1 = android.text.TextUtils.isEmpty(r6)
            if (r1 != 0) goto L_0x00f7
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x00f7
            return r2
        L_0x00f7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.titleSizeIsChanged(org.json.JSONObject, org.json.JSONObject):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:148:?, code lost:
        r15 = io.dcloud.common.util.PdrUtil.stringToColor(r6);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:147:0x02b9 */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0058 A[Catch:{ Exception -> 0x02de }] */
    /* JADX WARNING: Removed duplicated region for block: B:169:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0074 A[Catch:{ Exception -> 0x02de }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007e A[Catch:{ Exception -> 0x02de }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0090 A[Catch:{ Exception -> 0x02de }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00cf A[Catch:{ Exception -> 0x02de }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00df A[SYNTHETIC, Splitter:B:48:0x00df] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00f5 A[Catch:{ Exception -> 0x02de }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0107 A[SYNTHETIC, Splitter:B:59:0x0107] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0158  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01b0 A[Catch:{ Exception -> 0x02de }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateTitleNViewStatus(io.dcloud.common.DHInterface.ITitleNView r19, io.dcloud.common.DHInterface.IWebview r20, float r21, org.json.JSONObject r22, float r23) {
        /*
            r7 = r19
            r8 = r22
            java.lang.String r0 = "titlecolor"
            java.lang.String r9 = "#00333333"
            java.lang.String r1 = "titleColor"
            java.lang.String r10 = "color"
            if (r7 == 0) goto L_0x02de
            java.lang.String r2 = "backgroundColor"
            java.lang.String r11 = r8.optString(r2)     // Catch:{ Exception -> 0x02de }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x02de }
            java.lang.String r3 = r19.getStatusBarColor()     // Catch:{ Exception -> 0x02de }
            r2.<init>(r3)     // Catch:{ Exception -> 0x02de }
            int r3 = r2.optInt(r10)     // Catch:{ Exception -> 0x02de }
            java.lang.String r4 = "alpha"
            boolean r2 = r2.optBoolean(r4)     // Catch:{ Exception -> 0x02de }
            java.lang.String r4 = r8.optString(r1)     // Catch:{ Exception -> 0x02de }
            boolean r5 = r8.has(r1)     // Catch:{ Exception -> 0x02de }
            if (r5 == 0) goto L_0x0037
            java.lang.String r0 = r8.optString(r1)     // Catch:{ Exception -> 0x02de }
        L_0x0035:
            r4 = r0
            goto L_0x0042
        L_0x0037:
            boolean r1 = r8.has(r0)     // Catch:{ Exception -> 0x02de }
            if (r1 == 0) goto L_0x0042
            java.lang.String r0 = r8.optString(r0)     // Catch:{ Exception -> 0x02de }
            goto L_0x0035
        L_0x0042:
            r12 = r4
            int r0 = r19.getTitleColor()     // Catch:{ Exception -> 0x02de }
            java.lang.String r1 = "splitLine"
            org.json.JSONObject r4 = r8.optJSONObject(r1)     // Catch:{ Exception -> 0x02de }
            r1 = 0
            if (r4 == 0) goto L_0x006d
            java.lang.String r5 = "undefined"
            boolean r5 = r5.equals(r4)     // Catch:{ Exception -> 0x02de }
            if (r5 != 0) goto L_0x006d
            java.lang.String r5 = r4.optString(r10)     // Catch:{ Exception -> 0x02de }
            boolean r6 = r4.has(r10)     // Catch:{ Exception -> 0x02de }
            if (r6 == 0) goto L_0x006a
            java.lang.String r6 = ""
            boolean r6 = r6.equals(r5)     // Catch:{ Exception -> 0x02de }
            if (r6 == 0) goto L_0x006e
        L_0x006a:
            java.lang.String r5 = "#CCCCCC"
            goto L_0x006e
        L_0x006d:
            r5 = r1
        L_0x006e:
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x02de }
            if (r6 != 0) goto L_0x0075
            r1 = r5
        L_0x0075:
            r13 = 1132396544(0x437f0000, float:255.0)
            r6 = 0
            r14 = 1065353216(0x3f800000, float:1.0)
            int r15 = (r21 > r6 ? 1 : (r21 == r6 ? 0 : -1))
            if (r15 != 0) goto L_0x0090
            java.lang.String r2 = changeColorAlpha((java.lang.String) r11, (float) r6)     // Catch:{ Exception -> 0x02de }
            int r3 = changeColorAlpha((int) r3, (float) r6)     // Catch:{ Exception -> 0x02de }
            int r16 = changeColorAlpha((int) r0, (float) r6)     // Catch:{ Exception -> 0x02de }
            java.lang.String r5 = changeColorAlpha((java.lang.String) r5, (float) r6)     // Catch:{ Exception -> 0x02de }
            r14 = r2
            goto L_0x009f
        L_0x0090:
            float r6 = r21 / r23
            int r16 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r16 < 0) goto L_0x00a2
            int r16 = changeColorAlpha((int) r0, (float) r14)     // Catch:{ Exception -> 0x02de }
            java.lang.String r5 = changeColorAlpha((java.lang.String) r5, (float) r14)     // Catch:{ Exception -> 0x02de }
            r14 = r11
        L_0x009f:
            r2 = r16
            goto L_0x00cd
        L_0x00a2:
            int r14 = getColorAlpha(r11)     // Catch:{ Exception -> 0x02de }
            float r14 = (float) r14     // Catch:{ Exception -> 0x02de }
            float r14 = r14 / r13
            int r14 = (r14 > r6 ? 1 : (r14 == r6 ? 0 : -1))
            if (r14 <= 0) goto L_0x00b1
            java.lang.String r14 = changeColorAlpha((java.lang.String) r11, (float) r6)     // Catch:{ Exception -> 0x02de }
            goto L_0x00b2
        L_0x00b1:
            r14 = r11
        L_0x00b2:
            int r13 = android.graphics.Color.alpha(r3)     // Catch:{ Exception -> 0x02de }
            float r13 = (float) r13     // Catch:{ Exception -> 0x02de }
            r17 = 1132396544(0x437f0000, float:255.0)
            float r13 = r13 / r17
            int r13 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
            if (r13 > 0) goto L_0x00c1
            if (r2 == 0) goto L_0x00c5
        L_0x00c1:
            int r3 = changeColorAlpha((int) r3, (float) r6)     // Catch:{ Exception -> 0x02de }
        L_0x00c5:
            int r2 = changeColorAlpha((int) r0, (float) r6)     // Catch:{ Exception -> 0x02de }
            java.lang.String r5 = changeColorAlpha((java.lang.String) r5, (float) r6)     // Catch:{ Exception -> 0x02de }
        L_0x00cd:
            if (r0 == r2) goto L_0x00d2
            r7.setTitleColor((int) r2)     // Catch:{ Exception -> 0x02de }
        L_0x00d2:
            r7.setStatusBarColor(r3)     // Catch:{ Exception -> 0x02de }
            if (r1 == 0) goto L_0x00f3
            boolean r0 = r1.equalsIgnoreCase(r5)     // Catch:{ Exception -> 0x02de }
            if (r0 != 0) goto L_0x00f3
            if (r4 == 0) goto L_0x00e8
            r4.put(r10, r5)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x00e8
        L_0x00e3:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()     // Catch:{ Exception -> 0x02de }
        L_0x00e8:
            r5 = 0
            java.lang.String r6 = ""
            r1 = r19
            r2 = r20
            r3 = r4
            setSplitLine(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x02de }
        L_0x00f3:
            if (r11 == 0) goto L_0x00f8
            r7.setBackgroundColor(r14)     // Catch:{ Exception -> 0x02de }
        L_0x00f8:
            java.lang.String r1 = "buttons"
            java.lang.String r2 = "backButton"
            java.lang.String r3 = "background"
            java.lang.String r4 = "#7F333333"
            java.lang.String r5 = "#FFFFFF"
            r6 = 1050253722(0x3e99999a, float:0.3)
            if (r15 != 0) goto L_0x0158
            org.json.JSONObject r2 = r8.optJSONObject(r2)     // Catch:{ Exception -> 0x02de }
            if (r2 == 0) goto L_0x0118
            boolean r9 = r2.has(r3)     // Catch:{ Exception -> 0x02de }
            if (r9 == 0) goto L_0x0118
            java.lang.String r2 = r2.optString(r3)     // Catch:{ Exception -> 0x02de }
            goto L_0x0119
        L_0x0118:
            r2 = r4
        L_0x0119:
            r9 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r10 = changeColorAlpha((java.lang.String) r5, (float) r9)     // Catch:{ Exception -> 0x02de }
            java.lang.String r11 = changeColorAlpha((java.lang.String) r5, (float) r6)     // Catch:{ Exception -> 0x02de }
            r7.setBackButtonColor(r2, r10, r11)     // Catch:{ Exception -> 0x02de }
            java.lang.String r2 = changeColorAlpha((java.lang.String) r5, (float) r9)     // Catch:{ Exception -> 0x02de }
            java.lang.String r9 = changeColorAlpha((java.lang.String) r5, (float) r6)     // Catch:{ Exception -> 0x02de }
            r7.setHomeButtonColor(r4, r2, r9)     // Catch:{ Exception -> 0x02de }
            org.json.JSONArray r1 = r8.optJSONArray(r1)     // Catch:{ Exception -> 0x02de }
            if (r1 == 0) goto L_0x02de
            r0 = 0
        L_0x0138:
            int r2 = r1.length()     // Catch:{ Exception -> 0x02de }
            if (r0 >= r2) goto L_0x02de
            org.json.JSONObject r2 = r1.optJSONObject(r0)     // Catch:{ Exception -> 0x02de }
            if (r2 == 0) goto L_0x0155
            java.lang.String r2 = r2.optString(r3, r4)     // Catch:{ Exception -> 0x02de }
            r8 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r9 = changeColorAlpha((java.lang.String) r5, (float) r8)     // Catch:{ Exception -> 0x02de }
            java.lang.String r8 = changeColorAlpha((java.lang.String) r5, (float) r6)     // Catch:{ Exception -> 0x02de }
            r7.setButtonColorByIndex(r0, r2, r9, r8)     // Catch:{ Exception -> 0x02de }
        L_0x0155:
            int r0 = r0 + 1
            goto L_0x0138
        L_0x0158:
            java.lang.String r11 = "colorPressed"
            int r13 = (r23 > r21 ? 1 : (r23 == r21 ? 0 : -1))
            if (r13 > 0) goto L_0x01e1
            r13 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r3 = changeColorAlpha((java.lang.String) r12, (float) r13)     // Catch:{ Exception -> 0x02de }
            org.json.JSONObject r2 = r8.optJSONObject(r2)     // Catch:{ Exception -> 0x02de }
            java.lang.String r4 = changeColorAlpha((java.lang.String) r3, (float) r6)     // Catch:{ Exception -> 0x02de }
            if (r2 == 0) goto L_0x0198
            boolean r5 = r2.has(r10)     // Catch:{ Exception -> 0x02de }
            if (r5 != 0) goto L_0x017a
            boolean r5 = r2.has(r11)     // Catch:{ Exception -> 0x02de }
            if (r5 == 0) goto L_0x0198
        L_0x017a:
            java.lang.String r4 = r2.optString(r10)     // Catch:{ Exception -> 0x02de }
            java.lang.String r2 = r2.optString(r11)     // Catch:{ Exception -> 0x02de }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x02de }
            if (r5 == 0) goto L_0x018d
            java.lang.String r4 = changeColorAlpha((java.lang.String) r3, (float) r6)     // Catch:{ Exception -> 0x02de }
            goto L_0x0198
        L_0x018d:
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x02de }
            if (r5 == 0) goto L_0x019a
            java.lang.String r2 = changeColorAlpha((java.lang.String) r4, (float) r6)     // Catch:{ Exception -> 0x02de }
            goto L_0x019a
        L_0x0198:
            r2 = r4
            r4 = r3
        L_0x019a:
            r7.setBackButtonColor(r9, r4, r2)     // Catch:{ Exception -> 0x02de }
            r2 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r2 = changeColorAlpha((java.lang.String) r3, (float) r2)     // Catch:{ Exception -> 0x02de }
            java.lang.String r4 = changeColorAlpha((java.lang.String) r3, (float) r6)     // Catch:{ Exception -> 0x02de }
            r7.setHomeButtonColor(r9, r2, r4)     // Catch:{ Exception -> 0x02de }
            org.json.JSONArray r1 = r8.optJSONArray(r1)     // Catch:{ Exception -> 0x02de }
            if (r1 == 0) goto L_0x02de
            r0 = 0
        L_0x01b1:
            int r2 = r1.length()     // Catch:{ Exception -> 0x02de }
            if (r0 >= r2) goto L_0x02de
            org.json.JSONObject r2 = r1.optJSONObject(r0)     // Catch:{ Exception -> 0x02de }
            if (r2 == 0) goto L_0x01de
            java.lang.String r4 = r2.optString(r10)     // Catch:{ Exception -> 0x02de }
            java.lang.String r2 = r2.optString(r11)     // Catch:{ Exception -> 0x02de }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x02de }
            if (r5 == 0) goto L_0x01d1
            java.lang.String r2 = changeColorAlpha((java.lang.String) r3, (float) r6)     // Catch:{ Exception -> 0x02de }
            r4 = r3
            goto L_0x01db
        L_0x01d1:
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x02de }
            if (r5 == 0) goto L_0x01db
            java.lang.String r2 = changeColorAlpha((java.lang.String) r4, (float) r6)     // Catch:{ Exception -> 0x02de }
        L_0x01db:
            r7.setButtonColorByIndex(r0, r9, r4, r2)     // Catch:{ Exception -> 0x02de }
        L_0x01de:
            int r0 = r0 + 1
            goto L_0x01b1
        L_0x01e1:
            float r9 = r21 / r23
            r13 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r12 = changeColorAlpha((java.lang.String) r12, (float) r13)     // Catch:{ Exception -> 0x02de }
            java.lang.String r13 = color2Color(r5, r12, r9)     // Catch:{ Exception -> 0x02de }
            org.json.JSONObject r2 = r8.optJSONObject(r2)     // Catch:{ Exception -> 0x02de }
            if (r2 == 0) goto L_0x01fe
            boolean r14 = r2.has(r3)     // Catch:{ Exception -> 0x02de }
            if (r14 == 0) goto L_0x01fe
            java.lang.String r14 = r2.optString(r3)     // Catch:{ Exception -> 0x02de }
            goto L_0x01ff
        L_0x01fe:
            r14 = r4
        L_0x01ff:
            java.lang.String r15 = changeColorAlpha((java.lang.String) r13, (float) r6)     // Catch:{ Exception -> 0x02de }
            if (r2 == 0) goto L_0x022a
            java.lang.String r15 = r2.optString(r10)     // Catch:{ Exception -> 0x02de }
            java.lang.String r2 = r2.optString(r11)     // Catch:{ Exception -> 0x02de }
            boolean r18 = android.text.TextUtils.isEmpty(r15)     // Catch:{ Exception -> 0x02de }
            if (r18 == 0) goto L_0x0219
            java.lang.String r2 = changeColorAlpha((java.lang.String) r12, (float) r6)     // Catch:{ Exception -> 0x02de }
            r15 = r12
            goto L_0x0223
        L_0x0219:
            boolean r18 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x02de }
            if (r18 == 0) goto L_0x0223
            java.lang.String r2 = changeColorAlpha((java.lang.String) r15, (float) r6)     // Catch:{ Exception -> 0x02de }
        L_0x0223:
            java.lang.String r15 = color2Color(r5, r15, r9)     // Catch:{ Exception -> 0x02de }
            r16 = 1065353216(0x3f800000, float:1.0)
            goto L_0x022e
        L_0x022a:
            r2 = r15
            r16 = 1065353216(0x3f800000, float:1.0)
            r15 = r13
        L_0x022e:
            float r0 = r16 - r9
            int r16 = android.graphics.Color.parseColor(r14)     // Catch:{ Exception -> 0x02de }
            int r6 = android.graphics.Color.alpha(r16)     // Catch:{ Exception -> 0x02de }
            float r6 = (float) r6     // Catch:{ Exception -> 0x02de }
            r16 = 1132396544(0x437f0000, float:255.0)
            float r6 = r6 / r16
            int r6 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r6 >= 0) goto L_0x0249
            java.lang.String r6 = changeColorAlpha((java.lang.String) r14, (float) r0)     // Catch:{ Exception -> 0x02de }
            r7.setBackButtonColor(r6, r15, r2)     // Catch:{ Exception -> 0x02de }
            goto L_0x024c
        L_0x0249:
            r7.setBackButtonColor(r14, r15, r2)     // Catch:{ Exception -> 0x02de }
        L_0x024c:
            int r2 = android.graphics.Color.parseColor(r4)     // Catch:{ Exception -> 0x02de }
            int r2 = android.graphics.Color.alpha(r2)     // Catch:{ Exception -> 0x02de }
            float r2 = (float) r2     // Catch:{ Exception -> 0x02de }
            r6 = 1132396544(0x437f0000, float:255.0)
            float r2 = r2 / r6
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x026b
            java.lang.String r2 = changeColorAlpha((java.lang.String) r4, (float) r0)     // Catch:{ Exception -> 0x02de }
            r6 = 1050253722(0x3e99999a, float:0.3)
            java.lang.String r14 = changeColorAlpha((java.lang.String) r13, (float) r6)     // Catch:{ Exception -> 0x02de }
            r7.setHomeButtonColor(r2, r13, r14)     // Catch:{ Exception -> 0x02de }
            goto L_0x0275
        L_0x026b:
            r6 = 1050253722(0x3e99999a, float:0.3)
            java.lang.String r2 = changeColorAlpha((java.lang.String) r13, (float) r6)     // Catch:{ Exception -> 0x02de }
            r7.setHomeButtonColor(r4, r13, r2)     // Catch:{ Exception -> 0x02de }
        L_0x0275:
            org.json.JSONArray r1 = r8.optJSONArray(r1)     // Catch:{ Exception -> 0x02de }
            if (r1 == 0) goto L_0x02de
            r2 = 0
        L_0x027c:
            int r6 = r1.length()     // Catch:{ Exception -> 0x02de }
            if (r2 >= r6) goto L_0x02de
            org.json.JSONObject r6 = r1.optJSONObject(r2)     // Catch:{ Exception -> 0x02de }
            if (r6 == 0) goto L_0x02d6
            java.lang.String r8 = r6.optString(r10)     // Catch:{ Exception -> 0x02de }
            java.lang.String r13 = r6.optString(r11)     // Catch:{ Exception -> 0x02de }
            boolean r14 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Exception -> 0x02de }
            if (r14 == 0) goto L_0x029f
            r14 = 1050253722(0x3e99999a, float:0.3)
            java.lang.String r13 = changeColorAlpha((java.lang.String) r12, (float) r14)     // Catch:{ Exception -> 0x02de }
            r8 = r12
            goto L_0x02ac
        L_0x029f:
            r14 = 1050253722(0x3e99999a, float:0.3)
            boolean r15 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Exception -> 0x02de }
            if (r15 == 0) goto L_0x02ac
            java.lang.String r13 = changeColorAlpha((java.lang.String) r8, (float) r14)     // Catch:{ Exception -> 0x02de }
        L_0x02ac:
            java.lang.String r8 = color2Color(r5, r8, r9)     // Catch:{ Exception -> 0x02de }
            java.lang.String r6 = r6.optString(r3, r4)     // Catch:{ Exception -> 0x02de }
            int r15 = android.graphics.Color.parseColor(r6)     // Catch:{ Exception -> 0x02b9 }
            goto L_0x02bd
        L_0x02b9:
            int r15 = io.dcloud.common.util.PdrUtil.stringToColor(r6)     // Catch:{ Exception -> 0x02de }
        L_0x02bd:
            int r15 = android.graphics.Color.alpha(r15)     // Catch:{ Exception -> 0x02de }
            float r15 = (float) r15     // Catch:{ Exception -> 0x02de }
            r16 = 1132396544(0x437f0000, float:255.0)
            float r15 = r15 / r16
            int r15 = (r0 > r15 ? 1 : (r0 == r15 ? 0 : -1))
            if (r15 >= 0) goto L_0x02d2
            java.lang.String r6 = changeColorAlpha((java.lang.String) r6, (float) r0)     // Catch:{ Exception -> 0x02de }
            r7.setButtonColorByIndex(r2, r6, r8, r13)     // Catch:{ Exception -> 0x02de }
            goto L_0x02db
        L_0x02d2:
            r7.setButtonColorByIndex(r2, r6, r8, r13)     // Catch:{ Exception -> 0x02de }
            goto L_0x02db
        L_0x02d6:
            r14 = 1050253722(0x3e99999a, float:0.3)
            r16 = 1132396544(0x437f0000, float:255.0)
        L_0x02db:
            int r2 = r2 + 1
            goto L_0x027c
        L_0x02de:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TitleNViewUtil.updateTitleNViewStatus(io.dcloud.common.DHInterface.ITitleNView, io.dcloud.common.DHInterface.IWebview, float, org.json.JSONObject, float):void");
    }

    public static String changeColorAlpha(String str, float f) {
        String[] stringSplit;
        try {
            if (TextUtils.isEmpty(str) || 0.0f > f || f > 1.0f) {
                return null;
            }
            if (str.startsWith("#")) {
                String substring = str.substring(1, str.length());
                if (substring.length() == 3) {
                    str = "#" + substring.charAt(0) + substring.charAt(0) + substring.charAt(1) + substring.charAt(1) + substring.charAt(2) + substring.charAt(2);
                }
                int parseColor = Color.parseColor(str);
                String hexString = Integer.toHexString((int) (f * 255.0f));
                if (1 == hexString.length()) {
                    hexString = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString;
                }
                String hexString2 = Integer.toHexString(Color.red(parseColor));
                if (1 == hexString2.length()) {
                    hexString2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString2;
                }
                String hexString3 = Integer.toHexString(Color.green(parseColor));
                if (1 == hexString3.length()) {
                    hexString3 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString3;
                }
                String hexString4 = Integer.toHexString(Color.blue(parseColor));
                if (1 == hexString4.length()) {
                    hexString4 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString4;
                }
                return "#" + hexString + hexString2 + hexString3 + hexString4;
            } else if (!str.startsWith("rgba") || !str.contains(Operators.BRACKET_START_STR) || !str.contains(Operators.BRACKET_END_STR) || !str.contains(",") || (stringSplit = PdrUtil.stringSplit(str.substring(str.indexOf(Operators.BRACKET_START_STR) + 1, str.indexOf(Operators.BRACKET_END_STR)), ",")) == null || 4 != stringSplit.length) {
                return null;
            } else {
                return "rgba(" + stringSplit[0] + "," + stringSplit[1] + "," + stringSplit[2] + "," + f + Operators.BRACKET_END_STR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
