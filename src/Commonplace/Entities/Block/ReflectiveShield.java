package Commonplace.Entities.Block;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
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
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

import static mindustry.Vars.*;
import static mindustry.gen.Groups.bullet;

public class ReflectiveShield extends Block {
    protected static final ObjectMap<Bullet, Float> angles = new ObjectMap<>();
    protected static final Vec2 vec = new Vec2();

    public float width = 300;
    public float height = 220;
    public float force = 0.2f;
    public float speedForce = 0.01f;
    public float peaceRange = 50;
    public float numUse = 0.25f;
    public float damageUse = 0.1f;
    public float maxDamage = 30;

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

    @Override
    public void setBars() {
        super.setBars();

        addBar("extraPower",
                (ReflectiveShieldBuild r) -> new Bar(Core.bundle.format("bar.extraPower", (r.powerUsing * 60)),
                        Pal.power, () -> Math.min(1, r.powerUsing / (r.power.graph.getLastPowerProduced() + 1))));
    }

    public class ReflectiveShieldBuild extends Building {
        protected float boost = 0;
        public float powerUsing = 0;

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

            powerUsing = 0;
            if (efficiency > 0) {
                if (rotation % 2 == 0) {
                    bullet.intersect(x + offset - height, y + offset - width,
                            boost * 2 * height, boost * 2 * width,
                            b -> {
                                if (b.team != this.team && !b.vel.isZero() && b.type.reflectable && !angles.containsKey(b)) {
                                    angles.put(b, b.vel.angle());
                                }
                            }
                    );
                } else {
                    bullet.intersect(x + offset - width, y + offset - height,
                            boost * 2 * width, boost * 2 * height,
                            b -> {
                                if (b.team != this.team && !b.vel.isZero() && b.type.reflectable && !angles.containsKey(b)) {
                                    angles.put(b, b.vel.angle());
                                }
                            }
                    );
                }

                removes.clear();
                angles.each((b, f) -> {
                    if (inRange(b)) {
                        if (!within(b, peaceRange)) b.keepAlive = true;
                        float mu = b.damage > maxDamage ? 0.2f : 1f;
                        vec.trns(f + 180, mu * (b.type.speed * speedForce * efficiency + force * efficiency));
                        b.vel.add(vec);
                        powerUsing += b.damage * damageUse / mu;
                    } else {
                        if (Angles.angleDist(b.vel.angle(), angles.get(b)) > 120) {
                            b.time = 0;
                            b.team = team;
                            b.owner = this;
                        }
                        removes.add(b);
                    }
                });
                for (Bullet b : removes) {
                    angles.remove(b);
                }

                if (angles.size > (1f / numUse)) {
                    powerUsing *= angles.size * numUse;
                }
            }

            this.power.graph.useBatteries(powerUsing);
        }

        public boolean inRange(Bullet b) {
            float w = rotation % 2 == 0 ? height : width;
            float h = rotation % 2 == 0 ? width : height;
            return Math.abs(b.x - x) <= w + b.hitSize * 2 && Math.abs(b.y - y) <= h + b.hitSize * 2;
        }

        @Override
        public void draw() {
            super.draw();
            Draw.z(Layer.blockAdditive);
            Draw.blend(Blending.additive);
            Draw.blend();
            Draw.reset();
            drawShield();
        }

        public void drawShield() {
            float bx = x + offset, by = y + offset;
            Draw.z(Layer.shields);
            if (rotation % 2 == 0) {
                Fill.rect(bx, by, height * boost * 2, width * boost * 2);
            } else {
                Fill.rect(bx, by, width * boost * 2, height * boost * 2);
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
