package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;

import util.Pair;

/**
 * @author Mikhail Andrenkov
 * @since February 14, 2018
 * @version 1.1
 *
 * <p>The <i>Window</i> class represents the application GLFW window.</p>
 */
public class Window {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Returns the singleton Window instance.
	 * 
	 * @return The singleton instance.
	 */
	public static Window getInstance() {
		if (singleton == null) {
			singleton = new Window();
		}
		return singleton;
	}

	/**
	 * Returns the GLFW handle to the window.
	 *
	 * @return The GLFW handle.
	 */
	public long getHandle() {
		return handle;
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * The reference to the Window singleton.
	 */
	private static Window singleton = null;

	/**
	 * List of OpenGL flags to enable.
	 */
	private final int[] GL_FLAGS = new int[] {
		GL_DEPTH_TEST,
		GL_CULL_FACE,
		GL_POLYGON_SMOOTH,
		GL_MULT
	};

	/**
	 * List of GLFW window hints.
	 */
	private final ArrayList<Pair<Integer, Integer>> GLFW_HINTS = new ArrayList<>(Arrays.asList(
		new Pair<Integer, Integer>(GLFW_VISIBLE,      1),
		new Pair<Integer, Integer>(GLFW_RESIZABLE,    1),
		new Pair<Integer, Integer>(GLFW_FOCUSED,      1),
		new Pair<Integer, Integer>(GLFW_REFRESH_RATE, 144),
		new Pair<Integer, Integer>(GLFW_FLOATING,     1),
		new Pair<Integer, Integer>(GLFW_SAMPLES,      1)
	));

	/**
	 * Flag that determines whether VSYNC is enabled.
	 */
	private final int VSYNC = 1;

	/**
	 * Internal reference to the GLFW window.
	 */
	private long handle = NULL;

	/**
	 * Constructs a Window object.
	 */
	private Window() {
		initGLFW();
		initCallbacks();
		initGL();
	}

	/**
	 * Initializes the GLFW parameters of this Window.
	 */
	private void initGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW.");
		}

		for (Pair<Integer, Integer> hint : GLFW_HINTS) {
			glfwWindowHint(hint.getFirst(), hint.getSecond());
		}

		handle = glfwCreateWindow(1600, 900, "Geoscape", NULL, NULL);
		if (handle == NULL) {
			throw new IllegalStateException("Failed to create GLFW window.");
		}
	
		glfwMakeContextCurrent(handle);
		glfwShowWindow(handle);
		glfwSwapInterval(VSYNC);
	}

	/**
	 * Initializes the callbacks of this Window.
	 */
	private void initCallbacks() {
		// Ensure the OpenGL viewport matches the Window dimensions.
		glfwSetWindowSizeCallback(handle, (localWindow, newWidth, newHeight) -> {
			glViewport(0, 0, newWidth, newHeight);
		});

		//glfwSetKeyCallback(handle, (localWindow, key, scancode, action, mods) -> {
		//	if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
		//		glfwSetWindowShouldClose(localWindow, true);
		//});
	}

	/**
	 * Initializes the OpenGL parameters of this Window.
	 */
	private void initGL() {
		GL.createCapabilities();

		for (int flag : GL_FLAGS) {
			glEnable(flag);
		}

		// Set the background colour of the Window.
		glClearColor(0.05f, 0.05f, 0.05f, 1.0f);
		//glClearColor(1, 1, 1, 1);

		// Load the OpenGL projection matrix to set the viewing frustrum.
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		// The vertical FOV of the viewing frustrum.
		float fov = 70f;
		// The aspect ratio of the viewing frustrum.
		float ratio = 16f/9f;
		// The distance to the near Z-plane of the viewing frustrum.
		float near = 0.01f;
		// The distance to the far Z-plane of the viewing frustrum.
		float far = 10f;

		// Calculate the bounding box of the near plane in terms of the FOV and 
		// aspect ratio.
		float y = (float) Math.tan(Math.toRadians(fov/2))*near;
		float x = ratio*y;
		glFrustum(x, -x, -y, y, near, far);

		Logger.info("Defining viewing frustum with dimensions (%.3f, %.3f) to (%.3f, %.3f) over [%.3f, %.3f].", -x, -y, x, y, near, far);

		// Load the OpenGL ModelView matrix to manipulate vertex coordinates. 
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		// Fill both faces of any rendered polygons.
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}