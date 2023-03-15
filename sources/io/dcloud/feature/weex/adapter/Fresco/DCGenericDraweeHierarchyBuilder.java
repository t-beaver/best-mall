package io.dcloud.feature.weex.adapter.Fresco;

import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import com.facebook.common.internal.Preconditions;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import java.util.Arrays;
import java.util.List;

public class DCGenericDraweeHierarchyBuilder {
    public static final ScalingUtils.ScaleType DEFAULT_ACTUAL_IMAGE_SCALE_TYPE = ScalingUtils.ScaleType.CENTER_CROP;
    public static final int DEFAULT_FADE_DURATION = 300;
    public static final ScalingUtils.ScaleType DEFAULT_SCALE_TYPE = ScalingUtils.ScaleType.CENTER_INSIDE;
    private ColorFilter mActualImageColorFilter;
    private PointF mActualImageFocusPoint;
    private Matrix mActualImageMatrix;
    private ScalingUtils.ScaleType mActualImageScaleType;
    private Drawable mBackground;
    private float mDesiredAspectRatio;
    private int mFadeDuration;
    private Drawable mFailureImage;
    private ScalingUtils.ScaleType mFailureImageScaleType;
    private List<Drawable> mOverlays;
    private Drawable mPlaceholderImage;
    private ScalingUtils.ScaleType mPlaceholderImageScaleType;
    private Drawable mPressedStateOverlay;
    private Drawable mProgressBarImage;
    private ScalingUtils.ScaleType mProgressBarImageScaleType;
    private Resources mResources;
    private Drawable mRetryImage;
    private ScalingUtils.ScaleType mRetryImageScaleType;
    private RoundingParams mRoundingParams;

    public DCGenericDraweeHierarchyBuilder(Resources resources) {
        this.mResources = resources;
        init();
    }

    public static DCGenericDraweeHierarchyBuilder newInstance(Resources resources) {
        return new DCGenericDraweeHierarchyBuilder(resources);
    }

    private void init() {
        this.mFadeDuration = 300;
        this.mDesiredAspectRatio = 0.0f;
        this.mPlaceholderImage = null;
        ScalingUtils.ScaleType scaleType = DEFAULT_SCALE_TYPE;
        this.mPlaceholderImageScaleType = scaleType;
        this.mRetryImage = null;
        this.mRetryImageScaleType = scaleType;
        this.mFailureImage = null;
        this.mFailureImageScaleType = scaleType;
        this.mProgressBarImage = null;
        this.mProgressBarImageScaleType = scaleType;
        this.mActualImageScaleType = DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
        this.mActualImageMatrix = null;
        this.mActualImageFocusPoint = null;
        this.mActualImageColorFilter = null;
        this.mBackground = null;
        this.mOverlays = null;
        this.mPressedStateOverlay = null;
        this.mRoundingParams = null;
    }

    public DCGenericDraweeHierarchyBuilder reset() {
        init();
        return this;
    }

    public Resources getResources() {
        return this.mResources;
    }

    public DCGenericDraweeHierarchyBuilder setFadeDuration(int i) {
        this.mFadeDuration = i;
        return this;
    }

    public int getFadeDuration() {
        return this.mFadeDuration;
    }

    public DCGenericDraweeHierarchyBuilder setDesiredAspectRatio(float f) {
        this.mDesiredAspectRatio = f;
        return this;
    }

    public float getDesiredAspectRatio() {
        return this.mDesiredAspectRatio;
    }

