package Commonplace.Content.Override;

import Commonplace.AI.MissileAI_II;
import Commonplace.Content.SpecialContent.Effects;
import Commonplace.Content.DefaultContent.FStatusEffects;
import Commonplace.Entities.FAbility.SprintingAbility2;
import Commonplace.Entities.FAbility.TimeLargeDamageAbility;
import Commonplace.Entities.FBulletType.*;
import Commonplace.Entities.FUnit.F.LongLifeUnitEntity;
import Commonplace.Entities.FUnit.Override.*;
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
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.pattern.*;
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
import static mindustry.Vars.tilePayload;

public class UnitOverride {
    static Weapon weapon;
    static Color color;

    public static void load() {
        UnitTypes.alpha.buildSpeed = 1f;
        UnitTypes.alpha.mineSpeed = 8f;
        UnitTypes.beta.buildSpeed = 1.5f;
        UnitTypes.beta.mineSpeed = 9f;
        UnitTypes.gamma.buildSpeed = 2f;
        UnitTypes.gamma.mineSpeed = 12f;

        UnitTypes.dagger.constructor = FMechUnit::create;
        UnitTypes.mace.constructor = FMechUnit::create;
        UnitTypes.fortress.constructor = FMechUnit::create;
        UnitTypes.scepter.constructor = FMechUnit::create;
        UnitTypes.reign.constructor = FMechUnit::create;

        UnitTypes.nova.constructor = FMechUnitLegacyNova::create;
        UnitTypes.pulsar.constructor = FMechUnitLegacyNova::create;
        UnitTypes.quasar.constructor = FMechUnitLegacyNova::create;
        UnitTypes.vela.constructor = FMechUnitLegacyNova::create;
        UnitTypes.corvus.constructor = FMechUnitLegacyNova::create;

        UnitTypes.crawler.constructor = FMechUnit::create;
        UnitTypes.atrax.constructor = FLegsUnit::create;
        UnitTypes.spiroct.constructor = FLegsUnit::create;
        UnitTypes.arkyid.constructor = FLegsUnit::create;
        UnitTypes.toxopid.constructor = FLegsUnit::create;

        UnitTypes.flare.constructor = FUnitEntity::create;
        UnitTypes.horizon.constructor = FUnitEntity::create;
        UnitTypes.zenith.constructor = FUnitEntity::create;
        UnitTypes.antumbra.constructor = FUnitEntity::create;
        UnitTypes.eclipse.constructor = FUnitEntity::create;

        UnitTypes.poly.constructor = LongLifeUnitEntity::create;
        UnitTypes.mega.constructor = FPayloadUnit::create;
        UnitTypes.quad.constructor = FPayloadUnit::create;
        UnitTypes.oct.constructor = FPayloadUnit::create;

        UnitTypes.risso.constructor = FUnitWaterMove::create;
        UnitTypes.minke.constructor = FUnitWaterMove::create;
        UnitTypes.bryde.constructor = FUnitWaterMove::create;
        UnitTypes.sei.constructor = FUnitWaterMove::create;
        UnitTypes.omura.constructor = FUnitWaterMove::create;

        UnitTypes.retusa.constructor = FUnitWaterMove::create;
        UnitTypes.oxynoe.constructor = FUnitWaterMove::create;
        UnitTypes.cyerce.constructor = FUnitWaterMove::create;
        UnitTypes.aegires.constructor = FUnitWaterMove::create;
        UnitTypes.navanax.constructor = FUnitWaterMove::create;

        UnitTypes.stell.constructor = FTankUnit::create;
        UnitTypes.locus.constructor = FTankUnit::create;
        UnitTypes.precept.constructor = FTankUnit::create;
        UnitTypes.vanquish.constructor = FTankUnit::create;
        UnitTypes.conquer.constructor = FTankUnit::create;

        UnitTypes.merui.constructor = FLegsUnit::create;
        UnitTypes.cleroi.constructor = FLegsUnit::create;
        UnitTypes.anthicus.constructor = FLegsUnit::create;
        UnitTypes.tecta.constructor = FLegsUnit::create;
        UnitTypes.collaris.constructor = FLegsUnit::create;

        UnitTypes.elude.constructor = FElevationMoveUnit::create;
        UnitTypes.avert.constructor = FUnitEntity::create;
        UnitTypes.obviate.constructor = FUnitEntity::create;
        UnitTypes.quell.constructor = FPayloadUnit::create;
        UnitTypes.disrupt.constructor = FPayloadUnit::create;
        /*===================================================================================================*/
        /*===================================================================================================*/
        /*===================================================================================================*/
        /*===================================================================================================*/
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
        UnitTypes.mace.weapons.get(0).shootCone = 200;
        UnitTypes.mace.weapons.get(0).bullet.incendChance = 1;
        UnitTypes.mace.weapons.get(0).bullet.incendAmount = 2;
        weapon = new Weapon() {{
            reload = 600;
            inaccuracy = 15;
            shootY = 2f;
            x = y = 0;
            mirror = false;
            shootSound = Sounds.flame;
            shoot = new ShootMulti(new ShootPattern() {{
                shots = 110;
                shotDelay = 3;
            }}, new ShootBarrel() {{
                barrels = new float[]{
                        5, 0, 0,
                        -5, 0, 0
                };
                shots = 2;
            }});
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
                shootSound = Sounds.flame;
                shootEffect = new Effect(32f, 266f, e -> {
                    color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());

                    randLenVectors(e.id, 20, e.finpow() * 200, e.rotation, 15,
                            (x, y) -> Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.5f));
                });
            }};
        }};
        UnitTypes.mace.weapons.add(weapon);

        UnitTypes.fortress.health = 1800;
        weapon = UnitTypes.fortress.weapons.get(0);
        weapon.bullet.lightning = 6;
        weapon.bullet.lightColor = Pal.bulletYellow;
        weapon.bullet.lightningDamage = 13;
        weapon.bullet.lightningLength = 6;
        weapon.bullet.lightningCone = 360;
        weapon.bullet.incendChance = 0.1f;
        weapon.bullet.incendAmount = 3;
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
        weapon.bullet.homingDelay = 15;
        weapon.bullet.homingPower = 0.07f;
        weapon.bullet.homingRange = 200;

        UnitTypes.spiroct.health = 1500;
        UnitTypes.spiroct.armor = 21;
        weapon = UnitTypes.spiroct.weapons.get(0);
        SapBulletType s = (SapBulletType) weapon.bullet;
        s.length = 100;
        s.damage = 33;
        weapon = UnitTypes.spiroct.weapons.get(1);
        s = (SapBulletType) weapon.bullet;
        s.length = 125;
        s.damage = 28;
        s.fragBullets = 1;
        s.fragBullet = new EmpBulletType() {{
            hittable = reflectable = absorbable = false;
            damage = 6;
            speed = 0;
            lifetime = 0.001f;
            radius = 25;
            timeIncrease = 1;
            timeDuration = 120;
            powerDamageScl = 1.6f;
            powerSclDecrease = 0.3f;
            hitUnits = false;
            hitColor = Color.valueOf("bf92f9");
        }};

        UnitTypes.arkyid.health = 28000;

        UnitTypes.toxopid.health = 77000;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.flare.armor = 15;
        UnitTypes.flare.speed = 4;
        UnitTypes.flare.health = 200;
        UnitTypes.flare.circleTarget = true;
        weapon = UnitTypes.flare.weapons.first();
        weapon.shoot.shots = 2;
        weapon.shoot.shotDelay = 6;
        weapon.bullet = new FireBulletType(4, 9) {{
            lifetime = 45;
            ammoMultiplier = 2;
            hittable = absorbable = reflectable = false;
            collidesTiles = true;
            collides = true;
            pierce = false;
            pierceBuilding = true;
            buildingDamageMultiplier = 2f;
            fireTrailChance = 1;
            velMin = velMax = speed;
        }};

        UnitTypes.horizon.health = 500;
        UnitTypes.horizon.armor = 5;
        UnitTypes.horizon.speed = 2.15f;
        weapon = UnitTypes.horizon.weapons.get(0);
        weapon.shootStatus = FStatusEffects.frenzy;
        weapon.shootStatusDuration = 60;
        weapon.shoot.shots = 4;
        weapon.shoot.shotDelay = 4;
        weapon.bullet.buildingDamageMultiplier = 1.65f;

        UnitTypes.zenith.health = 1500;
        UnitTypes.zenith.armor = 8;
        UnitTypes.zenith.speed = 2;
        UnitTypes.zenith.range = 540;
        weapon = UnitTypes.zenith.weapons.get(0);
        weapon.reload = 300;
        weapon.inaccuracy = 30;
        weapon.shoot.shots = 10;
        weapon.shoot.shotDelay = 15;
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
            for (int i = 0; i < 2; i++) {
                Rand rand = new Rand(b.id * 2L + i);
                float lenScl = rand.random(0.5f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (2.9f * intensity), 4f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                }));
            }
        }).layer(Layer.bullet - 1f);
        m.trailChance = 0.1f;

        UnitTypes.antumbra.health = 25200;
        UnitTypes.antumbra.armor = 12;
        UnitTypes.antumbra.abilities.add(new SprintingAbility2() {{
            sprintingReload = 240;
            sprintingDamage = 100;
            sprintingDuration = 10;
            sprintingLength = 10;
            sprintingRadius = 150;
            rotate = false;
        }});
        weapon = UnitTypes.antumbra.weapons.first();
        weapon.bullet.damage *= 1.2f;
        weapon.bullet.status = StatusEffects.slow;
        weapon.bullet.statusDuration = 60;

        UnitTypes.eclipse.health = 77000;
        UnitTypes.eclipse.weapons.add(new Weapon() {{
            reload = 480;
            bullet = new MissileExplosionBulletType(0, 0) {{
                rangeOverride = 300;

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

                    range = 600;

                    constructor = TimedKillUnit::create;
                    controller = u -> new MissileAI_II();
                    hidden = true;
                    flying = true;
                    shootOnDeath = false;
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
                        shoot = new ShootAlternate() {{
                            shots = 3;
                            barrels = 3;
                            shotDelay = 5f;
                        }};
                        bullet = new BasicBulletType() {{
                            homingRange = 600;
                            homingDelay = 80;
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

        UnitTypes.poly.health = 700;
        UnitTypes.poly.buildSpeed = 1;
        UnitTypes.poly.weapons.get(0).bullet.splashDamageRadius = 20;


        UnitTypes.mega.health = 4000;
        UnitTypes.mega.armor = 45;
        UnitTypes.mega.payloadCapacity = 3 * 3 * tilePayload;


        UnitTypes.quad.health = 22000;
        UnitTypes.quad.speed = 2;
        UnitTypes.quad.range = 270;
        UnitTypes.quad.payloadCapacity = 4 * 4 * tilePayload;
        weapon = UnitTypes.quad.weapons.get(0);
        weapon.ignoreRotation = true;
        weapon.shootCone = 180f;
        weapon.reload = 300;
        weapon.ejectEffect = Fx.none;
        weapon.bullet = new StrikeBulletType() {{
            sprite = "large-bomb";
            width = height = 960 / 4f;

            maxRange = 300;

            backColor = Pal.heal;
            frontColor = Color.white;
            mixColorTo = Color.white;

            hitSound = Sounds.plasmaboom;
            hitShake = 4f;

            lifetime = 120;

            despawnEffect = Effects.greenBombLarge;
            hitEffect = Fx.massiveExplosion;
            keepVelocity = false;
            spin = 2f;

            shrinkX = shrinkY = 0.7f;

            speed = 0f;
            collides = false;

            hitTeam = true;

            healPercent = 15f;
            splashDamage = 400f;
            splashDamageRadius = 800f;
        }};


        UnitTypes.oct.health = 77000;
        UnitTypes.oct.payloadCapacity = 6.5f * 6.5f * tilePayload;

        /*-----------------------------------------------------------------------------*/
        UnitTypes.risso.health = 430;
        UnitTypes.risso.armor = 7;
        weapon = UnitTypes.risso.weapons.get(0);
        weapon.bullet.damage = 13.5f;
        weapon.bullet.lifetime = 90;
        weapon.bullet.init();
        weapon = UnitTypes.risso.weapons.get(1);
        weapon.bullet.lifetime = 97.5f;
        weapon.bullet.damage = 18;
        weapon.bullet.splashDamage = 15;
        weapon.bullet.fragBullets = 1;
        weapon.bullet.fragBullet = new PointBulletType2() {{
            absorbable = reflectable = hittable = collides = false;

            speed = 0;
            damage = 0;
            lifetime = 90;
            despawnEffect = hitEffect = Fx.none;
            laserDelay = 30;
            laserInterval = 45;
            laserSpreadRandom = 360;
            laserRange = 77;
            laserGroups = 3;
            laserColor = Pal.bulletYellowBack;
            laserEffect = Effects.laserLinkLower;
            laserBulletType = new BulletType(0, 16) {{
                lifetime = 1;
                absorbable = reflectable = hittable = keepVelocity = false;
                despawnEffect = hitEffect = Fx.none;
            }};

            init();
        }};
        weapon.bullet.init();

        UnitTypes.minke.health = 800;
        UnitTypes.minke.armor = 14;
        weapon = UnitTypes.minke.weapons.get(0);
        weapon.reload = 120;
        weapon.bullet = new AroundBulletType() {{
            speed = 4.5f;
            lifetime = 200;
            damage = 54;
            weapon.bullet.splashDamage = 27 * 2.25f;

            trailColor = Pal.bulletYellowBack;
            trailLength = 12;
            trailWidth = 2;

            statusEffect = StatusEffects.slow;
            statusTime = 300;
            targetRange = 300;
            roundIntervalBullets = 1;
            roundBulletInterval = 5;
            roundIntervalBullet = new FlakBulletType(4.2f, 4.5f) {{
                lifetime = 60f;
                ammoMultiplier = 4f;
                shootEffect = Fx.shootSmall;
                width = 6f;
                height = 8f;
                hitEffect = Fx.flakExplosion;
                splashDamage = 18;
                splashDamageRadius = 15f;
                collidesGround = true;
                keepVelocity = false;
            }};
        }};
        weapon.bullet.keepVelocity = false;
        weapon.bullet.init();
        weapon = UnitTypes.minke.weapons.get(1);
        weapon.bullet.damage = 30;
        weapon.bullet.splashDamage = 60;
        weapon.bullet.lifetime = 120;
        weapon.bullet.speed = 4.5f;
        weapon.bullet.init();

        UnitTypes.bryde.health = 1410;
        UnitTypes.bryde.armor = 20;
        ShieldRegenFieldAbility ability = (ShieldRegenFieldAbility) UnitTypes.bryde.abilities.first();
        ability.amount = 100;
        ability.max = 200;
        weapon = UnitTypes.bryde.weapons.get(0);
        weapon.bullet.damage = 22;
        weapon.bullet.splashDamage = 70;
        weapon.bullet.lifetime = 126;
        weapon.bullet.weaveMag = 0.35f;
        weapon.bullet.weaveScale = 30;
        weapon.bullet.weaveRandom = true;
        weapon.bullet.fragBullet = new BasicBulletType(3.2f, 15) {{
            collidesTiles = collidesAir = false;

            hitEffect = Fx.massiveExplosion;
            drag = -0.005f;
            knockback = 1.5f;
            lifetime = 30f;
            height = 15.5f;
            width = 15f;
            shrinkX = 0.15f;
            shrinkY = 0.63f;
            shrinkInterp = Interp.slope;
            splashDamageRadius = 40f;
            splashDamage = 70f;
            backColor = Pal.missileYellowBack;
            frontColor = Pal.missileYellow;
            trailEffect = Fx.artilleryTrail;
            trailInterval = 2;
            hitShake = 4f;

            shootEffect = Fx.shootBig2;

            status = StatusEffects.blasted;
            statusDuration = 60f;
        }};
        weapon.bullet.fragBullets = 7;
        weapon.bullet.fragRandomSpread = 120;
        weapon.bullet.init();
        weapon = UnitTypes.bryde.weapons.get(1);
        weapon.bullet.damage = 18;
        weapon.bullet.splashDamage = 15;
        weapon.bullet.lifetime = 105;
        weapon.bullet.init();

        UnitTypes.sei.health = 22000;

        UnitTypes.omura.health = 77000;

        /*-----------------------------------------------------------------------------*/
        UnitTypes.retusa.health = 550;
        UnitTypes.retusa.speed = 0.7f;
        UnitTypes.retusa.armor = 8;
        weapon = UnitTypes.retusa.weapons.get(1);
        weapon.shoot = new ShootBarrel() {{
            shots = 3;
            barrels = new float[]{
                    0, 0, 0,
                    0, 0, 120,
                    0, 0, 240,
            };
        }};
        weapon.bullet.pierce = true;
        weapon.bullet.pierceBuilding = true;
        weapon.bullet.pierceCap = 3;
        weapon.bullet.lifetime = 120.5f;
        weapon.bullet.homingDelay = 60.5f;
        weapon.bullet.intervalBullets = 1;
        weapon.bullet.intervalDelay = 30.5f;
        weapon.bullet.bulletInterval = 18f;
        weapon.bullet.intervalBullet = new BasicBulletType() {{
            reflectable = absorbable = collidesAir = false;
            keepVelocity = false;

            speed = 0;
            lifetime = 180;
            damage = 180;
            buildingDamageMultiplier = 0.1f;
            width = 12;
            height = 4.8f;
            shrinkX = shrinkY = 0;
            frontColor = backColor = color = hitColor = Pal.heal;

            collidesTeam = true;
            healAmount = 32;
            healPercent = 1;
        }};
        weapon.bullet.init();

        UnitTypes.oxynoe.health = 1060;
        UnitTypes.oxynoe.armor = 10;
        UnitTypes.oxynoe.speed = 1.66f;
        StatusFieldAbility statusFieldAbility = (StatusFieldAbility) UnitTypes.oxynoe.abilities.get(0);
        statusFieldAbility.range = 90;
        statusFieldAbility.duration = 420;
        weapon = UnitTypes.oxynoe.weapons.get(0);
        weapon.reload = 3;
        weapon.shootStatus = FStatusEffects.deploy;
        weapon.shootStatusDuration = 10;
        weapon.bullet.lifetime = 27;
        weapon.bullet.damage = 34.5f;
        weapon.bullet.shootEffect = new Effect(32f, 120f, e -> {
            color(Color.white, Pal.heal, Color.gray, e.fin());

            randLenVectors(e.id, 12, e.finpow() * 90f, e.rotation, 10f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.5f);
                Drawf.light(e.x + x, e.y + y, 16f * e.fout(), Pal.heal, 0.6f);
            });
        });
        weapon.bullet.init();

        UnitTypes.cyerce.health = 1870;
        UnitTypes.cyerce.armor = 16;
        weapon = UnitTypes.cyerce.weapons.get(1);
        BulletType bullet = weapon.bullet.fragBullet;
        bullet.speed = 3.8f;
        weapon.bullet.lifetime = 120;
        weapon.bullet.damage = 37.5f;
        weapon.bullet.splashDamage = 37.5f;
        weapon.bullet.fragBullets = 1;
        weapon.bullet.fragBullet = new SummonBulletType() {{
            lifetime = 0;
            speed = 0;
            damage = 0;
            keepVelocity = false;
            summonRange = 220;
            summonNumber = 10;
            summonDespawned = true;
            summonBullet = bullet;
        }};
        weapon.bullet.init();

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
        weapon = UnitTypes.collaris.weapons.get(0);
        UnitTypes.collaris.targetAir = true;
        weapon.bullet.damage = 520;
        weapon.bullet.splashDamage = 85f;
        weapon.bullet.splashDamageRadius = 20f;
        weapon.bullet.bulletInterval = 20;
        weapon.bullet.intervalBullets = 3;
        weapon.bullet.intervalRandomSpread = 30;
        weapon.bullet.intervalAngle = 0;
        weapon.bullet.intervalBullet = new BasicBulletType() {{
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
        weapon.bullet.fragBullet.damage = 100;
        weapon.bullet.fragBullet.splashDamage = 92f;
        weapon.bullet.fragBullet.splashDamageRadius = 20;

        /*-----------------------------------------------------------------------------*/
        UnitTypes.nova.speed = 2f;
        UnitTypes.nova.armor = 21f;
        UnitTypes.nova.health = 220;
        UnitTypes.nova.buildSpeed = 0;
        UnitTypes.nova.abilities.add(new RepairFieldAbility(50, 60 * 8, 4));
        color = Color.valueOf("ffa998");
        weapon = UnitTypes.nova.weapons.get(0);
        weapon.reload = 4;
        weapon.bullet.lifetime = 75;
        weapon.bullet.speed = 8;
        weapon.bullet.damage = 18;
        weapon.bullet.healAmount = 0;
        weapon.bullet.healPercent = 0;
        weapon.bullet.reflectable = true;
        weapon.bullet.collidesTeam = false;
        weapon.bullet.smokeEffect = new Effect(8, e -> {
            color(Color.white, color, e.fin());
            stroke(0.5f + e.fout());
            Lines.circle(e.x, e.y, e.fin() * 5f);

            Drawf.light(e.x, e.y, 23f, color, e.fout() * 0.7f);
        });
        weapon.bullet.hitEffect = weapon.bullet.smokeEffect;
        weapon.bullet.despawnEffect = weapon.bullet.smokeEffect;
        weapon.bullet.lightColor = color;
        ((LaserBoltBulletType) weapon.bullet).frontColor = color;
        ((LaserBoltBulletType) weapon.bullet).backColor = color;

        UnitTypes.pulsar.health = 560;
        UnitTypes.pulsar.speed = 1.1f;
        UnitTypes.pulsar.range = 220;
        weapon = UnitTypes.pulsar.weapons.get(0);
        weapon.shoot.shots = 3;
        weapon.shoot.shotDelay = 6;
        weapon.reload = 360;
        weapon.inaccuracy = 12;
        weapon.bullet = new BasicBulletType() {{
            absorbable = false;
            collidesTeam = true;
            rangeOverride = 200;
            speed = 5;
            damage = 1;
            lifetime = 40;
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
                length = 20;

                damageInterval = 15;
                rotateSpeed = 4;

                healPercent = 1f;
                collidesTeam = true;
            }};
        }};
        weapon.bullet.init();

        UnitTypes.quasar.health = 2200;
        ForceFieldAbility fAbility = (ForceFieldAbility) UnitTypes.quasar.abilities.get(0);
        fAbility.regen = 0.8f;
        fAbility.max = 1000;
        weapon = UnitTypes.quasar.weapons.get(0);
        weapon.reload = 35;
        weapon.bullet.damage = 85;
        weapon.bullet.status = StatusEffects.slow;
        weapon.bullet.statusDuration = 210;
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
        weapon = UnitTypes.corvus.weapons.get(0);
        weapon.bullet.fragOnHit = false;
        weapon.bullet.fragBullets = 16;
        weapon.bullet.fragRandomSpread = 75;
        weapon.bullet.fragLifeMax = 1.8f;
        weapon.bullet.fragLifeMin = 0.2f;
        weapon.bullet.fragVelocityMax = weapon.bullet.fragVelocityMin = 1f;
        weapon.bullet.fragBullet = new BulletType() {{
            collides = hittable = absorbable = false;
            lifetime = 20;
            speed = 23;
            damage = 0;

            splashDamageRadius = 120;
            splashDamage = 110f;

            despawnEffect = Effects.lightningDown;
            hitColor = Pal.heal;
        }};

        /*-----------------------------------------------------------------------------*/
    }
}
