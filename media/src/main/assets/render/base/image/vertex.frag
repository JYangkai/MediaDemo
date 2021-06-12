attribute vec4 aPos;
attribute vec2 aCoordinate;
uniform mat4 uMatrix;
varying vec2 vCoordinate;
void main(){
    vCoordinate = aCoordinate;
    gl_Position = uMatrix * aPos;
}