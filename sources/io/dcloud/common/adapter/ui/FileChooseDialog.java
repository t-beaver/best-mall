package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.util.PdrUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileChooseDialog extends Dialog {
    /* access modifiers changed from: private */
    public Activity activity;
    private int onedp = PdrUtil.pxFromDp(1.0f, getContext().getApplicationContext().getResources().getDisplayMetrics());
    private int onesp = PdrUtil.pxFromSp(1.0f, getContext().getApplicationContext().getResources().getDisplayMetrics());
    public List<File> uris = new ArrayList();

    private class GridAdapter extends BaseAdapter {
        private Context context;
        private List<Item> items;

        private class ViewHolder {
            ImageView iv;
            TextView tv;

            private ViewHolder() {
            }
        }

        GridAdapter(Context context2, List<Item> list) {
            this.context = context2;
            this.items = list;
        }

        public int getCount() {
            return this.items.size();
        }

        public Object getItem(int i) {
            return this.items.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = FileChooseDialog.this.createView(this.context, this.items.get(i));
                viewHolder = new ViewHolder();
                ViewGroup viewGroup2 = (ViewGroup) view;
                viewHolder.iv = (ImageView) viewGroup2.getChildAt(0);
                viewHolder.tv = (TextView) viewGroup2.getChildAt(1);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.iv.setImageDrawable(this.items.get(i).icon);
            viewHolder.tv.setText(this.items.get(i).name);
            return view;
        }
    }

    private class Item {
        Intent i;
        Drawable icon;
        String name;

        Item(String str, Drawable drawable, Intent intent) {
            this.name = str;
            this.icon = drawable;
            this.i = intent;
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x01d9 A[LOOP:1: B:23:0x01d3->B:25:0x01d9, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public FileChooseDialog(android.content.Context r18, android.app.Activity r19, android.content.Intent r20) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r20
            r17.<init>(r18)
            r4 = 1
            r0.requestWindowFeature(r4)
            r0.activity = r2
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            r0.uris = r5
            android.content.Context r5 = r17.getContext()
            android.content.Context r5 = r5.getApplicationContext()
            android.content.res.Resources r5 = r5.getResources()
            android.util.DisplayMetrics r5 = r5.getDisplayMetrics()
            r6 = 1065353216(0x3f800000, float:1.0)
            int r5 = io.dcloud.common.util.PdrUtil.pxFromDp(r6, r5)
            r0.onedp = r5
            android.content.Context r5 = r17.getContext()
            android.content.Context r5 = r5.getApplicationContext()
            android.content.res.Resources r5 = r5.getResources()
            android.util.DisplayMetrics r5 = r5.getDisplayMetrics()
            int r5 = io.dcloud.common.util.PdrUtil.pxFromSp(r6, r5)
            r0.onesp = r5
            android.widget.LinearLayout r5 = new android.widget.LinearLayout
            r5.<init>(r1)
            r5.setOrientation(r4)
            android.widget.TextView r6 = new android.widget.TextView
            r6.<init>(r1)
            int r7 = io.dcloud.base.R.string.dcloud_choose_an_action
            r6.setText(r7)
            int r7 = r0.onesp
            int r7 = r7 * 5
            float r7 = (float) r7
            r6.setTextSize(r7)
            r7 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r6.setTextColor(r7)
            android.text.TextPaint r7 = r6.getPaint()
            r7.setFakeBoldText(r4)
            r4 = 17
            r6.setGravity(r4)
            android.widget.LinearLayout$LayoutParams r7 = new android.widget.LinearLayout$LayoutParams
            r8 = -1
            r9 = -2
            r7.<init>(r8, r9)
            r5.addView(r6, r7)
            android.widget.GridView r6 = new android.widget.GridView
            r6.<init>(r1)
            int r7 = r0.onedp
            int r7 = r7 * 10
            r6.setHorizontalSpacing(r7)
            int r7 = r0.onedp
            int r7 = r7 * 10
            r6.setVerticalSpacing(r7)
            r7 = 4
            r6.setNumColumns(r7)
            int r7 = r0.onedp
            int r7 = r7 * 20
            r10 = 0
            r6.setPadding(r10, r7, r10, r7)
            android.widget.LinearLayout$LayoutParams r7 = new android.widget.LinearLayout$LayoutParams
            r7.<init>(r8, r9)
            r5.addView(r6, r7)
            android.view.View r7 = new android.view.View
            r7.<init>(r1)
            r11 = -3355444(0xffffffffffcccccc, float:NaN)
            r7.setBackgroundColor(r11)
            android.view.ViewGroup$LayoutParams r11 = new android.view.ViewGroup$LayoutParams
            int r12 = r0.onedp
            r11.<init>(r8, r12)
            r5.addView(r7, r11)
            android.widget.Button r7 = new android.widget.Button
            r7.<init>(r1)
            int r1 = r7.getPaddingLeft()
            int r11 = r0.onedp
            int r11 = r11 * 15
            int r12 = r7.getPaddingRight()
            int r13 = r0.onedp
            int r13 = r13 * 15
            r7.setPadding(r1, r11, r12, r13)
            r1 = 0
            r7.setBackground(r1)
            r1 = 17039360(0x1040000, float:2.424457E-38)
            r7.setText(r1)
            io.dcloud.common.adapter.ui.FileChooseDialog$1 r1 = new io.dcloud.common.adapter.ui.FileChooseDialog$1
            r1.<init>()
            r7.setOnClickListener(r1)
            r7.setGravity(r4)
            android.widget.LinearLayout$LayoutParams r1 = new android.widget.LinearLayout$LayoutParams
            r1.<init>(r8, r9)
            r5.addView(r7, r1)
            android.view.ViewGroup$LayoutParams r1 = new android.view.ViewGroup$LayoutParams
            r1.<init>(r8, r8)
            r0.setContentView(r5, r1)
            java.lang.String r1 = r20.getType()
            java.lang.String r4 = "video/"
            boolean r1 = r1.startsWith(r4)
            if (r1 == 0) goto L_0x010a
            android.content.Intent r1 = new android.content.Intent
            java.lang.String r4 = "android.media.action.VIDEO_CAPTURE"
            r1.<init>(r4)
            java.lang.String r4 = ".mp4"
            goto L_0x0113
        L_0x010a:
            android.content.Intent r1 = new android.content.Intent
            java.lang.String r4 = "android.media.action.IMAGE_CAPTURE"
            r1.<init>(r4)
            java.lang.String r4 = ".jpg"
        L_0x0113:
            android.content.pm.PackageManager r5 = r19.getPackageManager()
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            java.util.List r8 = r5.queryIntentActivities(r1, r10)
            java.util.Iterator r8 = r8.iterator()     // Catch:{ Exception -> 0x01c4 }
        L_0x0124:
            boolean r9 = r8.hasNext()     // Catch:{ Exception -> 0x01c4 }
            if (r9 == 0) goto L_0x01c0
            java.lang.Object r9 = r8.next()     // Catch:{ Exception -> 0x01c4 }
            android.content.pm.ResolveInfo r9 = (android.content.pm.ResolveInfo) r9     // Catch:{ Exception -> 0x01c4 }
            java.lang.CharSequence r11 = r9.loadLabel(r5)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x01c4 }
            android.content.pm.ActivityInfo r12 = r9.activityInfo     // Catch:{ Exception -> 0x01c4 }
            android.content.pm.ApplicationInfo r12 = r12.applicationInfo     // Catch:{ Exception -> 0x01c4 }
            android.graphics.drawable.Drawable r12 = r5.getApplicationIcon(r12)     // Catch:{ Exception -> 0x01c4 }
            android.content.Intent r13 = new android.content.Intent     // Catch:{ Exception -> 0x01c4 }
            r13.<init>(r1)     // Catch:{ Exception -> 0x01c4 }
            java.io.File r14 = new java.io.File     // Catch:{ Exception -> 0x01c4 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01c4 }
            r15.<init>()     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r10 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceRootDir     // Catch:{ Exception -> 0x01c4 }
            r15.append(r10)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r10 = "/DCIM/captured_image/"
            r15.append(r10)     // Catch:{ Exception -> 0x01c4 }
            java.lang.String r10 = r15.toString()     // Catch:{ Exception -> 0x01c4 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01c4 }
            r15.<init>()     // Catch:{ Exception -> 0x01c4 }
            r18 = r5
            r16 = r6
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01c8 }
            r15.append(r5)     // Catch:{ Exception -> 0x01c8 }
            r15.append(r4)     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r5 = r15.toString()     // Catch:{ Exception -> 0x01c8 }
            r14.<init>(r10, r5)     // Catch:{ Exception -> 0x01c8 }
            java.io.File r5 = r14.getParentFile()     // Catch:{ Exception -> 0x01c8 }
            boolean r5 = r5.exists()     // Catch:{ Exception -> 0x01c8 }
            if (r5 != 0) goto L_0x0185
            java.io.File r5 = r14.getParentFile()     // Catch:{ Exception -> 0x01c8 }
            r5.mkdirs()     // Catch:{ Exception -> 0x01c8 }
        L_0x0185:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01c8 }
            r5.<init>()     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r6 = r19.getPackageName()     // Catch:{ Exception -> 0x01c8 }
            r5.append(r6)     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r6 = ".dc.fileprovider"
            r5.append(r6)     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x01c8 }
            android.net.Uri r5 = androidx.core.content.FileProvider.getUriForFile(r2, r5, r14)     // Catch:{ Exception -> 0x01c8 }
            java.util.List<java.io.File> r6 = r0.uris     // Catch:{ Exception -> 0x01c8 }
            r6.add(r14)     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r6 = "output"
            r13.putExtra(r6, r5)     // Catch:{ Exception -> 0x01c8 }
            android.content.pm.ActivityInfo r5 = r9.activityInfo     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r6 = r5.packageName     // Catch:{ Exception -> 0x01c8 }
            java.lang.String r5 = r5.name     // Catch:{ Exception -> 0x01c8 }
            r13.setClassName(r6, r5)     // Catch:{ Exception -> 0x01c8 }
            io.dcloud.common.adapter.ui.FileChooseDialog$Item r5 = new io.dcloud.common.adapter.ui.FileChooseDialog$Item     // Catch:{ Exception -> 0x01c8 }
            r5.<init>(r11, r12, r13)     // Catch:{ Exception -> 0x01c8 }
            r7.add(r5)     // Catch:{ Exception -> 0x01c8 }
            r5 = r18
            r6 = r16
            r10 = 0
            goto L_0x0124
        L_0x01c0:
            r16 = r6
            r1 = r5
            goto L_0x01ca
        L_0x01c4:
            r18 = r5
            r16 = r6
        L_0x01c8:
            r1 = r18
        L_0x01ca:
            r4 = 0
            java.util.List r4 = r1.queryIntentActivities(r3, r4)
            java.util.Iterator r4 = r4.iterator()
        L_0x01d3:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0206
            java.lang.Object r5 = r4.next()
            android.content.pm.ResolveInfo r5 = (android.content.pm.ResolveInfo) r5
            java.lang.CharSequence r6 = r5.loadLabel(r1)
            java.lang.String r6 = r6.toString()
            android.content.pm.ActivityInfo r8 = r5.activityInfo
            android.content.pm.ApplicationInfo r8 = r8.applicationInfo
            android.graphics.drawable.Drawable r8 = r1.getApplicationIcon(r8)
            android.content.Intent r9 = new android.content.Intent
            r9.<init>(r3)
            android.content.pm.ActivityInfo r5 = r5.activityInfo
            java.lang.String r10 = r5.packageName
            java.lang.String r5 = r5.name
            r9.setClassName(r10, r5)
            io.dcloud.common.adapter.ui.FileChooseDialog$Item r5 = new io.dcloud.common.adapter.ui.FileChooseDialog$Item
            r5.<init>(r6, r8, r9)
            r7.add(r5)
            goto L_0x01d3
        L_0x0206:
            io.dcloud.common.adapter.ui.FileChooseDialog$GridAdapter r1 = new io.dcloud.common.adapter.ui.FileChooseDialog$GridAdapter
            r1.<init>(r2, r7)
            r2 = r16
            r2.setAdapter(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.FileChooseDialog.<init>(android.content.Context, android.app.Activity, android.content.Intent):void");
    }

    /* access modifiers changed from: private */
    public View createView(Context context, final Item item) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(item.icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int i = this.onedp * 50;
        linearLayout.addView(imageView, new LinearLayout.LayoutParams(i, i));
        TextView textView = new TextView(context);
        textView.setText(item.name);
        textView.setTextSize((float) (this.onesp * 4));
        textView.setGravity(17);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = this.onedp * 10;
        linearLayout.addView(textView, layoutParams);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (item.i.getAction() == null || (!item.i.getAction().equals("android.media.action.IMAGE_CAPTURE") && !item.i.getAction().equals("android.media.action.VIDEO_CAPTURE"))) {
                    FileChooseDialog.this.activity.startActivityForResult(item.i, 1);
                } else {
                    PermissionUtil.requestSystemPermissions(FileChooseDialog.this.activity, new String[]{"android.permission.CAMERA"}, PermissionUtil.getRequestCode(), new PermissionUtil.Request() {
                        public void onDenied(String str) {
                        }

                        public void onGranted(String str) {
                            FileChooseDialog.this.activity.startActivityForResult(item.i, 2);
                        }
                    });
                }
            }
        });
        return linearLayout;
    }

    public void show() {
        super.show();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = 80;
        attributes.width = -1;
        attributes.height = -2;
        View decorView = getWindow().getDecorView();
        int i = this.onedp;
        decorView.setPadding(0, i * 20, 0, i * 10);
        getWindow().getDecorView().setBackgroundColor(-1);
        getWindow().setAttributes(attributes);
    }
}
