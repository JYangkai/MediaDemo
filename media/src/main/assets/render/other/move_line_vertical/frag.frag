precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
uniform float uOffset;
const vec4 COLOR = vec4(0.0, 0.0, 1.0, 1.0);
const float SIZE = 0.005;
void main(){
    if (vCoordinate.y > uOffset - SIZE && vCoordinate.y < uOffset + SIZE) {
        gl_FragColor = COLOR;
    } else {
        gl_FragColor = texture2D(uSampler, vCoordinate);
    }
}