package Commonplace.Entities.BulletType;

import Commonplace.Utils.Classes.Damage2;
import arc.math.Angles;
import arc.math.Interp;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Fires;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;

import static mindustry.Vars.indexer;

public class StrikeBulletType extends BasicBulletType {
    public boolean hitTeam = false;
    public float baseForce = 5;
    public Interp forceInterp = Interp.reverse;
    public Interp damageInterp = Interp.reverse;

    @Override
    public void hit(Bullet b, float x, float y) {
        super.hit(b, x, y);

        Units.nearby(null, x, y, splashDamageRadius, u -> {
            if (hitTeam || u.team != b.team) {
                float angle = u.angleTo(x, y) + 180;
                float len = forceInterp.apply(u.dst(x, y) / splashDamageRadius) * baseForce;
                u.vel.add(Angles.trnsx(angle, len), Angles.trnsy(angle, len));
            }
        });
    }

    @Override
    public void createSplashDamage(Bullet b, float x, float y) {
        if (splashDamageRadius > 0 && !b.absorbed) {
            Damage2.damageInterp(hitTeam ? Team.derelict : b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier(), damageInterp, splashDamagePierce, collidesAir, collidesGround, scaledSplashDamage, b);

            if (status != StatusEffects.none) {
                Damage.status(hitTeam ? Team.derelict : b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
            }

            if (heals()) {
                indexer.eachBlock(b.team, x, y, splashDamageRadius, Building::damaged, other -> {
                    healEffect.at(other.x, other.y, 0f, healColor, other.block);
                    other.heal(healPercent / 100f * other.maxHealth() + healAmount);
                });
            }

            if (makeFire) {
                indexer.eachBlock(null, x, y, splashDamageRadius, other -> other.team != b.team, other -> Fires.create(other.tile));
            }
        }
    }
}
