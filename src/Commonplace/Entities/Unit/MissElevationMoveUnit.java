package Commonplace.Entities.Unit;

import arc.math.Mathf;
import mindustry.gen.ElevationMoveUnit;

public class MissElevationMoveUnit extends ElevationMoveUnit {
    public static MissElevationMoveUnit create() {
        return new MissElevationMoveUnit();
    }

    @Override
    public int classId() {
        return 111;
    }

    @Override
    public void rawDamage(float damage) {
        if (Mathf.chance(0.5f + healthf() * 0.3f)) {
            super.rawDamage(damage);
        }
    }
}
