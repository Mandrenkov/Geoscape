package env;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <i>Camera</i> class manipulates the view perspective.</p>
 */
public class Camera {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Axis represents an axis in Cartesian space.
     */
    public enum Axis {
        X,  // X-axis
        Y,  // Y-axis
        Z   // Z-axis
    };

    /**
     * Returns the singleton Camera instance.
     *
     * @return The singleton instance.
     */
    public static Camera getInstance() {
        if (singleton == null) {
            singleton = new Camera();
        }
        return singleton;
    }

    /**
     * Apply the translation and rotation matrices of this Camera to the ModelView
     * matrix.  All subsequent glVertex() calls will be translated by the position
     * of this Camera and then rotated according to the angle of this Camera. 
     */
    public void capture() {
        glLoadIdentity();
        glMultMatrixf(rotationMatrix);
        glMultMatrixf(translationMatrix);
    }

    /**
     * Rotates the Camera about the given Axis by the specified value in degrees.
     *
     * @param rotation The degree of CCW rotation about the Axis.
     * @param x        The X-component of the line.
     * @param y        The Y-component of the line.
     * @param z        The Z-component of the line.
     */
    public void rotate(float rotation, float x, float y, float z) {
        glPushMatrix();
            glLoadMatrixf(rotationMatrix);
            glRotatef(rotation, x, y, z);
            glGetFloatv(GL_MODELVIEW_MATRIX, rotationMatrix);
        glPopMatrix();
    }

    /**
     * Translates this Camera by the given coordinate values.
     *
     * @param dx The amount to translate the X-coordinate of this Camera.
     * @param dy The amount to translate the Y-coordinate of this Camera.
     * @param dz The amount to translate the Z-coordinate of this Camera.
     */
    public void translate(float dx, float dy, float dz) {
        glPushMatrix();
            glLoadMatrixf(translationMatrix);
            glTranslatef(-dx, -dy, -dz);
            glGetFloatv(GL_MODELVIEW_MATRIX, translationMatrix);
        glPopMatrix();
    }

    /**
     * Sets the position of this Camera.
     *
     * @param x The new X-coordinate of this Camera.
     * @param y The new Y-coordinate of this Camera.
     * @param z The new Z-coordinate of this Camera.
     */
    public void setPosition(float x, float y, float z) {
        glPushMatrix();
            glLoadIdentity();
            glTranslatef(-x, -y, -z);
            glGetFloatv(GL_MODELVIEW_MATRIX, translationMatrix);
        glPopMatrix();
    }

    // Private members
    // -------------------------------------------------------------------------

    /**
     * The reference to the Camera singleton.
     */
    private static Camera singleton = null;

    /**
     * The translation matrix representing the position of this Camera.
     */
    private float[] translationMatrix;

    /**
     * The rotation matrix representing the angle of this Camera.
     */
    private float[] rotationMatrix;

    /**
     * Constructs a new Camera at the origin facing down the positive Z-axis.
     */
    private Camera() {
        glLoadIdentity();

        translationMatrix = new float[16];
        glGetFloatv(GL_MODELVIEW_MATRIX, translationMatrix);

        rotationMatrix = new float[16];
        glGetFloatv(GL_MODELVIEW_MATRIX, rotationMatrix);
    }
}