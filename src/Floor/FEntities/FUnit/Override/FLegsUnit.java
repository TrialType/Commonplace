package Floor.FEntities.FUnit.Override;

import Floor.FTools.interfaces.FUnitUpGrade;
import Floor.FTools.classes.UnitUpGrade;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.ctype.ContentType;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Leg;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.entities.units.StatusEntry;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.LegsUnit;
import mindustry.gen.Sounds;
import mindustry.graphics.InverseKinematics;
import mindustry.input.InputHandler;
import mindustry.io.TypeIO;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import java.util.Random;

public class FLegsUnit extends LegsUnit implements FUnitUpGrade {
    protected int damageLevel = 0;
    protected int speedLevel = 0;
    protected int healthLevel = 0;
    protected int reloadLevel = 0;
    protected int againLevel = 0;
    protected int shieldLevel = 0;
    protected ShieldRegenFieldAbility sfa = null;

    public int level = 0;
    public float exp = 0;
    protected FLegsUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.curMoveOffset = new Vec2();
        this.legs = new Leg[0];
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
    }

    public static FLegsUnit create() {
        return new FLegsUnit();
    }
    @Override
    public int classId(){
        return 103;
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        level = read.i();
        exp = read.f();

        damageLevel = read.i();
        speedLevel = read.i();
        reloadLevel = read.i();
        healthLevel = read.i();
        againLevel = read.i();
        shieldLevel = read.i();
    }

    @Override
    public void write(Writes write) {
        super.write(write);

        write.i(level);
        write.f(exp);
        write.i(damageLevel);
        write.i(speedLevel);
        write.i(reloadLevel);
        write.i(healthLevel);
        write.i(againLevel);
        write.i(shieldLevel);
    }

    @Override
    public void update(){
        if (shieldLevel > 0 && sfa == null) {
            sfa = new ShieldRegenFieldAbility(maxHealth / 100 * shieldLevel,
                    maxHealth * shieldLevel / 10, 120, 60);
        }

        float offset;
        float range;
        if (!Vars.net.client() || this.isLocal()) {
            offset = this.x;
            range = this.y;
            this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
            if (Mathf.equal(offset, this.x)) {
                this.vel.x = 0.0F;
            }

            if (Mathf.equal(range, this.y)) {
                this.vel.y = 0.0F;
            }

            this.vel.scl(Math.max(1.0F - this.drag * Time.delta, 0.0F));
        }

        float cx;
        float cy;
        if (this.type.bounded) {
            offset = 0.0F;
            range = 0.0F;
            cx = (float)Vars.world.unitHeight();
            cy = (float)Vars.world.unitWidth();
            if (Vars.state.rules.limitMapArea && !this.team.isAI()) {
                offset = (float)(Vars.state.rules.limitY * 8);
                range = (float)(Vars.state.rules.limitX * 8);
                cx = (float)(Vars.state.rules.limitHeight * 8) + offset;
                cy = (float)(Vars.state.rules.limitWidth * 8) + range;
            }

            if (!Vars.net.client() || this.isLocal()) {
                float dx = 0.0F;
                float dy = 0.0F;
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
        this.itemTime = Mathf.lerpDelta(this.itemTime, (float)Mathf.num(this.hasItem()), 0.05F);
        if (Mathf.dst(this.deltaX(), this.deltaY()) > 0.001F) {
            this.baseRotation = Angles.moveToward(this.baseRotation, Mathf.angle(this.deltaX(), this.deltaY()), this.type.rotateSpeed);
        }

        if (this.type.lockLegBase) {
            this.baseRotation = this.rotation;
        }

        offset = this.type.legLength;
        if (this.legs.length != this.type.legCount) {
            this.resetLegs();
        }

        range = this.type.legSpeed;
        int div = Math.max(this.legs.length / this.type.legGroupSize, 2);
        this.moveSpace = offset / 1.6F / ((float)div / 2.0F) * this.type.legMoveSpace;
        this.totalLength += this.type.legContinuousMove ? this.type.speed * this.speedMultiplier * Time.delta : Mathf.dst(this.deltaX(), this.deltaY());
        cy = this.moveSpace * 0.85F * this.type.legForwardScl;
        boolean moving = this.moving();
        Vec2 moveOffset = !moving ? Tmp.v4.setZero() : Tmp.v4.trns(Angles.angle(this.deltaX(), this.deltaY()), cy);
        moveOffset = this.curMoveOffset.lerpDelta(moveOffset, 0.1F);
        this.lastDeepFloor = null;
        int deeps = 0;

        for(int i = 0; i < this.legs.length; ++i) {
            float dstRot = this.legAngle(i);
            Vec2 baseOffset = this.legOffset(Tmp.v5, i).add(this.x, this.y);
            Leg l = this.legs[i];
            l.joint.sub(baseOffset).clampLength(this.type.legMinLength * offset / 2.0F, this.type.legMaxLength * offset / 2.0F).add(baseOffset);
            l.base.sub(baseOffset).clampLength(this.type.legMinLength * offset, this.type.legMaxLength * offset).add(baseOffset);
            float stageF = (this.totalLength + (float)i * this.type.legPairOffset) / this.moveSpace;
            int stage = (int)stageF;
            int group = stage % div;
            boolean move = i % div == group;
            boolean side = i < this.legs.length / 2;
            boolean backLeg = Math.abs((float)i + 0.5F - (float)this.legs.length / 2.0F) <= 0.501F;
            if (backLeg && this.type.flipBackLegs) {
                side = !side;
            }

            if (this.type.flipLegSide) {
                side = !side;
            }

            l.moving = move;
            l.stage = moving ? stageF % 1.0F : Mathf.lerpDelta(l.stage, 0.0F, 0.1F);
            floor = Vars.world.floorWorld(l.base.x, l.base.y);
            if (floor.isDeep()) {
                ++deeps;
                this.lastDeepFloor = floor;
            }

            if (l.group != group) {
                if (!move && (moving || !this.type.legContinuousMove) && i % div == l.group) {
                    if (!Vars.headless && !this.inFogTo(Vars.player.team())) {
                        if (floor.isLiquid) {
                            floor.walkEffect.at(l.base.x, l.base.y, this.type.rippleScale, floor.mapColor);
                            floor.walkSound.at(this.x, this.y, 1.0F, floor.walkSoundVolume);
                        } else {
                            Fx.unitLandSmall.at(l.base.x, l.base.y, this.type.rippleScale, floor.mapColor);
                        }

                        if (this.type.stepShake > 0.0F) {
                            Effect.shake(this.type.stepShake, this.type.stepShake, l.base);
                        }
                    }

                    if (this.type.legSplashDamage > 0.0F) {
                        Damage.damage(this.team, l.base.x, l.base.y, this.type.legSplashRange, this.type.legSplashDamage * Vars.state.rules.unitDamage(this.team), false, true);
                    }
                }

                l.group = group;
            }

            Vec2 legDest = Tmp.v1.trns(dstRot, offset * this.type.legLengthScl).add(baseOffset).add(moveOffset);
            Vec2 jointDest = Tmp.v2;
            InverseKinematics.solve(offset / 2.0F, offset / 2.0F, Tmp.v6.set(l.base).sub(baseOffset), side, jointDest);
            jointDest.add(baseOffset);
            Tmp.v6.set(baseOffset).lerp(l.base, 0.5F);
            if (move) {
                float moveFract = stageF % 1.0F;
                l.base.lerpDelta(legDest, moveFract);
                l.joint.lerpDelta(jointDest, moveFract / 2.0F);
            }

            l.joint.lerpDelta(jointDest, range / 4.0F);
            l.joint.sub(baseOffset).clampLength(this.type.legMinLength * offset / 2.0F, this.type.legMaxLength * offset / 2.0F).add(baseOffset);
            l.base.sub(baseOffset).clampLength(this.type.legMinLength * offset, this.type.legMaxLength * offset).add(baseOffset);
        }

        if (deeps != this.legs.length || !this.floorOn().isDeep()) {
            this.lastDeepFloor = null;
        }

        if (this.mineTile != null) {
            Building core = this.closestCore();
            Item item = this.getMineResult(this.mineTile);
            if (core != null && item != null && !this.acceptsItem(item) && this.within(core, 220.0F) && !this.offloadImmediately()) {
                div = core.acceptStack(this.item(), this.stack().amount, this);
                if (div > 0) {
                    Call.transferItemTo(this, this.item(), div, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
                    this.clearItem();
                }
            }

            if ((!Vars.net.client() || this.isLocal()) && !this.validMine(this.mineTile)) {
                this.mineTile = null;
                this.mineTimer = 0.0F;
            } else if (this.mining() && item != null) {
                this.mineTimer += Time.delta * this.type.mineSpeed;
                if (Mathf.chance(0.06 * (double)Time.delta)) {
                    Fx.pulverizeSmall.at(this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), 0.0F, item.color);
                }

                if (this.mineTimer >= 50.0F + (this.type.mineHardnessScaling ? (float)item.hardness * 15.0F : 15.0F)) {
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

        this.shieldAlpha -= Time.delta / 15.0F;
        if (this.shieldAlpha < 0.0F) {
            this.shieldAlpha = 0.0F;
        }

        floor = this.floorOn();
        if (this.isGrounded() && !this.type.hovering) {
            this.apply(floor.status, floor.statusDuration);
        }

        this.applied.clear();
        this.speedMultiplier = this.damageMultiplier = this.healthMultiplier = this.reloadMultiplier = this.buildSpeedMultiplier = this.dragMultiplier = 1.0F;
        this.disarmed = false;
        int index;
        if (!this.statuses.isEmpty()) {
            index = 0;

            label416:
            while(true) {
                while(true) {
                    if (index >= this.statuses.size) {
                        break label416;
                    }

                    StatusEntry entry = (StatusEntry)this.statuses.get(index++);
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
        speedMultiplier *= (1 + speedLevel * 0.2f);
        damageMultiplier *= (1 + damageLevel * 0.2f);
        reloadMultiplier *= (1 + reloadLevel * 0.2f);
        heal(maxHealth * healthLevel * 0.0001f);
        if (sfa != null) {
            sfa.update(this);
        }

        if (level > 60) {
            int boost2 = level - 60;
            float lBoost = (float) Math.pow(1.01f, boost2);
            healthMultiplier *= lBoost;
            if (speedMultiplier * lBoost >= 10) {
                float hb = 1 + (lBoost * speedMultiplier / 10);
                speedMultiplier = 10;
                healthMultiplier *= hb;
            } else {
                speedMultiplier *= lBoost;
            }
            damageMultiplier *= lBoost;
            reloadMultiplier *= lBoost;
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

        if (Vars.state.rules.unitAmmo && this.ammo < (float)this.type.ammoCapacity - 1.0E-4F) {
            this.resupplyTime += Time.delta;
            if (this.resupplyTime > 10.0F) {
                this.type.ammoType.resupply(this);
                this.resupplyTime = 0.0F;
            }
        }

        Ability[] var25 = this.abilities;
        index = var25.length;

        for(div = 0; div < index; ++div) {
            Ability a = var25[div];
            a.update(this);
        }

        if (this.trail != null) {
            this.trail.length = this.type.trailLength;
            offset = this.type.useEngineElevation ? this.elevation : 1.0F;
            range = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * offset;
            cx = this.x + Angles.trnsx(this.rotation + 180.0F, range);
            cy = this.y + Angles.trnsy(this.rotation + 180.0F, range);
            this.trail.update(cx, cy);
        }

        this.drag = this.type.drag * (this.isGrounded() ? this.floorOn().dragMultiplier : 1.0F) * this.dragMultiplier * Vars.state.rules.dragMultiplier;
        if (this.team != Vars.state.rules.waveTeam && Vars.state.hasSpawns() && (!Vars.net.client() || this.isLocal()) && this.hittable()) {
            offset = Vars.state.rules.dropZoneRadius + this.hitSize / 2.0F + 1.0F;

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
                offset = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * this.elevation;
                range = Mathf.range(this.type.engineSize);
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

        WeaponMount[] var31 = this.mounts;
        index = var31.length;

        for(div = 0; div < index; ++div) {
            WeaponMount mount = var31[div];
            mount.weapon.update(this, mount);
        }
    }

    @Override
    public void kill() {
        if ((new Random()).nextInt(10) + 1 <= againLevel) {
            FLegsUnit fu = (FLegsUnit) type.create(team);
            fu.x(x);
            fu.y(y);
            fu.rotation(rotation);
            if (level >= 120) {
                UnitUpGrade.getPower(fu, 0, false, true);
                fu.setLevel(level / 2);
            } else {
                fu.setDamageLevel(damageLevel / 2);
                fu.setHealthLevel(healthLevel / 2);
                fu.setSpeedLevel(speedLevel / 2);
                fu.setShieldLevel(shieldLevel / 2);
                fu.setReloadLevel(reloadLevel / 2);
                fu.setLevel(damageLevel / 2 + healthLevel / 2 + speedLevel / 2 + shieldLevel / 2 + reloadLevel / 2);
            }
            if(shieldLevel >= 2){
                fu.sfa = new ShieldRegenFieldAbility(maxHealth / 200 * shieldLevel,
                        maxHealth * shieldLevel / 20, 120, 60);
            }
            fu.health(maxHealth / 10 * againLevel);
            fu.add();
        }
        super.kill();
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
            exp = exp - (4 + level) * maxHealth / 10;
            level++;
            number++;
        }
        return number;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public void setDamageLevel(int damageLevel) {
        this.damageLevel = damageLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public void setHealthLevel(int healthLevel) {
        this.healthLevel = healthLevel;
    }

    public int getReloadLevel() {
        return reloadLevel;
    }

    public void setReloadLevel(int reloadLevel) {
        this.reloadLevel = reloadLevel;
    }

    public int getAgainLevel() {
        return againLevel;
    }

    public void setAgainLevel(int againLevel) {
        this.againLevel = againLevel;
    }

    public int getShieldLevel() {
        return shieldLevel;
    }

    public void setShieldLevel(int shieldLevel) {
        this.shieldLevel = shieldLevel;
    }
    @Override
    public void sfa(int level) {
        sfa = new ShieldRegenFieldAbility(maxHealth / 100 * shieldLevel,
                maxHealth * shieldLevel / 10, 120, 60);
    }
    public int baseLevel() {
        return damageLevel + shieldLevel + speedLevel + healthLevel + reloadLevel + againLevel;
    }
}
