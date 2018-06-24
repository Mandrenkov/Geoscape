package env;

import static org.lwjgl.glfw.GLFW.*;

import core.Logger;
import env.Camera;
import geo.Vector;

/**
 * The Viewer class adjusts the location and rotation of the Camera singleton.
 */
public class Viewer {
    
    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Viewer object using the given Window handle.
     * 
     * @param handle The handle of the GLFW Window associated with this Viewer.
     */
    public Viewer(long handle) {
        this.velocity = new Vector(0, 0, 0);
        glfwSetKeyCallback(handle, this::keyCallback);
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
                this.escapeCallback(window, action);
                break;
            case GLFW_KEY_UP:
            case GLFW_KEY_DOWN:
            case GLFW_KEY_LEFT:
            case GLFW_KEY_RIGHT:
            case GLFW_KEY_SPACE:
            case GLFW_KEY_LEFT_CONTROL:
                this.moveCallback(key, action);
                break;
            default:
                String name = glfwGetKeyName(key, scancode);
                Logger.debug("The '%s' key does not have any registered callbacks.", name);
        }
    }
    
    /**
     * Adjusts the state of the Camera singleton according to the state of this Viewer.
     */
    public void update() {
        float dx = this.velocity.getX()/10;
        float dy = this.velocity.getY()/10;
        float dz = this.velocity.getZ()/10;
        
        Camera camera = Camera.getInstance();
        camera.translate(dx, dy, dz);
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The current velocity of this Viewer.
     */
    private Vector velocity;

    /**
     * Closes the given GLFW window if the ESCAPE key is released.
     * 
     * @param window The window to close.
     * @param action The action applied to the ESCAPE key.
     */
    private void escapeCallback(long window, int action) {
        boolean close = action == GLFW_RELEASE;
        glfwSetWindowShouldClose(window, close);
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
            case GLFW_KEY_UP:           direction = new Vector(+1,  0,  0); break;
            case GLFW_KEY_DOWN:         direction = new Vector(-1,  0,  0); break;
            case GLFW_KEY_RIGHT:        direction = new Vector( 0, +1,  0); break;
            case GLFW_KEY_LEFT:         direction = new Vector( 0, -1,  0); break;
            case GLFW_KEY_SPACE:        direction = new Vector( 0,  0, +1); break;
            case GLFW_KEY_LEFT_CONTROL: direction = new Vector( 0,  0, -1); break;
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
}