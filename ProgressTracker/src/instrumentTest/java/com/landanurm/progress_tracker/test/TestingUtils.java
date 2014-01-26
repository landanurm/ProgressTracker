package com.landanurm.progress_tracker.test;

import junit.framework.AssertionFailedError;

/**
 * Created by Leonid on 26.01.14.
 */
public class TestingUtils {

    public static void assertExpectedException(Class<? extends Exception> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
            throw new AssertionFailedError("Expected exception: <" + exceptionClass.getName() + ">");
        } catch (Exception e) {
            if (!exceptionClass.equals(e.getClass())) {
                throw new AssertionFailedError(
                        "Expected exception: <" + exceptionClass.getName() + "> " +
                                "but was: <" + e.getClass().getName() + ">"
                );
            }
        }
    }
}
