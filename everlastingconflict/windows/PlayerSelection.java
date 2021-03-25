package everlastingconflict.windows;

import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.elementosvisuales.ComboBoxOption;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerSelection {
    public ComboBox isActiveCombo;
    public ComboBox raceCombo;
    public ComboBox subRaceCombo;
    public ComboBox teamCombo;
    public ComboBox leaderCombo;
    public ComboBox juggernautCombo;
    public ComboBox difficultyCombo;
    public boolean isMainPlayer;
    public float x, y;

    public PlayerSelection(float x, float y, boolean isMainPlayer) {
        this.x = x;
        this.y = y;
        this.isMainPlayer = isMainPlayer;
        if (!isMainPlayer) {
            isActiveCombo = new ComboBox(null,
                    Arrays.asList("AI", "Closed").stream().map(pt -> new ComboBoxOption(pt)).collect(Collectors.toList())
                    , x, y);
        }
        raceCombo = new ComboBox("Raza:",
                Arrays.stream(RaceEnum.values()).map(r -> new ComboBoxOption(r.getName(), r.getDescription(),
                        r.getImagePath())).collect(Collectors.toList()),
                x + 200, y);
        subRaceCombo = new ComboBox("SubRaza:",
                SubRaceEnum.findByRace(RaceEnum.FENIX.getName()).stream().map(r -> new ComboBoxOption(r.getName(),
                        r.getDescription(),
                        r.getImagePath())).collect(Collectors.toList()),
                x + 450, y);
        teamCombo = new ComboBox("Equipo:", IntStream.rangeClosed(1, 4)
                .boxed().map(n -> new ComboBoxOption(n.toString())).collect(Collectors.toList()), x + 700, y);
        difficultyCombo = new ComboBox("Dificultad:",
                Arrays.asList("Fácil", "Normal", "Difícil").stream().map(pt -> new ComboBoxOption(pt)).collect(Collectors.toList()), x + 850, y);
        leaderCombo = new ComboBox("Lider:",
                Arrays.asList("No", "Sí").stream().map(pt -> new ComboBoxOption(pt)).collect(Collectors.toList()),
                x + 1100, y);
        juggernautCombo = new ComboBox("Juggernaut:",
                Arrays.asList("No", "Sí").stream().map(pt -> new ComboBoxOption(pt)).collect(Collectors.toList()),
                x + 1100, y);
    }

    public boolean isLeader() {
        return "Sí".equals(this.leaderCombo.optionSelected.text);
    }

    public boolean isJuggernaut() {
        return "Sí".equals(this.juggernautCombo.optionSelected.text);
    }

    public boolean isPlayerActive() {
        return isActiveCombo == null || !"Closed".equals(isActiveCombo.optionSelected.text);
    }

    public void update(Input input, List<PlayerSelection> playerSelections) throws SlickException {
        if (isActiveCombo != null) {
            isActiveCombo.checkIfItsClicked(input);
            isActiveCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
        }
        raceCombo.checkIfItsClicked(input);
        subRaceCombo.checkIfItsClicked(input);
        teamCombo.checkIfItsClicked(input);
        leaderCombo.checkIfItsClicked(input);
        juggernautCombo.checkIfItsClicked(input);
        difficultyCombo.checkIfItsClicked(input);
        if (raceCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
            subRaceCombo.options =
                    SubRaceEnum.findByRace(raceCombo.optionSelected.text).stream().map(r -> new ComboBoxOption(r.getName(), r.getDescription(),
                            r.getImagePath())).collect(Collectors.toList());
            subRaceCombo.optionSelected = subRaceCombo.options.get(0);
            subRaceCombo.initProportionsBasedOnOptions();
        }
        subRaceCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
        teamCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
        if (leaderCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
            if (isLeader()) {
                playerSelections.stream().filter(ps -> !ps.equals(this) && ps.teamCombo.optionSelected.text.equals(this.teamCombo.optionSelected.text))
                        .forEach(ps -> ps.leaderCombo.optionSelected = new ComboBoxOption("No"));
            }
        }
        if (juggernautCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
            if (isJuggernaut()) {
                playerSelections.stream().filter(ps -> !ps.equals(this))
                        .forEach(ps -> ps.juggernautCombo.optionSelected = new ComboBoxOption("No"));
            }
        }
        difficultyCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
    }

    public void render(Input input, Graphics g) {
        if (isActiveCombo != null) {
            isActiveCombo.render(input, g);
        }
        if (isPlayerActive()) {
            raceCombo.render(input, g);
            subRaceCombo.render(input, g);
            if (!WindowMenuPlayerSelection.isJuggernautGame()) {
                teamCombo.render(input, g);
            } else {
                juggernautCombo.render(input, g);
            }
            if (WindowMenuPlayerSelection.isLeaderGame()) {
                leaderCombo.render(input, g);
            }
            difficultyCombo.render(input, g);
        }
    }

    public void renderHoveredDescription(Input input, Graphics g) {
        raceCombo.renderHoveredDescription(input, g);
        subRaceCombo.renderHoveredDescription(input, g);
    }
}
