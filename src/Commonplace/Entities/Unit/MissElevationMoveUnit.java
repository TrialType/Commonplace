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
        float chance = Mathf.random();
        if (chance < 0.5f + healthf() * 0.2f) {
            super.rawDamage(damage);
        } else if (chance < 0.8f + healthf() * 0.05f) {
            heal(10);
        }
    }
}
