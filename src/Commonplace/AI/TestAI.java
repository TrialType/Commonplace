package Commonplace.AI;

import Commonplace.Tools.Classes.PathFinder;
import mindustry.Vars;
import mindustry.entities.units.AIController;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

public class TestAI extends AIController {

    @Override
    public void updateMovement() {
        CoreBlock.CoreBuild core = unit.closestEnemyCore();
        if (core != null) {
            Tile close = PathFinder.getPath(unit.tileOn(), core.tile, 0);
            moveTo(close,0);
        }
    }
}
