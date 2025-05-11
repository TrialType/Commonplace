package Commonplace.Entities.BulletType;

import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;

public class MagneticStormBulletType extends BulletType {
    public int trailNum = 3;

    private float cdist;
    private Unit result;

    public MagneticStormBulletType() {
        speed = 1;
        trailEffect = Fx.chainLightning;
        scaleLife = true;
        collides = false;
        keepVelocity = false;
        reflectable = absorbable = hittable = false;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);

        float px = b.x + b.lifetime * b.vel.x, py = b.y + b.lifetime * b.vel.y;
        b.time = b.lifetime;
        for (int i = 0; i < trailNum; i++) {
            trailEffect.at(b.x, b.y, 0, trailColor, new Vec2(px, py));
        }
        b.set(px, py);

        cdist = 0f;
        result = null;
        float range = 1f;

        Units.nearbyEnemies(b.team, px - range, py - range, range * 2f, range * 2f, e -> {
            if (e.dead() || !e.checkTarget(collidesAir, collidesGround) || !e.hittable()) return;

            e.hitbox(Tmp.r1);
            if (!Tmp.r1.contains(px, py)) return;

            float dst = e.dst(px, py) - e.hitSize;
            if ((result == null || dst < cdist)) {
                result = e;
                cdist = dst;
            }
        });

        if (result != null) {
            b.collision(result, px, py);
        } else if (collidesTiles) {
            Building build = Vars.world.buildWorld(px, py);
            if (build != null && build.team != b.team) {
                build.collision(b);
                hit(b, px, py);
            }
        }

        b.remove();

        b.vel.setZero();
    }
}
