package Commonplace.Loader.Special;

import arc.Core;

public class Debug {
    public static void load() {
        //arc.Events.run(EventType.Trigger.update, CorrosionMist::init);

        Core.settings.put("launch-search", true);
        Core.settings.put("launch-pow", 3);
    }
}
