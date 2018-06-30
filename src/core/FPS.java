package core;

/**
 * The FPS class tracks the current FPS.
 */
public class FPS {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs an FPS counter with the given Window and update delay.
     * 
     * @param window The window that applies to this FPS counter.
     * @param delay  The number of seconds to wait between Window title updates.
     */
    public FPS(Window window, double delay) {
        this.window = window;
        this.delay = delay;
        this.frames = 0;
    }

    /**
     * Updates the number of frames that are tracked by this FPS counter.
     * 
     * This function should be called every time a new frame is displayed on the screen.
     */
    public void update(double now) {
        ++this.frames;
        if (now >= this.syncTime) {
            this.updateWindowTitle();
            this.frames = 0;
            this.syncTime = now + this.delay;
        }
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The Window object associated with this FPS counter.
     */
    private Window window;

    /**
     * The number of frames that have transpired since the last synchronization
     * time updated.
     */
    private int frames;

    /**
     * The next moment in time when the FPS counter in the Window title should
     * be updated.
     */
    private double syncTime;

    /**
     * The number of seconds in between Window title updates.
     */
    private double delay;

    /**
     * Updates the title of the Window associated with this FPS counter.
     */
    private void updateWindowTitle() {
        String format = "%s (%d FPS)";
        String base = this.window.getBaseTitle();
        int fps = (int) (this.frames/this.delay);
        String title = String.format(format, base, fps);
        this.window.setTitle(title);
    }
}