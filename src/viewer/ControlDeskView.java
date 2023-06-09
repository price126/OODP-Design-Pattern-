package viewer;/* viewer.ControlDeskView.java
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

import control.ControlDesk;
import control.ControlDeskEvent;
import control.ControlDeskObserver;
import game.PinsetterSubscriber;
import lane.Lane;
import lane.LaneSubscriber;
import user.PartyQueue;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

// import java.io.IOException;
import java.util.*;

public class ControlDeskView implements ActionListener, ControlDeskObserver {

	private final JButton addParty,finished,query; //,resume;
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

		win = CustomView.createWindow("Control Desk");

		JPanel colPanel = CustomView.createMainPanel();

		// Controls Panel
		JPanel controlsPanel = CustomView.createTitledBorderPanel(5,1,"Controls");

		//Buttons
		addParty = CustomView.createButtonInPanel("Add Party",controlsPanel);
		addParty.addActionListener(this);
		query = CustomView.createButtonInPanel("History",controlsPanel);
		query.addActionListener(this);
		finished = CustomView.createButtonInPanel("Finished",controlsPanel);
		finished.addActionListener(this);

		// Lane Status Panel
		JPanel laneStatusPanel = CustomView.createTitledBorderPanel(numLanes,1,"Lane Status");

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

		// Party user.Queue Panel
		JPanel partyPanel = CustomView.createFlowLayoutPanel();
		partyPanel.setBorder(new TitledBorder("Party user.Queue"));

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

		CustomView.addContentsOnWindow(win,colPanel);

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		CustomView.setWindowCentered(win);
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
		else if (e.getSource().equals(query)) {
			QueryView queryview = new QueryView();
		}
		else if (e.getSource().equals(finished)) {
			win.setVisible(false);
			System.exit(0);
		}
	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the viewer.AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		PartyQueue.addPartyQueue(controlDesk,addPartyView.party);
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the control.ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(ce.getPartyQueue());
	}
}
