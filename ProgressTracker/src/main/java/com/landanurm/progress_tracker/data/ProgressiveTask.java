package com.landanurm.progress_tracker.data;

import com.landanurm.progress_tracker.util.ObjectsComparator;

import java.io.Serializable;

/**
 * Created by Leonid on 26.01.14.
 */
public class ProgressiveTask implements Serializable {
    public String name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ((Object) this).getClass() != obj.getClass()) {
            return false;
        }
        ProgressiveTask other = (ProgressiveTask) obj;
        return ObjectsComparator.areEqual(name, other.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
