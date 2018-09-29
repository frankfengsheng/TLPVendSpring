package com.tcn.funcommon.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class Utils {

    private static final String PATH_SDCARD = "/mnt/sdcard";

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height/ (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //计算图片的缩放值
    public static int calculateInSampleSizeLow(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height/ (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 缩放图片
    public static Bitmap fitBitmap(Bitmap bm, int newWidth ,int newHeight){
         // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 获取U盘或是sd卡的路径
     * @return
     */
    public static String getExternalStorageDirectory(){

        String dir = new String();

        try {
            File file = new File(PATH_SDCARD);
            if(file.exists() && file.isDirectory()){
                dir = PATH_SDCARD;
                return dir;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) continue;
                if (line.contains("asec")) continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        dir = columns[1] ;
                    }
                }
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dir;
    }

    /**
     * 获取U盘或是sd卡的路径
     * @return
     */
    public static String getExternalMountPath(){

        String dir = new String();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure")) continue;
                if (line.contains("asec")) continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        dir = columns[1] ;
                    }
                }
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dir;
    }

    public static List<String> getImageWithGifPathFromUDisk(String path) {
        List<String> bitmapList = new ArrayList<String>();
        if (null == path) {
            return bitmapList;
        }
        try {
            File file = new File(path);
            if(!file.exists()){
                file = new File(PATH_SDCARD+path.substring(path.indexOf("/",5)));
                if(!file.exists()){
                    return bitmapList;
                }
            }
            File[] files = file.listFiles();

            for(int i=0 ;i < files.length ;i++)
            {
                if(files[i].isFile())
                {
                    String filename = files[i].getName();
                    //获取bmp,jpg,png格式的图片
                    if(isTcnImageWithGif(filename))
                    {
                        String filePath = files[i].getAbsolutePath();
                        bitmapList.add(filePath);
                    }
                }else if(files[i].isDirectory()){
                    path = files[i].getAbsolutePath();
                    getImageWithGifPathFromUDisk(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapList;
    }

    public static List<String> getImagePathFromUDisk(String path)
    {
        List<String> bitmapList = new ArrayList<String>();
        if (null == path) {
            return bitmapList;
        }
        try {
            File file = new File(path);
            if(!file.exists()){
                file = new File(PATH_SDCARD+path.substring(path.indexOf("/",5)));
                if(!file.exists()){
                    return bitmapList;
                }
            }
            File[] files = file.listFiles();

            for(int i=0 ;i < files.length ;i++)
            {
                if(files[i].isFile())
                {
                    String filename = files[i].getName();
                    //获取bmp,jpg,png格式的图片
                    if(isTcnImage(filename))
                    {
                        String filePath = files[i].getAbsolutePath();
                        bitmapList.add(filePath);
                    }
                }else if(files[i].isDirectory()){
                    path = files[i].getAbsolutePath();
                    List<String> mList = getImagePathFromUDisk(path);
                    for (String tmpPath:mList) {
                        bitmapList.add(tmpPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapList;
    }

    public static String getFirstImagePathFromUDisk(String folderPath)
    {
        String filePath = null;
        if (null == folderPath) {
            return null;
        }
        try {
            File file = new File(folderPath);
            if(!file.exists()){
                file = new File(PATH_SDCARD+folderPath.substring(folderPath.indexOf("/",5)));
                if(!file.exists()){
                    return null;
                }
            }

            File[] files = file.listFiles();

            for(int i=0 ;i < files.length ;i++)
            {
                if(files[i].isFile())
                {
                    String filename = files[i].getName();
                    //获取bmp,jpg,png格式的图片
                    if(isTcnImage(filename))
                    {
                        filePath = files[i].getAbsolutePath();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public static List<String> getImageNameFromUDisk(String path)
    {
        List<String> bitmapList=new ArrayList<String>();
        try {
            File file = new File(path);
            if(!file.exists()){
                file = new File(PATH_SDCARD+path.substring(path.indexOf("/",5)));
                if(!file.exists()){
                    return bitmapList;
                }
            }

            File[] files = file.listFiles();

            for(int i=0 ;i<files.length ;i++)
            {
                if(files[i].isFile())
                {
                    String filename = files[i].getName();
                    //获取bmp,jpg,png格式的图片
                    if(isTcnImage(filename))
                    {

                        bitmapList.add(filename.substring(0,filename.indexOf(".")));
//										fileList.add(filePath);
                    }
                }else if(files[i].isDirectory()){
                    path = files[i].getAbsolutePath();
                    List<String> mList = getImageNameFromUDisk(path);
                    for (String tmpPath:mList) {
                        bitmapList.add(tmpPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapList;
    }

    /**
     * 获取所有视频文件路径
     * @param path
     * @return
     */
    public static List<String> getVideoPathFromUDisk(String path)
    {
        List<String> bitmapList=new ArrayList<String>();
        try {
            File file = new File(path);
            if(!file.exists()){
                file = new File(PATH_SDCARD+path.substring(path.indexOf("/",5)));
                if(!file.exists()){
                    return bitmapList;
                }
            }

            File[] files = file.listFiles();
            for(int i=0 ;i<files.length ;i++)
            {
                if(files[i].isFile())
                {
                    String filename = files[i].getName();
                    //获取bmp,jpg,png格式的图片
                    if(isTcnVideo(filename))
                    {
                        String filePath = files[i].getAbsolutePath();
                        bitmapList.add(filePath);
                    }
                }else if(files[i].isDirectory()){

                    path = files[i].getAbsolutePath();

                    List<String> mList = getVideoPathFromUDisk(path);
                    for (String tmpPath:mList) {
                        bitmapList.add(tmpPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapList;
    }

    /**
     * 获取所有视频图片文件路径
     * @param path
     * @return
     */
    public static List<String> getVideoAndImagePathFromUDisk(String path)
    {
        List<String> bitmapList=new ArrayList<String>();
        try {
            File file = new File(path);
            if(!file.exists()){
                file = new File(PATH_SDCARD+path.substring(path.indexOf("/",5)));
                if(!file.exists()){
                    return bitmapList;
                }
            }

            File[] files = file.listFiles();
            for(int i=0 ;i<files.length ;i++)
            {
                if(files[i].isFile())
                {
                    String filename = files[i].getName();
                    //获取bmp,jpg,png格式的图片
                    if(isTcnVideoOrImage(filename))
                    {
                        String filePath = files[i].getAbsolutePath();
                        bitmapList.add(filePath);
//										fileList.add(filePath);
                    }
                }else if(files[i].isDirectory()){

                    path = files[i].getAbsolutePath();
                    List<String> mList = getVideoAndImagePathFromUDisk(path);
                    for (String tmpPath:mList) {
                        bitmapList.add(tmpPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapList;
    }

    public static boolean isTcnVideoOrImage(String filename) {
        if (null == filename) {
            return false;
        }
        boolean bRet = false;
        String mFilename = filename.toLowerCase();
        if(mFilename.endsWith(".mp4") || mFilename.endsWith(".mp6") || mFilename.endsWith(".3gp") || mFilename.endsWith(".mkv")
                || mFilename.endsWith(".avi") || mFilename.endsWith(".wmv")  || mFilename.endsWith(".flv") || mFilename.endsWith(".jpg")
                || mFilename.endsWith(".jpeg") || mFilename.endsWith(".png") || mFilename.endsWith(".bmp"))
        {
            bRet = true;
        } else {
            bRet = false;
        }
        return bRet;
    }

    public static boolean isTcnVideo(String filename) {
        if (null == filename) {
            return false;
        }
        boolean bRet = false;
        String mFilename = filename.toLowerCase();
        if(mFilename.endsWith(".mp4") || mFilename.endsWith(".mp6") || mFilename.endsWith(".3gp") || mFilename.endsWith(".mkv")
                || mFilename.endsWith(".avi") || mFilename.endsWith(".wmv") || mFilename.endsWith(".flv"))
        {
            bRet = true;
        } else {
            bRet = false;
        }
        return bRet;
    }

    public static boolean isTcnImage(String filename) {
        if (null == filename) {
            return false;
        }
        boolean bRet = false;
        String mFilename = filename.toLowerCase();
        if(mFilename.endsWith(".jpg") || mFilename.endsWith(".jpeg") || mFilename.endsWith(".png") || mFilename.endsWith(".bmp")) {
            bRet = true;
        }
        return bRet;
    }

    public static boolean isTcnImageWithGif(String filename) {
        if (null == filename) {
            return false;
        }
        boolean bRet = false;
        String mFilename = filename.toLowerCase();
        if(mFilename.endsWith(".jpg") || mFilename.endsWith(".jpeg") || mFilename.endsWith(".png")
                || mFilename.endsWith(".bmp") || mFilename.endsWith(".gif")) {
            bRet = true;
        }
        return bRet;
    }
}
