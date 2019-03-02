package neonracer.render.gl.core;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * A model is a mesh that has been uploaded
 * to the GPU and is ready for rendering
 */
public class Model {

    private int vao;

    private int vertexBuffer;
    private int colorBuffer;
    private int texCoordBuffer;

    private int vertices;

    private Model(int vao, int vertexBuffer, int colorBuffer, int texCoordBuffer, int vertices) {
        this.vao = vao;
        this.vertexBuffer = vertexBuffer;
        this.colorBuffer = colorBuffer;
        this.texCoordBuffer = texCoordBuffer;
        this.vertices = vertices;
    }

    public static Model create(Mesh mesh) {
        mesh.getVertices().flip();
        mesh.getColors().flip();
        mesh.getTexCoords().flip();

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, mesh.getVertices(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        int colorBuffer = -1;
        if (mesh.getColorCount() > 0) {
            colorBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
            glBufferData(GL_ARRAY_BUFFER, mesh.getColors(), GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        }

        int texCoordBuffer = -1;
        if (mesh.getTexCoordCount() > 0) {
            texCoordBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texCoordBuffer);
            glBufferData(GL_ARRAY_BUFFER, mesh.getTexCoords(), GL_STATIC_DRAW);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        return new Model(vao, vertexBuffer, colorBuffer, texCoordBuffer, mesh.getVertexCount());
    }

    public void destroy() {
        glDeleteBuffers(vertexBuffer);
        glDeleteBuffers(colorBuffer);
        glDeleteBuffers(texCoordBuffer);
    }

    public void draw() {
        boolean hasColors = colorBuffer != -1;
        boolean hasTexture = texCoordBuffer != -1;

        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        if (hasColors) glEnableVertexAttribArray(1);
        if (hasTexture) glEnableVertexAttribArray(2);

        glDrawArrays(GL_TRIANGLES, 0, vertices);

        if (hasTexture) glDisableVertexAttribArray(2);
        if (hasColors) glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);

        glBindVertexArray(0);
    }

}
