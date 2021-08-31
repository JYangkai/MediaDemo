precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
void main(){
    float x = vCoordinate.x;
    float y = vCoordinate.y;

    if (x < 0.5) {
        x *= 2.0;
    } else {
        x = (x - 0.5) * 2.0;
    }

    if (y < 0.5) {
        y *= 2.0;
    } else {
        y = (y - 0.5) * 2.0;
    }

    gl_FragColor = texture2D(uSampler, vec2(x, y));
}