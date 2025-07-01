package Commonplace.Loader.Override;

import Commonplace.Entities.BulletType.StatusBulletType;
import Commonplace.Entities.BulletType.ProtectKillerBulletType;
import Commonplace.Entities.Shoot.ContinuesShoot;
import Commonplace.Loader.DefaultContent.StatusEffects2;
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.ShootSpread;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.ShieldWall;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PointDefenseTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret;
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
                speed = 0.25f;
                lifetime = 60;
                width = height = 6;
                damage = b.damage * 0.5f;
                splashDamage = b.splashDamage * 0.3f;
                splashDamageRadius = b.splashDamageRadius * 0.75f;
            }};
        });

        ((ItemTurret) Blocks.scatter).range = 280;
        ((ItemTurret) Blocks.scatter).ammoTypes.each((i, b) -> {
            b.speed = 6;
            b.pierce = true;
            b.pierceCap = 3;
        });
        ((ItemTurret) Blocks.scatter).limitRange(2);

        ((ItemTurret) Blocks.salvo).ammoTypes.each((i, b) -> b.damage *= 2f);

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

        ((ItemTurret) Blocks.fuse).range = 160;
        ((ItemTurret) Blocks.fuse).shoot = new ShootSpread(7, 10);
        ((ItemTurret) Blocks.fuse).ammoTypes.each((i, b) -> {
            ((ShrapnelBulletType) b).length = 150;
            b.damage /= 1.2f;
        });

        Blocks.swarmer.itemCapacity = 100;
        ((ItemTurret) Blocks.swarmer).reload = 60;
        ((ItemTurret) Blocks.swarmer).maxAmmo = 100;
        ((ItemTurret) Blocks.swarmer).shoot = new ContinuesShoot(0.9f, 24).shots(4).shootDelay(2).barrels(
                -4, -1.25f, 0,
                0, 0, 0,
                4, -1.25f, 0
        );

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
        ((ItemTurret) Blocks.foreshadow).limitRange(0);

        ((PowerTurret) Blocks.arc).shootType.damage = 26;

        ((PowerTurret) Blocks.lancer).range = 205;
        ((PowerTurret) Blocks.lancer).shootType.damage = 160;
        ((PowerTurret) Blocks.lancer).shootType.statusDuration = 25;
        ((PowerTurret) Blocks.lancer).shootType.status = StatusEffects.slow;
        ((LaserBulletType) ((PowerTurret) Blocks.lancer).shootType).length = 200;

        Blocks.parallax.scaledHealth = 300;
        ((TractorBeamTurret) Blocks.parallax).force = 24;
        ((TractorBeamTurret) Blocks.parallax).range = 300;
        ((TractorBeamTurret) Blocks.parallax).damage = 0.5f;
        ((TractorBeamTurret) Blocks.parallax).statusDuration = 5;
        ((TractorBeamTurret) Blocks.parallax).status = StatusEffects2.torn;

        ((PointDefenseTurret) Blocks.segment).retargetTime = 3;

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