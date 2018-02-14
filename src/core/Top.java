package core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import env.World;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <i>Top</i> class is the execution entry point.</p>
 */
public class Top {

	/**
	 * Toggles debug statement display
	 */
	public static final boolean DEBUG = true;

	/**
	 * Execution entry point.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		Logger.info("Application starting...");

		Window window = new Window();
		World world = new World();

		try {
			Loop.run(window, world);

			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(window.getReference());
			glfwDestroyWindow(window.getReference());
		} finally {
			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}

		Logger.info("Application terminated.");
	}

}