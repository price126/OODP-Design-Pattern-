package control;
/* control.ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: control.ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		control.ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added control.ControlDeskEvent and control.ControlDeskObserver. Updated user.Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of control.ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the viewer.ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/**
 * Class that represents control desk
 *
 */

import lane.Lane;
import user.PartyQueue;
import user.Queue;

import java.util.HashSet;
import java.util.Vector;


public class ControlDesk extends Thread implements ControlDeskInterface{

	/** The collection of Lanes */
	public final HashSet lanes;

	/** The party wait queue */
	public final Queue partyQueue;

	/** The resume queue */
	public Queue stopPartyQueue;

	/** The number of lanes represented */
	private final int numLanes;
	
	/** The collection of subscribers */
	public final Vector subscribers;

    /**
     * Constructor for the control.ControlDesk class
     *
     * @param numLanes the number of lanes to be represented
     *
     */

	public ControlDesk(int numLanes) {
		this.numLanes = numLanes;
		lanes = new HashSet(numLanes);
		partyQueue = new Queue();
		stopPartyQueue = new Queue();

		subscribers = new Vector();

		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane());
		}
		
		this.start();
	}
	
	/**
	 * Main loop for control.ControlDesk's thread
	 * 
	 */
	public void run() {
		while (true) {
			
			PartyQueue.assignLane(this);
			
			try {
				sleep(250);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * Accessor for the number of lanes represented by the control.ControlDesk
     * 
     * @return an int containing the number of lanes represented
     *
     */

	public int getNumLanes() {
		return numLanes;
	}

    /**
     * Accessor method for lanes
     * 
     * @return a HashSet of Lanes
     *
     */

	public HashSet getLanes() {
		return lanes;
	}
}
