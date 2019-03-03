package neonracer.render;

import org.joml.Matrix4f;

public class RenderContext {

    private Matrix4f worldMatrix;

    private Matrix4f guiMatrix;

    public Matrix4f getWorldMatrix() {
        return worldMatrix;
    }

    void setWorldMatrix(Matrix4f worldMatrix) {
        this.worldMatrix = worldMatrix;
    }

    public Matrix4f getGuiMatrix() {
        return guiMatrix;
    }

    void setGuiMatrix(Matrix4f guiMatrix) {
        this.guiMatrix = guiMatrix;
    }

}
