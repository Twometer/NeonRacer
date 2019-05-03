#version 140

in vec2 textureCoords;
out vec4 out_Colour;
uniform sampler2D tex1;
uniform sampler2D tex2;

void main(void){
    vec4 col1 = texture(tex1, textureCoords);
    vec4 col2 = texture(tex2, textureCoords);
    out_Colour = col1 + col2 * 2;
}