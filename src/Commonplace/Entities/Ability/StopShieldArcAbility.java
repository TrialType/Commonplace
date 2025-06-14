package Commonplace.Entities.Ability;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;

public class StopShieldArcAbility extends ShieldArcAbility {
    public float maxVel = 0.1f;
    public float outSpeed = 5f / 60;
    public float inSpeed = 8f / 60;
    public float rotateSpeed = 1;

    protected float range = 0, angleMove = 0;

    @Override
    public void update(Unit unit) {
        if (alpha == 0f) {
            angleMove += rotateSpeed * Time.delta;
            angleMove %= 360;
        }

        if (data < max) {
            data += Time.delta * regen;
        }

        boolean active = data > 0 && (unit.isShooting || !whenShooting) && unit.vel.len() <= maxVel;
        alpha = Math.max(alpha - Time.delta / 10f, 0f);

        if (active) {
            range = Mathf.lerpDelta(range, radius, outSpeed);
            widthScale = Mathf.lerpDelta(widthScale, 1f, 0.06f);
            Vec2 pos = new Vec2(x, y).rotate(unit.rotation - 90f).add(unit);

            Groups.bullet.intersect(unit.x - radius, unit.y - radius, radius * 2f, radius * 2f, b -> {
                if (b.team != unit.team && b.type.absorbable && this.data > 0 && !pos.within(b, this.radius + this.width / 2f) &&
                        Tmp.v1.set(b).add(b.vel).within(pos, this.radius + this.width / 2f) &&
                        Angles.within(pos.angleTo(b), unit.rotation + this.angleOffset + angleMove, this.angle / 2f)) {

                    b.absorb();
                    Fx.absorb.at(b);

                    if (this.data <= b.damage()) {
                        this.data -= this.cooldown * this.regen;
                    }

                    this.data -= b.damage();
                    this.alpha = 1f;
                }
            });
        } else {
            range = Mathf.lerpDelta(range, 0, inSpeed);
            widthScale = Mathf.lerpDelta(widthScale, 0f, 0.11f);
        }
    }

    @Override
    public void draw(Unit unit) {
        if (widthScale > 0.001f && range > 0.001f) {
            Draw.z(Layer.shields);

            Draw.color(unit.team.color, Color.white, Mathf.clamp(alpha));
            var pos = new Vec2(x, y).rotate(unit.rotation - 90f).add(unit);

            if (!Vars.renderer.animateShields) {
                Draw.alpha(0.4f);
            }

            if (region != null) {
                Vec2 rp = offsetRegion ? pos : Tmp.v1.set(unit);
                Draw.yscl = widthScale;
                Draw.rect(region, rp.x, rp.y, unit.rotation - 90);
                Draw.yscl = 1f;
            }

            if (drawArc) {
                Lines.stroke(width * widthScale);
                Lines.arc(pos.x, pos.y, range, angle / 360f, unit.rotation + angleOffset + angleMove - angle / 2f);
            }
            Draw.reset();
        }
    }

    @Override
    public String localized() {
        return Core.bundle.get(getBundle() + ".name");
    }

    @Override
    public String getBundle() {
        return "ability.stop-shield-arc";
    }
}
