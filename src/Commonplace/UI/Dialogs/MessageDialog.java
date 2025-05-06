package Commonplace.UI.Dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

public class MessageDialog extends BaseDialog {
    public MessageDialog(String title, DialogStyle style) {
        super(Core.bundle.get(title), style);
    }

    @Override
    public Dialog show() {
        cont.clear();
        buttons.clear();
        cont.pane(t -> {
            for (int i = 1; i <= 10; i++) {
                int fi = i;
                t.table(l -> rebuildStoryLine(l, fi)).width(250).height(100);
                t.row();
            }
        }).width(300);
        addCloseButton();
        return super.show();
    }

    public void rebuildStoryLine(Table t, int index) {
        t.clear();
        if (Core.settings.getBool("c-message" + index + "-unlock")) {
            t.button(Icon.redo, () -> Vars.ui.showText(Core.bundle.get("message" + index + ".name"), Core.bundle.get("message" + index)));
            t.button(Icon.trash, () -> {
                Core.settings.put("c-message" + index + "-unlock", false);
                rebuildStoryLine(t, index);
            });
        } else {
            t.setColor(Color.black);
            t.image(Icon.lock).center();
        }
    }
}
