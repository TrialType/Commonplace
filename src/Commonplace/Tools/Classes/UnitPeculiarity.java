package Commonplace.Tools.Classes;

import Commonplace.Tools.Interfaces.PeculiarityC;
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
            u.maxHealth = (int) (u.maxHealth * 1.1f);
            u.health = (int) (u.health * 1.1f);
        };
        Peculiarity HealBreak = u -> {
            u.maxHealth = (int) (u.maxHealth * 0.95f);
            u.health = Math.min(u.maxHealth, u.health);
        };
        Peculiarity HealToDamage = u -> {
            for (WeaponMount mount : u.mounts) {
                BulletType bullet = mount.weapon.bullet.copy();
                bullet.damage *= 1.05f;
                mount.weapon.bullet = bullet;
            }
            u.maxHealth = (int) (u.maxHealth * 0.95f);
            u.health = Math.min(u.maxHealth, u.health);
        };

        all.addAll(HealGrow, HealBreak, HealToDamage);
        wellPeculiarity.addAll(HealGrow);
        middenPeculiarity.addAll(HealToDamage);
        badPeculiarity.addAll(HealBreak);
    }

    public static void applyWell(Unit u, PeculiarityC p) {
        Peculiarity peculiarity = wellPeculiarity.random();
        peculiarity.apply(u);
        p.apply(all.indexOf(peculiarity));
    }

    public static void applyBad(Unit u, PeculiarityC p) {
        Peculiarity peculiarity = badPeculiarity.random();
        peculiarity.apply(u);
        p.apply(all.indexOf(peculiarity));
    }

    public static void applyMidden(Unit u, PeculiarityC p) {
        Peculiarity peculiarity = middenPeculiarity.random();
        peculiarity.apply(u);
        p.apply(all.indexOf(peculiarity));
    }

    public static void apply(Unit u, int num, float well, float midden) {
        if (u instanceof PeculiarityC p && !blackList.contains(u.type)) {
            for (int i = 0; i < num; i++) {
                float value = Mathf.random(1f);
                if (value >= well) {
                    applyWell(u, p);
                } else if (value >= midden) {
                    applyMidden(u, p);
                } else {
                    applyBad(u, p);
                }
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
