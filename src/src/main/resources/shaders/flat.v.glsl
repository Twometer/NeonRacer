#version 330 core

layout(location = 0) in vec2 position;

out vec4 fragmentColor;

uniform vec4 color;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main(void) {
    gl_Position = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
    fragmentColor = color;
}