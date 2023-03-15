package io.dcloud.feature.uniapp.ui.shadow;

import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.List;

public class UniBoxShadowOptions {
    private static final String TAG = "UniBoxShadowOptions";
    public float blur = 0.0f;
    public int color = -16777216;
    public float hShadow;
    public boolean isInset = false;
    public List<IParser> optionParamParsers;
    public float[] radii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public float spread = 0.0f;
    public PointF topLeft = null;
    public float vShadow;
    public int viewHeight = 0;
    public int viewWidth = 0;
    /* access modifiers changed from: private */
    public float viewport = 750.0f;

    public interface IParser {
        void parse(String str);
    }

    public UniBoxShadowOptions(float f) {
        if (750.0f != 0.0f) {
            this.viewport = f;
        }
        this.optionParamParsers = new ArrayList();
        AnonymousClass1 r4 = new IParser() {
            public void parse(String str) {
                if (!TextUtils.isEmpty(str)) {
                    float floatValue = WXUtils.getFloat(str, Float.valueOf(0.0f)).floatValue();
                    UniBoxShadowOptions uniBoxShadowOptions = UniBoxShadowOptions.this;
                    uniBoxShadowOptions.spread = WXViewUtils.getRealSubPxByWidth(floatValue, uniBoxShadowOptions.viewport);
                    WXLogUtils.w(UniBoxShadowOptions.TAG, "Experimental box-shadow attribute: spread");
                }
            }
        };
        this.optionParamParsers.add(new IParser() {
            public void parse(String str) {
                if (!TextUtils.isEmpty(str)) {
                    float floatValue = WXUtils.getFloat(str, Float.valueOf(0.0f)).floatValue();
                    UniBoxShadowOptions uniBoxShadowOptions = UniBoxShadowOptions.this;
                    uniBoxShadowOptions.blur = WXViewUtils.getRealSubPxByWidth(floatValue, uniBoxShadowOptions.viewport);
                }
            }
        });
        this.optionParamParsers.add(r4);
    }

    public UniBoxShadowOptions scale(float f) {
        if (f <= 0.0f || f > 1.0f) {
            return null;
        }
        UniBoxShadowOptions uniBoxShadowOptions = new UniBoxShadowOptions(this.viewport);
        uniBoxShadowOptions.hShadow = this.hShadow * f;
        uniBoxShadowOptions.vShadow = this.vShadow * f;
        uniBoxShadowOptions.blur = this.blur * f;
        uniBoxShadowOptions.spread = this.spread * f;
        int i = 0;
        while (true) {
            float[] fArr = this.radii;
            if (i >= fArr.length) {
                break;
            }
            uniBoxShadowOptions.radii[i] = fArr[i] * f;
            i++;
        }
        uniBoxShadowOptions.viewHeight = (int) (((float) this.viewHeight) * f);
        uniBoxShadowOptions.viewWidth = (int) (((float) this.viewWidth) * f);
        if (this.topLeft != null) {
            PointF pointF = new PointF();
            uniBoxShadowOptions.topLeft = pointF;
            pointF.x = this.topLeft.x * f;
            uniBoxShadowOptions.topLeft.y = this.topLeft.y * f;
        }
        uniBoxShadowOptions.color = this.color;
        uniBoxShadowOptions.isInset = this.isInset;
        WXLogUtils.d(TAG, "Scaled BoxShadowOptions: [" + f + "] " + uniBoxShadowOptions);
        return uniBoxShadowOptions;
    }

    public Rect getTargetCanvasRect() {
        return new Rect(0, 0, this.viewWidth + (((int) (this.blur + this.spread + Math.abs(this.hShadow))) * 2), this.viewHeight + (((int) (this.blur + this.spread + Math.abs(this.vShadow))) * 2));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("BoxShadowOptions{");
        stringBuffer.append("h-shadow=");
        stringBuffer.append(this.hShadow);
        stringBuffer.append(", v-shadow=");
        stringBuffer.append(this.vShadow);
        stringBuffer.append(", blur=");
        stringBuffer.append(this.blur);
        stringBuffer.append(", spread=");
        stringBuffer.append(this.spread);
        stringBuffer.append(", corner-radius=");
        stringBuffer.append(Operators.ARRAY_START_STR + this.radii[0] + "," + this.radii[2] + "," + this.radii[4] + "," + this.radii[6] + Operators.ARRAY_END_STR);
        stringBuffer.append(", color=#");
        stringBuffer.append(Integer.toHexString(this.color));
        stringBuffer.append(", inset=");
        stringBuffer.append(this.isInset);
        stringBuffer.append(Operators.BLOCK_END);
        return stringBuffer.toString();
    }
}
