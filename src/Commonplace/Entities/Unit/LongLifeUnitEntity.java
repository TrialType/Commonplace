package Commonplace.Entities.Unit;

import arc.util.Time;
import mindustry.gen.UnitEntity;

public class LongLifeUnitEntity extends UnitEntity {
    private float hitTimer = 45;

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
    public void update(){
        hitTimer += Time.delta;
        super.update();
    }

    @Override
    public void rawDamage(float damage) {
        super.rawDamage(damage * Math.min(1, hitTimer / 45));
        hitTimer = 0;
    }
}
