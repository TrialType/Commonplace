package Commonplace.Entities.Ability;

import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;

public class SapAbility extends Ability {
    public float range = 60;
    public float minHealPercent = 0.05f;
    public float heal = 4f / 60;
    public float healPercent = 0.001f;
    public boolean targetSame = false;
    public Boolf<Unit> canAdd = u -> true;

    @Override
    public void update(Unit u) {
        if (u.health < u.maxHealth) {
            float def = heal + u.maxHealth * healPercent;
            final float[] value = new float[1];
            Units.nearby(u.team, u.x, u.y, range, unit -> {
                if (u.health < u.maxHealth && (targetSame || unit.type != u.type)) {
                    value[0] = Math.min(u.maxHealth - u.health, def);
                    float val;
                    float min = unit.maxHealth * minHealPercent;
                    if ((unit.health - value[0]) > min) {
                        val = value[0];
                    } else if (unit.health > min) {
                        val = unit.health - min;
                    } else {
                        val = 0;
                    }
                    if (canAdd.get(unit) && val > 0) {
                        unit.health -= val;
                        u.heal(val);
                    }
                }
            });
        }
    }

    @Override
    public void draw(Unit u) {
        float fin = Time.time % 240 / 240;
        float fut = 1 - fin;

        Draw.color(Pal.sap);
        Draw.alpha(0.2f + fut * 0.8f);
        Lines.stroke(5 * fin);
        Lines.circle(u.x, u.y, 10 + range * fut);
    }
}
