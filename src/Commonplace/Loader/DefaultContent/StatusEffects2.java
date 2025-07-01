package Commonplace.Loader.DefaultContent;

import Commonplace.Loader.Special.Effects;
import Commonplace.Type.StatusEffectType.ConditionsNumberStatusEffect;
import Commonplace.Type.StatusEffectType.SupperStatus;
import arc.func.Cons;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.effect.ParticleEffect;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;

import java.lang.reflect.Field;

import static Commonplace.Utils.Classes.UnitPeculiarity.*;
import static mindustry.content.StatusEffects.*;

public class StatusEffects2 {
//    public final static Cons<StatusEffect> well = wellPeculiarity::add;
//    public final static Cons<StatusEffect> well3 = s -> {
//        wellPeculiarity.add(s);
//        wellPeculiarity.add(s);
//        wellPeculiarity.add(s);
//    };
//    public final static Cons<StatusEffect> well5 = s -> {
//        wellPeculiarity.add(s);
//        wellPeculiarity.add(s);
//        wellPeculiarity.add(s);
//        wellPeculiarity.add(s);
//        wellPeculiarity.add(s);
//    };
//    public final static Cons<StatusEffect> midden = middenPeculiarity::add;
//    public final static Cons<StatusEffect> midden3 = s -> {
//        middenPeculiarity.add(s);
//        middenPeculiarity.add(s);
//        middenPeculiarity.add(s);
//    };
//    public final static Cons<StatusEffect> midden5 = s -> {
//        middenPeculiarity.add(s);
//        middenPeculiarity.add(s);
//        middenPeculiarity.add(s);
//        middenPeculiarity.add(s);
//        middenPeculiarity.add(s);
//    };
//    public final static Cons<StatusEffect> bad = badPeculiarity::add;
//    public final static Cons<StatusEffect> bad3 = s -> {
//        badPeculiarity.add(s);
//        badPeculiarity.add(s);
//        badPeculiarity.add(s);
//    };
//    public final static Cons<StatusEffect> bad5 = s -> {
//        badPeculiarity.add(s);
//        badPeculiarity.add(s);
//        badPeculiarity.add(s);
//        badPeculiarity.add(s);
//        badPeculiarity.add(s);
//    };
//    public final static Cons<StatusPack> node = p -> {
//    };

    public final static Floatp zero = () -> 0f;
    public final static Floatp wave30t50t20l4 = () -> {
        int wave = Vars.state.wave - 1;
        if (wave < 30) {
            return wave / 30f;
        } else if (wave < 80) {
            return 1 + (wave - 30) / 50f;
        } else {
            return 2 + Math.min(2, (wave - 80) / 20f);
        }
    };
    public final static Floatp wave20l14 = () -> Math.min(1.1f, Vars.state.wave / 20f);
    public final static Floatp wave30t80t20l2 = () -> {
        int wave = Vars.state.wave - 1;
        if (wave < 30) {
            return wave / 60f;
        } else if (wave < 80) {
            return 0.5f + (wave - 30) / 100f;
        } else {
            return 1f + Math.min(1f, (wave - 80) / 40f);
        }
    };

    public final static Seq<StatusEffect> burnings = new Seq<>();
    public static StatusEffect StrongStop, boostSpeed, HardHit, onePercent,
            torn, tardy, swift, tension, abyss, gasify, sublimation,
            grow, seethe, friability, back, frenzy, deploy, impatience, loose,
            shocked, aging, erosion, disturb, fearless, sluggish;

    public static StatusEffect fireKiller;

    public static StatusEffect pureA, pureT,
            catalyzeI, catalyzeII, catalyzeIII, catalyzeIV, catalyzeV,
            corrosionI, corrosionII, corrosionIII, corrosionIV, corrosionV,
            corrosionVI, corrosionVII, corrosionVIII, corrosionIX, corrosionX;

