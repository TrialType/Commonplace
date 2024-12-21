package Commonplace.Loader.Special;

import Commonplace.Type.Renders.WaveRenderer;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.entities.Effect;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;

public class Effects {
    public final static Rand rand = new Rand();

    private final static Cons<Effect.EffectContainer> LightningGrows = c -> {
        if (c.data instanceof EffectMultipleTimes e) {
            rand.setSeed(c.id);

            float reload = 1f / (rand.random(e.time) + 1);
            int point = (int) e.values[0];
            int time = (int) (c.fin() / reload);
            float fin = (c.fin() % reload) / reload;
            float radius = e.values[1];

            rand.setSeed(c.id + 100L * time);
            Seq<Vec2> points = findCircleLightningPoints(c.x, c.y, radius, radius * e.values[2], e.values[3], e.values[4], point);
            drawLightningMove(point, points, e.values[5], fin);
        }
    };

    public static Effect lightning2 = new Effect(3, 500f, c -> {
        if (!(c.data instanceof Seq)) return;
        Seq<Vec2> lines = c.data();

        stroke(3f * c.fout());
        color(c.color, Color.white, c.fin());

        for (int i = 0; i < lines.size - 1; i++) {
            Vec2 cur = lines.get(i);
            Vec2 next = lines.get(i + 1);

            Lines.line(cur.x, cur.y, next.x, next.y, false);
        }

        for (Vec2 p : lines) {
            Fill.circle(p.x, p.y, Lines.getStroke() / 2f);
        }
    }), rockDown = new Effect(35, 300, c -> {
        float len = -260 * c.fout();
        float x = c.x + Angles.trnsx(c.rotation, len);
        float y = c.y + Angles.trnsy(c.rotation, len);
        float radius = 10 + 6 * c.fout();
        if (c.data instanceof TextureRegion region) {
            Draw.rect(region, x, y, radius, radius, c.rotation);
        } else {
            Draw.color(Color.valueOf("ffdCa4"));
            Fill.circle(x, y, radius);
        }
    }), rockDownWave = new ExplosionEffect() {{
        lifetime = 180;

        waveRadBase = 0;
        waveRad = 130;
        waveStroke = 4;
        waveLife = 120;
        waveColor = Color.valueOf("f7cba4");

        smokeColor = Color.valueOf("ffdCa4");
        smokeSize = 22;
        smokes = 28;
        smokeSizeBase = 0;
        smokeRad = 130;

        sparks = 0;
    }}, rockDownLarge = new Effect(105, 300, c -> {
        float len = -390 * c.fout();
        float x = c.x + Angles.trnsx(c.rotation, len);
        float y = c.y + Angles.trnsy(c.rotation, len);
        float radius = 24 + 20 * c.fout();
        if (c.data instanceof TextureRegion region) {
            Draw.rect(region, x, y, radius, radius, c.rotation);
        } else {
            Draw.color(Color.valueOf("ffdCa4"));
            Fill.circle(x, y, radius);
        }
    }), rockDownWaveLarge = new ExplosionEffect() {{
        lifetime = 450;

        waveRadBase = 0;
        waveRad = 460;
        waveStroke = 7;
        waveLife = 280;
        waveColor = Color.valueOf("f7cba4");

        smokeColor = Color.valueOf("ffdCa4");
        smokeSize = 44;
        smokes = 28;
        smokeSizeBase = 0;
        smokeRad = 280;

        sparks = 0;
    }}, laserLink = new Effect(25, c -> {
        if (c.data instanceof Vec2 v) {
            Draw.color(c.color);
            float stroke = 6 * (0.5f - Math.abs(c.fin() - 0.5f));
            Lines.stroke(stroke);
            Lines.line(c.x, c.y, v.x, v.y);
            Drawf.light(c.x, c.y, v.x, v.y, stroke + 1, c.color, 0.6f);
        }
    }), laserLinkLower = new Effect(25, c -> {
        if (c.data instanceof Vec2 v) {
            Draw.color(c.color);
            Lines.stroke(1);
            Vec2 from = new Vec2(v).sub(c.x, c.y), to = new Vec2(c.x, c.y);
            if (c.fin() <= 0.35f) {
                from.setLength(from.len() * c.fin() / 0.35f);
                to.add(from);
                from.set(c.x, c.y);
            } else if (c.fin() > 0.65f) {
                from.setLength(from.len() * c.fout() / 0.35f);
                to.set(v);
                from.scl(-1).add(to);
            } else {
                from.set(to);
                to.set(v);
            }
            Lines.line(from.x, from.y, to.x, to.y);
            Drawf.light(from.x, from.y, to.x, to.y, 4, c.color, 0.6f);
        }
    }), boostDelay = new Effect(60, 60, c -> {
        rand.setSeed(c.id);

        Draw.color(Pal.accent);
        float len, rotate;
        for (int i = 0; i < 15; i++) {
            len = rand.random(60) * c.fout();
            rotate = rand.random(360);

            Fill.circle(c.x + Angles.trnsx(rotate, len), c.y + Angles.trnsy(rotate, len), c.fout() * rand.random(7));
        }
    }), boosting = new Effect(60, 250, c -> {
        Draw.color(c.color);
        float rotate = c.rotation;
        Fill.circle(c.x + Angles.trnsx(rotate + 90, c.finpow() * 25),
                c.y + Angles.trnsy(rotate + 90, c.finpow() * 25), 2);
        Fill.circle(c.x + Angles.trnsx(rotate - 90, c.finpow() * 25),
                c.y + Angles.trnsy(rotate - 90, c.finpow() * 25), 2);
    }), BombLarge = new Effect(40f, 800f, e -> {
        Color color = e.color;
        color(color);
        stroke(e.fout() * 5);
        float circleRad = 4f + e.finpow() * 520f;
        Lines.circle(e.x, e.y, circleRad);

        color(color);
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 48f, 800f * e.fout(), i * 90);
        }

