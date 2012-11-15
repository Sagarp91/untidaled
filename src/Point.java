/**
* Name:    MisMatch
* Section: 1
* Program: Term Project ["UnTidaled"]
* Date: 10/9/2012
*
*/

/**
 * Used in place of java.util.Point for brevity and consistency (java's version
 * returns double values for its getters).
 * @author Tommy
 * @version 1.1 11/14/2012
 */
public class Point {
	public int x, y;
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public String toString(){
		return x + "," + y;
	}
}
