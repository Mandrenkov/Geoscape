package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import bio.BioMap;
import bio.BioMapFactory;
import env.Backdrop;
import env.Colour;
import env.Grid;
import env.Light;
import env.Overlay;
import env.Platform;
import env.World;
import geo.Vertex;
import util.RNG;
import view.Camera;
import view.FrameTracker;
import view.Viewer;
import view.Window;

/**
 * The Simulation class represents the Geoscape simulation.
 */
public class Simulation {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Simulation object with the given Window.
     * 
     * @param window The Window that will display this Simulation.
     */
    public Simulation(Window window) {
        this.viewer = new Viewer(window);

        // Instantiate the World representing this Simulation.
        float minX = -0.8f;
        float minY = -0.8f;
        float maxX =  0.8f;
        float maxY =  0.8f;
        this.world = new World("Geoscape", minX, minY, maxX, maxY);

        // Create the Backdrop behind the World.
        Backdrop backdrop = new Backdrop(new Vertex(Vertex.ORIGIN), 5f);
        this.world.add(backdrop);

        // Create the Platform underneath the World.
        float minZ = -0.200f;
        float maxZ =  0.003f;
        int platformSize = 10;
        Platform platform = new Platform(minX, minY, minZ, maxX, maxY, maxZ, platformSize, platformSize);
        this.world.add(platform);

        // Create the landscape of the World.
        {
            int size = Top.DEBUG ? 100 : 300;
            BioMap biomap = BioMapFactory.create(BioMapFactory.Type.LAND, size, size);
            Grid land = new Grid("Land", size, size, 0.06f, minX, minY, maxX, maxY, biomap);
            this.world.addGrids(land);
        }

        // Create the water in the World.
        {
            int size = Top.DEBUG ? 100 : 150;
            BioMap biomap = BioMapFactory.create(BioMapFactory.Type.WATER, size, size);
            Grid water = new Grid("Water", size, size, 0.015f, minX, minY, maxX, maxY, biomap);
            this.world.addGrids(water);
        }

        // Create a set of Lights to illuminate the World.
        {
            Colour yellow = new Colour(1f, 0.5f, 0);

            float x = RNG.random(-1.0f, 1.0f);
            float y = RNG.random(-1.0f, 1.0f);
            float z = RNG.random(0.6f, 0.8f);
            Vertex location = new Vertex(x, y, z);

            Light sun = new Light(location, yellow);
            this.world.addLights(sun);
        }
    }

    /**
     * Starts the Simulation.  This function will only return execution control
     * to its caller when the Window singleton is closed or an unhandled
     * exception is raised.
     */
    public void start() {
        Logger.info("Rendering %s.", this.world);
        // Ideally, the Geoscape controls should be displayed on the screen.  Oh well.
        this.viewer.logControls();
        loop();
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The World associated with this Simulation.
     */
    private World world;

    /**
     * The Viewer that manipulates the state of the Camera.
     */
    private Viewer viewer;

    /**
     * Continuously renders the World of this Simulation by controlling the
     * high-level graphics pipeline flow and synchronizing the framerate of the
     * render Window.
     */
    private void loop() {
        Window window = Window.getInstance();
        Camera camera = Camera.getInstance();

        Colour dark = new Colour(0, 0, 0, 0.5f);
        Overlay pauseOverlay = new Overlay(dark);

        long handle = window.getHandle();

        while (!glfwWindowShouldClose(handle)) {
            // Clear the GL buffers.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Update the FPS counter.
            FrameTracker fpsTracker = window.getFrameTracker();
            fpsTracker.update();

            // Update the state of the Camera singleton.
            this.viewer.update();
            camera.capture();

            // Update the position of the Lights with respect to the new
            // ModelView matrix.
            for (Light light : world.getLights()) {
                light.glPosition();
            }

            // Update and draw the World.
            double now = glfwGetTime();
            world.update(now);
            world.draw();

            // Draw the pause Overlay if the Viewer is paused.
            boolean paused = viewer.isPaused();
            if (paused) {
                pauseOverlay.draw();
            }

            // Prepare for the next frame.
            glfwSwapBuffers(handle);
            glfwPollEvents();
        }
    }
}