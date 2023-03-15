package io.dcloud.feature.weex.adapter.Fresco;

import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;
import com.facebook.drawee.drawable.DrawableParent;
import com.facebook.drawee.drawable.FadeDrawable;
import com.facebook.drawee.drawable.ForwardingDrawable;
import com.facebook.drawee.drawable.MatrixDrawable;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.SettableDraweeHierarchy;
import com.facebook.imagepipeline.systrace.FrescoSystrace;

public class DCGenericDraweeHierarchy implements SettableDraweeHierarchy {
    private static final int ACTUAL_IMAGE_INDEX = 2;
    private static final int BACKGROUND_IMAGE_INDEX = 0;
    private static final int FAILURE_IMAGE_INDEX = 5;
    private static final int OVERLAY_IMAGES_INDEX = 6;
    private static final int PLACEHOLDER_IMAGE_INDEX = 1;
    private static final int PROGRESS_BAR_IMAGE_INDEX = 3;
    private static final int RETRY_IMAGE_INDEX = 4;
    private final ForwardingDrawable mActualImageWrapper;
    private final Drawable mEmptyActualImageDrawable;
    private final FadeDrawable mFadeDrawable;
    private final Resources mResources;
    private RoundingParams mRoundingParams;
    private final DCRootDrawable mTopLevelDrawable;

