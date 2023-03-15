package io.dcloud.feature.nativeObj;

import android.text.TextUtils;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NativeBitmapMgr {
    public final String SUCCESS_INFO = "{path:'file://%s', w:%d, h:%d, size:%d}";
    private HashMap<String, String> mIds = new HashMap<>();
    protected LinkedHashMap<String, NativeView> mNativeViews = new LinkedHashMap<>();
    private HashMap<String, INativeBitmap> mSnaps = new HashMap<>();

    /* renamed from: io.dcloud.feature.nativeObj.NativeBitmapMgr$7  reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action;

        /* JADX WARNING: Can't wrap try/catch for region: R(76:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|(3:75|76|78)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(78:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|75|76|78) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00a8 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00cc */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00d8 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00e4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00f0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00fc */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x0108 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x0114 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x0120 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x012c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x0138 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0144 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x0150 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x015c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:61:0x0168 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:63:0x0174 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x0180 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:67:0x018c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:69:0x0198 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:71:0x01a4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:73:0x01b0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:75:0x01bc */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action[] r0 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action = r0
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.View     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.setStyle     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.addEventListener     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.interceptTouchEvent     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.setTouchEventRect     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0049 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.getViewById     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0054 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.evalWeexJS     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0060 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.drawRichText     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x006c }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.drawBitmap     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0078 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.drawText     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0084 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.drawInput     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0090 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.getInputValueById     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x009c }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.getInputFocusById     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00a8 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.setInputFocusById     // Catch:{ NoSuchFieldError -> 0x00a8 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a8 }
                r2 = 14
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00a8 }
            L_0x00a8:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00b4 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.show     // Catch:{ NoSuchFieldError -> 0x00b4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b4 }
                r2 = 15
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b4 }
            L_0x00b4:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00c0 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.hide     // Catch:{ NoSuchFieldError -> 0x00c0 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c0 }
                r2 = 16
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c0 }
            L_0x00c0:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00cc }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_close     // Catch:{ NoSuchFieldError -> 0x00cc }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00cc }
                r2 = 17
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00cc }
            L_0x00cc:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00d8 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_animate     // Catch:{ NoSuchFieldError -> 0x00d8 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00d8 }
                r2 = 18
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00d8 }
            L_0x00d8:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00e4 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_reset     // Catch:{ NoSuchFieldError -> 0x00e4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00e4 }
                r2 = 19
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00e4 }
            L_0x00e4:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00f0 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_restore     // Catch:{ NoSuchFieldError -> 0x00f0 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00f0 }
                r2 = 20
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00f0 }
            L_0x00f0:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x00fc }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_drawRect     // Catch:{ NoSuchFieldError -> 0x00fc }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00fc }
                r2 = 21
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00fc }
            L_0x00fc:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0108 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.isVisible     // Catch:{ NoSuchFieldError -> 0x0108 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0108 }
                r2 = 22
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0108 }
            L_0x0108:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0114 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.Bitmap     // Catch:{ NoSuchFieldError -> 0x0114 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0114 }
                r2 = 23
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0114 }
            L_0x0114:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0120 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.getItems     // Catch:{ NoSuchFieldError -> 0x0120 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0120 }
                r2 = 24
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0120 }
            L_0x0120:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x012c }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.getBitmapById     // Catch:{ NoSuchFieldError -> 0x012c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x012c }
                r2 = 25
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x012c }
            L_0x012c:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0138 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.clear     // Catch:{ NoSuchFieldError -> 0x0138 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0138 }
                r2 = 26
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0138 }
            L_0x0138:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0144 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.bitmapRecycle     // Catch:{ NoSuchFieldError -> 0x0144 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0144 }
                r2 = 27
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0144 }
            L_0x0144:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0150 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.load     // Catch:{ NoSuchFieldError -> 0x0150 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0150 }
                r2 = 28
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0150 }
            L_0x0150:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x015c }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.loadBase64Data     // Catch:{ NoSuchFieldError -> 0x015c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x015c }
                r2 = 29
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x015c }
            L_0x015c:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0168 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.save     // Catch:{ NoSuchFieldError -> 0x0168 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0168 }
                r2 = 30
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0168 }
            L_0x0168:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0174 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.toBase64Data     // Catch:{ NoSuchFieldError -> 0x0174 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0174 }
                r2 = 31
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0174 }
            L_0x0174:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0180 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.startAnimation     // Catch:{ NoSuchFieldError -> 0x0180 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0180 }
                r2 = 32
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0180 }
            L_0x0180:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x018c }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.clearAnimation     // Catch:{ NoSuchFieldError -> 0x018c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x018c }
                r2 = 33
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x018c }
            L_0x018c:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x0198 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_clearRect     // Catch:{ NoSuchFieldError -> 0x0198 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0198 }
                r2 = 34
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0198 }
            L_0x0198:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x01a4 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.view_draw     // Catch:{ NoSuchFieldError -> 0x01a4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x01a4 }
                r2 = 35
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x01a4 }
            L_0x01a4:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x01b0 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.setImages     // Catch:{ NoSuchFieldError -> 0x01b0 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x01b0 }
                r2 = 36
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x01b0 }
            L_0x01b0:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x01bc }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.addImages     // Catch:{ NoSuchFieldError -> 0x01bc }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x01bc }
                r2 = 37
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x01bc }
            L_0x01bc:
                int[] r0 = $SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ NoSuchFieldError -> 0x01c8 }
                io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r1 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.currentImageIndex     // Catch:{ NoSuchFieldError -> 0x01c8 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x01c8 }
                r2 = 38
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x01c8 }
            L_0x01c8:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeBitmapMgr.AnonymousClass7.<clinit>():void");
        }
    }

    protected enum Action {
        Bitmap,
        getItems,
        getBitmapById,
        clear,
        load,
        loadBase64Data,
        save,
        toBase64Data,
        View,
        startAnimation,
        clearAnimation,
        getViewById,
        drawBitmap,
        drawText,
        evalWeexJS,
        drawRichText,
        show,
        hide,
        setImages,
        addImages,
        view_animate,
        view_reset,
        view_restore,
        view_drawRect,
        isVisible,
        addEventListener,
        interceptTouchEvent,
        setTouchEventRect,
        bitmapRecycle,
        setStyle,
        view_clearRect,
        view_draw,
        view_close,
        currentImageIndex,
        drawInput,
        getInputValueById,
        getInputFocusById,
        setInputFocusById
    }

    private void createBitmap(IApp iApp, String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "" + System.currentTimeMillis();
        }
        if (!this.mSnaps.containsKey(str)) {
            this.mSnaps.put(str, new NativeBitmap(iApp, str2, str, str3));
            this.mIds.put(str2, str);
        }
    }

    private NativeView getNativeView(String str, String str2) {
        NativeView nativeView = this.mNativeViews.get(str2);
        if (nativeView != null || TextUtils.isEmpty(str)) {
            return nativeView;
        }
        for (NativeView next : this.mNativeViews.values()) {
            if (TextUtils.equals(next.mID, str)) {
                return next;
            }
        }
        return nativeView;
    }

    private void load(final IWebview iWebview, NativeBitmap nativeBitmap, String str, final String str2) {
        nativeBitmap.load(iWebview, iWebview.obtainFrameView().obtainMainView().getContext(), str, TextUtils.isEmpty(str2) ? null : new ICallBack() {
            public Object onCallBack(int i, Object obj) {
                Deprecated_JSUtil.execCallback(iWebview, str2, (String) null, JSUtil.OK, false, false);
                return null;
            }
        }, TextUtils.isEmpty(str2) ? null : new ICallBack() {
            public Object onCallBack(int i, Object obj) {
                String string = obj == null ? iWebview.getContext().getString(R.string.dcloud_native_obj_load_failed) : obj.toString();
                IWebview iWebview = iWebview;
                String str = str2;
                Deprecated_JSUtil.execCallback(iWebview, str, "{\"code\":-100,\"message\":\"" + string + "\"}", JSUtil.ERROR, true, false);
                return null;
            }
        });
    }

    private void loadBase64Data(final IWebview iWebview, NativeBitmap nativeBitmap, String str, final String str2) {
        AnonymousClass3 r0;
        AnonymousClass4 r1 = null;
        if (TextUtils.isEmpty(str2)) {
            r0 = null;
        } else {
            r0 = new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    Deprecated_JSUtil.execCallback(iWebview, str2, (String) null, JSUtil.OK, false, false);
                    return null;
                }
            };
        }
        if (!TextUtils.isEmpty(str2)) {
            r1 = new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    IWebview iWebview = iWebview;
                    String str = str2;
                    Deprecated_JSUtil.execCallback(iWebview, str, "{\"code\":-100,\"message\":\"" + iWebview.getContext().getString(R.string.dcloud_native_obj_load_failed) + "\"}", JSUtil.ERROR, true, false);
                    return null;
                }
            };
        }
        nativeBitmap.loadBase64Data(str, r0, r1);
    }

    private void save(final IWebview iWebview, NativeBitmap nativeBitmap, String str, JSONObject jSONObject, final String str2) {
        nativeBitmap.save(iWebview.obtainFrameView().obtainApp(), str, new NativeBitmapSaveOptions(jSONObject.toString()), iWebview.getScale(), TextUtils.isEmpty(str2) ? null : new ICallBack() {
            public Object onCallBack(int i, Object obj) {
                String str;
                if (obj == null || !(obj instanceof NativeBitmapSaveOptions)) {
                    str = null;
                } else {
                    NativeBitmapSaveOptions nativeBitmapSaveOptions = (NativeBitmapSaveOptions) obj;
                    str = StringUtil.format("{path:'file://%s', w:%d, h:%d, size:%d}", nativeBitmapSaveOptions.path, Integer.valueOf(nativeBitmapSaveOptions.width), Integer.valueOf(nativeBitmapSaveOptions.height), Long.valueOf(nativeBitmapSaveOptions.size));
                }
                Deprecated_JSUtil.execCallback(iWebview, str2, str, JSUtil.OK, true, false);
                return null;
            }
        }, TextUtils.isEmpty(str2) ? null : new ICallBack() {
            public Object onCallBack(int i, Object obj) {
                Deprecated_JSUtil.execCallback(iWebview, str2, "{\"code\":-100,\"message\":\"\"+webview.getContext().getString(R.string.dcloud_native_obj_load_failed)+\"\"}", JSUtil.ERROR, true, false);
                return null;
            }
        });
    }

    public void destroy() {
        try {
            for (INativeBitmap clear : this.mSnaps.values()) {
                try {
                    clear.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mIds.clear();
            this.mSnaps.clear();
            for (NativeView next : this.mNativeViews.values()) {
                if (next != null) {
                    next.clearNativeViewData();
                }
            }
            this.mNativeViews.clear();
            NativeTypefaceFactory.clearCache();
            System.gc();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void destroyNativeView(NativeView nativeView) {
        NativeView nativeView2 = this.mNativeViews.get(nativeView.mUUID);
        nativeView.clearNativeViewData();
        if (nativeView2 == nativeView) {
            this.mNativeViews.remove(nativeView.mUUID);
        } else {
            this.mNativeViews.remove(nativeView.mID);
        }
    }

    /* JADX WARNING: type inference failed for: r13v6, types: [java.lang.Object, io.dcloud.feature.nativeObj.NativeView] */
    /* JADX WARNING: type inference failed for: r5v7, types: [io.dcloud.feature.nativeObj.NativeView] */
    /* JADX WARNING: type inference failed for: r5v8, types: [io.dcloud.feature.nativeObj.TitleNView] */
    /* JADX WARNING: type inference failed for: r5v9, types: [io.dcloud.feature.nativeObj.NativeImageSlider] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object doForFeature(java.lang.String r12, java.lang.Object r13) {
        /*
            r11 = this;
            java.lang.String r0 = "addNativeView"
            boolean r0 = r0.equals(r12)
            r1 = 0
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x004a
            java.lang.Object[] r13 = (java.lang.Object[]) r13
            r12 = r13[r2]
            io.dcloud.common.DHInterface.IFrameView r12 = (io.dcloud.common.DHInterface.IFrameView) r12
            r13 = r13[r3]
            java.lang.String r13 = (java.lang.String) r13
            io.dcloud.feature.nativeObj.NativeView r13 = r11.getNativeView(r13, r13)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "addNativeView outter"
            r0.append(r2)
            r0.append(r13)
            java.lang.String r0 = r0.toString()
            java.lang.String r2 = "adadad"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r0)
            if (r13 == 0) goto L_0x018f
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "addNativeView inner"
            r0.append(r3)
            r0.append(r12)
            java.lang.String r0 = r0.toString()
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r0)
            r13.attachToViewGroup(r12)
            goto L_0x018f
        L_0x004a:
            java.lang.String r0 = "removeNativeView"
            boolean r0 = r0.equals(r12)
            if (r0 == 0) goto L_0x0063
            java.lang.Object[] r13 = (java.lang.Object[]) r13
            r12 = r13[r3]
            java.lang.String r12 = (java.lang.String) r12
            io.dcloud.feature.nativeObj.NativeView r12 = r11.getNativeView(r12, r12)
            if (r12 == 0) goto L_0x018f
            r12.removeFromViewGroup()
            goto L_0x018f
        L_0x0063:
            java.lang.String r0 = "getNativeView"
            boolean r0 = r0.equals(r12)
            if (r0 == 0) goto L_0x0081
            java.lang.Object[] r13 = (java.lang.Object[]) r13     // Catch:{ Exception -> 0x007b }
            r12 = r13[r2]     // Catch:{ Exception -> 0x007b }
            io.dcloud.common.DHInterface.IFrameView r12 = (io.dcloud.common.DHInterface.IFrameView) r12     // Catch:{ Exception -> 0x007b }
            r12 = r13[r3]     // Catch:{ Exception -> 0x007b }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ Exception -> 0x007b }
            io.dcloud.feature.nativeObj.NativeView r1 = r11.getNativeView(r12, r12)     // Catch:{ Exception -> 0x007b }
            goto L_0x018f
        L_0x007b:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x018f
        L_0x0081:
            java.lang.String r0 = "makeRichText"
            boolean r0 = r0.equals(r12)
            if (r0 == 0) goto L_0x0091
            java.lang.Object[] r13 = (java.lang.Object[]) r13
            android.widget.TextView r12 = io.dcloud.feature.nativeObj.richtext.RichTextLayout.makeRichText(r13)
            goto L_0x0190
        L_0x0091:
            java.lang.String r0 = "View"
            boolean r0 = r0.equals(r12)
            r4 = 2
            if (r0 == 0) goto L_0x011f
            java.lang.Object[] r13 = (java.lang.Object[]) r13     // Catch:{ Exception -> 0x0119 }
            r12 = r13[r2]     // Catch:{ Exception -> 0x0119 }
            io.dcloud.common.DHInterface.IFrameView r12 = (io.dcloud.common.DHInterface.IFrameView) r12     // Catch:{ Exception -> 0x0119 }
            r12 = r13[r3]     // Catch:{ Exception -> 0x0119 }
            io.dcloud.common.DHInterface.IWebview r12 = (io.dcloud.common.DHInterface.IWebview) r12     // Catch:{ Exception -> 0x0119 }
            r0 = r13[r4]     // Catch:{ Exception -> 0x0119 }
            r9 = r0
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Exception -> 0x0119 }
            r0 = 3
            r0 = r13[r0]     // Catch:{ Exception -> 0x0119 }
            r8 = r0
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x0119 }
            r0 = 4
            r0 = r13[r0]     // Catch:{ Exception -> 0x0119 }
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ Exception -> 0x0119 }
            if (r0 != 0) goto L_0x00bb
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x0119 }
            r0.<init>()     // Catch:{ Exception -> 0x0119 }
        L_0x00bb:
            r10 = r0
            int r0 = r13.length     // Catch:{ Exception -> 0x0119 }
            r2 = 5
            if (r0 <= r2) goto L_0x00c5
            r0 = r13[r2]     // Catch:{ Exception -> 0x0119 }
            org.json.JSONArray r0 = (org.json.JSONArray) r0     // Catch:{ Exception -> 0x0119 }
            goto L_0x00c6
        L_0x00c5:
            r0 = r1
        L_0x00c6:
            int r2 = r13.length     // Catch:{ Exception -> 0x0119 }
            r3 = 6
            if (r2 <= r3) goto L_0x00cf
            r13 = r13[r3]     // Catch:{ Exception -> 0x0119 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ Exception -> 0x0119 }
            goto L_0x00d1
        L_0x00cf:
            java.lang.String r13 = "nativeView"
        L_0x00d1:
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r2 = r11.mNativeViews     // Catch:{ Exception -> 0x0119 }
            boolean r2 = r2.containsKey(r8)     // Catch:{ Exception -> 0x0119 }
            if (r2 != 0) goto L_0x018f
            java.lang.String r2 = "ImageSlider"
            boolean r2 = r13.equals(r2)     // Catch:{ Exception -> 0x0119 }
            if (r2 == 0) goto L_0x00ed
            io.dcloud.feature.nativeObj.NativeImageSlider r13 = new io.dcloud.feature.nativeObj.NativeImageSlider     // Catch:{ Exception -> 0x0119 }
            android.content.Context r6 = r12.getContext()     // Catch:{ Exception -> 0x0119 }
            r5 = r13
            r7 = r12
            r5.<init>(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0119 }
            goto L_0x010c
        L_0x00ed:
            java.lang.String r2 = "TitleNView"
            boolean r13 = r2.equals(r13)     // Catch:{ Exception -> 0x0119 }
            if (r13 == 0) goto L_0x0101
            io.dcloud.feature.nativeObj.TitleNView r13 = new io.dcloud.feature.nativeObj.TitleNView     // Catch:{ Exception -> 0x0119 }
            android.content.Context r6 = r12.getContext()     // Catch:{ Exception -> 0x0119 }
            r5 = r13
            r7 = r12
            r5.<init>(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0119 }
            goto L_0x010c
        L_0x0101:
            io.dcloud.feature.nativeObj.NativeView r13 = new io.dcloud.feature.nativeObj.NativeView     // Catch:{ Exception -> 0x0119 }
            android.content.Context r6 = r12.getContext()     // Catch:{ Exception -> 0x0119 }
            r5 = r13
            r7 = r12
            r5.<init>(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0119 }
        L_0x010c:
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r2 = r11.mNativeViews     // Catch:{ Exception -> 0x0119 }
            java.lang.String r3 = r13.mUUID     // Catch:{ Exception -> 0x0119 }
            r2.put(r3, r13)     // Catch:{ Exception -> 0x0119 }
            r11.initViewDrawItme(r12, r13, r0)     // Catch:{ Exception -> 0x0119 }
            r12 = r13
            goto L_0x0190
        L_0x0119:
            r12 = move-exception
            r12.printStackTrace()
            goto L_0x018f
        L_0x011f:
            java.lang.String r0 = "updateSubNViews"
            boolean r12 = r0.equals(r12)
            if (r12 == 0) goto L_0x018f
            java.lang.Object[] r13 = (java.lang.Object[]) r13
            r12 = r13[r3]
            io.dcloud.common.DHInterface.IWebview r12 = (io.dcloud.common.DHInterface.IWebview) r12
            r13 = r13[r4]
            org.json.JSONArray r13 = (org.json.JSONArray) r13
            io.dcloud.common.DHInterface.IFrameView r0 = r12.obtainFrameView()
            io.dcloud.common.adapter.ui.AdaFrameView r0 = (io.dcloud.common.adapter.ui.AdaFrameView) r0
            r4 = 0
        L_0x0138:
            int r5 = r13.length()
            if (r4 >= r5) goto L_0x018f
            org.json.JSONObject r5 = r13.getJSONObject(r4)     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r6 = "id"
            java.lang.String r6 = r5.optString(r6)     // Catch:{ JSONException -> 0x0188 }
            boolean r7 = io.dcloud.common.util.PdrUtil.isEmpty(r6)     // Catch:{ JSONException -> 0x0188 }
            if (r7 != 0) goto L_0x018c
            java.util.ArrayList<io.dcloud.common.DHInterface.INativeView> r7 = r0.mChildNativeViewList     // Catch:{ JSONException -> 0x0188 }
            java.util.Iterator r7 = r7.iterator()     // Catch:{ JSONException -> 0x0188 }
        L_0x0154:
            boolean r8 = r7.hasNext()     // Catch:{ JSONException -> 0x0188 }
            if (r8 == 0) goto L_0x016d
            java.lang.Object r8 = r7.next()     // Catch:{ JSONException -> 0x0188 }
            io.dcloud.common.DHInterface.INativeView r8 = (io.dcloud.common.DHInterface.INativeView) r8     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r9 = r8.getViewId()     // Catch:{ JSONException -> 0x0188 }
            boolean r9 = r6.equals(r9)     // Catch:{ JSONException -> 0x0188 }
            if (r9 == 0) goto L_0x0154
            io.dcloud.feature.nativeObj.NativeView r8 = (io.dcloud.feature.nativeObj.NativeView) r8     // Catch:{ JSONException -> 0x0188 }
            goto L_0x016e
        L_0x016d:
            r8 = r1
        L_0x016e:
            if (r8 == 0) goto L_0x018c
            java.lang.String r6 = "tags"
            org.json.JSONArray r6 = r5.optJSONArray(r6)     // Catch:{ JSONException -> 0x0188 }
            java.lang.String r7 = "styles"
            org.json.JSONObject r5 = r5.optJSONObject(r7)     // Catch:{ JSONException -> 0x0188 }
            if (r6 != 0) goto L_0x0180
            r7 = 1
            goto L_0x0181
        L_0x0180:
            r7 = 0
        L_0x0181:
            r8.setStyle(r5, r7)     // Catch:{ JSONException -> 0x0188 }
            r11.initViewDrawItme(r12, r8, r6)     // Catch:{ JSONException -> 0x0188 }
            goto L_0x018c
        L_0x0188:
            r5 = move-exception
            r5.printStackTrace()
        L_0x018c:
            int r4 = r4 + 1
            goto L_0x0138
        L_0x018f:
            r12 = r1
        L_0x0190:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeBitmapMgr.doForFeature(java.lang.String, java.lang.Object):java.lang.Object");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v6, resolved type: io.dcloud.feature.nativeObj.NativeView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v7, resolved type: io.dcloud.feature.nativeObj.NativeView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v8, resolved type: io.dcloud.common.DHInterface.INativeBitmap} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v9, resolved type: io.dcloud.feature.nativeObj.NativeView} */
    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r0v7, types: [java.lang.Object, io.dcloud.feature.nativeObj.NativeView] */
    /* JADX WARNING: type inference failed for: r8v9, types: [int] */
    /* JADX WARNING: type inference failed for: r1v55, types: [io.dcloud.feature.nativeObj.NativeView] */
    /* JADX WARNING: type inference failed for: r1v56, types: [io.dcloud.feature.nativeObj.NativeImageSlider] */
    /* JADX WARNING: type inference failed for: r8v21 */
    /* JADX WARNING: type inference failed for: r8v22 */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:171|172) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:233|234) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:238|239) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:254|255) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:259|260) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:271|272) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:276|277) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:286|287) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:291|292) */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:330|331) */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:315|316|317|318|319|320|(1:322)) */
    /* JADX WARNING: Code restructure failed: missing block: B:172:?, code lost:
        r0 = io.dcloud.common.util.PdrUtil.stringToColor(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:?, code lost:
        r0 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:239:?, code lost:
        r0 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:?, code lost:
        r0 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:260:?, code lost:
        r0 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:272:?, code lost:
        r0 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:277:?, code lost:
        r0 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:287:?, code lost:
        r2 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:292:?, code lost:
        r2 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:331:?, code lost:
        r2 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:357:?, code lost:
        return r0;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:171:0x0333 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:233:0x046c */
    /* JADX WARNING: Missing exception handler attribute for start block: B:238:0x047b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:254:0x04b0 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:259:0x04bf */
    /* JADX WARNING: Missing exception handler attribute for start block: B:271:0x0500 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:276:0x0510 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:286:0x0543 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:291:0x0553 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:318:0x0612 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:330:0x0639 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0322 A[Catch:{ Exception -> 0x067f }] */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x0329 A[Catch:{ Exception -> 0x067f }] */
    /* JADX WARNING: Removed duplicated region for block: B:342:0x0655 A[Catch:{ Exception -> 0x067f }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:271:0x0500=Splitter:B:271:0x0500, B:286:0x0543=Splitter:B:286:0x0543, B:157:0x030b=Splitter:B:157:0x030b, B:233:0x046c=Splitter:B:233:0x046c, B:168:0x032d=Splitter:B:168:0x032d, B:339:0x064d=Splitter:B:339:0x064d, B:254:0x04b0=Splitter:B:254:0x04b0, B:276:0x0510=Splitter:B:276:0x0510, B:318:0x0612=Splitter:B:318:0x0612, B:291:0x0553=Splitter:B:291:0x0553, B:330:0x0639=Splitter:B:330:0x0639, B:238:0x047b=Splitter:B:238:0x047b, B:259:0x04bf=Splitter:B:259:0x04bf} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r21, java.lang.String r22, java.lang.String[] r23) {
        /*
            r20 = this;
            r7 = r20
            r9 = r21
            r1 = r23
            java.lang.String r0 = "viewId"
            java.lang.String r2 = "color"
            r6 = 0
            io.dcloud.common.DHInterface.IFrameView r3 = r21.obtainFrameView()     // Catch:{ Exception -> 0x0686 }
            io.dcloud.common.DHInterface.IApp r3 = r3.obtainApp()     // Catch:{ Exception -> 0x0686 }
            if (r3 != 0) goto L_0x0017
            return r6
        L_0x0017:
            r3.obtainAppId()     // Catch:{ Exception -> 0x0686 }
            io.dcloud.feature.nativeObj.NativeBitmapMgr$Action r4 = io.dcloud.feature.nativeObj.NativeBitmapMgr.Action.valueOf(r22)     // Catch:{ Exception -> 0x001f }
            goto L_0x0020
        L_0x001f:
            r4 = r6
        L_0x0020:
            int[] r5 = io.dcloud.feature.nativeObj.NativeBitmapMgr.AnonymousClass7.$SwitchMap$io$dcloud$feature$nativeObj$NativeBitmapMgr$Action     // Catch:{ Exception -> 0x0686 }
            int r4 = r4.ordinal()     // Catch:{ Exception -> 0x0686 }
            r4 = r5[r4]     // Catch:{ Exception -> 0x0686 }
            java.lang.String r5 = "false"
            java.lang.String r10 = "ImageSlider"
            java.lang.String r11 = "null"
            r12 = 4
            r13 = 3
            r14 = 2
            r15 = 1
            r8 = 0
            switch(r4) {
                case 1: goto L_0x0622;
                case 2: goto L_0x0604;
                case 3: goto L_0x05ef;
                case 4: goto L_0x05d8;
                case 5: goto L_0x05b9;
                case 6: goto L_0x059d;
                case 7: goto L_0x0563;
                case 8: goto L_0x0526;
                case 9: goto L_0x04dd;
                case 10: goto L_0x0497;
                case 11: goto L_0x0457;
                case 12: goto L_0x043f;
                case 13: goto L_0x0423;
                case 14: goto L_0x0403;
                case 15: goto L_0x03c0;
                case 16: goto L_0x039b;
                case 17: goto L_0x0385;
                case 18: goto L_0x0370;
                case 19: goto L_0x035f;
                case 20: goto L_0x034e;
                case 21: goto L_0x02de;
                case 22: goto L_0x02b2;
                case 23: goto L_0x0284;
                case 24: goto L_0x026f;
                case 25: goto L_0x0257;
                case 26: goto L_0x022e;
                case 27: goto L_0x021d;
                case 28: goto L_0x0208;
                case 29: goto L_0x01f3;
                case 30: goto L_0x01c9;
                case 31: goto L_0x01b2;
                case 32: goto L_0x010a;
                case 33: goto L_0x00f2;
                case 34: goto L_0x00be;
                case 35: goto L_0x00a8;
                case 36: goto L_0x0081;
                case 37: goto L_0x005a;
                case 38: goto L_0x003a;
                default: goto L_0x0036;
            }
        L_0x0036:
            r19 = r6
            goto L_0x0683
        L_0x003a:
            r0 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            r1 = r1[r15]     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x01c6 }
            if (r0 == 0) goto L_0x0036
            java.lang.String r1 = r0.getViewType()     // Catch:{ Exception -> 0x01c6 }
            boolean r1 = r1.equals(r10)     // Catch:{ Exception -> 0x01c6 }
            if (r1 == 0) goto L_0x0036
            io.dcloud.feature.nativeObj.NativeImageSlider r0 = (io.dcloud.feature.nativeObj.NativeImageSlider) r0     // Catch:{ Exception -> 0x01c6 }
            int r0 = r0.getCurrentImageIndex()     // Catch:{ Exception -> 0x01c6 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x027d
        L_0x005a:
            r0 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            r2 = r1[r15]     // Catch:{ Exception -> 0x01c6 }
            r1 = r1[r14]     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x01c6 }
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)     // Catch:{ Exception -> 0x01c6 }
            if (r2 != 0) goto L_0x0036
            if (r0 == 0) goto L_0x0036
            java.lang.String r2 = r0.getViewType()     // Catch:{ Exception -> 0x01c6 }
            boolean r2 = r2.equals(r10)     // Catch:{ Exception -> 0x01c6 }
            if (r2 == 0) goto L_0x0036
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ Exception -> 0x01c6 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeImageSlider r0 = (io.dcloud.feature.nativeObj.NativeImageSlider) r0     // Catch:{ Exception -> 0x01c6 }
            r0.addImages(r9, r2)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x0036
        L_0x0081:
            r0 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            r2 = r1[r15]     // Catch:{ Exception -> 0x01c6 }
            r1 = r1[r14]     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x01c6 }
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)     // Catch:{ Exception -> 0x01c6 }
            if (r2 != 0) goto L_0x0036
            if (r0 == 0) goto L_0x0036
            java.lang.String r2 = r0.getViewType()     // Catch:{ Exception -> 0x01c6 }
            boolean r2 = r2.equals(r10)     // Catch:{ Exception -> 0x01c6 }
            if (r2 == 0) goto L_0x0036
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ Exception -> 0x01c6 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeImageSlider r0 = (io.dcloud.feature.nativeObj.NativeImageSlider) r0     // Catch:{ Exception -> 0x01c6 }
            r0.setImages(r9, r2)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x0036
        L_0x00a8:
            r0 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            r2 = r1[r15]     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x01c6 }
            if (r0 == 0) goto L_0x0036
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ JSONException -> 0x0036 }
            r1 = r1[r14]     // Catch:{ JSONException -> 0x0036 }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x0036 }
            r7.initViewDrawItme(r9, r0, r2)     // Catch:{ JSONException -> 0x0036 }
            goto L_0x0036
        L_0x00be:
            r0 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            r2 = r1[r15]     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeView r8 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x01c6 }
            if (r8 == 0) goto L_0x0036
            r0 = r1[r14]     // Catch:{ Exception -> 0x01c6 }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x01c6 }
            if (r2 != 0) goto L_0x00dd
            boolean r2 = r0.equals(r11)     // Catch:{ Exception -> 0x01c6 }
            if (r2 != 0) goto L_0x00dd
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x01c6 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x01c6 }
            r14 = r2
            goto L_0x00de
        L_0x00dd:
            r14 = r6
        L_0x00de:
            r16 = r1[r13]     // Catch:{ Exception -> 0x01c6 }
            r10 = 0
            r11 = 0
            r12 = -1
            r13 = 0
            r15 = 0
            java.lang.String r17 = "clear"
            r18 = 0
            r19 = 1
            r9 = r21
            r8.makeOverlay(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x0036
        L_0x00f2:
            android.app.Activity r0 = r3.getActivity()     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.common.adapter.ui.FrameSwitchView r0 = io.dcloud.common.adapter.ui.FrameSwitchView.getInstance(r0)     // Catch:{ Exception -> 0x01c6 }
            boolean r2 = r0.isInit()     // Catch:{ Exception -> 0x01c6 }
            if (r2 != 0) goto L_0x0103
            r0.initView()     // Catch:{ Exception -> 0x01c6 }
        L_0x0103:
            r1 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            r0.clearSwitchAnimation(r1)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x0036
        L_0x010a:
            r10 = r1[r8]     // Catch:{  }
            r2 = r1[r15]     // Catch:{  }
            int r4 = r1.length     // Catch:{  }
            if (r4 <= r14) goto L_0x0114
            r4 = r1[r14]     // Catch:{  }
            goto L_0x0115
        L_0x0114:
            r4 = r6
        L_0x0115:
            int r5 = r1.length     // Catch:{  }
            if (r5 <= r13) goto L_0x011b
            r1 = r1[r13]     // Catch:{  }
            goto L_0x011c
        L_0x011b:
            r1 = r6
        L_0x011c:
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{  }
            r5.<init>(r2)     // Catch:{  }
            java.lang.String r2 = r5.optString(r0, r6)     // Catch:{  }
            boolean r8 = android.text.TextUtils.isEmpty(r2)     // Catch:{  }
            java.lang.String r12 = "uuid"
            java.lang.String r13 = "texts"
            if (r8 != 0) goto L_0x014c
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r5 = r7.mNativeViews     // Catch:{  }
            boolean r5 = r5.containsKey(r2)     // Catch:{  }
            if (r5 == 0) goto L_0x0149
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r5 = r7.mNativeViews     // Catch:{  }
            java.lang.Object r5 = r5.get(r2)     // Catch:{  }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r8 = r7.mNativeViews     // Catch:{  }
            java.lang.Object r2 = r8.get(r2)     // Catch:{  }
            io.dcloud.feature.nativeObj.NativeView r2 = (io.dcloud.feature.nativeObj.NativeView) r2     // Catch:{  }
            r2.mShow = r15     // Catch:{  }
            r2 = r6
            goto L_0x0158
        L_0x0149:
            r2 = r6
            r5 = r2
            goto L_0x0158
        L_0x014c:
            java.lang.String r2 = r5.optString(r13, r6)     // Catch:{  }
            java.lang.String r5 = r5.optString(r12, r6)     // Catch:{  }
            io.dcloud.common.DHInterface.INativeBitmap r5 = r7.getBitmapByUuid(r5)     // Catch:{  }
        L_0x0158:
            boolean r8 = android.text.TextUtils.isEmpty(r4)     // Catch:{  }
            if (r8 != 0) goto L_0x0195
            boolean r8 = r4.equals(r11)     // Catch:{  }
            if (r8 != 0) goto L_0x0195
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{  }
            r8.<init>(r4)     // Catch:{  }
            java.lang.String r0 = r8.optString(r0, r6)     // Catch:{  }
            boolean r4 = android.text.TextUtils.isEmpty(r0)     // Catch:{  }
            if (r4 != 0) goto L_0x0186
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r4 = r7.mNativeViews     // Catch:{  }
            java.lang.Object r4 = r4.get(r0)     // Catch:{  }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r8 = r7.mNativeViews     // Catch:{  }
            java.lang.Object r0 = r8.get(r0)     // Catch:{  }
            io.dcloud.feature.nativeObj.NativeView r0 = (io.dcloud.feature.nativeObj.NativeView) r0     // Catch:{  }
            r0.mShow = r15     // Catch:{  }
            r13 = r4
            r14 = r6
            goto L_0x0197
        L_0x0186:
            java.lang.String r0 = r8.optString(r13, r6)     // Catch:{  }
            java.lang.String r4 = r8.optString(r12, r6)     // Catch:{  }
            io.dcloud.common.DHInterface.INativeBitmap r4 = r7.getBitmapByUuid(r4)     // Catch:{  }
            r14 = r0
            r13 = r4
            goto L_0x0197
        L_0x0195:
            r13 = r6
            r14 = r13
        L_0x0197:
            android.app.Activity r0 = r3.getActivity()     // Catch:{  }
            io.dcloud.common.adapter.ui.FrameSwitchView r8 = io.dcloud.common.adapter.ui.FrameSwitchView.getInstance(r0)     // Catch:{  }
            boolean r0 = r8.isInit()     // Catch:{  }
            if (r0 != 0) goto L_0x01a8
            r8.initView()     // Catch:{  }
        L_0x01a8:
            r9 = r21
            r11 = r5
            r12 = r2
            r15 = r1
            r8.startAnimation(r9, r10, r11, r12, r13, r14, r15)     // Catch:{  }
            goto L_0x0036
        L_0x01b2:
            r0 = r1[r8]     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapByUuid(r0)     // Catch:{ Exception -> 0x01c6 }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x01c6 }
            if (r0 == 0) goto L_0x01c0
            java.lang.String r6 = r0.toBase64Data()     // Catch:{ Exception -> 0x01c6 }
        L_0x01c0:
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r6, r15)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x027d
        L_0x01c6:
            r0 = move-exception
            goto L_0x0689
        L_0x01c9:
            r0 = r1[r8]     // Catch:{ Exception -> 0x0686 }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapByUuid(r0)     // Catch:{ Exception -> 0x0686 }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x0686 }
            r2 = r1[r15]     // Catch:{ Exception -> 0x0686 }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x0686 }
            r4 = r1[r14]     // Catch:{ Exception -> 0x0686 }
            r5.<init>(r4)     // Catch:{ Exception -> 0x0686 }
            r8 = r1[r13]     // Catch:{ Exception -> 0x0686 }
            java.lang.String r1 = r21.obtainFullUrl()     // Catch:{ Exception -> 0x0686 }
            java.lang.String r4 = r3.convert2AbsFullPath(r1, r2)     // Catch:{ Exception -> 0x0686 }
            if (r0 == 0) goto L_0x0036
            r1 = r20
            r2 = r21
            r3 = r0
            r19 = r6
            r6 = r8
            r1.save(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x01f3:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapByUuid(r0)     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r14]     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r7.loadBase64Data(r9, r0, r2, r1)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0208:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapByUuid(r0)     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r14]     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r7.load(r9, r0, r2, r1)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x021d:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapByUuid(r0)     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r0.recycle()     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x022e:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapByUuid(r0)     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x067f }
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r7.mIds     // Catch:{ Exception -> 0x067f }
            java.lang.String r2 = r0.getId()     // Catch:{ Exception -> 0x067f }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ Exception -> 0x067f }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Exception -> 0x067f }
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.INativeBitmap> r2 = r7.mSnaps     // Catch:{ Exception -> 0x067f }
            r2.remove(r1)     // Catch:{ Exception -> 0x067f }
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r7.mIds     // Catch:{ Exception -> 0x067f }
            java.lang.String r2 = r0.getId()     // Catch:{ Exception -> 0x067f }
            r1.remove(r2)     // Catch:{ Exception -> 0x067f }
            r0.clear()     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0257:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.INativeBitmap r0 = r7.getBitmapById(r0)     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeBitmap r0 = (io.dcloud.feature.nativeObj.NativeBitmap) r0     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0268
            java.lang.String r6 = r0.toJsString()     // Catch:{ Exception -> 0x067f }
            goto L_0x026a
        L_0x0268:
            r6 = r19
        L_0x026a:
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r6, r8)     // Catch:{ Exception -> 0x01c6 }
            goto L_0x027d
        L_0x026f:
            r19 = r6
            org.json.JSONArray r0 = r20.getItems()     // Catch:{ Exception -> 0x067f }
            java.lang.String r1 = r0.toString()     // Catch:{ Exception -> 0x067f }
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r1, r8)     // Catch:{ Exception -> 0x0280 }
        L_0x027d:
            r6 = r0
            goto L_0x0695
        L_0x0280:
            r0 = move-exception
            r6 = r1
            goto L_0x0689
        L_0x0284:
            r19 = r6
            int r0 = r1.length     // Catch:{ Exception -> 0x067f }
            if (r0 <= r14) goto L_0x02a7
            r6 = r1[r14]     // Catch:{ Exception -> 0x067f }
            boolean r0 = io.dcloud.common.util.PdrUtil.isNetPath(r6)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0292
            goto L_0x02a9
        L_0x0292:
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x067f }
            if (r0 != 0) goto L_0x02a7
            boolean r0 = r6.equals(r11)     // Catch:{ Exception -> 0x067f }
            if (r0 != 0) goto L_0x02a7
            java.lang.String r0 = r21.obtainFullUrl()     // Catch:{ Exception -> 0x067f }
            java.lang.String r6 = r3.convert2AbsFullPath(r0, r6)     // Catch:{ Exception -> 0x067f }
            goto L_0x02a9
        L_0x02a7:
            r6 = r19
        L_0x02a9:
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            r7.createBitmap(r3, r0, r1, r6)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x02b2:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x02d5
            android.view.ViewParent r1 = r0.getParent()     // Catch:{ Exception -> 0x067f }
            if (r1 == 0) goto L_0x02d5
            int r0 = r0.getVisibility()     // Catch:{ Exception -> 0x067f }
            if (r0 != 0) goto L_0x02cb
            goto L_0x02cc
        L_0x02cb:
            r15 = 0
        L_0x02cc:
            java.lang.String r0 = java.lang.String.valueOf(r15)     // Catch:{ Exception -> 0x067f }
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r8)     // Catch:{ Exception -> 0x067f }
            goto L_0x027d
        L_0x02d5:
            java.lang.String r0 = java.lang.String.valueOf(r8)     // Catch:{ Exception -> 0x067f }
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r8)     // Catch:{ Exception -> 0x067f }
            goto L_0x027d
        L_0x02de:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r3 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r8 = r7.getNativeView(r0, r3)     // Catch:{ Exception -> 0x067f }
            if (r8 == 0) goto L_0x0683
            r0 = r1[r14]     // Catch:{ Exception -> 0x067f }
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)     // Catch:{ Exception -> 0x067f }
            java.lang.String r3 = "#FFFFFF"
            if (r0 != 0) goto L_0x0312
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0308 }
            r0 = r1[r14]     // Catch:{ JSONException -> 0x0308 }
            r6.<init>(r0)     // Catch:{ JSONException -> 0x0308 }
            boolean r0 = r6.has(r2)     // Catch:{ JSONException -> 0x0306 }
            if (r0 == 0) goto L_0x0314
            java.lang.String r0 = r6.optString(r2)     // Catch:{ JSONException -> 0x0306 }
            goto L_0x0310
        L_0x0306:
            r0 = move-exception
            goto L_0x030b
        L_0x0308:
            r0 = move-exception
            r6 = r19
        L_0x030b:
            r0.printStackTrace()     // Catch:{ Exception -> 0x067f }
            r0 = r1[r14]     // Catch:{ Exception -> 0x067f }
        L_0x0310:
            r3 = r0
            goto L_0x0314
        L_0x0312:
            r6 = r19
        L_0x0314:
            r0 = r1[r13]     // Catch:{ Exception -> 0x067f }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x067f }
            if (r2 != 0) goto L_0x0329
            boolean r2 = r0.equals(r11)     // Catch:{ Exception -> 0x067f }
            if (r2 != 0) goto L_0x0329
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r2.<init>(r0)     // Catch:{ Exception -> 0x067f }
            r14 = r2
            goto L_0x032b
        L_0x0329:
            r14 = r19
        L_0x032b:
            r16 = r1[r12]     // Catch:{ Exception -> 0x067f }
            int r0 = android.graphics.Color.parseColor(r3)     // Catch:{ Exception -> 0x0333 }
        L_0x0331:
            r12 = r0
            goto L_0x0338
        L_0x0333:
            int r0 = io.dcloud.common.util.PdrUtil.stringToColor(r3)     // Catch:{ Exception -> 0x067f }
            goto L_0x0331
        L_0x0338:
            r2 = 5
            r0 = r1[r2]     // Catch:{ Exception -> 0x067f }
            boolean r0 = r5.equals(r0)     // Catch:{ Exception -> 0x067f }
            r18 = r0 ^ 1
            r10 = 0
            r11 = 0
            r13 = 0
            java.lang.String r17 = "rect"
            r9 = r21
            r15 = r6
            r8.makeOverlay(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x034e:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r0.clearAnimate()     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x035f:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r0.resetNativeView()     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0370:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r2 = r1[r14]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r13]     // Catch:{ Exception -> 0x067f }
            r0.StartAnimate(r9, r2, r1)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0385:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r2 = r7.mNativeViews     // Catch:{ Exception -> 0x067f }
            r2.remove(r1)     // Catch:{ Exception -> 0x067f }
            r0.removeFromViewGroup()     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x039b:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r0.setVisibility(r12)     // Catch:{ Exception -> 0x067f }
            android.view.ViewParent r0 = r0.getParent()     // Catch:{ Exception -> 0x067f }
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            boolean r1 = r0 instanceof io.dcloud.common.adapter.ui.DHImageView     // Catch:{ Exception -> 0x067f }
            if (r1 == 0) goto L_0x0683
            io.dcloud.common.adapter.ui.DHImageView r0 = (io.dcloud.common.adapter.ui.DHImageView) r0     // Catch:{ Exception -> 0x067f }
            r0.clear()     // Catch:{ Exception -> 0x067f }
            r0.setVisibility(r12)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x03c0:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r1)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r0.setVisibility(r8)     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.IApp r1 = r21.obtainApp()     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.IWebAppRootView r1 = r1.obtainWebAppRootView()     // Catch:{ Exception -> 0x067f }
            android.view.View r1 = r1.obtainMainView()     // Catch:{ Exception -> 0x067f }
            android.view.ViewParent r1 = r1.getParent()     // Catch:{ Exception -> 0x067f }
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1     // Catch:{ Exception -> 0x067f }
            android.view.ViewParent r2 = r0.getParent()     // Catch:{ Exception -> 0x067f }
            if (r2 == 0) goto L_0x03e9
            goto L_0x0683
        L_0x03e9:
            r0.mShow = r15     // Catch:{ Exception -> 0x067f }
            int r2 = r1.getChildCount()     // Catch:{ Exception -> 0x067f }
        L_0x03ef:
            if (r8 >= r2) goto L_0x03fe
            android.view.View r3 = r1.getChildAt(r8)     // Catch:{ Exception -> 0x067f }
            boolean r3 = r3 instanceof io.dcloud.feature.internal.splash.ISplash     // Catch:{ Exception -> 0x067f }
            if (r3 == 0) goto L_0x03fb
            r2 = r8
            goto L_0x03fe
        L_0x03fb:
            int r8 = r8 + 1
            goto L_0x03ef
        L_0x03fe:
            r1.addView(r0, r2)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0403:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            r2 = r1[r14]     // Catch:{ Exception -> 0x0683 }
            int r3 = r1.length     // Catch:{ Exception -> 0x0683 }
            if (r12 > r3) goto L_0x041c
            r1 = r1[r13]     // Catch:{ Exception -> 0x0683 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0683 }
            boolean r8 = r1.booleanValue()     // Catch:{ Exception -> 0x0683 }
        L_0x041c:
            if (r0 == 0) goto L_0x0683
            r0.setInputFocusById(r2, r8)     // Catch:{ Exception -> 0x0683 }
            goto L_0x0683
        L_0x0423:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            r1 = r1[r14]     // Catch:{ Exception -> 0x0683 }
            if (r0 == 0) goto L_0x0683
            boolean r0 = r0.getInputFocusById(r1)     // Catch:{ Exception -> 0x0683 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0683 }
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r8)     // Catch:{ Exception -> 0x0683 }
            goto L_0x027d
        L_0x043f:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            r1 = r1[r14]     // Catch:{ Exception -> 0x0683 }
            if (r0 == 0) goto L_0x0683
            java.lang.String r0 = r0.getInputValueById(r1)     // Catch:{ Exception -> 0x0683 }
            java.lang.String r0 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r0, r15)     // Catch:{ Exception -> 0x0683 }
            goto L_0x027d
        L_0x0457:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r8 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r8 == 0) goto L_0x0683
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x046c }
            r2 = r1[r14]     // Catch:{ JSONException -> 0x046c }
            r0.<init>(r2)     // Catch:{ JSONException -> 0x046c }
        L_0x046a:
            r14 = r0
            goto L_0x0472
        L_0x046c:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r0.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x046a
        L_0x0472:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x047b }
            r2 = r1[r13]     // Catch:{ JSONException -> 0x047b }
            r0.<init>(r2)     // Catch:{ JSONException -> 0x047b }
        L_0x0479:
            r15 = r0
            goto L_0x0481
        L_0x047b:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r0.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x0479
        L_0x0481:
            r0 = r1[r12]     // Catch:{ Exception -> 0x0486 }
            r16 = r0
            goto L_0x0488
        L_0x0486:
            r16 = r19
        L_0x0488:
            r10 = 0
            r11 = 0
            r12 = -1
            r13 = 0
            java.lang.String r17 = "input"
            r18 = 1
            r9 = r21
            r8.makeOverlay(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0497:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r8 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            r11 = r1[r14]     // Catch:{ Exception -> 0x067f }
            if (r8 == 0) goto L_0x0683
            if (r11 == 0) goto L_0x0683
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x04b0 }
            r2 = r1[r13]     // Catch:{ JSONException -> 0x04b0 }
            r0.<init>(r2)     // Catch:{ JSONException -> 0x04b0 }
        L_0x04ae:
            r14 = r0
            goto L_0x04b6
        L_0x04b0:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r0.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x04ae
        L_0x04b6:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x04bf }
            r2 = r1[r12]     // Catch:{ JSONException -> 0x04bf }
            r0.<init>(r2)     // Catch:{ JSONException -> 0x04bf }
        L_0x04bd:
            r2 = 5
            goto L_0x04c5
        L_0x04bf:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r0.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x04bd
        L_0x04c5:
            r16 = r1[r2]     // Catch:{ Exception -> 0x067f }
            r2 = 6
            r1 = r1[r2]     // Catch:{ Exception -> 0x067f }
            boolean r1 = r5.equals(r1)     // Catch:{ Exception -> 0x067f }
            r18 = r1 ^ 1
            r10 = 0
            r12 = -1
            r13 = 0
            java.lang.String r17 = "font"
            r9 = r21
            r15 = r0
            r8.makeOverlay(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x04dd:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r8 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r8 == 0) goto L_0x0683
            r0 = r1[r14]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeBitmap r10 = r7.getSrcNativeBitmap(r9, r3, r0)     // Catch:{ Exception -> 0x067f }
            if (r10 == 0) goto L_0x0683
            android.graphics.Bitmap r0 = r10.getBitmap()     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0500 }
            r2 = r1[r13]     // Catch:{ JSONException -> 0x0500 }
            r0.<init>(r2)     // Catch:{ JSONException -> 0x0500 }
        L_0x04fe:
            r13 = r0
            goto L_0x0506
        L_0x0500:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r0.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x04fe
        L_0x0506:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0510 }
            r2 = r1[r12]     // Catch:{ JSONException -> 0x0510 }
            r0.<init>(r2)     // Catch:{ JSONException -> 0x0510 }
        L_0x050d:
            r14 = r0
            r2 = 5
            goto L_0x0516
        L_0x0510:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r0.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x050d
        L_0x0516:
            r16 = r1[r2]     // Catch:{ Exception -> 0x067f }
            r11 = 0
            r12 = -1
            r15 = 0
            java.lang.String r17 = "img"
            r18 = 1
            r9 = r21
            r8.makeOverlay(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0526:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            r3 = r1[r14]     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            boolean r2 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x067f }
            if (r2 != 0) goto L_0x0683
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0543 }
            r4 = r1[r13]     // Catch:{ JSONException -> 0x0543 }
            r2.<init>(r4)     // Catch:{ JSONException -> 0x0543 }
        L_0x0541:
            r4 = r2
            goto L_0x0549
        L_0x0543:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r2.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x0541
        L_0x0549:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0553 }
            r5 = r1[r12]     // Catch:{ JSONException -> 0x0553 }
            r2.<init>(r5)     // Catch:{ JSONException -> 0x0553 }
        L_0x0550:
            r5 = r2
            r2 = 5
            goto L_0x0559
        L_0x0553:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r2.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x0550
        L_0x0559:
            r6 = r1[r2]     // Catch:{ Exception -> 0x067f }
            r1 = r0
            r2 = r21
            r1.makeRichText(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0563:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r0 = r1[r15]     // Catch:{ Exception -> 0x067f }
            r0 = r1[r14]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r13]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r12]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.IFrameView r3 = r21.obtainFrameView()     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.obtainWindowMgr()     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x067f }
            r5 = 10
            java.lang.Object[] r6 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x067f }
            io.dcloud.common.DHInterface.IApp r10 = r21.obtainApp()     // Catch:{ Exception -> 0x067f }
            r6[r8] = r10     // Catch:{ Exception -> 0x067f }
            java.lang.String r10 = "weex,io.dcloud.feature.weex.WeexFeature"
            r6[r15] = r10     // Catch:{ Exception -> 0x067f }
            java.lang.String r10 = "evalWeexJS"
            r6[r14] = r10     // Catch:{ Exception -> 0x067f }
            java.lang.Object[] r10 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x067f }
            r10[r8] = r9     // Catch:{ Exception -> 0x067f }
            r10[r15] = r0     // Catch:{ Exception -> 0x067f }
            r10[r14] = r2     // Catch:{ Exception -> 0x067f }
            r10[r13] = r1     // Catch:{ Exception -> 0x067f }
            r6[r13] = r10     // Catch:{ Exception -> 0x067f }
            r3.processEvent(r4, r5, r6)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x059d:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            int r2 = r1.length     // Catch:{ Exception -> 0x067f }
            if (r2 <= r15) goto L_0x05a7
            r6 = r1[r15]     // Catch:{ Exception -> 0x067f }
            goto L_0x05a9
        L_0x05a7:
            r6 = r19
        L_0x05a9:
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r6)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            org.json.JSONObject r0 = r0.toJSON()     // Catch:{ Exception -> 0x067f }
            java.lang.String r0 = io.dcloud.common.util.JSUtil.wrapJsVar((org.json.JSONObject) r0)     // Catch:{ Exception -> 0x067f }
            goto L_0x027d
        L_0x05b9:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r1 = r1[r14]     // Catch:{ Exception -> 0x067f }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x067f }
            if (r2 != 0) goto L_0x0683
            boolean r2 = r1.equals(r11)     // Catch:{ Exception -> 0x067f }
            if (r2 != 0) goto L_0x0683
            r0.setTouchEventRect(r1)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x05d8:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r1 = r1[r14]     // Catch:{ Exception -> 0x067f }
            boolean r1 = io.dcloud.common.util.PdrUtil.parseBoolean(r1, r15, r8)     // Catch:{ Exception -> 0x067f }
            r0.interceptTouchEvent(r1)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x05ef:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r2 = r1[r14]     // Catch:{ Exception -> 0x067f }
            r1 = r1[r13]     // Catch:{ Exception -> 0x067f }
            r0.addEventListener(r2, r9, r1)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0604:
            r19 = r6
            r0 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r2 = r1[r15]     // Catch:{ Exception -> 0x067f }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0612 }
            r1 = r1[r14]     // Catch:{ JSONException -> 0x0612 }
            r3.<init>(r1)     // Catch:{ JSONException -> 0x0612 }
            goto L_0x0617
        L_0x0612:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r3.<init>()     // Catch:{ Exception -> 0x067f }
        L_0x0617:
            io.dcloud.feature.nativeObj.NativeView r0 = r7.getNativeView(r0, r2)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0683
            r0.setStyle(r3, r15)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x0622:
            r19 = r6
            r5 = r1[r8]     // Catch:{ Exception -> 0x067f }
            r4 = r1[r15]     // Catch:{ Exception -> 0x067f }
            int r0 = r1.length     // Catch:{ Exception -> 0x067f }
            if (r0 <= r12) goto L_0x062e
            r0 = r1[r12]     // Catch:{ Exception -> 0x067f }
            goto L_0x0630
        L_0x062e:
            java.lang.String r0 = "nativeView"
        L_0x0630:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0639 }
            r3 = r1[r14]     // Catch:{ JSONException -> 0x0639 }
            r2.<init>(r3)     // Catch:{ JSONException -> 0x0639 }
        L_0x0637:
            r6 = r2
            goto L_0x063f
        L_0x0639:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x067f }
            r2.<init>()     // Catch:{ Exception -> 0x067f }
            goto L_0x0637
        L_0x063f:
            int r2 = r1.length     // Catch:{ Exception -> 0x067f }
            if (r2 <= r13) goto L_0x064b
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ JSONException -> 0x064b }
            r1 = r1[r13]     // Catch:{ JSONException -> 0x064b }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x064b }
            r8 = r2
            goto L_0x064d
        L_0x064b:
            r8 = r19
        L_0x064d:
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r1 = r7.mNativeViews     // Catch:{ Exception -> 0x067f }
            boolean r1 = r1.containsKey(r4)     // Catch:{ Exception -> 0x067f }
            if (r1 != 0) goto L_0x0683
            boolean r0 = r0.equals(r10)     // Catch:{ Exception -> 0x067f }
            if (r0 == 0) goto L_0x0668
            io.dcloud.feature.nativeObj.NativeImageSlider r0 = new io.dcloud.feature.nativeObj.NativeImageSlider     // Catch:{ Exception -> 0x067f }
            android.content.Context r2 = r21.getContext()     // Catch:{ Exception -> 0x067f }
            r1 = r0
            r3 = r21
            r1.<init>(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x067f }
            goto L_0x0674
        L_0x0668:
            io.dcloud.feature.nativeObj.NativeView r0 = new io.dcloud.feature.nativeObj.NativeView     // Catch:{ Exception -> 0x067f }
            android.content.Context r2 = r21.getContext()     // Catch:{ Exception -> 0x067f }
            r1 = r0
            r3 = r21
            r1.<init>(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x067f }
        L_0x0674:
            r7.initViewDrawItme(r9, r0, r8)     // Catch:{ Exception -> 0x067f }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.nativeObj.NativeView> r1 = r7.mNativeViews     // Catch:{ Exception -> 0x067f }
            java.lang.String r2 = r0.mUUID     // Catch:{ Exception -> 0x067f }
            r1.put(r2, r0)     // Catch:{ Exception -> 0x067f }
            goto L_0x0683
        L_0x067f:
            r0 = move-exception
            r6 = r19
            goto L_0x0689
        L_0x0683:
            r6 = r19
            goto L_0x0695
        L_0x0686:
            r0 = move-exception
            r19 = r6
        L_0x0689:
            java.lang.String r1 = r0.toString()
            java.lang.String r2 = "NativeBitmapMgr"
            io.dcloud.common.adapter.util.Logger.e(r2, r1)
            r0.printStackTrace()
        L_0x0695:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeBitmapMgr.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public INativeBitmap getBitmapById(String str) {
        return getBitmapByUuid(this.mIds.get(str));
    }

    public INativeBitmap getBitmapByUuid(String str) {
        return this.mSnaps.get(str);
    }

    public JSONArray getItems() {
        JSONArray jSONArray = new JSONArray();
        for (Map.Entry value : this.mSnaps.entrySet()) {
            try {
                jSONArray.put(new JSONObject(((NativeBitmap) value.getValue()).toJsString()));
            } catch (Exception unused) {
            }
        }
        return jSONArray;
    }

    public NativeBitmap getSrcNativeBitmap(IWebview iWebview, IApp iApp, String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException unused) {
            jSONObject = null;
        }
        if (jSONObject != null) {
            return (NativeBitmap) getBitmapById(jSONObject.optString("id"));
        }
        if (!PdrUtil.isNetPath(str)) {
            str = (TextUtils.isEmpty(str) || str.equals("null")) ? null : iApp.convert2AbsFullPath(iWebview.obtainFullUrl(), str);
        }
        if (PdrUtil.isEmpty(str)) {
            return null;
        }
        String str2 = iApp.obtainAppId() + str.hashCode();
        return new NativeBitmap(iApp, str2, str2, str);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:72|73) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r16 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r8 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
        r1 = io.dcloud.common.util.PdrUtil.stringToColor(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:?, code lost:
        r8 = new org.json.JSONObject();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0058 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x006a */
    /* JADX WARNING: Missing exception handler attribute for start block: B:36:0x00bb */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x00ce */
    /* JADX WARNING: Missing exception handler attribute for start block: B:72:0x0136 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:92:0x01a3 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:98:0x01b6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initViewDrawItme(io.dcloud.common.DHInterface.IWebview r23, io.dcloud.feature.nativeObj.NativeView r24, org.json.JSONArray r25) {
        /*
            r22 = this;
            r0 = r23
            r13 = r24
            r14 = r25
            java.lang.String r15 = "richTextStyles"
            java.lang.String r12 = "rectStyles"
            java.lang.String r11 = "id"
            java.lang.String r10 = "color"
            if (r14 == 0) goto L_0x01f0
            if (r13 == 0) goto L_0x01f0
            r1 = 0
            r9 = 0
        L_0x0014:
            int r1 = r25.length()     // Catch:{ Exception -> 0x01ec }
            if (r9 >= r1) goto L_0x01e7
            org.json.JSONObject r1 = r14.getJSONObject(r9)     // Catch:{ Exception -> 0x01ec }
            boolean r2 = r1.has(r11)     // Catch:{ Exception -> 0x01ec }
            r3 = 0
            if (r2 == 0) goto L_0x002b
            java.lang.String r2 = r1.optString(r11)     // Catch:{ Exception -> 0x01ec }
            r8 = r2
            goto L_0x002c
        L_0x002b:
            r8 = r3
        L_0x002c:
            java.lang.String r2 = "tag"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Exception -> 0x01ec }
            java.lang.String r4 = "img"
            boolean r4 = r2.equals(r4)     // Catch:{ Exception -> 0x01ec }
            java.lang.String r5 = "position"
            if (r4 == 0) goto L_0x0098
            java.lang.String r2 = "src"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Exception -> 0x01ec }
            io.dcloud.common.DHInterface.IApp r3 = r23.obtainApp()     // Catch:{ Exception -> 0x01ec }
            r7 = r22
            io.dcloud.feature.nativeObj.NativeBitmap r3 = r7.getSrcNativeBitmap(r0, r3, r2)     // Catch:{ Exception -> 0x01ec }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0058 }
            java.lang.String r4 = "sprite"
            java.lang.String r4 = r1.optString(r4)     // Catch:{ JSONException -> 0x0058 }
            r2.<init>(r4)     // Catch:{ JSONException -> 0x0058 }
            goto L_0x005d
        L_0x0058:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r2.<init>()     // Catch:{ Exception -> 0x01ec }
        L_0x005d:
            r6 = r2
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x006a }
            java.lang.String r1 = r1.optString(r5)     // Catch:{ JSONException -> 0x006a }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x006a }
            r16 = r2
            goto L_0x0071
        L_0x006a:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r1.<init>()     // Catch:{ Exception -> 0x01ec }
            r16 = r1
        L_0x0071:
            r4 = 0
            r5 = -1
            r17 = 0
            java.lang.String r18 = "img"
            r19 = 0
            r20 = 1
            r1 = r24
            r2 = r23
            r7 = r16
            r16 = r8
            r8 = r17
            r17 = r9
            r9 = r16
            r21 = r10
            r10 = r18
            r18 = r11
            r11 = r19
            r14 = r12
            r12 = r20
            r1.makeOverlay(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x01ec }
            goto L_0x00e4
        L_0x0098:
            r16 = r8
            r17 = r9
            r21 = r10
            r18 = r11
            r14 = r12
            java.lang.String r4 = "font"
            boolean r4 = r2.equals(r4)     // Catch:{ Exception -> 0x01ec }
            java.lang.String r6 = "text"
            if (r4 == 0) goto L_0x00e8
            java.lang.String r4 = r1.optString(r6)     // Catch:{ Exception -> 0x01ec }
            if (r4 == 0) goto L_0x00e4
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00bb }
            java.lang.String r3 = r1.optString(r5)     // Catch:{ JSONException -> 0x00bb }
            r2.<init>(r3)     // Catch:{ JSONException -> 0x00bb }
            goto L_0x00c0
        L_0x00bb:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r2.<init>()     // Catch:{ Exception -> 0x01ec }
        L_0x00c0:
            r7 = r2
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00ce }
            java.lang.String r3 = "textStyles"
            java.lang.String r1 = r1.optString(r3)     // Catch:{ JSONException -> 0x00ce }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x00ce }
            r8 = r2
            goto L_0x00d4
        L_0x00ce:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r1.<init>()     // Catch:{ Exception -> 0x01ec }
            r8 = r1
        L_0x00d4:
            r3 = 0
            r5 = -1
            r6 = 0
            java.lang.String r10 = "font"
            r11 = 0
            r12 = 1
            r1 = r24
            r2 = r23
            r9 = r16
            r1.makeOverlay(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x01ec }
        L_0x00e4:
            r20 = r21
            goto L_0x01dc
        L_0x00e8:
            java.lang.String r4 = "rect"
            boolean r4 = r2.equals(r4)     // Catch:{ Exception -> 0x01ec }
            java.lang.String r7 = "null"
            if (r4 == 0) goto L_0x0152
            r12 = r21
            boolean r2 = r1.has(r12)     // Catch:{ Exception -> 0x01ec }
            if (r2 == 0) goto L_0x00ff
            java.lang.String r2 = r1.optString(r12)     // Catch:{ Exception -> 0x01ec }
            goto L_0x0101
        L_0x00ff:
            java.lang.String r2 = "#FFFFFF"
        L_0x0101:
            java.lang.String r4 = r1.optString(r5)     // Catch:{ Exception -> 0x01ec }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x01ec }
            if (r5 != 0) goto L_0x0118
            boolean r5 = r4.equals(r7)     // Catch:{ Exception -> 0x01ec }
            if (r5 != 0) goto L_0x0118
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r5.<init>(r4)     // Catch:{ Exception -> 0x01ec }
            r7 = r5
            goto L_0x0119
        L_0x0118:
            r7 = r3
        L_0x0119:
            boolean r4 = r1.has(r14)     // Catch:{ Exception -> 0x01ec }
            if (r4 == 0) goto L_0x012f
            org.json.JSONObject r1 = r1.optJSONObject(r14)     // Catch:{ Exception -> 0x01ec }
            boolean r3 = r1.has(r12)     // Catch:{ Exception -> 0x01ec }
            if (r3 == 0) goto L_0x012d
            java.lang.String r2 = r1.optString(r12)     // Catch:{ Exception -> 0x01ec }
        L_0x012d:
            r8 = r1
            goto L_0x0130
        L_0x012f:
            r8 = r3
        L_0x0130:
            int r1 = android.graphics.Color.parseColor(r2)     // Catch:{ Exception -> 0x0136 }
        L_0x0134:
            r5 = r1
            goto L_0x013b
        L_0x0136:
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r2)     // Catch:{ Exception -> 0x01ec }
            goto L_0x0134
        L_0x013b:
            r3 = 0
            r4 = 0
            r6 = 0
            java.lang.String r10 = "rect"
            r11 = 0
            r19 = 1
            r1 = r24
            r2 = r23
            r9 = r16
            r20 = r12
            r12 = r19
            r1.makeOverlay(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x01ec }
            goto L_0x01dc
        L_0x0152:
            r20 = r21
            java.lang.String r4 = "richtext"
            boolean r4 = r2.equals(r4)     // Catch:{ Exception -> 0x01ec }
            if (r4 == 0) goto L_0x0191
            java.lang.String r4 = r1.optString(r6)     // Catch:{ Exception -> 0x01ec }
            java.lang.String r2 = r1.optString(r5)     // Catch:{ Exception -> 0x01ec }
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x01ec }
            if (r5 != 0) goto L_0x0176
            boolean r5 = r2.equals(r7)     // Catch:{ Exception -> 0x01ec }
            if (r5 != 0) goto L_0x0176
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r5.<init>(r2)     // Catch:{ Exception -> 0x01ec }
            goto L_0x0177
        L_0x0176:
            r5 = r3
        L_0x0177:
            boolean r2 = r1.has(r15)     // Catch:{ Exception -> 0x01ec }
            if (r2 == 0) goto L_0x0183
            org.json.JSONObject r1 = r1.optJSONObject(r15)     // Catch:{ Exception -> 0x01ec }
            r6 = r1
            goto L_0x0184
        L_0x0183:
            r6 = r3
        L_0x0184:
            r1 = r24
            r2 = r23
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r16
            r1.makeRichText(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x01ec }
            goto L_0x01dc
        L_0x0191:
            java.lang.String r3 = "input"
            boolean r3 = r2.equals(r3)     // Catch:{ Exception -> 0x01ec }
            if (r3 == 0) goto L_0x01ce
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x01a3 }
            java.lang.String r3 = r1.optString(r5)     // Catch:{ JSONException -> 0x01a3 }
            r2.<init>(r3)     // Catch:{ JSONException -> 0x01a3 }
            goto L_0x01a8
        L_0x01a3:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r2.<init>()     // Catch:{ Exception -> 0x01ec }
        L_0x01a8:
            r7 = r2
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x01b6 }
            java.lang.String r3 = "inputStyles"
            java.lang.String r1 = r1.optString(r3)     // Catch:{ JSONException -> 0x01b6 }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x01b6 }
            r8 = r2
            goto L_0x01bc
        L_0x01b6:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x01ec }
            r1.<init>()     // Catch:{ Exception -> 0x01ec }
            r8 = r1
        L_0x01bc:
            r3 = 0
            r4 = 0
            r5 = -1
            r6 = 0
            java.lang.String r10 = "input"
            r11 = 0
            r12 = 1
            r1 = r24
            r2 = r23
            r9 = r16
            r1.makeOverlay(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x01ec }
            goto L_0x01dc
        L_0x01ce:
            java.lang.String r3 = "weex"
            boolean r2 = r2.equals(r3)     // Catch:{ Exception -> 0x01ec }
            if (r2 == 0) goto L_0x01dc
            r2 = r16
            r13.makeWeexView(r0, r1, r2)     // Catch:{ Exception -> 0x01ec }
        L_0x01dc:
            int r9 = r17 + 1
            r12 = r14
            r11 = r18
            r10 = r20
            r14 = r25
            goto L_0x0014
        L_0x01e7:
            r0 = 1
            r13.nativeInvalidate(r0)     // Catch:{ Exception -> 0x01ec }
            goto L_0x01f0
        L_0x01ec:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01f0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeBitmapMgr.initViewDrawItme(io.dcloud.common.DHInterface.IWebview, io.dcloud.feature.nativeObj.NativeView, org.json.JSONArray):void");
    }
}
