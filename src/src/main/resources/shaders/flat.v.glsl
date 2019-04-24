#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec3 color;

out vec4 fragmentColor;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main(void) {
    gl_Position = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
    fragmentColor = vec4(color, 1.0f);
}