package io.dcloud.feature.barcode2.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.feature.barcode2.view.DetectorViewConfig;
import java.util.regex.Pattern;

final class CameraConfigurationManager {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final int DESIRED_SHARPNESS = 30;
    private static final String TAG = "CameraConfigurationManager";
    private static final int TEN_DESIRED_ZOOM = 27;
    private Point cameraResolution;
    private final Context context;
    private Camera.Parameters parameters;
    private int previewFormat;
    private String previewFormatString;

    CameraConfigurationManager(Context context2) {
        this.context = context2;
    }

    private static int findBestMotZoomValue(CharSequence charSequence, int i) {
        String[] split = COMMA_PATTERN.split(charSequence);
        int length = split.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            try {
                double parseDouble = Double.parseDouble(split[i2].trim());
                int i4 = (int) (10.0d * parseDouble);
                double d = (double) i;
                Double.isNaN(d);
                if (Math.abs(d - parseDouble) < ((double) Math.abs(i - i3))) {
                    i3 = i4;
                }
                i2++;
            } catch (NumberFormatException unused) {
                return i;
            }
        }
        return i3;
    }

    private static Point findBestPreviewSizeValue(CharSequence charSequence, Point point) {
        String[] split = COMMA_PATTERN.split(charSequence);
        int length = split.length;
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            String trim = split[i2].trim();
            int indexOf = trim.indexOf(120);
            if (indexOf < 0) {
                String str = TAG;
                Log.w(str, "Bad preview-size: " + trim);
            } else {
                try {
                    int parseInt = Integer.parseInt(trim.substring(0, indexOf));
                    int parseInt2 = Integer.parseInt(trim.substring(indexOf + 1));
                    if (parseInt >= parseInt2) {
                        int abs = Math.abs(parseInt - point.x) + Math.abs(parseInt2 - point.y);
                        if (abs == 0) {
                            i4 = parseInt2;
                            i3 = parseInt;
                            break;
                        } else if (abs < i) {
                            i4 = parseInt2;
                            i = abs;
                            i3 = parseInt;
                        }
                    } else {
                        continue;
                    }
                } catch (NumberFormatException unused) {
                    String str2 = TAG;
                    Log.w(str2, "Bad preview-size: " + trim);
                }
            }
            i2++;
        }
        if (i3 <= 0 || i4 <= 0) {
            return null;
        }
        return new Point(i3, i4);
    }

    private static Point findBestPreviewSizeValue0(CharSequence charSequence, Point point) {
        String str;
        String[] split = COMMA_PATTERN.split(charSequence);
        int i = 0;
        int i2 = 0;
        char c = 0;
        int i3 = 0;
        while (true) {
            if (i >= split.length) {
                break;
            }
            if (c == 0 || c == 1) {
                str = split[i];
            } else {
                str = split[(split.length - 1) - i];
            }
            String trim = str.trim();
            int indexOf = trim.indexOf(120);
            if (indexOf < 0) {
                String str2 = TAG;
                Log.w(str2, "Bad preview-size: " + trim);
            } else {
                try {
                    int parseInt = Integer.parseInt(trim.substring(0, indexOf));
                    i3 = Integer.parseInt(trim.substring(indexOf + 1));
                    if (c != 0) {
                        if (i2 > 0 && parseInt <= point.y && i3 <= point.x) {
                            i2 = parseInt;
                            break;
                        }
                    } else if (i2 != 0) {
                        i = -1;
                        if (parseInt > i2) {
                            i2 = 0;
                            c = 65535;
                        } else {
                            i2 = 0;
                            c = 1;
                        }
                        i3 = 0;
                    }
                    i2 = parseInt;
                } catch (NumberFormatException unused) {
                    String str3 = TAG;
                    Log.w(str3, "Bad preview-size: " + trim);
                }
            }
            i++;
        }
        if (i2 <= 0 || i3 <= 0) {
            return null;
        }
        return new Point(i2, i3);
    }

    private void setFlash(Camera.Parameters parameters2) {
        if (!Build.MODEL.contains("Behold II") || CameraManager.SDK_INT != 3) {
            parameters2.set("flash-value", 2);
        } else {
            parameters2.set("flash-value", 1);
        }
        parameters2.set("flash-mode", "off");
    }

    private void setSharpness(Camera.Parameters parameters2) {
        String str = parameters2.get("sharpness-max");
        int i = 30;
        if (str != null) {
            try {
                int parseInt = Integer.parseInt(str);
                if (30 > parseInt) {
                    i = parseInt;
                }
            } catch (NumberFormatException unused) {
                String str2 = TAG;
                Log.w(str2, "Bad sharpness-max: " + str);
            }
        }
        parameters2.set("sharpness", i);
    }

    private void setZoom(Camera.Parameters parameters2) {
        String str = parameters2.get("zoom-supported");
        if (str == null || Boolean.parseBoolean(str)) {
            int i = 27;
            String str2 = parameters2.get("max-zoom");
            if (str2 != null) {
                try {
                    int parseDouble = (int) (Double.parseDouble(str2) * 10.0d);
                    if (27 > parseDouble) {
                        i = parseDouble;
                    }
                } catch (NumberFormatException unused) {
                    String str3 = TAG;
                    Log.w(str3, "Bad max-zoom: " + str2);
                }
            }
            String str4 = parameters2.get("taking-picture-zoom-max");
            if (str4 != null) {
                try {
                    int parseInt = Integer.parseInt(str4);
                    if (i > parseInt) {
                        i = parseInt;
                    }
                } catch (NumberFormatException unused2) {
                    String str5 = TAG;
                    Log.w(str5, "Bad taking-picture-zoom-max: " + str4);
                }
            }
            String str6 = parameters2.get("mot-zoom-values");
            if (str6 != null) {
                i = findBestMotZoomValue(str6, i);
            }
            String str7 = parameters2.get("mot-zoom-step");
            if (str7 != null) {
                try {
                    int parseDouble2 = (int) (Double.parseDouble(str7.trim()) * 10.0d);
                    if (parseDouble2 > 1) {
                        i -= i % parseDouble2;
                    }
                } catch (NumberFormatException unused3) {
                }
            }
            if (!(str2 == null && str6 == null)) {
                double d = (double) i;
                Double.isNaN(d);
                parameters2.set("zoom", String.valueOf(d / 10.0d));
            }
            if (str4 != null) {
                parameters2.set("taking-picture-zoom", i);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Point getCameraResolution() {
        return this.cameraResolution;
    }

    /* access modifiers changed from: package-private */
    public int getPreviewFormat() {
        return this.previewFormat;
    }

    /* access modifiers changed from: package-private */
    public String getPreviewFormatString() {
        return this.previewFormatString;
    }

    /* access modifiers changed from: package-private */
    public void initFromCameraParameters(Camera camera) {
        if (this.parameters == null) {
            this.parameters = camera.getParameters();
        }
        this.previewFormat = this.parameters.getPreviewFormat();
        this.previewFormatString = this.parameters.get("preview-format");
        String str = TAG;
        Log.d(str, "Default preview format: " + this.previewFormat + '/' + this.previewFormatString);
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        int width = rect.width();
        int height = rect.height();
        boolean z = true;
        if (this.context.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        if (z) {
            this.cameraResolution = getCameraResolution(this.parameters, new Point(height, width));
        } else {
            this.cameraResolution = getCameraResolution(this.parameters, new Point(width, height));
        }
    }

    /* access modifiers changed from: package-private */
    public void setDesiredCameraParameters(Camera camera) {
        if (this.parameters == null) {
            this.parameters = camera.getParameters();
        }
        String str = TAG;
        Log.d(str, "Setting preview size: " + this.cameraResolution);
        Camera.Parameters parameters2 = this.parameters;
        Point point = this.cameraResolution;
        parameters2.setPreviewSize(point.x, point.y);
        setFlash(this.parameters);
        setZoom(this.parameters);
        try {
            camera.setParameters(this.parameters);
        } catch (Exception unused) {
        }
    }

    public static Point getCameraResolution(Camera.Parameters parameters2, Point point) {
        String str = parameters2.get("preview-size-values");
        if (str == null) {
            str = parameters2.get("preview-size-value");
        }
        Point point2 = null;
        if (str != null) {
            String str2 = TAG;
            Logger.i(str2, "preview-size-values parameter: " + str);
            point2 = findBestPreviewSizeValue(str, point);
        }
        return point2 == null ? new Point((point.x >> 3) << 3, (point.y >> 3) << 3) : point2;
    }
}
