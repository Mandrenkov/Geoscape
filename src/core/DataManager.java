package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import model.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>DataManager</b> class.</p>
 */ 
public class DataManager {

	/* Window */

	public long window = NULL;
	public final int INIT_WIDTH = 640;
	public final int INIT_HEIGHT = 480;
	public final String WINDOW_TITLE = "Randomized Geoscape";

	public int[] glFlags;
	public int[][] windowHints;

	/* GUI */

	public float[] colourBG;
	public float[] colourBase;

	public static final int ROWS = 200;
	public static final int COLS = 200;
	public static final float PADDING_X = 0.2f;
	public static final float PADDING_Y = 0.2f;
	public static final float MIN_X = -1 + PADDING_X;
	public static final float MIN_Y = -1 + PADDING_Y;
	public static final float MAX_X =  1 - PADDING_X;
	public static final float MAX_Y =  1 - PADDING_Y;
	public static final float RANGE_X = MAX_X - MIN_X;
	public static final float RANGE_Y = MAX_Y - MIN_Y;
	public static final float BOUNDARY_PADDING = 0.01f;

	public static final float VIEW_FOV = 70f;
	public static final float VIEW_ASPECT = 1f;
	public static final float VIEW_Z_NEAR = 0.01f;
	public static final float VIEW_Z_FAR = 5f;

	public static final float BASE_MIN_Z = -0.30f;
	public static final float BASE_MAX_Z = 0.00f;

	/* Model */

	public ArrayList<Terrain> landscape;

	private TerrainMap terrainMap;


	public DataManager() {
		setupWindow();
		setupGUI();
		setupModel();
	}

	private void setupWindow() {
		glFlags = new int[] {
			GL_DEPTH_TEST,
			GL_CULL_FACE,
			GL_MULT
		};

		windowHints = new int[][] {
			{GLFW_VISIBLE, GLFW_FALSE},
			{GLFW_RESIZABLE, GLFW_TRUE},
			{GLFW_SAMPLES, 4}
		};
	}

	private void setupGUI() {
		colourBG   = new float[] {0.6f, 0.9f, 1.0f, 1.0f};
		colourBase = new float[] {0.1f, 0.1f, 0.1f};
	}

	private void setupModel() {
		landscape = new ArrayList<>();
		terrainMap = new TerrainMap(ROWS, COLS);


	}


	public void generateLandscape() {
		float boundaryX1 = -0.3f;
		float boundaryX2 =  0.3f;
		float boundaryY1 =  0.0f;

		landscape.add(new Hill     (new Grid(MIN_X     , MIN_Y, boundaryX1, boundaryY1)));
		landscape.add(new Tundra   (new Grid(boundaryX1, MIN_Y, boundaryX2, boundaryY1)));
		landscape.add(new Wetland  (new Grid(boundaryX2, MIN_Y, MAX_X     , boundaryY1)));

		landscape.add(new Mountain (new Grid(MIN_X     , boundaryY1, boundaryX1, MAX_Y)));
		landscape.add(new Desert   (new Grid(boundaryX1, boundaryY1, boundaryX2, MAX_Y)));
		landscape.add(new Plateau  (new Grid(boundaryX2, boundaryY1, MAX_X     , MAX_Y)));

		//landscape.add(new Lake    (new Grid(MIN_X, MIN_Y, MAX_X, MAX_Y)));
		landscape.add(new Lake    (new Grid(MIN_X + BOUNDARY_PADDING, MIN_Y + BOUNDARY_PADDING, MAX_X - BOUNDARY_PADDING, MAX_Y - BOUNDARY_PADDING)));
	}

	public int[] getWindowDimensions() {
		Pointer widthPtr = new Pointer();
		Pointer heightPtr = new Pointer();

		glfwGetWindowSize(window, widthPtr.array(), heightPtr.array());

		return new int[] {widthPtr.get(), heightPtr.get()};
	}

	public int getWindowHeight() {
		return getWindowDimensions()[1];
	}

	public int getWindowWidth() {
		return getWindowDimensions()[0];
	}


}