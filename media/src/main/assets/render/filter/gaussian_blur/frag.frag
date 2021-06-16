precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 高斯模糊次数
const int BLUR_CORE = 30;
// 模糊半径
uniform vec2 uBlurRadius;
// 高斯权重
uniform float uWeight[BLUR_CORE];

// 边界值处理
vec2 clampCoordinate(vec2 coordinate) {
    return vec2(clamp(coordinate.x, 0.0, 1.0), clamp(coordinate.y, 0.0, 1.0));
}

void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    vec3 finalColor = sourceColor.rgb * uWeight[0];

    for (int i = 1; i < BLUR_CORE; i++) {
        finalColor += texture2D(uSampler, clampCoordinate(vCoordinate - uBlurRadius * float(i))).rgb * uWeight[i];
        finalColor += texture2D(uSampler, clampCoordinate(vCoordinate + uBlurRadius * float(i))).rgb * uWeight[i];
    }

    gl_FragColor = vec4(finalColor, sourceColor.a);
}