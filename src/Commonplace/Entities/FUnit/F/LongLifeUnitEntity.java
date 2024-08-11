package Commonplace.Entities.FUnit.F;

import Commonplace.Entities.FUnit.Override.FUnitEntity;

public class LongLifeUnitEntity extends FUnitEntity {
    protected LongLifeUnitEntity() {
    }

    public static LongLifeUnitEntity create() {
        return new LongLifeUnitEntity();
    }

    @Override
    public int classId() {
        return 112;
    }

    @Override
    public void rawDamage(float damage) {
        super.rawDamage(damage * Math.min(1, 1 - hitTime));
    }
}
