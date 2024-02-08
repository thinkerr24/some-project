package com.rr.learn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JDKThreadPoolTest {
    public static void main(String[] args) {
        // Not recommend in Alibaba development manual
        ExecutorService service = Executors.newFixedThreadPool(5);
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
        Executors.newScheduledThreadPool(5);

    }
}
