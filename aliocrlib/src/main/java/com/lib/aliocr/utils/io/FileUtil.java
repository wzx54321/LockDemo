package com.lib.aliocr.utils.io;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Date;




/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

@SuppressWarnings("WeakerAccess")
public class FileUtil {
    private static final String TAG = "FileUtil";
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;


    /*
      ********************************************读写***************************
     */

    /**
     * Returns a human-readable version of the file size, where the input
     * represents a specific number of bytes.
     *
     * @param size the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String formatSize(long size) {
        float ONE_KB = 1024F;
        float ONE_MB = ONE_KB * ONE_KB;
        float ONE_GB = ONE_KB * ONE_MB;
        String displaySize;
        DecimalFormat df = new DecimalFormat("0.0");
        if (size >= ONE_KB && size < ONE_MB) {
            displaySize = String.valueOf(df.format(size / ONE_KB)) + " KB";
        } else if (size >= ONE_MB && size < ONE_GB) {
            displaySize = String.valueOf(df.format(size / ONE_MB)) + " MB";
        } else if (size >= ONE_GB) {
            displaySize = String.valueOf(df.format(size / ONE_GB)) + " GB";
        } else if (size == 0) {
            displaySize = " 0KB";
        } else {
            displaySize = "1KB";
            Log.d("formatSize",
                    String.valueOf(df.format(size)) + " B");
        }
        /*
         * else {
         * displaySize = String.valueOf(df.format(size)) + " B";
         * }
         */
        return displaySize;
    }


    /**
     * 递归删除文件目录
     *
     * @param dir 文件目录
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFileDir(File dir) {
        Log.d(TAG,
                "deleteFileDir.dir = " + dir);
        try {
            if (dir.exists() && dir.isDirectory()) {// 判断是文件还是目录
                if (dir.listFiles().length == 0) {// 若目录下没有文件则直接删除
                    dir.delete();
                } else {// 若有则把文件放进数组，并判断是否有下级目录
                    File delFile[] = dir.listFiles();
                    int len = dir.listFiles().length;
                    for (int j = 0; j < len; j++) {
                        if (delFile[j].isDirectory()) {
                            deleteFileDir(delFile[j]);// 递归调用deleteFileDir方法并取得子目录路径
                        } else {
                            boolean isDel = delFile[j].delete();// 删除文件
                            Log.d(TAG,
                                    "deleteFileDir.delFile[" + j + "] = " + delFile[j] + ", isDelete  = " + isDel);
                        }
                    }
                }
                deleteFileDir(dir);// 递归调用
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     *
     * @param file 文件目录
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(File file) {

        try {
            Log.d(TAG,
                    "deleteFile.file = " + file);
            if (file != null && file.isFile() && file.exists()) {
                file.delete();
                Log.d(TAG,
                        "deleteFile.file = " + file + " Succeed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,
                    "deleteFile.file = " + file + " exception=" + e.toString());
        }
    }

    /**
     * 读取指定文件的输出
     */
    public static byte[] getFileOutputBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b,
                        0,
                        n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 读取文件内容到字节数组
     *
     * @param file
     * @return
     */
    public static byte[] getFileOutputBytes(File file) {
        byte[] bytes = null;
        if (file.exists()) {
            byte[] buffer;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(file),
                        DEFAULT_BUFFER_SIZE);
                byteArrayOutputStream = new ByteArrayOutputStream();
                bos = new BufferedOutputStream(byteArrayOutputStream,
                        DEFAULT_BUFFER_SIZE);
                buffer = new byte[DEFAULT_BUFFER_SIZE];
                int len;
                while ((len = bis.read(buffer,
                        0,
                        DEFAULT_BUFFER_SIZE)) != -1) {
                    bos.write(buffer,
                            0,
                            len);
                }
                bos.flush();
                bytes = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                return null;
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG,
                "getFileOutputBytes.file = " + file + ", bytes.length = " + (bytes == null ? 0 : bytes.length));
        return bytes;
    }

    /**
     * 读取文件内容到字节数组
     *
     * @param file
     * @param offset
     * @param len
     * @return
     */
    public static byte[] getFileOutputBytes(File file,
                                            long offset,
                                            long len) {
        byte[] bytes = null;
        if (file.exists() && offset >= 0 && len > offset && offset < file.length()) {
            RandomAccessFile raf = null;
            ByteArrayOutputStream bos = null;
            try {
                raf = new RandomAccessFile(file,
                        "r");
                raf.seek(offset);
                bos = new ByteArrayOutputStream();
                int b;
                long count = offset;
                while ((b = raf.read()) != -1 && count < len) {
                    bos.write(b);
                    count++;
                }
                bos.flush();
                bytes = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (raf != null) {
                        raf.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }


    /**
     * 读取指定文件的输出
     */
    public static String getFileOutputString(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path), 8192);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append("\n").append(line);
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取指定文件的输出
     *
     * @param file
     * @param encoding
     * @return
     */
    public static String getFileOutputString(File file,
                                             String encoding) {
        String result = null;
        if (file.exists()) {
            char[] buffer;
            BufferedReader br = null;
            InputStreamReader isr = null;
            BufferedWriter bw = null;
            StringWriter sw = new StringWriter();
            try {
                isr = encoding == null ? new InputStreamReader(new FileInputStream(file)) : new InputStreamReader(new FileInputStream(file),
                        encoding);
                br = new BufferedReader(isr);
                bw = new BufferedWriter(sw);
                buffer = new char[DEFAULT_BUFFER_SIZE];
                int len;
                while ((len = br.read(buffer,
                        0,
                        DEFAULT_BUFFER_SIZE)) != -1) {
                    bw.write(buffer,
                            0,
                            len);
                }
                bw.flush();
                result = sw.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                    sw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG,
                "readFileToString.file = " + file + ", encoding = " + encoding + ", result = " + result);
        return result;
    }


    /**
     * 将字节写入到文件
     *
     * @param file
     * @param bytes
     * @param offset
     * @return
     */
    public static boolean writeBytesToFile(File file,
                                           byte[] bytes,
                                           long offset) {
        boolean isOk = false;
        if (!file.exists()) {
            try {
                // noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.exists() && bytes != null && offset >= 0) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file,
                        "rw");
                raf.seek(offset);
                raf.write(bytes);
                isOk = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOk;
    }

    /**
     * 写字符串到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param content
     * @return
     */
    public static boolean writeStringToFile(File file,
                                            String content) {
        return writeStringToFile(file,
                content,
                false);
    }

    /**
     * 写字符串到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param content
     * @param isAppend
     * @return
     */
    public static boolean writeStringToFile(File file,
                                            String content,
                                            boolean isAppend) {
        boolean isWriteOk = false;
        if (TextUtils.isEmpty(content))
            return false;

        char[] buffer;
        int count = 0;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                createNewFileAndParentDir(file);
            }
            if (file.exists()) {
                br = new BufferedReader(new StringReader(content));
                bw = new BufferedWriter(new FileWriter(file,
                        isAppend));
                buffer = new char[DEFAULT_BUFFER_SIZE];
                int len;
                while ((len = br.read(buffer,
                        0,
                        DEFAULT_BUFFER_SIZE)) != -1) {
                    bw.write(buffer,
                            0,
                            len);
                    count += len;
                }
                bw.flush();
            }
            isWriteOk = content.length() == count;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isWriteOk;
    }

    /**
     * 写字节数组到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param bytes
     * @return
     */
    public static boolean writeBytesToFile(File file,
                                           byte[] bytes) {
        return writeBytesToFile(file,
                bytes,
                false);
    }

    /**
     * 写字节数组到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param bytes
     * @param isAppend
     * @return
     */
    public static boolean writeBytesToFile(File file,
                                           byte[] bytes,
                                           boolean isAppend) {
        boolean isWriteOk = false;
        byte[] buffer;
        int count = 0;
        ByteArrayInputStream byteArrayInputStream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (!file.exists()) {
                createNewFileAndParentDir(file);
            }
            if (file.exists()) {
                bos = new BufferedOutputStream(new FileOutputStream(file,
                        isAppend),
                        DEFAULT_BUFFER_SIZE);
                byteArrayInputStream = new ByteArrayInputStream(bytes);
                bis = new BufferedInputStream(byteArrayInputStream,
                        DEFAULT_BUFFER_SIZE);
                buffer = new byte[DEFAULT_BUFFER_SIZE];
                int len;
                while ((len = bis.read(buffer,
                        0,
                        DEFAULT_BUFFER_SIZE)) != -1) {
                    bos.write(buffer,
                            0,
                            len);
                    count += len;
                }
                bos.flush();
            }
            isWriteOk = bytes.length == count;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG,
                "writeByteArrayToFile.file = " + file + ", bytes.length = " + (bytes == null ? 0 : bytes.length) + ", isAppend = " + isAppend + ", isWriteOk = " + isWriteOk);
        return isWriteOk;
    }

    /**
     * ********************************************创建***************************
     * /**
     * 创建目录
     *
     * @param dir
     * @return
     */
    public static boolean createDir(File dir) {
        boolean isMkdirs = true;
        if (!dir.exists()) {
            isMkdirs = dir.mkdirs();
            Log.d(TAG,
                    "createDir = " + dir + ", isMkdirs = " + isMkdirs);
        }
        return isMkdirs;
    }

    /**
     * 创建文件父目录
     *
     * @param file
     * @return
     */
    public static boolean createParentDir(File file) {
        boolean isMkdirs = true;
        if (!file.exists()) {
            File dir = file.getParentFile();
            isMkdirs = createDir(dir);
        }
        return isMkdirs;
    }

    /**
     * 创建文件及其父目录
     *
     * @param file
     * @return
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean createNewFileAndParentDir(File file) {
        @SuppressWarnings("UnusedAssignment")
        boolean isCreateNewFileOk = true;
        isCreateNewFileOk = createParentDir(file);
        // 创建父目录失败，直接返回false，不再创建子文件
        if (isCreateNewFileOk) {
            if (!file.exists()) {
                try {
                    isCreateNewFileOk = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    isCreateNewFileOk = false;
                }
            }
        }

        return isCreateNewFileOk;
    }

    /**
     * 根据文件名称获取文件的后缀字符串
     *
     * @param filename 文件的名称,可能带路径
     * @return 文件的后缀字符串
     */
    public static String getFileExtensionFromUrl(String filename) {
        if (!TextUtils.isEmpty(filename)) {
            int dotPos = filename.lastIndexOf('.');
            if (0 <= dotPos) {
                return filename.substring(dotPos + 1);
            }
        }
        return "";
    }

    /**
     * 拷贝文件
     *
     * @param sourceFile 源文件
     * @param destFile   目标文件
     * @return 是否拷贝成功
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean copyFile(File sourceFile,
                                   File destFile) {
        boolean isCopyOk = false;
        byte[] buffer;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        // 如果此时没有文件夹目录就创建
        String canonicalPath = "";
        try {
            canonicalPath = destFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!destFile.exists()) {
            if (canonicalPath.lastIndexOf(File.separator) >= 0) {
                canonicalPath = canonicalPath.substring(0,
                        canonicalPath.lastIndexOf(File.separator));
                File dir = new File(canonicalPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFile),
                    DEFAULT_BUFFER_SIZE);
            bos = new BufferedOutputStream(new FileOutputStream(destFile),
                    DEFAULT_BUFFER_SIZE);
            buffer = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = bis.read(buffer,
                    0,
                    DEFAULT_BUFFER_SIZE)) != -1) {
                bos.write(buffer,
                        0,
                        len);
            }
            bos.flush();
            isCopyOk = sourceFile.length() == destFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG,
                "copyFile.sourceFile = " + sourceFile + ", destFile = " + destFile + ", isCopyOk = " + isCopyOk);
        return isCopyOk;
    }

    /*** 获取文件夹大小 ***/
    public static long getFileSize(File f) {
        long size = 0;
        try {
            File fList[] = f.listFiles();
            for (File file : fList) {
                if (file.isDirectory()) {
                    size = size + getFileSize(file);
                } else {
                    size = size + file.length();
                }
            }
            return size;
        } catch (Exception e) {
            return size;
        }
    }

    /**
     * 获取内存设置
     *
     * @param context
     * @return
     */
    public static int getMemoryCacheSize(Context context) {
        int memoryCacheSize = 0;
        ActivityManager manager = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        int memClass;
        if (manager != null){
            memClass = manager.getMemoryClass();
            memoryCacheSize = (memClass / 3) * 1024 * 1024; // 1/3 of app memory
        }
        // limit
        return memoryCacheSize;
    }

   /* /**
     * 保存APP资源文件到文件系统
     *
     * @param resID      drawable 资源文件
     * @param defineName 定义的文件名
     * @return 文件路径
     */
    /*public static String saveResImgToFile(int resID,
                                          String defineName) {
        String path = null;
        try {
            File Dir = FileConfig.getScreenShortCutDir();
            path = Dir.getAbsolutePath() + File.separator + defineName;
            File file = new File(path);
            // 文件已存在
            Bitmap bitmap = BitmapFactory.decodeResource(WithinApplication.getAppContext()
                            .getResources(),
                    resID);
            boolean isCompressed = ImageUtil.compressSmallImage(bitmap,
                    file,
                    FileConfig.FEED_TEMP_PIC_MAX_SIZE);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (isCompressed) {
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
            */

    /**
     * 如果存在就文件名追加（count），如report（1）
     *
     * @param file
     * @return
     */
    public static File formatDownloadFileName(File file) {
        if (file.getName()
                .contains(".")) {
            String postfix = file.getName()
                    .substring(file.getName()
                            .lastIndexOf("."));
            if (file.exists() && file.getParentFile()
                    .exists()) {
                int count = 0;
                for (File child : file.getParentFile()
                        .listFiles()) {
                    if (child.getName()
                            .contains(file.getName()
                                    .replace(postfix,
                                            ""))) {
                        count++;
                    }
                }
                String fileName = String.format(file.getName()
                                .replace(postfix,
                                        "") +
                                "(%d)",
                        count) +
                        postfix;
                file = new File(file.getParentFile(),
                        fileName);
            }
        }
        return file;
    }


    public static Intent getFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag",
                "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri,
                type);
        return intent;
    }

    public static String getMIMEType(File f) {
        @SuppressWarnings("UnusedAssignment")
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length())
                .toLowerCase();

        /* 依扩展名的类型决定MimeType */
        switch (end) {
            case "pdf":
                type = "application/pdf";//

                break;
            case "m4a":
            case "mp3":
            case "mid":
            case "xmf":
            case "ogg":
            case "wav":
                type = "audio/*";
                break;
            case "3gp":
            case "mp4":
                type = "video/*";
                break;
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                type = "image/*";
                break;
            case "apk":
            /* android.permission.INSTALL_PACKAGES */
                type = "application/vnd.android.package-archive";
                break;
            case "pptx":
            case "ppt":
                type = "application/vnd.ms-powerpoint";
                break;
            case "docx":
            case "doc":
                type = "application/vnd.ms-word";
                break;
            case "xlsx":
            case "xls":
                type = "application/vnd.ms-excel";
                break;
            case "txt":
                type = "text/plain";
                break;
            default:
            /* 如果无法直接打开，就跳出软件列表给用户选择 */
                type = "*/*";
                break;
        }
        return type;
    }

    public static int clearCacheFolder(final File dir,
                                       final int numDays) {

        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {

                    // first delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child,
                                numDays);
                    }

                    // then delete the files and subdirectories in this dir
                    // only empty directories can be deleted, so subDirs have
                    // been done first
                    if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return deletedFiles;
    }



    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getDiskCacheDir(Context context,
                                         String uniqueName) {
        // 检查是否安装或存储媒体是内置的,如果是这样的话,试着使用外部缓存dir否则使用内部缓存目录
        boolean isMounted = SdCardUtil.isSdCardAvailable();
        File cacheFile;
        if (isMounted) {
            cacheFile = getExternalCacheDir(context);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
            if (!cacheFile.canWrite()) {
                cacheFile = context.getCacheDir();
            }
        } else {
            cacheFile = context.getCacheDir();
        }
        File file = new File(cacheFile.getPath() + File.separator + uniqueName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取外设的缓存目录：Android/data/cache....<br>
     * 在Froyo之前需要自己建立缓存目录
     */
    public static File getExternalCacheDir(Context context) {

        if (SdCardUtil.isSdCardAvailable()) {

            return context.getExternalCacheDir();


        } else {
            // 使用data/data文件夹
            return context.getCacheDir();
        }
    }


}
