package Commonplace.Entities.Unit;

import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.ElevationMoveUnit;

public class MissElevationMoveUnit extends ElevationMoveUnit {
    public float healTimer = 0;

    public static MissElevationMoveUnit create() {
        return new MissElevationMoveUnit();
    }

    @Override
    public int classId() {
        return 111;
    }

    @Override
    public void update() {
        if (hitTime > 0) {
            healTimer = 0;
        } else {
            healTimer += Time.delta;
        }

        if (healTimer >= 600) {
            heal((vel.isZero() && !isShooting ? 0.2f : 0.12f) * Time.delta);
        }
        super.update();
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

    @Override
    public void read(Reads read) {
        super.read(read);
        healTimer = read.f();
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        write.f(healTimer);
    }
}
