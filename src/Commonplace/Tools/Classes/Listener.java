package Commonplace.Tools.Classes;

import arc.Core;
import arc.func.Boolp;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.SectorPresets;
import mindustry.gen.Groups;

public abstract class Listener {
    public static boolean inited = false;
    public final static Seq<Entry> listens = new Seq<>();
    public final static Seq<Entry> updaters = new Seq<>();
    public final static Seq<Entry> removers = new Seq<>();

    static {
        listens.addAll(
                new Entry(() -> !Core.settings.getBool("c-message1-unlock", false) && Vars.state.getSector() == SectorPresets.ruinousShores.sector,
                        () -> !Groups.build.contains(b -> b.block == Blocks.copperWall),
                        () -> Core.settings.put("c-message1-unlock", true))
        );
    }

    public static void update() {
        if (!Vars.state.isGame()) {
            inited = false;
            return;
        }

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
