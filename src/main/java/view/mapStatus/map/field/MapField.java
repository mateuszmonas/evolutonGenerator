package view.mapStatus.map.field;

import elements.MapElement;
import javafx.scene.control.Button;

import java.util.Set;

public class MapField extends Button implements Field {

    public MapField() {
        setWidth(50);
        setHeight(50);
    }

    @Override
    public void update(MapElement elementToDisplay, Set<MapElement> elements) {
        if (elementToDisplay != null) setText(elementToDisplay.toString());
        else setText("");
    }
}