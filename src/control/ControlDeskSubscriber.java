package control;

public class ControlDeskSubscriber {

    /**
     * Allows objects to subscribe as observers
     *
     * @param adding the control.ControlDeskObserver that will be subscribed
     *
     */

    public static void subscribe(ControlDesk controlDesk, ControlDeskObserver adding) {
        controlDesk.subscribers.add(adding);
    }

    /**
     * Broadcast an event to subscribing objects.
     *
     * @param event the control.ControlDeskEvent to broadcast
     *
     */

    public static void publish(ControlDesk controlDesk, ControlDeskEvent event) {
        for (Object subscriber : controlDesk.subscribers) {
            ((ControlDeskObserver) subscriber).receiveControlDeskEvent(event);
        }
    }
}
