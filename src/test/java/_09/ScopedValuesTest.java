package _09;

import org.junit.jupiter.api.Test;

public class ScopedValuesTest {
    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

    @Test
    void scopedValues_likeController_invokeMethod_without_passing_userId() {
        ScopedValue.runWhere(USER_ID, "<USER_ID>", doSomething());
    }

    private Runnable doSomething() {
        return () -> {
            // You can get scoped value by static reference instead of passing it through the method invocations
            // and in compare to ThreadLocal you dnon't need to care to remove before return the thread to the pool
            String userId = ScopedValuesTest.USER_ID.get();
            System.out.println("User id is: " + userId);
        };
    }
}
