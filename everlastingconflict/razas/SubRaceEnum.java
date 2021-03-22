package everlastingconflict.razas;

import java.util.Arrays;

public enum SubRaceEnum {
    OSO("Oso", "Facción centrada en la agresividad y el combate cuerpo a cuerpo", "media/Razas/Oso.png"),
    TORTUGA("Tortuga", "Facción centrada en la defensa y la protección", "media/Razas/Tortuga.png"),
    CUERVO("Cuervo", "Facción centrada en la tecnología y las habilidades", "media/Razas/Cuervo.png");

    private String name;
    private String description;
    private String imagePath;

    SubRaceEnum(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public static SubRaceEnum findByName(String name) {
        return Arrays.stream(values()).filter(sr -> sr.name.equals(name)).findFirst().orElse(null);
    }
}
