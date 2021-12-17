import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test-system");

        ActorRef[] carPlaces = new ActorRef[3];
        carPlaces[0] = system.actorOf(CarPlaceActor.props(), "car_place_1");
        carPlaces[1] = system.actorOf(CarPlaceActor.props(), "car_place_2");
        carPlaces[2] = system.actorOf(CarPlaceActor.props(), "car_place_3");

        CarProducerActor carProducerActor = new CarProducerActor(carPlaces, system);
        carProducerActor.start();
    }
}
