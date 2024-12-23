package Commonplace.Entities.Ability;

import arc.Core;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;

public class BoostExplosionAbility extends Ability {
    public float minSpeed = 5;
    public float splashDamage = 1400;
    public float splashDamageRadius = 40;

    private boolean get = false;

    @Override
    public void update(Unit unit) {
        if (get && unit.vel.len() < minSpeed) {
            unit.vel.setZero();
            get = false;
            Damage.damage(unit.team, unit.x, unit.y, splashDamageRadius, splashDamage, false, true, true, false, null);
            Fx.massiveExplosion.at(unit);
        } else if (unit.vel.len() > minSpeed) {
            get = true;
        }
    }

    @Override
    public String localized() {
        return Core.bundle.get("ability.boost-explosion.name");
    }
}
