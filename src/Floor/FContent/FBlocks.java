package Floor.FContent;

import Floor.FEntities.FBlock.*;
import Floor.FEntities.FBulletType.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.abilities.EnergyFieldAbility;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.entities.part.FlarePart;
import mindustry.entities.part.ShapePart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.consumers.ConsumePower;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;
import static mindustry.type.ItemStack.with;

public class FBlocks {
    //test
    public static Block reflective, pu;
    //units
    public static Block outPowerFactory, inputPowerFactory;
    //defense
    public static Block eleFenceII, eleFenceIII, autoWall;
    //turret
    public static Block fourNet, fireBoost, windTurret, tranquil, billow, residual;
    //crafting
    public static Block primarySolidification, intermediateSolidification, advancedSolidification, ultimateSolidification;
    //effect
    public static Block buildCore, slowProject, unitUpper;

    public static void load() {
        primarySolidification = new StackCrafter("primary-solidification") {{
            itemCapacity = 60;
            liquidCapacity = 120;
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionCopper, 14),
                    ItemStack.with(Items.copper, 5),
                    LiquidStack.empty, 120
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionLead, 14),
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
            health = 250;

            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionCopper, 30),
                    ItemStack.with(Items.copper, 12),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionLead, 30),
                    ItemStack.with(Items.lead, 12),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionTitanium, 30),
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
            health = 750;

            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionCopper, 60),
                    ItemStack.with(Items.copper, 30),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionLead, 60),
                    ItemStack.with(Items.lead, 30),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionTitanium, 60),
                    ItemStack.with(Items.titanium, 24),
                    LiquidStack.empty, 90
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionThorium, 60),
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
            health = 750;

            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionCopper, 90),
                    ItemStack.with(Items.copper, 48),
                    LiquidStack.empty, 60
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionLead, 90),
                    ItemStack.with(Items.lead, 48),
                    LiquidStack.empty, 60
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionTitanium, 90),
                    ItemStack.with(Items.titanium, 48),
                    LiquidStack.empty, 60
            ));
            switchStack.add(new ProductStack(
                    ItemStack.empty,
                    LiquidStack.with(FLiquids.fusionThorium, 90),
                    ItemStack.with(Items.thorium, 48),
                    LiquidStack.empty, 60
            ));

            consume(new ConsumePower(100, 0, false));

            requirements(Category.crafting, ItemStack.with(Items.metaglass, 1500, Items.copper, 1450,
                    Items.lead, 1400, Items.graphite, 1350, Items.titanium, 1400, Items.thorium, 1450, Items.surgeAlloy, 500));
        }};
//======================================================================================================================
        outPowerFactory = new GradeFactory("out_power_factory") {{
            requirements(Category.units, with(Items.copper, 2000, Items.lead, 2400, Items.silicon, 2800));

            itemCapacity = 770;
            size = 11;
            consumePower(500);

            constructTime = 60f * 10f;
        }};
        inputPowerFactory = new GradeFactory("input_power_factory") {{
            requirements(Category.units, with(Items.copper, 2000, Items.lead, 2400, Items.silicon, 2800));

            itemCapacity = 770;
            size = 11;
            consumePower(500);

            constructTime = 60f * 10f;
            out = false;
        }};
//======================================================================================================================
        reflective = new ReflectiveShield("reflective") {{
            health = 650;
            size = 2;

            consumePower(5);
            requirements(Category.effect, ItemStack.with(Items.copper, 1));
        }};
        pu = new PureProject("pu") {{
            health = 650;

            consumePower(50f);

            requirements(Category.effect, ItemStack.with(Items.copper, 1));
        }};
