package Commonplace.Utils.Interfaces;

import Commonplace.Entities.UnitType.GroupUnitType;
import Commonplace.Utils.Classes.UnitGroup;
import arc.func.Cons;
import arc.math.geom.Position;

public interface Groupmate extends Position {
    short priority();

    int max();

    int min();

    boolean isGroupmate();

    boolean canBeMate(GroupUnitType type);

    void out();

    void leader(boolean leader);

    void group(UnitGroup group);

    Cons<UnitGroup> stronger();
}
