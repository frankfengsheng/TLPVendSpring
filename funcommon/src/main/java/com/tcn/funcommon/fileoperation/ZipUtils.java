package com.tcn.funcommon.fileoperation;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/4/4.
 */
public class ZipUtils {
    public static final String TAG="ZIP";

    /**
     * 解压zip到指定的路径
     * @param zipFileString  ZIP的名称
     * @param outPathString   要解压缩路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String  szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                Log.e(TAG,outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()){
                    Log.e(TAG, "Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static void UnZipFolder(String zipFileString, String outPathString,String  szName) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            //szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                Log.e(TAG,outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()){
                    Log.e(TAG, "Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * 压缩文件和文件夹
     * @param srcFileString   要压缩的文件或文件夹
     * @param zipFileString   解压完成的Zip路径
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString)throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        ZipFiles(file.getParent()+File.separator, file.getName(), outZip);
        //完成和关闭
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam)throws Exception{
        if(zipOutputSteam == null)
            return;
        File file = new File(folderString+fileString);
        if (file.isFile()) {
            ZipEntry zipEntry =  new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while((len=inputStream.read(buffer)) != -1)
            {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        }
        else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry =  new ZipEntry(fileString+File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString+ File.separator+fileList[i], zipOutputSteam);
            }
        }
    }

    /**
     * 返回zip的文件输入流
     * @param zipFileString  zip的名称
     * @param fileString     ZIP的文件名
     * @return InputStream
     * @throws Exception
     */
    public static InputStream UpZip(String zipFileString, String fileString)throws Exception {
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);
        return zipFile.getInputStream(zipEntry);
    }

    /**
     * 返回ZIP中的文件列表（文件和文件夹）
     * @param zipFileString     ZIP的名称
     * @param bContainFolder    是否包含文件夹
     * @param bContainFile      是否包含文件
     * @return
     * @throws Exception
     */
    public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile)throws Exception {
        List<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // 获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }
            } else {
                File file = new File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }
        inZip.close();
        return fileList;
    }


    public static void compress(String zipFile,String... pathName) {
        ZipOutputStream out = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (int i=0;i<pathName.length;i++){
                compress(new File(pathName[i]), out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compress(String zipFilePathName,List<String> pathName) {
        boolean bRet = false;
        ZipOutputStream out = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePathName);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (int i=0;i<pathName.size();i++){
                compress(new File(pathName.get(i)), out, basedir);
            }
            out.close();
            bRet = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bRet;
    }

    public static void compress(String zipFilePathName,String srcPathName) {
        File file = new File(srcPathName);
        if (!file.exists())
            throw new RuntimeException(srcPathName + "不存在！");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePathName);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                    new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            System.out.println("压缩：" + basedir + file.getName());
            compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /** 压缩一个目录 */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            int BUFFER = 8192;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
