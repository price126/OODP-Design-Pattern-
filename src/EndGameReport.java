/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

public class EndGameReport implements ActionListener, ListSelectionListener {

	private final JFrame win;
	private final JButton printButton;
	private final JButton finished;
	private Vector myVector;
	private final Vector retVal;

	private int result;

	private String selectedMember;

	public EndGameReport( String partyName, Party party ) {
	
		result =0;
		retVal = new Vector();

		win = ViewComponents.MakeWindow("End Game Report for " + partyName + "?" );

		JPanel colPanel = ViewComponents.GridLayoutPanel(1,2);

		// Member Panel
		JPanel partyPanel = ViewComponents.FlowLayoutPanel();
		partyPanel.setBorder(new TitledBorder("Party Members"));
		
		Vector myVector = new Vector();
		for (Object o : party.getMembers()) {
			myVector.add(((Bowler) o).getNick());
		}

		JList memberList = new JList(myVector);
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(memberList);
		partyPanel.add(partyPane);
		partyPanel.add(memberList);

		// Button Panel
		JPanel buttonPanel = ViewComponents.GridLayoutPanel(2,1);

		printButton = ViewComponents.MakeButtons("PrintReport",buttonPanel);
		printButton.addActionListener(this);
		finished = ViewComponents.MakeButtons("Finished",buttonPanel);
		finished.addActionListener(this);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(buttonPanel);

		ViewComponents.AddContentsToWindow(win,colPanel);

		// Center Window on Screen
		ViewComponents.SetWindowPosition(win);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(printButton)) {		
			//Add selected to the vector.
			retVal.add(selectedMember);
		}
		else if (e.getSource().equals(finished)) {
			win.setVisible(false);
			result = 1;
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		selectedMember =
			((String) ((JList) e.getSource()).getSelectedValue());
	}

	public Vector getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return retVal;	
	}

	public static void main(String[] args) {
		Vector bowlers = new Vector();
		for ( int i=0; i<4; i++ ) {
			bowlers.add( new Bowler( "aaaaa", "aaaaa", "aaaaa" ) );
		}
		Party party = new Party( bowlers );
		String partyName="wank";
		EndGameReport e = new EndGameReport( partyName, party );
	}
	
}

