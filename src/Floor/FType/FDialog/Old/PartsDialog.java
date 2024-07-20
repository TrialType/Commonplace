package Floor.FType.FDialog.Old;

import arc.Core;
import arc.func.Cons;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.entities.part.*;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.BaseDialog;

public class PartsDialog extends BaseDialog {
    protected Table listOn;
    protected Seq<DrawPart> parts = new Seq<>();

    protected static String dia = "part";

    public PartsDialog() {
        super(Core.bundle.get("dialog.part.warning"));

        shown(this::rebuild);
        buttons.button("@back", Icon.left, this::hide).width(200);
        buttons.button(Core.bundle.get("@add"), Icon.add, () -> {
            parts.add(new ShapePart());
            rebuildList();
        }).width(200);
    }

    public PartsDialog(Seq<DrawPart> parts) {
        this();
        setParts(parts);
    }

    public void setParts(Seq<DrawPart> parts) {
        this.parts = parts == null ? new Seq<>() : parts;
    }

    public void rebuild() {
        cont.clear();
        cont.pane(t -> listOn = t).width(1400);
        rebuildList();
    }

    public void rebuildList() {
        listOn.clear();
        for (int i = 0; i < parts.size; i++) {
            int finI = i;
            listOn.row();
            listOn.table(t -> {
                t.setBackground(Tex.buttonEdge1);
                rebuildPart(t, finI, findType(parts.get(finI), p -> parts.set(finI, p)), parts.get(finI));
            }).width(1400);
        }
    }

    public void rebuildPart(Table t, int index, String type, DrawPart part) {
        t.clear();
        t.pane(tt -> {
            tt.setBackground(Tex.buttonEdge1);
            tt.label(() -> Core.bundle.get("dialog.part." + type)).right().width(200).pad(5);
            tt.button(b -> {
                b.image(Icon.rotate);

                b.clicked(() -> ProjectUtils.createSelectDialog(b, (tb, hide) -> {
                    tb.top();
                    tb.button(Core.bundle.get("dialog.part.shape"), () -> {
                        if (type.equals("shape")) {
                            hide.run();
                            return;
                        }
                        ShapePart shapePart = new ShapePart();
                        shapePart.turretShading = part.turretShading;
                        shapePart.under = part.under;
                        shapePart.weaponIndex = part.weaponIndex;
                        shapePart.recoilIndex = part.recoilIndex;
                        parts.set(index, shapePart);
                        rebuildPart(t, index, "shape", parts.get(index));
                        hide.run();
                    }).width(100);
                    tb.row();
                    tb.button(Core.bundle.get("dialog.part.hover"), () -> {
                        if (type.equals("hover")) {
                            hide.run();
                            return;
                        }
                        HoverPart hoverPart = new HoverPart();
                        hoverPart.turretShading = part.turretShading;
                        hoverPart.under = part.under;
                        hoverPart.weaponIndex = part.weaponIndex;
                        hoverPart.recoilIndex = part.recoilIndex;
                        parts.set(index, hoverPart);
                        rebuildPart(t, index, "hover", parts.get(index));
                        hide.run();
                    }).width(100);
                    tb.row();
                    tb.button(Core.bundle.get("dialog.part.halo"), () -> {
                        if (type.equals("halo")) {
                            hide.run();
                            return;
                        }
                        HaloPart haloPart = new HaloPart();
                        haloPart.turretShading = part.turretShading;
                        haloPart.under = part.under;
                        haloPart.weaponIndex = part.weaponIndex;
                        haloPart.recoilIndex = part.recoilIndex;
                        parts.set(index, haloPart);
                        rebuildPart(t, index, "halo", parts.get(index));
                        hide.run();
                    }).width(100);
                    tb.row();
                    tb.button(Core.bundle.get("dialog.part.flare"), () -> {
                        if (type.equals("flare")) {
                            hide.run();
                            return;
                        }
                        FlarePart flarePart = new FlarePart();
                        flarePart.turretShading = part.turretShading;
                        flarePart.under = part.under;
                        flarePart.weaponIndex = part.weaponIndex;
                        flarePart.recoilIndex = part.recoilIndex;
                        parts.set(index, flarePart);
                        rebuildPart(t, index, "flare", parts.get(index));
                        hide.run();
                    }).width(100);
                }));
            }, () -> {
            }).right().pad(5);
            tt.button(Icon.trash, () -> {
                parts.remove(index);
                rebuildList();
            }).right().pad(5);
        }).width(1400);
        t.row();
        t.pane(b -> rebuildPartBase(b, part)).width(1400);
        t.row();
        t.pane(y -> rebuildPartType(y, type, part)).width(1400);
    }

