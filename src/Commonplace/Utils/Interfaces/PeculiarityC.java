package Commonplace.Utils.Interfaces;

import arc.struct.IntSeq;

public interface PeculiarityC {
    default void apply(int id) {
        getPes().add(id);
    }

    IntSeq getPes();
}
