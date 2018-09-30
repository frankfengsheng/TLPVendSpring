package com.tcn.vendspring.netUtil;

import android.content.Context;
import android.text.TextUtils;

import com.tcn.rescommon.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by wumengmeng on 2016/7/16/0016.
 */
public class OkHttpFactory {
    private static OkHttpClient okHttpClient;

    private static void createHttpClient(final Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = null;
                        try {
                            response = chain.proceed(chain.request());
                        } catch (Exception e) {
                            throw e;
                        }

                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            BufferedSource source = responseBody.source();
                            source.request(Long.MAX_VALUE); // Buffer the entire body.
                            Buffer buffer = source.buffer();
                            Charset charset = Charset.forName("UTF-8");
                            MediaType contentType = responseBody.contentType();
                            if (contentType != null) {
                                try {
                                    charset = contentType.charset(Charset.forName("UTF-8"));
                                } catch (UnsupportedCharsetException e) {
                                    return response;
                                }
                            }
                            if (!isPlaintext(buffer)) {
                                return response;
                            }
                            if (responseBody.contentLength() != 0) {
                                String res = buffer.clone().readString(charset);
                                try {
                                    if (!TextUtils.isEmpty(res) && res.startsWith("(")) {
                                        res = res.substring(1, res.length() - 1);
                                    }
                                    JSONObject jobj = new JSONObject(res);
                                    final int code = jobj.getInt("status");
                                    String msg = jobj.getString("msg");
                                    if (code == -1) {
//                                        Intent intent = new Intent();
//                                        intent.setAction("com.homedo.update");
//                                        intent.putExtra("msg", msg);
//                                        context.sendBroadcast(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return response;
                    }
                }).build();
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                if (Character.isISOControl(prefix.readUtf8CodePoint())) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    public static OkHttpClient getOkHttpClient(Context context) {
        if (okHttpClient == null) {
            synchronized (OkHttpFactory.class) {
                if (null == okHttpClient) {
                    createHttpClient(context.getApplicationContext());
                }
            }
        }
        return okHttpClient;
    }
}
