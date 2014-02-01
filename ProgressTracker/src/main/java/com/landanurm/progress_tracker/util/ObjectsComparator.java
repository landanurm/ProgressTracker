package com.landanurm.progress_tracker.util;

/**
 * Created by Leonid on 01.02.14.
 */
public class ObjectsComparator {
    public static boolean areEqual(Object obj1, Object obj2) {
        return (obj1 != null) ? obj1.equals(obj2) : (obj2 == null);
    }
}
