package io.dcloud.common.core.ui;

import android.content.Context;
import io.dcloud.common.ui.d;

class c extends b {
    TabBarWebview C;
    b D;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    c(android.content.Context r8, io.dcloud.common.core.ui.l r9, io.dcloud.common.DHInterface.IApp r10, io.dcloud.common.core.ui.a r11, int r12, org.json.JSONObject r13) {
        /*
            r7 = this;
            r6 = 0
            r0 = r7
            r1 = r8
            r2 = r9
            r3 = r10
            r4 = r11
            r5 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6)
            io.dcloud.common.core.ui.TabBarWebview r11 = new io.dcloud.common.core.ui.TabBarWebview
            r0 = r11
            r2 = r10
            r3 = r9
            r4 = r7
            r5 = r13
            r0.<init>(r1, r2, r3, r4, r5)
            r7.C = r11
            r7.q = r11
            java.lang.String r8 = "tab"
            r11.setFrameId(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.c.<init>(android.content.Context, io.dcloud.common.core.ui.l, io.dcloud.common.DHInterface.IApp, io.dcloud.common.core.ui.a, int, org.json.JSONObject):void");
    }

    public void c(b bVar) {
        if (this.C.isInsertLauch()) {
            this.C.append(bVar);
            d(bVar);
        }
    }

    public boolean checkPagePathIsTab(String str) {
        TabBarWebview tabBarWebview = this.C;
        if (tabBarWebview != null) {
            return tabBarWebview.checkPagePathIsTab(str);
        }
        return false;
    }

    public void d(b bVar) {
        this.D = bVar;
        TabBarWebview tabBarWebview = this.C;
        if (tabBarWebview != null) {
            tabBarWebview.tabItemActive(bVar);
        }
    }

    public void dispose() {
        super.dispose();
        this.D = null;
        this.C = null;
    }

    /* access modifiers changed from: protected */
    public void initMainView(Context context, int i, Object obj) {
        setMainView(new d(context));
    }

    public void removeFrameView(b bVar) {
        TabBarWebview tabBarWebview = this.C;
        if (tabBarWebview != null) {
            tabBarWebview.removeFrameView(bVar);
        }
    }

    public b v() {
        return this.D;
    }
}
