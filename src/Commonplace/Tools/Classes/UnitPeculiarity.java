package Commonplace.Tools.Classes;

import Commonplace.Tools.Interfaces.PeculiarityC;
import arc.func.Cons;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static mindustry.content.UnitTypes.*;

public abstract class UnitPeculiarity {
    public static final Seq<UnitType> blackList = new Seq<>();
    public static final Seq<Peculiarity> all = new Seq<>();
    public static final Seq<Peculiarity> wellPeculiarity = new Seq<>();
    public static final Seq<Peculiarity> middenPeculiarity = new Seq<>();
    public static final Seq<Peculiarity> badPeculiarity = new Seq<>();

    public static final Seq<Peculiarity> applier = new Seq<>();

    public static void applyWell(PeculiarityC p) {
        var peculiarity = wellPeculiarity.random();
        applier.add(peculiarity);
        p.apply(peculiarity.id);
    }

    public static void applyWell(PeculiarityC p, int num) {
        if (num == 1) {
            applyWell(p);
        } else {
            wellPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                var peculiarity = wellPeculiarity.get(i);
                applier.add(peculiarity);
                p.apply(peculiarity.id);
            }
        }
    }

    public static void applyWellAll(PeculiarityC p, int time) {
        for (int i = 0; i < time; i++) {
            for (var peculiarity : wellPeculiarity) {
                applier.add(peculiarity);
                p.apply(peculiarity.id);
            }
        }
    }

    public static void applyMidden(PeculiarityC p) {
        var peculiarity = middenPeculiarity.random();
        applier.add(peculiarity);
        p.apply(peculiarity.id);
    }

    public static void applyMidden(PeculiarityC p, int num) {
        if (num == 1) {
            applyMidden(p);
        } else {
            middenPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                var peculiarity = middenPeculiarity.get(i);
                applier.add(peculiarity);
                p.apply(peculiarity.id);
            }
        }
    }

    public static void applyMiddenAll(PeculiarityC p, int time) {
        for (int i = 0; i < time; i++) {
            for (var peculiarity : middenPeculiarity) {
                applier.add(peculiarity);
                p.apply(peculiarity.id);
            }
        }
    }

    public static void applyBad(PeculiarityC p) {
        var peculiarity = badPeculiarity.random();
        applier.add(peculiarity);
        p.apply(peculiarity.id);
    }

    public static void applyBad(PeculiarityC p, int num) {
        if (num == 1) {
            applyBad(p);
        } else {
            badPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                var peculiarity = badPeculiarity.get(i);
                applier.add(peculiarity);
                p.apply(peculiarity.id);
            }
        }

    }

    public static void applyBadAll(PeculiarityC p, int time) {
        for (int i = 0; i < time; i++) {
            for (var peculiarity : badPeculiarity) {
                applier.add(peculiarity);
                p.apply(peculiarity.id);
            }
        }
    }

    public static void apply(Unit u, int num, float well, float midden) {
        if (u instanceof PeculiarityC && !blackList.contains(u.type)) {
            int w = 0, m = 0, b = 0;
            for (int i = 0; i < num; i++) {
                float value = Mathf.random();
                if (value >= well) {
                    w++;
                } else if (value >= midden) {
                    m++;
                } else {
                    b++;
                }
            }
            apply(u, w, m, b);
        }
    }

    public static void apply(Unit u, int well, int midden, int bad) {
        if (u instanceof PeculiarityC p && !blackList.contains(u.type)) {
            applier.clear();
            //well
            if (well > wellPeculiarity.size) {
                applyWellAll(p, well / wellPeculiarity.size);
                applyWell(p, well % wellPeculiarity.size);
            } else if (well > 0) {
                applyWell(p, well);
            }
            //midden
            if (midden > middenPeculiarity.size) {
                applyMiddenAll(p, midden / middenPeculiarity.size);
                applyMidden(p, midden % middenPeculiarity.size);
            } else if (midden > 0) {
                applyMidden(p, midden);
            }
            //bad
            if (bad > badPeculiarity.size) {
                applyBadAll(p, bad / badPeculiarity.size);
                applyBad(p, bad % badPeculiarity.size);
            } else if (bad > 0) {
                applyBad(p, bad);
            }

            //smale to big
            applier.sort(pe -> pe.value);
            for (var pe : applier) {
                pe.get(u);
            }
        }
    }

    public static void load(Unit u, int[] peculiar) {
        applier.clear();
        for (int index : peculiar) {
            applier.add(all.get(index));
        }
        applier.sort(p -> p.value);
        for (var p : applier) {
            p.get(u);
        }
    }

    public static void init() {
        blackList.addAll(alpha, beta, gamma, mono);

        //well
        Peculiarity HealGrow = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 1.1f);
            u.health = (int) (u.health * 1.1f);
        });
        Peculiarity HealGrow2 = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 1.3f);
            u.health = (int) (u.health * 1.3f);
        });
        Peculiarity HealGrow3 = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 1.6f);
            u.health = (int) (u.health * 1.6f);
        });
        Peculiarity DamageGrow = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 1.1f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity DamageGrow2 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 1.3f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity DamageGrow3 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 1.6f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity ReloadGrow = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 0.95f;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity ReloadGrow2 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 0.85f;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity ReloadGrow3 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 0.7f;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity Strong = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 5f);
            u.health = (int) (u.health * 5f);
        });

        //midden
        Peculiarity HealToDamage = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 1.1f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
            u.maxHealth = (int) (u.maxHealth * 0.9f);
            u.health = Math.min(u.maxHealth, u.health);
        });
        Peculiarity DamageToReload = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                weapon.reload *= 1.1f;
                bullet.damage *= 0.9f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity FragsToDamage = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 1.1f;
                bullet.fragBullets -= 1;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity Heal2ReloadToDamage = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                weapon.reload *= 1.1f;
                bullet.damage *= 1.35f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
            u.maxHealth = (int) (u.maxHealth * 0.75f);
            u.health = Math.min(u.maxHealth, u.health);
        });
        Peculiarity Glass = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 6.5f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
            u.maxHealth = Math.max((int) (u.type.health / 100), 35);
            u.health = Math.min(u.maxHealth, u.health);
        }, 3);
        Peculiarity Stone = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 5;
                u.mounts[i] = new WeaponMount(weapon);
            }
            u.maxHealth *= 15;
            u.health *= 15;
        });
        Peculiarity Hill = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 10;
                u.mounts[i] = new WeaponMount(weapon);
            }
            u.maxHealth *= 25;
            u.health *= 25;
        });

        //bad
        Peculiarity HealBreak = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 0.95f);
            u.health = Math.min(u.maxHealth, u.health);
        });
        Peculiarity HealBreak2 = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 0.85f);
            u.health = Math.min(u.maxHealth, u.health);
        });
        Peculiarity HealBreak3 = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 0.7f);
            u.health = Math.min(u.maxHealth, u.health);
        });
        Peculiarity DamageBreak = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 0.95f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity DamageBreak2 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 0.85f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity DamageBreak3 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                BulletType bullet = weapon.bullet.copy();
                bullet.damage *= 0.7f;
                weapon.bullet = bullet;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity ReloadBreak = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 1.1f;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity ReloadBreak2 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 1.3f;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity ReloadBreak3 = new Peculiarity(u -> {
            for (int i = 0; i < u.mounts.length; i++) {
                Weapon weapon = u.mounts[i].weapon.copy();
                weapon.reload *= 1.6f;
                u.mounts[i] = new WeaponMount(weapon);
            }
        });
        Peculiarity Incomplete = new Peculiarity(u -> {
            u.maxHealth = (int) (u.maxHealth * 0.45f);
            u.health = Math.min(u.maxHealth, u.health);
        });

        wellPeculiarity.addAll(
                HealGrow, HealGrow, HealGrow, HealGrow, HealGrow, HealGrow,
                HealGrow, HealGrow, HealGrow, HealGrow, HealGrow, HealGrow,
                HealGrow2, HealGrow2, HealGrow2, HealGrow2, HealGrow2, HealGrow2, HealGrow2,
                HealGrow3, HealGrow3, HealGrow3,

                DamageGrow, DamageGrow, DamageGrow, DamageGrow, DamageGrow, DamageGrow,
                DamageGrow, DamageGrow, DamageGrow, DamageGrow, DamageGrow, DamageGrow,
                DamageGrow2, DamageGrow2, DamageGrow2, DamageGrow2, DamageGrow2, DamageGrow2, DamageGrow2,
                DamageGrow3, DamageGrow3, DamageGrow3,

                ReloadGrow, ReloadGrow, ReloadGrow, ReloadGrow, ReloadGrow, ReloadGrow,
                ReloadGrow, ReloadGrow, ReloadGrow, ReloadGrow, ReloadGrow, ReloadGrow,
                ReloadGrow2, ReloadGrow2, ReloadGrow2, ReloadGrow2, ReloadGrow2, ReloadGrow2, ReloadGrow2,
                ReloadGrow3, ReloadGrow3, ReloadGrow3,

                Strong
        );
        middenPeculiarity.addAll(
                HealToDamage, HealToDamage, HealToDamage, HealToDamage, HealToDamage, HealToDamage,
                HealToDamage, HealToDamage, HealToDamage, HealToDamage, HealToDamage, HealToDamage,

                DamageToReload, DamageToReload, DamageToReload, DamageToReload, DamageToReload, DamageToReload,
                DamageToReload, DamageToReload, DamageToReload, DamageToReload, DamageToReload, DamageToReload,

                FragsToDamage, FragsToDamage, FragsToDamage, FragsToDamage, FragsToDamage, FragsToDamage,
                FragsToDamage, FragsToDamage, FragsToDamage, FragsToDamage, FragsToDamage, FragsToDamage,

                Heal2ReloadToDamage, Heal2ReloadToDamage, Heal2ReloadToDamage, Heal2ReloadToDamage,
                Heal2ReloadToDamage, Heal2ReloadToDamage, Heal2ReloadToDamage,

                Glass, Stone, Hill
        );
        badPeculiarity.addAll(
                HealBreak, HealBreak, HealBreak, HealBreak, HealBreak, HealBreak,
                HealBreak, HealBreak, HealBreak, HealBreak, HealBreak, HealBreak,
                HealBreak2, HealBreak2, HealBreak2, HealBreak2, HealBreak2, HealBreak2, HealBreak2,
                HealBreak3, HealBreak3, HealBreak3,

                DamageBreak, DamageBreak, DamageBreak, DamageBreak, DamageBreak, DamageBreak,
                DamageBreak, DamageBreak, DamageBreak, DamageBreak, DamageBreak, DamageBreak,
                DamageBreak2, DamageBreak2, DamageBreak2, DamageBreak2, DamageBreak2, DamageBreak2, DamageBreak2,
                DamageBreak3, DamageBreak3, DamageBreak3,

                ReloadBreak, ReloadBreak, ReloadBreak, ReloadBreak, ReloadBreak, ReloadBreak,
                ReloadBreak, ReloadBreak, ReloadBreak, ReloadBreak, ReloadBreak, ReloadBreak,
                ReloadBreak2, ReloadBreak2, ReloadBreak2, ReloadBreak2, ReloadBreak2, ReloadBreak2, ReloadBreak2,
                ReloadBreak3, ReloadBreak3, ReloadBreak3,

                Incomplete
        );
    }

    public static class Peculiarity implements Cons<Unit> {
        final Cons<Unit> apply;
        final int id;
        final int value;

        public Peculiarity(Cons<Unit> apply) {
            id = all.size;
            all.add(this);
            this.apply = apply;
            this.value = 1;
        }

        public Peculiarity(Cons<Unit> apply, int value) {
            id = all.size;
            all.add(this);
            this.apply = apply;
            this.value = value;
        }

        @Override
        public void get(Unit u) {
            apply.get(u);
        }
    }
}
