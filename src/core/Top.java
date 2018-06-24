package core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import env.Camera;
import util.RNG;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <i>Top</i> class is the execution entry point.</p>
 */
public class Top {
    /**
     * Toggles debug parameters and the display of logged messages.
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

            long handle = Window.getInstance().getHandle();

            Simulation simulation = new Simulation(handle);
            simulation.start();

            // Free the window callbacks and destroy the window.
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