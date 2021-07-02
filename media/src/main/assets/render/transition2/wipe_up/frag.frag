precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uProgress;
const float OFFSET = 0.2;
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, vCoordinate);

    float progress = 1.0 - uProgress;

    gl_FragColor = mix(sourceColor, sourceColor2, smoothstep(progress, progress + OFFSET, vCoordinate.y));
}