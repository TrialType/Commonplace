package Commonplace.Entities.Weapon;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.ai.types.MissileAI;
import mindustry.audio.SoundLoop;
import mindustry.entities.Effect;
import mindustry.entities.Mover;
import mindustry.entities.Predict;
import mindustry.entities.Sized;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Entityc;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

import static mindustry.Vars.headless;
import static mindustry.Vars.state;

public class Weapon2 extends Weapon {
    public void update(Unit unit, WeaponMount mount, float weaponReloadMul, float bulletLife, boolean bulletLifeOverride) {
        boolean can = unit.canShoot();
        float bulletMul = bulletLifeOverride ? bulletLife / bullet.lifetime : bulletLife;
        float lastReload = mount.reload;
        float reload = unit.reloadMultiplier * weaponReloadMul;
        mount.reload = Math.max(mount.reload - Time.delta * reload, 0);
        mount.recoil = Mathf.approachDelta(mount.recoil, 0, reload / recoilTime);
        if (recoils > 0) {
            if (mount.recoils == null) mount.recoils = new float[recoils];
            for (int i = 0; i < recoils; i++) {
                mount.recoils[i] = Mathf.approachDelta(mount.recoils[i], 0, reload / recoilTime);
            }
        }
        mount.smoothReload = Mathf.lerpDelta(mount.smoothReload, mount.reload / this.reload, smoothReloadSpeed);
        mount.charge = mount.charging && shoot.firstShotDelay > 0 ? Mathf.approachDelta(mount.charge, 1, 1 / shoot.firstShotDelay) : 0;

        float warmupTarget = (can && mount.shoot) || (continuous && mount.bullet != null) || mount.charging ? 1f : 0f;
        if (linearWarmup) {
            mount.warmup = Mathf.approachDelta(mount.warmup, warmupTarget, shootWarmupSpeed);
        } else {
            mount.warmup = Mathf.lerpDelta(mount.warmup, warmupTarget, shootWarmupSpeed);
        }

        float mountX = unit.x + Angles.trnsx(unit.rotation - 90, x, y), mountY = unit.y + Angles.trnsy(unit.rotation - 90, x, y);

        if (!controllable && autoTarget) {
            if ((mount.retarget -= Time.delta) <= 0f) {
                mount.target = findTarget(unit, mountX, mountY, bullet.range * bulletMul, bullet.collidesAir, bullet.collidesGround);
                mount.retarget = mount.target == null ? targetInterval : targetSwitchInterval;
            }

            if (mount.target != null && checkTarget(unit, mount.target, mountX, mountY, bullet.range * bulletMul)) {
                mount.target = null;
            }

            boolean shoot = false;

            if (mount.target != null) {
                shoot = mount.target.within(mountX, mountY, bullet.range * bulletMul + Math.abs(shootY) + (mount.target instanceof Sized s ? s.hitSize() / 2f : 0f)) && can;

                if (predictTarget) {
                    Vec2 to = Predict.intercept(unit, mount.target, bullet.speed);
                    mount.aimX = to.x;
                    mount.aimY = to.y;
                } else {
                    mount.aimX = mount.target.x();
                    mount.aimY = mount.target.y();
                }
            }

            mount.shoot = mount.rotate = shoot;
        } else if (mount.target != null) {
            mount.shoot = mount.target.within(mountX, mountY, bullet.range * bulletMul + Math.abs(shootY) + (mount.target instanceof Sized s ? s.hitSize() / 2f : 0f));
        }

        if (rotate && (mount.rotate || mount.shoot) && can) {
            float axisX = unit.x + Angles.trnsx(unit.rotation - 90, x, y), axisY = unit.y + Angles.trnsy(unit.rotation - 90, x, y);

            mount.targetRotation = Angles.angle(axisX, axisY, mount.aimX, mount.aimY) - unit.rotation;
            mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, rotateSpeed * Time.delta);
            if (rotationLimit < 360) {
                float dst = Angles.angleDist(mount.rotation, baseRotation);
                if (dst > rotationLimit / 2f) {
                    mount.rotation = Angles.moveToward(mount.rotation, baseRotation, dst - rotationLimit / 2f);
                }
            }
        } else if (!rotate) {
            mount.rotation = baseRotation;
            mount.targetRotation = unit.angleTo(mount.aimX, mount.aimY);
        }

        float weaponRotation = unit.rotation - 90 + (rotate ? mount.rotation : baseRotation),
                bulletX = mountX + Angles.trnsx(weaponRotation, this.shootX, this.shootY),
                bulletY = mountY + Angles.trnsy(weaponRotation, this.shootX, this.shootY),
                shootAngle = bulletRotation(unit, mount, bulletX, bulletY);

        if (alwaysShooting) mount.shoot = true;

