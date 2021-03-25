/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.windows;

import everlastingconflict.RTS;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.gestion.Game;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class WindowMain extends BasicGame {

    public static WindowCombat combatWindow = new WindowCombat();
    public static WindowMenuChallenges challengeWindow = new WindowMenuChallenges();
    public static WindowMenu menuWindow = new WindowMenu();
    public static WindowMenuPlayerSelection playerSelectionWindow = new WindowMenuPlayerSelection();
    public static WindowMenuChangelog changelogWindow = new WindowMenuChangelog();
    public static Window currentWindow;

    public static void windowSwitch(GameContainer container, Game p, String t) throws SlickException {
        Window contador = null;
        switch (t) {
            case "Menu":
                contador = menuWindow;
                break;
            case "Combat":
                combatWindow.game = p;
                contador = combatWindow;
                break;
            case "Challenge":
                contador = challengeWindow;
                break;
            case "PlayerSelection":
                contador = playerSelectionWindow;
                break;
            case "Changelog":
                contador = changelogWindow;
                break;
        }
        if (contador != null) {
            contador.init(container);
            currentWindow = contador;
        }

    }

    public WindowMain() {
        super("VentanaPrincipal");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        ElementosComunes.init();
        windowSwitch(container, new Game(), "Menu");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        try {
            currentWindow.update(container, delta);
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw ex;
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        try {
            currentWindow.render(container, g);
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw ex;
        }
    }

    public static void exit(GameContainer container) {
        RTS.canvas.dispose();
        RTS.mainFrame.dispose();
        container.exit();
        System.exit(0);
    }
}
