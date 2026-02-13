package com.petarmc.petarlib.task;

import com.petarmc.petarlib.PetarLib;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskSchedulerTest {

    private TaskScheduler scheduler = new TaskScheduler(2);

    @BeforeEach
    void setUpPlugin() throws Exception {
        PetarLib plugin = mock(PetarLib.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("test"));
        setPlugin(plugin);
    }

    @AfterEach
    void tearDown() throws Exception {
        scheduler.shutdown();
        setPlugin(null);
    }

    @Test
    void runAsync_executesTask() throws Exception {
        AtomicBoolean ran = new AtomicBoolean(false);
        CompletableFuture<Void> done = new CompletableFuture<>();

        scheduler.runAsync(() -> {
            ran.set(true);
            done.complete(null);
        });

        done.get(1, TimeUnit.SECONDS);
        assertTrue(ran.get(), "Task should have run");
    }

    @Test
    void runAsync_catchesExceptionAndContinues() throws Exception {
        AtomicBoolean secondRan = new AtomicBoolean(false);
        CompletableFuture<Void> done = new CompletableFuture<>();

        scheduler.runAsync(() -> { throw new RuntimeException("boom"); });
        scheduler.runAsync(() -> { secondRan.set(true); done.complete(null); });

        done.get(1, TimeUnit.SECONDS);
        assertTrue(secondRan.get(), "Scheduler should continue after exception");
    }

    @Test
    void runDelayed_executesAfterDelay() throws Exception {
        AtomicInteger order = new AtomicInteger();
        CompletableFuture<Void> done = new CompletableFuture<>();

        scheduler.runDelayed(() -> order.compareAndSet(0, 1), 100);
        scheduler.runDelayed(() -> { order.compareAndSet(1, 2); done.complete(null); }, 150);

        done.get(1, TimeUnit.SECONDS);
        assertEquals(2, order.get(), "Tasks should run in delay order");
    }

    @Test
    void shutdown_isTolerant() {
        assertDoesNotThrow(() -> {
            scheduler.shutdown();
            scheduler.shutdown();
        });
    }

    private void setPlugin(PetarLib plugin) throws Exception {
        Field f = PetarLib.class.getDeclaredField("plugin");
        f.setAccessible(true);
        f.set(null, plugin);
    }
}
