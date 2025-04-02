package Commonplace.Entities.Block;

import Commonplace.Utils.Interfaces.RangePure;
import Commonplace.Type.Control.CorrosionMist;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;

import static mindustry.Vars.world;

public class PureProject extends Block {
    public float protectRange = 200;
    public int level = -3;

    public PureProject(String name) {
        super(name);
        solid = true;
        update = true;
        breakable = destructible = true;
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("power", build -> new Bar(
                () -> Core.bundle.format("stat.pure-power", (int) (build.efficiency * level)) +
                        ":" + (int) (build.efficiency * level),
                () -> Pal.breakInvalid,
                () -> build.efficiency));
    }

    public class PureBuild extends Building implements RangePure {
        protected final IntSet protects = new IntSet();

        @Override
        public void updateTile() {
            if (!CorrosionMist.changer.contains(this) && couldUse()) {
                CorrosionMist.addChanger(this);
            }
        }

        @Override
        public void created() {
            CorrosionMist.changer.add(this);
            float fx = this.x + offset, fy = this.y + offset;
            for (float px = -protectRange; px <= protectRange; px += Vars.tilesize) {
                for (float py = -protectRange; py <= protectRange; py += Vars.tilesize) {
                    if (Mathf.dst(px, py) <= 3 + protectRange) {
                        Tile t = world.tileWorld(px + fx, py + fy);
                        if (t != null) {
                            protects.add(t.pos());
                        }
                    }
                }
            }
            CorrosionMist.addChanger(this);
        }

        @Override
        public void remove() {
            super.remove();
            CorrosionMist.removeChanger(this);
        }

        @Override
        public void draw() {
            super.draw();
            Draw.z(Layer.floor + 0.1f);
            Draw.alpha(0.07f);
            Lines.stroke(2);
            Fill.circle(x, y, protectRange);
        }

        @Override
        public void read(Reads read, byte version) {
            super.read(read, version);
            int n = read.i();
            for (int i = 0; i < n; i++) {
                protects.add(read.i());
            }
            CorrosionMist.addChanger(this);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(protects.size);
            protects.each(write::i);
        }

        @Override
        public int level() {
            return enabled ? (int) (level * efficiency) : 0;
        }

        @Override
        public IntSet changes() {
            return protects;
        }

        @Override
        public boolean couldUse() {
            return this.added && this.health > 0 && !this.dead && enabled && efficiency > 0;
        }
    }
}
