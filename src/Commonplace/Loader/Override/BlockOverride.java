package Commonplace.Loader.Override;

import Commonplace.Entities.BulletType.*;
import Commonplace.Entities.Shoot.ContinuesShoot;
import Commonplace.Loader.DefaultContent.StatusEffects2;
import arc.graphics.Color;
import mindustry.content.*;
import arc.graphics.g2d.Fill;
import mindustry.entities.abilities.MoveLightningAbility;
import mindustry.entities.pattern.ShootSummon;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.ShootSpread;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.blocks.defense.ShieldWall;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.MassDriver;
import mindustry.world.blocks.heat.HeatProducer;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.units.UnitFactory;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

public class BlockOverride {
    public static void load() {
        Blocks.coreShard.health = 3900;
        Blocks.coreFoundation.health = 12500;
        Blocks.coreNucleus.health = 21000;
        Blocks.coreBastion.health = 16000;
        Blocks.coreCitadel.health = 56000;
        Blocks.coreAcropolis.health = 140000;

        Blocks.copperWall.health = 80 * 10;
        Blocks.copperWallLarge.health = 80 * 40;
        Blocks.titaniumWall.health = 110 * 10;
        Blocks.titaniumWallLarge.health = 110 * 40;
        Blocks.plastaniumWall.health = 125 * 10;
        Blocks.plastaniumWallLarge.health = 125 * 40;
        Blocks.thoriumWall.health = 200 * 10;
        Blocks.thoriumWallLarge.health = 200 * 40;
        Blocks.phaseWall.health = 150 * 10;
        ((Wall) Blocks.phaseWall).chanceDeflect = 12;
        Blocks.phaseWallLarge.health = 150 * 40;
        ((Wall) Blocks.phaseWall).chanceDeflect = 12;
        Blocks.surgeWall.health = 250 * 10;
        ((Wall) Blocks.surgeWall).lightningDamage = 30;
        ((Wall) Blocks.surgeWall).lightningChance = 0.07f;
        Blocks.surgeWallLarge.health = 250 * 40;
        ((Wall) Blocks.surgeWallLarge).lightningDamage = 30;
        ((Wall) Blocks.surgeWallLarge).lightningChance = 0.07f;

        Blocks.berylliumWall.health = 130 * 10;
        Blocks.berylliumWallLarge.health = 130 * 40;
        Blocks.tungstenWall.health = 180 * 10;
        Blocks.tungstenWallLarge.health = 180 * 40;
        Blocks.blastDoor.health = 175 * 40;
        Blocks.reinforcedSurgeWall.health = 250 * 10;
        ((Wall) Blocks.reinforcedSurgeWall).lightningDamage = 40;
        ((Wall) Blocks.reinforcedSurgeWall).lightningChance = 0.07f;
        Blocks.reinforcedSurgeWallLarge.health = 250 * 40;
        ((Wall) Blocks.reinforcedSurgeWallLarge).lightningDamage = 40;
        ((Wall) Blocks.reinforcedSurgeWallLarge).lightningChance = 0.07f;
        Blocks.carbideWall.health = 270 * 10;
        Blocks.carbideWallLarge.health = 270 * 40;
        Blocks.shieldedWall.health = 260 * 40;
        ((ShieldWall) Blocks.shieldedWall).chanceDeflect = 12;
        ((ShieldWall) Blocks.shieldedWall).shieldHealth = 2250;

        Blocks.shockMine.health = 150;
        ((ShockMine) Blocks.shockMine).damage = 30;
        ((ShockMine) Blocks.shockMine).shots = 1;
        ((ShockMine) Blocks.shockMine).length = 15;
        ((ShockMine) Blocks.shockMine).tendrils = 7;
        ((ShockMine) Blocks.shockMine).bullet = new StatusBulletType();

        ((MassDriver) Blocks.massDriver).range = 600f;

        ((ItemTurret) Blocks.duo).shoot.shots = 2;
        ((ItemTurret) Blocks.duo).range = 150;
        ((ItemTurret) Blocks.duo).reload = 10f;
        ((ItemTurret) Blocks.duo).inaccuracy = 5;
        ((ItemTurret) Blocks.duo).ammoTypes.clear();
        ((ItemTurret) Blocks.duo).ammo(
                Items.copper, new ProtectKillerBulletType() {{
                    speed = 2.5f;
                    damage = 9;
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;
                    ammoMultiplier = 4;
                }},
                Items.graphite, new ProtectKillerBulletType() {{
                    speed = 3.5f;
                    damage = 18;
                    width = 9f;
                    height = 12f;
                    reloadMultiplier = 0.6f;
                    ammoMultiplier = 8;
                    lifetime = 60f;
                }},
                Items.silicon, new ProtectKillerBulletType() {{
                    speed = 3f;
                    damage = 12;
                    width = 7f;
                    height = 9f;
                    homingPower = 0.1f;
                    reloadMultiplier = 1.5f;
                    ammoMultiplier = 10;
                    lifetime = 60f;
                }}
        );
        ((ItemTurret) Blocks.duo).limitRange(2);

        ((ItemTurret) Blocks.scatter).range = 280;
        ((ItemTurret) Blocks.scatter).ammoTypes.each((i, b) -> {
            b.speed = 6;
            b.pierce = true;
            b.pierceCap = 3;
        });
        ((ItemTurret) Blocks.scatter).limitRange(2);

        ((ItemTurret) Blocks.scorch).reload = 4;
        ((ItemTurret) Blocks.scorch).range = 80;
        ((ItemTurret) Blocks.scorch).ammoTypes.each((i, b) -> {
            b.damage *= 1.2f;
            b.buildingDamageMultiplier = 0.25f;
            b.shootEffect = new Effect(34f, 80f, e -> {
                color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());

                randLenVectors(e.id, 10, e.finpow() * 120f, e.rotation, 10f, (x, y) -> Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f));
            });
        });
        ((ItemTurret) Blocks.scorch).limitRange(2);

        ((ItemTurret) Blocks.hail).ammoTypes.each((i, b) -> {
            b.fragBullets = 3;
            b.fragBullet = new BasicBulletType() {{
                speed = 0.25f;
                lifetime = 60;
                width = height = 6;
                damage = b.damage * 0.5f;
                frontColor = backColor = i.color;
                splashDamage = b.splashDamage * 0.3f;
                splashDamageRadius = b.splashDamageRadius * 0.75f;
            }};
        });

        ((PowerTurret) Blocks.lancer).range = 205;
        ((PowerTurret) Blocks.lancer).shootType.damage = 160;
        ((PowerTurret) Blocks.lancer).shootType.statusDuration = 25;
        ((PowerTurret) Blocks.lancer).shootType.status = StatusEffects.slow;
        ((LaserBulletType) ((PowerTurret) Blocks.lancer).shootType).length = 200;

        ((PowerTurret) Blocks.arc).shootType.damage = 26;

        Blocks.parallax.scaledHealth = 300;
        ((TractorBeamTurret) Blocks.parallax).force = 24;
        ((TractorBeamTurret) Blocks.parallax).range = 300;
        ((TractorBeamTurret) Blocks.parallax).damage = 0.5f;
        ((TractorBeamTurret) Blocks.parallax).statusDuration = 5;
        ((TractorBeamTurret) Blocks.parallax).status = StatusEffects2.torn;

        Blocks.swarmer.itemCapacity = 100;
        ((ItemTurret) Blocks.swarmer).reload = 60;
        ((ItemTurret) Blocks.swarmer).maxAmmo = 100;
        ((ItemTurret) Blocks.swarmer).shoot = new ContinuesShoot(0.9f, 24).shots(4).shootDelay(2).barrels(
                -4, -1.25f, 0,
                0, 0, 0,
                4, -1.25f, 0
        );

        ((ItemTurret) Blocks.salvo).ammoTypes.each((i, b) -> b.damage = b.damage * 2f + 8f);

        ((PointDefenseTurret) Blocks.segment).retargetTime = 3;

        ((ItemTurret) Blocks.fuse).range = 160;
        ((ItemTurret) Blocks.fuse).shoot = new ShootSpread(7, 10);
        ((ItemTurret) Blocks.fuse).ammoTypes.each((i, b) -> {
            ((ShrapnelBulletType) b).length = 150;
            b.damage /= 1.2f;
        });

        ((ItemTurret) Blocks.ripple).range = 400;
        ((ItemTurret) Blocks.ripple).inaccuracy = 6;
        ((ItemTurret) Blocks.ripple).ammoTypes.each((i, b) -> b.buildingDamageMultiplier = 1.5f);
        ((ItemTurret) Blocks.ripple).limitRange(1);

        ((ItemTurret) Blocks.cyclone).ammoTypes.each((i, b) -> {
            b.damage *= 1.2f;
            b.splashDamage *= 1.2f;
            b.statusDuration *= 1.5f;
            if (b.lightning > 0) {
                b.lightningLength += 15;
            }
        });

        ((ItemTurret) Blocks.foreshadow).range = 800;
        ((ItemTurret) Blocks.foreshadow).ammoTypes.get((Items.surgeAlloy)).damage *= 1.25f;
        ((RailBulletType) ((ItemTurret) Blocks.foreshadow).ammoTypes.get((Items.surgeAlloy))).length = 800;

        ((ItemTurret) Blocks.spectre).size = 4;

        ((LaserTurret) Blocks.meltdown).size = 4;

        ((ItemTurret) Blocks.breach).ammoTypes.forEach(e -> {
            if (e.key == Items.beryllium) {
                e.value.fragBullets = 5;
                e.value.fragSpread = 72;
                e.value.fragRandomSpread = 20;
                e.value.fragBullet = new BasicBulletType(6f, 34) {{
                    width = 8f;
                    hitSize = 5f;
                    height = 14f;
                    pierceCap = 2;
                    lifetime = 8f;
                    ammoMultiplier = 1;
                    buildingDamageMultiplier = 0.3f;

                    pierce = true;
                    pierceBuilding = true;

                    trailLength = 10;
                    trailWidth = 2.1f;
                    smokeEffect = Fx.shootBigSmoke;
                    hitEffect = despawnEffect = Fx.hitBulletColor;

                    frontColor = Color.white;
                    hitColor = backColor = trailColor = Pal.berylShot;
                }};

                e.value.fragOnHit = true;
                e.value.setDefaults = false;
            } else if (e.key == Items.tungsten) {
                e.value.ammoMultiplier = 2;
                e.value.fragSpread = 90;
                e.value.fragBullets = 4;
                e.value.fragRandomSpread = 35;
                e.value.fragBullet = new BasicBulletType(7.5f, 47) {{
                    width = 9f;
                    hitSize = 5f;
                    height = 15f;
                    pierceCap = 3;
                    lifetime = 6f;
                    ammoMultiplier = 1;
                    buildingDamageMultiplier = 0.3f;

                    pierce = true;
                    pierceBuilding = true;

                    trailLength = 10;
                    trailWidth = 2.1f;
                    smokeEffect = Fx.shootBigSmoke;
                    hitEffect = despawnEffect = Fx.hitBulletColor;

                    frontColor = Color.white;
                    hitColor = backColor = trailColor = Pal.tungstenShot;
                }};

                e.value.fragOnHit = true;
                e.value.setDefaults = false;
            } else if (e.key == Items.carbide) {
                e.value.damage = 800;
                e.value.fragSpread = 10;
                e.value.fragBullets = 5;
                e.value.fragBullet.damage = 450;
                e.value.fragBullet.pierceArmor = true;
            }
        });

        ((ItemTurret) Blocks.diffuse).shoot = new ContinuesShoot(new ShootSpread(12, 5), 0.45f, 4).shootDelay(10);
        ((ItemTurret) Blocks.diffuse).maxAmmo = 40;
        ((ItemTurret) Blocks.diffuse).ammoPerShot = 2;
        ((ItemTurret) Blocks.diffuse).ammoTypes.forEach(e -> {
            e.value.knockback = 1.7f;
            e.value.ammoMultiplier = 10;
        });

        ((ContinuousLiquidTurret) Blocks.sublimate).ammoTypes.forEach(e -> {
            if (e.key == Liquids.ozone) {
                e.value.damage = 70;
                e.value.knockback = 2f;
                e.value.statusDuration = 60;
                e.value.status = StatusEffects2.gasify;
            } else if (e.key == Liquids.cyanogen) {
                e.value.damage = 145;
                e.value.knockback = 4f;
                e.value.statusDuration = 60;
                e.value.status = StatusEffects2.sublimation;
            }
        });

        ((ItemTurret) Blocks.titan).ammoTypes.forEach(e -> {
            if (e.key == Items.thorium) {
                e.value.statusDuration = 70;
            } else if (e.key == Items.oxide) {
                e.value.statusDuration = 80;
            } else if (e.key == Items.carbide) {
                e.value.statusDuration = 90;
            }
            e.value.status = StatusEffects2.oscillatoryDisturbance;
        });

        ((ItemTurret) Blocks.disperse).ammoTypes.clear();
        ((ItemTurret) Blocks.disperse).ammo(
                Items.tungsten, new PercentKillBulletType() {{
                    damage = 75;
                    speed = 8.5f;
                    shrinkY = 0.3f;
                    lifetime = 34f;
                    width = height = 16;
                    rotationOffset = 90f;
                    velocityScaleRandMax = 1.11f;

                    percent = f -> f * f * f - 0.5f;

                    sprite = "mine-bullet";
                    backSprite = "large-bomb-back";
                    collidesTiles = false;
                    collidesGround = false;
                    shootEffect = Fx.shootBig2;
                    smokeEffect = Fx.shootSmokeDisperse;
                    frontColor = Color.white;
                    backColor = trailColor = hitColor = Color.sky;
                    trailChance = 0.44f;
                    ammoMultiplier = 3f;

                    trailRotation = true;
                    trailEffect = Fx.disperseTrail;

                    hitEffect = despawnEffect = Fx.hitBulletColor;
                }},
                Items.thorium, new PercentKillBulletType() {{
                    damage = 100;
                    speed = 9.5f;
                    pierceCap = 3;
                    shrinkY = 0.3f;
                    lifetime = 34f;
                    width = height = 16;
                    rotationOffset = 90f;
                    reloadMultiplier = 0.85f;
                    velocityScaleRandMax = 1.5f;

                    percent = f -> f - 0.3f;

                    sprite = "mine-bullet";
                    backSprite = "large-bomb-back";
                    collidesTiles = false;
                    collidesGround = false;
                    shootEffect = Fx.shootBig2;
                    smokeEffect = Fx.shootSmokeDisperse;
                    frontColor = Color.white;
                    backColor = trailColor = hitColor = Color.valueOf("e89dbd");

                    trailChance = 0.44f;
                    ammoMultiplier = 2.5f;

                    trailRotation = true;
                    trailEffect = Fx.disperseTrail;

                    hitEffect = despawnEffect = Fx.hitBulletColor;
                }},
                Items.silicon, new PercentKillBulletType() {{
                    damage = 40;
                    homingPower = 0.045f;

                    speed = 9f;
                    shrinkY = 0.3f;
                    lifetime = 34f;
                    trailLength = 7;
                    width = height = 16;
                    ammoMultiplier = 3f;
                    rotationOffset = 90f;
                    extraRangeMargin = 32f;
                    reloadMultiplier = 0.9f;
                    velocityScaleRandMax = 1.11f;

                    percent = f -> f - 0.65f;

                    sprite = "mine-bullet";
                    backSprite = "large-bomb-back";

                    pierceCap = 2;
                    collidesTiles = false;
                    collidesGround = false;
                    shootEffect = Fx.shootBig2;
                    frontColor = Pal.graphiteAmmoFront;
                    smokeEffect = Fx.shootSmokeDisperse;
                    hitEffect = despawnEffect = Fx.hitBulletColor;
                    backColor = trailColor = hitColor = Pal.graphiteAmmoBack;
                }},
                Items.surgeAlloy, new PercentKillBulletType() {{
                    speed = 9f;
                    damage = 65;
                    lifetime = 34f;
                    shrinkY = 0.3f;
                    width = height = 16;
                    rangeChange = 8f * 3f;
                    reloadMultiplier = 0.75f;
                    velocityScaleRandMax = 1.11f;

                    percent = f -> f * f - 0.5f;

                    lightning = 3;
                    lightningLength = 4;
                    lightningDamage = 18f;
                    lightningLengthRand = 3;

                    trailChance = 0.44f;
                    ammoMultiplier = 5f;
                    rotationOffset = 90f;

                    trailRotation = true;
                    collidesTiles = false;
                    collidesGround = false;

                    sprite = "mine-bullet";
                    backSprite = "large-bomb-back";

                    shootEffect = Fx.shootBig2;
                    trailEffect = Fx.disperseTrail;
                    smokeEffect = Fx.shootSmokeDisperse;
                    hitEffect = despawnEffect = Fx.hitBulletColor;

                    frontColor = Color.white;
                    backColor = trailColor = hitColor = Pal.surge;

                    bulletInterval = 4f;
                    intervalBullet = new BulletType() {{
                        collidesGround = false;
                        collidesTiles = false;
                        lightningLengthRand = 4;
                        lightningLength = 2;
                        lightningCone = 30f;
                        lightningDamage = 25f;
                        lightning = 1;
                        hittable = collides = false;
                        instantDisappear = true;
                        hitEffect = despawnEffect = Fx.none;
                    }};
                }}
        );

        ((PowerTurret) Blocks.afflict).shootType.statusDuration = 150f;
        ((PowerTurret) Blocks.afflict).shootType.fragBullet.statusDuration = 120f;
        ((PowerTurret) Blocks.afflict).shootType.intervalBullet.statusDuration = 90f;
        ((PowerTurret) Blocks.afflict).shootType.status = StatusEffects2.electricalDisturbance;
        ((PowerTurret) Blocks.afflict).shootType.fragBullet.status = StatusEffects2.electricalDisturbance;
        ((PowerTurret) Blocks.afflict).shootType.intervalBullet.status = StatusEffects2.electricalDisturbance;

        ((ContinuousTurret) Blocks.lustre).shootType = new PointLaserBulletType2() {{
            damage = 150f;
            buildingDamageMultiplier = 0.043f;
            hitColor = Color.valueOf("fda981");
        }};

        ((ItemTurret) Blocks.scathe).ammoTypes.forEach(e -> {
            if (e.key == Items.carbide) {
                e.value.spawnUnit.weapons.add(new Weapon() {{
                    mirror = false;
                    shootOnDeath = true;
                    controllable = false;
                    aiControllable = false;

                    shootCone = 360;
                    x = y = shootX = shootY = 0;

                    bullet = new BuildingBoosterBulletType() {{
                        lifetime = 1;
                        enemyMul = 0.8f;
                        enemyDuration = 150;
                        splashDamageRadius = 200;

                        statusDuration = 150;
                        status = StatusEffects2.impact;
                    }};
                }});
            } else if (e.key == Items.surgeAlloy) {
                UnitType type = e.value.spawnUnit.weapons.first().bullet.fragBullet.spawnUnit;
                type.speed = 7.1f;
                type.rotateSpeed = 2.1f;
                type.lifetime = 40 * 3.5f;
                type.abilities.add(new MoveLightningAbility(0, 0, 0.85f, 0, 0, 6, Color.white) {{
                    bulletSpread = 180;
                    bullet = new LightningBulletType() {{
                        damage = 40;
                        lightningLength = 8;
                        lightningColor = Color.valueOf("f3e979");
                    }};
                }});
            } else if (e.key == Items.phaseFabric) {
                e.value.spawnUnit.weapons.add(new Weapon() {{
                    mirror = false;
                    shootOnDeath = true;
                    controllable = false;
                    aiControllable = false;

                    shootCone = 360;
                    x = y = shootX = shootY = 0;

                    bullet = new DespwanStatusBulletType() {{
                        lifetime = 1;
                    }};
                }});
            }
        });

        ((ItemTurret) Blocks.smite).ammoTypes.forEach(e -> {
            if (e.key == Items.surgeAlloy) {
                e.value.bulletInterval = 5;
                e.value.intervalBullet = e.value.copy();
                e.value.intervalBullet.damage = 80;
                e.value.intervalBullet.lifetime = 25;
                e.value.intervalBullet.homingRange = 300;
                e.value.intervalBullet.homingPower = 0.25f;
            }
        });

        ((PowerTurret) Blocks.malign).shoot = new ShootSummon(0f, 0f, 11, 45f);
        ((PowerTurret) Blocks.malign).shootType.fragBullets = 2;
        ((PowerTurret) Blocks.malign).shootType.fragRandomSpread = 12;
        ((PowerTurret) Blocks.malign).shootType.fragBullet.lifetime = 35;
        ((PowerTurret) Blocks.malign).shootType.fragBullet.knockback = 4f;
        ((PowerTurret) Blocks.malign).shootType.fragBullet.lightningLength = 8;
        ((PowerTurret) Blocks.malign).shootType.fragBullet.lightningDamage = 60;
        ((PowerTurret) Blocks.malign).shootType.fragBullet.lightningColor = Color.valueOf("d370d3");
        ((LaserBulletType) ((PowerTurret) Blocks.malign).shootType.fragBullet).length = 220;
        ((LaserBulletType) ((PowerTurret) Blocks.malign).shootType.fragBullet).lightningDelay = 0.8f;
        ((LaserBulletType) ((PowerTurret) Blocks.malign).shootType.fragBullet).lightningSpacing = 60;

        Blocks.plastaniumConveyor.itemCapacity = 15;

        ((Drill) Blocks.mechanicalDrill).drillTime = 480;
        ((Drill) Blocks.pneumaticDrill).drillTime = 320;
        ((Drill) Blocks.laserDrill).drillTime = 224;
        ((Drill) Blocks.blastDrill).drillTime = 224;

        ((UnitFactory) Blocks.airFactory).plans.find(p -> p.unit == UnitTypes.mono).
                requirements = ItemStack.with(Items.copper, 40, Items.lead, 40);

        ((GenericCrafter) Blocks.siliconSmelter).craftTime = 32;

        ((AttributeCrafter) Blocks.siliconCrucible).craftTime = 72;

        ((GenericCrafter) Blocks.graphitePress).craftTime = 72;

        ((GenericCrafter) Blocks.multiPress).craftTime = 24;

        ((GenericCrafter) Blocks.plastaniumCompressor).craftTime = 48;

        ((GenericCrafter) Blocks.phaseWeaver).craftTime = 96;

        ((GenericCrafter) Blocks.kiln).craftTime = 24;

        ((GenericCrafter) Blocks.surgeSmelter).craftTime = 60;

        ((GenericCrafter) Blocks.cryofluidMixer).craftTime = 96;

        ((GenericCrafter) Blocks.pyratiteMixer).craftTime = 64;

        ((GenericCrafter) Blocks.blastMixer).craftTime = 64;

        ((GenericCrafter) Blocks.melter).craftTime = 8;

        ((Separator) Blocks.separator).craftTime = 28;

        ((Separator) Blocks.disassembler).craftTime = 12;

        ((GenericCrafter) Blocks.sporePress).craftTime = 16;

        ((GenericCrafter) Blocks.pulverizer).craftTime = 32;

        ((GenericCrafter) Blocks.coalCentrifuge).craftTime = 24;

        ((GenericCrafter) Blocks.siliconArcFurnace).craftTime = 40;

        ((GenericCrafter) Blocks.electrolyzer).craftTime = 8;

        ((HeatCrafter) Blocks.atmosphericConcentrator).craftTime = 64;

        ((HeatProducer) Blocks.oxidationChamber).craftTime = 96;

        ((HeatProducer) Blocks.electricHeater).heatOutput = 4;

        ((HeatProducer) Blocks.slagHeater).heatOutput = 10;

        ((HeatCrafter) Blocks.carbideCrucible).craftTime = 108;

        ((HeatCrafter) Blocks.surgeCrucible).craftTime = 144;

        ((HeatCrafter) Blocks.cyanogenSynthesizer).craftTime = 64;

        ((HeatCrafter) Blocks.phaseSynthesizer).craftTime = 96;
    }
}