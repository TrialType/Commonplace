package Commonplace.Entities.BulletType;

import arc.math.Mathf;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.ai.types.MissileAI;
import mindustry.entities.Mover;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.blocks.ControlBlock;

import static mindustry.Vars.net;
import static mindustry.Vars.world;

public class PercentExplosionBulletType extends ExplosionBulletType {
    public float percent = 30;
    public float minPercent = 15;

    public float damageMultiplierMin = 0.8f;
    public float damageMultiplierMax = 1.5f;

    public PercentExplosionBulletType(float splashDamage, float splashDamageRadius) {
        super(splashDamage, splashDamageRadius);

        killShooter = false;
    }

    @Override
    public void init(Bullet b) {
        if (b.owner() instanceof Healthc h && !h.dead()) {
            if (killShooter) {
                h.kill();
            } else if (percent > 0) {
                float damage = minPercent >= 0 && b.owner instanceof Statusc s ?
                        Math.max(minPercent * s.healthMultiplier() * h.maxHealth(), percent * h.maxHealth()) / 100f :
                        percent * h.maxHealth() / 100f;
                if (b.owner() instanceof Shieldc s && s.shield() > 0) {
                    float shield = s.shield();
                    s.shield(0);
                    h.damagePierce(damage);
                    s.shield(shield);
                } else {
                    h.damagePierce(damage);
                }
            }
        }

        if (instantDisappear) {
            b.time = lifetime + 1f;
        }

        if (spawnBullets.size > 0) {
            for (var bullet : spawnBullets) {
                bullet.create(b, b.x, b.y, b.rotation());
            }
        }
    }

    public @Nullable Bullet create(
            @Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl,
            float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY, @Nullable Teamc target
    ) {
        if (!Mathf.chance(createChance)) return null;
        if (ignoreSpawnAngle) angle = 0;
        if (spawnUnit != null) {
            //don't spawn units clientside!
            if (!net.client()) {
                Unit spawned = spawnUnit.create(team);
                spawned.set(x, y);
                spawned.rotation = angle;
                //immediately spawn at top speed, since it was launched
                if (spawnUnit.missileAccelTime <= 0f) {
                    spawned.vel.trns(angle, spawnUnit.speed);
                }
                //assign unit owner
                if (spawned.controller() instanceof MissileAI ai) {
                    if (shooter instanceof Unit unit) {
                        ai.shooter = unit;
                    }

                    if (shooter instanceof ControlBlock control) {
                        ai.shooter = control.unit();
                    }

                }
                spawned.add();
            }
            //Since bullet init is never called, handle killing shooter here
            if (killShooter && owner instanceof Healthc h && !h.dead()) h.kill();

            //no bullet returned
            return null;
        }

        Bullet bullet = Bullet.create();
        bullet.type = this;
        bullet.owner = owner;
        bullet.shooter = (shooter == null ? owner : shooter);
        bullet.team = team;
        bullet.time = 0f;
        bullet.originX = x;
        bullet.originY = y;
        if (!(aimX == -1f && aimY == -1f)) {
            bullet.aimTile = target instanceof Building b ? b.tile : world.tileWorld(aimX, aimY);
        }
        bullet.aimX = aimX;
        bullet.aimY = aimY;

        bullet.initVel(angle, speed * velocityScl);
        if (backMove) {
            bullet.set(x - bullet.vel.x * Time.delta, y - bullet.vel.y * Time.delta);
        } else {
            bullet.set(x, y);
        }
        bullet.lifetime = lifetime * lifetimeScl;
        bullet.data = data;
        bullet.drag = drag;
        bullet.hitSize = hitSize;
        bullet.mover = mover;
        bullet.damage = (owner instanceof Healthc h ?
                Mathf.randomSeed((owner.id() + 17) * (long) h.health(),
                        1 - (1 - damageMultiplierMin) * h.healthf(),
                        1 + (damageMultiplierMax - 1) * h.healthf()) : 1) *
                (damage < 0 ? this.damage : damage) * bullet.damageMultiplier();
        //reset trail
        if (bullet.trail != null) {
            bullet.trail.clear();
        }
        bullet.add();

        if (keepVelocity && owner instanceof Velc v) bullet.vel.add(v.vel());
        return bullet;
    }
}
