package Commonplace.Entities.UnitType;

import Commonplace.Utils.Classes.UnitGroup;
import arc.func.Cons;
import arc.struct.Seq;
import mindustry.type.UnitType;

import static Commonplace.Utils.Classes.UnitGroup.none;

public class GroupUnitType extends UnitType {
    public static final short LOW = 1, MID = 2, HEIGHT = 3;

    public short priority = LOW;
    public int min = 3;
    public int max = 5;
    public float searchRange = 100;
    public float circleRange = 100;
    public Seq<GroupUnitType> teammates;
    public Cons<UnitGroup> stronger = none;

    public GroupUnitType(String name) {
        super(name);
    }
}
