package Commonplace.UI.Renders;

import Commonplace.UI.Shaders.TestShader;
import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.Effect;
import mindustry.graphics.Layer;

import static mindustry.Vars.*;

public class TestRender {
    public static boolean debug = false;
    public static TestShader shader = new TestShader(tree.get("shaders/tp.vert"), tree.get("shaders/ts.frag"));
    public static Mesh mesh = new Mesh(true, 4, 6,
            VertexAttribute.position,
            VertexAttribute.texCoords
    );
    public static FrameBuffer buffer = new FrameBuffer();

    public static void init() {
        mesh.setVertices(new float[]{
                -1f, -1f, 0f, 1f,
                -1f, 1f, 0f, 0f,
                1f, 1f, 1f, 0f,
                1f, -1f, 1f, 1f
        });
        mesh.setIndices(new short[]{
                0, 1, 2,
                0, 2, 3
        });
    }

    public static void draw() {
        if (debug) {
            float T = 90, use = 70, in = 40, time = Time.time;
            if (time % T <= use) {
                float path = (time % T > in ? in * (1 - Mathf.pow((time % T - in) / (use - in), 2)) : in - Mathf.pow(in - time % T, 2) / in);
                Draw.draw(Layer.background - 1, () -> {
                    buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
                    buffer.begin();
                });
                Draw.draw(Layer.weather + 1, () -> {
                    buffer.end();
                    shader.x = player.x;
                    shader.y = player.y;
                    shader.radiusIn = path;
                    shader.radiusOut = path * 1.5f;
                    buffer.getTexture().setWrap(Texture.TextureWrap.clampToEdge);
                    buffer.blit(shader);
                });
            }
            if (Math.abs(use - 10 - time % T) <= Time.delta) {
                Effect.shake(8, 20, player.x, player.y);
            }
        }
    }
}
