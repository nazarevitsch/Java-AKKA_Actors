import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;

public class CarPlaceActor extends AbstractActor {

    private List<ActorRef> waitingCars;
    private ActorRef parkedCar;

    public CarPlaceActor(){
        this.waitingCars = new ArrayList<>();
    }

    public static Props props() {
        return Props.create(CarPlaceActor.class);
    }

    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Command.DOES_PLACE_FREE, r -> {
                    checkPlaces();
                })
                .matchEquals(Command.I_HAVE_ALREADY_PLACE, r -> {
                    cleanParkPlace();
                    notifyAnotherCars();
                })
                .matchEquals(Command.I_GO_AWAY, r -> {
                    cleanParkPlace();
                    System.out.println("Is Free: " + getSelf());
                    notifyAnotherCars();
                })
                .build();
    }

    public void checkPlaces() {
//        System.out.println(getSelf().path() + " was asked for place by " + getSender().path());
        if (parkedCar == null) {
            parkedCar = getSender();
            getSender().tell(Command.YOU_CAN_TAKE_PLACE, getSelf());
        } else {
            waitingCars.add(getSender());
        }
    }

    public void cleanParkPlace() {
        parkedCar = null;
    }

    public void notifyAnotherCars() {
        if (waitingCars.size() > 0) {
            parkedCar = waitingCars.get(0);
            waitingCars.get(0).tell(Command.YOU_CAN_TAKE_PLACE, getSelf());
        }
    }
}
