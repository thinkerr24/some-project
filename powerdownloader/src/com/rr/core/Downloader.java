package com.rr.core;

import com.rr.constant.Constant;
import com.rr.utils.HttpUtils;
import com.rr.utils.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;

public class Downloader {
    public void download(String url) {
        String httpFileName = HttpUtils.getHttpFileName(url);
        // Full path for downloaded file
        httpFileName = Constant.DOWNLOAD_PATH + httpFileName;

        // Get connection object
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream input = httpURLConnection.getInputStream();
           BufferedInputStream bis = new BufferedInputStream(input);
           FileOutputStream fos = new FileOutputStream(httpFileName);
           BufferedOutputStream bos = new BufferedOutputStream(fos);
        ) {
            int len = -1;
            while((len = bis.read()) != -1) {
                bos.write(len);
            }
        } catch (FileNotFoundException e) {
            LogUtils.error("Download file not exist:{}", url);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.error("Failed to download");
        } finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
