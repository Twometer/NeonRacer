package neonracer.gui.input;

import neonracer.render.GameWindow;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardState implements IInputState {

    private boolean forward;

    private boolean reverse;

    private boolean left;

    private boolean right;

    private boolean spacebar;

    @Override
    public void update(GameWindow gameWindow) {
        forward = gameWindow.isKeyPressed(GLFW_KEY_W);
        left = gameWindow.isKeyPressed(GLFW_KEY_A);
        reverse = gameWindow.isKeyPressed(GLFW_KEY_S);
        right = gameWindow.isKeyPressed(GLFW_KEY_D);
        spacebar = gameWindow.isKeyPressed(GLFW_KEY_SPACE);
    }

    public boolean isSpacebar() {
        return spacebar;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isReverse() {
        return reverse;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

}
