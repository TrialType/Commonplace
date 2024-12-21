package Commonplace.Entities.Ability;

import arc.Core;
import arc.math.Angles;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.entities.abilities.UnitSpawnAbility;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;

import static mindustry.Vars.state;

public class UnitSpawnSupperAbility extends UnitSpawnAbility {
    public ObjectMap<StatusEffect, Float> status = new ObjectMap<>();

    public UnitSpawnSupperAbility(UnitType unit, float spawnTime, float spawnX, float spawnY) {
        super(unit, spawnTime, spawnX, spawnY);
    }

    @Override
    public void update(Unit unit) {
        timer += Time.delta * state.rules.unitBuildSpeed(unit.team);

        if (timer >= spawnTime && Units.canCreate(unit.team, this.unit)) {
            float x = unit.x + Angles.trnsx(unit.rotation, spawnY, spawnX), y = unit.y + Angles.trnsy(unit.rotation, spawnY, spawnX);
            spawnEffect.at(x, y, 0f, parentizeEffects ? unit : null);
            Unit u = this.unit.create(unit.team);
            u.set(x, y);
            u.rotation = unit.rotation;
            arc.Events.fire(new EventType.UnitCreateEvent(u, null, unit));

            if (!Vars.net.client()) {
                u.add();
            }

            status.each(u::apply);

            timer %= spawnTime;
        }
    }

    @Override
    public String localized() {
        return Core.bundle.get("ability.unit-spawn-super.name");
    }
}
