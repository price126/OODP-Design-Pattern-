package game;

import lane.Lane;
import lane.LaneSubscriber;
import user.Bowler;
import user.Party;

import java.io.Serializable;
import java.util.HashMap;

public class ScoreCalculator implements Serializable {
    public final HashMap scores;
    public Party party;
    public boolean partyAssigned;
    public int[] curScores;
    public int[][] cumulScores;
    public int[][] finalScores;

    public ScoreCalculator(){

        scores = new HashMap();
        partyAssigned = false;
    }
    public Party getParty() {
        return party;
    }
    
    public HashMap getScores() {
        return scores;
    }

    /** resetScores()
     *
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    public void resetScores(Party party) {

        for (Object obj : party.getMembers()) {
            int[] inputNum = new int[25];
            for (int i = 0; i != 25; i++) {
                inputNum[i] = -1;
            }
            scores.put(obj, inputNum);
        }
    }

    /** markScore()
     *
     * Method that marks a bowlers score on the board.
     *
     */
     public void markScore(Lane lane, int ball, int score){
        int[] curScore;
        int index =  ( lane.frameNumber * 2 + ball);
        curScore = (int[]) scores.get(lane.currentThrower);
        curScore[ index - 1] = score;
        scores.put(lane.currentThrower, curScore);
        getScore( lane.currentThrower, lane.frameNumber ,cumulScores,lane.bowlIndex,ball);
        LaneSubscriber.publish(lane,lane.lanePublish());
    }

    public boolean isStrikeCount(int[] curScore, int i, int current){
         return (i < current && i % 2 == 0 && curScore[i] == 10);
    }

    public boolean isSpareCount(int[] curScore, int i, int current){
        return (i % 2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1);
    }

    public void countScore18(int bowlIndex, int i, int[] curScore){
         if(i == 18){
            cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8] + curScore[i];
        }
        else if (i > 18) {
            cumulScores[bowlIndex][9] += curScore[i];
        }
    }

    public void strikeProcess(int i, int bowlIndex, int[] curScore){
        cumulScores[bowlIndex][i / 2] += 10;
        if (curScore[i + 1] != -1) {
            cumulScores[bowlIndex][i / 2] += curScore[i + 1] + curScore[i + 2] + cumulScores[bowlIndex][(i / 2) - 1];
        }
        else {
            cumulScores[bowlIndex][i / 2] += curScore[i + 2];
            if (i / 2 > 0) {
                cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][(i / 2) - 1];
            }

            if (curScore[i + 3] != -1) {
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
            } else {
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 4];
            }
        }
    }

    public void normalThrowProcess(int i, int bowlIndex, int[] curScore){
        if(i == 0){
            cumulScores[bowlIndex][i / 2] += curScore[i];
        }
        else if (i % 2 == 0) {
            cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1] + curScore[i];
        }
        else if (curScore[i] != -1) {
            cumulScores[bowlIndex][i / 2] += curScore[i];
        }
    }

    /** getScore()
     *
     * Method that calculates a bowlers score
     *
     * @param Cur        The bowler that is currently up
     * @param frame    The frame the current bowler is on
     *
     * @param cumulScores
     * @return			The bowlers total score
     */

    public void getScore(Bowler Cur, int frame, int[][] cumulScores, int bowlIndex, int ball) {
        int[] curScore;
        curScore = (int[]) scores.get(Cur);
        int current = 2*frame + ball - 1;

        for (int i = 0; i != 10; i++) {
            cumulScores[bowlIndex][i] = 0;
        }

        for (int i = 0; i != current + 2; i++) {
            //Spare:
            boolean isSpare = isSpareCount(curScore,i,current);
            boolean isStrike = isStrikeCount(curScore,i,current);

            if (isSpare) {
                cumulScores[bowlIndex][(i / 2)] += curScore[i + 1] + curScore[i];
            }

            else if(i >= 18){
                countScore18(bowlIndex,i,curScore);
            }


            else if (isStrike) {
                if (curScore[i + 2] != -1) {
                    if (curScore[i + 3] != -1 || curScore[i + 4] != -1) {
                        strikeProcess(i,bowlIndex,curScore);
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }

            else {
                //We're dealing with a normal throw, add it and be on our way.
                normalThrowProcess(i,bowlIndex,curScore);
            }
        }
    }
}
