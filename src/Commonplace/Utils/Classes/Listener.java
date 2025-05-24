package Commonplace.Utils.Classes;

import arc.Core;
import arc.func.Boolp;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.SectorPresets;
import mindustry.gen.Tex;

public abstract class Listener {
    public static boolean inited = false;
    public final static Seq<Entry> listens = new Seq<>();
    public final static Seq<Entry> updaters = new Seq<>();
    public final static Seq<Entry> removers = new Seq<>();

    static {
        listens.addAll(
                new Entry(
                        () -> !Core.settings.getBool("c-message1-unlock", false) && Vars.state.getSector() == SectorPresets.ruinousShores.sector,
                        () -> Vars.world.tile(173, 135).build == null,
                        () -> {
                            Core.settings.put("c-message1-unlock", true);
                            createSign("@findMessage");
                        }
                )
        );
    }

    public static void update() {
        if (!inited) {
            init();
        }

        if (!updaters.isEmpty()) {
            removers.clear();
            for (Entry e : updaters) {
                if (e.canListen.get()) {
                    if (e.canApply.get()) {
                        e.apply.run();
                    }
                } else {
                    removers.add(e);
                }
            }
            updaters.removeAll(removers);
        }
    }

    public static void init() {
        updaters.clear();
        for (Entry e : listens) {
            if (e.canListen.get()) {
                updaters.add(e);
            }
        }
        inited = true;
    }

    public static void remove(Element... e) {
        for (Element el : e) {
            el.actions(Actions.remove());
            Core.app.post(el::remove);
            el.remove();
        }
    }

    public static void createSign(String text) {
        Table display = new Table();
        display.setBackground(Tex.buttonDown);
        display.setSize(200, 54);
        display.setPosition(775, 1000);
        display.label(() -> Core.bundle.get(text)).center().height(54);

        Element e = new Element();
        e.fillParent = true;
        e.clicked(() -> actionRemove(
                () -> display.actions(Actions.moveBy(0, 54, 2)),
                () -> remove(display, e), 240, 120));

        actionRemove(
                () -> display.actions(Actions.moveBy(0, 54, 2)),
                () -> remove(display, e), 240, 120);

        Core.scene.add(display);
        display.top();
        Core.scene.add(e);
        display.actions(Actions.fadeIn(0), Actions.moveBy(0, -54, 2));
    }

    public static void actionRemove(Runnable action, Runnable remover, float stop, float duration) {
        Time.run(stop, action);
        Time.run(stop + duration, remover);
    }

    public static class Entry {
        public Boolp canListen;
        public Boolp canApply;
        public Runnable apply;

        public Entry(Boolp canListen, Boolp canResult, Runnable update) {
            this.canListen = canListen;
            this.canApply = canResult;
            this.apply = update;
        }
    }
}
