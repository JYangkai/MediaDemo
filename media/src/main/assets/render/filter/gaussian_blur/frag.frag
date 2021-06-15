precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 高斯模糊次数
const int BLUR_CORE = 30;
// 模糊半径
uniform vec2 uBlurRadius;
// 高斯权重
uniform float uWeight[BLUR_CORE];

vec2 normalCoordinate(vec2 coordinate) {
    vec2 normal = coordinate;
    if (normal.x < 0.0) {
        normal.x = 0.0;
    } else if (normal.x > 1.0) {
        normal.x = 1.0;
    }

    if (normal.y < 0.0) {
        normal.y = 0.0;
    } else if (normal.y > 1.0) {
        normal.y = 1.0;
    }

    return normal;
}

void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    vec3 finalColor = sourceColor.rgb * uWeight[0];

    for (int i = 1; i < BLUR_CORE; i++) {
        finalColor += texture2D(uSampler, normalCoordinate(vCoordinate - uBlurRadius * float(i))).rgb * uWeight[i];
        finalColor += texture2D(uSampler, normalCoordinate(vCoordinate + uBlurRadius * float(i))).rgb * uWeight[i];
    }

    gl_FragColor = vec4(finalColor, sourceColor.a);
}