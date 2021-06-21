precision mediump float;
uniform sampler2D uSampler;
varying vec2 vCoordinate;
// 实际尺寸
uniform vec2 uSize;
// 旋转角度
uniform float uRotate;
// 中心点
const vec2 CENTER = vec2(0.5, 0.5);
void main(){
    // 实际坐标
    vec2 coordinate = vCoordinate * uSize;
    // 中心点坐标
    vec2 center = CENTER * uSize;

    // 半径
    float radius = distance(coordinate, center);

    // 旋转角度
    float rotate = uRotate * (1.0 - (radius / center.x) * (radius / center.y));

    // 当前旋转角度正余弦值
    float cosA = (coordinate.x - center.x) / radius;
    float sinA = (coordinate.y - center.y) / radius;

    // 外部传入旋转角度正余弦值
    float cosB = cos(rotate);
    float sinB = sin(rotate);

    // 两者加起来后的正余弦值
    float cosC = cosA * cosB - sinA * sinB;
    float sinC = sinA * cosB + cosA * sinB;

    // 计算新坐标
    float newX = center.x + radius * cosC;
    float newY = center.y + radius * sinC;

    vec2 newCoordinate = vec2(newX, newY) / uSize;

    gl_FragColor = texture2D(uSampler, newCoordinate);
}