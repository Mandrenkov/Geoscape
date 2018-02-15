package core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <i>Top</i> class is the execution entry point.</p>
 */
public class Top {
	/**
	 * Toggles debug parameters and the display of logged messages.
	 */
	public static final boolean DEBUG = true;

	/**
	 * Execution entry point.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		Logger.info("Application starting...");

		try {
			Simulation.getInstance().start();

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