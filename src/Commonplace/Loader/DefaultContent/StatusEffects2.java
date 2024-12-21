package Commonplace.Loader.DefaultContent;

import Commonplace.Type.StatusEffectType.WithMoreStatus;
import arc.func.Cons;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;

import static Commonplace.Utils.Classes.UnitPeculiarity.*;
import static mindustry.content.StatusEffects.*;

public class StatusEffects2 {
    public final static Cons<StatusEffect> well = wellPeculiarity::add;
    public final static Cons<StatusEffect> well3 = s -> {
        wellPeculiarity.add(s);
        wellPeculiarity.add(s);
        wellPeculiarity.add(s);
    };
    public final static Cons<StatusEffect> well5 = s -> {
        wellPeculiarity.add(s);
        wellPeculiarity.add(s);
        wellPeculiarity.add(s);
        wellPeculiarity.add(s);
        wellPeculiarity.add(s);
    };
    public final static Cons<StatusEffect> midden = middenPeculiarity::add;
    public final static Cons<StatusEffect> midden3 = s -> {
        middenPeculiarity.add(s);
        middenPeculiarity.add(s);
        middenPeculiarity.add(s);
    };
    public final static Cons<StatusEffect> midden5 = s -> {
        middenPeculiarity.add(s);
        middenPeculiarity.add(s);
        middenPeculiarity.add(s);
        middenPeculiarity.add(s);
        middenPeculiarity.add(s);
    };
    public final static Cons<StatusEffect> bad = badPeculiarity::add;
    public final static Cons<StatusEffect> bad3 = s -> {
        badPeculiarity.add(s);
        badPeculiarity.add(s);
        badPeculiarity.add(s);
    };
    public final static Cons<StatusEffect> bad5 = s -> {
        badPeculiarity.add(s);
        badPeculiarity.add(s);
        badPeculiarity.add(s);
        badPeculiarity.add(s);
        badPeculiarity.add(s);
    };

    public final static Seq<StatusEffect> burnings = new Seq<>();
    public static StatusEffect StrongStop, boostSpeed, HardHit, onePercent,
            torn, suppress, tardy, swift, tension, abyss, gasify, sublimation,
            grow, seethe, friability, back, frenzy, deploy, impatience, loose;

    public static StatusEffect fireKiller;

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
            healthMultiplier = 0.01f;
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
            opposite(gasify);
            opposite(sublimation);
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
        peculiarity_heal("__h1", 1.05f, well5);
        peculiarity_heal("__h2", 1.1f, well3);
        peculiarity_heal("__h3", 1.15f, well);
        peculiarity_damage("__d1", 1.05f, well5);
        peculiarity_damage("__d2", 1.1f, well3);
        peculiarity_damage("__d3", 1.15f, well);
        peculiarity_reload("__r1", 1.05f, well5);
        peculiarity_reload("__r2", 1.1f, well3);
        peculiarity_reload("__r3", 1.15f, well);
        peculiarity_heal("___grow", 2f, well);
        peculiarity_reload("___oil", 2f, well);
        peculiarity("__r__h__d", 1.05f, 1f, 1.05f, 1.05f, well);

        peculiarity("_h__d", 1.1f, 1, 0.91f, 1, midden5);
        peculiarity("_d__r", 0.91f, 1, 1, 1.1f, midden5);
        peculiarity("_s__r", 1, 0.91f, 1, 1.1f, midden5);
        peculiarity("_h__r", 1, 1, 0.91f, 1.1f, midden5);
        peculiarity("_r__s", 1, 1.1f, 1, 0.91f, midden5);
        peculiarity("_h_r__d", 1.1f, 1, 0.96f, 0.96f, midden3);
        peculiarity("_h_s__d", 1.1f, 0.96f, 1, 0.96f, midden3);
        peculiarity("___glass", 2f, 1, 0.5f, 1f, midden);
        peculiarity("___loading", 1f, 1 / 1.5f, 1 / 1.5f, 3f, midden);
        peculiarity("___X", 0.5f, 1, 1, 2, midden);
        peculiarity("___armour", 1, 0.5f, 2, 1, midden);
        peculiarity("___stone", 1, 1, 1.5f, 1 / 1.5f, midden);
        peculiarity("___hill", 1, 1, 2.5f, 0.4f, midden);

        peculiarity_heal("_h1", 0.96f, bad5);
        peculiarity_heal("_h2", 0.91f, bad3);
        peculiarity_heal("_h3", 0.86f, bad);
        peculiarity_damage("_d1", 0.96f, bad5);
        peculiarity_damage("_d2", 0.91f, bad3);
        peculiarity_damage("_d3", 0.86f, bad);
        peculiarity_reload("_r1", 0.96f, bad5);
        peculiarity_reload("_r2", 0.91f, bad3);
        peculiarity_reload("_r3", 0.86f, bad);
        peculiarity_heal("___incomplete", 0.5f, bad);
        peculiarity_reload("___lock", 0.5f, bad);
    }

    public static void peculiarity_heal(String name, float mul, Cons<StatusEffect> change) {
        peculiarity(name, 1, 1, mul, 1, change);
    }

    public static void peculiarity_damage(String name, float mul, Cons<StatusEffect> change) {
        peculiarity(name, mul, 1, 1, 1, change);
    }

    public static void peculiarity_reload(String name, float mul, Cons<StatusEffect> change) {
        peculiarity(name, 1, 1, 1, mul, change);
    }

    public static void peculiarity(String name, float damageMul, float speedMul, float healMul, float reloadMul, Cons<StatusEffect> change) {
        StatusEffect result = new StatusEffect(name) {{
            show = false;
            permanent = true;
            damageMultiplier = damageMul;
            speedMultiplier = speedMul;
            healthMultiplier = healMul;
            reloadMultiplier = reloadMul;
        }};
        change.get(result);
    }
}