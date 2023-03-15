package com.taobao.weex.adapter;

import android.widget.ImageView;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

public interface IWXImgLoaderAdapter {
    void setImage(String str, ImageView imageView, WXImageQuality wXImageQuality, WXImageStrategy wXImageStrategy);
}
