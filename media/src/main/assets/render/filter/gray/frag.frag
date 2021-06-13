precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
const vec3 GRAY = vec3(0.299, 0.587, 0.144);
void main(){
    vec4 sourceColor = texture2D(uSampler, vCoordinate);

    float gray =dot(sourceColor.rgb, GRAY);

    gl_FragColor = vec4(gray, gray, gray, sourceColor.a);
}