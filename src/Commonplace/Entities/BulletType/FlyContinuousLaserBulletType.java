package Commonplace.Entities.BulletType;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.entities.Damage;
import mindustry.entities.Fires;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.defense.turrets.ContinuousTurret;

import static arc.util.Time.delta;

public class FlyContinuousLaserBulletType extends ContinuousLaserBulletType {
    public float flyTime = 600;
    public float flySpeed = 1f;

    @Override
    public void update(Bullet b) {
        super.update(b);
        if (flyTime > 0 && !b.timer.check(3, flyTime)) {
            b.keepAlive = true;
        } else if (flyTime > 0 && b.time + delta >= lifetime && b.timer.get(3, flyTime + lifetime + delta * 2)) {
            if (b.owner instanceof Unit u) {
                for (WeaponMount mount : u.mounts) {
                    if (mount.bullet == b) {
                        mount.bullet = null;
                    }
                }
            } else if (b.owner instanceof ContinuousTurret.ContinuousTurretBuild c) {
                c.bullets.removeAll(e -> e.bullet == b);
            }
            b.keepAlive = true;
            b.initVel(b.rotation(), flySpeed);
        }
    }

    @Override
    public void draw(Bullet b) {
        float realLength = Damage.findLength(b, currentLength(b), laserAbsorb, pierceCap);
        float rot = b.rotation();

        for (int i = 0; i < colors.length; i++) {
            Draw.color(Tmp.c1.set(colors[i]).mul(1f + Mathf.absin(Time.time, 1f, 0.1f)));

            float colorFin = i / (float) (colors.length - 1);
            float baseStroke = Mathf.lerp(strokeFrom, strokeTo, colorFin);
            float stroke = (width + Mathf.absin(Time.time, oscScl, oscMag)) * baseStroke;
            float ellipseLenScl = Mathf.lerp(1 - i / (float) (colors.length), 1f, pointyScaling);

            Lines.stroke(stroke);
            Lines.lineAngle(b.x, b.y, rot, realLength - frontLength, false);

            //back ellipse
            Drawf.flameFront(b.x, b.y, divisions, rot + 180f, backLength, stroke / 2f);

            //front ellipse
            Tmp.v1.trnsExact(rot, realLength - frontLength);
            Drawf.flameFront(b.x + Tmp.v1.x, b.y + Tmp.v1.y, divisions, rot, frontLength * ellipseLenScl, stroke / 2f);
        }

        Tmp.v1.trns(b.rotation(), realLength * 1.1f);

        Drawf.light(b.x, b.y, b.x + Tmp.v1.x, b.y + Tmp.v1.y, lightStroke, lightColor, 0.7f);
        Draw.reset();
    }

    @Override
    public void hitTile(Bullet b, Building build, float x, float y, float initialHealth, boolean direct) {
        if (makeFire && build.team != b.team) {
            Fires.create(build.tile);
        }

        if (heals() && build.team == b.team && !(build.block instanceof ConstructBlock)) {
            healEffect.at(build.x, build.y, 0f, healColor, build.block);
            build.heal(healPercent / 100f * build.maxHealth + healAmount);
        } else if (build.team != b.team && direct) {
            hit(b);
        }

        if (build.absorbLasers()) {
            if (Math.abs(b.x - build.x) <= build.hitSize() / 2 + 0.5 && Math.abs(b.y - build.y) <= build.hitSize() / 2 + 0.5) {
                b.hit = true;
                b.remove();
            }
        } else {
            handlePierce(b, initialHealth, x, y);
        }
    }

    @Override
    public float currentLength(Bullet b) {
        float m = 1;
        if (flyTime > 0) {
            float last = flyTime - b.timer.getTime(3);
            if (last >= 0 && last < fadeTime) {
                m *= last / fadeTime;
            }
        } else {
            float last = flyTime - b.time;
            if (last > 0 && last < fadeTime) {
                m *= last / fadeTime;
            }
        }
        return length * m;
    }
}
