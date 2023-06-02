import java.io.Serializable;

public class LaneSubscriber implements Serializable {


    /** subscribe
     *
     * Method that will add a subscriber
     *
     */

    public static void subscribe(Lane lane,LaneObserver adding ) {
        lane.subscribers.add( adding );
    }

    /** publish
     *
     * Method that publishes an event to subscribers
     *
     * @param event	Event that is to be published
     */

    public static  void publish( Lane lane,LaneEvent event ) {
        if( lane.subscribers.size() > 0 ) {
            for (Object subscriber : lane.subscribers) {
                ((LaneObserver) subscriber).receiveLaneEvent(event);
            }
        }
    }
}
