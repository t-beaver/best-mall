package com.dmcbig.mediapicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dmcbig.mediapicker.MediaPickerR;
import com.dmcbig.mediapicker.entity.Folder;
import com.dmcbig.mediapicker.entity.Media;
import io.dcloud.common.adapter.util.DeviceInfo;
import java.util.ArrayList;

public class FolderAdapter extends BaseAdapter {
    ArrayList<Folder> folders;
    int lastSelected = 0;
    private Context mContext;
    private LayoutInflater mInflater;

    class ViewHolder {
        ImageView cover;
        ImageView indicator;
        TextView name;
        TextView path;
        TextView size;

        ViewHolder(View view) {
            this.cover = (ImageView) view.findViewById(MediaPickerR.MP_ID_COVER);
            this.name = (TextView) view.findViewById(MediaPickerR.MP_ID_NAME);
            this.path = (TextView) view.findViewById(MediaPickerR.MP_ID_PATH);
            this.size = (TextView) view.findViewById(MediaPickerR.MP_ID_SIZE);
            this.indicator = (ImageView) view.findViewById(MediaPickerR.MP_ID_INDICATOR);
            view.setTag(this);
        }
    }

    public FolderAdapter(ArrayList<Folder> arrayList, Context context) {
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.folders = arrayList;
        this.mContext = context;
    }

    public int getCount() {
        return this.folders.size();
    }

    public long getItemId(int i) {
        return 0;
    }

    public ArrayList<Media> getSelectMedias() {
        return this.folders.get(this.lastSelected).getMedias();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        int i2 = 0;
        if (view == null) {
            view = this.mInflater.inflate(MediaPickerR.MP_LAYOUT_FOLDERS_VIEW_ITME, viewGroup, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Folder item = getItem(i);
        if (item.getMedias().size() > 0) {
            RequestManager with = Glide.with(this.mContext);
            with.load(Uri.parse(DeviceInfo.FILE_PROTOCOL + item.getMedias().get(0).path)).into(viewHolder.cover);
        } else {
            viewHolder.cover.setImageDrawable(ContextCompat.getDrawable(this.mContext, MediaPickerR.MP_DRAWABLE_DEFAULT_IMAGE));
        }
        viewHolder.name.setText(item.name);
        TextView textView = viewHolder.size;
        textView.setText(item.getMedias().size() + "" + this.mContext.getString(MediaPickerR.MP_STRING_COUNT_STRING));
        ImageView imageView = viewHolder.indicator;
        if (this.lastSelected != i) {
            i2 = 4;
        }
        imageView.setVisibility(i2);
        return view;
    }

    public void setSelectIndex(int i) {
        if (this.lastSelected != i) {
            this.lastSelected = i;
            notifyDataSetChanged();
        }
    }

    public void updateAdapter(ArrayList<Folder> arrayList) {
        this.folders = arrayList;
        notifyDataSetChanged();
    }

    public Folder getItem(int i) {
        return this.folders.get(i);
    }
}
