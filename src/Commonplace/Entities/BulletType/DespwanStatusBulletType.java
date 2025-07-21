package Commonplace.Entities.BulletType;

import arc.struct.Seq;
import mindustry.content.StatusEffects;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Shieldc;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class DespwanStatusBulletType extends BulletType {
    public float radius = 200;
    public float shield = 600;
    public float maxShield = -1;
    public float effectDuration = 150;
    public float extendMultiplier = 1;
    public boolean shieldExtend = true;
    public StatusEffect effect = StatusEffects.overclock;

    public DespwanStatusBulletType() {
        despawnHit = true;
    }

    @Override
    public void hit(Bullet b, float x, float y) {
        super.hit(b, x, y);

        Seq<Unit> units = new Seq<>();
        Units.nearby(b.team, x, y, radius, units::add);
        if (b.owner instanceof Unit u) {
            units.remove(u);
        }
        if (units.any()) {
            float adder = shield + Math.max((shieldExtend && b.owner instanceof Shieldc s ? s.shield() * extendMultiplier / units.size : 0), 0);
            units.each(u -> {
                float shield = u.shield;
                if (maxShield < 0) {
                    u.shield(shield + adder);
                } else if (shield < maxShield) {
                    u.shield(Math.min(maxShield, shield + adder));
                }

                if (effect != null) {
                    u.apply(effect, effectDuration);
                }
            });
        }
    }
}
