package io.dcloud.js.camera;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import androidtranscoder.MediaTranscoder;
import androidtranscoder.format.MediaFormatStrategyPresets;
import androidx.core.content.FileProvider;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.ContentUriUtil;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.CompressUtil;
import io.dcloud.common.util.DCFileUriData;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.js.camera.a;
import java.io.File;
import java.io.IOException;
import org.json.JSONObject;

public class CameraFeatureImpl implements IFeature {
    AbsMgr a = null;

    class a extends PermissionUtil.StreamPermissionRequest {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;
        final /* synthetic */ a.C0070a d;
        final /* synthetic */ IApp e;

        /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a  reason: collision with other inner class name */
        class C0064a implements ISysEventListener {
            final /* synthetic */ DCFileUriData a;

            /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a$a  reason: collision with other inner class name */
            class C0065a implements Runnable {

                /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a$a$a  reason: collision with other inner class name */
                class C0066a implements Runnable {
                    C0066a() {
                    }

                    public void run() {
                        String json = DOMException.toJSON(-5, DOMException.MSG_IO_ERROR);
                        a aVar = a.this;
                        Deprecated_JSUtil.execCallback(aVar.a, aVar.b, json, JSUtil.ERROR, true, false);
                    }
                }

                /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a$a$b */
                class b implements Runnable {
                    b() {
                    }

                    public void run() {
                        String json = DOMException.toJSON(-5, DOMException.MSG_IO_ERROR);
                        a aVar = a.this;
                        Deprecated_JSUtil.execCallback(aVar.a, aVar.b, json, JSUtil.ERROR, true, false);
                    }
                }

                /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a$a$c */
                class c implements ISysEventListener {
                    final /* synthetic */ String a;

                    /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a$a$c$a  reason: collision with other inner class name */
                    class C0067a implements Runnable {

                        /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$a$a$a$c$a$a  reason: collision with other inner class name */
                        class C0068a implements Runnable {
                            C0068a() {
                            }

                            public void run() {
                                c cVar = c.this;
                                a aVar = a.this;
                                Deprecated_JSUtil.execCallback(aVar.a, aVar.b, cVar.a, JSUtil.OK, false, false);
                            }
                        }

                        C0067a() {
                        }

                        public void run() {
                            a.this.e.getActivity().runOnUiThread(new C0068a());
                        }
                    }

                    c(String str) {
                        this.a = str;
                    }

                    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                        Object[] objArr = (Object[]) obj;
                        int intValue = ((Integer) objArr[0]).intValue();
                        int intValue2 = ((Integer) objArr[1]).intValue();
                        if (sysEventType == ISysEventListener.SysEventType.onActivityResult && intValue == a.c) {
                            if (intValue2 == -1) {
                                ThreadPool.self().addThreadTask(new C0067a());
                            } else {
                                String json = DOMException.toJSON(11, "resultCode is wrong");
                                a aVar = a.this;
                                Deprecated_JSUtil.execCallback(aVar.a, aVar.b, json, JSUtil.ERROR, true, false);
                            }
                            a.this.e.unregisterSysEventListener(this, sysEventType);
                        }
                        return false;
                    }
                }

                C0065a() {
                }

