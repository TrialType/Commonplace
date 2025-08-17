package Commonplace.Loader.Special;

import Commonplace.Entities.Ability.TimeGrowDamageAbility;
import Commonplace.IO.AllGameStats;
import Commonplace.IO.Save8Override;
import Commonplace.Loader.ProjectContent.Bullets;
import Commonplace.Loader.ProjectContent.Weapons;
import Commonplace.Utils.Classes.InputListener;
import Commonplace.Utils.Classes.Listener;
import Commonplace.Utils.Classes.UnitPeculiarity;
import Commonplace.Utils.Classes.Vars2;
import Commonplace.Utils.Interfaces.BuildUpGrade;
import Commonplace.UI.Dialogs.ProjectDialog;
import Commonplace.UI.Dialogs.MoreResearchDialog;
import Commonplace.Utils.Interfaces.OwnCreate;
import arc.Core;
import arc.func.Floatf;
import arc.math.Mathf;
import arc.math.Rand;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.entities.units.StatusEntry;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.io.SaveIO;
import mindustry.world.blocks.payloads.PayloadSource;

import static mindustry.Vars.*;

public class Events {
    private static long mapSeed;
    private static final Rand r = new Rand();

    public static int p_num;
    public static float p_well;
    public static float p_midden;
    public static float p_supper;

    public static int wave, baseP, baseB, rw, nm, rb;
    public static float supP, supB, shield;

    static {
        p_num = Core.settings.getInt("commonplace-p-num", 2);
        p_well = Core.settings.getFloat("commonplace-p-well", 0.5f);
        p_midden = Core.settings.getFloat("commonplace-p-midden", 0.2f);
        p_supper = Core.settings.getFloat("commonplace-p-supper", 0.001f);
    }

