precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 中心点
const vec2 CENTER = vec2(0.5, 0.5);
// 模糊次数
const int BLUR_CORE = 5;
// 高斯权重
uniform float uWeight[BLUR_CORE];
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    vec2 direction = (CENTER - vCoordinate) * 0.015;

    vec3 finalColor = sourceColor.rgb * uWeight[0];

    for (int i = 1; i < BLUR_CORE; i++) {
        finalColor += texture2D(uSampler, vCoordinate + direction * float(i)).rgb * uWeight[i];
    }

    gl_FragColor = vec4(finalColor, sourceColor.a);
}