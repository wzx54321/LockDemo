package com.lib.aliocr.http.interceptor;


import android.support.annotation.NonNull;
import android.util.Log;

import com.lib.aliocr.utils.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;


/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class HttpLog implements Interceptor {

    private boolean isPrintBinaryBody;

    public enum Level {
        NONE,       //不打印log
        BASIC,      //只打印 请求首行 和 响应首行
        HEADERS,    //打印请求和响应的所有 Header
        BODY        //所有数据全部打印
    }


    private volatile Level printLevel = Level.NONE;

    public void setPrintLevel(Level printLevel) {
        this.printLevel = printLevel;
    }

    public void setPrintBinaryBody(boolean printBinaryBody) {
        isPrintBinaryBody = printBinaryBody;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (printLevel == Level.NONE) {
            return chain.proceed(request);
        }

        //请求日志拦截
        logForRequest(request, chain.connection());
        // 执行请求，计算请求时间
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            //  Log.e(e, "<-- HTTP FAILED: ");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        //响应日志拦截
        return logForResponse(response, tookMs);
    }


    private void logForRequest(Request request, Connection connection) {

        boolean logBody = (printLevel == Level.BODY);
        boolean logHeaders = (printLevel == Level.BODY || printLevel == Level.HEADERS);
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        StringBuilder requestMessage = new StringBuilder();
        try {
            requestMessage.append("--> ").append(request.method()).append(' ').append(request.url()).append(' ').append(protocol).append("\n");


            if (logHeaders) {
                if (hasRequestBody) {
                    // Request body headers are only present when installed as a network interceptor. Force
                    // them to be included (when available) so there values are known.
                    if (requestBody.contentType() != null) {
                        requestMessage.append("\tContent-Type: ").append(requestBody.contentType()).append("\n");
                    }
                    if (requestBody.contentLength() != -1) {
                        requestMessage.append("\tContent-Length: ").append(requestBody.contentLength()).append("\n");
                    }
                }
                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    // Skip headers from the request body as they are explicitly logged above.
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        requestMessage.append("\t").append(name).append(": ").append(headers.value(i)).append("\n");
                    }
                }


                if (logBody && hasRequestBody) {
                    if (isPlaintext(requestBody.contentType())) {
                        requestMessage.append(bodyToString(request));
                    } else {
                        requestMessage.append("\tbody: maybe [binary body], omitted!\n");
                    }
                }
            }
        } catch (Exception e) {
            Log.e("orc", "intercept");
        } finally {

            requestMessage.append("--> END ").append(request.method());

            Log.i("HTTPLOG", requestMessage.toString());
        }
    }


    private Response logForResponse(Response response, long tookMs) {
        StringBuilder responseMsg = new StringBuilder();
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();
        boolean logBody = (printLevel == Level.BODY);
        boolean logHeaders = (printLevel == Level.BODY || printLevel == Level.HEADERS);

        try {
            responseMsg.append("<-- ").append(clone.code()).append(' ').append(clone.message()).append(' ').append(clone.request().url()).append(" (").append(tookMs).append("ms）" + "\n");
            if (logHeaders) {
                Headers headers = clone.headers();

                for (int i = 0, count = headers.size(); i < count; i++) {
                    responseMsg.append("\t").append(headers.name(i)).append(": ").append(headers.value(i)).append("\n");
                }

                if (logBody && HttpHeaders.hasBody(clone)) {
                    if (responseBody == null) return response;

                    if (isPrintBinaryBody || isPlaintext(responseBody.contentType())) {
                        byte[] bytes = IOUtils.toByteArray(responseBody.byteStream());
                        MediaType contentType = responseBody.contentType();
                        String body = new String(bytes, getCharset(contentType));
                        responseMsg.append("\tbody:").append(body);
                        responseBody = ResponseBody.create(responseBody.contentType(), bytes);
                        return response.newBuilder().body(responseBody).build();
                    } else {
                        responseMsg.append("\tbody: maybe [binary body], omitted! \n if you want to log it please call set isPrintBinaryBody is true");
                    }
                }
            }
        } catch (Exception e) {
            Log.e("", "logForResponse error");
        } finally {
            responseMsg.append("<-- END HTTP");

            Log.i("HTTPLOG", responseMsg.toString());
        }
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")) //
                return true;
        }
        return false;
    }

    private String bodyToString(Request request) {

        try {
            Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body == null) return "";
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = getCharset(body.contentType());
            return "\tbody:" + buffer.readString(charset) + "\n";
        } catch (Exception e) {
            Log.e("", "bodyToString error");
        }

        return "";
    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) charset = UTF8;
        return charset;
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

}
