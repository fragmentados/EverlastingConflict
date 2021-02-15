package everlastingconflict.mapas;

import java.util.ArrayList;
import java.util.List;

public enum MapEnum {
    SMALL("Small(2000 x 2000)", 2000, 2000),
    MEDIUM("Medium(4000 x 4000)", 4000, 4000),
    LARGE("Large(6000 x 6000)", 6000, 6000);

    String name;
    int width;
    int height;

    MapEnum(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static List<String> getAllNames() {
        List<String> allNames = new ArrayList<>();
        for (MapEnum value : values()) {
            allNames.add(value.getName());
        }
        return allNames;
    }

    public static MapEnum findByName(String name) {
        for (MapEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
