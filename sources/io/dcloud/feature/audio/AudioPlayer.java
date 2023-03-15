package io.dcloud.feature.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import com.taobao.weex.common.Constants;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class AudioPlayer extends AbsAudio implements ISysEventListener, IEventCallback {
    private IApp _app;
    private boolean autoplay = false;
    /* access modifiers changed from: private */
    public int bufferPercent = 0;
    private Map<String, String> events = new HashMap();
    private boolean isCanMix = false;
    private boolean isCanplay = false;
    private boolean isPlay = false;
    private boolean isPrepared = false;
    private boolean isStoped = false;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int i) {
            if ((i == -1 || i == -2 || i == -3) && !AudioPlayer.this.needPause) {
                AudioPlayer.this.pause();
            }
        }
    };
    private AudioManager mAudioMgr;
    String mFunId;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private String mSrcPath = "";
    /* access modifiers changed from: private */
    public IWebview mWebview;
    /* access modifiers changed from: private */
    public boolean needPause = false;
    private JSONObject params;
    private int startTime = Integer.MIN_VALUE;
    private float volume = 1.0f;

    private AudioPlayer(JSONObject jSONObject, IWebview iWebview) {
        this.params = jSONObject;
        this.mWebview = iWebview;
        addListener();
        this._app = iWebview.obtainFrameView().obtainApp();
        iWebview.obtainFrameView().addFrameViewListener(this);
        this._app.registerSysEventListener(this, ISysEventListener.SysEventType.onStop);
        setStyle(this.params);
    }

    private void addListener() {
        this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                AudioPlayer.this.execEvents("canplay", "");
            }
        });
        this.mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                AudioPlayer.this.execEvents("seeked", "");
            }
        });
        this.mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                int unused = AudioPlayer.this.bufferPercent = i;
            }
        });
        this.mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                if (i == 701) {
                    AudioPlayer.this.execEvents(IApp.ConfigProperty.CONFIG_WAITING, "");
                    return false;
                } else if (i != 702 || !mediaPlayer.isPlaying()) {
                    return false;
                } else {
                    AudioPlayer.this.execEvents(Constants.Value.PLAY, "");
                    return false;
                }
            }
        });
        this.mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                int i3;
                String str;
                if (i == 1) {
                    i3 = -99;
                    str = DOMException.MSG_UNKNOWN_ERROR;
                } else if (i != 100) {
                    i3 = 0;
                    str = null;
                } else {
                    i3 = 1303;
                    str = AudioPlayer.this.mWebview.getContext().getString(R.string.dcloud_audio_abnormal_rebuild);
                }
                if (i2 == -1010) {
                    i3 = -3;
                    str = DOMException.MSG_NOT_SUPPORT;
                } else if (i2 == -1007) {
                    i3 = DOMException.CODE_AUDIO_ERROR_MALFORMED;
                    str = DOMException.MSG_AUDIO_ERROR_MALFORMED;
                } else if (i2 == -1004) {
                    i3 = -5;
                    str = DOMException.MSG_IO_ERROR;
                } else if (i2 == -110) {
                    i3 = DOMException.CODE_AUDIO_ERROR_TIMED_OUT;
                    str = DOMException.MSG_AUDIO_ERROR_TIMED_OUT;
                }
                if (i3 != 0) {
                    AudioPlayer.this.failCallback(i3, str);
                    AudioPlayer.this.execEvents("error", DOMException.toJSON(i3, str));
                }
                return true;
            }
        });
        this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                AudioPlayer.this.execEvents("ended", "");
            }
        });
    }

    static AudioPlayer createAudioPlayer(JSONObject jSONObject, IWebview iWebview) {
        return new AudioPlayer(jSONObject, iWebview);
    }

    private void requestAudioFocus() {
        if (this.mAudioMgr == null) {
            this.mAudioMgr = (AudioManager) this.mWebview.getActivity().getSystemService("audio");
        }
        AudioManager audioManager = this.mAudioMgr;
        if (audioManager != null) {
            audioManager.requestAudioFocus(this.mAudioFocusChangeListener, 3, 1);
        }
    }

    private void setSpeed() {
        MediaPlayer mediaPlayer;
        JSONObject jSONObject = this.params;
        if (jSONObject != null) {
            try {
                float parseFloat = Float.parseFloat(jSONObject.optString("playbackRate"));
                if (parseFloat > 0.0f && (mediaPlayer = this.mMediaPlayer) != null && Build.VERSION.SDK_INT >= 23) {
                    PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
                    playbackParams.setSpeed(parseFloat);
                    this.mMediaPlayer.setPlaybackParams(playbackParams);
                }
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x006d A[Catch:{ Exception -> 0x00cc }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setSrc(java.lang.String r11) {
        /*
            r10 = this;
            java.lang.String r0 = "android_asset/"
            java.lang.String r1 = "/android_asset/"
            java.lang.String r2 = "content://"
            boolean r2 = r11.startsWith(r2)     // Catch:{ Exception -> 0x00cc }
            r3 = 0
            if (r2 == 0) goto L_0x001e
            android.net.Uri r11 = android.net.Uri.parse(r11)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            io.dcloud.common.DHInterface.IWebview r1 = r10.mWebview     // Catch:{ Exception -> 0x00cc }
            android.app.Activity r1 = r1.getActivity()     // Catch:{ Exception -> 0x00cc }
            r0.setDataSource(r1, r11)     // Catch:{ Exception -> 0x00cc }
            goto L_0x00c4
        L_0x001e:
            boolean r2 = io.dcloud.common.util.PdrUtil.isNetPath(r11)     // Catch:{ Exception -> 0x00cc }
            if (r2 != 0) goto L_0x00b1
            io.dcloud.common.DHInterface.IApp r2 = r10._app     // Catch:{ Exception -> 0x00cc }
            java.lang.String r11 = r2.checkPrivateDirAndCopy2Temp(r11)     // Catch:{ Exception -> 0x00cc }
            io.dcloud.common.DHInterface.IApp r2 = r10._app     // Catch:{ Exception -> 0x00cc }
            io.dcloud.common.DHInterface.IWebview r4 = r10.mWebview     // Catch:{ Exception -> 0x00cc }
            java.lang.String r4 = r4.obtainFullUrl()     // Catch:{ Exception -> 0x00cc }
            java.lang.String r11 = r2.convert2AbsFullPath(r4, r11)     // Catch:{ Exception -> 0x00cc }
            io.dcloud.application.DCLoudApplicationImpl r2 = io.dcloud.application.DCLoudApplicationImpl.self()     // Catch:{ Exception -> 0x00cc }
            android.content.Context r2 = r2.getContext()     // Catch:{ Exception -> 0x00cc }
            boolean r4 = io.dcloud.common.util.FileUtil.needMediaStoreOpenFile(r2)     // Catch:{ Exception -> 0x00cc }
            if (r4 == 0) goto L_0x006a
            boolean r4 = io.dcloud.common.util.FileUtil.checkPrivatePath(r2, r11)     // Catch:{ Exception -> 0x00cc }
            if (r4 != 0) goto L_0x006a
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x00cc }
            r4.<init>(r11)     // Catch:{ Exception -> 0x00cc }
            boolean r5 = r4.exists()     // Catch:{ Exception -> 0x00cc }
            if (r5 == 0) goto L_0x006a
            android.net.Uri r5 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI     // Catch:{ Exception -> 0x00cc }
            android.net.Uri r2 = io.dcloud.common.util.FileUtil.getFileUri(r2, r4, r5)     // Catch:{ Exception -> 0x00cc }
            if (r2 == 0) goto L_0x006a
            android.media.MediaPlayer r4 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            io.dcloud.common.DHInterface.IWebview r5 = r10.mWebview     // Catch:{ Exception -> 0x00cc }
            android.app.Activity r5 = r5.getActivity()     // Catch:{ Exception -> 0x00cc }
            r4.setDataSource(r5, r2)     // Catch:{ Exception -> 0x00cc }
            r2 = 1
            goto L_0x006b
        L_0x006a:
            r2 = 0
        L_0x006b:
            if (r2 != 0) goto L_0x00b8
            boolean r4 = r11.startsWith(r1)     // Catch:{ Exception -> 0x00cc }
            java.lang.String r5 = ""
            if (r4 == 0) goto L_0x007a
            java.lang.String r11 = r11.replace(r1, r5)     // Catch:{ Exception -> 0x00cc }
            goto L_0x0084
        L_0x007a:
            boolean r1 = r11.startsWith(r0)     // Catch:{ Exception -> 0x00cc }
            if (r1 == 0) goto L_0x0084
            java.lang.String r11 = r11.replace(r0, r5)     // Catch:{ Exception -> 0x00cc }
        L_0x0084:
            boolean r0 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r11)     // Catch:{ Exception -> 0x00cc }
            if (r0 != 0) goto L_0x00b8
            io.dcloud.common.DHInterface.IWebview r0 = r10.mWebview     // Catch:{ Exception -> 0x00cc }
            android.app.Activity r0 = r0.getActivity()     // Catch:{ Exception -> 0x00cc }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x00cc }
            android.content.res.AssetFileDescriptor r11 = r0.openFd(r11)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r4 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r5 = r11.getFileDescriptor()     // Catch:{ Exception -> 0x00cc }
            long r6 = r11.getStartOffset()     // Catch:{ Exception -> 0x00cc }
            long r8 = r11.getLength()     // Catch:{ Exception -> 0x00cc }
            r4.setDataSource(r5, r6, r8)     // Catch:{ Exception -> 0x00cc }
            r10.isCanplay = r3     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r11 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            r11.prepareAsync()     // Catch:{ Exception -> 0x00cc }
            return
        L_0x00b1:
            java.lang.String r0 = "utf-8"
            java.lang.String r11 = java.net.URLDecoder.decode(r11, r0)     // Catch:{ Exception -> 0x00cc }
            r2 = 0
        L_0x00b8:
            if (r2 != 0) goto L_0x00c4
            android.media.MediaPlayer r0 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            r0.reset()     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            r0.setDataSource(r11)     // Catch:{ Exception -> 0x00cc }
        L_0x00c4:
            r10.isCanplay = r3     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r11 = r10.mMediaPlayer     // Catch:{ Exception -> 0x00cc }
            r11.prepareAsync()     // Catch:{ Exception -> 0x00cc }
            goto L_0x00e5
        L_0x00cc:
            r11 = move-exception
            r10.stop()
            java.lang.String r0 = r11.getMessage()
            r1 = -5
            r10.failCallback(r1, r0)
            java.lang.String r11 = r11.getMessage()
            java.lang.String r11 = io.dcloud.common.constant.DOMException.toJSON((int) r1, (java.lang.String) r11)
            java.lang.String r0 = "error"
            r10.execEvents(r0, r11)
        L_0x00e5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.audio.AudioPlayer.setSrc(java.lang.String):void");
    }

    private void startPlay() {
        requestAudioFocus();
        this.isPrepared = true;
        this.mMediaPlayer.start();
        setSpeed();
        execEvents(Constants.Value.PLAY, "");
        this.isPlay = false;
    }

    private void successCallback() {
        Deprecated_JSUtil.excCallbackSuccess(this.mWebview, this.mFunId, "");
    }

    /* access modifiers changed from: package-private */
    public void addEventListener(String str, String str2) {
        this.events.put(str, str2);
    }

    /* access modifiers changed from: package-private */
    public void destory() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mWebview.obtainFrameView().removeFrameViewListener(this);
            this.mWebview.obtainFrameView().obtainApp().unregisterSysEventListener(this, ISysEventListener.SysEventType.onStop);
            this.mMediaPlayer = null;
            AudioManager audioManager = this.mAudioMgr;
            if (audioManager != null) {
                audioManager.abandonAudioFocus(this.mAudioFocusChangeListener);
            }
            this.mAudioMgr = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void execEvents(String str, String str2) {
        String str3 = this.events.get(str);
        if (!PdrUtil.isEmpty(str3)) {
            Deprecated_JSUtil.execCallback(this.mWebview, str3, str2, JSUtil.OK, !PdrUtil.isEmpty(str2), true);
        }
        str.hashCode();
        if (str.equals("ended")) {
            pause();
            successCallback();
        } else if (str.equals("canplay")) {
            this.isCanplay = true;
            if (this.autoplay) {
                play();
            }
            if (this.isPlay) {
                startPlay();
            }
            int i = this.startTime;
            if (i != Integer.MIN_VALUE) {
                this.mMediaPlayer.seekTo(i);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void failCallback(int i, String str) {
        Deprecated_JSUtil.excCallbackError(this.mWebview, this.mFunId, DOMException.toJSON(i, str), true);
    }

    /* access modifiers changed from: package-private */
    public String getBuffer() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        return Deprecated_JSUtil.wrapJsVar(PdrUtil.int2DecimalStr(mediaPlayer != null ? (this.bufferPercent * mediaPlayer.getDuration()) / 100 : -1, 1000), false);
    }

    /* access modifiers changed from: package-private */
    public String getDuration() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            return Deprecated_JSUtil.wrapJsVar(Constants.Name.UNDEFINED, false);
        }
        int duration = mediaPlayer.getDuration();
        if (duration < 0) {
            return Deprecated_JSUtil.wrapJsVar(Constants.Name.UNDEFINED, false);
        }
        return Deprecated_JSUtil.wrapJsVar(PdrUtil.int2DecimalStr(duration, 1000), false);
    }

    /* access modifiers changed from: package-private */
    public String getPosition() {
        return Deprecated_JSUtil.wrapJsVar(PdrUtil.int2DecimalStr(this.mMediaPlayer.getCurrentPosition(), 1000), false);
    }

    /* access modifiers changed from: package-private */
    public String getStyles(String str) {
        Object obj;
        if (PdrUtil.isEmpty(str)) {
            return JSUtil.wrapJsVar(this.params);
        }
        str.hashCode();
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2129294769:
                if (str.equals("startTime")) {
                    c = 0;
                    break;
                }
                break;
            case -810883302:
                if (str.equals("volume")) {
                    c = 1;
                    break;
                }
                break;
            case 114148:
                if (str.equals("src")) {
                    c = 2;
                    break;
                }
                break;
            case 3327652:
                if (str.equals("loop")) {
                    c = 3;
                    break;
                }
                break;
            case 1355420059:
                if (str.equals("playbackRate")) {
                    c = 4;
                    break;
                }
                break;
            case 1439562083:
                if (str.equals(Constants.Name.AUTOPLAY)) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                int i = this.startTime;
                obj = Integer.valueOf(i < 0 ? this.params.has("startTime") ? this.params.optInt("startTime") : 0 : i / 1000);
                break;
            case 1:
                obj = Float.valueOf(this.volume);
                break;
            case 2:
                obj = this.mSrcPath;
                break;
            case 3:
                obj = Boolean.valueOf(this.mMediaPlayer.isLooping());
                break;
            case 4:
                if (Build.VERSION.SDK_INT < 23) {
                    obj = 1;
                    break;
                } else {
                    obj = Float.valueOf(this.mMediaPlayer.getPlaybackParams().getSpeed());
                    break;
                }
            case 5:
                obj = Boolean.valueOf(this.params.optBoolean(Constants.Name.AUTOPLAY, false));
                break;
            default:
                if (this.params.has(str)) {
                    return JSUtil.wrapJsVar(this.params.optString(str));
                }
                return Deprecated_JSUtil.wrapJsVar(Constants.Name.UNDEFINED, false);
        }
        if (obj != null) {
            return JSUtil.wrapJsVar(obj.toString());
        }
        return Deprecated_JSUtil.wrapJsVar(Constants.Name.UNDEFINED, false);
    }

    /* access modifiers changed from: package-private */
    public String getVolume() {
        return JSUtil.wrapJsVar(this.volume);
    }

    public boolean isCanMix() {
        return this.isCanMix;
    }

    /* access modifiers changed from: package-private */
    public String isPause() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        boolean z = true;
        if (mediaPlayer != null) {
            z = true ^ mediaPlayer.isPlaying();
        }
        return JSUtil.wrapJsVar(z);
    }

    public Object onCallBack(String str, Object obj) {
        if ((!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) || !(obj instanceof IWebview)) {
            return null;
        }
        destory();
        return null;
    }

    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType != ISysEventListener.SysEventType.onStop) {
            return false;
        }
        destory();
        return false;
    }

    /* access modifiers changed from: package-private */
    public void pause() {
        this.autoplay = false;
        try {
            this.mMediaPlayer.pause();
        } catch (Exception unused) {
        }
        execEvents("pause", "");
    }

    /* access modifiers changed from: package-private */
    public void play() {
        if (this.isStoped && !this.mMediaPlayer.isPlaying()) {
            try {
                this.mMediaPlayer.prepareAsync();
                this.isStoped = false;
            } catch (Exception unused) {
                this.mSrcPath = "";
                setStyle(this.params);
                this.isStoped = false;
            }
        }
        try {
            this.isPrepared = false;
            this.isPlay = true;
            if (this.isCanplay) {
                startPlay();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            destory();
            failCallback(-1, e.toString());
            execEvents("error", DOMException.toJSON(-1, e.getMessage()));
        } catch (NumberFormatException unused2) {
        }
    }

    public void playbackRate(float f) {
        if (this.params == null) {
            this.params = new JSONObject();
        }
        try {
            this.params.put("playbackRate", (double) f);
        } catch (JSONException unused) {
        }
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            setSpeed();
        }
    }

    /* access modifiers changed from: package-private */
    public void removeEventListener(String str) {
        this.events.remove(str);
    }

    /* access modifiers changed from: package-private */
    public void resume() {
        requestAudioFocus();
        this.mMediaPlayer.start();
        setSpeed();
    }

    /* access modifiers changed from: package-private */
    public void seekTo(int i) {
        this.mMediaPlayer.seekTo(i);
        execEvents("seeking", "");
    }

    public void setCanMix(boolean z) {
        this.needPause = z;
    }

    public void setParams(JSONObject jSONObject) {
        this.params = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public void setSessionCategory(String str) {
        MediaPlayer mediaPlayer;
        if (!PdrUtil.isEmpty(str) && (mediaPlayer = this.mMediaPlayer) != null && !mediaPlayer.isPlaying()) {
            this.isCanMix = str.equals("ambient");
        }
    }

    /* access modifiers changed from: package-private */
    public void setStyle(JSONObject jSONObject) {
        String optString = jSONObject.optString("src");
        if (!PdrUtil.isEmpty(optString)) {
            if (PdrUtil.isEmpty(this.mSrcPath)) {
                this.mMediaPlayer.reset();
                setSrc(optString);
            } else if (!optString.equals(this.mSrcPath)) {
                this.mMediaPlayer.reset();
                setSrc(optString);
            }
        }
        JSONUtil.combinJSONObject(this.params, jSONObject);
        this.mSrcPath = jSONObject.optString("src");
        this.mMediaPlayer.setLooping(this.params.optBoolean("loop"));
        try {
            float parseFloat = Float.parseFloat(this.params.optString("volume", "1"));
            this.volume = parseFloat;
            if (parseFloat < 0.0f) {
                this.volume = 0.0f;
            } else if (parseFloat > 1.0f) {
                this.volume = 1.0f;
            }
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            float f = this.volume;
            mediaPlayer.setVolume(f, f);
            if (this.params.has("startTime")) {
                this.startTime = this.params.optInt("startTime") * 1000;
            }
            this.autoplay = this.params.optBoolean(Constants.Name.AUTOPLAY, false);
        } catch (Exception unused) {
        }
        try {
            float parseFloat2 = Float.parseFloat(this.params.optString("playbackRate"));
            if (parseFloat2 > 0.0f) {
                playbackRate(parseFloat2);
            }
        } catch (Exception unused2) {
        }
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.isStoped = true;
            this.isCanplay = false;
            execEvents(Constants.Value.STOP, "");
        }
    }
}
