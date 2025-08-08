package Commonplace.Entities.BulletType;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;

public class PointBulletType2 extends BulletType {
    private float cdist = 0.0F;
    private Unit result;

    public boolean pointWithFrag = true;
    public boolean pointWithUnit = true;
    public boolean point = false;
    public float trailSpacing = 10.0F;

    public BulletType laserBulletType = null;
    public Effect laserEffect = Fx.none;
    public Color laserColor = Color.white;
    public float laserDelay = 0;
    public float laserInterval = 0;
    public float laserSpreadRandom = 360;
    public float laserSpread = 0;
    public float laserAngle = 0;
    public float laserRange = 350f;
    public int laserGroups = 35;

    public Effect intervalHitEffect = Fx.none;
    public Effect intervalLinkEffect = Fx.none;
    public boolean intervalPoint = true;
    public float intervalRange = 350f;

    public PointBulletType2() {
        super();

        scaleLife = true;
        collides = false;
        reflectable = false;
        keepVelocity = false;
        backMove = false;
    }

    @Override
    public void update(Bullet b) {
        super.update(b);
        updateLaser(b);
    }

    public void updateLaser(Bullet b) {
        if (laserBulletType != null && b.time >= laserDelay && b.timer.get(3, laserInterval)) {
            float angle;
            float length;
            for (int i = 0; i < laserGroups; i++) {
                angle = laserAngle + b.rotation() + Mathf.range(laserSpreadRandom) +
                        ((i - (laserGroups - 1f) / 2f) * laserSpread);
                length = Mathf.range(laserRange);
                Bullet b1 = laserBulletType.create(b.owner, b.team, b.x + Angles.trnsx(angle, length),
                        b.y + Angles.trnsy(angle, length), angle,
                        laserBulletType.damage * laserBulletType.damageMultiplier(b),
                        1, 1, null);
                angle = laserAngle + b.rotation() + Mathf.range(laserSpreadRandom) +
                        ((i - (laserGroups - 1f) / 2f) * laserSpread);
                length = Mathf.range(laserRange);
                Bullet b2 = laserBulletType.create(b.owner, b.team, b.x + Angles.trnsx(angle, length),
                        b.y + Angles.trnsy(angle, length), angle,
                        laserBulletType.damage * laserBulletType.damageMultiplier(b),
                        1, 1, null);
                float x1 = b1.x, y1 = b1.y, x2 = b2.x, y2 = b2.y;
                Damage.collideLine(b1, b.team, x1, y1, Angles.angle(x1, y1, x2, y2), b1.dst(b2), false, false, -1);
                laserEffect.at(x1, y1, 0, laserColor, new Vec2(x2, y2));
            }
        }
    }

    public void updateBulletInterval(Bullet b) {
        if (intervalBullet != null && b.time >= intervalDelay && b.timer.get(2, bulletInterval)) {
            float ang = b.rotation();
            if (intervalPoint) {
                Vec2[] ves = new Vec2[intervalBullets];
                ves[0] = new Vec2(b.x, b.y);
                float x = b.x, y = b.y;
                for (int i = 0; i < intervalBullets; i++) {
                    float angle = Mathf.range(ang + Mathf.range(intervalRandomSpread) +
                            intervalAngle + ((i - (intervalBullets - 1f) / 2f) * intervalSpread));
                    float len = Mathf.range(intervalRange);
                    float bx = b.x + Angles.trnsx(angle, len);
                    float by = b.y + Angles.trnsy(angle, len);
                    intervalBullet.create(b, bx, by, angle);
                    intervalHitEffect.at(bx, by, 0, new Vec2(b.x, b.y));
                    Vec2 v = new Vec2(bx, by).sub(x, y);
                    ves[i] = v;
                    x = bx;
                    y = by;
                }
                intervalLinkEffect.at(b.x, b.y, b.rotation(), ves);
            } else {
                for (int i = 0; i < intervalBullets; i++) {
                    float rot = ang + Mathf.range(intervalRandomSpread) + intervalAngle + ((i - (intervalBullets - 1f) / 2f) * intervalSpread);
                    intervalBullet.create(b, b.x, b.y, rot);
                }
            }
        }
    }

    public void init(Bullet b) {
        super.init(b);
        if (point) {
            this.scaleLife = true;
            this.collides = false;
            this.reflectable = false;
            this.keepVelocity = false;
            this.backMove = false;

            float px = b.x + b.lifetime * b.vel.x;
            float py = b.y + b.lifetime * b.vel.y;
            float rot = b.rotation();
            Geometry.iterateLine(0.0F, b.x, b.y, px, py, this.trailSpacing, (x, y) -> this.trailEffect.at(x, y, rot));
            b.time = b.lifetime;
            b.set(px, py);
            cdist = 0.0F;
            result = null;
            float range = 1.0F;
            Units.nearbyEnemies(b.team, px - range, py - range, range * 2.0F, range * 2.0F, (e) -> {
                if (!e.dead() && e.checkTarget(this.collidesAir, this.collidesGround) && e.hittable()) {
                    e.hitbox(Tmp.r1);
                    if (Tmp.r1.contains(px, py)) {
                        float dst = e.dst(px, py) - e.hitSize;
                        if (result == null || dst < cdist) {
                            result = e;
                            cdist = dst;
                        }

                    }
                }
            });
            if (result != null) {
                b.collision(result, px, py);
            } else if (this.collidesTiles) {
                Building build = Vars.world.buildWorld(px, py);
                if (build != null && build.team != b.team) {
                    build.collision(b);
                }
            }
            if (pointWithFrag) {
                createFrags(b, b.x, b.y);
            }
            if (pointWithUnit) {
                createUnits(b, b.x, b.y);
            }

            b.remove();
            b.vel.setZero();
        }
    }
}
