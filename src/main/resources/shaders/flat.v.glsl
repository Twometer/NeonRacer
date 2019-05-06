#version 330 core

layout(location = 0) in vec2 position;
layout(location = 2) in vec2 texCoords;

out vec4 fragmentColor;
out vec2 framentCoords;

uniform vec4 color;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main(void) {
    gl_Position = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
    fragmentColor = color;
    framentCoords = texCoords;
}