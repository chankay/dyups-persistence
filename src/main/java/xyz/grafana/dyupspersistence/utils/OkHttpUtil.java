package xyz.grafana.dyupspersistence.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于OkHttp简单封装.
 */
public class OkHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    private static final OkHttpClient client  = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
            .readTimeout(120, TimeUnit.SECONDS)          //设置读超时
            .writeTimeout(60,TimeUnit.SECONDS)          //设置写超时
            .retryOnConnectionFailure(true)             //是否自动重连
            .build();

    /**
     * get请求同步.
     * @param url
     * @param headers
     * @return
     */
    public static Response httpGet(String url, Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * get请求异步.
     * @param url
     * @param headers
     * @param callback
     */
    public static void httpGet(String url, Map<String, String> headers,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    /**
     * post请求同步.
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static Response httpPost(String url, Map<String, String> headers,RequestBody body) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * post请求异步.
     * @param url
     * @param headers
     * @param body
     * @param callback
     */
    public static void httpPost(String url, Map<String, String> headers,RequestBody body,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * post json请求同步.
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static Response httpPost(String url, Map<String, String> headers, JSON json) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json.toJSONString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * post json请求异步.
     * @param url
     * @param headers
     * @param json
     * @param callback
     */
    public static void httpPost(String url, Map<String, String> headers,JSON json,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * httpPost json请求异步.
     * @param url
     * @param headers

     */
    public static Response httpPost(String url, Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * put请求同步.
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static Response httpPut(String url, Map<String, String> headers,RequestBody body) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * put请求异步.
     * @param url
     * @param headers
     * @param body
     * @param callback
     */
    public static void httpPut(String url, Map<String, String> headers,RequestBody body,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * put json请求同步.
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static Response httpPut(String url, Map<String, String> headers, JSON json) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json.toJSONString());

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * put json请求异步.
     * @param url
     * @param headers
     * @param json
     * @param callback
     */
    public static void httpPut(String url, Map<String, String> headers,JSON json,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    /**
     * put json请求异步.
     * @param url
     * @param headers

     */
    public static Response httpPut(String url, Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    /**
     * patch请求同步.
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static Response httpPatch(String url, Map<String, String> headers,RequestBody body) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * patch请求异步.
     * @param url
     * @param headers
     * @param body
     * @param callback
     */
    public static void httpPatch(String url, Map<String, String> headers,RequestBody body,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * patch json请求同步.
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static Response httpPatch(String url, Map<String, String> headers, JSON json) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json.toJSONString());

        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * patch json请求异步.
     * @param url
     * @param headers
     * @param json
     * @param callback
     */
    public static void httpPatch(String url, Map<String, String> headers,JSON json,Callback callback) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * delete json请求异步.
     * @param url
     * @param headers

     */
    public static Response httpDelete(String url, Map<String, String> headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers!=null){
            for (Map.Entry<String, String> e : headers.entrySet()) {
                headersBuilder.add(e.getKey(), e.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .headers(headersBuilder.build())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}