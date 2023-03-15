package com.taobao.weex.ui.component;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.FrameLayout;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.WXVideoView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;

@Component(lazyload = false)
public class WXVideo extends WXComponent<FrameLayout> {
    /* access modifiers changed from: private */
    public boolean mAutoPlay;
    /* access modifiers changed from: private */
    public boolean mError;
    boolean mPrepared;
    /* access modifiers changed from: private */
    public boolean mStopped;
    private WXVideoView.Wrapper mWrapper;

    @Deprecated
    public WXVideo(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXVideo(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public FrameLayout initComponentHostView(Context context) {
        final WXVideoView.Wrapper wrapper = new WXVideoView.Wrapper(context);
        wrapper.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", "onError:" + i);
                }
                wrapper.getProgressBar().setVisibility(8);
                WXVideo.this.mPrepared = false;
                boolean unused = WXVideo.this.mError = true;
                if (WXVideo.this.getEvents().contains(Constants.Event.FAIL)) {
                    WXVideo.this.notify(Constants.Event.FAIL, Constants.Value.STOP);
                }
                return true;
            }
        });
        wrapper.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", "onPrepared");
                }
                wrapper.getProgressBar().setVisibility(8);
                WXVideo.this.mPrepared = true;
                if (WXVideo.this.mAutoPlay) {
                    wrapper.start();
                }
                wrapper.getVideoView().seekTo(5);
                if (wrapper.getMediaController() != null) {
                    if (!WXVideo.this.mStopped) {
                        wrapper.getMediaController().show(3);
                    } else {
                        wrapper.getMediaController().hide();
                    }
                }
                boolean unused = WXVideo.this.mStopped = false;
            }
        });
        wrapper.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", "onCompletion");
                }
                if (WXVideo.this.getEvents().contains(Constants.Event.FINISH)) {
                    WXVideo.this.notify(Constants.Event.FINISH, Constants.Value.STOP);
                }
            }
        });
        wrapper.setOnVideoPauseListener(new WXVideoView.VideoPlayListener() {
            public void onPause() {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", "onPause");
                }
                if (WXVideo.this.getEvents().contains("pause")) {
                    WXVideo.this.notify("pause", "pause");
                }
            }

            public void onStart() {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", "onStart");
                }
                if (WXVideo.this.getEvents().contains("start")) {
                    WXVideo.this.notify("start", Constants.Value.PLAY);
                }
            }
        });
        this.mWrapper = wrapper;
        return wrapper;
    }

    /* access modifiers changed from: private */
    public void notify(String str, String str2) {
        HashMap hashMap = new HashMap(2);
        hashMap.put(Constants.Name.PLAY_STATUS, str2);
        hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        hashMap3.put(Constants.Name.PLAY_STATUS, str2);
        hashMap2.put(TemplateDom.KEY_ATTRS, hashMap3);
        WXSDKManager.getInstance().fireEvent(getInstanceId(), getRef(), str, hashMap, hashMap2);
    }

    public void bindData(WXComponent wXComponent) {
        super.bindData(wXComponent);
        addEvent(Constants.Event.APPEAR);
    }

    public void notifyAppearStateChange(String str, String str2) {
        super.notifyAppearStateChange(str, str2);
        this.mWrapper.createVideoViewIfVisible();
    }

    public void destroy() {
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -167173695:
                if (str.equals(Constants.Name.ZORDERTOP)) {
                    c = 0;
                    break;
                }
                break;
            case 114148:
                if (str.equals("src")) {
                    c = 1;
                    break;
                }
                break;
            case 1438608771:
                if (str.equals(Constants.Name.AUTO_PLAY)) {
                    c = 2;
                    break;
                }
                break;
            case 1439562083:
                if (str.equals(Constants.Name.AUTOPLAY)) {
                    c = 3;
                    break;
                }
                break;
            case 1582764102:
                if (str.equals(Constants.Name.PLAY_STATUS)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool != null) {
                    this.mWrapper.getVideoView().setZOrderOnTop(bool.booleanValue());
                }
                return true;
            case 1:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setSrc(string);
                }
                return true;
            case 2:
            case 3:
                Boolean bool2 = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool2 != null) {
                    setAutoPlay(bool2.booleanValue());
                }
                return true;
            case 4:
                String string2 = WXUtils.getString(obj, (String) null);
                if (string2 != null) {
                    setPlaystatus(string2);
                }
                return true;
            default:
                return super.setProperty(str, obj);
        }
    }

    @WXComponentProp(name = "src")
    public void setSrc(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null && !TextUtils.isEmpty(str)) {
            this.mWrapper.setVideoURI(getInstance().rewriteUri(Uri.parse(str), "video"));
            this.mWrapper.getProgressBar().setVisibility(0);
        }
    }

    @WXComponentProp(name = "autoPlay")
    public void setAutoPlay(boolean z) {
        this.mAutoPlay = z;
        if (z) {
            this.mWrapper.createIfNotExist();
            this.mWrapper.start();
        }
    }

    @WXComponentProp(name = "controls")
    public void setControls(String str) {
        if (TextUtils.equals(Constants.Name.CONTROLS, str)) {
            this.mWrapper.setControls(true);
        } else if (TextUtils.equals("nocontrols", str)) {
            this.mWrapper.setControls(false);
        }
    }

    @WXComponentProp(name = "playStatus")
    public void setPlaystatus(String str) {
        if (!this.mPrepared || this.mError || this.mStopped) {
            if ((this.mError || this.mStopped) && str.equals(Constants.Value.PLAY)) {
                this.mError = false;
                this.mWrapper.resume();
                this.mWrapper.getProgressBar().setVisibility(0);
            }
        } else if (str.equals(Constants.Value.PLAY)) {
            this.mWrapper.start();
        } else if (str.equals("pause")) {
            this.mWrapper.pause();
        } else if (str.equals(Constants.Value.STOP)) {
            this.mWrapper.stopPlayback();
            this.mStopped = true;
        }
    }
}
