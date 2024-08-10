package Commonplace;

import Commonplace.FContent.DefaultContent.*;
import Commonplace.FContent.Override.BlockOverride;
import Commonplace.FContent.Override.TechOverride;
import Commonplace.FContent.Override.UnitOverride;
import Commonplace.FContent.ProjectContent.Sign;
import Commonplace.FContent.ProjectContent.UnitProjects;
import Commonplace.FContent.SpecialContent.*;
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
        MCommands.load();
        MEntities.load();
        FStatusEffects.load();
        FWeathers.load();
        UnitOverride.load();
        BlockOverride.load();
        FUnits.load();
        UnitProjects.load();
        MEvents.load();
        CBlocks.load();
        FPlanets.load();
        PFloors.load();
        FPlanetGenerators.load();
        Techs.load();
        TechOverride.load();
    }
}