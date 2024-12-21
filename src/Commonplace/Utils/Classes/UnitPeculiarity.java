package Commonplace.Utils.Classes;

import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.pooling.Pools;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;

import java.lang.reflect.Field;

import static Commonplace.Loader.DefaultContent.Units2.*;
import static mindustry.content.UnitTypes.*;

public abstract class UnitPeculiarity {

    public static final Seq<UnitType> blackList = new Seq<>();

    public static final Seq<StatusEffect> wellPeculiarity = new Seq<>();
    public static final Seq<StatusEffect> middenPeculiarity = new Seq<>();
    public static final Seq<StatusEffect> badPeculiarity = new Seq<>();

    public static void applyWell(Unit p, int num) {
        if (num == 1) {
            apply(p, wellPeculiarity.random());
        } else {
            wellPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                apply(p, wellPeculiarity.get(i));
            }
        }
    }

    public static void applyMidden(Unit p, int num) {
        if (num == 1) {
            apply(p, middenPeculiarity.random());
        } else {
            middenPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                apply(p, middenPeculiarity.get(i));
            }
        }
    }

    public static void applyBad(Unit p, int num) {
        if (num == 1) {
            apply(p, badPeculiarity.random());
        } else {
            badPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                apply(p, badPeculiarity.get(i));
            }
        }
    }

    public static void apply(Unit u, StatusEffect effect) {
        Class<? extends Unit> unit = u.getClass();
        try {
            Field statuses = unit.getDeclaredField("statuses");
            statuses.setAccessible(true);
            @SuppressWarnings("unchecked")
            Seq<StatusEntry> entry = (Seq<StatusEntry>) statuses.get(u);

            entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(effect, 1));
            effect.applied(u, 1, false);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            var su = unit.getSuperclass();
            while (su != Unit.class) {
                try {
                    Field statuses = su.getDeclaredField("statuses");
                    statuses.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    Seq<StatusEntry> entry = (Seq<StatusEntry>) statuses.get(u);

                    entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(effect, 1));
                    effect.applied(u, 1, false);
                    break;
                } catch (NoSuchFieldException | IllegalAccessException ei) {
                    su = su.getSuperclass();
                }
            }
        }
    }

    public static void applyAll(Unit u, int well, int midden, int bad) {
        Class<? extends Unit> unit = u.getClass();
        try {
            Field statuses = unit.getDeclaredField("statuses");
            statuses.setAccessible(true);
            @SuppressWarnings("unchecked")
            Seq<StatusEntry> entry = (Seq<StatusEntry>) statuses.get(u);

            for (int i = 0; i < well; i++) {
                for (var peculiarity : wellPeculiarity) {
                    entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(peculiarity, 1));
                    peculiarity.applied(u, 1, false);
                }
            }
            for (int i = 0; i < midden; i++) {
                for (var peculiarity : middenPeculiarity) {
                    entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(peculiarity, 1));
                    peculiarity.applied(u, 1, false);
                }
            }
            for (int i = 0; i < bad; i++) {
                for (var peculiarity : badPeculiarity) {
                    entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(peculiarity, 1));
                    peculiarity.applied(u, 1, false);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            var su = unit.getSuperclass();
            while (su != Unit.class) {
                try {
                    Field statuses = su.getDeclaredField("statuses");
                    statuses.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    Seq<StatusEntry> entry = (Seq<StatusEntry>) statuses.get(u);

                    for (int i = 0; i < well; i++) {
                        for (var peculiarity : wellPeculiarity) {
                            entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(peculiarity, 1));
                            peculiarity.applied(u, 1, false);
                        }
                    }
                    for (int i = 0; i < midden; i++) {
                        for (var peculiarity : middenPeculiarity) {
                            entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(peculiarity, 1));
                            peculiarity.applied(u, 1, false);
                        }
                    }
                    for (int i = 0; i < bad; i++) {
                        for (var peculiarity : badPeculiarity) {
                            entry.add(Pools.obtain(StatusEntry.class, StatusEntry::new).set(peculiarity, 1));
                            peculiarity.applied(u, 1, false);
                        }
                    }
                    break;
                } catch (NoSuchFieldException | IllegalAccessException ei) {
                    su = su.getSuperclass();
                }
            }
        }
    }

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

    public static void apply(Unit u, int well, int midden, int bad) {
        if (!blackList.contains(u.type)) {
            //well
            if (well > 0) {
                applyWell(u, well % wellPeculiarity.size);
            }
            //midden
            if (midden > 0) {
                applyMidden(u, midden % middenPeculiarity.size);
            }
            //bad
            if (bad > 0) {
                applyBad(u, bad % badPeculiarity.size);
            }
            applyAll(u, well / wellPeculiarity.size, midden / middenPeculiarity.size, bad / badPeculiarity.size);
        }
    }

    public static void init() {
        blackList.addAll(alpha, beta, gamma, exterminate, garrison, transfer, herald, shuttle1,
                support_a, support_h, velocity, velocity_d, velocity_s, hidden, cave, bulletInterception,
                rejuvenate, rejuvenate_a, vibrate, crane);
    }
}
