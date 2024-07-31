package Floor.FContent.SpecialContent;

import Floor.FAI.StrongBoostAI;
import mindustry.ai.UnitCommand;

public class FCommands {
    public static UnitCommand STB;
    public static void load(){
        STB = new UnitCommand("STB","STB",u -> new StrongBoostAI());
    }
}
