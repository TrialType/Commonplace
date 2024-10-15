package Commonplace.Entities.Unit.F;

import Commonplace.Entities.Unit.Override.FUnitWaterMove;
import mindustry.content.Fx;

public class BoostShootUnitWaterMove extends FUnitWaterMove {
    public static BoostShootUnitWaterMove create() {
        return new BoostShootUnitWaterMove();
    }

    @Override
    public int classId() {
        return 101;
    }

    @Override
    public boolean canShoot() {
        return !this.disarmed;
    }
}
