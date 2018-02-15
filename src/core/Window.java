package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import util.Colour;

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
	 * The initial width of the window.
	 */
	private final int INIT_WIDTH = 1600;

	/**
	 * The initial height of the window.
	 */
	private final int INIT_HEIGHT = 900;

	/**
	 * The title of the window.
	 */
	private final String TITLE = "Geoscape";

	/**
	 * List of OpenGL flags to enable.
	 */
	private final int[] GL_FLAGS = new int[] {
		GL_DEPTH_TEST,
		GL_CULL_FACE,
		GL_MULT
	};

	/**
	 * List of GLFW window hints.
	 */
	private final int[][] GLFW_HINTS = new int[][] {
		{GLFW_VISIBLE,      0},
		{GLFW_RESIZABLE,    1},
		{GLFW_FOCUSED,      1},
		{GLFW_REFRESH_RATE, 144},
		{GLFW_SAMPLES,      4}
	};

	/**
	 * Flag that determines whether VSYNC is enabled.
	 */
	private final int VSYNC = 0;

	/**
	 * Internal reference to the GLFW window.
	 */
	private long handle = NULL;

	/**
	 * The field of view (FOV) of the Window viewport.
	 */
	private final float VIEW_FOV = 70f;

	/**
	 * The aspect ratio of the Window viewport.
	 */
	private final float VIEW_ASPECT = 1f;

	/**
	 * The distance to the nearest frustrum plane of the Window.
	 */
	private final float VIEW_Z_NEAR = 0.03f;

	/**
	 * The distance to the furthest frustrum plane of the Window.
	 */
	private final float VIEW_Z_FAR = 5f;

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

		for (int[] hint : GLFW_HINTS) {
			glfwWindowHint(hint[0], hint[1]);
		}

		handle = glfwCreateWindow(INIT_WIDTH, INIT_HEIGHT, TITLE, NULL, NULL);
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
		// TODO: Streamline this function
		GL.createCapabilities();

		for (int flag : GL_FLAGS) {
			glEnable(flag);
		}

		glClearColor(Colour.BACKDROP[0], Colour.BACKDROP[1], Colour.BACKDROP[2], Colour.BACKDROP[3]);

		// Matrix Initialization
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		// Define frustum
		float yTop  = (float) (VIEW_Z_NEAR*Math.tan(Math.toRadians(VIEW_FOV/2)));
		float xLeft = yTop * VIEW_ASPECT;
		glFrustum(xLeft, -xLeft, -yTop, yTop, VIEW_Z_NEAR, VIEW_Z_FAR);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
}