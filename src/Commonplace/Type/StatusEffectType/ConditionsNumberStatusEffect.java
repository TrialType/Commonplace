package Commonplace.Type.StatusEffectType;

import arc.func.Floatf;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class ConditionsNumberStatusEffect extends StatusEffect {
    public boolean truthDamage = false;
    public Floatf<Unit> damageResult = u -> 0f;

    public ConditionsNumberStatusEffect(String name) {
        super(name);
    }

    @Override
    public void update(Unit unit, float time) {
        damage = damageResult.get(unit);
        if (damage > 0) {
            if (truthDamage) {
                float shield = unit.shield;
                unit.shield(0);
                unit.damageContinuousPierce(damage);
                unit.shield(shield);
            } else {
                unit.damageContinuousPierce(damage);
            }
        } else if (damage < 0) {
            unit.heal(-1f * damage * Time.delta);
        }

        if (effect != Fx.none && Mathf.chanceDelta(effectChance)) {
            Tmp.v1.rnd(Mathf.range(unit.type.hitSize / 2f));
            effect.at(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, 0, color, parentizeEffect ? unit : null);
        }
    }
}
