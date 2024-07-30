package Floor.FType.FDialog.New;

import Floor.FContent.FItems;
import Floor.FTools.Lists.UnitProject;
import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.IntIntMap;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.state;
import static mindustry.Vars.ui;

public class ProjectDialog extends BaseDialog {
    public static ProjectDialog project;
    public IntIntMap map = new IntIntMap();
    public int heavy = 0;

    public ProjectDialog(String title) {
        super(title);

        hidden(() -> state.set(GameState.State.playing));
        shown(this::rebuild);

        buttons.button("@back", Icon.rightOpen, () -> {
            Unit u = Vars.player.unit();
            if (u != null && u.health > 0 && !u.dead && u.spawnedByCore) {
                if (couldApply()) {
                    applyProject(u);
                    hide();
                } else {
                    ui.showInfo(Core.bundle.get("@tooHeavy"));
                }
            } else {
                hide();
            }
        });
    }

    public static void create() {
        project = new ProjectDialog("");
    }

    public void rebuild() {
        cont.clear();
        cont.pane(t -> UnitProject.all.each(p ->
                t.table(l -> createLine(l, p, map.containsKey(p.id) ? map.get(p.id) : 0)).width(400))).width(500);
    }

    public void createLine(Table table, UnitProject project, int num) {
        table.clear();
        TextureRegion region = Core.atlas.find(project.icon, "ohno");
        table.add(new Image(region)).size(15).left().pad(5);
        table.button(b -> {
            b.image(Icon.down);
            b.setBackground(Tex.windowEmpty);
            b.clicked(() -> {
                if (num > 0) {
                    map.put(project.id, num - 1);
                    createLine(table, project, num - 1);
                }
            });
        }, () -> {
        }).width(60).pad(50);
        table.add(num + "").width(60).pad(5);
        table.button(b -> {
            b.image(Icon.add);
            b.setBackground(Tex.windowEmpty);
            b.clicked(() -> {
                map.put(project.id, num + 1);
                createLine(table, project, num + 1);
            });
        }, () -> {
        }).width(60).pad(5);
        table.label(() -> Core.bundle.format("@heavyShow", project.heavy, num)).width(100).pad(5);
        table.button("", () -> ui.showText("", project.description)).width(25).pad(15);
    }

    public boolean couldApply() {
        updateHeavy();
        return heavy <= maxHeavy();
    }

    public void updateHeavy() {
        heavy = 0;
        map.forEach(e -> heavy += (int) (UnitProject.get(e.key).heavy * e.value));
    }

    public int maxHeavy() {
        for (int i = FItems.allSize.length - 1; i >= 0; i--) {
            if (FItems.allSize[i].unlocked()) {
                return (int) Math.pow(2, i + 1);
            }
        }
        return 0;
    }

    public void applyProject(Unit u) {
        clearProject(u);
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

    public void setMap(IntIntMap map) {
        this.map = map;
    }
}
