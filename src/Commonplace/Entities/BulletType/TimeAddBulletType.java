package Commonplace.Entities.BulletType;

import arc.util.Time;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;

public class TimeAddBulletType extends BasicBulletType {
    public float damageAddSeconds = 20;

    public TimeAddBulletType(float speed, float damage) {
        super(speed, damage, "missile");
    }

    @Override
    public void update(Bullet b) {
        super.update(b);

        b.damage += damageAddSeconds / Time.toSeconds;
    }
}
