package Floor.FContent.DefaultContent;

import mindustry.type.SectorPreset;

import static Floor.FContent.DefaultContent.FPlanets.ENGSWEIS;

public class FPlanetGenerators {
    public static SectorPreset fullWater, longestDown;

    public static void load() {
        fullWater = new SectorPreset("szc", ENGSWEIS, 96) {{
            difficulty = 10;
        }};

        longestDown = new SectorPreset("long-down", ENGSWEIS, 64) {{
            alwaysUnlocked = true;

            difficulty = 6;
        }};
    }
}
