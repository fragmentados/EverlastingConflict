/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.windows;

import everlastingconflict.RTS;
import everlastingconflict.elements.util.ElementosComunes;
import org.lwjgl.opengl.Display;
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
    public static WindowMenuTutorial tutorialWindow = new WindowMenuTutorial();
    public static Window currentWindow;
    public static String previousWindow;

    public static void windowSwitch(GameContainer container, String t, String previousWindow) throws SlickException {
        WindowMain.windowSwitch(container, t);
        WindowMain.previousWindow = previousWindow;
    }

    public static void windowSwitch(GameContainer container, String t) throws SlickException {
        Window contador = null;
        switch (t) {
            case "Menu":
                contador = menuWindow;
                break;
            case "Combat":
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
            case "Tutorial":
                contador = tutorialWindow;
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
        ElementosComunes.initSimpleResources();
        windowSwitch(container, "Menu");
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

    @Override
    public boolean closeRequested() {
        System.exit(0);
        return false;
    }

    public static void exit(GameContainer container) {
        if (WindowCombat.CURRENT_BSO != null) {
            WindowCombat.CURRENT_BSO.stop();
        }
        container.setForceExit(true);
        container.exit();
        RTS.mainFrame.dispose();
        Display.destroy();
        System.exit(0);
    }
}
