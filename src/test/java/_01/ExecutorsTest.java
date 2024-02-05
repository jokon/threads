package _01;

import org.junit.jupiter.api.Test;
import utils.ThreadUtils;

import java.util.Collections;
import java.util.concurrent.*;

public class ExecutorsTest {

    @Test
    void simpleOperations() throws ExecutionException, InterruptedException {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            long startTime = System.currentTimeMillis();
            Runnable runnable = ThreadUtils.getRunnable("Runnable", 500, startTime);
            Callable<String> callable = ThreadUtils.getCallable("Callable", 500, startTime);

            // execute + runnable
            executor.execute(runnable);
            executor.awaitTermination(2, TimeUnit.SECONDS);

            Future<?> future;

            // submit + runnable
            future = executor.submit(runnable);
            System.out.println(STR."future result for submit on runnable: \{future.get()}");

            // submit + runnable (with default result)
            future = executor.submit(runnable,  "return for runnable ");
            System.out.println(STR."future result for submit on runnable with default result: \{future.get()}");

            // submit + callable
            future = executor.submit(callable);
            System.out.println(STR."future result for submit on callable: \{future.get()}");

            // invokeAll + callables
            executor.invokeAll(Collections.singleton(callable));

            // invoke any + callables
            executor.invokeAny(Collections.singleton(callable));

            executor.shutdown();
        }
    }
}
