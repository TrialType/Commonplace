package Commonplace.UI.Fragments;

import Commonplace.Utils.Classes.Vars2;
import arc.Core;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Time;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class DebugFragment {
    private long lastToast = 0;

    public void build(Group parent) {
        parent.fill(t -> {
            t.visible(() -> Vars2.debug);
            t.table(s -> {
                s.setBackground(Styles.black6);
                s.table(c -> {
                    Element e = new Element();
                    c.add(e);
                    e.setFillParent(true);
                    e.clicked(() -> {
                        Vars2.useRandom = !Vars2.useRandom;
                        showInfo(Core.bundle.format("@useRandomChange", Vars2.useRandom));
                    });
                    c.clicked(() -> {
                        Vars2.useRandom = !Vars2.useRandom;
                        showInfo(Core.bundle.format("@useRandomChange", Vars2.useRandom));
                    });
                    c.add(Core.bundle.get("@useRandom"));
                }).width(280).row();
                s.table(c -> {
                    Element e = new Element();
                    c.add(e);
                    e.setFillParent(true);
                    e.clicked(() -> {
                        Vars2.lockRandom = !Vars2.lockRandom;
                        showInfo(Core.bundle.format("@lockRandomChange", Vars2.lockRandom));
                    });
                    c.clicked(() -> {
                        Vars2.lockRandom = !Vars2.lockRandom;
                        showInfo(Core.bundle.format("@lockRandomChange", Vars2.lockRandom));
                    });
                    c.add(Core.bundle.get("@lockRandom"));
                }).width(280);
            });
            t.left();
        });
    }

    public void showInfo(String text) {
        float duration = Time.timeSinceMillis(lastToast);
        if (duration >= 3000) {
            lastToast = Time.millis();
            Table t = new Table(Tex.windowEmpty);
            t.setLayoutEnabled(true);
            t.add(text).width(280).wrap().get().setAlignment(Align.center, Align.center);
            t.setPosition(Core.camera.width / 2, Core.camera.height / 4, Align.center);
            Core.scene.table().top().add(t);
            t.actions(Actions.fadeIn(1f), Actions.delay(1), Actions.fadeOut(1f), Actions.remove());
        } else {
            Time.run((3000 - duration) / 1000f * 60, () -> showInfo(text));
        }
    }
}
