package Commonplace.Content.SpecialContent;

import Commonplace.Entities.FUnit.F.*;
import Commonplace.Entities.FUnit.Override.*;
import mindustry.gen.EntityMapping;

public class Entities {
    public static void load() {
        EntityMapping.idMap[103] = FLegsUnit::create;
        EntityMapping.idMap[104] = FMechUnit::create;
        EntityMapping.idMap[105] = FTankUnit::create;
        EntityMapping.idMap[106] = FUnitEntity::create;
        EntityMapping.idMap[107] = FUnitWaterMove::create;
        EntityMapping.idMap[108] = FPayloadUnit::create;
        EntityMapping.idMap[109] = FElevationMoveUnit::create;
        EntityMapping.idMap[110] = FCrawlUnit::create;
        EntityMapping.idMap[111] = FMechUnitLegacyNova::create;
        EntityMapping.idMap[112] = LongLifeUnitEntity::create;
        EntityMapping.idMap[113] = BoostUnitEntity::create;
        EntityMapping.idMap[114] = TileMiner::create;
        EntityMapping.idMap[115] = TileSpawnerUnit::create;
        EntityMapping.idMap[116] = HiddenUnit::create;
        EntityMapping.idMap[117] = UnderLandMechUnit::create;
        EntityMapping.idMap[118] = CaveUnit::create;
        EntityMapping.idMap[119] = TimeUpGradeUnit::create;
        EntityMapping.idMap[120] = FollowUnit::create;
        EntityMapping.idMap[121] = SpawnerUnit::create;
    }
}