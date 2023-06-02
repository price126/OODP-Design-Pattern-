/*  $Id$
 *
 *  Revisions:
 *    $Log: LaneEvent.java,v $
 *    Revision 1.6  2003/02/16 22:59:34  ???
 *    added mechnanical problem flag
 *
 *    Revision 1.5  2003/02/02 23:55:31  ???
 *    Many many changes.
 *
 *    Revision 1.4  2003/02/02 22:44:26  ???
 *    More data.
 *
 *    Revision 1.3  2003/02/02 17:49:31  ???
 *    Modified.
 *
 *    Revision 1.2  2003/01/30 21:21:07  ???
 *    *** empty log message ***
 *
 *    Revision 1.1  2003/01/19 22:12:40  ???
 *    created laneevent and laneobserver
 *
 *
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class LaneEvent implements Serializable, LaneEventInterface{

	private final Party p;
	final int ball;
	final Bowler bowler;
	final int[][] cumulScore;
	final HashMap score;
	final int index;
	final int frameNum;
	final boolean mechProb;
//	int check;

	public LaneEvent(Map<Object,Object> params){
		CalculateScore calculatescore = (CalculateScore) params.get("calculateScore");
		p = calculatescore.party;
		index = (int) params.get("bowlIndex");
		bowler = (Bowler) params.get("currentThrower");
		cumulScore = calculatescore.cumulScores;
		score = calculatescore.scores;
		frameNum = (int) params.get("frameNumber");
		ball = (int) params.get("ball");
		mechProb = (boolean) params.get("gameIsHalted");
//		if (frameNum == 1 && ball == 0 && index == 0) {
//			check = 1;
//		}
//		else{
//			check = 0;
//		}
	}

	public Party getParty() {
		return p;
	}

	public Vector getPartyMembers() {
		return p.getMembers();
	}

}
 
