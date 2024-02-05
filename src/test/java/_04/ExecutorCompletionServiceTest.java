package _04;

import org.junit.jupiter.api.Test;
import utils.ThreadUtils;

import java.util.concurrent.*;

public class ExecutorCompletionServiceTest {

    @Test
    void notInOrder_fastest_first_by_take() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // ExecutorCompletionService - use this wrapper for executor service makes
        // the tasks results will be available in order of execution period (by take() method)
        // take() can be used instead of get() for CompletionService
        CompletionService<String> completableService = new ExecutorCompletionService<>(executorService);

        Callable<String> fast = ThreadUtils.getCallable("fast", 100, startTime);
        Callable<String> slow = ThreadUtils.getCallable("slow", 200, startTime);

        completableService.submit(slow);
        completableService.submit(fast);

        Future<String> firstResult = completableService.take();
        System.out.println("first: "+ firstResult.get());

        Future<String> secondResult = completableService.take();
        System.out.println("second: " + secondResult.get());
    }
}
