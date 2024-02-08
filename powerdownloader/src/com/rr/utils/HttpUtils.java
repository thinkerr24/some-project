package com.rr.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http util class
 */
public class HttpUtils {

    /**
     *  Get download file size
     * @param url
     * @return
     * @throws IOException
     */
    public static long getHttpFileContentLength(String url) throws IOException {
        HttpURLConnection httpURLConnection = null;
        int contentLength = 0;
        try {
            httpURLConnection = getHttpURLConnection(url);
            contentLength = httpURLConnection.getContentLength();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return contentLength;
    }

    /**
     * Chunk download
     *
     * @param url      file url
     * @param startPos
     * @param endPos
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url, long startPos, long endPos) throws IOException {
        HttpURLConnection httpURLConnection = getHttpURLConnection(url);
        LogUtils.info("Download position: from {} to {}", startPos, endPos);
        if (endPos != 0) {
            // bytes=100-200
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-" + endPos);
        } else {
            httpURLConnection.setRequestProperty("RANGE", "bytes=" + startPos + "-");
        }
        return httpURLConnection;
    }

    /**
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
     * get download file name
     *
     * @param url
     * @return
     */
    public static String getHttpFileName(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }
}
