package Floor.FEntities.FUnit.Override;

import Floor.FTools.FUnitUpGrade;
import Floor.FTools.LayAble;
import Floor.FTools.UnitChainAble;
import arc.math.Mathf;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.ElevationMoveUnit;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.io.TypeIO;

import java.util.HashMap;
import java.util.Map;

public class FElevationMoveUnit extends ElevationMoveUnit implements FUnitUpGrade, LayAble {
    private final Seq<Integer> idList = new Seq<>();
    public Seq<Unit> units = new Seq<>();
    public Map<String, Integer> unitAbilities = new HashMap<>();
    public int level = 0;
    public float exp = 0;

    protected FElevationMoveUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
    }

    public static FElevationMoveUnit create() {
        return new FElevationMoveUnit();
    }

    @Override
    public int classId() {
        return 109;
    }

    @Override
    public void read(Reads read) {
        short REV = read.s();
        if (REV == 0) {
            TypeIO.readAbilities(read, this.abilities);
            this.ammo = read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = TypeIO.readTile(read);
            TypeIO.readMounts(read, this.mounts);
            this.plans = TypeIO.readPlansQueue(read);
            this.rotation = read.f();
            this.shield = read.f();
            this.spawnedByCore = read.bool();
            this.stack = TypeIO.readItems(read, this.stack);
            int statuses_LENGTH = read.i();
            this.statuses.clear();

            for (int INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
                StatusEntry statuses_ITEM = TypeIO.readStatus(read);
                if (statuses_ITEM != null) {
                    this.statuses.add(statuses_ITEM);
                }
            }

            this.team = TypeIO.readTeam(read);
            this.type = Vars.content.getByID(ContentType.unit, read.s());
            this.updateBuilding = read.bool();
            this.vel = TypeIO.readVec2(read, this.vel);
            this.x = read.f();
            this.y = read.f();
            this.afterRead();


            int number = read.i();
            for (int i = 0; i < number; i++) {
                idList.add(read.i());
            }
            number = read.i();
            for (int i = 0; i < number; i++) {
                unitAbilities.put(read.str(), read.i());
            }
            level = read.i();
            exp = read.f();
        } else {
            throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'elude'");
        }
    }

    @Override
    public void write(Writes write) {
        write.s(0);
        TypeIO.writeAbilities(write, this.abilities);
        write.f(this.ammo);
        TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        TypeIO.writeTile(write, this.mineTile);
        TypeIO.writeMounts(write, this.mounts);
        write.i(this.plans.size);

        int INDEX;
        for (INDEX = 0; INDEX < this.plans.size; ++INDEX) {
            TypeIO.writePlan(write, this.plans.get(INDEX));
        }

        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);

        for (INDEX = 0; INDEX < this.statuses.size; ++INDEX) {
            TypeIO.writeStatus(write, this.statuses.get(INDEX));
        }

        TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        TypeIO.writeVec2(write, this.vel);
        write.f(this.x);
        write.f(this.y);

        write.i(units.size);
        for (Unit u : units) {
            write.i(u.id);
        }
        write.i(unitAbilities.size());
        for (String s : unitAbilities.keySet()) {
            write.str(s);
            write.i(unitAbilities.get(s));
        }
        write.i(level);
        write.f(exp);
    }

    @Override
    public void update() {
        super.update();
        if (units.size == 0 && idList.size > 0) {
            for (int i : idList) units.add(Groups.unit.getByID(i));
            idList.clear();
        }
        for (Unit u : units) {
            if (!u.within(x, y, u.speed() * 40)) {
                if (u instanceof UnitChainAble uca) {
                    uca.UnderUnit(null);
                    uca.upon(false);
                }
                units.remove(u);
                continue;
            }
            if ((u.dead || u.health() <= 0) || (u instanceof UnitChainAble uca && uca.UnderUnit() != this)) {
                units.remove(u);
                continue;
            }
            if (u instanceof UnitChainAble uca && uca.UnderUnit() == null) uca.UnderUnit(this);
        }
    }

    @Override
    public Map<String, Integer> getMap() {
        return unitAbilities;
    }

    @Override
    public Seq<Unit> getUit() {
        return units;
    }
    @Override
    public int getLevel() {
        return level;
    }
    @Override
    public void setLevel(int l){
        level = l;
    }
    @Override
    public float getExp() {
        return exp;
    }
    @Override
    public void addExp(float exp) {
        this.exp = exp + this.exp;
    }

    @Override
    public int number() {
        int number = 0;
        while (exp > (4 + level) * maxHealth / 10) {
            exp = exp % (4 + level) * maxHealth / 10;
            number++;
        }
        return number;
    }
}
