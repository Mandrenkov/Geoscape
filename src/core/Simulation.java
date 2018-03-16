package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import bio.BioTriangle;
import bio.BioMap;
import bio.Biome;
import env.Camera;
import env.Grid;
import env.Light;
import env.Platform;
import env.World;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
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

        // Create the Platform underneath the World.
        float minZ = -0.30f;
        float maxZ =  0.00f;
        Platform platform = new Platform(minX, minY, minZ, maxX, maxY, maxZ);
        this.world.add(platform);

        int size = Top.DEBUG ? 300 : 300;

        // Generate the BioMap characterizing the landscape of the World.
        BioMap biomap = new BioMap(size, size);
        biomap.setRect(0,                     0,                    biomap.getCols() - 1, biomap.getRows() - 1,       Biome.HILL);
        biomap.setCloud(0,                    biomap.getRows()*2/3, biomap.getCols()/3,   biomap.getRows() - 1, 4, 4, Biome.PLAIN);
        biomap.setCloud(biomap.getCols()*2/3, biomap.getRows()/2,   biomap.getCols() - 1, biomap.getRows() - 1, 4, 4, Biome.DESERT);
        biomap.setCloud(0,                    0,                    biomap.getCols()/4,   biomap.getRows()/3,   4, 4, Biome.TUNDRA);
        biomap.setCloud(biomap.getCols()/2,   0,                    biomap.getCols() - 1, biomap.getRows()/2,   4, 4, Biome.MOUNTAIN);

        // Instantiate a Grid using the generated BioMap.
        Grid land = new Grid("Land", size, size, minX, minY, maxX, maxY, biomap);
        this.world.add(land);

        // Declare the set of Lights which illuminate the World in this simulation.
        Light[] lights = new Light[]{
            new Light(new Vertex(-3f, -3f, 3f))
        };
        this.world.add(lights);

        // Use the Lights to illuminate the landscape Grid.
        for (BioTriangle biogle : land.getTriangles()) {
            biogle.illuminate(lights);
        }
    }

    /**
     * Starts the Simulation.  This function will only return execution control
     * to its caller when the Window singleton is closed or an unhandled exception
     * is raised.
     */
    public void start() {
        Logger.info("Rendering %s.", this.world);
        syncTime = glfwGetTime();
        loop();
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * Target degree of rotation along the z-axis every second.
     */
    private static final float Z_ROTATE_DELTA = 30;

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
        long handle = Window.getInstance().getHandle();
        while (!glfwWindowShouldClose(handle)) {
            // Clear the GL buffers.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render the World.
            world.draw();

            // Update the framerate synchronizer and rotate the viewport by a
            // factor that is proportional to the current frame period.
            double now = glfwGetTime();
            double period = now - syncTime;
            syncTime = now;

            float rotation = (float) (period*Z_ROTATE_DELTA);

            Camera camera = Camera.getInstance();
            camera.rotate(rotation, 0, 0, 1);

            // Prepare for the next frame.
            glfwSwapBuffers(handle);
            glfwPollEvents();
        }
    }
}