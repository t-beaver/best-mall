package com.taobao.weex.ui.component.list.template;

import android.os.AsyncTask;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.ui.component.list.WXCell;

class AsyncCellLoadTask extends AsyncTask<Void, Void, Void> {
    private WXCell source;
    /* access modifiers changed from: private */
    public String template;
    /* access modifiers changed from: private */
    public WXRecyclerTemplateList templateList;

    public AsyncCellLoadTask(String str, WXCell wXCell, WXRecyclerTemplateList wXRecyclerTemplateList) {
        this.template = str;
        this.source = wXCell;
        this.templateList = wXRecyclerTemplateList;
    }

    /* access modifiers changed from: protected */
    public Void doInBackground(Void... voidArr) {
        TemplateCache templateCache = this.templateList.getTemplatesCache().get(this.template);
        if (!(templateCache == null || templateCache.cells == null)) {
            while (templateCache.cells.size() < this.templateList.getTemplateCacheSize()) {
                System.currentTimeMillis();
                WXCell wXCell = (WXCell) this.templateList.copyComponentFromSourceCell(this.source);
                WXEnvironment.isOpenDebugLog();
                if (wXCell == null || isDestory()) {
                    return null;
                }
                templateCache.cells.add(wXCell);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r4 = r3.templateList.getTemplatesCache().get(r3.template);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPostExecute(java.lang.Void r4) {
        /*
            r3 = this;
            boolean r4 = r3.isDestory()
            if (r4 == 0) goto L_0x0007
            return
        L_0x0007:
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r4 = r3.templateList
            java.util.concurrent.ConcurrentHashMap r4 = r4.getTemplatesCache()
            java.lang.String r0 = r3.template
            java.lang.Object r4 = r4.get(r0)
            com.taobao.weex.ui.component.list.template.TemplateCache r4 = (com.taobao.weex.ui.component.list.template.TemplateCache) r4
            if (r4 != 0) goto L_0x0018
            return
        L_0x0018:
            java.util.concurrent.ConcurrentLinkedQueue<com.taobao.weex.ui.component.list.WXCell> r0 = r4.cells
            r1 = 0
            if (r0 == 0) goto L_0x0035
            java.util.concurrent.ConcurrentLinkedQueue<com.taobao.weex.ui.component.list.WXCell> r0 = r4.cells
            int r0 = r0.size()
            if (r0 != 0) goto L_0x0026
            goto L_0x0035
        L_0x0026:
            android.os.MessageQueue r0 = android.os.Looper.myQueue()
            com.taobao.weex.ui.component.list.template.AsyncCellLoadTask$1 r2 = new com.taobao.weex.ui.component.list.template.AsyncCellLoadTask$1
            r2.<init>(r4)
            r0.addIdleHandler(r2)
            r4.isLoadIng = r1
            return
        L_0x0035:
            r4.isLoadIng = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.template.AsyncCellLoadTask.onPostExecute(java.lang.Void):void");
    }

    /* access modifiers changed from: private */
    public boolean isDestory() {
        if (this.source.getInstance() == null || this.source.getInstance().isDestroy()) {
            return true;
        }
        return this.templateList.isDestoryed();
    }

    public void startTask() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}
