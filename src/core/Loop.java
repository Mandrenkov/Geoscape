package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import view.*;

import model.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Loop</b> class.</p>
 */ 
public class Loop {

	public static void run(Window window, World world) {
		Render render = new Render();

		System.out.printf("Rendering %d x %d Grid\n", World.ROWS, World.COLS);

		double lastTime = glfwGetTime();
		double target = 1000.0/60.0;

        while (!glfwWindowShouldClose(window.getReference())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render.run(world);

            double currentTime = glfwGetTime();
            double frameDelta = (currentTime - lastTime)*1000;
            lastTime = currentTime;

            if (frameDelta < target) {
            	try {
            		Thread.sleep((long) (target - frameDelta));
            	} catch (InterruptedException e) {
            		e.printStackTrace();
            	}
            }

			glfwSwapBuffers(window.getReference());
			glfwPollEvents();
		}
	}

}