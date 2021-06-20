precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
uniform float uScale;
void main(){
    float scaleX = (2.0 * vCoordinate.x + uScale - 1.0) / (2.0 * uScale);
    float scaleY = (2.0 * vCoordinate.y + uScale - 1.0) / (2.0 * uScale);

    gl_FragColor = texture2D(uSampler, vec2(scaleX, scaleY));
}