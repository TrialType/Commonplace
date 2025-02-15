package Commonplace.Loader.Special;

import Commonplace.Loader.ProjectContent.Bullets;
import Commonplace.Loader.ProjectContent.Weapons;
import Commonplace.Utils.Classes.UnitPeculiarity;
import Commonplace.Utils.Interfaces.BuildUpGrade;
import Commonplace.Type.Dialogs.ProjectDialog;
import Commonplace.Type.Dialogs.MoreResearchDialog;
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
import mindustry.game.Team;
import mindustry.gen.*;

import java.lang.reflect.Field;


public class Events {
    private static final Rand r = new Rand();
    public static int p_num;
    public static int supper_opp;
    public static float p_well;
    public static float p_midden;
    public static float p_supper;

    static {
        p_num = Core.settings.getInt("commonplace-p-num", 5);
        supper_opp = Core.settings.getInt("commonplace-supper-opp", 10);
        p_well = Core.settings.getFloat("commonplace-p-well", 0.5f);
        p_midden = Core.settings.getFloat("commonplace-p-midden", 0.2f);
        p_supper = Core.settings.getFloat("commonplace-p-supper", 0.001f);
    }

    public static void load() {
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, UnitPeculiarity::init));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, () -> Vars.ui.research = new MoreResearchDialog()));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, ProjectDialog::create));
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
        });

        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (e.unit instanceof OwnCreate o && o.created()) {
                return;
            }

            if (e.unit.team != Team.sharded) {
                float threat = Vars.state.isCampaign() ? Vars.state.getSector().threat : e.unit.team.cores().size * 0.25f;
                if (threat < 0.2f) {
                    PeculiarChance(3, 10, 0.0001f, 0.65f, 0.45f, e.unit);
                } else if (threat < 0.4f) {
                    PeculiarChance(6, 10, 0.0002f, 0.6f, 0.4f, e.unit);
                } else if (threat < 0.6f) {
                    PeculiarChance(9, 10, 0.0004f, 0.54f, 0.34f, e.unit);
                } else if (threat < 0.8f) {
                    PeculiarChance(15, 10, 0.0008f, 0.47f, 0.27f, e.unit);
                } else {
                    PeculiarChance(20, 10, 0.0016f, 0.39f, 0.19f, e.unit);
                }
            } else {
                PeculiarChance(p_num, supper_opp, p_supper, p_well, p_midden, e.unit);
            }

            if (e.unit instanceof OwnCreate o) {
                o.created(true);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            boolean isBoss = isBoss(e.unit);
            int wave = Vars.state.wave - 1;
            int extra = (isBoss ? Math.max(1, wave / 20) : 0);
            float chance = 0.001f * wave / 5 * (isBoss ? 2 : 1);

            if (wave < 8) {
                UnitPeculiarity.apply(e.unit, extra, r.nextInt(3), r.nextInt(3));
            } else if (wave < 18) {
                UnitPeculiarity.apply(e.unit, r.nextInt(3) + extra, r.nextInt(3), r.nextInt(3));
            } else if (wave < 30) {
                UnitPeculiarity.apply(e.unit, r.nextInt(4) + extra, r.nextInt(3), r.nextInt(3));
            } else {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, 1);
                }

                if (wave < 42) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(5) + extra, r.nextInt(3), r.nextInt(3));
                } else if (wave < 55) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(7) + extra, r.nextInt(3), r.nextInt(2));
                } else if (wave < 70) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(8) + extra, r.nextInt(2), r.nextInt(2));
                } else if (wave <= 85) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(9) + extra, r.nextInt(2), r.nextInt(2));
                } else if (wave <= 100) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(11) + extra, r.nextInt(2), r.nextInt(2));
                } else if (wave <= 115) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(12) + extra, r.nextInt(2), r.nextInt(2));
                } else if (wave <= 130) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(13) + extra, r.nextInt(2), r.nextInt(2));
                } else if (wave <= 300) {
                    UnitPeculiarity.apply(e.unit, r.nextInt(14 + Math.min(10, (wave - 130) / 10)) + extra, 0, 0);
                } else if (wave <= 500) {
                    extra = isBoss ? 15 + (extra - 15) / 3 : 0;
                    e.unit.shield(e.unit.shield + 3000 * (wave - 300));
                    UnitPeculiarity.apply(e.unit, r.nextInt(25) + extra, 0, 0);
                } else {
                    extra = isBoss ? 20 + (extra - 20) / 7 : 0;
                    e.unit.shield(e.unit.shield + 7000 * (wave - 300));
                    UnitPeculiarity.apply(e.unit, r.nextInt(25) + extra, 0, 0);
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

    public static void PeculiarChance(int num, int s_opp, float supper, float well, float midden, Unit unit) {
        if (Mathf.chance(supper)) {
            UnitPeculiarity.applySuper(unit, 1);
            if (num > s_opp) {
                UnitPeculiarity.apply(unit, num - s_opp, well, midden);
            }
        } else {
            UnitPeculiarity.apply(unit, num, well, midden);
        }
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