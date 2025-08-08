package Commonplace.Entities.Unit;

import Commonplace.Entities.UnitType.UnitType2;
import Commonplace.Entities.Weapon.Weapon2;
import Commonplace.Utils.Classes.Camp;
import Commonplace.Utils.Interfaces.Camper;
import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.scene.ui.layout.Table;
import arc.struct.Bits;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Structs;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.ai.types.LogicAI;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.core.World;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.EntityCollisions;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.*;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.io.TypeIO;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.ExplosionShield;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;

import java.util.Iterator;

public abstract class CampUnit extends Unit implements Camper {
    public UnitType2 type2;
    public transient float lastHealth = 0;
    public transient float healthBalance = 0;

    public transient float valueR;
    public transient float changeR;
    public transient float valueOR;
    public transient boolean ovrR;

    public transient float penetrationTimer = 0;

    protected UnitController controller;
    protected static final Vec2 tmp1 = new Vec2();
    protected static final Vec2 tmp2 = new Vec2();
    protected transient boolean added;
    protected transient Bits applied;
    protected transient float buildCounter;
    protected transient boolean isRotate;
    protected transient BuildPlan lastActive;
    protected transient int lastSize;
    protected transient float resupplyTime;
    protected Seq<StatusEntry> statuses;
    protected transient boolean wasFlying;
    protected transient boolean wasHealed;
    protected transient boolean wasPlayer;

    public void resetCamp() {
        changeR = 1;
        valueR = -1;
        valueOR = -1;
        ovrR = false;
    }

    @Nullable
    public Building buildOn() {
        return Vars.world.buildWorld(this.x, this.y);
    }

    @Nullable
    public Player getPlayer() {
        return this.isPlayer() ? (Player) this.controller : null;
    }

    @Nullable
    public String getControllerName() {
        if (this.isPlayer()) {
            return this.getPlayer().coloredName();
        } else {
            UnitController var2 = this.controller;
            if (var2 instanceof LogicAI ai) {
                if (ai.controller != null) {
                    return ai.controller.lastAccessed;
                }
            }

            return null;
        }
    }

    @Nullable
    public EntityCollisions.SolidPred solidity() {
        return isFlying() || penetrationTimer > 0 ? null : EntityCollisions::solid;
    }

    @Nullable
    public BuildPlan buildPlan() {
        return this.plans.size == 0 ? null : this.plans.first();
    }

    @Nullable
    public Item getMineResult(Tile tile) {
        if (tile == null) {
            return null;
        } else {
            Item result;
            if (this.type.mineFloor && tile.block() == Blocks.air) {
                result = tile.drop();
            } else {
                if (!this.type.mineWalls) {
                    return null;
                }

                result = tile.wallDrop();
            }

            return this.canMine(result) ? result : null;
        }
    }

    @Nullable
    public Tile tileOn() {
        return Vars.world.tileWorld(this.x, this.y);
    }

    @Nullable
    public Floor drownFloor() {
        return this.floorOn();
    }

    @Nullable
    public CoreBlock.CoreBuild closestCore() {
        return Vars.state.teams.closestCore(this.x, this.y, this.team);
    }

    @Nullable
    public CoreBlock.CoreBuild closestEnemyCore() {
        return Vars.state.teams.closestEnemyCore(this.x, this.y, this.team);
    }

    @Nullable
    public CoreBlock.CoreBuild core() {
        return this.team.core();
    }

    @Override
    public boolean isRotate() {
        return this.isRotate;
    }

    @Override
    public void read(Reads read) {
        short REV = read.s();
        int statuses_LENGTH;
        int INDEX;
        StatusEntry statuses_ITEM;
        if (REV == 0) {
            this.ammo = read.f();
            read.f();
            this.controller = TypeIO.readController(read, this.controller);
            read.bool();
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            TypeIO.readMounts(read, this.mounts);
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
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 1) {
            this.ammo = read.f();
            read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.health = read.f();
            this.isShooting = read.bool();
            TypeIO.readMounts(read, this.mounts);
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
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 2) {
            this.ammo = read.f();
            read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            TypeIO.readMounts(read, this.mounts);
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
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 3) {
            this.ammo = read.f();
            read.f();
            this.controller = TypeIO.readController(read, this.controller);
            this.elevation = read.f();
            this.flag = read.d();
            this.health = read.f();
            this.isShooting = read.bool();
            this.mineTile = TypeIO.readTile(read);
            TypeIO.readMounts(read, this.mounts);
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
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 4) {
            this.ammo = read.f();
            read.f();
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
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 5) {
            this.ammo = read.f();
            read.f();
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
            this.x = read.f();
            this.y = read.f();
        } else if (REV == 6) {
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
        } else {
            if (REV != 7) {
                throw new IllegalArgumentException("Unknown revision '" + REV + "' for entity type 'flare'");
            }

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
        }

        this.afterRead();
    }

    @Override
    public void write(Writes write) {
        write.s(7);
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
    }

    @Override
    public <T extends Entityc> T self() {
        //noinspection unchecked
        return (T) this;
    }

    @Override
    public <T> T as() {
        //noinspection unchecked
        return (T) this;
    }

    @Override
    public Color statusColor() {
        if (this.statuses.size == 0) {
            return Tmp.c1.set(Color.white);
        } else {
            float r = 1.0F;
            float g = 1.0F;
            float b = 1.0F;
            float total = 0.0F;

            float intensity;
            for (Iterator<StatusEntry> var5 = this.statuses.iterator(); var5.hasNext(); total += intensity) {
                StatusEntry entry = var5.next();
                intensity = entry.time < 10.0F ? entry.time / 10.0F : 1.0F;
                r += entry.effect.color.r * intensity;
                g += entry.effect.color.g * intensity;
                b += entry.effect.color.b * intensity;
            }

            float count = (float) this.statuses.size + total;
            return Tmp.c1.set(r / count, g / count, b / count, 1.0F);
        }
    }

    @Override
    public TextureRegion icon() {
        return this.type.uiIcon;
    }

    @Override
    public Bits statusBits() {
        return this.applied;
    }

    @Override
    public boolean acceptsItem(Item item) {
        return !this.hasItem() || item == this.stack.item && this.stack.amount + 1 <= this.itemCapacity();
    }

    @Override
    public boolean activelyBuilding() {
        if (this.isBuilding()) {
            BuildPlan plan = this.buildPlan();
            if (!Vars.state.isEditor() && plan != null && !this.within(plan, Vars.state.rules.infiniteResources ? Float.MAX_VALUE : this.type.buildRange)) {
                return false;
            }
        }

        return this.isBuilding() && this.updateBuilding;
    }

    @Override
    public boolean canBuild() {
        return this.type.buildSpeed > 0.0F && this.buildSpeedMultiplier > 0.0F;
    }

    @Override
    public boolean canDrown() {
        return this.isGrounded() && this.type.canDrown;
    }

    @Override
    public boolean canLand() {
        return !this.onSolid() && Units.count(this.x, this.y, this.physicSize(), (f) -> f != this && f.isGrounded()) == 0;
    }

    @Override
    public boolean canMine() {
        return this.type.mineSpeed * Vars.state.rules.unitMineSpeed(this.team()) > 0.0F && this.type.mineTier >= 0;
    }

    @Override
    public boolean canMine(Item item) {
        if (item == null) {
            return false;
        } else {
            return this.type.mineTier >= item.hardness;
        }
    }

    @Override
    public boolean canPass(int tileX, int tileY) {
        EntityCollisions.SolidPred s = this.solidity();
        return s == null || !s.solid(tileX, tileY);
    }

    @Override
    public boolean canPassOn() {
        return this.canPass(this.tileX(), this.tileY());
    }

    @Override
    public boolean canShoot() {
        return !this.disarmed && (!this.type.canBoost || !this.isFlying());
    }

    @Override
    public boolean canTarget(Teamc other) {
        label27:
        {
            if (other != null) {
                if (other instanceof Unit u) {
                    if (u.checkTarget(this.type.targetAir, this.type.targetGround)) {
                        break label27;
                    }
                } else if (other instanceof Building) {
                    if (this.type.targetGround) {
                        break label27;
                    }
                }
            }

            return false;
        }
        return true;
    }

    @Override
    public boolean cheating() {
        return this.team.rules().cheat;
    }

    @Override
    public boolean checkTarget(boolean targetAir, boolean targetGround) {
        return this.isGrounded() && targetGround || this.isFlying() && targetAir;
    }

    @Override
    public boolean collides(Hitboxc other) {
        return this.hittable();
    }

    @Override
    public boolean damaged() {
        return this.health < this.maxHealth - 0.001F;
    }

    @Override
    public boolean displayable() {
        return this.type.hoverable;
    }

    @Override
    public boolean hasEffect(StatusEffect effect) {
        return this.applied.get(effect.id);
    }

    @Override
    public boolean hasItem() {
        return this.stack.amount > 0;
    }

