package Commonplace;

import Commonplace.Entities.Block.Filler;
import Commonplace.Loader.DefaultContent.*;
import Commonplace.Loader.Override.*;
import Commonplace.Loader.Special.*;
import arc.KeyBinds;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.mod.Mod;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Build;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumePowerCondition;

public class Main extends Mod {
    @Override
    public void loadContent() {
//        Sign.load();
//        Liquids2.load();
//        Commands.load();
//        Entities.load();
        StatusEffects2.load();
//        Weathers2.load();
//        Units2.load();
//        UnitProjects.load();
        Events.load();
//        Blocks2.load();
//        Planets2.load();
//        SectorPresets2.load();
//        SectorPresetOverride.load();
//        TechOverride.load();
        UnitOverride.load();
        BlockOverride.load();
//        Techs.load();
//        Debug.load();

        new Filler("shock-mine-builder", Blocks.shockMine) {{
            requirements(Category.effect, ItemStack.with(Items.silicon, 75, Items.lead, 100, Items.graphite, 50), true);
            consume(new ConsumePowerCondition(5, b -> b instanceof FillerBuild f && f.progress < 1));

            size = 2;
        }};
    }
}