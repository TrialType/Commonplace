package Commonplace.Entities.BulletType;

import arc.Events;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.entities.Damage;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.game.EventType;
import mindustry.gen.*;

public class PercentKillBulletType extends BasicBulletType {
    protected static final EventType.UnitDamageEvent bulletDamageEvent = new EventType.UnitDamageEvent();

    public float hitDamage = 400;
    public float hitPercent = 0.02f;
    public float hitSplashDamageRadius = 24;
    public boolean hitAir = true;
    public boolean hitGround = true;
    public Interp percent = Interp.linear;

    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health) {
        boolean wasDead = entity instanceof Unit u && u.dead;

        if (entity instanceof Healthc h) {
            if (Mathf.chance(percent.apply(1 - h.healthf()))) {
                health = hitDamage + hitPercent * h.maxHealth() * (entity instanceof Statusc s ? Math.max(1, s.healthMultiplier()) : 1);
                if (hitSplashDamageRadius > 0) {
                    Damage.damage(b.team, entity.x(), entity.y(), hitSplashDamageRadius, damage + health, hitAir, hitGround);
                } else {
                    h.damage(damage + health);
                }
            } else {
                float damage = b.damage;
                float shield = entity instanceof Shieldc s ? Math.max(s.shield(), 0f) : 0f;
                if (maxDamageFraction > 0) {
                    float cap = h.maxHealth() * maxDamageFraction + shield;
                    damage = Math.min(damage, cap);
                    health = Math.min(health, cap);
                } else {
                    health += shield;
                }

                if (lifesteal > 0f && b.owner instanceof Healthc o) {
                    float result = Math.max(Math.min(h.health(), damage), 0);
                    o.heal(result * lifesteal);
                }

                if (pierceArmor) {
                    h.damagePierce(damage);
                } else {
                    h.damage(damage);
                }
            }
        }

        if (entity instanceof Unit unit) {
            Tmp.v3.set(unit).sub(b).nor().scl(knockback * 80f);
            if (impact) Tmp.v3.setAngle(b.rotation() + (knockback < 0 ? 180f : 0f));
            unit.impulse(Tmp.v3);
            unit.apply(status, statusDuration);

            Events.fire(bulletDamageEvent.set(unit, b));
        }

        if (!wasDead && entity instanceof Unit unit && unit.dead) {
            Events.fire(new EventType.UnitBulletDestroyEvent(unit, b));
        }

        handlePierce(b, health, entity.x(), entity.y());
    }
}
