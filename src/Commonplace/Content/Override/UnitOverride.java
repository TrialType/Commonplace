package Commonplace.Content.Override;

import Commonplace.Content.SpecialContent.Effects;
import Commonplace.Content.DefaultContent.StatusEffects2;
import Commonplace.Entities.Ability.*;
import Commonplace.Entities.BulletType.*;
import Commonplace.Entities.Unit.F.LongLifeUnitEntity;
import Commonplace.Entities.Unit.Override.*;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.Effect;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.entities.effect.WrapEffect;
import mindustry.entities.pattern.*;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.weapons.RepairBeamWeapon;

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

        UnitTypes.evoke.buildSpeed = 1.7f;
        UnitTypes.evoke.mineSpeed = 9f;
        UnitTypes.incite.buildSpeed = 2f;
        UnitTypes.incite.mineSpeed = 12f;
        UnitTypes.emanate.buildSpeed = 2.3f;
        UnitTypes.emanate.mineSpeed = 13.5f;

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
        weapon.bullet = new ProtectKillerBulletType() {{
            damage = 16;
            speed = 4;
            lifetime = 90;
            width = height = 8;

            minArmor = 7;
            damageArmorMultiplier = 1.25f;
            maxArmorDamageAdder = 20;
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
            shootStatus = StatusEffects.fast;
            shootStatusDuration = 350;
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
        weapon.shoot.shots = 5;
        weapon.inaccuracy = 10;
        weapon.bullet.createChance = 0.75f;
        weapon.bullet.damage = 40;
        weapon.bullet.splashDamage = 120;
        weapon.bullet.incendChance = 0.7f;
        weapon.bullet.incendAmount = 5;
        UnitTypes.fortress.weapons.add(new Weapon() {{
            reload = 18;
            rotate = false;
            shootCone = 30;
            inaccuracy = 12;

            shoot = new ShootAlternate();
            shoot.shots = 8;

            bullet = new LiquidBulletType(Liquids.oil) {{
                damage = 8;
                speed = 3;
                lifetime = 80;
                puddleSize = 10;

                statusDuration = 15;

                despawnHit = true;
                scaleLife = true;
                collidesAir = false;
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
        UnitTypes.crawler.abilities.add(new StatusOwnAbility(StatusEffects2.swift, 900, 900, 1));
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

        UnitTypes.atrax.speed = 0.3f;
        UnitTypes.atrax.health = 1200;
        UnitTypes.atrax.armor = 8f;
        UnitTypes.atrax.legSplashDamage = 70f;
        UnitTypes.atrax.legSplashRange = 16f;
        UnitTypes.atrax.targetAir = true;
        weapon = UnitTypes.atrax.weapons.first();
        weapon.shoot.firstShotDelay = 30;
        weapon.reload = 90;
        weapon.bullet.collidesAir = true;
        ((LiquidBulletType) weapon.bullet).orbSize = 6;
        ((LiquidBulletType) weapon.bullet).puddleSize = 25;
        weapon.bullet.lifetime = 100;
        weapon.bullet.speed = 4;
        weapon.bullet.drag = 0.04f;
        weapon.bullet.homingDelay = 10;
        weapon.bullet.homingPower = 0.1f;
        weapon.bullet.homingRange = 100;
        weapon.bullet.pierceBuilding = weapon.bullet.pierce = true;
        weapon.bullet.pierceCap = 4;
        weapon.bullet.intervalBullets = 2;
        weapon.bullet.intervalDelay = 5;
        weapon.bullet.intervalAngle = 90;
        weapon.bullet.intervalSpread = 180;
        weapon.bullet.bulletInterval = 3;
        weapon.bullet.intervalBullet = new LiquidBulletType(Liquids.slag) {{
            reflectable = absorbable = hittable = false;
            despawnHit = true;

            hitEffect = despawnEffect = Fx.none;

            damage = 6;
            lifetime = 1;
            speed = 2;

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

        UnitTypes.spiroct.health = 1500;
        UnitTypes.spiroct.armor = 21;
        weapon = UnitTypes.spiroct.weapons.get(0);
        weapon.bullet = new SapRadiusBulletType() {{
            sapStrength = 0.5f;
            length = 100f;
            damage = 33;
            shootEffect = Fx.shootSmall;
            hitColor = color = Color.valueOf("bf92f9");
            despawnEffect = Fx.none;
            width = 0.54f;
            lifetime = 35f;
            knockback = -1.24f;
        }};
        weapon = UnitTypes.spiroct.weapons.get(1);
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
        UnitTypes.spiroct.abilities.add(new SapAbility());

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
            radius = 0;
            ammoMultiplier = 2;
            hittable = absorbable = reflectable = false;
            collidesTiles = true;
            collides = true;
            pierce = true;
            pierceBuilding = true;
            buildingDamageMultiplier = 2f;
            fireTrailChance = 1;
            velMin = velMax = speed;
        }};

        UnitTypes.horizon.health = 500;
        UnitTypes.horizon.armor = 5;
        UnitTypes.horizon.speed = 2.15f;
        weapon = UnitTypes.horizon.weapons.get(0);
        weapon.shootStatus = StatusEffects2.frenzy;
        weapon.shootStatusDuration = 60;
        weapon.shoot.shots = 4;
        weapon.shoot.shotDelay = 4;
        weapon.bullet.buildingDamageMultiplier = 1.65f;

        UnitTypes.zenith.health = 1500;
        UnitTypes.zenith.armor = 8;
        UnitTypes.zenith.speed = 2;
        UnitTypes.zenith.range = 2000f / 7;
        weapon = UnitTypes.zenith.weapons.get(0);
        weapon.reload = 5;
        weapon.inaccuracy = 30;
        MissileBulletType m = (MissileBulletType) weapon.bullet;
        m.speed = 5;
        m.damage = 20;
        m.lifetime = 100;
        m.height = 14;
        m.width = 7;
        m.weaveMag = 2;
        m.weaveScale = 20;
        m.splashDamageRadius = 16;
        m.splashDamage = 25;
        m.homingDelay = 40;
        m.homingRange = 400;
        m.homingPower = 0.02f;
        m.rangeOverride = m.maxRange = 2000f / 7;

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
        weapon.bullet.status = StatusEffects.blasted;
        weapon.bullet.statusDuration = 60;

        UnitTypes.eclipse.health = 77000;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.mono.health = 2000;
        UnitTypes.mono.mineSpeed = 4f;

        UnitTypes.poly.health = 700;
        UnitTypes.poly.buildSpeed = 1;
        UnitTypes.poly.weapons.get(0).bullet.splashDamageRadius = 20;

        UnitTypes.mega.health = 10000;
        UnitTypes.mega.armor = 35;
        UnitTypes.poly.buildSpeed = 3.5f;
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
            splashDamageRadius = 800f;
        }};

        UnitTypes.oct.health = 77000;
        UnitTypes.oct.payloadCapacity = 6.5f * 6.5f * tilePayload;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.risso.health = 430;
        UnitTypes.risso.armor = 7;
        weapon = UnitTypes.risso.weapons.get(0);
        weapon.reload = 6;
        weapon.bullet.damage = 14f;
        weapon = UnitTypes.risso.weapons.get(1);
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

        UnitTypes.minke.health = 800;
        UnitTypes.minke.armor = 14;
        UnitTypes.minke.speed = 4;
        weapon = UnitTypes.minke.weapons.get(0);
        weapon.bullet.damage = 15f;
        weapon.bullet.splashDamage = 27;
        weapon.bullet.homingDelay = 0;
        weapon.bullet.homingRange = 252;
        weapon.bullet.homingPower = 0.1f;
        weapon = UnitTypes.minke.weapons.get(1);
        weapon.reload = 5;
        weapon.inaccuracy = 45;
        weapon.bullet.damage = 30;
        weapon.bullet.splashDamage = 60;

        UnitTypes.bryde.health = 1410;
        UnitTypes.bryde.armor = 20;
        ShieldRegenFieldAbility ability = (ShieldRegenFieldAbility) UnitTypes.bryde.abilities.first();
        ability.amount = 200;
        ability.max = 600;
        weapon = UnitTypes.bryde.weapons.get(0);
        weapon.reload = 150;
        weapon.velocityRnd = 0.5f;
        weapon.inaccuracy = 15;
        weapon.shoot = new ShootMulti(new ShootSpread(5, 7f) {{
            shotDelay = 8;
        }}, new ShootPattern() {{
            shots = 8;
        }});
        weapon.bullet.speed = 6;
        weapon.bullet.lifetime = 120;
        weapon.bullet.damage = 35;
        weapon.bullet.splashDamage = 50;
        weapon = UnitTypes.bryde.weapons.get(1);
        weapon.reload = 15;
        weapon.bullet.damage = 18;
        weapon.bullet.splashDamage = 15;
        weapon.bullet.lifetime = 105;
        weapon.bullet.collidesTiles = false;
        weapon.bullet.collidesGround = false;

        UnitTypes.sei.health = 22000;

        UnitTypes.omura.health = 77000;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.retusa.health = 550;
        UnitTypes.retusa.speed = 1;
        UnitTypes.retusa.armor = 8;
        UnitTypes.retusa.abilities.add(new StatusFieldAbility(StatusEffects.shielded, 300, 120, 60));
        RepairBeamWeapon repair = (RepairBeamWeapon) UnitTypes.retusa.weapons.get(0);
        repair.repairSpeed = 1.5f;
        weapon = UnitTypes.retusa.weapons.get(1);
        weapon.shoot.shots = 3;
        weapon.inaccuracy = 5;
        weapon.bullet.pierce = true;
        weapon.bullet.pierceBuilding = true;
        weapon.bullet.collideFloor = false;
        weapon.bullet.pierceCap = 3;
        weapon.bullet.lifetime = 120f;
        weapon.bullet.buildingDamageMultiplier = 3f;

        UnitTypes.oxynoe.health = 1060;
        UnitTypes.oxynoe.armor = 10;
        UnitTypes.oxynoe.speed = 1.66f;
        StatusFieldAbility statusFieldAbility = (StatusFieldAbility) UnitTypes.oxynoe.abilities.get(0);
        statusFieldAbility.range = 90;
        statusFieldAbility.duration = 420;
        weapon = UnitTypes.oxynoe.weapons.get(0);
        weapon.reload = 3;
        weapon.shootStatus = StatusEffects2.deploy;
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

        UnitTypes.cyerce.health = 1870;
        UnitTypes.cyerce.armor = 16;
        weapon = UnitTypes.cyerce.weapons.get(1);
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
            summonBullet = bullet;
        }};

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
        weapon = UnitTypes.obviate.weapons.first();
        weapon.bullet.splashDamageRadius = 12;
        weapon.bullet.status = StatusEffects.electrified;
        weapon.bullet.statusDuration = 90;
        weapon.bullet.fragBullets = 7;
        weapon.bullet.fragBullet = new MoveLightningBulletType() {{
            damage = 20;
            buildingDamageMultiplier = 0.1f;
            lifetime = 60;
            lightningLength = 3;
            damagePoints = 12;
            points = 40;
            lightningColor = Pal.sap;

            hitEffect = despawnEffect = Fx.none;
        }};

        UnitTypes.quell.health = 22000;
        UnitTypes.quell.weapons.get(0).bullet.spawnUnit.weapons.get(0).bullet.splashDamage = 220f;

        UnitTypes.disrupt.health = 42000;
        UnitTypes.disrupt.uiIcon = UnitTypes.disrupt.fullIcon = Core.atlas.find("disrupt");
        UnitTypes.disrupt.weapons.get(0).bullet.spawnUnit.weapons.get(0).bullet.splashDamage = 280f;
        /*-----------------------------------------------------------------------------*/
        UnitTypes.anthicus.health = 10150;
        UnitType u = UnitTypes.anthicus.weapons.get(0).bullet.spawnUnit;
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

        UnitTypes.tecta.health = 26550;
        ShieldArcAbility sab = (ShieldArcAbility) UnitTypes.tecta.abilities.first();
        sab.max = 3500;
        sab.regen = 1.2f;
        sab.cooldown = 6 * 60f;
        UnitTypes.tecta.abilities.add(new ShieldArcAbility() {{
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
        weapon = UnitTypes.tecta.weapons.first();
        weapon.shoot.shots = 15;
        weapon.shoot.shotDelay = 3;
        weapon.shootStatus = StatusEffects.slow;
        weapon.shootStatusDuration = 60;
        weapon.bullet.damage = 90;
        weapon.bullet.splashDamage = 85;

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
        UnitTypes.nova.abilities.add(new RepairOwnAbility(50, 60 * 8, 0));
        color = Color.valueOf("ffa998");
        weapon = UnitTypes.nova.weapons.get(0);
        weapon.reload = 4;
        weapon.bullet.lifetime = 55;
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
        weapon = UnitTypes.pulsar.weapons.get(0);
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

        UnitTypes.quasar.health = 2200;
        ForceFieldAbility fAbility = (ForceFieldAbility) UnitTypes.quasar.abilities.get(0);
        fAbility.regen = 1f;
        fAbility.max = 1800;
        weapon = UnitTypes.quasar.weapons.get(0);
        weapon.reload = 35;
        weapon.bullet.damage = 85;
        weapon.bullet.status = StatusEffects.slow;
        weapon.bullet.statusDuration = 210;
        weapon.bullet.knockback = -3f;
        UnitTypes.quasar.weapons.add(new Weapon() {{
            x = y = 0;
            mirror = false;
            reload = 120;
            bullet = new BulletType(0, 0) {{
                despawnEffect = hitEffect = Fx.none;
                collides = absorbable = reflectable = hittable = false;
                rangeOverride = 135;
                keepVelocity = false;
                status = StatusEffects2.tardy;
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
