package Floor;

import Floor.FContent.DefaultContent.*;
import Floor.FContent.Override.BlockOverride;
import Floor.FContent.Override.TechOverride;
import Floor.FContent.Override.UnitOverride;
import Floor.FContent.ProjectContent.FSign;
import Floor.FContent.ProjectContent.UnitProjects;
import Floor.FContent.SpecialContent.*;
import mindustry.mod.Mod;

public class Floor extends Mod {
    public Floor() {
    }

    @Override
    public void loadContent() {
        FSign.load();
        FSettings.load();
        FLiquids.load();
        FItems.load();
        FCommands.load();
        FEntities.load();
        FStatusEffects.load();
        FWeathers.load();
        UnitOverride.load();
        BlockOverride.load();
        FUnits.load();
        UnitProjects.load();
        FEvents.load();
        FBlocks.load();
        FPlanets.load();
        PFloors.load();
        FPlanetGenerators.load();
        Techs.load();
        TechOverride.load();
    }
}