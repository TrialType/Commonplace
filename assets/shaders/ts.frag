uniform vec4 u_data;
uniform vec2 u_campos;
uniform vec2 u_resolution;

//varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;

void main(){
    vec2 c = v_texCoord.xy;
    vec2 coords = (c * u_resolution) + u_campos;
    vec2 center = u_data.rg;
    float close = u_data.b, far = u_data.a;
    float len = distance(coords, center);
    if (len < far) {
        float m = (far - close) * pow(len / far, 3);
        gl_FragColor = texture2D(u_texture, ((center + (coords - center) / len * (close + m)) - u_campos) / u_resolution);
    } else {
        gl_FragColor = texture2D(u_texture, v_texCoord);
    }
}