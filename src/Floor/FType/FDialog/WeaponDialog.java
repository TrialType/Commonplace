package Floor.FType.FDialog;

import Floor.FEntities.FBulletType.LimitBulletType;
import arc.Core;
import arc.func.Cons;
import arc.scene.ui.layout.Table;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Icon;
import mindustry.type.Weapon;
import mindustry.type.weapons.PointDefenseWeapon;
import mindustry.type.weapons.RepairBeamWeapon;
import mindustry.ui.dialogs.BaseDialog;

import java.lang.reflect.Field;

import static Floor.FType.FDialog.ProjectDialogUtils.*;
import static mindustry.Vars.ui;

public class WeaponDialog extends BaseDialog implements EffectTableGetter {
    public Weapon weapon;
    protected BulletDialog bulletDialog;
    protected LimitBulletType bullet;
    protected float bulletHeavy = 0;
    protected float heavyBack;
    protected float heavy = 0;
    protected String type = "default";
    protected Table baseOn;
    protected Table typeOn;
    protected Table effectOn;

    protected Runnable reBase = this::rebuildBase;
    protected Runnable reType = this::rebuildType;
    protected static String dia = "weapon";
    protected StrBool levUser = str -> couldUse(str, getVal(str));
    protected BoolGetter hevUser = () -> weapon.shoot.shots * bulletHeavy + heavy <= freeSize;

    public WeaponDialog(String title, Weapon rollback, Cons<Weapon> apply, Cons<Float> heavyApply) {
        super(title);

        if (this.weapon == null) {
            this.weapon = rollback;
            updateHeavy();
            heavyBack = heavy;
        }
        setWeapon(weapon);
        bulletDialog = new BulletDialog(this, "");
        bulletDialog.hidden(() -> freeSize += this.heavy);
        bullet = new LimitBulletType();
        buttons.button("@back", Icon.left, () -> {
            weapon = rollback;
            updateHeavy();
            hide();
        });
        buttons.button("@apply", Icon.right, () -> {
            updateHeavy();
            if (heavy + weapon.shoot.shots * bulletHeavy <= freeSize) {
                apply.get(weapon);
                hide();
            }
            ui.showInfo("tooHeavy");
        });
        buttons.button("@setZero", () -> {
            weapon.reload = Float.MAX_VALUE;
            weapon.shoot.shots = 0;
            weapon.targetSwitchInterval = weapon.targetInterval = Float.MAX_VALUE;
            if (weapon instanceof RepairBeamWeapon r) {
                r.repairSpeed = r.fractionRepairSpeed = r.beamWidth = 0;
            }
            updateHeavy();
            rebuild();
        });
        shown(this::rebuild);
        hidden(() -> {
            freeSize -= this.heavy;
            freeSize -= weapon.shoot.shots * bulletHeavy;
            heavyApply.get(weapon.shoot.shots * bulletHeavy + this.heavy);
        });
    }

    public void rebuild() {
        cont.table(t -> {
            t.label(() -> Core.bundle.get("dialog.weapon." + type)).pad(5);
            t.button(b -> {
                b.image(Icon.pencilSmall);

                b.clicked(() -> createSelectDialog(b, (tb, hide) -> {
                    tb.button(Core.bundle.get("dialog.weapon.default"), () -> {
                        if (type.equals("default")) {
                            hide.run();
                            return;
                        }
                        Weapon w = new Weapon();
                        cloneWeapon(weapon, w);
                        weapon = w;
                        type = "default";
                        hide.run();
                    });
                    tb.row();
                    tb.button(Core.bundle.get("dialog.weapon.defense"), () -> {
                        if (type.equals("defense")) {
                            hide.run();
                            return;
                        }
                        PointDefenseWeapon w = new PointDefenseWeapon();
                        cloneWeapon(weapon, w);
                        weapon = w;
                        type = "defense";
                        rebuildType();
                        hide.run();
                    });
                    tb.row();
                    tb.button(Core.bundle.get("dialog.weapon.repair"), () -> {
                        if (type.equals("repair")) {
                            hide.run();
                            return;
                        }
                        RepairBeamWeapon w = new RepairBeamWeapon();
                        cloneWeapon(weapon, w);
                        weapon = w;
                        type = "repair";
                        rebuildType();
                        hide.run();
                    });
                }));
            }, () -> {
            });
        });
        cont.clear();
        cont.pane(t -> baseOn = t);
        cont.pane(t -> typeOn = t);
        rebuildBase();
        rebuildType();
    }

