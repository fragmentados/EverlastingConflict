package everlastingconflict.ventanas;

import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.elementosvisuales.ComboBoxOption;
import everlastingconflict.razas.RaceEnum;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WindowMenuChallenges extends WindowMenuBasic {

    public BotonSimple volver, aceptar, salir;
    private ComboBox raceTutorial;

    @Override
    public void init(GameContainer container) throws SlickException {
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(40), WindowCombat.responsiveY(85));
        aceptar = new BotonSimple("Aceptar", WindowCombat.responsiveX(45), WindowCombat.responsiveY(85));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - 60, 0);
        aceptar.canBeUsed = volver.canBeUsed = false;
        raceTutorial = new ComboBox("Raza:",
                Arrays.stream(RaceEnum.values()).map(r -> new ComboBoxOption(r.getName(), r.getDescription(),
                        r.getImagePath())).collect(Collectors.toList()),
                WindowCombat.responsiveX(45), WindowCombat.responsiveY(40));
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {

            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, null, "Menu");
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            }
            raceTutorial.checkIfItsClicked(input);
            if (raceTutorial.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        Input input  = container.getInput();
        raceTutorial.render(input, g);
        salir.render(g);
    }
}
