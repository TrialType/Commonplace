package Commonplace.Entities.Unit;

import mindustry.game.EventType;
import mindustry.gen.TankUnit;

public class ReplenishmentTankEventUnit extends TankUnit {
    protected ReplenishmentTankEventUnit() {
        super();
    }

    public static ReplenishmentTankEventUnit create() {
        return new ReplenishmentTankEventUnit();
    }

    @Override
    public void add() {
        super.add();

        if (!dead) {
            arc.Events.fire(new EventType.UnitCreateEvent(this, null, null));
        }
    }
}
