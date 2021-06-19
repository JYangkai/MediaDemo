precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 模糊半径
uniform int uBlurRadius;
// 模糊步长
uniform vec2 uBlurOffset;
// 总权重
uniform float uSumWeight;
// PI
const float PI = 3.1415926;

// 边界值处理
vec2 clampCoordinate(vec2 coordinate) {
    return vec2(clamp(coordinate.x, 0.0, 1.0), clamp(coordinate.y, 0.0, 1.0));
}

// 计算权重
float getWeight(int i) {
    float sigma = float(uBlurRadius) / 3.0;
    return (1.0 / sqrt(2.0 * PI * sigma * sigma)) * exp(-float(i * i) / (2.0 * sigma * sigma)) / uSumWeight;
}

void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    if (uBlurRadius <= 1) {
        gl_FragColor = sourceColor;
        return;
    }

    float weight = getWeight(0);

    vec3 finalColor = sourceColor.rgb * weight;

    for (int i = 1; i < uBlurRadius; i++) {
        weight = getWeight(i);
        finalColor += texture2D(uSampler, clampCoordinate(vCoordinate - uBlurOffset * float(i))).rgb * weight;
        finalColor += texture2D(uSampler, clampCoordinate(vCoordinate + uBlurOffset * float(i))).rgb * weight;
    }

    gl_FragColor = vec4(finalColor, sourceColor.a);
}