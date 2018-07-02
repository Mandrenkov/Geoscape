package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import core.Build;
import util.Pair;

/**
 * The Window class represents the application GLFW window.
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

    /**
     * Returns the base title of this Window.
     * 
     * @return The base title.
     */
    public String getBaseTitle() {
        return this.baseTitle;
    }

    /**
     * Returns the title of this Window.
     * 
     * @return The title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of this Window to the specified value.
     * 
     * @param title The new title of this Window.
     */
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(this.handle, title);
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
     * The base title of this Window.
     */
    private String baseTitle = String.format("Geoscape %d.%d", Build.getMajorVersion(), Build.getMinorVersion());

    /**
     * The current title of this Window.
     */
    private String title = baseTitle;

    /**
     * Constructs a Window object.
     */
    private Window() {
        initGLFW();
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

        this.handle = glfwCreateWindow(1600, 900, this.title, NULL, NULL);
        if (this.handle == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        glfwMakeContextCurrent(this.handle);
        glfwShowWindow(this.handle);

        String iconPath = "../assets/icon.png";
        this.setIcon(iconPath);

        int vsync = GL_TRUE;
        glfwSwapInterval(vsync);

        glfwSetWindowSizeCallback(this.handle, this::windowSizeCallback);

        // Hide the mouse cursor when the Geoscape window is in focus.
        glfwSetInputMode(this.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    /**
     * Sets the icon of this Window to the image located at the given path.
     * 
     * @param path The path to the icon.
     */
    private void setIcon(String path) {
        // Load the icon into a BufferedImage object.
        BufferedImage icon = this.readImage(path);
        if (icon == null) {
            return;
        }

        // Convert the BufferedImage object into a ByteBuffer object.
        ByteBuffer buffer = this.copyImageToBuffer(icon);

        // Create a GLFW icon from the icon ByteBuffer.
        GLFWImage glfwIcon = GLFWImage.malloc();
        glfwIcon.set(icon.getWidth(), icon.getHeight(), buffer);

        // Create a GLFW image buffer to store the GLFW icon.
        GLFWImage.Buffer glfwIcons = GLFWImage.malloc(1);
        glfwIcons.put(0, glfwIcon);
    
        // Set the icon of the GLFW window to the lone icon in the GLFW image buffer.
        glfwSetWindowIcon(this.handle, glfwIcons);

        // Free the memory occupied by the GLFW icon and image buffer.
        glfwIcons.free();
        glfwIcon.free();
    }

    /**
     * Reads the image located at the given path into a BufferedImage object.
     * 
     * @param path The path where the image is located.
     * 
     * @return The BufferedImage representation of the image.
     */
    private BufferedImage readImage(String path) {
        try {
            File file = new File(path);
            return ImageIO.read(file);
        } catch (IOException e) {
            Logger.error("Failed to read image at '%s': %s", path, e.getMessage());
            return null;
        }
    }

    /**
     * Copies the contents of the given BufferedImage object into a ByteBuffer.
     * 
     * @param image The BufferedImage object to copy.
     * 
     * @return The ByteBuffer representation of the given image.
     */
    private ByteBuffer copyImageToBuffer(BufferedImage image) {
        // The number of bytes in the ByteBuffer is the number of pixels in the
        // image multiplied by the number of bytes needed to represent an RGBA value.
        int width = image.getWidth();
        int height = image.getHeight();
        int capacity = 4*width*height;

        ByteBuffer buffer = BufferUtils.createByteBuffer(capacity);
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                int offset = 4*(row*width + col);

                // The RGBA value of a BufferedImage pixel is encoded as 0xAARRGGBB.
                int pixel = image.getRGB(col, row);
                byte a = (byte) (pixel >> 24);
                byte r = (byte) (pixel >> 16);
                byte g = (byte) (pixel >> 8);
                byte b = (byte) (pixel >> 0);

                // The RGBA value of a GLFW pixel is encoded as 0xRRGGBBAA.
                buffer.put(offset + 0, r);
                buffer.put(offset + 1, g);
                buffer.put(offset + 2, b);
                buffer.put(offset + 3, a);
            }
        }
        // The buffer must be flipped to toggle between reading and writing.
        buffer.flip();
        return buffer;
    }

    /**
     * Adjusts the OpenGL viewport to match the given Window dimensions.
     * 
     * @param window The handle to the GLFW Window.
     * @param width  The width of the window.
     * @param height The height of the window.
     */
    private void windowSizeCallback(long window, int width, int height) {
        glViewport(0, 0, width, height);
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
        float far = 100f;

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
        float[] ambient = {0.4f, 0.4f, 0.4f, 1};
        glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambient);

        // All materials in the OpenGL scene reflect ambient and diffuse light.
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        glEnable(GL_COLOR_MATERIAL);

        // Each polygon in the OpenGL scene is shaded using one colour. 
        glShadeModel(GL_FLAT);
    }
}