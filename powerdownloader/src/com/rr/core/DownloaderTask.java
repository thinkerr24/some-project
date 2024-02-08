package com.rr.core;

import com.rr.constant.Constant;
import com.rr.utils.HttpUtils;
import com.rr.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

/**
 * Chunk download task
 */
public class DownloaderTask implements Callable<Boolean> {

    private String url;

    // Download start position
    private long startPos;

    // Download end position
    private long endPos;

    // Identify which part of current download
    private int part;

    public DownloaderTask(String url, long startPos, long endPos, int part) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
    }

    @Override
    public Boolean call() throws Exception {
        // Get filename
        String httpFileName = HttpUtils.getHttpFileName(url);
        // Chunk file name
        httpFileName = httpFileName + ".temp" + part;
        // Download path
        httpFileName = Constant.DOWNLOAD_PATH + httpFileName;
        // Get chunk download url
        HttpURLConnection httpURLConnection = HttpUtils.getHttpURLConnection(url, startPos, endPos);
        try (
                InputStream input = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                RandomAccessFile accessFile = new RandomAccessFile(httpFileName, "rw");
        ) {

            byte[] buffer = new byte[Constant.BYTE_SIZE];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                // Download size in 1 second, operate by atomic class
                DownloadInfoThread.downSize.add(len);
                accessFile.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            LogUtils.error("File not exist{}.", url);
            return false;
        } catch (Exception e) {
            LogUtils.error("Download exception.");
            return false;
        } finally {
            httpURLConnection.disconnect();
        }
        return true;
    }
}
