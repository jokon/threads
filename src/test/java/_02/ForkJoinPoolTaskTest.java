package _02;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ForkJoinPoolTaskTest {

    private static class CustomRecursiveTask extends RecursiveTask<String> {

        private final long startTime;
        private String workload;
        private static final int THRESHLOD = 2;
        public CustomRecursiveTask(String workload, long startTime) {
            this.workload = workload;
            this.startTime = startTime;
        }

        @Override
        protected String compute() {
            if (workload.length() > THRESHLOD) {
                return ForkJoinTask.invokeAll(getSubtasks())
                        .stream()
                        .map(ForkJoinTask::join)
                        .collect(Collectors.joining());
            } else {
                return process(workload);
            }
        }

        private String process(String workload) {
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
                return result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private List<CustomRecursiveTask> getSubtasks() {
            List<CustomRecursiveTask> subtasks = new ArrayList<>();

            subtasks.add(new CustomRecursiveTask(workload.substring(0, THRESHLOD), startTime));
            subtasks.add(new CustomRecursiveTask(workload.substring(THRESHLOD), startTime));
            return subtasks;
        }
    }

    private List<CustomRecursiveTask> getForkJoinTasks(String input, long startTime) {
        return Arrays.asList(
                new CustomRecursiveTask(input, startTime)
        );
    }

    @Test
    void testForkJoinPool() {
        long startTime = System.currentTimeMillis();

        try (ForkJoinPool forkJoinPool = ForkJoinPool.commonPool()) {
            forkJoinPool.setParallelism(2);
            String result = getForkJoinTasks("0c1b2t3a4r5t6h7j", startTime)
                    .stream()
                    .map(forkJoinPool::invoke)
                    .collect(Collectors.joining());
            System.out.println("Final result: " + result);
            forkJoinPool.awaitQuiescence(10, TimeUnit.SECONDS);
        }
    }
}
