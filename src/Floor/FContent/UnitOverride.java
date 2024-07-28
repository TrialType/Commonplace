package Floor.FContent;

import Floor.FAI.MissileAI_II;
import Floor.FEntities.FAbility.LevelSign;
import Floor.FEntities.FAbility.TimeLargeDamageAbility;
import Floor.FEntities.FBulletType.ContinuousLightningBulletType;
import Floor.FEntities.FBulletType.FlyContinuousLaserBulletType;
import Floor.FEntities.FBulletType.MissileExplosionBulletType;
import Floor.FEntities.FUnit.F.TimeUpGradeUnit;
import Floor.FEntities.FUnit.Override.*;
import Floor.FEntities.FWeapon.SuctionWeapon;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Rand;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.Effect;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.pattern.ShootPattern;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Sounds;
import mindustry.gen.TimedKillUnit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class UnitOverride {
    static Weapon weapon;
    static BulletType bullet;
    static Color color;

    public static void load() {
        UnitTypes.alpha.buildSpeed = 1f;
        UnitTypes.alpha.mineSpeed = 8f;
        UnitTypes.beta.buildSpeed = 1.5f;
        UnitTypes.beta.mineSpeed = 9f;
        UnitTypes.gamma.buildSpeed = 2f;
        UnitTypes.gamma.mineSpeed = 12f;

        UnitTypes.dagger.constructor = FMechUnit::create;
        UnitTypes.dagger.abilities.add(new LevelSign());
        UnitTypes.mace.constructor = FMechUnit::create;
        UnitTypes.mace.abilities.add(new LevelSign());
        UnitTypes.fortress.constructor = FMechUnit::create;
        UnitTypes.fortress.abilities.add(new LevelSign());
        UnitTypes.scepter.constructor = FMechUnit::create;
        UnitTypes.scepter.abilities.add(new LevelSign());
        UnitTypes.reign.constructor = FMechUnit::create;
        UnitTypes.reign.abilities.add(new LevelSign());

        UnitTypes.nova.constructor = FMechUnitLegacyNova::create;
        UnitTypes.nova.abilities.add(new LevelSign());
        UnitTypes.pulsar.constructor = FMechUnitLegacyNova::create;
        UnitTypes.pulsar.abilities.add(new LevelSign());
        UnitTypes.quasar.constructor = FMechUnitLegacyNova::create;
        UnitTypes.quasar.abilities.add(new LevelSign());
        UnitTypes.vela.constructor = FMechUnitLegacyNova::create;
        UnitTypes.vela.abilities.add(new LevelSign());
        UnitTypes.corvus.constructor = FMechUnitLegacyNova::create;
        UnitTypes.corvus.abilities.add(new LevelSign());

        UnitTypes.crawler.constructor = FMechUnit::create;
        UnitTypes.crawler.abilities.add(new LevelSign());
        UnitTypes.atrax.constructor = FLegsUnit::create;
        UnitTypes.atrax.abilities.add(new LevelSign());
        UnitTypes.spiroct.constructor = FLegsUnit::create;
        UnitTypes.spiroct.abilities.add(new LevelSign());
        UnitTypes.arkyid.constructor = FLegsUnit::create;
        UnitTypes.arkyid.abilities.add(new LevelSign());
        UnitTypes.toxopid.constructor = FLegsUnit::create;
        UnitTypes.toxopid.abilities.add(new LevelSign());

        UnitTypes.flare.constructor = FUnitEntity::create;
        UnitTypes.flare.abilities.add(new LevelSign());
        UnitTypes.horizon.constructor = FUnitEntity::create;
        UnitTypes.horizon.abilities.add(new LevelSign());
        UnitTypes.zenith.constructor = FUnitEntity::create;
        UnitTypes.zenith.abilities.add(new LevelSign());
        UnitTypes.antumbra.constructor = FUnitEntity::create;
        UnitTypes.antumbra.abilities.add(new LevelSign());
        UnitTypes.eclipse.constructor = TimeUpGradeUnit::create;
        UnitTypes.eclipse.abilities.add(new LevelSign());

        UnitTypes.poly.constructor = FUnitEntity::create;
        UnitTypes.poly.abilities.add(new LevelSign());
        UnitTypes.mega.constructor = FPayloadUnit::create;
        UnitTypes.mega.abilities.add(new LevelSign());
        UnitTypes.quad.constructor = FPayloadUnit::create;
        UnitTypes.quad.abilities.add(new LevelSign());
        UnitTypes.oct.constructor = FPayloadUnit::create;
        UnitTypes.oct.abilities.add(new LevelSign());

        UnitTypes.risso.constructor = FUnitWaterMove::create;
        UnitTypes.risso.abilities.add(new LevelSign());
        UnitTypes.minke.constructor = FUnitWaterMove::create;
        UnitTypes.minke.abilities.add(new LevelSign());
        UnitTypes.bryde.constructor = FUnitWaterMove::create;
        UnitTypes.bryde.abilities.add(new LevelSign());
        UnitTypes.sei.constructor = FUnitWaterMove::create;
        UnitTypes.sei.abilities.add(new LevelSign());
        UnitTypes.omura.constructor = FUnitWaterMove::create;
        UnitTypes.omura.abilities.add(new LevelSign());

        UnitTypes.retusa.constructor = FUnitWaterMove::create;
        UnitTypes.retusa.abilities.add(new LevelSign());
        UnitTypes.oxynoe.constructor = FUnitWaterMove::create;
        UnitTypes.oxynoe.abilities.add(new LevelSign());
        UnitTypes.cyerce.constructor = FUnitWaterMove::create;
        UnitTypes.cyerce.abilities.add(new LevelSign());
        UnitTypes.aegires.constructor = FUnitWaterMove::create;
        UnitTypes.aegires.abilities.add(new LevelSign());
        UnitTypes.navanax.constructor = FUnitWaterMove::create;
        UnitTypes.navanax.abilities.add(new LevelSign());

        UnitTypes.stell.constructor = FTankUnit::create;
        UnitTypes.stell.abilities.add(new LevelSign());
        UnitTypes.locus.constructor = FTankUnit::create;
        UnitTypes.locus.abilities.add(new LevelSign());
        UnitTypes.precept.constructor = FTankUnit::create;
        UnitTypes.precept.abilities.add(new LevelSign());
        UnitTypes.vanquish.constructor = FTankUnit::create;
        UnitTypes.vanquish.abilities.add(new LevelSign());
        UnitTypes.conquer.constructor = FTankUnit::create;
        UnitTypes.conquer.abilities.add(new LevelSign());

        UnitTypes.merui.constructor = FLegsUnit::create;
        UnitTypes.merui.abilities.add(new LevelSign());
        UnitTypes.cleroi.constructor = FLegsUnit::create;
        UnitTypes.cleroi.abilities.add(new LevelSign());
        UnitTypes.anthicus.constructor = FLegsUnit::create;
        UnitTypes.anthicus.abilities.add(new LevelSign());
        UnitTypes.tecta.constructor = FLegsUnit::create;
        UnitTypes.tecta.abilities.add(new LevelSign());
        UnitTypes.collaris.constructor = FLegsUnit::create;
        UnitTypes.collaris.abilities.add(new LevelSign());

        UnitTypes.elude.constructor = FElevationMoveUnit::create;
        UnitTypes.elude.abilities.add(new LevelSign());
        UnitTypes.avert.constructor = FUnitEntity::create;
        UnitTypes.avert.abilities.add(new LevelSign());
        UnitTypes.obviate.constructor = FUnitEntity::create;
        UnitTypes.obviate.abilities.add(new LevelSign());
        UnitTypes.quell.constructor = FPayloadUnit::create;
        UnitTypes.quell.abilities.add(new LevelSign());
        UnitTypes.disrupt.constructor = FPayloadUnit::create;
        UnitTypes.disrupt.abilities.add(new LevelSign());
        /*=================================================================*/
        /*=================================================================*/
        /*=================================================================*/
        /*=================================================================*/
        UnitTypes.dagger.speed = 0.2f;
        weapon = UnitTypes.dagger.weapons.get(0);
        weapon.reload = 45;
        weapon.shoot = new ShootPattern() {{
            shots = 3;
            shotDelay = 10;
        }};
        weapon.bullet = new BasicBulletType() {{
            damage = 8;
            speed = 4;
            lifetime = 90;
            width = height = 8;
            splashDamage = 8;
            splashDamageRadius = 13;
            keepVelocity = false;
        }};

        UnitTypes.mace.health = 1000;
        UnitTypes.mace.armor = 8;
        UnitTypes.mace.weapons.get(0).bullet.incendChance = 1;
        UnitTypes.mace.weapons.get(0).bullet.incendAmount = 2;
        weapon = new Weapon() {{
            reload = 600;
            inaccuracy = 15;
            shootY = 2f;
            mirror = false;
            shootSound = Sounds.flame;
            shoot = new ShootPattern() {{
                shots = 110;
                shotDelay = 3;
            }};

            bullet = new BulletType() {{
                absorbable = reflectable = hittable = false;
                pierce = true;
                pierceCap = 2;
                lifetime = 25;
                speed = 8;
                damage = 7;
                status = StatusEffects.burning;
                statusDuration = 3;
                despawnEffect = hitEffect = Fx.none;
                shootSound = Sounds.none;
                shootEffect = new Effect(32f, 266f, e -> {
                    color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());

                    randLenVectors(e.id, 20, e.finpow() * 200, e.rotation, 15,
                            (x, y) -> Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.5f));
                });
            }};
        }};
        UnitTypes.mace.weapons.add(weapon);
        weapon = weapon.copy();
        weapon.x = -5;
        UnitTypes.mace.weapons.add(weapon);

        UnitTypes.fortress.health = 1800;
        weapon = UnitTypes.fortress.weapons.get(0);
        weapon.bullet.lightning = 6;
        weapon.bullet.lightColor = Pal.bulletYellow;
        weapon.bullet.lightningDamage = 3;
        weapon.bullet.lightningLength = 6;
        weapon.bullet.lightningCone = 360;
        weapon.bullet.incendChance = 0.1f;
        weapon.bullet.incendAmount = 1;
        UnitTypes.fortress.weapons.add(new Weapon() {{
            reload = 18;
            rotate = true;
            rotateSpeed = 10;

            bullet = new BasicBulletType() {{
                width = height = 7;
                damage = 8;
                speed = 2;
                lifetime = 120;
                collidesTiles = false;
            }};
        }});

        UnitTypes.scepter.health = 31500;
        UnitTypes.scepter.weapons.get(0).bullet.damage = 150;
        UnitTypes.scepter.weapons.get(0).bullet.lightningDamage = 60;
        UnitTypes.scepter.weapons.get(1).bullet.damage = 30;

        UnitTypes.reign.health = 84000;
        UnitTypes.reign.weapons.get(0).bullet.damage = 240;
        UnitTypes.reign.weapons.get(0).bullet.splashDamage = 54;
        UnitTypes.reign.weapons.get(0).bullet.fragBullet.damage = 60;
        UnitTypes.reign.weapons.get(0).bullet.fragBullet.splashDamage = 45;
        UnitTypes.reign.weapons.get(0).bullet.fragBullets = 6;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.crawler.health = 350;
        UnitTypes.crawler.speed = 2.5f;
        UnitTypes.crawler.abilities.add(new StatusFieldAbility(FStatusEffects.swift, 900, 900, 1));
        weapon = UnitTypes.crawler.weapons.get(0);
        weapon.reload = 30;
        weapon.bullet.killShooter = false;
        weapon.bullet.splashDamageRadius = 60;
        weapon.bullet.buildingDamageMultiplier = 2.5f;
        weapon.bullet.shootEffect = new ExplosionEffect() {{
            lifetime = 25;
            clip = 70;

            smokes = 24;
            smokeRad = 70;
            smokeSize = 4;
            smokeSizeBase = 0;
            smokeColor = Color.valueOf("EEAA88ff");

            sparks = 36;
            sparkRad = 70;
            sparkLen = 4;
            sparkColor = Color.valueOf("EEAA88ff");

            waveLife = 25;
            waveStroke = 2;
            waveRad = 80;
            waveRadBase = 0;
            waveColor = Color.valueOf("EEAA88ff");
        }};

        UnitTypes.atrax.speed = 0.3f;
        UnitTypes.atrax.health = 1200;
        UnitTypes.atrax.armor = 8f;
        UnitTypes.atrax.targetAir = true;
        weapon = UnitTypes.atrax.weapons.get(0);
        weapon.shoot = new ShootSpread(6, 30) {{
            shotDelay = 1;
        }};
        weapon.bullet.damage = 23;
        weapon.bullet.lifetime = 100;
        weapon.bullet.collidesAir = true;

        UnitTypes.arkyid.health = 28000;

        UnitTypes.toxopid.health = 77000;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.flare.armor = 6;
        UnitTypes.flare.speed = 3;
        UnitTypes.flare.health = 160;
        UnitTypes.flare.circleTarget = true;
        UnitTypes.flare.abilities.add(new ForceFieldAbility(12, 0.2f, 240, 12 * 60));

        UnitTypes.zenith.health = 1500;
        UnitTypes.zenith.armor = 8;
        UnitTypes.zenith.speed = 2;
        UnitTypes.zenith.range = 540;
        weapon = UnitTypes.zenith.weapons.get(0);
        weapon.reload = 300;
        weapon.inaccuracy = 30;
        weapon.shoot = new ShootPattern() {{
            shots = 10;
            shotDelay = 15;
        }};
        MissileBulletType m = (MissileBulletType) weapon.bullet;
        m.speed = 6;
        m.damage = 28;
        m.lifetime = 90;
        m.height = 14;
        m.width = 7;
        m.splashDamageRadius = 25;
        m.splashDamage = 36f;
        m.inaccuracy = 0;
        m.homingDelay = 30;
        m.homingRange = 540;
        m.homingPower = 0.1f;
        m.trailEffect = new Effect(180f, 220f, b -> {
            float intensity = 2f;

            color(b.color, 0.7f);
            for (int i = 0; i < 4; i++) {
                Rand rand = new Rand(b.id * 2L + i);
                float lenScl = rand.random(0.5f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (2.9f * intensity), 7f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                }));
            }
        }).layer(Layer.bullet - 1f);
        m.trailChance = 0.1f;

        UnitTypes.antumbra.health = 25200;

        UnitTypes.eclipse.health = 77000;
        UnitTypes.eclipse.weapons.add(new Weapon() {{
            reload = 480;
            bullet = new MissileExplosionBulletType(0, 0) {{
                withHealth = true;

                hittable = false;
                absorbable = false;
                instantDisappear = true;
                scaledSplashDamage = true;
                collides = false;
                keepVelocity = false;

                lifetime = 0;

                spawnUnit = new UnitType("eclipse1") {{
                    lifetime = 1260;

                    constructor = TimedKillUnit::create;
                    controller = u -> new MissileAI_II();
                    hidden = true;
                    flying = true;
                    health = 38500;
                    speed = 0.1f;
                    armor = 13;
                    weapons.add(new Weapon() {{
                        alwaysShooting = true;
                        controllable = aiControllable = false;
                        reload = 1202;

                        shoot = new ShootPattern() {{
                            firstShotDelay = 1200;
                        }};

                        bullet = new MissileExplosionBulletType(0, 0) {{

                            hittable = false;
                            lifetime = 1f;
                            speed = 0f;
                            rangeOverride = 20f;
                            shootEffect = Fx.massiveExplosion;
                            instantDisappear = true;
                            scaledSplashDamage = true;
                            killShooter = true;
                            collides = false;
                            keepVelocity = false;

                            spawnUnit = new UnitType("eclipse2") {{
                                range = maxRange = 1000;
                                hidden = true;
                                flying = true;
                                targetable = false;
                                hittable = false;
                                speed = 11;
                                lifetime = 1201;

                                trailLength = 12;
                                trailWidth = 4;
                                trailChance = 1;

                                constructor = TimedKillUnit::create;
                                controller = u -> new MissileAI_II();

                                weapons.add(new Weapon() {{
                                    bullet = new ExplosionBulletType(10000, 500) {{
                                        range = rangeOverride = 24;
                                    }};
                                }});
                            }};
                        }};
                    }});
                    weapons.add(new Weapon() {{
                        reload = 20;
                        shoot = new ShootPattern() {{
                            shots = 3;
                        }};
                        bullet = new BasicBulletType() {{
                            inaccuracy = 24;

                            homingRange = 1000;
                            homingDelay = 20;
                            homingPower = 0.07f;
                            lifetime = 120;
                            speed = 5;
                            damage = 120;
                        }};
                    }});
                }};
            }};
        }});

        /*-----------------------------------------------------------------------------*/

        UnitTypes.quad.health = 22000;


        UnitTypes.oct.health = 77000;

        /*-----------------------------------------------------------------------------*/

        UnitTypes.sei.health = 22000;

        UnitTypes.omura.health = 77000;

        /*-----------------------------------------------------------------------------*/

        UnitTypes.aegires.health = 42000;
        UnitTypes.aegires.abilities.add(new TimeLargeDamageAbility(1.95f, 180) {{
            buildingExpand = 0.5f;
        }});

        UnitTypes.navanax.health = 70000;

        /*-----------------------------------------------------------------------------*/

        UnitTypes.precept.health = 17500;

        UnitTypes.vanquish.health = 38500;

        UnitTypes.conquer.health = 77000;

        /*-----------------------------------------------------------------------------*/

        UnitTypes.obviate.health = 8050;

        UnitTypes.quell.health = 22000;
        UnitTypes.quell.weapons.get(0).bullet.spawnUnit.weapons.get(0).bullet.splashDamage = 220f;

        UnitTypes.disrupt.health = 42000;
        UnitTypes.disrupt.weapons.get(0).bullet.spawnUnit.weapons.get(0).bullet.splashDamage = 280f;
        /*-----------------------------------------------------------------------------*/

        UnitTypes.anthicus.health = 10150;
        UnitTypes.anthicus.weapons.get(0).bullet.spawnUnit.weapons.get(0).bullet.splashDamage = 280f;

        UnitTypes.tecta.health = 26550;

        UnitTypes.collaris.health = 63000;
        bullet = UnitTypes.collaris.weapons.get(0).bullet;
        UnitTypes.collaris.targetAir = true;
        bullet.damage = 520;
        bullet.splashDamage = 85f;
        bullet.splashDamageRadius = 20f;
        bullet.bulletInterval = 20;
        bullet.intervalBullets = 3;
        bullet.intervalRandomSpread = 30;
        bullet.intervalAngle = 0;
        bullet.intervalBullet = new BasicBulletType() {{
            lifetime = 180;
            damage = 120;
            speed = 6;
            homingPower = 0.08F;
            homingRange = 1000;
            homingDelay = 30;
            trailChance = 1F;
            trailColor = Pal.techBlue;
            trailWidth = 2.2f;
            trailLength = 30;
        }};
        bullet.fragBullet.damage = 100;
        bullet.fragBullet.splashDamage = 92f;
        bullet.fragBullet.splashDamageRadius = 20;

        /*-----------------------------------------------------------------------------*/
        UnitTypes.nova.speed = 2f;
        UnitTypes.nova.armor = 21f;
        UnitTypes.nova.health = 220;
        UnitTypes.nova.buildSpeed = 0;
        UnitTypes.nova.abilities.add(new RepairFieldAbility(60, 60 * 8, 4));
        color = Color.valueOf("ffa998");
        weapon = UnitTypes.nova.weapons.get(0);
        weapon.reload = 4;
        bullet = weapon.bullet;
        bullet.lifetime = 90;
        bullet.speed = 8;
        bullet.damage = 18;
        bullet.healAmount = 0;
        bullet.healPercent = 0;
        bullet.reflectable = true;
        bullet.collidesTeam = false;
        bullet.smokeEffect = new Effect(8, e -> {
            color(Color.white, color, e.fin());
            stroke(0.5f + e.fout());
            Lines.circle(e.x, e.y, e.fin() * 5f);

            Drawf.light(e.x, e.y, 23f, color, e.fout() * 0.7f);
        });
        bullet.hitEffect = bullet.smokeEffect;
        bullet.despawnEffect = bullet.smokeEffect;
        bullet.lightColor = color;
        ((LaserBoltBulletType) bullet).frontColor = color;
        ((LaserBoltBulletType) bullet).backColor = color;

        UnitTypes.pulsar.health = 560;
        UnitTypes.pulsar.speed = 1.1f;
        UnitTypes.pulsar.range = 150;
        weapon = UnitTypes.pulsar.weapons.get(0);
        weapon.shoot.shots = 5;
        weapon.shoot.shotDelay = 6;
        weapon.reload = 360;
        weapon.inaccuracy = 12;
        weapon.bullet = new BasicBulletType() {{
            absorbable = false;
            collidesTeam = true;
            speed = 9;
            damage = 1;
            lifetime = 50;
            healPercent = 2f;
            width = height = 25;
            homingDelay = 0;
            homingPower = 0.03f;
            homingRange = 600;
            frontColor = backColor = Pal.heal;
            hitEffect = despawnEffect = Fx.none;

            fragOnHit = false;
            fragOnAbsorb = false;
            fragBullets = 1;
            fragBullet = new ContinuousLightningBulletType() {{
                damage = 13;
                lifetime = 260;
                length = 35;

                damageInterval = 60;
                rotateSpeed = 4;

                bulletLightningType.healPercent = 1f;
                bulletLightningType.collidesTeam = true;
            }};
        }};

        UnitTypes.quasar.health = 2200;
        ForceFieldAbility fAbility = (ForceFieldAbility) UnitTypes.quasar.abilities.get(0);
        fAbility.regen = 0.8f;
        fAbility.max = 1000;
        weapon = UnitTypes.quasar.weapons.get(0);
        weapon.reload = 35;
        weapon.bullet.damage = 65;
        UnitTypes.quasar.weapons.add(new Weapon() {{
            x = y = 0;
            mirror = false;
            reload = 120;
            bullet = new BulletType(0, 0) {{
                despawnEffect = hitEffect = Fx.none;
                collides = absorbable = reflectable = hittable = false;
                rangeOverride = 135;
                keepVelocity = false;
                status = FStatusEffects.tardy;
                statusDuration = 180;
                splashDamageRadius = 135;
                shootEffect = new Effect(45, e -> {
                    color(Pal.heal);
                    stroke(e.fout() * 2f);
                    Lines.circle(e.x, e.y, 4f + e.finpow() * 135);
                });
            }};
        }});

        UnitTypes.vela.health = 22000;
        UnitTypes.vela.weapons.get(0).bullet = new FlyContinuousLaserBulletType() {{
            damage = 35f;
            length = 180f;
            hitEffect = Fx.hitMeltHeal;
            drawSize = 420f;
            lifetime = 160f;
            shake = 1f;
            despawnEffect = Fx.smokeCloud;
            smokeEffect = Fx.none;

            chargeEffect = Fx.greenLaserChargeSmall;

            incendChance = 0.1f;
            incendSpread = 5f;
            incendAmount = 1;

            healPercent = 1f;
            collidesTeam = true;

            colors = new Color[]{Pal.heal.cpy().a(.2f), Pal.heal.cpy().a(.5f), Pal.heal.cpy().mul(1.2f), Color.white};
        }};
        UnitTypes.vela.rotateSpeed = 5.4F;

        UnitTypes.corvus.health = 77000;
        UnitTypes.corvus.weapons.remove(0);
        UnitTypes.corvus.weapons.add(new SuctionWeapon("corvus-weapon") {{
            range = 200;
            time = 600;

            shootSound = Sounds.laserblast;
            chargeSound = Sounds.lasercharge;
            soundPitchMin = 1f;
            top = false;
            mirror = false;
            shake = 14f;
            shootY = 5f;
            x = y = 0;
            reload = 350f;
            recoil = 0f;

            cooldownTime = 350f;

            shootStatusDuration = 60f * 2f;
            shootStatus = StatusEffects.unmoving;
            shoot.firstShotDelay = Fx.greenLaserCharge.lifetime;
            parentizeEffects = true;

            bullet = new LaserBulletType() {{
                length = 460f;
                damage = 1120f;
                width = 75f;

                lifetime = 65f;

                lightningSpacing = 35f;
                lightningLength = 25;
                lightningDelay = 1.1f;
                lightningLengthRand = 5;
                lightningDamage = 150;
                lightningAngleRand = 40f;
                largeHit = true;
                lightColor = lightningColor = Pal.heal;

                chargeEffect = Fx.greenLaserCharge;

                healPercent = 25f;
                collidesTeam = true;

                sideAngle = 15f;
                sideWidth = 0f;
                sideLength = 0f;
                colors = new Color[]{Pal.heal.cpy().a(0.4f), Pal.heal, Color.white};
            }};
        }});

        /*-----------------------------------------------------------------------------*/
    }
}