                /* access modifiers changed from: private */
                public static /* synthetic */ void a(a.C0070a aVar, String str, IApp iApp, IWebview iWebview, String str2, String str3) {
                    if (aVar.h) {
                        CompressUtil.compressImage(str, str, false, iApp.getActivity());
                    }
                    iApp.getActivity().runOnUiThread(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0015: INVOKE  
                          (wrap: android.app.Activity : 0x000c: INVOKE  (r1v2 android.app.Activity) = (r3v0 'iApp' io.dcloud.common.DHInterface.IApp) io.dcloud.common.DHInterface.IAppInfo.getActivity():android.app.Activity type: INTERFACE)
                          (wrap: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA : 0x0012: CONSTRUCTOR  (r2v1 io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA) = 
                          (r4v0 'iWebview' io.dcloud.common.DHInterface.IWebview)
                          (r5v0 'str2' java.lang.String)
                          (r6v0 'str3' java.lang.String)
                         call: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA.<init>(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void type: CONSTRUCTOR)
                         android.app.Activity.runOnUiThread(java.lang.Runnable):void type: VIRTUAL in method: io.dcloud.js.camera.CameraFeatureImpl.a.a.a.a(io.dcloud.js.camera.a$a, java.lang.String, io.dcloud.common.DHInterface.IApp, io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void, dex: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                        	at jadx.core.codegen.ClassGen.addInnerClass(ClassGen.java:249)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:238)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                        	at jadx.core.codegen.ClassGen.addInnerClass(ClassGen.java:249)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:238)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                        	at jadx.core.codegen.ClassGen.addInnerClass(ClassGen.java:249)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:238)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.util.ArrayList.forEach(ArrayList.java:1259)
                        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                        	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                        	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                        	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                        	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                        	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                        	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                        	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                        	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                        	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                        Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0012: CONSTRUCTOR  (r2v1 io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA) = 
                          (r4v0 'iWebview' io.dcloud.common.DHInterface.IWebview)
                          (r5v0 'str2' java.lang.String)
                          (r6v0 'str3' java.lang.String)
                         call: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA.<init>(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void type: CONSTRUCTOR in method: io.dcloud.js.camera.CameraFeatureImpl.a.a.a.a(io.dcloud.js.camera.a$a, java.lang.String, io.dcloud.common.DHInterface.IApp, io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void, dex: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	... 74 more
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA, state: NOT_LOADED
                        	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:260)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:606)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                        	... 80 more
                        */
                    /*
                        boolean r1 = r1.h
                        if (r1 == 0) goto L_0x000c
                        android.app.Activity r1 = r3.getActivity()
                        r0 = 0
                        io.dcloud.common.util.CompressUtil.compressImage(r2, r2, r0, r1)
                    L_0x000c:
                        android.app.Activity r1 = r3.getActivity()
                        io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA r2 = new io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$gDnxNnI07kpyxHgrhs8Mg5WBwKA
                        r2.<init>(r4, r5, r6)
                        r1.runOnUiThread(r2)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.C0065a.a(io.dcloud.js.camera.a$a, java.lang.String, io.dcloud.common.DHInterface.IApp, io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void");
                }

                public void run() {
                    String str;
                    String str2;
                    DCFileUriData dCFileUriData = C0064a.this.a;
                    if (dCFileUriData.isReplace) {
                        if (DHFile.copyFile(dCFileUriData.fileReplacePath, dCFileUriData.filePath, true, false) != 1) {
                            a.this.e.getActivity().runOnUiThread(new C0066a());
                            return;
                        }
                        try {
                            DHFile.deleteFile(C0064a.this.a.fileReplacePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    C0064a aVar = C0064a.this;
                    a aVar2 = a.this;
                    if (aVar2.d.e) {
                        str2 = b.a(aVar.a.filePath);
                        if (TextUtils.isEmpty(str2)) {
                            a.this.e.getActivity().runOnUiThread(new b());
                            return;
                        }
                        str = a.this.e.convert2RelPath(str2);
                    } else {
                        str = aVar2.e.convert2RelPath(aVar.a.filePath);
                        str2 = C0064a.this.a.filePath;
                    }
                    String str3 = str2;
                    String str4 = str;
                    JSONObject jSONObject = a.this.d.g;
                    if (jSONObject == null || !jSONObject.has("width") || !a.this.d.g.has("height")) {
                        C0064a.this.a.clear();
                        ThreadPool self = ThreadPool.self();
                        a aVar3 = a.this;
                        self.addThreadTask(
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x016b: INVOKE  
                              (r0v10 'self' io.dcloud.common.util.ThreadPool)
                              (wrap: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ : 0x0168: CONSTRUCTOR  (r4v2 io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ) = 
                              (wrap: io.dcloud.js.camera.a$a : 0x015d: IGET  (r5v0 io.dcloud.js.camera.a$a) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.d io.dcloud.js.camera.a$a)
                              (r6v0 'str3' java.lang.String)
                              (wrap: io.dcloud.common.DHInterface.IApp : 0x015f: IGET  (r7v0 io.dcloud.common.DHInterface.IApp) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.e io.dcloud.common.DHInterface.IApp)
                              (wrap: io.dcloud.common.DHInterface.IWebview : 0x0161: IGET  (r8v0 io.dcloud.common.DHInterface.IWebview) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.a io.dcloud.common.DHInterface.IWebview)
                              (wrap: java.lang.String : 0x0163: IGET  (r9v0 java.lang.String) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.b java.lang.String)
                              (r10v0 'str4' java.lang.String)
                             call: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ.<init>(io.dcloud.js.camera.a$a, java.lang.String, io.dcloud.common.DHInterface.IApp, io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void type: CONSTRUCTOR)
                             io.dcloud.common.util.ThreadPool.addThreadTask(java.lang.Runnable):void type: VIRTUAL in method: io.dcloud.js.camera.CameraFeatureImpl.a.a.a.run():void, dex: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                            	at java.util.ArrayList.forEach(ArrayList.java:1259)
                            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                            	at jadx.core.codegen.ClassGen.addInnerClass(ClassGen.java:249)
                            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:238)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                            	at java.util.ArrayList.forEach(ArrayList.java:1259)
                            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                            	at jadx.core.codegen.ClassGen.addInnerClass(ClassGen.java:249)
                            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:238)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                            	at java.util.ArrayList.forEach(ArrayList.java:1259)
                            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                            	at jadx.core.codegen.ClassGen.addInnerClass(ClassGen.java:249)
                            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:238)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                            	at java.util.ArrayList.forEach(ArrayList.java:1259)
                            	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                            	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                            	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                            	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                            	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                            	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                            	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                            	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                            	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0168: CONSTRUCTOR  (r4v2 io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ) = 
                              (wrap: io.dcloud.js.camera.a$a : 0x015d: IGET  (r5v0 io.dcloud.js.camera.a$a) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.d io.dcloud.js.camera.a$a)
                              (r6v0 'str3' java.lang.String)
                              (wrap: io.dcloud.common.DHInterface.IApp : 0x015f: IGET  (r7v0 io.dcloud.common.DHInterface.IApp) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.e io.dcloud.common.DHInterface.IApp)
                              (wrap: io.dcloud.common.DHInterface.IWebview : 0x0161: IGET  (r8v0 io.dcloud.common.DHInterface.IWebview) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.a io.dcloud.common.DHInterface.IWebview)
                              (wrap: java.lang.String : 0x0163: IGET  (r9v0 java.lang.String) = (r2v4 'aVar3' io.dcloud.js.camera.CameraFeatureImpl$a) io.dcloud.js.camera.CameraFeatureImpl.a.b java.lang.String)
                              (r10v0 'str4' java.lang.String)
                             call: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ.<init>(io.dcloud.js.camera.a$a, java.lang.String, io.dcloud.common.DHInterface.IApp, io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String):void type: CONSTRUCTOR in method: io.dcloud.js.camera.CameraFeatureImpl.a.a.a.run():void, dex: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:123)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:107)
                            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:787)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:728)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:368)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                            	... 81 more
                            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ, state: NOT_LOADED
                            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:260)
                            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:606)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:231)
                            	... 87 more
                            */
                        /*
                            this = this;
                            r1 = r21
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.common.util.DCFileUriData r0 = r0.a
                            boolean r2 = r0.isReplace
                            r3 = 0
                            if (r2 == 0) goto L_0x0037
                            java.lang.String r2 = r0.fileReplacePath
                            java.lang.String r0 = r0.filePath
                            r4 = 1
                            int r0 = io.dcloud.common.adapter.io.DHFile.copyFile(r2, r0, r4, r3)
                            if (r0 == r4) goto L_0x0029
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r0 = r0.e
                            android.app.Activity r0 = r0.getActivity()
                            io.dcloud.js.camera.CameraFeatureImpl$a$a$a$a r2 = new io.dcloud.js.camera.CameraFeatureImpl$a$a$a$a
                            r2.<init>()
                            r0.runOnUiThread(r2)
                            return
                        L_0x0029:
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this     // Catch:{ IOException -> 0x0033 }
                            io.dcloud.common.util.DCFileUriData r0 = r0.a     // Catch:{ IOException -> 0x0033 }
                            java.lang.String r0 = r0.fileReplacePath     // Catch:{ IOException -> 0x0033 }
                            io.dcloud.common.adapter.io.DHFile.deleteFile(r0)     // Catch:{ IOException -> 0x0033 }
                            goto L_0x0037
                        L_0x0033:
                            r0 = move-exception
                            r0.printStackTrace()
                        L_0x0037:
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.js.camera.a$a r4 = r2.d
                            boolean r4 = r4.e
                            if (r4 == 0) goto L_0x006d
                            io.dcloud.common.util.DCFileUriData r0 = r0.a
                            java.lang.String r0 = r0.filePath
                            java.lang.String r0 = io.dcloud.js.camera.b.a((java.lang.String) r0)
                            boolean r2 = android.text.TextUtils.isEmpty(r0)
                            if (r2 == 0) goto L_0x0062
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r0 = r0.e
                            android.app.Activity r0 = r0.getActivity()
                            io.dcloud.js.camera.CameraFeatureImpl$a$a$a$b r2 = new io.dcloud.js.camera.CameraFeatureImpl$a$a$a$b
                            r2.<init>()
                            r0.runOnUiThread(r2)
                            return
                        L_0x0062:
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r2 = r2.e
                            java.lang.String r2 = r2.convert2RelPath(r0)
                            goto L_0x007d
                        L_0x006d:
                            io.dcloud.common.DHInterface.IApp r2 = r2.e
                            io.dcloud.common.util.DCFileUriData r0 = r0.a
                            java.lang.String r0 = r0.filePath
                            java.lang.String r2 = r2.convert2RelPath(r0)
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.common.util.DCFileUriData r0 = r0.a
                            java.lang.String r0 = r0.filePath
                        L_0x007d:
                            r6 = r0
                            r10 = r2
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.js.camera.a$a r0 = r0.d
                            org.json.JSONObject r0 = r0.g
                            if (r0 == 0) goto L_0x014e
                            java.lang.String r2 = "width"
                            boolean r0 = r0.has(r2)
                            if (r0 == 0) goto L_0x014e
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.js.camera.a$a r0 = r0.d
                            org.json.JSONObject r0 = r0.g
                            java.lang.String r2 = "height"
                            boolean r0 = r0.has(r2)
                            if (r0 == 0) goto L_0x014e
                            java.io.File r0 = new java.io.File
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.common.util.DCFileUriData r2 = r2.a
                            java.lang.String r2 = r2.filePath
                            r0.<init>(r2)
                            com.dmcbig.mediapicker.entity.Media r2 = new com.dmcbig.mediapicker.entity.Media
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r4 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.common.util.DCFileUriData r4 = r4.a
                            java.lang.String r12 = r4.filePath
                            long r14 = java.lang.System.currentTimeMillis()
                            java.lang.String r20 = r0.getParent()
                            r16 = 1
                            r17 = 1
                            r19 = -1001(0xfffffffffffffc17, float:NaN)
                            java.lang.String r13 = ""
                            r11 = r2
                            r11.<init>(r12, r13, r14, r16, r17, r19, r20)
                            android.content.Intent r0 = new android.content.Intent
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r4 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r4 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r4 = r4.e
                            android.app.Activity r4 = r4.getActivity()
                            java.lang.Class<io.dcloud.feature.gallery.imageedit.IMGEditActivity> r5 = io.dcloud.feature.gallery.imageedit.IMGEditActivity.class
                            r0.<init>(r4, r5)
                            java.lang.StringBuilder r4 = new java.lang.StringBuilder
                            r4.<init>()
                            java.lang.String r5 = "file://"
                            r4.append(r5)
                            java.lang.String r5 = r2.path
                            r4.append(r5)
                            java.lang.String r4 = r4.toString()
                            android.net.Uri r4 = android.net.Uri.parse(r4)
                            java.lang.String r5 = "IMAGE_URI"
                            r0.putExtra(r5, r4)
                            int r4 = r2.id
                            java.lang.String r5 = "IMAGE_MEDIA_ID"
                            r0.putExtra(r5, r4)
                            java.lang.String r4 = "IMAGE_INDEX"
                            r0.putExtra(r4, r3)
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r4 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r4 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.js.camera.a$a r4 = r4.d
                            org.json.JSONObject r4 = r4.g
                            java.lang.String r4 = r4.toString()
                            java.lang.String r5 = "IMAGE_CROP"
                            r0.putExtra(r5, r4)
                            java.lang.String r2 = r2.path
                            java.lang.String r4 = "IMAGE_SAVE_PATH"
                            r0.putExtra(r4, r2)
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r2 = r2.e
                            io.dcloud.js.camera.CameraFeatureImpl$a$a$a$c r4 = new io.dcloud.js.camera.CameraFeatureImpl$a$a$a$c
                            r4.<init>(r10)
                            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r5 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onActivityResult
                            r2.registerSysEventListener(r4, r5)
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.common.util.DCFileUriData r2 = r2.a
                            r2.clear()
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r2 = r2.e
                            android.app.Activity r2 = r2.getActivity()
                            int r4 = io.dcloud.js.camera.a.c
                            r2.startActivityForResult(r0, r4)
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.common.DHInterface.IApp r0 = r0.e
                            android.app.Activity r0 = r0.getActivity()
                            r0.overridePendingTransition(r3, r3)
                            goto L_0x016e
                        L_0x014e:
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r0 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.common.util.DCFileUriData r0 = r0.a
                            r0.clear()
                            io.dcloud.common.util.ThreadPool r0 = io.dcloud.common.util.ThreadPool.self()
                            io.dcloud.js.camera.CameraFeatureImpl$a$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.this
                            io.dcloud.js.camera.CameraFeatureImpl$a r2 = io.dcloud.js.camera.CameraFeatureImpl.a.this
                            io.dcloud.js.camera.a$a r5 = r2.d
                            io.dcloud.common.DHInterface.IApp r7 = r2.e
                            io.dcloud.common.DHInterface.IWebview r8 = r2.a
                            java.lang.String r9 = r2.b
                            io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ r2 = new io.dcloud.js.camera.-$$Lambda$CameraFeatureImpl$a$a$a$H7AuhLD7YZIKZiAKNggjJT8M2BQ
                            r4 = r2
                            r4.<init>(r5, r6, r7, r8, r9, r10)
                            r0.addThreadTask(r2)
                        L_0x016e:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.camera.CameraFeatureImpl.a.C0064a.C0065a.run():void");
                    }
                }

                C0064a(DCFileUriData dCFileUriData) {
                    this.a = dCFileUriData;
                }

                public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                    Object[] objArr = (Object[]) obj;
                    int intValue = ((Integer) objArr[0]).intValue();
                    int intValue2 = ((Integer) objArr[1]).intValue();
                    if (sysEventType == ISysEventListener.SysEventType.onActivityResult && intValue == a.a) {
                        if (intValue2 == -1) {
                            ThreadPool.self().addThreadTask(new C0065a());
                        } else {
                            String json = DOMException.toJSON(11, "resultCode is wrong");
                            a aVar = a.this;
                            Deprecated_JSUtil.execCallback(aVar.a, aVar.b, json, JSUtil.ERROR, true, false);
                        }
                        a.this.e.unregisterSysEventListener(this, sysEventType);
                    }
                    return false;
                }
            }

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            a(IApp iApp, IWebview iWebview, String str, String str2, a.C0070a aVar, IApp iApp2) {
                super(iApp);
                this.a = iWebview;
                this.b = str;
                this.c = str2;
                this.d = aVar;
                this.e = iApp2;
            }

            public void onDenied(String str) {
                Deprecated_JSUtil.execCallback(this.a, this.b, DOMException.toJSON(11, DOMException.MSG_NO_PERMISSION), JSUtil.ERROR, true, false);
            }

            public void onGranted(String str) {
                try {
                    if (JSUtil.checkOperateDirErrorAndCallback(this.a, this.b, this.c)) {
                        Deprecated_JSUtil.execCallback(this.a, this.b, DOMException.toJSON(-5, DOMException.MSG_IO_ERROR), JSUtil.ERROR, true, false);
                        return;
                    }
                    File file = new File(this.c);
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    DCFileUriData shareImageUri = FileUtil.getShareImageUri(this.a.getContext(), file, this.d.a(), intent);
                    this.e.registerSysEventListener(new C0064a(shareImageUri), ISysEventListener.SysEventType.onActivityResult);
                    intent.putExtra("output", shareImageUri.fileUri);
                    this.a.getActivity().startActivityForResult(intent, a.a);
                } catch (Exception e2) {
                    Deprecated_JSUtil.execCallback(this.a, this.b, DOMException.toJSON(11, e2.getMessage()), JSUtil.ERROR, true, false);
                }
            }
        }

        class b extends PermissionUtil.StreamPermissionRequest {
            final /* synthetic */ String[] a;
            final /* synthetic */ IApp b;
            final /* synthetic */ IWebview c;
            final /* synthetic */ String d;

            class a implements ISysEventListener {
                final /* synthetic */ String a;
                final /* synthetic */ a.C0070a b;
                final /* synthetic */ String c;

                /* renamed from: io.dcloud.js.camera.CameraFeatureImpl$b$a$a  reason: collision with other inner class name */
                class C0069a implements MediaTranscoder.Listener {
                    final /* synthetic */ Dialog a;

                    C0069a(Dialog dialog) {
                        this.a = dialog;
                    }

                    public void onTranscodeCanceled() {
                        this.a.dismiss();
                        a aVar = a.this;
                        CameraFeatureImpl.this.a(aVar.b.h, aVar.a);
                        String json = DOMException.toJSON(-2, DOMException.MSG_USER_CANCEL);
                        b bVar = b.this;
                        Deprecated_JSUtil.execCallback(bVar.c, bVar.d, json, JSUtil.ERROR, true, false);
                    }

                    public void onTranscodeCompleted() {
                        this.a.dismiss();
                        a aVar = a.this;
                        CameraFeatureImpl.this.a(aVar.b.h, aVar.a);
                        a aVar2 = a.this;
                        String convert2RelPath = b.this.b.convert2RelPath(aVar2.c);
                        b bVar = b.this;
                        Deprecated_JSUtil.execCallback(bVar.c, bVar.d, convert2RelPath, JSUtil.OK, false, false);
                    }

                    public void onTranscodeFailed(Exception exc) {
                        this.a.dismiss();
                        a aVar = a.this;
                        CameraFeatureImpl.this.a(aVar.b.h, aVar.a);
                        String json = DOMException.toJSON(-99, exc.getMessage());
                        b bVar = b.this;
                        Deprecated_JSUtil.execCallback(bVar.c, bVar.d, json, JSUtil.ERROR, true, false);
                    }

                    public void onTranscodeProgress(double d) {
                    }
                }

                a(String str, a.C0070a aVar, String str2) {
                    this.a = str;
                    this.b = aVar;
                    this.c = str2;
                }

                public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                    Object[] objArr = (Object[]) obj;
                    int intValue = ((Integer) objArr[0]).intValue();
                    int intValue2 = ((Integer) objArr[1]).intValue();
                    if (sysEventType == ISysEventListener.SysEventType.onActivityResult && intValue == a.b) {
                        if (intValue2 != -1) {
                            b bVar = b.this;
                            Deprecated_JSUtil.execCallback(bVar.c, bVar.d, (String) null, JSUtil.ERROR, false, false);
                        } else if (new File(this.a).exists() || DHFile.copyFile(ContentUriUtil.getImageAbsolutePath(b.this.b.getActivity(), ((Intent) objArr[2]).getData()), this.a) == 1) {
                            Dialog dialog = null;
                            try {
                                if (this.b.h) {
                                    Dialog a2 = b.a(b.this.c.getContext());
                                    a2.show();
                                    MediaTranscoder.getInstance().transcodeVideo(this.a, this.c, MediaFormatStrategyPresets.createAndroid720pStrategy(2, 1.0d), (MediaTranscoder.Listener) new C0069a(a2));
                                } else {
                                    String convert2RelPath = b.this.b.convert2RelPath(this.c);
                                    b bVar2 = b.this;
                                    Deprecated_JSUtil.execCallback(bVar2.c, bVar2.d, convert2RelPath, JSUtil.OK, false, false);
                                }
                            } catch (IOException e) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                CameraFeatureImpl.this.a(this.b.h, this.a);
                                String json = DOMException.toJSON(-99, e.getMessage());
                                b bVar3 = b.this;
                                Deprecated_JSUtil.execCallback(bVar3.c, bVar3.d, json, JSUtil.ERROR, true, false);
                            }
                        } else {
                            b bVar4 = b.this;
                            Deprecated_JSUtil.execCallback(bVar4.c, bVar4.d, (String) null, JSUtil.ERROR, false, false);
                            b.this.b.unregisterSysEventListener(this, sysEventType);
                            return false;
                        }
                        b.this.b.unregisterSysEventListener(this, sysEventType);
                    }
                    return false;
                }
            }

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            b(IApp iApp, String[] strArr, IApp iApp2, IWebview iWebview, String str) {
                super(iApp);
                this.a = strArr;
                this.b = iApp2;
                this.c = iWebview;
                this.d = str;
            }

