package user;
/* user.Queue.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
 
import java.util.Vector;
 
public class Queue {
	private final Vector v;
	
	/** user.Queue()
	 * 
	 * creates a new queue
	 */
	public Queue() {
		v = new Vector();
	}
	
	public Object poll() {
		return v.remove(0);
	}

	public void add(Object o) {
		v.addElement(o);
	}
	
	public boolean isEmpty() {
		return v.size() != 0;
	}

	public Vector getVector() {
		return v;
	}
	
}
