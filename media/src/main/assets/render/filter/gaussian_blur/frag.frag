precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 高斯模糊次数
const int BLUR_CORE = 5;
// 模糊半径
uniform vec2 uBlurRadius;
// 高斯权重
uniform float uWeight[BLUR_CORE];
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    vec3 finalColor = sourceColor.rgb * uWeight[0];

    for (int i = 1; i < BLUR_CORE; i++) {
        finalColor += texture2D(uSampler, vCoordinate - uBlurRadius * float(i)).rgb * uWeight[i];
        finalColor += texture2D(uSampler, vCoordinate + uBlurRadius * float(i)).rgb * uWeight[i];
    }

    finalColor /= 2.0;
    finalColor += 0.1;

    gl_FragColor = vec4(finalColor, sourceColor.a);
}