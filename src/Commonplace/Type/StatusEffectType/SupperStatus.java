package Commonplace.Type.StatusEffectType;

import arc.func.Floatp;
import mindustry.type.StatusEffect;

public class SupperStatus extends StatusEffect {
    public float baseDamage = 1;
    public float baseReload = 1;
    public float baseHealth = 1;
    public float baseSpeed = 1;
    public Floatp damageAdder = () -> 0;
    public Floatp reloadAdder = () -> 0;
    public Floatp healthAdder = () -> 0;
    public Floatp speedAdder = () -> 0;

    public SupperStatus(String name) {
        super(name);
    }
}
