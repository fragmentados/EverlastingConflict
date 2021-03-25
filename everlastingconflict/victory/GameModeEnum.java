package everlastingconflict.victory;

public enum GameModeEnum {
    ANHILATION("Aniquilación", "Los jugadores son eliminados en el momento en el que pierden su edificio principal",
            new AnhilationVictoryCondition()),
    LEADER("Jugador Lider", "Cada equipo es eliminado en el momento en el que su lider es eliminado",
            new LeaderVictoryCondition()),
    JUGGERNAUT("Juggernaut", "Un jugador es elegido como juggernaut y el resto de jugadores van en su contra. El " +
            "juggernaut recibe ventajas en función de su raza y del número de jugadores enemigos:\n -Fenix: Obtienen " +
            "una ventaja de recursos iniciales\n -Eternium: Obtienen una ventaja de recursos y una serie de edificios " +
            "iniciales\n -Clark: Obtienen una reduccion de coste de las unidades basicas y un set de unidades basicas " +
            "adicional\n -Guardianes: Se activan automaticamente una serie de edificios y obtienen una ventaja de " +
            "recursos\n -Maestros: El manipulador ve mejorados sus stats iniciales",
            new AnhilationVictoryCondition());

    public String label;
    public String description;
    public VictoryCondition victoryCondition;

    GameModeEnum(String label, String description, VictoryCondition victoryCondition) {
        this.label = label;
        this.description = description;
        this.victoryCondition = victoryCondition;
    }

    public static GameModeEnum findByLabel(String label) {
        for (GameModeEnum value : values()) {
            if (value.label.equals(label)) {
                return value;
            }
        }
        return null;
    }
}
