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

public class SupperExplosionBulletType extends ExplosionBulletType {
    public SupperExplosionBulletType(float splashDamage, float splashDamageRadius) {
        super(splashDamage, splashDamageRadius);
    }

    @Override
    public void init(Bullet b) {
        if (instantDisappear) {
            b.time = lifetime + 1f;
        }

        if (spawnBullets.size > 0) {
            for (var bullet : spawnBullets) {
                bullet.create(b, b.x, b.y, b.rotation());
            }
        }
    }

    @Override
    public Bullet create(Entityc owner, Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY) {
        if (!Mathf.chance(createChance)) return null;
        if (ignoreSpawnAngle) angle = 0;
        if (spawnUnit != null) {
            if (!net.client()) {
                Unit spawned = spawnUnit.create(team);
                spawned.set(x, y);
                spawned.rotation = angle;
                if (spawnUnit.missileAccelTime <= 0f) {
                    spawned.vel.trns(angle, spawnUnit.speed);
                }
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
            if (killShooter && owner instanceof Healthc h && !h.dead()) h.kill();
            return null;
        }

        Bullet bullet = Bullet.create();
        bullet.type = this;
        bullet.owner = shooter;
        bullet.team = team;
        bullet.time = 0f;
        bullet.originX = x;
        bullet.originY = y;
        if (!(aimX == -1f && aimY == -1f)) {
            bullet.aimTile = world.tileWorld(aimX, aimY);
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
        bullet.damage = (damage < 0 ? this.damage : damage) * bullet.damageMultiplier();
        if (bullet.trail != null) {
            bullet.trail.clear();
        }
        bullet.add();

        if (keepVelocity && owner instanceof Velc v) bullet.vel.add(v.vel());
        if (killShooter && owner instanceof Healthc h && !h.dead()) h.kill();
        return bullet;
    }
}
