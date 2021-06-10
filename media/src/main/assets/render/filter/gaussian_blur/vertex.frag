attribute vec4 aPos;
attribute vec2 aCoordinate;
varying vec2 vCoordinate;

const int GAUSSIAN_CORE = 5;

uniform float uWidthOffset;
uniform float uHeightOffset;

varying vec4 blurCoordinates[GAUSSIAN_CORE];

void main(){
    vCoordinate = aCoordinate;
    gl_Position = aPos;

    vec2 singleOffset = vec2(uWidthOffset, uHeightOffset);

    for (int i = 0; i < GAUSSIAN_CORE; i++){
        blurCoordinates[i] = vec4(vCoordinate - float(i + 1) * singleOffset,
        vCoordinate + float(i + 1) * singleOffset);
    }
}