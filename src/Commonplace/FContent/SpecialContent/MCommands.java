package Commonplace.FContent.SpecialContent;

import Commonplace.FAI.BoostFlyingAI;
import mindustry.ai.UnitCommand;

public class MCommands {
    public static UnitCommand STB;
    public static void load(){
        STB = new UnitCommand("STB","STB",u -> new BoostFlyingAI());
    }
}
