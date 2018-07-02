package env;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import core.Logger;
import env.Camera;
import geo.Vector;
import util.Pair;

/**
 * The Viewer class adjusts the location and rotation of the Camera singleton.
 */
public class Viewer {
    
    // Public members
    // -------------------------------------------------------------------------

    /**
     * Logs a description of the controls that are accepted by Viewer objects.
     */
    public static void logControls() {
        // The format of a line resembles the following:
        //     Key 1      Key 2      Description
        // ^   ^          ^          ^
        // Where ^ represents a synchronization position.
        String format = "%-11s    %-11s    %s";
        String empty = "-";
 
        Logger.info("Controls:");
        Logger.info(1, format, "Esc", empty, "Pause");
        Logger.info(1, format, "W", "Up Arrow", "Move forward");
        Logger.info(1, format, "S", "Down Arrow", "Move backward");
        Logger.info(1, format, "A", "Left Arrow", "Strafe left");
        Logger.info(1, format, "D", "Right Arrow", "Strafe right");
        Logger.info(1, format, "Space", empty, "Ascend");
        Logger.info(1, format, "Left Ctrl", "Right Ctrl", "Descend");
        Logger.info(1, format, "Mouse", empty, "Look around");
        Logger.info(1, format, "V", "-", "Toggles Vsync");
    }

    /**
     * Constructs a Viewer object using the given Window handle.
     * 
     * @param handle The handle of the GLFW Window associated with this Viewer.
     */
    public Viewer(long handle) {
        this.velocity = new Vector(0, 0, 0);
        this.cursor = null;
        this.paused = false;

        // Set the initial rotation of the Viewer.
        this.pitch = 30f;
        this.yaw = -45f;
        this.rotate();

        // Set the initial state of the Camera.
        Camera camera = Camera.getInstance();
        camera.translate(-2, -2, 1.5f);

        glfwSetKeyCallback(handle, this::keyCallback);
        glfwSetCursorPosCallback(handle, this::cursorCallback);
    }

