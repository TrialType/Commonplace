package Commonplace.Loader.Special;

import Commonplace.Entities.Unit.*;
import mindustry.gen.EntityMapping;

public class Entities {
    public static void load() {
        EntityMapping.idMap[102] = Garrison::create;
        EntityMapping.idMap[111] = MissElevationMoveUnit::create;
        EntityMapping.idMap[112] = LongLifeUnitEntity::create;
        EntityMapping.idMap[113] = BoostUnitEntity::create;
        EntityMapping.idMap[114] = TileMiner::create;
        EntityMapping.idMap[115] = TileSpawnerUnit::create;
        EntityMapping.idMap[116] = HiddenUnit::create;
        EntityMapping.idMap[117] = UnderLandMechUnit::create;
        EntityMapping.idMap[118] = CaveUnit::create;
        EntityMapping.idMap[120] = FollowUnit::create;
        EntityMapping.idMap[121] = SpawnerUnit::create;
        EntityMapping.idMap[90] = CampMechUnit::create;
    }
}