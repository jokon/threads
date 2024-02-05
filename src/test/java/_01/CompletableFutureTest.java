package _01;

import org.junit.jupiter.api.Test;
import utils.ThreadUtils;

import java.util.concurrent.*;

/**
 * Future - java 5
 * CompletableFuture - Java 8
 */
public class CompletableFutureTest {

    @Test
    void completableFuture_thenApply() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Executor executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> commonPart = CompletableFuture
                .supplyAsync(ThreadUtils.getStringSupplier("Common part ", 2000, startTime), executor);

        // Order of thenApplyAsync and thenApply has impact on summarized time of execution
        // async -> sync -> longer - sync waits for previous to finish
        // sync -> async -> shorter execution time - sync waits from previous (common part) but
        // async not need to wait for previous (sync) task.
        CompletableFuture<String> completableFuture2 = commonPart
                .thenApply(ThreadUtils.getStringModifyFunction(" World2", 500, startTime));
        CompletableFuture<String> completableFuture = commonPart
                .thenApply(ThreadUtils.getStringModifyFunction(" World1", 500, startTime));

        completableFuture.join();
        completableFuture2.join();

        System.out.println("time: " + (System.currentTimeMillis() - startTime));
    }

    @Test
    void completableFuture_thenApply_async() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Executor executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> commonPart = CompletableFuture
                .supplyAsync(ThreadUtils.getStringSupplier("Common part ", 2000, startTime), executor);

        CompletableFuture<String> completableFuture2 = commonPart
                .thenApply(ThreadUtils.getStringModifyFunction(" World2", 500, startTime));
        CompletableFuture<String> completableFuture = commonPart
                .thenApplyAsync(ThreadUtils.getStringModifyFunction(" World1", 500, startTime), executor);

        completableFuture.join();
        completableFuture2.join();

        System.out.println("time: " + (System.currentTimeMillis() - startTime));
    }



    @Test
    void thenApply_can_be_executed_by_other_thread() {
        long startTime = System.currentTimeMillis();

        CompletableFuture<String> taskCompletableFuture = CompletableFuture.supplyAsync(
                () -> {
                    int something = 0;

                    // thenApply will be executed by main thread
                    //return "Hey " + 0;
                    // thenApply will be executed by thread pool
                    return "Hey " + something;
                    //TODO why is the difference?
                    // you have no guarantee which thread pool will take the job when supply async without providing the executor
                }
        );
        CompletableFuture<String> stringCompletableFuture = taskCompletableFuture
                .thenApply(ThreadUtils.getStringModifyFunction(" +then apply", 100, startTime));
        stringCompletableFuture.join();
    }

}
