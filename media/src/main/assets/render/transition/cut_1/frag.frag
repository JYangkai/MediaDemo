precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uOffset;
void main(){
    float x = vCoordinate.x;
    float y = vCoordinate.y;

    if (y < 0.5) {
        x += uOffset;
        if (x <= 1.0) {
            gl_FragColor = texture2D(uSampler, vec2(x, y));
        } else {
            gl_FragColor = texture2D(uSampler2, vec2(x - 1.0, y));
        }
    } else {
        x -= uOffset;
        if (x >= 0.0) {
            gl_FragColor = texture2D(uSampler, vec2(x, y));
        } else {
            gl_FragColor = texture2D(uSampler2, vec2(x + 1.0, y));
        }
    }
}