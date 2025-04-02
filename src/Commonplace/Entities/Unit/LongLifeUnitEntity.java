package Commonplace.Entities.Unit;

import mindustry.gen.UnitEntity;

public class LongLifeUnitEntity extends UnitEntity {
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
        if (damage > this.health + this.shield + this.armor) {
            super.rawDamage(damage);
        } else {
            super.rawDamage(damage * Math.min(1, 1 - hitTime));
        }
    }
}
