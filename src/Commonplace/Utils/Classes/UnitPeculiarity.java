package Commonplace.Utils.Classes;

import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;

import static Commonplace.Loader.DefaultContent.StatusEffects2.*;
import static Commonplace.Loader.DefaultContent.Units2.*;
import static mindustry.content.UnitTypes.*;

public abstract class UnitPeculiarity {

    public static final Seq<UnitType> blackList = new Seq<>();

    public static final Seq<StatusEffect> wellPeculiarity = new Seq<>();
    public static final Seq<StatusEffect> middenPeculiarity = new Seq<>();
    public static final Seq<StatusEffect> badPeculiarity = new Seq<>();

    public static void applyWell(Unit p, int num) {
        if (num == 1) {
            p.apply(wellPeculiarity.random());
        } else {
            wellPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                p.apply(wellPeculiarity.get(i));
            }
        }
    }

    public static void applyWellAll(Unit p, int time) {
        for (int i = 0; i < time; i++) {
            for (var peculiarity : wellPeculiarity) {
                p.apply(peculiarity);
            }
        }
    }

    public static void applyMidden(Unit p, int num) {
        if (num == 1) {
            p.apply(middenPeculiarity.random());
        } else {
            middenPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                p.apply(middenPeculiarity.get(i));
            }
        }
    }

    public static void applyMiddenAll(Unit p, int time) {
        for (int i = 0; i < time; i++) {
            for (var peculiarity : middenPeculiarity) {
                p.apply(peculiarity);
            }
        }
    }

    public static void applyBad(Unit p, int num) {
        if (num == 1) {
            p.apply(badPeculiarity.random());
        } else {
            badPeculiarity.shuffle();
            for (int i = 0; i < num; i++) {
                p.apply(badPeculiarity.get(i));
            }
        }
    }

    public static void applyBadAll(Unit p, int time) {
        for (int i = 0; i < time; i++) {
            for (var peculiarity : badPeculiarity) {
                p.apply(peculiarity);
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
                applyWellAll(u, well / wellPeculiarity.size);
                applyWell(u, well % wellPeculiarity.size);
            }
            //midden
            if (midden > 0) {
                applyMiddenAll(u, midden / middenPeculiarity.size);
                applyMidden(u, midden % middenPeculiarity.size);
            }
            //bad
            if (bad > 0) {
                applyBadAll(u, bad / badPeculiarity.size);
                applyBad(u, bad % badPeculiarity.size);
            }
        }
    }

    public static void init() {
        blackList.addAll(alpha, beta, gamma, mono, exterminate, garrison, transfer, herald, shuttle1,
                support_a, support_h, velocity, velocity_d, velocity_s, hidden, cave, strike, bulletInterception,
                rejuvenate, rejuvenate_a, vibrate, crane);

        wellPeculiarity.addAll(peculiarity__heal1, peculiarity__heal2, peculiarity__heal3,
                peculiarity__damage1, peculiarity__damage2, peculiarity__damage3,
                peculiarity__reload1, peculiarity__reload2, peculiarity__reload3,
                peculiarity__grow);

        middenPeculiarity.addAll(peculiarity_heal__damage, peculiarity_damage__reload,
                peculiarity_heal_reload__damage, peculiarity_glass, peculiarity_stone, peculiarity_hill);

        badPeculiarity.addAll(peculiarity_heal1, peculiarity_heal2, peculiarity_heal3,
                peculiarity_damage1, peculiarity_damage2, peculiarity_damage3,
                peculiarity_reload1, peculiarity_reload2, peculiarity_reload3,
                peculiarity_incomplete);
    }
}
