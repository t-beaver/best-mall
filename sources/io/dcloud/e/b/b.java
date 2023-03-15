package io.dcloud.e.b;

import android.app.Application;
import io.dcloud.common.DHInterface.IConfusionMgr;
import io.dcloud.common.DHInterface.INativeAppInfo;
import java.lang.ref.SoftReference;

public class b implements INativeAppInfo {
    private IConfusionMgr a;
    private SoftReference<Application> b;

    public b(Application application) {
        a(application);
        a(this.a);
    }

    private void a(Application application) {
        this.b = new SoftReference<>(application);
    }

    public Application getApplication() {
        SoftReference<Application> softReference = this.b;
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }

    public IConfusionMgr getCofusionMgr() {
        return this.a;
    }

    private void a(IConfusionMgr iConfusionMgr) {
        if (iConfusionMgr == null) {
            iConfusionMgr = io.dcloud.e.c.b.c();
        }
        this.a = iConfusionMgr;
    }
}
