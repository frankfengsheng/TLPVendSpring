package com.tcn.vendspring.netUtil;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by wumengmeng on 2016/8/7/0007.
 */
public class JsonConverterFactory extends Converter.Factory {
    public static JsonConverterFactory create() {
        return new JsonConverterFactory(new Gson());
    }

    public static JsonConverterFactory create(Gson gson) {
        return new JsonConverterFactory(gson);
    }

    private final Gson gson;
    private String json;

    private JsonConverterFactory(Gson gson) {
        if (gson == null){
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new JsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new JsonRequestBodyConverter<JSONObject>();
    }


    final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            try {
                String responseBodyStr = value.string();
                JSONObject jsonObject = new JSONObject(responseBodyStr);
//                JSONObject jsonObject = new JSONObject(value.string());
//                json = jsonObject.toString();
                int code = (int) jsonObject.get("status");
                if (code != 1 && code != 19 && code != 48 && code!=200) {
                    //code 不是成功 这个时候 data就是异常的情况 直接替换掉
                    responseBodyStr = responseBodyStr.replace("data", "dataerror");
                }
                JSONObject jsonObject1 = new JSONObject(responseBodyStr);
                json = jsonObject1.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            String json = value.string();
            Log.e("resultJson", "result : " + json);
//            if (!TextUtils.isEmpty(json)&&json.startsWith("(")) {
//                json = json.substring(1, json.length() - 1);
//            }
            try {
                return adapter.fromJson(json);
            } finally {
                value.close();
            }
        }
    }

    public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

        @Override
        public RequestBody convert(T value) throws IOException {
            return RequestBody.create(MEDIA_TYPE, value.toString());
        }
    }
}

