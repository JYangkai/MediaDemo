precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
uniform int uBlurRadius;
uniform vec2 uBlurOffset;

// 边界值处理
vec2 clampCoordinate(vec2 coordinate) {
    return vec2(clamp(coordinate.x, 0.0, 1.0), clamp(coordinate.y, 0.0, 1.0));
}

void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    if (uBlurRadius <= 1) {
        gl_FragColor = sourceColor;
        return;
    }

    vec3 finalColor = sourceColor.rgb;

    for (int i = 0; i < uBlurRadius; i++) {
        finalColor += texture2D(uSampler, clampCoordinate(vCoordinate + uBlurOffset * float(i))).rgb;
        finalColor += texture2D(uSampler, clampCoordinate(vCoordinate - uBlurOffset * float(i))).rgb;
    }

    finalColor /= float(2 * uBlurRadius + 1);

    gl_FragColor = vec4(finalColor, sourceColor.a);
}