package Floor.FType.FDialog;

import Floor.FEntities.FBulletType.LimitBulletType;
import arc.Core;
import arc.func.*;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Strings;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static Floor.FType.FDialog.ProjectUtils.*;
import static mindustry.Vars.ui;

public class BulletDialog extends BaseDialog implements EffectTableGetter {
    protected int boost;
    protected Cons<BulletType> apply;
    protected Cons<Float> heavyApply;
    protected String newType = "bullet";
    //global
    protected LimitBulletType bullet;
    protected BulletDialog dialog;
    protected float bulletHeavy = 0;
    protected float heavy = 0.5f;
    protected Table typeOn;
    protected Table baseOn;
    protected Table effectOn;
    protected Table flo;
    protected static final Seq<String> types = new Seq<>(new String[]{
            "bullet", "laser", "lightning", "continuousF", "continuousL", "point", "rail"
    });

    protected static String dia = "bullet";
    protected Runnable reb = this::rebuildBase;
    protected Runnable ret = this::rebuildType;
    protected Boolf<String> levUser = str -> couldUse(str, findVal(str));
    protected Boolp hevUser = () -> boost * (heavy + bullet.fragBullets * bulletHeavy) <= freeSize;

    public BulletDialog(Prov<BulletType> def, Cons<Float> heavyApply, Cons<BulletType> apply, String title, Intp boost) {
        super(title);
        shown(this::loadBase);
        hidden(this::heavyOut);

        this.apply = apply;
        this.boost = boost.get();
        this.heavyApply = heavyApply;
        this.bullet = def.get() instanceof LimitBulletType l ? l : new LimitBulletType();
        if (bullet.fragBullet != null) {
            updateDialog(true);
        }
        updateHeavy();

        buttons.button("@back", Icon.left, () -> {
            LimitBulletType b = this.bullet;
            bullet = def.get() instanceof LimitBulletType l ? l : new LimitBulletType();
            updateHeavy();
            if (this.boost * (heavy + bullet.fragBullets * bulletHeavy) > freeSize) {
                ui.showInfo(Core.bundle.get("@tooHeavy"));
                this.bullet = b;
            } else {
                apply.get(bullet);
                heavyApply.get(heavy + bullet.fragBullets * bulletHeavy);
                hide();
            }
        }).width(210f);
        buttons.button(Core.bundle.get("@apply"), Icon.right, () -> {
            if (this.boost * (heavy + bullet.fragBullets * bulletHeavy) <= freeSize) {
                if (getHeavy("percent", findVal("percent")) > 0) {
                    bullet.havePercent = true;
                }
                if (getHeavy("emp", findVal("emp")) > 0) {
                    bullet.haveEmp = true;
                }
                if (newType.equals("lightning")) {
                    if (bullet.havePercent) {
                        bullet.lightningType = new LimitBulletType() {{
                            havePercent = true;
                            percent = bullet.percent;
                            lightningDamage = bullet.lightningDamage;
                        }};
                    }
                }
                apply.get(bullet);
                heavyApply.get(heavy + bullet.fragBullets * bulletHeavy);
                hide();
            } else {
                ui.showInfo(Core.bundle.get("@tooHeavy"));
            }
        }).width(210f);
        buttons.button(Core.bundle.get("@setZero"), Icon.defense, () -> {
            bullet.setZero();
            updateHeavy();
            loadBase();
        }).width(210f);
    }

    public void loadBase() {
        cont.clear();
        cont.pane(this::Front).width(1400);
    }

    public void Front(Table table) {
        table.table(t -> {
            t.add(Core.bundle.get("dialog.bullet-type") + " : ").size(25).left().width(100);
            t.label(() -> Core.bundle.format("dialog." + newType)).size(25).left().width(100).pad(1);
            t.button(b -> {
                b.image(Icon.down).size(25);

                b.clicked(() -> createSelectDialog(b, (tb, hide) -> {
                    tb.top();
                    for (String name : types) {
                        tb.button(Core.bundle.get("dialog." + name), () -> {
                            newType = name;
                            bullet.type = name;
                            hide.run();
                            updateHeavy();

                            rebuildType();

                        }).width(100);
                        tb.row();
                    }
                }));
            }, Styles.logici, () -> {
            }).size(25).left().pad(5);
            t.row();
            t.label(() -> Core.bundle.get("@heavyUse") + ": " + boost *
                    (heavy + bullet.fragBullets * bulletHeavy) + " / " + freeSize).size(25).left().width(100).pad(5);
        }).width(1400);
        table.row();
        table.pane(t -> {
            t.setBackground(Tex.buttonEdge1);
            baseOn = t;
        }).width(1400);
        rebuildBase();
        table.row();
        table.pane(t -> {
            t.setBackground(Tex.buttonEdge1);
            typeOn = t;
        }).width(1400);
        rebuildType();
    }

