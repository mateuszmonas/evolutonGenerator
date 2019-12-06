package map;


import elements.Animal;
import elements.Grass;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    WorldMap map = new WorldMap(100, 30);

    @Test
    void testRemoveDeadAnimals() {

    }

    @Test
    void testPlaceAndRemoveAnimal() {
        Animal animal1 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(100, 30)).build();
        Animal animal2 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(-2, 0)).build();
        Animal animal3 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(0, 0)).build();
        Animal animal4 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(99, 29)).build();
        Animal animal5 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(99, 29)).build();
        assertThrows(IllegalArgumentException.class, () -> map.placeAnimal(animal1));
        assertThrows(IllegalArgumentException.class, () -> map.placeAnimal(animal2));
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        assertTrue(((ArrayList<Animal>) map.objectAt(new Vector2d(0, 0))).contains(animal3));
        assertTrue(map.animals.contains(animal3));
        assertTrue(((ArrayList<Animal>) map.objectAt(new Vector2d(99, 29))).contains(animal4));
        assertTrue(map.animals.contains(animal4));
        map.placeAnimal(animal5);
        assertTrue(((ArrayList<Animal>) map.objectAt(new Vector2d(99, 29))).contains(animal5));
        assertTrue(map.animals.contains(animal5));

        map.removeAnimal(animal3);
        assertNull(map.objectAt(new Vector2d(0, 0)));
        assertFalse(map.animals.contains(animal3));
        map.removeAnimal(animal4);
        assertFalse(((ArrayList<Animal>) map.objectAt(new Vector2d(99, 29))).contains(animal4));
        assertFalse(map.animals.contains(animal4));
        assertTrue(((ArrayList<Animal>) map.objectAt(new Vector2d(99, 29))).contains(animal5));
        assertTrue(map.animals.contains(animal5));

    }

    @Test
    void testCanMoveTo() {
    }

    @Test
    void testIsOccupied() {
        Animal animal1 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(0, 0)).build();
        map.placeAnimal(animal1);
        assertTrue(map.isOccupied(new Vector2d(0, 0)));
        assertFalse(map.isOccupied(new Vector2d(2, 0)));
    }

    @Test
    void testPositionChanged() {
    }

    @Test
    void testObjectAt() {
        assertNull(map.objectAt(new Vector2d(0, 0)));
        Animal animal1 = new Animal.AnimalBuilder().onMap(map).atPosition(new Vector2d(0, 0)).build();
        map.placeGrass(new Grass(new Vector2d(0, 0)));
        map.placeAnimal(animal1);
        assertTrue(map.objectAt(new Vector2d(0, 0)) instanceof ArrayList);
        }

    @Test
    void testPlaceAndRemoveGrass() {
        Grass grass1 = new Grass(new Vector2d(2, 9));
        Grass grass2 = new Grass(new Vector2d(2, 9));
        map.placeGrass(grass1);
        assertEquals(grass1, map.objectAt(new Vector2d(2, 9)));
        assertThrows(IllegalArgumentException.class, () -> map.placeGrass(grass2));
        map.removeGrass(grass1);
        assertNull(map.objectAt(new Vector2d(2, 9)));
    }

    @Test
    void testNormalisePosition() {
        WorldMap map = new WorldMap(10, 10);
        Vector2d position = new Vector2d(-1, 8);
        assertEquals(new Vector2d(9, 8), map.normalisePosition(position));
        position = new Vector2d(-11, 8);
        assertEquals(new Vector2d(9, 8), map.normalisePosition(position));
        position = new Vector2d(-10, 8);
        assertEquals(new Vector2d(0, 8), map.normalisePosition(position));

    }

    @Test
    void testRemoveAnimal() {
    }
}