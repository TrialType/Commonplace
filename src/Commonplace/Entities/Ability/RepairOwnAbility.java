package Commonplace.Entities.Ability;

import arc.util.Time;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.gen.Unit;

public class RepairOwnAbility extends RepairFieldAbility {
    public RepairOwnAbility(float amount, float reload, float range) {
        super(amount, reload, range);
    }

    @Override
    public void update(Unit unit){
        timer += Time.delta;

        if(timer >= reload){
            if(unit.damaged()){
                healEffect.at(unit, parentizeEffects);
                activeEffect.at(unit, range);
            }
            unit.heal(amount);

            timer = 0f;
        }
    }
}
