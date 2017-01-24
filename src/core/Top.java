package core;

import model.*;
import view.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Top</b> class.</p>
 */ 
public class Top {

	public static DataManager data;

	public void run() {
		new InitView().run();
		new InitModel().run();

		for (Terrain terrain : data.landscape) {
			System.out.println(terrain);

			new NoiseGen(
					terrain,
					terrain.getPerlinRows(),
					terrain.getPerlinCols(),
					terrain.getElevationScale()
			).run();

			terrain.updateColours();
		}
	}

	public static void main(String[] args) {
		data = new DataManager();
		data.generateLandscape();

		System.out.println("Application Active...\n");

		try {
			new Top().run();
			new Loop().run();

			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(data.window);
			glfwDestroyWindow(data.window);
		} finally {
			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}

		System.out.println("\nApplication Terminated.");
	}

}