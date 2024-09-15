package Commonplace.Content.SpecialContent;

import Commonplace.Content.ProjectContent.Bullets;
import Commonplace.Content.ProjectContent.Weapons;
import Commonplace.Entities.FAbility.LevelSign;
import Commonplace.Entities.FUnit.Override.*;
import Commonplace.Tools.Classes.Listener;
import Commonplace.Tools.Classes.UnitPeculiarity;
import Commonplace.Tools.Interfaces.BuildUpGrade;
import Commonplace.Tools.Interfaces.PeculiarityC;
import Commonplace.FType.FDialog.ProjectDialog;
import Commonplace.FType.FDialog.Old.MoreResearchDialog;
import arc.Core;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

import java.util.Random;


public class Events {
    private static final ObjectMap<Class<?>, Prov<? extends Unit>> classes = new ObjectMap<>();
    private static final Random r = new Random();

    static {
        classes.put(MechUnit.class, FMechUnit::create);
        classes.put(LegsUnit.class, FLegsUnit::create);
        classes.put(UnitEntity.class, FUnitEntity::create);
        classes.put(UnitWaterMove.class, FUnitWaterMove::create);
        classes.put(ElevationMoveUnit.class, FElevationMoveUnit::create);
        classes.put(TankUnit.class, FTankUnit::create);
        classes.put(PayloadUnit.class, FPayloadUnit::create);
    }

    public static void load() {
        arc.Events.run(EventType.Trigger.update, Listener::update);
        //arc.Events.run(EventType.Trigger.update, CorrosionMist::init);

        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, UnitPeculiarity::init));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.research = new MoreResearchDialog()));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.planet.shown(() -> Vars.ui.planet.buttons.button(Core.bundle.get("@story"), Icon.book, () -> {
            BaseDialog dialog = new BaseDialog(Core.bundle.get("@story"));
            dialog.cont.pane(t -> {
                for (int i = 1; i <= 10; i++) {
                    int fi = i;
                    t.table(l -> rebuildStoryLine(l, fi)).width(250).height(100);
                    t.row();
                }
            }).width(300);
            dialog.addCloseButton();
            dialog.show();
        }).width(200).height(54).bottom())));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, ProjectDialog::create));
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            Bullets.load();
            Weapons.load();
        });
        arc.Events.on(EventType.ContentInitEvent.class, e -> {
            for (UnitType u : Vars.content.units()) {
                if (classes.get(u.constructor.get().getClass()) != null) {
                    u.constructor = classes.get(u.constructor.get().getClass());
                    u.abilities.add(new LevelSign());
                }
            }
        });

        arc.Events.on(EventType.UnitCreateEvent.class, e -> {
            if (e.unit instanceof PeculiarityC) {
                UnitPeculiarity.apply(e.unit, 5, 0.6f, 0.3f);
            }
        });

        arc.Events.on(EventType.UnitBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                if (e.unit instanceof PeculiarityC uug) {
                    fug.addExp(e.unit.maxHealth * Math.max(1, uug.getLevel() / 15));
                } else {
                    fug.addExp(e.unit.maxHealth);
                }
            }
        });
        arc.Events.on(EventType.BuildingBulletDestroyEvent.class, e -> {
            if (e.bullet.owner instanceof BuildUpGrade fug) {
                fug.addExp(e.build.maxHealth);
            }
        });
    }

    public static void rebuildStoryLine(Table t, int index) {
        t.clear();
        if (Core.settings.getBool("c-message" + index + "-unlock")) {
            t.button(Icon.redo, () -> Vars.ui.showText(Core.bundle.get("message" + index + ".name"), Core.bundle.get("message" + index)));
            t.button(Icon.trash, () -> {
                Core.settings.put("c-message" + index + "-unlock", false);
                rebuildStoryLine(t, index);
            });
        } else {
            t.setColor(Color.black);
            t.image(Icon.lock).center();
        }
    }

    public static class UnitDestroyOtherEvent {
        public Unit killer;
        public Healthc other;

        public UnitDestroyOtherEvent(Unit killer, Healthc other) {
            this.killer = killer;
            this.other = other;
        }
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