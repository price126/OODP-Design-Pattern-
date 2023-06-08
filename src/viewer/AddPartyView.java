package viewer;/* viewer.AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: viewer.AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added control.ControlDeskEvent and control.ControlDeskObserver. Updated user.Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import user.BowlerFile;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	private final int maxSize;

	private final JFrame win;
	private final JButton addPatron,newPatron,remPatron,finished;
	public final JList partyList,allBowlers;
	public final Vector party;
	public Vector bowlerdb;
	private final ControlDeskView controlDesk;
	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		win = CustomView.createWindow("Add Party");

		JPanel colPanel = CustomView.createGridLayoutPanel(1,3);

		// Party Panel
		JPanel partyPanel = CustomView.createFlowLayoutPanel();
		partyPanel.setBorder(new TitledBorder("Your Party"));

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = new JPanel();
		bowlerPanel.setLayout(new FlowLayout());
		bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = CustomView.createGridLayoutPanel(4,1);

		addPatron = CustomView.createButtonInPanel("Add to Party",buttonPanel);
		addPatron.addActionListener(this);
		remPatron = CustomView.createButtonInPanel("Remove Member",buttonPanel);
		remPatron.addActionListener(this);
		newPatron = CustomView.createButtonInPanel("New Patron",buttonPanel);
		newPatron.addActionListener(this);
		finished = CustomView.createButtonInPanel("Finished",buttonPanel);
		finished.addActionListener(this);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

		CustomView.addContentsOnWindow(win,colPanel);
		// Center Window on Screen
		CustomView.setWindowCentered(win);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron)) {
			checkPatronJoined();
		}

		if (e.getSource().equals(remPatron)) {
			if (selectedMember != null) {
				party.removeElement(selectedMember);
				partyList.setListData(party);
			}
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView( this );
		}
		if (e.getSource().equals(finished)) {
			if ( party != null && party.size() > 0) {
				controlDesk.updateAddParty( this );
			}
			win.setVisible(false);
		}
	}

	public void checkPatronJoined(){
		if (selectedNick != null && party.size() < maxSize) {
			if (party.contains(selectedNick)) {
				System.err.println("Member already in Party");
			} else {
				party.add(selectedNick);
				partyList.setListData(party);
			}
		}
	}

/**
 * Handler for List actions
 * @param e the ListActionEvent that triggered the handler
 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

}
