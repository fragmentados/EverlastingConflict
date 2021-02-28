package everlastingconflict.ventanas;

import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.razas.RaceNameEnum;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerSelection {
    public ComboBox isActiveCombo;
    public ComboBox raceCombo;
    public ComboBox teamCombo;
    public float x, y;

    public PlayerSelection(float x, float y) {
        this.x = x;
        this.y = y;
        isActiveCombo = new ComboBox(null,  Arrays.asList("AI", "Closed"), x, y);
        raceCombo = new ComboBox("Raza:", RaceNameEnum.getAllNames(), x + 200, y);
        teamCombo = new ComboBox("Equipo:", IntStream.rangeClosed(1, 4)
                .boxed().map(n -> n.toString()).collect(Collectors.toList()), x + 400, y);
    }

    public boolean isPlayerActive() {
        return !"Closed".equals(isActiveCombo.opcion_seleccionada);
    }

    public void update(Input input) {
        isActiveCombo.checkIfItsClicked(input);
        raceCombo.checkIfItsClicked(input);
        teamCombo.checkIfItsClicked(input);
        isActiveCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
        raceCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
        teamCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
    }

    public void render(Graphics g) {
        isActiveCombo.render(g);
        if (isPlayerActive()) {
            raceCombo.render(g);
            teamCombo.render(g);
        }
    }
}
