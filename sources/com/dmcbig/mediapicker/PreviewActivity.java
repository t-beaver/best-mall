package com.dmcbig.mediapicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.view.PreviewFragment;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PreviewActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    TextView bar_title;
    View bottom;
    ImageView check_image;
    LinearLayout check_layout;
    Button done;
    String doneBtnText = "";
    private Class editActivityClass;
    private boolean editable = true;
    ArrayList<Fragment> fragmentArrayList;
    ImageView fullImage;
    TextView imageEdit;
    boolean isSingle = false;
    private int max;
    ArrayList<Media> preRawList;
    ArrayList<Media> selects;
    View top;
    ViewPager viewpager;

    public class AdapterFragment extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;

        public AdapterFragment(FragmentManager fragmentManager, List<Fragment> list) {
            super(fragmentManager);
            this.mFragments = list;
        }

        public int getCount() {
            return this.mFragments.size();
        }

        public Fragment getItem(int i) {
            return this.mFragments.get(i);
        }

        public int getItemPosition(Object obj) {
            return -2;
        }
    }

    private void setTopAndBottomBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(Color.parseColor("#21282C"));
            window.setNavigationBarColor(Color.parseColor("#21282C"));
        }
    }

    public void done(ArrayList<Media> arrayList, int i) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, arrayList);
        intent.putIntegerArrayListExtra(PickerConfig.ORIGINAL_PREVIEW_INDEX, getIntent().getIntegerArrayListExtra(PickerConfig.ORIGINAL_PREVIEW_INDEX));
        intent.putParcelableArrayListExtra(PickerConfig.EDITED_PREVIEW_DATA, this.preRawList);
        intent.putExtra(PickerConfig.FULL_IMAGE, this.fullImage.isSelected());
        setResult(i, intent);
        finish();
    }

    public int isSelect(Media media, ArrayList<Media> arrayList) {
        if (arrayList.size() <= 0) {
            return -1;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).path.equals(media.path)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 0 && intent != null) {
            int intExtra = intent.getIntExtra("IMAGE_INDEX", -1);
            int intExtra2 = intent.getIntExtra("_id", -1);
            if (intExtra2 != -1 && intExtra != -1) {
                Media media = new Media(intent.getStringExtra("PATH"), intent.getStringExtra("_display_name"), intent.getLongExtra("date_added", System.currentTimeMillis()), intent.getIntExtra("mime_type", 0), intent.getLongExtra("_size", 0), intExtra2, intent.getStringExtra("PARENTPATH"));
                int isSelect = isSelect(this.preRawList.get(intExtra), this.selects);
                if (isSelect >= 0) {
                    this.selects.set(isSelect, media);
                }
                this.preRawList.set(intExtra, media);
                this.fragmentArrayList.set(intExtra, PreviewFragment.newInstance(media, ""));
                this.viewpager.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public void onBackPressed() {
        done(this.selects, PickerConfig.RESULT_UPDATE_CODE);
        super.onBackPressed();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == MediaPickerR.MP_ID_BTN_BACK) {
            done(this.selects, PickerConfig.RESULT_UPDATE_CODE);
        } else if (id == MediaPickerR.MP_ID_DONE) {
            done(this.selects, PickerConfig.RESULT_CODE);
        } else if (id == MediaPickerR.MP_ID_CHECK_LAYOUT) {
            Media media = this.preRawList.get(this.viewpager.getCurrentItem());
            int isSelect = isSelect(media, this.selects);
            if (isSelect < 0) {
                this.check_image.setImageDrawable(ContextCompat.getDrawable(this, MediaPickerR.MP_DRAWABLE_BNT_SELECTED));
                this.selects.add(media);
            } else {
                this.check_image.setImageDrawable(ContextCompat.getDrawable(this, MediaPickerR.MP_DRAWABLE_BNT_UNSELECTED));
                this.selects.remove(isSelect);
            }
            setDoneView(this.selects.size());
        } else if (id == R.id.gallery_preview_edit) {
            int currentItem = this.viewpager.getCurrentItem();
            Media media2 = this.preRawList.get(currentItem);
            Intent intent = new Intent(this, this.editActivityClass);
            intent.putExtra("IMAGE_URI", Uri.parse(DeviceInfo.FILE_PROTOCOL + media2.path));
            intent.putExtra("IMAGE_MEDIA_ID", media2.id);
            intent.putExtra("IMAGE_INDEX", currentItem);
            startActivityForResult(intent, 0);
        } else if (id == R.id.check_origin_image_layout) {
            ImageView imageView = this.fullImage;
            imageView.setSelected(!imageView.isSelected());
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(MediaPickerR.MP_LAYOUT_PREVIEW_MAIN);
        setTopAndBottomBarColor();
        findViewById(MediaPickerR.MP_ID_BTN_BACK).setOnClickListener(this);
        this.isSingle = getIntent().getBooleanExtra(PickerConfig.SINGLE_SELECT, false);
        this.check_image = (ImageView) findViewById(MediaPickerR.MP_ID_CHECK_IMAGE);
        LinearLayout linearLayout = (LinearLayout) findViewById(MediaPickerR.MP_ID_CHECK_LAYOUT);
        this.check_layout = linearLayout;
        linearLayout.setOnClickListener(this);
        this.bar_title = (TextView) findViewById(MediaPickerR.MP_ID_BAR_TITLE);
        Button button = (Button) findViewById(MediaPickerR.MP_ID_DONE);
        this.done = button;
        button.setOnClickListener(this);
        this.top = findViewById(MediaPickerR.MP_ID_TOP);
        this.bottom = findViewById(MediaPickerR.MP_ID_BOTTOM);
        TextView textView = (TextView) findViewById(R.id.gallery_preview_edit);
        this.imageEdit = textView;
        textView.setOnClickListener(this);
        this.fullImage = (ImageView) findViewById(R.id.check_origin_image);
        this.fullImage.setSelected(getIntent().getBooleanExtra(PickerConfig.FULL_IMAGE, false));
        String stringExtra = getIntent().getStringExtra(PickerConfig.SIZE_TYPE);
        if (stringExtra == null) {
            findViewById(R.id.check_origin_image_layout).setOnClickListener(this);
        } else if (stringExtra.contains(Constants.Value.ORIGINAL) && stringExtra.contains("compressed")) {
            findViewById(R.id.check_origin_image_layout).setOnClickListener(this);
        } else if (!stringExtra.contains(Constants.Value.ORIGINAL)) {
            findViewById(R.id.check_origin_image_layout).setVisibility(8);
        }
        if (getIntent().getIntExtra(PickerConfig.SELECT_MODE, 101) == 102) {
            findViewById(R.id.check_origin_image_layout).setVisibility(8);
        }
        this.editable = getIntent().getBooleanExtra(PickerConfig.IMAGE_EDITABLE, true);
        String stringExtra2 = getIntent().getStringExtra(PickerConfig.DONE_BUTTON_TEXT);
        this.doneBtnText = stringExtra2;
        if (!PdrUtil.isEmpty(stringExtra2)) {
            this.done.setText(this.doneBtnText);
        }
        this.max = getIntent().getIntExtra(PickerConfig.MAX_SELECT_COUNT, Integer.MAX_VALUE);
        try {
            this.editActivityClass = Class.forName("io.dcloud.feature.gallery.imageedit.IMGEditActivity");
        } catch (ClassNotFoundException unused) {
        }
        if (this.isSingle) {
            this.bottom.setVisibility(8);
        }
        this.viewpager = (ViewPager) findViewById(MediaPickerR.MP_ID_VIEWPAGER);
        this.preRawList = getIntent().getParcelableArrayListExtra(PickerConfig.PRE_RAW_LIST);
        this.selects = new ArrayList<>();
        ArrayList<Media> arrayList = this.preRawList;
        if (arrayList == null || arrayList.size() == 0) {
            onBackPressed();
            return;
        }
        this.selects.addAll(this.preRawList);
        if (BaseInfo.sGlobalFullScreen) {
            setFullScreen(this, true);
        }
        setView(this.preRawList);
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
        TextView textView = this.bar_title;
        textView.setText((i + 1) + "/" + this.preRawList.size());
        Media media = this.preRawList.get(i);
        if (".gif".equalsIgnoreCase(media.extension) || this.editActivityClass == null || media.mediaType == 3 || !this.editable) {
            this.imageEdit.setVisibility(4);
        } else {
            this.imageEdit.setVisibility(0);
        }
        this.check_image.setImageDrawable(ContextCompat.getDrawable(this, isSelect(media, this.selects) < 0 ? MediaPickerR.MP_DRAWABLE_BNT_UNSELECTED : MediaPickerR.MP_DRAWABLE_BNT_SELECTED));
    }

    public void setBarStatus() {
        Log.e("setBarStatus", "setBarStatus");
        if (this.top.getVisibility() == 0) {
            this.top.setVisibility(8);
            if (!this.isSingle) {
                this.bottom.setVisibility(8);
                return;
            }
            return;
        }
        this.top.setVisibility(0);
        if (!this.isSingle) {
            this.bottom.setVisibility(0);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDoneView(int i) {
        if (PdrUtil.isEmpty(this.doneBtnText)) {
            this.doneBtnText = getString(MediaPickerR.MP_STRING_DONE);
        }
        if (this.max == Integer.MAX_VALUE) {
            Button button = this.done;
            button.setText(this.doneBtnText + Operators.BRACKET_START_STR + i + Operators.BRACKET_END_STR);
            return;
        }
        Button button2 = this.done;
        button2.setText(this.doneBtnText + Operators.BRACKET_START_STR + i + "/" + this.max + Operators.BRACKET_END_STR);
    }

    public void setFullScreen(Activity activity, boolean z) {
        Window window = activity.getWindow();
        if (z) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags |= 1024;
            window.setAttributes(attributes);
            return;
        }
        WindowManager.LayoutParams attributes2 = window.getAttributes();
        attributes2.flags &= -1025;
        window.setAttributes(attributes2);
    }

    /* access modifiers changed from: package-private */
    public void setView(ArrayList<Media> arrayList) {
        setDoneView(arrayList.size());
        TextView textView = this.bar_title;
        textView.setText("1/" + this.preRawList.size());
        Media media = this.preRawList.get(0);
        if (".gif".equalsIgnoreCase(media.extension) || this.editActivityClass == null || media.mediaType == 3 || !this.editable) {
            this.imageEdit.setVisibility(4);
        }
        this.fragmentArrayList = new ArrayList<>();
        Iterator<Media> it = arrayList.iterator();
        while (it.hasNext()) {
            this.fragmentArrayList.add(PreviewFragment.newInstance(it.next(), ""));
        }
        this.viewpager.setAdapter(new AdapterFragment(getSupportFragmentManager(), this.fragmentArrayList));
        this.viewpager.addOnPageChangeListener(this);
    }
}