            public void onDenied(String str) {
                Deprecated_JSUtil.execCallback(this.c, this.d, DOMException.toJSON(11, DOMException.MSG_NO_PERMISSION), JSUtil.ERROR, true, false);
            }

            public void onGranted(String str) {
                String str2;
                try {
                    a.C0070a a2 = a.a(this.a[1], false);
                    String convert2AbsFullPath = this.b.convert2AbsFullPath(this.c.obtainFullUrl(), a2.a());
                    if (JSUtil.checkOperateDirErrorAndCallback(this.c, this.d, convert2AbsFullPath)) {
                        Deprecated_JSUtil.execCallback(this.c, this.d, DOMException.toJSON(-5, DOMException.MSG_IO_ERROR), JSUtil.ERROR, true, false);
                        return;
                    }
                    if (a2.h) {
                        str2 = convert2AbsFullPath + ".temp";
                    } else {
                        str2 = convert2AbsFullPath;
                    }
                    File file = new File(str2);
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    this.b.registerSysEventListener(new a(str2, a2, convert2AbsFullPath), ISysEventListener.SysEventType.onActivityResult);
                    Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
                    if (a2.b() != 0) {
                        intent.putExtra("android.intent.extra.durationLimit", a2.b());
                    }
                    if (Build.VERSION.SDK_INT >= 29) {
                        intent.putExtra("output", FileProvider.getUriForFile(this.c.getContext(), this.c.getContext().getPackageName() + ".dc.fileprovider", file));
                    }
                    this.c.getActivity().startActivityForResult(intent, a.b);
                } catch (Exception e2) {
                    Deprecated_JSUtil.execCallback(this.c, this.d, DOMException.toJSON(11, e2.getMessage()), JSUtil.ERROR, true, false);
                }
            }
        }

