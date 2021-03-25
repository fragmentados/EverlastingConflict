/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.gestion.Game;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Input;

public class GameStep {

    public String text;
    public boolean requiresClick;

    public GameStep() {
        requiresClick = false;
    }

    public GameStep(String t) {
        text = t;
        this.text = Game.anadir_saltos_de_linea(this.text, WindowCombat.VIEWPORT_SIZE_WIDTH);
        requiresClick = true;
    }

    public GameStep(String t, boolean requiresClick) {
        this(t);
        this.requiresClick = requiresClick;
    }

    public boolean check(Game p) {
        Input input = RTS.canvas.getContainer().getInput();
        return WindowCombat.continuar.isHovered(WindowCombat.playerX + input.getMouseX(), WindowCombat.playerY + input.getMouseY());
    }

    public void effect(Game p) {

    }
}
