package Commonplace.Loader.Special;

import Commonplace.Type.Control.CorrosionMist;
import Commonplace.Type.Dialogs.PlanetDialog2;
import Commonplace.Type.Renders.TestRender;
import Commonplace.Utils.Classes.Listener;
import arc.Core;
import arc.util.Time;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.game.EventType;

public class Debug {
    public static void load() {
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Timer.schedule(Listener::update, 10, 1));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.planet = new PlanetDialog2()));

        arc.Events.on(EventType.WorldLoadEvent.class, e -> {
            CorrosionMist.changer.clear();
            CorrosionMist.cleaner.clear();
            CorrosionMist.timeBoost.clear();
            CorrosionMist.update = false;
        });
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Timer.schedule(CorrosionMist::update, 10, 1));
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(5f, () -> CorrosionMist.maps.add(Core.bundle.get("test_map"))));

        Core.settings.put("launch-search", true);
        Core.settings.put("launch-pow", 3);

        TestRender.init();
        arc.Events.run(EventType.Trigger.draw, TestRender::draw);
    }
}
