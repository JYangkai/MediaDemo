precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uOffset;
void main(){
    if (vCoordinate.y < uOffset) {
        gl_FragColor = texture2D(uSampler2, vCoordinate);
    } else {
        gl_FragColor = texture2D(uSampler, vCoordinate);
    }
}