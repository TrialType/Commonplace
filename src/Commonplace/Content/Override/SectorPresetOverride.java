package Commonplace.Content.Override;

import mindustry.content.SectorPresets;
import mindustry.maps.generators.FileMapGenerator;

public class SectorPresetOverride {
    public static void load() {
        SectorPresets.impact0078.generator = new FileMapGenerator("impact0078", SectorPresets.impact0078);
        SectorPresets.groundZero.generator = new FileMapGenerator("groundZero", SectorPresets.groundZero);
    }
}
