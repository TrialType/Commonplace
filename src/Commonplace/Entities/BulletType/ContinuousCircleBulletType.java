package Commonplace.Entities.BulletType;

import mindustry.entities.Damage;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.gen.Bullet;

public class ContinuousCircleBulletType extends ContinuousBulletType {
    public float lengthTo = 0;
    public float damageTo = 0;

    public ContinuousCircleBulletType(){
        super();

        pierceBuilding = true;
    }

    @Override
    public void applyDamage(Bullet b) {
        Damage.damage(b.team, b.x, b.y, length * (1 - b.fin()) + b.fin() * lengthTo,
                damage * (1 - b.fin()) + b.fin() * damageTo,
                false, collidesAir, collidesTiles, false, b);
        if (status != null) {
            Damage.status(b.team, b.x, b.y, length * (1 - b.fin()) + b.fin() * lengthTo,
                    status, statusDuration, collidesAir, collidesTiles);
        }
    }
}