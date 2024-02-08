package com.rr.core;

import com.rr.constant.Constant;

import java.util.concurrent.atomic.LongAdder;


/**
 * Show download info
 */
public class DownloadInfoThread implements Runnable {

    // file size
    private long httpFileContentLength;
    // finished download size(AtomicLong)
    public static LongAdder finishedSize = new LongAdder();

    // download size last time
    public double prevSize;

    // summary download size this time
    public static volatile LongAdder downSize = new LongAdder();

    public DownloadInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }

    @Override
    public void run() {
        // File total size
        String httpFileSize = String.format("%.2f", httpFileContentLength / Constant.MB);

        // Calculate download speed every second
        int speed = (int) ((downSize.doubleValue() - prevSize) / Constant.KB);
        prevSize = downSize.doubleValue();

        // Residual file size
        double remainSize = httpFileContentLength - finishedSize.doubleValue() - downSize.doubleValue();

        // Estimate residual time
        String remainTime = String.format("%.1f", remainSize / Constant.KB / speed);
        if ("Infinity".equals(remainTime)) {
            remainTime = "-";
        }

        // Calculate finished size
        String currentFileSize = String.format("%.2f", (downSize.doubleValue() - finishedSize.doubleValue()) / Constant.MB);

        String downInfo = String.format("Finished size:%smb/%smb, speed:%skb/s, remain time: %ss", currentFileSize, httpFileSize,
                speed, remainTime);
        System.out.print("\r");
        System.out.print(downInfo);
    }

}
