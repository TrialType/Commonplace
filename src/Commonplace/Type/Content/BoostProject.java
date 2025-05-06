package Commonplace.Type.Content;

import arc.func.Cons;
import arc.struct.IntIntMap;
import arc.struct.Seq;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.type.Weapon;

public class BoostProject extends UnitProject {
    public static final int addPart = 2;
    public static final int addPartPart = 1;
    public static final int changePart = 0;
    public static Cons<Weapon> weaponBoos = w -> {
    };
    public static Cons<Ability> abilityBoos = a -> {
    };

    public BulletType bullet;
    public int valve = changePart;

    public Cons<Weapon> applyW = w -> {
    };

    public Cons<Ability> applyA = a -> {
    };

    public BoostProject(float heavy, int max, String name) {
        super(heavy, max, name, u -> {
        });
    }

    public static void updateApply(IntIntMap map) {
        Seq<Integer> ids = new Seq<>(map.size);
        for (int i : map.keys().toArray().toArray()) {
            ids.add(i);
        }
        ids.sort(o -> -((BoostProject) all.get(o)).valve);
        weaponBoos = w -> {
            for (int id : ids) {
                BoostProject b = (BoostProject) all.get(id);
                for (int i = 0; i < map.get(id); i++) {
                    b.applyW.get(w);
                }
            }
        };
        abilityBoos = a -> map.forEach(e -> {
            for (int id : ids) {
                BoostProject b = (BoostProject) all.get(id);
                for (int i = 0; i < map.get(id); i++) {
                    b.applyA.get(a);
                }
            }
        });
    }
}
