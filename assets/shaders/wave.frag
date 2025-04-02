uniform int u_nums;
uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform vec4 u_waves[NUM];
uniform vec4 u_colors[NUM];
uniform sampler2D u_texture;

varying vec2 v_texCoord;

vec4 blendOver(vec4 a, vec4 b) {
    float newAlpha = mix(b.w, 1.0, a.w);
    vec3 newColor = mix(b.w * b.xyz, a.xyz, a.w);
    float divideFactor = (newAlpha > 0.001 ? (1.0 / newAlpha) : 1.0);
    return vec4(divideFactor * newColor, newAlpha);
}

void main() {
    vec2 c = v_texCoord.xy;
    vec2 coords = (c * u_resolution) + u_campos;

    for (int i = 0; i < u_nums; i++) {
        vec4 center = u_waves[i];
        float len = distance(coords, center.xy);
        if (len < center.z && len > center.z - center.w) {
            vec2 offset = pow((1 - len / 160), 2) * step(len, 160) * 12 * (coords - center.xy) / len;
            c = (coords + offset - u_campos) / u_resolution;
            vec4 color = u_colors[i];
            color.a = 0.5;
            gl_FragColor = blendOver(texture2D(u_texture, c), color);
            return;
        }
    }

    gl_FragColor = texture2D(u_texture, v_texCoord);
}