//======================================================================================================================
        residual = new ItemTurret("residual") {{
            requirements(Category.turret, with(Items.copper, 100, Items.graphite, 80, Items.titanium, 25, Items.silicon, 25));
            ammo(
                    Items.titanium, new BasicBulletType(4, 15) {{
                        width = 7f;
                        height = 9f;
                        lifetime = 60f;
                        ammoMultiplier = 4;
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootBigSmoke;
                        frontColor = backColor = Color.valueOf("8da1e3");
                        pierce = true;
                        pierceCap = 3;
                    }},
                    Items.thorium, new BasicBulletType(4, 18) {{
                        width = 9f;
                        height = 12f;
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootBigSmoke;
                        frontColor = backColor = Color.valueOf("f9a3c7");
                        reloadMultiplier = 0.9f;
                        ammoMultiplier = 4;
                        lifetime = 60f;
                        pierce = true;
                        pierceCap = 2;
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
            range = 190;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 800;
            inaccuracy = 0;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            researchCostMultiplier = 8f;

            limitRange();
        }};
        billow = new PowerTurret("billow") {{
            requirements(Category.turret, ItemStack.with(Items.titanium, 340,
                    Items.copper, 300, Items.graphite, 350
            ));
            consume(new ConsumePower(8, 480, false));

            size = 2;
            recoil = 3;
            range = 540;
            health = 1000;
            rotateSpeed = 10f;
            researchCostMultiplier = 8f;
            canOverdrive = false;

            shootY = 3f;
            reload = 400;
            inaccuracy = 0;
            shootCone = 15f;

            shootType = new BulletType(0, 0) {{
                rangeOverride = 540;
                ammoMultiplier = 2f;
                absorbable = reflectable = hittable = collides = false;
                spawnUnit = new MissileUnitType("billow1") {{
                    health = 600;
                    armor = 24;
                    lifetime = 360;
                    speed = 1.5f;
                    trailLength = 0;
                    playerControllable = false;
                    logicControllable = false;

                    abilities.add(new EnergyFieldAbility(1, 60, 300) {{
                        status = FStatusEffects.suppress;
                        statusDuration = 420;
                        effectRadius = 0;
                        healPercent = 1;
                    }});

                    abilities.add(new EnergyFieldAbility(0, 12, 120) {{
                        status = StatusEffects.unmoving;
                        statusDuration = 380;
                        effectRadius = 0;
                        healPercent = 0;
                        sectors = 3;
                        color = Pal.redderDust;
                        healEffect = hitEffect = damageEffect = Fx.none;
                        shootSound = Sounds.none;
                    }});

                }};
            }};
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
        tranquil = new PowerTurret("tranquil") {{
            consume(new ConsumePower(20, 0, false));

            health = 2000;
            size = 4;

            recoil = 2f;
            range = 400;
            reload = 90;
            consumesPower = true;
            hasPower = true;
            consumeAmmoOnce = false;
            canOverdrive = false;

            shootType = new BulletType() {{
                hittable = reflectable = absorbable = false;

                speed = 4.5f;
                lifetime = 90;

                trailChance = 1f;
                trailInterp = Interp.slope;
                trailWidth = 4.5f;
                trailLength = 19;
                trailEffect = new MultiEffect(Fx.artilleryTrail, Fx.artilleryTrailSmoke);
                rangeOverride = 200;

                status = StatusEffects.unmoving;
                statusDuration = 60;
                splashDamageRadius = 60;
                splashDamage = 32;

                shootEffect = smokeEffect = Fx.none;
                hitEffect = new ExplosionEffect() {{
                    lifetime = 360;
                    waveColor = Pal.redLight;
                    waveRadBase = 60;
                    waveRad = 61;
                    waveStroke = 4;
                    waveLife = 45;
                    smokes = 13;
                    smokeRad = 60;
                    smokeColor = Pal.redLight;
                    smokeSizeBase = 0;
                    smokeSize = 5;
                }};
                despawnEffect = hitEffect;

                parts.add(new ShapePart() {{
                    radius = 5.5f;
                    radiusTo = 5.5f;
                    circle = true;
                    color = colorTo = Pal.redLight;
                }});

                fragRandomSpread = 360;
                fragBullets = 6;
                fragLifeMax = 2.5f;
                fragLifeMin = 2f;
                fragVelocityMin = 0.4f;
                fragVelocityMax = 0.5f;
                fragOnAbsorb = true;
                fragOnHit = true;
                fragBullet = new BulletType() {{
                    damage = 12;
                    splashDamageRadius = 24;
                    status = FStatusEffects.torn;
                    statusDuration = 240;

                    hitEffect = new ExplosionEffect() {{
                        lifetime = 300;

                        waveColor = Pal.redLight;
                        waveRadBase = 28;
                        waveRad = 24;
                        waveStroke = 1;
                        waveLife = 240;

                        smokes = 14;
                        smokeRad = 16;
                        smokeColor = Pal.redLight;
                        smokeSizeBase = 0;
                        smokeSize = 3;
                    }};
                    despawnEffect = hitEffect;
                }};

                intervalDelay = 2;
                bulletInterval = 8;
                intervalSpread = 22;
                intervalAngle = -11;
                intervalBullets = 2;
                intervalBullet = new BasicBulletType() {{
                    speed = 1.5f;
                    lifetime = 90;

                    frontColor = backColor = trailColor = Pal.redLight;
                    trailLength = 7;

                    status = FStatusEffects.torn;
                    statusDuration = 240;

                    splashDamageRadius = 24;
                    splashDamage = 12;

                    hitEffect = new ExplosionEffect() {{
                        lifetime = 240;

                        waveColor = Pal.redLight;
                        waveRadBase = 16;
                        waveRad = 28;
                        waveStroke = 1;
                        waveLife = 240;

                        smokes = 12;
                        smokeRad = 16;
                        smokeColor = Pal.redLight;
                        smokeSizeBase = 0;
                        smokeSize = 3;
                    }};
                    despawnEffect = hitEffect;
                }};
            }};

            requirements(Category.turret, ItemStack.with(
                    Items.copper, 1000,
                    Items.titanium, 700,
                    Items.graphite, 800
            ));
        }};
        windTurret = new ItemTurret("wind_turret") {{
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
                    applyEffect = FStatusEffects.gasify;

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

                fragBullet = new FreeBulletType() {{
                    lifetime = 1000;
                    damage = 0;
                    speed = 0;
                    hittable = absorbable = reflectable = collides = false;
                    despawnEffect = hitEffect = Fx.none;

                    parts.add(new FlarePart() {{
                        sides = 3;
                        radius = 55;
                        radiusTo = 0;
                        rotMove = 1000;
                        stroke = 40;
                        innerScl = 0.2f;
                        color1 = Color.valueOf("EBEEF5");
                        color2 = Color.valueOf("EBEEF5");
                    }});

                    intervalBullets = 3;
                    intervalRandomSpread = 360;
                    bulletInterval = 3;
                    intervalDelay = 0;
                    intervalBullet = new BulletType(0, 0) {{
                        rangeOverride = 160;
                        lifetime = 0;
                        splashDamageRadius = 58;
                        splashDamage = 62;
                        status = FStatusEffects.suppress;
                        statusDuration = 360;

                        collides = hittable = absorbable = reflectable = false;
                    }};

                    intervalHitEffect = new ExplosionEffect() {{
                        lifetime = 13;
                        sparks = 0;
                        waveLife = 0;
                        smokes = 6;
                        smokeRad = 47;
                        smokeColor = Color.valueOf("EBEEF522");
                        smokeSizeBase = 1.5f;

                        renderer = f -> {
                            Draw.color(this.waveColor);
                            f.scaled(this.waveLife, (i) -> {
                                Lines.stroke(this.waveStroke * i.fout());
                                Lines.circle(f.x, f.y, this.waveRadBase + i.fin() * this.waveRad);
                            });
                            Draw.color(this.smokeColor);
                            if (this.smokeSize > 0.0F) {
                                Angles.randLenVectors(f.id, this.smokes, 2.0F + this.smokeRad * f.finpow(), (x, y) -> Fill.circle(f.x + x, f.y + y, f.fout() * this.smokeSize + this.smokeSizeBase));
                            }

                            Draw.color(this.sparkColor);
                            Lines.stroke(f.fout() * this.sparkStroke);
                            Angles.randLenVectors(f.id + 1, this.sparks, 1.0F + this.sparkRad * f.finpow(), (x, y) -> {
                                Lines.lineAngle(f.x + x, f.y + y, Mathf.angle(x, y), 1.0F + f.fout() * this.sparkLen);
                                Drawf.light(f.x + x, f.y + y, f.fout() * this.sparkLen * 4.0F, this.sparkColor, 0.7F);
                            });

                            if (f.data instanceof Vec2 v) {
                                color(Color.valueOf("EBEEF522"));
                                Lines.stroke(4.5f * (1 - f.fin()));
                                Lines.line(f.x, f.y, v.x, v.y);
                            }
                        };
                    }};
                }};
            }});
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
                        circleRange = 160;

                        statusTime = 35;
                        statusEffect = FStatusEffects.abyss;
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

                            status = FStatusEffects.gasify;
                            statusDuration = 300;
                            lightColor = Pal.darkFlame;
                        }};
                    }});

            requirements(Category.turret, ItemStack.with(Items.titanium, 4999,
                    Items.copper, 4999, Items.thorium, 4999, Items.silicon, 4999, Items.phaseFabric, 4999));
        }};
