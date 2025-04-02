package Commonplace.Entities.Block;

import arc.Events;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.Damage;
import mindustry.entities.Lightning;
import mindustry.entities.TargetPriority;
import mindustry.game.EventType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Statusc;
import mindustry.type.StatusEffect;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.BlockFlag;

public class Decoy extends Wall {
    public float adaptDamageMax = 1.25f;
    public float adaptDamageMin = 0.75f;
    public float adaptability = 0;
    public float flexibility = 0;
    public float farDeflect = 0f;
    public float farDeflectChance = 0;
    public boolean adaptLoss = true;
    public float hitStatusDuration = 0;
    public StatusEffect hitStatus = null;

    public Decoy(String name) {
        super(name);

        flags = EnumSet.of(BlockFlag.battery, BlockFlag.factory, BlockFlag.generator, BlockFlag.reactor);
        priority = TargetPriority.core;
    }

    public class DecoyBuild extends WallBuild {
        public float adapt = 0;

        @Override
        public boolean collision(Bullet b) {
            float damage = b.damage() * b.type().buildingDamageMultiplier;
            if (farDeflect > 0 && b.owner instanceof Healthc h && Mathf.chance(farDeflectChance)) {
                h.damage(farDeflect * damage);
                if (hitStatus != null && b.owner instanceof Statusc s) {
                    s.apply(hitStatus, hitStatusDuration);
                }
            }

            if (adaptLoss) {
                float time = timer.getTime(0);
                timer.reset(0, 0);
                adapt -= time / flexibility * 60;
                adapt = Math.max(adapt, 0);
            }

            if (adapt < damage * adaptDamageMax && adapt < flexibility) {
                adapt += adaptability * damage;
                adapt = Math.min(adapt, flexibility);
            }

            return superCollision(b, findDamage(damage));
        }

        public boolean superCollision(Bullet b, float damage) {
            boolean wasDead = this.health <= 0.0F;
            if (!b.type.pierceArmor) {
                damage = Damage.applyArmor(damage, this.block.armor);
            }

            this.damage(b.team, damage);
            Events.fire(bulletDamageEvent.set(this, b));
            if (this.health <= 0.0F && !wasDead) {
                Events.fire(new EventType.BuildingBulletDestroyEvent(this, b));
            }

            if (lightningChance > 0f) {
                if (Mathf.chance(lightningChance)) {
                    Lightning.create(team, lightningColor, lightningDamage, x, y, b.rotation() + 180f, lightningLength);
                    lightningSound.at(tile, Mathf.random(0.9f, 1.1f));
                }
            }

            if (chanceDeflect > 0f) {
                if (b.vel.len() <= 0.1f || !b.type.reflectable) return true;

                if (!Mathf.chance(chanceDeflect / b.damage())) return true;

                deflectSound.at(tile, Mathf.random(0.9f, 1.1f));

                b.trns(-b.vel.x, -b.vel.y);

                float penX = Math.abs(x - b.x), penY = Math.abs(y - b.y);

                if (penX > penY) {
                    b.vel.x *= -1;
                } else {
                    b.vel.y *= -1;
                }

                b.owner = this;
                b.team = team;
                b.time += 1f;

                return false;
            }

            return true;
        }

        public float findDamage(float damage) {
            if (damage < 0) {
                return 0;
            }
            if (damage * adaptDamageMin < adapt) {
                return damage * damage * adaptDamageMin / adapt;
            }
            return damage;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(adapt);
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read);
            adapt = read.f();
        }
    }
}
