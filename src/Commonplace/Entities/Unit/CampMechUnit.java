package Commonplace.Entities.Unit;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.entities.Effect;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.Mechc;
import mindustry.io.TypeIO;

import java.nio.FloatBuffer;

public class CampMechUnit extends CampUnit implements Mechc {
    private transient float rotation_LAST_;
    private transient float rotation_TARGET_;
    private transient float x_LAST_;
    private transient float x_TARGET_;
    private transient float y_LAST_;
    private transient float y_TARGET_;
    private transient float baseRotation_LAST_;
    private transient float baseRotation_TARGET_;

    public float baseRotation;
    public transient float walkExtension;
    public transient float walkTime;
    protected transient boolean walked;

    protected CampMechUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>(4);
    }

    public static CampMechUnit create() {
        return new CampMechUnit();
    }

    @Override
    public int classId() {
        return 90;
    }

    public void read(Reads read) {
        read.s();
        int statuses_LENGTH;
        int INDEX;
        StatusEntry statuses_ITEM;
        TypeIO.readAbilities(read, this.abilities);
        this.ammo = read.f();
        this.baseRotation = read.f();
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
        statuses_LENGTH = read.i();
        this.statuses.clear();

        for (INDEX = 0; INDEX < statuses_LENGTH; ++INDEX) {
            statuses_ITEM = TypeIO.readStatus(read);
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
    }

    public void write(Writes write) {
        write.s(7);
        TypeIO.writeAbilities(write, this.abilities);
        write.f(this.ammo);
        write.f(this.baseRotation);
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

        System.out.println("write");
    }

    @Override
    public void approach(Vec2 vector) {
        if (!vector.isZero(0.001F)) {
            this.walked = true;
        }

        this.vel.approachDelta(vector, this.type.accel * this.speed());
    }

    @Override
    public void moveAt(Vec2 vector, float acceleration) {
        if (!vector.isZero()) {
            this.walked = true;
        }

        Vec2 t = tmp1.set(vector);
        tmp2.set(t).sub(this.vel).limit(acceleration * vector.len() * Time.delta);
        this.vel.add(tmp2);
    }

    @Override
    public void updateWalk() {
        if (this.walked || Vars.net.client()) {
            float bot = this.deltaLen();
            this.baseRotation = Angles.moveToward(this.baseRotation, this.deltaAngle(), this.type().baseRotateSpeed * Mathf.clamp(bot / this.type().speed / Time.delta) * Time.delta);
            this.walkTime += bot;
            this.walked = false;
        }

        float bot = this.walkExtend(false);
        float left = this.walkExtend(true);
        float offset = left % 1.0F;
        float range = this.walkExtension;
        int side;
        float cy;
        if (!Vars.headless && offset < range && left % 2.0F > 1.0F && !this.isFlying() && !this.inFogTo(Vars.player.team())) {
            side = -Mathf.sign(bot);
            cy = this.hitSize / 2.0F * (float) side;
            float length = this.type.mechStride * 1.35F;
            float cx = this.x + Angles.trnsx(this.baseRotation, length, cy);
            cy = this.y + Angles.trnsy(this.baseRotation, length, cy);
            if (this.type.stepShake > 0.0F) {
                Effect.shake(this.type.stepShake, this.type.stepShake, cx, cy);
            }

            if (this.type.mechStepParticles) {
                Effect.floorDust(cx, cy, this.hitSize / 8.0F);
            }
        }

        this.walkExtension = offset;
    }

    @Override
    public void rotateMove(Vec2 vec) {
        this.moveAt(Tmp.v2.trns(this.baseRotation, vec.len()));
        if (!vec.isZero()) {
            this.baseRotation = Angles.moveToward(this.baseRotation, vec.angle(), this.type.rotateSpeed * Math.max(Time.delta, 1.0F));
        }
    }

    @Override
    public float baseRotation() {
        return baseRotation;
    }

    @Override
    public float walkExtend(boolean scaled) {
        float raw = this.walkTime % (this.type.mechStride * 4.0F);
        if (scaled) {
            return raw / this.type.mechStride;
        } else {
            if (raw > this.type.mechStride * 3.0F) {
                raw -= this.type.mechStride * 4.0F;
            } else if (raw > this.type.mechStride * 2.0F) {
                raw = this.type.mechStride * 2.0F - raw;
            } else if (raw > this.type.mechStride) {
                raw = this.type.mechStride * 2.0F - raw;
            }

            return raw;
        }
    }

    @Override

    public float walkExtension() {
        return this.walkExtension;
    }

    @Override
    public float walkTime() {
        return walkTime;
    }

    @Override
    public void baseRotation(float v) {
        baseRotation = v;
    }

    @Override
    public void walkExtension(float v) {
        walkExtension = v;
    }

    @Override
    public void walkTime(float v) {
        walkTime = v;
    }

    public void interpolate() {
        if (this.lastUpdated != 0L && this.updateSpacing != 0L) {
            float timeSinceUpdate = (float) Time.timeSinceMillis(this.lastUpdated);
            float alpha = Math.min(timeSinceUpdate / (float) this.updateSpacing, 2.0F);
            this.baseRotation = Mathf.slerp(this.baseRotation_LAST_, this.baseRotation_TARGET_, alpha);
            this.rotation = Mathf.slerp(this.rotation_LAST_, this.rotation_TARGET_, alpha);
            this.x = Mathf.lerp(this.x_LAST_, this.x_TARGET_, alpha);
            this.y = Mathf.lerp(this.y_LAST_, this.y_TARGET_, alpha);
        } else if (this.lastUpdated != 0L) {
            this.baseRotation = this.baseRotation_TARGET_;
            this.rotation = this.rotation_TARGET_;
            this.x = this.x_TARGET_;
            this.y = this.y_TARGET_;
        }
    }

    public void readSync(Reads read) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
        boolean islocal = this.isLocal();
        TypeIO.readAbilities(read, this.abilities);
        this.ammo = read.f();
        if (!islocal) {
            this.baseRotation_LAST_ = this.baseRotation;
            this.baseRotation_TARGET_ = read.f();
        } else {
            read.f();
            this.baseRotation_LAST_ = this.baseRotation;
            this.baseRotation_TARGET_ = this.baseRotation;
        }

        this.controller = TypeIO.readController(read, this.controller);
        if (!islocal) {
            this.elevation = read.f();
        } else {
            read.f();
        }

        this.flag = read.d();
        this.health = read.f();
        this.isShooting = read.bool();
        if (!islocal) {
            this.mineTile = TypeIO.readTile(read);
        } else {
            TypeIO.readTile(read);
        }

        if (!islocal) {
            TypeIO.readMounts(read, this.mounts);
        } else {
            TypeIO.readMounts(read);
        }

        if (!islocal) {
            this.plans = TypeIO.readPlansQueue(read);
        } else {
            TypeIO.readPlansQueue(read);
        }

        if (!islocal) {
            this.rotation_LAST_ = this.rotation;
            this.rotation_TARGET_ = read.f();
        } else {
            read.f();
            this.rotation_LAST_ = this.rotation;
            this.rotation_TARGET_ = this.rotation;
        }

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
        if (!islocal) {
            this.updateBuilding = read.bool();
        } else {
            read.bool();
        }

        if (!islocal) {
            this.vel = TypeIO.readVec2(read, this.vel);
        } else {
            TypeIO.readVec2(read);
        }

        if (!islocal) {
            this.x_LAST_ = this.x;
            this.x_TARGET_ = read.f();
        } else {
            read.f();
            this.x_LAST_ = this.x;
            this.x_TARGET_ = this.x;
        }

        if (!islocal) {
            this.y_LAST_ = this.y;
            this.y_TARGET_ = read.f();
        } else {
            read.f();
            this.y_LAST_ = this.y;
            this.y_TARGET_ = this.y;
        }

        this.afterSync();
    }

    public void readSyncManual(FloatBuffer buffer) {
        if (this.lastUpdated != 0L) {
            this.updateSpacing = Time.timeSinceMillis(this.lastUpdated);
        }

        this.lastUpdated = Time.millis();
        this.baseRotation_LAST_ = this.baseRotation;
        this.baseRotation_TARGET_ = buffer.get();
        this.rotation_LAST_ = this.rotation;
        this.rotation_TARGET_ = buffer.get();
        this.x_LAST_ = this.x;
        this.x_TARGET_ = buffer.get();
        this.y_LAST_ = this.y;
        this.y_TARGET_ = buffer.get();
    }

    public void snapInterpolation() {
        this.updateSpacing = 16L;
        this.lastUpdated = Time.millis();
        this.baseRotation_LAST_ = this.baseRotation;
        this.baseRotation_TARGET_ = this.baseRotation;
        this.rotation_LAST_ = this.rotation;
        this.rotation_TARGET_ = this.rotation;
        this.x_LAST_ = this.x;
        this.x_TARGET_ = this.x;
        this.y_LAST_ = this.y;
        this.y_TARGET_ = this.y;
    }

    public void snapSync() {
        this.updateSpacing = 16L;
        this.lastUpdated = Time.millis();
        this.baseRotation_LAST_ = this.baseRotation_TARGET_;
        this.baseRotation = this.baseRotation_TARGET_;
        this.rotation_LAST_ = this.rotation_TARGET_;
        this.rotation = this.rotation_TARGET_;
        this.x_LAST_ = this.x_TARGET_;
        this.x = this.x_TARGET_;
        this.y_LAST_ = this.y_TARGET_;
        this.y = this.y_TARGET_;
    }

    public void writeSync(Writes write) {
        TypeIO.writeAbilities(write, this.abilities);
        write.f(this.ammo);
        write.f(this.baseRotation);
        TypeIO.writeController(write, this.controller);
        write.f(this.elevation);
        write.d(this.flag);
        write.f(this.health);
        write.bool(this.isShooting);
        TypeIO.writeTile(write, this.mineTile);
        TypeIO.writeMounts(write, this.mounts);
        TypeIO.writePlansQueueNet(write, this.plans);
        write.f(this.rotation);
        write.f(this.shield);
        write.bool(this.spawnedByCore);
        TypeIO.writeItems(write, this.stack);
        write.i(this.statuses.size);

        for (int INDEX = 0; INDEX < this.statuses.size; ++INDEX) {
            TypeIO.writeStatus(write, (StatusEntry) this.statuses.get(INDEX));
        }

        TypeIO.writeTeam(write, this.team);
        write.s(this.type.id);
        write.bool(this.updateBuilding);
        TypeIO.writeVec2(write, this.vel);
        write.f(this.x);
        write.f(this.y);
    }

    public void writeSyncManual(FloatBuffer buffer) {
        buffer.put(this.baseRotation);
        buffer.put(this.rotation);
        buffer.put(this.x);
        buffer.put(this.y);
    }
}
