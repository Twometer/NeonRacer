package neonracer.render.engine.postproc;

import neonracer.render.gl.core.Fbo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

class FboManager {

    private float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};

    private int vao;

    FboManager() {
        initialize();
    }

    private void initialize() {
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, POSITIONS, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    void begin() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
    }

    void copyFbo(Fbo src, Fbo target) {
        if (target != null) target.bind();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, src.getColorTexture());
        glClear(GL_COLOR_BUFFER_BIT);
        fullscreenQuad();
        if (target != null) target.unbind();
    }

    void fullscreenQuad() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    void end() {
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

}
