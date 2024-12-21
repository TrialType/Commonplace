package Commonplace.Type.Renders;

import Commonplace.Type.Shaders.WaveShader;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Bloom;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.struct.Seq;
import mindustry.game.EventType;
import mindustry.graphics.Layer;

import static arc.Core.graphics;
import static mindustry.Vars.renderer;

public class WaveRenderer {
    public static boolean init = false;
    public static WaveShader shader;
    public final static Seq<WavePlace> places = new Seq<>();
    public static FrameBuffer buffer = new FrameBuffer();

    public static void init() {
        init = true;

        Events.run(EventType.Trigger.draw, WaveRenderer::WaveDraw);
    }

    public static void WaveDraw() {
        Draw.draw(Layer.max - 2, () -> {
            buffer.resize(graphics.getWidth(), graphics.getHeight());
            buffer.begin();
        });

        Draw.draw(Layer.max - 1, () -> {
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

//            if (places.size >= num || shader == null) {
//                if (shader != null) {
//                    shader.dispose();
//                }
//                if (places.size >= num) {
//                    num *= num;
//                }
//                Shader.prependFragmentCode = "#define MAX_NUM " + num + "\n";
//                shader = new WaveShader();
//                Shader.prependFragmentCode = "";
//            }
            if (shader == null) {
                Shader.prependFragmentCode = "#define MAX_NUM " + places.size + "\n";
                shader = new WaveShader();
            }

            shader.waves = waves;
            shader.color = color;
            buffer.blit(shader);

            Bloom bloom = renderer.bloom;
            if (bloom != null) {
                bloom.capture();
                bloom.render();
            }

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
