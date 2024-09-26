package Commonplace.Entities.FAbility;

import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;

public class BoostExplosionAbility extends Ability {
    public float minSpeed = 30;
    public float splashDamage = 1400;
    public float splashDamageRadius = 80;

    private boolean get = false;

    @Override
    public void update(Unit unit) {
        if (get && unit.vel.len() < minSpeed) {
            unit.vel.setZero();
            get = false;
            Damage.damage(unit.team, unit.x, unit.y, splashDamageRadius, splashDamage, false, true, true, false, null);
            Fx.flakExplosionBig.at(unit);
        } else if (unit.vel.len() > minSpeed) {
            get = true;
        }
    }
}
