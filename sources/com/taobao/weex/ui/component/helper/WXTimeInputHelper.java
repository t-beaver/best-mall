package com.taobao.weex.ui.component.helper;

import android.widget.TextView;
import com.taobao.weex.appfram.pickers.DatePickerImpl;
import com.taobao.weex.ui.component.AbstractEditComponent;
import java.util.Map;

public class WXTimeInputHelper {
    public static void pickDate(String str, String str2, final AbstractEditComponent abstractEditComponent) {
        final TextView textView = (TextView) abstractEditComponent.getHostView();
        DatePickerImpl.pickDate(textView.getContext(), textView.getText().toString(), str, str2, new DatePickerImpl.OnPickListener() {
            public void onPick(boolean z, String str) {
                if (z) {
                    textView.setText(str);
                    abstractEditComponent.performOnChange(str);
                }
            }
        }, (Map<String, Object>) null);
    }

    public static void pickTime(final AbstractEditComponent abstractEditComponent) {
        final TextView textView = (TextView) abstractEditComponent.getHostView();
        DatePickerImpl.pickTime(textView.getContext(), textView.getText().toString(), new DatePickerImpl.OnPickListener() {
            public void onPick(boolean z, String str) {
                if (z) {
                    textView.setText(str);
                    abstractEditComponent.performOnChange(str);
                }
            }
        }, (Map<String, Object>) null);
    }
}
