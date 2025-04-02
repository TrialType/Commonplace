package Commonplace.Entities.Unit;

import Commonplace.Utils.Interfaces.OwnCreate;
import mindustry.game.EventType;
import mindustry.gen.PayloadUnit;

public class ReplenishmentPayloadEventUnit extends PayloadUnit implements OwnCreate {
    protected boolean event = false;

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

    @Override
    public void created(boolean create) {
        event = create;
    }

    @Override
    public boolean created() {
        return event;
    }
}
