package Commonplace.Loader.Special;

import Commonplace.Loader.ProjectContent.Bullets;
import Commonplace.Loader.ProjectContent.Weapons;
import Commonplace.Utils.Classes.UnitPeculiarity;
import Commonplace.Utils.Interfaces.BuildUpGrade;
import Commonplace.Type.Dialogs.ProjectDialog;
import Commonplace.Type.Dialogs.MoreResearchDialog;
import arc.Core;
import arc.math.Mathf;
import arc.math.Rand;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.*;


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
        p_supper = Core.settings.getFloat("commonplace-p-supper", 0.0001f);
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
            if (Mathf.chance(p_supper)) {
                UnitPeculiarity.applySuper(e.unit, 1);
                if (p_num > supper_opp) {
                    UnitPeculiarity.apply(e.unit, p_num - supper_opp, p_well, p_midden);
                }
            } else {
                UnitPeculiarity.apply(e.unit, p_num, p_well, p_midden);
            }
        });
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            int wave = Vars.state.wave;
            int extra = (wave >= 42 ? 1 : 0) + (e.unit.isBoss() ? Math.max(1, wave / 20) : 0);
            float chance = 0.001f * wave / 5 * (e.unit.isBoss() ? 2 : 1);
            if (wave < 8) {
                UnitPeculiarity.apply(e.unit, extra, r.nextInt(2), r.nextInt(3));
            } else if (wave < 18) {
                UnitPeculiarity.apply(e.unit, extra, r.nextInt(3), r.nextInt(3));
            } else if (wave < 30) {
                UnitPeculiarity.apply(e.unit, r.nextInt(2) + extra, r.nextInt(3), r.nextInt(4));
            } else if (wave < 42) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(4) + extra, r.nextInt(5), r.nextInt(4));
            } else if (wave < 55) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(5) + extra, r.nextInt(5), r.nextInt(3));
            } else if (wave < 70) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(6) + extra, r.nextInt(6), r.nextInt(3));
            } else if (wave <= 85) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(8) + extra, r.nextInt(7), r.nextInt(3));
            } else if (wave <= 100) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(11) + extra, r.nextInt(8), r.nextInt(2));
            } else if (wave <= 115) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(13) + extra, r.nextInt(8), r.nextInt(2));
            } else if (wave <= 135) {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(16) + extra, r.nextInt(10), r.nextInt(2));
            } else {
                if (Mathf.chance(chance)) {
                    UnitPeculiarity.applySuper(e.unit, extra);
                }
                UnitPeculiarity.apply(e.unit, r.nextInt(16 + Math.min(20, (wave - 135) / 3)) + extra, r.nextInt(15), 0);
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