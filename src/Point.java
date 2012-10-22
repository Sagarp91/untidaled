/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

/**
 * Used in place of java.awt.Point so that double values can be assigned. Will
 * be used if we decide to implement movement and turns.
 * @author Tommy
 * @version 1.0 10/7/2012
 */
public class Point {
	protected double x, y;
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	/**
	 * Calculates the distance between this point and the given point.
	 * @param pt The point to compare to.
	 * @return The distance between the points.
	 */
	public double distanceTo(Point pt){
		return Math.sqrt(Math.pow(x - pt.getX(), 2) + Math.pow(y - pt.getY(), 2));
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
