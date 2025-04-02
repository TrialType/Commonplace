package Commonplace.Utils.Interfaces;

import arc.struct.IntSet;

public interface RangePure {
    int level();

    IntSet changes();

    boolean couldUse();
}
