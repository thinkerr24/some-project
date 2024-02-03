package com.rr.core;

import com.rr.constant.Constant;
import com.rr.utils.FileUtils;
import com.rr.utils.HttpUtils;
import com.rr.utils.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Downloader {

    public ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public void download(String url) {
        String httpFileName = HttpUtils.getHttpFileName(url);
        // Full path for downloaded file
        httpFileName = Constant.DOWNLOAD_PATH + httpFileName;

        // Get local file size
        long localFileLength = FileUtils.getFileContentLength(httpFileName);

        // Get connection object
        HttpURLConnection httpURLConnection = null;
        DownloadInfoThread downloadInfoThread = null;
        try {
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
            // Get download file total size
            int contentLength = httpURLConnection.getContentLength();

            // Judge if file downloaded(exist)
            if (localFileLength >= contentLength) {
                LogUtils.info("******{} has been downloaded, there is no need to re-download!******", httpFileName);
                return ;
            }

            // Get download info
            downloadInfoThread = new DownloadInfoThread(contentLength);
            // Apply thread to execute the task, one second everytime
            service.scheduleAtFixedRate(downloadInfoThread, 1, 1, TimeUnit.SECONDS);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream input = httpURLConnection.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(input);
             FileOutputStream fos = new FileOutputStream(httpFileName);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
        ) {
            int len = -1;
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                downloadInfoThread.downSize += len;
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            LogUtils.error("Download file not exist:{}", url);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.error("Failed to download");
        } finally {
            System.out.println();
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            service.shutdownNow();
        }
    }
}
