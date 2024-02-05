package _02;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * with Java 7 the Fork/Join framework has been introduced.
 * was designed to speed up work that can be broken into smaller pieces recursively,
 * see CustomRecursiveAction#compute()
 *
 * RecursiveAction doesn't return a value in comparison to RecursiveTask
 */
public class ForkJoinPoolActionTest {

    private static class CustomRecursiveAction extends RecursiveAction {

        private final long startTime;
        private String workload;
        private static final int THRESHLOD = 2;
        public CustomRecursiveAction(String workload, long startTime) {
            this.workload = workload;
            this.startTime = startTime;
        }

        @Override
        protected void compute() {
            if (workload.length() > THRESHLOD) {
                ForkJoinTask.invokeAll(getSubtasks());
            } else {
                process(workload);
            }
        }

        private void process(String workload) {
            try {
                String threadName = Thread.currentThread().getName();
                long timeFromStart = System.currentTimeMillis() - startTime;
                System.out.println(threadName + ": " + workload + " started" + "(" + timeFromStart + ")");
                Thread.sleep(500);
                if (workload.contains("a")) {
                    Thread.sleep(3_500);
                }

                timeFromStart = System.currentTimeMillis() - startTime;
                String result = workload.toUpperCase();
                System.out.println(threadName + ": " + result + " ended" + "(" + timeFromStart + ")");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private List<CustomRecursiveAction> getSubtasks() {
            List<CustomRecursiveAction> subtasks = new ArrayList<>();
            subtasks.add(new CustomRecursiveAction(workload.substring(0, THRESHLOD), startTime));
            subtasks.add(new CustomRecursiveAction(workload.substring(THRESHLOD), startTime));
            return subtasks;
        }
    }

    @Test
    void testForkJoinPool() {
        long startTime = System.currentTimeMillis();
        String input = "0c1b2t3a4r5t6h7j";

        try (ForkJoinPool forkJoinPool = ForkJoinPool.commonPool()) {
            forkJoinPool.setParallelism(2);
            List<CustomRecursiveAction> actions = List.of(
                    new CustomRecursiveAction(input, startTime)
            );

            // invoke method execute actions and wait for finish (execute + join)
            actions.forEach(forkJoinPool::invoke);

//            actions.forEach(forkJoinPool::execute);
//            'execute' don't wait for finish you need to wait by join
//            actions.forEach(ForkJoinTask::join);
//            or by await
//            forkJoinPool.awaitQuiescence(10, TimeUnit.SECONDS);
        }
    }
}
