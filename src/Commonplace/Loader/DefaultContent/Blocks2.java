package Commonplace.Loader.DefaultContent;

import Commonplace.Loader.Special.Effects;
import Commonplace.Entities.Block.*;
import Commonplace.Entities.BulletType.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.util.Time;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.WaveEffect;
import mindustry.entities.part.ShapePart;
import mindustry.entities.pattern.*;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.production.Fracker;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumePowerCondition;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawFlame;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.world;
import static mindustry.content.Items.*;
import static mindustry.type.ItemStack.with;

public class Blocks2 {
    //test
    public static Block pu;
    //defense
    public static Block eleFence, eleFenceLarge, decoy, decoyLarge, polymerizationWall, polymerizationWallLarge,
            weakPowerWall, weakPowerWallLarge, superPowerWall, superPowerWallLarge;
    //turret
    public static Block fourNet, fireBoost, wind, plain, hill, butte, scattering, life, steadyRain, wonton, scale, stack,
            flowers;
    //power
    public static Block sporeCombustionGenerator;
    //crafting
    public static Block primarySolidification, intermediateSolidification, advancedSolidification, ultimateSolidification,
            phaseAmplifier, hotSiliconSmelter, pulverizerSupper;
    //effect
    public static Block buildCore, slowProject, unitUpper, reflective, coreLaunch, coreLaunchLarge, mendProjectorLarge,
            forceProjectorLarge, bulletSlower, shockMineFiller, automaticDistributor, automaticDistributorLarge;
    //distribution
    public static Block multiMassDriver, sorterOverflowGate, sorterUnderflowGate;
    //liquid
    public static Block oilExtractorLarge;

    public static void load() {
        primarySolidification = new StackCrafter("primary-solidification") {{
            health = 350;
            itemCapacity = 60;
            liquidCapacity = 120;
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionCopper, 14),
                    ItemStack.with(Items.copper, 5),
                    LiquidStack.empty, 120
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionLead, 14),
                    ItemStack.with(Items.lead, 5),
                    LiquidStack.empty, 120
            ));

            hasPower = false;

            requirements(Category.crafting, ItemStack.with(Items.metaglass, 35, Items.copper, 40, Items.lead, 25));
        }};
        intermediateSolidification = new StackCrafter("intermediate-solidification") {{
            itemCapacity = 150;
            liquidCapacity = 300;
            size = 2;
            health = 550;

            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionCopper, 30),
                    ItemStack.with(Items.copper, 12),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionLead, 30),
                    ItemStack.with(Items.lead, 12),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionTitanium, 30),
                    ItemStack.with(Items.titanium, 10),
                    LiquidStack.empty, 90
            ));

            consume(new ConsumePower(5, 0, false));

            requirements(Category.crafting, ItemStack.with(Items.metaglass, 125, Items.copper, 150,
                    Items.lead, 100, Items.graphite, 140));
        }};
        advancedSolidification = new StackCrafter("advanced-solidification") {{
            itemCapacity = 360;
            liquidCapacity = 600;
            size = 3;
            health = 1050;

            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionCopper, 60),
                    ItemStack.with(Items.copper, 30),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionLead, 60),
                    ItemStack.with(Items.lead, 30),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionTitanium, 60),
                    ItemStack.with(Items.titanium, 24),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionThorium, 60),
                    ItemStack.with(Items.thorium, 20),
                    LiquidStack.empty, 90
            ));

            consume(new ConsumePower(20, 0, false));

            requirements(Category.crafting, ItemStack.with(Items.metaglass, 500, Items.copper, 450,
                    Items.lead, 400, Items.graphite, 350, Items.titanium, 300));
        }};
        ultimateSolidification = new StackCrafter("ultimate-solidification") {{
            itemCapacity = 360;
            liquidCapacity = 900;
            size = 4;
            health = 1550;

            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionCopper, 90),
                    ItemStack.with(Items.copper, 48),
                    LiquidStack.empty, 60
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionLead, 90),
                    ItemStack.with(Items.lead, 48),
                    LiquidStack.empty, 60
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionTitanium, 90),
                    ItemStack.with(Items.titanium, 48),
                    LiquidStack.empty, 60
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(Liquids2.fusionThorium, 90),
                    ItemStack.with(Items.thorium, 48),
                    LiquidStack.empty, 60
            ));

            consume(new ConsumePower(100, 0, false));

            requirements(Category.crafting, ItemStack.with(Items.metaglass, 1500, Items.copper, 1450,
                    Items.lead, 1400, Items.graphite, 1350, Items.titanium, 1400, Items.thorium, 1450, Items.surgeAlloy, 500));
        }};
        phaseAmplifier = new GenericCrafter("phase-amplifier") {{
            requirements(Category.crafting, ItemStack.with(phaseFabric, 25, thorium, 80, copper, 100));
            outputItem = new ItemStack(phaseFabric, 4);
            consumeItems(ItemStack.with(thorium, 4, graphite, 1));
            consumePower(15);
            envEnabled |= Env.space;
            craftEffect = Fx.smeltsmoke;
            researchCostMultiplier = 10;
            hasPower = true;
            craftTime = 120;
            size = 3;
            itemCapacity = 20;
        }};
        hotSiliconSmelter = new GenericCrafter("hot-silicon-smelter") {{
            requirements(Category.crafting, with(Items.copper, 35, Items.lead, 30, graphite, 30));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(Items.silicon, 5);
            craftTime = 60f;
            size = 2;
            hasPower = true;
            hasLiquids = true;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;

            consumeLiquid(Liquids.slag, 0.10f);
            consumeItems(with(Items.coal, 2, Items.sand, 2));
            consumePower(0.50f);
        }};
        pulverizerSupper = new StackCrafter("pulverizer-supper") {{
            health = 350;
            itemCapacity = 60;
            liquidCapacity = 120;
            switchStack.add(new ProductStack(
                    ItemStack.with(copper, 1),
                    LiquidStack.empty,
                    ItemStack.with(scrap, 1),
                    LiquidStack.empty, 15
            ), new ProductStack(
                    ItemStack.with(lead, 1),
                    LiquidStack.empty,
                    ItemStack.with(scrap, 1),
                    LiquidStack.empty, 15
            ), new ProductStack(
                    ItemStack.with(titanium, 1),
                    LiquidStack.empty,
                    ItemStack.with(scrap, 3),
                    LiquidStack.empty, 30
            ), new ProductStack(
                    ItemStack.with(thorium, 1),
                    LiquidStack.empty,
                    ItemStack.with(scrap, 6),
                    LiquidStack.empty, 45
            ));
            consumePower(0.5f);
            requirements(Category.crafting, ItemStack.with(Items.copper, 30, Items.lead, 25));
        }};
