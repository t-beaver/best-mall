package io.dcloud.sdk.core.module;

import android.app.Activity;
import android.view.View;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.TidUtil;
import io.dcloud.sdk.poly.base.utils.e;

public abstract class DCBaseAOL {
    private final DCloudAdSlot a;
    private final Activity b;
    private int c = -1;
    protected int d = -1;
    protected int e = -1;
    private boolean f = false;
    protected int g;
    private String h;
    private String i;
    private String j;
    private String k;
    protected AOLLoader.VideoAdInteractionListener l;
    protected AOLLoader.FeedAdInteractionListener m;

    public DCBaseAOL(DCloudAdSlot dCloudAdSlot, Activity activity) {
        this.a = dCloudAdSlot;
        this.b = activity;
    }

    public final void a(String str) {
        this.i = str;
    }

    public void b(String str) {
        this.j = str;
    }

    public void biddingFail(int i2, int i3, int i4) {
        e.b("uniAd", "bidding fail:" + getType() + ",Win:" + i2 + ",second:" + i3 + ",slot:" + getSlotId());
    }

    public void biddingSuccess(int i2, int i3) {
        e.b("uniAd", "bidding success:" + getType() + ",Win:" + i2 + ",second:" + i3 + ",slot:" + getSlotId());
    }

    public void c(String str) {
        this.k = str;
    }

    public final void d(String str) {
        this.h = str;
    }

    public abstract void destroy();

    public String e() {
        return this.i;
    }

    /* access modifiers changed from: protected */
    public String f() {
        return this.j;
    }

    public int g() {
        return this.c;
    }

    public Activity getActivity() {
        return this.b;
    }

    public int getAdStatus() {
        return -1;
    }

    public int getAdType() {
        return this.g;
    }

    public View getExpressAdView(Activity activity) {
        return null;
    }

    public AOLLoader.FeedAdInteractionListener getFeedAdCallback() {
        return this.m;
    }

    public DCloudAdSlot getSlot() {
        return this.a;
    }

    public String getSlotId() {
        return this.h;
    }

    public String getTid() {
        return TidUtil.getTid(getType(), getAdType());
    }

    public abstract String getType();

    public AOLLoader.VideoAdInteractionListener getVideoAdCallback() {
        return this.l;
    }

    public String h() {
        return this.a.getAdpid();
    }

    public String i() {
        return this.a.getEI();
    }

    public boolean isSlotSupportBidding() {
        return this.f;
    }

    public abstract boolean isValid();

    public String j() {
        return this.k;
    }

    public int k() {
        return this.d;
    }

    public void render() {
    }

    public final void setBiddingECPM(int i2) {
        if (i2 > 0) {
            e.c(getType() + " current cpm:" + i2);
            this.c = i2;
        }
    }

    public void startLoadTime() {
    }

    public void a(boolean z) {
        this.f = z;
    }

    public final void a(AOLLoader.VideoAdInteractionListener videoAdInteractionListener) {
        this.l = videoAdInteractionListener;
    }

    public final void a(AOLLoader.FeedAdInteractionListener feedAdInteractionListener) {
        this.m = feedAdInteractionListener;
    }
}
