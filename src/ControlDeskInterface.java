import java.util.HashSet;

public interface ControlDeskInterface {
    void run();
//    void assignLane();
//    void addPartyQueue(Vector partyNicks);
//    Vector getPartyQueue();
    int getNumLanes();
//    void subscribe(ControlDeskObserver adding);
    HashSet getLanes();
}


