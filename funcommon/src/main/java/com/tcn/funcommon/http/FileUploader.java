package com.tcn.funcommon.http;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/9.
 */
public class FileUploader {
    /**
     * android上传文件到服务器
     *
     * 下面为 http post 报文格式
     *
     POST/logsys/home/uploadIspeedLog!doDefault.html HTTP/1.1
     　　 Accept: text/plain,
     　　 Accept-Language: zh-cn
     　　 Host: 192.168.24.56
     　　 Content-Type:multipart/form-data;boundary=-----------------------------7db372eb000e2
     　　 User-Agent: WinHttpClient
     　　 Content-Length: 3693
     　　 Connection: Keep-Alive   注：上面为报文头
     　　 -------------------------------7db372eb000e2
     　　 Content-Disposition: form-data; name="file"; filename="kn.jpg"
     　　 Content-Type: image/jpeg
     　　 (此处省略jpeg文件二进制数据...）
     　　 -------------------------------7db372eb000e2--
     *
     * @param picPaths
     *            需要上传的文件路径集合
     * @param requestURL
     *            请求的url
     * @return 返回响应的内容
     */
    public int uploadFile(String[] picPaths, String requestURL) {
        String boundary = UUID.randomUUID().toString(); // 边界标识 随机生成
        String prefix = "--", end = "\r\n";
        String content_type = "multipart/form-data"; // 内容类型
        String CHARSET = "utf-8"; // 设置编码
        int TIME_OUT = 10 * 10000000; // 超时时间
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "utf-8"); // 设置编码
           // conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", content_type + ";boundary=" + boundary);
            /**
             * 当文件不为空，把文件包装并且上传
             */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(end);
            dos.write(stringBuffer.toString().getBytes());

            String name = "userName";
            dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + end);
            dos.writeBytes(end);
            dos.writeBytes("zhangSan");
            dos.writeBytes(end);


            for(int i = 0; i < picPaths.length; i++){
                File file = new File(picPaths[i]);

                StringBuffer sb = new StringBuffer();
                sb.append(prefix);
                sb.append(boundary);
                sb.append(end);

                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"" + i + "\"; filename=\"" + file.getName() + "\"" + end);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + end);
                sb.append(end);
                dos.write(sb.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[8192];//8k
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(end.getBytes());//一个文件结束标志
            }
            byte[] end_data = (prefix + boundary + prefix + end).getBytes();//结束 http 流
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            Log.e("TAG", "response code:" + res);
            if (res == 200) {
                return 0;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
