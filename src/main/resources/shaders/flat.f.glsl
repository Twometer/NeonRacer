#version 330 core

in vec2 framentCoords;
in vec4 fragmentColor;

out vec4 outColor;

uniform bool hasTexture;
uniform sampler2D colorTexture;

void main(void) {
    outColor = hasTexture ? texture(colorTexture, framentCoords) : fragmentColor;
}