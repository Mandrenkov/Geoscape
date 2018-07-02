package core;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The FrameTracker class tracks the FPS of a Window.
 */
public class FrameTracker {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a FrameTracker with the given Window and update interval.
     * 
     * @param window   The window to be associated this FrameTracker.
     * @param interval The number of seconds to wait between Window title updates.
     */
    public FrameTracker(Window window, double interval) {
        this.window = window;
        this.interval = interval;
        this.frames = 0;

        this.prevTime = 0;
        this.nextTime = interval;
    }

    /**
     * Returns the current FPS of the Window associated with the FrameTracker.
     * 
     * @return The FPS.
     */
    public int getFPS() {
        double now = glfwGetTime();
        double period = now - this.prevTime;
        return (int) (this.frames/period);
    }

    /**
     * Increments the number of frames that have transpired and potentially
     * updates the title of the Window associated with this FrameTracker.
     * 
     * This function should be called every time a new frame is displayed on the
     * Window.
     */
    public void update() {
        ++this.frames;

        double now = glfwGetTime();
        if (now >= this.nextTime) {
            this.updateWindowTitle();
            this.frames = 0;
            this.prevTime = now;
            this.nextTime = now + this.interval;
        }
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The Window object associated with this FrameTracker.
     */
    private Window window;

    /**
     * The number of frames that have transpired since the last synchronization
     * time updated.
     */
    private int frames;

    /**
     * The last time this FrameTracker has updated the Window title.
     */
    private double prevTime;

    /**
     * The next time this FrameTracker will update the Window title.
     */
    private double nextTime;

    /**
     * The number of seconds in between Window title updates.
     */
    private double interval;

    /**
     * Updates the title of the Window associated with this FrameTracker.
     */
    private void updateWindowTitle() {
        String title = String.format("%s (%d FPS)", this.window.getBaseTitle(), this.getFPS());
        this.window.setTitle(title);
    }
}