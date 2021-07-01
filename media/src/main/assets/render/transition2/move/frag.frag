precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;
varying vec2 vCoordinate;
uniform float uProgress;
uniform vec2 uDirectional;
void main(){
    vec2 coordinate = vCoordinate + uProgress * sign(uDirectional);
    vec2 fractCoordinate = fract(coordinate);

    vec4 sourceColor = texture2D(uSampler, fractCoordinate);
    vec4 sourceColor2 = texture2D(uSampler2, fractCoordinate);

    gl_FragColor = mix(
        sourceColor2,
        sourceColor,
        step(0.0, coordinate.y) * step(coordinate.y, 1.0) * step(0.0, coordinate.x) * step(coordinate.x, 1.0)
    );
}