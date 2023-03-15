package com.taobao.weex.font;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FontAdapter {
    private List<FontListener> mFontListener = new CopyOnWriteArrayList();

    public void addFontListener(FontListener fontListener) {
        this.mFontListener.add(fontListener);
    }

    public void removeFontListener(FontListener fontListener) {
        this.mFontListener.remove(fontListener);
    }

    public void onAddFontRule(String str, String str2, String str3) {
        synchronized (this) {
            for (FontListener onAddFontRule : this.mFontListener) {
                onAddFontRule.onAddFontRule(str, str2, str3);
            }
        }
    }

    public void onFontLoad(String str, String str2, String str3) {
        synchronized (this) {
            for (FontListener onFontLoad : this.mFontListener) {
                onFontLoad.onFontLoad(str, str2, str3);
            }
        }
    }
}
