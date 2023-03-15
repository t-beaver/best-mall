package io.dcloud.feature.audio;

import android.media.AudioManager;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.adapter.util.MessageHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AudioFeatureImpl implements IFeature, MessageHandler.IMessages {
    static final String TAG = "AudioFeatureImpl";
    HashMap<String, ArrayList> mAppsAudioObj = null;

    private Object findAppObj(String str, String str2) {
        ArrayList appObjList = getAppObjList(str);
        if (!appObjList.isEmpty()) {
            Iterator it = appObjList.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if ((next instanceof AbsAudio) && ((AbsAudio) next).mUuid.equals(str2)) {
                    return next;
                }
            }
        }
        return null;
    }

    private ArrayList getAppObjList(String str) {
        ArrayList arrayList = this.mAppsAudioObj.get(str);
        if (arrayList != null) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList(2);
        this.mAppsAudioObj.put(str, arrayList2);
        return arrayList2;
    }

    private void putAppObjList(String str, Object obj) {
        getAppObjList(str).add(obj);
    }

    private void removeAppObjFromList(String str, Object obj) {
        ArrayList appObjList = getAppObjList(str);
        if (appObjList != null) {
            appObjList.remove(obj);
        }
    }

    private void setCanPlay(String str, String str2, boolean z) {
        Iterator it = getAppObjList(str).iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof AudioPlayer) {
                AudioPlayer audioPlayer = (AudioPlayer) next;
                if (!audioPlayer.mUuid.equals(str2) && !z) {
                    audioPlayer.pause();
                }
                audioPlayer.setCanMix(z);
            }
        }
    }

    private void setSpeakerphoneOn(AudioManager audioManager, boolean z) {
        if (z) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(1);
            return;
        }
        audioManager.setSpeakerphoneOn(false);
        audioManager.setRouting(0, 1, -1);
        audioManager.setMode(3);
    }

    public void dispose(String str) {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0070, code lost:
        if (r10.equals("getBuffered") == false) goto L_0x0047;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r9, java.lang.String r10, java.lang.String[] r11) {
        /*
            r8 = this;
            io.dcloud.common.DHInterface.IFrameView r0 = r9.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()
            java.lang.String r0 = r0.obtainAppId()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "execute pJsArgs[0]="
            r1.append(r2)
            r2 = 0
            r3 = r11[r2]
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "AudioFeatureImpl"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r3, (java.lang.String) r1)
            java.lang.String r1 = "AudioSyncExecMethod"
            boolean r1 = r1.equals(r10)
            r3 = 2
            r4 = 3
            r5 = 0
            r6 = 1
            if (r1 == 0) goto L_0x0108
            r10 = r11[r2]
            r11 = r11[r6]
            org.json.JSONArray r11 = io.dcloud.common.util.JSONUtil.createJSONArray(r11)
            r10.hashCode()
            r10.hashCode()
            r1 = -1
            int r7 = r10.hashCode()
            switch(r7) {
                case -1469262177: goto L_0x007e;
                case 85887754: goto L_0x0073;
                case 555863637: goto L_0x006a;
                case 700693540: goto L_0x005f;
                case 804240344: goto L_0x0054;
                case 1606340381: goto L_0x0049;
                default: goto L_0x0047;
            }
        L_0x0047:
            r3 = -1
            goto L_0x0088
        L_0x0049:
            java.lang.String r3 = "CreatePlayer"
            boolean r10 = r10.equals(r3)
            if (r10 != 0) goto L_0x0052
            goto L_0x0047
        L_0x0052:
            r3 = 5
            goto L_0x0088
        L_0x0054:
            java.lang.String r3 = "getStyles"
            boolean r10 = r10.equals(r3)
            if (r10 != 0) goto L_0x005d
            goto L_0x0047
        L_0x005d:
            r3 = 4
            goto L_0x0088
        L_0x005f:
            java.lang.String r3 = "getPaused"
            boolean r10 = r10.equals(r3)
            if (r10 != 0) goto L_0x0068
            goto L_0x0047
        L_0x0068:
            r3 = 3
            goto L_0x0088
        L_0x006a:
            java.lang.String r4 = "getBuffered"
            boolean r10 = r10.equals(r4)
            if (r10 != 0) goto L_0x0088
            goto L_0x0047
        L_0x0073:
            java.lang.String r3 = "getDuration"
            boolean r10 = r10.equals(r3)
            if (r10 != 0) goto L_0x007c
            goto L_0x0047
        L_0x007c:
            r3 = 1
            goto L_0x0088
        L_0x007e:
            java.lang.String r3 = "getPosition"
            boolean r10 = r10.equals(r3)
            if (r10 != 0) goto L_0x0087
            goto L_0x0047
        L_0x0087:
            r3 = 0
        L_0x0088:
            switch(r3) {
                case 0: goto L_0x00f5;
                case 1: goto L_0x00e2;
                case 2: goto L_0x00d3;
                case 3: goto L_0x00c4;
                case 4: goto L_0x00ab;
                case 5: goto L_0x008d;
                default: goto L_0x008b;
            }
        L_0x008b:
            goto L_0x0113
        L_0x008d:
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r2)
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r11, (int) r6)
            io.dcloud.feature.audio.AudioPlayer r11 = io.dcloud.feature.audio.AudioPlayer.createAudioPlayer(r11, r9)
            r11.mUuid = r10
            io.dcloud.common.DHInterface.IFrameView r9 = r9.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r9 = r9.obtainApp()
            java.lang.String r9 = r9.obtainAppId()
            r8.putAppObjList(r9, r11)
            goto L_0x0113
        L_0x00ab:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r2)
            java.lang.Object r9 = r8.findAppObj(r0, r9)
            io.dcloud.feature.audio.AudioPlayer r9 = (io.dcloud.feature.audio.AudioPlayer) r9
            int r10 = r11.length()
            if (r10 <= r6) goto L_0x00bf
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r6)
        L_0x00bf:
            java.lang.String r5 = r9.getStyles(r5)
            goto L_0x0113
        L_0x00c4:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r2)
            java.lang.Object r9 = r8.findAppObj(r0, r9)
            io.dcloud.feature.audio.AudioPlayer r9 = (io.dcloud.feature.audio.AudioPlayer) r9
            java.lang.String r5 = r9.isPause()
            goto L_0x0113
        L_0x00d3:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r2)
            java.lang.Object r9 = r8.findAppObj(r0, r9)
            io.dcloud.feature.audio.AudioPlayer r9 = (io.dcloud.feature.audio.AudioPlayer) r9
            java.lang.String r5 = r9.getBuffer()
            goto L_0x0113
        L_0x00e2:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r2)
            java.lang.Object r9 = r8.findAppObj(r0, r9)
            io.dcloud.feature.audio.AudioPlayer r9 = (io.dcloud.feature.audio.AudioPlayer) r9
            java.lang.String r9 = r9.getDuration()
            java.lang.String r5 = java.lang.String.valueOf(r9)
            goto L_0x0113
        L_0x00f5:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r2)
            java.lang.Object r9 = r8.findAppObj(r0, r9)
            io.dcloud.feature.audio.AudioPlayer r9 = (io.dcloud.feature.audio.AudioPlayer) r9
            java.lang.String r9 = r9.getPosition()
            java.lang.String r5 = java.lang.String.valueOf(r9)
            goto L_0x0113
        L_0x0108:
            java.lang.Object[] r0 = new java.lang.Object[r4]
            r0[r2] = r9
            r0[r6] = r10
            r0[r3] = r11
            io.dcloud.common.adapter.util.MessageHandler.sendMessage(r8, r0)
        L_0x0113:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.audio.AudioFeatureImpl.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void init(AbsMgr absMgr, String str) {
        this.mAppsAudioObj = new HashMap<>(2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: io.dcloud.feature.audio.AudioPlayer} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:94|95|(2:97|143)(1:132)) */
    /* JADX WARNING: Code restructure failed: missing block: B:132:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:?, code lost:
        r0 = (int) (java.lang.Double.parseDouble(io.dcloud.common.util.JSONUtil.getString(r0, 1)) * 1000.0d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01fc, code lost:
        if (r0 >= 0) goto L_0x01fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01fe, code lost:
        r15.seekTo(r0);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:94:0x01ec */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0244  */
    /* JADX WARNING: Removed duplicated region for block: B:138:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute(java.lang.Object r18) {
        /*
            r17 = this;
            r1 = r17
            r0 = r18
            java.lang.Object[] r0 = (java.lang.Object[]) r0
            r2 = 0
            r3 = r0[r2]
            io.dcloud.common.DHInterface.IWebview r3 = (io.dcloud.common.DHInterface.IWebview) r3
            r4 = 1
            r5 = r0[r4]
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r6 = 2
            r0 = r0[r6]
            java.lang.String[] r0 = (java.lang.String[]) r0
            io.dcloud.common.DHInterface.IFrameView r7 = r3.obtainFrameView()
            io.dcloud.common.DHInterface.IApp r7 = r7.obtainApp()
            java.lang.String r8 = r7.obtainAppId()
            r9 = r0[r2]
            r0 = r0[r4]
            org.json.JSONArray r0 = io.dcloud.common.util.JSONUtil.createJSONArray(r0)
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r2)
            java.lang.String r11 = "RecorderExecMethod"
            boolean r11 = r11.equals(r5)
            java.lang.String r12 = "stop"
            java.lang.String r13 = "resume"
            java.lang.String r14 = "pause"
            if (r11 == 0) goto L_0x00d9
            android.content.Context r2 = r3.getContext()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r11 = "Audio-"
            r7.append(r11)
            r7.append(r5)
            java.lang.String r5 = r7.toString()
            io.dcloud.common.util.AppRuntime.checkPrivacyComplianceAndPrompt(r2, r5)
            java.lang.String r2 = "record"
            boolean r2 = r2.equals(r9)     // Catch:{ Exception -> 0x00b7 }
            if (r2 == 0) goto L_0x007e
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r4)     // Catch:{ Exception -> 0x00b7 }
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r0, (int) r6)     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.feature.audio.recorder.RecordOption r4 = new io.dcloud.feature.audio.recorder.RecordOption     // Catch:{ Exception -> 0x00b7 }
            r4.<init>(r3, r0)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r0 = r4.mFileName     // Catch:{ Exception -> 0x00b7 }
            boolean r0 = io.dcloud.common.util.JSUtil.checkOperateDirErrorAndCallback(r3, r2, r0)     // Catch:{ Exception -> 0x00b7 }
            if (r0 == 0) goto L_0x0073
            return
        L_0x0073:
            io.dcloud.feature.audio.AudioRecorderMgr r0 = io.dcloud.feature.audio.AudioRecorderMgr.startRecorder(r4, r2)     // Catch:{ Exception -> 0x00b7 }
            r0.mUuid = r10     // Catch:{ Exception -> 0x00b7 }
            r1.putAppObjList(r8, r0)     // Catch:{ Exception -> 0x00b7 }
            goto L_0x0252
        L_0x007e:
            boolean r0 = r14.equals(r9)     // Catch:{ Exception -> 0x00b7 }
            if (r0 == 0) goto L_0x008f
            java.lang.Object r0 = r1.findAppObj(r8, r10)     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.feature.audio.AudioRecorderMgr r0 = (io.dcloud.feature.audio.AudioRecorderMgr) r0     // Catch:{ Exception -> 0x00b7 }
            r0.pause()     // Catch:{ Exception -> 0x00b7 }
            goto L_0x0252
        L_0x008f:
            boolean r0 = r12.equals(r9)     // Catch:{ Exception -> 0x00b7 }
            if (r0 == 0) goto L_0x00a6
            java.lang.Object r0 = r1.findAppObj(r8, r10)     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.feature.audio.AudioRecorderMgr r0 = (io.dcloud.feature.audio.AudioRecorderMgr) r0     // Catch:{ Exception -> 0x00b7 }
            r0.stop()     // Catch:{ Exception -> 0x00b7 }
            r0.successCallback()     // Catch:{ Exception -> 0x00b7 }
            r1.removeAppObjFromList(r8, r0)     // Catch:{ Exception -> 0x00b7 }
            goto L_0x0252
        L_0x00a6:
            boolean r0 = r13.equals(r9)     // Catch:{ Exception -> 0x00b7 }
            if (r0 == 0) goto L_0x0252
            java.lang.Object r0 = r1.findAppObj(r8, r10)     // Catch:{ Exception -> 0x00b7 }
            io.dcloud.feature.audio.AudioRecorderMgr r0 = (io.dcloud.feature.audio.AudioRecorderMgr) r0     // Catch:{ Exception -> 0x00b7 }
            r0.resume()     // Catch:{ Exception -> 0x00b7 }
            goto L_0x0252
        L_0x00b7:
            r0 = move-exception
            r0.printStackTrace()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "RecorderExecMethod _methodName="
            r2.append(r3)
            r2.append(r9)
            java.lang.String r3 = "; e ="
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            io.dcloud.common.adapter.util.Logger.e(r0)
            goto L_0x0252
        L_0x00d9:
            java.lang.String r11 = "AudioExecMethod"
            boolean r5 = r11.equals(r5)
            if (r5 == 0) goto L_0x0252
            r5 = 0
            r11 = -1
            java.lang.Object r15 = r1.findAppObj(r8, r10)     // Catch:{ Exception -> 0x023e }
            io.dcloud.feature.audio.AudioPlayer r15 = (io.dcloud.feature.audio.AudioPlayer) r15     // Catch:{ Exception -> 0x023e }
            int r16 = r9.hashCode()     // Catch:{ Exception -> 0x023b }
            switch(r16) {
                case -934426579: goto L_0x0162;
                case -906224877: goto L_0x0158;
                case -625809843: goto L_0x014e;
                case -541487286: goto L_0x0143;
                case -388163342: goto L_0x0138;
                case 3443508: goto L_0x012e;
                case 3540994: goto L_0x0126;
                case 94756344: goto L_0x011c;
                case 106440182: goto L_0x0114;
                case 589623268: goto L_0x0109;
                case 1355420059: goto L_0x00fd;
                case 1403417351: goto L_0x00f2;
                default: goto L_0x00f0;
            }     // Catch:{ Exception -> 0x023b }
        L_0x00f0:
            goto L_0x016a
        L_0x00f2:
            java.lang.String r12 = "setRoute"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 6
            goto L_0x016b
        L_0x00fd:
            java.lang.String r12 = "playbackRate"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 11
            goto L_0x016b
        L_0x0109:
            java.lang.String r12 = "setStyles"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 9
            goto L_0x016b
        L_0x0114:
            boolean r9 = r9.equals(r14)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 1
            goto L_0x016b
        L_0x011c:
            java.lang.String r12 = "close"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 4
            goto L_0x016b
        L_0x0126:
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 3
            goto L_0x016b
        L_0x012e:
            java.lang.String r12 = "play"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 0
            goto L_0x016b
        L_0x0138:
            java.lang.String r12 = "setSessionCategory"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 10
            goto L_0x016b
        L_0x0143:
            java.lang.String r12 = "removeEventListener"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 8
            goto L_0x016b
        L_0x014e:
            java.lang.String r12 = "addEventListener"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 7
            goto L_0x016b
        L_0x0158:
            java.lang.String r12 = "seekTo"
            boolean r9 = r9.equals(r12)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 5
            goto L_0x016b
        L_0x0162:
            boolean r9 = r9.equals(r13)     // Catch:{ Exception -> 0x023b }
            if (r9 == 0) goto L_0x016a
            r9 = 2
            goto L_0x016b
        L_0x016a:
            r9 = -1
        L_0x016b:
            switch(r9) {
                case 0: goto L_0x022a;
                case 1: goto L_0x0226;
                case 2: goto L_0x020d;
                case 3: goto L_0x0209;
                case 4: goto L_0x0202;
                case 5: goto L_0x01dc;
                case 6: goto L_0x01bc;
                case 7: goto L_0x01af;
                case 8: goto L_0x01a6;
                case 9: goto L_0x019d;
                case 10: goto L_0x018e;
                case 11: goto L_0x0170;
                default: goto L_0x016e;
            }     // Catch:{ Exception -> 0x023b }
        L_0x016e:
            goto L_0x0252
        L_0x0170:
            int r2 = r0.length()     // Catch:{ Exception -> 0x023b }
            if (r2 <= r4) goto L_0x0252
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r4)     // Catch:{ Exception -> 0x023b }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0252 }
            if (r2 != 0) goto L_0x0252
            float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ Exception -> 0x0252 }
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0252
            r15.playbackRate(r0)     // Catch:{ Exception -> 0x0252 }
            goto L_0x0252
        L_0x018e:
            int r2 = r0.length()     // Catch:{ Exception -> 0x023b }
            if (r2 <= r4) goto L_0x0198
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r4)     // Catch:{ Exception -> 0x023b }
        L_0x0198:
            r15.setSessionCategory(r5)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x019d:
            org.json.JSONObject r0 = r0.optJSONObject(r4)     // Catch:{ Exception -> 0x023b }
            r15.setStyle(r0)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x01a6:
            java.lang.String r0 = r0.optString(r4)     // Catch:{ Exception -> 0x023b }
            r15.removeEventListener(r0)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x01af:
            java.lang.String r2 = r0.optString(r4)     // Catch:{ Exception -> 0x023b }
            java.lang.String r0 = r0.optString(r6)     // Catch:{ Exception -> 0x023b }
            r15.addEventListener(r2, r0)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x01bc:
            android.content.Context r3 = r3.getContext()     // Catch:{ Exception -> 0x023b }
            java.lang.String r5 = "audio"
            java.lang.Object r3 = r3.getSystemService(r5)     // Catch:{ Exception -> 0x023b }
            android.media.AudioManager r3 = (android.media.AudioManager) r3     // Catch:{ Exception -> 0x023b }
            java.lang.String r0 = r0.optString(r4)     // Catch:{ Exception -> 0x023b }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x023b }
            if (r0 != r4) goto L_0x01d7
            r1.setSpeakerphoneOn(r3, r2)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x01d7:
            r1.setSpeakerphoneOn(r3, r4)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x01dc:
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r4)     // Catch:{ Exception -> 0x01ec }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Exception -> 0x01ec }
            if (r2 < 0) goto L_0x0252
            int r2 = r2 * 1000
            r15.seekTo(r2)     // Catch:{ Exception -> 0x01ec }
            goto L_0x0252
        L_0x01ec:
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r4)     // Catch:{ Exception -> 0x0252 }
            double r2 = java.lang.Double.parseDouble(r0)     // Catch:{ Exception -> 0x0252 }
            r4 = 4652007308841189376(0x408f400000000000, double:1000.0)
            double r2 = r2 * r4
            int r0 = (int) r2     // Catch:{ Exception -> 0x0252 }
            if (r0 < 0) goto L_0x0252
            r15.seekTo(r0)     // Catch:{ Exception -> 0x0252 }
            goto L_0x0252
        L_0x0202:
            r15.destory()     // Catch:{ Exception -> 0x023b }
            r1.removeAppObjFromList(r8, r15)     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x0209:
            r15.stop()     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x020d:
            boolean r0 = r15.isCanMix()     // Catch:{ Exception -> 0x023b }
            r1.setCanPlay(r8, r10, r0)     // Catch:{ Exception -> 0x023b }
            boolean r0 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r7)     // Catch:{ Exception -> 0x023b }
            if (r0 == 0) goto L_0x0222
            java.lang.String r0 = ""
            r15.mFunId = r0     // Catch:{ Exception -> 0x023b }
            r15.play()     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x0222:
            r15.resume()     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x0226:
            r15.pause()     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x022a:
            boolean r2 = r15.isCanMix()     // Catch:{ Exception -> 0x023b }
            r1.setCanPlay(r8, r10, r2)     // Catch:{ Exception -> 0x023b }
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r0, (int) r4)     // Catch:{ Exception -> 0x023b }
            r15.mFunId = r0     // Catch:{ Exception -> 0x023b }
            r15.play()     // Catch:{ Exception -> 0x023b }
            goto L_0x0252
        L_0x023b:
            r0 = move-exception
            r5 = r15
            goto L_0x023f
        L_0x023e:
            r0 = move-exception
        L_0x023f:
            r0.printStackTrace()
            if (r5 == 0) goto L_0x0252
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_PARAMETER_ERROR
            r5.failCallback(r11, r0)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r11, (java.lang.String) r0)
            java.lang.String r2 = "onError"
            r5.execEvents(r2, r0)
        L_0x0252:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.audio.AudioFeatureImpl.execute(java.lang.Object):void");
    }
}
