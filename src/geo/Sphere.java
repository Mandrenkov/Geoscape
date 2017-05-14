package geo;

import java.util.ArrayList;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Sphere</b> class.</p>
 */
public class Sphere {

	private static final int REFINE_STEPS = 5;

	private ArrayList<Point> points;
	private ArrayList<Triangle> triangles;
	private Point origin;
	private float radius;

	public Sphere(Point origin, float radius) {
		this.origin = origin;
		this.radius = radius;

		generateTriangles();
	}

	public void generateTriangles() {
		points = new ArrayList<>();
		triangles = new ArrayList<>();

		genDiamond();
		genRefine();
		genScale();
		genTranslate();
	}

	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}

	private void genDiamond() {
		Point u = new Point(0f, 0f,  1);
		Point d = new Point(0f, 0f, -1f);
		Point l = new Point(-1f, 0f, 0f);
		Point r = new Point( 1f, 0f, 0f);
		Point b = new Point(0f, -1f, 0f);
		Point f = new Point(0f,  1f, 0f);

		points.add(u);
		points.add(d);
		points.add(l);
		points.add(r);
		points.add(b);
		points.add(f);

		triangles.add(new Triangle(u, f, l));
		triangles.add(new Triangle(u, l, b));
		triangles.add(new Triangle(u, b, r));
		triangles.add(new Triangle(u, r, f));
		triangles.add(new Triangle(d, l, f));
		triangles.add(new Triangle(d, f, r));
		triangles.add(new Triangle(d, r, b));
		triangles.add(new Triangle(d, b, l));
	}

	private void genRefine() {
		for (int a = 0 ; a < REFINE_STEPS ; a++) {
			ArrayList<Triangle> newTriangles = new ArrayList<>();

			for (Triangle t : triangles) {
				Point[] triPoints = t.getPoints();
				Point[] midPoints = new Point[3];

				Point midPoint;
				GeoVector midVector;

				for (int i = 0 ; i < 3 ; ++i) {
					midPoint = triPoints[i].average(triPoints[(i + 1) % 3]);

					midVector = new GeoVector(midPoint);
					midVector.normalize();

					midPoint = new Point(midVector.getX(), midVector.getY(), midVector.getZ());
					midPoints[i] = midPoint;
					points.add(midPoint);
				}

				newTriangles.add(new Triangle(midPoints[0], triPoints[0], midPoints[2]));
				newTriangles.add(new Triangle(midPoints[1], triPoints[1], midPoints[0]));
				newTriangles.add(new Triangle(midPoints[2], triPoints[2], midPoints[1]));
				newTriangles.add(new Triangle(midPoints[2], midPoints[1], midPoints[0]));
			}

			triangles = newTriangles;
		}
	}

	private void genScale() {
		for (Point p : points) {
			p.setX(p.getX()*radius);
			p.setY(p.getY()*radius);
			p.setZ(p.getZ()*radius);
		}
	}

	private void genTranslate() {
		for (Point p : points) {
			p.setX(p.getX() + origin.getX());
			p.setY(p.getY() + origin.getY());
			p.setZ(p.getZ() + origin.getZ());;
		}
	}
}