    DCGenericDraweeHierarchy(DCGenericDraweeHierarchyBuilder dCGenericDraweeHierarchyBuilder) {
        int i = 0;
        ColorDrawable colorDrawable = new ColorDrawable(0);
        this.mEmptyActualImageDrawable = colorDrawable;
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.beginSection("GenericDraweeHierarchy()");
        }
        this.mResources = dCGenericDraweeHierarchyBuilder.getResources();
        this.mRoundingParams = dCGenericDraweeHierarchyBuilder.getRoundingParams();
        ForwardingDrawable forwardingDrawable = new ForwardingDrawable(colorDrawable);
        this.mActualImageWrapper = forwardingDrawable;
        int i2 = 1;
        int size = (dCGenericDraweeHierarchyBuilder.getOverlays() != null ? dCGenericDraweeHierarchyBuilder.getOverlays().size() : 1) + (dCGenericDraweeHierarchyBuilder.getPressedStateOverlay() != null ? 1 : 0);
        Drawable[] drawableArr = new Drawable[(size + 6)];
        drawableArr[0] = buildBranch(dCGenericDraweeHierarchyBuilder.getBackground(), (ScalingUtils.ScaleType) null);
        drawableArr[1] = buildBranch(dCGenericDraweeHierarchyBuilder.getPlaceholderImage(), dCGenericDraweeHierarchyBuilder.getPlaceholderImageScaleType());
        drawableArr[2] = buildActualImageBranch(forwardingDrawable, dCGenericDraweeHierarchyBuilder.getActualImageScaleType(), dCGenericDraweeHierarchyBuilder.getActualImageFocusPoint(), dCGenericDraweeHierarchyBuilder.getActualImageColorFilter());
        drawableArr[3] = buildBranch(dCGenericDraweeHierarchyBuilder.getProgressBarImage(), dCGenericDraweeHierarchyBuilder.getProgressBarImageScaleType());
        drawableArr[4] = buildBranch(dCGenericDraweeHierarchyBuilder.getRetryImage(), dCGenericDraweeHierarchyBuilder.getRetryImageScaleType());
        drawableArr[5] = buildBranch(dCGenericDraweeHierarchyBuilder.getFailureImage(), dCGenericDraweeHierarchyBuilder.getFailureImageScaleType());
        if (size > 0) {
            if (dCGenericDraweeHierarchyBuilder.getOverlays() != null) {
                for (Drawable buildBranch : dCGenericDraweeHierarchyBuilder.getOverlays()) {
                    drawableArr[i + 6] = buildBranch(buildBranch, (ScalingUtils.ScaleType) null);
                    i++;
                }
                i2 = i;
            }
            if (dCGenericDraweeHierarchyBuilder.getPressedStateOverlay() != null) {
                drawableArr[i2 + 6] = buildBranch(dCGenericDraweeHierarchyBuilder.getPressedStateOverlay(), (ScalingUtils.ScaleType) null);
            }
        }
        FadeDrawable fadeDrawable = new FadeDrawable(drawableArr);
        this.mFadeDrawable = fadeDrawable;
        fadeDrawable.setTransitionDuration(dCGenericDraweeHierarchyBuilder.getFadeDuration());
        DCRootDrawable dCRootDrawable = new DCRootDrawable(DCWrappingUtils.maybeWrapWithRoundedOverlayColor(fadeDrawable, this.mRoundingParams));
        this.mTopLevelDrawable = dCRootDrawable;
        dCRootDrawable.mutate();
        resetFade();
        if (FrescoSystrace.isTracing()) {
            FrescoSystrace.endSection();
        }
    }

    private Drawable buildActualImageBranch(Drawable drawable, ScalingUtils.ScaleType scaleType, PointF pointF, ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
        return DCWrappingUtils.maybeWrapWithScaleType(drawable, scaleType, pointF);
    }

    private Drawable buildBranch(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        return DCWrappingUtils.maybeWrapWithScaleType(DCWrappingUtils.maybeApplyLeafRounding(drawable, this.mRoundingParams, this.mResources), scaleType);
    }

    private void resetActualImages() {
        this.mActualImageWrapper.setDrawable(this.mEmptyActualImageDrawable);
    }

    private void resetFade() {
        FadeDrawable fadeDrawable = this.mFadeDrawable;
        if (fadeDrawable != null) {
            fadeDrawable.beginBatchMode();
            this.mFadeDrawable.fadeInAllLayers();
            fadeOutBranches();
            fadeInLayer(1);
            this.mFadeDrawable.finishTransitionImmediately();
            this.mFadeDrawable.endBatchMode();
        }
    }

    private void fadeOutBranches() {
        fadeOutLayer(1);
        fadeOutLayer(2);
        fadeOutLayer(3);
        fadeOutLayer(4);
        fadeOutLayer(5);
    }

    private void fadeInLayer(int i) {
        if (i >= 0) {
            this.mFadeDrawable.fadeInLayer(i);
        }
    }

    private void fadeOutLayer(int i) {
        if (i >= 0) {
            this.mFadeDrawable.fadeOutLayer(i);
        }
    }

    private void setProgress(float f) {
        Drawable drawable = this.mFadeDrawable.getDrawable(3);
        if (drawable != null) {
            if (f >= 0.999f) {
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).stop();
                }
                fadeOutLayer(3);
            } else {
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
                fadeInLayer(3);
            }
            drawable.setLevel(Math.round(f * 10000.0f));
        }
    }

    public DCRootDrawable getTopLevelDrawable() {
        return this.mTopLevelDrawable;
    }

    public Rect getBounds() {
        return this.mTopLevelDrawable.getBounds();
    }

    public void reset() {
        resetActualImages();
        resetFade();
    }

    public void setImage(Drawable drawable, float f, boolean z) {
        Drawable maybeApplyLeafRounding = DCWrappingUtils.maybeApplyLeafRounding(drawable, this.mRoundingParams, this.mResources);
        maybeApplyLeafRounding.mutate();
        this.mActualImageWrapper.setDrawable(maybeApplyLeafRounding);
        this.mFadeDrawable.beginBatchMode();
        fadeOutBranches();
        fadeInLayer(2);
        setProgress(f);
        if (z) {
            this.mFadeDrawable.finishTransitionImmediately();
        }
        this.mFadeDrawable.endBatchMode();
    }

    public void setProgress(float f, boolean z) {
        if (this.mFadeDrawable.getDrawable(3) != null) {
            this.mFadeDrawable.beginBatchMode();
            setProgress(f);
            if (z) {
                this.mFadeDrawable.finishTransitionImmediately();
            }
            this.mFadeDrawable.endBatchMode();
        }
    }

    public void setFailure(Throwable th) {
        this.mFadeDrawable.beginBatchMode();
        fadeOutBranches();
        if (this.mFadeDrawable.getDrawable(5) != null) {
            fadeInLayer(5);
        } else {
            fadeInLayer(1);
        }
        this.mFadeDrawable.endBatchMode();
    }

    public void setRetry(Throwable th) {
        this.mFadeDrawable.beginBatchMode();
        fadeOutBranches();
        if (this.mFadeDrawable.getDrawable(4) != null) {
            fadeInLayer(4);
        } else {
            fadeInLayer(1);
        }
        this.mFadeDrawable.endBatchMode();
    }

    public void setControllerOverlay(Drawable drawable) {
        this.mTopLevelDrawable.setControllerOverlay(drawable);
    }

    private DrawableParent getParentDrawableAtIndex(int i) {
        DrawableParent drawableParentForIndex = this.mFadeDrawable.getDrawableParentForIndex(i);
        if (drawableParentForIndex.getDrawable() instanceof MatrixDrawable) {
            drawableParentForIndex = (MatrixDrawable) drawableParentForIndex.getDrawable();
        }
        return drawableParentForIndex.getDrawable() instanceof ScaleTypeDrawable ? (ScaleTypeDrawable) drawableParentForIndex.getDrawable() : drawableParentForIndex;
    }

    private void setChildDrawableAtIndex(int i, Drawable drawable) {
        if (drawable == null) {
            this.mFadeDrawable.setDrawable(i, (Drawable) null);
            return;
        }
        getParentDrawableAtIndex(i).setDrawable(DCWrappingUtils.maybeApplyLeafRounding(drawable, this.mRoundingParams, this.mResources));
    }

    private ScaleTypeDrawable getScaleTypeDrawableAtIndex(int i) {
        DrawableParent parentDrawableAtIndex = getParentDrawableAtIndex(i);
        if (parentDrawableAtIndex instanceof ScaleTypeDrawable) {
            return (ScaleTypeDrawable) parentDrawableAtIndex;
        }
        return DCWrappingUtils.wrapChildWithScaleType(parentDrawableAtIndex, ScalingUtils.ScaleType.FIT_XY);
    }

    private boolean hasScaleTypeDrawableAtIndex(int i) {
        return getParentDrawableAtIndex(i) instanceof ScaleTypeDrawable;
    }

    public void setFadeDuration(int i) {
        this.mFadeDrawable.setTransitionDuration(i);
    }

    public int getFadeDuration() {
        return this.mFadeDrawable.getTransitionDuration();
    }

    public void setActualImageFocusPoint(PointF pointF) {
        Preconditions.checkNotNull(pointF);
        getScaleTypeDrawableAtIndex(2).setFocusPoint(pointF);
    }

    public void setActualImageScaleType(ScalingUtils.ScaleType scaleType) {
        Preconditions.checkNotNull(scaleType);
        getScaleTypeDrawableAtIndex(2).setScaleType(scaleType);
    }

    public ScalingUtils.ScaleType getActualImageScaleType() {
        if (!hasScaleTypeDrawableAtIndex(2)) {
            return null;
        }
        return getScaleTypeDrawableAtIndex(2).getScaleType();
    }

    public void setActualImageColorFilter(ColorFilter colorFilter) {
        this.mActualImageWrapper.setColorFilter(colorFilter);
    }

    public void getActualImageBounds(RectF rectF) {
        this.mActualImageWrapper.getTransformedBounds(rectF);
    }

    public void setPlaceholderImage(Drawable drawable) {
        setChildDrawableAtIndex(1, drawable);
    }

    public void setPlaceholderImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        setChildDrawableAtIndex(1, drawable);
        getScaleTypeDrawableAtIndex(1).setScaleType(scaleType);
    }

    public boolean hasPlaceholderImage() {
        return this.mFadeDrawable.getDrawable(1) != null;
    }

    public void setPlaceholderImageFocusPoint(PointF pointF) {
        Preconditions.checkNotNull(pointF);
        getScaleTypeDrawableAtIndex(1).setFocusPoint(pointF);
    }

    public void setPlaceholderImage(int i) {
        setPlaceholderImage(this.mResources.getDrawable(i));
    }

    public void setPlaceholderImage(int i, ScalingUtils.ScaleType scaleType) {
        setPlaceholderImage(this.mResources.getDrawable(i), scaleType);
    }

    public void setFailureImage(Drawable drawable) {
        setChildDrawableAtIndex(5, drawable);
    }

    public void setFailureImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        setChildDrawableAtIndex(5, drawable);
        getScaleTypeDrawableAtIndex(5).setScaleType(scaleType);
    }

    public void setFailureImage(int i) {
        setFailureImage(this.mResources.getDrawable(i));
    }

    public void setFailureImage(int i, ScalingUtils.ScaleType scaleType) {
        setFailureImage(this.mResources.getDrawable(i), scaleType);
    }

    public void setRetryImage(Drawable drawable) {
        setChildDrawableAtIndex(4, drawable);
    }

    public void setRetryImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        setChildDrawableAtIndex(4, drawable);
        getScaleTypeDrawableAtIndex(4).setScaleType(scaleType);
    }

    public void setRetryImage(int i) {
        setRetryImage(this.mResources.getDrawable(i));
    }

    public void setRetryImage(int i, ScalingUtils.ScaleType scaleType) {
        setRetryImage(this.mResources.getDrawable(i), scaleType);
    }

    public void setProgressBarImage(Drawable drawable) {
        setChildDrawableAtIndex(3, drawable);
    }

    public void setProgressBarImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        setChildDrawableAtIndex(3, drawable);
        getScaleTypeDrawableAtIndex(3).setScaleType(scaleType);
    }

    public void setProgressBarImage(int i) {
        setProgressBarImage(this.mResources.getDrawable(i));
    }

    public void setProgressBarImage(int i, ScalingUtils.ScaleType scaleType) {
        setProgressBarImage(this.mResources.getDrawable(i), scaleType);
    }

    public void setBackgroundImage(Drawable drawable) {
        setChildDrawableAtIndex(0, drawable);
    }

    public void setOverlayImage(int i, Drawable drawable) {
        Preconditions.checkArgument(i >= 0 && i + 6 < this.mFadeDrawable.getNumberOfLayers(), "The given index does not correspond to an overlay image.");
        setChildDrawableAtIndex(i + 6, drawable);
    }

    public void setOverlayImage(Drawable drawable) {
        setOverlayImage(0, drawable);
    }

    public void setRoundingParams(RoundingParams roundingParams) {
        this.mRoundingParams = roundingParams;
        DCWrappingUtils.updateOverlayColorRounding(this.mTopLevelDrawable, roundingParams);
        for (int i = 0; i < this.mFadeDrawable.getNumberOfLayers(); i++) {
            DCWrappingUtils.updateLeafRounding(getParentDrawableAtIndex(i), this.mRoundingParams, this.mResources);
        }
    }

    public RoundingParams getRoundingParams() {
        return this.mRoundingParams;
    }

    public boolean hasImage() {
        return this.mActualImageWrapper.getDrawable() != this.mEmptyActualImageDrawable;
    }
}
