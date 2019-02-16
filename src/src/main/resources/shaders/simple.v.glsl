#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texCoords;

out vec2 textureCoords;
out vec4 fragmentColor;

uniform mat4 projectionMatrix;

void main(void) {
    gl_Position = projectionMatrix * vec4(position, 0.0, 1.0);
    fragmentColor = vertexColor;
    textureCoords = texCoords;
}