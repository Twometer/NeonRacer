#version 330 core

in vec4 fragmentColor;

out vec4 outColor;

uniform sampler2D colorTexture;

void main(void) {
    outColor = fragmentColor;
}