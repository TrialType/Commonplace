package Commonplace.Entities.BulletType;

import arc.math.Mathf;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class EffectBulletType extends BulletType {

    @Override
    public void updateTrailEffects(Bullet b) {
        if (trailChance > 0) {
            if (Mathf.chanceDelta(trailChance)) {
                trailEffect.at(b.x, b.y, trailRotation ? b.rotation() : trailParam, trailColor, trailEffect.followParent ? b : null);
            }
        }

        if (trailInterval > 0f) {
            if (b.timer(0, trailInterval)) {
                trailEffect.at(b.x, b.y, trailRotation ? b.rotation() : trailParam, trailColor, trailEffect.followParent ? b : null);
            }
        }
    }
}
