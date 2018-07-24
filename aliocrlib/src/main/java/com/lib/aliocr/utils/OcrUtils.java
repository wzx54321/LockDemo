package com.lib.aliocr.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.lib.aliocr.R;
import com.lib.aliocr.widget.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Encoder;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class OcrUtils {
    public static String getImageStr(String imgFile) {
        InputStream inputStream;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public static Bitmap byte2bitmap(byte[] data) {

        if (null == data) {

            return null;

        }

        return BitmapFactory.decodeByteArray(data,
                0,
                data.length);

    }

    public static boolean saveBitmap(Bitmap bitmap,
                                     String path) {
        return saveBitmap(bitmap,
                path,
                100);
    }

    /**
     * 将bitmap位图保存到path路径下，图片格式为Bitmap.CompressFormat.JPEG，质量为100
     *
     * @param bitmap
     * @param path
     * @param quality 压缩的比率（1-100）
     */

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean saveBitmap(Bitmap bitmap,
                                     String path,
                                     int quality) {

        try {

            File file = new File(path);

            File parent = file.getParentFile();

            if (!parent.exists()) {

                parent.mkdirs();

            }

            // if(file.exists()){
            // FileUtil.deleteFile(file);
            // }

            FileOutputStream fos = new FileOutputStream(file);

            boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG,
                    quality,
                    fos);
            fos.flush();

            fos.close();

            return b;

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.isRecycled();
            }
        }

        return false;

    }


    /**
     * 图片压缩并保存到文件(是否压缩都保存)
     */
    public static boolean compressSmallImage(Bitmap image,
                                             File file,
                                             int kb) {
        return compressSmallImage(image,
                file,
                kb,
                true);
    }

    /**
     * 图片压缩并保存到文件
     *
     * @param image      源文件
     * @param file       要保存到的file
     * @param kb         限制的size
     * @param iSsaveFile (可为false,如File已存在但无压缩的情况下可设置为false)
     * @return 是否存储了文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean compressSmallImage(Bitmap image,
                                             File file,
                                             int kb,
                                             boolean iSsaveFile) {
        if (image == null)
            return false;
        Boolean isCompressed = false;
        boolean isSaveFileSuccessed = false;// 是否存储成功了文件夹
        File parent = file.getParentFile();

        if (!parent.exists()) {

            parent.mkdirs();

        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = null;
        image.compress(Bitmap.CompressFormat.JPEG,
                100,
                baos);

        int options = 100;
        Bitmap temp;
        while ((baos.size() <= 0 ? baos.toByteArray().length : baos.size()) / 1024 > kb) {
            isCompressed = true;
            baos.reset();
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG,
                    options,
                    baos);
        }
        byte[] byteArray = baos.toByteArray();
        try {
            // 不管有无压缩，都写入文件
            if (iSsaveFile) {
                out = new FileOutputStream(file);
                out.write(byteArray,
                        0,
                        byteArray.length);
                isSaveFileSuccessed = true;
            } else {
                // iSsaveFile==false 时，只有压缩后写入文件
                if (isCompressed) {
                    out = new FileOutputStream(file);
                    out.write(byteArray,
                            0,
                            byteArray.length);
                    isSaveFileSuccessed = true;
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isSaveFileSuccessed) {

        }
        return isSaveFileSuccessed;
    }


    public static String getPath(Context context,
                                 Uri uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String docId;
                String[] split;
                String type;
                if (isExternalStorageDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else {
                    if (isDownloadsDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        Uri split1 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(docId));
                        return getDataColumn(context,
                                split1,
                                null,
                                null);
                    }

                    if (isMediaDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        split = docId.split(":");
                        type = split[0];
                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{split[1]};
                        return getDataColumn(context,
                                contentUri,
                                "_id=?",
                                selectionArgs);
                    }
                }
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context,
                    uri,
                    null,
                    null);
        }

        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }


        return null;
    }

    public static String getDataColumn(Context context,
                                       Uri uri,
                                       String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        try {
            cursor = context.getContentResolver()
                    .query(uri,
                            projection,
                            selection,
                            selectionArgs,
                            null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow("_data");
                String var9 = cursor.getString(index);
                return var9;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private static Intent getGalleryIntent(Context context, boolean includeDocuments) {
        PackageManager packageManager = context.getPackageManager();
        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);

        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }
        Intent target;
        if (galleryIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = galleryIntents.get(galleryIntents.size() - 1);
            galleryIntents.remove(galleryIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, context.getString(R.string.pick_image_intent_chooser_title));

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, galleryIntents.toArray(new Parcelable[galleryIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get all Gallery intents for getting image from one of the apps of the device that handle images.
     */
    private static List<Intent> getGalleryIntents(@NonNull
                                                          PackageManager packageManager, String action, boolean includeDocuments) {
        List<Intent> intents = new ArrayList<>();
        Intent galleryIntent = Intent.ACTION_GET_CONTENT.equals(action) ? new Intent(action)
                : new Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intents.add(intent);
        }

        // remove documents intent
        if (!includeDocuments) {
            for (Intent intent : intents) {
                if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    intents.remove(intent);
                    break;
                }
            }
        }
        return intents;
    }


    public static boolean isReadExternalStoragePermissionsRequired(@NonNull Context context, @NonNull
            Uri uri) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                isUriRequiresPermissions(context, uri);
    }


    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     * Only relevant for API version 23 and above.
     *
     * @param context used to access Android APIs, like content resolve, it is your activity/fragment/widget.
     * @param uri     the result URI of image pick.
     */
    public static boolean isUriRequiresPermissions(@NonNull Context context, @NonNull Uri uri) {
        try {
            ContentResolver resolver = context.getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (Exception e) {
            return true;
        }
    }


    public static void beginCrop(Uri source, Activity activity, Fragment fragment) {
        Uri destination = Uri.fromFile(new File(activity.getCacheDir(), "cropped"));

        Crop crop = Crop.of(source, destination).withAspect(1, 1.3f);
        if (fragment == null)
            crop.start(activity);
        else
            crop.start(activity, fragment);

    }
}
