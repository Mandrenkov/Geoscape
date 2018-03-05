package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import bio.Biogle;
import bio.Biomap;
import bio.Biome;
import env.Camera;
import env.Grid;
import env.Light;
import env.Platform;
import env.World;
import geo.Vertex;

/**
 * @author Mikhail Andrenkov
 * @since February 13, 2018
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

		// Create the Platform underneath World.
		float minZ = -0.30f;
		float maxZ =  0.00f;
		Platform platform = new Platform(minX, minY, minZ, maxX, maxY, maxZ);
		this.world.add(platform);

		int size = Top.DEBUG ? 100 : 300;

		// Generate the Biomap characterizing the landscape of the World.
		Biomap biomap = new Biomap(size, size);
		biomap.setRect(0,                    0,                    biomap.getRows(),   biomap.getCols(),   Biome.HILL);
		biomap.setSoft(biomap.getRows()*2/3, 0,                    biomap.getRows(),   biomap.getCols()/3, Biome.PLAIN);
		biomap.setSoft(biomap.getRows()/2,   biomap.getCols()*2/3, biomap.getRows(),   biomap.getCols(),   Biome.DESERT);
		biomap.setSoft(0,                    0,                    biomap.getRows()/3, biomap.getCols()/4, Biome.TUNDRA);
		biomap.setSoft(0,                    biomap.getCols()/2,   biomap.getRows()/2, biomap.getCols(),   Biome.MOUNTAIN);
		
		// Instantiate a Grid using the generated Biomap.
		Grid land = new Grid("Land", size, size, minX, minY, maxX, maxY, biomap);
		world.add(land);

		// Declare the set of Lights which illuminate the World in this simulation.
		Light[] lights = new Light[]{
			new Light(new Vertex(-3f, -3f, 3f))
		};
		world.add(lights);

		// Use the Lights to illuminate the landscape Grid.
		for (Biogle biogle : land.getBiogles()) {
			biogle.illuminate(lights);
		}
	}

	/**
	 * Starts the Simulation.  This function will only return execution control
	 * to its caller when the Window singleton is closed or an unhandled exception
	 * is raised.
	 */
	public void start() {
		Logger.info("Rendering World %s.", this.world);

		// Initialize the Window and Camera singletons.
		Window.getInstance();
		Camera.getInstance();

		// Setup the previous loop time.
		syncTime = glfwGetTime();
		
		loop();
	}


	// Private members
	// -------------------------------------------------------------------------

	/**
	 * Target degree of rotation along the z-axis every second.
	 */
	private static final float Z_ROTATE_DELTA = -30.00f;

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

			float rotation = (float) period*Z_ROTATE_DELTA;

			Camera camera = Camera.getInstance();
			camera.rotate(Camera.Axis.Z, rotation);

			// Prepare for the next frame.
			glfwSwapBuffers(handle);
			glfwPollEvents();
		}	
	}
}