        color();
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 23f, 280f * e.fout(), i * 90);
        }

        Drawf.light(e.x, e.y, circleRad * 1.6f, color, e.fout());
    }), BombMid = new Effect(40f, 120, e -> {
        Color color = e.color;
        color(color);
        stroke(e.fout() * 5);
        float circleRad = 4f + e.finpow() * 17f;
        Lines.circle(e.x, e.y, circleRad);

        color(color);
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 12f, 110f * e.fout(), i * 90);
        }

        color();
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 6f, 42f * e.fout(), i * 90);
        }

        Drawf.light(e.x, e.y, circleRad * 1.6f, color, e.fout());
    }), lightningDown = new Effect(15, c -> {
        rand.setSeed(c.id);
        Seq<Vec2> lines = new Seq<>();
        Draw.color(c.color);
        Lines.stroke(4 * c.fout());
        Lines.circle(c.x, c.y, c.fin() * 130);

        for (int j = 0; j < 7; j++) {
            findLineLightningPoints(c.x, c.y, 20, 20, 6, lines);

            stroke(8f * c.fout());
            color(c.color, c.color, c.fin());

            Fill.circle(c.x, c.y, Lines.getStroke() / 2f);
            for (int i = 0; i < (lines.size - 1) * c.fin(); i++) {
                Vec2 cur = lines.get(i);
                Vec2 next = lines.get(i + 1);
                Lines.line(cur.x, cur.y, next.x, next.y, false);

                Fill.circle(next.x, next.y, Lines.getStroke() / 2f);
            }
        }
    }), LightningCircle = new Effect(30, 30, c -> {
        float len = 25 * c.fin();
        Draw.color(c.color);
        Lines.stroke(1f);
        Lines.circle(c.x, c.y, len);

        Draw.color(c.color.cpy().mul(1.3f));
        Lines.stroke(0.5f);
        EffectMultipleTimes e = new EffectMultipleTimes(2, 7, len, 0.3f, 1.3f, 30, 0.55f);
        for (int i = 0; i < 8; i++) {
            int finalI = i;
            c.scaled(c.lifetime, r -> {
                r.id += finalI + 1;
                r.data = e;
                LightningGrows.get(r);
            });
        }
    }), LightningCircleLarge = new Effect(90, 80, c -> {
        float len = 60 * c.fin();
        Draw.color(c.color);
        Lines.stroke(1f);
        Lines.circle(c.x, c.y, len);

        Draw.color(c.color.cpy().mul(1.69f));
        Lines.stroke(1.2f);
        EffectMultipleTimes e = new EffectMultipleTimes(4, 45, len, 0.05f, 1.2f, 15, 0.55f);
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            c.scaled(c.lifetime, r -> {
                r.id += finalI + 1;
                r.data = e;
                LightningGrows.get(r);
            });
        }
    }), ball = new Effect(150, 120, c -> {
        Draw.z(Layer.effect);
        Color color = Pal.suppress;
        float rad = Math.min(1, c.fin()) * (4 + Mathf.absin(8f, 1f));
        float x = c.x, y = c.y;

        Lines.stroke(2f);
        Draw.color(color);

        Lines.circle(x, y, rad);
        Fill.circle(x, y, rad * 0.33f);
        Drawf.light(x, y, rad, color, 4);
        Draw.reset();
    }) {{
        rotWithParent = true;
    }}, lightningSmallIn = new Effect(30, 20, c -> {
        Draw.z(Layer.flyingUnit + 1);
        Draw.color(Color.white, c.color, Color.white, c.fin());
        Lines.stroke(1.2f);

        int point = 20;
        Seq<Vec2> points = new Seq<>(point);
        rand.setSeed(c.id);
        findLineLightningPoints(c.x, c.y, 30, 2f, point, points);
        points.reverse();

        drawLightningMove(point, points, 0.55f, c.fin());
    }) {{
        rotWithParent = true;
    }}, lightningSmallOut = new Effect(30, 20, c -> {
        Draw.z(Layer.flyingUnit + 1);
        Draw.color(Color.white, c.color, Color.white, c.fin());
        Lines.stroke(1.2f);

        int point = 20;
        Seq<Vec2> points = new Seq<>(point);
        rand.setSeed(c.id);
        findLineLightningPoints(c.x, c.y, 30, 2f, point, points);

        drawLightningMove(point, points, 0.55f, c.fin());
    }) {{
        rotWithParent = true;
    }}, lockShake = new Effect(60, 180, c -> {
        float fin4 = Interp.pow4.apply(c.fin());
        WaveRenderer.addPlace(c.x, c.y, 160 * fin4, 25 + 15 * c.fin(), Color.valueOf("879BA3").a(1 - fin4));
    }) {{
        followParent = true;
    }};

    public static void drawLightningMove(int num, Seq<Vec2> points, float grow, float fin) {
        int from = fin <= grow ? 0 : (int) ((num - 1) * (fin - grow) / (1 - grow));
        int to = fin <= grow ? (int) ((num - 1) * (fin / grow)) : num - 1;
        Fill.circle(points.get(from).x, points.get(from).y, Lines.getStroke() / 2);
        for (int i = from; i < to; i++) {
            Vec2 p1 = points.get(i);
            Vec2 p2 = points.get(i + 1);
            Lines.line(p1.x, p1.y, p2.x, p2.y, false);

            Fill.circle(p2.x, p2.y, Lines.getStroke() / 2f);
        }
    }

    public static Seq<Vec2> findCircleLightningPoints(float x, float y, float radius, float len, float outScl, float rotateStep, int point) {
        Seq<Vec2> points = new Seq<>();
        int step = rand.chance(0.5f) ? 1 : -1;
        float rotation = rand.range(360);
        float dr;
        float px = x + Angles.trnsx(rotation, radius), py = y + Angles.trnsy(rotation, radius);
        points.add(new Vec2(px, py));

        for (int i = 0; i < point; i++) {
            rotation = Angles.angle(px, py, x, y);
            if (Mathf.dst(px, py, x, y) > radius * outScl) {
                dr = step * (90 - rotateStep);
            } else {
                dr = step * (90 + rand.range(rotateStep));
            }
            px += Angles.trnsx(rotation + dr, len) + rand.range(len / 2);
            py += Angles.trnsy(rotation + dr, len) + rand.range(len / 2);
            points.add(new Vec2(px, py));
        }

        return points;
    }

    public static void findLineLightningPoints(float x, float y, float rotationDes, float length, int points, Seq<Vec2> lines) {
        if (lines != null) {
            lines.clear();
            lines.add(new Vec2(x, y));
            float rotation = rand.range(360);
            for (int i = 1; i < points; i++) {
                lines.add(new Vec2(x + rand.range(length / 2f), y + rand.range(length / 2f)));
                rotation += rand.range(rotationDes);
                x += Angles.trnsx(rotation, length);
                y += Angles.trnsy(rotation, length);
            }
        }
    }

    public static void findLineLightningPoints(float x, float y, float rotation, float rotationDes, float length, int points, Seq<Vec2> lines) {
        if (lines != null) {
            lines.clear();
            lines.add(new Vec2(x, y));
            for (int i = 1; i < points; i++) {
                lines.add(new Vec2(x + rand.range(length / 2f), y + rand.range(length / 2f)));
                rotation += rand.range(rotationDes);
                x += Angles.trnsx(rotation, length);
                y += Angles.trnsy(rotation, length);
            }
        }
    }

    static class EffectMultipleTimes {
        int time;
        float[] values;

        public EffectMultipleTimes(int time, float... values) {
            this.time = time;
            this.values = values;
        }
    }
}
