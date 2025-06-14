package Commonplace.Entities.Ability;

import arc.Core;
import arc.math.Angles;
import arc.util.Time;
import mindustry.entities.Units;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class StatusEnemyAbility extends StatusFieldAbility {
    public StatusEnemyAbility(StatusEffect effect, float duration, float reload, float range) {
        super(effect, duration, reload, range);
    }

    @Override
    public void update(Unit unit){
        timer += Time.delta;

        if(timer >= reload && (!onShoot || unit.isShooting)){
            Units.nearby(unit.x - range, unit.y - range, 2 * range, 2 * range, other -> {
                if(other.team != unit.team){
                    other.apply(effect, duration);
                    applyEffect.at(other, parentizeEffects);
                }
            });

            float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, color, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }

    @Override
    public String localized() {
        return Core.bundle.get(getBundle() + ".name");
    }

    @Override
    public String getBundle() {
        return "ability.status-enemy";
    }
}
