package com.rr.learn;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPool = null;
        try {
            // Create thread pool
            threadPool = new ThreadPoolExecutor(2, 3, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));

            // Create task
            Runnable r = () -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            // Commit task into thread pool
            for (int i = 0; i < 5; i++) {
                threadPool.execute(r);
            }

            System.out.println(threadPool);

            TimeUnit.SECONDS.sleep(5);

            System.out.println(threadPool);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (threadPool != null) {
                // 关闭线程池(相对温和)
                // threadPool.shutdown();
                // 关闭线程池(相对暴力, 会discard掉blockQueue里的任务)
                // threadPool.shutdownNow();

                // practice
                threadPool.shutdown();
                if (!threadPool.awaitTermination(1, TimeUnit.MINUTES)) {
                    // If wait for a minute and still haven't closed it, then force it to close
                    threadPool.shutdownNow();
                }
            }

        }

    }
}
