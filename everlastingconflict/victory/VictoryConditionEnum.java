package everlastingconflict.victory;

import java.util.ArrayList;
import java.util.List;

public enum VictoryConditionEnum {
    ANHILATION("Aniquilación", "Los jugadores son eliminados en el momento en el que pierden su edificio principal", new AnhilationVictoryCondition()),
    LEADER("Jugador Lider", "Cada equipo es eliminado en el momento en el que su lider es eliminado", new LeaderVictoryCondition());

    public String label;
    public String description;
    public VictoryCondition condition;

    VictoryConditionEnum(String label, String description, VictoryCondition condition) {
        this.label = label;
        this.description = description;
        this.condition = condition;
    }

    public static List<String> getAllNames() {
        List<String> allNames = new ArrayList<>();
        for (VictoryConditionEnum value : values()) {
            allNames.add(value.label);
        }
        return allNames;
    }

    public static List<String> getAllDescriptions() {
        List<String> allDescriptions = new ArrayList<>();
        for (VictoryConditionEnum value : values()) {
            allDescriptions.add(value.description);
        }
        return allDescriptions;
    }

    public static VictoryConditionEnum findByLabel(String label) {
        for (VictoryConditionEnum value : values()) {
            if (value.label.equals(label)) {
                return value;
            }
        }
        return null;
    }
}
