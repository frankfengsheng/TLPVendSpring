package com.tcn.funcommon.media;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/**
 * Created by Administrator on 2016/4/8.
 */
public class QrCode {

    //二维码
    private static final int IMAGE_HALFWIDTH = 25;
    private static final int FOREGROUND_COLOR = 0xff000000; // 前景色
    private static final int BACKGROUND_COLOR = 0xffffffff; // 背景色

    /**
     * 生成二维码 中间插入小图片
     *
     * @param str
     *            内容
     * @return Bitmap
     * @throws WriterException
     */
    public static Bitmap getQrCodeBitmap(String str, Bitmap icon) throws WriterException {

        // 缩放图片
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / icon.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / icon.getHeight();
        m.setScale(sx, sy);

        // 重新构造一个2h*2h的图片
        icon = Bitmap.createBitmap(icon, 0, 0,icon.getWidth(), icon.getHeight(), m, false);

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 250, 250, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                    pixels[y * width + x] = icon.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = FOREGROUND_COLOR;
                    } else { // 无信息设置像素点为白色
                        pixels[y * width + x] = BACKGROUND_COLOR;
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }


    /**
     * 生成二维码 中间插入小图片
     *
     * @param str
     *            内容
     * @return Bitmap
     * @throws WriterException
     */
    public static boolean getQrCodeBitmap(String str, String filePath, Bitmap icon, int width, int height) {

        boolean bRet = false;

        try {
            // 缩放图片
            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / icon.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH / icon.getHeight();
            m.setScale(sx, sy);

            // 重新构造一个2h*2h的图片
            icon = Bitmap.createBitmap(icon, 0, 0,icon.getWidth(), icon.getHeight(), m, false);

            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            BitMatrix matrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int iWidth = matrix.getWidth();
            int iHeight = matrix.getHeight();
            // 二维矩阵转为一维像素数组,也就是一直横着排了
            int halfW = iWidth / 2;
            int halfH = iHeight / 2;
            int[] pixels = new int[iWidth * iHeight];
            for (int y = 0; y < iHeight; y++) {
                for (int x = 0; x < iWidth; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        pixels[y * iWidth + x] = icon.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (matrix.get(x, y)) {
                            pixels[y * iWidth + x] = FOREGROUND_COLOR;
                        } else { // 无信息设置像素点为白色
                            pixels[y * iWidth + x] = BACKGROUND_COLOR;
                        }
                    }

                }
            }
            Bitmap bitmap = Bitmap.createBitmap(iWidth, iHeight,
                    Bitmap.Config.RGB_565);
            // 通过像素数组生成bitmap
            bitmap.setPixels(pixels, 0, iWidth, 0, 0, iWidth, iHeight);

            bRet = (bitmap != null) && (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath)));

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
        return bRet;
    }

    /**
     * 生成二维码 中间插入小图片
     *
     * @param str
     *            内容
     * @return Bitmap
     * @throws WriterException
     */
    public static Bitmap getQrCodeBitmap(int w, int h, String str, Bitmap icon) throws WriterException {

        // 缩放图片
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / icon.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / icon.getHeight();
        m.setScale(sx, sy);

        // 重新构造一个2h*2h的图片
        icon = Bitmap.createBitmap(icon, 0, 0,icon.getWidth(), icon.getHeight(), m, false);

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, w, h, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                    pixels[y * width + x] = icon.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = FOREGROUND_COLOR;
                    } else { // 无信息设置像素点为白色
                        pixels[y * width + x] = BACKGROUND_COLOR;
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @return 生成二维码及保存文件是否成功
     */
    public static Bitmap createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm) {
        Bitmap bitmap = null;
        try {
            if (content == null || "".equals(content)) {
                return bitmap;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
         //   hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);

//            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
//                    BarcodeFormat.QR_CODE, widthPix, widthPix);

            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
           // return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        return bitmap;
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @return 生成二维码及保存文件是否成功
     */
    public static Bitmap createQRImageNoHints(String content, int widthPix, int heightPix, Bitmap logoBm) {
        Bitmap bitmap = null;
        try {
            if (content == null || "".equals(content)) {
                return bitmap;
            }

            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, widthPix, widthPix);

            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            // return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        return bitmap;
    }


    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(String content, String filePath, int widthPix, int heightPix, Bitmap logoBm) {
        boolean bRet = false;
        try {
            if (content == null || "".equals(content)) {
                return bRet;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            bRet = (bitmap != null) && (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath)));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        return bRet;
    }


    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

}
