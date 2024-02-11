package de.jcing.engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse {

    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static boolean[] clicks = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;

    private static  void onMousePressEvent(long window, int button, int action, int mods) {
        if (button >= 0 && button < buttons.length) {
            if (action == GLFW.GLFW_PRESS) {
                buttons[button] = true;
            } else if (action == GLFW.GLFW_RELEASE) {
                buttons[button] = false;
                clicks[button] = true; // Register click on release
            }
        }
    }



    public static void onMouseMovement(long window, double xpos, double ypos) {
        mouseX = xpos;
        mouseY = ypos;
    }


    // Method to check for button click
    public static boolean isButtonClicked(int button) {
        boolean clicked = clicks[button];
        clicks[button] = false; // Reset click status after checking
        return clicked;
    }

    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    // Set these callbacks on the GLFW window
    public static void setCallbacks(long window) {
        GLFW.glfwSetMouseButtonCallback(window, Mouse::onMousePressEvent);
        GLFW.glfwSetCursorPosCallback(window, Mouse::onMouseMovement);
    }
}
