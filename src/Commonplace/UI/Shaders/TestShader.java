package Commonplace.UI.Shaders;

import arc.Core;
import arc.files.Fi;
import arc.graphics.gl.Shader;

public class TestShader extends Shader {
    public float x, y, radiusIn, radiusOut;

    public TestShader(Fi vertexShader, Fi fragmentShader) {
        super(vertexShader, fragmentShader);
    }

    @Override
    public void apply() {
        setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);

        setUniformf("u_data", x, y, radiusIn, radiusOut);
    }
}
