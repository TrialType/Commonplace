package Commonplace.Loader.Override;

import mindustry.content.SectorPresets;
import mindustry.content.TechTree;
import mindustry.content.UnitTypes;
import mindustry.game.Objectives;

public class TechOverride {
    public static void load() {
        TechTree.all.each(t -> {
            if (t.content == UnitTypes.nova) {
                t.objectives.add(new Objectives.SectorComplete(SectorPresets.overgrowth));
            }
        });
    }
}
