package Commonplace.UI.Dialogs;

import Commonplace.Loader.DefaultContent.StatusEffects2;
import Commonplace.Loader.ProjectContent.Sign;
import Commonplace.Type.Content.BoostProject;
import Commonplace.Type.Content.UnitProject;
import arc.Core;
import arc.func.Cons;
import arc.func.Intc;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.IntIntMap;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import java.util.Iterator;

import static mindustry.Vars.*;

public class ProjectDialog extends BaseDialog {
    public static ProjectDialog project;
    public IntIntMap map = new IntIntMap();
    public IntIntMap boostMap = new IntIntMap();
    public int heavy = 0;
    public Cons<IntIntMap> write1 = m -> {
    };
    public Cons<IntIntMap> write2 = m -> {
    };

    public ProjectDialog(String title) {
        super(title);

        hidden(() -> state.set(GameState.State.playing));
        hidden(() -> write1.get(map));
        hidden(() -> write2.get(boostMap));
        shown(this::rebuild);

        buttons.button("@back", Icon.left, () -> {
            if (state.isEditor()) {
                hide();
            }
            Unit u = Vars.player.unit();
            if (u != null && u.health > 0 && !u.dead && u.spawnedByCore && state.isGame()) {
                if (heavyPass() && numPass()) {
                    applyProject(u);
                    player.unit().apply(StatusEffects2.strongStop, 180);
                    hide();
                } else if (!heavyPass()) {
                    ui.showInfo(Core.bundle.get("@tooHeavy"));
                } else {
                    ui.showInfo(Core.bundle.get("@tooMany"));
                }
            } else {
                hide();
            }
        }).width(200);

        buttons.button("@edit", Icon.edit, () -> {
            BaseDialog dialog = new BaseDialog("@editor.export");
            dialog.cont.pane(p -> {
                p.margin(10f);
                p.table(Tex.button, t -> {
                    TextButton.TextButtonStyle style = Styles.flatt;
                    t.defaults().size(280f, 60f).left();

                    t.button("@schematic.copy", Icon.copy, style, () -> {
                        dialog.hide();
                        Core.app.setClipboardText(toString());
                    }).marginLeft(12f);
                    t.row();
                    t.button("@schematic.copy.import", Icon.download, style, () -> {
                        dialog.hide();
                        load(Core.app.getClipboardText().replace("\r\n", "\n"));
                    }).marginLeft(12f).disabled(b -> Core.app.getClipboardText() == null);
                });
            });

            dialog.addCloseButton();
            dialog.show();
        }).name("edit").width(200);
    }

    public static void create() {
        project = new ProjectDialog("");
    }

    public void rebuild() {
        cont.clear();
        cont.pane(table -> {
            table.table(this::createHeavy).width(1000).row();
            table.table(t -> {
                for (UnitProject p : UnitProject.all) {
                    if (p.unlocked() || state.isEditor() || state.rules.infiniteResources) {
                        if (!(p instanceof BoostProject)) {
                            t.table(l -> createLine(l, p, i -> map.put(p.sid, i),
                                    map.containsKey(p.sid) ? map.get(p.sid) : 0)).width(900).row();
                        }
                    }
                }
            }).width(1000).row();
            table.table(t -> {
                for (UnitProject p : UnitProject.all) {
                    if (p.unlocked() || state.isEditor() || state.rules.infiniteResources) {
                        if (p instanceof BoostProject b) {
                            t.table(l -> createLine(l, p, i -> boostMap.put(b.sid, i),
                                    boostMap.containsKey(b.sid) ? boostMap.get(b.sid) : 0)).width(900).row();
                        }
                    }
                }
            }).width(1000);
        });
    }

    public void createHeavy(Table table) {
        updateHeavy();
        table.label(() -> Core.bundle.format("@heavyShow", heavy, maxHeavy())).width(200);
    }

