package Commonplace.Entities.Block;

import Commonplace.Loader.DefaultContent.Units2;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntMap;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.StatusEffects;
import mindustry.core.Renderer;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;
import mindustry.ui.Bar;
import mindustry.world.Block;

import static arc.util.Time.*;
import static mindustry.Vars.*;

public class ElectricFence extends Block {
    public int connect = 2;
    public float radius = 400;
    public float damage = 0.1f;
    public float statusTime = 240;
    public StatusEffect statusEffect = StatusEffects.burning;
    public float force = 90;
    public float reload = 600;
    public boolean air = false;

    public ElectricFence(String name) {
        super(name);

        destructible = true;
        configurable = true;
        update = solid = true;
        swapDiagonalPlacement = true;
        config(Integer.class, (build, inter) -> {
            if (build instanceof ElectricFenceBuild e) {
                Building b = world.build(inter);
                if (b instanceof ElectricFenceBuild eb && b.block instanceof ElectricFence ef) {
                    if (e.builds.contains(b.pos())) {
                        e.removeLink(eb);
                    } else if (e.builds.size < connect && eb.builds.size < ef.connect) {
                        e.addLink(eb, ef);
                    }
                }
            }
        });
    }

    protected void setupColor(float satisfaction) {
        Draw.color(Color.white, Pal.powerLight, (1f - satisfaction) * 0.86f + Mathf.absin(3f, 0.1f));
        Draw.alpha(Renderer.laserOpacity);
    }

    @Override
    public void setBars() {
        super.setBars();

        addBar("connections", (ElectricFenceBuild b) -> new Bar(() ->
                Core.bundle.format("bar.powerlines", b.builds.size, connect),
                () -> Pal.items,
                () -> (float) b.builds.size / (float) connect));
    }

    public class FenceLine {
        private float using;
        private float timer;
        private boolean broken;
        private boolean update;

        public Team team;

        public float x;
        public float y;
        public float half;
        public float rotate;

        public float width;

        public float max;
        public float time;
        public boolean air;
        public float damage;
        public float statusDuration;
        public StatusEffect statusEffect;


        public FenceLine(Team team, float max, Building b1, Building b2, float time, float damage, StatusEffect statusEffect, float statusDuration, float timer, float width, boolean air, boolean broken) {
            float x1 = b1.getX(), x2 = b2.getX(), y1 = b1.getY(), y2 = b2.getY();
            this.team = team;
            this.x = (x1 + x2) / 2;
            this.y = (y1 + y2) / 2;
            this.half = Mathf.dst(x1, y1, x2, y2) / 2;
            this.rotate = Angles.angle(x1, y1, x2, y2);
            this.max = max;
            this.time = time;
            this.damage = damage;
            this.statusEffect = statusEffect;
            this.statusDuration = statusDuration;
            this.air = air;
            this.width = width;
            this.timer = timer;
            this.broken = broken;
            update = false;
        }

        public void update(float efficiency) {
            if (update) {
                update = false;
            } else {
                if (!broken && efficiency > 0) {
                    using = 0;
                    Units.nearbyEnemies(team, x, y, half, u -> {
                        if (dest(u) && closing(u)) {
                            float ro = u.vel.angle() - rotate + 90;
                            float l = Mathf.cosDeg(ro) * u.vel.len() * Math.min(1, efficiency);
                            u.vel.sub(Mathf.cosDeg(rotate - 90) * l, Mathf.sinDeg(rotate - 90) * l);

                            using += u.hitSize * Math.abs(Mathf.cosDeg(ro) * Math.max(u.vel.len(), u.type.speed) * Math.min(1, efficiency));
                            u.damage(damage * efficiency);
                            u.apply(statusEffect, statusDuration * efficiency);
                            if (using >= max) {
                                broken = true;
                            }
                        }
                    });
                    if (broken) {
                        using = max;
                    }
                    float f = 1 - using / max;
                    if (width > f) {
                        width = Math.max(width - delta / 20, f);
                    } else {
                        width = Math.min(width + delta / 20, f);
                    }
                } else if (efficiency > 0) {
                    timer -= delta;
                    width = Math.max(width - delta / 20, 0.1f);
                    if (timer <= 0) {
                        broken = false;
                        timer = time;
                    }
                } else {
                    width = Math.max(width - delta / 20, 0.1f);
                }
                update = true;
            }
        }

        public boolean dest(Unit u) {
            if (!Units2.boss.contains(u.type)) {
                float ux = u.x;
                float uy = u.y;
                float angle = Angles.angleDist(rotate, Angles.angle(x, y, ux, uy));
                float len = Mathf.dst(ux, uy, x, y);
                if (Mathf.sinDeg(angle) * len <= size * tilesize / 2f && len * Math.abs(Mathf.cosDeg(angle)) <= half) {
                    if (u.physref == null || u.physref.body.layer < 3) {
                        return air || u.isGrounded();
                    }
                }
            }
            return false;
        }

        public boolean closing(Unit u) {
            float ux = u.x;
            float uy = u.y;
            float ua = u.vel.angle();
            Vec2 v = new Vec2().trns(rotate, half);
            float angle1 = Angles.angle(ux, uy, x + v.x, y + v.y);
            float angle2 = Angles.angle(ux, uy, x - v.x, y - v.y);
            float angle3 = Angles.angleDist(angle1, angle2);
            if (angle3 != 180 || !u.vel.isZero()) {
                float angle = rotate + 90;
                float angle4 = Angles.angleDist(angle1, angle);
                float angle5 = Angles.angleDist(angle2, angle);
                if (angle4 + angle5 > angle3) {
                    angle = rotate - 90;
                }
                return Angles.angleDist(ua, angle) < 90;
            }
            return false;
        }
    }

