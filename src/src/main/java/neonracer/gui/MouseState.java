package neonracer.gui;

import neonracer.render.GameWindow;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseState implements IInputState {

    private Vector2f position;

    private boolean left;

    private boolean middle;

    private boolean right;

    @Override
    public void update(GameWindow gameWindow) {
        position = gameWindow.getCursorPosition();
        left = gameWindow.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT);
        middle = gameWindow.isMouseButtonPressed(GLFW_MOUSE_BUTTON_MIDDLE);
        right = gameWindow.isMouseButtonPressed(GLFW_MOUSE_BUTTON_RIGHT);
    }

    public Vector2f getPosition() {
        return position;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isMiddle() {
        return middle;
    }

    public boolean isRight() {
        return right;
    }

}
