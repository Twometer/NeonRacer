#version 330 core

in vec2 textureCoords;
in vec4 fragmentColor;

out vec4 outColor;

uniform sampler2D colorTexture;

void main(void) {
    outColor = texture(colorTexture, vec2(textureCoords.x,-textureCoords.y));
}