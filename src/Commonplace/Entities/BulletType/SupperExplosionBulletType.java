package Commonplace.Entities.BulletType;

import mindustry.ai.types.MissileAI;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.gen.*;

public class SupperExplosionBulletType extends ExplosionBulletType {
    public SupperExplosionBulletType(float splashDamage, float splashDamageRadius) {
        super(splashDamage, splashDamageRadius);
    }

    @Override
    public void init(Bullet b) {
        if (killShooter) {
            if (b.owner instanceof Unit u && u.controller() instanceof MissileAI m) {
                u.kill();
                b.owner = m.shooter;
            } else if (b.owner instanceof Healthc h) {
                h.kill();
            }
        } else if (b.owner instanceof Unit u && u.controller() instanceof MissileAI m) {
            b.owner = m.shooter;
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
