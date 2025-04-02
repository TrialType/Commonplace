package Commonplace.Entities.BulletType;

import Commonplace.Loader.Special.Effects;
import Commonplace.Utils.Classes.Damage2;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.types.MissileAI;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Mover;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.Build;
import mindustry.world.blocks.ControlBlock;

import static mindustry.Vars.net;
import static mindustry.Vars.world;

public class MoveLightningBulletType extends LightningBulletType {
    public int damagePoints = 2;
    public int points = 20;

    public MoveLightningBulletType() {
        super();

        collides = reflectable = absorbable = hittable = false;
        pierce = pierceBuilding = true;
        pierceCap = -1;
    }

    @Override
    public void update(Bullet bullet) {
        //noinspection rawtypes
        if (bullet.data instanceof Seq vs && vs.size > 0) {
            int from, to, index = (int) ((vs.size + damagePoints) * bullet.fin());
            if (index <= damagePoints) {
                from = 0;
                to = Math.min(index, vs.size) - 1;
            } else if (index >= vs.size) {
                from = index - 1 - damagePoints;
                to = vs.size - 1;
            } else {
                from = index - 1 - damagePoints;
                to = index - 1;
            }

            if (to >= from) {
                Seq<Vec2> show = new Seq<>();
                for (int i = from; i <= to; i++) {
                    show.add((Vec2) vs.get(i));
                }
                Vec2 f, t;
                for (int i = 0; i < show.size - 1; i++) {
                    f = show.get(i);
                    t = show.get(i + 1);
                    boolean absorb = Damage2.collideLineMoveLightning(bullet, bullet.team, hitEffect, f.x, f.y, f.angleTo(t), f.dst(t), false, pierceCap);

                    if (absorb) {
                        if (i == 0) {
                            bullet.remove();
                            return;
                        }

                        show.truncate(i + 1);
                        break;
                    }
                }
                Fx.lightning.at(show.first().x, show.first().y, 0, lightningColor, show);
            }
        }
    }

    @Override
    public float estimateDPS() {
        return damage * 60 * damagePoints / lightningLength;
    }

    @Override
    public Bullet create(Entityc owner, Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, Mover mover, float aimX, float aimY) {
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
        bullet.owner = owner;
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
        bullet.drag = drag;
        bullet.hitSize = hitSize;
        bullet.mover = mover;
        bullet.damage = (damage < 0 ? this.damage : damage) * bullet.damageMultiplier();
        if (bullet.trail != null) {
            bullet.trail.clear();
        }
        bullet.add();

        if (keepVelocity && owner instanceof Velc v) bullet.vel.add(v.vel());

        Seq<Vec2> point = new Seq<>();
        Effects.findLineLightningPoints(bullet.x, bullet.y, bullet.rotation(), 6f, lightningLength, points, point);
        bullet.data = point;

        return bullet;
    }
}