    /**
     * Performs an action in response to a key event.
     * 
     * @param window   The handle of the GLFW Window that generated the key event.
     * @param key      The key that was pressed, repeated, or released.
     * @param scancode The scan code of the key that was pressed.
     * @param action   The action that triggered the key event.
     * @param mods     The modifier bits for the key event.
     */
    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        switch (key) {
            case GLFW_KEY_ESCAPE:
                this.pauseCallback(window, action);
                break;
            case GLFW_KEY_V:
                this.vsyncCallback(window, action);
                break;
            case GLFW_KEY_W:            case GLFW_KEY_UP:
            case GLFW_KEY_S:            case GLFW_KEY_DOWN:
            case GLFW_KEY_A:            case GLFW_KEY_LEFT:
            case GLFW_KEY_D:            case GLFW_KEY_RIGHT:
            case GLFW_KEY_SPACE:
            case GLFW_KEY_LEFT_CONTROL: case GLFW_KEY_RIGHT_CONTROL:
                this.moveCallback(key, action);
                break;
            default:
                String name = glfwGetKeyName(key, scancode);
                Logger.debug("The '%s' key does not have any registered callbacks.", name);
        }
    }

    /**
     * Performs an action in response to a cursor event.
     * 
     * @param window   The handle of the GLFW Window that generated the cursor event.
     * @param x        The current X position of the cursor.
     * @param y        The current Y position of the cursor.
     */
    public void cursorCallback(long window, double x, double y) {
        // The rotation state of this Viewer should not be updated if the Viewer
        // is paused or the previous position of the cursor is not known.
        boolean noRotation = this.paused || this.cursor == null;
        if (noRotation) {
            this.cursor = new Pair<>(x, y);
            return;
        }
        
        // Calculate the displacement of the mouse cursor.
        float dx = (float) (x - this.cursor.getFirst());
        float dy = (float) (y - this.cursor.getSecond());

        float sensitivity = 0.15f;

        // The change in the yaw of the Viewer is proportional to the magnitude
        // of the horizontal mouse displacement.
        this.yaw += sensitivity*dx;

        // The change in the pitch of the Viewer is proportional to the magnitude
        // of the vertical mouse displacement.
        float fov = 160;
        float maxPitch =  fov/2;
        float minPitch = -fov/2;
        float rawPitch = this.pitch + sensitivity*dy;
        this.pitch = Math.max(minPitch, Math.min(maxPitch, rawPitch));

        // Update the rotation angle of the Camera.
        this.rotate();

        // Update the internal position of the Cursor.
        this.cursor.set(x, y);
    }

    /**
     * Returns true if this Viewer is paused.
     * 
     * @return True if this Viewer is paused.
     */
    public boolean isPaused() {
        return this.paused;
    }
    
    /**
     * Adjusts the state of the Camera singleton according to the state of this Viewer.
     */
    public void update() {
        // The Camera should not move if the Viewer is paused.
        if (this.paused) {
            return;
        }

        // The forward velocity is encoded in the X-component of the velocity Vector.
        float angle = (float) Math.toRadians(-this.yaw);
        float forward = this.velocity.getX(); 
        float forwardX = (float) Math.sin(angle)*forward;
        float forwardY = (float) Math.cos(angle)*forward;

        // The right velocity is encoded in the Y-component of the velocity Vector.
        float rightAngle = angle - (float) Math.PI/2;
        float right = this.velocity.getY(); 
        float rightX = (float) Math.sin(rightAngle)*right;
        float rightY = (float) Math.cos(rightAngle)*right;

        // Generate the displacement Vector.
        float dx = forwardX + rightX;
        float dy = forwardY + rightY;
        float dz = this.velocity.getZ();
        Vector displacement = new Vector(dx, dy, dz);

        // Normalize the Vector according to the velocity of this Viewer.
        float velocity = 0.01f;
        if (!displacement.isZero()) {
            float magnitude = displacement.magnitude();
            displacement.scale(velocity/magnitude);
        }
        
        // Translate the Camera according to the computed displacement.
        Camera camera = Camera.getInstance();
        camera.translate(displacement.getX(), displacement.getY(), displacement.getZ());
    }

    
    // Private members
    // -------------------------------------------------------------------------

    /**
     * The current velocity of the Viewer.
     */
    private Vector velocity;

    /**
     * The last known position of the mouse cursor.
     */
    private Pair<Double, Double> cursor;

    /**
     * The pitch of the Viewer.
     */
    private float pitch;

    /**
     * The yaw of the Viewer.
     */
    private float yaw;

    /**
     * The pause state of this Viewer.
     */
    private boolean paused;

    /**
     * The Vsync state of this Viewer.
     */
    private boolean vsync;

    /**
     * Toggles the pause state of the given GLFW window if the Esc key is released.
     * 
     * @param window The window to pause.
     * @param action The action applied to the Esc key.
     */
    private void pauseCallback(long window, int action) {
        if (action == GLFW_RELEASE) {
            this.paused = !this.paused;
            int cursorMode = this.paused ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED;
            glfwSetInputMode(window, GLFW_CURSOR, cursorMode);

            String verb = this.paused ? "paused" : "unpaused";
            Logger.info("Simulation %s.", verb);
        }
    }

    /**
     * Toggles the Vsync state of the given GLFW window if the V key is released.
     * 
     * @param window The window to toggle Vsync.
     * @param action The action applied to the V key.
     */
    private void vsyncCallback(long window, int action) {
        if (action == GLFW_RELEASE) {
            this.vsync = !this.vsync;
            int interval = this.vsync ? GL_TRUE : GL_FALSE;
            glfwSwapInterval(interval);

            String verb = this.vsync ? "enabled" : "disabled";
            Logger.info("Vsync %s.", verb);
        }
    }

    /**
     * Adjusts the velocity of this Viewer according to the given key event.
     * 
     * @param key    The key associated with the key event.
     * @param action The action that was performed.
     */
    private void moveCallback(int key, int action) {
        // Only the GLFW_PRESS and GLFW_RELEASE events are of interest.
        if (action == GLFW_REPEAT) {
            return;
        }

        // Compute the direction associated with the given key.
        Vector direction = new Vector(0, 0, 0);
        switch (key) {
            case GLFW_KEY_W:            case GLFW_KEY_UP:            direction = new Vector(+1,  0,  0); break;
            case GLFW_KEY_S:            case GLFW_KEY_DOWN:          direction = new Vector(-1,  0,  0); break;
            case GLFW_KEY_D:            case GLFW_KEY_RIGHT:         direction = new Vector( 0, +1,  0); break;
            case GLFW_KEY_A:            case GLFW_KEY_LEFT:          direction = new Vector( 0, -1,  0); break;
            case GLFW_KEY_SPACE:                                     direction = new Vector( 0,  0, +1); break;
            case GLFW_KEY_LEFT_CONTROL: case GLFW_KEY_RIGHT_CONTROL: direction = new Vector( 0,  0, -1); break;
            default:
                String name = glfwGetKeyName(key, 0);
                Logger.error("The '%s' key is not associated with a direction.", name);
        }

        // Reverse the intended direction of the event if the key is being released.
        boolean reverse = action == GLFW_RELEASE;
        if (reverse) {
            float x = direction.getX();
            float y = direction.getY();
            float z = direction.getZ();
            direction = new Vector(-x, -y, -z);
        }
        
        // Adjust the velocity of this Viewer according to the key event.
        this.velocity.add(direction);
    }

    /**
     * Updates the rotation of the Camera singleton according to the yaw and
     * pitch of this Viewer.
     */
    private void rotate() {
        // The Camera should not rotate if the Viewer is paused.
        if (this.paused) {
            return;
        }

        Camera camera = Camera.getInstance();
        camera.setRotation(-90 + this.pitch, 1, 0, 0);
        camera.rotate(this.yaw, 0, 0, -1);
    }
}