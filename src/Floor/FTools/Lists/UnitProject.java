package Floor.FTools.Lists;

import arc.func.Cons;
import arc.struct.Seq;
import mindustry.gen.Unit;

public class UnitProject {
    public static UnitProject handTurret = new UnitProject(1, "", "", u -> {
    });

    public final static Seq<UnitProject> all = new Seq<>();
    public final int id;
    public final float heavy;
    public String icon = "";
    public String description = "";
    public final Cons<Unit> applier;

    public UnitProject(float heavy, String icon, String description, Cons<Unit> applier) {
        this.applier = applier;
        this.heavy = heavy;
        this.icon = icon;
        this.description = description;
        id = all.size;
        all.add(this);
    }

    public UnitProject(float heavy, Cons<Unit> applier) {
        this.applier = applier;
        this.heavy = heavy;
        id = all.size;
        all.add(this);
    }

    public static UnitProject get(int id) {
        return all.get(id);
    }
}
