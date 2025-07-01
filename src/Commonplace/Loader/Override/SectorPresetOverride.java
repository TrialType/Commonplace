package Commonplace.Loader.Override;

import mindustry.content.SectorPresets;
import mindustry.maps.generators.FileMapGenerator;

public class SectorPresetOverride {
    public static void load() {
        SectorPresets.craters.generator = new FileMapGenerator("craters", SectorPresets.craters);
        SectorPresets.desolateRift.generator = new FileMapGenerator("desolateRift", SectorPresets.desolateRift);
        SectorPresets.frozenForest.generator = new FileMapGenerator("frozenForest", SectorPresets.frozenForest);
        SectorPresets.fungalPass.generator = new FileMapGenerator("fungalPass", SectorPresets.fungalPass);
        SectorPresets.overgrowth.generator = new FileMapGenerator("overgrowth", SectorPresets.overgrowth);
        SectorPresets.biomassFacility.generator = new FileMapGenerator("biomassFacility", SectorPresets.biomassFacility);
    }
}
