precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
void main(){
    if (vCoordinate.y < 0.5) {
        gl_FragColor = texture2D(uSampler, vCoordinate + vec2(0.0, 0.25));
    } else {
        gl_FragColor = texture2D(uSampler, vCoordinate - vec2(0.0, 0.25));
    }
}