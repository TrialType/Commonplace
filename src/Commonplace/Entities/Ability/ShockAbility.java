package Commonplace.Entities.Ability;

import Commonplace.Loader.Special.Effects;
import arc.math.Angles;
import arc.util.Time;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class ShockAbility extends StatusFieldAbility {
    public float damage = 35;
    public float airDamage = 25;
    public float buildingDamage = 45;
    public float shake = 6;
    public float shakeDuration = 60;

    public ShockAbility(StatusEffect effect, float duration, float reload, float range) {
        super(effect, duration, reload, range);

        activeEffect = Effects.lockShake;
        parentizeEffects = true;
    }

    @Override
    public void update(Unit unit) {
        timer += Time.delta;

        if (timer >= reload && (!onShoot || unit.isShooting)) {
            final boolean[] act = {false};

            Units.nearby(unit.x - range, unit.y - range, 2 * range, 2 * range, other -> {
                if (other.within(unit, range) && other.team != unit.team) {
                    other.damage(other.isFlying() ? airDamage : damage);
                    other.apply(effect, duration);
                    applyEffect.at(other, parentizeEffects);
                    act[0] = true;
                }
            });

            Units.nearbyBuildings(unit.x, unit.y, range, b -> {
                if (b.team != unit.team) {
                    b.damage(buildingDamage);
                    act[0] = true;
                }
            });

            if (act[0]) {
                activeEffect.at(unit.x + Angles.trnsx(unit.rotation, effectY, effectX), unit.y + Angles.trnsy(unit.rotation, effectY, effectX), effectSizeParam ? range : unit.rotation, parentizeEffects ? unit : null);
                if (shake > 0) {
                    Effect.shake(shake, shakeDuration, unit);
                }
            }

            timer = 0f;
        }
    }
}
