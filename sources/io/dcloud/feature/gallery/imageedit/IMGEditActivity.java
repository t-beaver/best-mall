package io.dcloud.feature.gallery.imageedit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import androidx.core.app.ActivityOptionsCompat;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.gallery.imageedit.c.b;
import io.dcloud.feature.gallery.imageedit.c.d;

public class IMGEditActivity extends a {
    private boolean j = false;
    private int k = 0;
    private int l = 0;
    private int m = 80;
    private boolean n = true;
    private boolean o = false;
    private String p;
    int q;

    public /* bridge */ /* synthetic */ void a() {
        super.a();
    }

    public /* bridge */ /* synthetic */ void b() {
        super.b();
    }

    public /* bridge */ /* synthetic */ void c(int i) {
        super.c(i);
    }

    public void e() {
        finish();
    }

    public void f() {
        int i = 0;
        if (!this.j) {
            this.a.a();
            if (this.a.getMode() == b.CLIP) {
                i = 1;
            }
            b(i);
        } else if (PdrUtil.isEmpty(this.p) || !this.p.equals("camera")) {
            finish();
        } else {
            startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 0, ActivityOptionsCompat.makeCustomAnimation(this, 0, 0).toBundle());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0087  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void g() {
        /*
            r6 = this;
            java.lang.String r0 = ""
            android.content.Intent r1 = r6.getIntent()
            java.lang.String r2 = "IMAGE_CROP"
            java.lang.String r1 = r1.getStringExtra(r2)
            android.content.Intent r2 = r6.getIntent()
            java.lang.String r3 = "IMAGE_MEDIA_ID"
            r4 = 0
            int r2 = r2.getIntExtra(r3, r4)
            r6.q = r2
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L_0x0025
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0025 }
            r2.<init>(r1)     // Catch:{ JSONException -> 0x0025 }
            goto L_0x0026
        L_0x0025:
            r2 = 0
        L_0x0026:
            if (r2 == 0) goto L_0x0094
            int r1 = r2.length()
            r3 = 1
            if (r1 <= r3) goto L_0x0094
            java.lang.String r1 = "[^0-9]"
            java.util.regex.Pattern r1 = java.util.regex.Pattern.compile(r1)
            java.lang.String r5 = "width"
            java.lang.String r5 = r2.optString(r5)     // Catch:{ Exception -> 0x005f }
            java.util.regex.Matcher r5 = r1.matcher(r5)     // Catch:{ Exception -> 0x005f }
            java.lang.String r5 = r5.replaceAll(r0)     // Catch:{ Exception -> 0x005f }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Exception -> 0x005f }
            r6.k = r5     // Catch:{ Exception -> 0x005f }
            java.lang.String r5 = "height"
            java.lang.String r5 = r2.optString(r5)     // Catch:{ Exception -> 0x005f }
            java.util.regex.Matcher r1 = r1.matcher(r5)     // Catch:{ Exception -> 0x005f }
            java.lang.String r0 = r1.replaceAll(r0)     // Catch:{ Exception -> 0x005f }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x005f }
            r6.l = r0     // Catch:{ Exception -> 0x005f }
            goto L_0x0060
        L_0x005f:
        L_0x0060:
            r0 = 80
            java.lang.String r1 = "quality"
            int r0 = r2.optInt(r1, r0)
            r6.m = r0
            java.lang.String r0 = "resize"
            boolean r0 = r2.optBoolean(r0, r3)
            r6.n = r0
            int r0 = r6.k
            if (r0 <= 0) goto L_0x007f
            int r1 = r6.l
            if (r1 <= 0) goto L_0x007f
            io.dcloud.feature.gallery.imageedit.view.IMGView r5 = r6.a
            r5.b(r0, r1)
        L_0x007f:
            java.lang.String r0 = "saveToAlbum"
            boolean r1 = r2.has(r0)
            if (r1 == 0) goto L_0x008d
            boolean r0 = r2.optBoolean(r0, r4)
            r6.o = r0
        L_0x008d:
            io.dcloud.feature.gallery.imageedit.c.b r0 = io.dcloud.feature.gallery.imageedit.c.b.CLIP
            r6.a((io.dcloud.feature.gallery.imageedit.c.b) r0)
            r6.j = r3
        L_0x0094:
            android.content.Intent r0 = r6.getIntent()
            java.lang.String r1 = "IMAGE_SOURCE"
            java.lang.String r0 = r0.getStringExtra(r1)
            r6.p = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gallery.imageedit.IMGEditActivity.g():void");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:72:0x01db */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01e4 A[Catch:{ FileNotFoundException -> 0x01ff }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01f9 A[Catch:{ FileNotFoundException -> 0x01ff }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void h() {
        /*
            r25 = this;
            r1 = r25
            android.content.Intent r0 = r25.getIntent()
            java.lang.String r2 = "IMAGE_SAVE_PATH"
            java.lang.String r0 = r0.getStringExtra(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            long r3 = java.lang.System.currentTimeMillis()
            r2.append(r3)
            java.lang.String r3 = ".jpg"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            r4 = 29
            java.lang.String r5 = "/DImage/"
            if (r3 == 0) goto L_0x0068
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 < r4) goto L_0x004c
            java.lang.String r0 = android.os.Environment.DIRECTORY_PICTURES
            java.io.File r0 = r1.getExternalFilesDir(r0)
            java.lang.String r0 = r0.getAbsolutePath()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            r3.append(r5)
            r3.append(r2)
            java.lang.String r0 = r3.toString()
            goto L_0x0068
        L_0x004c:
            java.lang.String r0 = android.os.Environment.DIRECTORY_DCIM
            java.io.File r0 = android.os.Environment.getExternalStoragePublicDirectory(r0)
            java.lang.String r0 = r0.getAbsolutePath()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            r3.append(r5)
            r3.append(r2)
            java.lang.String r0 = r3.toString()
        L_0x0068:
            r3 = r0
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            r6 = 0
            if (r0 != 0) goto L_0x02bf
            io.dcloud.feature.gallery.imageedit.view.IMGView r0 = r1.a
            android.graphics.Bitmap r0 = r0.i()
            if (r0 == 0) goto L_0x02bf
            int r7 = r1.q
            r8 = -1001(0xfffffffffffffc17, float:NaN)
            r10 = -1
            r11 = 100
            if (r7 != r8) goto L_0x00bc
            java.io.File r2 = new java.io.File
            r2.<init>(r3)
            boolean r3 = r2.exists()
            if (r3 != 0) goto L_0x008f
            r25.finish()
        L_0x008f:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x00ab }
            r3.<init>(r2)     // Catch:{ FileNotFoundException -> 0x00ab }
            android.graphics.Bitmap r0 = r1.a((android.graphics.Bitmap) r0)     // Catch:{ FileNotFoundException -> 0x00ab }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x00ab }
            int r4 = r1.m     // Catch:{ FileNotFoundException -> 0x00ab }
            if (r4 <= r11) goto L_0x00a1
            r9 = 100
            goto L_0x00a7
        L_0x00a1:
            if (r4 >= 0) goto L_0x00a6
            r9 = 80
            goto L_0x00a7
        L_0x00a6:
            r9 = r4
        L_0x00a7:
            r0.compress(r2, r9, r3)     // Catch:{ FileNotFoundException -> 0x00ab }
            goto L_0x00af
        L_0x00ab:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00af:
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            r1.setResult(r10, r0)
            r25.finish()
            goto L_0x02be
        L_0x00bc:
            boolean r7 = r1.j
            java.lang.String r8 = "PATH"
            java.lang.String r12 = "_id"
            java.lang.String r13 = "IMAGE_INDEX"
            if (r7 == 0) goto L_0x0146
            boolean r7 = r1.o
            if (r7 != 0) goto L_0x0146
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.io.File r4 = r25.getExternalCacheDir()
            java.lang.String r4 = r4.getPath()
            r3.append(r4)
            r3.append(r5)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            java.io.File r4 = r3.getParentFile()     // Catch:{ IOException -> 0x011f }
            boolean r4 = r4.exists()     // Catch:{ IOException -> 0x011f }
            if (r4 != 0) goto L_0x00fa
            java.io.File r4 = r3.getParentFile()     // Catch:{ IOException -> 0x011f }
            r4.mkdirs()     // Catch:{ IOException -> 0x011f }
        L_0x00fa:
            boolean r4 = r3.exists()     // Catch:{ IOException -> 0x011f }
            if (r4 != 0) goto L_0x0103
            r3.createNewFile()     // Catch:{ IOException -> 0x011f }
        L_0x0103:
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x011f }
            r4.<init>(r3)     // Catch:{ IOException -> 0x011f }
            android.graphics.Bitmap r0 = r1.a((android.graphics.Bitmap) r0)     // Catch:{ IOException -> 0x011f }
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ IOException -> 0x011f }
            int r5 = r1.m     // Catch:{ IOException -> 0x011f }
            if (r5 <= r11) goto L_0x0115
            r9 = 100
            goto L_0x011b
        L_0x0115:
            if (r5 >= 0) goto L_0x011a
            r9 = 80
            goto L_0x011b
        L_0x011a:
            r9 = r5
        L_0x011b:
            r0.compress(r3, r9, r4)     // Catch:{ IOException -> 0x011f }
            goto L_0x0125
        L_0x011f:
            r1.setResult(r6)
            r25.finish()
        L_0x0125:
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            r3 = 2147483647(0x7fffffff, float:NaN)
            r0.putExtra(r12, r3)
            android.content.Intent r3 = r25.getIntent()
            int r3 = r3.getIntExtra(r13, r10)
            r0.putExtra(r13, r3)
            r0.putExtra(r8, r2)
            r1.setResult(r10, r0)
            r25.finish()
            goto L_0x02be
        L_0x0146:
            int r5 = android.os.Build.VERSION.SDK_INT
            java.lang.String r7 = "_data"
            java.lang.String r14 = "image/jpeg"
            java.lang.String r15 = "datetaken"
            java.lang.String r9 = "_display_name"
            java.lang.String r10 = "mime_type"
            if (r5 < r4) goto L_0x01be
            android.content.ContentValues r3 = new android.content.ContentValues
            r3.<init>()
            long r4 = java.lang.System.currentTimeMillis()
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r3.put(r15, r4)
            r3.put(r10, r14)
            java.lang.Integer r4 = java.lang.Integer.valueOf(r6)
            java.lang.String r5 = "is_pending"
            r3.put(r5, r4)
            r3.put(r9, r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = android.os.Environment.DIRECTORY_PICTURES
            r2.append(r4)
            java.lang.String r4 = "/DImage"
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            java.lang.String r4 = "relative_path"
            r3.put(r4, r2)
            android.content.ContentResolver r2 = r25.getContentResolver()
            android.net.Uri r4 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            android.net.Uri r2 = r2.insert(r4, r3)
            android.content.ContentResolver r3 = r25.getContentResolver()     // Catch:{ FileNotFoundException -> 0x01bb }
            java.io.OutputStream r3 = r3.openOutputStream(r2)     // Catch:{ FileNotFoundException -> 0x01bb }
            boolean r4 = r1.j     // Catch:{ FileNotFoundException -> 0x01bb }
            if (r4 == 0) goto L_0x01b6
            android.graphics.Bitmap r0 = r1.a((android.graphics.Bitmap) r0)     // Catch:{ FileNotFoundException -> 0x01bb }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x01bb }
            int r5 = r1.m     // Catch:{ FileNotFoundException -> 0x01bb }
            if (r5 <= r11) goto L_0x01ae
            r5 = 100
            goto L_0x01b2
        L_0x01ae:
            if (r5 >= 0) goto L_0x01b2
            r5 = 80
        L_0x01b2:
            r0.compress(r4, r5, r3)     // Catch:{ FileNotFoundException -> 0x01bb }
            goto L_0x01bb
        L_0x01b6:
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x01bb }
            r0.compress(r4, r11, r3)     // Catch:{ FileNotFoundException -> 0x01bb }
        L_0x01bb:
            r18 = r2
            goto L_0x0224
        L_0x01be:
            java.io.File r2 = new java.io.File
            r2.<init>(r3)
            boolean r4 = r2.exists()
            if (r4 != 0) goto L_0x01db
            java.io.File r4 = r2.getParentFile()     // Catch:{ IOException -> 0x01db }
            if (r4 == 0) goto L_0x01d8
            boolean r5 = r4.exists()     // Catch:{ IOException -> 0x01db }
            if (r5 != 0) goto L_0x01d8
            r4.mkdirs()     // Catch:{ IOException -> 0x01db }
        L_0x01d8:
            r2.createNewFile()     // Catch:{ IOException -> 0x01db }
        L_0x01db:
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x01ff }
            r4.<init>(r2)     // Catch:{ FileNotFoundException -> 0x01ff }
            boolean r2 = r1.j     // Catch:{ FileNotFoundException -> 0x01ff }
            if (r2 == 0) goto L_0x01f9
            android.graphics.Bitmap r0 = r1.a((android.graphics.Bitmap) r0)     // Catch:{ FileNotFoundException -> 0x01ff }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x01ff }
            int r5 = r1.m     // Catch:{ FileNotFoundException -> 0x01ff }
            if (r5 <= r11) goto L_0x01f1
            r5 = 100
            goto L_0x01f5
        L_0x01f1:
            if (r5 >= 0) goto L_0x01f5
            r5 = 80
        L_0x01f5:
            r0.compress(r2, r5, r4)     // Catch:{ FileNotFoundException -> 0x01ff }
            goto L_0x0203
        L_0x01f9:
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x01ff }
            r0.compress(r2, r11, r4)     // Catch:{ FileNotFoundException -> 0x01ff }
            goto L_0x0203
        L_0x01ff:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0203:
            android.content.ContentValues r0 = new android.content.ContentValues
            r0.<init>()
            r0.put(r7, r3)
            long r2 = java.lang.System.currentTimeMillis()
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            r0.put(r15, r2)
            r0.put(r10, r14)
            android.content.ContentResolver r2 = r25.getContentResolver()
            android.net.Uri r3 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            android.net.Uri r2 = r2.insert(r3, r0)
            goto L_0x01bb
        L_0x0224:
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            android.content.ContentResolver r17 = r25.getContentResolver()
            java.lang.String r19 = "_display_name"
            java.lang.String r20 = "date_added"
            java.lang.String r21 = "mime_type"
            java.lang.String r22 = "_size"
            java.lang.String r23 = "_data"
            java.lang.String r24 = "_id"
            java.lang.String[] r19 = new java.lang.String[]{r19, r20, r21, r22, r23, r24}
            r20 = 0
            r21 = 0
            r22 = 0
            android.database.Cursor r2 = r17.query(r18, r19, r20, r21, r22)
            if (r2 == 0) goto L_0x02b7
            r2.moveToNext()
            int r3 = r2.getColumnIndex(r9)
            java.lang.String r3 = r2.getString(r3)
            java.lang.String r4 = "date_added"
            int r5 = r2.getColumnIndex(r4)
            long r5 = r2.getLong(r5)
            int r11 = r2.getColumnIndex(r10)
            int r11 = r2.getInt(r11)
            java.lang.String r14 = "_size"
            int r15 = r2.getColumnIndex(r14)
            r16 = r13
            r17 = r14
            long r13 = r2.getLong(r15)
            int r15 = r2.getColumnIndex(r12)
            int r15 = r2.getInt(r15)
            int r7 = r2.getColumnIndexOrThrow(r7)
            java.lang.String r7 = r2.getString(r7)
            r18 = r8
            java.lang.String r8 = r1.a((java.lang.String) r7)
            r2.close()
            r0.putExtra(r9, r3)
            r0.putExtra(r4, r5)
            r0.putExtra(r10, r11)
            r2 = r17
            r0.putExtra(r2, r13)
            r0.putExtra(r12, r15)
            java.lang.String r2 = "PARENTPATH"
            r0.putExtra(r2, r8)
            android.content.Intent r2 = r25.getIntent()
            r3 = r16
            r4 = -1
            int r2 = r2.getIntExtra(r3, r4)
            r0.putExtra(r3, r2)
            r2 = r18
            r0.putExtra(r2, r7)
            goto L_0x02b8
        L_0x02b7:
            r4 = -1
        L_0x02b8:
            r1.setResult(r4, r0)
            r25.finish()
        L_0x02be:
            return
        L_0x02bf:
            r1.setResult(r6)
            r25.finish()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gallery.imageedit.IMGEditActivity.h():void");
    }

    public void i() {
        this.a.b();
        if (this.j) {
            h();
        } else {
            b(this.a.getMode() == b.CLIP ? 1 : 0);
        }
    }

    public void j() {
        this.a.h();
    }

    public void k() {
        this.a.c();
    }

    public /* bridge */ /* synthetic */ void l() {
        super.l();
    }

    public void m() {
        b mode = this.a.getMode();
        if (mode == b.DOODLE) {
            this.a.k();
        } else if (mode == b.MOSAIC) {
            this.a.l();
        }
    }

    public /* bridge */ /* synthetic */ void n() {
        super.n();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public /* bridge */ /* synthetic */ void onBackPressed() {
        super.onBackPressed();
    }

    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    public /* bridge */ /* synthetic */ void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

    public /* bridge */ /* synthetic */ void onShow(DialogInterface dialogInterface) {
        super.onShow(dialogInterface);
    }

    public void a(d dVar) {
        this.a.a(dVar);
    }

    public /* bridge */ /* synthetic */ void b(int i) {
        super.b(i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0044 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0045  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap c() {
        /*
            r8 = this;
            android.content.Intent r0 = r8.getIntent()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.String r2 = "IMAGE_URI"
            android.os.Parcelable r0 = r0.getParcelableExtra(r2)
            android.net.Uri r0 = (android.net.Uri) r0
            if (r0 != 0) goto L_0x0013
            return r1
        L_0x0013:
            java.lang.String r2 = r0.getPath()
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0041
            java.lang.String r2 = r0.getScheme()
            r2.hashCode()
            java.lang.String r3 = "file"
            boolean r3 = r2.equals(r3)
            if (r3 != 0) goto L_0x003b
            java.lang.String r3 = "asset"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0035
            goto L_0x0041
        L_0x0035:
            io.dcloud.feature.gallery.imageedit.c.h.a r2 = new io.dcloud.feature.gallery.imageedit.c.h.a
            r2.<init>(r8, r0)
            goto L_0x0042
        L_0x003b:
            io.dcloud.feature.gallery.imageedit.c.h.c r2 = new io.dcloud.feature.gallery.imageedit.c.h.c
            r2.<init>(r0)
            goto L_0x0042
        L_0x0041:
            r2 = r1
        L_0x0042:
            if (r2 != 0) goto L_0x0045
            return r1
        L_0x0045:
            android.graphics.BitmapFactory$Options r3 = new android.graphics.BitmapFactory$Options
            r3.<init>()
            r4 = 1
            r3.inSampleSize = r4
            r3.inJustDecodeBounds = r4
            r2.a(r3)
            int r4 = r3.outWidth
            r5 = 1149239296(0x44800000, float:1024.0)
            r6 = 1065353216(0x3f800000, float:1.0)
            r7 = 1024(0x400, float:1.435E-42)
            if (r4 <= r7) goto L_0x006a
            float r4 = (float) r4
            float r4 = r4 * r6
            float r4 = r4 / r5
            int r4 = java.lang.Math.round(r4)
            int r4 = io.dcloud.feature.gallery.imageedit.c.k.b.a(r4)
            r3.inSampleSize = r4
        L_0x006a:
            int r4 = r3.outHeight
            if (r4 <= r7) goto L_0x0082
            int r7 = r3.inSampleSize
            float r4 = (float) r4
            float r4 = r4 * r6
            float r4 = r4 / r5
            int r4 = java.lang.Math.round(r4)
            int r4 = io.dcloud.feature.gallery.imageedit.c.k.b.a(r4)
            int r4 = java.lang.Math.max(r7, r4)
            r3.inSampleSize = r4
        L_0x0082:
            r4 = 0
            r3.inJustDecodeBounds = r4
            android.graphics.Bitmap r2 = r2.a(r3)
            if (r2 != 0) goto L_0x00cd
            android.content.Intent r2 = r8.getIntent()
            java.lang.String r4 = "IMAGE_MEDIA_ID"
            r5 = -1
            int r2 = r2.getIntExtra(r4, r5)
            if (r2 != r5) goto L_0x0099
            return r1
        L_0x0099:
            r4 = -1001(0xfffffffffffffc17, float:NaN)
            if (r2 != r4) goto L_0x00b5
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x00ac }
            java.io.File r4 = new java.io.File     // Catch:{ FileNotFoundException -> 0x00ac }
            java.lang.String r0 = r0.getPath()     // Catch:{ FileNotFoundException -> 0x00ac }
            r4.<init>(r0)     // Catch:{ FileNotFoundException -> 0x00ac }
            r2.<init>(r4)     // Catch:{ FileNotFoundException -> 0x00ac }
            goto L_0x00ad
        L_0x00ac:
            r2 = r1
        L_0x00ad:
            if (r2 != 0) goto L_0x00b0
            return r1
        L_0x00b0:
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r2, r1, r3)
            return r0
        L_0x00b5:
            android.net.Uri r0 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            long r4 = (long) r2
            android.net.Uri r0 = android.content.ContentUris.withAppendedId(r0, r4)
            android.content.ContentResolver r2 = r8.getContentResolver()     // Catch:{ FileNotFoundException -> 0x00c5 }
            java.io.InputStream r0 = r2.openInputStream(r0)     // Catch:{ FileNotFoundException -> 0x00c5 }
            goto L_0x00c6
        L_0x00c5:
            r0 = r1
        L_0x00c6:
            if (r0 != 0) goto L_0x00c9
            return r1
        L_0x00c9:
            android.graphics.Bitmap r2 = io.dcloud.feature.gallery.imageedit.c.k.a.a((java.io.InputStream) r0, (android.graphics.BitmapFactory.Options) r3)
        L_0x00cd:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gallery.imageedit.IMGEditActivity.c():android.graphics.Bitmap");
    }

    public void a(b bVar) {
        if (this.a.getMode() == bVar) {
            bVar = b.NONE;
        }
        this.a.setMode(bVar);
        n();
        if (bVar == b.CLIP) {
            b(1);
        }
    }

    private Bitmap a(Bitmap bitmap) {
        if (!this.n || this.l <= 0 || this.k <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(((float) this.k) / ((float) bitmap.getWidth()), ((float) this.l) / ((float) bitmap.getHeight()));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public void a(int i) {
        this.a.setPenColor(i);
    }

    public String a(String str) {
        String[] split = str.split("/");
        return split[split.length - 2];
    }
}
