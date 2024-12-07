package Commonplace.Loader.DefaultContent;

import Commonplace.Type.StatusEffectType.WithMoreStatus;
import arc.func.Cons;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;

import static mindustry.content.StatusEffects.*;

public class StatusEffects2 {
    public final static Cons<StatusEffect> none = s -> {
    };

    public final static Seq<StatusEffect> burnings = new Seq<>();
    public static StatusEffect StrongStop, boostSpeed, HardHit, onePercent,
            torn, suppress, tardy, swift, tension, abyss, gasify, sublimation,
            grow, seethe, friability, back, frenzy, deploy, impatience, loose;

    public static StatusEffect fireKiller;

    //well
    public static StatusEffect peculiarity__heal1, peculiarity__heal2, peculiarity__heal3,
            peculiarity__damage1, peculiarity__damage2, peculiarity__damage3,
            peculiarity__reload1, peculiarity__reload2, peculiarity__reload3,
            peculiarity__grow;

    //midden
    public static StatusEffect peculiarity_heal__damage, peculiarity_damage__reload,
            peculiarity_heal_reload__damage,
            peculiarity_glass, peculiarity_stone, peculiarity_hill;

    //bad
    public static StatusEffect peculiarity_heal1, peculiarity_heal2, peculiarity_heal3,
            peculiarity_damage1, peculiarity_damage2, peculiarity_damage3,
            peculiarity_reload1, peculiarity_reload2, peculiarity_reload3,
            peculiarity_incomplete;

    public static StatusEffect pureA, pureT,
            catalyzeI, catalyzeII, catalyzeIII, catalyzeIV, catalyzeV,
            corrosionI, corrosionII, corrosionIII, corrosionIV, corrosionV,
            corrosionVI, corrosionVII, corrosionVIII, corrosionIX, corrosionX;

    public static WithMoreStatus pWet, pTarred, pFreezing, pMelting, pMuddy;

