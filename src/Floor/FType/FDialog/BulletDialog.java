package Floor.FType.FDialog;

import Floor.FEntities.FBulletType.LimitBulletType;
import Floor.FEntities.FEffect.IOEffect;
import Floor.FEntities.FEffect.IOMulti;
import arc.Core;
import arc.func.Cons;
import arc.func.Cons2;
import arc.graphics.Color;
import arc.math.Interp;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Strings;
import arc.util.Tmp;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.ui;

public class BulletDialog extends BaseDialog {
    public int boost = 1;
    public WeaponDialog parentW;
    public BulletDialog parentB;
    public final Seq<String> types = new Seq<>(new String[]{
            "bullet", "laser", "lightning", "continuousF", "continuousL", "point", "rail"
    });
    public String newType = "bullet";
    //global
    public LimitBulletType bullet = new LimitBulletType();
    public LimitBulletType FBullet;
    public float bulletHeavy = 0;
    public float fromHeavy = 0;
    public float heavy = 0.5f;
    public Table typeOn;
    public Table baseOn;
    public Table effectOn;

    public BulletDialog(BaseDialog parent, String title) {
        super(title);
        shown(this::loadBase);
        this.parentW = null;
        this.parentB = null;
        if (parent instanceof WeaponDialog wd) {
            this.parentW = wd;
            (wd.bullet == null ? new LimitBulletType() : wd.bullet).copyTo(this.bullet);
            updateHeavy();
            this.fromHeavy = parentW.heavy;
            this.bulletHeavy = parentW.bulletHeavy - this.heavy;
            newType = bullet.type;
            boost = wd.weapon.shoot.shots;
        } else if (parent instanceof BulletDialog bd) {
            this.parentB = bd;
            (bd.FBullet == null ? new LimitBulletType() : bd.FBullet).copyTo(this.bullet);
            updateHeavy();
            this.fromHeavy = parentB.fromHeavy + parentB.heavy * parentB.boost;
            this.bulletHeavy = parentB.bulletHeavy - this.heavy;
            newType = bullet.type;
            boost = bd.bullet.fragBullets * bd.boost;
        }

        buttons.button("@back", Icon.left, () -> {
            hide();
            if (parentB != null) {
                parentB.bulletHeavy = this.heavy * boost + this.bulletHeavy;
            } else if (parentW != null) {
                parentW.bulletHeavy = this.heavy * boost + this.bulletHeavy;
            }
        }).size(210f, 64f);
        buttons.button(Core.bundle.get("@apply"), Icon.right, () -> {
            if (ProjectsLocated.getHeavy("percent", findVal("percent")) > 0) {
                bullet.havePercent = true;
            }
            if (ProjectsLocated.getHeavy("emp", findVal("emp")) > 0) {
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
            if (parentB != null) {
                if (parentB.FBullet == null) {
                    parentB.FBullet = new LimitBulletType();
                }
                bullet.copyTo(parentB.FBullet);
                parentB.bulletHeavy = this.heavy * boost + this.bulletHeavy;
            } else if (parentW != null) {
                if (parentW.bullet == null) {
                    parentW.bullet = new LimitBulletType();
                }
                bullet.copyTo(parentW.bullet);
                parentW.bulletHeavy = this.heavy * boost + this.bulletHeavy;
            }
            hide();
        }).size(210f, 64f);
        buttons.button("@toZero", Icon.defense, () -> {
            bullet.setZero();
            updateHeavy();
            rebuildType();
            rebuildBase();
        }).size(210f, 64f);
    }

    public void loadBase() {
        cont.pane(this::Front);
    }

    public void Front(Table table) {
        table.table(t -> {
            t.background(Tex.scroll);
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

                        }).growX().width(200);
                        tb.row();
                    }
                }));
            }, Styles.logici, () -> {
            }).size(25).left().pad(5);
            t.row();
            t.label(() -> Core.bundle.get("@heavyUse") + ": " +
                    (heavy + fromHeavy + bulletHeavy) + " / " +
                    ProjectsLocated.freeSize).size(25).left().width(100).pad(5);
        }).pad(2).growX().row();

        table.table(t -> typeOn = t).pad(2).left().growX();
        rebuildType();
        table.row();
        table.table(t -> baseOn = t).pad(2).left().growX();
        rebuildBase();
    }

    public void rebuildType() {
        typeOn.clear();
        typeOn.background(Tex.buttonDown);
        switch (newType) {
            case "bullet" -> {
                createLevDialog("range", "bulletBase", typeOn, bullet.range,
                        f -> bullet.range = f, f -> bullet.range = f);
                createNumberDialog("lifetime", typeOn, bullet.lifetime, 0, Float.MAX_VALUE,
                        f -> {
                            bullet.lifetime = f;
                            bullet.speed = bullet.range / (f == 0 ? 0.0001f : f);
                        });
                createNumberDialog("bulletWide", typeOn, bullet.width, 0, 45,
                        f -> bullet.width = f);
                createNumberDialog("bulletHeight", typeOn, bullet.height, 0, 45,
                        f -> bullet.height = f);
                typeOn.row();
            }
            case "laser" -> {
                createLevDialog("laserLength", "bulletBase", typeOn, bullet.laserCLength,
                        f -> bullet.laserCLength = f, f -> bullet.laserCLength = f);
                createNumberDialog("laserWidth", typeOn, bullet.width, 0.01f, 45,
                        f -> bullet.width = f);
            }
            case "lightning" -> {
                createLevDialog("bulletLightningLength", "bulletBase", typeOn, bullet.bulletLightningLength,
                        f -> bullet.bulletLightningLength = (int) (f + 0), f -> bullet.bulletLightningLength = (int) (f + 0));
                createLevDialog("bulletLightningLengthRand", "bulletBase", typeOn, bullet.bulletLightningLengthRand,
                        f -> bullet.bulletLightningLengthRand = (int) (f + 0), f -> bullet.bulletLightningLengthRand = (int) (f + 0));
            }
            case "continuousF" -> {
                createLevDialog("flareLength", "bulletBase", typeOn, bullet.laserCLength,
                        f -> bullet.laserCLength = f, f -> bullet.laserCLength = f);
                createLevDialog("lifetime", "bulletBase", typeOn, bullet.lifetime,
                        f -> bullet.lifetime = f, f -> bullet.lifetime = f);
                createNumberDialog("flareWidth", typeOn, bullet.flareWidth, 0, 30,
                        f -> bullet.flareWidth = f);
            }
            case "continuousL" -> {
                createLevDialog("laserCLength", "bulletBase", typeOn, bullet.flareLength,
                        f -> bullet.flareLength = f, f -> bullet.flareLength = f);
                createLevDialog("lifetime", "bulletBase", typeOn, bullet.lifetime,
                        f -> bullet.lifetime = f, f -> bullet.lifetime = f);
                createNumberDialog("fadeTime", typeOn, bullet.fadeTime, 12, 36,
                        f -> bullet.fadeTime = f);
            }
            case "point" -> {
                createLevDialog("range", "bulletBase", typeOn, bullet.range,
                        f -> bullet.range = f, f -> bullet.range = f);
                createNumberDialog("trailSpacing", typeOn, 10, 10, 180,
                        f -> bullet.trailSpacing = f);
            }
            case "rail" -> {
                createLevDialog("railLength", "bulletBase", typeOn, bullet.railLength,
                        f -> bullet.railLength = f, f -> bullet.railLength = f);
                createNumberDialog("pointEffectSpace", typeOn, 10, 10, 180,
                        f -> bullet.pointEffectSpace = f);
            }
        }
    }

    public void rebuildBase() {
        baseOn.clear();
        createTypeLine(baseOn, "bulletBase");

        baseOn.table(s -> {
            s.background(Tex.buttonDown);
            createLevDialog("damage", "bulletBase", s, bullet.damage,
                    f -> bullet.damage = f, f -> bullet.damage = f);
        }).growX();

        createTypeLine(baseOn, "frags");

        baseOn.table(s -> {
            s.background(Tex.buttonDown);
            createNumberDialog("fragAngle", s, bullet.fragAngle, -12, 12,
                    f -> bullet.fragAngle = f);
            createLevDialog("frags", "frags", s, bullet.fragBullets,
                    f -> bullet.fragBullets = (int) (f + 0), f -> bullet.fragBullets = (int) (f + 0));
            s.row();
            s.label(() -> Core.bundle.get("writeFrag") + "->").width(150);
            s.button(Icon.pencilSmall, () -> {
                ProjectsLocated.freeSize -= this.heavy * boost;
                BulletDialog bd = new BulletDialog(this, "");
                bd.hidden(() -> ProjectsLocated.freeSize += this.heavy * boost);
                bd.show();
            }).pad(15).width(24);
            s.row();
        }).growX();

        createTypeLine(baseOn, "lightning");

        baseOn.table(s -> {
            s.background(Tex.buttonDown);
            createNumberDialog("lightningAngle", s, bullet.lightningAngle, 0, 360,
                    f -> bullet.lightningAngle = f);
            createNumberDialog("lightningAngleRand", s, bullet.lightningAngleRand, 0, 360,
                    f -> bullet.lightningAngle = f);
            createLevDialog("lightningLength", "lightning", s, bullet.lightningLength,
                    f -> bullet.lightningLength = (int) (f + 0), f -> bullet.lightningLength = (int) (f + 0));
            s.row();
            createLevDialog("lightningLengthRand", "lightning", s, bullet.lightningLengthRand,
                    f -> bullet.lightningLengthRand = (int) (f + 0), f -> bullet.lightningLengthRand = (int) (f + 0));
            createLevDialog("lightningDamage", "lightning", s, bullet.lightningDamage,
                    f -> bullet.lightningDamage = f + 0, f -> bullet.lightningDamage = f + 0);
            createLevDialog("lightnings", "lightning", s, bullet.lightning,
                    f -> bullet.lightning = (int) (f + 0), f -> bullet.lightning = (int) (f + 0));
        }).growX();

        createTypeLine(baseOn, "percent");

        baseOn.table(p -> createLevDialog("percent", "percent", p, bullet.percent,
                f -> bullet.percent = f, f -> bullet.percent = f));

        baseOn.row();
        baseOn.label(() -> Core.bundle.get("dialog.bullet.effects") + ": ");
        baseOn.row();

        baseOn.table(this::rebuildEffect).grow();
    }

    public void rebuildEffect(Table on) {
        on.clear();
        on.table(l -> createEffectLine(l, "shootEffect", bullet.shootEffect)).growX();
        on.table(l -> createEffectLine(l, "despawnEffect", bullet.despawnEffect)).growX();
        on.row();
        on.table(l -> createEffectLine(l, "hitEffect", bullet.hitEffect)).growX();
        on.table(l -> createEffectLine(l, "chargeEffect", bullet.chargeEffect)).growX();
        on.row();
        on.table(l -> createEffectLine(l, "smokeEffect", bullet.smokeEffect)).growX();
    }

    public void createEffectLine(Table t, String name, IOMulti list) {
        t.label(() -> Core.bundle.get("dialog.bullet." + name) + "->");
        t.button(Icon.pencil, () -> {
            BaseDialog bd = new BaseDialog("");
            bd.cont.pane(li -> {
                li.table(ta -> {
                    effectOn = ta;
                    rebuildEffectList(list);
                }).grow();
                li.row();
                li.button(Icon.add, () -> {
                    IOEffect effect = new IOEffect();
                    IOEffect[] effects = new IOEffect[list.effects.length + 1];
                    System.arraycopy(list.effects, 0, effects, 0, list.effects.length);
                    effects[effects.length - 1] = effect;
                    list.effects = effects;
                    rebuildEffectList(list);
                });
            }).growX().growY();
            bd.row();
            bd.buttons.button(Icon.left, bd::hide);
            bd.show();
        });
    }

    public void rebuildEffectList(IOMulti list) {
        effectOn.clear();
        for (int i = 0; i < list.effects.length; i++) {
            int finalI = i;
            effectOn.table(t -> {
                t.label(() -> finalI + "").growX();
                t.button(Icon.pencil, () -> createEffectDialog(list.effects[finalI], () -> rebuildEffectList(list))).growX();
                t.button(Icon.trash, () -> {
                    IOEffect[] effects = new IOEffect[list.effects.length - 1];
                    for (int j = 0; j < list.effects.length; j++) {
                        if (j != finalI) {
                            effects[j] = list.effects[j];
                        }
                    }
                    list.effects = effects;
                    rebuildEffectList(list);
                }).growX();
            }).growX();
            effectOn.row();
        }
    }

    public void createTypeLine(Table t, String type) {
        t.row();
        t.table(table -> {
            table.background(Tex.scroll);
            table.label(() -> Core.bundle.get("dialog.bullet." + type)).left();
            table.row();
            table.label(() -> Core.bundle.get("@heavyUse") + ":  " + ProjectsLocated.getHeavy(type, findVal(type))).left().pad(5);
            table.label(() -> Core.bundle.get("@maxLevel") + ":  " + ProjectsLocated.maxLevel.get(type)).left().pad(5);
        });
        t.row();
    }

    public void createEffectDialog(IOEffect body, Runnable hide) {
        EffectDialog ed = new EffectDialog("");
        ed.hidden(hide);
        ed.setEffect(body).show();
    }

    public void createLevDialog(String name, String line, Table t, float value, Cons<Float> changer, Cons<Float> rollback) {
        t.table(type -> {
            type.add(Core.bundle.get("dialog.bullet." + name) + ":").pad(3).color(Color.red);
            type.label(() -> value + "").pad(3);
            type.button(Icon.pencil, Styles.flati, () -> ui.showTextInput(Core.bundle.get("dialog.bullet." + name), "", 15, value + "", true, str -> {
                if (Strings.canParsePositiveFloat(str)) {
                    float amount = Strings.parseFloat(str);
                    changer.get(amount);
                    float now = this.heavy;
                    updateHeavy();
                    if (ProjectsLocated.couldUse(line, findVal(line)) && boost * heavy +
                            bullet.fragBullets * bulletHeavy + fromHeavy <= ProjectsLocated.freeSize) {
                        rebuildBase();
                        rebuildType();
                    } else {
                        if (!ProjectsLocated.couldUse(line, findVal(line))) {
                            ui.showInfo(Core.bundle.get("@levelOutOfBounds"));
                        } else if (!(heavy + bulletHeavy + fromHeavy <= ProjectsLocated.freeSize)) {
                            ui.showInfo(Core.bundle.get("@tooHeavy"));
                        }
                        this.heavy = now;
                        rollback.get(value);
                    }
                    return;
                }
                ui.showInfo(Core.bundle.get("@inputError"));
            })).size(55);
        }).pad(10).fillX();
    }

    public void createNumberDialog(String name, Table t, float value, float min, float max, Cons<Float> changer) {
        t.table(base -> {
            base.add(Core.bundle.get("dialog.bullet." + name) + ":");
            base.label(() -> value + "").pad(3);
            base.button(Icon.pencil, Styles.flati, () -> ui.showTextInput(Core.bundle.get("dialog.bullet." + name), "", 15, value + "", true, str -> {
                if (Strings.canParsePositiveFloat(str)) {
                    float amount = Strings.parseFloat(str);
                    if ((amount >= min && amount <= max) || max < min) {
                        changer.get(amount);
                        rebuildBase();
                        rebuildType();
                        return;
                    }
                }
                ui.showInfo(Core.bundle.format("configure.invalid", min, max));
            })).size(55);
        }).pad(10).fillX();
    }

    public void createSelectDialog(Button b, Cons2<Table, Runnable> table) {
        Table ta = new Table() {
            @Override
            public float getPrefHeight() {
                return Math.min(super.getPrefHeight(), Core.graphics.getHeight());
            }

            @Override
            public float getPrefWidth() {
                return Math.min(super.getPrefWidth(), Core.graphics.getWidth());
            }
        };
        ta.margin(4);

        Element hitter = new Element();

        Runnable hide = () -> {
            Core.app.post(hitter::remove);
            ta.actions(Actions.fadeOut(0.3f, Interp.fade), Actions.remove());
        };

        hitter.fillParent = true;
        hitter.tapped(hide);

        Core.scene.add(hitter);

        ta.update(() -> {
            if (b.parent == null || !b.isDescendantOf(Core.scene.root)) {
                Core.app.post(() -> {
                    hitter.remove();
                    ta.remove();
                });
                return;
            }

            b.localToStageCoordinates(Tmp.v1.set(b.getWidth() / 2f, b.getHeight() / 2f));
            ta.setPosition(Tmp.v1.x, Tmp.v1.y, Align.center);
            if (ta.getWidth() > Core.scene.getWidth()) ta.setWidth(Core.graphics.getWidth());
            if (ta.getHeight() > Core.scene.getHeight()) ta.setHeight(Core.graphics.getHeight());
            ta.keepInStage();
            ta.invalidateHierarchy();
            ta.pack();
        });

        Core.scene.add(ta);

        ta.top().pane(select -> table.get(select, hide)).pad(0f).top().scrollX(false);
        ta.actions(Actions.alpha(0), Actions.fadeIn(0.001f));

        ta.pack();
    }

    public float findVal(String name) {
        return switch (name) {
            case "bulletBase" -> switch (newType) {
                case "point" -> bullet.damage / 1.4f + bullet.range / 8;
                case "bullet" -> bullet.damage + bullet.range / 8 + bullet.pierceCap;
                case "laser" -> bullet.damage + bullet.laserLength / 8 + bullet.pierceCap;
                case "continuousF" ->
                        bullet.damage * 1.2f * bullet.lifetime * (40 / bullet.damageInterval) + bullet.flareLength / 8;
                case "continuousL" ->
                        bullet.damage * 1.2f * bullet.lifetime * (40 / bullet.damageInterval) + bullet.laserCLength / 8;
                case "lightning" ->
                        bullet.damage * 1.2f + (bullet.bulletLightningLength + bullet.bulletLightningLengthRand / 1.2f) / 8;
                default -> bullet.damage * 1.2f + bullet.railLength / 8;
            };
            case "splash" -> switch (newType) {
                case "point", "bullet", "laser" -> bullet.splashDamage * bullet.splashDamageRadius;
                case "continuousF", "continuousL" ->
                        bullet.lifetime * bullet.splashDamage * bullet.splashDamageRadius * 1.2f * (40 / bullet.damageInterval);
                case "lightning", "rail" -> bullet.splashDamage * bullet.splashDamageRadius * 1.2f;
                default -> 100000;
            };
            case "lightning" -> switch (newType) {
                case "point", "bullet", "laser" ->
                        bullet.lightning * bullet.lightningDamage * bullet.lightningLength * bullet.lightningLengthRand;
                case "continuousF", "continuousL" ->
                        bullet.lightning * bullet.lightningDamage * bullet.lightningLength *
                                bullet.lightningLengthRand * 1.2f * bullet.lifetime * (40 / bullet.damageInterval);
                case "lightning", "rail" ->
                        bullet.lightning * bullet.lightningDamage * bullet.lightningLength * bullet.lightningLengthRand * 1.2f;
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
            case "none" -> 0;
            default -> 100000;
        };
    }

    public void updateHeavy() {
        heavy = 0.5f;
        heavy += ProjectsLocated.getHeavy("damage", findVal("damage"));
        heavy += ProjectsLocated.getHeavy("pass", findVal("pass"));
        heavy += ProjectsLocated.getHeavy("prices", findVal("prices"));
        heavy += ProjectsLocated.getHeavy("splash", findVal("splash"));
        heavy += ProjectsLocated.getHeavy("lightning", findVal("lightning"));
        heavy += ProjectsLocated.getHeavy("percent", findVal("percent"));
        heavy += ProjectsLocated.getHeavy("frags", findVal("frags"));
    }
}