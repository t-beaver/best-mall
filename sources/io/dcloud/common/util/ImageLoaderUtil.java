package io.dcloud.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.facebook.common.statfs.StatFsHelper;
import com.nostra13.dcloudimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.ImageLoaderConfiguration;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.QueueProcessingType;
import com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.File;
import java.util.ArrayList;

public class ImageLoaderUtil {
    private static ArrayList<String> downloadUrls = new ArrayList<>();

    public static void addNetIconDownloadUrl(String str) {
        if (PdrUtil.isNetPath(str) && !downloadUrls.contains(str)) {
            downloadUrls.add(str);
        }
    }

    public static void clearCache() {
        downloadUrls.clear();
        ImageLoaderL.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static DisplayImageOptions getIconDisplayOptions(Context context) {
        return new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading((Drawable) new ColorDrawable(0)).build();
    }

    public static String getIconLoaclfolder() {
        return DeviceInfo.sBaseFsRootPath + "icons/";
    }

    public static String getOtherImageLoaclfolder() {
        return DeviceInfo.sBaseFsRootPath + "images/";
    }

    public static DisplayImageOptions getStreamIconDisplayOptions(Context context) {
        return new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(DCloudAdapterUtil.getImageOnLoadingId(context)).build();
    }

    public static void initImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            File file = new File(getIconLoaclfolder());
            if (!file.exists()) {
                file.mkdirs();
            }
            ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB, StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2097152)).memoryCacheSize(2097152).threadPriority(3).threadPoolSize(3).denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(getIconDisplayOptions(context)).discCache(new UnlimitedDiscCache(file)).imageDownloader(new BaseImageDownloader(context)).imageDecoder(new BaseImageDecoder(false)).build());
        }
    }

    public static void initImageLoaderL(Context context) {
        if (!ImageLoaderL.getInstance().isInited()) {
            File file = new File(getOtherImageLoaclfolder());
            if (!file.exists()) {
                file.mkdirs();
            }
            ImageLoaderL.getInstance().init(new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB, StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB).tasksProcessingOrder(QueueProcessingType.LIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2097152)).memoryCacheSize(2097152).threadPriority(3).threadPoolSize(3).discCacheFileCount(100).denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(getIconDisplayOptions(context)).discCache(new UnlimitedDiscCache(file)).imageDownloader(new BaseImageDownloader(context)).imageDecoder(new BaseImageDecoder(false)).build());
        }
    }

    public static boolean isDownload(String str) {
        return downloadUrls.contains(str);
    }

    public static void updateIcon(String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = ImageLoader.getInstance().getDiscCache().get(str);
            if (file.exists()) {
                file.delete();
            }
            ImageLoader.getInstance().loadImage(str, (ImageLoadingListener) null);
        }
    }
}
