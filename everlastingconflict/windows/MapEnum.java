package everlastingconflict.windows;

import java.util.ArrayList;
import java.util.List;

public enum MapEnum {
    SMALL("Small", 4000f, 4000f, 2),
    MEDIUM("Medium", 6000f, 6000f, 4),
    LARGE("Large", 8000f, 8000f, 6);

    String name;
    float width;
    float height;
    int maxPlayers;

    MapEnum(String name, float width, float height, int maxPlayers) {
        this.width = width;
        this.height = height;
        this.name = name + "(" + (int) this.width + " x " + (int) this.height + ")";
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }


    public static List<String> getAllNames() {
        List<String> allNames = new ArrayList<>();
        for (MapEnum value : values()) {
            allNames.add(value.getName());
        }
        return allNames;
    }

    public static List<String> getAllPlayerLimits() {
        List<String> allNames = new ArrayList<>();
        for (MapEnum value : values()) {
            allNames.add("Jugadores máximos : " + value.getMaxPlayers());
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
