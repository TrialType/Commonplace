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
        FLiquids.load();
        Commands.load();
        Entities.load();
        FStatusEffects.load();
        FWeathers.load();
        FUnits.load();
        UnitProjects.load();
        Events.load();
        CBlocks.load();
        FPlanets.load();
        FSectorPresets.load();
        SectorPresetOverride.load();
        TechOverride.load();
        UnitOverride.load();
        BlockOverride.load();
        Techs.load();
    }
}