    public static void load() {
        onePercent = new StatusEffect("one-percent") {{
            show = false;

            speedMultiplier = 0.01f;
            reloadMultiplier = 0.01f;
            dragMultiplier = 0.01f;
        }};
        StrongStop = new StatusEffect("strong_stop") {{
            show = false;

            speedMultiplier = 0;
            buildSpeedMultiplier = 0;
            disarm = true;
        }};
        boostSpeed = new StatusEffect("boost_speed") {{
            speedMultiplier = 15;
            show = false;
        }};
        HardHit = new StatusEffect("hard_hit") {{
            damageMultiplier = 0.8F;
            speedMultiplier = 0.75F;
            healthMultiplier = 0.4F;
            reloadMultiplier = 0.5F;
        }};
        torn = new StatusEffect("torn") {{
            speedMultiplier = 0.8F;
            healthMultiplier = 0.8F;
        }};
        suppress = new StatusEffect("suppress") {{
            speedMultiplier = 0.6F;
            healthMultiplier = 0.6F;
        }};
        loose = new StatusEffect("loose") {{
            speedMultiplier = 0.4f;
            healthMultiplier = 0.4f;
            reloadMultiplier = 0.8f;
        }};
        tardy = new StatusEffect("tardy") {{
            speedMultiplier = 0.55F;
        }};
        swift = new StatusEffect("swift") {{
            speedMultiplier = 2.4f;
            init(() -> {
                opposite(StatusEffects.slow);
                opposite(tardy);
            });
        }};
        tension = new StatusEffect("tension") {{
            speedMultiplier = 0.85f;
            reloadMultiplier = 0.85f;
            healthMultiplier = 0.95f;
            damage = 6;
        }};
        abyss = new StatusEffect("abyss") {{
            speedMultiplier = 0.1f;
            reloadMultiplier = 0.1f;
            healthMultiplier = 0.7f;
            damage = 54;
        }};
        burning.transitionDamage = 54;
        burning.damage = 0.9f;

        gasify = new StatusEffect("gasify") {{
            damage = 1.6f;
            transitionDamage = 85;
            effect = Fx.burning;

            init(() -> {
                opposite(wet, StatusEffects.freezing, fireKiller);
                affinity(StatusEffects.tarred, (unit, result, time) -> {
                    unit.damagePierce(transitionDamage);
                    Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                    result.set(gasify, Math.min(time + result.time, 300f));
                });
            });
        }};
        sublimation = new StatusEffect("sublimation") {{
            damage = 4;
            transitionDamage = 137;
            effect = Fx.burning;

            init(() -> {
                opposite(wet, StatusEffects.freezing, fireKiller);
                affinity(StatusEffects.tarred, (unit, result, time) -> {
                    unit.damagePierce(transitionDamage);
                    Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                    result.set(sublimation, Math.min(time + result.time, 300f));
                });
            });
        }};
        grow = new StatusEffect("grow") {{
            healthMultiplier = 7;
            damage = -3;
        }};
        seethe = new StatusEffect("seethe") {{
            damageMultiplier = 2.7f;
            speedMultiplier = 1.9f;
            reloadMultiplier = 3f;
            healthMultiplier = 0.56f;
            damage = 4f;
        }};
        friability = new StatusEffect("friability") {{
            healthMultiplier = 0.001f;
            damageMultiplier = 25;
            speedMultiplier = 3.8f;
        }};
        back = new StatusEffect("back") {{
            healthMultiplier = 2.4f;
            damage = -1;
        }};
        frenzy = new StatusEffect("frenzy") {{
            healthMultiplier = 2f;
            speedMultiplier = 1.75f;
            reloadMultiplier = 2f;

            init(() -> {
                opposite(wet, freezing);
                affinity(burning, (unit, result, time) -> {
                    unit.apply(overdrive, Math.min(unit.getDuration(overdrive) + 300f, 36000));
                    result.set(corroded, 600);
                });
            });
        }};
        deploy = new StatusEffect("deploy") {{
            healthMultiplier = 3.5f;
            damageMultiplier = 2.5f;
            reloadMultiplier = 2.4f;
            speedMultiplier = 0.15f;

            init(() -> {
                opposite(burning, gasify, sublimation);
                affinity(burning, (unit, result, time) -> {
                    unit.damagePierce(20);
                    result.set(impatience, time + result.time);
                });
            });
        }};
        impatience = new StatusEffect("impatience") {{
            healthMultiplier = 0.7f;
            damageMultiplier = 0.6f;
            reloadMultiplier = 1.33f;
            speedMultiplier = 1.5f;
        }};


        fireKiller = new StatusEffect("fire-killer") {{
            opposite(burning);
            affinity(burning, (unit, result, time) -> unit.unapply(burning));
            affinity(gasify, (unit, result, time) -> unit.unapply(gasify));
            affinity(sublimation, (unit, result, time) -> unit.unapply(sublimation));
        }};


        pureA = new StatusEffect("pure-a") {{
            show = false;
            permanent = true;
        }};
        pureT = new StatusEffect("pure-t") {{
            show = false;
        }};
        catalyzeI = new StatusEffect("catalyze-1") {{
            show = false;
        }};
        catalyzeII = new StatusEffect("catalyze-2") {{
            show = false;
        }};
        catalyzeIII = new StatusEffect("catalyze-3") {{
            show = false;
        }};
        catalyzeIV = new StatusEffect("catalyze-4") {{
            show = false;
        }};
        catalyzeV = new StatusEffect("catalyze-5") {{
            show = false;
        }};
        corrosionI = new StatusEffect("corrosion-1") {{
            damage = 0.1f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionII, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionIII, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionIV, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionV, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionVI, s.time * 2.5f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionII = new StatusEffect("corrosion-2") {{
            damage = 0.3f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionIII, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionIV, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionV, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionVI, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionVII, s.time * 2.5f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionIII = new StatusEffect("corrosion-3") {{
            damage = 0.6f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionIV, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionV, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionVI, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionVII, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionVIII, s.time * 2.5f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionIV = new StatusEffect("corrosion-4") {{
            damage = 1f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionV, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionVI, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionVII, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionVIII, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionIX, s.time * 2.5f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionV = new StatusEffect("corrosion-5") {{
            damage = 1.5f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionVI, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionVII, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionVIII, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionIX, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2.5f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionVI = new StatusEffect("corrosion-6") {{
            damage = 1.8f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionVII, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionVIII, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionIX, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 3f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionVII = new StatusEffect("corrosion-7") {{
            damage = 2.3f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionVIII, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionIX, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 1.7f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2.5f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 3.2f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionVIII = new StatusEffect("corrosion-8") {{
            damage = 2.9f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionIX, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 1.4f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2.8f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 3.6f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionIX = new StatusEffect("corrosion-9") {{
            damage = 3f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 1.2f);
                    u.unapply(this);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 1.8f);
                    u.unapply(this);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2.3f);
                    u.unapply(this);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 3f);
                    u.unapply(this);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 4f);
                    u.unapply(this);
                    u.unapply(catalyzeV);
                });
            });
        }};
        corrosionX = new StatusEffect("corrosion-10") {{
            damage = 4.5f;
            transitions.put(StatusEffects2.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(StatusEffects2.pureT, (u, s, t) -> u.unapply(this));
            init(() -> {
                trans(catalyzeI, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 1.5f);
                    u.unapply(catalyzeI);
                });
                trans(catalyzeII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2f);
                    u.unapply(catalyzeII);
                });
                trans(catalyzeIII, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 2.6f);
                    u.unapply(catalyzeIII);
                });
                trans(catalyzeIV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 3.4f);
                    u.unapply(catalyzeIV);
                });
                trans(catalyzeV, (u, s, t) -> {
                    u.apply(corrosionX, s.time * 4.5f);
                    u.unapply(catalyzeV);
                });
            });
        }};
        pWet = new WithMoreStatus("p-wet") {{
            show = false;
            sign = true;
            with.addAll(wet, corrosionI);
        }};
        pTarred = new WithMoreStatus("p-tarred") {{
            show = false;
            sign = true;
            with.addAll(StatusEffects.tarred, corrosionI);
        }};
        pFreezing = new WithMoreStatus("p-freezing") {{
            show = false;
            sign = true;
            with.addAll(StatusEffects.freezing, corrosionI);
        }};
        pMelting = new WithMoreStatus("p-melting") {{
            show = false;
            sign = true;
            with.addAll(StatusEffects.melting, corrosionI);
        }};
        pMuddy = new WithMoreStatus("p-muddy") {{
            show = false;
            sign = true;
            with.addAll(StatusEffects.muddy, corrosionI);
        }};
        burnings.addAll(burning, gasify);

        load_peculiarity();
    }

