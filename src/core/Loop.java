package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import view.*;

import model.*;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
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
	 * Application update loop
	 *
	 * @param window Application window
	 * @param world World to be rendered
	 */
	public static void run(Window window, World world) {
		Render render = new Render();

		System.out.printf("Rendering %d x %d World.\n", World.ROWS, World.COLS);

		double targetTime = glfwGetTime();

        while (!glfwWindowShouldClose(window.getReference())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render.run(world);

            targetTime = syncFPS(targetTime);

			glfwSwapBuffers(window.getReference());
			glfwPollEvents();
		}
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