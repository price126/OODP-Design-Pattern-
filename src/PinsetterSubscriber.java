public class PinsetterSubscriber {
    /** subscribe()
     *
     * subscribe objects to send events to
     *
     * @pre none
     * @post the subscriber object will recieve events when their generated
     */
    public static void subscribe(Pinsetter pinsetter,PinsetterObserver subscriber) {
        pinsetter.subscribers.add(subscriber);
    }
}
