package Commonplace.Loader.Special;

import Commonplace.AI.BoostFlyingAI;
import mindustry.ai.UnitCommand;

public class Commands {
    public static UnitCommand boostFlying;
    public static void load(){
        boostFlying = new UnitCommand("boostFlying","boostFlying", u -> new BoostFlyingAI());
    }
}
