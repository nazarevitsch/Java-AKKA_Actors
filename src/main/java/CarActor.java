import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public class CarActor extends AbstractActor {

    private ActorRef[] parkPlaces;
    private ActorRef parkPlace;

    public CarActor(ActorRef[] parkPlaces) {
        System.out.println("New car has just arrived " + getSelf());
        this.parkPlaces = parkPlaces;
        for (int i = 0; i < parkPlaces.length; i++) {
            if (parkPlace == null) {
                parkPlaces[i].tell(Command.DOES_PLACE_FREE, getSelf());
            }
        }
    }

    public static Props props(ActorRef[] parkPlaces) {
        return Props.create(CarActor.class, (Object) parkPlaces);
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
            System.out.println(getSelf() + " take place: " + parkPlace);
            waitingAndGoAway();
        } else {
            getSender().tell(Command.I_HAVE_ALREADY_PLACE, getSelf());
        }
    }

    private void waitingAndGoAway() {
        getContext().system().scheduler().scheduleOnce(Duration.ofMillis(4000),
                parkPlace, Command.I_GO_AWAY, getContext().dispatcher(), getSelf());
    }

}
