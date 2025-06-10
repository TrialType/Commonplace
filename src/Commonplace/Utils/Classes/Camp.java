package Commonplace.Utils.Classes;

import Commonplace.Entities.Unit.CampUnit;
import Commonplace.Entities.UnitType.UnitType2;
import Commonplace.Type.CampChanger.OrderedFloat;
import Commonplace.Type.CampChanger.OrderedChanger;
import arc.func.Boolc;
import arc.func.Floatc;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.game.Team;

public class Camp {
    public static final Seq<Camp> all = new Seq<>();

    public String name;
    public Seq<CampUnit> units = new Seq<>();
    public ObjectMap<Team, Seq<OrderedChanger>> changers = new ObjectMap<>();

    public float rangeValue;
    public boolean lastRangeOverride;
    public ObjectMap<Team, Float> ranges = new ObjectMap<>();

    static {
        new Camp("dark");
    }

    private Camp(String name) {
        all.add(this);

        this.name = name;
    }

    public static void updateEach() {
        all.forEach(Camp::update);
    }

    public void update() {
        setDefault();

        IntSet used = new IntSet();

        float[] floats = new float[3];
        Object[] objects = new Object[]{
                null,
                (Floatc) (f -> rangeValue = f),
                (Boolc) (b -> lastRangeOverride = b)
        };
        boolean[] bools = new boolean[1];

        units.each(u -> {
            if (u.type2 != null) {
                UnitType2 type = u.type2;
                boolean unused = !used.contains(type.id);
                used.add(type.id);
                if (type.rangeMulSuper || unused) {
                    Team team = u.team;
                    OrderedFloat order;
                    objects[0] = (Floatc) (f -> ranges.put(team, f));
                    for (int i = type.campRangeChanger.size - 1; i >= 0 && (order = type.campRangeChanger.get(i)).value >= rangeValue; i--) {
                        if (order.team) {
                            floats[0] = rangeValue;
                            floats[1] = ranges.get(team, 1f);
                            bools[0] = lastRangeOverride;
                            order.applyCamp(floats, bools, objects);
                        } else {
                            order.setPos(u.x, u.y);
                            changers.get(team, new Seq<>()).add(order);
                        }
                    }
                }
            }
        });

        changers.each((t, s) -> s.sort(o -> -o.value));

        units.each(u -> {
            if (u.type2.canApply) {
                Seq<OrderedChanger> changes = changers.get(u.team, new Seq<>());
                OrderedChanger order;
                for (int i = changers.size - 1; i >= 0 && (order = changes.get(i)).valid(u); i--) {
                    order.apply(u);
                }
            }
        });
    }

    public void setDefault() {
        rangeValue = -1f;
        lastRangeOverride = false;

        units.removeAll(u -> !u.isValid());
        units.each(CampUnit::resetCamp);

        ranges.clear();
        changers.clear();
    }
}
