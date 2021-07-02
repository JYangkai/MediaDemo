precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uProgress;
const float OFFSET = 0.1;
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, vCoordinate);

    float smoothStep1 = smoothstep(0.5 - (uProgress + OFFSET), 0.5 - uProgress, vCoordinate.y);
    float smoothStep2 = smoothstep(0.5 + uProgress, 0.5 + (uProgress + OFFSET), vCoordinate.y);

    gl_FragColor = mix(sourceColor, sourceColor2, smoothStep1 - smoothStep2);
}