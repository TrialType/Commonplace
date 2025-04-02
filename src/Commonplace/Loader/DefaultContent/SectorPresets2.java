package Commonplace.Loader.DefaultContent;

import mindustry.type.SectorPreset;

import static Commonplace.Loader.DefaultContent.Planets2.ENGSWEIS;

public class SectorPresets2 {
    public static SectorPreset longestDown;

    public static void load() {
        longestDown = new SectorPreset("long-down", ENGSWEIS, 64) {{
            alwaysUnlocked = true;

            difficulty = 6;
        }};
    }
}
