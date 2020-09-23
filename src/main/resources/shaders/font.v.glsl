#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec2 texCoords;

out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main(void) {
    gl_Position = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
    textureCoords = texCoords;
}