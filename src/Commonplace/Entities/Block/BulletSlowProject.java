package Commonplace.Entities.Block;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class BulletSlowProject extends Block {
    public float range = 100;
    public float reload = 45;
    public float slow = 1;
    public float lowest = 0.2f;
    public float slowPercent = 0f;

    public Effect applyEffect = Fx.none;

    public BulletSlowProject(String name) {
        super(name);

        connectedPower = true;
        breakable = true;
        hasPower = true;
        update = true;
        solid = true;
        sync = true;
        destructible = true;
        group = BlockGroup.projectors;
        envEnabled = Env.any;

        setBars();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Draw.color(Pal.muddy);
        Lines.stroke(3f);
        Lines.circle(x * tilesize + offset, y * tilesize + offset, range);
        Draw.color(player.team().color);
        Lines.stroke(1f);
        Lines.circle(x * tilesize + offset, y * tilesize + offset, range);
        Draw.color();
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("", (BulletStopProjectBuild b) -> new Bar("bar.load", Pal.powerBar, () -> Math.min(1, b.timer / reload)));
    }


    public class BulletStopProjectBuild extends Building {
        float timer;

        @Override
        public void draw() {
            super.draw();

            Draw.color(Pal.muddy);

            if(renderer.animateShields){
                Draw.z(Layer.shields + 0.1f);
                Fill.circle(x, y, range);
            }else{
                Draw.z(Layer.shields + 0.1f);
                Lines.stroke(0.5f);
                Draw.alpha(0.17f);
                Fill.circle(x, y, range);
                Draw.alpha(0.3f);
                Lines.circle(x, y, range);
                Draw.reset();
            }
        }

        @Override
        public void updateTile() {
            timer += efficiency * Time.delta;
            if (timer > reload) {
                timer %= reload;
                applyEffect.at(x, y, rotation);

                Groups.bullet.each(b -> {
                    if (b.dst(this) <= range && b.team != team && b.vel.len() > lowest && b.type.speed > 0) {
                        float speed = b.vel.len() - b.type.speed * slowPercent - slow;
                        b.initVel(b.rotation(), Math.max(speed, lowest));
                    }
                });
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(timer);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            this.timer = read.f();
        }
    }
}
