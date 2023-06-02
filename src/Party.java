/*
 * Party.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: Party.java,v $
 *   Revision 1.3  2003/02/09 21:21:31  ???
 *   Added lots of comments
 *
 *   Revision 1.2  2003/01/12 22:23:32  ???
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/01/12 19:09:12  ???
 *   Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 *  Container that holds bowlers
 *
 */

import java.io.Serializable;
import java.util.*;

public class Party implements Serializable {

	/** Vector of bowlers in this party */	
    private Vector myBowlers;
	
	/**
	 * Constructor for a Party
	 * 
	 * @param bowlers	Vector of bowlers that are in this party
	 */
		
    public Party( Vector bowlers ) {
		myBowlers = new Vector(bowlers);
    }

	/**
	 * Accessor for members in this party
	 * 
	 * @return 	A vector of the bowlers in this party
	 */

    public Vector getMembers() {
		return myBowlers;
    }

	/** resetBowlerIterator()
	 *
	 * sets the current bower iterator back to the first bowler
	 *
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	public void resetBowlerIterator(Lane lane) {
		lane.bowlerIterator = myBowlers.iterator();
	}

	/** assignParty()
	 *
	 * assigns a party to this lane
	 *
	 * @pre none
	 * @post the party has been assigned to the lane
	 *
	 *
	 */
	public void assignParty( Lane curLane ) {
		curLane.calculateScore.party = this;
		resetBowlerIterator(curLane);
		curLane.calculateScore.partyAssigned = true;

		curLane.calculateScore.curScores = new int[myBowlers.size()];
		curLane.calculateScore.cumulScores = new int[myBowlers.size()][10];
		curLane.calculateScore.finalScores = new int[myBowlers.size()][128]; //Hardcoding a max of 128 games, bite me.
		curLane.gameNumber = 0;

		curLane.calculateScore.resetScores(this);
		curLane.gameFinished = false;
		curLane.frameNumber = 0;
	}

	public void assignPausedParty( Lane curLane , Vector resumeLane) {
		curLane.setter.rnd = (Random) resumeLane.get(1);
		curLane.setter.pins  = (boolean[]) resumeLane.get(2);
		curLane.setter.foul = (boolean) resumeLane.get(3);
		curLane.setter.throwNumber = (int) resumeLane.get(4);

//		curLane.calculateScore.curScores = new int[((CalculateScore)((resumeLane).get(5))).party.myBowlers.size()];
//		curLane.calculateScore.finalScores = new int[((CalculateScore)((resumeLane).get(5))).party.myBowlers.size()][128];
//		curLane.calculateScore.cumulScores = new int[((CalculateScore)((resumeLane).get(5))).party.myBowlers.size()][10];

		curLane.calculateScore = (CalculateScore) resumeLane.get(5);
		resetBowlerIterator(curLane);

		curLane.gameIsHalted = false;

		curLane.gameFinished = (boolean) resumeLane.get(7);
		curLane.ball = (int) resumeLane.get(8);
//		curLane.bowlIndex = (int) resumeLane.get(9);
		curLane.bowlIndex = 0;
		curLane.frameNumber = (int) resumeLane.get(10);
		curLane.tenthFrameStrike = (boolean) resumeLane.get(11);
		curLane.canThrowAgain = (boolean) resumeLane.get(12);
		curLane.gameNumber = (int) resumeLane.get(13);
		curLane.currentThrower = (Bowler) resumeLane.get(14);
		System.out.println(curLane.frameNumber + " " + curLane.bowlIndex + " " + curLane.ball);
	}



}
