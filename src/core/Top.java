package core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import env.Camera;
import util.RNG;

/**
 * The Top class is the execution entry point.
 */
public class Top {
    /**
     * Toggles debug parameters and the logging verbosity.
     */
    public static boolean DEBUG = false;

    /**
     * Execution entry point.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Logger.info("Launching %s", Build.getVersionString());
        Logger.info("The following seed was used to initialize the RNG: %d.", RNG.getSeed());

        try {
            // Initialize the Window and Camera singletons.
            Window.getInstance();
            Camera.getInstance();

            // Start a Simulation using the Window singleton.
            Window window = Window.getInstance();
            Simulation simulation = new Simulation(window);
            simulation.start();

            // Free the window callbacks and destroy the window.
            long handle = window.getHandle();
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