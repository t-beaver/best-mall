package com.dmcbig.mediapicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import androidtranscoder.MediaTranscoder;
import androidtranscoder.format.MediaFormatStrategyPresets;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dcloud.android.widget.toast.ToastCompat;
import com.dmcbig.mediapicker.adapter.FolderAdapter;
import com.dmcbig.mediapicker.adapter.MediaGridAdapter;
import com.dmcbig.mediapicker.adapter.SpacingDecoration;
import com.dmcbig.mediapicker.data.DataCallback;
import com.dmcbig.mediapicker.data.ImageLoader;
import com.dmcbig.mediapicker.data.MediaLoader;
import com.dmcbig.mediapicker.data.VideoLoader;
import com.dmcbig.mediapicker.entity.Folder;
import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CompressUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.gallery.imageedit.IMGEditActivity;
import io.dcloud.js.gallery.GalleryFeatureImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class PickerActivity extends FragmentActivity implements DataCallback, View.OnClickListener {
    Intent argsIntent;
    Button category_btn;
    String cropOptions;
    String docPath;
    Button done;
    String doneBtnText = "";
    ImageView fullImage;
    MediaGridAdapter gridAdapter;
    boolean isCompress = false;
    private boolean isFinish = false;
    boolean isSingle = false;
    /* access modifiers changed from: private */
    public FolderAdapter mFolderAdapter;
    ListPopupWindow mFolderPopupWindow;
    Button preview;
    RecyclerView recyclerView;
    Iterator<Media> selectIterator;

    /* access modifiers changed from: private */
    /* renamed from: compress */
    public void lambda$done$0$PickerActivity(final ArrayList<Media> arrayList) {
        final String str;
        if (!isFinishing() && !isDestroyed()) {
            if (this.selectIterator.hasNext()) {
                final Media next = this.selectIterator.next();
                if (next.extension.equalsIgnoreCase(".png") || next.extension.equalsIgnoreCase(".jpg") || next.extension.equalsIgnoreCase(".jpeg")) {
                    String[] split = next.path.split("/");
                    String compressImage = CompressUtil.compressImage(next.path, this.docPath + "uniapp_temp/compressed/" + System.currentTimeMillis() + "_" + split[split.length - 1], next.extension.equalsIgnoreCase(".png"), this);
                    if (!TextUtils.isEmpty(compressImage)) {
                        next.path = compressImage;
                    }
                    lambda$done$0$PickerActivity(arrayList);
                } else if (next.mediaType == 3) {
                    if (this.docPath.endsWith("/")) {
                        str = this.docPath + "compress_video_" + SystemClock.elapsedRealtime() + ".mp4";
                    } else {
                        str = this.docPath + "/compress_video_" + SystemClock.elapsedRealtime() + ".mp4";
                    }
                    try {
                        new File(str).getParentFile().mkdirs();
                        MediaTranscoder.getInstance().transcodeVideo(next.path, str, MediaFormatStrategyPresets.createAndroid720pStrategy(2, 1.0d), (MediaTranscoder.Listener) new MediaTranscoder.Listener() {
                            public void onTranscodeCanceled() {
                                PickerActivity.this.lambda$done$0$PickerActivity(arrayList);
                            }

                            public void onTranscodeCompleted() {
                                next.path = str;
                                PickerActivity.this.lambda$done$0$PickerActivity(arrayList);
                            }

                            public void onTranscodeFailed(Exception exc) {
                                PickerActivity.this.lambda$done$0$PickerActivity(arrayList);
                            }

                            public void onTranscodeProgress(double d) {
                            }
                        });
                    } catch (Exception unused) {
                        lambda$done$0$PickerActivity(arrayList);
                    }
                } else {
                    lambda$done$0$PickerActivity(arrayList);
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, arrayList);
                        PickerActivity.this.setResult(PickerConfig.RESULT_CODE, intent);
                        PickerActivity.this.finish();
                    }
                });
            }
        }
    }

    private void getOtherArgs() {
        String stringExtra = this.argsIntent.getStringExtra(PickerConfig.DONE_BUTTON_TEXT);
        this.doneBtnText = stringExtra;
        if (!PdrUtil.isEmpty(stringExtra)) {
            this.done.setText(this.doneBtnText);
        }
        if (!PdrUtil.isEmpty(this.cropOptions)) {
            this.done.setVisibility(4);
            findViewById(MediaPickerR.MP_ID_PREVIEW).setVisibility(4);
            this.isCompress = false;
            findViewById(R.id.check_origin_image_layout).setVisibility(8);
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

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            super.attachBaseContext(context);
        } else {
            super.attachBaseContext(LanguageUtil.updateContextLanguageAfterO(context, false));
        }
    }

    /* access modifiers changed from: package-private */
    public void createAdapter() {
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, PickerConfig.GridSpanCount));
        this.recyclerView.addItemDecoration(new SpacingDecoration(PickerConfig.GridSpanCount, PickerConfig.GridSpace));
        this.recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        ArrayList parcelableArrayListExtra = this.argsIntent.getParcelableArrayListExtra(PickerConfig.DEFAULT_SELECTED_LIST);
        final String stringExtra = this.argsIntent.getStringExtra(PickerConfig.SELECTED_MAX_CALLBACK_ID);
        int intExtra = this.argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, Integer.MAX_VALUE);
        long longExtra = this.argsIntent.getLongExtra(PickerConfig.MAX_SELECT_SIZE, Long.MAX_VALUE);
        this.isSingle = this.argsIntent.getBooleanExtra(PickerConfig.SINGLE_SELECT, false);
        if (!TextUtils.isEmpty(this.cropOptions)) {
            this.isSingle = true;
        }
        if (this.isSingle) {
            Button button = this.done;
            if (button != null) {
                button.setVisibility(8);
            }
            Button button2 = this.preview;
            if (button2 != null) {
                button2.setVisibility(4);
            }
        }
        MediaGridAdapter mediaGridAdapter = new MediaGridAdapter(arrayList, this, parcelableArrayListExtra, intExtra, longExtra, this.isSingle);
        this.gridAdapter = mediaGridAdapter;
        mediaGridAdapter.setOnMaxListener(new MediaGridAdapter.OnPickerSelectMaxListener() {
            public void onMaxed() {
                if (!TextUtils.isEmpty(stringExtra)) {
                    GalleryFeatureImpl.onMaxed(PickerActivity.this, stringExtra);
                }
            }
        });
        this.recyclerView.setAdapter(this.gridAdapter);
    }

    /* access modifiers changed from: package-private */
    public void createFolderAdapter() {
        this.mFolderAdapter = new FolderAdapter(new ArrayList(), this);
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        this.mFolderPopupWindow = listPopupWindow;
        listPopupWindow.setBackgroundDrawable(new ColorDrawable(-1));
        this.mFolderPopupWindow.setAdapter(this.mFolderAdapter);
        ListPopupWindow listPopupWindow2 = this.mFolderPopupWindow;
        double screenHeight = (double) ScreenUtils.getScreenHeight(this);
        Double.isNaN(screenHeight);
        listPopupWindow2.setHeight((int) (screenHeight * 0.6d));
        this.mFolderPopupWindow.setAnchorView(findViewById(MediaPickerR.MP_ID_FOOTER));
        this.mFolderPopupWindow.setModal(true);
        this.mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PickerActivity.this.mFolderAdapter.setSelectIndex(i);
                PickerActivity pickerActivity = PickerActivity.this;
                pickerActivity.category_btn.setText(pickerActivity.mFolderAdapter.getItem(i).name);
                PickerActivity pickerActivity2 = PickerActivity.this;
                pickerActivity2.gridAdapter.updateAdapter(pickerActivity2.mFolderAdapter.getSelectMedias());
                PickerActivity.this.mFolderPopupWindow.dismiss();
            }
        });
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.isFinish) {
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void done(ArrayList<Media> arrayList) {
        this.isFinish = true;
        if (!this.isCompress || arrayList == null || arrayList.size() <= 0) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, arrayList);
            setResult(PickerConfig.RESULT_CODE, intent);
            finish();
            return;
        }
        this.done.setClickable(false);
        this.done.setFocusable(true);
        findViewById(R.id.loading).setVisibility(0);
        this.selectIterator = arrayList.iterator();
        ThreadPool.self().addThreadTask(new Runnable(arrayList) {
            public final /* synthetic */ ArrayList f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                PickerActivity.this.lambda$done$0$PickerActivity(this.f$1);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void getMediaData() {
        int intExtra = this.argsIntent.getIntExtra(PickerConfig.SELECT_MODE, 101);
        if (intExtra == 101) {
            getLoaderManager().initLoader(intExtra, (Bundle) null, new MediaLoader(this, this));
        } else if (intExtra == 100) {
            getLoaderManager().initLoader(intExtra, (Bundle) null, new ImageLoader(this, this));
        } else if (intExtra == 102) {
            getLoaderManager().initLoader(intExtra, (Bundle) null, new VideoLoader(this, this));
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (intent != null && intent.hasExtra(PickerConfig.FULL_IMAGE)) {
            this.fullImage.setSelected(intent.getBooleanExtra(PickerConfig.FULL_IMAGE, false));
            this.isCompress = !this.fullImage.isSelected();
        }
        if (i == 200) {
            ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (i2 == 1990) {
                ArrayList<Integer> integerArrayListExtra = intent.getIntegerArrayListExtra(PickerConfig.ORIGINAL_PREVIEW_INDEX);
                ArrayList parcelableArrayListExtra2 = intent.getParcelableArrayListExtra(PickerConfig.EDITED_PREVIEW_DATA);
                if (integerArrayListExtra != null && integerArrayListExtra.size() > 0 && parcelableArrayListExtra2 != null && parcelableArrayListExtra2.size() > 0) {
                    ArrayList<Media> medias = this.gridAdapter.getMedias();
                    for (int i3 = 0; i3 < integerArrayListExtra.size(); i3++) {
                        int intValue = integerArrayListExtra.get(i3).intValue();
                        if (intValue >= 0) {
                            medias.set(intValue, (Media) parcelableArrayListExtra2.get(i3));
                        }
                    }
                    this.gridAdapter.updateAdapter(medias);
                }
                this.gridAdapter.updateSelectAdapter(parcelableArrayListExtra);
                setButtonText();
            } else if (i2 == 19901026) {
                done(parcelableArrayListExtra);
            }
        } else if (i == 201 && i2 == -1 && intent != null) {
            int intExtra = intent.getIntExtra("IMAGE_INDEX", -1);
            int intExtra2 = intent.getIntExtra("_id", -1);
            if (intExtra2 != -1 && intExtra != -1) {
                Media media = new Media(intent.getStringExtra("PATH"), intent.getStringExtra("_display_name"), intent.getLongExtra("date_added", System.currentTimeMillis()), intent.getIntExtra("mime_type", 0), intent.getLongExtra("_size", 0), intExtra2, intent.getStringExtra("PARENTPATH"));
                ArrayList arrayList = new ArrayList();
                arrayList.add(media);
                done(arrayList);
            }
        }
    }

    public void onBackPressed() {
        done(new ArrayList());
        super.onBackPressed();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == MediaPickerR.MP_ID_BTN_BACK) {
            done(new ArrayList());
        } else if (id == MediaPickerR.MP_ID_CATEGORY_BTN) {
            if (this.mFolderPopupWindow.isShowing()) {
                this.mFolderPopupWindow.dismiss();
            } else {
                this.mFolderPopupWindow.show();
            }
        } else if (id == MediaPickerR.MP_ID_DONE) {
            done(this.gridAdapter.getSelectMedias());
        } else if (id == MediaPickerR.MP_ID_PREVIEW) {
            if (this.gridAdapter.getSelectMedias().size() <= 0) {
                ToastCompat.makeText((Context) this, (CharSequence) getString(MediaPickerR.MP_STRING_SELECT_NULL), 0).show();
            } else if (PdrUtil.isEmpty(this.cropOptions)) {
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT, this.argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, Integer.MAX_VALUE));
                ArrayList<Media> selectMedias = this.gridAdapter.getSelectMedias();
                intent.putExtra(PickerConfig.PRE_RAW_LIST, selectMedias);
                ArrayList arrayList = new ArrayList();
                Iterator<Media> it = selectMedias.iterator();
                while (it.hasNext()) {
                    arrayList.add(Integer.valueOf(this.gridAdapter.getItemIndex(it.next())));
                }
                intent.putExtra(PickerConfig.FULL_IMAGE, this.fullImage.isSelected());
                intent.putExtra(PickerConfig.SIZE_TYPE, this.argsIntent.getStringExtra(PickerConfig.SIZE_TYPE));
                intent.putIntegerArrayListExtra(PickerConfig.ORIGINAL_PREVIEW_INDEX, arrayList);
                intent.putExtra(PickerConfig.IMAGE_EDITABLE, this.argsIntent.getBooleanExtra(PickerConfig.IMAGE_EDITABLE, true));
                intent.putExtra(PickerConfig.DONE_BUTTON_TEXT, this.argsIntent.getStringExtra(PickerConfig.DONE_BUTTON_TEXT));
                intent.putExtra(PickerConfig.SELECT_MODE, this.argsIntent.getIntExtra(PickerConfig.SELECT_MODE, 101));
                startActivityForResult(intent, 200);
            }
        } else if (id == R.id.check_origin_image_layout) {
            ImageView imageView = this.fullImage;
            imageView.setSelected(!imageView.isSelected());
            this.isCompress = !this.fullImage.isSelected();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        this.argsIntent = getIntent();
        setContentView(MediaPickerR.MP_LAYOUT_PICKER_MAIN);
        this.recyclerView = (RecyclerView) findViewById(MediaPickerR.MP_ID_RECYCLER_VIEW);
        findViewById(MediaPickerR.MP_ID_BTN_BACK).setOnClickListener(this);
        setTitleBar();
        this.done = (Button) findViewById(MediaPickerR.MP_ID_DONE);
        this.category_btn = (Button) findViewById(MediaPickerR.MP_ID_CATEGORY_BTN);
        this.preview = (Button) findViewById(MediaPickerR.MP_ID_PREVIEW);
        this.fullImage = (ImageView) findViewById(R.id.check_origin_image);
        if (this.argsIntent.getIntExtra(PickerConfig.SELECT_MODE, 101) == 102) {
            boolean booleanExtra = this.argsIntent.getBooleanExtra(PickerConfig.COMPRESSED, false);
            this.isCompress = booleanExtra;
            this.fullImage.setSelected(!booleanExtra);
            findViewById(R.id.check_origin_image_layout).setVisibility(8);
        } else {
            Intent intent = this.argsIntent;
            if (intent == null || !intent.hasExtra(PickerConfig.SIZE_TYPE)) {
                this.isCompress = true;
                findViewById(R.id.check_origin_image_layout).setOnClickListener(this);
            } else {
                String stringExtra = this.argsIntent.getStringExtra(PickerConfig.SIZE_TYPE);
                if (stringExtra == null) {
                    this.isCompress = true;
                    findViewById(R.id.check_origin_image_layout).setOnClickListener(this);
                } else if (stringExtra.contains(Constants.Value.ORIGINAL) && stringExtra.contains("compressed")) {
                    this.isCompress = true;
                    findViewById(R.id.check_origin_image_layout).setOnClickListener(this);
                } else if (stringExtra.contains(Constants.Value.ORIGINAL)) {
                    this.isCompress = false;
                    this.fullImage.setSelected(true);
                } else {
                    this.isCompress = true;
                    findViewById(R.id.check_origin_image_layout).setVisibility(8);
                }
            }
        }
        this.done.setOnClickListener(this);
        this.category_btn.setOnClickListener(this);
        this.preview.setOnClickListener(this);
        if (BaseInfo.sGlobalFullScreen) {
            setFullScreen(this, true);
        }
        this.cropOptions = this.argsIntent.getStringExtra(PickerConfig.IMAGE_CROP);
        this.docPath = this.argsIntent.getStringExtra(PickerConfig.DOC_PATH);
        createAdapter();
        createFolderAdapter();
        getMediaData();
        getOtherArgs();
        setTopAndBottomBarColor();
    }

    public void onData(ArrayList<Folder> arrayList) {
        setView(arrayList);
        this.category_btn.setText(arrayList.get(0).name);
        this.mFolderAdapter.updateAdapter(arrayList);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        RuningAcitvityUtil.removeRuningActivity(getComponentName().getClassName());
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        RuningAcitvityUtil.putRuningActivity(this);
    }

    /* access modifiers changed from: package-private */
    public void setButtonText() {
        int intExtra = this.argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, Integer.MAX_VALUE);
        if (PdrUtil.isEmpty(this.doneBtnText)) {
            this.doneBtnText = getString(MediaPickerR.MP_STRING_DONE);
        }
        if (intExtra == Integer.MAX_VALUE) {
            Button button = this.done;
            button.setText(this.doneBtnText + Operators.BRACKET_START_STR + this.gridAdapter.getSelectMedias().size() + Operators.BRACKET_END_STR);
        } else {
            Button button2 = this.done;
            button2.setText(this.doneBtnText + Operators.BRACKET_START_STR + this.gridAdapter.getSelectMedias().size() + "/" + intExtra + Operators.BRACKET_END_STR);
        }
        if (PdrUtil.isEmpty(this.cropOptions)) {
            Button button3 = this.preview;
            button3.setText(getString(MediaPickerR.MP_STRING_PREVIEW) + Operators.BRACKET_START_STR + this.gridAdapter.getSelectMedias().size() + Operators.BRACKET_END_STR);
            return;
        }
        this.done.setVisibility(4);
        findViewById(MediaPickerR.MP_ID_PREVIEW).setVisibility(4);
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

    public void setTitleBar() {
        int intExtra = this.argsIntent.getIntExtra(PickerConfig.SELECT_MODE, 101);
        if (intExtra == 101) {
            ((TextView) findViewById(MediaPickerR.MP_ID_BAR_TITLE)).setText(getString(MediaPickerR.MP_STRING_SELECT_TITLE));
        } else if (intExtra == 100) {
            ((TextView) findViewById(MediaPickerR.MP_ID_BAR_TITLE)).setText(getString(MediaPickerR.MP_STRING_SELECT_IMAGE_TITLE));
        } else if (intExtra == 102) {
            ((TextView) findViewById(MediaPickerR.MP_ID_BAR_TITLE)).setText(getString(MediaPickerR.MP_STRING_SELECT_VIDEO_TITLE));
        }
    }

    /* access modifiers changed from: package-private */
    public void setView(ArrayList<Folder> arrayList) {
        this.gridAdapter.updateAdapter(arrayList.get(0).getMedias());
        setButtonText();
        this.gridAdapter.setOnItemClickListener(new MediaGridAdapter.OnRecyclerViewItemClickListener() {
            public void onItemClick(View view, Media media, ArrayList<Media> arrayList) {
                PickerActivity pickerActivity = PickerActivity.this;
                if (!pickerActivity.isSingle) {
                    pickerActivity.setButtonText();
                } else if (PdrUtil.isEmpty(pickerActivity.cropOptions)) {
                    Intent intent = new Intent(PickerActivity.this, PreviewActivity.class);
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, PickerActivity.this.argsIntent.getIntExtra(PickerConfig.MAX_SELECT_COUNT, Integer.MAX_VALUE));
                    intent.putExtra(PickerConfig.SINGLE_SELECT, true);
                    ArrayList arrayList2 = new ArrayList(1);
                    arrayList2.add(media);
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(Integer.valueOf(PickerActivity.this.gridAdapter.getItemIndex(media)));
                    intent.putExtra(PickerConfig.PRE_RAW_LIST, arrayList2);
                    intent.putExtra(PickerConfig.FULL_IMAGE, PickerActivity.this.fullImage.isSelected());
                    intent.putExtra(PickerConfig.SIZE_TYPE, PickerActivity.this.argsIntent.getStringExtra(PickerConfig.SIZE_TYPE));
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerActivity.this.argsIntent.getIntExtra(PickerConfig.SELECT_MODE, 101));
                    intent.putIntegerArrayListExtra(PickerConfig.ORIGINAL_PREVIEW_INDEX, arrayList3);
                    PickerActivity.this.startActivityForResult(intent, 200);
                } else if (".gif".equalsIgnoreCase(media.extension) || media.mediaType == 3) {
                    ArrayList arrayList4 = new ArrayList();
                    arrayList4.add(media);
                    PickerActivity.this.done(arrayList4);
                } else {
                    Intent intent2 = new Intent(PickerActivity.this, IMGEditActivity.class);
                    int itemIndex = PickerActivity.this.gridAdapter.getItemIndex(media);
                    intent2.putExtra("IMAGE_URI", Uri.parse(DeviceInfo.FILE_PROTOCOL + media.path));
                    intent2.putExtra("IMAGE_MEDIA_ID", media.id);
                    intent2.putExtra("IMAGE_INDEX", itemIndex);
                    intent2.putExtra("IMAGE_CROP", PickerActivity.this.cropOptions);
                    PickerActivity.this.startActivityForResult(intent2, PickerConfig.CODE_PICKER_CROP);
                }
            }
        });
    }
}
