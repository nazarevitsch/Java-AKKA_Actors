import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

public class CarActor extends AbstractActor {

    private ActorRef[] parkPlaces;
    private ActorRef parkPlace;
    private String name;

    public CarActor(ActorRef[] parkPlaces, String name) {
        System.out.println("New car has just arrived " + name);
        this.name = name;
        this.parkPlaces = parkPlaces;
        for (int i = 0; i < parkPlaces.length; i++) {
            if (parkPlace == null) {
                parkPlaces[i].tell(Command.DOES_PLACE_FREE, getSelf());
            }
        }
    }

    public static Props props(ActorRef[] parkPlaces, String name) {
        return Props.create(CarActor.class, (Object) parkPlaces, name);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Command.YOU_CAN_TAKE_PLACE, r -> {
                    takePlace();
                })
                .build();
    }

    private void takePlace() {
        if (parkPlace == null) {
            parkPlace = getSender();
            System.out.println(name + " take place: " + parkPlace);
            waitingAndGoAway();
        } else {
            getSender().tell(Command.I_HAVE_ALREADY_PLACE, getSelf());
        }
    }

    private void waitingAndGoAway() {
        getContext().system().scheduler().scheduleOnce(Duration.ofMillis((int) (Math.random() * 3000) + 3000),
                parkPlace, Command.I_GO_AWAY, getContext().dispatcher(), getSelf());
    }

}
