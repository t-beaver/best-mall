package io.dcloud.feature.weex.adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.widget.ImageView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.utils.WXDomUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.feature.weex.adapter.Fresco.DCGenericDraweeHierarchy;
import java.util.Map;

public class FrescoImageComponent extends WXImage {
    /* access modifiers changed from: private */
    public int mBitmapHeight = 0;
    /* access modifiers changed from: private */
    public int mBitmapWidth = 0;
    /* access modifiers changed from: private */
    public String mResizeMode = "scaleToFill";

    public FrescoImageComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, final BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void layoutBefore() {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                boolean containsKey = basicComponentData.getStyles().containsKey(Constants.Name.FLEX);
                this.mMeasureExactly = false;
                if (i == 0) {
                    if (FrescoImageComponent.this.mResizeMode.equals("heightFix") && FrescoImageComponent.this.mBitmapHeight > 0 && FrescoImageComponent.this.mBitmapWidth > 0 && !Float.isNaN(f2)) {
                        this.mMeasureWidth = ((float) FrescoImageComponent.this.mBitmapWidth) * (f2 / ((float) FrescoImageComponent.this.mBitmapHeight));
                        this.mMeasureExactly = true;
                    } else if (!containsKey) {
                        this.mMeasureWidth = (float) ((int) WXViewUtils.getRealPxByWidth(320.0f, FrescoImageComponent.this.getInstance().getInstanceViewPortWidthWithFloat()));
                    }
                }
                if (i2 != 0) {
                    return;
                }
                if (FrescoImageComponent.this.mResizeMode.equals("widthFix") && FrescoImageComponent.this.mBitmapHeight > 0 && FrescoImageComponent.this.mBitmapWidth > 0 && !Float.isNaN(f)) {
                    this.mMeasureHeight = ((float) FrescoImageComponent.this.mBitmapHeight) * (f / ((float) FrescoImageComponent.this.mBitmapWidth));
                    this.mMeasureExactly = true;
                } else if (!containsKey) {
                    this.mMeasureHeight = (float) ((int) WXViewUtils.getRealPxByWidth(240.0f, FrescoImageComponent.this.getInstance().getInstanceViewPortWidthWithFloat()));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public ImageView initComponentHostView(Context context) {
        FrescoImageView frescoImageView = new FrescoImageView(context);
        ((DCGenericDraweeHierarchy) frescoImageView.getHierarchy()).setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        return frescoImageView;
    }

    @WXComponentProp(name = "fadeShow")
    public void setFadeAnim(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null) {
            ((FrescoImageView) getHostView()).setFadeShow(Boolean.valueOf(str).booleanValue());
        }
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -934437708:
                if (str.equals("resize")) {
                    c = 0;
                    break;
                }
                break;
            case 3357091:
                if (str.equals("mode")) {
                    c = 1;
                    break;
                }
                break;
            case 2049757303:
                if (str.equals(Constants.Name.RESIZE_MODE)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setResizeMode(string);
                }
                return true;
            case 1:
                String string2 = WXUtils.getString(obj, (String) null);
                if (string2 != null) {
                    setResizeMode(string2);
                }
                return true;
            case 2:
                String string3 = WXUtils.getString(obj, (String) null);
                if (string3 != null) {
                    setResizeMode(string3);
                }
                return true;
            default:
                return super.setProperty(str, obj);
        }
    }

