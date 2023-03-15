package io.dcloud.feature.weex.adapter.Fresco;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.systrace.FrescoSystrace;

public class DCGenericDraweeView extends DraweeView<DCGenericDraweeHierarchy> {
    private static Supplier<? extends AbstractDraweeControllerBuilder> sDraweecontrollerbuildersupplier;
    private AbstractDraweeControllerBuilder mControllerBuilder;

    public static void initialize(Supplier<? extends AbstractDraweeControllerBuilder> supplier) {
        sDraweecontrollerbuildersupplier = supplier;
    }

    public static void shutDown() {
        sDraweecontrollerbuildersupplier = null;
    }

    public DCGenericDraweeView(Context context, DCGenericDraweeHierarchy dCGenericDraweeHierarchy) {
        super(context);
        setHierarchy(dCGenericDraweeHierarchy);
        init();
    }

    public DCGenericDraweeView(Context context) {
        super(context);
        inflateHierarchy(context, (AttributeSet) null);
        init();
    }

    public DCGenericDraweeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflateHierarchy(context, attributeSet);
        init();
    }

    public DCGenericDraweeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        inflateHierarchy(context, attributeSet);
        init();
    }

    public DCGenericDraweeView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        inflateHierarchy(context, attributeSet);
        init();
    }

    private void init() {
        try {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.beginSection("SimpleDraweeView#init");
            }
            if (isInEditMode()) {
                getTopLevelDrawable().setVisible(true, false);
                getTopLevelDrawable().invalidateSelf();
            } else {
                Preconditions.checkNotNull(sDraweecontrollerbuildersupplier, "SimpleDraweeView was not initialized!");
                this.mControllerBuilder = (AbstractDraweeControllerBuilder) sDraweecontrollerbuildersupplier.get();
            }
        } finally {
            if (FrescoSystrace.isTracing()) {
                FrescoSystrace.endSection();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void inflateHierarchy(Context context, AttributeSet attributeSet) {
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("GenericDraweeView#inflateHierarchy");
        }
        DCGenericDraweeHierarchyBuilder inflateBuilder = DCGenericDraweeHierarchyInflater.inflateBuilder(context, attributeSet);
        setAspectRatio(inflateBuilder.getDesiredAspectRatio());
        setHierarchy(inflateBuilder.build());
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    /* access modifiers changed from: protected */
    public AbstractDraweeControllerBuilder getControllerBuilder() {
        return this.mControllerBuilder;
    }

    public void setImageRequest(ImageRequest imageRequest) {
        setController(this.mControllerBuilder.setImageRequest(imageRequest).setOldController(getController()).build());
    }

    public void setImageURI(Uri uri) {
        setImageURI(uri, (Object) null);
    }

    public void setImageURI(String str) {
        setImageURI(str, (Object) null);
    }

    public void setImageURI(Uri uri, Object obj) {
        setController(this.mControllerBuilder.setCallerContext(obj).setUri(uri).setOldController(getController()).build());
    }

    public void setImageURI(String str, Object obj) {
        setImageURI(str != null ? Uri.parse(str) : null, obj);
    }

    public void setActualImageResource(int i) {
        setActualImageResource(i, (Object) null);
    }

    public void setActualImageResource(int i, Object obj) {
        setImageURI(UriUtil.getUriForResourceId(i), obj);
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
    }
}
