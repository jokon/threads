package utils;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadUtils {

    public static Runnable getRunnable(String jobTitle, long processingTime, long startTime) {

       return () -> {
            String threadName = Thread.currentThread().getName();

            long timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{jobTitle} started (\{timeFromStart})");
            sleep(processingTime);
            timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{jobTitle} ending (\{timeFromStart})");
        };
    }

    public static void sleep(long processingTime) {
        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Callable<String> getCallable(String jobTitle, long processingTime, long startTime) {
        return () -> {
            String threadName = Thread.currentThread().getName();

            long timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{jobTitle} started (\{timeFromStart})");
            sleep(processingTime);
            timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{jobTitle} ending (\{timeFromStart})");

            return jobTitle;
        };
    }

    public static Function<String, String> getStringModifyFunction(String jobTitle, long processingTime, long startTime) {
        return str -> {
            String threadName = Thread.currentThread().getName();

            String title = str + jobTitle;
            long timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{title} started (\{timeFromStart})");
            sleep(processingTime);
            timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{title} ending (\{timeFromStart})");

            return title;
        };
    }

    public static Supplier<String> getStringSupplier(String jobTitle, long processingTime, long startTime) {
        return () -> {
            String threadName = Thread.currentThread().getName();

            long timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{jobTitle} started (\{timeFromStart})");
            sleep(processingTime);
            timeFromStart = System.currentTimeMillis() - startTime;
            System.out.println(STR."\{threadName}: \{jobTitle} ending (\{timeFromStart})");

            return jobTitle;
        };
    }

}
