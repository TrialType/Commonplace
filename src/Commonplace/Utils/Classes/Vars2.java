package Commonplace.Utils.Classes;

import Commonplace.Type.Dialogs.MessageDialog;
import mindustry.ui.Styles;

public abstract class Vars2 {
    public static boolean useRandom = false;
    public static boolean lockRandom = true;
    public static final MessageDialog message = new MessageDialog("@story", Styles.defaultDialog);

    public static void load() {
    }
}