    public static void load() {
        onePercent = new StatusEffect("one-percent") {{
            show = false;

            speedMultiplier = 0.01f;
            reloadMultiplier = 0.01f;
            dragMultiplier = 0.01f;
        }};
        StrongStop = new StatusEffect("strong-stop") {{
            show = false;

            speedMultiplier = 0;
            buildSpeedMultiplier = 0;
            disarm = true;
        }};
        boostSpeed = new StatusEffect("boost-speed") {{
            speedMultiplier = 15;
            show = false;
        }};
        HardHit = new StatusEffect("hard-hit") {{
            damageMultiplier = 0.8F;
            speedMultiplier = 0.75F;
            healthMultiplier = 0.4F;
            reloadMultiplier = 0.5F;
        }};
        torn = new StatusEffect("torn") {{
            speedMultiplier = 0.7F;
            healthMultiplier = 0.7F;
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
        shocked = new StatusEffect("shocked") {{
            speedMultiplier = 0.75f;
            reloadMultiplier = 0.65f;
            damageMultiplier = 0.55f;
        }};
        disturb = new StatusEffect("disturb") {{
            speedMultiplier = 0.9f;
            reloadMultiplier = 0.9f;
            effect = Effects.disturb;
            effectChance = 0.2f;
            dragMultiplier = 0.6f;
        }};
        sluggish = new StatusEffect("sluggish") {{
            reloadMultiplier = 0.25f;
            speedMultiplier = 0.5f;
        }};


        aging = new ConditionsNumberStatusEffect("aging") {{
            speedMultiplier = 0.95f;
            reloadMultiplier = 0.95f;
            damageMultiplier = 1.05f;
            healthMultiplier = 0.95f;
            damageResult = u -> u.vel.len() / Vars.tilesize;

            opposite(tarred);
        }};
        erosion = new ConditionsNumberStatusEffect("erosion") {{
            speedMultiplier = 0.85f;
            reloadMultiplier = 0.85f;
            healthMultiplier = 0.85f;
            damageResult = u -> 1.5f * u.vel.len() / Vars.tilesize;

            opposite(tarred);
        }};
        fearless = new ConditionsNumberStatusEffect("fearless") {{
            speedMultiplier = 2f;

            truthDamage = true;
            damageResult = u -> u.maxHealth * 0.03f * u.healthMultiplier / 60f;
        }};


        StatusEffect.TransitionHandler none = (unit, result, time) -> {
        };
        fireKiller = new StatusEffect("fire-killer") {{
            affinity(gasify, none);
            affinity(burning, none);
            affinity(sublimation, none);
        }};
        trans(gasify, fireKiller, (unit, result, time) -> result.effect = fireKiller);
        trans(burning, fireKiller, (unit, result, time) -> result.effect = fireKiller);
        trans(sublimation, fireKiller, (unit, result, time) -> result.effect = fireKiller);


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
        burnings.addAll(burning, gasify);

        load_peculiarity();
        load_override();
    }

    public static void load_peculiarity() {
//        peculiarity_health("__h1", 1.05f, opposites(well5, modname("_h1")));
//        peculiarity_health("__h2", 1.1f, opposites(well3, modname("_h2")));
//        peculiarity_health("__h3", 1.15f, opposites(well, modname("_h3")));
//        peculiarity_damage("__d1", 1.05f, opposites(well5, modname("_d1")));
//        peculiarity_damage("__d2", 1.1f, opposites(well3, modname("_d2")));
//        peculiarity_damage("__d3", 1.15f, opposites(well, modname("_d3")));
//        peculiarity_reload("__r1", 1.05f, opposites(well5, modname("_r1")));
//        peculiarity_reload("__r2", 1.1f, opposites(well3, modname("_r2")));
//        peculiarity_reload("__r3", 1.15f, opposites(well, modname("_r3")));
//        peculiarity_health("___grow", 2f, well);
//        peculiarity_reload("___oil", 1.7f, well);
//        peculiarity_reload("___slip", 2.5f, s -> {
//            well.get(s);
//            s.dragMultiplier = 0.15f;
//        });
//        peculiarity("__r__h__d", 1.05f, 1f, 1.05f, 1.05f, well);
//
//        peculiarity("_h__d", 1.1f, 1, 0.91f, 1, midden5);
//        peculiarity("_d__r", 0.91f, 1, 1, 1.1f, midden5);
//        peculiarity("_s__r", 1, 0.91f, 1, 1.1f, midden5);
//        peculiarity("_h__r", 1, 1, 0.91f, 1.1f, midden5);
//        peculiarity("_r__s", 1, 1.1f, 1, 0.91f, midden5);
//        peculiarity("_h_r__d", 1.1f, 1, 0.96f, 0.96f, midden3);
//        peculiarity("_h_s__d", 1.1f, 0.96f, 1, 0.96f, midden3);
//        peculiarity("___glass", 2f, 1, 0.5f, 1f, midden);
//        peculiarity("___loading", 1f, 1 / 1.5f, 1 / 1.5f, 3f, midden);
//        peculiarity("___X", 0.5f, 1, 1, 2, midden);
//        peculiarity("___armour", 1, 0.5f, 2, 1, midden);
//        peculiarity("___stone", 1, 1, 1.5f, 1 / 1.5f, midden);
//        peculiarity("___hill", 1, 1, 2.5f, 0.4f, midden);
//
//        peculiarity_health("_h1", 0.96f, opposites(bad5, modname("__h1")));
//        peculiarity_health("_h2", 0.91f, opposites(bad3, modname("__h2")));
//        peculiarity_health("_h3", 0.86f, opposites(bad, modname("__h3")));
//        peculiarity_damage("_d1", 0.96f, opposites(bad5, modname("__d1")));
//        peculiarity_damage("_d2", 0.91f, opposites(bad3, modname("__d2")));
//        peculiarity_damage("_d3", 0.86f, opposites(bad, modname("__d3")));
//        peculiarity_reload("_r1", 0.96f, opposites(bad5, modname("__r1")));
//        peculiarity_reload("_r2", 0.91f, opposites(bad3, modname("__r2")));
//        peculiarity_reload("_r3", 0.86f, opposites(bad, modname("__r3")));
//        peculiarity_health("___incomplete", 0.5f, bad);
//        peculiarity_reload("___lock", 0.5f, bad);
        peculiarity_health5(1.02f, well);
        peculiarity_health3(1.06f, well);
        peculiarity_health(1.1f, well);
        peculiarity_damage5(1.02f, well);
        peculiarity_damage3(1.06f, well);
        peculiarity_damage(1.1f, well);
        peculiarity_reload5(1.02f, well);
        peculiarity_reload3(1.06f, well);
        peculiarity_reload(1.1f, well);
        peculiarity_health(1.2f, well);
        peculiarity_reload(1.2f, well);
        peculiarity(1.03f, 1f, 1.03f, 1.05f, well);

        peculiarity5(1.03f, 1, 0.98f, 1, mid);
        peculiarity5(0.98f, 1, 1, 1.03f, mid);
        peculiarity5(1, 0.98f, 1, 1.03f, mid);
        peculiarity5(1, 1, 0.98f, 1.03f, mid);
        peculiarity5(1, 1.03f, 1, 0.98f, mid);
        peculiarity3(1.05f, 1, 0.98f, 0.98f, mid);
        peculiarity3(1.03f, 0.98f, 1, 0.98f, mid);
        peculiarity(1.2f, 0.84f, 1, 1f, mid);
        peculiarity(1f, 1 / 1.1f, 1 / 1.1f, 1.2f, mid);
        peculiarity(0.84f, 1, 1.2f, 1, mid);
        peculiarity(1, 1.2f, 1, 0.84f, mid);
        peculiarity(1, 1.2f, 0.84f, 1, mid);
        peculiarity(1, 1.3f, 0.77f, 1, mid);
        peculiarity(0.9f, 1f, 1.3f, 1, -5, mid);
        peculiarity(1, 1.3f, 1, 1, -10, mid);

        peculiarity_health5(0.98f, bad);
        peculiarity_health3(0.943f, bad);
        peculiarity_health(0.9f, bad);
        peculiarity_damage5(0.98f, bad);
        peculiarity_damage3(0.943f, bad);
        peculiarity_damage(0.9f, bad);
        peculiarity_reload5(0.98f, bad);
        peculiarity_reload3(0.953f, bad);
        peculiarity_reload(0.9f, bad);
        peculiarity_health(0.83f, bad);
        peculiarity_reload(0.83f, bad);

        super_damage("_sd", 1.5f, 1f, 1 / 6f, wave30t50t20l4, wave20l14, e -> {
            e.effectChance = 0.1f;
            e.effect = new ParticleEffect() {{
                lifetime = 30;
                line = true;
                lenFrom = 3;
                lenTo = 1;
                strokeFrom = 0.8f;
                colorFrom = colorTo = Color.valueOf("EE7777").mul(Pal.accent);
            }};
        });
        super_heal("_sh", 1.5f, 1f, 1 / 6f, wave30t50t20l4, wave20l14, e -> {
            e.effectChance = 0.1f;
            e.effect = new ParticleEffect() {{
                lifetime = 30;
                line = true;
                lenFrom = 4;
                lenTo = 1;
                strokeFrom = 0.8f;
                colorFrom = colorTo = Color.valueOf("77EE77").mul(Pal.accent);
            }};
        });
        super_reload("_sr", 1.5f, 1f, 1 / 6f, wave30t50t20l4, wave20l14, e -> {
            e.effectChance = 0.1f;
            e.effect = new ParticleEffect() {{
                lifetime = 30;
                line = true;
                lenFrom = 4;
                lenTo = 1;
                strokeFrom = 0.8f;
                colorFrom = colorTo = Pal.techBlue.cpy();
            }};
        });
        super_peculiarity("_sdr", 1.25f, 1f, 1, 1.25f, 1 / 6f, wave30t80t20l2, wave20l14, zero, wave30t80t20l2, e -> {
            e.effectChance = 0.1f;
            e.effect = new ParticleEffect() {{
                lifetime = 30;
                line = true;
                lenFrom = 4;
                lenTo = 1;
                strokeFrom = 0.8f;
                colorFrom = colorTo = Pal.redLight.cpy().mul(Pal.techBlue);
            }};
        });
    }

    public static void load_override() {
        wet.affinities.add(sporeSlowed);
        sporeSlowed.affinities.add(wet);
        trans(wet, sporeSlowed, (unit, result, time) -> {
            if (result.time + time > 300) {
                unit.apply(erosion, 300);
                result.time -= 300 - time;
            }
            result.effect = sporeSlowed;
        });
        trans(sporeSlowed, wet, (unit, result, time) -> {
            if (result.time + time > 300) {
                unit.apply(erosion, 300);
                result.time -= 300 - time;
            }
        });

        trans(burning, frenzy, (unit, result, time) -> {
            if (result.time > time) {
                unit.apply(aging, time);
            } else {
                result.effect = frenzy;
                unit.apply(aging, result.time);
                result.time = time - result.time;
            }
        });
        trans(frenzy, burning, (unit, result, time) -> {
            if (result.time > time) {
                unit.apply(aging, time);
            } else {
                result.effect = burning;
                unit.apply(aging, result.time);
                result.time = time - result.time;
            }
        });
    }

    public static void trans(StatusEffect base, StatusEffect face, StatusEffect.TransitionHandler handler) {
        try {
            Field field = StatusEffect.class.getDeclaredField("transitions");
            field.setAccessible(true);
            //noinspection unchecked
            ObjectMap<StatusEffect, StatusEffect.TransitionHandler> transitions = (ObjectMap<StatusEffect, StatusEffect.TransitionHandler>) field.get(base);
            transitions.put(face, handler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void peculiarity(float d, float h, float r, float s, Seq<StatusPack> packs) {
        packs.add(new StatusPack(e -> {
            e.damageMultiplier *= d;
            e.healthMultiplier *= h;
            e.reloadMultiplier *= r;
            e.speedMultiplier *= s;
        }));
    }

    public static void peculiarity3(float d, float h, float r, float s, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> {
            e.damageMultiplier *= d;
            e.healthMultiplier *= h;
            e.reloadMultiplier *= r;
            e.speedMultiplier *= s;
        });
        packs.add(p, p, p);
    }

    public static void peculiarity5(float d, float h, float r, float s, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> {
            e.damageMultiplier *= d;
            e.healthMultiplier *= h;
            e.reloadMultiplier *= r;
            e.speedMultiplier *= s;
        });
        packs.addAll(p, p, p, p, p);
    }

    public static void peculiarity(float d, float h, float r, float s, float a, Seq<StatusPack> packs) {
        packs.add(new StatusPack(e -> {
            e.damageMultiplier *= d;
            e.healthMultiplier *= h;
            e.reloadMultiplier *= r;
            e.speedMultiplier *= s;
            e.armorOverride += a;
        }));
    }

    public static void peculiarity3(float d, float h, float r, float s, float a, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> {
            e.damageMultiplier *= d;
            e.healthMultiplier *= h;
            e.reloadMultiplier *= r;
            e.speedMultiplier *= s;
            e.armorOverride += a;
        });
        packs.add(p, p, p);
    }

    public static void peculiarity5(float d, float h, float r, float s, float a, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> {
            e.damageMultiplier *= d;
            e.healthMultiplier *= h;
            e.reloadMultiplier *= r;
            e.speedMultiplier *= s;
            e.armorOverride += a;
        });
        packs.addAll(p, p, p, p, p);
    }

    public static void peculiarity_heal(String name, float heal, float a, Seq<StatusPack> packs) {
        packs.add(new StatusPack(
                new StatusEffect(name) {{
                    show = false;
                    damage = -heal;
                    permanent = true;
                }}
                , e -> e.armorOverride += a
        ));
    }

    public static void peculiarity_heal3(String name, float heal, float a, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(
                new StatusEffect(name) {{
                    show = false;
                    damage = -heal;
                    permanent = true;
                }}
                , e -> e.armorOverride += a
        );
        packs.add(p, p, p);
    }

    public static void peculiarity_heal5(String name, float heal, float a, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(
                new StatusEffect(name) {{
                    show = false;
                    damage = -heal;
                    permanent = true;
                }}
                , e -> e.armorOverride += a
        );
        packs.addAll(p, p, p, p, p);
    }

    public static void peculiarity_damage(float mul, Seq<StatusPack> packs) {
        packs.add(new StatusPack(e -> e.damageMultiplier *= mul));
    }

    public static void peculiarity_damage3(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.damageMultiplier *= mul);
        packs.add(p, p, p);
    }