//======================================================================================================================
        eleFenceII = new ElectricFence("ele_fenceII") {{
            health = 1500;
            size = 3;
            clipSize = 3;
            hasPower = true;

            maxLength = 400;
            maxConnect = 15;
            maxFenceSize = 150;
            eleDamage = 1.2f;
            air = true;
            statusEffect = FStatusEffects.gasify;
            statusTime = 300;

            consume(new ConsumePower(25, 5000, false));

            requirements(Category.defense, ItemStack.with(
                    Items.titanium, 350,
                    Items.copper, 600,
                    Items.silicon, 300
            ));
        }};
        eleFenceIII = new ElectricFence("ele_fenceIII") {{
            health = 3000;
            size = 4;
            clipSize = 4;
            hasPower = true;

            maxLength = 650;
            maxConnect = 25;
            maxFenceSize = 300;
            eleDamage = 2.5f;
            air = true;
            statusEffect = FStatusEffects.sublimation;
            statusTime = 420;

            consume(new ConsumePower(75, 15000, false));

            requirements(Category.defense, ItemStack.with(
                    Items.titanium, 450,
                    Items.copper, 1000,
                    Items.silicon, 500
            ));
        }};
        autoWall = new AutoBlock("auto-wall") {{
            size = 2;
            health = 350;

            requirements(Category.defense, ItemStack.with(Items.copper, 145, Items.graphite, 35));

            creates.putAll(ItemStack.with(Items.copper, 120, Items.silicon, 10), Blocks.copperWall,
                    ItemStack.with(Items.titanium, 120, Items.silicon, 10), Blocks.titaniumWall,
                    ItemStack.with(Items.thorium, 120, Items.silicon, 10), Blocks.thoriumWall,
                    ItemStack.with(Items.phaseFabric, 120, Items.silicon, 10), Blocks.phaseWall,
                    ItemStack.with(Items.surgeAlloy, 120, Items.silicon, 10), Blocks.surgeWall);
        }};
