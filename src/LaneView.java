/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class LaneView implements Serializable, LaneObserver, ActionListener {

	private boolean initDone;

	final JFrame frame;
	final Container cpanel;
	Vector bowlers;

	JPanel[][] balls,scores,ballGrid;
	JLabel[][] ballLabel,scoreLabel;
	JPanel[] pins;

	JButton maintenance, pause, resume, save;
	final Lane lane;

	public LaneView(Lane lane, int laneNum) {
		this.lane = lane;
		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
			}
		});
		cpanel.add(new JPanel());
	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}

	private JPanel makeFrame(Party party) {
		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		int a=23,b=10;
		balls = new JPanel[numBowlers][a];
		ballLabel = new JLabel[numBowlers][a];
		scores = new JPanel[numBowlers][b];
		scoreLabel = new JLabel[numBowlers][b];

		ballGrid = new JPanel[numBowlers][b];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			BallGridView.BallLabel(i,this);
			BallGridView.BallGrid(i,this);
			BallGridView.setpinscore(i,this);
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void receiveLaneEvent(LaneEvent le) {
		if (!lane.calculateScore.partyAssigned) {
			return;
		}

		int numBowlers = le.getPartyMembers().size();

		while (!initDone) {
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

//		if (le.check == 1) {
			System.out.println("Making the frame.");
			cpanel.removeAll();
			cpanel.add(makeFrame(le.getParty()), "Center");

			// Button Panel
			JPanel buttonPanel = ViewComponents.FlowLayoutPanel();
			maintenance = ViewComponents.MakeButtons("Maintenance Call",buttonPanel);
			maintenance.addActionListener(this);
			pause = ViewComponents.MakeButtons("Pause",buttonPanel);
			pause.addActionListener(this);
			resume = ViewComponents.MakeButtons("Resume",buttonPanel);
			resume.addActionListener(this);
			save = ViewComponents.MakeButtons("Save Game",buttonPanel);
			save.addActionListener(this);

			cpanel.add(buttonPanel, "South");
			frame.pack();
//		}

		int[][] lescores = le.cumulScore;
		showScore(le,numBowlers,lescores);
	}

	public void showScore(LaneEvent le,int numBowlers,int[][] lescores){
		for (int k = 0; k < numBowlers; k++) {
			setScoreLabel(le,lescores,k);
			setBallLabel(le,k);
		}
	}

	public void setScoreLabel(LaneEvent le,int[][] lescores,int k){
		for (int i = 0; i <= le.frameNum - 1; i++) {
			if (lescores[k][i] != 0)
					scoreLabel[k][i].setText((Integer.valueOf(lescores[k][i])).toString());
		}
	}

	public void setBallLabel(LaneEvent le,int k){
		for (int i = 0; i < 21; i++) {
			int val = ((int[]) le.score.get(bowlers.get(k)))[i];
			if(val == -1){
				continue;
			}

			int val1 = 0;
			if(i > 0) {
				val1 = ((int[]) le.score.get(bowlers.get(k)))[i - 1];
			}

			String st = checkString(le,val,val1,i,k);
			ballLabel[k][i].setText(st);
		}
	}

	public String checkString(LaneEvent le, int val, int val1, int i, int k){
		String st = "";
		if (val == 10 && (i % 2 == 0 || i == 19))
			st = "X";
		else if (val + val1 == 10 && i % 2 == 1)
			st = "/";
		else if (val == -2 )
			st = "F";
		else
			st = (Integer.valueOf(((int[]) le.score.get(bowlers.get(k)))[i])).toString();
		return st;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
		else if (e.getSource().equals(pause)) {
			lane.pauseGame();
		}
		else if (e.getSource().equals(resume)) {
			lane.unPauseGame();
		}
		else if (e.getSource().equals(save)) {
			lane.pauseGame();
			try {
				PausedLanesFile.addPausedLane(lane);
			} catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

}
