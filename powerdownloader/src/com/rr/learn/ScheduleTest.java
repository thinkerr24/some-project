package com.rr.learn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {

    public static void main(String[] args) {
        System.out.println("beginTime:" + System.currentTimeMillis());
        // Fetch object
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        // Start execution after a delay of two seconds and loop execution every 3 seconds
        service.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + ':' + System.currentTimeMillis());
            // Mock time-consumer
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 3, TimeUnit.SECONDS);
    }

    public static void schedule() {
        // Fetch object
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        // Execute after 2 seconds
        service.schedule(() -> System.out.println(Thread.currentThread().getName()), 2, TimeUnit.SECONDS);

        // Close
        service.shutdown();
    }
}
