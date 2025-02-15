package Commonplace.Entities.Unit;

import Commonplace.Entities.UnitType.GroupUnitType;
import Commonplace.Utils.Classes.UnitGroup;
import Commonplace.Utils.Interfaces.Groupmate;

import arc.func.Cons;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.StatusEntry;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.MechUnit;
import mindustry.gen.Sounds;
import mindustry.input.InputHandler;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public class GroupUnit extends MechUnit implements Groupmate {
    public boolean leader = false;

    public UnitGroup group;
    public GroupUnitType groupType;

    public static GroupUnit create() {
        return new GroupUnit();
    }

    @Override
    public int classId() {
        return 103;
    }

    @Override
    public void update() {
        if (leader) {
            group.updateGroup();
        }

        limitVel();
        limitBounds();
        this.updateBuildLogic();
        Floor floor = this.floorOn();
        if (this.isFlying() != this.wasFlying) {
            if (this.wasFlying && this.tileOn() != null) {
                Fx.unitLand.at(this.x, this.y, this.floorOn().isLiquid ? 1.0F : 0.5F, this.tileOn().floor().mapColor);
            }

            this.wasFlying = this.isFlying();
        }

        if (!this.hovering && this.isGrounded() && (this.splashTimer += Mathf.dst(this.deltaX(), this.deltaY())) >= 7.0F + this.hitSize() / 8.0F) {
            floor.walkEffect.at(this.x, this.y, this.hitSize() / 8.0F, floor.mapColor);
            this.splashTimer = 0.0F;
            if (this.emitWalkSound()) {
                floor.walkSound.at(this.x, this.y, Mathf.random(floor.walkSoundPitchMin, floor.walkSoundPitchMax), floor.walkSoundVolume);
            }
        }

        this.updateDrowning();
        this.hitTime -= Time.delta / 9.0F;
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
        this.itemTime = Mathf.lerpDelta(this.itemTime, (float) Mathf.num(this.hasItem()), 0.05F);
        updateWalk();
        updateMine();
        updateStatus();
        this.shieldAlpha -= Time.delta / 15.0F;
        if (this.shieldAlpha < 0.0F) {
            this.shieldAlpha = 0.0F;
        }

        floor = this.floorOn();
        if (this.isGrounded() && !this.type.hovering) {
            this.apply(floor.status, floor.statusDuration);
        }


        if (Vars.net.client() && !this.isLocal() || this.isRemote()) {
            this.interpolate();
        }

        this.type.update(this);
        if (this.wasHealed && this.healTime <= -1.0F) {
            this.healTime = 1.0F;
        }

        this.healTime -= Time.delta / 20.0F;
        this.wasHealed = false;
        if (this.team.isOnlyAI() && Vars.state.isCampaign() && Vars.state.getSector().isCaptured()) {
            this.kill();
        }

        if (!Vars.headless && this.type.loopSound != Sounds.none) {
            Vars.control.sound.loop(this.type.loopSound, this, this.type.loopSoundVolume);
        }

        if (!this.type.supportsEnv(Vars.state.rules.env) && !this.dead) {
            Call.unitEnvDeath(this);
            this.team.data().updateCount(this.type, -1);
        }

        if (Vars.state.rules.unitAmmo && this.ammo < (float) this.type.ammoCapacity - 1.0E-4F) {
            this.resupplyTime += Time.delta;
            if (this.resupplyTime > 10.0F) {
                this.type.ammoType.resupply(this);
                this.resupplyTime = 0.0F;
            }
        }

        Ability[] var14 = this.abilities;
        int index = var14.length;

        for (int accepted = 0; accepted < index; ++accepted) {
            Ability a = var14[accepted];
            a.update(this);
        }

        if (this.trail != null) {
            this.trail.length = this.type.trailLength;
            float offset = this.type.useEngineElevation ? this.elevation : 1.0F;
            float range = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * offset;
            float cx = this.x + Angles.trnsx(this.rotation + 180.0F, range);
            float cy = this.y + Angles.trnsy(this.rotation + 180.0F, range);
            this.trail.update(cx, cy);
        }

        this.drag = this.type.drag * (this.isGrounded() ? this.floorOn().dragMultiplier : 1.0F) * this.dragMultiplier * Vars.state.rules.dragMultiplier;
        if (this.team != Vars.state.rules.waveTeam && Vars.state.hasSpawns() && (!Vars.net.client() || this.isLocal()) && this.hittable()) {
            float offset = Vars.state.rules.dropZoneRadius + this.hitSize / 2.0F + 1.0F;

            for (Tile spawn : Vars.spawner.getSpawns()) {
                if (this.within(spawn.worldx(), spawn.worldy(), offset)) {
                    this.velAddNet(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(1.1F - this.dst(spawn) / offset).scl(0.45F * Time.delta));
                }
            }
        }

        if (this.dead || this.health <= 0.0F) {
            this.drag = 0.01F;
            if (Mathf.chanceDelta(0.1)) {
                Tmp.v1.rnd(Mathf.range(this.hitSize));
                this.type.fallEffect.at(this.x + Tmp.v1.x, this.y + Tmp.v1.y);
            }

            if (Mathf.chanceDelta(0.2)) {
                float offset = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * this.elevation;
                float range = Mathf.range(this.type.engineSize);
                this.type.fallEngineEffect.at(this.x + Angles.trnsx(this.rotation + 180.0F, offset) + Mathf.range(range), this.y + Angles.trnsy(this.rotation + 180.0F, offset) + Mathf.range(range), Mathf.random());
            }

            this.elevation -= this.type.fallSpeed * Time.delta;
            if (this.isGrounded() || this.health <= -this.maxHealth) {
                Call.unitDestroy(this.id);
            }
        }

        Tile tile = this.tileOn();
        floor = this.floorOn();
        if (tile != null && this.isGrounded() && !this.type.hovering) {
            if (tile.build != null) {
                tile.build.unitOn(this);
            }

            if (floor.damageTaken > 0.0F) {
                this.damageContinuous(floor.damageTaken);
            }
        }

        if (tile != null && !this.canPassOn()) {
            if (this.type.canBoost) {
                this.elevation = 1.0F;
            } else if (!Vars.net.client()) {
                this.kill();
            }
        }

        if (!Vars.net.client() && !this.dead) {
            this.controller.updateUnit();
        }

        if (!this.controller.isValidController()) {
            this.resetController();
        }

        if (this.spawnedByCore && !this.isPlayer() && !this.dead) {
            Call.unitDespawn(this);
        }

        WeaponMount[] var20 = this.mounts;
        index = var20.length;

        for (int accepted = 0; accepted < index; ++accepted) {
            WeaponMount mount = var20[accepted];
            mount.weapon.update(this, mount);
        }
    }

    public void limitVel() {
        if (!Vars.net.client() || this.isLocal()) {
            float offset = this.x;
            float range = this.y;
            this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
            if (Mathf.equal(offset, this.x)) {
                this.vel.x = 0.0F;
            }

            if (Mathf.equal(range, this.y)) {
                this.vel.y = 0.0F;
            }

            this.vel.scl(Math.max(1.0F - this.drag * Time.delta, 0.0F));
        }
    }

    public void limitBounds() {
        float cx;
        float cy;
        float dy;
        if (this.type.bounded) {
            float offset = 0.0F;
            float range = 0.0F;
            cx = (float) Vars.world.unitHeight();
            cy = (float) Vars.world.unitWidth();
            if (Vars.state.rules.limitMapArea && !this.team.isAI()) {
                offset = (float) (Vars.state.rules.limitY * 8);
                range = (float) (Vars.state.rules.limitX * 8);
                cx = (float) (Vars.state.rules.limitHeight * 8) + offset;
                cy = (float) (Vars.state.rules.limitWidth * 8) + range;
            }

            if (!Vars.net.client() || this.isLocal()) {
                float dx = 0.0F;
                dy = 0.0F;
                if (this.x < range) {
                    dx += -(this.x - range) / 30.0F;
                }

                if (this.y < offset) {
                    dy += -(this.y - offset) / 30.0F;
                }

                if (this.x > cy) {
                    dx -= (this.x - cy) / 30.0F;
                }

                if (this.y > cx) {
                    dy -= (this.y - cx) / 30.0F;
                }

                this.velAddNet(dx * Time.delta, dy * Time.delta);
            }

            if (this.isGrounded()) {
                this.x = Mathf.clamp(this.x, range, cy - 8.0F);
                this.y = Mathf.clamp(this.y, offset, cx - 8.0F);
            }

            if (this.x < -250.0F + range || this.y < -250.0F + offset || this.x >= cy + 250.0F || this.y >= cx + 250.0F) {
                this.kill();
            }
        }
    }

    public void updateWalk() {
        if (this.walked || Vars.net.client()) {
            float offset = this.deltaLen();
            this.baseRotation = Angles.moveToward(this.baseRotation, this.deltaAngle(), this.type().baseRotateSpeed * Mathf.clamp(offset / this.type().speed / Time.delta) * Time.delta);
            this.walkTime += offset;
            this.walked = false;
        }

        float offset = this.walkExtend(false);
        float range = this.walkExtend(true);
        float cx = range % 1.0F;
        float cy = this.walkExtension;
        if (!Vars.headless && cx < cy && range % 2.0F > 1.0F && !this.isFlying() && !this.inFogTo(Vars.player.team())) {
            int side = -Mathf.sign(offset);
            float dy = this.hitSize / 2.0F * (float) side;
            float length = this.type.mechStride * 1.35F;
            cx = this.x + Angles.trnsx(this.baseRotation, length, dy);
            cy = this.y + Angles.trnsy(this.baseRotation, length, dy);
            if (this.type.stepShake > 0.0F) {
                Effect.shake(this.type.stepShake, this.type.stepShake, cx, cy);
            }

            if (this.type.mechStepParticles) {
                Effect.floorDust(cx, cy, this.hitSize / 8.0F);
            }
        }

        this.walkExtension = cx;
    }

    public void updateMine() {
        if (this.mineTile != null) {
            Building core = this.closestCore();
            Item item = this.getMineResult(this.mineTile);
            if (core != null && item != null && !this.acceptsItem(item) && this.within(core, 220.0F) && !this.offloadImmediately()) {
                int accepted = core.acceptStack(this.item(), this.stack().amount, this);
                if (accepted > 0) {
                    Call.transferItemTo(this, this.item(), accepted, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
                    this.clearItem();
                }
            }

            if ((!Vars.net.client() || this.isLocal()) && !this.validMine(this.mineTile)) {
                this.mineTile = null;
                this.mineTimer = 0.0F;
            } else if (this.mining() && item != null) {
                this.mineTimer += Time.delta * this.type.mineSpeed;
                if (Mathf.chance(0.06 * (double) Time.delta)) {
                    Fx.pulverizeSmall.at(this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), 0.0F, item.color);
                }

                if (this.mineTimer >= 50.0F + (this.type.mineHardnessScaling ? (float) item.hardness * 15.0F : 15.0F)) {
                    this.mineTimer = 0.0F;
                    if (Vars.state.rules.sector != null && this.team() == Vars.state.rules.defaultTeam) {
                        Vars.state.rules.sector.info.handleProduction(item, 1);
                    }

                    if (core != null && this.within(core, 220.0F) && core.acceptStack(item, 1, this) == 1 && this.offloadImmediately()) {
                        if (this.item() == item && !Vars.net.client()) {
                            this.addItem(item);
                        }

                        Call.transferItemTo(this, item, 1, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
                    } else if (this.acceptsItem(item)) {
                        InputHandler.transferItemToUnit(item, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), this);
                    } else {
                        this.mineTile = null;
                        this.mineTimer = 0.0F;
                    }
                }

                if (!Vars.headless) {
                    Vars.control.sound.loop(this.type.mineSound, this, this.type.mineSoundVolume);
                }
            }
        }
    }

    public void updateStatus() {
        this.applied.clear();
        this.speedMultiplier = this.damageMultiplier = this.healthMultiplier = this.reloadMultiplier = this.buildSpeedMultiplier = this.dragMultiplier = 1.0F;
        this.disarmed = false;
        int index;
        if (!this.statuses.isEmpty()) {
            index = 0;

            label338:
            while (true) {
                while (true) {
                    if (index >= this.statuses.size) {
                        break label338;
                    }

                    StatusEntry entry = this.statuses.get(index++);
                    entry.time = Math.max(entry.time - Time.delta, 0.0F);
                    if (entry.effect != null && (!(entry.time <= 0.0F) || entry.effect.permanent)) {
                        this.applied.set(entry.effect.id);
                        this.speedMultiplier *= entry.effect.speedMultiplier;
                        this.healthMultiplier *= entry.effect.healthMultiplier;
                        this.damageMultiplier *= entry.effect.damageMultiplier;
                        this.reloadMultiplier *= entry.effect.reloadMultiplier;
                        this.buildSpeedMultiplier *= entry.effect.buildSpeedMultiplier;
                        this.dragMultiplier *= entry.effect.dragMultiplier;
                        this.disarmed |= entry.effect.disarm;
                        entry.effect.update(this, entry.time);
                    } else {
                        Pools.free(entry);
                        --index;
                        this.statuses.remove(index);
                    }
                }
            }
        }

        if (group != null) {
            damageMultiplier *= group.damageMul;
            damageMultiplier += group.damageAdd;

            healthMultiplier *= group.healthMul;
            healthMultiplier += group.healthAdd;

            reloadMultiplier *= group.reloadMul;
            reloadMultiplier += group.reloadAdd;

            group.abilities.each(a -> a.update(this));
            if (leader) {
                group.leadAbility.each(a -> a.update(this));
            }

            if (group.damageMin > 0 && damageMultiplier < group.damageMin) {
                damageMultiplier = group.damageMin;
            }
            if (group.damageMax > 0 && damageMultiplier > group.damageMax) {
                damageMultiplier = group.damageMax;
            }

            if (group.healthMin > 0 && healthMultiplier < group.healthMin) {
                healthMultiplier = group.healthMin;
            }
            if (group.healthMax > 0 && healthMultiplier > group.healthMax) {
                healthMultiplier = group.healthMax;
            }

            if (group.reloadMin > 0 && reloadMultiplier < group.reloadMin) {
                reloadMultiplier = group.reloadMin;
            }
            if (group.reloadMax > 0 && reloadMultiplier > group.reloadMax) {
                reloadMultiplier = group.reloadMax;
            }
        }
    }

    @Override
    public void setType(UnitType type) {
        super.setType(type);
        groupType = (GroupUnitType) type;
    }

    public boolean canJoin(UnitGroup group) {
        return this.group == null && group.canJoin(groupType);
    }

    @Override
    public short priority() {
        return groupType == null ? 0 : groupType.priority;
    }

    @Override
    public int max() {
        return groupType.max;
    }

    @Override
    public int min() {
        return groupType.min;
    }

    @Override
    public boolean isGroupmate() {
        return added && !dead && health > 0;
    }

    @Override
    public boolean canBeMate(GroupUnitType type) {
        return groupType.teammates.contains(type);
    }

    @Override
    public void out() {
        group = null;
        leader = false;
    }

    @Override
    public void leader(boolean leader) {
        this.leader = leader;
    }

    @Override
    public void group(UnitGroup group) {
        this.group = group;
    }

    @Override
    public Cons<UnitGroup> stronger() {
        return groupType.stronger;
    }
}
