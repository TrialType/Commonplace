package Commonplace.Entities.Unit;

import Commonplace.Utils.Interfaces.OwnCreate;
import mindustry.game.EventType;
import mindustry.gen.LegsUnit;

public class ReplenishmentLegsEventUnit extends LegsUnit implements OwnCreate {
    protected boolean event = false;

    protected ReplenishmentLegsEventUnit() {
        super();
    }

    public static ReplenishmentLegsEventUnit create() {
        return new ReplenishmentLegsEventUnit();
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
