package Commonplace.Entities.BulletType;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.gen.Bullet;

public class MovePointBulletType extends ContinuousBulletType {
    public float range = 10;
    public float stroke = 2;
    public float min = 10;
    public float shoot = 0;
    public float radius = 0;
    public boolean interval = true;
    public Color frontColor = Color.white;
    public Color damageColor = Color.white;
    public Color laserColor = Color.white;
    public Interp move = Interp.linear;
    public Effect damageEffect = Fx.none;
    public Effect frontEffect = Fx.none;

    private final Vec2 t = new Vec2();

    @Override
    public void update(Bullet b) {
        if (!continuous) return;

        if (!interval || b.timer(1, damageInterval)) {
            applyDamage(b);
        }

        if (shake > 0) {
            Effect.shake(shake, shake, b);
        }

        setTarget(b);
        float ro = t.sub(b).angle();
        t.add(b);
        frontEffect.at(b.x, b.y, ro, frontColor);
        trailEffect.at(t.x, t.y, ro, trailColor);

        updateBulletInterval(b);
    }

    @Override
    public void draw(Bullet b) {
        super.draw(b);

        setTarget(b);

        Lines.stroke(stroke);
        Draw.color(laserColor);
        Lines.line(b.x, b.y, t.x, t.y);
    }

    @Override
    public void init(Bullet b) {
        super.init(b);

        if (!continuous) {
            applyDamage(b);
        }

        t.set(b.aimX, b.aimY).sub(b).limit(length).add(b);
        b.aimX = t.x;
        b.aimY = t.y;

        b.fdata = Mathf.random(360f) + 360 * (min + Mathf.random(range));
    }

    @Override
    public void applyDamage(Bullet b) {
        setTarget(b);
        Damage.collidePoint(b, b.team, hitEffect, t.x, t.y);
        if (splashDamageRadius > 0) {
            Damage.damage(b.team, t.x, t.y, splashDamageRadius, splashDamage, splashDamagePierce, collidesAir, collidesGround, largeHit, b);
        }
        damageEffect.at(t.x, t.y, t.sub(b).angle(), damageColor, b);
    }

    public void setTarget(Bullet b) {
        if (b.time < shoot) {
            t.trns(b.fdata % 360 + 180, b.fdata / 360).add(b.aimX, b.aimY).sub(b).
                    scl(b.time / shoot).limit(length + b.fdata / 360).add(b);
        } else {
            float fin = 2 * (move.apply((b.time - shoot) / (b.lifetime - shoot)) - 0.5f);
            float len = fin * b.fdata / 360;
            t.trns(b.fdata % 360, len).add(b.aimX, b.aimY).sub(b).limit(length + b.fdata / 360).add(b);
        }
    }
}
