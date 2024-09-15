package Commonplace.Entities.FAbility;

import Commonplace.Content.SpecialContent.Events;
import Commonplace.Tools.Interfaces.PeculiarityC;
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
    public boolean power = false;
    public boolean rand = false;
    public int num = 0;
    public int speedTo = 0;
    public int damageTo = 0;
    public int reloadTo = 0;
    public int shieldTo = 0;
    public int healthTo = 0;
    public int againTo = 0;

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

            if (u instanceof PeculiarityC uug && power) {
                if (rand) {
                    arc.Events.fire(new Events.GetPowerEvent(u, num, false));
                } else {
                    int maxSpeed = Math.max(speedTo, uug.getSpeedLevel());
                    int maxDamage = Math.max(damageTo, uug.getDamageLevel());
                    int maxReload = Math.max(reloadTo, uug.getReloadLevel());
                    int maxShield = Math.max(shieldTo, uug.getShieldLevel());
                    int maxHealth = Math.max(healthTo, uug.getHealthLevel());
                    int maxAgain = Math.max(againTo, uug.getAgainLevel());
                    uug.setSpeedLevel(maxSpeed);
                    uug.setDamageLevel(maxDamage);
                    uug.setReloadLevel(maxReload);
                    uug.setShieldLevel(maxShield);
                    uug.setHealthLevel(maxHealth);
                    uug.setAgainLevel(maxAgain);
                    uug.setLevel(maxSpeed + maxDamage + maxReload + maxShield + maxHealth + maxAgain);
                }
            }

            if (!Vars.net.client()) {
                u.add();
            }

            status.each(u::apply);

            timer = 0f;
        }
    }
}
