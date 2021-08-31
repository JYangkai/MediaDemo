precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
void main(){
    float x = vCoordinate.x;
    float y = vCoordinate.y;

    if (x < 0.33) {
        x *= 3.0;
    } else if (x < 0.66) {
        x = (x - 0.33) * 3.0;
    } else {
        x = (x - 0.66) * 3.0;
    }

    if (y < 0.33) {
        y *= 3.0;
    } else if (y < 0.66) {
        y = (y - 0.33) * 3.0;
    } else {
        y = (y - 0.66) * 3.0;
    }

    gl_FragColor = texture2D(uSampler, vec2(x, y));
}