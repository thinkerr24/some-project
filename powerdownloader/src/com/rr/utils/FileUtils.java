package com.rr.utils;

import java.io.File;

public class FileUtils {
    // Get local file size
    public static long getFileContentLength(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() ? file.length() : 0;
    }
}
