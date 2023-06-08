package viewer;

import user.Bowler;

import javax.swing.*;
import java.awt.*;

public class BallAndScore {

    public static void BallLabel(int i, LaneView lv){
        int val = 23;
        for (int j = 0; j != val; j++) {
            lv.ballLabel[i][j] = new JLabel(" ");
            lv.balls[i][j] = new JPanel();
            lv.balls[i][j].setBorder(
                    BorderFactory.createLineBorder(Color.BLACK));
            lv.balls[i][j].add(lv.ballLabel[i][j]);
        }
    }

    public static void BallGrid(int i, LaneView lv){
        int val = 9,val2=18;
        for (int j = 0; j != val; j++) {
            lv.ballGrid[i][j] = CustomView. createGridLayoutPanel(0,3);
            lv.ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
            lv.ballGrid[i][j].add(lv.balls[i][2 * j], BorderLayout.EAST);
            lv.ballGrid[i][j].add(lv.balls[i][2 * j + 1], BorderLayout.EAST);
        }

        lv.ballGrid[i][val] = CustomView. createGridLayoutPanel(0,3);
        lv.ballGrid[i][val].add(lv.balls[i][val2]);
        lv.ballGrid[i][val].add(lv.balls[i][val2+1]);
        lv.ballGrid[i][val].add(lv.balls[i][val2+2]);
    }

    public static void setPinScore(int i,LaneView lv){
        lv.pins[i] = new JPanel();
        lv.pins[i].setBorder(
                BorderFactory.createTitledBorder(
                        ((Bowler) lv.bowlers.get(i)).getNick()));
        lv.pins[i].setLayout(new GridLayout(0, 10));

        for (int k = 0; k != 10; k++) {
            lv.scores[i][k] = new JPanel();
            lv.scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
            lv.scores[i][k].setBorder(
                    BorderFactory.createLineBorder(Color.BLACK));
            lv.scores[i][k].setLayout(new GridLayout(0, 1));
            lv.scores[i][k].add(lv.ballGrid[i][k], BorderLayout.EAST);
            lv.scores[i][k].add(lv.scoreLabel[i][k], BorderLayout.SOUTH);
            lv.pins[i].add(lv.scores[i][k], BorderLayout.EAST);
        }
    }
}
