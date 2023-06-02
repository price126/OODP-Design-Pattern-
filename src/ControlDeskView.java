/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 *
 *  Revisions:
 * 		$Log$
 *
 */

/**
 * Class for representation of the control desk
 *
 */

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.IOException;
import java.util.*;

public class ControlDeskView implements ActionListener, ControlDeskObserver{

	private final JButton addParty,finished,assign,query,resume;
	private final JFrame win;
	private final JList partyList;
	private final int maxMembers;
	private final ControlDesk controlDesk;

	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */

	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {
		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.getNumLanes();

		win = ViewComponents.MakeWindow("Control Desk");

		JPanel colPanel = ViewComponents.MakeMainPanel();

		// Controls Panel
		JPanel controlsPanel = ViewComponents.MakePanel(5,1,"Controls");

		//Buttons
		addParty = ViewComponents.MakeButtons("Add Party",controlsPanel);
		addParty.addActionListener(this);
		assign = ViewComponents.MakeButtons("Assign Lanes",controlsPanel);
		assign.addActionListener(this);
		query = ViewComponents.MakeButtons("Records",controlsPanel);
		query.addActionListener(this);
		resume = ViewComponents.MakeButtons("Resume games",controlsPanel);
		resume.addActionListener(this);
		finished = ViewComponents.MakeButtons("Finished",controlsPanel);
		finished.addActionListener(this);
		// Lane Status Panel
		JPanel laneStatusPanel = ViewComponents.MakePanel(numLanes,1,"Lane Status");

		HashSet lanes=controlDesk.getLanes();
		Iterator it = lanes.iterator();
		int laneCount=0;

		while (it.hasNext()) {
			Lane curLane = (Lane) it.next();
			laneCount += 1;
			LaneStatusView laneStat = new LaneStatusView(curLane,laneCount);
			LaneSubscriber.subscribe(curLane,laneStat);

			PinsetterSubscriber.subscribe(curLane.setter,laneStat);
			JPanel lanePanel = laneStat.jp;
			lanePanel.setBorder(new TitledBorder("Lane " + laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		// Party Queue Panel
		JPanel partyPanel = ViewComponents.FlowLayoutPanel();
		partyPanel.setBorder(new TitledBorder("Party Queue"));

		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");

		ViewComponents.AddContentsToWindow(win,colPanel);

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		ViewComponents.SetWindowPosition(win);
	}

	/**
	 * Handler for actionEvents
	 *
	 * @param e	the ActionEvent that triggered the handler
	 *
	 */

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addParty)) {
			AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
		}
		else if (e.getSource().equals(assign)) {
			PartyQueue.assignLane(controlDesk);
		}
		else if (e.getSource().equals(finished)) {
			win.setVisible(false);
			System.exit(0);
		}
		else if (e.getSource().equals(query)) {
			QueryView queryview = new QueryView();
		}
		else if (e.getSource().equals(resume)) {
			try {
				ResumeView resumeView = new ResumeView(controlDesk);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
//			try {
//
//				Vector<Boolean> returnedFlags = PausedLanesFile.readPausedLanesFlags();
//				Vector<Party> returnedParties = PausedLanesFile.readPausedLanesParties();
//				Vector<HashMap> returnedScores = PausedLanesFile.readPausedLanesScores();
//
//
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			} catch (ClassNotFoundException ex) {
//				ex.printStackTrace();
//			}
//			Iterator it = returnedParty.iterator();
//			while (it.hasNext()) {
//				Party obj = (Party) it.next();
//				for (Object o: obj.getMembers())
//				{
//					Bowler a = (Bowler) o;
//					System.out.print(a.getNick()+" ");
//				}
//				System.out.println();
		}
	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		PartyQueue.addPartyQueue(controlDesk,addPartyView.party);
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(ce.getPartyQueue());
	}
}
