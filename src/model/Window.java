package model;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>Window</i> class represents the application window.</p>
 */
public class Window {

		/* Public variables */

		// Physical window attributes
		public final int INIT_WIDTH = 1400;
		public final int INIT_HEIGHT = 800;
		public final String WINDOW_TITLE = "Randomized Geoscape";

		// OpenGL flags to enable
		public final int[] GL_FLAGS = new int[] {
			GL_DEPTH_TEST,
			GL_CULL_FACE,
			GL_MULT
		};
		// GLFW window hints
		public final int[][] GLFW_HINTS = new int[][] {
			{GLFW_VISIBLE, GLFW_FALSE},
			{GLFW_RESIZABLE, GLFW_TRUE},
			{GLFW_SAMPLES, 4}
		};
		public final int GLFW_VSYNC = 0;

		// View perspective
		public static final float VIEW_FOV = 70f;
		public static final float VIEW_ASPECT = 1f;
		public static final float VIEW_Z_NEAR = 0.01f;
		public static final float VIEW_Z_FAR = 5f;

		/* Private variables */

		private long window = NULL;

		/* Public methods */

		/**
		 * Constructor
		 */
		public Window() {
			initWindow();
			initGL();

			rotateView();
		}

		/**
		 * Returns the reference to the GLFW window.
		 *
		 * @return The reference to the GLFW window.
		 */
		public long getReference() {
			return window;
		}

		/**
		 * Returns the dimensions of the window.
		 *
		 * @return The dimensions of the window.
		 */
		public int[] getWindowDimensions() {
			Pointer widthPtr = new Pointer();
			Pointer heightPtr = new Pointer();

			glfwGetWindowSize(window, widthPtr.array(), heightPtr.array());

			return new int[] {widthPtr.get(), heightPtr.get()};
		}

		/**
		 * Returns the height of the window.
		 *
		 * @return The height of the window.
		 */
		public int getWindowHeight() {
			return getWindowDimensions()[1];
		}

		/**
		 * Returns the width of the window.
		 *
		 * @return The width of the window.
		 */
		public int getWindowWidth() {
			return getWindowDimensions()[0];
		}

		/* Private methods */

		/**
		 * Initializes the GLFW window
		 */
		private void initWindow() {
			GLFWErrorCallback.createPrint(System.err).set();

			if (!glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW");

			for (int[] hintPair : GLFW_HINTS) {
				glfwWindowHint(hintPair[0], hintPair[1]);
			}

			window = glfwCreateWindow(INIT_WIDTH, INIT_HEIGHT, WINDOW_TITLE, NULL, NULL);

			if (window == NULL) {
				throw new RuntimeException("Failed to create the GLFW window");
			}

			glfwSetKeyCallback(window, (localWindow, key, scancode, action, mods) -> {
				if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
					glfwSetWindowShouldClose(localWindow, true);
			});

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - INIT_WIDTH)/2, (vidmode.height() - INIT_HEIGHT)/2);

			glfwMakeContextCurrent(window);		// Make the OpenGL context current
			glfwSwapInterval(GLFW_VSYNC);		// Set v-sync
			glfwShowWindow(window);				// Make the window visible

			glfwSetWindowSizeCallback(window, (localWindow, newWidth, newHeight) -> {
				glViewport(0, 0, newWidth, newHeight);
			});
		}

		/**
		 * Initializes the GL parameters
		 */
		private void initGL() {
			GL.createCapabilities();

	        for (int flag : GL_FLAGS) {
	        	glEnable(flag);
	        }

	        Colour.clearColour(Colour.BACKDROP);

			// Matrix Initialization
	        glMatrixMode(GL_PROJECTION);
	        glLoadIdentity();

	        // Define frustum
	        float yTop  = (float) (VIEW_Z_NEAR * Math.tan(Math.toRadians(VIEW_FOV / 2)));
		    float xLeft = yTop * VIEW_ASPECT;
		    glFrustum(xLeft, -xLeft, -yTop, yTop, VIEW_Z_NEAR, VIEW_Z_FAR);

	        glMatrixMode(GL_MODELVIEW);
	        glLoadIdentity();
		}

		/**
		 * Rotates the perspective of the camera to its initial state.
		 */
		private void rotateView() {
			glRotatef(-50f, 1f, 0f, 0f);

			glTranslatef(0f, 0f, -1.5f);
			glTranslatef(0f, 1.6f, 0f);

			glRotatef(45f, 0f, 0f, 1f);
		}

}