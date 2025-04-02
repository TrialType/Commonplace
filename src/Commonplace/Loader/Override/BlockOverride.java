package Commonplace.Loader.Override;

import Commonplace.Entities.BulletType.StatusBulletType;
import Commonplace.Loader.DefaultContent.StatusEffects2;
import Commonplace.Loader.DefaultContent.Units2;
import Commonplace.Entities.BulletType.ProtectKillerBulletType;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.ShootSpread;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PointDefenseTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.MassDriver;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.heat.HeatProducer;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;

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
        ((Wall) Blocks.surgeWall).lightningChance = 0.07f;
        ((Wall) Blocks.surgeWall).lightningDamage = 30;
        Blocks.surgeWallLarge.health = 250 * 40;
        ((Wall) Blocks.surgeWallLarge).lightningChance = 0.07f;
        ((Wall) Blocks.surgeWallLarge).lightningDamage = 30;

        Blocks.shockMine.health = 150;
        ((ShockMine) Blocks.shockMine).damage = 30;
        ((ShockMine) Blocks.shockMine).shots = 1;
        ((ShockMine) Blocks.shockMine).length = 15;
        ((ShockMine) Blocks.shockMine).tendrils = 7;
        ((ShockMine) Blocks.shockMine).bullet = new StatusBulletType();

        ((MassDriver) Blocks.massDriver).range = 600f;

//        UnitFactory uf = (UnitFactory) Blocks.airFactory;
//        uf.plans.add(new UnitFactory.UnitPlan(Units2.barb, 1800, ItemStack.with(Items.silicon, 20, Items.titanium, 10)));
//        Reconstructor rt = (Reconstructor) Blocks.additiveReconstructor;
//        rt.upgrades.add(new UnitType[]{Units2.barb, Units2.hammer});
//        rt = (Reconstructor) Blocks.multiplicativeReconstructor;
//        rt.upgrades.add(new UnitType[]{Units2.hammer, Units2.buying});
//        rt = (Reconstructor) Blocks.exponentialReconstructor;
//        rt.upgrades.add(new UnitType[]{Units2.buying, Units2.crazy});
//        rt = (Reconstructor) Blocks.tetrativeReconstructor;
//        rt.upgrades.add(new UnitType[]{Units2.crazy, Units2.transition});

        ItemTurret turret = (ItemTurret) Blocks.salvo;
        turret.ammoTypes.each((i, b) -> b.damage += 4);

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

        ((ItemTurret) Blocks.hail).ammoTypes.each((i, b) -> {
            b.fragBullets = 3;
            b.fragBullet = new BasicBulletType() {{
                width = height = 6;
                damage = b.damage * 0.2f;
                lifetime = 15;
                speed = 0.5f;
            }};
            b.fragRandomSpread = 360;
        });

        ((ItemTurret) Blocks.scatter).ammoTypes.each((i, b) -> {
            b.pierceArmor = true;
            b.pierce = true;
            b.pierceCap = 3;
        });

        ((ItemTurret) Blocks.salvo).ammoTypes.each((i, b) -> {
            b.knockback = 4;
            b.damage *= 1.4f;
        });

        ((ItemTurret) Blocks.scorch).reload = 8;
        ((ItemTurret) Blocks.scorch).range = 120;
        ((ItemTurret) Blocks.scorch).ammoTypes.each((i, b) -> {
            b.lifetime = 36;
            b.damage *= 1.2f;
            b.buildingDamageMultiplier = 0.25f;
            b.shootEffect = new Effect(34f, 80f, e -> {
                color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());

                randLenVectors(e.id, 10, e.finpow() * 120f, e.rotation, 10f, (x, y) -> Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f));
            });
        });

        ((ItemTurret) Blocks.fuse).range = 160;
        ((ItemTurret) Blocks.fuse).shoot = new ShootSpread(7, 10);
        ((ItemTurret) Blocks.fuse).ammoTypes.each((i, b) -> {
            ((ShrapnelBulletType) b).length = 150;
            b.damage /= 1.5f;
        });

        ((ItemTurret) Blocks.swarmer).range = 250;
        ((ItemTurret) Blocks.swarmer).reload = 40;
        ((ItemTurret) Blocks.swarmer).shoot.shots = 10;
        ((ItemTurret) Blocks.swarmer).shoot.shotDelay = 3;
        ((ItemTurret) Blocks.swarmer).ammoTypes.each((i, b) -> {
            b.damage *= 1.15f;
            b.splashDamage *= 1.15f;
            b.lightningDamage *= 1.15f;
            b.ammoMultiplier += 1;
        });
        ((ItemTurret) Blocks.swarmer).limitRange(5);

        ((ItemTurret) Blocks.ripple).reload = 45;
        ((ItemTurret) Blocks.ripple).range = 400;
        ((ItemTurret) Blocks.ripple).shoot.shots = 40;
        ((ItemTurret) Blocks.ripple).ammoTypes.each((i, b) -> {
            b.createChance = 0.2f;
            b.buildingDamageMultiplier = 1.5f;
        });
        ((ItemTurret) Blocks.ripple).limitRange(1);

        ((ItemTurret) Blocks.cyclone).ammoTypes.each((i, b) -> {
            b.damage *= 1.2f;
            b.splashDamage *= 1.2f;
            b.statusDuration *= 1.5f;
            if (b.fragBullet != null) {
                b.fragBullets += 4;
            }
            if (b.lightning > 0) {
                b.lightningDamage = b.lightningDamage > 0 ? b.lightningDamage * 1.4f : b.damage;
                b.lightningLength += 15;
            }
        });

        ((ItemTurret) Blocks.foreshadow).range = 800;
        ((ItemTurret) Blocks.foreshadow).ammoTypes.get((Items.surgeAlloy)).damage *= 1.25f;
        ((ItemTurret) Blocks.foreshadow).limitRange(0);

        ((PowerTurret) Blocks.arc).shootType.damage = 26;
        ((PowerTurret) Blocks.arc).shootType.lightningType.pierceArmor = true;

        ((PowerTurret) Blocks.lancer).range = 220;
        ((PowerTurret) Blocks.lancer).shootType.damage = 160;
        ((PowerTurret) Blocks.lancer).shootType.pierceCap = -1;
        ((PowerTurret) Blocks.lancer).shootType.status = StatusEffects.slow;
        ((PowerTurret) Blocks.lancer).shootType.statusDuration = 45;
        ((LaserBulletType) ((PowerTurret) Blocks.lancer).shootType).length = 200;

        Blocks.parallax.scaledHealth = 300;
        ((TractorBeamTurret) Blocks.parallax).force = 24;
        ((TractorBeamTurret) Blocks.parallax).range = 300;
        ((TractorBeamTurret) Blocks.parallax).damage = 0.5f;
        ((TractorBeamTurret) Blocks.parallax).targetGround = true;

        ((PointDefenseTurret) Blocks.segment).retargetTime = 4;

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