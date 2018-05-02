package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import bio.BioMap;
import bio.Biome;
import env.Backdrop;
import env.Camera;
import env.Colour;
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

        /**
         * Create the Backdrop behind the World.
         */
        Backdrop backdrop = new Backdrop(new Vertex(Vertex.ORIGIN), 5f);
        this.world.add(backdrop);

        /**
         * Create the Platform underneath the World.
         */
        float minZ = -0.15f;
        float maxZ =  0.00f;
        Platform platform = new Platform(minX, minY, minZ, maxX, maxY, maxZ);
        this.world.add(platform);

        /**
         * Create the Grid representing the landscape in the World.
         */
        int landSize = Top.DEBUG ? 100 : 300;

        BioMap landMap = new BioMap(landSize, landSize);
        landMap.setRect(0,                      landMap.getRows()/3,   landMap.getCols() - 1, landMap.getRows() - 1,       Biome.HILL);
        landMap.setCloud(0,                     landMap.getRows()*2/3, landMap.getCols()/3,   landMap.getRows() - 1, 4, 4, Biome.PLAINS);
        landMap.setCloud(landMap.getCols()*2/3, landMap.getRows()*2/3, landMap.getCols() - 1, landMap.getRows() - 1, 4, 4, Biome.DESERT);
        landMap.setCloud(0,                     0,                     landMap.getCols()/4,   landMap.getRows()/3,   4, 4, Biome.TUNDRA);
        landMap.setCloud(landMap.getCols()*2/3, landMap.getRows()/4,   landMap.getCols() - 1, landMap.getRows()*2/3, 4, 4, Biome.MOUNTAIN);

        Grid land = new Grid("Land", landSize, landSize, 0.08f, minX, minY, maxX, maxY, landMap);
        this.world.addGrids(land);

        /**
         * Create the Grid representing the water in the World.
         */
        int waterSize = Top.DEBUG ? 100 : 150;

        BioMap waterMap = new BioMap(waterSize, waterSize);
        waterMap.setRect(5, 0, waterMap.getCols() - 1, waterMap.getRows() - 6, Biome.WATER);

        Grid water = new Grid("Water", waterSize, waterSize, 0.02f, minX, minY, maxX, maxY, waterMap);
        this.world.addGrids(water);

        /**
         * Create a set of Lights to illuminate the World.
         */
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