package Commonplace.FContent.DefaultContent;

import Commonplace.FType.FStatusEffect.WithMoreStatus;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;

public class FStatusEffects {
    public final static Seq<StatusEffect> burnings = new Seq<>();
    public static StatusEffect StrongStop, boostSpeed, HardHit, onePercent,
            torn, suppress, tardy, swift, tension, abyss, gasify, sublimation,
            grow, seethe, friability, back, frenzy, deploy, impatience;

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
        StatusEffects.burning.transitionDamage = 54;
        StatusEffects.burning.damage = 1.2f;
        gasify = new StatusEffect("gasify") {{
            damage = 5.6f;
            transitionDamage = 480;
            effect = Fx.burning;

            init(() -> {
                opposite(StatusEffects.wet, StatusEffects.freezing);
                affinity(StatusEffects.tarred, (unit, result, time) -> {
                    unit.damagePierce(transitionDamage);
                    Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                    result.set(gasify, Math.min(time + result.time, 300f));
                });
            });
        }};
        sublimation = new StatusEffect("sublimation") {{
            damage = 8f;
            transitionDamage = 650;
            effect = Fx.burning;
        }};
        grow = new StatusEffect("grow") {{
            healthMultiplier = 20;
            damage = -4;
        }};
        seethe = new StatusEffect("seethe") {{
            damageMultiplier = 2.7f;
            speedMultiplier = 1.9f;
            reloadMultiplier = 0.33f;
            healthMultiplier = 0.56f;
            damage = 4f;
        }};
        friability = new StatusEffect("friability") {{
            healthMultiplier = 0.001f;
            damageMultiplier = 25;
            speedMultiplier = 4.2f;
        }};
        back = new StatusEffect("back") {{
            healthMultiplier = 2.4f;
            damage = -100;
        }};
        frenzy = new StatusEffect("frenzy") {{
            healthMultiplier = 2f;
            speedMultiplier = 1.75f;
            reloadMultiplier = 0.5f;
        }};
        deploy = new StatusEffect("deploy") {{
            healthMultiplier = 2f;
            damageMultiplier = 2.5f;
            reloadMultiplier = 0.6f;
            speedMultiplier = 0.1f;
        }};
        impatience = new StatusEffect("impatience") {{
            healthMultiplier = 0.7f;
            damageMultiplier = 0.6f;
            reloadMultiplier = 1.3f;
            speedMultiplier = 1.5f;
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            transitions.put(FStatusEffects.pureA, (u, s, t) -> u.unapply(this));
            transitions.put(FStatusEffects.pureT, (u, s, t) -> u.unapply(this));
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
            with.addAll(StatusEffects.wet, corrosionI);
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
        burnings.addAll(StatusEffects.burning, gasify);
    }
}