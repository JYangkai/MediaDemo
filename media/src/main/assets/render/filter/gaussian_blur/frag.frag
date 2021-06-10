precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;

const int GAUSSIAN_CORE = 5;

varying vec4 blurCoordinates[GAUSSIAN_CORE];

void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    vec4 color = sourceColor;

    for (int i = 0; i < GAUSSIAN_CORE; i++) {
        color += texture2D(uSampler, blurCoordinates[i].xy);
        color += texture2D(uSampler, blurCoordinates[i].zw);
    }

    gl_FragColor = vec4(color.rgb * 1.0 / float(2 * GAUSSIAN_CORE + 1), sourceColor.a);
}