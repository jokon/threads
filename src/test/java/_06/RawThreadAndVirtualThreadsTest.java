package _06;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.jupiter.api.Test;
import utils.ThreadUtils;

import java.util.Random;
import java.util.concurrent.*;

public class RawThreadAndVirtualThreadsTest {

    @Test
    void threadFactory_submit_get() throws ExecutionException, InterruptedException {

        long startTime = System.currentTimeMillis();
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .build();

        ExecutorService threadPool = Executors.newFixedThreadPool(50, threadFactory);

        Future<String> result = threadPool.submit(ThreadUtils.getCallable("title", 10, startTime));


        System.out.println("result: " + result.get());
    }

    @Test
    void virtualThreads() {
        var executorService = Executors.newVirtualThreadPerTaskExecutor();

        executorService.submit(() -> {
            // your code here

        });
    }

    @Test
    void thread() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Runnable runnable = ThreadUtils.getRunnable("Runnable", 500, startTime);
        Runnable runnable2 = ThreadUtils.getRunnable("Runnable2", 100, startTime);
        Thread thread = Thread.startVirtualThread(runnable);
        Thread thread2 = Thread.startVirtualThread(runnable2);

        thread.join();
        thread2.join();
    }

    @Test
    void virtualThread_performance() {
        getRundomNumbers(true);
    }

    @Test
    void thread_performance() {
        getRundomNumbers(false);
    }

    private static void getRundomNumbers(boolean vThreads) {
        System.out.println( "Using vThreads: " + vThreads);

        long start = System.currentTimeMillis();

        Random random = new Random();
        Runnable runnable = () -> { double i = random.nextDouble(1000) % random.nextDouble(1000);  };
        for (int i = 0; i < 50000; i++){
            if (vThreads){
                Thread.startVirtualThread(runnable);
            } else {
                Thread t = new Thread(runnable);
                t.start();
            }
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Run time: " + timeElapsed);
    }
}