    public void createLine(Table table, UnitProject project, Intc put, int num) {
        table.clear();
        TextureRegion region = project.uiIcon;
        table.add(new Image(region)).size(30).left().pad(5);
        table.add(project.localizedName).width(200).pad(35);
        table.button(b -> {
            b.image(Icon.down);
            b.setBackground(Tex.windowEmpty);
            b.clicked(() -> {
                if (num > 0) {
                    put.get(num - 1);
                    createLine(table, project, put, num - 1);
                }
            });
        }, () -> {
        }).size(30).pad(5);
        table.add(num + "").width(25).pad(10);
        table.button(b -> {
            b.image(Icon.up);
            b.setBackground(Tex.windowEmpty);
            b.clicked(() -> {
                if (UnitProject.get(project.sid).max > num) {
                    put.get(num + 1);
                    createLine(table, project, put, num + 1);
                }
            });
        }, () -> {
        }).size(30).pad(5);
        table.add(Core.bundle.format("@maxValue", UnitProject.get(project.sid).max)).width(100).pad(10);
        table.label(() -> Core.bundle.format("@heavyShow", project.heavy, num)).width(170).pad(25);
        table.button("", () -> ui.showText("", project.description)).size(30).pad(25);
        updateHeavy();
    }

    public boolean heavyPass() {
        updateHeavy();
        return heavy <= maxHeavy();
    }

    public boolean numPass() {
        Iterator<IntIntMap.Entry> iterator = map.entries().iterator();
        IntIntMap.Entry entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (UnitProject.get(entry.key).max < entry.value) {
                return false;
            }
        }
        iterator = boostMap.entries();
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (UnitProject.get(entry.key).max < entry.value) {
                return false;
            }
        }
        return true;
    }

    public void updateHeavy() {
        heavy = 0;
        if (!map.isEmpty()) {
            map.forEach(e -> heavy += (int) (UnitProject.get(e.key).heavy * e.value));
        }
        if (!boostMap.isEmpty()) {
            boostMap.forEach(e -> heavy += (int) (UnitProject.get(e.key).heavy * e.value));
        }
    }

    public int maxHeavy() {
        if(state.rules.infiniteResources){
            return 1 + (int) Math.pow(2, Sign.allSize.length);
        }
        for (int i = Sign.allSize.length - 1; i >= 0; i--) {
            if (Sign.allSize[i].unlocked()) {
                return 1 + (int) Math.pow(2, i + 1);
            }
        }
        return 1;
    }

    public void applyProject(Unit u) {
        clearProject(u);
        BoostProject.updateApply(boostMap);
        map.forEach(e -> {
            UnitProject p = UnitProject.get(e.key);
            for (int i = 0; i < e.value; i++) {
                p.applier.get(u);
            }
        });
    }

    public void clearProject(Unit u) {
        WeaponMount[] mount = new WeaponMount[u.type.weapons.size];
        System.arraycopy(u.mounts, 0, mount, 0, mount.length);
        u.mounts = mount;
        Ability[] ability = new Ability[u.type.abilities.size];
        System.arraycopy(u.abilities, 0, ability, 0, ability.length);
        u.abilities = ability;
    }

    public void setMap(IntIntMap map, IntIntMap boostMap) {
        this.map = map;
        this.boostMap = boostMap;
    }

    public void setWrite(Cons<IntIntMap> write1, Cons<IntIntMap> write2) {
        this.write1 = write1;
        this.write2 = write2;
    }

    public void load(String s) {
        if (s.startsWith("[UnitProject]")) {
            map.clear();
            boostMap.clear();
            String[] str = s.split("\n");
            String[] values;
            boolean first = true;
            for (String string : str) {
                if (!string.equals("[UnitProject]")) {
                    if (string.endsWith("[map1]")) {
                        first = true;
                    } else if (string.endsWith("[map2]")) {
                        first = false;
                    } else if (string.endsWith("[end]")) {
                        rebuild();
                    } else if (first) {
                        values = string.split("--");
                        map.put(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    } else {
                        values = string.split("--");
                        boostMap.put(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[UnitProject]");
        builder.append("\n");
        builder.append("[map1]");
        map.forEach(e -> {
            builder.append("\n");
            builder.append(e.key);
            builder.append("--");
            builder.append(e.value);
        });
        builder.append("\n");
        builder.append("[map2]");
        boostMap.forEach(e -> {
            builder.append("\n");
            builder.append(e.key);
            builder.append("--");
            builder.append(e.value);
        });
        builder.append("\n");
        builder.append("[end]");

        return builder.toString();
    }
}
