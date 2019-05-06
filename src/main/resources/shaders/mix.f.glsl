#version 140

in vec2 textureCoords;
out vec4 out_Colour;
uniform sampler2D tex1;
uniform sampler2D tex2;

void main(void){
    vec4 A = texture(tex2, textureCoords); // glow
    vec4 B = texture(tex1, textureCoords); // color

    out_Colour = 2 * A + B;

    /*
    float alpha = A.a;
    float ialpha = 1.0 - alpha;

    vec4 aaa = vec4(alpha * A.r, alpha * A.g, alpha * A.b, 1.0f);
    vec4 bbb = vec4(ialpha * B.r, ialpha * B.g, ialpha * B.b, 1.0f);

    out_Colour = aaa + bbb;*/
}