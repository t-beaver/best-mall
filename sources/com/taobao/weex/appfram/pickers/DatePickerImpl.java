package com.taobao.weex.appfram.pickers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.module.WXModalUIModule;
import com.taobao.weex.utils.WXLogUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DatePickerImpl {
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_YEAR = 1900;
    private static SimpleDateFormat dateFormatter;
    private static SimpleDateFormat timeFormatter;

    public interface OnPickListener {
        void onPick(boolean z, String str);
    }

    public static void pickDate(Context context, String str, String str2, String str3, final OnPickListener onPickListener, Map<String, Object> map) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(parseDate(str));
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                String str;
                String str2;
                int i4 = i2 + 1;
                if (i4 < 10) {
                    str = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + i4;
                } else {
                    str = String.valueOf(i4);
                }
                if (i3 < 10) {
                    str2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + i3;
                } else {
                    str2 = String.valueOf(i3);
                }
                OnPickListener.this.onPick(true, i + Operators.SUB + str + Operators.SUB + str2);
            }
        }, instance.get(1), instance.get(2), instance.get(5));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        Calendar instance2 = Calendar.getInstance(Locale.getDefault());
        Calendar instance3 = Calendar.getInstance(Locale.getDefault());
        instance2.set(DEFAULT_START_YEAR, 0, 1);
        instance3.set(DEFAULT_END_YEAR, 11, 31);
        if (!TextUtils.isEmpty(str3)) {
            if (datePicker.getMaxDate() >= parseDate(str3).getTime()) {
                datePicker.setMinDate(parseDate(str3).getTime());
            } else {
                datePicker.setMinDate(instance2.getTimeInMillis());
                datePicker.setMaxDate(instance3.getTimeInMillis());
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            if (datePicker.getMinDate() <= parseDate(str2).getTime()) {
                datePicker.setMaxDate(parseDate(str2).getTime());
            } else {
                datePicker.setMinDate(instance2.getTimeInMillis());
                datePicker.setMaxDate(instance3.getTimeInMillis());
            }
        }
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                OnPickListener.this.onPick(false, (String) null);
            }
        });
        Object obj = null;
        setButtonText(datePickerDialog, -2, String.valueOf(map != null ? map.get(WXModalUIModule.CANCEL_TITLE) : null));
        if (map != null) {
            obj = map.get("confirmTitle");
        }
        setButtonText(datePickerDialog, -1, String.valueOf(obj));
        datePickerDialog.show();
    }

    public static void pickTime(Context context, String str, final OnPickListener onPickListener, Map<String, Object> map) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(parseTime(str));
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                String str;
                String str2;
                if (i < 10) {
                    str = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + i;
                } else {
                    str = String.valueOf(i);
                }
                if (i2 < 10) {
                    str2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + i2;
                } else {
                    str2 = String.valueOf(i2);
                }
                OnPickListener.this.onPick(true, str + ":" + str2);
            }
        }, instance.get(11), instance.get(12), false);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                OnPickListener.this.onPick(false, (String) null);
            }
        });
        Object obj = null;
        setButtonText(timePickerDialog, -2, String.valueOf(map != null ? map.get(WXModalUIModule.CANCEL_TITLE) : null));
        if (map != null) {
            obj = map.get("confirmTitle");
        }
        setButtonText(timePickerDialog, -1, String.valueOf(obj));
        timePickerDialog.show();
    }

    private static Date parseDate(String str) {
        if (dateFormatter == null) {
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        try {
            return dateFormatter.parse(str);
        } catch (ParseException e) {
            WXLogUtils.w("[DatePickerImpl] " + e.toString());
            return new Date();
        }
    }

    private static Date parseTime(String str) {
        if (timeFormatter == null) {
            timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        try {
            return timeFormatter.parse(str);
        } catch (ParseException e) {
            WXLogUtils.w("[DatePickerImpl] " + e.toString());
            return new Date();
        }
    }

    private static void setButtonText(final AlertDialog alertDialog, final int i, final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence) && !"null".equals(charSequence)) {
            try {
                alertDialog.getWindow().getDecorView().post(WXThread.secure((Runnable) new Runnable() {
                    public void run() {
                        Button button = alertDialog.getButton(i);
                        if (button != null) {
                            button.setAllCaps(false);
                            button.setText(charSequence);
                        }
                    }
                }));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
