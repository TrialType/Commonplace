package Commonplace.Entities.Ability;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;

public class NetAbility extends Ability {
    public float x, y;
    public float maxVel = 3;
    public float range = 250;
    public float minSpeed = 0.5f;
    public float velMul = 0.75f;

    private Unit target;
    private float timer = 0;

    public NetAbility(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(Unit unit) {
        if (target != null) {
            Vec2 v = new Vec2(x, y).rotate(unit.rotation).add(unit);
            if (target.dst(v) > range || target.team == unit.team || target.dead || target.health <= 0 || !target.isAdded()) {
                data = -1;
                target = null;
                return;
            }
            float f = (maxVel - target.vel.len()) / (maxVel - minSpeed);
            target.vel.scl(1 - (f * (1 - velMul)));
            timer += f * Time.delta + unit.id / 1000f % 1;
        } else if (data > 0) {
            target = Groups.unit.getByID((int) data);
            data = -1;
        } else {
            Vec2 v = new Vec2(x, y).rotate(unit.rotation).add(unit);
            Units.nearbyEnemies(unit.team, v.x, v.y, range, u -> {
                if (data <= 0 && u.vel.len() < maxVel && u.speed() >= minSpeed) {
                    target = u;
                    data = u.id;
                    target.vel.scl(1 - ((maxVel - target.vel.len()) / (maxVel - minSpeed) * (1 - velMul)));
                }
            });
            timer += Time.delta + unit.id / 1000f % 1;
        }
    }

    @Override
    public void draw(Unit unit) {
        if (target != null) {
            Vec2 d = new Vec2().trns(timer % 360, target.hitSize + Mathf.sin(timer / 30) * 2.5f);
            Vec2 v = new Vec2(x, y).rotate(unit.rotation).add(unit);
            Draw.z(Layer.flyingUnit + 0.1f);
            Draw.color(Color.green);
            Lines.stroke(1.5f);
            Fill.circle(v.x, v.y, 2);
            Fill.circle(target.x + d.x, target.y + d.y, 1.5f);
            Fill.circle(target.x - d.x, target.y - d.y, 1.5f);
            Lines.line(v.x, v.y, target.x + d.x, target.y + d.y);
            Lines.line(v.x, v.y, target.x - d.x, target.y - d.y);
        }
    }
}
