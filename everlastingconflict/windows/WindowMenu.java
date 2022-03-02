/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.windows;

import everlastingconflict.elementosvisuales.BotonSimple;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class WindowMenu extends WindowMenuBasic {

    public BotonSimple combate, tutorial, salir, changelog, challenge, story;

    @Override
    public void init(GameContainer container) throws SlickException {
        super.init(container);
        story = new BotonSimple("Historia", WindowCombat.middleScreenX("Historia"), WindowCombat.responsiveY(20));
        tutorial = new BotonSimple("Tutorial", WindowCombat.middleScreenX("Tutorial"), WindowCombat.responsiveY(30));
        combate = new BotonSimple("Combate", WindowCombat.middleScreenX("Combate"), WindowCombat.responsiveY(40));
        challenge = new BotonSimple("Desafios", "Nuevo!", WindowCombat.middleScreenX("Desafios"),
                WindowCombat.responsiveY(50));
        changelog = new BotonSimple("Changelog", "Nuevo!", WindowCombat.middleScreenX("Changelog"),
                WindowCombat.responsiveY(60));
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - "Salir".length() * 10, 0);
        WindowMenuBasic.subTitle = null;
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            WindowMain.exit(container);
        }
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (story.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "Story");
            } else if (combate.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "PlayerSelection");
            } else if (tutorial.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "Tutorial");
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            } else if (changelog.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "Changelog");
            } else if (challenge.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "Challenge");
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        story.render(g);
        combate.render(g);
        tutorial.render(g);
        challenge.render(g);
        changelog.render(g);
        salir.render(g);
    }

}
