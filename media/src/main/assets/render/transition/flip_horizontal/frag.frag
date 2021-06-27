precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uCompress;
uniform int uUseSampler;
void main(){
    float newX = (2.0 * vCoordinate.x - uCompress) / (2.0 * (1.0 - uCompress));

    if (newX < uCompress / 2.0 || newX > 1.0 - uCompress / 2.0) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        return;
    }

    if (uUseSampler == 0) {
        gl_FragColor = texture2D(uSampler, vec2(newX, vCoordinate.y));
    } else {
        gl_FragColor = texture2D(uSampler2, vec2(newX, vCoordinate.y));
    }
}