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
    public boolean requiresContinueClick;

    public GameStep() {
        requiresClick = false;
    }

    public GameStep(String t) {
        text = t;
        this.text = Game.formatTextToFitWidth(this.text, WindowCombat.VIEWPORT_SIZE_WIDTH);
        this.requiresContinueClick = requiresClick = true;
    }

    public GameStep(String t, boolean requiresClick) {
        this(t);
        this.requiresContinueClick = this.requiresClick = requiresClick;
    }

    public GameStep(String t, boolean requiresClick, boolean requiresContinueClick) {
        this(t, requiresClick);
        this.requiresContinueClick = requiresContinueClick;
    }

    public boolean check(Game p) {
        Input input = RTS.canvas.getContainer().getInput();
        return WindowCombat.continuar.isHovered(WindowCombat.playerX + input.getMouseX(), WindowCombat.playerY + input.getMouseY());
    }

    public void effect(Game p) {

    }
}
