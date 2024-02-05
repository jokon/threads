package _07;

import org.junit.jupiter.api.Test;
import utils.ThreadUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class StructuredConcurrency {

    private static class Runner {
        private final long time;
        private final String name;

        private boolean willFail = false;

        public Runner(String name, long time) {
            this.name = name;
            this.time = time;
        }

        public Runner(String name, long time, boolean willFail) {
            this.name = name;
            this.time = time;
            this.willFail = willFail;
        }

        public String getResult() {
            ThreadUtils.sleep(time);
            if (willFail) {
                throw new RuntimeException("Failed on demand");
            }
            System.out.println(this.name + " finished");
            return this.name + " finished";
        }
    }

    private static class Relay {

    }

    @Test
    void without_structuredConcurrency_ols_way() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        Runner speedy = new Runner("Speedy", 100);
        Runner average = new Runner("Average", 300);
        Runner slowy = new Runner("Slowy", 500);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> speedyResult = executor.submit(speedy::getResult);
            Future<String> averageResult = executor.submit(average::getResult);
            Future<String> slowResult = executor.submit(slowy::getResult);


            String slowResultString = slowResult.get();
            String speedyResultString = speedyResult.get();
            String averageResultString = averageResult.get();

            long time = System.currentTimeMillis() - startTime;
            System.out.println(STR."results: \{speedyResultString} \{averageResultString} \{slowResultString} in time: \{time}");
        }
    }

    @Test
    void structuredConcurrency() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        Runner speedy = new Runner("Speedy", 100);
        Runner average = new Runner("Average", 300, true);
        Runner slowy = new Runner("Slowy", 500);

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<String> speedyResult = scope.fork(speedy::getResult);
            Supplier<String> averageResult = scope.fork(average::getResult);
            Supplier<String> slowResult = scope.fork(slowy::getResult);

            scope.join();
            //scope.throwIfFailed();

            long time = System.currentTimeMillis() - startTime;
            if (scope.exception().isPresent()) {
                System.out.println(STR."Failed time: \{time} :" + scope.exception().get().getMessage());
            } else {
                System.out.println(STR."results: \{speedyResult.get()} \{averageResult.get()} \{slowResult.get()} in time: \{time}");
            }
        }

    }
}
