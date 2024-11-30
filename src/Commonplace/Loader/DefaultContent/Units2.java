package Commonplace.Loader.DefaultContent;

import Commonplace.AI.*;
import Commonplace.Loader.Special.Commands;
import Commonplace.Loader.Special.Effects;
import Commonplace.Entities.Ability.*;
import Commonplace.Entities.BulletType.*;
import Commonplace.Entities.Unit.F.*;
import Commonplace.Entities.Unit.Override.FLegsUnit;
import Commonplace.Entities.Unit.Override.FUnitEntity;
import Commonplace.Entities.UnitType.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.ai.types.GroundAI;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.entities.part.FlarePart;
import mindustry.entities.part.HoverPart;
import mindustry.entities.part.ShapePart;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;
import mindustry.type.weapons.BuildWeapon;
import mindustry.type.weapons.PointDefenseWeapon;
import mindustry.type.weapons.RepairBeamWeapon;

import static arc.graphics.g2d.Lines.lineAngle;
import static arc.math.Angles.randLenVectors;

public class Units2 {
    public static Seq<UnitType> boss = new Seq<>();
    //tool
    public static UnitType transfer, shuttle1, bulletInterception_a, rejuvenate_a;

    //command
    public static UnitType strike;

    //support
    public static UnitType support_h, support_a;

    ////ENGSWEISBoss
    public static UnitType velocity, velocity_d, velocity_s, hidden, cave;

    //ENGSWEISEntity
    public static UnitType barb, hammer, buying, crazy, transition, shuttle;

    //ENGSWEISLand
    public static UnitType dive, befall;

    //WUGENANSMechUnit
    public static UnitType recluse;

    //special
    public static UnitType bulletInterception, rejuvenate, vibrate, crane;

    //attack
    public static UnitType garrison, herald, exterminate, execute, hello;

    //Test
    public static UnitType e;

