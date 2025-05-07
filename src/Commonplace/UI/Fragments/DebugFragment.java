package Commonplace.UI.Fragments;

import Commonplace.Utils.Classes.Vars2;
import arc.Core;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Time;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class DebugFragment {
    public Table useRandomTable = new Table(Tex.windowEmpty);

    private long lastToast = 0;

    public void build(Group parent) {
        useRandomTable.label(() -> Core.bundle.format("@useRandomChange", Vars2.useRandom)).wrap().get().setAlignment(Align.center, Align.center);
        parent.addChild(useRandomTable);
        useRandomTable.setFillParent(true);
        useRandomTable.touchable = Touchable.disabled;

        parent.fill(t -> {
            t.visible(() -> Vars2.debug);
            t.table(s -> {
                s.setBackground(Styles.black6);
                s.add("-----------------------").row();
                s.table(c -> {
                    Element e = new Element();
                    e.setFillParent(true);
                    Stack stack = new Stack(e, new Label(() -> Core.bundle.get("@useRandom")));
                    stack.setFillParent(true);
                    c.add(stack);
                    c.clicked(() -> {
                        Vars2.useRandom = !Vars2.useRandom;
                        showInfo(useRandomTable);
                    });
                }).width(280).row();
                s.add("-----------------------").row();
                s.table(c -> {
                    Element e = new Element();
                    e.setFillParent(true);
                    Stack stack = new Stack(e, new Label(() -> Core.bundle.get("@lockRandom")));
                    stack.setFillParent(true);
                    c.add(stack);
                    c.clicked(() -> {
                        Vars2.lockRandom = !Vars2.lockRandom;
                        showInfo(useRandomTable);
                    });
                }).width(280).row();
                s.add("-----------------------").row();
            });
            t.left();
        });
    }

    public void showInfo(Table t) {
        float duration = Time.timeSinceMillis(lastToast);
        if (duration < 3000) {
            t.getActions().clear();
            t.actions(Actions.fadeOut(0.25f));
        }
        lastToast = Time.millis();
        t.actions(Actions.fadeIn(1f), Actions.delay(1), Actions.fadeOut(1f));
    }
}
