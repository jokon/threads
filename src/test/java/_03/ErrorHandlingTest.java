package _03;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

public class ErrorHandlingTest {

    @Test
    void handle() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int x = 10;
            return x / 0;
        }).handle((result, error) -> {
            if (error != null) {
                System.out.println("Error occurred!: " + error.getMessage());
                return 0;
            }
            return result;
        });
        System.out.println(future.join());;
    }

    @Test
    void handle_with_callback() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int x = 10;
            return x / 2;
        }).handle((result, error) -> {
            if (error != null) {
                System.out.println("Error occurred!: " + error.getMessage());
                return 5;
            }
            return result;
        }).thenApplyAsync(x -> x + 20);
        System.out.println(future.join());
    }

    @Test
    void exceptionally() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int x = 10;
            return x / 0;
        }).exceptionally(error -> {
            System.out.println("Error occurred!: " + error.getMessage());
            return 0;
        });
        System.out.println(future.join());
    }

    @Test
    void exceptionally_with_callback() {
        CompletableFuture.supplyAsync(() -> {
            int x = 10;
            return x / 0;
        }).exceptionally(error -> {
            System.out.println("Error occurred!: " + error.getMessage());
            return 0;
        }).thenAcceptAsync(x -> {
            System.out.println(x + 10);
        });
    }

    @Test
    void whenComplete() {
        CompletableFuture.supplyAsync(() -> {
            int x = 10;
            return x / 2;
        }).whenComplete((result, error) -> {
            if (error != null) {
                System.out.println("Error occurred!: " + error.getMessage());
            } else {
                System.out.println(result);
            }
        });
    }

    // examples from https://salithachathuranga94.medium.com/completablefuture-in-java-97b0b392657
}
