/**
 * 
 */
package navigation;

import com.jaxelson.ExtendedPoint2D;

/**
 * @author jason
 *
 */
public class GravPoint extends ExtendedPoint2D {
	private static final long serialVersionUID = -6763002593178923706L;
	
	double power;
	public GravPoint(double x, double y, double pPower) {
		super(x, y);
		power = pPower;
	}
}
