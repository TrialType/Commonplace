package Commonplace.Entities.BulletType;

import Commonplace.Loader.Special.Effects;
import Commonplace.Utils.Classes.Damage2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.core.World;
import mindustry.entities.Effect;
import mindustry.entities.bullet.ContinuousBulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Healthc;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import static mindustry.Vars.*;

public class ContinuousLightningBulletType extends ContinuousBulletType {
    private static final ObjectMap<Bullet, Seq<HitTimer>> hits = new ObjectMap<>();
    private static final Rand random = new Rand();
    private static final IntSet hit = new IntSet();
    private static final float hitRange = 30f;
    private static boolean round = false;
    private float time;

    public float radius = 6.25f;
    public float rotateSpeed = 5;
    public float reload = 60;
    public Color baseColor = Pal.heal;
    public Color bulletLightningColor = Pal.heal;

    @Override
    public void update(Bullet b) {
        Seq<HitTimer> ids = hits.get(b);
        ids.remove(h -> h.entity == null || (h.entity instanceof Healthc heal && (heal.dead() || heal.health() <= 0)));
        ids.each(HitTimer::update);

        b.vel.setZero();
        if ((!b.timer.check(3, time)) || (b.timer.get(3, time) && b.timer.get(4, reload))) {
            if (!(b.data instanceof Seq<?>)) {
                ids.each(h -> h.set(damageInterval));
                b.data = create(b.team, b.rotation(), b.id);
            }
            if (!continuous) return;
            applyDamage(b);
            if (shake > 0) Effect.shake(shake, shake, b);
            updateBulletInterval(b);
            //noinspection unchecked
            Effects.lightning2.at(b.x, b.y, b.rotation(), bulletLightningColor, points(((Seq<Vec2>) b.data), b.x, b.y));
            //noinspection unchecked
            rotate((Seq<Vec2>) b.data);

            b.timer.reset(4, 0);
        } else {
            b.data = null;
            b.timer.reset(3, time + 1);
        }
    }

    public void rotate(Seq<Vec2> vs) {
        Vec2 c = new Vec2();
        for (Vec2 v : vs) {
            c.trns(v.angle() + rotateSpeed, v.len());
            v.set(c);
        }
    }

    public Seq<Vec2> points(Seq<Vec2> def, float x, float y) {
        Seq<Vec2> re = new Seq<>(def.size);
        for (Vec2 v : def) {
            Vec2 n = new Vec2();
            n.set(x + v.x, y + v.y);
            re.add(n);
        }
        return re;
    }

    @Override
    public void applyDamage(Bullet b) {
        if (b.data instanceof Seq<?>) {
            //noinspection unchecked
            Seq<Vec2> vs = points((Seq<Vec2>) b.data, b.x, b.y);
            Vec2 before = null;
            for (Vec2 v : vs) {
                if (before != null) {
                    Damage2.collideLineInterval(b, b.team, hitEffect, before.x, before.y, before.angleTo(v), before.dst(v), false, false, -1,
                            i -> hits.get(b).add(new HitTimer(i, damageInterval)),
                            i -> !hits.get(b).contains(h -> h.entity == i),
                            i -> hits.get(b).contains(h -> h.entity == i && h.get(damageInterval)));
                }
                before = v;
//                FDamage.collidePointInterval(b, b.team, hitEffect, v.x, v.y,
//                        i -> hits.get(b).add(new HitTimer(i, damageInterval)),
//                        i -> !hits.get(b).contains(h -> h.entity == i),
//                        i -> hits.get(b).contains(h -> h.entity == i && h.get(damageInterval)));
            }
        }
    }

    @Override
    public void draw(Bullet b) {
        super.draw(b);

        if (radius > 0) {
            Draw.color(baseColor);
            Fill.circle(b.x, b.y, radius * b.fout());
        }
    }

    @Override
    public void init() {
        super.init();

        time = 400 / rotateSpeed;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);
        hits.put(b, new Seq<>());
    }

    @Override
    public void removed(Bullet b) {
        super.removed(b);
        hits.get(b).clear();
        hits.remove(b);
    }

    public Seq<Vec2> create(Team team, float rotation, int seed) {
        random.setSeed(seed);
        hit.clear();

        Seq<Vec2> lines = new Seq<>((int) (length / 2) + 2);
        lines.add(new Vec2(0, 0));
        round = false;

        float x = 0, y = 0;
        for (int i = 0; i < length / 2; i++) {
            lines.add(new Vec2(x + Mathf.range(3f), y + Mathf.range(3f)));

            if (lines.size > 1) {
                round = false;
                Vec2 from = lines.get(lines.size - 2);
                Vec2 to = lines.get(lines.size - 1);
                World.raycastEach(World.toTile(from.getX()), World.toTile(from.getY()), World.toTile(to.getX()), World.toTile(to.getY()), (wx, wy) -> {

                    Tile tile = world.tile(wx, wy);
                    if (tile != null && (tile.build != null && tile.build.isInsulated()) && tile.team() != team) {
                        round = true;
                        //snap it instead of removing
                        lines.get(lines.size - 1).set(wx * tilesize, wy * tilesize);
                        return true;
                    }
                    return false;
                });
                if (round) break;
            }

            rotation += random.range(20f);
            x += Angles.trnsx(rotation, hitRange / 2f);
            y += Angles.trnsy(rotation, hitRange / 2f);
        }

        return lines;
    }

    public static class HitTimer {
        public Entityc entity;
        public float timer;

        public HitTimer(Entityc entity, float timer) {
            this.entity = entity;
            this.timer = timer;
        }

        public void update() {
            timer += Time.delta;
        }

        public boolean get(float reload) {
            if (timer >= reload) {
                timer = 0;
                return true;
            }
            return false;
        }

        public void set(float timer) {
            this.timer = timer;
        }
    }
}
