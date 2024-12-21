uniform vec2 u_campos;
uniform vec2 u_resolution;

uniform vec4 u_waves[MAX_NUM];
uniform vec4 u_colors[MAX_NUM];

uniform sampler2D u_texture;

varying vec2 v_texCoords;

vec4 blendOver(vec4 a, vec4 b) {
    float newAlpha = mix(b.w, 1.0, a.w);
    vec3 newColor = mix(b.w * b.xyz, a.xyz, a.w);
    float divideFactor = (newAlpha > 0.001 ? (1.0 / newAlpha) : 1.0);
    return vec4(divideFactor * newColor, newAlpha);
}

void main(){
    vec2 c = v_texCoords.xy;
    vec2 coords = (c * u_resolution) + u_campos;
    vec4 color = vec4(1, 1, 1, 0);

    for (int i = 0; i < MAX_NUM; i++){
        vec4 fire = u_waves[i];
        float len = distance(coords, fire.xy);
        if (len < fire.z && len > fire.z - fire.w){
            //color = blendOver(texture2D(u_texture, c).rgba, u_colors[i] * (fire.b - len / 1.5) / fire.b);
            //if ((len / fire.b) > 0.8){
            //    float ro;
            //    if (coords.x - fire.r == 0){
            //        ro = 90;
            //    } else {
            //        ro = atan(coords.y - fire.g, coords.x - fire.r);
            //    }
            //    color = texture2D(u_texture, c + vec2(cos(ro), sin(ro)) / u_resolution);
            //    blendOver(texture2D(u_texture, c + vec2(cos(ro), sin(ro))), u_colors[i] * (fire.b - len / 2.5) / fire.b);
            //}
            color = u_colors[i];
            color.a *= (len + fire.w - fire.z) / fire.w;
        }
    }

    gl_FragColor = blendOver(texture2D(u_texture, c), color);
}