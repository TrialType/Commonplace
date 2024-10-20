package Commonplace.Entities.BulletType;

import arc.math.geom.Vec2;
import mindustry.entities.Damage;
import mindustry.entities.Units;
import mindustry.entities.bullet.SapBulletType;
import mindustry.gen.*;

public class SapRadiusBulletType extends SapBulletType {
    public float radius = 60;

    @Override
    public void init(Bullet b) {
        if(killShooter && b.owner() instanceof Healthc h && !h.dead()){
            h.kill();
        }

        if(instantDisappear){
            b.time = lifetime + 1f;
        }

        if(spawnBullets.size > 0){
            for(var bullet : spawnBullets){
                bullet.create(b, b.x, b.y, b.rotation());
            }
        }

        Healthc target = Damage.linecast(b, b.x, b.y, b.rotation(), length);
        b.data = target;

        if (target != null) {
            float result = Math.max(Math.min(target.health(), damage), 0) * sapStrength;

            if (b.owner instanceof Healthc h) {
                final float[] heals = {0};
                if (h.health() + result < h.maxHealth()) {
                    heals[0] = 0;
                    h.heal(result);
                } else if (h.health() < h.maxHealth()) {
                    heals[0] = h.maxHealth() - h.health();
                    h.heal();
                } else {
                    heals[0] = result;
                }

                if (b.owner instanceof Teamc t && heals[0] > 0) {
                    Units.nearby(t.team(), t.x(), t.y(), radius, u -> {
                        if (heals[0] > 0) {
                            if (u.health < u.maxHealth) {
                                float heal = Math.min(u.maxHealth - u.health, heals[0]);
                                u.heal(heal);
                                heals[0] -= heal;
                            }
                        }
                    });
                }
            }
        }

        if (target instanceof Hitboxc hit) {
            hit.collision(b, hit.x(), hit.y());
            b.collision(hit, hit.x(), hit.y());
        } else if (target instanceof Building tile) {
            if (tile.collide(b)) {
                tile.collision(b);
                hit(b, tile.x, tile.y);
            }
        } else {
            b.data = new Vec2().trns(b.rotation(), length).add(b.x, b.y);
        }
    }
}
