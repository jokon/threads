package _00;

import org.junit.jupiter.api.Test;
import utils.ThreadUtils;

public class RawThreadsTest {

    @Test
    void test() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Runnable runnable = ThreadUtils.getRunnable("Simple runnable", 100, startTime);
        Thread thread = new Thread(runnable);
        thread.start();

        // alternatively
//        CompletableFuture.runAsync(runnable);

        // wait for result
        thread.join();
    }

    @Test
    void run_threads_and_virtualThreads() {
        long startTime = System.currentTimeMillis();
        Runnable task = ThreadUtils.getRunnable("", 100, startTime);
        Thread thread = Thread.ofPlatform().start(task);


        Thread thread2 = Thread.ofPlatform()
                .daemon()
                .name("my-custom-thread")
                .unstarted(task);

        //virtual threads
        Thread thread3 = Thread.ofVirtual()
                .start(task);

        Thread thread4 = Thread.startVirtualThread(task);
    }
}
