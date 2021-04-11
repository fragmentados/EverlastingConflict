package everlastingconflict.races.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SubRaceEnum {
    OSO("Oso", RaceEnum.FENIX.getName(), "Facción centrada en la agresividad y el combate cuerpo a cuerpo", "media/Razas/Oso.png"),
    TORTUGA("Tortuga", RaceEnum.FENIX.getName(), "Facción centrada en la defensa y la protección", "media/Razas/Tortuga.png"),
    CUERVO("Cuervo", RaceEnum.FENIX.getName(), "Facción centrada en la tecnología y las habilidades", "media/Razas/Cuervo.png"),
    ERRADICARDORES("Erradicadores", RaceEnum.ETERNIUM.getName(), "Facción centrada en la ofensiva en puntos críticos del ciclo Eternium", "media/Razas/Erradicadores.png"),
    PROTECTORES("Protectores", RaceEnum.ETERNIUM.getName(), "Facción centrada en la defensa en los puntos vulnerables del ciclo Eternium", "media/Razas/Protectores.png"),
    ERUDITOS("Eruditos", RaceEnum.ETERNIUM.getName(), "Facción centrada en el desarrollo de las tecnologías y la recolección de recursos", "media/Razas/Eruditos.png"),
    DESPEDAZADORES("Despedazadores", RaceEnum.CLARK.getName(), "Facción centrada en el combate cuerpo a cuerpo y la recolección de materia", "media/Razas/Despedazadores.png"),
    REGURGITADORES("Regurgitadores", RaceEnum.CLARK.getName(), "Facción centrada en el combate a distancia y la fusión", "media/Razas/Regurgitadores.png"),
    RUMIANTES("Rumiantes", RaceEnum.CLARK.getName(), "Facción centrada en el desarrollo y en la domesticación", "media/Razas/Rumiantes.png"),
    EJERCITO("Ejercito", RaceEnum.GUARDIANES.getName(), "Facción centrada en los tanques y la ofensiva", "media/Razas/Ejercito.png"),
    IGLESIA("Iglesia", RaceEnum.GUARDIANES.getName(), "Facción centrada en la defensa y las unidades del vaticano", "media/Razas/Iglesia.png"),
    POLICIA("Policia", RaceEnum.GUARDIANES.getName(), "Facción centrada en los eventos y en la tecnologia", "media/Razas/Policia.png"),
    INVOCADOR("Invocador", RaceEnum.MAESTROS.getName(), "Facción centrada en las habilidades de invocación", "media/Razas/Invocador.png"),
    HECHICERO("Hechicero", RaceEnum.MAESTROS.getName(), "Facción centrada en las habilidades que infligen daño", "media/Razas/Hechicero.png"),
    LUCHADOR("Luchador", RaceEnum.MAESTROS.getName(), "Facción centrada en el combate directo con el manipulador", "media/Razas/Luchador.png"),
    ALMIRANTES("Almirantes", RaceEnum.ALIANZA.getName(), "Facción centrada en la ofensiva y en los vehículos", "media/Razas/Almirantes.png"),
    COLONIZADORES("Colonizadores", RaceEnum.ALIANZA.getName(), "Facción centrada en la defensa", "media/Razas/Colonizadores.png"),
    EXPLORADORES("Exploradores", RaceEnum.ALIANZA.getName(), "Facción centrada en el vuelo de la nave", "media/Razas/Exploradores.png");

    private String name;
    private String race;
    private String description;
    private String imagePath;

    SubRaceEnum(String name, String race, String description, String imagePath) {
        this.name = name;
        this.race = race;
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

    public static List<SubRaceEnum> findByRace(String race) {
        return Arrays.stream(values()).filter(sr -> sr.race.equals(race)).collect(Collectors.toList());
    }
}
