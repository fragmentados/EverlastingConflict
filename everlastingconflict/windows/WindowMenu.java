/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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


public class WindowMenu extends WindowMenuBasic {

    public BotonSimple combate, tutorial, volver, aceptar, salir, changelog, challenge;
    private ComboBox raceTutorial;
    private Image tutorialImage;
    public boolean start;
    private boolean tutorialMode = false;

    @Override
    public void init(GameContainer container) throws SlickException {
        super.init(container);
        tutorial = new BotonSimple("Tutorial", WindowCombat.responsiveX(45), WindowCombat.responsiveY(40));
        combate = new BotonSimple("Combate", WindowCombat.responsiveX(45), WindowCombat.responsiveY(50));
        challenge = new BotonSimple("Desafios", "Nuevo!", WindowCombat.responsiveX(45), WindowCombat.responsiveY(60));
        changelog = new BotonSimple("Changelog", "Nuevo!", WindowCombat.responsiveX(45), WindowCombat.responsiveY(70));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - 60, 0);
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(40), WindowCombat.responsiveY(85));
        aceptar = new BotonSimple("Aceptar", WindowCombat.responsiveX(45), WindowCombat.responsiveY(85));
        aceptar.canBeUsed = volver.canBeUsed = false;
        raceTutorial = new ComboBox("Raza:",
                Arrays.stream(RaceEnum.values()).map(r -> new ComboBoxOption(r.getName(), r.getDescription(),
                        r.getImagePath())).collect(Collectors.toList()),
                WindowCombat.responsiveX(45), WindowCombat.responsiveY(40));
        tutorialImage = new Image("media/" + raceTutorial.optionSelected.text + ".png");
    }

    public void start(GameContainer container) {
        try {
            WindowMain.windowSwitch(container,
                    GuidedGame.createTutorialByRace(raceTutorial.optionSelected.text),
                    "Combat");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        if (start) {
            start(container);
            start = false;
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            WindowMain.exit(container);
        }
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (combate.isHovered(input.getMouseX(), input.getMouseY())) {
                tutorial.canBeUsed = combate.canBeUsed = false;
                aceptar.canBeUsed = volver.canBeUsed = true;
                WindowMain.windowSwitch(container, null, "PlayerSelection");
            } else if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                start = true;
                tutorialMode = false;
            } else if (tutorial.isHovered(input.getMouseX(), input.getMouseY())) {
                volver.canBeUsed = tutorialMode = aceptar.canBeUsed = true;
                challenge.canBeUsed = changelog.canBeUsed = combate.canBeUsed = tutorial.canBeUsed = false;
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                tutorialMode = volver.canBeUsed = aceptar.canBeUsed = false;
                challenge.canBeUsed = changelog.canBeUsed = combate.canBeUsed = tutorial.canBeUsed = true;
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            } else if (changelog.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, null, "Changelog");
            } else if (challenge.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, null, "Challenge");
            }
            raceTutorial.checkIfItsClicked(input);
            if (raceTutorial.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
                tutorialImage = new Image("media/" + raceTutorial.optionSelected.text + ".png");
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        Input input = container.getInput();
        combate.render(g);
        tutorial.render(g);
        challenge.render(g);
        changelog.render(g);
        salir.render(g);
        volver.render(g);
        aceptar.render(g);
        if (tutorialMode) {
            tutorialImage.draw(WindowCombat.responsiveX(35), WindowCombat.responsiveY(45));
            g.drawString(Game.anadir_saltos_de_linea(RaceEnum.raceEnumMap.get(raceTutorial.optionSelected.text).getDescription(), 500), WindowCombat.responsiveX(50), WindowCombat.responsiveY(50));
            raceTutorial.render(input, g);
        }
    }

}