    public void setResizeMode(String str) {
        FrescoImageView frescoImageView = (FrescoImageView) getHostView();
        ScalingUtils.ScaleType scaleType = ScalingUtils.ScaleType.FIT_XY;
        this.mResizeMode = str;
        if (!TextUtils.isEmpty(str)) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1881872635:
                    if (str.equals("stretch")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1687059567:
                    if (str.equals("top right")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1614504594:
                    if (str.equals("heightFix")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1387149201:
                    if (str.equals("widthFix")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1383228885:
                    if (str.equals("bottom")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1364013995:
                    if (str.equals("center")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1362001767:
                    if (str.equals("aspectFit")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1024435214:
                    if (str.equals("top left")) {
                        c = 7;
                        break;
                    }
                    break;
                case -797304696:
                    if (str.equals("scaleToFill")) {
                        c = 8;
                        break;
                    }
                    break;
                case -667379492:
                    if (str.equals("bottom left")) {
                        c = 9;
                        break;
                    }
                    break;
                case 115029:
                    if (str.equals("top")) {
                        c = 10;
                        break;
                    }
                    break;
                case 3317767:
                    if (str.equals("left")) {
                        c = 11;
                        break;
                    }
                    break;
                case 94852023:
                    if (str.equals(IApp.ConfigProperty.CONFIG_COVER)) {
                        c = 12;
                        break;
                    }
                    break;
                case 108511772:
                    if (str.equals("right")) {
                        c = 13;
                        break;
                    }
                    break;
                case 727618043:
                    if (str.equals("aspectFill")) {
                        c = 14;
                        break;
                    }
                    break;
                case 791733223:
                    if (str.equals("bottom right")) {
                        c = 15;
                        break;
                    }
                    break;
                case 951526612:
                    if (str.equals("contain")) {
                        c = 16;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    scaleType = ScalingUtils.ScaleType.FIT_XY;
                    break;
                case 1:
                    scaleType = new CustomScaleType(1.0f, 0.0f);
                    break;
                case 2:
                case 3:
                    scaleType = ScalingUtils.ScaleType.FIT_CENTER;
                    break;
                case 4:
                    scaleType = new CustomScaleType(0.5f, 1.0f);
                    break;
                case 5:
                    scaleType = new CustomScaleType(0.5f, 0.5f);
                    break;
                case 6:
                    scaleType = ScalingUtils.ScaleType.FIT_CENTER;
                    break;
                case 7:
                    scaleType = new CustomScaleType(0.0f, 0.0f);
                    break;
                case 8:
                    scaleType = ScalingUtils.ScaleType.FIT_XY;
                    break;
                case 9:
                    scaleType = new CustomScaleType(0.0f, 1.0f);
                    break;
                case 10:
                    scaleType = new CustomScaleType(0.5f, 0.0f);
                    break;
                case 11:
                    scaleType = new CustomScaleType(0.0f, 0.5f);
                    break;
                case 12:
                    scaleType = ScalingUtils.ScaleType.CENTER_CROP;
                    break;
                case 13:
                    scaleType = new CustomScaleType(1.0f, 0.5f);
                    break;
                case 14:
                    scaleType = ScalingUtils.ScaleType.CENTER_CROP;
                    break;
                case 15:
                    scaleType = new CustomScaleType(1.0f, 1.0f);
                    break;
                case 16:
                    scaleType = ScalingUtils.ScaleType.FIT_CENTER;
                    break;
            }
        }
        ((DCGenericDraweeHierarchy) frescoImageView.getHierarchy()).setActualImageScaleType(scaleType);
    }

    public void onImageFinish(boolean z, Map map) {
        super.onImageFinish(z, map);
        if (map != null) {
            String str = this.mResizeMode;
            str.hashCode();
            if (str.equals("heightFix")) {
                this.mBitmapWidth = Integer.parseInt(map.get("width").toString());
                this.mBitmapHeight = Integer.parseInt(map.get("height").toString());
                float layoutHeight = ((float) this.mBitmapWidth) * (getLayoutHeight() / ((float) this.mBitmapHeight));
                if (getLayoutWidth() != layoutHeight) {
                    setStyleWidth(layoutHeight);
                }
            } else if (str.equals("widthFix")) {
                this.mBitmapWidth = Integer.parseInt(map.get("width").toString());
                this.mBitmapHeight = Integer.parseInt(map.get("height").toString());
                float layoutWidth = ((float) this.mBitmapHeight) * (getLayoutWidth() / ((float) this.mBitmapWidth));
                if (getLayoutHeight() != layoutWidth) {
                    setStyleHeight(layoutWidth);
                }
            }
        }
    }

    private void setStyleHeight(final float f) {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (FrescoImageComponent.this.getInstance() != null) {
                    WXBridgeManager.getInstance().setStyleHeight(FrescoImageComponent.this.getInstanceId(), FrescoImageComponent.this.getRef(), f);
                }
            }
        });
    }

    private void setStyleWidth(final float f) {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (FrescoImageComponent.this.getInstance() != null) {
                    WXBridgeManager.getInstance().setStyleWidth(FrescoImageComponent.this.getInstanceId(), FrescoImageComponent.this.getRef(), f);
                }
            }
        });
    }

    public class CustomScaleType implements ScalingUtils.ScaleType {
        private float dxf;
        private float dyf;

        public CustomScaleType(float f, float f2) {
            this.dxf = f;
            this.dyf = f2;
        }

        public Matrix getTransform(Matrix matrix, Rect rect, int i, int i2, float f, float f2) {
            float f3 = (float) i;
            float realPxByWidth = WXViewUtils.getRealPxByWidth(f3, FrescoImageComponent.this.getInstance().getInstanceViewPortWidthWithFloat()) / f3;
            matrix.setScale(realPxByWidth, realPxByWidth);
            matrix.postTranslate(((float) rect.left) + ((((float) rect.width()) - (f3 * realPxByWidth)) * this.dxf), ((float) rect.top) + ((((float) rect.height()) - (((float) i2) * realPxByWidth)) * this.dyf));
            return matrix;
        }
    }

    public void updateProperties(Map<String, Object> map) {
        if (getHostView() != null) {
            super.updateProperties(map);
            updateBorderRadius();
        }
    }

    private void updateBorderRadius() {
        BorderDrawable borderDrawable = WXViewUtils.getBorderDrawable(getHostView());
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadii(borderDrawable != null ? borderDrawable.getBorderInnerRadius(new RectF(0.0f, 0.0f, WXDomUtils.getContentWidth(this), WXDomUtils.getContentHeight(this))) : new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});
        ((DCGenericDraweeHierarchy) ((FrescoImageView) getHostView()).getHierarchy()).setRoundingParams(roundingParams);
    }
}
