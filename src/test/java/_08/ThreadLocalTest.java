package _08;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {

    @Test
    void threadLocalTest_remove() {
        ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "initial value");

        threadLocal.set("value set in main thread");

        Runnable r = () -> {
            System.out.println("from inside the thread: " + threadLocal.get());
            threadLocal.set("new value");
            // with ThreadLocal you should clean modification made inside the thread before returning the thread to the pool
            threadLocal.remove();
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i=0; i < 3; i ++) {
            executor.submit(r);
        }
    }

    @Test
    void threadLocalTest_no_remove() {
        ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "initial value");

        threadLocal.set("value set in main thread");

        Runnable r = () -> {
            System.out.println("from inside the thread: " + threadLocal.get());
            threadLocal.set("new value");
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i=0; i < 3; i ++) {
            executor.submit(r);
        }
    }
}
