package Commonplace.Loader.Special;

import Commonplace.Loader.ProjectContent.Bullets;
import Commonplace.Loader.ProjectContent.Weapons;
import Commonplace.Type.Dialogs.PlanetDialog2;
import Commonplace.Utils.Classes.Listener;
import Commonplace.Utils.Classes.UnitPeculiarity;
import Commonplace.Utils.Interfaces.BuildUpGrade;
import Commonplace.Type.Dialogs.ProjectDialog;
import Commonplace.Type.Dialogs.MoreResearchDialog;
import arc.Core;
import arc.graphics.Color;
import arc.math.Rand;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.ui.dialogs.BaseDialog;


public class Events {
    private static final Rand r = new Rand();
    public static int p_num;
    public static float p_well;
    public static float p_midden;

    static {
        p_num = Core.settings.getInt("commonplace-p-num",5);
        p_well = Core.settings.getFloat("commonplace-p-well",0.5f);
        p_midden = Core.settings.getFloat("commonplace-p-midden",0.2f);
    }

    public static void load() {
        //arc.Events.run(EventType.Trigger.update, Listener::update);

        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, UnitPeculiarity::init));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, () -> Vars.ui.research = new MoreResearchDialog()));
        //arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.planet = new PlanetDialog2()));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, ProjectDialog::create));

        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
        });

        arc.Events.on(EventType.UnitCreateEvent.class, e -> UnitPeculiarity.apply(e.unit, p_num, p_well, p_midden));
        arc.Events.on(EventType.UnitSpawnEvent.class, e -> {
            int wave = Vars.state.wave;
            int extra = 1 + (e.unit.isBoss() ? wave / 20 : 0);
            if (wave < 8) {
                UnitPeculiarity.apply(e.unit, 0, r.nextInt(2), r.nextInt(3));
            } else if (wave < 18) {
                UnitPeculiarity.apply(e.unit, 0, r.nextInt(3), r.nextInt(3));
            } else if (wave < 30) {
                UnitPeculiarity.apply(e.unit, r.nextInt(2) + extra, r.nextInt(3), r.nextInt(4));
            } else if (wave < 42) {
                UnitPeculiarity.apply(e.unit, r.nextInt(4) + extra, r.nextInt(5), r.nextInt(4));
            } else if (wave < 55) {
                UnitPeculiarity.apply(e.unit, r.nextInt(5) + extra, r.nextInt(5), r.nextInt(3));
            } else if (wave < 70) {
                UnitPeculiarity.apply(e.unit, r.nextInt(6) + extra, r.nextInt(6), r.nextInt(3));
            } else if (wave <= 85) {
                UnitPeculiarity.apply(e.unit, r.nextInt(8) + extra, r.nextInt(7), r.nextInt(3));
            } else if (wave <= 100) {
                UnitPeculiarity.apply(e.unit, r.nextInt(11) + extra, r.nextInt(8), r.nextInt(2));
            } else if (wave <= 115) {
                UnitPeculiarity.apply(e.unit, r.nextInt(13) + extra, r.nextInt(8), r.nextInt(2));
            } else if (wave <= 135) {
                UnitPeculiarity.apply(e.unit, r.nextInt(16) + extra, r.nextInt(10), r.nextInt(2));
            } else {
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