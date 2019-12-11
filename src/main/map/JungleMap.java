package map;

import elements.MapElement;
import data.Rectangle;
import data.Vector;

import java.util.*;

public class JungleMap implements WorldMap {

    protected Map<Vector, Set<MapElement>> elements = new HashMap<>();
    private Rectangle area;

    public JungleMap(Rectangle area) {
        this.area = area;
    }

    public void addElement(MapElement element) throws IllegalArgumentException {
        if (!area.contains(element.getPosition())) {
            throw new IllegalArgumentException(element.getPosition().toString() + " is out of map bounds");
        }
        if (!elements.containsKey(element.getPosition())) {
            elements.put(element.getPosition(), new HashSet<>());
        }
        elements.get(element.getPosition()).add(element);
    }

    @Override
    public void onPositionChange(MapElement element, Vector oldPosition) {
        if (!elements.containsKey(oldPosition)) {
            throw new IllegalArgumentException("no element at position " + oldPosition.toString());
        }

        oldPosition = area.normalisePosition(oldPosition);
        elements.get(oldPosition).remove(element);
        if (elements.get(oldPosition).isEmpty()) {
            elements.remove(oldPosition);
        }
        addElement(element);
    }

    public Set<MapElement> objectsAt(Vector position) {
        position = area.normalisePosition(position);
        return elements.getOrDefault(position, null);
    }

    public void removeElement(MapElement element) {
        Vector position = area.normalisePosition(element.getPosition());
        if (!elements.containsKey(position)) {
            throw new IllegalArgumentException("No animal at position " + position.toString());
        }
        elements.get(position).remove(element);
        if (elements.get(position).isEmpty())
            elements.remove(position);

    }

    @Override
    public void onRemoval(MapElement element) {
        removeElement(element);
    }
}
