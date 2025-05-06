package Commonplace.UI.Shaders;

import arc.Core;
import arc.graphics.gl.Shader;

import static mindustry.Vars.tree;

public class WaveShader extends Shader {
    public float[] waves;
    public float[] color;

    public WaveShader() {
        super(tree.get("shaders/tp.vert"), tree.get("shaders/wave.frag"));
    }

    @Override
    public void apply() {
        setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);

        setUniformi("u_nums", waves.length / 4);
        setUniform4fv("u_waves", waves, 0, waves.length);
        setUniform4fv("u_colors", color, 0, color.length);
    }
}
