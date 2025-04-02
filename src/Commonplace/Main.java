package Commonplace;

import Commonplace.Loader.DefaultContent.*;
import Commonplace.Loader.Override.*;
import Commonplace.Loader.Special.*;
import Commonplace.Type.Renders.TestRender;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class Main extends Mod {
    @Override
    public void loadContent() {
//        Sign.load();
//        Liquids2.load();
//        Commands.load();
//        Entities.load();
        StatusEffects2.load();
//        Weathers2.load();
        Units2.load();
//        UnitProjects.load();
        Events.load();
        Blocks2.load();
//        Planets2.load();
//        SectorPresets2.load();
//        SectorPresetOverride.load();
//        TechOverride.load();
        UnitOverride.load();
        BlockOverride.load();
//        Techs.load();
//        Debug.load();
    }

    @Override
    public void init(){
        TestRender.init();
        arc.Events.run(EventType.Trigger.draw, TestRender::draw);
    }
}