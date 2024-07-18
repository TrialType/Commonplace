package Floor.FEntities.FBlock;

import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

import static mindustry.Vars.*;
import static mindustry.gen.Groups.bullet;

public class ReflectiveShield extends Block {
    protected static final ObjectMap<Bullet, Float> angles = new ObjectMap<>();
    protected static final Vec2 vec = new Vec2();

    public float width = 200;
    public float height = 150;
    public float force = 0.2f;

    public ReflectiveShield(String name) {
        super(name);

        hasPower = true;
        rotate = true;
        update = true;
        solid = true;
        sync = true;
        destructible = true;
        group = BlockGroup.projectors;
        envEnabled = Env.any;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        Draw.color(Pal.range);
        Lines.stroke(2f);
        float bx = x * tilesize + offset, by = y * tilesize + offset;
        if (rotation % 2 == 0) {
            Lines.rect(bx - height, by - width, height * 2, width * 2);
        } else {
            Lines.rect(bx - width, by - height, width * 2, height * 2);
        }
        Draw.color();
    }

    public class ReflectiveShieldBuild extends Building {
        protected float boost = 0;

        @Override
        public void updateTile() {
            if (efficiency > 0) {
                boost = Mathf.lerpDelta(boost, 1, 1f);
            } else {
                boost = Mathf.lerpDelta(boost, 0, 1f);
            }

            Seq<Bullet> removes = new Seq<>();
            angles.each((b, f) -> {
                if (b == null || !b.isAdded()) {
                    removes.add(b);
                }
            });
            for (Bullet b : removes) {
                angles.remove(b);
            }

            if (efficiency > 0) {
                if (rotation % 2 == 0) {
                    bullet.intersect(x + offset - height, y + offset - width,
                            boost * 2 * height, boost * 2 * width,
                            b -> {
                                if (b.team != this.team && b.type.reflectable && !angles.containsKey(b)) {
                                    angles.put(b, b.vel.angle());
                                }
                            }
                    );
                } else {
                    bullet.intersect(x + offset - width, y + offset - height,
                            boost * 2 * width, boost * 2 * height,
                            b -> {
                                if (b.team != this.team && b.type.reflectable && !angles.containsKey(b)) {
                                    angles.put(b, b.vel.angle());
                                }
                            }
                    );
                }
            }

            angles.each((b, f) -> {
                if (inRange(b)) {
                    vec.trns(-f, force * efficiency);
                    b.vel.add(vec);
                } else {
                    b.time = 0;
                    b.team = team;
                    b.owner = this;
                    removes.add(b);
                }
            });
            for (Bullet b : removes) {
                angles.remove(b);
            }
        }

        public boolean inRange(Bullet b) {



            return true;
        }

        @Override
        public void draw() {
            super.draw();
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.blend();
            Draw.z(Layer.block);
            Draw.reset();
            drawShield();
        }

        public void drawShield() {
            float bx = x + offset, by = y + offset;
            if (rotation % 2 == 0) {
                Lines.rect(bx - height * boost, by - width * boost, height * boost * 2, width * boost * 2);
            } else {
                Lines.rect(bx - width * boost, by - height * boost, width * boost * 2, height * boost * 2);
            }
            Draw.reset();
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(boost);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            this.boost = read.f();
        }
    }
}
