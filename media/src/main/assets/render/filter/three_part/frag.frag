precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
void main(){
    if (vCoordinate.y < 0.33) {
        gl_FragColor = texture2D(uSampler, vCoordinate + vec2(0.0, 0.33));
    } else if (vCoordinate.y > 0.66){
        gl_FragColor = texture2D(uSampler, vCoordinate - vec2(0.0, 0.33));
    } else {
        gl_FragColor = texture2D(uSampler, vCoordinate);
    }
}