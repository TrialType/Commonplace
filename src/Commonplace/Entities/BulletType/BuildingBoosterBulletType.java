package Commonplace.Entities.BulletType;

import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;

public class BuildingBoosterBulletType extends BulletType {
    protected float radius;

    public float teamMul = -1;
    public float teamRadius = 0;
    public float teamDuration = 0;
    public float enemyMul = 0.25f;
    public float enemyRadius = 200;
    public float enemyDuration = 300;
    public Effect applyEffect = Fx.dynamicWave;
    public Effect applySlowEffect = Fx.none;
    public Effect applyBoostEffect = Fx.none;

    public BuildingBoosterBulletType() {
        speed = 0;
        damage = 1;
        lifetime = 1;
        despawnHit = true;
        collides = reflectable = absorbable = hittable = false;
    }

    @Override
    public void hit(Bullet bullet, float x, float y) {
        super.hit(bullet, x, y);

        Team t = bullet.team;
        applyEffect.at(x, y, 200, hitColor);
        Vars.indexer.eachBlock(null, x, y, radius, b -> b.team != Team.derelict && b.block.canOverdrive && b.within(x, y, radius), b -> {
            if (b.team == t && teamMul >= 0 && teamMul != 1 && b.within(x, y, teamRadius)) {
                if (teamMul > 1) {
                    applyBoostEffect.at(b.x, b.y, radius, hitColor);
                    b.applyBoost(teamMul, teamDuration);
                } else if (teamMul < 1) {
                    applySlowEffect.at(b.x, b.y, radius, hitColor);
                    b.applySlowdown(teamMul, teamDuration);
                }
            } else if (b.team != t && enemyMul >= 0 && enemyMul != 1 && b.within(x, y, enemyRadius)) {
                if (enemyMul > 1) {
                    applyBoostEffect.at(b.x, b.y, radius, hitColor);
                    b.applyBoost(enemyMul, enemyDuration);
                } else if (enemyMul < 1) {
                    applySlowEffect.at(b.x, b.y, radius, hitColor);
                    b.applySlowdown(enemyMul, enemyDuration);
                }
            }
        });
    }

    @Override
    public void init() {
        super.init();
        radius = Math.max(teamRadius, enemyRadius);
    }
}
