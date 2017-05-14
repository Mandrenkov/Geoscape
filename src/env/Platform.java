package env;

import static org.lwjgl.opengl.GL11.*;

import geo.Point;
import util.*;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <b>Platform</b> class represents the platform beneath the landscape.</p>
 */
public class Platform implements Drawable {
	/**
	 * Vertices of the rectangular prism that bounds the platform.
	 */
	Point[][][] pointPrism;

	/**
	 * Constructs a Platform object with the given geometric constraints.
	 *
	 * @param MIN_X Minimum X constraint
	 * @param MAX_X Maximum X constraint
	 * @param MIN_Y Minimum Y constraint
	 * @param MAX_Y Maximum Y constraint
	 * @param MIN_Z Minimum Z constraint
	 * @param MAX_Z Maximum Z constraint
	 */
	public Platform(final float MIN_X, final float MAX_X, final float MIN_Y, final float MAX_Y, final float MIN_Z, final float MAX_Z) {
		// Points denoting the vertices of the platform cube
		pointPrism = new Point[2][2][2];

		// Generate cube vertices
		for (int iZ = 0 ; iZ < 2 ; iZ++) {
			float z = iZ == 0 ? MIN_Z : MAX_Z;

			for (int iY = 0 ; iY < 2 ; iY++) {
				float y = iY == 0 ? MIN_Y : MAX_Y;

				for (int iX = 0 ; iX < 2 ; iX++) {
					float x = iX == 0 ? MIN_X : MAX_X;

					pointPrism[iZ][iY][iX] = new Point(x, y, z);
				}
			}
		}
	}

	/**
	 * Draws this Platform to the screen.
	 */
	public void draw() {
		// Front and back faces should be rendered
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		// Set platform colour
		Colour.setColour(Colour.PLATFORM);

		// Render platform
		glBegin(GL_QUADS);
			// X Constant
			Render.addVertex(pointPrism[0][0][0]); Render.addVertex(pointPrism[0][1][0]); Render.addVertex(pointPrism[1][1][0]); Render.addVertex(pointPrism[1][0][0]);
			Render.addVertex(pointPrism[0][0][1]); Render.addVertex(pointPrism[1][0][1]); Render.addVertex(pointPrism[1][1][1]); Render.addVertex(pointPrism[0][1][1]);

			// Y Constant
			Render.addVertex(pointPrism[0][0][0]); Render.addVertex(pointPrism[1][0][0]); Render.addVertex(pointPrism[1][0][1]); Render.addVertex(pointPrism[0][0][1]);
			Render.addVertex(pointPrism[0][1][0]); Render.addVertex(pointPrism[0][1][1]); Render.addVertex(pointPrism[1][1][1]); Render.addVertex(pointPrism[1][1][0]);

			// Z Constant
			Render.addVertex(pointPrism[0][0][0]); Render.addVertex(pointPrism[0][1][0]); Render.addVertex(pointPrism[0][1][1]); Render.addVertex(pointPrism[0][0][1]);
		glEnd();
	}
}