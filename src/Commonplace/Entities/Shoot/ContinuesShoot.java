package Commonplace.Entities.Shoot;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.pattern.ShootPattern;

public class ContinuesShoot extends ShootPattern {
    public int max = 5;
    public float chance = 0.35f;
    public ShootPattern base = null;
    public float[] barrels = {0f, 0f, 0f};

    public ContinuesShoot() {
    }

    public ContinuesShoot(ShootPattern base, float chance, int max) {
        this.max = max;
        this.base = base;
        this.chance = chance;
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

    public ContinuesShoot chance(float chance) {
        this.chance = chance;
        return this;
    }

    public void shoot(int totalShots, BulletHandler handler) {
        if (chance >= 1 && max < 0 && shotDelay <= 0) {
            return;
        }
        for (int i = 0; i < shots || (Mathf.chance(chance) && (max < 0 || i < max)); i++) {
            int index = ((i + totalShots) % (barrels.length / 3)) * 3;
            if (base != null) {
                if (firstShotDelay == 0) {
                    if (shotDelay == 0) {
                        base.shoot(totalShots, handler, null);
                    } else {
                        Time.run(shotDelay * i, () -> base.shoot(totalShots, handler, null));
                    }
                } else {
                    if (shotDelay == 0) {
                        Time.run(firstShotDelay, () -> base.shoot(totalShots, handler, null));
                        base.shoot(totalShots, handler, null);
                    } else {
                        Time.run(firstShotDelay + shotDelay * i, () -> base.shoot(totalShots, handler, null));
                    }
                }
            } else {
                handler.shoot(barrels[index], barrels[index + 1], barrels[index + 2], firstShotDelay + shotDelay * i);
            }
        }
    }
}
