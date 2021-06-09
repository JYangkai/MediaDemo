attribute vec4 aPos;
attribute vec4 aCoordinate;
uniform mat4 uMatrix;
uniform mat4 uOesMatrix;
varying vec2 vCoordinate;
void main(){
    vCoordinate = (uOesMatrix * aCoordinate).xy;
    gl_Position = uMatrix * aPos;
}