precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
void main(){
    if (vCoordinate.y > 0.33 && vCoordinate.y < 0.66) {
        gl_FragColor = texture2D(uSampler, vCoordinate);
    } else {
        gl_FragColor = texture2D(uSampler2, vCoordinate);
    }
}