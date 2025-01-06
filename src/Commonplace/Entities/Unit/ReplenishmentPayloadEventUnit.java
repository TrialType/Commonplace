package Commonplace.Entities.Unit;

import mindustry.game.EventType;
import mindustry.gen.PayloadUnit;

public class ReplenishmentPayloadEventUnit extends PayloadUnit {
    protected ReplenishmentPayloadEventUnit() {
        super();
    }

    public static ReplenishmentPayloadEventUnit create() {
        return new ReplenishmentPayloadEventUnit();
    }

    @Override
    public void add() {
        super.add();

        if (!dead) {
            arc.Events.fire(new EventType.UnitCreateEvent(this, null, null));
        }
    }
}
