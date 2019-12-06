package elements;

import map.Vector2d;

public class Grass implements IMapElement {

    private final int NUTRITION_VALUE = 4;
    private Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getNutritionValue() {
        return NUTRITION_VALUE;
    }

    @Override
    public String toString() {
        return "*";
    }
}