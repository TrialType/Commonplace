package Commonplace.Utils.Classes;

import Commonplace.UI.Dialogs.MessageDialog;
import Commonplace.UI.Fragments.DebugFragment;
import mindustry.Vars;
import mindustry.ui.Styles;

public abstract class Vars2 {
    public static DebugFragment debugFrag;

    public static boolean debug = true;
    public static boolean useRandom = false;
    public static boolean lockRandom = true;
    public static final MessageDialog message = new MessageDialog("@story", Styles.defaultDialog);

    public static void load(){
        debugFrag = new DebugFragment();

        debugFrag.build(Vars.ui.hudGroup);
    }
}
