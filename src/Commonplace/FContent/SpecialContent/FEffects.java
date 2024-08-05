package Commonplace.FContent.SpecialContent;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.entities.Effect;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.graphics.Drawf;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;

public class FEffects {
    public static Effect lightning2 = new Effect(3, 500f, e -> {
        if (!(e.data instanceof Seq)) return;
        Seq<Vec2> lines = e.data();

        stroke(3f * e.fout());
        color(e.color, Color.white, e.fin());

        for (int i = 0; i < lines.size - 1; i++) {
            Vec2 cur = lines.get(i);
            Vec2 next = lines.get(i + 1);

            Lines.line(cur.x, cur.y, next.x, next.y, false);
        }

        for (Vec2 p : lines) {
            Fill.circle(p.x, p.y, Lines.getStroke() / 2f);
        }
    }), rockDown = new Effect(35, 300, e -> {
        float len = -260 * e.fout();
        float x = e.x + Angles.trnsx(e.rotation, len);
        float y = e.y + Angles.trnsy(e.rotation, len);
        float radius = 10 + 6 * e.fout();
        if (e.data instanceof TextureRegion region) {
            Draw.rect(region, x, y, radius, radius, e.rotation);
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
    }}, rockDownLarge = new Effect(105, 300, e -> {
        float len = -390 * e.fout();
        float x = e.x + Angles.trnsx(e.rotation, len);
        float y = e.y + Angles.trnsy(e.rotation, len);
        float radius = 24 + 20 * e.fout();
        if (e.data instanceof TextureRegion region) {
            Draw.rect(region, x, y, radius, radius, e.rotation);
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
    }}, laserLink = new Effect(25, f -> {
        if (f.data instanceof Vec2 v) {
            Draw.color(f.color);
            float stroke = 6 * (0.5f - Math.abs(f.fin() - 0.5f));
            Lines.stroke(stroke);
            Lines.line(f.x, f.y, v.x, v.y);
            Drawf.light(f.x, f.y, v.x, v.y, stroke + 1, f.color, 0.6f);
        }
    }), laserLinkLower = new Effect(25, f -> {
        if (f.data instanceof Vec2 v) {
            Draw.color(f.color);
            Lines.stroke(1);
            Vec2 from = new Vec2(v).sub(f.x, f.y), to = new Vec2(f.x, f.y);
            if (f.fin() <= 0.35f) {
                from.setLength(from.len() * f.fin() / 0.35f);
                to.add(from);
                from.set(f.x, f.y);
            } else if (f.fin() > 0.65f) {
                from.setLength(from.len() * f.fout() / 0.35f);
                to.set(v);
                from.scl(-1).add(to);
            } else {
                from.set(to);
                to.set(v);
            }
            Lines.line(from.x, from.y, to.x, to.y);
            Drawf.light(from.x, from.y, to.x, to.y, 4, f.color, 0.6f);
        }
    });
}
