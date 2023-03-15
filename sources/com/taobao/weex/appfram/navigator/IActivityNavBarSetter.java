package com.taobao.weex.appfram.navigator;

public interface IActivityNavBarSetter {
    boolean clearNavBarLeftItem(String str);

    boolean clearNavBarMoreItem(String str);

    boolean clearNavBarRightItem(String str);

    boolean pop(String str);

    boolean push(String str);

    boolean setNavBarLeftItem(String str);

    boolean setNavBarMoreItem(String str);

    boolean setNavBarRightItem(String str);

    boolean setNavBarTitle(String str);
}
