package Commonplace.Content.DefaultContent;

import mindustry.type.SectorPreset;

import static Commonplace.Content.DefaultContent.FPlanets.ENGSWEIS;

public class FSectorPresets {
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
