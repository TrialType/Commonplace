package Commonplace.Loader.Special;

import Commonplace.Entities.Ability.TimeGrowDamageAbility;
import Commonplace.Loader.ProjectContent.Bullets;
import Commonplace.Loader.ProjectContent.Weapons;
import Commonplace.Utils.Classes.Camp;
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
import mindustry.entities.units.StatusEntry;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.world.blocks.defense.turrets.ItemTurret;

import java.lang.reflect.Field;

import static mindustry.Vars.*;

public class Events {
    private static boolean setSeed = true;
    private static final Rand r = new Rand();

    public static int p_num;
    public static float p_heal;
    public static float p_well;
    public static float p_midden;
    public static float p_supper;

    static {
        p_num = Core.settings.getInt("commonplace-p-num", 5);
        p_well = Core.settings.getFloat("commonplace-p-well", 0.5f);
        p_heal = Core.settings.getFloat("commonplace-p-heal", 0.5f);
        p_midden = Core.settings.getFloat("commonplace-p-midden", 0.2f);
        p_supper = Core.settings.getFloat("commonplace-p-supper", 0.001f);
    }

    public static void load() {
        arc.Events.run(EventType.Trigger.update, () -> {
            Camp.updateEach();
            TimeGrowDamageAbility.damages.clear();
        });

        arc.Events.on(EventType.WorldLoadEvent.class, e -> {
            setSeed = true;
            Listener.inited = false;
        });

        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, Vars2::load));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, UnitPeculiarity::init));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, () -> Vars.ui.research = new MoreResearchDialog()));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, ProjectDialog::create));
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
        });

        arc.Events.on(EventType.WaveEvent.class, e -> setSeed = true);
        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (!Vars2.useRandom || e.unit instanceof OwnCreate o && o.created()) {
                return;
            }

            if (e.unit.team != Vars.player.team()) {
                UnitPeculiarity.applyHeal(e.unit, 0.7f);
                if (state.isCampaign()) {
                    float threat = state.getSector().threat;
                    if (threat < 0.2f) {
                        PeculiarChance(3, 0.001f, 0.65f, 0.45f, e.unit);
                    } else if (threat < 0.4f) {
                        PeculiarChance(5, 0.002f, 0.6f, 0.4f, e.unit);
                    } else if (threat < 0.6f) {
                        PeculiarChance(8, 0.004f, 0.54f, 0.34f, e.unit);
                    } else if (threat < 0.8f) {
                        PeculiarChance(12, 0.008f, 0.47f, 0.27f, e.unit);
                    } else if (threat < 1f) {
                        PeculiarChance(16, 0.016f, 0.39f, 0.19f, e.unit);
                    } else {
                        PeculiarChance(20, 0.032f, 0.3f, 0.1f, e.unit);
                    }
                } else {
                    int cores = e.unit.team.cores().size;
                    if (cores == 0) {
                        PeculiarChance(5, 0.002f, 0.6f, 0.4f, e.unit);
                    } else if (cores < 2) {
                        PeculiarChance(20, 0.032f, 0.1f, 0.05f, e.unit);
                    } else if (cores < 4) {
                        PeculiarChance(16, 0.016f, 0.2f, 0.1f, e.unit);
                    } else if (cores < 6) {
                        PeculiarChance(12, 0.008f, 0.4f, 0.2f, e.unit);
                    } else {
                        PeculiarChance(8, 0.004f, 0.5f, 0.3f, e.unit);
                    }
                }
            } else {
                UnitPeculiarity.applyHeal(e.unit, p_heal);
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
                        long seed = mapSeed() + wave * 975L;
                        r.setSeed(seed);
                        UnitPeculiarity.setSeed(seed);
                    }

                    int extra = (isBoss ? Math.max(1, wave / 20) : Math.max(1, wave / 50));
                    float chance = 0.001f * wave / 5 * (isBoss ? 2 : 1);

                    if (wave < 8) {
                        UnitPeculiarity.applyHealSeed(e.unit, 0.25f);
                        UnitPeculiarity.applySeed(e.unit, extra, 2, r.nextInt(3));
                    } else if (wave < 18) {
                        UnitPeculiarity.applyHealSeed(e.unit, 0.25f);
                        UnitPeculiarity.applySeed(e.unit, r.nextInt(2) + extra, 2, r.nextInt(3));
                    } else if (wave < 30) {
                        UnitPeculiarity.applyHealSeed(e.unit, 0.25f);
                        UnitPeculiarity.applySeed(e.unit, 1 + r.nextInt(2) + extra, 2, r.nextInt(3));
                    } else {
                        if (r.chance(chance)) {
                            UnitPeculiarity.applySuperSeed(e.unit, 1);
                        }

                        if (wave < 42) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.25f);
                            UnitPeculiarity.applySeed(e.unit, 1 + r.nextInt(3) + extra, 3, r.nextInt(3));
                        } else if (wave < 55) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.5f);
                            UnitPeculiarity.applySeed(e.unit, 1 + r.nextInt(3) + extra, 3, r.nextInt(2));
                        } else if (wave < 70) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.5f);
                            UnitPeculiarity.applySeed(e.unit, 2 + r.nextInt(3) + extra, 3, r.nextInt(2));
                        } else if (wave <= 85) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.5f);
                            UnitPeculiarity.applySeed(e.unit, 2 + r.nextInt(4) + extra, 2, r.nextInt(2));
                        } else if (wave <= 100) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.5f);
                            UnitPeculiarity.applySeed(e.unit, 3 + r.nextInt(4) + extra, 2, r.nextInt(2));
                        } else if (wave <= 115) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.75f);
                            UnitPeculiarity.applySeed(e.unit, 3 + r.nextInt(5) + extra, 1, r.nextInt(2));
                        } else if (wave <= 130) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.75f);
                            UnitPeculiarity.applySeed(e.unit, 4 + r.nextInt(5) + extra, 1, r.nextInt(2));
                        } else if (wave <= 200) {
                            UnitPeculiarity.applyHealSeed(e.unit, 0.75f);
                            UnitPeculiarity.applySeed(e.unit, 6 + r.nextInt((wave - 130) / 10) + extra, 0, 0);
                        } else if (wave <= 300) {
                            extra = 15 + (isBoss ? (extra - 15) / 2 : (extra - 15) / 3);
                            e.unit.shield(e.unit.shield + 30 * (wave - 200));
                            UnitPeculiarity.applyHealSeed(e.unit, 0.75f);
                            UnitPeculiarity.applySeed(e.unit, r.nextInt(19) + extra, 0, 0);
                        } else {
                            extra = Math.min(25 + (isBoss ? (extra - 25) / 3 : (extra - 15) / 4), 80);
                            e.unit.shield(e.unit.shield + 3000 + 40 * (wave - 300));
                            UnitPeculiarity.applyHealSeed(e.unit, 0.9f);
                            UnitPeculiarity.applySeed(e.unit, r.nextInt(26) + extra, 0, 0);
                        }
                    }
                }
            }
        });

        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (Vars2.useRandom && e.bullet.owner instanceof Unit u && !UnitPeculiarity.blackList.contains(u.type)) {
                int[] count = {0, 0};
                StatusEntry se = u.applyDynamicStatus();
                float fin = Math.max(1, e.unit.maxHealth / u.maxHealth);
                float h = se.healthMultiplier, d = se.damageMultiplier, s = se.damageMultiplier, r = se.reloadMultiplier;
                UnitPeculiarity.sup.each(p -> {
                    if (p.effect != null && u.hasEffect(p.effect)) {
                        count[0]++;
                    }
                });
                UnitPeculiarity.heal.each(p -> {
                    if (u.hasEffect(p)) {
                        count[1]++;
                    }
                });
                if (count[1] < 1) {
                    UnitPeculiarity.applyHeal(u, 0.5f);
                }
                UnitPeculiarity.sup.each(p -> {
                    if (count[0] < 2 && p.effect != null && e.unit.hasEffect(p.effect)) {
                        if (Mathf.chance(0.1f)) {
                            count[0]++;
                            u.apply(p.effect);
                        } else {
                            if (Mathf.chance(fin * (1 - d / 2.5f))) {
                                se.damageMultiplier += Mathf.random(0.2f);
                            }
                            if (Mathf.chance(fin * (1 - h / 2.5f))) {
                                se.healthMultiplier += Mathf.random(0.2f);
                            }
                            if (Mathf.chance(fin * (1 - r / 2.5f))) {
                                se.reloadMultiplier += Mathf.random(0.2f);
                            }
                            if (Mathf.chance(fin * (1 - s / 2.5f))) {
                                se.speedMultiplier += Mathf.random(0.2f);
                            }
                        }
                    }
                });
                if (Mathf.chance(fin * (1 - d / 1.5f))) {
                    se.damageMultiplier += Mathf.random(0.1f);
                }
                if (Mathf.chance(fin * (1 - h / 1.5f))) {
                    se.healthMultiplier += Mathf.random(0.1f);
                }
                if (Mathf.chance(fin * (1 - r / 1.5f))) {
                    se.reloadMultiplier += Mathf.random(0.1f);
                }
                if (Mathf.chance(fin * (1 - s / 1.5f))) {
                    se.speedMultiplier += Mathf.random(0.1f);
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

    public static long mapSeed() {
        if (!state.isGame()) {
            return System.currentTimeMillis();
        } else {
            long result = 0;
            String name = state.map.name();
            for (int i = 0; i < name.length(); i++) {
                result += name.charAt(i);
                result *= 13L;
            }
            return result;
        }
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