        class c extends PermissionUtil.StreamPermissionRequest {
            final /* synthetic */ a a;
            final /* synthetic */ String[] b;
            final /* synthetic */ IWebview c;

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            c(IApp iApp, a aVar, String[] strArr, IWebview iWebview) {
                super(iApp);
                this.a = aVar;
                this.b = strArr;
                this.c = iWebview;
            }

            public void onDenied(String str) {
            }

            public void onGranted(String str) {
                this.a.b();
                String[] strArr = this.b;
                if (strArr.length >= 3) {
                    Deprecated_JSUtil.execCallback(this.c, strArr[2], this.a.a(), JSUtil.OK, true, false);
                }
            }
        }

        public void dispose(String str) {
        }

        public String execute(IWebview iWebview, String str, String[] strArr) {
            String str2 = str;
            IApp obtainApp = iWebview.obtainFrameView().obtainApp();
            Context context = iWebview.getContext();
            AppRuntime.checkPrivacyComplianceAndPrompt(context, "Camera-" + str2);
            String str3 = strArr[0];
            if (Build.VERSION.SDK_INT >= 24) {
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
            }
            if (str2.equals("captureImage")) {
                a.C0070a a2 = a.a(strArr[1], true);
                String convert2AbsFullPath = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), a2.a());
                if (FileUtil.checkPrivatePath(iWebview.getContext(), convert2AbsFullPath) || FileUtil.getPathForPublicType(convert2AbsFullPath) != null) {
                    PermissionUtil.usePermission(obtainApp.getActivity(), "camera", PermissionUtil.PMS_CAMERA, 2, new a(obtainApp, iWebview, str3, convert2AbsFullPath, a2, obtainApp));
                } else {
                    Deprecated_JSUtil.execCallback(iWebview, str3, DOMException.toJSON(-5, DOMException.MSG_PATH_NOT_PRIVATE_ERROR), JSUtil.ERROR, true, false);
                    return null;
                }
            } else if (str2.equals("startVideoCapture")) {
                PermissionUtil.usePermission(obtainApp.getActivity(), "camera", PermissionUtil.PMS_CAMERA, 2, new b(obtainApp, strArr, obtainApp, iWebview, str3));
            } else if (str2.equals("getCamera")) {
                a aVar = new a(PdrUtil.parseInt(strArr[1], 1));
                if (PermissionUtil.checkSelfPermission(obtainApp.getActivity(), "android.permission.CAMERA") == 0) {
                    aVar.b();
                    return aVar.a();
                }
                PermissionUtil.usePermission(obtainApp.getActivity(), "camera", PermissionUtil.PMS_CAMERA, 2, new c(obtainApp, aVar, strArr, iWebview));
            }
            return null;
        }

        public void init(AbsMgr absMgr, String str) {
            this.a = absMgr;
        }

        /* access modifiers changed from: private */
        public void a(boolean z, String str) {
            if (z) {
                try {
                    if (str.endsWith(".temp")) {
                        new File(str).delete();
                    }
                } catch (Exception unused) {
                }
            }
        }
    }
