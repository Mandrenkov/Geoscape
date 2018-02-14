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
	 * Returns the singleton Simulation instance.
	 * 
	 * @return The Simulation singleton.
	 */
	public static Simulation getInstance() {
		if (instance == null) {
			instance = new Simulation();
		}
		return instance;
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

		syncTime = glfwGetTime();
		glEnable(GL_DEPTH_TEST);
		Render.rotateAxis('Z', -100f);
		
		loop();
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * Target delay (seconds) between frames to reach the target FPS.
	 */
	private static final double TARGET_FRAME_DELAY = 1.0/60.0;

	/**
	 * Amount to increment z-axis angle every frame.
	 */
	private static final float Z_ROTATE_DELTA = -0.30f; // -0.30f

	/**
	 * The singleton Simulation instance.
	 */
	private static Simulation instance;

	/**
	 * The World associated with this Simulation.
	 */
	private World world;

	/**
	 * The time used to synchronize the framerate during each loop iteration.
	 */
	private double syncTime;

	/**
	 * Constructs a Simulation object.
	 */
	private Simulation() {
		world = new World();
	}

	/**
	 * Continuously renders the World of this Simulation by controlling the
	 * high-level graphics pipeline flow and synchronizing the framerate of the
	 * render Window.
	 */
	private void loop() {
		long winref = Window.getInstance().getReference();
        while (!glfwWindowShouldClose(winref)) {
			// Clear the GL buffers.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// Render the World.
			world.render();
			Render.rotateAxis('Z', Z_ROTATE_DELTA);

			// Prepare for the next frame.
			glfwSwapBuffers(winref);
			glfwPollEvents();
			syncFPS();
		}	
	}

	/**
	 * Synchronizes the current frame rate with respect to |TARGET_FRAME_DELAY|.
	 */
	private void syncFPS() {
		double target = syncTime + TARGET_FRAME_DELAY;
		double syncTime = glfwGetTime();
        double delta = target - syncTime;
        if (delta > 0) {
        	try {
        		Thread.sleep((long) (1000*delta));
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        }
	}
}