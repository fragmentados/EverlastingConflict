package everlastingconflict.races.enums;

import org.newdawn.slick.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum RaceEnum {
    FENIX("Fénix", "Los fénix son una banda rebelde humana que reune seguidores en ciudades civiles para enfrentarse " +
            "a la oposición Eternium. Cuantos más seguidores consiguen mas soldados pueden reclutar y mejores " +
            "tecnologías pueden descubrir.", "media/Razas/Fenix.png", new Color(186, 64, 64)),
    ETERNIUM("Eternium", "Los eternium llegaron a la Tierra en busca de recursos que habían agotado en su planeta " +
            "natal, son una raza alienígena con grandes avances tecnológicos cuya única debilidad es el tiempo en sí " +
            "mismo.", "media/Razas/Eternium.png", new Color(15, 81, 99)),
    CLARK("Clark", "Los clark son una raza alienígena basada en la depredación y los instintos básicos. Obtienen " +
            "recursos devorando bestias locales y basan su ejército en la fusión de unidades básicas para obtener " +
            "unidades más poderosas.", "media/Razas/Clark.png", new Color(23, 92, 25)),
    GUARDIANES("Guardianes", "Los guardianes de la paz son los humanos que, en cuanto llegaron los eternium a la " +
            "tierra, obtaron por la colaboración pacífica con ellos donándoles los recursos que necesitaban a cambio " +
            "de sus avances tecnológicos. " +
            "Se basan en la aceptación de la población de forma similar a los Fénix pero deben impedir eventos " +
            "negativos que reducirán su aceptación", "media/Razas/Guardianes.png", new Color(174, 74, 74)),
    MAESTROS("Maestros", "Los maestros del universo son una raza de poderío absoluto con apenas una decena de " +
            "individuos restantes. " +
            "Por ello llegan a los planetas con un solo individuo pero a medida que esté va combatiendo y mejorando " +
            "obtiene poderosas habilidades y se vuelve más fuerte que la unidad de cualquier raza", "media/Razas" +
            "/Maestros.png", new Color(83, 20, 90));

    private String name;
    private String description;
    private String imagePath;
    private Color color;

    public static final Map<String, RaceEnum> raceEnumMap = Collections.unmodifiableMap(initializeMapping());

    private static Map<String, RaceEnum> initializeMapping() {
        Map<String, RaceEnum> raceEnumMap = new HashMap<>();
        for (RaceEnum s : RaceEnum.values()) {
            raceEnumMap.put(s.name, s);
        }
        return raceEnumMap;
    }

    RaceEnum(String name, String description, String imagePath, Color color) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public static List<String> sortRaceNames(List<String> raceNames) {
        return raceNames.stream()
                .map(r -> raceEnumMap.get(r)).sorted().map(r -> r.getName()).collect(Collectors.toList());
    }

    public static RaceEnum findByName(String name) {
        return raceEnumMap.get(name);
    }
}
