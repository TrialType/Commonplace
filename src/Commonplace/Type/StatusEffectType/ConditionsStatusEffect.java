package Commonplace.Type.StatusEffectType;

import arc.func.Floatf;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class ConditionsStatusEffect extends StatusEffect {
    public boolean loseSpeed = false;
    public boolean loseReload = false;
    public boolean loseDamage = false;
    public boolean loseHealth = false;
    public boolean truthDamage = false;
    public boolean randomSpeed = false;
    public boolean randomReload = false;
    public boolean randomDamage = false;
    public boolean randomHealth = false;
    public float speedDelta = -0.009f;
    public float reloadDelta = -0.009f;
    public float damageDelta = -0.009f;
    public float healthDelta = -0.009f;
    public float randomSpeedMax = 1f;
    public float randomSpeedMin = 1f;
    public float randomReloadMin = 1f;
    public float randomReloadMax = 1f;
    public float randomDamageMin = 1f;
    public float randomDamageMax = 1f;
    public float randomHealthMin = 1f;
    public float randomHealthMax = 1f;
    public Floatf<Unit> damageResult = u -> 0f;

    public Seq<StatusEffect> additionStatus = new Seq<>();

    public ConditionsStatusEffect(String name) {
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

        if (loseSpeed) {
            unit.speedMultiplier(unit.speedMultiplier / speedMultiplier);
            speedMultiplier = 1 + speedDelta * time;
            unit.speedMultiplier(unit.speedMultiplier * speedMultiplier);
        } else if (randomSpeed) {
            unit.speedMultiplier(unit.speedMultiplier / speedMultiplier);
            speedMultiplier = Mathf.random(randomSpeedMin, randomSpeedMax);
            unit.speedMultiplier(unit.speedMultiplier * speedMultiplier);
        }

        if (loseReload) {
            unit.reloadMultiplier(unit.reloadMultiplier / reloadMultiplier);
            reloadMultiplier = 1 + reloadDelta * time;
            unit.reloadMultiplier(unit.reloadMultiplier * reloadMultiplier);
        } else if (randomReload) {
            unit.reloadMultiplier(unit.reloadMultiplier / reloadMultiplier);
            reloadMultiplier = Mathf.random(randomReloadMin, randomReloadMax);
            unit.reloadMultiplier(unit.reloadMultiplier * reloadMultiplier);
        }

        if (loseDamage) {
            unit.damageMultiplier(unit.damageMultiplier / damageMultiplier);
            damageMultiplier = 1 + damageDelta * time;
            unit.damageMultiplier(unit.damageMultiplier * damageMultiplier);
        } else if (randomDamage) {
            unit.damageMultiplier(unit.damageMultiplier / damageMultiplier);
            damageMultiplier = Mathf.random(randomDamageMin, randomDamageMax);
            unit.damageMultiplier(unit.damageMultiplier * damageMultiplier);
        }

        if (loseHealth) {
            unit.healthMultiplier(unit.healthMultiplier / healthMultiplier);
            healthMultiplier = 1 + healthDelta * time;
            unit.healthMultiplier(unit.healthMultiplier * healthMultiplier);
        } else if (randomHealth) {
            unit.healthMultiplier(unit.healthMultiplier / healthMultiplier);
            healthMultiplier = Mathf.random(randomHealthMin, randomHealthMax);
            unit.healthMultiplier(unit.healthMultiplier * healthMultiplier);
        }

        if (effect != Fx.none && Mathf.chanceDelta(effectChance)) {
            Tmp.v1.rnd(Mathf.range(unit.type.hitSize / 2f));
            effect.at(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, 0, color, parentizeEffect ? unit : null);
        }
    }

    @Override
    public void applied(Unit unit, float time, boolean extend) {
        super.applied(unit, time, extend);
        additionStatus.each(unit::apply);
    }

    @Override
    public void init() {
        super.init();

        randomSpeed &= !loseSpeed;
        randomDamage &= !loseDamage;
        randomReload &= !loseReload;
        randomHealth &= !loseHealth;
    }
}
