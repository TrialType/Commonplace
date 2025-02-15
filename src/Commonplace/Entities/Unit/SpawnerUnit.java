package Commonplace.Entities.Unit;

import Commonplace.Utils.Interfaces.OwnerSpawner;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Groups;
import mindustry.gen.LegsUnit;
import mindustry.gen.Unit;

public class SpawnerUnit extends LegsUnit implements OwnerSpawner {
    Seq<Integer> childId = new Seq<>();
    Seq<Unit> child = new Seq<>();

    public static SpawnerUnit create() {
        return new SpawnerUnit();
    }

    @Override
    public int classId() {
        return 121;
    }

    @Override
    public void update() {
        super.update();

        if (childId.size > 0) {
            for (Unit u : child) {
                if (u instanceof OwnerSpawner s) {
                    s.spawner(null);
                }
            }
            child.clear();
            for (int id : childId) {
                child.add(Groups.unit.getByID(id));
            }
            childId.clear();
        }

        child.removeAll(u -> u.dead || u.health <= 0 || !u.isAdded());
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        int num = read.i();
        for (int i = 0; i < num; i++) {
            childId.add(read.i());
        }
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        write.i(child.size);
        for (Unit u : child) {
            write.i(u.id);
        }
    }

    @Override
    public Seq<Unit> unit() {
        return child;
    }
}
