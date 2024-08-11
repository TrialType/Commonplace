package Commonplace.Content.SpecialContent;

import Commonplace.AI.BoostFlyingAI;
import mindustry.ai.UnitCommand;

public class Commands {
    public static UnitCommand STB;
    public static void load(){
        STB = new UnitCommand("STB","STB",u -> new BoostFlyingAI());
    }
}
