package viewer;

import control.ControlDesk;
import control.ControlDeskSubscriber;
import lane.Alley;

public class drive {

	public static void main(String[] args) {

		int numLanes = 3;
		int maxPatronsPerParty=5;

		Alley a = new Alley( numLanes );
		ControlDesk controlDesk = a.getControlDesk();

		ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty);
		ControlDeskSubscriber.subscribe(controlDesk,cdv);
	}
}
