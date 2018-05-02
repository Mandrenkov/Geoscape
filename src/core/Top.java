package core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import env.Camera;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <i>Top</i> class is the execution entry point.</p>
 */
public class Top {
    /**
     * Toggles debug parameters and the display of logged messages.
     */
    public static boolean DEBUG = true;

    /**
     * Execution entry point.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Logger.info("Application is starting...");

        try {
            // Initialize the Window and Camera singletons.
            Window.getInstance();
            Camera.getInstance();

            Simulation simulation = new Simulation();
            simulation.start();

            // Free the window callbacks and destroy the window.
            long handle = Window.getInstance().getHandle();
            glfwFreeCallbacks(handle);
            glfwDestroyWindow(handle);
        } finally {
            // Terminate GLFW and free the error callback.
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }

        Logger.info("Application terminated.");
    }
}