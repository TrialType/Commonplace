package Commonplace.Type.Renders;

import Commonplace.Type.Shaders.WaveShader;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.struct.Seq;
import mindustry.game.EventType;
import mindustry.graphics.Layer;

import static arc.Core.graphics;

public class WaveRenderer {
    public static boolean init = false;
    public static int num = 1;
    public static WaveShader shader;
    public final static Seq<WavePlace> places = new Seq<>();
    public static FrameBuffer buffer = new FrameBuffer();

    public static void init() {
        init = true;

        Events.run(EventType.Trigger.draw, WaveRenderer::WaveDraw);
    }

    public static void WaveDraw() {
        Draw.draw(Layer.background - 1, () -> {
            buffer.resize(graphics.getWidth(), graphics.getHeight());
            buffer.begin();
        });

        Draw.draw(Layer.weather - 1, () -> {
            buffer.end();

            float[] waves = new float[places.size * 4];
            float[] color = new float[places.size * 4];

            for (int i = 0; i < places.size; i++) {
                WavePlace place = places.get(i);
                waves[i * 4] = place.x;
                waves[i * 4 + 1] = place.y;
                waves[i * 4 + 2] = place.range;
                waves[i * 4 + 3] = place.width;

                color[i * 4] = place.color.r;
                color[i * 4 + 1] = place.color.g;
                color[i * 4 + 2] = place.color.b;
                color[i * 4 + 3] = place.color.a;
            }

            if (shader == null) {
                Shader.prependFragmentCode = "#define NUM " + num + "\n";
                shader = new WaveShader();
            }
            if (places.size > num) {
                num *= 2;
                Shader.prependFragmentCode = "#define NUM " + num + "\n";
                shader = new WaveShader();
            }

            shader.waves = waves;
            shader.color = color;

            buffer.blit(shader);

            places.clear();
        });
    }

    public static void addPlace(float x, float y, float range, float width, Color color) {
        if (!init) init();
        places.add(new WavePlace(x, y, range, width, color));
    }

    public static class WavePlace {
        float x, y;
        float range;
        float width;
        Color color;

        public WavePlace(float x, float y, float range, float width, Color color) {
            this.x = x;
            this.y = y;
            this.range = range;
            this.width = width;
            this.color = color;
        }
    }
}
