package Commonplace.Entities.Unit;

import Commonplace.Utils.Interfaces.OwnCreate;
import mindustry.game.EventType;
import mindustry.gen.TankUnit;

public class ReplenishmentTankEventUnit extends TankUnit implements OwnCreate {
    protected boolean event = false;

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

    @Override
    public void created(boolean create) {
        event = create;
    }

    @Override
    public boolean created() {
        return event;
    }
}
