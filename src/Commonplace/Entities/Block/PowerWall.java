package Commonplace.Entities.Block;

import arc.Events;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Lightning;
import mindustry.game.EventType;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.Wall;

public class PowerWall extends Wall {
    public boolean powerUsing = true;
    public float healChance = 1;
    public float healPercent = 0.1f;
    public float powerHit = 2.5f;

    public PowerWall(String name) {
        super(name);

        hasPower = true;
        connectedPower = true;
        consumesPower = true;
        conductivePower = true;
    }

    public class PowerWallBuild extends WallBuild {
        @Override
        public boolean collision(Bullet bullet) {
            hit = 1f;
            boolean wasDead = this.health <= 0.0F;
            float damage = bullet.damage() * bullet.type().buildingDamageMultiplier;
            if (!bullet.type.pierceArmor) {
                damage = Damage.applyArmor(damage, this.block.armor);
            }
            this.damage(bullet.team, damage);
            Events.fire(bulletDamageEvent.set(this, bullet));
            if (this.health <= 0.0F && !wasDead) {
                Events.fire(new EventType.BuildingBulletDestroyEvent(this, bullet));
            }
            if (power.graph.getLastPowerProduced() + power.graph.getLastPowerStored() >= powerHit) {
                power.graph.useBatteries(powerHit);
                if (health > 0) {
                    if (Mathf.chance(healChance) && health < maxHealth) {
                        Fx.healBlockFull.at(x, y, rotation * 90, Pal.heal, block);
                        health = Math.min(health + maxHealth * healPercent, maxHealth);
                    }
                }
                if (lightningChance > 0f) {
                    if (Mathf.chance(lightningChance)) {
                        Lightning.create(team, lightningColor, lightningDamage, x, y, bullet.rotation() + 180f, lightningLength);
                        lightningSound.at(tile, Mathf.random(0.9f, 1.1f));
                    }
                }
                if (chanceDeflect > 0f) {
                    if (bullet.vel.len() <= 0.1f || !bullet.type.reflectable) return true;
                    if (!Mathf.chance(chanceDeflect / bullet.damage())) return true;
                    deflectSound.at(tile, Mathf.random(0.9f, 1.1f));
                    bullet.trns(-bullet.vel.x, -bullet.vel.y);
                    float penX = Math.abs(x - bullet.x), penY = Math.abs(y - bullet.y);
                    if (penX > penY) {
                        bullet.vel.x *= -1;
                    } else {
                        bullet.vel.y *= -1;
                    }
                    bullet.owner = this;
                    bullet.team = team;
                    bullet.time += 1f;
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean shouldConsume() {
            return powerUsing;
        }
    }
}
