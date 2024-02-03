package com.rr.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtils {

    public static void info(String msg, Object... args) {
        log(msg, "-info-", args);
    }

    public static void error(String msg, Object... args) {
        log(msg, "-error-", args);
    }


    private static void log(String msg, String level, Object ...args) {
        if (args != null && args.length > 0) {
            msg = String.format(msg.replace("{}", "%s"), args);
        }

        String threadName = Thread.currentThread().getName();
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd hh:mm:ss")) + " " + threadName + " " + level + " " + msg);
    }
}
