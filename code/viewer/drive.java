package viewer;

import lane.Alley;
import control.ControlDesk;
import viewer.ControlDeskView;

public class drive {

    public static void main(String[] args) {

        int numLanes = 3;
        int maxPatronsPerParty = 5;

        Alley defaultLanes = new Alley(numLanes);
        ControlDesk controlDesk = defaultLanes.getControlDesk();

        ControlDeskView cdv = new ControlDeskView(controlDesk, maxPatronsPerParty);
        controlDesk.subscribe(cdv);

    }
}
