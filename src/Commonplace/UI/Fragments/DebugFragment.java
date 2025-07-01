package Commonplace.UI.Fragments;

import Commonplace.UI.Elements.UnitGroup;
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
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class DebugFragment {
    public UnitGroup enemyTable;
    public Table lastToastTable = null;
    public Table useRandomTable = new Table(Tex.windowEmpty);
    public Table lockRandomTable = new Table(Tex.windowEmpty);

    private long lastToast = 0;

    public void build(Group parent) {
        useRandomTable.label(() -> Core.bundle.format("@useRandomChange", Vars2.useRandom)).wrap().get().setAlignment(Align.center, Align.center);
        parent.addChild(useRandomTable);
        useRandomTable.setFillParent(true);
        useRandomTable.touchable = Touchable.disabled;
        useRandomTable.actions(Actions.fadeOut(0));

        lockRandomTable.label(() -> Core.bundle.format("@lockRandomChange", Vars2.lockRandom)).wrap().get().setAlignment(Align.center, Align.center);
        parent.addChild(lockRandomTable);
        lockRandomTable.setFillParent(true);
        lockRandomTable.touchable = Touchable.disabled;
        lockRandomTable.actions(Actions.fadeOut(0));

        parent.fill(t -> {
            t.visible(() -> Vars2.debug);
            t.table(s -> {
                s.setBackground(Styles.black6);
                s.add("-----------------------").row();
                s.table(c -> {
                    Element e = new Element();
                    e.setFillParent(true);
                    Stack stack = new Stack(e, new Label(() -> Core.bundle.format("@useRandomChange", Vars2.useRandom)));
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
                    Stack stack = new Stack(e, new Label(() -> Core.bundle.format("@lockRandomChange", Vars2.lockRandom)));
                    stack.setFillParent(true);
                    c.add(stack);
                    c.clicked(() -> {
                        Vars2.lockRandom = !Vars2.lockRandom;
                        showInfo(lockRandomTable);
                    });
                }).width(280).row();
                s.add("-----------------------").row();
            });
            t.left();
        });

        enemyTable = new UnitGroup();
        enemyTable.visible(() -> Vars2.debug);
        enemyTable.setPosition(0, 800);
        parent.addChild(enemyTable);
    }

    public void showInfo(Table t) {
        float duration = Time.timeSinceMillis(lastToast);
        if (duration < 2000) {
            lastToastTable.getActions().clear();
            lastToastTable.actions(Actions.fadeOut(0));
        }
        lastToastTable = t;
        lastToast = Time.millis();
        t.actions(Actions.fadeIn(0.75f), Actions.delay(0.5f), Actions.fadeOut(0.75f));
    }

    public void addInfo(UnitType type) {
        if (enemyTable != null) {
            enemyTable.addLabel(type);
        }
    }
}
