package Commonplace.Entities.Ability;

import arc.Core;
import arc.math.Angles;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class StatusOwnAbility extends StatusFieldAbility {
    public StatusOwnAbility(StatusEffect effect, float duration, float reload, float range) {
        super(effect, duration, reload, range);
        activeEffect = Fx.none;
        applyEffect = Fx.none;
    }

    @Override
    public void update(Unit unit) {
        timer += Time.delta;

        if (timer >= reload && (!onShoot || unit.isShooting)) {
            unit.apply(effect, duration);
            applyEffect.at(unit, parentizeEffects);

            float x = unit.x + Angles.trnsx(unit.rotation, effectX, effectY), y = unit.y + Angles.trnsy(unit.rotation, effectX, effectY);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }

    @Override
    public String localized() {
        return Core.bundle.get(getBundle() + ".name");
    }

    @Override
    public String getBundle() {
        return "ability.status-own";
    }
}