    public static void load() {
        support_a = new UnitType("support-a") {{
            constructor = FLegsUnit::create;

            health = 12000;
            armor = 13;
            speed = 1.5f;
            hitSize = 30;

            weapons.add(new Weapon() {{
                inaccuracy = 80;
                reload = 15;
                shootY = 15;

                shoot.shots = 5;
                shoot.shotDelay = 3;

                bullet = new BasicBulletType() {{
                    pierce = pierceBuilding = true;
                    pierceCap = 2;
                    width = 6;
                    height = 21;
                    damage = 580;
                    speed = 9;
                    lifetime = 60;
                    splashDamage = 220;
                    splashDamageRadius = 20;
                    status = StatusEffects.blasted;
                    statusDuration = 20;
                    buildingDamageMultiplier = 0.22f;
                }};
            }});

            weapons.add(new Weapon() {{
                reload = 90;
                inaccuracy = 0;

                bullet = new BasicBulletType() {{
                    pierce = true;
                    pierceBuilding = true;
                    width = 10;
                    height = 30;
                    damage = 1300;
                    speed = 11;
                    lifetime = 80;
                    splashDamage = 100;
                    splashDamageRadius = 40;
                    status = StatusEffects.blasted;
                    statusDuration = 60;
                    buildingDamageMultiplier = 0.22f;

                    fragBullets = 15;
                    fragRandomSpread = 120;
                    fragBullet = new BasicBulletType() {{
                        width = height = 9;
                        damage = 200;
                        speed = 8;
                        lifetime = 15;
                        splashDamage = 80;
                        splashDamageRadius = 12;
                        buildingDamageMultiplier = 0.22f;
                    }};

                    bulletInterval = 20;
                    intervalDelay = 30;
                    intervalSpread = 5;
                    intervalBullets = 3;
                    intervalBullet = new SummonBulletType() {{
                        speed = 16;
                        lifetime = 120;
                        drag = 0.04f;
                        splashDamageRadius = 16;
                        status = StatusEffects.unmoving;
                        statusDuration = 35;

                        summonRange = 150;
                        summonBullets = 7;
                        summonInterval = 0;
                        summonDelay = 40;
                        summonBullet = new LaserBulletType(256) {{
                            length = 260;
                            width = 5;
                            laserEffect = Fx.none;
                            buildingDamageMultiplier = 0.22f;
                            chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);
                        }};
                    }};
                }};
            }});

            weapons.add(new Weapon() {{
                alwaysShooting = true;
                mirror = false;
                reload = 1810;
                x = y = 0;
                shoot.firstShotDelay = 3600;
                bullet = new BulletType(0, 0) {{
                    lifetime = 0;
                    killShooter = true;
                    despawnEffect = hitEffect = Fx.none;
                }};
            }});
        }};
        support_h = new UnitType("support-h") {{
            constructor = UnitEntity::create;
            commands = new UnitCommand[]{UnitCommand.moveCommand, UnitCommand.rebuildCommand};
            defaultCommand = UnitCommand.rebuildCommand;

            flying = true;
            stepShake = 0;
            health = 9000;
            armor = 10;
            speed = 2f;
            hitSize = 14;
            buildRange = 180;
            buildSpeed = 3;
            abilities.add(new StatusFieldAbility(StatusEffects2.back, 90, 60, 120) {{
                activeEffect = Fx.healWave;
            }});

            weapons.add(new RepairBeamWeapon() {{
                targetUnits = true;
                targetBuildings = true;
                rotateSpeed = 12;
                reload = 30;

                repairSpeed = 12;
                fractionRepairSpeed = 4f;
            }});
            weapons.add(new BuildWeapon() {{
                rotateSpeed = 12;
            }});
            weapons.add(new Weapon() {{
                reload = 1000000;
                alwaysShooting = true;
                mirror = false;
                x = y = 0;
                shoot = new ShootPattern() {{
                    firstShotDelay = 3600;
                }};

                bullet = new ExplosionBulletType(0, 120) {{
                    collidesTeam = true;
                    healAmount = 1000;
                    healPercent = 37f;
                }};
            }});
        }};
        strike = new MissileUnitType("strike") {{
            constructor = TimedKillUnit::create;
            controller = u -> new FlyingFinderAI();

            flying = true;
            faceTarget = true;
            circleTarget = true;
            health = 12000;
            armor = 15;
            speed = 6;
            hitSize = 9;
            lifetime = 167;

            range = maxRange = 300;

            weapons.add(new Weapon() {{
                x = y = 0;
                shootCone = 360;
                mirror = false;
                shootOnDeath = true;

                bullet = new ExplosionBulletType(180, 32) {{
                    rangeOverride = 4;
                }};
            }});
        }};
        crane = new UnitType("crane") {{
            constructor = FUnitEntity::create;

            flying = true;
            health = 100000;
            armor = 20;
            speed = 0.5f;

            weapons.add(new Weapon() {{
                reload = 60;
                mirror = false;
                continuous = alwaysContinuous = true;
                x = y = 0;
                bullet = new PointLaserBulletType() {{
                    hitEffect = Fx.massiveExplosion;
                    shootEffect = Fx.railShoot;
                    lifetime = 2;
                    speed = 0;
                    damage = 200;
                    splashDamage = 200;
                    splashDamageRadius = 40;

                    rangeOverride = 300;
                }};
            }});

            weapons.add(new Weapon() {{
                reload = 90;
                x = y = 0;
                mirror = false;

                shoot = new ShootAlternate() {{
                    barrels = 5;
                    spread = 12;
                    shots = 5;
                }};

                bullet = new BulletType() {{
                    collides = reflectable = absorbable = hittable = false;
                    shootEffect = despawnEffect = hitEffect = Fx.none;

                    spawnUnit = strike;

                    lifetime = 0;
                    speed = 0;
                    damage = 0;
                    splashDamage = 0;
                    splashDamageRadius = 0;

                    rangeOverride = 1000;
                }};
            }});
        }};
        vibrate = new UnitType("vibrate") {{
            constructor = FLegsUnit::create;

            speed = 1;
            health = 20000;
            armor = 18;
            hitSize = 35;
            stepShake = 0;
            mechLandShake = 0;
            faceTarget = false;

            abilities.add(new StatusFieldAbility(StatusEffects.overclock, 180, 90, 80));
            abilities.add(new StatusFieldAbility(StatusEffects.shielded, 300, 420, 40));

            weapons.add(new Weapon() {{
                shootCone = 360;
                reload = 240;
                mirror = false;
                rotate = false;
                x = y = 0;
                shootX = shootY = 0;
                shoot = new ShootSummon(0, 0, 12, 15) {{
                    shots = 3;
                    shotDelay = 6;
                }};
                bullet = new EMPLarge() {{
                    reflectable = false;
                    damage = 140;
                    speed = 8;
                    lifetime = 60;
                    trailColor = Pal.lightishGray;
                    trailLength = 8;
                    trailWidth = 3;
                    homingPower = 0.1f;
                    homingDelay = 120;
                    homingRange = 1000;

                    maxLife = 300;
                    downRange = 275;
                    downDamage = 120;
                }};
            }});
            weapons.add(new Weapon() {{
                shootCone = 360;
                reload = 120;
                mirror = true;
                rotate = false;
                y = 0;
                x = 5;
                shootX = 0;
                shootY = -4;
                shoot = new ShootBarrel() {{
                    shots = 4;
                    shotDelay = 6;
                    barrels = new float[]{
                            0, 0, 95,
                            0, 0, 100,
                            0, 0, 105,
                            0, 0, 110
                    };
                }};
                bullet = new SummonBulletType() {{
                    width = height = 12;
                    damage = 60;
                    speed = 8;
                    lifetime = 120;
                    homingDelay = 0.1f;
                    homingRange = 1000;
                    homingPower = 0.1f;
                    trailColor = Pal.redDust;
                    trailLength = 16;
                    trailWidth = 2;
                    hittable = false;

                    summonBullet = new LightningBulletType() {{
                        lightningLength = 25;
                        damage = 130;
                        speed = 80;

                        lightningColor = Pal.redDust;
                    }};
                    summonRange = 80;
                    summonBullets = 10;
                }};
            }});
            weapons.add(new Weapon() {{
                shootCone = 360;
                reload = 120;
                mirror = true;
                rotate = false;
                y = 0;
                x = 15;
                shootX = 0;
                shootY = -4;
                shoot = new ShootBarrel() {{
                    shots = 2;
                    shotDelay = 6;
                    barrels = new float[]{
                            0, 0, -5,
                            0, 0, 5
                    };
                }};
                bullet = new SummonBulletType() {{
                    width = height = 12;
                    damage = 100;
                    speed = 8;
                    lifetime = 60;
                    homingDelay = 0.1f;
                    homingRange = 1000;
                    homingPower = 0.1f;
                    trailColor = Pal.techBlue;
                    trailLength = 16;
                    trailWidth = 2;
                    absorbable = false;

                    summonBullet = new MissileBulletType(5, 320) {{
                        lifetime = 60;
                        frontColor = backColor = trailColor = Pal.techBlue;
                    }};
                    summonRange = 120;
                    summonBullets = 14;
                    summonInterval = 9;
                }};
            }});
        }};
        rejuvenate_a = new MissileUnitType("rejuvenate-a") {{
            hidden = true;

            health = 3000;
            armor = 100;
            speed = 6;
            range = maxRange = 16;
            lifetime = 300;
            trailLength = 15;
            trailColor = Color.valueOf("00DDaAFF");
            immunities.addAll(StatusEffects.slow, StatusEffects2.tardy, StatusEffects2.StrongStop);

            abilities.add(new ShieldArcAbility() {{
                radius = 20;
                regen = 1;
                max = 7000;
                cooldown = 1200;
                angle = 115;
                whenShooting = false;
            }});

            weapons.add(new Weapon() {{
                bullet = new ExplosionBulletType(15, 120) {{
                    rangeOverride = 120;
                    status = StatusEffects2.suppress;
                    statusDuration = 300;
                }};
            }});

            weapons.add(new Weapon() {{
                reload = 30;
                x = 6;
                y = 13;
                xRand = 25;
                alwaysShooting = true;

                shoot = new ShootPattern() {{
                    shots = 5;
                }};
                bullet = new LightningBulletType() {{
                    damage = 30;
                    collidesTeam = true;
                    status = StatusEffects.sapped;
                    statusDuration = 150;
                    healAmount = 250;
                    lightningLength = 35;
                    lightningColor = Color.valueOf("00DDAAFF");
                }};
            }});
        }};
        rejuvenate = new UnitType("rejuvenate") {{
            constructor = LegsUnit::create;
            aiController = HealthOnlyAI::new;

            health = 50000;
            armor = 180;
            speed = 1.3f;
            accel = 0.9f;
            drag = 0.9f;
            hitSize = 35;
            range = maxRange = 1000;
            stepShake = 0;
            isEnemy = false;

            abilities.add(new StatusFieldAbility(StatusEffects2.grow, 3600, 300, 160));
            abilities.add(new ShieldRegenFieldAbility(5000, 100000, 600, 160));

            weapons.add(new RepairBeamWeapon() {{
                rotateSpeed = 12;
                reload = 480;

                repairSpeed = 10;
                fractionRepairSpeed = 4f;
            }});

            weapons.add(new Weapon() {{
                shootCone = 360;
                reload = 120;
                mirror = true;
                shootX = 8;
                shootY = 12;
                shoot = new ShootBarrel() {{
                    shots = 2;
                    shotDelay = 16;
                    barrels = new float[]{
                            0, 0, 30,
                            0, 0, 150
                    };
                }};

                bullet = new BulletType(0, 0) {{
                    hittable = reflectable = absorbable = collides = false;
                    lifetime = 0;

                    spawnUnit = rejuvenate_a;
                }};
            }});
        }};
        bulletInterception_a = new UnitType("bulletInterception_a") {{
            constructor = FollowUnit::create;
            controller = u -> new FollowAI();

            health = 3000;
            speed = 2.5f;
            armor = 20;
            rotateSpeed = 5;
            drag = 0.2f;
            accel = 1;
            maxRange = range = 120;
            isEnemy = false;
            flying = true;
            logicControllable = false;
            faceTarget = false;
            playerControllable = false;
            allowedInPayloads = false;
            hittable = false;
            targetable = false;
            pickupUnits = false;
            useUnitCap = false;
            createWreck = false;
            hidden = true;
            alwaysUnlocked = true;
            weapons.add(new PointDefenseWeapon() {{
                mirror = false;
                reload = 0.1F;
                rotateSpeed = 360;
                recoil = 0;
                x = 0;
                shootOnDeath = true;
                targetInterval = 0;
                targetSwitchInterval = 0;
                bullet = new BasicBulletType() {{
                    damage = 500;
                    rangeOverride = 100;
                }};
            }});
        }};
        bulletInterception = new UnitType("bulletInterception") {{
            constructor = SpawnerUnit::create;

            hitSize = 45;
            range = 1000;
            health = 10000;
            armor = 24;
            speed = 1;
            legCount = 6;
            legGroupSize = 2;
            legLength = 14;
            legContinuousMove = false;
            stepShake = 0;
            abilities.add(new ShieldArcAbility() {{
                regen = 2.5F;
                max = 50000;
                cooldown = 300;
                angle = 360;
                whenShooting = false;
            }});
            abilities.add(new OwnerUnitSpawnAbility(bulletInterception_a, 600, 0, 0) {{
                maxNum = 4;
            }});


            weapons.add(new PointDefenseWeapon() {{
                mirror = false;
                rotateSpeed = 360;
                reload = 0.1F;
                targetInterval = 0;
                targetSwitchInterval = 0;
                bullet = new BasicBulletType() {{
                    damage = 300;
                    maxRange = 100;
                    hitEffect = Fx.none;
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 120;
                mirror = false;
                x = 0;
                y = -10;
                shoot = new ShootBarrel() {{
                    shots = 2;
                    barrels = new float[]{
                            0, 0, -60,
                            0, 0, 60
                    };
                }};
                shootSound = Sounds.artillery;
                bullet = new BulletType() {{
                    intervalSpread = 30;
                    shootEffect = Fx.artilleryTrailSmoke;
                    trailEffect = Fx.artilleryTrailSmoke;
                    trailChance = 0.5f;
                    status = StatusEffects2.seethe;
                    statusDuration = 600;
                    splashDamageRadius = 158;
                    speed = 6;
                    lifetime = 280;
                    homingPower = 0.9F;
                    homingRange = 1000;
                    homingDelay = 120;
                    hitEffect = despawnEffect = Fx.none;
                }};
            }});
        }};
        shuttle1 = new MissileUnitType("shuttle1") {{
            constructor = TimedKillUnit::create;
            controller = u -> new FlyingFinderAI();

            flying = true;
            health = 6000;
            armor = 30;
            speed = 3;
            lifetime = 1200;
            logicControllable = false;
            playerControllable = false;
            useUnitCap = false;
            isEnemy = false;
            physics = false;
            faceTarget = false;
            allowedInPayloads = false;
            hidden = true;

            weapons.add(new Weapon() {{
                mirror = false;
                x = 0;
                reload = 10f;

                shootStatus = StatusEffects.slow;
                shootStatusDuration = 37;
                shoot = new ShootAlternate() {{
                    shots = 4;
                    shotDelay = 3;
                    barrels = 3;
                    spread = 20;
                }};
                bullet = new BasicBulletType() {{
                    speed = 3.5f;
                    drag = -0.1f;
                    width = 12;
                    height = 36;
                    damage = 70;
                    lifetime = 16;
                    splashDamage = 45;
                    splashDamageRadius = 32;
                    rangeOverride = 160;
                    hitEffect = despawnEffect = Fx.shockwave;
                }};
            }});
        }};
        shuttle = new BoostUnitType("shuttle") {{
            constructor = BoostUnitEntity::create;
            aiController = BoostFlyingAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, Commands.boostFlying};

            speed1 = 3;
            health2 = 57750;
            hitPercent = 95;
            hitFirstPercent = true;
            hitReload = 3600;
            boostReload = 3600;

            flying = true;
            health = 57750;
            armor = 20;
            speed = 3;
            hitSize = 49;
            drag = 0.1F;
            faceTarget = true;
            rotateSpeed = 3;
            strafePenalty = 0;
            engines.add(new UnitEngine(0, -25, 5, -90));
            engines.add(new UnitEngine(13.7F, -24, 4, -90));
            engines.add(new UnitEngine(-13.7F, -24, 4, -90));
            weapons.add(new Weapon() {{
                reload = 480;
                mirror = false;
                rotate = false;
                alternate = true;
                bullet = new BulletType() {{
                    spawnUnit = shuttle1;
                    rangeOverride = 400;
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 37;
                mirror = true;
                x = 13.2F;
                y = 1.4F;
                rotate = false;
                bullet = new BasicBulletType() {{
                    scaleLife = false;
                    shrinkY = 3;
                    lightOpacity = 0.5F;
                    lightColor = new Color(243, 0, 0, 255);
                    shootEffect = Fx.none;
                    hitEffect = shootEffect;
                    despawnEffect = shootEffect;
                    drawSize = 2;
                    hitSize = 2;
                    lifetime = 20;
                    speed = 7;
                    damage = 25;
                    width = 2;
                    height = 2;
                    lightning = 12;
                    lightningColor = lightColor;
                    lightningDamage = 100;
                    lightningLength = 12;
                    lightningCone = 360;
                    fragBullets = 3;
                    fragBullet = new BasicBulletType() {{
                        width = 0;
                        height = 0;
                        speed = 0;
                        lifetime = 60;
                        lightning = 6;
                        lightningColor = lightColor;
                        lightningDamage = 25;
                        lightningLength = 15;
                    }};
                }};
            }});
        }};
        velocity_s = new BoostUnitType("velocity_s") {{
            constructor = BoostUnitEntity::create;
            speed = 6;
            hidden = true;
        }};
        velocity_d = new UnitType("velocity_d") {{
            constructor = PayloadUnit::create;
            health = 50000;
            armor = 12;
            speed = 0;
            rotateSpeed = 0;
            baseRotateSpeed = 0;
            hitSize = 20;
            hidden = true;
            createScorch = false;
            createWreck = false;

            weapons.add(new Weapon() {{
                controllable = false;
                autoTarget = true;
                alwaysShooting = true;
                aiControllable = false;
                reload = 2000;
                bullet = new BasicBulletType() {{
                    hitEffect = Fx.massiveExplosion;
                    lifetime = 0;
                    damage = 0;
                    splashDamage = 5000;
                    splashDamageRadius = 160;
                }};
            }});

            weapons.add(new Weapon() {{
                controllable = false;
                autoTarget = true;
                alwaysShooting = true;
                aiControllable = false;
                shootSound = Sounds.explosion;
                shoot = new ShootBarrel() {{
                    firstShotDelay = 2000;
                }};
                reload = 2000;
                bullet = new BasicBulletType() {{
                    hitEffect = Fx.massiveExplosion;
                    killShooter = true;
                    lifetime = 0;
                    damage = 0;
                    splashDamage = 5000;
                    splashDamageRadius = 160;
                    spawnUnit = velocity_s;
                }};
            }});
        }};
        dive = new UnitType("dive") {{
            constructor = FLegsUnit::create;

            speed = 0.4F;
            health = 6000;
            armor = 30;
            hitSize = 13;
            lightRadius = 1000;

            weapons.add(new Weapon() {{
                top = false;
                mirror = false;
                shootY = 0f;
                reload = 4;
                ejectEffect = Fx.none;
                recoil = 1f;
                x = 0f;
                shootSound = Sounds.flame;
                shoot = new ShootSpread() {{
                    shots = 16;
                    spread = 11.25F;
                }};
                bullet = new BasicBulletType() {{
                    shootEffect = Fx.shootPyraFlame;
                    lifetime = 20;
                    width = 0;
                    height = 0;
                    speed = 5;
                    damage = 55;
                    despawnEffect = hitEffect = Fx.none;
                    status = StatusEffects.burning;
                    statusDuration = 300;
                }};
            }});
        }};
        befall = new UnitType("befall") {{
            constructor = FLegsUnit::create;

            health = 10000;
            armor = 60;
            speed = 0.5f;
            hitSize = 20;
            range = maxRange = 56;

            abilities.add(new TimeLargeDamageAbility());
        }};
        velocity = new BoostUnitType("velocity") {{
            constructor = BoostUnitEntity::create;

            hidden = true;

            hitDamage = 50;
            hitReload = 15;
            speed1 = 2.5F;

            flying = true;
            health = 100000;
            armor = 30;
            speed = 2.5F;
            hitSize = 35;
            createScorch = false;
            createWreck = false;

            abilities.add(new SpawnDeathAbility(velocity_d, 1, 0));

            weapons.add(new Weapon() {{
                reload = 300;
                controllable = false;
                mirror = false;
                alwaysShooting = true;
                shootSound = Sounds.none;
                bullet = new BulletType() {{
                    despawnEffect = Fx.none;
                    hitEffect = Fx.none;
                    speed = 0;
                    damage = 0;
                    lifetime = 0;
                    splashDamage = 20000;
                    splashDamageRadius = 56;
                    splashDamagePierce = true;
                    shootEffect = new WaveEffect() {{
                        lifetime = 120;
                        colorTo = Color.white;
                        colorFrom = Pal.darkPyraFlame;
                        sizeTo = 56;
                    }};
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 50;
                mirror = true;
                x = 0;
                y = 0;
                shoot = new ShootBarrel() {{
                    barrels = new float[]{0, -20, 70};
                }};
                bullet = new BasicBulletType() {{
                    rangeOverride = 500;
                    sprite = "jamming_bomb";
                    width = 24;
                    height = 55;
                    trailLength = 23;
                    trailWidth = 5;
                    speed = 11;
                    damage = 300;
                    lifetime = 210;
                    homingRange = 1000;
                    homingDelay = 30;
                    homingPower = 0.4F;
                    hitEffect = Fx.none;
                    despawnEffect = Fx.none;
                    absorbable = false;
                    reflectable = false;
                    keepVelocity = false;

                    intervalBullets = 5;
                    intervalDelay = 100;
                    bulletInterval = 15;
                    intervalBullet = new BasicBulletType(11, 2) {{
                        lifetime = 120;
                        trailChance = 1;
                        trailColor = Pal.lightTrail;
                        trailInterp = Interp.swingOut;

                        weaveMag = 3;
                        weaveScale = 15;

                        homingPower = 0.3f;
                        homingRange = 1000;
                        homingDelay = 20;
                    }};
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 50;
                mirror = true;
                x = 0;
                y = 0;
                shoot = new ShootBarrel() {{
                    barrels = new float[]{0, -25, 50};
                }};
                bullet = new BasicBulletType() {{
                    sprite = "fire_bomb";
                    absorbable = false;
                    reflectable = false;
                    width = 15;
                    height = 40;
                    trailLength = 20;
                    trailWidth = 4;
                    speed = 7;
                    damage = 300;
                    lifetime = 210;
                    homingRange = 1000;
                    homingDelay = 40;
                    homingPower = 0.3F;
                    hitEffect = Fx.none;
                    splashDamage = 0;
                    splashDamageRadius = 156;
                    splashDamagePierce = true;
                    statusDuration = 300;
                    keepVelocity = false;
                    status = StatusEffects2.sublimation;
                    incendSpread = 360;
                    incendChance = 1;
                    incendAmount = 15;

                    fragBullets = 1;
                    fragLifeMin = 6;
                    fragLifeMax = 6;
                    fragVelocityMin = 0;
                    fragVelocityMax = 0;
                    fragBullet = new PointBulletType2() {{
                        collides = absorbable = hittable = reflectable = false;

                        point = false;
                        intervalRandomSpread = 360;
                        intervalBullets = 2;
                        intervalDelay = 10;
                        bulletInterval = 10;
                        //intervalHitEffect = ;
                        intervalBullet = new BulletType() {{
                            speed = 1.2f;
                            damage = 75;

                            incendSpread = 360;
                            incendChance = 1;
                            incendAmount = 6;

                            status = StatusEffects2.torn;
                            statusDuration = 240;
                        }};
                    }};
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 50;
                mirror = true;
                x = 0;
                y = 0;
                shoot = new ShootBarrel() {{
                    barrels = new float[]{0, -30, 30};
                }};
                bullet = new BulletType() {{
                    damage = 0;
                    speed = 1f;
                    lifetime = 120;
                    hittable = absorbable = reflectable = collides = false;
                    hitEffect = despawnEffect = new ExplosionEffect() {{
                        lifetime = 180;

                        smokeColor = Color.valueOf("110000DD");
                        sparks = 40;
                        smokes = 60;
                        smokeRad = 100;
                        smokeSize = 24;
                        smokeSizeBase = 2;
                    }};
                    hitSound = despawnSound = Sounds.artillery;

                    fragBullets = 15;
                    fragRandomSpread = 360;
                    fragLifeMin = 300;
                    fragLifeMax = 300;
                    fragVelocityMax = fragVelocityMin = 1.5f;
                    fragBullet = new BasicBulletType() {{
                        sprite = "high_explosive_projectile";
                        absorbable = false;
                        reflectable = false;
                        splashDamagePierce = true;

                        trailEffect = Fx.missileTrailSmoke;
                        trailChance = 0.4f;

                        width = 5;
                        height = 10;
                        speed = 6;
                        damage = 0;
                        lifetime = 300;

                        homingRange = 1000;
                        homingDelay = 0;
                        homingPower = 0.2F;
                        splashDamage = 80;
                        splashDamageRadius = 156;

                        status = StatusEffects2.torn;
                        statusDuration = 180;
                        keepVelocity = false;
                        despawnEffect = new WaveEffect() {{
                            strokeFrom = 14;
                            strokeTo = 10;
                            lifetime = 160;
                            sizeTo = 168;
                            colorFrom = Color.valueOf("110000DD");
                            colorTo = colorFrom;
                        }};
                        hitEffect = despawnEffect;
                    }};
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 1200;
                bullet = new PercentBulletType() {{
                    width = 0;
                    height = 0;
                    speed = 0.5F;
                    damage = 20000;
                    lifetime = 1200;
                    splashDamageRadius = 0;
                    shootEffect = Fx.none;
                    homingRange = 1000;
                    homingDelay = 50;
                    homingPower = 0.2F;
                    keepVelocity = false;
                    lightColor = Color.valueOf("8D0000FF");
                    scaleLife = false;
                    shrinkY = 0;
                    trailLength = 0;
                    trailChance = 1;
                    WL = true;
                    lightningPercent = 12;
                    lightning = 12;
                    lightningLength = 10;
                    trailEffect = new ParticleEffect() {{
                        particles = 80;
                        cone = 360;
                        lifetime = 4;
                        line = true;
                        lenFrom = 7;
                        lenTo = 7;
                        strokeFrom = 1;
                        strokeTo = 1;
                        cap = false;
                        colorFrom = Color.valueOf("914B00FF");
                        colorTo = colorFrom;
                    }};
                    parts.add(new ShapePart() {{
                        circle = true;
                        radius = 11.5F;
                        radiusTo = 11.5F;
                        color = Color.valueOf("914B00FF");
                    }});
                    parts.add(new HoverPart() {{
                        radius = 56;
                        color = Color.valueOf("914B00FF");
                        phase = 150;
                        sides = 18;
                    }});
                }};
            }});
        }};
        crazy = new BoostUnitType("crazy") {{
            constructor = BoostUnitEntity::create;
            aiController = BoostFlyingAI::new;
            defaultCommand = Commands.boostFlying;
            commands = new UnitCommand[]{UnitCommand.moveCommand, defaultCommand};
            immunities.add(StatusEffects.slow);
            immunities.add(StatusEffects.unmoving);
            immunities.add(StatusEffects.wet);
            immunities.add(StatusEffects.sporeSlowed);

            health2 = 13200;
            speed1 = 3;

            range = maxRange = 200;
            hitReload = 7;
            hitPercent = 1.5F;
            hitChangeHel = 1000;
            hitDamage = 280;
            hitFirstPercent = true;

            circleTarget = true;
            flying = true;
            targetAir = targetGround = true;
            rotateSpeed = 360;
            speed = 7;
            drag = 1F;
            accel = 0.5F;
            health = 13200;
            armor = 23;
            hitSize = 9;
        }};
        barb = new BoostUnitType("barb") {{
            constructor = BoostUnitEntity::create;
            aiController = BoostFlyingAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, Commands.boostFlying};
            flying = true;
            targetAir = targetGround = true;

            speed1 = 0.8F;
            hitDamage = 0;
            hitPercent = 10;
            hitChangeHel = -1;
            hitFirstPercent = true;
            hitReload = 90;

            speed = 1.3F;
            rotateSpeed = 5;
            faceTarget = true;
            health = 240;
            armor = 7;
            engineOffset = 7;
            engineSize = 2.5F;
            itemCapacity = 30;
            hitSize = 5;

            weapons.add(new Weapon() {{
                reload = 60;
                y = 3.8F;
                x = 0;
                mirror = false;
                shoot.shots = 5;
                shoot.shotDelay = 7;
                bullet = new BasicBulletType() {{
                    width = height = 16;
                    speed = 5;
                    damage = 20;
                    lifetime = 44.4f;
                }};
            }});
        }};
        transfer = new TileMinerUnitType("transfer") {{
            constructor = TileMiner::create;
            defaultCommand = new UnitCommand("TileMine", "TileMine", TileMineAI::new);
            aiController = TileMineAI::new;
            controller = u -> u.team.isAI() && !u.team.rules().rtsAi ? aiController.get() : new CommandAI();
            commands = new UnitCommand[]{
                    defaultCommand,
                    new UnitCommand("TilePut", "TilePut", TilePutAI::new)
            };
            hidden = true;
            alwaysUnlocked = true;
            isEnemy = false;
            useUnitCap = false;
            playerControllable = logicControllable = false;

            flying = true;
            health = 600;
            speed = 1.7F;
            armor = 9;
            drag = 0.8F;
            accel = 0.8F;
            rotateSpeed = 4;

            mineFloor = true;
            mineWalls = true;
            mineRange = 0;
            mineSpeed = 1;
            mineTier = 5;
        }};
        buying = new BoostUnitType("buying") {{
            constructor = TileSpawnerUnit::create;
            aiController = BoostFlyingAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, Commands.boostFlying};

            boostReload = 1800;
            hitReload = 600;
            hitPercent = 30;
            hitFirstPercent = true;
            boostDuration = 25;
            speed1 = 0.3F;
            health2 = 2000;
            number = 3;

            rotateSpeed = 5.4f;
            buildSpeed = 3f;
            hitSize = 24f;
            flying = true;
            speed = 0.4f;
            engineOffset = 9;
            engineSize = 3;
            accel = 0.9F;
            drag = 0.9F;
            health = 3000;
            armor = 19;

            abilities.add(new OwnerUnitSpawnAbility(transfer, 2400, 1, 1));
            abilities.add(new ShieldRegenFieldAbility(90, 900, 90, range));

            weapons.add(new Weapon() {{
                range = 150;
                reload = 180;
                y = 3.8F;
                x = 2.7F;
                top = false;
                alternate = false;
                shoot.shots = 15;
                shoot.shotDelay = 1;
                bullet = new BasicBulletType() {{
                    homingDelay = 45;
                    homingPower = 0.1F;
                    homingRange = 1000;
                    inaccuracy = 360;
                    weaveRandom = true;
                    speed = 3.7F;
                    damage = 1;
                    lifetime = 240;
                    splashDamage = 15;
                    splashDamageRadius = 12;
                    trailChance = 1F;
                }};
            }});
        }};
        hammer = new BoostUnitType("hammer") {{
            constructor = BoostUnitEntity::create;
            aiController = BoostFlyingAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, Commands.boostFlying};

            health = 800;
            speed = 1.2F;
            armor = 12;
            accel = 0.9F;
            drag = 0.9F;
            flying = true;

            hitPercent = 20;
            hitFirstPercent = true;
            health2 = 350;
            speed1 = 1.0F;
            hitReload = 600;
            boostReload = 1800;
            boostDuration = 45;

            abilities.add(new ForceFieldAbility(hitSize * 2, 0.2f, 300, 1800));
            abilities.add(new EMPAbility());

            weapons.add(new Weapon() {{
                reload = 42;
                x = y = 0;
                inaccuracy = 20;
                mirror = false;
                shoot = new ShootMulti(new ShootPattern() {{
                    shots = 2;
                    shotDelay = 5;
                }}, new ShootSpread() {{
                    shots = 8;
                    spread = 22.5F;
                }});
                bullet = new BasicBulletType() {{
                    damage = 28;
                    lifetime = 60;
                    speed = 4;
                }};
            }});
        }};
        transition = new BoostUnitType("transition") {{
            constructor = BoostUnitEntity::create;
            aiController = BoostFlyingAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, Commands.boostFlying};

            health = 66000;
            speed = 2F;
            armor = 28;
            accel = 0.9F;
            drag = 0.9F;
            flying = true;
            hitSize = 40;

            hitPercent = 75;
            hitFirstPercent = true;
            health2 = 66000;
            speed1 = 1.3F;
            hitReload = 3600;
            boostReload = 3600;

            weapons.add(new Weapon() {{
                range = 320;
                reload = 780;
                mirror = false;
                shoot = new ShootBarrel() {{
                    firstShotDelay = 120;
                }};
                bullet = new SqrtDamageBullet() {{
                    damage = 600;
                    speed = 0;
                    lifetime = 1200;
                    halfWidth = 120;
                    sqrtLength = 320;
                    chargeEffect = new ParticleEffect() {{
                        lifetime = 120;
                        sizeFrom = 3;
                        sizeTo = 0;
                        interp = Interp.zero;
                        colorFrom = Pal.redLight;
                        colorTo = Pal.redDust;
                        particles = 15;
                        cone = 60;
                    }};
                }};
            }});

            weapons.add(new Weapon() {{
                reload = 5;
                alternate = false;
                bullet = new BasicBulletType() {{
                    damage = 35;
                    speed = 4;
                    drag = -0.001f;
                    lifetime = 90;
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 900;
                alternate = false;
                y = 10;

                bullet = new PercentBulletType() {{
                    homingRange = 1000;
                    homingPower = 0.1F;
                    percent = 3.5F;
                    firstPercent = true;
                    speed = 5.5F;
                    lifetime = 120;
                    trailChance = 1;
                    trailColor = Pal.redLight;
                    hitEffect = Fx.dynamicWave;
                }};
            }});
            weapons.add(new Weapon() {{
                reload = 900;
                alternate = false;
                y = -10;
                bullet = new PercentBulletType() {{
                    homingRange = 1000;
                    homingPower = 0.1F;
                    percent = 7.5F;
                    firstPercent = true;
                    speed = 5.5F;
                    lifetime = 120;
                    trailChance = 1;
                    trailColor = Pal.redLight;
                    hitEffect = Fx.dynamicWave;
                }};
            }});
        }};
        hidden = new UnitType("hidden") {{
            constructor = HiddenUnit::create;
            aiController = HiddenAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, new UnitCommand("findAuto", "findAuto", u -> new HiddenAI())};

            targetAir = targetGround = true;
            range = maxRange = 200;
            hitSize = 40;
            flying = true;
            health = 3000000;
            armor = 150;
            speed = 0.1F;
            rotateSpeed = 12;
            drag = 0.9F;
            accel = 0.9F;
            faceTarget = false;

            immunities.addAll(
                    StatusEffects2.swift,
                    StatusEffects.slow, StatusEffects.wet,
                    StatusEffects.unmoving, StatusEffects2.StrongStop,
                    StatusEffects.fast, StatusEffects.freezing
            );

            abilities.add(new StatusFieldAbility(StatusEffects2.swift, 600, 610, 100));
            abilities.add(new ShieldRegenFieldAbility(10000, 100000, 610, 100));
            abilities.add(new UnitSpawnAbility(hammer, 600, 1, 1));
            abilities.add(new UnitSpawnAbility(hammer, 600, -1, 1));
            abilities.add(new UnitSpawnAbility(hammer, 600, -1, -1));
            abilities.add(new UnitSpawnAbility(hammer, 600, 1, -1));

            weapons.add(new Weapon() {{
                reload = 45f;
                mirror = false;
                y = x = 0;
                shoot = new ShootPattern() {{
                    shots = 5;
                }};
                bullet = new BasicBulletType() {{
                    range = maxRange = 1000;
                    inaccuracy = 60;
                    width = height = 0;
                    damage = 0;
                    lifetime = 1800;
                    speed = 0.9f;
                    lightning = 24;
                    lightningDamage = 800;
                    lightningColor = Color.valueOf("436E2D");
                    lightningLength = 35;
                    lightningLengthRand = 15;
                }};
            }});
        }};
        recluse = new WUGENANSMechUnitType("recluse") {{
            constructor = UnderLandMechUnit::create;
            aiController = LandMoveAI::new;
            commands = new UnitCommand[]{UnitCommand.moveCommand, new UnitCommand("lm", "lm", u -> new LandMoveAI())};

            health = 1200;
            upDamage = 13;
            speed = 1.2f;

            weapons.add(new Weapon() {{
                reload = 180;
                bullet = new EMPLarge() {{
                    trailEffect = new MultiEffect(new Effect(90, e -> {
                        Draw.color(Color.valueOf("990000"));
                        Lines.stroke(3 * (1 - e.fin()));
                        Lines.ellipse(e.x, e.y, 36, 0.75f * e.fin(), 0.15f * e.fin(), e.rotation + 90);
                        Drawf.light(e.x, e.y, e.fout() * 27, Color.valueOf("990000"), 0.7f);
                    }), new Effect(15, e -> randLenVectors(e.id + 1, 12,
                            27 * e.fin(), e.rotation, 60, (x, y) -> {
                                Draw.color(Color.valueOf("990000"));
                                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fin());
                                Drawf.light(e.x + x, e.y + y, e.fout() * 4f, Color.valueOf("990000"), 0.7f);
                            })));
                    trailRotation = true;
                    trailWidth = 1;
                    trailLength = 19;
                    trailChance = 0.4f;
                    trailColor = Color.valueOf("990000");
                    shootEffect = Fx.shootBigColor;
                    smokeEffect = Fx.shootSmokeSquareBig;

                    width = height = 4;
                    frontColor = backColor = Color.valueOf("990000aa");
                    lifetime = 120;
                    speed = 13;
                    damage = 1;
                }};
            }});
        }};
        cave = new UnitType("cave") {{
            constructor = CaveUnit::create;

            hidden = true;

            hittable = false;
            targetable = false;
            playerControllable = logicControllable = false;
            faceTarget = false;
            physics = false;

            rotateSpeed = 0;
            hitSize = 45;
            health = 1;
            armor = 0;
            speed = 0F;
            drag = 1;
            range = maxRange = 1000;
            targetAir = targetGround = true;
        }};

        //attack
        garrison = new UnitType("garrison") {{
            constructor = Garrison::new;
            controller = u -> new GarrisonAI();

            hitSize = 20;
            health = 2500000;
            armor = 100;
            speed = 0.35f;
            legCount = 8;
            stepShake = 0;
            legMaxLength = 45;
            legLength = 45;
            legGroupSize = 2;
            legContinuousMove = false;

            weapons.add(new Weapon() {{
                reload = 600;
                mirror = false;
                x = y = 0;
                shootStatus = StatusEffects2.deploy;
                shootStatusDuration = 5;
                bullet = new BasicBulletType() {{
                    damage = 10000;
                    speed = 10;
                    lifetime = 90;
                    pierce = true;
                    pierceBuilding = true;
                    pierceCap = 8;

                    hitSize = 12;
                    width = height = 12;
                    trailWidth = 2;
                    trailLength = 10;

                    splashDamage = 2000;
                    splashDamageRadius = 52;

                    hitColor = Pal.bulletYellowBack;
                    hitEffect = despawnEffect = Fx.scatheExplosion;
                }};
            }});
        }};
        exterminate = new UnitType("exterminate") {{
            constructor = ElevationMoveUnit::create;

            hovering = true;
            health = 100000;
            armor = 70;
            speed = 2;

            weapons.add(new Weapon() {{
                mirror = false;
                x = y = 0;
                reload = 90;
                shootCone = 360;

                shoot = new ShootAlternate(15);
                shoot.shots = 2;

                bullet = new BulletType() {{
                    damage = 1;
                    speed = 4;
                    lifetime = 180;

                    homingRange = 720;
                    homingPower = 0.2f;

                    hittable = absorbable = reflectable = false;
                    keepVelocity = false;
                    despawnHit = true;

                    hitEffect = despawnEffect = new Effect(90, 170, c -> {
                        Draw.color(Pal.redderDust);
                        Lines.stroke(5 * c.fout());
                        Lines.circle(c.x, c.y, 150 * c.fin());
                    });

                    fragBullets = 1;
                    fragBullet = new ContinuousCircleBulletType() {{
                        reflectable = absorbable = hittable = false;

                        lifetime = 240;
                        damage = 200;
                        damageTo = 600;
                        damageInterval = 15;
                        length = 60;
                        lengthTo = 0;

                        status = StatusEffects.slow;
                        statusDuration = 15;

                        despawnHit = true;
                        fragBullets = 1;
                        fragBullet = new BulletType() {{
                            reflectable = absorbable = hittable = false;

                            lifetime = 0;
                            damage = 0;
                            speed = 0;
                            splashDamage = 1000;
                            splashDamageRadius = 100;

                            status = StatusEffects.blasted;
                            statusDuration = 90;

                            hitColor = Pal.redderDust;
                            hitEffect = despawnEffect = Effects.BombMid;
                        }};

                        parts.addAll(new FlarePart() {{
                            sides = 3;
                            rotate = true;
                            spinSpeed = 20;
                            radius = 45;
                            radiusTo = 0;
                            stroke = 2.5f;
                            color1 = color2 = Pal.redderDust;
                        }}, new FlarePart() {{
                            sides = 3;
                            rotate = true;
                            rotation = 180;
                            spinSpeed = 20;
                            radius = 45;
                            radiusTo = 0;
                            stroke = 2.5f;
                            color1 = color2 = Pal.redderDust;
                        }});
                    }};

                    parts.add(new FlarePart() {{
                        sides = 3;
                        rotate = true;
                        spinSpeed = 12;
                        radius = radiusTo = 30;
                        stroke = 2.5f;
                        color1 = color2 = Pal.redderDust;
                    }});
                }};
            }});
        }};
        herald = new UnitType("herald") {{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            health = 40000;
            armor = 300;
            speed = 2;
            maxRange = 200;

            abilities.add(new BoostExplosionAbility());

            weapons.add(new Weapon() {{
                mirror = false;
                x = y = 0;
                reload = 150;
                shootStatus = StatusEffects2.back;
                shootStatusDuration = 10;

                shoot = new ShootBarrel() {{
                    shots = 6;
                    shotDelay = 1;
                    barrels = new float[]{0f, 0f, 180f};
                }};

                bullet = new BulletType(0, 0) {{
                    lifetime = 0;
                    collides = collidesAir = collidesTiles = false;
                    reflectable = absorbable = hittable = false;
                    keepVelocity = false;

                    recoil = 10;
                    rangeOverride = 150;

                    shootEffect = Fx.none;
                    shootSound = Sounds.none;
                    hitEffect = despawnEffect = Fx.none;
                }};
            }});
        }};

        boss.add(velocity);
        boss.add(velocity_d);
        boss.add(velocity_s);
        boss.add(hidden);
        boss.add(cave);
    }
}