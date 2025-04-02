package Commonplace.Entities.BulletType;

import arc.Events;
import arc.util.Tmp;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.game.EventType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Unit;

public class ProtectKillerBulletType extends BasicBulletType {
    public float minArmor = 0;
    public float damageArmorMultiplier = 0.6f;
    public float maxArmorDamageAdder = 10;
    public float minShield = 0;
    public float damageShieldMultiplier = 0.5f;
    public float maxShieldDamageAdder = 1000;

    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health) {
        boolean wasDead = entity instanceof Unit u && u.dead;
        float damage = b.damage;
        if (entity instanceof Unit u) {
            if (u.armor > minArmor) {
                damage += Math.min(maxArmorDamageAdder < 0 ? Float.MAX_VALUE : maxArmorDamageAdder, u.armor * damageArmorMultiplier);
            }
            if (u.shield > minShield) {
                damage += Math.min(maxShieldDamageAdder < 0 ? Float.MAX_VALUE : maxShieldDamageAdder, u.shield * damageShieldMultiplier);
            }
        }

        if (entity instanceof Healthc h) {
            if (pierceArmor) {
                h.damagePierce(damage);
            } else {
                h.damage(damage);
            }
        }

        if (entity instanceof Unit unit) {
            Tmp.v3.set(unit).sub(b).nor().scl(knockback * 80f);
            if (impact) Tmp.v3.setAngle(b.rotation() + (knockback < 0 ? 180f : 0f));
            unit.impulse(Tmp.v3);
            unit.apply(status, statusDuration);

            Events.fire(new EventType.UnitDamageEvent().set(unit, b));
        }

        if (!wasDead && entity instanceof Unit unit && unit.dead) {
            Events.fire(new EventType.UnitBulletDestroyEvent(unit, b));
        }

        handlePierce(b, health, entity.x(), entity.y());
    }
}
