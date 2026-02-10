package com.petarmc.petarlib.task;

import com.petarmc.petarlib.Config;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.petarmc.petarlib.PetarLib.getPlugin;

/**
 * Schedules and executes tasks async or with a delay. It runs tasks on a separate thread and not on the main server thread.
 * Never call Bukkit/Spigot/Paper API methods from tasks scheduled with this class, because it can cause crashes and/or data corruption.
 * Use Bukkit's scheduler ({@link org.bukkit.scheduler.BukkitScheduler#runTask(org.bukkit.plugin.Plugin, java.lang.Runnable)}) for tasks that need to interact with the server API.
 */
    public class TaskScheduler {

    private final ScheduledExecutorService scheduler;

    /**
     * Creates a TaskScheduler with a fixed number of threads.
     *
     * @param threads the number of threads for asynchronous tasks; minimum 1
     */
    public TaskScheduler(int threads) {
        scheduler = Executors.newScheduledThreadPool(Math.max(1, threads));
    }

    /**
     * Runs a task asynchronously.
     * Exceptions thrown by the task are caught and logged.
     *
     * @param r the Runnable task to execute
     */
    public void runAsync(Runnable r) {
        try {
            scheduler.submit(() -> {
                try {
                    r.run();
                } catch (Throwable e) {
                    getPlugin().getLogger().severe("TaskScheduler task threw: " + e.getMessage());
                }
            });
        } catch (RejectedExecutionException e) {
            getPlugin().getLogger().severe("TaskScheduler.runAsync rejected: " + e.getMessage());
        }
    }

    /**
     * Runs a task after a specified delay in ms.
     * Exceptions thrown by the task are caught and logged.
     *
     * @param r       the runnable task to execute
     * @param delayMs the delay in ms before executing the task
     */
    public void runDelayed(Runnable r, long delayMs) {
        scheduler.schedule(() -> {
            try {
                r.run();
            } catch (Throwable t) {
                getPlugin().getLogger().severe("Delayed task threw: " + t.getMessage());
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Shuts down the scheduler, waiting up to 5 seconds
     * before forcing shutdown. Logs shutdown progress.
     */
    public void shutdown() {
        if (Config.debugMode) {getPlugin().getLogger().info("Shutting down TaskScheduler...");}
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                if (Config.debugMode) {getPlugin().getLogger().info("Forcing TaskScheduler shutdown..."); }
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (Config.debugMode) {getPlugin().getLogger().info("TaskScheduler stopped");}
    }
}
