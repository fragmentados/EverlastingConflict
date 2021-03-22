package everlastingconflict.ventanas;

import java.util.ArrayList;
import java.util.List;

public enum MapEnum {
    SMALL("Small(2000 x 2000)", 2000, 2000, 2),
    MEDIUM("Medium(4000 x 4000)", 4000, 4000, 4),
    LARGE("Large(6000 x 6000)", 6000, 6000, 6);

    String name;
    int width;
    int height;
    int maxPlayers;

    MapEnum(String name, int width, int height, int maxPlayers) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.maxPlayers = maxPlayers;
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