    @Override
    public boolean hasWeapons() {
        return this.type.hasWeapons();
    }

    @Override
    public boolean hittable() {
        return this.type.hittable(this);
    }

    @Override
    public boolean ignoreSolids() {
        return false;
    }

    @Override
    public boolean inFogTo(Team viewer) {
        if (this.team != viewer && Vars.state.rules.fog) {
            if (this.hitSize <= 16.0F) {
                return !Vars.fogControl.isVisible(viewer, this.x, this.y);
            } else {
                float trn = this.hitSize / 2.0F;
                Point2[] var3 = Geometry.d8;
                int var4 = var3.length;

                //noinspection ForLoopReplaceableByForEach
                for (int var5 = 0; var5 < var4; ++var5) {
                    Point2 p = var3[var5];
                    if (Vars.fogControl.isVisible(viewer, this.x + (float) p.x * trn, this.y + (float) p.y * trn)) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean inRange(Position other) {
        return this.within(other, this.type.range);
    }

    @Override
    public boolean isAI() {
        return this.controller instanceof AIController;
    }

    @Override
    public boolean isAdded() {
        return this.added;
    }

    @Override
    public boolean isBoss() {
        return this.hasEffect(StatusEffects.boss);
    }

    @Override
    public boolean isBuilding() {
        return this.plans.size != 0;
    }

    // mindustryX display
    public boolean allowCommand() {
        return isCommandable();
    }

    @Override
    public boolean isCommandable() {
        return this.controller instanceof CommandAI;
    }

    @Override
    public boolean isEnemy() {
        return this.type.isEnemy;
    }

    @Override
    public boolean isFlying() {
        return this.elevation >= 0.09F;
    }

    @Override
    public boolean isGrounded() {
        return this.elevation < 0.001F;
    }

    @Override
    public boolean isImmune(StatusEffect effect) {
        return this.type.immunities.contains(effect);
    }

    @Override
    public boolean isLocal() {
        if (this != Vars.player.unit()) {
            return controller() == Vars.player;
        }
        return true;
    }

    @Override
    public boolean isMissile() {
        return this instanceof TimedKillc;
    }

    @Override
    public boolean isPathImpassable(int tileX, int tileY) {
        return !this.type.flying && Vars.world.tiles.in(tileX, tileY) && this.type.pathCost.getCost(this.team.id, Vars.pathfinder.get(tileX, tileY)) == -1;
    }

    @Override
    public boolean isPlayer() {
        return this.controller instanceof Player;
    }

    @Override
    public boolean isRemote() {
        return this.isPlayer() && !this.isLocal();
    }

    @Override
    public boolean isSyncHidden(Player player) {
        return !this.isShooting() && this.inFogTo(player.team());
    }

    @Override
    public boolean isValid() {
        return !this.dead && this.isAdded();
    }

    @Override
    public boolean mining() {
        return this.mineTile != null && !this.activelyBuilding();
    }

    @Override
    public boolean moving() {
        return !this.vel.isZero(0.01F);
    }

    @Override
    public boolean offloadImmediately() {
        return this.isPlayer();
    }

    @Override
    public boolean onSolid() {
        Tile tile = this.tileOn();
        return tile == null || tile.solid();
    }

    @Override
    public boolean playerControllable() {
        return this.type.playerControllable;
    }

    @Override
    public boolean serialize() {
        return true;
    }

    @Override
    public boolean shouldSkip(BuildPlan plan, Building core) {
        if (!Vars.state.rules.infiniteResources && !this.team.rules().infiniteResources && !plan.breaking && core != null && !plan.isRotation(this.team) && (!this.isBuilding() || this.within(this.plans.last(), this.type.buildRange))) {
            return plan.stuck && !core.items.has(plan.block.requirements) || Structs.contains(plan.block.requirements, (i) -> !core.items.has(i.item, Math.min(i.amount, 15)) && Mathf.round((float) i.amount * Vars.state.rules.buildCostMultiplier) > 0) && !plan.initialized;
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldUpdateController() {
        return true;
    }

    @Override
    public boolean targetable(Team targeter) {
        return this.type.targetable(this, targeter);
    }

    @Override
    public boolean validMine(Tile tile) {
        return this.validMine(tile, true);
    }

    @Override
    public boolean validMine(Tile tile, boolean checkDst) {
        if (tile == null) {
            return false;
        } else if (checkDst && !this.within(tile.worldx(), tile.worldy(), this.type.mineRange)) {
            return false;
        } else {
            return this.getMineResult(tile) != null;
        }
    }

    @Override
    public double sense(Content content) {
        if (content == this.stack().item) {
            return this.stack().amount;
        } else {
            double var10000;
            Payloadc pay;
            if (content instanceof UnitType u) {
                if (this instanceof Payloadc) {
                    pay = (Payloadc) this;
                    var10000 = (pay.payloads().isEmpty() ? 0 : pay.payloads().count((p) -> {
                        if (p instanceof UnitPayload up) {
                            return up.unit.type == u;
                        }
                        return false;
                    }));
                } else {
                    var10000 = 0.0;
                }

                return var10000;
            } else if (content instanceof Block b) {
                if (this instanceof Payloadc) {
                    pay = (Payloadc) this;
                    var10000 = pay.payloads().isEmpty() ? 0 : pay.payloads().count((p) -> {
                        if (p instanceof BuildPayload bp) {
                            return bp.build.block == b;
                        }
                        return false;
                    });
                } else {
                    var10000 = 0.0;
                }

                return var10000;
            } else {
                return Double.NaN;
            }
        }
    }

    @Override
    public double sense(LAccess sensor) {
        double var10000;
        Payloadc pay;
        UnitController var3;
        float var4;
        Player player;
        switch (sensor) {
            case totalItems:
                var10000 = this.stack().amount;
                break;
            case itemCapacity:
                var10000 = this.type.itemCapacity;
                break;
            case rotation:
                var10000 = this.rotation;
                break;
            case health:
                var10000 = this.health;
                break;
            case shield:
                var10000 = this.shield;
                break;
            case maxHealth:
                var10000 = this.maxHealth;
                break;
            case ammo:
                var10000 = !Vars.state.rules.unitAmmo ? (double) this.type.ammoCapacity : (double) this.ammo;
                break;
            case ammoCapacity:
                var10000 = this.type.ammoCapacity;
                break;
            case x:
                var10000 = World.conv(this.x);
                break;
            case y:
                var10000 = World.conv(this.y);
                break;
            case velocityX:
                var10000 = (this.vel.x * 60.0F / 8.0F);
                break;
            case velocityY:
                var10000 = (this.vel.y * 60.0F / 8.0F);
                break;
            case dead:
                var10000 = !this.dead && this.isAdded() ? 0.0 : 1.0;
                break;
            case team:
                var10000 = this.team.id;
                break;
            case shooting:
                var10000 = this.isShooting() ? 1.0 : 0.0;
                break;
            case boosting:
                var10000 = this.type.canBoost && this.isFlying() ? 1.0 : 0.0;
                break;
            case range:
                var10000 = (this.range() / 8.0F);
                break;
            case shootX:
                var10000 = World.conv(this.aimX());
                break;
            case shootY:
                var10000 = World.conv(this.aimY());
                break;
            case cameraX:
                var3 = this.controller;
                if (var3 instanceof Player) {
                    player = (Player) var3;
                    var4 = World.conv(player.con == null ? Core.camera.position.x : player.con.viewX);
                } else {
                    var4 = 0.0F;
                }

                var10000 = var4;
                break;
            case cameraY:
                var3 = this.controller;
                if (var3 instanceof Player) {
                    player = (Player) var3;
                    var4 = World.conv(player.con == null ? Core.camera.position.y : player.con.viewY);
                } else {
                    var4 = 0.0F;
                }

                var10000 = var4;
                break;
            case cameraWidth:
                var3 = this.controller;
                if (var3 instanceof Player) {
                    player = (Player) var3;
                    var4 = World.conv(player.con == null ? Core.camera.width : player.con.viewWidth);
                } else {
                    var4 = 0.0F;
                }

                var10000 = var4;
                break;
            case cameraHeight:
                var3 = this.controller;
                if (var3 instanceof Player) {
                    player = (Player) var3;
                    var4 = World.conv(player.con == null ? Core.camera.height : player.con.viewHeight);
                } else {
                    var4 = 0.0F;
                }

                var10000 = var4;
                break;
            case mining:
                var10000 = this.mining() ? 1.0 : 0.0;
                break;
            case mineX:
                var10000 = this.mining() ? (double) this.mineTile.x : -1.0;
                break;
            case mineY:
                var10000 = this.mining() ? (double) this.mineTile.y : -1.0;
                break;
            case armor:
                var10000 = this.armorOverride >= 0.0F ? (double) this.armorOverride : (double) this.armor;
                break;
            case flag:
                var10000 = this.flag;
                break;
            case speed:
                var10000 = (this.type.speed * 60.0F / 8.0F * this.speedMultiplier);
                break;
            case controlled:
                byte var7;
                if (!this.isValid()) {
                    var7 = 0;
                } else if (this.controller instanceof LogicAI) {
                    var7 = 1;
                } else if (this.controller instanceof Player) {
                    var7 = 2;
                } else {
                    label150:
                    {
                        var3 = this.controller;
                        if (var3 instanceof CommandAI command) {
                            if (command.hasCommand()) {
                                var7 = 3;
                                break label150;
                            }
                        }

                        var7 = 0;
                    }
                }

                var10000 = var7;
                break;
            case payloadCount:
                if (this instanceof Payloadc) {
                    pay = (Payloadc) this;
                    var10000 = pay.payloads().size;
                } else {
                    var10000 = 0.0;
                }
                break;
            case totalPayload:
                if (this instanceof Payloadc) {
                    pay = (Payloadc) this;
                    var10000 = (pay.payloadUsed() / 64.0F);
                } else {
                    var10000 = 0.0;
                }
                break;
            case payloadCapacity:
                var10000 = this.type.payloadCapacity / 64.0F;
                break;
            case size:
                var10000 = this.hitSize / 8.0F;
                break;
            case color:
                var10000 = Color.toDoubleBits(this.team.color.r, this.team.color.g, this.team.color.b, 1.0F);
                break;
            default:
                var10000 = Double.NaN;
        }

        return var10000;
    }

    @Override
    public float ammof() {
        return this.ammo / (float) this.type.ammoCapacity;
    }

    @Override
    public float bounds() {
        return this.hitSize * 2.0F;
    }

    @Override
    public float clipSize() {
        if (this.isBuilding()) {
            return Vars.state.rules.infiniteResources ? Float.MAX_VALUE : Math.max(this.type.clipSize, (float) this.type.region.width) + this.type.buildRange + 32.0F;
        } else {
            return this.mining() ? this.type.clipSize + this.type.mineRange : this.type.clipSize;
        }
    }

    @Override
    public float deltaAngle() {
        return Mathf.angle(this.deltaX, this.deltaY);
    }

    @Override
    public float deltaLen() {
        return Mathf.len(this.deltaX, this.deltaY);
    }

    @Override
    public float floorSpeedMultiplier() {
        Floor on = !this.isFlying() && !this.type.hovering ? this.floorOn() : Blocks.air.asFloor();
        return on.speedMultiplier * this.speedMultiplier;
    }

    @Override
    public float getDuration(StatusEffect effect) {
        StatusEntry entry = this.statuses.find((e) -> e.effect == effect);
        return entry == null ? 0.0F : entry.time;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float healthf() {
        return this.health / this.maxHealth;
    }

    @Override
    public float hitSize() {
        return this.hitSize;
    }

    @Override
    public float mass() {
        return this.hitSize * this.hitSize * 3.1415927F;
    }

    @Override
    public float physicSize() {
        return this.hitSize * 0.7F;
    }

    @Override
    public float prefRotation() {
        if (this.activelyBuilding() && this.type.rotateToBuilding) {
            return this.angleTo(this.buildPlan());
        } else if (this.mineTile != null) {
            return this.angleTo(this.mineTile);
        } else {
            return this.moving() && this.type.omniMovement ? this.vel().angle() : this.rotation;
        }
    }

    @Override
    public float range() {
        return camp().lastRangeOverride ? changeR : type.maxRange * changeR;
    }

    @Override
    public float speed() {
        float strafePenalty = !this.isGrounded() && this.isPlayer() ? Mathf.lerp(1.0F, this.type.strafePenalty, Angles.angleDist(this.vel().angle(), this.rotation) / 180.0F) : 1.0F;
        float boost = Mathf.lerp(1.0F, this.type.canBoost ? this.type.boostMultiplier : 1.0F, this.elevation);
        return this.type.speed * strafePenalty * boost * this.floorSpeedMultiplier();
    }

    @Override
    public int cap() {
        return Units.getCap(this.team);
    }

    @Override
    public int collisionLayer() {
        return this.type.allowLegStep && this.type.legPhysicsLayer ? 1 : (this.isGrounded() ? 0 : 2);
    }

    @Override
    public int count() {
        return this.team.data().countType(this.type);
    }

    @Override
    public int itemCapacity() {
        return this.type.itemCapacity;
    }

    @Override
    public int maxAccepted(Item item) {
        return this.stack.item != item && this.stack.amount > 0 ? 0 : this.itemCapacity() - this.stack.amount;
    }

    @Override
    public int tileX() {
        return World.toTile(this.x);
    }

    @Override
    public int tileY() {
        return World.toTile(this.y);
    }

    @Override
    public Object senseObject(LAccess sensor) {
        Object var10000;
        UnitController var8;
        switch (sensor) {
            case type:
                var10000 = this.type;
                break;
            case name:
                var8 = this.controller;
                if (var8 instanceof Player p) {
                    var10000 = p.name;
                } else {
                    var10000 = null;
                }
                break;
            case firstItem:
                var10000 = this.stack().amount == 0 ? null : this.item();
                break;
            case controller:
                if (!this.isValid()) {
                    var10000 = null;
                } else {
                    var8 = this.controller;
                    if (var8 instanceof LogicAI log) {
                        var10000 = log.controller;
                    } else {
                        var10000 = this;
                    }
                }
                break;
            case payloadType:
                if (this instanceof Payloadc pay) {
                    if (pay.payloads().isEmpty()) {
                        var10000 = null;
                    } else {
                        Object var5 = pay.payloads().peek();
                        if (var5 instanceof UnitPayload p1) {
                            var10000 = p1.unit.type;
                        } else {
                            var5 = pay.payloads().peek();
                            if (var5 instanceof BuildPayload p2) {
                                var10000 = p2.block();
                            } else {
                                var10000 = null;
                            }
                        }
                    }
                } else {
                    var10000 = null;
                }
                break;
            default:
                var10000 = noSensed;
        }

        return var10000;
    }

    @Override
    public String toString() {
        return "Unit#" + this.id() + ":" + this.type + " (" + this.x + ", " + this.y + ")";
    }

    @Override
    public CommandAI command() {
        UnitController var2 = this.controller;
        if (var2 instanceof CommandAI) {
            return (CommandAI) var2;
        } else {
            throw new IllegalArgumentException("Unit cannot be commanded - check isCommandable() first.");
        }
    }

    @Override
    public StatusEntry applyDynamicStatus() {
        StatusEntry entry;
        if (this.hasEffect(StatusEffects.dynamic)) {
            entry = this.statuses.find((s) -> s.effect.dynamic);
            if (entry != null) {
                return entry;
            }
        }

        entry = Pools.obtain(StatusEntry.class, StatusEntry::new);
        entry.set(StatusEffects.dynamic, Float.POSITIVE_INFINITY);
        this.statuses.add(entry);
        this.applied.set(StatusEffects.dynamic.id);
        entry.effect.applied(this, entry.time, false);
        return entry;
    }

    @Override
    public UnitController controller() {
        return this.controller;
    }

    @Override
    public Item item() {
        return this.stack.item;
    }

    @Override
    public Block blockOn() {
        Tile tile = this.tileOn();
        return tile == null ? Blocks.air : tile.block();
    }

    @Override
    public Floor floorOn() {
        Tile tile = this.tileOn();
        return tile != null && tile.block() == Blocks.air ? tile.floor() : (Floor) Blocks.air;
    }

    @Override
    public void add() {
        if (!this.added) {
            this.index__all = Groups.all.addIndex(this);
            this.index__unit = Groups.unit.addIndex(this);
            this.index__sync = Groups.sync.addIndex(this);
            this.index__draw = Groups.draw.addIndex(this);
            this.added = true;
            this.updateLastPosition();
            this.team.data().updateCount(this.type, 1);
            if (this.type.useUnitCap && this.count() > this.cap() && !this.spawnedByCore && !this.dead && !Vars.state.rules.editor) {
                Call.unitCapDeath(this);
                this.team.data().updateCount(this.type, -1);
            }

            if (type instanceof UnitType2 t2) {
                this.type2 = t2;
                camp().units.add(this);
            }
        }
    }

    @Override
    public void addBuild(BuildPlan place) {
        this.addBuild(place, true);
    }

    @Override
    public void addBuild(BuildPlan place, boolean tail) {
        if (this.canBuild()) {
            BuildPlan replace = null;
            Iterator<BuildPlan> var4 = this.plans.iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (var4.hasNext()) {
                BuildPlan plan = var4.next();
                if (plan.x == place.x && plan.y == place.y) {
                    replace = plan;
                    break;
                }
            }

            if (replace != null) {
                this.plans.remove(replace);
            }

            Tile tile = Vars.world.tile(place.x, place.y);
            if (tile != null) {
                Building var6 = tile.build;
                if (var6 instanceof ConstructBlock.ConstructBuild cons) {
                    place.progress = cons.progress;
                }
            }

            if (tail) {
                this.plans.addLast(place);
            } else {
                this.plans.addFirst(place);
            }

        }
    }

    @Override
    public void addItem(Item item) {
        this.addItem(item, 1);
    }

    @Override
    public void addItem(Item item, int amount) {
        this.stack.amount = this.stack.item == item ? this.stack.amount + amount : amount;
        this.stack.item = item;
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
    }

    @Override
    public void afterRead() {
        if (this.plans == null) {
            this.plans = new Queue<>(1);
        }

        this.updateLastPosition();
        this.setType(this.type);
        this.controller.unit(this);
        UnitController var2 = this.controller;
        if (var2 instanceof AIController ai) {
            if (ai.keepState()) {
                return;
            }
        }

        this.controller(this.type.createController(this));
    }

    @Override
    public void afterReadAll() {
        this.controller.afterRead(this);
    }

    @Override
    public void afterSync() {
        this.setType(this.type);
        this.controller.unit(this);
    }

    @Override
    public void aim(Position pos) {
        this.aim(pos.getX(), pos.getY());
    }

    @Override
    public void aim(float x, float y) {
        Tmp.v1.set(x, y).sub(this.x, this.y);
        if (Tmp.v1.len() < this.type.aimDst) {
            Tmp.v1.setLength(this.type.aimDst);
        }

        x = Tmp.v1.x + this.x;
        y = Tmp.v1.y + this.y;
        WeaponMount[] var3 = this.mounts;
        int var4 = var3.length;

        //noinspection ForLoopReplaceableByForEach
        for (int var5 = 0; var5 < var4; ++var5) {
            WeaponMount mount = var3[var5];
            if (mount.weapon.controllable) {
                mount.aimX = x;
                mount.aimY = y;
            }
        }

        this.aimX = x;
        this.aimY = y;
    }

    @Override
    public void aimLook(Position pos) {
        this.aim(pos);
        this.lookAt(pos);
    }

    @Override
    public void aimLook(float x, float y) {
        this.aim(x, y);
        this.lookAt(x, y);
    }

    @Override
    public void apply(StatusEffect effect) {
        this.apply(effect, 1.0F);
    }

    @Override
    public void apply(StatusEffect effect, float duration) {
        if (effect != StatusEffects.none && effect != null && !this.isImmune(effect)) {
            if (Vars.state.isCampaign()) {
                effect.unlock();
            }

            if (this.statuses.size > 0) {
                for (int i = 0; i < this.statuses.size; ++i) {
                    StatusEntry entry = this.statuses.get(i);
                    if (entry.effect == effect) {
                        entry.time = Math.max(entry.time, duration);
                        effect.applied(this, entry.time, true);
                        return;
                    }

                    if (entry.effect.applyTransition(this, effect, entry, duration)) {
                        return;
                    }
                }
            }

            if (!effect.reactive) {
                StatusEntry entry = Pools.obtain(StatusEntry.class, StatusEntry::new);
                entry.set(effect, duration);
                this.applied.set(effect.id);
                this.statuses.add(entry);
                effect.applied(this, duration, false);
            }

        }
    }

    @Override
    public void approach(Vec2 vector) {
        this.vel.approachDelta(vector, this.type.accel * this.speed());
    }

    @Override
    public void beforeWrite() {
        camp().units.remove(this);
    }

    @Override
    public void clampHealth() {
        this.health = Math.min(this.health, this.maxHealth);
        if (Float.isNaN(this.health)) {
            this.health = 0.0F;
        }

    }

    @Override
    public void clearBuilding() {
        this.plans.clear();
    }

    @Override
    public void clearItem() {
        this.stack.amount = 0;
    }

    @Override
    public void clearStatuses() {
        this.statuses.each((e) -> e.effect.onRemoved(this));
        this.statuses.clear();
    }

    @Override
    public void collision(Hitboxc other, float x, float y) {
        if (other instanceof Bullet bullet) {
            this.controller.hit(bullet);
        }

    }

    @Override
    public void controlWeapons(boolean rotate, boolean shoot) {
        WeaponMount[] var3 = this.mounts;
        int var4 = var3.length;

        //noinspection ForLoopReplaceableByForEach
        for (int var5 = 0; var5 < var4; ++var5) {
            WeaponMount mount = var3[var5];
            if (mount.weapon.controllable) {
                mount.rotate = rotate;
                mount.shoot = shoot;
            }
        }

        this.isRotate = rotate;
        this.isShooting = shoot;
    }

    @Override
    public void controlWeapons(boolean rotateShoot) {
        this.controlWeapons(rotateShoot, rotateShoot);
    }

    @Override
    public void controller(UnitController next) {
        this.controller = next;
        if (this.controller.unit() != this) {
            this.controller.unit(this);
        }

    }

    @Override
    public void damage(float amount) {
        this.rawDamage(Damage.applyArmor(amount, this.armorOverride >= 0.0F ? this.armorOverride : this.armor) / this.healthMultiplier / Vars.state.rules.unitHealth(this.team));
    }

    @Override
    public void damage(float amount, boolean withEffect) {
        float pre = this.hitTime;
        this.damage(amount);
        if (!withEffect) {
            this.hitTime = pre;
        }

    }

    @Override
    public void damageContinuous(float amount) {
        this.damage(amount * Time.delta, this.hitTime <= -1.0F);
    }

    @Override
    public void damageContinuousPierce(float amount) {
        this.damagePierce(amount * Time.delta, this.hitTime <= -11.0F);
    }

    @Override
    public void damagePierce(float amount) {
        this.damagePierce(amount, true);
    }

    @Override
    public void damagePierce(float amount, boolean withEffect) {
        float pre = this.hitTime;
        this.rawDamage(amount / this.healthMultiplier / Vars.state.rules.unitHealth(this.team));
        if (!withEffect) {
            this.hitTime = pre;
        }

    }

    @Override
    public void destroy() {
        if (this.isAdded() && this.type.killable) {
            float explosiveness = 2.0F + this.item().explosiveness * (float) this.stack().amount * 1.53F;
            float flammability = this.item().flammability * (float) this.stack().amount / 1.9F;
            float power = this.item().charge * Mathf.pow((float) this.stack().amount, 1.11F) * 160.0F;
            if (!this.spawnedByCore) {
                Damage.dynamicExplosion(this.x, this.y, flammability, explosiveness, power, (this.bounds() + this.type.legLength / 1.7F) / 2.0F, Vars.state.rules.damageExplosions && Vars.state.rules.unitCrashDamage(this.team) > 0.0F, this.item().flammability > 1.0F, this.team, this.type.deathExplosionEffect);
            } else {
                this.type.deathExplosionEffect.at(this.x, this.y, this.bounds() / 2.0F / 8.0F);
            }

            float shake = this.type.deathShake < 0.0F ? this.hitSize / 3.0F : this.type.deathShake;
            if (this.type.createScorch) {
                Effect.scorch(this.x, this.y, (int) (this.hitSize / 5.0F));
            }

            Effect.shake(shake, shake, this);
            this.type.deathSound.at(this);
            Events.fire(new EventType.UnitDestroyEvent(this));
            if (explosiveness > 7.0F && (this.isLocal() || this.wasPlayer)) {
                Events.fire(EventType.Trigger.suicideBomb);
            }

            WeaponMount[] var5 = this.mounts;
            int var6 = var5.length;

            int var7;
            for (var7 = 0; var7 < var6; ++var7) {
                WeaponMount mount = var5[var7];
                if (mount.weapon.shootOnDeath && (!mount.weapon.bullet.killShooter || mount.totalShots <= 0)) {
                    mount.reload = 0.0F;
                    mount.shoot = true;
                    mount.weapon.update(this, mount);
                }
            }

            float range;
            if (this.type.flying && !this.spawnedByCore && this.type.createWreck && Vars.state.rules.unitCrashDamage(this.team) > 0.0F) {
                Seq<Building> shields = Vars.indexer.getEnemy(this.team, BlockFlag.shield);
                range = Mathf.pow(this.hitSize, 0.75F) * this.type.crashDamageMultiplier * 5.0F * Vars.state.rules.unitCrashDamage(this.team);
                float finalRange = range;
                if (shields.isEmpty() || !shields.contains((b) -> {
                    if (b instanceof ExplosionShield s) {
                        return s.absorbExplosion(this.x, this.y, finalRange);
                    }
                    return false;
                })) {
                    Damage.damage(this.team, this.x, this.y, Mathf.pow(this.hitSize, 0.94F) * 1.25F, range, true, false, true);
                }
            }

            if (!Vars.headless && this.type.createScorch) {
                for (int i = 0; i < this.type.wreckRegions.length; ++i) {
                    if (this.type.wreckRegions[i].found()) {
                        range = this.type.hitSize / 4.0F;
                        Tmp.v1.rnd(range);
                        Effect.decal(this.type.wreckRegions[i], this.x + Tmp.v1.x, this.y + Tmp.v1.y, this.rotation - 90.0F);
                    }
                }
            }

            Ability[] var12 = this.abilities;
            var6 = var12.length;

            for (var7 = 0; var7 < var6; ++var7) {
                Ability a = var12[var7];
                a.death(this);
            }

            this.type.killed(this);
            this.remove();
        }
    }

    @Override
    public void display(Table table) {
        this.type.display(this, table);
    }

    @Override
    public void draw() {
        Iterator<StatusEntry> var1 = this.statuses.iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (var1.hasNext()) {
            StatusEntry e = var1.next();
            e.effect.draw(this, e.time);
        }

        this.type.draw(this);
    }

    @Override
    public void drawBuildPlans() {
        label43:
        for (int i = 0; i < 2; ++i) {
            Iterator<BuildPlan> var2 = this.plans.iterator();

            while (true) {
                BuildPlan plan;
                do {
                    do {
                        if (!var2.hasNext()) {
                            continue label43;
                        }

                        plan = var2.next();
                    } while (plan.progress > 0.01F);
                } while (this.buildPlan() == plan && plan.initialized && (this.within((float) (plan.x * 8), (float) (plan.y * 8), this.type.buildRange) || Vars.state.isEditor()));

                if (i == 0) {
                    this.drawPlan(plan, 1.0F);
                } else {
                    this.drawPlanTop(plan, 1.0F);
                }
            }
        }

        Draw.reset();
    }

    @Override
    public void drawBuilding() {
        boolean active = this.activelyBuilding();
        if (active || this.lastActive != null) {
            Draw.z(115.0F);
            BuildPlan plan = active ? this.buildPlan() : this.lastActive;
            Tile tile = plan.tile();
            CoreBlock.CoreBuild core = this.team.core();
            if (tile != null && this.within(plan, Vars.state.rules.infiniteResources ? Float.MAX_VALUE : this.type.buildRange)) {
                if (core != null && active && !this.isLocal() && !(tile.block() instanceof ConstructBlock)) {
                    Draw.z(84.0F);
                    this.drawPlan(plan, 0.5F);
                    this.drawPlanTop(plan, 0.5F);
                    Draw.z(115.0F);
                }

                if (this.type.drawBuildBeam) {
                    float focusLen = this.type.buildBeamOffset + Mathf.absin(Time.time, 3.0F, 0.6F);
                    float px = this.x + Angles.trnsx(this.rotation, focusLen);
                    float py = this.y + Angles.trnsy(this.rotation, focusLen);
                    this.drawBuildingBeam(px, py);
                }

            }
        }
    }

    @Override
    public void drawBuildingBeam(float px, float py) {
        boolean active = this.activelyBuilding();
        if (active || this.lastActive != null) {
            Draw.z(115.0F);
            BuildPlan plan = active ? this.buildPlan() : this.lastActive;
            Tile tile = Vars.world.tile(plan.x, plan.y);
            if (tile != null && this.within(plan, Vars.state.rules.infiniteResources ? Float.MAX_VALUE : this.type.buildRange)) {
                int size = plan.breaking ? (active ? tile.block().size : this.lastSize) : plan.block.size;
                float tx = plan.drawx();
                float ty = plan.drawy();
                Lines.stroke(1.0F, plan.breaking ? Pal.remove : Pal.accent);
                Draw.z(122.0F);
                Draw.alpha(this.buildAlpha);
                if (!active && !(tile.build instanceof ConstructBlock.ConstructBuild)) {
                    Fill.square(plan.drawx(), plan.drawy(), (float) (size * 8) / 2.0F);
                }

                Drawf.buildBeam(px, py, tx, ty, (float) (8 * size) / 2.0F);
                Fill.square(px, py, 1.8F + Mathf.absin(Time.time, 2.2F, 1.1F), this.rotation + 45.0F);
                Draw.reset();
                Draw.z(115.0F);
            }
        }
    }

    @Override
    public void drawPlan(BuildPlan plan, float alpha) {
        plan.animScale = 1.0F;
        if (plan.breaking) {
            Vars.control.input.drawBreaking(plan);
        } else {
            plan.block.drawPlan(plan, Vars.control.input.allPlans(), Build.validPlace(plan.block, this.team, plan.x, plan.y, plan.rotation) || Vars.control.input.planMatches(plan), alpha);
        }

    }

    @Override
    public void drawPlanTop(BuildPlan plan, float alpha) {
        if (!plan.breaking) {
            Draw.reset();
            Draw.mixcol(Color.white, 0.24F + Mathf.absin(Time.globalTime, 6.0F, 0.28F));
            Draw.alpha(alpha);
            plan.block.drawPlanConfigTop(plan, this.plans);
        }

    }

    @Override
    public void getCollisions(Cons<QuadTree> consumer) {
    }

    @Override
    public void handleSyncHidden() {
        this.remove();
        Vars.netClient.clearRemovedEntity(this.id);
    }

    @Override
    public void heal() {
        this.dead = false;
        this.health = this.maxHealth;
    }

    @Override
    public void heal(float amount) {
        this.health += amount;
        this.clampHealth();
        if (this.health < this.maxHealth && amount > 0.0F) {
            this.wasHealed = true;
        }

    }

    @Override
    public void healFract(float amount) {
        this.heal(amount * this.maxHealth);
    }

    @Override
    public void hitbox(Rect rect) {
        rect.setCentered(this.x, this.y, this.hitSize, this.hitSize);
    }

    @Override
    public void hitboxTile(Rect rect) {
        float size = Math.min(this.hitSize * 0.66F, 7.8F);
        rect.setCentered(this.x, this.y, size, size);
    }

    @Override
    public void impulse(Vec2 v) {
        this.impulse(v.x, v.y);
    }

    @Override
    public void impulse(float x, float y) {
        float mass = this.mass();
        this.vel.add(x / mass, y / mass);
    }

    @Override
    public void impulseNet(Vec2 v) {
        this.impulse(v.x, v.y);
        if (this.isRemote()) {
            float mass = this.mass();
            this.move(v.x / mass, v.y / mass);
        }

    }

    @Override
    public void kill() {
        if (!this.dead && !Vars.net.client() && this.type.killable) {
            Call.unitDeath(this.id);
        }
    }

    @Override
    public void killed() {
        this.wasPlayer = this.isLocal();
        this.health = Math.min(this.health, 0.0F);
        this.dead = true;
        if (!this.type.flying || !this.type.createWreck) {
            this.destroy();
        }

    }

    @Override
    public void landed() {
        if (this.type.mechLandShake > 0.0F) {
            Effect.shake(this.type.mechLandShake, this.type.mechLandShake, this);
        }

        this.type.landed(this);
    }

    @Override
    public void lookAt(Position pos) {
        this.lookAt(this.angleTo(pos));
    }

    @Override
    public void lookAt(float angle) {
        this.rotation = Angles.moveToward(this.rotation, angle, this.type.rotateSpeed * Time.delta * this.speedMultiplier());
    }

    @Override
    public void lookAt(float x, float y) {
        this.lookAt(this.angleTo(x, y));
    }

    @Override
    public void move(Vec2 v) {
        this.move(v.x, v.y);
    }

    @Override
    public void move(float cx, float cy) {
        EntityCollisions.SolidPred check = this.solidity();
        if (check != null) {
            Vars.collisions.move(this, cx, cy, check);
        } else {
            this.x += cx;
            this.y += cy;
        }

    }

    @Override
    public void moveAt(Vec2 vector) {
        this.moveAt(vector, this.type.accel);
    }

    @Override
    public void moveAt(Vec2 vector, float acceleration) {
        Vec2 t = tmp1.set(vector);
        tmp2.set(t).sub(this.vel).limit(acceleration * vector.len() * Time.delta);
        this.vel.add(tmp2);
    }

    @Override
    public void movePref(Vec2 movement) {
        if (this.type.omniMovement) {
            this.moveAt(movement);
        } else {
            this.rotateMove(movement);
        }

    }

    @Override
    public void rawDamage(float amount) {
        boolean hadShields = this.shield > 1.0E-4F;
        if (Float.isNaN(this.health)) {
            this.health = 0.0F;
        }

        if (hadShields) {
            this.shieldAlpha = 1.0F;
        }

        float shieldDamage = Math.min(Math.max(this.shield, 0.0F), amount);
        this.shield -= shieldDamage;
        this.hitTime = 1.0F;
        amount -= shieldDamage;
        if (amount > 0.0F && this.type.killable) {
            this.health -= amount;
            if (this.health <= 0.0F && !this.dead) {
                this.kill();
            }

            if (hadShields && this.shield <= 1.0E-4F) {
                Fx.unitShieldBreak.at(this.x, this.y, 0.0F, this.type.shieldColor(this), this);
            }
        }
    }

    @Override
    public void remove() {
        if (this.added) {
            Groups.all.removeIndex(this, this.index__all);
            this.index__all = -1;
            Groups.unit.removeIndex(this, this.index__unit);
            this.index__unit = -1;
            Groups.sync.removeIndex(this, this.index__sync);
            this.index__sync = -1;
            Groups.draw.removeIndex(this, this.index__draw);
            this.index__draw = -1;
            this.added = false;
            if (Vars.net.client()) {
                Vars.netClient.addRemovedEntity(this.id());
            }

            this.team.data().updateCount(this.type, -1);
            this.controller.removed(this);
            if (this.trail != null && this.trail.size() > 0) {
                Fx.trailFade.at(this.x, this.y, this.trail.width(), this.type.trailColor == null ? this.team.color : this.type.trailColor, this.trail.copy());
            }

            WeaponMount[] var1 = this.mounts;

            //noinspection ForLoopReplaceableByForEach
            for (int var3 = 0; var3 < var1.length; ++var3) {
                WeaponMount mount = var1[var3];
                if (mount.weapon.continuous && mount.bullet != null && mount.bullet.owner == this) {
                    mount.bullet.time = mount.bullet.lifetime - 10.0F;
                    mount.bullet = null;
                }

                if (mount.sound != null) {
                    mount.sound.stop();
                }
            }

            camp().units.remove(this);
        }
    }

    @Override
    public void removeBuild(int x, int y, boolean breaking) {
        int idx = this.plans.indexOf((req) -> req.breaking == breaking && req.x == x && req.y == y);
        if (idx != -1) {
            this.plans.removeIndex(idx);
        }

    }

    // mindustryX display
    public float healthBalance() {
        return healthBalance;
    }

    @Override
    public void resetController() {
        this.controller(this.type.createController(this));
    }

    @Override
    public void rotateMove(Vec2 vec) {
        this.moveAt(Tmp.v2.trns(this.rotation, vec.len()));
        if (!vec.isZero()) {
            this.rotation = Angles.moveToward(this.rotation, vec.angle(), this.type.rotateSpeed * Time.delta * this.speedMultiplier);
        }

    }

    @Override
    public void set(Position pos) {
        this.set(pos.getX(), pos.getY());
    }

    @Override
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void set(UnitType def, UnitController controller) {
        if (this.type != def) {
            this.setType(def);
        }

        this.controller(controller);
    }

    @Override
    public void setProp(UnlockableContent content, double value) {
        if (content instanceof Item) {
            this.stack.item = (Item) content;
            this.stack.amount = Mathf.clamp((int) value, 0, this.type.itemCapacity);
        }

    }

    @Override
    public void setProp(LAccess prop, double value) {
        switch (prop) {
            case rotation:
                this.rotation = (float) value;
                break;
            case health:
                this.health = (float) Mathf.clamp(value, 0.0, this.maxHealth);
                if (this.health <= 0.0F && !this.dead) {
                    this.kill();
                }
                break;
            case shield:
                this.shield = Math.max((float) value, 0.0F);
            case maxHealth:
            case ammo:
            case ammoCapacity:
            case dead:
            case shooting:
            case boosting:
            case range:
            case shootX:
            case shootY:
            case cameraX:
            case cameraY:
            case cameraWidth:
            case cameraHeight:
            case mining:
            case mineX:
            case mineY:
            default:
                break;
            case x:
                this.x = World.unconv((float) value);
                if (!this.isLocal()) {
                    this.snapInterpolation();
                }
                break;
            case y:
                this.y = World.unconv((float) value);
                if (!this.isLocal()) {
                    this.snapInterpolation();
                }
                break;
            case velocityX:
                this.vel.x = (float) (value * 8.0 / 60.0);
                break;
            case velocityY:
                this.vel.y = (float) (value * 8.0 / 60.0);
                break;
            case team:
                if (!Vars.net.client()) {
                    Team team = Team.get((int) value);
                    UnitController var6 = this.controller;
                    if (var6 instanceof Player p) {
                        p.team(team);
                    }

                    this.team = team;
                }
                break;
            case armor:
                this.statusArmor(Math.max((float) value, 0.0F));
                break;
            case flag:
                this.flag = value;
                break;
            case speed:
                this.statusSpeed(Mathf.clamp((float) value, 0.0F, 1000.0F));
        }

    }

    @Override
    public void setProp(LAccess prop, Object value) {
        switch (prop) {
            case team:
                if (value instanceof Team t) {
                    if (!Vars.net.client()) {
                        UnitController var9 = this.controller;
                        if (var9 instanceof Player p) {
                            p.team(t);
                        }

                        this.team = t;
                    }
                }
                break;
            case payloadType:
                if (this instanceof Payloadc pay) {
                    if (!Vars.net.client()) {
                        if (value instanceof Block b) {
                            if (b.synthetic()) {
                                Building build = b.newBuilding().create(b, this.team());
                                if (pay.canPickup(build)) {
                                    pay.addPayload(new BuildPayload(build));
                                }
                            }
                        } else if (value instanceof UnitType ut) {
                            Unit unit = ut.create(this.team());
                            if (pay.canPickup(unit)) {
                                pay.addPayload(new UnitPayload(unit));
                            }
                        } else if (value == null && pay.payloads().size > 0) {
                            pay.payloads().pop();
                        }
                    }
                }
        }

    }

    @Override
    public void setType(UnitType type) {
        this.type = type;
        this.maxHealth = type.health;
        this.drag = type.drag;
        this.armor = type.armor;
        this.hitSize = type.hitSize;
        if (this.mounts().length != type.weapons.size) {
            this.setupWeapons(type);
        }

        if (this.abilities.length != type.abilities.size) {
            this.abilities = new Ability[type.abilities.size];

            for (int i = 0; i < type.abilities.size; ++i) {
                this.abilities[i] = type.abilities.get(i).copy();
            }
        }

        if (this.controller == null) {
            this.controller(type.createController(this));
        }

        if (type instanceof UnitType2 t) {
            this.type2 = t;
        } else {
            type2 = null;
        }
    }

    @Override
    public void setWeaponRotation(float rotation) {
        WeaponMount[] var2 = this.mounts;
        int var3 = var2.length;

        //noinspection ForLoopReplaceableByForEach
        for (int var4 = 0; var4 < var3; ++var4) {
            WeaponMount mount = var2[var4];
            mount.rotation = rotation;
        }

    }

    @Override
    public void setupWeapons(UnitType def) {
        this.mounts = new WeaponMount[def.weapons.size];

        for (int i = 0; i < this.mounts.length; ++i) {
            this.mounts[i] = def.weapons.get(i).mountType.get(def.weapons.get(i));
        }

    }

    // mindustryX display
    public Seq<StatusEntry> statuses() {
        return statuses;
    }

    @Override
    public void statusArmor(float armor) {
        this.applyDynamicStatus().armorOverride = armor;
    }

    @Override
    public void statusBuildSpeed(float buildSpeed) {
        this.applyDynamicStatus().buildSpeedMultiplier = buildSpeed / this.type.buildSpeed;
    }

    @Override
    public void statusDamageMultiplier(float damageMultiplier) {
        this.applyDynamicStatus().damageMultiplier = damageMultiplier;
    }

    @Override
    public void statusDrag(float drag) {
        this.applyDynamicStatus().dragMultiplier = this.type.drag == 0.0F ? 0.0F : drag / this.type.drag;
    }

    @Override
    public void statusMaxHealth(float health) {
        this.applyDynamicStatus().healthMultiplier = health / this.maxHealth;
    }

    @Override
    public void statusReloadMultiplier(float reloadMultiplier) {
        this.applyDynamicStatus().reloadMultiplier = reloadMultiplier;
    }

    @Override
    public void statusSpeed(float speed) {
        this.applyDynamicStatus().speedMultiplier = speed / (this.type.speed * 60.0F / 8.0F);
    }

    @Override
    public void trns(Position pos) {
        this.trns(pos.getX(), pos.getY());
    }

    @Override
    public void trns(float x, float y) {
        this.set(this.x + x, this.y + y);
    }

    @Override
    public void unapply(StatusEffect effect) {
        this.statuses.remove((e) -> {
            if (e.effect == effect) {
                e.effect.onRemoved(this);
                Pools.free(e);
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public void unloaded() {
    }

    @Override
    public void update() {
        updateMoveDefault();

        updateCampMul();

        this.updateBuildLogic();

        penetrationTimer -= Time.delta;
        this.hitTime -= Time.delta / 9.0F;
        this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
        this.itemTime = Mathf.lerpDelta(this.itemTime, (float) Mathf.num(this.hasItem()), 0.05F);

        updateWalk();

        updateMine();

        this.shieldAlpha -= Time.delta / 15.0F;
        if (this.shieldAlpha < 0.0F) {
            this.shieldAlpha = 0.0F;
        }

        Floor floor = this.floorOn();
        if (this.isGrounded() && !this.type.hovering) {
            this.apply(floor.status, floor.statusDuration);
        }

        updateStatus();

        if (Vars.net.client() && !this.isLocal() || this.isRemote()) {
            this.interpolate();
        }

        this.type.update(this);

        updateMapLimit();

        floor = this.floorOn();
        Tile tile = this.tileOn();
        if (this.isFlying() != this.wasFlying) {
            if (this.wasFlying && tile != null) {
                Fx.unitLand.at(this.x, this.y, floor.isLiquid ? 1.0F : 0.5F, tile.floor().mapColor);
            }

            this.wasFlying = this.isFlying();
        }

        if (!this.type.hovering && this.isGrounded() && this.type.emitWalkEffect && (this.splashTimer += Mathf.dst(this.deltaX(), this.deltaY())) >= 7.0F + this.hitSize() / 8.0F) {
            floor.walkEffect.at(this.x, this.y, this.hitSize() / 8.0F, floor.mapColor);
            this.splashTimer = 0.0F;
            if (this.type.emitWalkSound) {
                floor.walkSound.at(this.x, this.y, Mathf.random(floor.walkSoundPitchMin, floor.walkSoundPitchMax), floor.walkSoundVolume);
            }
        }

        this.updateDrowning();

        updateHealTime();

        //noinspection DuplicatedCode
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

        updateAmmo();

        updateAbilities();

        updateTril();

        this.drag = this.type.drag * (this.isGrounded() ? this.floorOn().dragMultiplier : 1.0F) * this.dragMultiplier * Vars.state.rules.dragMultiplier;

        updateSpawnMove();

        updateDead();

        updateTileOn();

        if (!Vars.net.client() && !this.dead && this.shouldUpdateController()) {
            this.controller.updateUnit();
        }

        if (!this.controller.isValidController()) {
            this.resetController();
        }

        if (this.spawnedByCore && !this.isPlayer() && !this.dead) {
            Call.unitDespawn(this);
        }

        updateWeapon();
    }

    public void updateCampMul() {
        if (type2.canApply) {
            if (type2.camp.lastRangeOverride && ovrR) {
                changeR = Math.max(type2.camp.ranges.get(team), changeR);
            } else if (type2.camp.lastRangeOverride) {
                if (valueR <= camp().rangeValue) {
                    ovrR = true;
                    changeR = type2.camp.ranges.get(team, 0f);
                }
            } else if (ovrR) {
                if (valueR < camp().rangeValue) {
                    ovrR = false;
                    changeR = type2.camp.ranges.get(team, 1f);
                }
            } else {
                changeR *= type2.camp.ranges.get(team, 1f);
            }
        }
    }

    public void updateMoveDefault() {
        if (!Vars.net.client() || this.isLocal()) {
            float bot = this.x;
            @SuppressWarnings("SuspiciousNameCombination") float left = this.y;
            //noinspection DuplicatedCode
            this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
            if (Mathf.equal(bot, this.x)) {
                this.vel.x = 0.0F;
            }

            if (Mathf.equal(left, this.y)) {
                this.vel.y = 0.0F;
            }

            this.vel.scl(Math.max(1.0F - this.drag * Time.delta, 0.0F));
        }
    }

    public void updateWalk() {
    }

    public void updateMine() {
        int accepted;
        if (this.mineTile != null) {
            Building core = this.closestCore();
            Item item = this.getMineResult(this.mineTile);
            if (core != null && item != null && !this.acceptsItem(item) && this.within(core, 220.0F) && !this.offloadImmediately()) {
                accepted = core.acceptStack(this.item(), this.stack().amount, this);
                if (accepted > 0) {
                    Call.transferItemTo(this, this.item(), accepted, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
                    this.clearItem();
                }
            }

            if ((!Vars.net.client() || this.isLocal()) && !this.validMine(this.mineTile)) {
                this.mineTile = null;
                this.mineTimer = 0.0F;
            } else if (this.mining() && item != null) {
                this.mineTimer += Time.delta * this.type.mineSpeed * Vars.state.rules.unitMineSpeed(this.team());
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
        this.armorOverride = -1.0F;
        this.speedMultiplier = this.damageMultiplier = this.healthMultiplier = this.reloadMultiplier = this.buildSpeedMultiplier = this.dragMultiplier = 1.0F;
        this.disarmed = false;
        int index;
        if (!this.statuses.isEmpty()) {
            index = 0;
            label356:
            while (true) {
                while (true) {
                    if (index >= this.statuses.size) {
                        break label356;
                    }
                    StatusEntry entry = this.statuses.get(index++);
                    entry.time = Math.max(entry.time - Time.delta, 0.0F);
                    if (entry.effect != null && (!(entry.time <= 0.0F) || entry.effect.permanent)) {
                        this.applied.set(entry.effect.id);
                        if (entry.effect.dynamic) {
                            this.speedMultiplier *= entry.speedMultiplier;
                            this.healthMultiplier *= entry.healthMultiplier;
                            this.damageMultiplier *= entry.damageMultiplier;
                            this.reloadMultiplier *= entry.reloadMultiplier;
                            this.buildSpeedMultiplier *= entry.buildSpeedMultiplier;
                            this.dragMultiplier *= entry.dragMultiplier;
                            if (entry.armorOverride >= 0.0F) {
                                this.armorOverride = entry.armorOverride;
                            }
                        } else {
                            this.speedMultiplier *= entry.effect.speedMultiplier;
                            this.healthMultiplier *= entry.effect.healthMultiplier;
                            this.damageMultiplier *= entry.effect.damageMultiplier;
                            this.reloadMultiplier *= entry.effect.reloadMultiplier;
                            this.buildSpeedMultiplier *= entry.effect.buildSpeedMultiplier;
                            this.dragMultiplier *= entry.effect.dragMultiplier;
                        }
                        this.disarmed |= entry.effect.disarm;
                        entry.effect.update(this, entry.time);
                    } else {
                        if (entry.effect != null) {
                            entry.effect.onRemoved(this);
                        }
                        Pools.free(entry);
                        --index;
                        this.statuses.remove(index);
                    }
                }
            }
        }
    }

    public void updateMapLimit() {
        float range;
        float cx, bot, left;
        float cy;
        float offset;
        if (this.type.bounded) {
            bot = 0.0F;
            left = 0.0F;
            offset = (float) Vars.world.unitHeight();
            range = (float) Vars.world.unitWidth();
            if (Vars.state.rules.limitMapArea && !this.team.isAI()) {
                bot = (float) (Vars.state.rules.limitY * 8);
                left = (float) (Vars.state.rules.limitX * 8);
                offset = (float) (Vars.state.rules.limitHeight * 8) + bot;
                range = (float) (Vars.state.rules.limitWidth * 8) + left;
            }

            if (!Vars.net.client() || this.isLocal()) {
                cx = 0.0F;
                cy = 0.0F;
                if (this.x < left) {
                    cx += -(this.x - left) / 30.0F;
                }

                if (this.y < bot) {
                    cy += -(this.y - bot) / 30.0F;
                }

                if (this.x > range) {
                    cx -= (this.x - range) / 30.0F;
                }

                if (this.y > offset) {
                    cy -= (this.y - offset) / 30.0F;
                }

                this.velAddNet(cx * Time.delta, cy * Time.delta);
            }

            if (this.isGrounded()) {
                this.x = Mathf.clamp(this.x, left, range - 8.0F);
                this.y = Mathf.clamp(this.y, bot, offset - 8.0F);
            }

            if (this.x < -250.0F + left || this.y < -250.0F + bot || this.x >= range + 250.0F || this.y >= offset + 250.0F) {
                this.kill();
            }
        }
    }

    public void updateHealTime() {
        healthBalance = Mathf.lerp(healthBalance, health - lastHealth, Time.delta);
        lastHealth = health;

        if (this.wasHealed && this.healTime <= -1.0F) {
            this.healTime = 1.0F;
        }

        this.healTime -= Time.delta / 20.0F;
        this.wasHealed = false;
    }

    public void updateAmmo() {
        if (Vars.state.rules.unitAmmo && this.ammo < (float) this.type.ammoCapacity - 1.0E-4F) {
            this.resupplyTime += Time.delta;
            if (this.resupplyTime > 10.0F) {
                this.type.ammoType.resupply(this);
                this.resupplyTime = 0.0F;
            }
        }
    }

    public void updateAbilities() {
        Ability[] var15 = this.abilities;
        int var16 = var15.length;

        //noinspection ForLoopReplaceableByForEach
        for (int var18 = 0; var18 < var16; ++var18) {
            Ability a = var15[var18];
            a.update(this);
        }
    }

    public void updateTril() {
        if (this.trail != null) {
            this.trail.length = this.type.trailLength;
            float offset = this.type.useEngineElevation ? this.elevation : 1.0F;
            float range = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * offset;
            float cx = this.x + Angles.trnsx(this.rotation + 180.0F, range);
            float cy = this.y + Angles.trnsy(this.rotation + 180.0F, range);
            this.trail.update(cx, cy);
        }
    }

    public void updateSpawnMove() {
        if (this.team != Vars.state.rules.waveTeam && Vars.state.hasSpawns() && (!Vars.net.client() || this.isLocal()) && this.hittable()) {
            float offset = Vars.state.rules.dropZoneRadius + this.hitSize / 2.0F + 1.0F;
            //noinspection rawtypes
            Iterator var17 = Vars.spawner.getSpawns().iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (var17.hasNext()) {
                Tile spawn = (Tile) var17.next();
                if (this.within(spawn.worldx(), spawn.worldy(), offset)) {
                    this.velAddNet(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(1.1F - this.dst(spawn) / offset).scl(0.45F * Time.delta));
                }
            }
        }
    }

    public void updateDead() {
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
    }

    public void updateTileOn() {
        Tile tile = tileOn();
        Floor floor = floorOn();
        if (tile != null && tile.build != null) {
            tile.build.unitOnAny(this);
        }

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
    }

    public void updateWeapon() {
        //noinspection ForLoopReplaceableByForEach
        for (int accepted = 0; accepted < mounts.length; ++accepted) {
            WeaponMount mount = mounts[accepted];
            if (mount.weapon instanceof Weapon2 w2) {
                w2.update(this, mount, 1, changeR, camp().lastRangeOverride);
            } else {
                mount.weapon.update(this, mount);
            }
        }
    }

    @Override
    public void updateBoosting(boolean boost) {
        if (this.type.canBoost && !this.dead) {
            this.elevation = Mathf.approachDelta(this.elevation, (float) Mathf.num(boost || this.onSolid() || this.isFlying() && !this.canLand()), this.type.riseSpeed);
        }
    }

    @Override
    public void updateBuildLogic() {
        if (!(this.type.buildSpeed <= 0.0F)) {
            if (!Vars.headless) {
                if (this.lastActive != null && this.buildAlpha <= 0.01F) {
                    this.lastActive = null;
                }

                this.buildAlpha = Mathf.lerpDelta(this.buildAlpha, this.activelyBuilding() ? 1.0F : 0.0F, 0.15F);
            }
            this.validatePlans();
            if (this.updateBuilding && this.canBuild()) {
                float finalPlaceDst = Vars.state.rules.infiniteResources ? Float.MAX_VALUE : this.type.buildRange;
                boolean infinite = Vars.state.rules.infiniteResources || this.team().rules().infiniteResources;
                this.buildCounter += Time.delta;
                if (Float.isNaN(this.buildCounter) || Float.isInfinite(this.buildCounter)) {
                    this.buildCounter = 0.0F;
                }
                this.buildCounter = Math.min(this.buildCounter, 10.0F);
                boolean instant = Vars.state.rules.instantBuild && Vars.state.rules.infiniteResources;
                int maxPerFrame = instant ? this.plans.size : 10;
                int count = 0;
                CoreBlock.CoreBuild core = this.core();
                if (core != null || infinite) {
                    while ((this.buildCounter >= 1.0F || instant) && count++ < maxPerFrame && this.plans.size > 0) {
                        --this.buildCounter;
                        if (this.plans.size > 1) {
                            int total = 0;
                            BuildPlan plan;
                            for (int size = this.plans.size; (!this.within((plan = this.buildPlan()).tile(), finalPlaceDst) || this.shouldSkip(plan, core)) && total < size; ++total) {
                                this.plans.removeFirst();
                                this.plans.addLast(plan);
                            }
                        }
                        BuildPlan current = this.buildPlan();
                        Tile tile = current.tile();
                        this.lastActive = current;
                        this.buildAlpha = 1.0F;
                        if (current.breaking) {
                            this.lastSize = tile.block().size;
                        }
                        if (this.within(tile, finalPlaceDst)) {
                            if (!Vars.headless) {
                                Vars.control.sound.loop(Sounds.build, tile, 0.15F);
                            }
                            Building var10 = tile.build;
                            ConstructBlock.ConstructBuild entity;
                            if (var10 instanceof ConstructBlock.ConstructBuild) {
                                entity = (ConstructBlock.ConstructBuild) var10;
                                if (tile.team() != this.team && tile.team() != Team.derelict || !current.breaking && (entity.current != current.block || entity.tile != current.tile())) {
                                    this.plans.removeFirst();
                                    continue;
                                }
                            } else if (!current.initialized && !current.breaking && Build.validPlaceIgnoreUnits(current.block, this.team, current.x, current.y, current.rotation, true, true)) {
                                if (!Build.checkNoUnitOverlap(current.block, current.x, current.y)) {
                                    this.plans.removeFirst();
                                    this.plans.addLast(current);
                                    continue;
                                }
                                boolean hasAll = infinite || current.isRotation(this.team) || tile.team() == Team.derelict && tile.block() == current.block && tile.build != null && tile.block().allowDerelictRepair && Vars.state.rules.derelictRepair || !Structs.contains(current.block.requirements, (i) -> !core.items.has(i.item, Math.min(Mathf.round((float) i.amount * Vars.state.rules.buildCostMultiplier), 1)));
                                if (hasAll) {
                                    Call.beginPlace(this, current.block, this.team, current.x, current.y, current.rotation, current.block.instantBuild ? current.config : null);
                                    if (current.block.instantBuild) {
                                        if (this.plans.size > 0) {
                                            this.plans.removeFirst();
                                        }
                                        continue;
                                    }
                                } else {
                                    current.stuck = true;
                                }
                            } else {
                                if (current.initialized || !current.breaking || !Build.validBreak(this.team, current.x, current.y)) {
                                    this.plans.removeFirst();
                                    continue;
                                }
                                Call.beginBreak(this, this.team, current.x, current.y);
                            }
                            if (tile.build instanceof ConstructBlock.ConstructBuild && !current.initialized) {
                                Events.fire(new EventType.BuildSelectEvent(tile, this.team, this, current.breaking));
                                current.initialized = true;
                            }
                            var10 = tile.build;
                            if (var10 instanceof ConstructBlock.ConstructBuild) {
                                entity = (ConstructBlock.ConstructBuild) var10;
                                float bs = 1.0F / entity.buildCost * this.type.buildSpeed * this.buildSpeedMultiplier * Vars.state.rules.buildSpeed(this.team);
                                if (current.breaking) {
                                    entity.deconstruct(this, core, bs);
                                } else {
                                    entity.construct(this, core, bs, current.config);
                                }
                                current.stuck = Mathf.equal(current.progress, entity.progress);
                                current.progress = entity.progress;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateDrowning() {
        Floor floor = this.drownFloor();
        if (floor != null && floor.isLiquid && floor.drownTime > 0.0F && this.canDrown()) {
            this.lastDrownFloor = floor;
            this.drownTime += Time.delta / floor.drownTime / this.type.drownTimeMultiplier;
            if (Mathf.chanceDelta(0.05000000074505806)) {
                floor.drownUpdateEffect.at(this.x, this.y, this.hitSize, floor.mapColor);
            }

            if (this.drownTime >= 0.999F && !Vars.net.client()) {
                this.kill();
                Events.fire(new EventType.UnitDrownEvent(this));
            }
        } else {
            this.drownTime -= Time.delta / 50.0F;
        }

        this.drownTime = Mathf.clamp(this.drownTime);
    }

    @Override
    public void updateLastPosition() {
        this.deltaX = this.x - this.lastX;
        this.deltaY = this.y - this.lastY;
        this.lastX = this.x;
        this.lastY = this.y;
    }

    @Override
    public void validatePlans() {
        if (this.plans.size > 0) {
            Iterator<BuildPlan> it = this.plans.iterator();

            while (true) {
                BuildPlan plan;
                Tile tile;
                boolean isSameDerelict;
                do {
                    if (!it.hasNext()) {
                        return;
                    }

                    plan = it.next();
                    tile = Vars.world.tile(plan.x, plan.y);
                    isSameDerelict = tile != null && tile.build != null && tile.block() == plan.block && tile.build.tileX() == plan.x && tile.build.tileY() == plan.y && tile.team() == Team.derelict;
                } while (tile != null && (!plan.breaking || tile.block() != Blocks.air) && (plan.breaking || (tile.build == null || tile.build.rotation != plan.rotation || isSameDerelict) && plan.block.rotate || (tile.block() != plan.block || isSameDerelict) && (plan.block == null || (!plan.block.isOverlay() || plan.block != tile.overlay()) && (!plan.block.isFloor() || plan.block != tile.floor()))));

                it.remove();
            }
        }
    }

    @Override
    public void velAddNet(Vec2 v) {
        this.vel.add(v);
        if (this.isRemote()) {
            this.x += v.x;
            this.y += v.y;
        }

    }

    @Override
    public void velAddNet(float vx, float vy) {
        this.vel.add(vx, vy);
        if (this.isRemote()) {
            this.x += vx;
            this.y += vy;
        }

    }

    @Override
    public void wobble() {
        this.x += Mathf.sin(Time.time + (float) (this.id % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
        this.y += Mathf.cos(Time.time + (float) (this.id % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
    }

    @Override
    public Camp camp() {
        return type2 == null ? null : type2.camp;
    }
}
