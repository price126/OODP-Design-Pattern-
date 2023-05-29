package user;/* user.Queue.java
 *
 *  Version
 *  $Id$
 *
 *  Revisions:
 * 		$Log$
 *
 */

import java.util.Vector;

public class Queue<E> {
    private final Vector<E> v;

    /**
     * user.Queue()
     * <p>
     * creates a new queue
     */
    public Queue() {
        v = new Vector<>();
    }

    public E next() {
        return v.remove(0);
    }

    public void add(E o) {
        v.addElement(o);
    }

    public boolean hasMoreElements() {
        return v.size() != 0;
    }

    public Vector<E> asVector() {
        return v;
    }

}
