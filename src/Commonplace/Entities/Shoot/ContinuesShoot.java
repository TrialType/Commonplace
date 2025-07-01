package Commonplace.Entities.Shoot;

import arc.math.Mathf;
import mindustry.entities.pattern.ShootPattern;

public class ContinuesShoot extends ShootPattern {
    public int max = 5;
    public float chance = 0.35f;
    public float[] barrels = {0f, 0f, 0f};

    public ContinuesShoot() {
    }

    public ContinuesShoot(float chance, int max) {
        this.chance = chance;
        this.max = max;
    }

    public ContinuesShoot shots(int shots) {
        this.shots = shots;
        return this;
    }

    public ContinuesShoot barrels(float... barrels) {
        this.barrels = barrels;
        return this;
    }

    public ContinuesShoot shootDelay(float shootDelay) {
        this.shotDelay = shootDelay;
        return this;
    }

    public void shoot(int totalShots, BulletHandler handler) {
        if (chance >= 1 && max < 0 && shotDelay <= 0) {
            return;
        }
        for (int i = 0; i < shots || (Mathf.chance(chance) && (max < 0 || i < max)); i++) {
            int index = ((i + totalShots) % (barrels.length / 3)) * 3;
            handler.shoot(barrels[index], barrels[index + 1], barrels[index + 2], firstShotDelay + shotDelay * i);
        }
    }
}
