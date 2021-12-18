import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.LinkedList;

public class CarPlaceActor extends AbstractActor {

    private LinkedList<ActorRef> waitingCars;
    private ActorRef parkedCar;
    private String name;

    public CarPlaceActor(String name){
        this.name = name;
        this.waitingCars = new LinkedList<>();
    }

    public static Props props(String name) {
        return Props.create(CarPlaceActor.class, name);
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
                    System.out.println("Is Free: " + name);
                    notifyAnotherCars();
                })
                .build();
    }

    public void checkPlaces() {
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
        for (int i = 0; i < waitingCars.size(); i++) {
            if (waitingCars.getFirst().isTerminated()) {
                waitingCars.removeFirst();
            } else {
                parkedCar = waitingCars.getFirst();
                waitingCars.getFirst().tell(Command.YOU_CAN_TAKE_PLACE, getSelf());
                waitingCars.removeFirst();
                return;
            }
        }
    }

}
