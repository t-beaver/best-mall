package com.dmcbig.mediapicker.data;

import com.dmcbig.mediapicker.entity.Folder;
import java.util.ArrayList;

public interface DataCallback {
    void onData(ArrayList<Folder> arrayList);
}
