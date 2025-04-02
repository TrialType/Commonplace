package Commonplace.Entities.Unit;

import Commonplace.Loader.DefaultContent.Units2;
import Commonplace.Entities.UnitType.BoostUnitType;
import Commonplace.Utils.Classes.PhysicsWorldChanger;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.async.AsyncProcess;
import mindustry.async.PhysicsProcess;
import mindustry.ctype.ContentType;
import mindustry.entities.Units;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static Commonplace.Utils.Classes.Damage2.percentDamage;
import static mindustry.Vars.asyncCore;

public class BoostUnitEntity extends UnitEntity {
    public static final int Change = 0, Boost = 1, BoostReload = 2;

    public final static Seq<BoostUnitEntity> crazy = new Seq<>();
    public static final Seq<Tile> tiles = Seq.with();
    private final Map<Unit, Float> unitMap = new HashMap<>();
    private final Map<Building, Float> buildingMap = new HashMap<>();
    public static Changer bc = new Changer();
    public static PhysicsWorldChanger physicsWorldChanger;

    public final Interval timer = new Interval(3);
    public boolean boosting = false;
    public boolean changing = false;
    public boolean first = true;
    public float target = -1;

    protected BoostUnitEntity() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
    }

    public static BoostUnitEntity create() {
        return new BoostUnitEntity();
    }

    public static void change() {
        try {
            Field file1 = PhysicsProcess.class.getDeclaredField("physics");
            file1.setAccessible(true);
            for (AsyncProcess process : asyncCore.processes) {
                if (process instanceof PhysicsProcess) {
                    PhysicsProcess.PhysicsWorld pw = (PhysicsProcess.PhysicsWorld) file1.get(process);
                    if (!(pw instanceof PhysicsWorldChanger)) {
                        Field field2 = PhysicsProcess.PhysicsWorld.class.getDeclaredField("bodies");
                        field2.setAccessible(true);
                        physicsWorldChanger = new PhysicsWorldChanger(Vars.world.getQuadBounds(new Rect()));
                        //noinspection unchecked
                        physicsWorldChanger.bodies = (Seq<PhysicsProcess.PhysicsWorld.PhysicsBody>) field2.get(pw);
                        file1.set(process, physicsWorldChanger);
                    }
                }
            }
            if (bc != null) {
                asyncCore.processes.add(bc);
                bc = null;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        if (crazy.indexOf(this) < 0 && type == Units2.crazy) {
            crazy.add(this);
            change();
        }
        if (!team.isAI() || Units2.boss.contains(type)) {
            first = false;
        }
        if (changing && type instanceof BoostUnitType t) {
            if (timer.get(Change, t.exchangeTime)) {
                first = false;
                changing = false;
                if (t.number > 0) {
                    int[] counter = new int[]{t.number};
                    Units.nearby(team, x, y, range(), u -> {
                        if (counter[0] > 0 && u instanceof BoostUnitEntity b && !b.first && !b.changing) {
                            b.changing = true;
                            b.timer.reset(Change, 0);
                            if (b.type instanceof BoostUnitType ty) {
                                ty.changeEffect.at(b);
                            }
                            counter[0]--;
                        }
                    });
                }
            } else {
                vel.setZero();
            }
        }
        if (boosting) {
            vel.setZero();
        }

        super.update();

        unitMap.replaceAll((u, v) -> v + Time.delta);
        buildingMap.replaceAll((u, v) -> v + Time.delta);
        if (boosting && type instanceof BoostUnitType t) {
            if (timer.get(Boost, t.boostDuration + t.boostDelay)) {
                boosting = false;
                timer.reset(BoostReload, 0);
            } else if (timer.check(Boost, t.boostDelay)) {
                float damage = t.hitDamage;
                float changeHel = t.hitChangeHel;
                float percent = t.hitPercent;
                boolean firstPercent = t.hitFirstPercent;
                float reload = t.hitReload;
                float length = t.boostLength;
                rotation = target;
                vel.trns(rotation, length);
                Units.nearbyEnemies(team, x, y, length, u -> {
                    float timer = unitMap.computeIfAbsent(u, f -> reload);
                    if (timer >= reload) {
                        unitMap.put(u, 0F);
                        float angle = Angles.angleDist(rotation, angleTo(u));
                        if (angle <= 90) {
                            if (Mathf.sinDeg(angle) * dst(u) <= (hitSize + u.hitSize) * 0.5f) {
                                percentDamage(u, percent, damage, firstPercent, changeHel);
                            }
                        }
                    }
                });
                Units.nearbyBuildings(x, y, length, b -> {
                    if (b.team != team) {
                        float timer = buildingMap.computeIfAbsent(b, f -> reload);
                        if (timer >= reload) {
                            buildingMap.put(b, 0F);
                            final float[] angle = new float[1];
                            b.tile.getLinkedTilesAs(b.block, tiles);
                            if (tiles.contains(tile -> (angle[0] = Angles.angleDist(rotation, angleTo(b))) <= 90 &&
                                    Mathf.sinDeg(angle[0]) * dst(tile) <= hitSize / 2)) {
                                percentDamage(b, percent, damage, firstPercent, changeHel);
                            }
                        }
                    }
                });
                lastX = x;
                lastY = y;
                x += vel.x;
                y += vel.y;
                t.boostEffect.at(lastX, lastY, rotation, Pal.accent, new Vec2(x, y));
            }
        } else if (type == Units2.crazy && type instanceof BoostUnitType t) {
            Units.nearbyEnemies(team, x, y, hitSize, u -> {
                float timer = unitMap.computeIfAbsent(u, f -> t.hitReload);
                if (timer >= t.hitReload) {
                    unitMap.put(u, 0F);
                    percentDamage(u, t.hitPercent, t.hitDamage, t.hitFirstPercent, t.hitChangeHel);
                }
            });
            Units.nearbyBuildings(x, y, hitSize, b -> {
                if (b.team != team) {
                    float timer = buildingMap.computeIfAbsent(b, f -> t.hitReload);
                    if (timer >= t.hitReload) {
                        buildingMap.put(b, 0F);
                        percentDamage(b, t.hitPercent, t.hitDamage, t.hitFirstPercent, t.hitChangeHel);
                    }
                }
            });
        }
    }

    @Override
    public int classId() {
        return 113;
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        first = read.bool();
        changing = read.bool();
        boosting = read.bool();
        timer.reset(0, read.f());
        timer.reset(1, read.f());
        timer.reset(2, read.f());
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        write.bool(first);
        write.bool(changing);
        write.bool(boosting);
        write.f(timer.getTime(0));
        write.f(timer.getTime(1));
        write.f(timer.getTime(2));
    }

    @Override
    public float speed() {
        if (first) {
            if (type instanceof BoostUnitType eut) {
                return eut.speed1 * speedMultiplier;
            }
        }
        return super.speed();
    }

    public static class Changer implements AsyncProcess {
        @Override
        public void begin() {
            Seq<BoostUnitEntity> us = new Seq<>();
            for (BoostUnitEntity eu : crazy) {
                if (eu.dead || eu.health <= 0) {
                    us.add(eu);
                }
            }

            crazy.removeAll(us);

            for (BoostUnitEntity u : crazy) {
                if (u.target >= 0) {
                    u.physref.body.layer = 3;
                }
            }
        }
    }
}