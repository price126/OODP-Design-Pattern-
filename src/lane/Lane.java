package lane;
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   game.Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   game.Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import game.*;
import user.Bowler;
import viewer.EndGamePrompt;
import viewer.EndGameReport;

import java.io.Serializable;
import java.util.*;

public class Lane extends Thread implements Serializable, PinsetterObserver,LaneInterface{
	public final Pinsetter setter;
	public final Vector subscribers;
	public boolean gameIsHalted;
	public boolean gameFinished;
	public Iterator bowlerIterator;
	public int ball;
	public int bowlIndex;
	public int frameNumber;
	public boolean tenthFrameStrike;
	public boolean canThrowAgain;
	public int gameNumber;
	public Bowler currentThrower;			// = the thrower who just took a throw
	public ScoreCalculator scoreCalculator;

	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thread is executing
	 */
	public Lane() { 
		setter = new Pinsetter();
		subscribers = new Vector();

		scoreCalculator = new ScoreCalculator();

		gameIsHalted = false;
		gameNumber = 0;

		PinsetterSubscriber.subscribe(setter,this);
		this.start();
	}

	/** run()
	 * 
	 * entry point for execution of this lane 
	 */
	public void run() {
		while (true) {
			if (scoreCalculator.partyAssigned && !gameFinished) {	// we have a party on this lane,
								// so next bower can take a throw
				checkGameIsHalted();

				if (bowlerIterator.hasNext()) {
					currentThrower = (Bowler)bowlerIterator.next();
					canThrowAgain = true;
					tenthFrameStrike = false;
					ball = 0;

					simulateHitting();

					checkFrameNum();


					setter.reset();
					bowlIndex++;
				}

				else {
					frameNumber++;
					scoreCalculator.party.resetBowlerIterator(this);
					bowlIndex = 0;
					if (frameNumber > 9) {
						gameFinished = true;
						gameNumber++;
					}
				}
			}

			else if(scoreCalculator.partyAssigned){
				String partyName = ((Bowler) scoreCalculator.party.getMembers().get(0)).getNickName() + "'s Party";
				EndGamePrompt egp = new EndGamePrompt(partyName);
				int result = egp.getResult();
				egp.destroy();

				System.out.println("result was: " + result);

				// TODO: send record of scores to control desk
				if (result == 1) {					// yes, want to play again
					scoreCalculator.resetScores(scoreCalculator.party);
					gameFinished = false;
					frameNumber = 0;
					scoreCalculator.party.resetBowlerIterator(this);
				}
				else if (result == 2) {// no, dont want to play another game
					Vector printVector;
					EndGameReport egr = new EndGameReport( partyName, scoreCalculator.party);
					printVector = egr.getResult();
					scoreCalculator.partyAssigned = false;
					Iterator scoreIt = scoreCalculator.party.getMembers().iterator();
					scoreCalculator.party = null;
					scoreCalculator.partyAssigned = false;
					LaneSubscriber.publish(this,lanePublish());
					sendScore(scoreIt,printVector);
				}
			}

			sleep();
		}
	}

	public void checkFrameNum(){
		if (frameNumber == 9){
			scoreCalculator.finalScores[bowlIndex][gameNumber] = scoreCalculator.cumulScores[bowlIndex][9];
			addCurrentDate();
		}

	}

	public void sleep(){
		try {
			sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendScore(Iterator scoreIt,Vector printVector){
		int myIndex = 0;
		while (scoreIt.hasNext()){
			Bowler thisBowler = (Bowler)scoreIt.next();
			ScoreReport sr = new ScoreReport( thisBowler, scoreCalculator.finalScores[myIndex++], gameNumber );

			for (Object o : printVector) {
				if (thisBowler.getNick() == o) {
					System.out.println("Printing " + thisBowler.getNick());
					sr.sendPrintout();
				}
			}
		}
	}

	public void simulateHitting(){
		while (canThrowAgain) {
			setter.ballThrown();		// simulate the thrower's ball hiting
			ball++;
		}
	}

	public void checkGameIsHalted(){
		while (gameIsHalted) {
			try {
				sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addCurrentDate(){
		try{
			Date date = new Date();
			String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
			ScoreHistoryFile.addScore(currentThrower.getNick(), dateString, Integer.toString(scoreCalculator.cumulScores[bowlIndex][9]));
		} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
	}
	
	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desired
	 * 
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {
		
			if (pe.pinsDownOnThisThrow() >=  0) {			// this is a real throw
				scoreCalculator.markScore(this,pe.getThrowNumber(),pe.pinsDownOnThisThrow());

				// next logic handles the ?: what conditions dont allow them another throw?
				// handle the case of 10th frame first
				if (frameNumber == 9) {
					if (pe.totalPinsDown() == 10) {
						setter.resetPins();
						if(pe.getThrowNumber() == 1) {
							tenthFrameStrike = true;
						}
					}

					checkcanThrowAgain(pe);

				} else { // its not the 10th frame
			
					if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
						canThrowAgain = false;
					} else if (pe.getThrowNumber() == 2) {
						canThrowAgain = false;
					} else if (pe.getThrowNumber() == 3)
						System.out.println("I'm here...");
				}
			}  //  this is not a real throw, probably a reset
	}

	public void checkcanThrowAgain(PinsetterEvent pe){
		if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && !tenthFrameStrike)) {
			canThrowAgain = false;
		}
		if (pe.getThrowNumber() == 3) {
			canThrowAgain = false;
		}
	}

	/** lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 * 
	 * @return		The new lane event
	 */
	public LaneEvent lanePublish() {
		Map<Object,Object> params = new HashMap<>();

		params.put("calculateScore", scoreCalculator);
		params.put("bowlIndex",bowlIndex);
		params.put("currentThrower",currentThrower) ;
		params.put("frameNumber",frameNumber+1);
		params.put("ball",ball);
		params.put("gameIsHalted",gameIsHalted);

		return new LaneEvent(params);
	}
	
	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		LaneSubscriber.publish(this,lanePublish());
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		LaneSubscriber.publish(this,lanePublish());
	}
}
