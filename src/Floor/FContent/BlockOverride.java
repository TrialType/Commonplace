package Floor.FContent;

import mindustry.content.Blocks;
import mindustry.entities.bullet.BulletType;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.production.Drill;

public class BlockOverride {
    public static void load() {
        Blocks.copperWall.health = 90 * 4 * 3;
        Blocks.copperWallLarge.health = 90 * 4 * 12;
        Blocks.titaniumWall.health = 120 * 4 * 3;
        Blocks.titaniumWallLarge.health = 120 * 4 * 12;
        Blocks.plastaniumWall.health = 135 * 4 * 3;
        Blocks.plastaniumWallLarge.health = 135 * 4 * 12;
        Blocks.thoriumWall.health = 210 * 4 * 3;
        Blocks.thoriumWallLarge.health = 210 * 4 * 12;
        Blocks.phaseWall.health = 160 * 4 * 3;
        Blocks.phaseWallLarge.health = 160 * 4 * 12;
        Blocks.surgeWall.health = 240 * 4 * 3;
        Blocks.surgeWallLarge.health = 240 * 4 * 12;

        ((ItemTurret) Blocks.duo).shoot.shots = 2;
        ((ItemTurret) Blocks.duo).reload = 10f;
        ((ItemTurret) Blocks.duo).range = 165;
        ((ItemTurret) Blocks.duo).inaccuracy = 5;
        for (BulletType bullet : ((ItemTurret) Blocks.duo).ammoTypes.values()) {
            bullet.ammoMultiplier *= 2;
        }

        ((Conveyor) Blocks.conveyor).speed = 0.05f;
        ((Conveyor) Blocks.conveyor).displayedSpeed = 7;

        ((Conveyor) Blocks.titaniumConveyor).speed = 0.12f;
        ((Conveyor) Blocks.titaniumConveyor).displayedSpeed = 16.5f;

        ((StackConveyor) Blocks.plastaniumConveyor).speed = 0.1f;
        Blocks.plastaniumConveyor.itemCapacity = 20;

        ((ArmoredConveyor) Blocks.armoredConveyor).speed = 0.12f;
        ((ArmoredConveyor) Blocks.armoredConveyor).displayedSpeed = 15;

        ((Drill) Blocks.mechanicalDrill).drillTime = 480;
        ((Drill) Blocks.pneumaticDrill).drillTime = 320;
        ((Drill) Blocks.laserDrill).drillTime = 274;
        ((Drill) Blocks.blastDrill).drillTime = 274;
    }
}