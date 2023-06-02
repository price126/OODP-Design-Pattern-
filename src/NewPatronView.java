/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a patron
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.text.View;

public class NewPatronView implements ActionListener {

	private final JFrame win;
	private final JButton abort,finished;
	private final JTextField nickField,fullField,emailField;
	private String nick, full, email;
	private boolean done;
	private final AddPartyView addParty;

	public NewPatronView(AddPartyView v) {

		addParty=v;	
		done = false;

		win = ViewComponents.MakeWindow("Add Patron");

		JPanel colPanel = ViewComponents.MakeMainPanel();

		// Patron Panel
		JPanel patronPanel = ViewComponents.MakePanel(3,1,"Your Info");

		// Controls Panel
		nickField = ViewComponents.MakeField("Nick Name",patronPanel);
		fullField = ViewComponents.MakeField("Full Name",patronPanel);
		emailField = ViewComponents.MakeField("E-Mail",patronPanel);

		// Button Panel
		JPanel buttonPanel = ViewComponents.MakePanel(4,1,"Buttons");
		Insets buttonMargin = new Insets(4, 4, 4, 4);

		finished = ViewComponents.MakeButtons("Add Patron",buttonPanel);
		finished.addActionListener(this);
		abort = ViewComponents.MakeButtons("Abort",buttonPanel);
		abort.addActionListener(this);

		// Clean up main panel
		colPanel.add(patronPanel, "Center");
		colPanel.add(buttonPanel, "East");

		ViewComponents.AddContentsToWindow(win,colPanel);
		// Center Window on Screen
		ViewComponents.SetWindowPosition(win);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abort)) {
			done = true;
			win.setVisible(false);
		}

		if (e.getSource().equals(finished)) {
			nick = nickField.getText();
			full = fullField.getText();
			email = emailField.getText();
			done = true;
//			addParty.updateNewPatron( this );
			updateNewPatron(this,addParty);
			win.setVisible(false);
		}

	}

	/**
	 * Called by NewPatronView to notify AddPartyView to update
	 *
	 * @param newPatron the NewPatronView that called this method
	 */

	public void updateNewPatron(NewPatronView newPatron,AddPartyView addParty) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
						newPatron.getNick(),
						newPatron.getFull(),
						newPatron.getEmail());
				addParty.bowlerdb = new Vector(BowlerFile.getBowlers());
				addParty.allBowlers.setListData(addParty.bowlerdb);
				addParty.party.add(newPatron.getNick());
				addParty.partyList.setListData(addParty.party);
			} else {
				System.err.println( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}


	public boolean done() {
		return done;
	}

	public String getNick() {
		return nick;
	}

	public String getFull() {
		return full;
	}

	public String getEmail() {
		return email;
	}

}
