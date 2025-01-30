package Commonplace.Entities.UnitType;

import Commonplace.Utils.Interfaces.TeamStronger;
import arc.struct.Seq;
import mindustry.type.UnitType;

public class TeamUnitType extends UnitType {
    public static final short LOW = 1, MID = 1 << 1, HEIGHT = 2 << 1;
    public static final TeamStronger none = (m, n) -> {
    };

    public short priority = LOW;
    public int min = 3;
    public int max = 5;
    public TeamStronger stronger = none;
    public Seq<UnitType> teammates;

    public TeamUnitType(String name) {
        super(name);
    }
}