//======================================================================================================================
        sporeCombustionGenerator = new ConsumeGenerator("spore-combustion-generator") {{
            requirements(Category.power, with(Items.copper, 35, Items.graphite, 25, Items.lead, 40, Items.silicon, 30));
            size = 2;

            powerProduction = 9f;
            itemDuration = 100f;

            outputLiquid = new LiquidStack(Liquids.water, 0.15f);

            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.03f;
            generateEffect = Fx.generatespark;

            consumeItem(sporePod);
        }};
//======================================================================================================================
        oilExtractorLarge = new Fracker("oil-extractor-large") {{
            requirements(Category.production, with(titanium, 150, Items.graphite, 125, Items.thorium, 135, Items.silicon, 100, surgeAlloy, 35));
            health = 1500;
            size = 4;

            result = Liquids.oil;
            updateEffect = Fx.pulverize;
            updateEffectChance = 0.05f;
            pumpAmount = 0.6f;
            liquidCapacity = 30f;
            attribute = Attribute.oil;
            baseEfficiency = 0.5f;
            itemUseTime = 60f;

            consumeItem(Items.sand, 2);
            consumePower(5);
            consumeLiquid(Liquids.water, 0.6f);
        }};
//======================================================================================================================
        //debug
        pu = new PureProject("pu") {{
            health = 650;

            consumePower(50f);

            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with(Items.copper, 1));
        }};
