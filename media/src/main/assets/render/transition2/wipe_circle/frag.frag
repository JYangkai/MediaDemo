precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uProgress;
const vec2 CENTER = vec2(0.5);
const float OFFSET = 0.2;
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, vCoordinate);

    float radius = length(vCoordinate - CENTER);
    float maxR = length(CENTER);
    float curR = maxR * uProgress;

    gl_FragColor = mix(sourceColor2, sourceColor, smoothstep(curR, curR + OFFSET, radius));
}