    public static void peculiarity_damage5(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.damageMultiplier *= mul);
        packs.addAll(p, p, p, p, p);
    }

    public static void peculiarity_health(float mul, Seq<StatusPack> packs) {
        packs.add(new StatusPack(e -> e.healthMultiplier *= mul));
    }

    public static void peculiarity_health3(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.healthMultiplier *= mul);
        packs.add(p, p, p);
    }

    public static void peculiarity_health5(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.healthMultiplier *= mul);
        packs.addAll(p, p, p, p, p);
    }

    public static void peculiarity_reload(float mul, Seq<StatusPack> packs) {
        packs.add(new StatusPack(e -> e.reloadMultiplier *= mul));
    }

    public static void peculiarity_reload3(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.reloadMultiplier *= mul);
        packs.add(p, p, p);
    }

    public static void peculiarity_reload5(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.reloadMultiplier *= mul);
        packs.addAll(p, p, p, p, p);
    }

    public static void peculiarity_speed(float mul, Seq<StatusPack> packs) {
        packs.add(new StatusPack(e -> e.speedMultiplier *= mul));
    }

    public static void peculiarity_speed3(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.speedMultiplier *= mul);
        packs.add(p, p, p);
    }

    public static void peculiarity_speed5(float mul, Seq<StatusPack> packs) {
        StatusPack p = new StatusPack(e -> e.speedMultiplier *= mul);
        packs.addAll(p, p, p, p, p);
    }

    public static void super_damage(String name, float damageMul, float speedMul, float heal, Floatp damageAdd, Floatp speedAdd, Cons<StatusEffect> change) {
        super_peculiarity(name, damageMul, speedMul, 1, 1, heal, damageAdd, speedAdd, zero, zero, change);
    }

    public static void super_heal(String name, float healMul, float speedMul, float heal, Floatp healAdd, Floatp speedAdd, Cons<StatusEffect> change) {
        super_peculiarity(name, 1, speedMul, healMul, 1, heal, zero, speedAdd, healAdd, zero, change);
    }

    public static void super_reload(String name, float reloadMul, float speedMul, float heal, Floatp reloadAdd, Floatp speedAdd, Cons<StatusEffect> change) {
        super_peculiarity(name, 1, speedMul, 1, reloadMul, heal, zero, speedAdd, zero, reloadAdd, change);
    }

    public static void super_peculiarity(String name, float damageMul, float speedMul, float healMul, float reloadMul, float heal, Floatp damageAdd, Floatp speedAdd, Floatp healAdd, Floatp reloadAdd, Cons<StatusEffect> change) {
        StatusEffect s = new SupperStatus(name) {{
            show = false;
            damage = -heal;
            permanent = true;
            baseDamageMultiplier = damageMul;
            baseReloadMultiplier = reloadMul;
            baseHealthMultiplier = healMul;
            baseSpeedMultiplier = speedMul;
            waveDamageAdder = damageAdd;
            waveReloadAdder = reloadAdd;
            waveHealthAdder = healAdd;
            waveSpeedAdder = speedAdd;
        }};
        sup.add(new StatusPack(s));
        change.get(s);
    }

