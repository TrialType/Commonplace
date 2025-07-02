package Commonplace.Entities.BulletType;

import Commonplace.Utils.Classes.Damage2;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.SuppressionFieldAbility;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Unit;

public class ContinuousLinkBulletType extends ContinuousBulletType {
    public Effect damagePointEffect = Fx.none;

    protected final Vec2 vec = new Vec2();

    public ContinuousLinkBulletType() {
        speed = 0;
        collides = hittable = reflectable = absorbable = false;
    }


    @Override
    public void update(Bullet b) {
        super.update(b);
        updateTrailEffects(b);
    }

    @Override
    public void applyDamage(Bullet b) {
        if (b.owner instanceof Healthc h && h.dead()) {
            return;
        }

        if (b.owner instanceof Unit u && new Seq<>(u.abilities).contains(a -> a instanceof SuppressionFieldAbility)) {
            for (Ability a : u.abilities) {
                if (a instanceof SuppressionFieldAbility s) {
                    vec.set(s.x, s.y).rotate(u.rotation - 90).add(u).sub(b);
                    Damage2.collideLine(b, b.team, b.x, b.y, vec.angle(), vec.len(), largeHit, false, -1);
                    Fx.chainLightning.at(b.x, b.y, 0, lightningColor, vec.add(b).cpy());
                }
            }
        } else if (b.owner instanceof Position p) {
            vec.set(p.getX(), p.getY()).sub(b);
            Damage2.collideLine(b, b.team, b.x, b.y, vec.angle(), vec.len(), largeHit, false, -1);
            Fx.chainLightning.at(b.x, b.y, 0, lightningColor, p);
        }

        if (splashDamageRadius > 0) {
            Damage.damage(b.team, b.x, b.y, splashDamageRadius, splashDamage,
                    splashDamagePierce, collidesAir, collidesGround, largeHit, b);
            damagePointEffect.at(b.x, b.y, b.rotation(), hitColor, b);
        }

        if (lightning > 0) {
            Lightning.create(b.team, lightningColor, lightningDamage, b.x, b.y,
                    b.rotation() + lightningAngle + Mathf.random(lightningCone),
                    lightningLength + Mathf.random(lightningLengthRand));
        }
    }
}
