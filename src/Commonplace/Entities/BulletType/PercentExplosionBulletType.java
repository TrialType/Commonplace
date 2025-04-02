package Commonplace.Entities.BulletType;

import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Shieldc;
import mindustry.gen.Statusc;

public class PercentExplosionBulletType extends ExplosionBulletType {
    public float percent = 35;
    public float minPercent = 20;

    public PercentExplosionBulletType(float splashDamage, float splashDamageRadius) {
        super(splashDamage, splashDamageRadius);

        killShooter = false;
    }

    @Override
    public void init(Bullet b) {
        if (b.owner() instanceof Healthc h && !h.dead()) {
            if (killShooter) {
                h.kill();
            } else if (percent > 0) {
                float damage = minPercent >= 0 && b.owner instanceof Statusc s ?
                        Math.max(minPercent * s.healthMultiplier() * h.maxHealth(), percent * h.maxHealth()) / 100f :
                        percent * h.maxHealth() / 100f;
                if (b.owner() instanceof Shieldc s && s.shield() > 0) {
                    float shield = s.shield();
                    s.shield(0);
                    h.damagePierce(damage);
                    s.shield(shield);
                } else {
                    h.damagePierce(damage);
                }
            }
        }

        if (instantDisappear) {
            b.time = lifetime + 1f;
        }

        if (spawnBullets.size > 0) {
            for (var bullet : spawnBullets) {
                bullet.create(b, b.x, b.y, b.rotation());
            }
        }
    }
}
