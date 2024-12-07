package Commonplace.AI;

import Commonplace.Entities.Unit.TileMiner;
import Commonplace.Utils.Classes.Located;
import arc.math.geom.Vec2;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.gen.*;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.storage.CoreBlock;

import static Commonplace.Utils.Classes.Located.miners;
import static mindustry.Vars.world;

public class TileMineAI extends AIController {
    protected TileMiner miner = null;
    protected Item targetItem;
    protected Tile ore;
    protected Vec2 targetPos;

    public TileMineAI(Unit unit) {
        this.unit = unit;
    }

    public TileMineAI() {
    }

    @Override
    public void updateMovement() {
        if (miner != null) {
            if (!miner.canMine()) return;

            boolean full = miner.tiles[0] != null && miner.tiles[1] != null;
            float range = miner.hitSize * 6;

            if (ore != null && (!miner.validMine(ore) || ore.solid() ||
                    (miners.containsKey(ore) && miners.get(ore) != miner) ||
                    !ore.within(miner, range * 1.25f))) {
                ore = null;
                miner.mineTile = null;
            }

            if (miner.team.isAI()) {
                if (full) {
                    Teamc target = Units.closestTarget(miner.team, miner.x, miner.y, Float.MAX_VALUE,
                            u -> u.type.estimateDps() > 0, building -> building instanceof Turret.TurretBuild);
                    if (target != null) {
                        moveTo(target, 0);
                    }
                } else {
                    if (targetItem == null) {
                        CoreBlock.CoreBuild core = unit.closestEnemyCore();
                        if (core != null) {
                            targetItem = unit.type.mineItems.min(
                                    i -> (ore = Located.findOre(miner, i)) != null,
                                    i -> core.items.get(i)
                            );
                        }
                    }

                    if (ore == null && targetItem != null) {
                        ore = Located.findOre(miner, targetItem);
                    }

                    if (ore != null) {
                        moveTo(ore, range * 0.8f);
                        if (miner.within(ore, range) && (!miners.containsKey(ore) || miners.get(ore) == miner)) {
                            miner.mineTile = ore;
                            miners.put(ore, miner);
                        }
                    } else {
                        targetItem = null;
                    }
                }
            } else {
                if (full) {
                    moveTo(miner.spawner, 5);
                } else {
                    targetPos = unit.command().targetPos;
                    if (targetPos != null) {
                        unit.lookAt(targetPos);
                        moveTo(targetPos, range * 0.8f);

                        Tile tile = world.tileWorld((int) (targetPos.x), (int) (targetPos.y));
                        if (tile != null && unit.canMine(tile.drop())) {
                            ore = tile;
                        } else {
                            ore = null;
                            unit.mineTile = null;
                        }
                    }
                    if (ore != null) {
                        if (miner.within(ore, range) && (!miners.containsKey(ore) || miners.get(ore) == miner)) {
                            miner.mineTile = ore;
                            miners.put(ore, miner);
                        }
                    }
                }
            }
        } else if (unit instanceof TileMiner) {
            miner = (TileMiner) unit;
            ore = miner.mineTile;
        }
    }

    @Override
    public void init() {
        if (unit instanceof TileMiner) {
            miner = (TileMiner) unit;
            ore = miner.mineTile;
        }
    }
}
