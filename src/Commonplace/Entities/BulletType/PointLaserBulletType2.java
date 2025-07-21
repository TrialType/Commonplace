package Commonplace.Entities.BulletType;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.PointLaserBulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;

public class PointLaserBulletType2 extends PointLaserBulletType {
    private float angleDelta;

    public float side = 6;
    public float radius = 45;
    public float rotateSpeed = 2.5f;
    public float damageInterval2 = 3f;

    public PointLaserBulletType2() {
        super();
    }

    @Override
    public void draw(Bullet b) {
        super.draw(b);

        Draw.color(color);
        Drawf.laser(laser, laserEnd, b.x, b.y, b.aimX, b.aimY, b.fslope() * (1f - oscMag + Mathf.absin(Time.time, oscScl, oscMag)));
        Vec2 v = new Vec2((Vec2) b.data);
        for (int i = 0; i < side; i++) {
            Drawf.laser(laser, laserEnd, b.aimX + v.x, b.aimY + v.y, b.aimX, b.aimY, b.fslope() * (1f - oscMag + Mathf.absin(Time.time, oscScl, oscMag)));
            v.rotate(angleDelta);
        }
        Draw.reset();
    }

    @Override
    public void update(Bullet b) {
        updateTrail(b);
        updateTrailEffects(b);
        updateBulletInterval(b);

        Vec2 v = (Vec2) b.data;
        v.rotate(rotateSpeed);
        v = new Vec2(v);

        if (b.timer.get(0, damageInterval)) {
            Damage.collidePoint(b, b.team, hitEffect, b.aimX, b.aimY);
        }

        if (b.timer.get(1, beamEffectInterval)) {
            beamEffect.at(b.aimX, b.aimY, beamEffectSize * b.fslope(), hitColor);
            for (int i = 0; i < side; i++) {
                beamEffect.at(b.aimX + v.x, b.aimY + v.y, beamEffectSize * b.fslope(), hitColor);
                v.rotate(angleDelta);
            }
        }

        if (shake > 0) {
            Effect.shake(shake, shake, b);
        }

        if (b.timer.get(3, damageInterval2)) {
            for (int i = 0; i < side; i++) {
                Damage.collidePoint(b, b.team, hitEffect, b.aimX + v.x, b.aimY + v.y);
                v.rotate(angleDelta);
            }
        }
    }

    @Override
    public void updateTrailEffects(Bullet b) {
        Vec2 v = new Vec2((Vec2) b.data);
        if (trailChance > 0) {
            if (Mathf.chanceDelta(trailChance)) {
                trailEffect.at(b.aimX, b.aimY, trailRotation ? b.angleTo(b.aimX, b.aimY) : (trailParam * b.fslope()), trailColor);
                for (int i = 0; i < side; i++) {
                    trailEffect.at(b.aimX + v.x, b.aimY + v.y, trailRotation ? b.angleTo(b.aimX, b.aimY) : (trailParam * b.fslope()), trailColor);
                    v.rotate(angleDelta);
                }
            }
        }

        if (trailInterval > 0f) {
            if (b.timer(0, trailInterval)) {
                trailEffect.at(b.aimX, b.aimY, trailRotation ? b.angleTo(b.aimX, b.aimY) : (trailParam * b.fslope()), trailColor);
                for (int i = 0; i < side; i++) {
                    trailEffect.at(b.aimX + v.x, b.aimY + v.y, trailRotation ? b.angleTo(b.aimX, b.aimY) : (trailParam * b.fslope()), trailColor);
                    v.rotate(angleDelta);
                }
            }
        }
    }


    @Override
    public void init(Bullet b) {
        super.init(b);

        b.data = new Vec2().trns(b.rotation() + 180, radius);
    }

    @Override
    public void init() {
        super.init();
        angleDelta = 360f / side;
    }
}
