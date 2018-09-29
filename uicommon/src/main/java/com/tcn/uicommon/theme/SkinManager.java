package com.tcn.uicommon.theme;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by ice on 14-10-8.
 * 皮肤资源管理器<单例>
 */
public class SkinManager {

    private static SkinManager mSkinManager;
    // private AssetManager mAssetManager;
    private Resources mResources = null;
    private Context mContext = null;
    private SkinManager (Context context) {
        //  this.mAssetManager = context.getAssets();
        mResources = context.getResources();
        mContext = context;
    }

    public synchronized static SkinManager getInstance(Context context){

        if (mSkinManager == null) {
            mSkinManager = new SkinManager(context);
        }
        return mSkinManager;
    }


    /**
     * 根据皮肤文件名 和 资源文件名 获取Assets 里面的皮肤资源Drawable对象
     * @param
     * @param fileName   资源文件名
     * @return
     */
    public Drawable getSkinDrawable(int skinType, String fileName) {
        Drawable drawable = null;
        if (null == mResources || null == mContext) {
            return drawable;
        }
        int resId = mResources.getIdentifier(fileName, "drawable", mContext.getPackageName());
        if (resId != 0) {
            drawable = mResources.getDrawable(resId);
        }
        return drawable;
    }

    public int getLayoutId(String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString, "layout", mContext.getPackageName());
    }

    public int getStringId(String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString, "string", mContext.getPackageName());
    }

    public int getDrawableId(String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString, "drawable", mContext.getPackageName());
    }

    public int getStyleId(String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString, "style", mContext.getPackageName());
    }

    public int getId(Context paramContext, String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString,"id", mContext.getPackageName());
    }

    public int getColorId(Context paramContext, String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString, "color", mContext.getPackageName());
    }
    public int getArrayId(String paramString) {
        if (null == mResources || null == mContext) {
            return -1;
        }
        return mResources.getIdentifier(paramString, "array", mContext.getPackageName());
    }


    /**
     * 根据皮肤文件名 和 资源文件名 获取Assets 里面的皮肤资源Bitmap对象
     * @param skinFileName
     * @param fileName
     * @return
     */
    /*public Bitmap getSkinBitmap(String skinFileName, String fileName){
        Bitmap image = null;
        try {
            InputStream inputStream = mAssetManager.open(skinFileName + "/" + fileName);
            image = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }*/

}
