package Commonplace.Utils.Classes;

import Commonplace.Type.StatusEffectType.SupperStatus;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import arc.util.pooling.Pools;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static Commonplace.Loader.DefaultContent.Units2.*;
import static mindustry.content.UnitTypes.*;

public abstract class UnitPeculiarity {
    public static final Rand r = new Rand();
    public static final Seq<UnitType> blackList = new Seq<>();

    public static final Seq<StatusPack> mid = new Seq<>();
    public static final Seq<StatusPack> bad = new Seq<>();
    public static final Seq<StatusPack> well = new Seq<>();
    public static final Seq<StatusPack> sup = new Seq<>();

    public static final Map<String, String> opposites = new HashMap<>();

    public static void apply(Unit u, int num, float well, float midden) {
        if (!blackList.contains(u.type)) {
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

    public static void apply(Unit u, int w, int m, int b) {
        if (!blackList.contains(u.type)) {
            if (w > 0) {
                apply(u, w % well.size, well);
            }
            if (m > 0) {
                apply(u, m % mid.size, mid);
            }
            if (b > 0) {
                apply(u, b % bad.size, bad);
            }

            applyAll(u, w / well.size, m / mid.size, b / bad.size);
        }
    }

    public static void applySeed(Unit u, int w, int m, int b) {
        if (!blackList.contains(u.type)) {
            if (w > 0) {
                applySeed(u, w % well.size, well);
            }
            if (m > 0) {
                applySeed(u, m % mid.size, mid);
            }
            if (b > 0) {
                applySeed(u, b % bad.size, bad);
            }

            applyAll(u, w / well.size, m / mid.size, b / bad.size);
        }
    }

    public static void applySuper(Unit u, int num) {
        if (num <= 1) {
            apply(u, sup.random());
        } else {
            sup.shuffle();
            for (int i = 0; i < num; i++) {
                apply(u, sup.get(i));
            }
        }
    }

    public static void applySuperSeed(Unit u, int num) {
        if (num <= 1) {
            apply(u, sup.get(r.nextInt(sup.size)));
        } else {
            shuffle(sup);
            for (int i = 0; i < num; i++) {
                apply(u, sup.get(i));
            }
        }
    }

    public static void apply(Unit u, int num, Seq<StatusPack> effects) {
        if (num == 1) {
            apply(u, effects.random());
        } else {
            effects.shuffle();
            for (int i = 0; i < num; i++) {
                apply(u, effects.get(i));
            }
        }
    }

    public static void applySeed(Unit u, int num, Seq<StatusPack> effects) {
        if (num == 1) {
            apply(u, effects.get(r.nextInt(effects.size)));
        } else {
            shuffle(effects);
            for (int i = 0; i < num; i++) {
                apply(u, effects.get(i));
            }
        }
    }

    public static void applyAll(Unit u, int w, int m, int b) {
        for (int i = 0; i < w; i++) {
            well.each(p -> apply(u, p));
        }
        for (int i = 0; i < m; i++) {
            mid.each(p -> apply(u, p));
        }
        for (int i = 0; i < b; i++) {
            bad.each(p -> apply(u, p));
        }
    }

    public static void apply(Unit u, StatusPack pack) {
        if (pack.effect != null) {
            apply(u, pack.effect);
        }
        if (pack.apply != null) {
            pack.apply.get(u.applyDynamicStatus());
        }
    }

    public static void apply(Unit u, StatusEffect effect) {
        Class<? extends Unit> unit = u.getClass();
        try {
            Field statuses = unit.getDeclaredField("statuses");
            statuses.setAccessible(true);
            @SuppressWarnings("unchecked")
            Seq<StatusEntry> entry = (Seq<StatusEntry>) statuses.get(u);

            apply(u, entry, effect);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            var su = unit.getSuperclass();
            while (su != Unit.class) {
                try {
                    Field statuses = su.getDeclaredField("statuses");
                    statuses.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    Seq<StatusEntry> entry = (Seq<StatusEntry>) statuses.get(u);

                    apply(u, entry, effect);
                    break;
                } catch (NoSuchFieldException | IllegalAccessException ei) {
                    su = su.getSuperclass();
                }
            }
        }
    }

    private static void apply(Unit u, Seq<StatusEntry> entry, StatusEffect effect) {
        String opp = opposites.get(effect.name);
        if (opp != null && entry.contains(e -> opp.equals(e.effect.name))) {
            entry.remove(e -> opp.equals(e.effect.name));
        } else {
            entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(effect, 1));
            effect.applied(u, 1, false);
        }
    }

    public static <T> void shuffle(Seq<T> list) {
        T[] value = list.items;
        for (int i = 0; i < list.size; i++) {
            int n = r.nextInt(list.size);
            T tmp = value[i];
            value[i] = value[n];
            value[n] = tmp;
        }
    }

    public static void setSeed(long seed) {
        well.sort(s -> s.id);
        mid.sort(s -> s.id);
        bad.sort(s -> s.id);
        sup.sort(s -> s.id);
        r.setSeed(seed);
    }

    public static void init() {
        blackList.addAll(alpha, beta, gamma, exterminate, garrison, transfer, herald, shuttle1,
                support_a, support_h, velocity, velocity_d, velocity_s, hidden, cave, bulletInterception,
                rejuvenate, rejuvenate_a, vibrate, crane);
    }

    public static void updateSuper() {
        sup.each(s -> {
            if (s.effect instanceof SupperStatus sta) {
                sta.damageMultiplier = sta.baseDamage + sta.damageAdder.get();
                sta.reloadMultiplier = sta.baseReload + sta.reloadAdder.get();
                sta.healthMultiplier = sta.baseHealth + sta.healthAdder.get();
                sta.speedMultiplier = sta.baseSpeed + sta.speedAdder.get();
                sta.dragMultiplier = sta.speedMultiplier * 0.75f;
            }
        });
    }

    public static class StatusPack {
        public static int all = 0;

        public int id;
        public StatusEffect effect;
        public Cons<StatusEntry> apply;

        public StatusPack(StatusEffect effect) {
            id = all;
            all++;

            this.effect = effect;
            apply = null;
        }

        public StatusPack(Cons<StatusEntry> apply) {
            id = all;
            all++;

            this.apply = apply;
            effect = null;
        }
    }
}
