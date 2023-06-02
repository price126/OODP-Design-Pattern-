import javax.swing.*;
import java.awt.*;

public class BallGridView {
    public static void BallGrid(int i, LaneView laneview){
        int val = 9,val2=18;
        for (int j = 0; j != val; j++) {
            laneview.ballGrid[i][j] = ViewComponents.GridLayoutPanel(0,3);
            laneview.ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
            laneview.ballGrid[i][j].add(laneview.balls[i][2 * j], BorderLayout.EAST);
            laneview.ballGrid[i][j].add(laneview.balls[i][2 * j + 1], BorderLayout.EAST);
        }

        laneview.ballGrid[i][val] = ViewComponents.GridLayoutPanel(0,3);
        laneview.ballGrid[i][val].add(laneview.balls[i][val2]);
        laneview.ballGrid[i][val].add(laneview.balls[i][val2+1]);
        laneview.ballGrid[i][val].add(laneview.balls[i][val2+2]);
    }

    public static void BallLabel(int i, LaneView laneview){
        int val = 23;
        for (int j = 0; j != val; j++) {
            laneview.ballLabel[i][j] = new JLabel(" ");
            laneview.balls[i][j] = new JPanel();
            laneview.balls[i][j].setBorder(
                    BorderFactory.createLineBorder(Color.BLACK));
            laneview.balls[i][j].add(laneview.ballLabel[i][j]);
        }
    }

    public static void setpinscore(int i,LaneView laneview){
        laneview.pins[i] = new JPanel();
        laneview.pins[i].setBorder(
                BorderFactory.createTitledBorder(
                        ((Bowler) laneview.bowlers.get(i)).getNick()));
        laneview.pins[i].setLayout(new GridLayout(0, 10));

        for (int k = 0; k != 10; k++) {
            laneview.scores[i][k] = new JPanel();
            laneview.scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
            laneview.scores[i][k].setBorder(
                    BorderFactory.createLineBorder(Color.BLACK));
            laneview.scores[i][k].setLayout(new GridLayout(0, 1));
            laneview.scores[i][k].add(laneview.ballGrid[i][k], BorderLayout.EAST);
            laneview.scores[i][k].add(laneview.scoreLabel[i][k], BorderLayout.SOUTH);
            laneview.pins[i].add(laneview.scores[i][k], BorderLayout.EAST);
        }
    }
}