    public static void load() {
        //功能性监听
        arc.Events.run(EventType.Trigger.update, () -> {
            //Camp.updateEach();
            updateWave();
            InputListener.update();
            TimeGrowDamageAbility.damages.clear();
        });
        arc.Events.run(EventType.Trigger.draw, InputListener::draw);

        arc.Events.on(EventType.WorldLoadEvent.class, e -> {
            seed();
            wave = -1;
            updateWave();
            Listener.inited = false;
            if (!(state.stats instanceof AllGameStats)) {
                state.stats = new AllGameStats(state.stats);
            }
        });

        //覆盖监听
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, () -> {
            Vars2.load();
            ProjectDialog.create();
            UnitPeculiarity.init();
            Vars.ui.research = new MoreResearchDialog();
            SaveIO.versions.put(8, new Save8Override());
        }));
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
            UnitTypes.stell.weapons.first().bullet.pierce = false;
        });

        //功能性监听
        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            AllGameStats.LowGameStats stats = ((AllGameStats) state.stats).get(e.unit.team);
            stats.unitsCreatedHealth += e.unit.maxHealth * state.rules.unitHealthMultiplier * state.rules.unitHealth(e.unit.team);

            if (!Vars.net.client() && e.unit.team != state.rules.defaultTeam && !e.unit.type.unlocked()) {
                Vars2.debugFrag.addInfo(e.unit.type);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (e.unit.team.isAI() && !e.unit.type.unlocked()) {
                Vars2.debugFrag.addInfo(e.unit.type);
            }
        });
        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof Teamc t) {
                AllGameStats.LowGameStats stats = ((AllGameStats) state.stats).get(t.team());
                stats.enemyUnitsDestroyedHealth += e.unit.maxHealth * state.rules.unitHealthMultiplier * state.rules.unitHealth(e.unit.team);
            }
        });

        //随机性监听
        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (!Vars2.useRandom || e.spawner instanceof PayloadSource.PayloadSourceBuild || (e.unit instanceof OwnCreate o && o.created())) {
                if (e.unit instanceof OwnCreate o) {
                    o.created(true);
                }
                return;
            }

            float mul = state.rules.teams.get(e.unit.team).unitBuildSpeedMultiplier;
            mul = mul == 0 ? 1 : mul;
            float extra = 1 + ((AllGameStats) state.stats).get(e.unit.team()).unitsCreatedHealth / mul / 400000f;
            if (e.unit.team != state.rules.defaultTeam) {
                float wm = (1 - mul) * 0.4f + 1, sup = mul * extra;
                if (state.isCampaign()) {
                    float threat = state.getSector().threat;
                    if (threat < 0.2f) {
                        PeculiarChance((int) (3 * mul), 0.001f * sup, 0.65f * wm, 0.45f * wm, e.unit);
                    } else if (threat < 0.4f) {
                        PeculiarChance((int) (5 * mul), 0.002f * sup, 0.6f * wm, 0.4f * wm, e.unit);
                    } else if (threat < 0.6f) {
                        PeculiarChance((int) (8 * mul), 0.004f * sup, 0.54f * wm, 0.34f * wm, e.unit);
                    } else if (threat < 0.8f) {
                        PeculiarChance((int) (12 * mul), 0.008f * sup, 0.47f * wm, 0.27f * wm, e.unit);
                    } else if (threat < 1f) {
                        PeculiarChance((int) (16 * mul), 0.016f * sup, 0.39f * wm, 0.19f * wm, e.unit);
                    } else {
                        PeculiarChance((int) (20 * mul), 0.032f * sup, 0.3f * wm, 0.1f * wm, e.unit);
                    }
                } else {
                    int cores = e.unit.team.cores().size;
                    if (cores == 0) {
                        PeculiarChance((int) (5 * mul), 0.002f * sup, 0.6f * wm, 0.4f * wm, e.unit);
                    } else if (cores < 2) {
                        PeculiarChance((int) (20 * mul), 0.032f * sup, 0.1f * wm, 0.05f * wm, e.unit);
                    } else if (cores < 4) {
                        PeculiarChance((int) (16 * mul), 0.016f * sup, 0.2f * wm, 0.1f * wm, e.unit);
                    } else if (cores < 6) {
                        PeculiarChance((int) (12 * mul), 0.008f * sup, 0.4f * wm, 0.2f * wm, e.unit);
                    } else {
                        PeculiarChance((int) (8 * mul), 0.004f * sup, 0.5f * wm, 0.3f * wm, e.unit);
                    }
                }
            } else {
                PeculiarChance(p_num, p_supper * extra, p_well, p_midden, e.unit);
            }

            if (e.unit instanceof OwnCreate o) {
                o.created(true);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (Vars2.useRandom && spawner.isSpawning()) {
                boolean isBoss = e.unit.isBoss();

                if (r.chance(isBoss ? supB : supP)) {
                    UnitPeculiarity.applySuperSeed(e.unit, 1);
                }
                e.unit.shield(e.unit.shield + shield);
                UnitPeculiarity.applySeed(e.unit, r.random(rw) + (isBoss ? baseB : baseP), nm, r.random(rb));
            }
        });

        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (Vars2.useRandom && !(e.unit instanceof TimedKillUnit) && e.bullet.shooter instanceof Unit u && !UnitPeculiarity.blackList.contains(u.type)) {
                StatusEntry se = u.applyDynamicStatus();

                int[] count = {UnitPeculiarity.sup.count(p -> p.effect != null && u.hasEffect(p.effect))};

                float h = se.healthMultiplier, d = se.damageMultiplier, s = se.damageMultiplier, r = se.reloadMultiplier;
                float extra = 1 + ((AllGameStats) state.stats).get(u.team).enemyUnitsDestroyedHealth / 400000f;
                float fin = Math.max(1, e.unit.maxHealth / u.maxHealth) * extra;
                float sup = 1.3f * extra, def = 1.1f * extra;

                UnitPeculiarity.sup.each(p -> {
                    if (p.effect != null && e.unit.hasEffect(p.effect)) {
                        if (count[0] < 2 && !u.hasEffect(p.effect) && Mathf.chance(0.1f * fin)) {
                            count[0]++;
                            u.apply(p.effect);
                        } else {
                            if (d < sup) {
                                se.damageMultiplier += 0.008f * Math.max(10 * (sup - d) / sup, 1);
                            }
                            if (h < sup) {
                                se.healthMultiplier += 0.008f * Math.max(10 * (sup - h) / sup, 1);
                            }
                            if (r < sup) {
                                se.reloadMultiplier += 0.008f * Math.max(10 * (sup - r) / sup, 1);
                            }
                            if (s < sup) {
                                se.speedMultiplier += 0.008f * Math.max(10 * (sup - s) / sup, 1);
                            }
                        }
                    }
                });

                if (d < def) {
                    se.damageMultiplier += 0.005f * Math.max(10 * (def - d) / def, 1);
                }
                if (h < def) {
                    se.healthMultiplier += 0.005f * Math.max(10 * (def - h) / def, 1);
                }
                if (r < def) {
                    se.reloadMultiplier += 0.005f * Math.max(10 * (def - r) / def, 1);
                }
                if (s < def) {
                    se.speedMultiplier += 0.005f * Math.max(10 * (def - s) / def, 1);
                }
            }
        });
        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                fug.addExp(e.unit.maxHealth * e.unit.healthMultiplier * e.unit.team.rules().blockHealthMultiplier);
            }
        });
        arc.Events.on(EventType.BuildingBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                fug.addExp(e.build.maxHealth * e.build.team.rules().blockHealthMultiplier);
            }
        });
    }

    public static void seed() {
        if (state.map == null) {
            mapSeed = System.currentTimeMillis();
        } else {
            long result = 0;
            String name = state.map.name();
            for (int i = 0; i < name.length(); i++) {
                result += name.charAt(i);
                result *= 13L;
            }
            mapSeed = result % Long.MAX_VALUE;
        }
    }

    public static void updateWave() {
        if (state.wave != wave) {
            UnitPeculiarity.updateSuper();

            if (Vars2.lockRandom) {
                long seed = mapSeed + state.wave * 975L;
                r.setSeed(seed);
                UnitPeculiarity.setSeed(seed);
            }

            float mul = state.rules.teams.get(state.rules.waveTeam).unitBuildSpeedMultiplier;
            float w = state.wave * mul;

            wave = state.wave;
            supP = 0.001f * w / 8;
            supB = 0.002f * w / 8;
            baseP = Math.round(w / 30f);
            baseB = Math.round(w / 15f);

            if (w < 6) {
                rw = 0;
                nm = 4;
                rb = 4;
                shield = 0;
            } else if (w < 12) {
                rw = 2;
                nm = 4;
                rb = 4;
                shield = 0;
            } else if (w < 18) {
                rw = 3;
                nm = 4;
                rb = 4;
                shield = 0;
            } else if (w < 24) {
                rw = 4;
                nm = 4;
                rb = 3;
                shield = 0;
            } else if (w < 30) {
                rw = 5;
                nm = 4;
                rb = 3;
                shield = 0;
            } else if (w < 38) {
                rw = 6;
                nm = 4;
                rb = 3;
                shield = 0;
            } else if (w <= 46) {
                rw = 7;
                nm = 4;
                rb = 2;
                shield = 0;
            } else if (w <= 54) {
                rw = 8;
                nm = 4;
                rb = 2;
                shield = 0;
            } else if (w <= 62) {
                rw = 9;
                nm = 3;
                rb = 2;
                shield = 0;
            } else if (w <= 70) {
                rw = 10;
                nm = 3;
                rb = 2;
                shield = 0;
            } else if (w <= 80) {
                rw = 11;
                nm = 3;
                rb = 0;
                shield = 0;
            } else if (w <= 90) {
                rw = 12;
                nm = 3;
                rb = 0;
                shield = 0;
            } else if (w <= 100) {
                rw = 13;
                nm = 3;
                rb = 0;
                shield = 0;
            } else if (w <= 110) {
                rw = 14;
                nm = 3;
                rb = 0;
                shield = 0;
            } else if (w <= 120) {
                rw = 15;
                nm = 2;
                rb = 0;
                shield = 0;
            } else if (w <= 130) {
                rw = 16;
                nm = 2;
                rb = 0;
                shield = 0;
            } else if (w <= 140) {
                rw = 17;
                nm = 2;
                rb = 0;
                shield = 0;
            } else if (w <= 150) {
                rw = 18;
                nm = 2;
                rb = 0;
                shield = 0;
            } else if (w <= 160) {
                rw = 19;
                nm = 1;
                rb = 0;
                shield = 0;
            } else if (w <= 170) {
                rw = 20;
                nm = 1;
                rb = 0;
                shield = 0;
            } else if (w <= 180) {
                rw = 21;
                nm = 1;
                rb = 0;
                shield = 0;
            } else if (w <= 190) {
                rw = 22;
                nm = 1;
                rb = 0;
                shield = 0;
            } else if (w <= 200) {
                rw = 23;
                nm = 1;
                rb = 0;
                shield = 0;
            } else if (w <= 300) {
                rw = 24;
                nm = 1;
                rb = 0;
                shield = (w - 200) * 50;
            } else if (w <= 400) {
                rw = 30;
                nm = 0;
                rb = 0;
                shield = 5000 + (w - 300) * 100;
            } else if (w <= 500) {
                rw = 40;
                nm = 0;
                rb = 0;
                shield = 15000 + (w - 400) * 200;
            } else {
                rw = 0;
                nm = 50;
                rb = 0;
                shield = 50000;
            }
        }
    }

    public static void PeculiarChance(int num, float supper, float well, float midden, Unit unit) {
        if (Mathf.chance(supper)) {
            UnitPeculiarity.applySuper(unit, 1);
        }
        UnitPeculiarity.apply(unit, num - 1, well, midden);
    }
}