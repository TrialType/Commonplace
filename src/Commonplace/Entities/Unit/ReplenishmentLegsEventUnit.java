package Commonplace.Entities.Unit;

import mindustry.game.EventType;
import mindustry.gen.LegsUnit;

public class ReplenishmentLegsEventUnit extends LegsUnit {
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
}
