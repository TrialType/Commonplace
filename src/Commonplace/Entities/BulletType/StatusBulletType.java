package Commonplace.Entities.BulletType;

import Commonplace.Loader.DefaultContent.StatusEffects2;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;

public class StatusBulletType extends BulletType {
    public StatusBulletType() {
        super();

        speed = 0;
        damage = 1;
        lifetime = 1;

        splashDamage = 0;
        splashDamageRadius = 20;

        status = StatusEffects2.tardy;
        statusDuration = 120;

        collides = collidesTiles = collidesAir = hittable = reflectable = absorbable = false;

        hitEffect = smokeEffect = despawnEffect = shootEffect = trailEffect = chargeEffect = Fx.none;
    }
}
