package Commonplace.Loader.Special;

import Commonplace.Entities.Ability.TimeGrowDamageAbility;
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
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.units.StatusEntry;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.blocks.payloads.PayloadSource;

import java.lang.reflect.Field;

import static mindustry.Vars.*;

public class Events {
    private static long mapSeed;
    private static boolean setSeed = true;
    private static boolean mapChange = true;
    private static final Rand r = new Rand();

    public static int p_num;
    public static float p_well;
    public static float p_midden;
    public static float p_supper;

    static {
        p_num = Core.settings.getInt("commonplace-p-num", 2);
        p_well = Core.settings.getFloat("commonplace-p-well", 0.5f);
        p_midden = Core.settings.getFloat("commonplace-p-midden", 0.2f);
        p_supper = Core.settings.getFloat("commonplace-p-supper", 0.001f);
    }

    public static void load() {
        arc.Events.run(EventType.Trigger.update, () -> {
            //Camp.updateEach();
            InputListener.update();
            TimeGrowDamageAbility.damages.clear();
        });
        arc.Events.run(EventType.Trigger.draw, InputListener::draw);

        arc.Events.on(EventType.WorldLoadEvent.class, e -> {
            setSeed = true;
            mapChange = true;
            Listener.inited = false;
        });

        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, () -> {
            Vars2.load();
            ProjectDialog.create();
            UnitPeculiarity.init();
            Vars.ui.research = new MoreResearchDialog();
        }));
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
            UnitTypes.stell.weapons.first().bullet.pierce = false;
        });

        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (e.unit.team.isAI() && !e.unit.type.unlocked()) {
                Vars2.debugFrag.addInfo(e.unit.type);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (e.unit.team.isAI() && !e.unit.type.unlocked()) {
                Vars2.debugFrag.addInfo(e.unit.type);
            }
        });

        arc.Events.on(EventType.WaveEvent.class, e -> setSeed = true);
        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (!Vars2.useRandom || e.unit instanceof OwnCreate o && o.created() || e.spawner instanceof PayloadSource.PayloadSourceBuild) {
                return;
            }

            if (e.unit.team != state.rules.defaultTeam) {
                if (state.isCampaign()) {
                    float threat = state.getSector().threat;
                    float sn = state.rules.planet.campaignRules.difficulty.enemySpawnMultiplier;
                    float wm = (1 - sn) * 0.4f + 1;
                    if (threat < 0.2f) {
                        PeculiarChance((int) (3 * sn), 0.001f * sn, 0.65f * wm, 0.45f * wm, e.unit);
                    } else if (threat < 0.4f) {
                        PeculiarChance((int) (5 * sn), 0.002f * sn, 0.6f * wm, 0.4f * wm, e.unit);
                    } else if (threat < 0.6f) {
                        PeculiarChance((int) (8 * sn), 0.004f * sn, 0.54f * wm, 0.34f * wm, e.unit);
                    } else if (threat < 0.8f) {
                        PeculiarChance((int) (12 * sn), 0.008f * sn, 0.47f * wm, 0.27f * wm, e.unit);
                    } else if (threat < 1f) {
                        PeculiarChance((int) (16 * sn), 0.016f + sn, 0.39f * wm, 0.19f * wm, e.unit);
                    } else {
                        PeculiarChance((int) (20 * sn), 0.032f * sn, 0.3f * wm, 0.1f * wm, e.unit);
                    }
                } else {
                    int cores = e.unit.team.cores().size;
                    float mul = state.rules.teams.get(e.unit.team).unitBuildSpeedMultiplier;
                    float wm = (1 - mul) * 0.4f + 1;
                    if (cores == 0) {
                        PeculiarChance((int) (5 * mul), 0.002f * mul, 0.6f * wm, 0.4f * wm, e.unit);
                    } else if (cores < 2) {
                        PeculiarChance((int) (20 * mul), 0.032f * mul, 0.1f * wm, 0.05f * wm, e.unit);
                    } else if (cores < 4) {
                        PeculiarChance((int) (16 * mul), 0.016f * mul, 0.2f * wm, 0.1f * wm, e.unit);
                    } else if (cores < 6) {
                        PeculiarChance((int) (12 * mul), 0.008f * mul, 0.4f * wm, 0.2f * wm, e.unit);
                    } else {
                        PeculiarChance((int) (8 * mul), 0.004f * mul, 0.5f * wm, 0.3f * wm, e.unit);
                    }
                }
            } else {
                PeculiarChance(p_num, p_supper, p_well, p_midden, e.unit);
            }

            if (e.unit instanceof OwnCreate o) {
                o.created(true);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            if (Vars2.useRandom) {
                if (spawner.isSpawning()) {
                    boolean isBoss = isBoss(e.unit);
                    int wave = state.wave - 1;
                    if (Vars2.lockRandom && setSeed) {
                        setSeed = false;
                        long seed = seed() + wave * 975L;
                        r.setSeed(seed);
                        UnitPeculiarity.setSeed(seed);
                    }

                    float mul;
                    if (state.isCampaign()) {
                        mul = state.rules.planet.campaignRules.difficulty.enemySpawnMultiplier;
                    } else {
                        mul = state.rules.teams.get(e.unit.team).unitBuildSpeedMultiplier;
                    }
                    int extra = Math.round((isBoss ? Math.max(1, wave / 20) : Math.max(1, wave / 50)) * mul);
                    float chance = 0.001f * wave / 5 * (isBoss ? 2 : 1) * mul;

                    if (wave < 8 / mul) {
                        UnitPeculiarity.applySeed(e.unit, extra, 2, r.random(3));
                    } else if (wave < 18 / mul) {
                        UnitPeculiarity.applySeed(e.unit, r.random(2) + extra, 2, r.random(3));
                    } else if (wave < 30 / mul) {
                        UnitPeculiarity.applySeed(e.unit, 1 + r.random(2) + extra, 2, r.random(3));
                    } else {
                        if (r.chance(chance)) {
                            UnitPeculiarity.applySuperSeed(e.unit, 1);
                        }

                        if (wave < 42 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 1 + r.random(3) + extra, 3, r.random(3));
                        } else if (wave < 55 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 1 + r.random(3) + extra, 3, r.random(2));
                        } else if (wave < 70 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 2 + r.random(3) + extra, 3, r.random(2));
                        } else if (wave <= 85 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 2 + r.random(4) + extra, 2, r.random(2));
                        } else if (wave <= 100 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 3 + r.random(4) + extra, 2, r.random(2));
                        } else if (wave <= 115 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 3 + r.random(5) + extra, 1, r.random(2));
                        } else if (wave <= 130 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 4 + r.random(5) + extra, 1, r.random(2));
                        } else if (wave <= 200 / mul) {
                            UnitPeculiarity.applySeed(e.unit, 6 + r.random((wave - 130) / 10) + extra, 0, 0);
                        } else if (wave <= 300 / mul) {
                            extra = 15 + (isBoss ? (extra - 15) / 2 : (extra - 15) / 3);
                            e.unit.shield(e.unit.shield + 30 * (wave - 200 / mul));
                            UnitPeculiarity.applySeed(e.unit, r.random(19) + extra, 0, 0);
                        } else {
                            extra = Math.min(25 + (isBoss ? (extra - 25) / 3 : (extra - 15) / 4), 80);
                            e.unit.shield(e.unit.shield + 3000 + 40 * (wave - 300 / mul));
                            UnitPeculiarity.applySeed(e.unit, r.random(26) + extra, 0, 0);
                        }
                    }
                }
            }
        });

        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (Vars2.useRandom && e.bullet.owner instanceof Unit u && !UnitPeculiarity.blackList.contains(u.type)) {
                int[] count = {0};
                StatusEntry se = u.applyDynamicStatus();
                float fin = Math.max(1, e.unit.maxHealth / u.maxHealth);
                float h = se.healthMultiplier, d = se.damageMultiplier, s = se.damageMultiplier, r = se.reloadMultiplier;
                UnitPeculiarity.sup.each(p -> {
                    if (p.effect != null && u.hasEffect(p.effect)) {
                        count[0]++;
                    }
                });
                UnitPeculiarity.sup.each(p -> {
                    if (p.effect != null && e.unit.hasEffect(p.effect)) {
                        if (count[0] < 2 && !u.hasEffect(p.effect) && Mathf.chance(0.1f * fin)) {
                            count[0]++;
                            u.apply(p.effect);
                        } else {
                            if (d < 1.6) {
                                se.damageMultiplier += Mathf.random(0.01f * fin);
                            }
                            if (h < 1.6) {
                                se.healthMultiplier += Mathf.random(0.01f * fin);
                            }
                            if (r < 1.6) {
                                se.reloadMultiplier += Mathf.random(0.01f * fin);
                            }
                            if (s < 1.6) {
                                se.speedMultiplier += Mathf.random(0.01f * fin);
                            }
                        }
                    }
                });
                if (d < 1.2) {
                    se.damageMultiplier += Mathf.random(0.00375f * fin);
                }
                if (h < 1.2) {
                    se.healthMultiplier += Mathf.random(0.00375f * fin);
                }
                if (r < 1.2) {
                    se.reloadMultiplier += Mathf.random(0.00375f * fin);
                }
                if (s < 1.2) {
                    se.speedMultiplier += Mathf.random(0.00375f * fin);
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

    public static long seed() {
        if (mapChange) {
            mapChange = false;
            if (!state.isGame()) {
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
        return mapSeed;
    }

    public static void PeculiarChance(int num, float supper, float well, float midden, Unit unit) {
        if (Mathf.chance(supper)) {
            UnitPeculiarity.applySuper(unit, 1);
        }
        UnitPeculiarity.apply(unit, num - 1, well, midden);
    }

    public static boolean isBoss(Unit u) {
        Class<? extends Unit> unit = u.getClass();
        try {
            Field field = unit.getDeclaredField("statuses");
            field.setAccessible(true);
            //noinspection unchecked
            Seq<StatusEntry> entry = (Seq<StatusEntry>) field.get(u);
            return entry.first().effect == StatusEffects.boss;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            var su = unit.getSuperclass();
            while (su != Unit.class) {
                try {
                    Field field = unit.getDeclaredField("statuses");
                    field.setAccessible(true);
                    //noinspection unchecked
                    Seq<StatusEntry> entry = (Seq<StatusEntry>) field.get(u);
                    return entry.contains(s -> s.effect == StatusEffects.boss);
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    su = su.getSuperclass();
                }
            }
        }
        return false;
    }

    public static class GetPowerEvent {
        Unit getter;
        int number;
        boolean full;

        public GetPowerEvent(Unit u, int n, boolean full) {
            getter = u;
            number = n;
            this.full = full;
        }
    }
}