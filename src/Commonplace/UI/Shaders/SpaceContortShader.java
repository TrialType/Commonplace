package Commonplace.UI.Shaders;

import arc.graphics.gl.Shader;

import static arc.Core.files;
import static mindustry.Vars.tree;

public class SpaceContortShader extends Shader {
    public SpaceContortShader() {
        super(files.internal("shaders/screenspace.vert"), tree.get("shaders/spacecontort.frag"));
    }
}
