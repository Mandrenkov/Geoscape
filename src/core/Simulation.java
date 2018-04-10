package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import bio.BioMap;
import bio.Biome;
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

        // Create the Platform underneath the World.
        float minZ = -0.15f;
        float maxZ =  0.00f;
        Platform platform = new Platform(minX, minY, minZ, maxX, maxY, maxZ);
        this.world.add(platform);

        int size = Top.DEBUG ? 100 : 300;

        // Generate the BioMap characterizing the landscape of the World.
        BioMap biomap = new BioMap(size, size);
        biomap.setRect(0,                     0,                    biomap.getCols() - 1, biomap.getRows() - 1,       Biome.HILL);
        biomap.setCloud(0,                    biomap.getRows()*2/3, biomap.getCols()/3,   biomap.getRows() - 1, 4, 4, Biome.PLAINS);
        biomap.setCloud(biomap.getCols()*2/3, biomap.getRows()/2,   biomap.getCols() - 1, biomap.getRows() - 1, 4, 4, Biome.DESERT);
        biomap.setCloud(0,                    0,                    biomap.getCols()/4,   biomap.getRows()/3,   4, 4, Biome.TUNDRA);
        biomap.setCloud(biomap.getCols()/2,   0,                    biomap.getCols() - 1, biomap.getRows()/2,   4, 4, Biome.MOUNTAIN);

        // Instantiate a Grid using the generated BioMap.
        Grid land = new Grid("Land", size, size, 0.05f, minX, minY, maxX, maxY, biomap);
        this.world.add(land);

        // Declare the set of Lights which illuminate the World in this simulation.
        Light[] lights = new Light[]{
            new Light(new Vertex(0f, -0.8f, 0.75f), new Colour(1f, 0.5f, 0))
        };
        this.world.addLights(lights);
    }

    /**
     * Starts the Simulation.  This function will only return execution control
     * to its caller when the Window singleton is closed or an unhandled exception
     * is raised.
     */
    public void start() {
        Logger.info("Rendering %s.", this.world);
        syncTime = glfwGetTime();

        Camera camera = Camera.getInstance();
        camera.rotate(-65, 1, 0, 0);
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
        float height = 1.0f;
        // The origin of the Camera path.
        Vertex origin = new Vertex(0.2f, -0.2f, 0);
        // The radius of the Camera path.
        float radius = 2.5f;
        
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

            for (Light light : world.getLights()) {
                light.glPosition();
            }

            // Reposition the Lights to adjust to the new Camera state.
            //for (Light light : world.getLights()) {
                //light.glPosition();
            //}

            // Render the World.
            world.draw();

            

            // Prepare for the next frame.
            glfwSwapBuffers(handle);
            glfwPollEvents();
        }
    }
}