    public void rebuildPartBase(Table t, DrawPart part) {
        t.clear();
        ProjectUtils.createMessageLine(t, dia, "default");
        t.row();
        ProjectUtils.createBooleanDialog(t, dia, "turretShading", part.turretShading,
                b -> part.turretShading = b, () -> rebuildPartBase(t, part));
        ProjectUtils.createBooleanDialog(t, dia, "under", part.under,
                b -> part.under = b, () -> rebuildPartBase(t, part));
        t.row();
        ProjectUtils.createNumberDialog(t, dia, "weaponIndex", part.weaponIndex,
                f -> part.weaponIndex = (int) (f + 0), () -> rebuildPartBase(t, part));
        ProjectUtils.createNumberDialog(t, dia, "recoilIndex", part.recoilIndex,
                f -> part.recoilIndex = (int) (f + 0), () -> rebuildPartBase(t, part));
    }

    public void rebuildPartType(Table t, String type, DrawPart part) {
        Runnable reb = () -> rebuildPartType(t, type, part);
        t.clear();
        ProjectUtils.createMessageLine(t, dia, "type");
        t.row();
        switch (type) {
            case "shape" -> {
                ShapePart shapePart = (ShapePart) part;
                ProjectUtils.createBooleanDialog(t, dia, "circle", shapePart.circle,
                        b -> shapePart.circle = b, reb);
                ProjectUtils.createBooleanDialog(t, dia, "hollow", shapePart.hollow,
                        b -> shapePart.hollow = b, reb);
                ProjectUtils.createBooleanDialog(t, dia, "mirror", shapePart.mirror,
                        b -> shapePart.mirror = b, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "x", shapePart.x,
                        f -> shapePart.x = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "y", shapePart.y,
                        f -> shapePart.y = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "rotation", shapePart.rotation,
                        f -> shapePart.rotation = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "moveX", shapePart.moveX,
                        f -> shapePart.moveX = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "moveY", shapePart.moveY,
                        f -> shapePart.moveY = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "moveRot", shapePart.moveRot,
                        f -> shapePart.moveRot = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "sides", shapePart.sides,
                        f -> shapePart.sides = (int) (f + 0), reb);
                ProjectUtils.createNumberDialog(t, dia, "radius", shapePart.radius,
                        f -> shapePart.radius = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "radiusTo", shapePart.radiusTo,
                        f -> shapePart.radiusTo = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "stroke", shapePart.stroke,
                        f -> shapePart.stroke = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "strokeTo", shapePart.strokeTo,
                        f -> shapePart.strokeTo = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "rotateSpeed", shapePart.rotateSpeed,
                        f -> shapePart.rotateSpeed = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "layer", shapePart.layer,
                        f -> shapePart.layer = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "layerOffset", shapePart.layerOffset,
                        f -> shapePart.layerOffset = f, reb);
                ProjectUtils.createPartProgressSelect(t, dia, "progress", p -> shapePart.progress = p);
                t.row();
                ProjectUtils.createColorDialog(t, dia, "color", shapePart.color,
                        c -> shapePart.color = c, reb);
                ProjectUtils.createColorDialog(t, dia, "colorTo", shapePart.colorTo,
                        c -> shapePart.colorTo = c, reb);
            }
            case "hover" -> {
                HoverPart hoverPart = (HoverPart) part;
                ProjectUtils.createNumberDialog(t, dia, "x", hoverPart.x,
                        f -> hoverPart.x = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "y", hoverPart.y,
                        f -> hoverPart.y = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "rotation", hoverPart.rotation,
                        f -> hoverPart.rotation = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "phase", hoverPart.phase,
                        f -> hoverPart.phase = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "stroke", hoverPart.stroke,
                        f -> hoverPart.stroke = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "minStroke", hoverPart.minStroke,
                        f -> hoverPart.minStroke = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "radius", hoverPart.radius,
                        f -> hoverPart.radius = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "circles", hoverPart.circles,
                        f -> hoverPart.circles = (int) (f + 0), reb);
                ProjectUtils.createNumberDialog(t, dia, "sides", hoverPart.sides,
                        f -> hoverPart.sides = (int) (f + 0), reb);
                t.row();
                ProjectUtils.createBooleanDialog(t, dia, "mirror", hoverPart.mirror,
                        b -> hoverPart.mirror = b, reb);
                ProjectUtils.createNumberDialog(t, dia, "layer", hoverPart.layer,
                        f -> hoverPart.layer = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "layerOffset", hoverPart.layerOffset,
                        f -> hoverPart.layerOffset = f, reb);
                t.row();
                ProjectUtils.createColorDialog(t, dia, "color", hoverPart.color,
                        c -> hoverPart.color = c, reb);
            }
            case "halo" -> {
                HaloPart haloPart = (HaloPart) part;
                ProjectUtils.createBooleanDialog(t, dia, "tri", haloPart.tri,
                        b -> haloPart.tri = b, reb);
                ProjectUtils.createBooleanDialog(t, dia, "hollow", haloPart.hollow,
                        b -> haloPart.hollow = b, reb);
                ProjectUtils.createBooleanDialog(t, dia, "mirror", haloPart.mirror,
                        b -> haloPart.mirror = b, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "radius", haloPart.radius,
                        f -> haloPart.radius = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "sides", haloPart.sides,
                        f -> haloPart.sides = (int) (f + 0), reb);
                ProjectUtils.createNumberDialog(t, dia, "radiusTo", haloPart.radiusTo,
                        f -> haloPart.radiusTo = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "shapes", haloPart.shapes,
                        f -> haloPart.shapes = (int) (f + 0), reb);
                ProjectUtils.createNumberDialog(t, dia, "stroke", haloPart.stroke,
                        f -> haloPart.stroke = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "strokeTo", haloPart.strokeTo,
                        f -> haloPart.strokeTo = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "x", haloPart.x,
                        f -> haloPart.x = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "y", haloPart.y,
                        f -> haloPart.y = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "shapeRotation", haloPart.shapeRotation,
                        f -> haloPart.shapeRotation = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "moveX", haloPart.moveX,
                        f -> haloPart.moveX = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "moveY", haloPart.moveY,
                        f -> haloPart.moveY = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "shapeMoveRot", haloPart.shapeMoveRot,
                        f -> haloPart.shapeMoveRot = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "haloRotateSpeed", haloPart.haloRotateSpeed,
                        f -> haloPart.haloRotateSpeed = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "haloRotation", haloPart.haloRotation,
                        f -> haloPart.haloRotation = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "rotateSpeed", haloPart.rotateSpeed,
                        f -> haloPart.rotateSpeed = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "triLength", haloPart.triLength,
                        f -> haloPart.triLength = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "triLengthTo", haloPart.triLengthTo,
                        f -> haloPart.triLengthTo = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "haloRadius", haloPart.haloRadius,
                        f -> haloPart.haloRadius = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "haloRadiusTo", haloPart.haloRadiusTo,
                        f -> haloPart.haloRadiusTo = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "layer", haloPart.layer,
                        f -> haloPart.layer = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "layerOffset", haloPart.layerOffset,
                        f -> haloPart.layerOffset = f, reb);
                t.row();
                ProjectUtils.createPartProgressSelect(t, dia, "progress", p -> haloPart.progress = p);
                ProjectUtils.createColorDialog(t, dia, "color", haloPart.color,
                        c -> haloPart.color = c, reb);
                ProjectUtils.createColorDialog(t, dia, "colorTo", haloPart.colorTo,
                        c -> haloPart.colorTo = c, reb);
            }
            case "flare" -> {
                FlarePart flarePart = (FlarePart) part;
                ProjectUtils.createNumberDialog(t, dia, "sides", flarePart.sides,
                        f -> flarePart.sides = (int) (f + 0), reb);
                ProjectUtils.createNumberDialog(t, dia, "radius", flarePart.radius,
                        f -> flarePart.radius = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "radiusTo", flarePart.radiusTo,
                        f -> flarePart.radiusTo = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "stroke", flarePart.stroke,
                        f -> flarePart.stroke = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "innerScl", flarePart.innerScl,
                        f -> flarePart.innerScl = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "innerRadScl", flarePart.innerRadScl,
                        f -> flarePart.innerRadScl = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "x", flarePart.x,
                        f -> flarePart.x = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "y", flarePart.y,
                        f -> flarePart.y = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "rotation", flarePart.rotation,
                        f -> flarePart.rotation = f, reb);
                t.row();
                ProjectUtils.createNumberDialog(t, dia, "rotMove", flarePart.rotMove,
                        f -> flarePart.rotMove = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "spinSpeed", flarePart.spinSpeed,
                        f -> flarePart.spinSpeed = f, reb);
                ProjectUtils.createNumberDialog(t, dia, "layer", flarePart.layer,
                        f -> flarePart.layer = f, reb);
                t.row();
                ProjectUtils.createBooleanDialog(t, dia, "followRotation", flarePart.followRotation,
                        b -> flarePart.followRotation = b, reb);
                ProjectUtils.createPartProgressSelect(t, dia, "progress", p -> flarePart.progress = p);
                ProjectUtils.createColorDialog(t, dia, "color1", flarePart.color1,
                        c -> flarePart.color1 = c, reb);
                t.row();
                ProjectUtils.createColorDialog(t, dia, "color2", flarePart.color2,
                        c -> flarePart.color2 = c, reb);
            }
        }
    }

    public String findType(DrawPart part, Cons<DrawPart> changer) {
        if (part instanceof ShapePart) {
            return "shape";
        } else if (part instanceof HoverPart) {
            return "hover";
        } else if (part instanceof HaloPart) {
            return "halo";
        } else if (part instanceof FlarePart) {
            return "flare";
        } else {
            changer.get(new ShapePart());
            return "shape";
        }
    }
}