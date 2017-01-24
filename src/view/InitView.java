package view;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import core.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>InitView</b> class.</p>
 */ 
public class InitView {

	private static DataManager data;

	public void run() {
		data = Top.data;

		initWindow();
		initGL();
	}

	private void initWindow() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		for (int[] hintPair : data.windowHints) {
			glfwWindowHint(hintPair[0], hintPair[1]);
		}

		int width = data.INIT_WIDTH;
		int height = data.INIT_HEIGHT;
		String title = data.WINDOW_TITLE;

		long window = glfwCreateWindow(width, height, title, NULL, NULL);

		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (localWindow, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(localWindow, true);
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width)/2, (vidmode.height() - height)/2);

		glfwMakeContextCurrent(window);		// Make the OpenGL context current
		glfwSwapInterval(0);				// Enable v-sync
		glfwShowWindow(window);				// Make the window visible

		data.window = window;
	}

	private void initGL() {
		GL.createCapabilities();

		glfwSetWindowSizeCallback(data.window, (localWindow, newWidth, newHeight) -> {
			glViewport(0, 0, newWidth, newHeight);
		});

        glClearColor(data.colourBG[0], data.colourBG[1], data.colourBG[2], data.colourBG[3]);

        for (int flag : Top.data.glFlags) {
        	glEnable(flag);
        }


		/* Matrix Initialization */
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // Define Frustum
        float yTop  = (float) (data.VIEW_Z_NEAR * Math.tan(Math.toRadians(data.VIEW_FOV / 2)));
	    float xLeft = yTop * data.VIEW_ASPECT;
	    glFrustum(xLeft, -xLeft, -yTop, yTop, data.VIEW_Z_NEAR, data.VIEW_Z_FAR);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();


        /* View Transforms */
		glRotatef(-50f, 1f, 0f, 0f);


		glTranslatef(0f, 0f, -1.5f);
		glTranslatef(0f, 1.6f, 0f);

		glRotatef(45f, 0f, 0f, 1f);
	}

}