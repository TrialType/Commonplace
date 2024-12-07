package Commonplace.Entities.Unit;

import Commonplace.Utils.Interfaces.OwnerSpawner;
import arc.math.Mathf;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class TileSpawnerUnit extends BoostUnitEntity implements OwnerSpawner {
    private final Seq<Integer> minerId = new Seq<>();
    public Seq<Unit> miners = new Seq<>();

    protected TileSpawnerUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
    }

    public static TileSpawnerUnit create() {
        return new TileSpawnerUnit();
    }

    @Override
    public void update() {
        if (minerId.size > 0 && miners.size == 0) {
            for (int id : minerId) {
                miners.add(Groups.unit.getByID(id));
            }
            minerId.clear();
        }
        miners.removeAll(u -> u == null || u.dead || u.health() < 0);
        super.update();
    }

    @Override
    public int classId() {
        return 115;
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        int number = read.i();
        for (int i = 0; i < number; i++) {
            minerId.add(read.i());
        }
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        write.i(miners.size);
        for (int i = 0; i < miners.size; i++) {
            write.i(miners.get(i).id);
        }
    }

    @Override
    public Seq<Unit> unit() {
        return miners;
    }
}
