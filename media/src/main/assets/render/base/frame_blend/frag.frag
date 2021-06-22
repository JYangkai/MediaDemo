precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, vCoordinate);

    gl_FragColor = mix(sourceColor, sourceColor2, 0.5);
}