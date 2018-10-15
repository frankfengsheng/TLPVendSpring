package com.tlp.vendspring;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MSUIUtils {

    private static class SingletonHolder {
        private static MSUIUtils instance = new MSUIUtils();
        private SingletonHolder() {
            //do nothing
        }

    }

    public static MSUIUtils getInstance() {
        return MSUIUtils.SingletonHolder.instance;
    }

    public void displayImage(String url, ImageView imageView, int resId, Context context) {
        if (null == imageView) {
            return;
        }
        if ((null == url) || (url.length() < 1)) {
            imageView.setImageResource(resId);
            return;
        }
        Glide.with(context).load(url).into(imageView);

    }
}
