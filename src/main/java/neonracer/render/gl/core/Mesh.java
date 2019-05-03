package neonracer.render.gl.core;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * A mesh is a collection of vertices, colors and
 * textures coordinates stored in CPU memory (RAM)
 */
public class Mesh {

    private FloatBuffer vertices;

    private FloatBuffer colors;

    private FloatBuffer texCoords;

    private int vertexCount;

    private int colorCount;

    private int texCoordCount;

    public Mesh(int vertexCapacity) {
        vertices = MemoryUtil.memAllocFloat(vertexCapacity * 2);
        colors = MemoryUtil.memAllocFloat(vertexCapacity * 3);
        texCoords = MemoryUtil.memAllocFloat(vertexCapacity * 2);
    }

    public void putVertex(float x, float y) {
        vertices.put(x);
        vertices.put(y);
        vertexCount++;
    }

    public void putColor(float r, float g, float b) {
        colors.put(r);
        colors.put(g);
        colors.put(b);
        colorCount++;
    }

    public void putTexCoord(float u, float v) {
        texCoords.put(u);
        texCoords.put(v);
        texCoordCount++;
    }

    int getVertexCount() {
        return vertexCount;
    }

    int getColorCount() {
        return colorCount;
    }

    int getTexCoordCount() {
        return texCoordCount;
    }

    FloatBuffer getVertices() {
        return vertices;
    }

    FloatBuffer getColors() {
        return colors;
    }

    FloatBuffer getTexCoords() {
        return texCoords;
    }

    public void destroy() {
        MemoryUtil.memFree(vertices);
        MemoryUtil.memFree(colors);
        MemoryUtil.memFree(texCoords);
    }
}
