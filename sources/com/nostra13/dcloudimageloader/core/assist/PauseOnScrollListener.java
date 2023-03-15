package com.nostra13.dcloudimageloader.core.assist;

import android.widget.AbsListView;
import com.nostra13.dcloudimageloader.core.ImageLoader;

public class PauseOnScrollListener implements AbsListView.OnScrollListener {
    private final AbsListView.OnScrollListener externalListener;
    private ImageLoader imageLoader;
    private final boolean pauseOnFling;
    private final boolean pauseOnScroll;

    public PauseOnScrollListener(ImageLoader imageLoader2, boolean z, boolean z2) {
        this(imageLoader2, z, z2, (AbsListView.OnScrollListener) null);
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        AbsListView.OnScrollListener onScrollListener = this.externalListener;
        if (onScrollListener != null) {
            onScrollListener.onScroll(absListView, i, i2, i3);
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 0) {
            this.imageLoader.resume();
        } else if (i != 1) {
            if (i == 2 && this.pauseOnFling) {
                this.imageLoader.pause();
            }
        } else if (this.pauseOnScroll) {
            this.imageLoader.pause();
        }
        AbsListView.OnScrollListener onScrollListener = this.externalListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(absListView, i);
        }
    }

    public PauseOnScrollListener(ImageLoader imageLoader2, boolean z, boolean z2, AbsListView.OnScrollListener onScrollListener) {
        this.imageLoader = imageLoader2;
        this.pauseOnScroll = z;
        this.pauseOnFling = z2;
        this.externalListener = onScrollListener;
    }
}
