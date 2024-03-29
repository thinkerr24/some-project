package com.rr.core;

import com.rr.constant.Constant;
import com.rr.utils.FileUtils;
import com.rr.utils.HttpUtils;
import com.rr.utils.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Downloader {

    public ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    public ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM, Constant.THREAD_NUM, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(Constant.THREAD_NUM));

    private CountDownLatch countDownLatch = new CountDownLatch(Constant.THREAD_NUM);

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
                return;
            }

            // Get download info
            downloadInfoThread = new DownloadInfoThread(contentLength);
            // Apply thread to execute the task, one second everytime
            service.scheduleAtFixedRate(downloadInfoThread, 1, 1, TimeUnit.SECONDS);

            // split task
            ArrayList<Future> list = new ArrayList<>();
            split(url, list);

            countDownLatch.await();

            // Merge file
           if ( concat(httpFileName)) {
               clearTempFiles(httpFileName);
           }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println();
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            service.shutdownNow();

            // Close thread pool
            poolExecutor.shutdown();
        }
    }

    public void split(String url, ArrayList<Future> futureList) {
        // Get download file size
        try {
            long contentLength = HttpUtils.getHttpFileContentLength(url);

            // Calculate file size after chunk
            long size = contentLength / Constant.THREAD_NUM;

            // Calculate chunk num
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                // Calculate download start&end position
                long startPos = i * size;
                long endPos;
                if (i == Constant.THREAD_NUM - 1) {
                    // Download last chunk
                    endPos = 0;
                } else {
                    endPos = startPos + size;
                }

                // Add position 1 if there is not first chunk
                if (startPos != 0) {
                    startPos++;
                }

                // Create task
                DownloaderTask downloaderTask = new DownloaderTask(url, startPos, endPos, i, countDownLatch);

                // Commit task to thread pool
                Future<Boolean> future = poolExecutor.submit(downloaderTask);
                futureList.add(future);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean concat(String fileName) {
        LogUtils.info("Start to merge file {}", fileName);

        byte[] buffer = new byte[Constant.BYTE_SIZE];
        int len = -1;
        try (RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw")) {
            for (int i = 0; i < Constant.THREAD_NUM; i++) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName + ".temp" + i))) {
                    while ((len = bis.read(buffer)) != -1) {
                        accessFile.write(buffer ,0, len);
                    }
                }
            }
            LogUtils.info("Complete file merge {}", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean clearTempFiles(String fileName) {
        for (int i = 0; i < Constant.THREAD_NUM; i++) {
            File file = new File(fileName + ".temp" + i);
            file.delete();
        }
        return true;
    }

}
