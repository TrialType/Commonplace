package Commonplace.Utils.Classes;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public abstract class InputListener {
    public static int lastClickX, lastClickY, mx, my, width, height;
    public static Seq<Building> buildings = new Seq<>();

    public static void update() {
        if (Vars.state.isEditor() && Core.input.keyDown(KeyCode.z)) {
            int x = Mathf.round(Core.input.mouseWorldX() / Vars.tilesize), y = Mathf.round(Core.input.mouseWorldY() / Vars.tilesize);
            if (lastClickX < 0 && lastClickY < 0) {
                lastClickX = x;
                lastClickY = y;
            }

            buildings.clear();
            Team t = Vars.player.team();
            mx = Math.min(x, lastClickX);
            my = Math.min(y, lastClickY);
            width = x + lastClickX - 2 * mx;
            height = y + lastClickY - 2 * my;
            for (int i = 0; i <= width; i ++) {
                for (int j = 0; j <= height; j ++) {
                    Building b = world.build(mx + i, my + j);
                    if (b != null && !buildings.contains(b) && b.team != t) {
                        buildings.add(b);
                    }
                }
            }
        } else {
            Team t = Vars.player.team();
            buildings.each(b -> b.changeTeam(t));

            reset();
        }
    }

    public static void draw() {
        if (lastClickX >= 0 && lastClickY >= 0) {
            Lines.stroke(1);
            Draw.color(Color.red.cpy().mul(Color.gray).mul(Color.blue));
            Lines.rect(mx * tilesize, my * tilesize, width * tilesize, height * tilesize, 0, 0);

            buildings.each(b -> Fill.circle(b.x, b.y, b.block.size * 2));
        }
    }

    public static void reset() {
        mx = my = -1;
        buildings.clear();
        width = height = -1;
        lastClickX = lastClickY = -1;
    }
}
