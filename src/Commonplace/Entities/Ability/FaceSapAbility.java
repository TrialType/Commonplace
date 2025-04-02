package Commonplace.Entities.Ability;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;

public class FaceSapAbility extends Ability {
    public float width = 20;
    public float length = 12;
    public float reload = 30;
    public float damage = 75;
    public float sapPercent = 1f;
    public boolean pierce = false;
    public Effect activeEffect = new Effect(60, r -> {
        Rand rand = new Rand(r.id);
        Draw.color(Pal.sapBullet);
        for (int i = 0; i < 8; i++) {
            float range = rand.random(25) + 3;
            float angle = rand.random(360);
            Tmp.v1.trns(angle, 3 + range * r.fin());
            Fill.circle(Tmp.v1.x + r.x, Tmp.v1.y + r.y, 3 * r.fout() + 1);
        }
    });

    private float timer = 0;

    @Override
    public void update(Unit unit) {
        timer += Time.delta;

        if (timer >= reload) {
            final boolean[] used = {false};
            Vec2 v = new Vec2();
            float ro = unit.rotation, ux = unit.x, uy = unit.y, size = unit.hitSize / 2;
            Units.nearbyEnemies(unit.team, ux, uy, Mathf.dst(width, length), u -> {
                v.set(u).sub(ux, uy);
                float angle = Angles.angleDist(ro, v.angle());
                if (angle <= 90 && Mathf.cosDeg(angle) * v.len() <= length + size + u.hitSize && Mathf.sinDeg(angle) * v.len() <= width + size) {
                    if (pierce) {
                        u.damagePierce(damage);
                    } else {
                        u.damage(damage);
                    }
                    activeEffect.at(u);
                    unit.heal(damage * sapPercent);
                    used[0] = true;
                }
            });
            Units.nearbyBuildings(ux, uy, Mathf.dst(width, length), b -> {
                v.set(b).sub(ux, uy);
                float angle = Angles.angleDist(ro, v.angle());
                if (b.team != unit. team && angle <= 90 && Mathf.cosDeg(angle) * v.len() <= length + size + b.block.sizeOffset * Vars.tilesize + b.block.offset && Mathf.sinDeg(angle) * v.len() <= width + size) {
                    if (pierce) {
                        b.damagePierce(damage);
                    } else {
                        b.damage(damage);
                    }
                    activeEffect.at(b);
                    unit.heal(damage * sapPercent);
                    used[0] = true;
                }
            });
            if (used[0]) {
                timer %= reload;
            }
        }
    }
}
