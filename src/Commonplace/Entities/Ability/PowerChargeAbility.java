package Commonplace.Entities.Ability;

import Commonplace.Loader.Special.Effects;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.ai.types.MissileAI;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.TimedKillUnit;
import mindustry.gen.Unit;

public class PowerChargeAbility extends Ability {
    public float mul = 2f;
    public float velMul = 1.55f;
    public float maxVelMul = 2.5f;
    public float chargeSpeed = 0.1f;
    public float missileChargeDurationMul = 0.5f;
    public Color lightningColor = Color.white;

    public float randomSpread = 360;
    public float spread = 0;
    public float angle = 0;
    public int bullets = 12;
    public BulletType bullet = null;

    protected Unit shooter = null;
    protected float power = 0;
    protected float timer = 0;
    protected float velTimer = 0;

    @Override
    public void update(Unit unit) {
        timer += Time.delta;
        velTimer += Time.delta;
        if (shooter == null) {
            if (unit.controller() instanceof MissileAI ai && ai.shooter != null && !ai.shooter.dead) {
                shooter = ai.shooter;
            }
        }
        if (shooter != null) {
            if (shooter.dead || !shooter.isAdded()) {
                shooter = null;
            } else {
                if (!(unit instanceof TimedKillUnit t) || t.fin() <= missileChargeDurationMul) {
                    power = Math.min(mul, power + chargeSpeed * Time.delta);
                    if (timer >= 5) {
                        Effects.chainLightningFollow.at(shooter.x, shooter.y, 0, lightningColor, new Effects.PointPack2(unit, shooter));
                        timer = 0;
                    }
                }
            }
        } else {
            power = Math.max(0, power - chargeSpeed * Time.delta);
        }

        if (power > 0) {
            if (unit instanceof TimedKillUnit t) {
                if (t.fin() > missileChargeDurationMul) {
                    unit.vel.setLength(Math.min(unit.vel.len() * velMul, unit.speed() * maxVelMul));
                }
            } else {
                if (velTimer > 10 && velTimer < 19) {
                    unit.vel.scl(velMul);
                } else if (velTimer >= 19) {
                    velTimer = 0;
                }
            }
        }
    }

    @Override
    public void death(Unit unit) {
        if (bullet != null) {
            for (int i = 0; i < bullets; i++) {
                bullet.create(shooter, shooter, unit.team, unit.x, unit.y,
                        unit.rotation + angle + Mathf.random(randomSpread) + (i - ((float) (bullets + 1) / 2)) * spread,
                        (float) (power == 0 ? 1 : bullet.damage * Math.pow(power, 2)), 1, 1,
                        null, null, unit.aimX, unit.aimY);
            }
        }
    }
}