    public static void load_peculiarity() {
        peculiarity__heal1 = peculiarity_heal("__h1", 1.05f, none);
        peculiarity__heal2 = peculiarity_heal("__h2", 1.1f, none);
        peculiarity__heal3 = peculiarity_heal("__h3", 1.15f, none);
        peculiarity__damage1 = peculiarity_damage("__d1", 1.05f, none);
        peculiarity__damage2 = peculiarity_damage("__d2", 1.1f, none);
        peculiarity__damage3 = peculiarity_damage("__d3", 1.15f, none);
        peculiarity__reload1 = peculiarity_reload("__r1", 1.05f, none);
        peculiarity__reload2 = peculiarity_reload("__r2", 1.1f, none);
        peculiarity__reload3 = peculiarity_reload("__r3", 1.15f, none);
        peculiarity__grow = peculiarity_heal("___grow", 2f, none);

        peculiarity_heal__damage = peculiarity("_h__d", 1.1f, 1, 0.9f, 1, none);
        peculiarity_damage__reload = peculiarity("_d__r", 0.9f, 1, 1, 1.1f, none);
        peculiarity_heal_reload__damage = peculiarity("_h_r__d", 1.1f, 1, 0.95f, 0.95f, none);
        peculiarity_glass = peculiarity("__glass", 2.5f, 1, 0.1f, 1f, none);
        peculiarity_hill = peculiarity("___hill", 1, 1, 5f, 0.2f, none);

        peculiarity_heal1 = peculiarity_heal("_h1", 0.95f, none);
        peculiarity_heal2 = peculiarity_heal("_h2", 0.9f, none);
        peculiarity_heal3 = peculiarity_heal("_h3", 0.85f, none);
        peculiarity_damage1 = peculiarity_damage("_d1", 0.95f, none);
        peculiarity_damage2 = peculiarity_damage("_d2", 0.9f, none);
        peculiarity_damage3 = peculiarity_damage("_d3", 0.85f, none);
        peculiarity_reload1 = peculiarity_reload("_r1", 0.95f, none);
        peculiarity_reload2 = peculiarity_reload("_r2", 0.9f, none);
        peculiarity_reload3 = peculiarity_reload("_r3", 0.85f, none);
        peculiarity_incomplete = peculiarity_heal("___incomplete", 0.5f, none);
    }

    public static StatusEffect peculiarity_heal(String name, float mul, Cons<StatusEffect> change) {
        return peculiarity(name, 1, 1, mul, 1, change);
    }

    public static StatusEffect peculiarity_damage(String name, float mul, Cons<StatusEffect> change) {
        return peculiarity(name, mul, 1, 1, 1, change);
    }

    public static StatusEffect peculiarity_reload(String name, float mul, Cons<StatusEffect> change) {
        return peculiarity(name, 1, 1, 1, mul, change);
    }

    public static StatusEffect peculiarity(String name, float damageMul, float speedMul, float healMul, float reloadMul, Cons<StatusEffect> change) {
        StatusEffect result = new StatusEffect(name) {{
            show = false;
            permanent = true;
            damageMultiplier = damageMul;
            speedMultiplier = speedMul;
            healthMultiplier = healMul;
            reloadMultiplier = reloadMul;
        }};
        change.get(result);
        return result;
    }
}