//======================================================================================================================
        butte = new PowerTurret("butte") {{
            requirements(Category.turret, ItemStack.with(phaseFabric, 75, thorium, 350, plastanium, 200, surgeAlloy, 100));
            consume(new ConsumePower(12, 0, false));

            health = 3000;
            size = 3;

            rotateSpeed = 1;
            recoil = 6f;
            range = 500;
            reload = 30;
            consumesPower = true;
            hasPower = true;
            consumeAmmoOnce = false;
            canOverdrive = false;

            shoot = new ShootAlternate() {{
                shots = 30;
                shotDelay = 1;
                barrels = 6;
                spread = 3;
            }};
            shootType = new BasicBulletType() {{
                sprite = "circle";

                inaccuracy = 15;

                weaveScale = 5.4f;
                weaveMag = 5;
                weaveRandom = true;

                width = 3;
                height = 3;
                shrinkX = shrinkY = 0;
                frontColor = backColor = hitColor = Pal.heal;
                trailLength = 0;
                trailWidth = 0;

                damage = 1;
                lifetime = 50;
                speed = 10;
                buildingDamageMultiplier = 0.1f;
                status = StatusEffects2.erosion;
                statusDuration = 240;

                splashDamage = 25;
                splashDamageRadius = 16;
                hitEffect = despawnEffect = Fx.hitBulletColor;

                fragBullets = 6;
                fragRandomSpread = 360;
                fragBullet = new BasicBulletType() {{
                    width = height = 3;
                    damage = 7;
                    speed = 3;
                    lifetime = 10;

                    trailLength = 3;
                    trailWidth = 2;
                    frontColor = trailColor = Pal.heal;

                    status = StatusEffects.muddy;
                    statusDuration = 240;

                    reflectable = false;

                    despawnEffect = hitEffect = Fx.none;
                }};
            }};
        }};
        hill = new PowerTurret("hill") {{
            consume(new ConsumePower(6, 0, false));

            health = 2000;
            size = 3;

            recoil = 6f;
            range = 350;
            reload = 65;
            consumesPower = true;
            hasPower = true;
            consumeAmmoOnce = false;
            canOverdrive = false;

            shoot.shots = 6;
            inaccuracy = 20;
            shootType = new BasicBulletType() {{
                absorbable = false;

                sprite = "circle";
                hitSize = 25;
                width = height = 30;
                shrinkX = shrinkY = 0;
                frontColor = backColor = Pal.heal;

                damage = 15;
                lifetime = 200;
                speed = 4;
                drag = 0.011f;
                knockback = 1;
                buildingDamageMultiplier = 0.1f;
                status = StatusEffects2.torn;
                statusDuration = 240;

                splashDamage = 10;
                splashDamageRadius = 60;
                hitEffect = despawnEffect = new Effect(90, e -> {
                    color(Pal.heal);
                    stroke(e.fout() * 7);
                    Lines.circle(e.x, e.y, 4f + e.finpow() * 60);
                });
            }};

            requirements(Category.turret, ItemStack.with(
                    plastanium, 120,
                    Items.titanium, 500,
                    Items.graphite, 500
            ));
        }};
        plain = new PowerTurret("plain") {{
            requirements(Category.turret, ItemStack.with(Items.titanium, 100,
                    silicon, 70, Items.graphite, 100
            ));
            consume(new ConsumePower(2, 480, false));

            size = 2;
            recoil = 3;
            range = 300;
            health = 1000;
            rotateSpeed = 10f;
            researchCostMultiplier = 100;
            canOverdrive = false;

            shootY = 3f;
            reload = 30;
            shootCone = 15f;

            shootType = new BasicBulletType() {{
                pierce = true;
                pierceCap = 8;

                width = height = 12;
                damage = 15;
                speed = 5;
                lifetime = 70;

                trailLength = 7;
                trailWidth = 3;
                frontColor = trailColor = Pal.heal;

                status = StatusEffects2.torn;
                statusDuration = 240;

                rangeOverride = 300;
                reflectable = false;
                collidesTiles = false;
            }};
        }};
        life = new ItemTurret("life") {{
            requirements(Category.turret, ItemStack.with(titanium, 100, thorium, 60, plastanium, 35, silicon, 80));

            health = 1000;
            size = 3;
            reload = 90;
            range = 500;
            recoil = 6;

            ammo(
                    titanium,
                    new PointBulletType2() {{
                        lifetime = 42f;
                        speed = 12;
                        damage = 40;

                        fragVelocityMin = fragVelocityMax = fragLifeMin = fragLifeMax = 1;
                        fragBullets = 10;
                        fragSpread = 36;
                        fragRandomSpread = 0;
                        buildingDamageMultiplier = 0.1f;
                        fragBullet = new BasicBulletType() {{
                            pierce = true;
                            collidesTiles = false;

                            sprite = "circle";
                            width = height = 20;
                            shrinkX = shrinkY = 0;
                            frontColor = backColor = Color.valueOf("8da1e3");
                            lifetime = 20;
                            speed = 14;
                            damage = 30;
                            bulletInterval = 4;
                            intervalAngle = 180;
                            intervalRandomSpread = 0;
                            intervalDelay = 4;
                            intervalBullet = new BasicBulletType() {{
                                collidesTiles = false;
                                pierce = true;
                                pierceCap = 2;
                                sprite = "circle";
                                width = height = 10;
                                shrinkX = shrinkY = 0;
                                frontColor = backColor = Color.valueOf("8da1e3");
                                damage = 25;
                                speed = 14;
                                lifetime = 20;
                            }};
                        }};
                    }}, thorium,
                    new PointBulletType2() {{
                        lifetime = 42f;
                        speed = 12;
                        damage = 35;

                        fragVelocityMin = fragVelocityMax = fragLifeMin = fragLifeMax = 1;
                        fragBullets = 10;
                        fragSpread = 36;
                        fragRandomSpread = 0;
                        buildingDamageMultiplier = 0.1f;
                        fragBullet = new BasicBulletType() {{
                            pierce = true;
                            pierceArmor = true;
                            collidesTiles = false;

                            sprite = "circle";
                            width = height = 20;
                            shrinkX = shrinkY = 0;
                            frontColor = backColor = Color.valueOf("f9a3c7");
                            lifetime = 20;
                            speed = 12;
                            damage = 25;

                            bulletInterval = 4;
                            intervalAngle = 180;
                            intervalRandomSpread = 0;
                            intervalDelay = 4;
                            intervalBullet = new BasicBulletType() {{
                                collidesTiles = false;
                                pierceArmor = true;
                                pierce = true;
                                pierceCap = 2;

                                sprite = "circle";
                                width = height = 10;
                                shrinkX = shrinkY = 0;
                                frontColor = backColor = Color.valueOf("f9a3c7");
                                damage = 20;
                                speed = 12;
                                lifetime = 20;
                            }};
                        }};
                    }}, surgeAlloy,
                    new PointBulletType2() {{
                        lifetime = 42f;
                        speed = 12;
                        damage = 40;

                        fragVelocityMin = fragVelocityMax = fragLifeMin = fragLifeMax = 1;
                        fragBullets = 10;
                        fragSpread = 36;
                        fragRandomSpread = 0;
                        buildingDamageMultiplier = 0.1f;
                        fragBullet = new BasicBulletType() {{
                            pierce = true;
                            collidesTiles = false;

                            sprite = "circle";
                            width = height = 20;
                            shrinkX = shrinkY = 0;
                            frontColor = backColor = Color.valueOf("f3e979");
                            lifetime = 30;
                            speed = 10;
                            damage = 30;

                            lightning = 3;
                            lightningDamage = 0;
                            lightningLength = 3;

                            bulletInterval = 6;
                            intervalBullets = 3;
                            intervalAngle = 180;
                            intervalRandomSpread = 0;
                            intervalDelay = 6;
                            intervalBullet = new LightningBulletType() {{
                                lightningLength = 40;
                                lightningLengthRand = 20;
                                lightningColor = hitColor = Color.valueOf("f3e979");
                                damage = 15;
                                speed = 0;
                            }};
                        }};
                    }}, plastanium,
                    new PointBulletType2() {{
                        lifetime = 42f;
                        speed = 12;
                        damage = 30;
                        splashDamage = 25;
                        splashDamageRadius = 12;

                        fragVelocityMin = fragVelocityMax = fragLifeMin = fragLifeMax = 1;
                        fragBullets = 10;
                        fragSpread = 36;
                        fragRandomSpread = 0;
                        buildingDamageMultiplier = 0.1f;
                        fragBullet = new BasicBulletType() {{
                            pierce = true;
                            collidesTiles = false;

                            sprite = "circle";
                            width = height = 20;
                            shrinkX = shrinkY = 0;
                            frontColor = backColor = Color.valueOf("cbd97f");
                            lifetime = 20;
                            speed = 10;
                            damage = 20;

                            splashDamage = 25;
                            splashDamageRadius = 16;

                            bulletInterval = 4;
                            intervalAngle = 180;
                            intervalRandomSpread = 0;
                            intervalDelay = 4;
                            intervalBullet = new BasicBulletType() {{
                                collidesTiles = false;
                                pierce = true;
                                pierceCap = 2;

                                sprite = "circle";
                                width = height = 10;
                                shrinkX = shrinkY = 0;
                                frontColor = backColor = Color.valueOf("cbd97f");

                                damage = 15;
                                speed = 10;
                                lifetime = 20;
                                splashDamage = 25;
                                splashDamageRadius = 8;
                            }};
                        }};
                    }}
            );
        }};
        scattering = new ItemTurret("scattering") {{
            requirements(Category.turret, with(Items.copper, 100, Items.graphite, 80, Items.titanium, 25, Items.silicon, 25));
            ammo(
                    Items.titanium, new BasicBulletType(4, 22.5f) {{
                        width = 7f;
                        height = 9f;
                        lifetime = 30;
                        ammoMultiplier = 4;
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootBigSmoke;
                        frontColor = backColor = Color.valueOf("8da1e3");
                        pierce = true;
                        pierceCap = 3;

                        fragOnHit = false;
                        fragOnAbsorb = false;
                        fragBullets = 3;
                        fragSpread = 15;
                        fragVelocityMin = fragVelocityMax = 1;
                        fragRandomSpread = 0;
                        fragBullet = new BasicBulletType(4, 22.5f) {{
                            width = 7f;
                            height = 9f;
                            lifetime = 30;
                            ammoMultiplier = 4;
                            shootEffect = Fx.shootBig;
                            smokeEffect = Fx.shootBigSmoke;
                            frontColor = backColor = Color.valueOf("8da1e3");
                            pierce = true;
                            pierceCap = 3;
                        }};
                    }},
                    Items.thorium, new BasicBulletType(4, 27) {{
                        width = 9f;
                        height = 12f;
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootBigSmoke;
                        frontColor = backColor = Color.valueOf("f9a3c7");
                        reloadMultiplier = 0.9f;
                        ammoMultiplier = 4;
                        lifetime = 30;
                        pierce = true;
                        pierceCap = 2;

                        fragOnHit = false;
                        fragOnAbsorb = false;
                        fragBullets = 3;
                        fragSpread = 20;
                        fragVelocityMin = fragVelocityMax = 1;
                        fragRandomSpread = 0;
                        fragBullet = new BasicBulletType(4, 40.5f) {{
                            width = 9f;
                            height = 12f;
                            shootEffect = Fx.shootBig;
                            smokeEffect = Fx.shootBigSmoke;
                            frontColor = backColor = Color.valueOf("f9a3c7");
                            reloadMultiplier = 0.9f;
                            ammoMultiplier = 4;
                            lifetime = 30;
                            pierce = true;
                            pierceCap = 2;
                        }};
                    }}
            );

            shoot = new ShootBarrel() {{
                shots = 3;
                shotDelay = 5;
            }};

            recoils = 2;
            recoil = 0.5f;
            shootY = 3f;
            reload = 20;
            size = 2;
            range = 240;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 800;
            inaccuracy = 8;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            researchCostMultiplier = 8f;
        }};
        wonton = new ItemTurret("wonton") {{
            requirements(Category.turret, ItemStack.with(titanium, 30, plastanium, 30, silicon, 30));

            size = 2;
            health = 700;
            recoil = 3;
            reload = 90;
            range = 400;
            targetGround = false;

            shoot.shots = 8;
            shoot.shotDelay = 4;
            inaccuracy = 15;

            ammo(
                    titanium,
                    new FlakBulletType() {{
                        flakDelay = 0;
                        explodeRange = 15;
                        explodeDelay = 0;

                        lifetime = 150;
                        speed = 7;
                        drag = 0.02f;
                        damage = 20;
                        splashDamage = 40;
                        splashDamageRadius = 20;

                        ammoMultiplier = 4;

                        status = StatusEffects2.torn;
                        statusDuration = 120;
                    }}, metaglass,
                    new FlakBulletType() {{
                        flakDelay = 0;
                        explodeRange = 15;
                        explodeDelay = 0;

                        lifetime = 75;
                        speed = 7;
                        drag = 0.02f;
                        damage = 20;
                        splashDamage = 40;
                        splashDamageRadius = 20;

                        ammoMultiplier = 4;

                        pierce = true;
                        pierceCap = 3;

                        fragBullets = 5;
                        fragBullet = new FlakBulletType() {{
                            flakDelay = 0;
                            explodeRange = 15;
                            explodeDelay = 0;

                            lifetime = 125;
                            speed = 5;
                            drag = 0.05f;
                            damage = 12;
                            splashDamage = 20;
                            splashDamageRadius = 20;
                        }};
                    }}, plastanium,
                    new FlakBulletType() {{
                        flakDelay = 0;
                        explodeRange = 15;
                        explodeDelay = 0;

                        ammoMultiplier = 4;
                        pierceArmor = true;

                        lifetime = 150;
                        speed = 7;
                        drag = 0.02f;
                        damage = 40;
                        splashDamage = 60;
                        splashDamageRadius = 20;

                        status = StatusEffects2.tardy;
                        statusDuration = 120;
                    }}
            );
        }};
        steadyRain = new ItemTurret("steady-rain") {{
            requirements(Category.turret, ItemStack.with(titanium, 160, plastanium, 60, silicon, 100));

            size = 3;
            health = 1400;
            recoil = 4;
            reload = 90;
            range = 300;
            targetAir = false;

            shoot.shots = 4;
            shoot.shotDelay = 6;
            inaccuracy = 9;
            ammo(
                    graphite,
                    new ArtilleryBulletType() {{
                        width = height = 10;
                        frontColor = backColor = hitColor = Color.valueOf("b2c6d2");
                        speed = 3;
                        damage = 0;
                        splashDamage = 65;
                        splashDamageRadius = 20;
                        status = StatusEffects.slow;
                        statusDuration = 60;
                    }}, thorium,
                    new ArtilleryBulletType() {{
                        width = height = 10;
                        frontColor = backColor = hitColor = Color.valueOf("f9a3c7");
                        speed = 5;
                        damage = 0;
                        splashDamage = 75;
                        splashDamageRadius = 20;

                        fragBullets = 5;
                        fragBullet = new ArtilleryBulletType() {{
                            width = height = 6;
                            frontColor = backColor = hitColor = Color.valueOf("f9a3c7");

                            damage = 0;
                            speed = 2;
                            lifetime = 45;
                            splashDamage = 35;
                            splashDamageRadius = 8;
                        }};
                    }}, plastanium,
                    new ArtilleryBulletType() {{
                        width = height = 10;
                        frontColor = backColor = hitColor = Color.valueOf("cbd97f");
                        speed = 3;
                        damage = 0;
                        splashDamage = 60;
                        splashDamageRadius = 12;
                        buildingDamageMultiplier = 3;
                    }}, phaseFabric,
                    new ArtilleryBulletType() {{
                        width = height = 10;
                        frontColor = backColor = hitColor = Color.valueOf("f4ba6e");

                        speed = 4;
                        damage = 0;
                        splashDamage = 115;
                        splashDamageRadius = 20;
                        status = StatusEffects.sapped;
                        statusDuration = 60;
                        knockback = 3;

                        fragBullets = 7;
                        fragBullet = new ArtilleryBulletType() {{
                            width = height = 6;
                            frontColor = backColor = hitColor = Color.valueOf("f4ba6e");

                            speed = 2;
                            lifetime = 60;
                            damage = 0;
                            splashDamage = 80;
                            splashDamageRadius = 20;
                            knockback = 2;
                        }};
                    }}
            );
            limitRange();
        }};
        wind = new ItemTurret("wind") {{
            requirements(Category.turret, ItemStack.with(
                    Items.titanium, 2000,
                    Items.copper, 2600,
                    Items.graphite, 1800,
                    Items.silicon, 1650,
                    Items.surgeAlloy, 300
            ));

            consume(new ConsumePower(200, 20000, false));
            coolant = consume(new ConsumeCoolant(1.3f));
            coolantMultiplier = 1.01f;

            hasItems = true;
            itemCapacity = 90;
            maxAmmo = 90;
            ammoPerShot = 10;
            consumeAmmoOnce = false;
            shootX = shootY = 0;

            range = 500;
            reload = 1200;
            size = 4;
            clipSize = 5;
            health = 8000;

            ammoTypes.put(Items.blastCompound, new BasicBulletType() {{
                ammoMultiplier = 1f;

                trailEffect = Fx.none;

                damage = 0;
                lifetime = 240;
                speed = 2.4f;
                width = height = 0;
                shrinkX = shrinkY = 0;
                backColor = frontColor = Pal.darkPyraFlame;

                trailLength = 127;
                trailWidth = 1.5f;
                trailChance = 1f;
                trailRotation = true;
                trailEffect = new Effect(90, e -> {
                    float len = (float) Math.abs(Math.cos(Math.toRadians((e.time + Time.time) * 5))) * 4,
                            x = e.x, y = e.y, rotation = e.rotation;
                    Draw.color(Pal.darkPyraFlame);
                    Fill.circle((float) (x + len * Math.cos(Math.toRadians(rotation + 90))),
                            (float) (y + len * Math.sin(Math.toRadians(rotation + 90))), (1 - e.fin()) * 2);
                    Fill.circle((float) (x + len * Math.cos(Math.toRadians(rotation - 90))),
                            (float) (y + len * Math.sin(Math.toRadians(rotation - 90))), (1 - e.fin()) * 2);
                });

                parts.addAll(new ShapePart() {{
                    rotateSpeed = 3;
                    sides = 3;
                    lifetime = 400;
                    radius = 11;
                    radiusTo = 11;
                    colorTo = color = Pal.darkPyraFlame;
                }}, new ShapePart() {{
                    rotateSpeed = 3;
                    sides = 3;
                    lifetime = 400;
                    rotation = 180;
                    radius = 11;
                    radiusTo = 11;
                    colorTo = color = Pal.darkPyraFlame;
                }}, new ShapePart() {{
                    rotateSpeed = -3;
                    sides = 3;
                    lifetime = 400;
                    rotation = 30;
                    radius = 11;
                    radiusTo = 11;
                    colorTo = color = Pal.darkPyraFlame;
                }}, new ShapePart() {{
                    rotateSpeed = -3;
                    sides = 3;
                    lifetime = 400;
                    rotation = 210;
                    radius = 11;
                    radiusTo = 11;
                    colorTo = color = Pal.darkPyraFlame;
                }});

                fragAngle = 0;
                fragRandomSpread = 0;
                fragSpread = 0;
                fragOnAbsorb = fragOnHit = true;
                fragBullets = 1;

                fragBullet = new WindBulletType() {{
                    lifetime = 600;
                    damage = 1.4f;
                    windPower = 0.45f;
                    windWidth = 500;
                    windLength = 250;
                    applyEffect = StatusEffects2.gasify;

                    fillRange = false;
                    windEffect = new Effect(120, 80f, e -> {
                        Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());
                        randLenVectors(e.id, 9, e.finpow() * Math.max(windLength, windWidth / 2) * 1.4f, e.rotation, 50,
                                (x, y) -> Fill.circle(e.x + x, e.y + y, 1 * (1 - e.fin()))
                        );
                    });
                    everyHit = new Effect(120, e -> {
                        Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());
                        if (e.data instanceof Unit u) {
                            float size = u.hitSize;
                            float angle = Angles.angle(e.x, e.y, u.x, u.y);
                            float x = u.x - Angles.trnsx(angle, size / 1.2f);
                            float y = u.y - Angles.trnsy(angle, size / 1.2f);
                            for (float i = -size / 1.5f; i <= size / 1.5f; i += 1) {
                                if (i + size / 1.5f <= 1.1 || i - size / 1.5f >= -1.9) {
                                    Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());
                                } else {
                                    Draw.color(Pal.darkPyraFlame, Color.valueOf("00000099"), Color.valueOf("00000044"), e.fin());
                                }
                                float len = (float) (size / 1.2f - Math.sqrt(size * size / 1.2f / 1.2f - i * i));
                                float lx = x + Angles.trnsx(angle + 90, i) + Angles.trnsx(angle, len);
                                float ly = y + Angles.trnsy(angle + 90, i) + Angles.trnsy(angle, len);
                                randLenVectors((long) (e.id + i), 1, e.finpow() * 200, angle, 24,
                                        (cx, cy) -> Fill.circle(lx + cx, ly + cy, 2 * (1 - e.fin()))
                                );
                                randLenVectors((long) (e.id - i), 1, e.finpow() * 200, angle, 24,
                                        (cx, cy) -> Fill.circle(lx + cx, ly + cy, 2 * (1 - e.fin()))
                                );
                            }
                        }
                        if (e.data instanceof Building b) {
                            float size = b.hitSize();
                            float angle = Angles.angle(e.x, e.y, b.x, b.y);
                            float x = b.x - Angles.trnsx(angle, size / 1.2f);
                            float y = b.y - Angles.trnsy(angle, size / 1.2f);
                            for (float i = -size / 1.5f; i <= size / 1.5f; i += 1) {
                                if (i + size / 1.5f <= 1.1 || i - size / 1.5f >= -1.9) {
                                    Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());
                                } else {
                                    Draw.color(Pal.darkPyraFlame, Color.valueOf("00000099"), Color.valueOf("00000044"), e.fin());
                                }
                                float len = (float) (size / 1.2f - Math.sqrt(size * size / 1.2f / 1.2f - i * i));
                                float lx = x + Angles.trnsx(angle + 90, i) + Angles.trnsx(angle, len);
                                float ly = y + Angles.trnsy(angle + 90, i) + Angles.trnsy(angle, len);
                                randLenVectors((long) (e.id + i), 1, e.finpow() * 200, angle, 24,
                                        (cx, cy) -> Fill.circle(lx + cx, ly + cy, 2 * (1 - e.fin()))
                                );
                                randLenVectors((long) (e.id - i), 1, e.finpow() * 200, angle, 24,
                                        (cx, cy) -> Fill.circle(lx + cx, ly + cy, 2 * (1 - e.fin()))
                                );
                            }
                        }
                    });

                    parts.addAll(new ShapePart() {{
                        rotateSpeed = 3;
                        sides = 3;
                        lifetime = 400;
                        radius = 11;
                        radiusTo = 0;
                        colorTo = color = Pal.darkPyraFlame;
                    }}, new ShapePart() {{
                        rotateSpeed = 3;
                        sides = 3;
                        lifetime = 400;
                        rotation = 180;
                        radius = 11;
                        radiusTo = 0;
                        colorTo = color = Pal.darkPyraFlame;
                    }}, new ShapePart() {{
                        rotateSpeed = 3;
                        sides = 3;
                        lifetime = 400;
                        rotation = 30;
                        radius = 11;
                        radiusTo = 0;
                        colorTo = color = Pal.darkPyraFlame;
                    }}, new ShapePart() {{
                        rotateSpeed = 3;
                        sides = 3;
                        lifetime = 400;
                        rotation = 210;
                        radius = 11;
                        radiusTo = 0;
                        colorTo = color = Pal.darkPyraFlame;
                    }});
                }};
            }});
            ammoTypes.put(Items.metaglass, new PointBulletType() {{
                ammoMultiplier = 1f;

                trailEffect = Fx.none;

                damage = 0;
                lifetime = 600;
                speed = 500;
                trailSpacing = 20f;

                fragAngle = 0;
                fragRandomSpread = 0;
                fragSpread = 0;
                fragOnAbsorb = fragOnHit = true;
                fragBullets = 1;

                fragBullet = new PointBulletType2() {{
                    lifetime = 1000;
                    damage = 0;
                    speed = 0;
                    hittable = absorbable = reflectable = collides = false;
                    despawnEffect = hitEffect = Fx.none;

                    laserGroups = 10;
                    laserInterval = 10;
                    laserDelay = 0;
                    laserBulletType = new BulletType(0, 62) {{
                        lifetime = 1;
                        status = StatusEffects2.torn;
                        statusDuration = 360;
                        hitEffect = despawnEffect = Fx.none;

                        collides = hittable = absorbable = reflectable = false;
                    }};
                    laserColor = Color.valueOf("EBeEF5");
                    laserEffect = Effects.laserLink;
                }};
            }});
        }};
        flowers = new ItemTurret("flowers") {{
            size = 2;
            range = 235;
            reload = 30;
            health = 400;
            inaccuracy = 30;

            shoot = new ShootMulti(new ShootPattern() {{
                shots = 3;
                shotDelay = 1;
            }}, new ShootPattern() {{
                shots = 2;
            }});

            ammo(graphite, new BasicBulletType(8, 2) {{
                lifetime = 30;

                homingPower = 2;
                trailLength = 7;
                homingDelay = 6;
                trailWidth = 1.5f;
                homingRange = 500;
                width = height = 6;
                buildingDamageMultiplier = 0.1f;

                pierce = pierceArmor = true;
                pierceCap = 4;

                frontColor = backColor = trailColor = hitColor = Color.valueOf("b2c6d2");
                shootSound = Sounds.lasershoot;
                shootEffect = Fx.none;
                despawnEffect = hitEffect = Fx.none;
            }}, titanium, new MagneticStormBulletType() {{
                damage = 30;
                lifetime = 240;
                inaccuracy = -30;

                trailColor = Color.valueOf("8da1e3");
                shootEffect = despawnEffect = hitEffect = Fx.none;
            }});

            requirements(Category.turret, ItemStack.with(copper, 100, titanium, 45, silicon, 45));
        }};
        fireBoost = new OwnerTurret("fire_boost") {{
            targetAir = targetGround = true;

            health = 3000;
            size = 4;
            range = 260;
            shootY = 35;
            reload = 5;
            recoil = 3;
            inaccuracy = 15;

            bullet = new ownerBulletType(8f, 8) {{
                absorbable = hittable = reflectable = false;

                lifetime = 6.7f;
                splashDamage = 6;
                splashDamageRadius = 20;

                despawnEffect = hitEffect = Fx.none;

                pierce = true;
                pierceBuilding = true;
                status = StatusEffects.burning;
                statusDuration = 240;
            }};

            requirements(Category.turret, ItemStack.with(
                    Items.titanium, 1500,
                    Items.graphite, 1500,
                    Items.graphite, 2000,
                    Items.silicon, 1500,
                    Items.phaseFabric, 1500,
                    Items.plastanium, 900
            ));
        }};
        fourNet = new LiquidTurret("four_net") {{
            scaledHealth = 10000;
            armor = 55;

            clipSize = 4;
            size = 4;
            reload = 180;
            range = 360;
            rotateSpeed = 12;
            liquidCapacity = 12;
            consumeAmmoOnce = false;
            inaccuracy = 0;

            hasPower = true;
            consumesPower = true;

            consume(new ConsumePower(1000, 100000, false));

            shoot = new ShootSpread() {{
                shots = 2;
                spread = 3;
            }};
            ammoTypes.putAll(
                    Liquids.water, new AroundBulletType() {{
                        ammoMultiplier = 5;

                        lifetime = 3600;
                        speed = 4;
                        damage = 500;
                        splashDamage = 500;
                        splashDamageRadius = 300;
                        trailLength = 25;
                        trailChance = 1;
                        status = StatusEffects.wet;
                        statusDuration = 240;

                        targetRange = 1000;
                        circleRange = 80;

                        statusTime = 35;
                        statusEffect = StatusEffects2.abyss;
                        frontColor = backColor = lightColor = trailColor = Color.valueOf("01066FAA");
                        applyEffect = new WaveEffect() {{
                            colorFrom = colorTo = Color.valueOf("01066FAA");
                            lifetime = 240;
                        }};
                    }},
                    Liquids.slag, new BulletType() {{
                        ammoMultiplier = 1;
                        lifetime = 0;
                        damage = 0;
                        absorbable = hittable = reflectable = collides = false;

                        fragRandomSpread = 30;
                        fragBullets = 36;
                        fragLifeMax = fragLifeMin = 3;
                        fragVelocityMin = 1.5f;
                        fragVelocityMax = 1.8f;
                        fragBullet = new LiquidBulletType() {{
                            liquid = Liquids.slag;

                            homingDelay = 35;
                            homingPower = 0.1f;
                            homingRange = 400;

                            damage = 700;
                            splashDamage = 700;
                            splashDamageRadius = 300;

                            status = StatusEffects2.gasify;
                            statusDuration = 300;
                            lightColor = Pal.darkFlame;
                        }};
                    }});

            requirements(Category.turret, ItemStack.with(Items.titanium, 4999,
                    Items.copper, 4999, Items.thorium, 4999, Items.silicon, 4999, Items.phaseFabric, 4999));
        }};
        scale = new DrillTurret("scale") {{
            requirements(Category.turret, ItemStack.with(copper, 100, lead, 150));

            health = 350;
            size = 1;
            clipSize = 1;
            reload = 30;
            range = 150;
            inaccuracy = 5;

            baseType = new BasicBulletType(7, 25) {{
                width = height = 10;
                shrinkX = shrinkY = 0;

                collidesAir = false;
            }};

            applier.put(copper, b -> {
                if (b.pierce) {
                    b.pierceCap += 1;
                } else {
                    b.pierce = b.pierceBuilding = true;
                    b.pierceCap = 2;
                }
                b.damage += 10;
                b.reloadMultiplier *= 1.5f;
            });
            applier.put(lead, b -> {
                b.collidesAir = true;
                b.lifetime += 10;
                b.rangeChange += b.speed * 10;
                b.splashDamage += 10;
                b.splashDamageRadius += 4;
            });
            applier.put(titanium, b -> {
                b.collidesAir = true;
                b.damage += 8;
                b.lifetime += 10;
                b.rangeChange += b.speed * 10;
                b.splashDamage += 15;
                b.splashDamageRadius += 6;
            });

            shootApplier.put(titanium, s -> s.shots += 1);

            limitRange(baseType, 7);
        }};
        stack = new DrillTurret("stack") {{
            requirements(Category.turret, ItemStack.with(copper, 250, lead, 170, silicon, 120, graphite, 150));

            health = 1500;
            size = 2;
            reload = 90;
            range = 240;
            inaccuracy = 5;

            baseType = new BasicBulletType(7, 40) {{
                width = height = 20;
                shrinkX = shrinkY = 0;
            }};

            applier.put(copper, b -> {
                if (b.pierce) {
                    b.pierceCap += 1;
                } else {
                    b.pierce = b.pierceBuilding = true;
                    b.pierceCap = 2;
                }
                b.damage += 12;
                b.reloadMultiplier *= 1.15f;
            });
            applier.put(lead, b -> {
                b.lifetime += 12;
                b.rangeChange += b.speed * 12;
                b.splashDamage += 17;
                b.splashDamageRadius += 6;
            });
            applier.put(titanium, b -> {
                if (b.status == null) {
                    b.status = StatusEffects.slow;
                    b.statusDuration = 17;
                } else {
                    b.statusDuration += 22;
                }
            });
            applier.put(thorium, b -> {
                b.damage *= 1.15f;
                b.lifetime += 7;
                b.rangeChange += b.speed * 7;
                b.splashDamage += 7;
                b.splashDamageRadius += 4;
                b.pierceArmor = true;
            });

            shootApplier.put(titanium, s -> s.shots += 1);
            shootApplier.put(thorium, s -> {
                s.shots += 2;
                s.shotDelay += 2;
                s.firstShotDelay += 5;
            });

            specialApplier.put(t -> {
                Tile t1 = t.tile;
                Tile t2 = world.tile(t1.x + 1, t1.y);
                Tile t3 = world.tile(t1.x, t1.y + 1);
                Tile t4 = world.tile(t1.x + 1, t1.y + 1);
                return t1.overlay().itemDrop == copper && t4.overlay().itemDrop == copper &&
                        t2.overlay().itemDrop == lead && t3.overlay().itemDrop == lead;
            }, b -> {
                b.status = StatusEffects2.gasify;
                b.statusDuration = 12;
                b.reloadMultiplier *= 1.55f;
            });

            limitRange(baseType, 7);
        }};
