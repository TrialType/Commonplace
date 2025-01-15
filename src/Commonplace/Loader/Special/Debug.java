package Commonplace.Loader.Special;

import Commonplace.Type.Control.CorrosionMist;
import Commonplace.Type.Dialogs.PlanetDialog2;
import Commonplace.Utils.Classes.Listener;
import arc.Core;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;

public class Debug {
    public static void load() {
        arc.Events.run(EventType.Trigger.update, Listener::update);
        arc.Events.run(EventType.Trigger.update, CorrosionMist::init);
        arc.Events.on(EventType.ClientLoadEvent.class, e -> Time.runTask(10f, () -> Vars.ui.planet = new PlanetDialog2()));

        Core.settings.put("launch-search", true);
        Core.settings.put("launch-pow", 3);
    }
}
