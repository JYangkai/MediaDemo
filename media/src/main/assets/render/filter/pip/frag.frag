precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
const float SCALE = 2.0;
void main(){
    float scaleX = (2.0 * vCoordinate.x + SCALE - 1.0) / (2.0 * SCALE);
    float scaleY = (2.0 * vCoordinate.y + SCALE - 1.0) / (2.0 * SCALE);

    if (vCoordinate.y > 0.33 && vCoordinate.y < 0.66) {
        gl_FragColor = texture2D(uSampler, vCoordinate);
    } else {
        gl_FragColor = texture2D(uSampler2, vec2(scaleX, scaleY));
    }
}