package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import bio.BioMap;
import bio.BioMapFactory;
import env.Backdrop;
import env.Camera;
import env.Colour;
import env.Grid;
import env.Light;
import env.Platform;
import env.World;
import geo.Vertex;

/**
 * @author  Mikhail Andrenkov
 * @since   May 5, 2018
 * @version 1.2
 *
 * <p>The <b>Simulation</b> class represents the Geoscape simulation.</p>
 */
public class Simulation {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Simulation object.
     */
    public Simulation() {
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
            Grid water = new Grid("Water", size, size, 0.02f, minX, minY, maxX, maxY, biomap);
            this.world.addGrids(water);
        }

        // Create a set of Lights to illuminate the World.
        Light[] lights = new Light[]{
            new Light(new Vertex(0f, -0.7f, 0.7f), new Colour(1f, 0.5f, 0))
        };
        this.world.addLights(lights);
    }

    /**
     * Starts the Simulation.  This function will only return execution control
     * to its caller when the Window singleton is closed or an unhandled
     * exception is raised.
     */
    public void start() {
        Logger.info("Rendering %s.", this.world);
        syncTime = glfwGetTime();

        Camera camera = Camera.getInstance();
        camera.rotate(-60, 1, 0, 0);
        camera.rotate(25, 0, 0, 1);

        loop();
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The World associated with this Simulation.
     */
    private World world;

    /**
     * The time used to synchronize the framerate during each loop iteration.
     */
    private double syncTime;

    /**
     * Continuously renders the World of this Simulation by controlling the
     * high-level graphics pipeline flow and synchronizing the framerate of the
     * render Window.
     */
    private void loop() {
        // The Camera is initially facing the positive Y-axis.
        float angle = (float) Math.PI*2/3;
        // The height of the Camera path.
        float height = 1.7f;
        // The origin of the Camera path.
        Vertex origin = new Vertex(0.1f, -0.1f, 0);
        // The radius of the Camera path.
        float radius = 3.0f;
        
        Camera camera = Camera.getInstance();
        long handle = Window.getInstance().getHandle();
        while (!glfwWindowShouldClose(handle)) {
            // Clear the GL buffers.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Update the framerate synchronizer and rotate the viewport by a
            // factor that is proportional to the current frame period.
            double now = glfwGetTime();
            double period = now - syncTime;
            syncTime = now;

            // The target degree of rotation along the z-axis every second.
            float rotationDelta = 30;
            float rotation = (float) (period*rotationDelta);
            camera.rotate(rotation, 0, 0, 1);
            angle += Math.toRadians(rotation);

            // Compute the position of the Camera.
            float x = (float) Math.cos(-angle)*radius + origin.getX();
            float y = (float) Math.sin(-angle)*radius + origin.getY();
            camera.setPosition(x, y, height);

            camera.capture();

            // Update the position of the Lights with respect to the new
            // ModelView matrix.
            for (Light light : world.getLights()) {
                light.glPosition();
            }

            // Update and draw the World.
            world.update(now);
            world.draw();

            // Prepare for the next frame.
            glfwSwapBuffers(handle);
            glfwPollEvents();
        }
    }
}