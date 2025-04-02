package Commonplace.AI;

import Commonplace.Entities.Unit.TileMiner;
import Commonplace.Utils.Classes.Located;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import mindustry.entities.units.AIController;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;

import static mindustry.Vars.world;

public class TilePutAI extends AIController {
    protected Vec2 pose = null;
    protected TileMiner miner = null;

    public TilePutAI(Unit unit) {
        this.unit = unit;
    }

    @Override
    public void updateMovement() {
        pose = unit.command().targetPos;
        if (miner != null && pose != null) {
            unit.lookAt(pose);
            moveTo(pose, unit.hitSize * 1.5f);
            if (unit.within(pose, unit.hitSize * 3)) {
                Floor floor = null;
                int index = 0;
                for (; index < miner.tiles.length; index++) {
                    if (miner.tiles[index] != null) {
                        floor = miner.tiles[index];
                        break;
                    }
                }

                Tile tile = world.tileWorld(pose.x, pose.y);
                if (tile != null && !tile.floor().isLiquid && !tile.solid() && floor != null &&
                        ((floor instanceof OreBlock) ? tile.overlay() : tile.floor()).itemDrop == null) {

                    if (floor instanceof OreBlock) {
                        tile.setOverlay(floor);
                    } else {
                        Floor overlay = tile.overlay();
                        tile.setFloor(floor);
                        tile.setOverlay(overlay);
                    }
                    tile.overlay().drawBase(tile);

                    IntSeq is = Located.ores[floor.itemDrop.id][tile.x / Located.quadrantSize][tile.y / Located.quadrantSize];
                    if (is == null) {
                        is = Located.ores[floor.itemDrop.id][tile.x / Located.quadrantSize][tile.y / Located.quadrantSize] = new IntSeq(false, 16);
                    }
                    is.add(tile.pos());
                    Located.allOres.increment(floor.itemDrop, 1);
                    miner.tiles[index] = null;
                }
            }
        } else {
            if (unit instanceof TileMiner) {
                miner = (TileMiner) unit;
            }
        }
    }

    @Override
    public void init() {
        if (unit instanceof TileMiner) {
            this.miner = (TileMiner) unit;
        }
    }
}
