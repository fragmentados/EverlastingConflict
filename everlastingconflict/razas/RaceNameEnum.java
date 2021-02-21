package everlastingconflict.razas;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum RaceNameEnum {
    FENIX("Fénix"),
    ETERNIUM("Eternium"),
    CLARK("Clark"),
    GUARDIANES("Guardianes"),
    MAESTROS("Maestros");

    private String name;

    private static final Map<String, RaceNameEnum> raceEnumMap = Collections.unmodifiableMap(initializeMapping());

    public static List<String> getAllNames() {
        List<String> allNames = raceEnumMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
        return sortRaceNames(allNames);
    }

    private static Map<String, RaceNameEnum> initializeMapping() {
        Map<String, RaceNameEnum> raceEnumMap = new HashMap<>();
        for (RaceNameEnum s : RaceNameEnum.values()) {
            raceEnumMap.put(s.name, s);
        }
        return raceEnumMap;
    }

    RaceNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> sortRaceNames(List<String> raceNames) {
        return raceNames.stream()
                .map(r -> raceEnumMap.get(r)).sorted().map(r -> r.getName()).collect(Collectors.toList());
    }
}
