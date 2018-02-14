package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import env.*;
import util.Render;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Loop</b> class.</p>
 */
public class Loop {

	/**
	 * Ideal delay (seconds) between frames to reach target FPS.
	 */
	private static final double TARGET_FRAME_DELAY = 1.0/60.0;

	/**
	 * Amount to increment z-axis angle every frame.
	 */
	private static final float Z_ROTATE_DELTA = -0.30f; // -0.30f

	/**
	 * Application update loop
	 *
	 * @param window Application window
	 * @param world World to be rendered
	 */
	public static void run(Window window, World world) {
		Logger.info("Rendering %d x %d World.", World.ROWS, World.COLS);

		double targetTime = glfwGetTime();
		Render.rotateAxis('Z', -100f);
        while (!glfwWindowShouldClose(window.getReference())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render(world);

            targetTime = syncFPS(targetTime);

			glfwSwapBuffers(window.getReference());
			glfwPollEvents();
		}
	}

	private static void render(World world) {
		if (Top.DEBUG) {
			Render.drawAxes();
		}
		world.getDrawables().forEach(d -> d.draw());
		Render.rotateAxis('Z', Z_ROTATE_DELTA);
	}

	/**
	 * Synchronizes the current frame rate with respect to TARGET_FRAME_DELAY.
	 *
	 * @param targetTime Ideal time of current frame.
	 * @return The next target frame time.
	 */
	private static double syncFPS(double targetTime) {
        double frameDelta = targetTime - glfwGetTime();

        if (frameDelta > 0) {
        	try {
        		Thread.sleep((long) (frameDelta*1000));
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        }

        return glfwGetTime() + TARGET_FRAME_DELAY;
	}

}