//======================================================================================================================
        eleFence = new ElectricFence("eleFence") {{
            health = 1500;
            armor = 10;
            size = 3;
            clipSize = 3;
            hasPower = true;

            radius = 350;
            connect = 10;
            force = 180;
            damage = 1.2f;
            reload = 1800;

            air = true;
            statusEffect = StatusEffects.burning;
            statusTime = 300;

            consume(new ConsumePower(15, 5000, false));

            requirements(Category.defense, ItemStack.with(
                    Items.titanium, 230,
                    Items.copper, 440,
                    Items.silicon, 200
            ));
        }};
        eleFenceLarge = new ElectricFence("eleFenceLarge") {{
            health = 3000;
            armor = 20;
            size = 4;
            clipSize = 4;
            hasPower = true;

            radius = 600;
            connect = 15;
            force = 800;
            damage = 2.5f;
            reload = 1800;

            air = true;
            statusEffect = StatusEffects2.gasify;
            statusTime = 420;

            consume(new ConsumePower(30, 15000, false));

            requirements(Category.defense, ItemStack.with(
                    thorium, 450,
                    Items.titanium, 650,
                    Items.silicon, 370
            ));
        }};
        decoy = new Decoy("decoy") {{
            requirements(Category.defense, ItemStack.with(silicon, 25, blastCompound, 5, titanium, 25));

            size = 1;
            clipSize = 1;
            health = 1000;

            farDeflect = 0.75f;
            farDeflectChance = 0.5f;
            adaptability = 1;
            adaptDamageMax = 1.25f;
            adaptDamageMin = 0.75f;
            flexibility = 15;

            breakable = true;
            destroyBulletSameTeam = true;
            destroyBullet = new BulletType() {{
                reflectable = hittable = absorbable = false;
                lifetime = 0;
                speed = 0;
                damage = 0;
                splashDamageRadius = 75;
                splashDamage = 150;

                lightning = 4;
                lightningDamage = 25;
                lightningLength = 15;
                lightningLengthRand = 4;
                lightningCone = 360;
                lightningColor = Color.valueOf("DD88DD");

                hitEffect = Fx.blockExplosionSmoke;
                despawnEffect = Fx.blockExplosionSmoke;
            }};
        }};
        decoyLarge = new Decoy("decoy-large") {{
            requirements(Category.defense, ItemStack.with(silicon, 200, phaseFabric, 35, blastCompound, 25,
                    titanium, 200, thorium, 150));

            size = 2;
            health = 3500;

            farDeflect = 1.25f;
            farDeflectChance = 0.75f;
            adaptability = 1;
            adaptDamageMax = 2f;
            adaptDamageMin = 0.5f;
            flexibility = 30;

            breakable = true;
            destroyBulletSameTeam = true;
            destroyBullet = new BulletType() {{
                reflectable = hittable = absorbable = false;
                lifetime = 0;
                speed = 0;
                damage = 0;
                splashDamageRadius = 150;
                splashDamage = 300;

                lightning = 18;
                lightningDamage = 40;
                lightningLength = 35;
                lightningLengthRand = 15;
                lightningCone = 360;
                lightningColor = Color.valueOf("DD88DD");

                hitEffect = Fx.blockExplosionSmoke;
                despawnEffect = Fx.blockExplosionSmoke;
            }};
        }};
        polymerizationWall = new Wall("polymerization-wall") {{
            requirements(Category.defense, ItemStack.with(thorium, 8, phaseFabric, 8, surgeAlloy, 8));
            size = 1;
            health = 500 * 8;
            absorbLasers = true;
            chanceDeflect = 12;
            lightningDamage = 30;
            lightningLength = 15;
            lightningChance = 0.3f;
            envDisabled |= Env.scorching;
        }};
        polymerizationWallLarge = new Wall("polymerization-wall-large") {{
            requirements(Category.defense, ItemStack.with(thorium, 32, phaseFabric, 32, surgeAlloy, 32));
            size = 2;
            health = 500 * 32;
            absorbLasers = true;
            chanceDeflect = 12;
            lightningDamage = 30;
            lightningLength = 15;
            lightningChance = 0.3f;
            envDisabled |= Env.scorching;
        }};
        weakPowerWall = new PowerWall("weak-power-wall") {{
            requirements(Category.defense, ItemStack.with(copper, 6, titanium, 6, silicon, 3));

            health = 100 * 8;
            size = 1;

            lightningChance = 0.1f;
            lightningLength = 6;
            lightningDamage = 15;
            chanceDeflect = 5;
            healChance = 0.25f;
            healPercent = 0.024f;
            powerHit = 10f;

            consumePower(0.2f);
        }};
        weakPowerWallLarge = new PowerWall("weak-power-wall-large") {{
            requirements(Category.defense, ItemStack.with(copper, 24, titanium, 24, silicon, 12));

            health = 100 * 32;
            size = 2;

            lightningChance = 0.1f;
            lightningLength = 6;
            lightningDamage = 15;
            chanceDeflect = 5;
            healChance = 0.25f;
            healPercent = 0.024f;
            powerHit = 10f;

            consumePower(0.2f);
        }};
        superPowerWall = new PowerWall("super-power-wall") {{
            requirements(Category.defense, ItemStack.with(thorium, 6, surgeAlloy, 6, silicon, 3));

            health = 220 * 8;
            size = 1;

            lightningChance = 0.2f;
            lightningLength = 10;
            lightningDamage = 25;
            chanceDeflect = 9;
            healChance = 0.25f;
            healPercent = 0.03f;
            powerHit = 12;

            consumePower(0.5f);
        }};
        superPowerWallLarge = new PowerWall("super-power-wall-large") {{
            requirements(Category.defense, ItemStack.with(thorium, 24, surgeAlloy, 24, silicon, 12));

            health = 220 * 32;
            size = 2;

            lightningChance = 0.2f;
            lightningLength = 10;
            lightningDamage = 25;
            chanceDeflect = 9;
            healChance = 0.25f;
            healPercent = 0.03f;
            powerHit = 12;

            consumePower(0.5f);
        }};
