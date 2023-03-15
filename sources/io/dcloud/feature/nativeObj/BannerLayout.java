package io.dcloud.feature.nativeObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.feature.nativeObj.data.NativeImageDataItem;
import io.dcloud.feature.nativeObj.photoview.BounceBackViewPager;
import io.dcloud.feature.nativeObj.photoview.subscaleview.SubsamplingScaleImageView;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BannerLayout extends RelativeLayout {
    public static final String CIRCULAR_INDICATOR = "default";
    public static final String NONE_INDICATOR = "none";
    public static final String WORD_INDICATOR = "number";
    public int MAX_VALUE;
    /* access modifiers changed from: private */
    public int WHAT_AUTO_PLAY;
    /* access modifiers changed from: private */
    public int autoPlayDuration;
    /* access modifiers changed from: private */
    public int currentPosition;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public ImageLoader imageLoader;
    private LinearLayout indicatorContainer;
    private int indicatorMargin;
    private Position indicatorPosition;
    private Shape indicatorShape;
    private int indicatorSpace;
    /* access modifiers changed from: private */
    public boolean isAllowImageDownload;
    /* access modifiers changed from: private */
    public boolean isAutoPlay;
    /* access modifiers changed from: private */
    public boolean isImageLoop;
    private boolean isImagePhoto;
    /* access modifiers changed from: private */
    public int itemCount;
    private String mIndicatorType;
    /* access modifiers changed from: private */
    public ArrayList<NativeImageDataItem> mUrls;
    /* access modifiers changed from: private */
    public OnBannerItemClickListener onBannerItemClickListener;
    /* access modifiers changed from: private */
    public ViewPager pager;
    private int ringIndicatorColor;
    private int scrollDuration;
    private Drawable selectedDrawable;
    private int selectedIndicatorColor;
    private int selectedIndicatorHeight;
    private int selectedIndicatorWidth;
    private Drawable unSelectedDrawable;
    private int unSelectedIndicatorColor;
    private int unSelectedIndicatorHeight;
    private int unSelectedIndicatorWidth;

    /* renamed from: io.dcloud.feature.nativeObj.BannerLayout$6  reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position;
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Shape;

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|(2:1|2)|3|5|6|7|8|9|10|11|12|13|14|(2:15|16)|17|19|20|21|22|24) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|5|6|7|8|9|10|11|12|13|14|15|16|17|19|20|21|22|24) */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0033 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0065 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0028 */
        static {
            /*
                io.dcloud.feature.nativeObj.BannerLayout$Position[] r0 = io.dcloud.feature.nativeObj.BannerLayout.Position.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position = r0
                r1 = 1
                io.dcloud.feature.nativeObj.BannerLayout$Position r2 = io.dcloud.feature.nativeObj.BannerLayout.Position.centerBottom     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.feature.nativeObj.BannerLayout$Position r3 = io.dcloud.feature.nativeObj.BannerLayout.Position.centerTop     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r2 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.feature.nativeObj.BannerLayout$Position r3 = io.dcloud.feature.nativeObj.BannerLayout.Position.leftBottom     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r4 = 3
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r2 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.feature.nativeObj.BannerLayout$Position r3 = io.dcloud.feature.nativeObj.BannerLayout.Position.leftTop     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4 = 4
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r2 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.feature.nativeObj.BannerLayout$Position r3 = io.dcloud.feature.nativeObj.BannerLayout.Position.rightBottom     // Catch:{ NoSuchFieldError -> 0x003e }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r4 = 5
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r2 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position     // Catch:{ NoSuchFieldError -> 0x0049 }
                io.dcloud.feature.nativeObj.BannerLayout$Position r3 = io.dcloud.feature.nativeObj.BannerLayout.Position.rightTop     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r4 = 6
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r2 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position     // Catch:{ NoSuchFieldError -> 0x0054 }
                io.dcloud.feature.nativeObj.BannerLayout$Position r3 = io.dcloud.feature.nativeObj.BannerLayout.Position.none     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r4 = 7
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                io.dcloud.feature.nativeObj.BannerLayout$Shape[] r2 = io.dcloud.feature.nativeObj.BannerLayout.Shape.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Shape = r2
                io.dcloud.feature.nativeObj.BannerLayout$Shape r3 = io.dcloud.feature.nativeObj.BannerLayout.Shape.rect     // Catch:{ NoSuchFieldError -> 0x0065 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0065 }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0065 }
            L_0x0065:
                int[] r1 = $SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Shape     // Catch:{ NoSuchFieldError -> 0x006f }
                io.dcloud.feature.nativeObj.BannerLayout$Shape r2 = io.dcloud.feature.nativeObj.BannerLayout.Shape.oval     // Catch:{ NoSuchFieldError -> 0x006f }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x006f }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x006f }
            L_0x006f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.BannerLayout.AnonymousClass6.<clinit>():void");
        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration;

        public FixedSpeedScroller(Context context) {
            super(context);
            this.mDuration = 1000;
        }

        public void startScroll(int i, int i2, int i3, int i4, int i5) {
            super.startScroll(i, i2, i3, i4, this.mDuration);
        }

        public void startScroll(int i, int i2, int i3, int i4) {
            super.startScroll(i, i2, i3, i4, this.mDuration);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
            this.mDuration = 1000;
        }

        public FixedSpeedScroller(BannerLayout bannerLayout, Context context, Interpolator interpolator, int i) {
            this(context, interpolator);
            this.mDuration = i;
        }
    }

    public interface ImageLoader extends Serializable {
        void displayImage(Context context, String str, View view, int i);
    }

    private class LoopPagerAdapter extends PagerAdapter {
        private int mChildCount = 0;
        private List<View> views;

        LoopPagerAdapter(List<View> list) {
            this.views = list;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt != null) {
                childAt.setTag((Object) null);
            }
        }

        public int getCount() {
            return (this.views.size() != 1 && BannerLayout.this.isImageLoop) ? BannerLayout.this.MAX_VALUE : this.views.size();
        }

        public int getItemPosition(Object obj) {
            int i = this.mChildCount;
            if (i <= 0) {
                return super.getItemPosition(obj);
            }
            this.mChildCount = i - 1;
            return -2;
        }

        public List<View> getViews() {
            return this.views;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            if (this.views.size() <= 0) {
                return null;
            }
            if (BannerLayout.this.isImageLoop) {
                i %= this.views.size();
            }
            View view = this.views.get(i);
            if (viewGroup.equals(view.getParent())) {
                viewGroup.removeView(view);
            }
            if (view.getTag() != null && (i == 0 || BannerLayout.this.isAllowImageDownload)) {
                BannerLayout.this.imageLoader.displayImage(BannerLayout.this.getContext(), ((NativeImageDataItem) view.getTag()).getUrl(), view, i);
            }
            viewGroup.addView(view);
            return view;
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public void notifyDataSetChanged() {
            this.mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        public void notifyItemsView(List<View> list) {
            this.views = list;
            notifyDataSetChanged();
        }
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int i);

        void onItemLongClick(int i);
    }

    public enum Position {
        centerBottom,
        rightBottom,
        leftBottom,
        centerTop,
        rightTop,
        leftTop,
        none
    }

    private static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int currentPosition;

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.currentPosition);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.currentPosition = parcel.readInt();
        }
    }

    private enum Shape {
        rect,
        oval
    }

    public BannerLayout(Context context, boolean z, boolean z2) {
        this(context, (AttributeSet) null, z, z2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: io.dcloud.feature.nativeObj.BannerImageView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: android.widget.RelativeLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: io.dcloud.feature.nativeObj.BannerImageView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: io.dcloud.feature.nativeObj.BannerImageView} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.view.View getImageView(io.dcloud.feature.nativeObj.data.NativeImageDataItem r4, final int r5) {
        /*
            r3 = this;
            boolean r0 = r3.isImagePhoto
            if (r0 == 0) goto L_0x0060
            android.widget.RelativeLayout r0 = new android.widget.RelativeLayout
            android.content.Context r1 = r3.getContext()
            r0.<init>(r1)
            r1 = 1
            r0.setClickable(r1)
            io.dcloud.feature.nativeObj.photoview.subscaleview.SubsamplingScaleImageView r1 = new io.dcloud.feature.nativeObj.photoview.subscaleview.SubsamplingScaleImageView
            android.content.Context r2 = r3.getContext()
            r1.<init>(r2)
            io.dcloud.feature.nativeObj.BannerLayout$2 r2 = new io.dcloud.feature.nativeObj.BannerLayout$2
            r2.<init>(r5)
            r1.setOnClickListener(r2)
            io.dcloud.feature.nativeObj.BannerLayout$3 r2 = new io.dcloud.feature.nativeObj.BannerLayout$3
            r2.<init>(r5)
            r1.setOnLongClickListener(r2)
            r5 = -1
            r1.setOrientation(r5)
            android.widget.RelativeLayout$LayoutParams r2 = new android.widget.RelativeLayout$LayoutParams
            r2.<init>(r5, r5)
            r0.addView(r1, r2)
            android.widget.ProgressBar r5 = new android.widget.ProgressBar
            android.content.Context r1 = r3.getContext()
            r5.<init>(r1)
            android.content.Context r1 = r3.getContext()     // Catch:{ Exception -> 0x0050 }
            android.content.res.Resources r1 = r1.getResources()     // Catch:{ Exception -> 0x0050 }
            int r2 = io.dcloud.PdrR.DRAWBLE_PROGRESSBAR_WHITE_CIRCLE     // Catch:{ Exception -> 0x0050 }
            android.graphics.drawable.Drawable r1 = r1.getDrawable(r2)     // Catch:{ Exception -> 0x0050 }
            r5.setIndeterminateDrawable(r1)     // Catch:{ Exception -> 0x0050 }
        L_0x0050:
            android.widget.RelativeLayout$LayoutParams r1 = new android.widget.RelativeLayout$LayoutParams
            r2 = 100
            r1.<init>(r2, r2)
            r2 = 13
            r1.addRule(r2)
            r0.addView(r5, r1)
            goto L_0x0071
        L_0x0060:
            io.dcloud.feature.nativeObj.BannerImageView r0 = new io.dcloud.feature.nativeObj.BannerImageView
            android.content.Context r1 = r3.getContext()
            r0.<init>(r1, r4)
            io.dcloud.feature.nativeObj.BannerLayout$4 r1 = new io.dcloud.feature.nativeObj.BannerLayout$4
            r1.<init>(r5)
            r0.setOnClickListener(r1)
        L_0x0071:
            r0.setTag(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.BannerLayout.getImageView(io.dcloud.feature.nativeObj.data.NativeImageDataItem, int):android.view.View");
    }

    private TextView getIndicatorTextView() {
        TextView textView = new TextView(getContext());
        this.indicatorContainer.addView(textView);
        textView.setGravity(17);
        textView.setTextColor(-1);
        textView.setPadding(10, 5, 10, 5);
        textView.setTextSize(15.0f);
        TextPaint paint = textView.getPaint();
        textView.setWidth(((int) paint.measureText(this.itemCount + "/" + this.itemCount)) + 40);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor(TitleNViewUtil.TRANSPARENT_BUTTON_BACKGROUND_COLOR));
        gradientDrawable.setCornerRadius(45.0f);
        textView.setBackgroundDrawable(gradientDrawable);
        return textView;
    }

    private void init(boolean z, boolean z2) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        GradientDrawable gradientDrawable3 = new GradientDrawable();
        int i = AnonymousClass6.$SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Shape[this.indicatorShape.ordinal()];
        if (i == 1) {
            gradientDrawable.setShape(0);
            gradientDrawable2.setShape(0);
            gradientDrawable3.setShape(0);
        } else if (i == 2) {
            gradientDrawable.setShape(1);
            gradientDrawable2.setShape(1);
            gradientDrawable3.setShape(1);
        }
        gradientDrawable3.setSize(this.selectedIndicatorWidth, this.selectedIndicatorHeight);
        gradientDrawable3.setColor(this.ringIndicatorColor);
        gradientDrawable.setColor(this.unSelectedIndicatorColor);
        gradientDrawable.setSize(this.unSelectedIndicatorWidth, this.unSelectedIndicatorHeight);
        this.unSelectedDrawable = new LayerDrawable(new Drawable[]{gradientDrawable});
        gradientDrawable2.setColor(this.selectedIndicatorColor);
        gradientDrawable2.setSize(this.selectedIndicatorWidth, this.selectedIndicatorHeight);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable3, gradientDrawable2});
        LayerDrawable layerDrawable2 = layerDrawable;
        layerDrawable2.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable2.setLayerInset(1, 2, 2, 2, 2);
        this.selectedDrawable = layerDrawable;
        this.isImagePhoto = z2;
        this.isImageLoop = z;
        int i2 = (int) (getResources().getDisplayMetrics().density * 5.0f);
        this.selectedIndicatorHeight = i2;
        this.selectedIndicatorWidth = i2;
        this.unSelectedIndicatorHeight = i2;
        this.unSelectedIndicatorWidth = i2;
    }

    private void setViews(List<View> list, int i) {
        ViewPager viewPager = this.pager;
        if (viewPager != null) {
            LoopPagerAdapter loopPagerAdapter = (LoopPagerAdapter) viewPager.getAdapter();
            if (loopPagerAdapter.getViews().size() <= 1 || list.size() != 1) {
                loopPagerAdapter.notifyItemsView(list);
            } else {
                removeAllViews();
                this.pager = null;
            }
        }
        if (this.pager == null) {
            ViewPager bounceBackViewPager = this.isImagePhoto ? new BounceBackViewPager(getContext()) : new ViewPager(getContext());
            this.pager = bounceBackViewPager;
            addView(bounceBackViewPager);
            this.pager.setAdapter(new LoopPagerAdapter(list));
        }
        setSliderTransformDuration(this.scrollDuration);
        initIndicatiorContainer();
        if (!this.isImageLoop || list.size() <= 1) {
            int i2 = i + 0;
            this.currentPosition = i2;
            this.pager.setCurrentItem(i2);
            switchIndicator(this.currentPosition);
        } else {
            int i3 = this.MAX_VALUE / 2;
            int i4 = (i3 - (i3 % this.itemCount)) + i;
            this.pager.setCurrentItem(i4);
            int i5 = i4 % this.itemCount;
            this.currentPosition = i5;
            switchIndicator(i5);
        }
        this.pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
                super.onPageScrollStateChanged(i);
            }

            public void onPageScrolled(int i, float f, int i2) {
                super.onPageScrolled(i, f, i2);
            }

            public void onPageSelected(int i) {
                View view;
                BannerLayout bannerLayout = BannerLayout.this;
                if (bannerLayout.isImageLoop) {
                    i %= BannerLayout.this.itemCount;
                }
                int unused = bannerLayout.currentPosition = i;
                if (!(BannerLayout.this.isAllowImageDownload || BannerLayout.this.imageLoader == null || (view = ((LoopPagerAdapter) BannerLayout.this.pager.getAdapter()).getViews().get(BannerLayout.this.currentPosition)) == null)) {
                    BannerLayout.this.imageLoader.displayImage(BannerLayout.this.getContext(), ((NativeImageDataItem) view.getTag()).getUrl(), view, BannerLayout.this.currentPosition);
                }
                BannerLayout bannerLayout2 = BannerLayout.this;
                bannerLayout2.switchIndicator(bannerLayout2.currentPosition);
            }
        });
        if (this.isAutoPlay) {
            startAutoPlay();
        }
    }

    private void startAutoPlay() {
        stopAutoPlay();
        if (this.isAutoPlay) {
            this.handler.sendEmptyMessageDelayed(this.WHAT_AUTO_PLAY, (long) this.autoPlayDuration);
        }
    }

    private void stopAutoPlay() {
        ViewPager viewPager = this.pager;
        if (viewPager != null) {
            viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
        }
        if (this.isAutoPlay) {
            this.handler.removeMessages(this.WHAT_AUTO_PLAY);
            ViewPager viewPager2 = this.pager;
            if (viewPager2 != null) {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem(), false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void switchIndicator(int i) {
        int i2 = 0;
        if (this.mIndicatorType.equals("number")) {
            View childAt = this.indicatorContainer.getChildAt(0);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setText((i + 1) + "/" + this.itemCount);
            }
        } else if (this.mIndicatorType.equals("default")) {
            while (i2 < this.indicatorContainer.getChildCount()) {
                ((ImageView) this.indicatorContainer.getChildAt(i2)).setImageDrawable(i2 == i ? this.selectedDrawable : this.unSelectedDrawable);
                i2++;
            }
        }
    }

    public void addViewUrls(ArrayList<NativeImageDataItem> arrayList, int i) {
        if (this.pager == null) {
            setViewUrls(arrayList, i);
            return;
        }
        ArrayList<NativeImageDataItem> arrayList2 = this.mUrls;
        if (arrayList2 != null) {
            arrayList2.addAll(arrayList);
        } else {
            this.mUrls = arrayList;
        }
        ArrayList arrayList3 = new ArrayList();
        this.itemCount = this.mUrls.size();
        for (int i2 = 0; i2 < this.mUrls.size(); i2++) {
            arrayList3.add(getImageView(this.mUrls.get(i2), i2));
        }
        initIndicatiorContainer();
        if (this.isImageLoop) {
            int i3 = this.MAX_VALUE / 2;
            int i4 = (i3 - (i3 % this.itemCount)) + i;
            this.pager.setCurrentItem(i4);
            int i5 = this.itemCount;
            int i6 = i4 % i5;
            this.currentPosition = i6;
            switchIndicator(i6 % i5);
        } else {
            int i7 = i + 0;
            this.currentPosition = i7;
            this.pager.setCurrentItem(i7);
            switchIndicator(this.currentPosition);
        }
        ((LoopPagerAdapter) this.pager.getAdapter()).notifyItemsView(arrayList3);
    }

    public void clearBannerData() {
        ViewPager viewPager = this.pager;
        if (viewPager != null) {
            viewPager.setAdapter((PagerAdapter) null);
            this.pager.removeAllViews();
            this.pager = null;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            stopAutoPlay();
        } else if (action == 1 || action == 3) {
            startAutoPlay();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public ViewPager getPager() {
        ViewPager viewPager = this.pager;
        if (viewPager != null) {
            return viewPager;
        }
        return null;
    }

    public ArrayList<NativeImageDataItem> getUrls() {
        return this.mUrls;
    }

    public void initIndicatiorContainer() {
        LinearLayout linearLayout = this.indicatorContainer;
        if (linearLayout != null) {
            removeView(linearLayout);
        }
        LinearLayout linearLayout2 = new LinearLayout(getContext());
        this.indicatorContainer = linearLayout2;
        linearLayout2.setGravity(16);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        switch (AnonymousClass6.$SwitchMap$io$dcloud$feature$nativeObj$BannerLayout$Position[this.indicatorPosition.ordinal()]) {
            case 1:
                layoutParams.addRule(14);
                layoutParams.addRule(12);
                break;
            case 2:
                layoutParams.addRule(14);
                layoutParams.addRule(10);
                break;
            case 3:
                layoutParams.addRule(9);
                layoutParams.addRule(12);
                break;
            case 4:
                layoutParams.addRule(9);
                layoutParams.addRule(10);
                break;
            case 5:
                layoutParams.addRule(11);
                layoutParams.addRule(12);
                break;
            case 6:
                layoutParams.addRule(11);
                layoutParams.addRule(10);
                break;
            case 7:
                layoutParams = null;
                break;
        }
        if (layoutParams != null) {
            int i = this.indicatorMargin;
            int statusHeight = (i / 2) + DeviceInfo.getStatusHeight(getContext());
            int i2 = this.indicatorMargin;
            layoutParams.setMargins(i, statusHeight, i2, i2);
            addView(this.indicatorContainer, layoutParams);
            if (this.mIndicatorType.equals("number")) {
                TextView indicatorTextView = getIndicatorTextView();
                indicatorTextView.setText("1/" + this.itemCount);
            } else {
                for (int i3 = 0; i3 < this.itemCount; i3++) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                    int i4 = this.indicatorSpace;
                    imageView.setPadding(i4, i4, i4, i4);
                    imageView.setImageDrawable(this.unSelectedDrawable);
                    this.indicatorContainer.addView(imageView);
                }
            }
            if (this.itemCount == 1) {
                this.indicatorContainer.setVisibility(4);
            } else {
                this.indicatorContainer.setVisibility(0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoPlay();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentPosition = savedState.currentPosition;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPosition = this.currentPosition;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }
    }

    public void setAllowImageDownload(boolean z, boolean z2) {
        ViewPager viewPager;
        this.isAllowImageDownload = z;
        if (z2 && (viewPager = this.pager) != null && viewPager.getAdapter() != null) {
            this.pager.getAdapter().notifyDataSetChanged();
        }
    }

    public void setAutoPlay(boolean z, int i) {
        this.isAutoPlay = z;
        this.autoPlayDuration = i;
    }

    public void setImageLoader(ImageLoader imageLoader2) {
        this.imageLoader = imageLoader2;
    }

    public void setImageLoop(Boolean bool) {
        this.isImageLoop = bool.booleanValue();
    }

    public void setIndicatorContainerData(Position position, int i, int i2, int i3, String str) {
        if (position != null) {
            this.indicatorPosition = position;
        }
        this.indicatorMargin = i;
        this.indicatorSpace = i2;
        this.selectedIndicatorHeight = i3;
        this.selectedIndicatorWidth = i3;
        this.unSelectedIndicatorHeight = i3;
        this.unSelectedIndicatorWidth = i3;
        if (!TextUtils.isEmpty(str)) {
            this.mIndicatorType = str;
        }
        if (this.mIndicatorType.equals("default")) {
            this.indicatorPosition = Position.centerBottom;
        } else if (this.mIndicatorType.equals("number")) {
            this.indicatorPosition = Position.centerTop;
        } else if (this.mIndicatorType.equals("none")) {
            this.indicatorPosition = Position.none;
        }
    }

    public void setIndicatorType(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mIndicatorType = str;
        }
        if (this.mIndicatorType.equals("default")) {
            this.indicatorPosition = Position.centerBottom;
        } else if (this.mIndicatorType.equals("number")) {
            this.indicatorPosition = Position.centerTop;
        } else if (this.mIndicatorType.equals("none")) {
            this.indicatorPosition = Position.none;
        }
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener2) {
        this.onBannerItemClickListener = onBannerItemClickListener2;
    }

    public void setScrollDuration(int i) {
        this.scrollDuration = i;
    }

    public void setSliderTransformDuration(int i) {
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            declaredField.set(this.pager, new FixedSpeedScroller(this, this.pager.getContext(), (Interpolator) null, i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setViewUrls(ArrayList<NativeImageDataItem> arrayList, int i) {
        ArrayList<NativeImageDataItem> arrayList2 = this.mUrls;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        this.mUrls = arrayList;
        ArrayList arrayList3 = new ArrayList();
        int size = arrayList.size();
        this.itemCount = size;
        if (size >= 1) {
            if (size == 2) {
                if (this.isImageLoop) {
                    arrayList3.add(getImageView(arrayList.get(0), 0));
                    arrayList3.add(getImageView(arrayList.get(1), 1));
                }
                arrayList3.add(getImageView(arrayList.get(0), 0));
                arrayList3.add(getImageView(arrayList.get(1), 1));
            } else {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    arrayList3.add(getImageView(arrayList.get(i2), i2));
                }
            }
            setViews(arrayList3, i);
            return;
        }
        throw new IllegalStateException("item count not equal zero");
    }

    public void setmIndicatorType(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mIndicatorType = str;
        }
    }

    public BannerLayout(Context context, AttributeSet attributeSet, boolean z, boolean z2) {
        this(context, attributeSet, 0, z, z2);
    }

    public BannerLayout(Context context, AttributeSet attributeSet, int i, boolean z, boolean z2) {
        super(context, attributeSet, i);
        this.mIndicatorType = "default";
        this.WHAT_AUTO_PLAY = 1000;
        this.isAutoPlay = false;
        this.selectedIndicatorColor = -1;
        this.unSelectedIndicatorColor = -5592406;
        this.ringIndicatorColor = -5592406;
        this.indicatorShape = Shape.oval;
        this.selectedIndicatorHeight = 15;
        this.selectedIndicatorWidth = 15;
        this.unSelectedIndicatorHeight = 15;
        this.unSelectedIndicatorWidth = 15;
        this.indicatorPosition = Position.centerBottom;
        this.autoPlayDuration = 4000;
        this.scrollDuration = 900;
        this.indicatorSpace = 3;
        this.indicatorMargin = 10;
        this.isAllowImageDownload = true;
        this.isImagePhoto = false;
        this.isImageLoop = false;
        this.MAX_VALUE = 150;
        this.handler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message message) {
                if (message.what == BannerLayout.this.WHAT_AUTO_PLAY && BannerLayout.this.pager != null && BannerLayout.this.isAutoPlay && BannerLayout.this.mUrls != null && BannerLayout.this.mUrls.size() > 1) {
                    if (BannerLayout.this.isImageLoop) {
                        BannerLayout.this.pager.setCurrentItem(BannerLayout.this.pager.getCurrentItem() + 1, true);
                    } else {
                        int currentItem = BannerLayout.this.pager.getCurrentItem() + 1;
                        if (currentItem >= BannerLayout.this.mUrls.size()) {
                            return false;
                        }
                        BannerLayout.this.pager.setCurrentItem(currentItem, true);
                    }
                    BannerLayout.this.handler.sendEmptyMessageDelayed(BannerLayout.this.WHAT_AUTO_PLAY, (long) BannerLayout.this.autoPlayDuration);
                }
                return false;
            }
        });
        SubsamplingScaleImageView.setPreferredBitmapConfig(Bitmap.Config.ARGB_8888);
        init(z, z2);
    }
}
