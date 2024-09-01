package Commonplace.Content.Override;

import mindustry.content.SectorPresets;
import mindustry.maps.generators.FileMapGenerator;

public class SectorPresetOverride {
    public static void load() {
        SectorPresets.impact0078.generator = new FileMapGenerator("impact0078", SectorPresets.impact0078);
        SectorPresets.groundZero.generator = new FileMapGenerator("Ground Zero", SectorPresets.groundZero);
        SectorPresets.frozenForest.generator = new FileMapGenerator("frozenForest", SectorPresets.frozenForest);
        SectorPresets.craters.generator = new FileMapGenerator("craters", SectorPresets.craters);
        SectorPresets.biomassFacility.generator = new FileMapGenerator("biomassFacility", SectorPresets.biomassFacility);
        SectorPresets.ruinousShores.generator = new FileMapGenerator("ruinousShores", SectorPresets.ruinousShores);
    }
}
