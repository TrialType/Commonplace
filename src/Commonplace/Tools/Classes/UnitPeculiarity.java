package Commonplace.Tools.Classes;

import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import static mindustry.content.UnitTypes.*;

public abstract class UnitPeculiarity {
    public static final Seq<UnitType> blackList = new Seq<>();
    public static final Seq<Peculiarity> all = new Seq<>();
    public static final Seq<Peculiarity> wellPeculiarity = new Seq<>();
    public static final Seq<Peculiarity> middenPeculiarity = new Seq<>();
    public static final Seq<Peculiarity> badPeculiarity = new Seq<>();

    public static void init() {
        blackList.addAll(alpha, beta, gamma, mono);

        Peculiarity HealGrow = u -> {
            u.maxHealth *= 1.5f;
            u.health *= 1.5f;
        };
        Peculiarity HealBreak = u -> u.maxHealth *= 0.75f;
        Peculiarity HealToDamage = u -> {
            for (WeaponMount mount : u.mounts) {
                BulletType bullet = mount.weapon.bullet.copy();
                bullet.damage *= 1.25f;
                mount.weapon.bullet = bullet;
            }
            u.maxHealth *= 0.75f;
        };

        all.addAll(HealGrow, HealBreak, HealToDamage);
        wellPeculiarity.addAll(HealGrow);
        middenPeculiarity.addAll(HealToDamage);
        badPeculiarity.addAll(HealBreak);
    }

    public static void applyWell(Unit u, int num) {
        Mathf.randomSeed(u.id);
        wellPeculiarity.shuffle();
        num = Math.min(num, wellPeculiarity.size);
        for (int i = 0; i < num; i++) {
            wellPeculiarity.get(i).apply(u);
        }
    }

    public static void applyWell(Unit u) {
        Mathf.randomSeed(u.id);
        wellPeculiarity.random().apply(u);
    }

    public static void applyBad(Unit u, int num) {
        Mathf.randomSeed(u.id);
        badPeculiarity.shuffle();
        num = Math.min(num, badPeculiarity.size);
        for (int i = 0; i < num; i++) {
            badPeculiarity.get(i).apply(u);
        }
    }

    public static void applyBad(Unit u) {
        Mathf.randomSeed(u.id);
        badPeculiarity.random().apply(u);
    }

    public static void applyMidden(Unit u, int num) {
        Mathf.randomSeed(u.id);
        middenPeculiarity.shuffle();
        num = Math.min(num, middenPeculiarity.size);
        for (int i = 0; i < num; i++) {
            middenPeculiarity.get(i).apply(u);
        }
    }

    public static void applyMidden(Unit u) {
        Mathf.randomSeed(u.id);
        middenPeculiarity.random().apply(u);
    }

    public static void apply(Unit u, int num, float well, float midden) {
        Mathf.randomSeed(u.id);
        for (int i = 0; i < num; i++) {
            float value = Mathf.random(1f);
            if (value >= well) {
                applyWell(u);
            } else if (value >= midden) {
                applyMidden(u);
            } else {
                applyBad(u);
            }
        }
    }

    public static void load(Unit u, int[] peculiar) {
        for (int index : peculiar) {
            all.get(index).apply(u);
        }
    }

    public interface Peculiarity {
        void apply(Unit u);
    }
}
