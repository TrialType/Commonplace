package Commonplace.Entities.Shoot;

import arc.math.Mathf;
import mindustry.entities.pattern.ShootPattern;

public class ContinuesShoot extends ShootPattern {
    public float chance = 0.35f;
    public int max = 5;

    public ContinuesShoot() {
    }

    public ContinuesShoot(float chance, int max) {
        this.chance = chance;
        this.max = max;
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
            handler.shoot(0, 0, 0, firstShotDelay + shotDelay * i);
        }
    }
}
