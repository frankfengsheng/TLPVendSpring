package com.tcn.vendspring.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.tcn.vendspring.MyAplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-5-19.
 * 冯 用于售货机数据库查询
 */

public class TLPDBUtils {
    public static TLPDBUtils instance;
    Context context;
    public static DbUtils db = null;
    public static TLPDBUtils getInstance(Context context) {
        if (context == null) {
            context = MyAplication.getAplicationContext();
        }
        db=DbUtils.create(context);
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new TLPDBUtils();
                }
            }
        }
        return instance;
     }
    /**
     *
     * @param entityClass 新闻查询数据方法
     * @param size  每次取多少条
     * @param offSet 每次取第几页
     * @param column
     * @param value
     * @param column2
     * @param value2
     * @param <t>
     * @return
     */
    public synchronized <t> List<t> searchNewsByCondition(Class<t> entityClass, int size, int offSet, String column, String value, String column2, String value2, boolean desc) {
        try {
            return db.findAll(Selector.from(entityClass).limit(size).offset(offSet*size).orderBy("NewsDate", desc).where(WhereBuilder.b(column, "=", value).and(column2, "=", value2)));
        } catch (Exception e) {
            Log.i("yyyyy",e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 新闻删除操作
     * @param deletEntity
     * @param <t>
     * @return
     */
    public synchronized <t> boolean deleteNewsByCondition(List<t> deletEntity) {
        try {
            // db.deleteAll(Selector.from(entityClass).limit(size).where(WhereBuilder.b(column,"",value)));
            db.deleteAll(deletEntity);

        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
