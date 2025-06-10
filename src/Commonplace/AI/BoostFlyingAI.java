package Commonplace.AI;

import Commonplace.Entities.Unit.BoostUnitEntity;
import Commonplace.Entities.UnitType.BoostUnitType;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.ai.types.FlyingAI;
import mindustry.entities.Predict;
import mindustry.entities.Sized;
import mindustry.entities.Units;
import mindustry.gen.Teamc;
import mindustry.mod.ClassMap;
import mindustry.mod.ContentParser;
import mindustry.type.Weapon;
import mindustry.world.blocks.storage.CoreBlock;

import static Commonplace.Entities.Unit.BoostUnitEntity.*;
import static java.lang.Math.*;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class BoostFlyingAI extends FlyingAI {
    private BoostUnitType type = null;
    private BoostUnitEntity u = null;

    //move
    public float last = 0;
    public int dse = 75;

    //for circleTarget
    public float orx;
    public float ory;
    public Teamc lastCircleTarget;
    public int order = 3;

    @Override
    public void updateUnit() {
        if (type != null && u != null) {
            if (useFallback() && (fallback != null || (fallback = fallback()) != null)) {
                fallback.unit(unit);
                fallback.updateUnit();
                return;
            }

            if (!u.first && !u.changing) {
                updateVisuals();
                updateTargeting();
            }
            if (!u.boosting && !u.changing) {
                updateMovement();
            }
        } else {
            init();
        }
    }

    @Override
    public void updateMovement() {
        unloadPayloads();
        float ux = unit.x;
        float uy = unit.y;
        if (!u.first) {
            updateTarget();
            if (unit.type.circleTarget) {
                if (target != null) {
                    circleShoot(30.0f);
                } else {
                    unit.rotation = unit.rotation + 14;
                }
            } else if (target != null) {
                moveTo(target, unit.range() * 0.3f);
                unit.lookAt(target);
                if (unit.dst(target) < type.boostLength * type.boostDuration / 2 &&
                        Angles.angleDist(unit.angleTo(target), unit.rotation) < 15 &&
                        u.timer.get(BoostReload, type.boostReload)) {
                    u.boosting = true;
                    u.target = u.angleTo(target);
                    u.timer.reset(Boost, 0);
                    if (type.boostDelayEffect != null) {
                        type.boostDelayEffect.at(ux, uy, unit.rotation, unit);
                    }
                }
            }
        } else {
            if (unit.hitTime > 0 || Units.closestTarget(unit.team, ux, uy, type.searchRange) != null) {
                type.changeEffect.at(ux, uy, unit.rotation, unit);
                u.changing = true;
                u.timer.reset(Change, 0);
            } else {
                u.rotation += 4;
                float x = Mathf.cosDeg(last) * unit.speed() + ux, y = Mathf.sinDeg(last) * unit.speed() + uy;
                if (x < 0 || x > world.width() * tilesize || y < 0 || y > world.height() * tilesize || timer.get(timerTarget2, 600)) {
                    CoreBlock.CoreBuild core = unit.closestEnemyCore();
                    if (core != null) {
                        last = u.angleTo(core) + dse;
                        dse = 360 - dse;
                    }
                } else {
                    unit.vel.trns(last, type.speed1);
                }
            }
        }
    }

    @Override
    public void init() {
        if (unit.type instanceof BoostUnitType) {
            type = (BoostUnitType) unit.type;
        }
        if (unit instanceof BoostUnitEntity) {
            u = (BoostUnitEntity) unit;
        }
    }

    public void circleShoot(float circleLength) {
        float ux = unit.x;
        float uy = unit.y;
        float tx = target.x();
        float ty = target.y();
        float sx = tx - ux;
        float sy = ty - uy;
        if (lastCircleTarget != target) {
            orx = sx;
            ory = sy;
            order = 3;
        }
        lastCircleTarget = target;
        if (order == 3 && unit.within(target, circleLength)) {
            order = 2;
        } else if (order == 2 && !unit.within(target, circleLength)) {
            order = 1;
        }
        if (order == 1) {
            float angle = Angles.angle(tx, ty, ux, uy) + 5;
            vec.set((float) (sx + cos(toRadians(angle))), (float) (sy + sin(toRadians(angle))));
            orx = sx;
            ory = sy;
            order = 3;
        } else if (order == 2) {
            vec.set(orx, ory);
            vec.setLength(unit.speed());
        } else {
            vec.set(sx, sy);
            vec.setLength(unit.speed());
        }
        unit.moveAt(vec);
    }

    public void updateWeapons() {
        float rotation = unit.rotation - 90;
        boolean ret = retarget();

        if (ret && unit.team.isAI()) {
            target = findMainTarget(unit.x, unit.y, unit.range(), unit.type.targetAir, unit.type.targetGround);
        } else if (ret && (target == null || !(target instanceof CoreBlock.CoreBuild))) {
            updateTarget();
        }

        noTargetTime += Time.delta;

        if (invalid(target)) {
            target = null;
        } else {
            noTargetTime = 0f;
        }

        unit.isShooting = false;

        for (var mount : unit.mounts) {
            Weapon weapon = mount.weapon;
            float wrange = weapon.range();

            //let uncontrollable weapons do their own thing
            if (!weapon.controllable || weapon.noAttack) continue;

            if (!weapon.aiControllable) {
                mount.rotate = false;
                continue;
            }

            float mountX = unit.x + Angles.trnsx(rotation, weapon.x, weapon.y),
                    mountY = unit.y + Angles.trnsy(rotation, weapon.x, weapon.y);

            if (unit.type.singleTarget) {
                mount.target = target;
            } else {
                if (ret) {
                    mount.target = findTarget(mountX, mountY, wrange, weapon.bullet.collidesAir, weapon.bullet.collidesGround);
                }

                if (checkTarget(mount.target, mountX, mountY, wrange)) {
                    mount.target = null;
                }
            }

            boolean shoot = false;

            if (mount.target != null) {
                shoot = mount.target.within(mountX, mountY, wrange + (mount.target instanceof Sized s ? s.hitSize() / 2f : 0f)) && shouldShoot();

                Vec2 to = Predict.intercept(unit, mount.target, weapon.bullet.speed);
                mount.aimX = to.x;
                mount.aimY = to.y;
            }

            unit.isShooting |= (mount.shoot = mount.rotate = shoot);

            if (mount.target == null && !shoot && !Angles.within(mount.rotation, mount.weapon.baseRotation, 0.01f) && noTargetTime >= rotateBackTimer) {
                mount.rotate = true;
                Tmp.v1.trns(unit.rotation + mount.weapon.baseRotation, 5f);
                mount.aimX = mountX + Tmp.v1.x;
                mount.aimY = mountY + Tmp.v1.y;
            }

            if (shoot) {
                unit.aimX = mount.aimX;
                unit.aimY = mount.aimY;
            }
        }
    }

    public void updateTarget() {
        target = Units.closestTarget(unit.team, unit.x, unit.y, unit.range() * 2, u -> !u.spawnedByCore(), b -> true);
        if (target == null) {
            target = unit.closestEnemyCore();
        }
    }
}