    public DCGenericDraweeHierarchyBuilder setPlaceholderImage(Drawable drawable) {
        this.mPlaceholderImage = drawable;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setPlaceholderImage(int i) {
        this.mPlaceholderImage = this.mResources.getDrawable(i);
        return this;
    }

    public Drawable getPlaceholderImage() {
        return this.mPlaceholderImage;
    }

    public DCGenericDraweeHierarchyBuilder setPlaceholderImageScaleType(ScalingUtils.ScaleType scaleType) {
        this.mPlaceholderImageScaleType = scaleType;
        return this;
    }

    public ScalingUtils.ScaleType getPlaceholderImageScaleType() {
        return this.mPlaceholderImageScaleType;
    }

    public DCGenericDraweeHierarchyBuilder setPlaceholderImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        this.mPlaceholderImage = drawable;
        this.mPlaceholderImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setPlaceholderImage(int i, ScalingUtils.ScaleType scaleType) {
        this.mPlaceholderImage = this.mResources.getDrawable(i);
        this.mPlaceholderImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setRetryImage(Drawable drawable) {
        this.mRetryImage = drawable;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setRetryImage(int i) {
        this.mRetryImage = this.mResources.getDrawable(i);
        return this;
    }

    public Drawable getRetryImage() {
        return this.mRetryImage;
    }

    public DCGenericDraweeHierarchyBuilder setRetryImageScaleType(ScalingUtils.ScaleType scaleType) {
        this.mRetryImageScaleType = scaleType;
        return this;
    }

    public ScalingUtils.ScaleType getRetryImageScaleType() {
        return this.mRetryImageScaleType;
    }

    public DCGenericDraweeHierarchyBuilder setRetryImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        this.mRetryImage = drawable;
        this.mRetryImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setRetryImage(int i, ScalingUtils.ScaleType scaleType) {
        this.mRetryImage = this.mResources.getDrawable(i);
        this.mRetryImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setFailureImage(Drawable drawable) {
        this.mFailureImage = drawable;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setFailureImage(int i) {
        this.mFailureImage = this.mResources.getDrawable(i);
        return this;
    }

    public Drawable getFailureImage() {
        return this.mFailureImage;
    }

    public DCGenericDraweeHierarchyBuilder setFailureImageScaleType(ScalingUtils.ScaleType scaleType) {
        this.mFailureImageScaleType = scaleType;
        return this;
    }

    public ScalingUtils.ScaleType getFailureImageScaleType() {
        return this.mFailureImageScaleType;
    }

    public DCGenericDraweeHierarchyBuilder setFailureImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        this.mFailureImage = drawable;
        this.mFailureImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setFailureImage(int i, ScalingUtils.ScaleType scaleType) {
        this.mFailureImage = this.mResources.getDrawable(i);
        this.mFailureImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setProgressBarImage(Drawable drawable) {
        this.mProgressBarImage = drawable;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setProgressBarImage(int i) {
        this.mProgressBarImage = this.mResources.getDrawable(i);
        return this;
    }

    public Drawable getProgressBarImage() {
        return this.mProgressBarImage;
    }

    public DCGenericDraweeHierarchyBuilder setProgressBarImageScaleType(ScalingUtils.ScaleType scaleType) {
        this.mProgressBarImageScaleType = scaleType;
        return this;
    }

    public ScalingUtils.ScaleType getProgressBarImageScaleType() {
        return this.mProgressBarImageScaleType;
    }

    public DCGenericDraweeHierarchyBuilder setProgressBarImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        this.mProgressBarImage = drawable;
        this.mProgressBarImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setProgressBarImage(int i, ScalingUtils.ScaleType scaleType) {
        this.mProgressBarImage = this.mResources.getDrawable(i);
        this.mProgressBarImageScaleType = scaleType;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setActualImageScaleType(ScalingUtils.ScaleType scaleType) {
        this.mActualImageScaleType = scaleType;
        this.mActualImageMatrix = null;
        return this;
    }

    public ScalingUtils.ScaleType getActualImageScaleType() {
        return this.mActualImageScaleType;
    }

    public DCGenericDraweeHierarchyBuilder setActualImageFocusPoint(PointF pointF) {
        this.mActualImageFocusPoint = pointF;
        return this;
    }

    public PointF getActualImageFocusPoint() {
        return this.mActualImageFocusPoint;
    }

    public DCGenericDraweeHierarchyBuilder setActualImageColorFilter(ColorFilter colorFilter) {
        this.mActualImageColorFilter = colorFilter;
        return this;
    }

    public ColorFilter getActualImageColorFilter() {
        return this.mActualImageColorFilter;
    }

    public DCGenericDraweeHierarchyBuilder setBackground(Drawable drawable) {
        this.mBackground = drawable;
        return this;
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public DCGenericDraweeHierarchyBuilder setOverlays(List<Drawable> list) {
        this.mOverlays = list;
        return this;
    }

    public DCGenericDraweeHierarchyBuilder setOverlay(Drawable drawable) {
        if (drawable == null) {
            this.mOverlays = null;
        } else {
            this.mOverlays = Arrays.asList(new Drawable[]{drawable});
        }
        return this;
    }

    public List<Drawable> getOverlays() {
        return this.mOverlays;
    }

    public DCGenericDraweeHierarchyBuilder setPressedStateOverlay(Drawable drawable) {
        if (drawable == null) {
            this.mPressedStateOverlay = null;
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{16842919}, drawable);
            this.mPressedStateOverlay = stateListDrawable;
        }
        return this;
    }

    public Drawable getPressedStateOverlay() {
        return this.mPressedStateOverlay;
    }

    public DCGenericDraweeHierarchyBuilder setRoundingParams(RoundingParams roundingParams) {
        this.mRoundingParams = roundingParams;
        return this;
    }

    public RoundingParams getRoundingParams() {
        return this.mRoundingParams;
    }

    private void validate() {
        List<Drawable> list = this.mOverlays;
        if (list != null) {
            for (Drawable checkNotNull : list) {
                Preconditions.checkNotNull(checkNotNull);
            }
        }
    }

    public DCGenericDraweeHierarchy build() {
        validate();
        return new DCGenericDraweeHierarchy(this);
    }
}
