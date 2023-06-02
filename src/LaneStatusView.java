/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;

public class LaneStatusView implements Serializable, ActionListener, LaneObserver, PinsetterObserver {

	public final JPanel jp;

	private final JLabel curBowler,pinsDown;
	private final JButton viewLane,viewPinSetter,maintenance;

	private final PinSetterView psv;
	private final LaneView lv;
	private final Lane lane;
	final int laneNum;

	boolean laneShowing,psShowing;

	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing=false;
		psShowing=false;

		psv = new PinSetterView( laneNum );
		PinsetterSubscriber.subscribe(lane.setter,psv);

		lv = new LaneView( lane, laneNum );
		LaneSubscriber.subscribe(lane,lv);

		jp = ViewComponents.FlowLayoutPanel();
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		viewLane = ViewComponents.MakeButtons("View Lane",buttonPanel);
		viewLane.addActionListener(this);
		viewPinSetter = ViewComponents.MakeButtons("Pinsetter",buttonPanel);
		viewPinSetter.addActionListener(this);

		maintenance = ViewComponents.MakeButtons(" ",buttonPanel);
        maintenance.addActionListener(this);
		maintenance.setBackground(Color.GREEN);


		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );

		jp.add( cLabel );
		jp.add( curBowler );
		jp.add( pdLabel );
		jp.add( pinsDown );
		jp.add(buttonPanel);

	}

	public void actionPerformed( ActionEvent e ) {
		if (lane.calculateScore.partyAssigned) {
			if (e.getSource().equals(viewPinSetter)) {
				if (psShowing) {
					psv.hide();
					psShowing = false;
				} else {
					psv.show();
					psShowing = true;
				}
			}

			if (e.getSource().equals(viewLane)) {
				if (laneShowing) {
					lv.hide();
					laneShowing = false;
				}
				else {
					lv.show();
					laneShowing = true;
				}
			}
		}

		if (e.getSource().equals(maintenance) && lane.calculateScore.partyAssigned) {
				lane.unPauseGame();
				maintenance.setBackground( Color.GREEN );
		}
	}

	public void receiveLaneEvent(LaneEvent le) {
		curBowler.setText( le.bowler.getNickName() );
		if ( le.mechProb ) {
			maintenance.setBackground( Color.RED );
		}
		if (lane.calculateScore.partyAssigned) {
			viewLane.setEnabled(true);
			viewPinSetter.setEnabled(true);
		} else {
			viewLane.setEnabled(false);
			viewPinSetter.setEnabled(false);
		}
	}

	public void receivePinsetterEvent(PinsetterEvent pe) {
		pinsDown.setText( ( new Integer(pe.totalPinsDown()) ).toString() );
	}
}
