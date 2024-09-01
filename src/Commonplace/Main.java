package Commonplace;

import Commonplace.Content.DefaultContent.*;
import Commonplace.Content.Override.BlockOverride;
import Commonplace.Content.Override.SectorPresetOverride;
import Commonplace.Content.Override.TechOverride;
import Commonplace.Content.Override.UnitOverride;
import Commonplace.Content.ProjectContent.Sign;
import Commonplace.Content.ProjectContent.UnitProjects;
import Commonplace.Content.SpecialContent.*;
import mindustry.mod.Mod;

public class Main extends Mod {
    public Main() {
    }

    @Override
    public void loadContent() {
        Sign.load();
        MSettings.load();
        FLiquids.load();
        FItems.load();
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