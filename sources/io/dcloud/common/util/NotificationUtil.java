package io.dcloud.common.util;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.taobao.weex.common.Constants;
import io.dcloud.PdrR;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationUtil {
    private static boolean isNotificationChannel = false;
    static String sChannelId = "DC_LOCAL_NEWS";
    static String sGroupId = "DC_LOCAL_GROUP";

    public static void cancelNotification(Context context, int i) {
        ((NotificationManager) context.getSystemService("notification")).cancel(i);
    }

    public static void createCustomNotification(Context context, String str, Bitmap bitmap, String str2, String str3, int i, PendingIntent pendingIntent) {
        Notification.Builder builder;
        String str4;
        createNotificationChannel(context);
        Context applicationContext = context.getApplicationContext();
        Logger.i("createCustomNotification content=" + str3);
        int i2 = PdrR.DRAWABLE_ICON;
        long currentTimeMillis = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 26) {
            builder = new Notification.Builder(applicationContext, sChannelId);
        } else {
            builder = new Notification.Builder(applicationContext);
        }
        builder.setWhen(currentTimeMillis);
        builder.setSmallIcon(i2);
        builder.setTicker(str);
        builder.setLargeIcon(bitmap);
        int i3 = PdrR.LAYOUT_CUSTION_NOTIFICATION_DCLOUD;
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            i3 = PdrR.getInt(applicationContext, Constants.Name.LAYOUT, "dcloud_custom_notification_transparent");
        } else if (isMiuiRom(applicationContext)) {
            i3 = PdrR.getInt(applicationContext, Constants.Name.LAYOUT, "dcloud_custom_notification_mi");
        }
        RemoteViews remoteViews = new RemoteViews(applicationContext.getPackageName(), i3);
        remoteViews.setImageViewBitmap(PdrR.ID_IMAGE_NOTIFICATION_DCLOUD, bitmap);
        remoteViews.setTextViewText(PdrR.ID_TITLE_NOTIFICATION_DCLOUD, str2);
        remoteViews.setTextViewText(PdrR.ID_TEXT_NOTIFICATION, str3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if (isMiuiRom(applicationContext)) {
            int i4 = simpleDateFormat.getCalendar().get(11);
            if (i4 >= 0 && i4 < 1) {
                str4 = applicationContext.getString(R.string.dcloud_nf_midnight);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build = builder.build();
                build.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build);
            } else if (i4 >= 1 && i4 < 6) {
                str4 = applicationContext.getString(R.string.dcloud_nf_morning);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build2 = builder.build();
                build2.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build2);
            } else if (i4 >= 6 && i4 < 12) {
                str4 = applicationContext.getString(R.string.dcloud_nf_forenoon);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build22 = builder.build();
                build22.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build22);
            } else if (i4 >= 12 && i4 < 13) {
                str4 = applicationContext.getString(R.string.dcloud_nf_noon);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build222 = builder.build();
                build222.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build222);
            } else if (i4 >= 13 && i4 < 18) {
                str4 = applicationContext.getString(R.string.dcloud_nf_afternoon);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build2222 = builder.build();
                build2222.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build2222);
            } else if (i4 >= 18 && i4 < 19) {
                str4 = applicationContext.getString(R.string.dcloud_nf_evening);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build22222 = builder.build();
                build22222.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build22222);
            } else if (i4 >= 19 && i4 < 24) {
                str4 = applicationContext.getString(R.string.dcloud_nf_night);
                remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                Notification build222222 = builder.build();
                build222222.flags |= 16;
                ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build222222);
            }
        }
        str4 = "";
        remoteViews.setTextViewText(PdrR.ID_TIME_NOTIFICATION_DCLOUD, str4 + format.substring(format.length() - 8, format.length() - 3));
        builder.setContent(remoteViews);
        builder.setContentIntent(pendingIntent);
        Notification build2222222 = builder.build();
        build2222222.flags |= 16;
        ((NotificationManager) applicationContext.getSystemService("notification")).notify(i, build2222222);
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26 && !isNotificationChannel) {
            isNotificationChannel = true;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(sGroupId, "local_badge"));
            NotificationChannel notificationChannel = new NotificationChannel(sChannelId, "Information notice", 3);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private static boolean isMiuiRom(Context context) {
        String property = System.getProperty("http.agent");
        return !TextUtils.isEmpty(property) && property.toLowerCase(Locale.ENGLISH).contains("miui");
    }

    public static void showNotification(Context context, String str, String str2, PendingIntent pendingIntent, Bitmap bitmap, int i) {
        Notification notification;
        Notification.Builder builder;
        createNotificationChannel(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 16) {
            if (i2 >= 26) {
                builder = new Notification.Builder(context, sChannelId);
            } else {
                builder = new Notification.Builder(context);
            }
            try {
                builder.setSmallIcon(context.createPackageContext(context.getPackageName(), 2).getResources().getIdentifier(AbsoluteConst.JSON_KEY_ICON, R.drawable.class.getSimpleName(), context.getPackageName()));
            } catch (Exception unused) {
            }
            builder.setLargeIcon(bitmap);
            builder.setContentTitle(str);
            builder.setContentText(str2);
            builder.setDefaults(1);
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        } else {
            Notification notification2 = new Notification();
            try {
                notification2.icon = context.createPackageContext(context.getPackageName(), 2).getResources().getIdentifier(AbsoluteConst.JSON_KEY_ICON, R.drawable.class.getSimpleName(), context.getPackageName());
            } catch (Exception unused2) {
            }
            notification2.largeIcon = bitmap;
            notification2.defaults = 1;
            notification2.flags = 16;
            try {
                notification2.getClass().getDeclaredMethod("setLatestEventInfo", new Class[]{Context.class, CharSequence.class, CharSequence.class, PendingIntent.class}).invoke(notification2, new Object[]{context, str, str2, pendingIntent});
            } catch (Exception e) {
                e.printStackTrace();
            }
            notification = notification2;
        }
        try {
            notificationManager.notify(i, notification);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void showNotification(Context context, String str, String str2, Intent intent, int i, int i2, int i3, boolean z) {
        PendingIntent pendingIntent;
        Notification notification;
        Notification.Builder builder;
        createNotificationChannel(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        int i4 = Build.VERSION.SDK_INT;
        int i5 = i4 >= 23 ? 1140850688 : 1073741824;
        if (z) {
            pendingIntent = PendingIntent.getActivity(context, i3, intent, i5);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, i3, intent, i5);
        }
        if (i4 >= 16) {
            if (i4 >= 26) {
                builder = new Notification.Builder(context, sChannelId);
            } else {
                builder = new Notification.Builder(context);
            }
            if (-1 != i) {
                builder.setSmallIcon(i);
            } else {
                builder.setSmallIcon(context.getApplicationInfo().icon);
            }
            if (-1 != i2) {
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), i2));
            } else {
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon));
            }
            builder.setContentTitle(str);
            builder.setContentText(str2);
            builder.setDefaults(1);
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        } else {
            Notification notification2 = new Notification();
            notification2.icon = context.getApplicationInfo().icon;
            if (-1 != i) {
                notification2.icon = i;
            } else {
                notification2.icon = context.getApplicationInfo().icon;
            }
            if (-1 != i2) {
                notification2.largeIcon = BitmapFactory.decodeResource(context.getResources(), i2);
            } else {
                notification2.largeIcon = BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon);
            }
            notification2.defaults = 1;
            notification2.flags = 16;
            try {
                notification2.getClass().getDeclaredMethod("setLatestEventInfo", new Class[]{Context.class, CharSequence.class, CharSequence.class, PendingIntent.class}).invoke(notification2, new Object[]{context, str, str2, pendingIntent});
            } catch (Exception e) {
                e.printStackTrace();
            }
            notification = notification2;
        }
        try {
            notificationManager.notify(i3, notification);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
