package Floor.FTools.Lists;

import arc.func.Cons;
import arc.struct.IntIntMap;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.type.Weapon;

public class BoostProject extends UnitProject {
    public static Cons<Weapon> weaponBoos = w -> {
    };
    public static Cons<Ability> abilityBoos = a -> {
    };

    public int frags = 0;
    public BulletType defFrag;
    public float damage = 0f;
    public float damagePercent = 1f;
    public int pierce = 0;
    public float fireChance = 0f;
    public float reloadW = 1;
    public Cons<Weapon> applyW = w -> {
    };

    public float rangePercent = 1f;
    public float shieldPercent = 1f;
    public float reloadA = 1;
    public Cons<Ability> applyA = a -> {
    };

    public BoostProject(float heavy, String name) {
        super(heavy, name, u -> {
        });
    }

    public static void updateApply(IntIntMap map) {
        weaponBoos = w -> map.forEach(e -> {
            BoostProject b = (BoostProject) all.get(e.key);
            for (int i = 0; i < e.value; i++) {
                b.applyW.get(w);
            }
        });
        abilityBoos = a -> map.forEach(e -> {
            BoostProject b = (BoostProject) all.get(e.key);
            for (int i = 0; i < e.value; i++) {
                b.applyA.get(a);
            }
        });
    }
}