//======================================================================================================================
        slowProject = new DownProject("slow_project") {{
            requirements(Category.effect, with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30));
            range = 8;
            downSpeed = 0.9f;

            consumePower(3.50f);
            size = 2;
            consumeItem(Items.phaseFabric).boost();
        }};
        buildCore = new DesCore("buildCore") {{
            size = 3;
            health = 500;
            armor = 5;
            itemCapacity = 2000;

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
//======================================================================================================================
        blockOverride();
    }

    public static void blockOverride() {
        UnitFactory uf = (UnitFactory) Blocks.airFactory;
        uf.plans.add(new UnitFactory.UnitPlan(FUnits.barb, 1800, ItemStack.with(Items.silicon, 20, Items.titanium, 10)));
        Reconstructor rt = (Reconstructor) Blocks.additiveReconstructor;
        rt.upgrades.add(new UnitType[]{FUnits.barb, FUnits.hammer});
        rt = (Reconstructor) Blocks.multiplicativeReconstructor;
        rt.upgrades.add(new UnitType[]{FUnits.hammer, FUnits.buying});
        rt = (Reconstructor) Blocks.exponentialReconstructor;
        rt.upgrades.add(new UnitType[]{FUnits.buying, FUnits.crazy});
        rt = (Reconstructor) Blocks.tetrativeReconstructor;
        rt.upgrades.add(new UnitType[]{FUnits.crazy, FUnits.transition});

        ItemTurret turret = (ItemTurret) Blocks.salvo;
        turret.ammoTypes.each((i, b) -> b.damage += 4);
    }
}