package Commonplace.Entities.Ability;

import arc.func.Boolf;
import arc.math.Angles;
import arc.util.Time;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.Units;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class DamageStatusAbility extends StatusFieldAbility {
    public float damage = 0;
    public float percent = 0.65f;
    public boolean self = false;
    public boolean extend = false;
    public Boolf<Unit> valid = u -> {
        if (u.healthf() <= percent * 1.1f) {
            return true;
        }

        int[] num = {0, 0, 0};
        Units.nearby(u.team, u.x, u.y, range * 2, unit -> {
            if (unit.within(u, range)) {
                num[1]++;
                if (!unit.hasEffect(effect) && unit.hasWeapons()) {
                    num[0]++;
                }
            }
            num[2]++;
        });

        return (num[0] == 1 && num[2] < 3) || num[0] > 3 || (num[1] > 1 && num[1] > u.team.data().unitCount - 4);
    };

    public DamageStatusAbility(float duration, float reload, float range) {
        super(StatusEffects.invincible, duration, reload, range);
    }

    public DamageStatusAbility(StatusEffect effect, float duration, float reload, float range) {
        super(effect, duration, reload, range);
    }

    public DamageStatusAbility self() {
        self = true;
        return this;
    }

    @Override
    public void update(Unit unit) {
        timer += Time.delta;

        if (timer >= reload && (!onShoot || unit.isShooting) && valid.get(unit)) {
            unit.damage(damage + percent * unit.maxHealth);

            Units.nearby(unit.team, unit.x, unit.y, range, other -> {
                if ((extend || !other.hasEffect(effect)) && (self || other != unit)) {
                    other.apply(effect, duration);
                    applyEffect.at(other, parentizeEffects);
                }
            });

            @SuppressWarnings({"SuspiciousNameCombination", "RedundantSuppression"})
            float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, color, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }
}
