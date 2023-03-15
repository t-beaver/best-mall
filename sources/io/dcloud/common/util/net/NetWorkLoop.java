package io.dcloud.common.util.net;

import io.dcloud.common.util.ThreadPool;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

public class NetWorkLoop {
    private final int MAX_EXE_REQUESTDATA = 5;
    private LoopComparator mComparator = new LoopComparator();
    protected LinkedList<NetWork> mExeTask = new LinkedList<>();
    protected LinkedList<NetWork> mQuestTask = new LinkedList<>();
    protected Thread mSyncThread;

    class LoopComparator implements Comparator<NetWork> {
        LoopComparator() {
        }

        public int compare(NetWork netWork, NetWork netWork2) {
            return netWork.mPriority - netWork2.mPriority;
        }
    }

    public synchronized void addNetWork(NetWork netWork) {
        this.mQuestTask.add(netWork);
        Collections.sort(this.mQuestTask, this.mComparator);
    }

    public void dispose() {
        this.mSyncThread = null;
        try {
            LinkedList<NetWork> linkedList = this.mExeTask;
            if (linkedList != null && linkedList.size() > 0) {
                Iterator it = this.mExeTask.iterator();
                while (it.hasNext()) {
                    ((NetWork) it.next()).dispose();
                }
                this.mExeTask.clear();
                this.mExeTask = null;
            }
            LinkedList<NetWork> linkedList2 = this.mQuestTask;
            if (linkedList2 != null && linkedList2.size() > 0) {
                Iterator it2 = this.mQuestTask.iterator();
                while (it2.hasNext()) {
                    ((NetWork) it2.next()).dispose();
                }
                this.mQuestTask.clear();
                this.mQuestTask = null;
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void execSyncTask(NetWork netWork) {
        netWork.mNetWorkLoop = this;
        netWork.startWork();
    }

    public synchronized void removeNetWork(final NetWork netWork) {
        try {
            if (this.mQuestTask.contains(netWork)) {
                this.mQuestTask.remove(netWork);
            }
            if (this.mExeTask.contains(netWork)) {
                this.mExeTask.remove(netWork);
            }
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    netWork.cancelWork();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void startThreadPool() {
        AnonymousClass1 r0 = new Thread() {
            /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
                java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                	at java.util.ArrayList.rangeCheck(ArrayList.java:659)
                	at java.util.ArrayList.get(ArrayList.java:435)
                	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
                	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:368)
                	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:172)
                	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:106)
                	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
                	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
                */
            public void run() {
                /*
                    r4 = this;
                L_0x0000:
                    io.dcloud.common.util.net.NetWorkLoop r0 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ Exception -> 0x0052 }
                    java.lang.Thread r1 = r0.mSyncThread     // Catch:{ Exception -> 0x0052 }
                    if (r1 != 0) goto L_0x0007
                    return
                L_0x0007:
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r0 = r0.mQuestTask     // Catch:{ Exception -> 0x0052 }
                    boolean r0 = r0.isEmpty()     // Catch:{ Exception -> 0x0052 }
                    if (r0 == 0) goto L_0x0015
                    r0 = 100
                    java.lang.Thread.sleep(r0)     // Catch:{ Exception -> 0x0052 }
                    goto L_0x0000
                L_0x0015:
                    io.dcloud.common.util.net.NetWorkLoop r0 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ Exception -> 0x0052 }
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r0 = r0.mExeTask     // Catch:{ Exception -> 0x0052 }
                    monitor-enter(r0)     // Catch:{ Exception -> 0x0052 }
                    io.dcloud.common.util.net.NetWorkLoop r1 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ all -> 0x004f }
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r1 = r1.mExeTask     // Catch:{ all -> 0x004f }
                    int r1 = r1.size()     // Catch:{ all -> 0x004f }
                    r2 = 5
                    if (r1 >= r2) goto L_0x004d
                    io.dcloud.common.util.net.NetWorkLoop r1 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ all -> 0x004f }
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r1 = r1.mQuestTask     // Catch:{ all -> 0x004f }
                    monitor-enter(r1)     // Catch:{ all -> 0x004f }
                    io.dcloud.common.util.net.NetWorkLoop r2 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ all -> 0x004a }
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r2 = r2.mQuestTask     // Catch:{ all -> 0x004a }
                    r3 = 0
                    java.lang.Object r2 = r2.get(r3)     // Catch:{ all -> 0x004a }
                    io.dcloud.common.util.net.NetWork r2 = (io.dcloud.common.util.net.NetWork) r2     // Catch:{ all -> 0x004a }
                    io.dcloud.common.util.net.NetWorkLoop r3 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ all -> 0x004a }
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r3 = r3.mExeTask     // Catch:{ all -> 0x004a }
                    r3.add(r2)     // Catch:{ all -> 0x004a }
                    io.dcloud.common.util.net.NetWorkLoop r3 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ all -> 0x004a }
                    java.util.LinkedList<io.dcloud.common.util.net.NetWork> r3 = r3.mQuestTask     // Catch:{ all -> 0x004a }
                    r3.remove(r2)     // Catch:{ all -> 0x004a }
                    io.dcloud.common.util.net.NetWorkLoop r3 = io.dcloud.common.util.net.NetWorkLoop.this     // Catch:{ all -> 0x004a }
                    r3.execSyncTask(r2)     // Catch:{ all -> 0x004a }
                    monitor-exit(r1)     // Catch:{ all -> 0x004a }
                    goto L_0x004d
                L_0x004a:
                    r2 = move-exception
                    monitor-exit(r1)     // Catch:{ all -> 0x004a }
                    throw r2     // Catch:{ all -> 0x004f }
                L_0x004d:
                    monitor-exit(r0)     // Catch:{ all -> 0x004f }
                    goto L_0x0000
                L_0x004f:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x004f }
                    throw r1     // Catch:{ Exception -> 0x0052 }
                L_0x0052:
                    r0 = move-exception
                    r0.printStackTrace()
                    goto L_0x0000
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.net.NetWorkLoop.AnonymousClass1.run():void");
            }
        };
        this.mSyncThread = r0;
        r0.start();
    }
}