    public class ElectricFenceBuild extends Building {
        public final IntSeq builds = new IntSeq();
        public final IntMap<Float> times = new IntMap<>();
        public final IntMap<FenceLine> lines = new IntMap<>();

        @Override
        public void updateTile() {
            Seq<Integer> removes = new Seq<>();
            times.forEach(e -> {
                e.value -= Math.min(world.build(e.key).efficiency, efficiency) * delta / 2f;
                if (e.value <= 0) {
                    removes.add(e.key);
                }
            });
            removes.each(times::remove);

            lines.forEach(e -> e.value.update(Math.min(world.build(e.key).efficiency, efficiency)));
        }

        public void addLink(ElectricFenceBuild b, ElectricFence o) {
            if (b.builds.size < o.connect && builds.size < connect) {
                int p = b.pos();
                builds.add(p);
                b.builds.add(pos());
                addNewLine(b, o, p);
            }
        }

        public void removeLink(ElectricFenceBuild b) {
            int p = b.pos(), pos = pos();
            if (builds.contains(p)) {
                FenceLine l = lines.get(p);
                if (l.broken) {
                    times.put(p, l.timer);
                    b.times.put(pos, l.timer);
                }
                builds.removeValue(p);
                b.builds.removeValue(pos);
                lines.remove(p);
                b.lines.remove(pos);
            }
        }

        public void addNewLine(ElectricFenceBuild b, ElectricFence o, int p) {
            FenceLine l = new FenceLine(
                    team, Math.min(o.force, force), this, b,
                    Math.max(o.reload, reload), Math.min(o.damage, damage),
                    statusEffect, Math.min(o.statusTime, statusTime),
                    times.get(p, 0f), 0.1f, air && o.air, times.containsKey(p)
            );
            lines.put(p, l);
            b.lines.put(pos(), l);
        }

        public void addOldLine(ElectricFenceBuild b, ElectricFence o, int p, boolean broken, float width, float timer) {
            FenceLine l = new FenceLine(
                    team, Math.min(o.force, force), this, b,
                    Math.max(o.reload, reload), Math.min(o.damage, damage),
                    statusEffect, Math.min(o.statusTime, statusTime),
                    timer, width, air && o.air, broken
            );
            lines.put(p, l);
            b.lines.put(pos(), l);
        }

        public void reset() {
            fresh();
            indexer.eachBlock(team, getX(), getY(), radius,
                    b -> (b instanceof ElectricFenceBuild e && b.block instanceof ElectricFence o) &&
                            b != this && e.builds.size < o.connect && builds.size < connect && b.team == team,
                    b -> addLink((ElectricFenceBuild) b, (ElectricFence) b.block));
        }

        public void fresh() {
            for (int p : builds.toArray()) {
                if (world.build(p) instanceof ElectricFenceBuild b) {
                    removeLink(b);
                }
            }
        }

        public void clear() {
            fresh();
            times.forEach(e -> {
                ElectricFenceBuild b = (ElectricFenceBuild) world.build(e.key);
                b.times.remove(pos());
            });
            times.clear();
        }

        @Override
        public void onProximityRemoved() {
            super.onProximityRemoved();
            clear();
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (other == this) {
                if (builds.size > 0) {
                    fresh();
                } else {
                    reset();
                }
                return false;
            } else if (other instanceof ElectricFenceBuild && other.within(this, radius) && other.team == team) {
                configure(other.pos());
                return false;
            }
            return true;
        }

        @Override
        public void drawConfigure() {
            Drawf.circles(x, y, tile.block().size * tilesize / 2f + 1f + Mathf.absin(Time.time, 4f, 1f));
            Drawf.circles(x, y, radius);
            builds.each(p -> {
                Building b = world.build(p);
                Drawf.square(b.getX(), b.getY(), b.block.size * tilesize / 2f + 1f, Pal.place);
            });
        }

        @Override
        public void draw() {
            super.draw();

            builds.each(p -> {
                Building b = world.build(p);
                FenceLine l = lines.get(p);

                if (l != null && b != null) {
                    setupColor(l.width);
                    Lines.stroke(6 * l.width);
                    if (l.broken && l.width == 0.1f) {
                        Vec2 v = new Vec2(l.x - getX(), l.y - getY()).setLength(l.half * (1 - l.timer / l.time)).add(this);
                        Lines.line(getX(), getY(), v.x, v.y);
                        v.set(l.x - b.getX(), l.y - b.getY()).setLength(l.half * (1 - l.timer / l.time)).add(b);
                        Lines.line(b.getX(), b.getY(), v.x, v.y);
                    } else {
                        Lines.line(getX(), getY(), b.getX(), b.getY());
                    }
                }
            });
        }

        @Override
        public void created() {
            reset();
        }

        @Override
        public void write(Writes write) {
            write.i(builds.size);
            lines.forEach(e -> {
                write.i(e.key);
                write.f(e.value.width);
                write.f(e.value.timer);
                write.bool(e.value.broken);
            });
            write.i(times.size);
            times.forEach(e -> {
                write.i(e.key);
                write.f(e.value);
            });
        }

        @Override
        public void read(Reads read, byte revision) {
            int num = read.i();
            for (int i = 0; i < num; i++) {
                int p = read.i();
                float width = read.f();
                float timer = read.f();
                boolean broken = read.bool();
                builds.add(p);
                Building b = world.build(p);
                if (b instanceof ElectricFenceBuild e && b.block instanceof ElectricFence o) {
                    addOldLine(e, o, p, broken, width, timer);
                }
            }
            num = read.i();
            for (int i = 0; i < num; i++) {
                times.put(read.i(), read.f());
            }
        }
    }
}