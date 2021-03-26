package everlastingconflict.windows;

import everlastingconflict.campaign.challenges.Challenge;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.elementosvisuales.ComboBoxOption;
import everlastingconflict.races.enums.RaceEnum;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WindowMenuChallenges extends WindowMenuBasic {

    public BotonSimple volver, aceptar, salir;
    private ComboBox raceCombo;

    @Override
    public void init(GameContainer container) throws SlickException {
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(43), WindowCombat.responsiveY(50));
        aceptar = new BotonSimple("Aceptar", WindowCombat.responsiveX(47), WindowCombat.responsiveY(50));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - 60, 0);
        raceCombo = new ComboBox("Raza:",
                Arrays.stream(RaceEnum.values()).map(r -> new ComboBoxOption(r.getName(), r.getDescription(),
                        r.getImagePath(), r.getColor())).collect(Collectors.toList()),
                WindowCombat.responsiveX(45), WindowCombat.responsiveY(40));
    }

    public void start(GameContainer container) {
        try {
            WindowMain.windowSwitch(container,
                    Challenge.createChallengeByRace(RaceEnum.findByName(raceCombo.optionSelected.text)), "Combat");
            WindowCombat.optionsMenuButtons.add(0, new BotonSimple("Reiniciar") {
                @Override
                public void effect() throws SlickException {
                    start(container);
                }
            });
        } catch (SlickException ex) {
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                start(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, null, "Menu");
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            }
            raceCombo.checkIfItsClicked(input);
            if (raceCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        volver.render(g);
        aceptar.render(g);
        salir.render(g);
        raceCombo.render(g);
    }
}
