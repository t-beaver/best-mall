package com.dmcbig.mediapicker.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dcloud.android.widget.toast.ToastCompat;
import com.dmcbig.mediapicker.MediaPickerR;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.adapter.MediaGridAdapter;
import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.utils.FileUtils;
import com.dmcbig.mediapicker.utils.ScreenUtils;
import io.dcloud.common.util.HarmonyUtils;
import io.dcloud.common.util.ThreadPool;
import java.io.IOException;
import java.util.ArrayList;

public class MediaGridAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    FileUtils fileUtils = new FileUtils();
    private final Handler handler;
    boolean isSingle = false;
    /* access modifiers changed from: private */
    public OnRecyclerViewItemClickListener mOnItemClickListener = null;
    /* access modifiers changed from: private */
    public OnPickerSelectMaxListener mOnMaxListener = null;
    long maxSelect;
    long maxSize;
    ArrayList<Media> medias;
    ArrayList<Media> selectMedias = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView check_image;
        public RelativeLayout gif_info;
        public View mask_view;
        public ImageView media_image;
        public TextView textView_size;
        public RelativeLayout video_info;

        public MyViewHolder(View view) {
            super(view);
            this.media_image = (ImageView) view.findViewById(MediaPickerR.MP_ID_MEDIA_IMAGE);
            this.check_image = (ImageView) view.findViewById(MediaPickerR.MP_ID_CHECK_IMAGE);
            this.mask_view = view.findViewById(MediaPickerR.MP_ID_MASK_VIEW);
            this.video_info = (RelativeLayout) view.findViewById(MediaPickerR.MP_ID_VIDEO_INFO);
            this.gif_info = (RelativeLayout) view.findViewById(MediaPickerR.MP_ID_GIF_INFO);
            this.textView_size = (TextView) view.findViewById(MediaPickerR.MP_ID_TEXTVIEW_SIZE);
            this.itemView.setLayoutParams(new AbsListView.LayoutParams(-1, MediaGridAdapter.this.getItemWidth()));
        }
    }

    public interface OnPickerSelectMaxListener {
        void onMaxed();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Media media, ArrayList<Media> arrayList);
    }

    public MediaGridAdapter(ArrayList<Media> arrayList, Context context2, ArrayList<Media> arrayList2, int i, long j, boolean z) {
        if (arrayList2 != null) {
            this.selectMedias = arrayList2;
        }
        this.maxSelect = (long) i;
        this.maxSize = j;
        this.medias = arrayList;
        this.context = context2;
        this.isSingle = z;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public int getItemCount() {
        return this.medias.size();
    }

    public int getItemIndex(Object obj) {
        return this.medias.indexOf(obj);
    }

    /* access modifiers changed from: package-private */
    public int getItemWidth() {
        int screenWidth = ScreenUtils.getScreenWidth(this.context);
        int i = PickerConfig.GridSpanCount;
        return (screenWidth / i) - i;
    }

    public ArrayList<Media> getMedias() {
        return this.medias;
    }

    public ArrayList<Media> getSelectMedias() {
        return this.selectMedias;
    }

    public int isSelect(Media media) {
        if (this.selectMedias.size() <= 0) {
            return -1;
        }
        for (int i = 0; i < this.selectMedias.size(); i++) {
            Media media2 = this.selectMedias.get(i);
            if (media2.path.equals(media.path)) {
                media2.id = media.id;
                return i;
            }
        }
        return -1;
    }

    public /* synthetic */ void lambda$onBindViewHolder$1$MediaGridAdapter(Uri uri, Media media, MyViewHolder myViewHolder) {
        Bitmap bitmap;
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                bitmap = this.context.getContentResolver().loadThumbnail(uri, new Size(getItemWidth(), getItemWidth()), (CancellationSignal) null);
            } else if (media.mediaType == 3) {
                bitmap = MediaStore.Video.Thumbnails.getThumbnail(this.context.getContentResolver(), (long) media.id, 1, (BitmapFactory.Options) null);
            } else {
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(this.context.getContentResolver(), (long) media.id, 1, (BitmapFactory.Options) null);
            }
            this.handler.post(new Runnable(bitmap) {
                public final /* synthetic */ Bitmap f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    MediaGridAdapter.MyViewHolder.this.media_image.setImageBitmap(this.f$1);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mOnItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnMaxListener(OnPickerSelectMaxListener onPickerSelectMaxListener) {
        this.mOnMaxListener = onPickerSelectMaxListener;
    }

    public void setSelectMedias(Media media) {
        int isSelect = isSelect(media);
        if (isSelect == -1) {
            this.selectMedias.add(media);
        } else {
            this.selectMedias.remove(isSelect);
        }
    }

    public void updateAdapter(ArrayList<Media> arrayList) {
        this.medias = arrayList;
        notifyDataSetChanged();
    }

    public void updateSelectAdapter(ArrayList<Media> arrayList) {
        if (arrayList != null) {
            this.selectMedias = arrayList;
        }
        notifyDataSetChanged();
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        int i2;
        Context context2;
        final Media media = this.medias.get(i);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        int i3 = media.mediaType;
        if (i3 != 1 && i3 == 3) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Uri withAppendedId = ContentUris.withAppendedId(uri, (long) media.id);
        if (HarmonyUtils.isHarmonyOs()) {
            ThreadPool.self().addThreadTask(new Runnable(withAppendedId, media, myViewHolder) {
                public final /* synthetic */ Uri f$1;
                public final /* synthetic */ Media f$2;
                public final /* synthetic */ MediaGridAdapter.MyViewHolder f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                public final void run() {
                    MediaGridAdapter.this.lambda$onBindViewHolder$1$MediaGridAdapter(this.f$1, this.f$2, this.f$3);
                }
            });
        } else {
            Glide.with(this.context).load(withAppendedId).into(myViewHolder.media_image);
        }
        int i4 = 0;
        if (media.mediaType == 3) {
            myViewHolder.gif_info.setVisibility(4);
            myViewHolder.video_info.setVisibility(0);
            myViewHolder.textView_size.setText(this.fileUtils.getSizeByUnit((double) media.size));
        } else {
            myViewHolder.video_info.setVisibility(4);
            myViewHolder.gif_info.setVisibility(".gif".equalsIgnoreCase(media.extension) ? 0 : 4);
        }
        int isSelect = isSelect(media);
        if (!this.isSingle) {
            View view = myViewHolder.mask_view;
            if (isSelect < 0) {
                i4 = 4;
            }
            view.setVisibility(i4);
            ImageView imageView = myViewHolder.check_image;
            if (isSelect >= 0) {
                context2 = this.context;
                i2 = MediaPickerR.MP_DRAWABLE_BNT_SELECTED;
            } else {
                context2 = this.context;
                i2 = MediaPickerR.MP_DRAWABLE_BNT_UNSELECTED;
            }
            imageView.setImageDrawable(ContextCompat.getDrawable(context2, i2));
        } else {
            myViewHolder.check_image.setVisibility(8);
            myViewHolder.mask_view.setVisibility(8);
        }
        myViewHolder.media_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int i;
                Context context;
                MediaGridAdapter mediaGridAdapter = MediaGridAdapter.this;
                if (mediaGridAdapter.isSingle) {
                    mediaGridAdapter.mOnItemClickListener.onItemClick(view, media, (ArrayList<Media>) null);
                    return;
                }
                int isSelect = mediaGridAdapter.isSelect(media);
                MediaGridAdapter mediaGridAdapter2 = MediaGridAdapter.this;
                if (((long) MediaGridAdapter.this.selectMedias.size()) < mediaGridAdapter2.maxSelect || isSelect >= 0) {
                    if (media.size > mediaGridAdapter2.maxSize) {
                        Context context2 = mediaGridAdapter2.context;
                        ToastCompat.makeText(context2, (CharSequence) MediaGridAdapter.this.context.getString(MediaPickerR.MP_STRING_MSG_SIZE_LIMIT) + FileUtils.fileSize(MediaGridAdapter.this.maxSize), 1).show();
                        return;
                    }
                    myViewHolder.mask_view.setVisibility(isSelect >= 0 ? 4 : 0);
                    ImageView imageView = myViewHolder.check_image;
                    if (isSelect >= 0) {
                        context = MediaGridAdapter.this.context;
                        i = MediaPickerR.MP_DRAWABLE_BNT_UNSELECTED;
                    } else {
                        context = MediaGridAdapter.this.context;
                        i = MediaPickerR.MP_DRAWABLE_BNT_SELECTED;
                    }
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, i));
                    MediaGridAdapter.this.setSelectMedias(media);
                    MediaGridAdapter.this.mOnItemClickListener.onItemClick(view, media, MediaGridAdapter.this.selectMedias);
                } else if (mediaGridAdapter2.mOnMaxListener != null) {
                    MediaGridAdapter.this.mOnMaxListener.onMaxed();
                }
            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(MediaPickerR.MP_LAYOUT_MEDIA_VIEW_ITEM, viewGroup, false));
    }
}
