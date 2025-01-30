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

        ((PointDefenseTurret) Blocks.segment).retargetTime = 2;

        ((Conveyor) Blocks.conveyor).speed = 0.05f;
        ((Conveyor) Blocks.conveyor).displayedSpeed = 7;

        ((Conveyor) Blocks.titaniumConveyor).speed = 0.12f;
        ((Conveyor) Blocks.titaniumConveyor).displayedSpeed = 16.5f;

        ((StackConveyor) Blocks.plastaniumConveyor).speed = 0.1f;
        Blocks.plastaniumConveyor.itemCapacity = 20;

        ((ArmoredConveyor) Blocks.armoredConveyor).speed = 0.12f;
        ((ArmoredConveyor) Blocks.armoredConveyor).displayedSpeed = 15;

        ((Drill) Blocks.mechanicalDrill).drillTime = 480;
        ((Drill) Blocks.pneumaticDrill).drillTime = 320;
        ((Drill) Blocks.laserDrill).drillTime = 274;
        ((Drill) Blocks.blastDrill).drillTime = 274;

        ((UnitFactory) Blocks.airFactory).plans.find(p -> p.unit == UnitTypes.mono).
                requirements = ItemStack.with(Items.copper, 40, Items.lead, 40);

        ((GenericCrafter) Blocks.siliconSmelter).outputItem = new ItemStack(Items.silicon, 3);
        ((GenericCrafter) Blocks.siliconSmelter).craftTime = 60;
        Blocks.siliconSmelter.itemCapacity = 12;
        ConsumeItems ci = Blocks.siliconSmelter.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 2;
        ci.items[1].amount = 4;
        Blocks.siliconSmelter.consumePower(1);

        ((AttributeCrafter) Blocks.siliconCrucible).outputItem = new ItemStack(Items.silicon, 12);
        Blocks.siliconCrucible.itemCapacity = 36;

        ((GenericCrafter) Blocks.graphitePress).outputItem = new ItemStack(Items.graphite, 3);
        ((GenericCrafter) Blocks.graphitePress).craftTime = 135;
        Blocks.graphitePress.itemCapacity = 12;
        ci = Blocks.graphitePress.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 4;

        ((GenericCrafter) Blocks.multiPress).outputItem = new ItemStack(Items.graphite, 3);
        Blocks.multiPress.itemCapacity = 21;

        ((GenericCrafter) Blocks.plastaniumCompressor).outputItem = new ItemStack(Items.plastanium, 3);
        ((GenericCrafter) Blocks.plastaniumCompressor).craftTime = 90;
        Blocks.plastaniumCompressor.itemCapacity = 12;
        ci = Blocks.plastaniumCompressor.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 4;
        ConsumeLiquid cl = Blocks.plastaniumCompressor.findConsumer(c -> c instanceof ConsumeLiquid);
        cl.amount = 0.5f;
        Blocks.plastaniumCompressor.consumePower(6);

        ((GenericCrafter) Blocks.phaseWeaver).outputItem = new ItemStack(Items.phaseFabric, 3);
        ((GenericCrafter) Blocks.phaseWeaver).craftTime = 180;
        Blocks.phaseWeaver.itemCapacity = 40;
        ci = Blocks.phaseWeaver.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 8;
        ci.items[1].amount = 20;
        Blocks.phaseWeaver.consumePower(10);

        ((GenericCrafter) Blocks.kiln).outputItem = new ItemStack(Items.metaglass, 3);
        ((GenericCrafter) Blocks.kiln).craftTime = 45;
        Blocks.kiln.itemCapacity = 12;
        ci = Blocks.kiln.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 2;
        ci.items[1].amount = 2;
        Blocks.kiln.consumePower(1.2f);

        ((GenericCrafter) Blocks.surgeSmelter).outputItem = new ItemStack(Items.surgeAlloy, 3);
        ((GenericCrafter) Blocks.surgeSmelter).craftTime = 112.5f;
        Blocks.surgeSmelter.itemCapacity = 24;
        ci = Blocks.surgeSmelter.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 6;
        ci.items[1].amount = 8;
        ci.items[2].amount = 4;
        ci.items[3].amount = 6;
        Blocks.surgeSmelter.consumePower(8);

        ((GenericCrafter) Blocks.cryofluidMixer).outputLiquid = new LiquidStack(Liquids.cryofluid, 18f / 60);
        Blocks.cryofluidMixer.liquidCapacity = 36;

        ((GenericCrafter) Blocks.pyratiteMixer).outputItem = new ItemStack(Items.pyratite, 3);
        ((GenericCrafter) Blocks.pyratiteMixer).craftTime = 120;
        Blocks.pyratiteMixer.itemCapacity = 12;
        ci = Blocks.pyratiteMixer.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 2;
        ci.items[1].amount = 4;
        ci.items[2].amount = 4;
        Blocks.pyratiteMixer.consumePower(0.4f);

        ((GenericCrafter) Blocks.blastMixer).outputItem = new ItemStack(Items.blastCompound, 3);
        ((GenericCrafter) Blocks.blastMixer).craftTime = 120;
        Blocks.blastMixer.itemCapacity = 12;
        ci = Blocks.blastMixer.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 2;
        ci.items[1].amount = 2;
        Blocks.blastMixer.consumePower(0.8f);

        ((GenericCrafter) Blocks.melter).outputLiquid = new LiquidStack(Liquids.slag, 18f / 60);
        Blocks.melter.liquidCapacity = 15;

        ((Separator) Blocks.separator).results = ItemStack.with(
                Items.copper, 3,
                Items.lead, 2,
                Items.graphite, 3,
                Items.titanium, 4
        );

        ((Separator) Blocks.disassembler).results = ItemStack.with(
                Items.sand, 2,
                Items.graphite, 1,
                Items.titanium, 3,
                Items.thorium, 4
        );

        ((GenericCrafter) Blocks.sporePress).outputLiquid = new LiquidStack(Liquids.oil, 27f / 60);
        Blocks.sporePress.liquidCapacity = 90;

        ((GenericCrafter) Blocks.pulverizer).outputItem = new ItemStack(Items.sand, 9);
        ((GenericCrafter) Blocks.pulverizer).craftTime = 60;
        Blocks.pulverizer.itemCapacity = 18;
        ci = Blocks.pulverizer.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 2;
        Blocks.pulverizer.consumePower(1f);

        ((GenericCrafter) Blocks.coalCentrifuge).outputItem = new ItemStack(Items.coal, 3);
        ((GenericCrafter) Blocks.coalCentrifuge).craftTime = 45;
        Blocks.coalCentrifuge.itemCapacity = 18;
        cl = Blocks.coalCentrifuge.findConsumer(c -> c instanceof ConsumeLiquid);
        cl.amount = 0.2f;
        Blocks.coalCentrifuge.consumePower(1.4f);

        ((GenericCrafter) Blocks.siliconArcFurnace).outputItem = new ItemStack(Items.silicon, 6);

        ((GenericCrafter) Blocks.electrolyzer).outputLiquids =
                LiquidStack.with(Liquids.ozone, 6f / 60, Liquids.hydrogen, 9f / 60);

        ((HeatCrafter) Blocks.atmosphericConcentrator).outputLiquid = new LiquidStack(Liquids.nitrogen, 6f / 60f);

        ((HeatProducer) Blocks.oxidationChamber).craftTime = 40f * 2;

        ((HeatProducer) Blocks.electricHeater).heatOutput = 4;

        ((HeatProducer) Blocks.slagHeater).heatOutput = 10;

        ((HeatCrafter) Blocks.carbideCrucible).outputItem = new ItemStack(Items.carbide, 3);
        ((HeatCrafter) Blocks.carbideCrucible).craftTime = 90 * 2.25f;
        Blocks.carbideCrucible.itemCapacity = 21;
        ci = Blocks.carbideCrucible.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 4;
        ci.items[1].amount = 6;

        ((HeatCrafter) Blocks.surgeCrucible).outputItem = new ItemStack(Items.surgeAlloy, 3);
        ((HeatCrafter) Blocks.surgeCrucible).craftTime = 90f * 3;
        ((HeatCrafter) Blocks.surgeCrucible).heatRequirement = 20;
        Blocks.surgeCrucible.itemCapacity = 21;
        ci = Blocks.surgeCrucible.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 6;
        cl = Blocks.surgeCrucible.findConsumer(c -> c instanceof ConsumeLiquid);
        cl.amount = 1f;
        Blocks.surgeCrucible.consumePower(4f);

        ((HeatCrafter) Blocks.cyanogenSynthesizer).outputLiquid = new LiquidStack(Liquids.cyanogen, 3f / 40);
        Blocks.cyanogenSynthesizer.liquidCapacity = 120;

        ((HeatCrafter) Blocks.phaseSynthesizer).outputItem = new ItemStack(Items.phaseFabric, 3);
        ((HeatCrafter) Blocks.phaseSynthesizer).craftTime = 90f * 2;
        ((HeatCrafter) Blocks.phaseSynthesizer).heatRequirement = 16;
        Blocks.phaseSynthesizer.itemCapacity = 42;
        ci = Blocks.phaseSynthesizer.findConsumer(c -> c instanceof ConsumeItems);
        ci.items[0].amount = 4;
        ci.items[1].amount = 12;
        cl = Blocks.phaseSynthesizer.findConsumer(c -> c instanceof ConsumeLiquid);
        cl.amount = 4f / 60;
        Blocks.phaseSynthesizer.consumePower(16);
    }
}