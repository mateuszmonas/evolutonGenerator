package elements;

import map.MapDirection;
import map.MoveDirection;
import map.Vector2d;
import map.WorldMap;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Animal implements IMapElement{
    private final int MIN_REPRODUCTION_ENERGY = 5;
    private int energy;
    private MapDirection direction;
    private Vector2d position;
    private WorldMap map;
    private int[] genome = new int[32];

    public Animal() {
    }

    public boolean canReproduce() {
        return energy >= MIN_REPRODUCTION_ENERGY;
    }

    public void reproduce(Animal parentB) {
        if (this.canReproduce() && parentB.canReproduce()) {
            energy = (int) Math.floor(0.75 * energy);
            parentB.energy = (int) Math.floor(0.75 * parentB.energy);
        }
        energy = (int) Math.floor(0.75 * energy);
    }

    public int[] getGenome() {
        return genome;
    }

    public int getEnergy() {
        return energy;
    }

    public void reduceEnergy(int energy) {
        this.energy -= energy;
    }

    public void increaseEnergy(int energy) {
        this.energy += energy;
    }

    private void eatGrass(Grass grass) {
        map.removeGrass(grass);
        increaseEnergy(grass.getNutritionValue());
    }

    public void move(MoveDirection direction) {
        switch (direction) {
            case TURN:
                this.direction = MapDirection.values()[(this.direction.ordinal() + genome[ThreadLocalRandom.current().nextInt(genome.length)]) % MapDirection.values().length];
                break;
            case MOVE:
                Vector2d newPosition = position.add(this.direction.toUnitVector());
                if (map.canMoveTo(newPosition)) {
                    Object objectAtNewPosition = map.objectAt(newPosition);
                    if (objectAtNewPosition instanceof Grass) eatGrass((Grass) objectAtNewPosition);
                    position = newPosition;
                } else {
                    Object objectAtNewPosition = map.objectAt(newPosition);
                    if (objectAtNewPosition instanceof Animal) reproduce((Animal) objectAtNewPosition);
                }
                break;
        }
    }

    public Vector2d getPosition() {
        return position;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public static class AnimalBuilder {

        private AnimalBuilder() {

        }

        public static MapAnimalBuilder newAnimalBuilder(){
            return new AnimalBuilderSteps();
        }

        public interface MapAnimalBuilder{
            AnimalBuilderSteps onMap(WorldMap map);
        }

        public static class AnimalBuilderSteps implements MapAnimalBuilder{
            WorldMap map;
            Vector2d position = new Vector2d(0, 0);
            int energy = 20;
            MapDirection direction = MapDirection.NORTH;
            int[] genome;

            public static MapAnimalBuilder animalBuilder(){
                return new AnimalBuilderSteps();
            }

            public AnimalBuilderSteps onMap(WorldMap map) {
                this.map = map;
                return this;
            }

            public AnimalBuilderSteps atPosition(Vector2d position) {
                this.position = position;
                return this;
            }

            public AnimalBuilderSteps facingDirection(MapDirection direction) {
                this.direction = direction;
                return this;
            }

            public AnimalBuilderSteps fromParents(Animal parent1, Animal parent2) {
                genome = generateGenome(parent1.genome, parent2.genome);
                return this;
            }

            public AnimalBuilderSteps withEnergy(int energy) {
                this.energy = energy;
                return this;
            }

            private int[] generateGenome(int[] genomeA, int[] genomeB) {
                int[] genome = new int[32];
                int div1 = ThreadLocalRandom.current().nextInt(1, 30);
                int div2 = ThreadLocalRandom.current().nextInt(div1, 31);
                System.arraycopy(genomeA, 0, genome, 0, div1 + 1);
                System.arraycopy(genomeB, div1 + 1, genome, div1 + 1, div2 - div1);
                System.arraycopy(genomeA, div2 + 1, genome, div2 + 1, genome.length - div2 - 1);
                Arrays.sort(genome);
                for (int i = 0; i < 7; i++) {
                    final int a = i;
                    if (Arrays.stream(genome).noneMatch(o -> o == a)) {
                        genome[i] = a;
                    }
                }
                Arrays.sort(genome);
                return genome;
            }

            private int[] generateGenome() {
                int[] genome = new int[32];
                for (int i = 0; i < 8; i++) {
                    genome[i] = i;
                }
                for (int i = 8; i < genome.length; i++) {
                    genome[i] = ThreadLocalRandom.current().nextInt(8);
                }
                Arrays.sort(genome);
                return genome;
            }

            public Animal build() {
                Animal animal = new Animal();
                animal.energy = energy;
                animal.position = position;
                animal.direction = direction;
                animal.map = map;
                if (genome == null) {
                    genome = generateGenome();
                }
                animal.genome = genome;
                return animal;
            }
        }
    }
}
