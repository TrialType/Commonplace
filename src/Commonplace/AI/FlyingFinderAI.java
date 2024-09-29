package Commonplace.AI;

import mindustry.ai.types.FlyingAI;
import mindustry.entities.Units;

public class FlyingFinderAI extends FlyingAI {

    @Override
    public void updateMovement() {
        target = Units.closestTarget(unit.team, unit.x, unit.y, Math.max(unit.type.maxRange,unit.range()) * 3);

        if (target != null) {
            super.updateMovement();
        }
    }
}
