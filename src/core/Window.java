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
 * @since March 13, 2018
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
        return this.handle;
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The reference to the Window singleton.
     */
    private static Window singleton = null;

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

        // Apply a set of GLFW hints for the upcoming window.
        ArrayList<Pair<Integer, Integer>> glfwHints = new ArrayList<>(Arrays.asList(
            new Pair<Integer, Integer>(GLFW_VISIBLE,      1),
            new Pair<Integer, Integer>(GLFW_RESIZABLE,    1),
            new Pair<Integer, Integer>(GLFW_FOCUSED,      1),
            new Pair<Integer, Integer>(GLFW_REFRESH_RATE, 144),
            new Pair<Integer, Integer>(GLFW_SAMPLES,      2)
        ));
        for (Pair<Integer, Integer> hint : glfwHints) {
            glfwWindowHint(hint.getFirst(), hint.getSecond());
        }

        this.handle = glfwCreateWindow(1600, 900, "Geoscape", NULL, NULL);
        if (this.handle == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        glfwMakeContextCurrent(this.handle);
        glfwShowWindow(this.handle);

        int vsync = GL_TRUE;
        glfwSwapInterval(vsync);
    }

    /**
     * Initializes the callbacks of this Window.
     */
    private void initCallbacks() {
        // Ensure the OpenGL viewport matches the Window dimensions.
        glfwSetWindowSizeCallback(this.handle, (localWindow, newWidth, newHeight) -> {
            glViewport(0, 0, newWidth, newHeight);
        });

        //glfwSetKeyCallback(this.handle, (localWindow, key, scancode, action, mods) -> {
        //    if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
        //        glfwSetWindowShouldClose(localWindow, true);
        //});
    }

    /**
     * Initializes the OpenGL parameters of this Window.
     */
    private void initGL() {
        GL.createCapabilities();

        // Apply a set of OpenGL flags.
        int[] glFlags = new int[] {
            GL_CULL_FACE,
            GL_DEPTH_TEST,
            GL_LIGHTING,
            GL_MULT,
            GL_POLYGON_SMOOTH
        };
        for (int flag : glFlags) {
            glEnable(flag);
        }

        // Set the background colour of the Window.
        glClearColor(0.05f, 0.05f, 0.05f, 1.0f);

        // Load the OpenGL projection matrix to set the viewing frustrum.
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // The vertical FOV of the viewing frustrum.
        float fov = 40f;
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

        Logger.debug("Viewing frustum has dimensions (%.3f, %.3f) to (%.3f, %.3f) over [%.3f, %.3f].", -x, -y, x, y, near, far);

        // Load the OpenGL ModelView matrix to manipulate vertex coordinates.
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Set the polygon rendering mode.  The effect of each option is described
        // below:
        //   GL_POINT - Render the vertexes of each polygon.
        //   GL_LINE  - Render the outlines of each polygon.
        //   GL_FILL  - Render the faces of each polygon.
        glPolygonMode(GL_FRONT, GL_FILL);

        // Set the ambient colour of the OpenGL scene.
        float[] ambient = {0.2f, 0.2f, 0.2f, 1};
        glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambient);

        // All materials in the OpenGL scene reflect ambient and diffuse light.
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        glEnable(GL_COLOR_MATERIAL);

        // Each polygon in the OpenGL scene is shaded using one colour. 
        glShadeModel(GL_FLAT);
    }
}