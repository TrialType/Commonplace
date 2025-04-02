package Commonplace.Type.StatusEffectType;

import arc.func.Floatp;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class SupperStatus extends StatusEffect {
    public float baseDamageMultiplier = 1;
    public float baseReloadMultiplier = 1;
    public float baseHealthMultiplier = 1;
    public float baseSpeedMultiplier = 1;
    public Floatp waveDamageAdder = () -> 0;
    public Floatp waveReloadAdder = () -> 0;
    public Floatp waveHealthAdder = () -> 0;
    public Floatp waveSpeedAdder = () -> 0;

    public SupperStatus(String name) {
        super(name);
    }

    public void update(Unit unit, float time) {
        damageMultiplier = baseDamageMultiplier + waveDamageAdder.get();
        reloadMultiplier = baseReloadMultiplier + waveReloadAdder.get();
        healthMultiplier = baseHealthMultiplier + waveHealthAdder.get();
        speedMultiplier = baseSpeedMultiplier + waveSpeedAdder.get();
        dragMultiplier = speedMultiplier * 0.75f;

        super.update(unit, time);
    }
}
