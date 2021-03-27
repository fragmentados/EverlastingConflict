package everlastingconflict.windows;

import everlastingconflict.campaign.tutorial.GuidedGame;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.elementosvisuales.ComboBoxOption;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.enums.RaceEnum;
import org.newdawn.slick.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WindowMenuTutorial extends WindowMenuBasic {

    public BotonSimple volver, aceptar, salir;
    private ComboBox raceCombo;
    private Image raceImage;

    @Override
    public void init(GameContainer container) throws SlickException {
        super.init(container);
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(40), WindowCombat.responsiveY(85));
        aceptar = new BotonSimple("Aceptar", WindowCombat.responsiveX(45), WindowCombat.responsiveY(85));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - "Salir".length() * 10, 0);
        raceCombo = new ComboBox("Raza:",
                Arrays.stream(RaceEnum.values()).map(r -> new ComboBoxOption(r.getName(), r.getDescription(),
                        r.getImagePath(), r.getColor())).collect(Collectors.toList()),
                WindowCombat.responsiveX(45), WindowCombat.responsiveY(40));
        raceImage = new Image("media/" + raceCombo.optionSelected.text + ".png");
        WindowMenuBasic.subTitle = "TUTORIAL";
    }

    @Override
    public void startGame(GameContainer container) {
        try {
            WindowCombat.game = GuidedGame.createTutorialByRace(raceCombo.optionSelected.text);
            WindowMain.windowSwitch(container, "Combat", "Tutorial");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            raceCombo.checkIfItsClicked(input);
            if (raceCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
                raceImage = new Image("media/" + raceCombo.optionSelected.text + ".png");
            } else if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                startGame(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "Menu");
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        volver.render(g);
        aceptar.render(g);
        salir.render(g);
        raceImage.draw(WindowCombat.responsiveX(35), WindowCombat.responsiveY(45));
        g.drawString(Game.anadir_saltos_de_linea(RaceEnum.raceEnumMap.get(raceCombo.optionSelected.text).getDescription(), 500), WindowCombat.responsiveX(50), WindowCombat.responsiveY(50));
        raceCombo.render(g);
    }

}
