package everlastingconflict.races.enums;

import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Manipulador;
import everlastingconflict.elements.impl.Taller;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Alianza;
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
            "/Maestros.png", new Color(83, 20, 90)),
    ALIANZA("Alianza", "La alianza estelar son humanos desarrollados en otro planeta, TitanPrime, más avanzados " +
            "tecnológicamente pero con menor población. Por esta razón tienen menos unidades pero son más poderosas " +
            "que las de los Guardianes o los Fénix.", "media/Razas/Alianza.png", new Color(193, 199, 98));

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

    public void initElements(Jugador ally) {
        switch (this) {
            case FENIX:
                ally.edificios.add(new Edificio(ally, "Sede", ally.x_inicial, ally.y_inicial));
                ally.unidades.add(new Unidad(ally, "Constructor", ally.x_inicial,
                        ally.verticalOffset(ally.y_inicial, 150)));
                ally.unidades.add(new Unidad(ally, "Recolector", ally.horizontalOffset(ally.x_inicial, 50),
                        ally.verticalOffset(ally.y_inicial, 150)));
                ally.unidades.add(new Unidad(ally, "Halcón", ally.x_inicial,
                        ally.verticalOffset(ally.y_inicial, 200)));
                ally.unidades.add(new Unidad(ally, "Fénix", ally.horizontalOffset(ally.x_inicial, 50),
                        ally.verticalOffset(ally.y_inicial, 200)));
                break;
            case ETERNIUM:
                ally.edificios.add(new Edificio(ally, "Mando Central", ally.x_inicial, ally.y_inicial));
                ally.unidades.add(new Unidad(ally, "Adepto", ally.horizontalOffset(ally.x_inicial, 50),
                        ally.verticalOffset(ally.y_inicial, 150)));
                break;
            case CLARK:
                ally.edificios.add(new Edificio(ally, "Primarca", ally.x_inicial, ally.y_inicial));
                ally.unidades.add(new Unidad(ally, "Depredador", ally.horizontalOffset(ally.x_inicial, -50),
                        ally.verticalOffset(ally.y_inicial, 150)));
                ally.unidades.add(new Unidad(ally, "Devorador", ally.horizontalOffset(ally.x_inicial, 10),
                        ally.verticalOffset(ally.y_inicial, 150)));
                ally.unidades.add(new Unidad(ally, "Cazador", ally.horizontalOffset(ally.x_inicial, 70),
                        ally.verticalOffset(ally.y_inicial, 150)));
                break;
            case MAESTROS:
                ally.unidades.add(new Manipulador(ally, ally.x_inicial, ally.y_inicial));
                break;
            case GUARDIANES:
                ally.edificios.add(new Edificio(ally, "Ayuntamiento", ally.x_inicial, ally.y_inicial));
                ally.edificios.add(new Taller(ally, "Taller bélico", ally.horizontalOffset(ally.x_inicial, 300),
                        ally.verticalOffset(ally.y_inicial, 250)));
                ally.edificios.add(new Edificio(ally, "Academia de pilotos",
                        ally.horizontalOffset(ally.x_inicial, 200), ally.y_inicial));
                ally.edificios.add(new Taller(ally, "Taller bélico", ally.horizontalOffset(ally.x_inicial, 300),
                        ally.verticalOffset(ally.y_inicial, 400)));
                ally.edificios.add(new Edificio(ally, "Templo", ally.horizontalOffset(ally.x_inicial, 400),
                        ally.y_inicial));
                ally.edificios.add(new Edificio(ally, "Laboratorio de I+D",
                        ally.horizontalOffset(ally.x_inicial, 500), ally.y_inicial));
                ally.edificios.add(new Edificio(ally, "Vaticano", ally.horizontalOffset(ally.x_inicial, 600),
                        ally.y_inicial));
                ally.edificios.add(new Edificio(ally, "Edificio gubernamental",
                        ally.horizontalOffset(ally.x_inicial, 700), ally.y_inicial));
                break;
            case ALIANZA:
                ally.edificios.add(new Edificio(ally, "Nave", ally.x_inicial, ally.y_inicial));
                Alianza.initElements();
                break;
        }
    }
}