    public void rebuildBase() {
        baseOn.clear();
        baseOn.label(() -> Core.bundle.get("dialog.weapon.bullet")).width(160);
        baseOn.button(Icon.pencilSmall, () -> {
            freeSize -= this.heavy;
            bulletDialog.show();
        }).size(15).pad(15);
        if (weapon.ejectEffect == null) {
            weapon.ejectEffect = new MultiEffect();
        }
        baseOn.row();
        createEffectList(baseOn, this, dia, "ejectEffect", weapon.ejectEffect);
        baseOn.row();
        createNumberDialog(baseOn, dia, "x", weapon.x, f -> weapon.x = f, reBase);
        createNumberDialog(baseOn, dia, "y", weapon.y, f -> weapon.y = f, reBase);
        createNumberDialog(baseOn, dia, "shootY", weapon.shootY, f -> weapon.shootY = f, reBase);
        baseOn.row();
        createNumberDialog(baseOn, dia, "shootX", weapon.shootX, f -> weapon.shootX = f, reBase);
        createNumberDialog(baseOn, dia, "shootCone", weapon.shootCone, f -> weapon.shootCone = f, reBase);
        createBooleanDialog(baseOn, dia, "rotate", weapon.rotate, b -> weapon.rotate = b, reBase);
        baseOn.row();
        createNumberDialog(baseOn, dia, "rotateSpeed", weapon.rotateSpeed, f -> weapon.rotateSpeed = f, reBase);
        createNumberDialog(baseOn, dia, "rotationLimit", weapon.rotationLimit, f -> weapon.rotationLimit = f, reBase);
        createNumberDialog(baseOn, dia, "baseRotation", weapon.baseRotation, f -> weapon.baseRotation = f, reBase);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "mirror", weapon.mirror, b -> weapon.mirror = b, reBase);
        createBooleanDialog(baseOn, dia, "alternate", weapon.alternate, b -> weapon.alternate = b, reBase);
        createBooleanDialog(baseOn, dia, "continuous", weapon.continuous, b -> weapon.continuous = b, reBase);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "alwaysContinuous", weapon.alwaysContinuous, b -> weapon.alwaysContinuous = b, reBase);
        createBooleanDialog(baseOn, dia, "controllable", weapon.controllable, b -> weapon.controllable = b, reBase);
        createBooleanDialog(baseOn, dia, "aiControllable", weapon.aiControllable, b -> weapon.aiControllable = b, reBase);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "alwaysShooting", weapon.alwaysShooting, b -> weapon.alwaysShooting = b, reBase);
        createBooleanDialog(baseOn, dia, "autoTarget", weapon.autoTarget, b -> weapon.autoTarget = b, reBase);
        createBooleanDialog(baseOn, dia, "predictTarget", weapon.predictTarget, b -> weapon.predictTarget = b, reBase);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "useAttackRange", weapon.useAttackRange, b -> weapon.useAttackRange = b, reBase);
        createLevDialog(baseOn, dia, "target", "targetInterval", weapon.targetInterval,
                f -> weapon.targetInterval = f, reBase, this::updateHeavy, levUser, hevUser);
        createLevDialog(baseOn, dia, "target", "targetSwitchInterval", weapon.targetSwitchInterval,
                f -> weapon.targetSwitchInterval = f, reBase, this::updateHeavy, levUser, hevUser);
        baseOn.row();
        createLevDialog(baseOn, dia, "reload", "reload", weapon.reload,
                f -> weapon.reload = f, reBase, this::updateHeavy, levUser, hevUser);
        createNumberDialog(baseOn, dia, "inaccuracy", weapon.inaccuracy, f -> weapon.inaccuracy = f, reBase);
        createNumberDialog(baseOn, dia, "inaccuracy", weapon.shake, f -> weapon.shake = f, reBase);
        baseOn.row();
        createNumberDialog(baseOn, dia, "recoil", weapon.recoil, f -> weapon.recoil = f, reBase);
        createNumberDialog(baseOn, dia, "recoils", weapon.recoils, f -> weapon.recoils = (int) (f + 0), reBase);
        createNumberDialog(baseOn, dia, "recoilTime", weapon.recoilTime, f -> weapon.recoilTime = f, reBase);
        baseOn.row();
        createNumberDialog(baseOn, dia, "recoilPow", weapon.recoilPow, f -> weapon.recoilPow = f, reBase);
        createNumberDialogWithLimit(baseOn, dia, "xRand", weapon.xRand, 25, 0, f -> weapon.xRand = f, reBase);
        createNumberDialogWithLimit(baseOn, dia, "rotationLimit", weapon.rotationLimit, 361, 0, f -> weapon.rotationLimit = f, reBase);
        baseOn.row();
        createNumberDialogWithLimit(baseOn, dia, "minWarmup", weapon.minWarmup, 0.99f, 0, f -> weapon.minWarmup = f, reBase);
        createNumberDialogWithLimit(baseOn, dia, "shootWarmupSpeed", weapon.shootWarmupSpeed, 0.99f, 0, f -> weapon.shootWarmupSpeed = f, reBase);
        createNumberDialogWithLimit(baseOn, dia, "smoothReloadSpeed", weapon.smoothReloadSpeed, 0.99f, 0, f -> weapon.smoothReloadSpeed = f, reBase);
        baseOn.row();
        createBooleanDialog(baseOn, dia, "ignoreRotation", weapon.ignoreRotation, b -> weapon.ignoreRotation = b, reBase);
        createBooleanDialog(baseOn, dia, "noAttack", weapon.noAttack, b -> weapon.noAttack = b, reBase);
        createBooleanDialog(baseOn, dia, "linearWarmup", weapon.linearWarmup, b -> weapon.linearWarmup = b, reBase);
        baseOn.row();
        createPartsDialog(baseOn, dia, "parts", weapon.parts, p -> weapon.parts = p);
    }

    public void rebuildType() {
        typeOn.clear();
        if (type.equals("defense")) {
            PointDefenseWeapon p = (PointDefenseWeapon) weapon;
            createColorDialog(typeOn, dia, "color", p.color,
                    c -> p.color = c, reType);
            if (p.beamEffect == null) {
                p.beamEffect = new MultiEffect();
            }
            createEffectList(typeOn, this, dia, "effect", p.beamEffect);
        } else if (type.equals("repair")) {
            RepairBeamWeapon r = (RepairBeamWeapon) weapon;
            createBooleanDialog(typeOn, dia, "targetBuildings", r.targetBuildings,
                    b -> r.targetBuildings = b, reType);
            createBooleanDialog(typeOn, dia, "targetUnits", r.targetUnits,
                    b -> r.targetUnits = b, reType);
            createNumberDialogWithLimit(typeOn, dia, "recentDamageMultiplier", r.recentDamageMultiplier, 1, 0,
                    f -> r.recentDamageMultiplier = f, reType);
            typeOn.row();
            createLevDialog(typeOn, dia, "target", "repairSpeed", r.repairSpeed,
                    f -> r.repairSpeed = f, reType, this::updateHeavy, levUser, hevUser);
            createLevDialog(typeOn, dia, "target", "fractionRepairSpeed", r.fractionRepairSpeed,
                    f -> r.fractionRepairSpeed = f, reType, this::updateHeavy, levUser, hevUser);
            createLevDialog(typeOn, dia, "target", "beamWidth", r.beamWidth,
                    f -> r.beamWidth = f, reType, this::updateHeavy, levUser, hevUser);
            typeOn.row();
            createNumberDialog(typeOn, dia, "pulseRadius", r.pulseRadius,
                    f -> r.pulseRadius = f, reType);
            createNumberDialog(typeOn, dia, "pulseStroke", r.pulseStroke,
                    f -> r.pulseStroke = f, reType);
            createNumberDialog(typeOn, dia, "widthSinMag", r.widthSinMag,
                    f -> r.widthSinMag = f, reType);
            typeOn.row();
            createNumberDialog(typeOn, dia, "widthSinScl", r.widthSinScl,
                    f -> r.widthSinScl = f, reType);
            if (r.healEffect == null) {
                r.healEffect = new MultiEffect();
            }
            createEffectList(typeOn, this, dia, "healEffect", r.healEffect);
            createColorDialog(typeOn, dia, "laserColor", r.laserColor,
                    c -> r.laserColor = c, reType);
            typeOn.row();
            createColorDialog(typeOn, dia, "laserTopColor", r.laserTopColor,
                    c -> r.laserTopColor = c, reType);
            createColorDialog(typeOn, dia, "healColor", r.healColor,
                    c -> r.healColor = c, reType);
        }
    }

    public float getVal(String type) {
        RepairBeamWeapon r = (RepairBeamWeapon) weapon;
        return switch (type) {
            case "number" -> weapon.shoot.shots;
            case "reload" -> weapon.reload;
            case "target" ->
                    this.type.equals("repair") ? r.repairSpeed * 15 + r.fractionRepairSpeed * 60 + r.beamWidth / 4 :
                            this.type.equals("defense") ? weapon.targetInterval * weapon.targetSwitchInterval : 0;
            default -> -1;
        };
    }

    public void updateHeavy() {
        heavy = 0.5f;
        heavy += getHeavy("number", weapon.shoot.shots);
        heavy += getHeavy("reload", weapon.reload);
        heavy += getHeavy("target", weapon.targetInterval * weapon.targetSwitchInterval);
    }

    public void setWeapon(Weapon weapon) {
        if (weapon instanceof RepairBeamWeapon r) {
            this.weapon = new RepairBeamWeapon();
            RepairBeamWeapon rb = (RepairBeamWeapon) this.weapon;
            Field[] fields = RepairBeamWeapon.class.getFields();
            for (Field field : fields) {
                try {
                    field.set(rb, field.get(r));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            type = "repair";
        } else if (weapon instanceof PointDefenseWeapon p) {
            this.weapon = new PointDefenseWeapon();
            PointDefenseWeapon pw = (PointDefenseWeapon) this.weapon;
            Field[] fields = PointDefenseWeapon.class.getFields();
            for (Field field : fields) {
                try {
                    field.set(pw, field.get(p));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            type = "defense";
        } else {
            this.weapon = new Weapon();
            cloneWeapon(weapon, this.weapon);
            type = "default";
        }
    }

    public void cloneWeapon(Weapon from, Weapon to) {
        Field[] fields = Weapon.class.getFields();
        for (Field field : fields) {
            try {
                field.set(to, field.get(from));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
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