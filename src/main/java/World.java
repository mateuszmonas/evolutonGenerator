import map.Simulation;

public class World {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        for (int i = 0; i < 100; i++) {
            simulation.simulate();
        }
    }
}
