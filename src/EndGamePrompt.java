/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.View;

public class EndGamePrompt implements ActionListener {

	private final JFrame win;
	private final JButton yesButton;
	private final JButton noButton;

	private int result;

	private String selectedNick, selectedMember;

	public EndGamePrompt( String partyName ) {

		result =0;
		win = ViewComponents.MakeWindow("Another Game for " + partyName + "?" );

		JPanel colPanel = ViewComponents.GridLayoutPanel(2,1);

		// Label Panel
		JPanel labelPanel = ViewComponents.FlowLayoutPanel();
		JLabel message = new JLabel( "Party " + partyName
			+ " has finished bowling.\nWould they like to bowl another game?" );

		labelPanel.add( message );

		// Button
		JPanel buttonPanel  = ViewComponents.GridLayoutPanel(1,2);

		yesButton = ViewComponents.MakeButtons("Yes",buttonPanel);
		yesButton.addActionListener(this);
		noButton = ViewComponents.MakeButtons("No",buttonPanel);
		noButton.addActionListener(this);

		// Clean up main panel
		colPanel.add(labelPanel);
		colPanel.add(buttonPanel);

		ViewComponents.AddContentsToWindow(win,colPanel);

		// Center Window on Screen
		ViewComponents.SetWindowPosition(win);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(yesButton)) {		
			result=1;
		}
		else if (e.getSource().equals(noButton)) {
			result=2;
		}

	}

	public int getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return result;	
	}
	
	public void distroy() {
		win.setVisible(false);
	}
	
}

