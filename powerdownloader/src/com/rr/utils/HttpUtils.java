package com.rr.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http util class
 */
public class HttpUtils {
    /**
     *
     * @param url file url
     * @return
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL httpURL = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpURL.openConnection();
        // Send identify info to server(Mock browser)
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        return httpURLConnection;
    }

    /**
     *  get download file name
     * @param url
     * @return
     */
    public static String getHttpFileName(String url) {
       int index = url.lastIndexOf("/");
       return url.substring(index + 1);
    }
}
