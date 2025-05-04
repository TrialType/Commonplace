package Commonplace.AI;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.ai.types.GroundAI;
import mindustry.entities.Units;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.world.Tile;

import static mindustry.Vars.state;
import static mindustry.Vars.tilesize;

public class GarrisonAI extends GroundAI {
    protected static final Vec2 targetPos = new Vec2();
    protected static final Vec2 vecOut = new Vec2();
    protected static final boolean[] noFound = {false};

    @Override
    public void updateMovement() {
        Building core = unit.closestEnemyCore();
        if (core == null || unit.dst(core) > unit.range() * 1.5f) {
            core = null;
        } else {
            target = core;
        }

        if (core != null && unit.within(core, unit.range() / 1.3f + core.block.size * tilesize / 2f)) {
            for (var mount : unit.mounts) {
                if (mount.weapon.controllable && mount.weapon.bullet.collidesGround) {
                    mount.target = core;
                }
            }
        }

        if (target == null) {
            if (state.rules.waves && unit.team == state.rules.defaultTeam) {
                Tile spawner = getClosestSpawner();
                if (spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 120f)) {
                    targetPos.set(spawner);
                    move();
                }
            }
        } else {
            targetPos.set(target);
            move();
        }

        if (unit.type.canBoost && unit.elevation > 0.001f && !unit.onSolid()) {
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, unit.type.riseSpeed);
        }

        faceTarget();
    }

    public void move() {
        vecOut.set(target);
        boolean move = Vars.controlPath.getPathPosition(unit, vecOut, targetPos, vecOut, noFound);

        if (move) {
            moveTo(vecOut, 0f, 100f, false, null, targetPos.epsilonEquals(vecOut, 4.1f));
        } else if (unit.dst(target) > unit.range()) {
            target = null;
        }
    }

    @Override
    public Teamc target(float x, float y, float range, boolean air, boolean ground) {
        if (target == null || target.dst(unit) > unit.range() * 1.5f) {
            return Units.closestTarget(unit.team, x, y, range, u -> {
                if (!u.checkTarget(air, ground)) {
                    return false;
                }
                return unit.canPass(u.tileX(), u.tileY()) || unit.dst(u) < unit.range();
            }, t -> ground);
        } else {
            return target;
        }
    }
}
