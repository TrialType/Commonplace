package Commonplace.Loader.Override;

import Commonplace.AI.FlyingFollowFarAI;
import Commonplace.Entities.Shoot.ContinuesShoot;
import Commonplace.Entities.Unit.MissElevationMoveUnit;
import Commonplace.Entities.Unit.ReplenishmentLegsEventUnit;
import Commonplace.Entities.Unit.ReplenishmentPayloadEventUnit;
import Commonplace.Entities.Unit.ReplenishmentTankEventUnit;
import Commonplace.Loader.Special.Effects;
import Commonplace.Loader.DefaultContent.StatusEffects2;
import Commonplace.Entities.Ability.*;
import Commonplace.Entities.BulletType.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.RegionPart;
import mindustry.entities.part.ShapePart;
import mindustry.entities.pattern.*;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;
import mindustry.type.weapons.PointDefenseWeapon;
import mindustry.type.weapons.RepairBeamWeapon;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.tilePayload;
import static mindustry.content.UnitTypes.*;

public class UnitOverride {
    static Weapon weapon;
    static Color color;

    public static void load() {
        alpha.health = 200;
        beta.health = 250;
        gamma.health = 300;

        evoke.buildSpeed = 1.7f;
        evoke.mineSpeed = 9f;
        incite.buildSpeed = 2f;
        incite.mineSpeed = 12f;
        emanate.buildSpeed = 2.3f;
        emanate.mineSpeed = 13.5f;

        dagger.health = 170;
        mace.health = 1000;
        fortress.health = 1800;
        scepter.health = 31500;
        reign.health = 84000;

        nova.health = 220;
        pulsar.health = 560;
        quasar.health = 2200;
        vela.health = 22000;
        corvus.health = 77000;

        crawler.health = 350;
        atrax.health = 1200;
        spiroct.health = 1500;
        arkyid.health = 28000;
        toxopid.health = 77000;

        flare.health = 120;
        horizon.health = 500;
        zenith.health = 1500;
        antumbra.health = 25200;
        eclipse.health = 77000;

        mono.health = 1200;
        poly.health = 700;
        mega.health = 6000;
        quad.health = 22000;
        oct.health = 77000;

        risso.health = 350;
        minke.health = 800;
        bryde.health = 1410;
        sei.health = 22000;
        omura.health = 77000;

        retusa.health = 400;
        oxynoe.health = 1060;
        cyerce.health = 1870;
        aegires.health = 42000;
        navanax.health = 70000;

        stell.health = 2125;
        locus.health = 8350;
        precept.health = 17500;
        vanquish.health = 38500;
        conquer.health = 77000;

        merui.health = 1700;
        cleroi.health = 3850;
        anthicus.health = 10150;
        tecta.health = 26550;
        collaris.health = 63000;

        elude.health = 1500;
        avert.health = 3850;
        obviate.health = 8050;
        quell.health = 22000;
        disrupt.health = 42000;
        /*===================================================================================================*/
        /*===================================================================================================*/
        /*===================================================================================================*/
        /*===================================================================================================*/
        alpha.armor = 3f;
        alpha.buildSpeed = 1f;
        alpha.mineSpeed = 8f;
        alpha.weapons.first().bullet = new ProtectKillerBulletType() {{
            damage = 11;
            speed = 2.5f;
            width = 7f;
            height = 9f;
            lifetime = 60f;
            shootEffect = Fx.shootSmall;
            smokeEffect = Fx.shootSmallSmoke;
            buildingDamageMultiplier = 0.01f;
            damageArmorMultiplier = 1;
            maxArmorDamageAdder = 15;
            damageShieldMultiplier = 1.25f;
            maxShieldDamageAdder = 1000;
        }};

        beta.armor = 5f;
        beta.buildSpeed = 1.5f;
        beta.mineSpeed = 9f;
        beta.weapons.first().bullet = new ProtectKillerBulletType() {{
            damage = 11;
            speed = 3;
            width = 7f;
            height = 9f;
            lifetime = 60f;
            shootEffect = Fx.shootSmall;
            smokeEffect = Fx.shootSmallSmoke;
            buildingDamageMultiplier = 0.01f;
            damageArmorMultiplier = 1.1f;
            maxArmorDamageAdder = 25;
            damageShieldMultiplier = 1.5f;
            maxShieldDamageAdder = 1250;
        }};

        gamma.armor = 8f;
        gamma.mineSpeed = 12f;
        gamma.weapons.first().bullet = new ProtectKillerBulletType() {{
            damage = 11;
            speed = 3.5f;
            width = 6.5f;
            height = 11f;
            lifetime = 70f;
            shootEffect = Fx.shootSmall;
            smokeEffect = Fx.shootSmallSmoke;
            buildingDamageMultiplier = 0.01f;
            homingPower = 0.04f;
            damageArmorMultiplier = 1.2f;
            maxArmorDamageAdder = 35;
            damageShieldMultiplier = 1.75f;
            maxShieldDamageAdder = 1500;
        }};
        /*-----------------------------------------------------------------------------*/
        dagger.speed = 0.3f;
        weapon = dagger.weapons.get(0);
        weapon.reload = 45;
        weapon.shoot = new ShootPattern() {{
            shots = 3;
            shotDelay = 2;
        }};
        weapon.bullet = new ProtectKillerBulletType() {{
            damage = 15;
            speed = 10;
            lifetime = 25;
            width = height = 8;
            shrinkX = shrinkY = 0;
            minArmor = 7;
            damageArmorMultiplier = 1.5f;
            maxArmorDamageAdder = 20;
            keepVelocity = false;
        }};

        mace.armor = 8;
        mace.weapons.get(0).shootCone = 200;
        mace.weapons.get(0).bullet.incendChance = 1;
        mace.weapons.get(0).bullet.incendAmount = 2;
        weapon = new Weapon() {{
            reload = 600;
            inaccuracy = 15;
            shootY = 2f;
            x = y = 0;
            mirror = false;
            shootSound = Sounds.flame;
            shootStatus = StatusEffects.fast;
            shootStatusDuration = 350;
            shoot = new ShootMulti(new ShootPattern() {{
                shots = 120;
                shotDelay = 1;
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
        mace.weapons.add(weapon);

        fortress.abilities.add(new StatusFieldAbility(StatusEffects2.fireKiller, 300, 120, 60) {{
            activeEffect = new WrapEffect(Fx.overdriveWave, Pal.techBlue);
        }});
        weapon = fortress.weapons.get(0);
        weapon.shoot.shots = 5;
        weapon.inaccuracy = 10;
        weapon.bullet.createChance = 0.75f;
        weapon.bullet.damage = 40;
        weapon.bullet.splashDamage = 120;
        weapon.bullet.incendChance = 1;
        weapon.bullet.incendAmount = 5;
        weapon.bullet.puddleLiquid = Liquids.oil;
        weapon.bullet.puddles = 5;
        weapon.bullet.puddleRange = 16;
        weapon.bullet.puddleAmount = 20;

        weapon = scepter.weapons.first();
        weapon.bullet.damage = 70;
        weapon.bullet.lightningDamage = 30;
        weapon.bullet.fragBullets = 2;
        weapon.bullet.fragBullet = new BulletType(4f, 1) {{
            despawnEffect = hitEffect = Fx.none;
            hitSound = despawnSound = Sounds.none;
            reflectable = hittable = false;
            lifetime = 15;
            lightning = 4;
            lightningLength = 8;
            lightningColor = Pal.surge;
            lightningDamage = 17;
        }};
        weapon = scepter.weapons.get(1);
        weapon.reload = 8;
        weapon.bullet = new ProtectKillerBulletType() {{
            speed = 7.5f;
            damage = 15;

            minArmor = 3;
            maxArmorDamageAdder = 60;
            damageArmorMultiplier = 5;

            minShield = 250;
            maxShieldDamageAdder = 300;
            damageShieldMultiplier = 1.2f;

            width = 7f;
            height = 9f;
            lifetime = 20f;
        }};
        weapon = scepter.weapons.get(2);
        weapon.shoot = new ShootSpread(5, 12);
        weapon.bullet.speed = 5;
        weapon.bullet.damage = 20;
        weapon.bullet.lifetime = 30;

        reign.weapons.get(0).bullet.damage = 240;
        reign.weapons.get(0).bullet.splashDamage = 54;
        reign.weapons.get(0).bullet.fragBullet.damage = 60;
        reign.weapons.get(0).bullet.fragBullet.splashDamage = 45;
        reign.weapons.get(0).bullet.fragBullets = 6;
        /*-----------------------------------------------------------------------------*/
        crawler.health = 350;
        crawler.speed = 2.5f;
        crawler.abilities.add(new StatusOwnAbility(StatusEffects2.fearless, 900, 600, 0));
        weapon = crawler.weapons.get(0);
        weapon.reload = 30;
        weapon.bullet = new PercentExplosionBulletType(100f, 44f) {{
            buildingDamageMultiplier = 2.5f;

            collidesTiles = false;
            scaledSplashDamage = false;

            hitSound = Sounds.explosion;
            hitEffect = Fx.pulverize;

            shootEffect = new ExplosionEffect() {{
                lifetime = 25;
                clip = 70;

                smokes = 24;
                smokeRad = 70;
                smokeSize = 4;
                smokeSizeBase = 0;
                smokeColor = Color.valueOf("EEaA88ff");

                sparks = 36;
                sparkRad = 70;
                sparkLen = 4;
                sparkColor = Color.valueOf("EEaA88ff");

                waveLife = 25;
                waveStroke = 2;
                waveRad = 80;
                waveRadBase = 0;
                waveColor = Color.valueOf("EEaA88ff");
            }};
        }};

        atrax.speed = 0.3f;
        atrax.health = 1200;
        atrax.armor = 8f;
        atrax.legSplashDamage = 70f;
        atrax.legSplashRange = 16f;
        atrax.targetAir = true;
        weapon = atrax.weapons.first();
        weapon.shoot.firstShotDelay = 30;
        weapon.reload = 90;
        weapon.bullet.collidesAir = true;
        ((LiquidBulletType) weapon.bullet).orbSize = 6;
        ((LiquidBulletType) weapon.bullet).puddleSize = 25;
        weapon.bullet.lifetime = 100;
        weapon.bullet.speed = 4;
        weapon.bullet.drag = 0.02f;
        weapon.bullet.homingDelay = 10;
        weapon.bullet.homingPower = 0.1f;
        weapon.bullet.homingRange = 200;
        weapon.bullet.pierceBuilding = weapon.bullet.pierce = true;
        weapon.bullet.pierceCap = 4;
        weapon.bullet.intervalBullets = 2;
        weapon.bullet.intervalDelay = 5;
        weapon.bullet.intervalAngle = 90;
        weapon.bullet.intervalSpread = 180;
        weapon.bullet.bulletInterval = 3;
        weapon.bullet.intervalBullet = new LiquidBulletType(Liquids.slag) {{
            reflectable = absorbable = hittable = false;
            hitEffect = despawnEffect = Fx.none;
            despawnHit = true;
            speed = 2;
            damage = 6;
            lifetime = 1;
            puddleSize = 15;
        }};
        weapon.bullet.chargeEffect = new Effect(31, 20, c -> {
            Draw.color(Liquids.slag.color);
            Fill.circle(c.x, c.y, 6 * c.fin());
        });
        weapon.bullet.hitEffect = weapon.bullet.despawnEffect = new Effect(16, e -> {
            color(e.color);
            randLenVectors(e.id, 14, 2f + e.fin() * 25f, e.rotation, 120, (x, y) -> Fill.circle(e.x + x, e.y + y, e.fout() * 4f));
        });

        spiroct.health = 1500;
        spiroct.armor = 12;
        spiroct.speed = 0.45f;
        weapon = spiroct.weapons.get(0);
        weapon.bullet = new SapRadiusBulletType() {{
            sapStrength = 0.5f;
            length = 100f;
            damage = 33;
            width = 0.54f;
            lifetime = 35f;
            knockback = -1.24f;
            despawnEffect = Fx.none;
            shootEffect = Fx.shootSmall;
            hitColor = color = Color.valueOf("bf92f9");
        }};
        weapon = spiroct.weapons.get(1);
        weapon.bullet = new SapRadiusBulletType() {{
            sapStrength = 0.8f;
            length = 125;
            damage = 28;
            shootEffect = Fx.shootSmall;
            hitColor = color = Color.valueOf("bf92f9");
            despawnEffect = Fx.none;
            width = 0.4f;
            lifetime = 25f;
            knockback = -0.65f;
        }};
        spiroct.abilities.add(new SapAbility());

        arkyid.armor = 15;
        weapon = arkyid.weapons.get(0);
        weapon.alternate = false;
        weapon.reload = 90;
        weapon.bullet = new BasicBulletType(1.3f, 35, "circle") {{
            lifetime = 120;
            width = height = 8;
            shrinkX = shrinkY = 0;
            reflectable = false;
            keepVelocity = false;
            lightningColor = Pal.sapBullet;
            shootEffect = smokeEffect = Fx.none;
            hitEffect = despawnEffect = Effects.explosionSmall;
            backColor = frontColor = hitColor = Pal.sapBullet;
            trailEffect = new Effect(75, r -> {
                Rand rand = new Rand(r.id);
                Draw.color(Pal.sapBullet);
                for (int i = 0; i < 8; i++) {
                    float range = rand.random(25);
                    float angle = rand.random(360);
                    Tmp.v1.trns(angle, 4 + range * r.fin());
                    Fill.circle(Tmp.v1.x + r.x, Tmp.v1.y + r.y, 3 * r.fout() + 1);
                }
            });

            trailInterval = 12;
            knockback = 0.8f;
            splashDamage = 90f;
            splashDamageRadius = 70f;
            hitShake = despawnShake = 5;
            status = StatusEffects.sapped;
            statusDuration = 60f * 10;
        }};
        weapon = arkyid.weapons.get(1);
        weapon.reload = 4;
        weapon.bullet.damage = 15f;
        weapon = arkyid.weapons.get(2);
        weapon.reload = 45;
        weapon.shoot.shots = 9;
        weapon.shoot.shotDelay = 4;
        weapon.shootSound = Sounds.none;
        weapon.bullet = new BasicBulletType(2f, 35, "circle") {{
            lifetime = 70;
            inaccuracy = 60;
            drag = 0.015f;
            weaveMag = 8;
            weaveScale = 3.25f;
            pierce = pierceBuilding = true;
            pierceCap = 5;
            width = height = 4;
            shrinkX = shrinkY = 0;
            backColor = frontColor = hitColor = Pal.sapBullet;
            shootEffect = smokeEffect = Fx.none;
            hitEffect = despawnEffect = new Effect(60, r -> {
                Rand rand = new Rand(r.id);
                Draw.color(Pal.sapBullet);
                for (int i = 0; i < 8; i++) {
                    float range = rand.random(10);
                    float angle = rand.random(360);
                    Tmp.v1.trns(angle, 4 + range * r.fin());
                    Fill.circle(Tmp.v1.x + r.x, Tmp.v1.y + r.y, 3 * r.fout() + 1);
                }
            });
        }};
        weapon = arkyid.weapons.get(3);
        weapon.shake = 0;
        weapon.reload = 30;
        weapon.shootSound = Sounds.sap;
        weapon.bullet = new SapBulletType() {{
            width = 1;
            length = 85f;
            damage = 75;
            lifetime = 30f;
            sapStrength = 1f;
            hitShake = despawnShake = 0;
            smokeEffect = Fx.none;
            despawnEffect = Fx.none;
            shootEffect = Fx.shootSmall;
            hitEffect = Fx.hitLaserBlast;
            hitColor = color = Color.valueOf("bf92f9");
        }};

        toxopid.armor = 22;
        toxopid.rotateSpeed = 2.5f;
        LightningBulletType l = new LightningBulletType() {{
            damage = 70;
            lightningColor = Pal.sapBullet;
            lightningLength = 15;
        }};
        weapon = toxopid.weapons.get(0);
        weapon.shoot = new ShootPattern();
        weapon.bullet = new ShrapnelBulletType() {{
            length = 120f;
            damage = 130f;
            width = 28f;
            serrationLenScl = 7f;
            serrationSpaceOffset = 60f;
            serrationFadeOffset = 0f;
            serrations = 10;
            serrationWidth = 6f;
            fromColor = Pal.sapBullet;
            toColor = Pal.sapBulletBack;
            shootEffect = smokeEffect = Fx.sparkShoot;
            spawnBullets.add(l, l, l, l);
        }};
        weapon = toxopid.weapons.get(1);
        weapon.rotateSpeed = 2f;
        weapon.bullet.fragBullets = 13;
        toxopid.abilities.add(new FaceSapAbility());
        /*-----------------------------------------------------------------------------*/
        flare.armor = 3;
        flare.speed = 4;
        flare.circleTarget = true;
        weapon = flare.weapons.first();
        weapon.shoot.shots = 2;
        weapon.shoot.shotDelay = 6;
        weapon.bullet = new FireBulletType(4, 9) {{
            lifetime = 45;
            radius = 0;
            ammoMultiplier = 2;
            hittable = absorbable = reflectable = false;
            collidesTiles = true;
            collides = true;
            pierce = true;
            pierceBuilding = true;
            fireTrailChance = 0.25f;
            velMin = velMax = speed;
        }};

        horizon.armor = 4;
        horizon.range = 24;
        horizon.speed = 2.5f;
        weapon = horizon.weapons.get(0);
        weapon.minShootVelocity = 0.1f;
        weapon.shootStatus = StatusEffects2.deploy;
        weapon.shootStatusDuration = 20;
        weapon.shoot.shots = 3;
        weapon.bullet.buildingDamageMultiplier = 1.65f;

        zenith.armor = 6;
        zenith.speed = 2;
        weapon = zenith.weapons.get(0);
        weapon.bullet.damage = 35;
        weapon.bullet.splashDamage = 32;
        zenith.weapons.add(new Weapon() {{
            reload = 150;
            x = 7f;
            rotate = true;
            shoot = new ShootMulti(new ShootPattern() {{
                shots = 10;
                shotDelay = 3;
            }}, new ShootPattern() {{
                shots = 3;
            }});
            inaccuracy = 5f;
            shootSound = Sounds.missile;
            bullet = new BulletType(30, 0) {{
                keepVelocity = false;
                splashDamageRadius = 25;
                splashDamage = 35;
                lifetime = 4;
                hitEffect = Fx.blastExplosion;
                despawnEffect = Fx.blastExplosion;
            }};
        }});

        antumbra.armor = 8;
        antumbra.abilities.add(new SprintingAbility2() {{
            sprintingReload = 420;
            sprintingDamage = 100;
            sprintingDuration = 10;
            sprintingLength = 10;
            sprintingRadius = 150;
            status = StatusEffects2.frenzy;
            statusDuration = 300;
        }});
        weapon = antumbra.weapons.first();
        weapon.bullet.damage *= 1.2f;
        weapon.bullet.status = StatusEffects.blasted;
        weapon.bullet.statusDuration = 60;

        weapon = eclipse.weapons.get(1);
        weapon.reload = 90;
        weapon.recoil = 5;
        weapon.shootSound = Sounds.largeExplosion;
        weapon.bullet = new BasicBulletType(0.2f, 500, "circle") {{
            frontColor = backColor = Pal.bulletYellowBack;
            width = height = 5;
            shrinkX = shrinkY = 0;

            hitShake = 8;
            drag = -0.01f;
            lifetime = 220f;
            splashDamage = 350;
            keepVelocity = false;
            splashDamageRadius = 50;
            scaledSplashDamage = true;
            buildingDamageMultiplier = 3;

            hitSoundVolume = 2;
            hitSound = Sounds.largeExplosion;

            trailInterval = 10;
            hitEffect = despawnEffect = new ExplosionEffect() {{
                lifetime = 45;
                sparks = 15;
                sparkRad = 42;
                sparkStroke = 1;
                sparkLen = 4;

                waveColor = Pal.bulletYellowBack;
                waveRad = 55;
                waveLife = 45;

                smokes = 13;
                smokeSizeBase = 1;
                smokeSize = 6;
                smokeRad = 42;
            }};
        }};
        eclipse.abilities.add(new ForceFieldAbility(45, 3, 4500, 1.5f));
        /*-----------------------------------------------------------------------------*/
        mono.mineSpeed = 4f;
        mono.abilities.add(new RepairOwnAbility(100, 180, 0));

        poly.weapons.get(0).bullet.splashDamageRadius = 20;

        mega.armor = 15;
        mega.payloadCapacity = 3 * 3 * tilePayload;
        mega.weapons.add(new PointDefenseWeapon() {{
            y = -6;
            rotate = true;
            rotateSpeed = 4;
            reload = 10;
            targetInterval = 10;
            targetSwitchInterval = 10;
            bullet = new BulletType() {{
                damage = 8;
            }};
        }});

        quad.speed = 2;
        quad.range = 270;
        quad.payloadCapacity = 4 * 4 * tilePayload;
        weapon = quad.weapons.get(0);
        weapon.ignoreRotation = true;
        weapon.shootCone = 180f;
        weapon.reload = 300;
        weapon.ejectEffect = Fx.none;
        weapon.bullet = new StrikeBulletType() {{
            sprite = "large-bomb";
            width = height = 960 / 4f;
            maxRange = 300;
            hitColor = backColor = Pal.heal;
            frontColor = Color.white;
            mixColorTo = Color.white;
            hitSound = Sounds.plasmaboom;
            hitShake = 4f;
            lifetime = 120;
            despawnEffect = Effects.BombLarge;
            hitEffect = Fx.massiveExplosion;
            keepVelocity = false;
            spin = 2f;
            shrinkX = shrinkY = 0.7f;
            speed = 0f;
            collides = false;
            hitTeam = true;
            healPercent = 15f;
            splashDamage = 400f;
            splashDamageRadius = 550f;
        }};

        oct.armor = 30;
        oct.payloadCapacity = 6.5f * 6.5f * tilePayload;
        oct.abilities.clear();
        oct.abilities.add(new ForceFieldAbility(150, 5, 20000, 480, 8, 0),
                new ForceFieldAbility(210, 4, 10000, 480, 6, 0));
        oct.abilities.add(new StatusFieldAbility(StatusEffects2.back, 30, 150, 150));
        /*-----------------------------------------------------------------------------*/
        risso.armor = 7;
        weapon = risso.weapons.get(0);
        weapon.reload = 6;
        weapon.bullet.damage = 14f;
        weapon = risso.weapons.get(1);
        weapon.shoot.shots = 2;
        weapon.bullet = new BasicBulletType(11, 18, "missile") {{
            keepVelocity = true;
            weaveRandom = true;
            width = 8f;
            height = 8f;
            shrinkY = 0f;
            splashDamageRadius = 25f;
            splashDamage = 15;
            lifetime = 25;
            status = StatusEffects.slow;
            statusDuration = 60;
            trailColor = Color.gray;
            backColor = Pal.bulletYellowBack;
            frontColor = Pal.bulletYellow;
            hitEffect = Fx.blastExplosion;
            despawnEffect = Fx.blastExplosion;
        }};

        minke.armor = 14;
        minke.speed = 4;
        weapon = minke.weapons.get(0);
        weapon.bullet.damage = 15f;
        weapon.bullet.splashDamage = 27;
        weapon.bullet.homingDelay = 0;
        weapon.bullet.homingRange = 252;
        weapon.bullet.homingPower = 0.1f;
        weapon = minke.weapons.get(1);
        weapon.reload = 5;
        weapon.inaccuracy = 45;
        weapon.bullet.damage = 30;
        weapon.bullet.splashDamage = 60;

        bryde.armor = 20;
        ShieldRegenFieldAbility ability = (ShieldRegenFieldAbility) bryde.abilities.first();
        ability.amount = 100;
        ability.max = 400;
        weapon = bryde.weapons.get(0);
        weapon.reload = 80;
        weapon.bullet.speed = 4;
        weapon.bullet.lifetime = 16f;
        weapon.bullet.status = StatusEffects.none;
        ((BasicBulletType) weapon.bullet).shrinkInterp = Interp.reverse;
        weapon.bullet.fragBullets = 8;
        weapon.bullet.fragLifeMin = 0.65f;
        weapon.bullet.fragRandomSpread = 30;
        weapon.bullet.fragVelocityMin = 0.65f;
        weapon.bullet.fragBullet = new ArtilleryBulletType(6f, 15) {{
            trailMult = 0.8f;
            hitEffect = Fx.massiveExplosion;
            knockback = 1.5f;
            lifetime = 51.2f / 1.5f;
            height = 15.5f;
            width = 15f;
            splashDamageRadius = 40f;
            splashDamage = 70f;
            backColor = Pal.missileYellowBack;
            frontColor = Pal.missileYellow;
            trailEffect = Fx.artilleryTrail;
            trailSize = 6f;
            hitShake = 4f;

            status = StatusEffects.blasted;
            statusDuration = 60f;
        }};
        weapon = bryde.weapons.get(0);
        weapon.bullet.pierceCap = 2;
        weapon.bullet.pierce = true;
        weapon.bullet.pierceBuilding = true;

        weapon = sei.weapons.first();
        weapon.rotateSpeed = 6;
        weapon.bullet.statusDuration = 90;
        weapon.bullet.status = StatusEffects.blasted;
        weapon = sei.weapons.get(1);
        weapon.shoot.shots = 4;
        weapon.bullet.pierceCap = 2;
        weapon.bullet.knockback = 0.05f;
        sei.abilities.add(new StatusOwnAbility(StatusEffects.fast, 30, 60, 0f));
        sei.abilities.add(new StatusEnemyAbility(StatusEffects2.torn, 90, 150, 180f));

        weapon = omura.weapons.first();
        weapon.bullet.fragBullets = 1;
        weapon.bullet.fragOnHit = true;
        weapon.bullet.setDefaults = false;
        weapon.bullet.pierceDamageFactor = 0.35f;
        weapon.bullet.fragBullet = new BulletType(0, 1) {{
            lifetime = 1;
            splashDamage = 45;
            splashDamageRadius = 16;
            buildingDamageMultiplier = 0.75f;
            hitEffect = despawnEffect = Fx.none;

            collides = false;
            reflectable = hittable = false;
        }};
        omura.abilities.clear();
        omura.abilities.add(new UnitSpawnSupperAbility(flare, 12 * 60f, 19.25f, -31.75f) {{
            status = ObjectMap.of(
                    StatusEffects2.frenzy, 60f * 60 * 60,
                    StatusEffects2.back, 60f * 60 * 60,
                    StatusEffects.overclock, 60f * 60 * 60
            );
        }}, new UnitSpawnSupperAbility(flare, 12 * 60f, -19.25f, -31.75f) {{
            status = ObjectMap.of(
                    StatusEffects2.frenzy, 60f * 60 * 60,
                    StatusEffects2.back, 60f * 60 * 60,
                    StatusEffects.overclock, 60f * 60 * 60
            );
        }});
        /*-----------------------------------------------------------------------------*/
        retusa.speed = 1;
        retusa.abilities.add(new StatusFieldAbility(StatusEffects.fast, 60, 90, 60));
        weapon = retusa.weapons.first();
        ((RepairBeamWeapon) weapon).repairSpeed = 0.45f;
        weapon = retusa.weapons.get(2);
        weapon.reload = 45;
        weapon.shoot.shots = 2;
        weapon.inaccuracy = 5;
        weapon.bullet = new BasicBulletType() {{
            sprite = "mine-bullet";
            width = height = 8f;
            layer = Layer.scorch;
            shootEffect = smokeEffect = Fx.none;

            maxRange = 50f;
            healPercent = 4f;

            backColor = Pal.heal;
            frontColor = Color.white;
            mixColorTo = Color.white;

            hitSound = Sounds.plasmaboom;

            hitSize = 22f;

            pierce = pierceBuilding = true;
            pierceCap = 2;
            collidesAir = false;
            collideFloor = false;
            keepVelocity = false;

            lifetime = 100f;
            buildingDamageMultiplier = 1.7f;

            hitEffect = new MultiEffect(Fx.blastExplosion, Fx.greenCloud);

            shrinkX = shrinkY = 0f;

            inaccuracy = 2f;
            weaveMag = 5f;
            weaveScale = 4f;
            speed = 0.7f;
            drag = -0.017f;
            homingPower = 0.05f;
            trailColor = Pal.heal;
            trailWidth = 3f;
            trailLength = 8;

            splashDamage = 33f;
            splashDamageRadius = 32f;
        }};

        oxynoe.speed = 1.66f;
        StatusFieldAbility statusFieldAbility = (StatusFieldAbility) oxynoe.abilities.get(0);
        statusFieldAbility.range = 90;
        statusFieldAbility.duration = 420;
        weapon = oxynoe.weapons.get(0);
        weapon.reload = 3;
        weapon.shootStatus = StatusEffects2.frenzy;
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

        weapon = cyerce.weapons.first();
        ((RepairBeamWeapon) weapon).repairSpeed = 0.4f;
        weapon = cyerce.weapons.get(1);
        BulletType bullet = weapon.bullet.fragBullet;
        bullet.speed = 3.8f;
        weapon.bullet = new SummonBulletType() {{
            damage = 40;
            speed = 2.5f;
            sprite = "missile-large";
            collidesGround = collidesAir = true;
            keepVelocity = false;
            width = height = 12f;
            shrinkY = 0f;
            drag = -0.003f;
            homingRange = 60f;
            lightRadius = 60f;
            lightOpacity = 0.7f;
            lightColor = Pal.heal;

            splashDamageRadius = 30f;
            splashDamage = 40;

            lifetime = 80f;
            backColor = Pal.heal;
            frontColor = Color.white;

            hitEffect = new ExplosionEffect() {{
                lifetime = 28f;
                waveStroke = 6f;
                waveLife = 10f;
                waveRadBase = 7f;
                waveColor = Pal.heal;
                waveRad = 30f;
                smokes = 6;
                smokeColor = Color.white;
                sparkColor = Pal.heal;
                sparks = 6;
                sparkRad = 35f;
                sparkStroke = 1.5f;
                sparkLen = 4f;
            }};

            weaveScale = 8f;
            weaveMag = 1f;

            trailColor = Pal.heal;
            trailWidth = 4.5f;
            trailLength = 29;

            summonRange = 220;
            summonBullets = 20;
        }};
        ((SummonBulletType) weapon.bullet).summonBullet = bullet;

        aegires.abilities.clear();
        aegires.abilities.add(new EnergyFieldAbility(17, 10, 180) {{
            statusDuration = 60f * 6f;
            maxTargets = 15;
            healPercent = 0.4f;
            sameTypeHealMult = 0.6f;
        }});
        aegires.abilities.add(new TimeGrowDamageAbility(1.5f, 180) {{
            buildingMul = 0.75f;
        }});

        navanax.armor = 20;
        weapon = navanax.weapons.first();
        weapon.reload = 60;
        weapon.continuous = false;
        weapon.shootSound = Sounds.shotgun;
        weapon.bullet = new ShrapnelBulletType() {{
            width = 15f;
            damage = 80;
            lifetime = 10;
            length = 115f;
            drawSize = 140f;
            maxRange = 110f;

            hitLarge = true;
            healPercent = 0.2f;
            collidesTeam = true;

            statusDuration = 90;
            status = StatusEffects.shocked;

            fromColor = Pal.heal;

            smokeEffect = Fx.none;
            shootEffect = Fx.shootHeal;
            hitEffect = Fx.hitMeltHeal;
        }};
        weapon = navanax.weapons.get(2);
        weapon.reload = 90;
        weapon.inaccuracy = 6;
        weapon.shoot.shots = 30;
        weapon.shoot.shotDelay = 2;
        weapon.continuous = false;
        weapon.shootSound = Sounds.lasershoot;
        weapon.bullet = new LaserBoltBulletType() {{
            speed = 4;
            damage = 15;
            lifetime = 25;
            maxRange = 110f;

            statusDuration = 90;
            status = StatusEffects2.disturb;
            frontColor = backColor = Pal.heal;

            smokeEffect = Fx.none;
            hitEffect = Fx.hitMeltHeal;
            shootEffect = Fx.shootHeal;
        }};
        weapon = navanax.weapons.get(3);
        weapon.reload = 60;
        weapon.continuous = false;
        weapon.shootSound = Sounds.laser;
        weapon.bullet = new LaserBulletType() {{
            width = 30;
            damage = 80;
            length = 110f;
            lifetime = 15f;
            maxRange = 110f;
            drawSize = 140f;
            lightningLength = 12;
            sideWidth = sideLength = 0;

            statusDuration = 90;
            status = StatusEffects.electrified;

            smokeEffect = Fx.none;
            hitEffect = Fx.hitMeltHeal;
            shootEffect = Fx.shootHeal;

            lightningColor = Pal.heal.cpy().a(.2f);
            colors = new Color[]{Pal.heal.cpy().a(.2f), Pal.heal, Color.white};
        }};
        ContinuousLaserBulletType clb = (ContinuousLaserBulletType) navanax.weapons.get(1).bullet;
        clb.width = 6f;
        clb.damage = 35;
        clb.length = 115f;
        clb.maxRange = 110f;

        /*-----------------------------------------------------------------------------*/
        stell.weapons.first().reload = 70f;
        stell.weapons.first().recoil = 2.5f;
        stell.weapons.first().shootSound = Sounds.dullExplosion;
        stell.weapons.first().bullet = new PointBulletType() {{
            speed = 4f;
            damage = 40;
            hitSize = 4f;
            lifetime = 40f;

            splashDamage = 30;
            splashDamageRadius = 28;

            trailEffect = Fx.none;
            smokeEffect = Fx.shootBigSmoke;
            shootEffect = Fx.shootBigColor;
            despawnEffect = hitEffect = new MultiEffect(Fx.artilleryTrailSmoke, Fx.hitBulletColor);
            hitColor = trailColor = Color.valueOf("feb380");
        }};

        weapon = locus.weapons.first();
        weapon.bullet.damage = 40;
        weapon.bullet.fragOnHit = true;
        weapon.bullet.despawnHit = false;
        weapon.bullet.setDefaults = false;
        weapon.bullet.fragBullets = 4;
        weapon.bullet.fragBullet = new RailBulletType() {{
            length = 25f;
            damage = 1;
            pierceArmor = true;
            hitColor = Color.valueOf("feb380");
            hitEffect = endEffect = Fx.hitBulletColor;
            pierceDamageFactor = 1f;

            smokeEffect = Fx.colorSpark;

            endEffect = new Effect(14f, e -> {
                color(e.color);
                Drawf.tri(e.x, e.y, e.fout() * 1.5f, 5f, e.rotation);
            });

            shootEffect = new Effect(10, e -> {
                color(e.color);
                float w = 1.2f + 7 * e.fout();

                Drawf.tri(e.x, e.y, w, 30f * e.fout(), e.rotation);
                color(e.color);

                for (int i : Mathf.signs) {
                    Drawf.tri(e.x, e.y, w * 0.9f, 18f * e.fout(), e.rotation + i * 90f);
                }

                Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
            });

            lineEffect = new Effect(20f, e -> {
                if (!(e.data instanceof Vec2 v)) return;

                color(e.color);
                stroke(e.fout() * 0.9f + 0.6f);

                Fx.rand.setSeed(e.id);
                for (int i = 0; i < 7; i++) {
                    Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                    Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 20f * Fx.rand.random(0.5f, 1f) + 0.3f);
                }

                e.scaled(14f, b -> {
                    stroke(b.fout() * 1.5f);
                    color(e.color);
                    Lines.line(e.x, e.y, v.x, v.y);
                });
            });
        }};

        weapon = precept.weapons.first();
        weapon.bullet.damage = 140;
        weapon.bullet.splashDamage = 65f;
        weapon.bullet.fragBullet.damage = 45f;
        weapon.bullet.intervalBullet = weapon.bullet.fragBullet;
        weapon.bullet.intervalBullets = 4;
        weapon.bullet.bulletInterval = 6;
        weapon.bullet.intervalDelay = 7;
        weapon.bullet.fragBullets = 7;

        vanquish.constructor = ReplenishmentTankEventUnit::create;
        weapon = vanquish.weapons.first();
        weapon.shoot = new ContinuesShoot().shootDelay(5);
        weapon.bullet.speed = 5;
        weapon.bullet.pierce = false;
        weapon.bullet.pierceBuilding = false;
        weapon.bullet.pierceCap = 1;
        weapon.bullet.fragBullets = 4;
        weapon.bullet.fragSpread = 30;
        weapon.bullet.fragOnHit = true;
        weapon.bullet.fragBullet = new BasicBulletType(6f, 28f) {{
            sprite = "missile-large";
            width = 8f;
            height = 12f;
            lifetime = 9f;
            hitSize = 4f;
            hitColor = backColor = trailColor = Color.valueOf("feb380");
            frontColor = Color.white;
            trailWidth = 2.8f;
            trailLength = 6;
            hitEffect = despawnEffect = Fx.blastExplosion;
            splashDamageRadius = 16f;
            splashDamage = 30f;

            pierce = pierceBuilding = true;
            pierceCap = 3;

            fragBullets = 8;
            fragLifeMin = 0.5f;
            fragRandomSpread = 360f;
            despawnSound = Sounds.dullExplosion;
            fragBullet = new BasicBulletType(3f, 45) {{
                sprite = "missile-large";
                width = 6.5f;
                height = 11f;
                lifetime = 10;
                hitSize = 3f;
                hitColor = backColor = trailColor = Color.valueOf("feb380");
                frontColor = Color.white;
                trailWidth = 2.5f;
                trailLength = 5;
                hitEffect = despawnEffect = Fx.blastExplosion;
                splashDamageRadius = 16;
                splashDamage = 35f;
            }};
        }};
        vanquish.weapons.get(1).shoot.shots = 2;
        vanquish.weapons.get(1).shoot.shotDelay = 6;
        vanquish.weapons.get(2).shoot.shots = 2;
        vanquish.weapons.get(2).shoot.shotDelay = 6;

        conquer.constructor = ReplenishmentTankEventUnit::create;
        weapon = conquer.weapons.first();
        weapon.bullet.damage = 400;
        Seq<BulletType> bullets = weapon.bullet.spawnBullets;
        for (int i = 0; i < bullets.size; i++) {
            BulletType temp = bullets.get(i);
            @SuppressWarnings("all")
            float fin = (i / 2 + 1) * 2f / bullets.size;
            float life = weapon.bullet.lifetime * fin * 1.35f;
            temp.pierceCap = 5;
            temp.damage = 70;
            temp.fragAngle = 0;
            temp.fragSpread = 180;
            temp.fragVelocityMin = 1;
            temp.fragRandomSpread = 0;
            temp.fragBullets = 2;
            temp.fragOnHit = false;
            temp.fragBullet = new BasicBulletType(weapon.bullet.speed / fin * 0.12f, 35) {{
                drag = 0.002f;
                width = 12f;
                height = 11f;
                lifetime = life;
                hitSize = 5f;
                pierceCap = 5;
                pierce = true;
                pierceBuilding = true;
                hitColor = backColor = trailColor = Color.valueOf("feb380");
                frontColor = Color.white;
                trailWidth = 2.5f;
                trailLength = 7;

                splashDamage = 65f;
                splashDamageRadius = 30f;
                despawnEffect = new ExplosionEffect() {{
                    lifetime = 50f;
                    waveStroke = 4f;
                    waveColor = sparkColor = trailColor;
                    waveRad = 30f;
                    smokeSize = 7f;
                    smokes = 6;
                    smokeSizeBase = 0f;
                    smokeColor = trailColor;
                    sparks = 5;
                    sparkRad = 30f;
                    sparkLen = 3f;
                    sparkStroke = 1.5f;
                }};

                spawnBullets.add(new BasicBulletType(weapon.bullet.speed / fin * 0.2f, 35) {{
                    drag = 0.002f;
                    width = 12f;
                    height = 11f;
                    lifetime = life * 1.05f;
                    hitSize = 5f;
                    pierceCap = 5;
                    pierce = true;
                    pierceBuilding = true;
                    hitColor = backColor = trailColor = Color.valueOf("feb380");
                    frontColor = Color.white;
                    trailWidth = 2.5f;
                    trailLength = 7;

                    splashDamage = 65f;
                    splashDamageRadius = 30f;
                    despawnEffect = new ExplosionEffect() {{
                        lifetime = 50f;
                        waveStroke = 4f;
                        waveColor = sparkColor = trailColor;
                        waveRad = 30f;
                        smokeSize = 7f;
                        smokes = 6;
                        smokeSizeBase = 0f;
                        smokeColor = trailColor;
                        sparks = 5;
                        sparkRad = 30f;
                        sparkLen = 3f;
                        sparkStroke = 1.5f;
                    }};
                }});
            }};
        }
        /*-----------------------------------------------------------------------------*/
        elude.constructor = MissElevationMoveUnit::create;
        elude.speed = 3f;
        elude.drag = 0.02f;
        elude.accel = 0.3f;
        elude.weapons.first().bullet.statusDuration = 150;
        elude.weapons.first().bullet.status = StatusEffects2.disturb;

        avert.weapons.first().bullet.fragBullets = 2;
        avert.weapons.first().bullet.fragVelocityMin = 1;
        avert.weapons.first().bullet.fragBullet = new BasicBulletType(8f, 25) {{
            width = 7f;
            height = 12f;
            lifetime = 30f;

            homingPower = 1;
            homingDelay = 0;
            homingRange = 100;

            pierceCap = 4;
            pierce = pierceBuilding = true;

            trailLength = 5;
            trailWidth = 1.5f;
            frontColor = Color.white;

            shootEffect = Fx.sparkShoot;
            smokeEffect = Fx.shootBigSmoke;

            hitColor = backColor = trailColor = Pal.suppress;
            hitEffect = despawnEffect = Fx.hitBulletColor;
        }};

        weapon = obviate.weapons.first();
        weapon.bullet.splashDamageRadius = 12;
        weapon.bullet.status = StatusEffects.electrified;
        weapon.bullet.statusDuration = 90;
        weapon.bullet.fragBullets = 7;
        weapon.bullet.fragBullet = new MoveLightningBulletType() {{
            damage = 7;
            buildingDamageMultiplier = 0.1f;
            lifetime = 60;
            lightningLength = 5;
            damagePoints = 6;
            points = 30;
            lightningColor = Pal.sap;
            pierceCap = 6;

            hitEffect = despawnEffect = Fx.none;
        }};

        quell.constructor = ReplenishmentPayloadEventUnit::create;
        quell.aiController = FlyingFollowFarAI::create;
        weapon = quell.weapons.first();
        weapon.bullet = new BulletType() {{
            speed = 0f;
            rangeOverride = 5.9f * 84;

            shootEffect = Fx.shootBig;
            smokeEffect = Fx.shootBigSmoke2;

            collidesAir = false;
            keepVelocity = false;

            spawnUnit = new MissileUnitType("quell-missile") {{
                health = 45;
                speed = 4.3f;
                maxRange = 6f;
                rotateSpeed = 8.8f;
                lifetime = 1.2f * 60;
                loopSoundVolume = 0.1f;

                targetAir = false;
                engineLayer = Layer.effect;
                outlineColor = Pal.darkOutline;
                engineColor = trailColor = Pal.sapBulletBack;

                weapons.add(new Weapon() {{
                    shootSound = Sounds.none;
                    shootCone = 360f;
                    mirror = false;
                    reload = 1f;
                    shootOnDeath = true;
                    bullet = new ExplosionBulletType(130f, 25f) {{
                        collidesAir = false;
                        buildingDamageMultiplier = 0.5f;
                        shootEffect = Fx.massiveExplosion;
                    }};
                }});

                abilities.add(new PowerChargeAbility() {{
                    lightningColor = Pal.sap.cpy().mul(1.3f, 1.1f, 1.1f, 1.1f);

                    bullet = new LightningBulletType() {{
                        damage = 7;
                        lifetime = 3;
                        lightningLength = 8;
                        lightningColor = Pal.sap.cpy().mul(1.1f);
                    }};
                }}, new MoveLightningAbility(10, 6, 0.5f, 0, 5, 15, Pal.sap));
            }};
        }};
//        weapon.bullet.rangeOverride = 5.9f * 84;
//        UnitType unit = weapon.bullet.fragBullet.spawnUnit;
//        unit.lifetime = 1.2f * 60;
//        unit.rotateSpeed = 8.8f;
//        unit.weapons.first().bullet.splashDamage = 140f;
//        unit.weapons.first().bullet.buildingDamageMultiplier = 0.5f;
//        unit.abilities.add(new PowerChargeAbility() {{
//            lightningColor = Pal.sap.cpy().mul(1.3f, 1.1f, 1.1f, 1.1f);
//
//            bullet = new LightningBulletType() {{
//                damage = 7;
//                lifetime = 3;
//                lightningLength = 8;
//                lightningColor = Pal.sap.cpy().mul(1.1f);
//            }};
//        }}, new MoveLightningAbility(10, 6, 0.5f, 0, 5, 15, Pal.sap));

        disrupt.constructor = ReplenishmentPayloadEventUnit::create;
        weapon = disrupt.weapons.first().bullet.spawnUnit.weapons.first();
        weapon.bullet = new SupperExplosionBulletType(160f, 25f) {{
            collidesAir = false;
            suppressionRange = 140f;
            shootEffect = new ExplosionEffect() {{
                lifetime = 50f;
                waveStroke = 5f;
                waveLife = 8f;
                waveColor = Color.white;
                sparkColor = smokeColor = Pal.suppress;
                waveRad = 40f;
                smokeSize = 4f;
                smokes = 7;
                smokeSizeBase = 0f;
                sparks = 10;
                sparkRad = 40f;
                sparkLen = 6f;
                sparkStroke = 2f;
            }};

            fragBullets = 1;
            fragBullet = new ContinuousLinkBulletType() {{
                speed = 3;
                damage = 10f;
                lifetime = 60;
                createChance = 0.5f;
                damageInterval = 5;

                splashDamage = 15;
                splashDamageRadius = 10;

                homingDelay = 0;
                homingPower = 0.1f;
                homingRange = 300;

                hitEffect = despawnEffect = Fx.none;
                trailLength = 0;
                trailWidth = 0;
                trailInterval = 10;
                trailEffect = new Effect(12, 40, c -> {
                    color(c.color);
                    randLenVectors(c.id, 12, 2f + 30 * c.finpow(),
                            (x, y) -> Fill.circle(c.x + x, c.y + y, c.fout() * 8));
                    randLenVectors(c.id + 1, 15, 2f + 30 * c.finpow(),
                            (x, y) -> Lines.line(c.x + x, c.y + y,
                                    c.x + x + 6 * x / Mathf.len(x, y), c.y + y + 6 * y / Mathf.len(x, y)));
                }).followParent(false);

                lightningColor = trailColor = hitColor = Pal.sap;
            }};
        }};
        /*-----------------------------------------------------------------------------*/
        weapon = merui.weapons.first();
        weapon.bullet.splashDamageRadius = 30;
        merui.weapons.add(new Weapon("merui-weapon") {{
            shootSound = Sounds.missile;
            mirror = false;
            showStatSprite = false;
            x = 0f;
            y = 1f;
            shootY = 4f;
            reload = 30f;
            cooldownTime = 42f;
            heatColor = Pal.turretHeat;

            bullet = new BasicBulletType() {{
                lifetime = 20;
                damage = 7;
                speed = 1.5f;
                trailWidth = 1f;
                trailLength = 7;
                trailColor = hitColor = Pal.techBlue;

                shootEffect = Fx.none;
                shootSound = Sounds.missile;
                despawnEffect = hitEffect = Fx.hitSquaresColor;
            }};
        }});

        cleroi.weapons.add(new Weapon() {{
            mirror = false;
            x = y = 0;
            shootY = 6;
            reload = 35;
            shoot.shots = 4;
            shoot.shotDelay = 3;
            bullet = new BasicBulletType() {{
                lifetime = 45;
                damage = 18;
                speed = 2.2f;
                inaccuracy = 5;
                homingPower = 0.03f;
                homingRange = 150;
                homingDelay = 5;
                trailWidth = 1f;
                trailLength = 13;
                trailColor = hitColor = Pal.techBlue;

                shootEffect = Fx.none;
                shootSound = Sounds.none;
                despawnEffect = hitEffect = Effects.colorSmoke;
            }};
        }});

        UnitType u = anthicus.weapons.get(0).bullet.spawnUnit;
        u.weapons.get(0).bullet.despawnHit = true;
        u.weapons.get(0).bullet.splashDamage = 280f;
        u.weapons.get(0).bullet.fragLifeMin = 0.4f;
        u.weapons.get(0).bullet.fragVelocityMin = 1f;
        u.weapons.get(0).bullet.fragBullets = 5;
        u.weapons.get(0).bullet.fragBullet = new BulletType(2, 1) {{
            drag = 0.08f;
            lifetime = 45;
            splashDamage = 50;
            splashDamageRadius = 12;
            keepVelocity = false;
            collides = absorbable = reflectable = hittable = false;
            despawnEffect = hitEffect = new MultiEffect(Fx.massiveExplosion, new WrapEffect(Fx.dynamicSpikes, Pal.techBlue, 12f), new WaveEffect() {{
                colorFrom = colorTo = Pal.techBlue;
                sizeTo = 26f;
                lifetime = 13f;
                strokeFrom = 2f;
            }});
            shootEffect = Fx.none;
        }};

        tecta.constructor = ReplenishmentLegsEventUnit::create;
        ShieldArcAbility sab = (ShieldArcAbility) tecta.abilities.first();
        sab.max = 3500;
        sab.regen = 1.2f;
        sab.cooldown = 6 * 60f;
        tecta.abilities.add(new ShieldArcAbility() {{
            region = "tecta-shield";
            radius = 54f;
            angle = 100f;
            regen = 1.6f;
            cooldown = 60f * 10f;
            max = 4000f;
            y = -20f;
            width = 6f;
            whenShooting = true;
        }});
        weapon = tecta.weapons.first();
        weapon.bullet.damage = 70;
        weapon.bullet.splashDamage = 75;
        weapon.bullet.intervalDelay = 6;
        weapon.bullet.intervalRandomSpread = 0;
        weapon.bullet.intervalSpread = 15;
        weapon.bullet.bulletInterval = 7;
        weapon.bullet.intervalBullets = 3;
        weapon.bullet.intervalBullet = new MissileBulletType(3f, 8) {{
            weaveMag = 3;
            weaveScale = 8;
            lifetime = 28;
            splashDamage = 14;
            splashDamageRadius = 12;
            frontColor = Color.white;
            hitSound = Sounds.none;
            width = height = 4f;

            lightColor = trailColor = backColor = Pal.techBlue;
            lightRadius = 16f;
            lightOpacity = 0.7f;

            trailWidth = 1.12f;
            trailChance = -1f;
            trailLength = 8;

            hitEffect = despawnEffect = Fx.none;
        }};

        collaris.constructor = ReplenishmentLegsEventUnit::create;
        weapon = collaris.weapons.get(0);
        collaris.targetAir = true;
        weapon.bullet.damage = 520;
        weapon.bullet.splashDamage = 85f;
        weapon.bullet.splashDamageRadius = 20f;
        weapon.bullet.bulletInterval = 20;
        weapon.bullet.intervalBullets = 3;
        weapon.bullet.intervalRandomSpread = 30;
        weapon.bullet.intervalAngle = 0;
        weapon.bullet.intervalBullet = new BasicBulletType() {{
            speed = 6;
            damage = 120;
            lifetime = 180;
            homingDelay = 30;
            homingRange = 1000;
            homingPower = 0.08F;
            trailChance = 1F;
            trailLength = 30;
            trailWidth = 2.2f;
            trailColor = Pal.techBlue;
        }};
        weapon.bullet.fragBullet.damage = 100;
        weapon.bullet.fragBullet.splashDamage = 92f;
        weapon.bullet.fragBullet.splashDamageRadius = 20;
        /*-----------------------------------------------------------------------------*/
        nova.speed = 2.4f;
        nova.armor = 8;
        nova.buildSpeed = 0;
        nova.abilities.add(new RepairOwnAbility(50, 60 * 8, 0));
        color = Color.valueOf("ffff90");
        weapon = nova.weapons.first();
        weapon.reload = 3;
        weapon.bullet = new DamageOwnBulletType() {{
            lifetime = 17.33f;
            speed = 9;
            damage = 16;
            width = 2;
            height = 7;
            healAmount = 0;
            healPercent = 0;
            lightOpacity = 0.6f;

            hittable = false;
            reflectable = true;
            collidesTeam = false;

            hitEffect = despawnEffect = smokeEffect = new Effect(8, e -> {
                color(Color.white, color, e.fin());
                stroke(0.5f + e.fout());
                Lines.circle(e.x, e.y, e.fin() * 5f);

                Drawf.light(e.x, e.y, 23f, color, e.fout() * 0.7f);
            });
            lightColor = frontColor = backColor = color;
        }};

        pulsar.speed = 1.1f;
        weapon = pulsar.weapons.get(0);
        weapon.inaccuracy = 12;
        weapon.bullet = new ConnectLightningBulletType() {{
            lightningColor = hitColor = Pal.heal;
            damage = 28;
            lightningLength = 9;
            lightningLengthRand = 10;
            shootEffect = Fx.shootHeal;
            healPercent = 1f;

            lightningType = new BulletType(0.0001f, 1f) {{
                lifetime = Fx.lightning.lifetime;
                hitEffect = Fx.hitLancer;
                despawnEffect = Fx.none;
                status = StatusEffects.shocked;
                statusDuration = 10f;
                hittable = absorbable = reflectable = false;
                healPercent = 1.6f;
                collidesTeam = true;
            }};
        }};

        ForceFieldAbility fAbility = (ForceFieldAbility) quasar.abilities.get(0);
        fAbility.regen = 1f;
        fAbility.max = 1800;
        weapon = quasar.weapons.get(0);
        weapon.reload = 35;
        weapon.bullet.damage = 85;
        weapon.bullet.status = StatusEffects.slow;
        weapon.bullet.statusDuration = 210;
        weapon.bullet.knockback = -3f;
        quasar.abilities.add(new StatusEnemyAbility(StatusEffects2.tardy, 180, 120, 135) {{
            applyEffect = new Effect(45, e -> {
                color(Pal.stoneGray.cpy().mul(Pal.heal));
                stroke(e.fout() * 2f);
                Lines.circle(e.x, e.y, 4f + e.finpow() * 135);
            });
        }});

        vela.weapons.get(0).bullet = new FlyContinuousLaserBulletType() {{
            damage = 45f;
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
        vela.rotateSpeed = 5.4F;

        weapon = corvus.weapons.get(0);
        weapon.bullet.fragOnHit = false;
        weapon.bullet.fragBullets = 16;
        weapon.bullet.fragRandomSpread = 75;
        weapon.bullet.fragLifeMax = 1.8f;
        weapon.bullet.fragLifeMin = 0.2f;
        weapon.bullet.fragVelocityMax = weapon.bullet.fragVelocityMin = 1f;
        weapon.bullet.fragBullet = new BulletType() {{
            collides = hittable = reflectable = false;
            lifetime = 20;
            speed = 23;
            damage = 1;

            fragOnAbsorb = false;
            despawnHit = true;
            fragBullets = 1;
            fragBullet = new BulletType() {{
                collides = hittable = reflectable = false;

                lifetime = 1;
                speed = 0;

                splashDamageRadius = 120;
                splashDamage = 110f;

                hitEffect = Fx.none;
                despawnEffect = Effects.lightningDown;
                hitColor = Pal.heal;
            }};

            hitEffect = despawnEffect = Fx.none;
        }};

        /*-----------------------------------------------------------------------------*/
    }
}