//    public static void super_damage(String name, float damageMul, float speedMul, float heal, Floatp damageAdd, Floatp speedAdd, Cons<StatusEffect> change) {
//        super_peculiarity(name, damageMul, speedMul, 1, 1, heal, damageAdd, speedAdd, zero, zero, change);
//    }
//
//    public static void super_heal(String name, float healMul, float speedMul, float heal, Floatp healAdd, Floatp speedAdd, Cons<StatusEffect> change) {
//        super_peculiarity(name, 1, speedMul, healMul, 1, heal, zero, speedAdd, healAdd, zero, change);
//    }
//
//    public static void super_reload(String name, float reloadMul, float speedMul, float heal, Floatp reloadAdd, Floatp speedAdd, Cons<StatusEffect> change) {
//        super_peculiarity(name, 1, speedMul, 1, reloadMul, heal, zero, speedAdd, zero, reloadAdd, change);
//    }
//
//    public static void super_peculiarity(String name, float damageMul, float speedMul, float healMul, float reloadMul, float heal, Floatp damageAdd, Floatp speedAdd, Floatp healAdd, Floatp reloadAdd, Cons<StatusEffect> change) {
//        StatusEffect status = new SupperStatus(name) {{
//            show = false;
//            damage = -heal;
//            permanent = true;
//            baseDamageMultiplier = damageMul;
//            baseReloadMultiplier = reloadMul;
//            baseHealthMultiplier = healMul;
//            baseSpeedMultiplier = speedMul;
//            waveDamageAdder = damageAdd;
//            waveReloadAdder = reloadAdd;
//            waveHealthAdder = healAdd;
//            waveSpeedAdder = speedAdd;
//        }};
//        superPeculiarity.add(status);
//        change.get(status);
//    }
//    public static String modname(String base) {
//        return Vars.content.transformName(base);
//    }
//
//    public static Cons<StatusEffect> opposites(Cons<StatusEffect> change, String other) {
//        return s -> {
//            change.get(s);
//            opposites.put(s.name, other);
//        };
//    }
//    public static void peculiarity_health(String name, float mul, Cons<StatusEffect> change) {
//        peculiarity(name, 1, 1, mul, 1, change);
//    }
//
//    public static void peculiarity_damage(String name, float mul, Cons<StatusEffect> change) {
//        peculiarity(name, mul, 1, 1, 1, change);
//    }
//
//    public static void peculiarity_reload(String name, float mul, Cons<StatusEffect> change) {
//        peculiarity(name, 1, 1, 1, mul, change);
//    }
//
//    public static void peculiarity(String name, float damageMul, float speedMul, float healMul, float reloadMul, Cons<StatusEffect> change) {
//        StatusEffect result = new StatusEffect(name) {{
//            show = false;
//            permanent = true;
//            damageMultiplier = damageMul;
//            speedMultiplier = speedMul;
//            healthMultiplier = healMul;
//            reloadMultiplier = reloadMul;
//        }};
//        change.get(result);
//    }
}