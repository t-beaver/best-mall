package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import io.dcloud.common.DHInterface.IContainerView;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.ITypeofAble;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AdaContainerFrameItem extends AdaFrameItem implements IContainerView {
    private boolean isITypeofAble;
    public ArrayList<AdaFrameItem> mChildArrayList;
    public ArrayList<INativeView> mChildNativeViewList;

    protected AdaContainerFrameItem(Context context) {
        super(context);
        this.isITypeofAble = false;
        this.mChildArrayList = null;
        this.mChildNativeViewList = null;
        this.mChildArrayList = new ArrayList<>(1);
    }

    public void addFrameItem(AdaFrameItem adaFrameItem, ViewGroup.LayoutParams layoutParams) {
        addFrameItem(adaFrameItem, -1, layoutParams);
    }

    public void addNativeViewChild(INativeView iNativeView) {
        if (this.mChildNativeViewList == null) {
            this.mChildNativeViewList = new ArrayList<>();
        }
        if (iNativeView != null && !this.mChildNativeViewList.contains(iNativeView)) {
            this.mChildNativeViewList.add(iNativeView);
        }
    }

    public boolean checkITypeofAble() {
        boolean z = this.isITypeofAble;
        if (z) {
            return z;
        }
        Iterator<AdaFrameItem> it = this.mChildArrayList.iterator();
        while (it.hasNext()) {
            AdaFrameItem next = it.next();
            if (next instanceof AdaContainerFrameItem) {
                return ((AdaContainerFrameItem) next).checkITypeofAble();
            }
        }
        return false;
    }

    public void clearView() {
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().dispose();
            }
            this.mChildArrayList.clear();
        }
        View view = this.mViewImpl;
        if ((view instanceof ViewGroup) && view != null) {
            ((ViewGroup) view).removeAllViews();
        }
        this.isITypeofAble = false;
    }

    public void dispose() {
        clearView();
        super.dispose();
    }

    public ArrayList<AdaFrameItem> getChilds() {
        return this.mChildArrayList;
    }

    public ViewGroup obtainMainViewGroup() {
        return (ViewGroup) this.mViewImpl;
    }

    public boolean onDispose() {
        boolean onDispose = super.onDispose();
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                onDispose |= it.next().onDispose();
            }
        }
        return onDispose;
    }

    public void onPopFromStack(boolean z) {
        super.onPopFromStack(z);
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onPopFromStack(z);
            }
        }
    }

    public void onPushToStack(boolean z) {
        super.onPushToStack(z);
        ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
        if (arrayList != null) {
            Iterator<AdaFrameItem> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onPushToStack(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResize() {
        super.onResize();
        Iterator<AdaFrameItem> it = this.mChildArrayList.iterator();
        while (it.hasNext()) {
            it.next().onResize();
        }
    }

    public void removeAllFrameItem() {
        if (this.mViewImpl != null) {
            clearView();
            ((ViewGroup) this.mViewImpl).removeAllViews();
        }
        this.isITypeofAble = false;
    }

    public void removeFrameItem(AdaFrameItem adaFrameItem) {
        if (adaFrameItem instanceof ITypeofAble) {
            this.isITypeofAble = false;
        }
        View view = this.mViewImpl;
        if (view != null) {
            ((ViewGroup) view).removeView(adaFrameItem.obtainMainView());
            this.mChildArrayList.remove(adaFrameItem);
        }
    }

    public void removeNativeViewChild(INativeView iNativeView) {
        ArrayList<INativeView> arrayList = this.mChildNativeViewList;
        if (arrayList != null && arrayList.contains(iNativeView)) {
            this.mChildNativeViewList.remove(iNativeView);
        }
    }

    public void sortNativeViewBringToFront() {
        ArrayList<INativeView> arrayList = this.mChildNativeViewList;
        if (arrayList != null) {
            Iterator<INativeView> it = arrayList.iterator();
            while (it.hasNext()) {
                INativeView next = it.next();
                if (!(next.obtanMainView() == null || next.obtanMainView().getParent() == null)) {
                    next.obtanMainView().bringToFront();
                }
            }
        }
    }

    public void addFrameItem(AdaFrameItem adaFrameItem) {
        addFrameItem(adaFrameItem, -1);
    }

    public void addFrameItem(AdaFrameItem adaFrameItem, int i, ViewGroup.LayoutParams layoutParams) {
        if (adaFrameItem instanceof ITypeofAble) {
            this.isITypeofAble = true;
        }
        if (this.mViewImpl instanceof ViewGroup) {
            View obtainMainView = adaFrameItem.obtainMainView();
            ViewParent parent = obtainMainView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(obtainMainView);
                this.mChildArrayList.remove(adaFrameItem);
            }
            if (((ViewGroup) this.mViewImpl).getChildCount() < i) {
                i = ((ViewGroup) this.mViewImpl).getChildCount();
            }
            ((ViewGroup) this.mViewImpl).addView(obtainMainView, i, layoutParams);
            if (i < 0 || i > this.mChildArrayList.size()) {
                i = this.mChildArrayList.size();
            }
            this.mChildArrayList.add(i, adaFrameItem);
        }
    }

    public void removeAllFrameItem(boolean z) {
        if (z) {
            View view = this.mViewImpl;
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View childAt = viewGroup.getChildAt(i);
                    if (!(childAt instanceof INativeView)) {
                        viewGroup.removeView(childAt);
                    }
                }
                ArrayList<AdaFrameItem> arrayList = this.mChildArrayList;
                if (arrayList != null) {
                    Iterator<AdaFrameItem> it = arrayList.iterator();
                    while (it.hasNext()) {
                        it.next().dispose();
                    }
                    this.mChildArrayList.clear();
                    return;
                }
                return;
            }
            return;
        }
        removeAllFrameItem();
    }

    public void addFrameItem(AdaFrameItem adaFrameItem, int i) {
        ViewGroup.LayoutParams layoutParams = adaFrameItem.obtainMainView().getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-1, -1);
        }
        addFrameItem(adaFrameItem, i, layoutParams);
    }
}