        if (continuous && mount.bullet != null) {
            if (!mount.bullet.isAdded() || mount.bullet.time >= mount.bullet.lifetime || mount.bullet.type != bullet) {
                mount.bullet = null;
            } else {
                mount.bullet.rotation(weaponRotation + 90);
                mount.bullet.set(bulletX, bulletY);
                mount.reload = this.reload;
                mount.recoil = 1f;
                unit.vel.add(Tmp.v1.trns(mount.bullet.rotation() + 180f, mount.bullet.type.recoil * Time.delta));
                if (shootSound != Sounds.none && !headless) {
                    if (mount.sound == null) mount.sound = new SoundLoop(shootSound, 1f);
                    mount.sound.update(bulletX, bulletY, true);
                }

                float shootLength = Math.min(Mathf.dst(bulletX, bulletY, mount.aimX, mount.aimY), range());
                float curLength = Mathf.dst(bulletX, bulletY, mount.bullet.aimX, mount.bullet.aimY);
                float resultLength = Mathf.approachDelta(curLength, shootLength, aimChangeSpeed);
                Tmp.v1.trns(shootAngle, mount.lastLength = resultLength).add(bulletX, bulletY);

                mount.bullet.aimX = Tmp.v1.x;
                mount.bullet.aimY = Tmp.v1.y;

                if (alwaysContinuous && mount.shoot) {
                    mount.bullet.time = mount.bullet.lifetime * mount.bullet.type.optimalLifeFract * mount.warmup;
                    mount.bullet.keepAlive = true;

                    unit.apply(shootStatus, shootStatusDuration);
                }
            }
        } else {
            mount.heat = Math.max(mount.heat - Time.delta * reload / cooldownTime, 0);

            if (mount.sound != null) {
                mount.sound.update(bulletX, bulletY, false);
            }
        }

        boolean wasFlipped = mount.side;
        if (otherSide != -1 && alternate && mount.side == flipSprite && mount.reload <= this.reload / 2f && lastReload > this.reload / 2f) {
            unit.mounts[otherSide].side = !unit.mounts[otherSide].side;
            mount.side = !mount.side;
        }

        if (mount.shoot && can && !(bullet.killShooter && mount.totalShots > 0) &&
                (!useAmmo || unit.ammo > 0 || !state.rules.unitAmmo || unit.team.rules().infiniteAmmo) &&
                (!alternate || wasFlipped == flipSprite) && mount.warmup >= minWarmup && unit.vel.len() >= minShootVelocity &&
                (mount.reload <= 0.0001f || (alwaysContinuous && mount.bullet == null)) &&
                (alwaysShooting || Angles.within(rotate ? mount.rotation : unit.rotation + baseRotation, mount.targetRotation, shootCone))
        ) {
            shoot(unit, mount, bulletX, bulletY, shootAngle, bulletMul);

            mount.reload = this.reload;

            if (useAmmo) {
                unit.ammo--;
                if (unit.ammo < 0) unit.ammo = 0;
            }
        }
    }

    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation, float bulletLife) {
        unit.apply(shootStatus, shootStatusDuration);

        if (shoot.firstShotDelay > 0) {
            mount.charging = true;
            chargeSound.at(shootX, shootY, Mathf.random(soundPitchMin, soundPitchMax));
            bullet.chargeEffect.at(shootX, shootY, rotation, bullet.keepVelocity || parentizeEffects ? unit : null);
        }

        shoot.shoot(mount.barrelCounter, (xOffset, yOffset, angle, delay, mover) -> {
            mount.totalShots++;
            int barrel = mount.barrelCounter;

            if (delay > 0f) {
                Time.run(delay, () -> {
                    int prev = mount.barrelCounter;
                    mount.barrelCounter = barrel;
                    bullet(unit, mount, xOffset, yOffset, angle, mover);
                    mount.barrelCounter = prev;
                });
            } else {
                bullet(unit, mount, xOffset, yOffset, angle, bulletLife, mover);
            }
        }, () -> mount.barrelCounter++);
    }

    protected void bullet(Unit unit, WeaponMount mount, float xOffset, float yOffset, float angleOffset, float bulletLife, Mover mover) {
        if (!unit.isAdded()) return;

        mount.charging = false;
        float xSpread = Mathf.range(xRand), ySpread = Mathf.range(yRand),
                weaponRotation = unit.rotation - 90 + (rotate ? mount.rotation : baseRotation),
                mountX = unit.x + Angles.trnsx(unit.rotation - 90, x, y),
                mountY = unit.y + Angles.trnsy(unit.rotation - 90, x, y),
                bulletX = mountX + Angles.trnsx(weaponRotation, this.shootX + xOffset + xSpread, this.shootY + yOffset + ySpread),
                bulletY = mountY + Angles.trnsy(weaponRotation, this.shootX + xOffset + xSpread, this.shootY + yOffset + ySpread),
                shootAngle = bulletRotation(unit, mount, bulletX, bulletY) + angleOffset,
                lifeScl = bulletLife * (bullet.scaleLife ? Mathf.clamp(Mathf.dst(bulletX, bulletY, mount.aimX, mount.aimY) / bullet.range / bulletLife) : 1f),
                angle = shootAngle + Mathf.range(inaccuracy + bullet.inaccuracy);

        Entityc shooter = unit.controller() instanceof MissileAI ai ? ai.shooter : unit;
        mount.bullet = bullet.create(unit, shooter, unit.team, bulletX, bulletY, angle, -1f, (1f - velocityRnd) + Mathf.random(velocityRnd) + extraVelocity, lifeScl, null, mover, mount.aimX, mount.aimY, mount.target);
        handleBullet(unit, mount, mount.bullet);

        if (!continuous) {
            shootSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));
        }

        ejectEffect.at(mountX, mountY, angle * Mathf.sign(this.x));
        bullet.shootEffect.at(bulletX, bulletY, angle, bullet.hitColor, unit);
        bullet.smokeEffect.at(bulletX, bulletY, angle, bullet.hitColor, unit);

        unit.vel.add(Tmp.v1.trns(shootAngle + 180f, bullet.recoil));
        Effect.shake(shake, shake, bulletX, bulletY);
        mount.recoil = 1f;
        if (recoils > 0) {
            mount.recoils[mount.barrelCounter % recoils] = 1f;
        }
        mount.heat = 1f;
    }
}
