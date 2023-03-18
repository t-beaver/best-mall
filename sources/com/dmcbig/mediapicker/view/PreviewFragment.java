package com.dmcbig.mediapicker.view;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.dcloud.android.widget.photoview.OnPhotoTapListener;
import com.dcloud.android.widget.photoview.PhotoView;
import com.dcloud.android.widget.toast.ToastCompat;
import com.dmcbig.mediapicker.MediaPickerR;
import com.dmcbig.mediapicker.PreviewActivity;
import com.dmcbig.mediapicker.entity.Media;
import io.dcloud.common.util.LoadAppUtils;

public class PreviewFragment extends Fragment {
    private PhotoView mPhotoView;
    ImageView play_view;

    /* access modifiers changed from: private */
    public boolean isIntentAvailable(Context context, Intent intent) {
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            return true;
        }
        return false;
    }

    public static PreviewFragment newInstance(Media media, String str) {
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("media", media);
        previewFragment.setArguments(bundle);
        return previewFragment;
    }

    public void onCreate(Bundle bundle) {
        setRetainInstance(true);
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(MediaPickerR.MP_LAYOUT_PREVIEW_FRAGMENT_ITEM, viewGroup, false);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Media media = (Media) getArguments().getParcelable("media");
        this.play_view = (ImageView) view.findViewById(MediaPickerR.MP_ID_PLAY_VIEW);
        PhotoView photoView = (PhotoView) view.findViewById(MediaPickerR.MP_ID_PHOTOVIEW);
        this.mPhotoView = photoView;
        photoView.setMaximumScale(5.0f);
        this.mPhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            public void onPhotoTap(ImageView imageView, float f, float f2) {
                ((PreviewActivity) PreviewFragment.this.getActivity()).setBarStatus();
            }
        });
        setPlayView(media);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        int i = media.mediaType;
        if (i != 1 && i == 3) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Glide.with(getActivity()).load(ContentUris.withAppendedId(uri, (long) media.id)).into((ImageView) this.mPhotoView);
    }

    /* access modifiers changed from: package-private */
    public void setPlayView(final Media media) {
        if (media.mediaType == 3) {
            this.play_view.setVisibility(0);
            this.play_view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent dataAndTypeIntent = LoadAppUtils.getDataAndTypeIntent(PreviewFragment.this.getContext(), media.path, "video/*");
                    PreviewFragment previewFragment = PreviewFragment.this;
                    if (previewFragment.isIntentAvailable(previewFragment.getContext(), dataAndTypeIntent)) {
                        PreviewFragment.this.startActivity(dataAndTypeIntent);
                    } else {
                        ToastCompat.makeText(PreviewFragment.this.getContext(), (CharSequence) PreviewFragment.this.getString(MediaPickerR.MP_STRING_CANT_PLAY_VIDEO), 0).show();
                    }
                }
            });
        }
    }
}
