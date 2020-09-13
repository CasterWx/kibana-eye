package com.antzuhl.kibana.schedule;

import com.antzuhl.kibana.core.RingBufferWheel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author AntzUhl
 * @Date 16:33
 */
public class DelayQueue {
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static RingBufferWheel wheel = new RingBufferWheel(executorService);

    public static void addTask(RingBufferWheel.Task task) {
        wheel.addTask(task);
    }
    static {
        wheel.start();
    }

    public static void stopAll(boolean force) {
        wheel.stop(force);
    }

    public static void refresh(boolean force) {
        stopAll(force);
        executorService = Executors.newFixedThreadPool(2);
        wheel = new RingBufferWheel(executorService);
        wheel.start();
    }
}
