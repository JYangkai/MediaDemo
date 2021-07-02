precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uProgress;
const float OFFSET = 0.2;
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, vCoordinate);

    float progress = uProgress;

    gl_FragColor = mix(sourceColor2, sourceColor, smoothstep(0.0, progress, vCoordinate.x + vCoordinate.y - progress * 2.0));
}