precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uOffset;
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, vCoordinate);

    float x = vCoordinate.x;

    if (uOffset > 0.5) {
        if (x < 0.5) {
            gl_FragColor = sourceColor;
        } else if (x > uOffset) {
            gl_FragColor = sourceColor2;
        } else {
            float newX = 0.5 * (x - 0.5) / (uOffset - 0.5) + 0.5;
            gl_FragColor = texture2D(uSampler, vec2(newX, vCoordinate.y));
        }
    } else {
        if (x < uOffset) {
            gl_FragColor = sourceColor;
        } else if (x > 0.5) {
            gl_FragColor = sourceColor2;
        } else {
            float newX = 0.5 * (x - uOffset) / (0.5 - uOffset);
            gl_FragColor = texture2D(uSampler2, vec2(newX, vCoordinate.y));
        }
    }
}