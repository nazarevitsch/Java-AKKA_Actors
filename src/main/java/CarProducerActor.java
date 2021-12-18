import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class CarProducerActor extends Thread {

    private ActorSystem system;
    private ActorRef[] parkPlaces;

    public CarProducerActor(ActorRef[] parkPlaces, ActorSystem system){
        this.parkPlaces = parkPlaces;
        this.system = system;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            try {
                i++;
                Thread.sleep((int) (Math.random() * 4000) + 3000);
                system.actorOf(CarActor.props(parkPlaces, "CAR-" + i), "car_" + i);
            } catch (InterruptedException e){}
        }
    }
}