    public void rebuildBase() {
        baseOn.clear();
        baseOn.setBackground(Tex.underlineOver);
        createLevDialog(baseOn, dia, "bulletBase", "damage", bullet.damage,
                f -> bullet.damage = f, reb, this::updateHeavy, levUser, hevUser);
        createNumberDialogWithLimit(baseOn, dia, "fragAngle", bullet.fragAngle,
                12, -12, f -> bullet.fragAngle = f, reb);
        createLevDialog(baseOn, dia, "frags", "fragBullets", bullet.fragBullets,
                f -> bullet.fragBullets = (int) (f + 0), reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        baseOn.label(() -> Core.bundle.get("dialog.bullet.writeFrag") + "->").width(100);
        if (dialog == null) {
            baseOn.button(Icon.add, () -> {
                updateDialog(true);
                rebuildBase();
            }).pad(5);
        } else {
            baseOn.button(Icon.pencil, () -> dialog.show()).pad(5);
            baseOn.button(Icon.trash, () -> {
                updateDialog(false);
                rebuildBase();
            }).pad(5);
        }
        createNumberDialogWithLimit(baseOn, dia, "lightningAngle", bullet.lightningAngle,
                360, 0, f -> bullet.lightningAngle = f, reb);
        createNumberDialogWithLimit(baseOn, dia, "lightningCone", bullet.lightningCone,
                360, 0, f -> bullet.lightningCone = f, reb);
        baseOn.row();
        createLevDialog(baseOn, dia, "lightning", "lightningLength", bullet.lightningLength,
                f -> bullet.lightningLength = (int) (f + 0), reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "lightning", "lightningLengthRand", bullet.lightningLengthRand,
                f -> bullet.lightningLengthRand = (int) (f + 0), reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "lightning", "lightningDamage", bullet.lightningDamage,
                f -> bullet.lightningDamage = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createLevDialog(baseOn, dia, "lightning", "lightnings", bullet.lightning,
                f -> bullet.lightning = (int) (f + 0), reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "percent", "percent", bullet.percent,
                f -> bullet.percent = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "emp", "empDamage", bullet.empDamage,
                f -> bullet.empDamage = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createLevDialog(baseOn, dia, "emp", "radius", bullet.radius,
                f -> bullet.radius = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "emp", "timeDuration", bullet.timeDuration,
                f -> bullet.timeDuration = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "emp", "timeIncrease", bullet.timeIncrease,
                f -> bullet.timeIncrease = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createLevDialog(baseOn, dia, "emp", "powerDamageScl", bullet.powerDamageScl,
                f -> bullet.powerDamageScl = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "emp", "powerSclDecrease", bullet.powerSclDecrease,
                f -> bullet.powerSclDecrease = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "emp", "unitDamageScl", bullet.unitDamageScl,
                f -> bullet.unitDamageScl = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        if (!(bullet.hitPowerEffect instanceof MultiEffect)) {
            bullet.hitPowerEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "hitPowerEffect", bullet.hitPowerEffect);
        if (!(bullet.chainEffect instanceof MultiEffect)) {
            bullet.chainEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "chainEffect", bullet.chainEffect);
        if (!(bullet.applyEffect instanceof MultiEffect)) {
            bullet.applyEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "applyEffect", bullet.applyEffect);
        baseOn.clear();
        createBooleanDialog(baseOn, dia, "hitUnits", bullet.hitUnits, b -> bullet.hitUnits = b, this::rebuildBase);
        createColorDialog(baseOn, dia, "mixColorFrom", bullet.mixColorFrom, b -> bullet.mixColorFrom = b, this::rebuildBase);
        createColorDialog(baseOn, dia, "mixColorTo", bullet.mixColorTo, b -> bullet.mixColorTo = b, this::rebuildBase);
        baseOn.row();
        createNumberDialog(baseOn, dia, "lifetime", bullet.lifetime, f -> {
            bullet.lifetime = Math.max(0.001f, f);
            bullet.speed = bullet.rangeOverride / bullet.lifetime;
        }, reb);
        createNumberDialog(baseOn, dia, "speed", bullet.speed, f -> {
            bullet.speed = Math.max(0.001f, f);
            bullet.lifetime = bullet.rangeOverride / bullet.speed;
        }, reb);
        createNumberDialogWithLimit(baseOn, dia, "hitSize", bullet.hitSize,
                360, 0, f -> bullet.hitSize = f, reb);
        baseOn.row();
        createNumberDialogWithLimit(baseOn, dia, "drawSize", bullet.drawSize,
                360, 0, f -> bullet.drawSize = f, reb);
        createNumberDialogWithLimit(baseOn, dia, "drag", bullet.drag,
                360, 0, f -> bullet.drag = f, reb);
        createNumberDialogWithLimit(baseOn, dia, "pierceCap", bullet.pierceCap,
                360, 0, f -> bullet.pierceCap = (int) (f + 0), reb);
        baseOn.row();
        createNumberDialogWithLimit(baseOn, dia, "pierceDamageFactor", bullet.pierceDamageFactor,
                360, 0, f -> bullet.pierceDamageFactor = f, reb);
        createNumberDialogWithLimit(baseOn, dia, "optimalLifeFract", bullet.optimalLifeFract,
                360, 0, f -> bullet.optimalLifeFract = f, reb);
        createNumberDialogWithLimit(baseOn, dia, "layer", bullet.layer,
                360, 0, f -> bullet.layer = f, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "pierce", bullet.pierce,
                b -> bullet.pierce = b, reb);
        createBooleanDialog(baseOn, dia, "pierceBuilding", bullet.pierceBuilding,
                b -> bullet.pierceBuilding = b, reb);
        createBooleanDialog(baseOn, dia, "removeAfterPierce", bullet.removeAfterPierce,
                b -> bullet.removeAfterPierce = b, reb);
        baseOn.row();
        if (!(bullet.shootEffect instanceof MultiEffect)) {
            bullet.shootEffect = new MultiEffect();
        }
        if (!(bullet.despawnEffect instanceof MultiEffect)) {
            bullet.despawnEffect = new MultiEffect();
        }
        if (!(bullet.hitEffect instanceof MultiEffect)) {
            bullet.hitEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "shootEffect", bullet.shootEffect);
        createEffectList(baseOn, this, dia, "despawnEffect", bullet.despawnEffect);
        createEffectList(baseOn, this, dia, "hitEffect", bullet.hitEffect);
        baseOn.row();
        if (!(bullet.chargeEffect instanceof MultiEffect)) {
            bullet.chargeEffect = new MultiEffect();
        }
        if (!(bullet.smokeEffect instanceof MultiEffect)) {
            bullet.smokeEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "chargeEffect", bullet.chargeEffect);
        createEffectList(baseOn, this, dia, "smokeEffect", bullet.smokeEffect);
        createSoundSelect(baseOn, dia, "hitSound", s -> bullet.hitSound = s);
        baseOn.row();
        createNumberDialog(baseOn, dia, "hitSoundPitch", bullet.hitSoundPitch,
                f -> bullet.hitSoundPitch = f, reb);
        createNumberDialog(baseOn, dia, "hitSoundVolume", bullet.hitSoundVolume,
                f -> bullet.hitSoundVolume = f, reb);
        createSoundSelect(baseOn, dia, "despawnSound", s -> bullet.despawnSound = s);
        baseOn.row();
        createNumberDialog(baseOn, dia, "inaccuracy", bullet.inaccuracy,
                f -> bullet.inaccuracy = f, reb);
        createNumberDialog(baseOn, dia, "ammoMultiplier", bullet.ammoMultiplier,
                f -> bullet.ammoMultiplier = f, reb);
        createNumberDialog(baseOn, dia, "reloadMultiplier", bullet.reloadMultiplier,
                f -> bullet.reloadMultiplier = f, reb);
        baseOn.row();
        createNumberDialog(baseOn, dia, "buildingDamageMultiplier", bullet.buildingDamageMultiplier,
                f -> bullet.buildingDamageMultiplier = f, reb);
        createNumberDialog(baseOn, dia, "recoil", bullet.recoil,
                f -> bullet.recoil = f, reb);
        createLevDialog(baseOn, dia, "splash", "splashDamage", bullet.splashDamage,
                f -> bullet.splashDamage = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "killShooter", bullet.killShooter,
                b -> bullet.killShooter = b, reb);
        createBooleanDialog(baseOn, dia, "instantDisappear", bullet.instantDisappear,
                b -> bullet.instantDisappear = b, reb);
        createBooleanDialog(baseOn, dia, "scaledSplashDamage", bullet.scaledSplashDamage,
                b -> bullet.scaledSplashDamage = b, reb);
        baseOn.row();
        createNumberDialog(baseOn, dia, "knockback", bullet.knockback,
                f -> bullet.knockback = f, reb);
        createNumberDialog(baseOn, dia, "createChance", bullet.createChance,
                f -> bullet.createChance = f, reb);
        createNumberDialog(baseOn, dia, "rangeChange", bullet.rangeChange,
                f -> bullet.rangeChange = f, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "impact", bullet.impact,
                b -> bullet.impact = b, reb);
        createBooleanDialog(baseOn, dia, "collidesTiles", bullet.collidesTiles,
                b -> bullet.collidesTiles = b, reb);
        createBooleanDialog(baseOn, dia, "collidesTeam", bullet.collidesTeam,
                b -> bullet.collidesTeam = b, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "collidesAir", bullet.collidesAir,
                b -> bullet.collidesAir = b, reb);
        createBooleanDialog(baseOn, dia, "collidesGround", bullet.collidesGround,
                b -> bullet.collidesGround = b, reb);
        createBooleanDialog(baseOn, dia, "collides", bullet.collides,
                b -> bullet.collides = b, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "collideFloor", bullet.collideFloor,
                b -> bullet.collideFloor = b, reb);
        createBooleanDialog(baseOn, dia, "collideTerrain", bullet.collideTerrain,
                b -> bullet.collideTerrain = b, reb);
        createBooleanDialog(baseOn, dia, "keepVelocity", bullet.keepVelocity,
                b -> bullet.keepVelocity = b, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "scaleLife", bullet.scaleLife,
                b -> bullet.scaleLife = b, reb);
        createBooleanDialog(baseOn, dia, "hittable", bullet.hittable,
                b -> bullet.hittable = b, reb);
        createBooleanDialog(baseOn, dia, "reflectable", bullet.reflectable,
                b -> bullet.reflectable = b, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "absorbable", bullet.absorbable,
                b -> bullet.absorbable = b, reb);
        createBooleanDialog(baseOn, dia, "backMove", bullet.backMove,
                b -> bullet.backMove = b, reb);
        createBooleanDialog(baseOn, dia, "ignoreSpawnAngle", bullet.ignoreSpawnAngle,
                b -> bullet.ignoreSpawnAngle = b, reb);
        baseOn.row();
        createLevDialog(baseOn, dia, "bulletBase", "rangeOverride", bullet.rangeOverride,
                f -> bullet.rangeOverride = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "bulletBase", "healPercent", bullet.healPercent,
                f -> bullet.healPercent = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "bulletBase", "healAmount", bullet.healAmount,
                f -> bullet.healAmount = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "fragOnHit", bullet.fragOnHit,
                b -> bullet.fragOnHit = b, reb);
        createBooleanDialog(baseOn, dia, "fragOnAbsorb", bullet.fragOnAbsorb,
                b -> bullet.fragOnAbsorb = b, reb);
        createBooleanDialog(baseOn, dia, "pierceArmor", bullet.pierceArmor,
                b -> bullet.pierceArmor = b, reb);
        baseOn.row();
        createNumberDialog(baseOn, dia, "hitShake", bullet.hitShake,
                f -> bullet.hitShake = f, reb);
        createNumberDialog(baseOn, dia, "despawnShake", bullet.despawnShake,
                f -> bullet.despawnShake = f, reb);
        createNumberDialog(baseOn, dia, "fragRandomSpread", bullet.fragRandomSpread,
                f -> bullet.fragRandomSpread = f, reb);
        baseOn.row();
        createNumberDialog(baseOn, dia, "fragSpread", bullet.fragSpread,
                f -> bullet.fragSpread = f, reb);
        createNumberDialog(baseOn, dia, "fragAngle", bullet.fragAngle,
                f -> bullet.fragAngle = f, reb);
        createNumberDialog(baseOn, dia, "fragRandomSpread", bullet.fragRandomSpread,
                f -> bullet.fragRandomSpread = f, reb);
        baseOn.row();
        createColorDialog(baseOn, dia, "hitColor", bullet.hitColor, c -> bullet.hitColor = c, reb);
        createColorDialog(baseOn, dia, "healColor", bullet.healColor, c -> bullet.healColor = c, reb);
        if (!(bullet.healEffect instanceof MultiEffect)) {
            bullet.healEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "healEffect", bullet.healEffect);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "faceOutwards", bullet.faceOutwards,
                b -> bullet.faceOutwards = b, reb);
        createBooleanDialog(baseOn, dia, "trailRotation", bullet.trailRotation,
                b -> bullet.trailRotation = b, reb);
        createBooleanLevDialog(baseOn, dia, "splashDamagePierce", bullet.splashDamagePierce,
                b -> bullet.splashDamagePierce = b, reb, () -> levUser.get("pierce"), hevUser);
        baseOn.row();
        createPartsDialog(baseOn, dia, "parts", bullet.parts);
        createColorDialog(baseOn, dia, "trailColor", bullet.trailColor, c -> bullet.trailColor = c, reb);
        createInterpolSelect(baseOn, dia, "trailInterp", i -> bullet.trailInterp = i);
        baseOn.row();
        createNumberDialog(baseOn, dia, "trailChance", bullet.trailChance,
                f -> bullet.trailChance = f, reb);
        createNumberDialog(baseOn, dia, "trailInterval", bullet.trailInterval,
                f -> bullet.trailInterval = f, reb);
        createNumberDialog(baseOn, dia, "trailParam", bullet.trailParam,
                f -> bullet.trailParam = f, reb);
        baseOn.row();
        createNumberDialog(baseOn, dia, "trailLength", bullet.trailLength,
                f -> bullet.trailLength = (int) (f + 0), reb);
        createNumberDialog(baseOn, dia, "trailWidth", bullet.trailWidth,
                f -> bullet.trailWidth = f, reb);
        createNumberDialog(baseOn, dia, "trailSinMag", bullet.trailSinMag,
                f -> bullet.trailSinMag = f, reb);
        baseOn.row();
        createNumberDialog(baseOn, dia, "trailSinScl", bullet.trailSinScl,
                f -> bullet.trailSinScl = f, reb);
        if (!(bullet.trailEffect instanceof MultiEffect)) {
            bullet.trailEffect = new MultiEffect();
        }
        createEffectList(baseOn, this, dia, "trailEffect", bullet.trailEffect);
        createLevDialog(baseOn, dia, "splash", "splashDamageRadius", bullet.splashDamageRadius,
                f -> bullet.splashDamageRadius = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createNumberDialog(baseOn, dia, "homingPower", bullet.homingPower,
                f -> bullet.homingPower = f, reb);
        createNumberDialog(baseOn, dia, "homingRange", bullet.homingRange,
                f -> bullet.homingRange = f, reb);
        createNumberDialog(baseOn, dia, "homingDelay", bullet.homingDelay,
                f -> bullet.homingDelay = f, reb);
        baseOn.row();
        createLevDialog(baseOn, dia, "suppression", "suppressionRange", bullet.suppressionRange,
                f -> bullet.suppressionRange = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "suppression", "suppressionDuration", bullet.suppressionDuration,
                f -> bullet.suppressionDuration = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "suppression", "suppressionEffectChance", bullet.suppressionEffectChance,
                f -> bullet.suppressionEffectChance = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createColorDialog(baseOn, dia, "lightningColor", bullet.lightningColor, c -> bullet.lightningColor = c, reb);
        createNumberDialog(baseOn, dia, "weaveScale", bullet.weaveScale,
                f -> bullet.weaveScale = f, reb);
        createNumberDialog(baseOn, dia, "weaveMag", bullet.weaveMag,
                f -> bullet.weaveMag = f, reb);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "weaveRandom", bullet.weaveRandom,
                b -> bullet.weaveRandom = b, reb);
        createLiquidSelect(baseOn, dia, "puddleLiquid", l -> bullet.puddleLiquid = l);
        createColorDialog(baseOn, dia, "lightColor", bullet.lightColor,
                c -> bullet.lightColor = c, reb);
        baseOn.row();
        createLevDialog(baseOn, dia, "puddles", "puddles", bullet.puddles,
                f -> bullet.puddles = (int) (f + 0), reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "puddles", "puddleRange", bullet.puddleRange,
                f -> bullet.puddleRange = f, reb, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "puddles", "puddleAmount", bullet.puddleAmount,
                f -> bullet.puddleAmount = f, reb, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createNumberDialog(baseOn, dia, "lightRadius", bullet.lightRadius,
                f -> bullet.lightRadius = f, reb);
        createNumberDialog(baseOn, dia, "lightOpacity", bullet.lightOpacity,
                f -> bullet.lightOpacity = f, reb);
    }

    public void rebuildType() {
        typeOn.clear();
        typeOn.setBackground(Tex.buttonEdge1);
        switch (newType) {
            case "bullet" -> {
                createNumberDialog(baseOn, dia, "width", bullet.width,
                        f -> bullet.width = f, ret);
                createNumberDialog(baseOn, dia, "height", bullet.height,
                        f -> bullet.height = f, ret);
                createNumberDialog(baseOn, dia, "shrinkX", bullet.shrinkX,
                        f -> bullet.shrinkX = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "shrinkY", bullet.shrinkY,
                        f -> bullet.shrinkY = f, ret);
                createNumberDialog(baseOn, dia, "spin", bullet.spin,
                        f -> bullet.spin = f, ret);
                createNumberDialog(baseOn, dia, "rotationOffset", bullet.rotationOffset,
                        f -> bullet.rotationOffset = f, ret);
                typeOn.row();
                createColorDialog(baseOn, dia, "backColor", bullet.backColor, c -> bullet.backColor = c, ret);
                createColorDialog(baseOn, dia, "frontColor", bullet.frontColor, c -> bullet.frontColor = c, ret);
                createInterpolSelect(typeOn, dia, "shrinkInterp", i -> bullet.shrinkInterp = i);
                typeOn.row();
                createColorDialog(baseOn, dia, "mixColorFrom", bullet.mixColorFrom, c -> bullet.mixColorFrom = c, ret);
                createColorDialog(baseOn, dia, "mixColorTo", bullet.mixColorTo, c -> bullet.mixColorTo = c, ret);
            }
            case "laser" -> {
                createColorDialogList(typeOn, dia, "colors", bullet.colors,
                        c -> bullet.colors = c);
                if (!(bullet.laserEffect instanceof MultiEffect)) {
                    bullet.laserEffect = new MultiEffect();
                }
                createEffectList(typeOn, this, dia, "", bullet.laserEffect);
                createBooleanDialog(typeOn, dia, "largeHit", bullet.largeHit,
                        b -> bullet.largeHit = b, ret);
                typeOn.row();
                createLevDialog(baseOn, dia, "bulletBase", "laserLength", bullet.laserLength,
                        f -> bullet.laserLength = f, ret, this::updateHeavy, levUser, hevUser);
                createNumberDialog(baseOn, dia, "width", bullet.width,
                        f -> bullet.width = f, ret);
                createNumberDialog(baseOn, dia, "lengthFalloff", bullet.lengthFalloff,
                        f -> bullet.lengthFalloff = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "sideLength", bullet.sideLength,
                        f -> bullet.sideLength = f, ret);
                createNumberDialog(baseOn, dia, "sideWidth", bullet.sideWidth,
                        f -> bullet.sideWidth = f, ret);
                createNumberDialog(baseOn, dia, "sideAngle", bullet.sideAngle,
                        f -> bullet.sideAngle = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "lightningSpacing", bullet.lightningSpacing,
                        f -> bullet.lightningSpacing = f, ret);
                createNumberDialog(baseOn, dia, "lightningDelay", bullet.lightningDelay,
                        f -> bullet.lightningDelay = f, ret);
                createNumberDialog(baseOn, dia, "lightningAngleRand", bullet.lightningAngleRand,
                        f -> bullet.lightningAngleRand = f, ret);
            }
            case "lightning" -> {
                createColorDialog(baseOn, dia, "bulletLightningColor", bullet.bulletLightningColor, c -> bullet.bulletLightningColor = c, ret);
                createLevDialog(baseOn, dia, "bulletBase", "bulletLightningLength", bullet.bulletLightningLength,
                        f -> bullet.bulletLightningLength = (int) (f + 0), ret, this::updateHeavy, levUser, hevUser);
                createLevDialog(baseOn, dia, "bulletBase", "bulletLightningLengthRand", bullet.bulletLightningLengthRand,
                        f -> bullet.bulletLightningLengthRand = (int) (f + 0), ret, this::updateHeavy, levUser, hevUser);
            }
            case "continuousF" -> {
                createBooleanDialog(typeOn, dia, "drawFlare", bullet.drawFlare,
                        b -> bullet.drawFlare = b, ret);
                createBooleanDialog(typeOn, dia, "rotateFlare", bullet.rotateFlare,
                        b -> bullet.rotateFlare = b, ret);
                createColorDialog(typeOn, dia, "flareColor", bullet.flareColor,
                        c -> bullet.flareColor = c, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "lightStroke", bullet.lightStroke,
                        f -> bullet.lightStroke = f, ret);
                createNumberDialog(baseOn, dia, "width", bullet.width,
                        f -> bullet.width = f, ret);
                createNumberDialog(baseOn, dia, "oscScl", bullet.oscScl,
                        f -> bullet.oscScl = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "divisions", bullet.divisions,
                        f -> bullet.divisions = (int) (f + 0), ret);
                createNumberDialog(baseOn, dia, "flareWidth", bullet.flareWidth,
                        f -> bullet.flareWidth = f, ret);
                createNumberDialog(baseOn, dia, "oscMag", bullet.oscMag,
                        f -> bullet.oscMag = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "flareInnerScl", bullet.flareInnerScl,
                        f -> bullet.flareInnerScl = f, ret);
                createLevDialog(baseOn, dia, "bulletBase", "flareLength", bullet.flareLength,
                        f -> bullet.flareLength = f, ret, this::updateHeavy, levUser, hevUser);
                createNumberDialog(baseOn, dia, "flareInnerLenScl", bullet.flareInnerLenScl,
                        f -> bullet.flareInnerLenScl = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "flareLayer", bullet.flareLayer,
                        f -> bullet.flareLayer = f, ret);
                createNumberDialog(baseOn, dia, "flareRotSpeed", bullet.flareRotSpeed,
                        f -> bullet.flareRotSpeed = f, ret);
                createInterpolSelect(typeOn, dia, "lengthInterp", i -> bullet.lengthInterp = i);
                typeOn.row();
                createColorDialogList(typeOn, dia, "colors", bullet.colors,
                        c -> bullet.colors = c);
                typeOn.table(t -> {
                    t.label(() -> Core.bundle.get("dialog.bullet.lengthWidthPans")).width(100);
                    t.button(Icon.pencil, () -> {
                        float[] n = new float[bullet.lengthWidthPans.length];
                        System.arraycopy(bullet.lengthWidthPans, 0, n, 0, bullet.lengthWidthPans.length);
                        BaseDialog bd = new BaseDialog("");
                        bd.buttons.button("@back", Icon.left, () -> {
                            bullet.lengthWidthPans = n;
                            bd.hide();
                        }).width(100);
                        bd.buttons.button(Core.bundle.get("@apply"), Icon.right, bd::hide).width(100);
                        bd.buttons.button(Core.bundle.get("@add"), Icon.add, () -> {
                            float[] nn = new float[bullet.lengthWidthPans.length + 3];
                            System.arraycopy(bullet.lengthWidthPans, 0, nn, 0, bullet.lengthWidthPans.length);
                            nn[nn.length - 1] = nn[nn.length - 2] = nn[nn.length - 3] = 0;
                            bullet.lengthWidthPans = nn;
                            rebuildFloatList(bullet.lengthWidthPans, f -> bullet.lengthWidthPans = f);
                        }).width(100);
                        bd.cont.pane(tb -> flo = tb).width(1400);
                        rebuildFloatList(bullet.lengthWidthPans, f -> bullet.lengthWidthPans = f);
                        bd.show();
                    });
                });
            }
            case "continuousL" -> {
                createNumberDialog(baseOn, dia, "fadeTime", bullet.fadeTime,
                        f -> bullet.fadeTime = f, ret);
                createNumberDialog(baseOn, dia, "lightStroke", bullet.lightStroke,
                        f -> bullet.lightStroke = f, ret);
                createNumberDialog(baseOn, dia, "divisions", bullet.divisions,
                        f -> bullet.divisions = (int) (f + 0), ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "strokeFrom", bullet.strokeFrom,
                        f -> bullet.strokeFrom = f, ret);
                createNumberDialog(baseOn, dia, "strokeTo", bullet.strokeTo,
                        f -> bullet.strokeTo = f, ret);
                createNumberDialog(baseOn, dia, "pointyScaling", bullet.pointyScaling,
                        f -> bullet.pointyScaling = (int) (f + 0), ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "backLength", bullet.backLength,
                        f -> bullet.backLength = f, ret);
                createNumberDialog(baseOn, dia, "frontLength", bullet.frontLength,
                        f -> bullet.frontLength = f, ret);
                createNumberDialog(baseOn, dia, "width", bullet.width,
                        f -> bullet.width = f, ret);
                typeOn.row();
                createNumberDialog(baseOn, dia, "oscScl", bullet.oscScl,
                        f -> bullet.oscScl = f, ret);
                createNumberDialog(baseOn, dia, "oscMag", bullet.oscMag,
                        f -> bullet.oscMag = f, ret);
                createLevDialog(typeOn, dia, "bulletBase", "laserCLength", bullet.laserCLength,
                        f -> bullet.laserCLength = f, ret, this::updateHeavy, levUser, hevUser);
                typeOn.row();
                createColorDialogList(typeOn, dia, "colors", bullet.colors,
                        c -> bullet.colors = c);
            }
            case "point" -> {
                createNumberDialog(baseOn, dia, "trailSpacing", bullet.trailSpacing,
                        f -> bullet.trailSpacing = f, ret);
            }
            case "rail" -> {
                if (!(bullet.pierceEffect instanceof MultiEffect)) {
                    bullet.pierceEffect = new MultiEffect();
                }
                createEffectList(typeOn, this, dia, "", bullet.pierceEffect);
                if (!(bullet.pointEffect instanceof MultiEffect)) {
                    bullet.pointEffect = new MultiEffect();
                }
                createEffectList(typeOn, this, dia, "", bullet.pointEffect);
                if (!(bullet.lineEffect instanceof MultiEffect)) {
                    bullet.lineEffect = new MultiEffect();
                }
                createEffectList(typeOn, this, dia, "", bullet.lineEffect);
                typeOn.row();
                if (!(bullet.endEffect instanceof MultiEffect)) {
                    bullet.endEffect = new MultiEffect();
                }
                createEffectList(typeOn, this, dia, "", bullet.endEffect);
                createLevDialog(baseOn, dia, "bulletBase", "railLength", bullet.railLength,
                        f -> bullet.railLength = f, ret, this::updateHeavy, levUser, hevUser);
                createNumberDialog(baseOn, dia, "pointEffectSpace", bullet.pointEffectSpace,
                        f -> bullet.pointEffectSpace = f, ret);
            }
        }
    }

    public void rebuildFloatList(float[] values, Cons<float[]> apply) {
        flo.clear();
        for (int i = 0; i * 3 < values.length; i++) {
            int finalI = i;
            flo.table(t -> {
                t.setBackground(Tex.buttonEdge1);

                t.label(() -> Core.bundle.get("dialog.bullet.fLength") + ":" + values[finalI * 3]).width(200);
                t.label(() -> Core.bundle.get("dialog.bullet.fWidth") + ":" + values[finalI * 3 + 1]).width(200);
                t.label(() -> Core.bundle.get("dialog.bullet.fPan") + ":" + values[finalI * 3 + 2]).width(200);
                t.button(Icon.pencil, () -> ui.showTextInput(Core.bundle.get("dialog.bullet.input"), Core.bundle.get("dialog.bullet.input"),
                        values[finalI * 3] + "," + values[finalI * 3 + 1] + "," + values[finalI * 3 + 2], str -> {
                            String[] s = str.split(",");
                            for (int j = 0; j < 3; j++) {
                                if (Strings.canParseFloat(s[j])) {
                                    values[finalI * 3 + j] = Float.parseFloat(s[j]);
                                } else {
                                    ui.showInfo(Core.bundle.get("@inputError"));
                                    return;
                                }
                            }
                            apply.get(values);
                            rebuildFloatList(values, apply);
                        })).width(100).pad(5);
                t.button(Icon.trash, () -> {
                    float[] n = new float[values.length - 3];
                    for (int j = 0, k = 0; j * 3 < values.length; j++) {
                        if (j != finalI) {
                            n[k * 3] = values[j * 3];
                            n[k * 3 + 1] = values[j * 3 + 1];
                            n[k * 3 + 2] = values[j * 3 + 2];
                            k++;
                        }
                    }
                    apply.get(n);
                    rebuildFloatList(n, apply);
                }).width(100).pad(5);
            }).width(1400);
            flo.row();
        }
    }

    public float findVal(String name) {
        return switch (name) {
            case "bulletBase" -> switch (newType) {
                case "point" -> bullet.damage / 1.4f + bullet.range / 8 + 100 *
                        Math.max(0, bullet.healPercent) + Math.max(0, bullet.healAmount);
                case "bullet" -> bullet.damage + bullet.range / 8 + bullet.pierceCap + 100 *
                        Math.max(0, bullet.healPercent) + Math.max(0, bullet.healAmount);
                case "laser" -> bullet.damage + bullet.laserLength / 8 + bullet.pierceCap + 100 *
                        Math.max(0, bullet.healPercent) + Math.max(0, bullet.healAmount);
                case "continuousF" ->
                        bullet.damage * 1.2f * bullet.lifetime * (40 / bullet.damageInterval) + bullet.flareLength / 8 +
                                100 * Math.max(0, bullet.healPercent) + Math.max(0, bullet.healAmount);
                case "continuousL" ->
                        bullet.damage * 1.2f * bullet.lifetime * (40 / bullet.damageInterval) + bullet.laserCLength / 8 +
                                100 * Math.max(0, bullet.healPercent) + Math.max(0, bullet.healAmount);
                case "lightning" ->
                        bullet.damage * 1.2f + (bullet.bulletLightningLength + bullet.bulletLightningLengthRand / 1.2f) /
                                8 + 100 * Math.max(0, bullet.healPercent) + Math.max(0, bullet.healAmount);
                default -> bullet.damage * 1.2f + bullet.railLength / 8 + 100 * Math.max(0, bullet.healPercent) +
                        Math.max(0, bullet.healAmount);
            };
            case "splash" -> switch (newType) {
                case "point", "bullet", "laser" ->
                        bullet.splashDamage * bullet.splashDamageRadius / 4 * (bullet.splashDamagePierce ? 1.5f : 1);
                case "continuousF", "continuousL" ->
                        bullet.lifetime * bullet.splashDamage * bullet.splashDamageRadius * 1.2f *
                                (40 / bullet.damageInterval) / 4 * (bullet.splashDamagePierce ? 1.5f : 1);
                case "lightning", "rail" ->
                        bullet.splashDamage * bullet.splashDamageRadius * 1.2f / 4 * (bullet.splashDamagePierce ? 1.5f : 1);
                default -> 100000;
            };
            case "lightning" -> switch (newType) {
                case "point", "bullet", "laser" ->
                        bullet.lightning * bullet.lightningDamage * bullet.lightningLength * bullet.lightningLengthRand / 4;
                case "continuousF", "continuousL" ->
                        bullet.lightning * bullet.lightningDamage * bullet.lightningLength *
                                bullet.lightningLengthRand * 1.2f * bullet.lifetime * (40 / bullet.damageInterval) / 4;
                case "lightning", "rail" ->
                        bullet.lightning * bullet.lightningDamage * bullet.lightningLength * bullet.lightningLengthRand * 1.2f / 4;
                default -> 100000;
            };
            case "percent" -> switch (newType) {
                case "point", "bullet", "laser" -> bullet.percent;
                case "continuousF", "continuousL" ->
                        bullet.percent * 1.2f * bullet.lifetime * (40 / bullet.damageInterval);
                case "lightning", "rail" -> bullet.percent * 1.2f;
                default -> 100000;
            };
            case "frags" -> switch (newType) {
                case "point", "bullet", "laser" -> bullet.fragBullets;
                case "continuousF", "continuousL" ->
                        bullet.fragBullets * 1.2f * bullet.lifetime * (40 / bullet.damageInterval);
                case "lightning", "rail" -> bullet.fragBullets * 1.2f;
                default -> 100000;
            };
            case "knock" -> switch (newType) {
                case "point", "bullet", "laser" -> bullet.knockback;
                case "continuousF", "continuousL" ->
                        bullet.knockback * 1.2f * bullet.lifetime * (40 / bullet.damageInterval);
                case "lightning", "rail" -> bullet.knockback * 1.2f;
                default -> 100000;
            };
            case "emp" -> switch (newType) {
                case "point", "bullet", "laser" ->
                        bullet.empDamage * bullet.radius / Math.max(0.01f, bullet.powerSclDecrease) *
                                bullet.timeIncrease * bullet.timeDuration / 4;
                case "continuousF", "continuousL" ->
                        bullet.empDamage * bullet.radius / Math.max(0.01f, bullet.powerSclDecrease) * bullet.timeIncrease *
                                bullet.timeDuration * 1.2f * bullet.lifetime * (40 / bullet.damageInterval) / 4;
                case "lightning", "rail" ->
                        bullet.empDamage * bullet.radius / Math.max(0.01f, bullet.powerSclDecrease) *
                                bullet.timeIncrease * bullet.timeDuration * 1.2f / 4;
                default -> 100000;
            };
            case "suppression" -> switch (newType) {
                case "point", "bullet", "laser" ->
                        Math.max(0, bullet.suppressionRange * bullet.suppressionRange * bullet.suppressionEffectChance);
                case "continuousF", "continuousL" ->
                        Math.max(0, bullet.suppressionRange * bullet.suppressionRange * bullet.suppressionEffectChance) *
                                1.2f * bullet.lifetime * (40 / bullet.damageInterval);
                case "lightning", "rail" ->
                        Math.max(0, bullet.suppressionRange * bullet.suppressionRange * bullet.suppressionEffectChance) * 1.2f;
                default -> 100000;
            };
            case "puddles" -> switch (newType) {
                case "point", "bullet", "laser" ->
                        Math.max(0, bullet.puddles * bullet.puddleRange * bullet.puddleAmount);
                case "continuousF", "continuousL" ->
                        Math.max(0, bullet.puddles * bullet.puddleRange * bullet.puddleAmount) *
                                1.2f * bullet.lifetime * (40 / bullet.damageInterval);
                case "lightning", "rail" ->
                        Math.max(0, bullet.puddles * bullet.puddleRange * bullet.puddleAmount) * 1.2f;
                default -> 100000;
            };
            case "none" -> 0;
            default -> 100000;
        };
    }

    public void updateHeavy() {
        heavy = 0.5f;
        heavy += getHeavy("bulletBase", findVal("bulletBase"));
        heavy += getHeavy("emp", findVal("emp"));
        heavy += getHeavy("splash", findVal("splash"));
        heavy += getHeavy("lightning", findVal("lightning"));
        heavy += getHeavy("percent", findVal("percent"));
        heavy += getHeavy("frags", findVal("frags"));
        heavy += getHeavy("knock", findVal("knock"));
        heavy += getHeavy("suppression", findVal("suppression"));
        bulletHeavy = dialog == null ? 0 : dialog.heavyOut();
    }

    public void updateDialog(boolean add) {
        if (add) {
            if (!(bullet.fragBullet instanceof LimitBulletType)) {
                bullet.fragBullet = new LimitBulletType();
            }
            dialog = new BulletDialog(() -> bullet.fragBullet, f -> bulletHeavy = f,
                    b -> bullet.fragBullet = b, "", () -> bullet.fragBullets * boost);
            bulletHeavy = dialog.heavyOut();
            dialog.shown(() -> freeSize -= boost * heavy);
            dialog.hidden(() -> freeSize += boost * heavy);
        } else {
            bullet.fragBullet = null;
            dialog = null;
            bulletHeavy = 0;
        }
    }

    public float heavyOut() {
        updateHeavy();
        float other = 0;
        if (dialog != null) {
            other = dialog.heavyOut();
        }
        return heavy + bullet.fragBullets * other;
    }

    @Override
    public Table get() {
        return effectOn;
    }

    @Override
    public void set(Table table) {
        effectOn = table;
    }
}