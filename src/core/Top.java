package core;

import model.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>Top</i> class is the execution entry point.</p>
 */
public class Top {

	/**
	 * Toggles debug statement display
	 */
	public static final boolean DEBUG = false;


	/**
	 * Execution entry point.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		System.out.println("Application Active...\n");

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

		System.out.println("\nApplication Terminated.");
	}

}