//======================================================================================================================
        slowProject = new DownProject("slow-project") {{
            requirements(Category.effect, with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30));
            range = 8;
            downSpeed = 0.9f;

            consumePower(3.50f);
            size = 2;
            consumeItem(Items.phaseFabric).boost();
        }};
        buildCore = new DesCore("build-core") {{
            size = 3;
            health = 500;
            armor = 5;
            itemCapacity = 2000;

            alwaysUnlocked = true;
            unitCapModifier = 2;
            requirements(Category.effect, ItemStack.with(Items.copper, 1000, Items.lead, 1000, Items.graphite, 1000, Items.silicon, 1000, Items.titanium, 1000));
        }};
        unitUpper = new UnitProjectBlock("unit-upper") {{
            size = 3;
            health = 2500;
            itemCapacity = 0;

            requirements(Category.effect, ItemStack.with(Items.copper, 2000, Items.lead, 2000,
                    Items.graphite, 2000, Items.silicon, 2000, Items.titanium, 2000));
        }};
        reflective = new ReflectiveShield("reflective") {{
            health = 650;
            size = 2;

            consumePower(5);
            requirements(Category.effect, BuildVisibility.debugOnly, ItemStack.with(Items.silicon, 400, Items.titanium, 400, Items.phaseFabric, 200));
        }};
        coreLaunch = new CoreLaunchBlock("core-launch") {{
            size = 2;
            health = 380;

            consumePower(0.5f);
            requirements(Category.effect, ItemStack.with(Items.silicon, 20, Items.copper, 50, Items.graphite, 30));
        }};
        coreLaunchLarge = new CoreLaunchBlock("core-launch-large") {{
            size = 3;
            health = 800;
            reload = 600;

            consumePower(1.2f);
            requirements(Category.effect, ItemStack.with(silicon, 30, titanium, 60, graphite, 40));
        }};
        mendProjectorLarge = new MendProjector("mend-projector-large") {{
            requirements(Category.effect, with(Items.titanium, 100, Items.silicon, 150, phaseFabric, 50));
            consumePower(3);
            size = 3;
            reload = 300f;
            range = 180;
            healPercent = 30;
            phaseBoost = 20;
            phaseRangeBoost = 125f;
            scaledHealth = 320;
            researchCostMultiplier = 10;
            consumeItem(Items.phaseFabric).boost();
        }};
        forceProjectorLarge = new ForceProjector("force-projector-large") {{
            requirements(Category.effect, with(Items.titanium, 200, thorium, 150, Items.silicon, 125, phaseFabric, 25));
            size = 4;
            phaseRadiusBoost = 150;
            radius = 252f;
            shieldHealth = 10000f;
            cooldownNormal = 3.5f;
            cooldownLiquid = 1.5f;
            cooldownBrokenBase = 0.25f;
            researchCostMultiplier = 10;

            itemConsumer = consumeItem(Items.phaseFabric).boost();
            consumePower(6f);
        }};
        bulletSlower = new BulletSlowProject("bullet-slower") {{
            requirements(Category.effect, ItemStack.with(silicon, 35, titanium, 25, lead, 45));

            health = 1500;
            slow = 0;
            size = 3;
            range = 250;
            reload = 75;
            slowPercent = 0.35f;

            consume(new ConsumePower(3, 0, false));
        }};
        shockMineFiller = new Filler("shock-mine-builder", Blocks.shockMine) {{
            requirements(Category.effect, ItemStack.with(Items.silicon, 75, Items.lead, 100, Items.graphite, 50), true);
            consume(new ConsumePowerCondition(5, b -> b instanceof FillerBuild f && f.progress < 1));

            size = 2;
        }};
        automaticDistributor = new AllocationBlock("automatic-distributor") {{
            size = 2;
            health = 350;
            consumePower(1.5f);
            requirements(Category.effect, ItemStack.with(copper, 75, titanium, 45, silicon, 35, graphite, 40));
        }};
        automaticDistributorLarge = new AllocationBlock("automatic-distributor-large") {{
            num = 8f;
            size = 3;
            radius = 160;
            health = 850;
            consumePower(7f);
            requirements(Category.effect, ItemStack.with(titanium, 100, thorium, 40, silicon, 75, graphite, 70));
        }};
//======================================================================================================================
        sorterOverflowGate = new SorterOverflowGate("sorter-overflow-gate") {{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
            invert = false;
        }};
        sorterUnderflowGate = new SorterOverflowGate("sorter-underflow-gate") {{
            requirements(Category.distribution, with(Items.lead, 2, Items.copper, 2));
            buildCostMultiplier = 3f;
            invert = true;
        }};
        multiMassDriver = new MultiMassDriver("multi-mass-drive") {{
            requirements(Category.distribution, with(Items.titanium, 125, Items.silicon, 50, Items.lead, 125, Items.thorium, 50));
            size = 3;
            itemCapacity = 360;
            reload = 300f;
            range = 600f;
            consumePower(3f);
        }};
    }
}