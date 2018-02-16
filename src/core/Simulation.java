package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import env.*;
import util.Render;

/**
 * @author Mikhail Andrenkov
 * @since February 13, 2018
 * @version 1.1
 *
 * <p>The <b>Simulation</b> class represents the Geoscape simulation.</p>
 */
public class Simulation {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a Simulation object.
	 */
	public Simulation() {
		world = new World();
	}

	/**
	 * Starts the Simulation.  This function will only return execution control
	 * to its caller when the Window singleton is closed or an unhandled exception
	 * is raised.
	 */
	public void start() {
		Logger.info("Rendering %d x %d World.", World.ROWS, World.COLS);

		// Initialize the Window singleton.
		Window.getInstance();

		// Setup the previous loop time.
		syncTime = glfwGetTime();
		
		// Orient the camera into its initial position.
		glRotatef(-50f, 1f, 0f, 0f);
		glTranslatef(0f, 0f, -1.5f);
		glTranslatef(0f, 1.6f, 0f);
		glRotatef(45f, 0f, 0f, 1f);
		Render.rotateAxis('Z', -100f);
		
		loop();
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * Target degree of rotation along the z-axis every second.
	 */
	private static final float Z_ROTATE_DELTA = -30.00f;

	/**
	 * The World associated with this Simulation.
	 */
	private World world;

	/**
	 * The time used to synchronize the framerate during each loop iteration.
	 */
	private double syncTime;

	/**
	 * Continuously renders the World of this Simulation by controlling the
	 * high-level graphics pipeline flow and synchronizing the framerate of the
	 * render Window.
	 */
	private void loop() {
		long handle = Window.getInstance().getHandle();
        while (!glfwWindowShouldClose(handle)) {
			// Clear the GL buffers.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// Render the World.
			world.render();

			// Update the framerate synchronizer and rotate the viewport by a
			// factor that is proportional to the current frame period.
			double now = glfwGetTime();
			double period = now - syncTime;
			syncTime = now;

			double rotation = Z_ROTATE_DELTA*period;
			Render.rotateAxis('Z', (float) rotation);

			// Prepare for the next frame.
			glfwSwapBuffers(handle);
			glfwPollEvents();
			//syncFPS();
		}	
	}
}