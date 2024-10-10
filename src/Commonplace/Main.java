package Commonplace;

import Commonplace.Content.DefaultContent.*;
import Commonplace.Content.Override.*;
import Commonplace.Content.ProjectContent.*;
import Commonplace.Content.SpecialContent.*;
import mindustry.mod.Mod;

public class Main extends Mod {
    @Override
    public void loadContent() {
        Sign.load();
        MSettings.load();
        Liquids2.load();
        Commands.load();
        Entities.load();
        StatusEffects2.load();
        Weathers2.load();
        Units2.load();
        UnitProjects.load();
        Events.load();
        Blocks2.load();
        Planets2.load();
        SectorPresets2.load();
        SectorPresetOverride.load();
        TechOverride.load();
        UnitOverride.load();
        BlockOverride.load();
        Techs.load();
    }
}