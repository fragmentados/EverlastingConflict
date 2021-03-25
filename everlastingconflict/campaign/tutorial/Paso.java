/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.gestion.Partida;
import everlastingconflict.ventanas.WindowCombat;
import org.newdawn.slick.Input;

/**
 * @author Elías
 */
public class Paso {

    public String texto;
    public boolean requiresClick = true;

    public Paso(String t) {
        texto = t;
        this.texto = Partida.anadir_saltos_de_linea(this.texto, WindowCombat.VIEWPORT_SIZE_WIDTH);
    }

    public Paso(String t, boolean requiresClick) {
        this(t);
        this.requiresClick = requiresClick;
    }

    public boolean comprobacion(Partida p) {
        Input input = RTS.canvas.getContainer().getInput();
        return WindowCombat.continuar.isHovered(WindowCombat.playerX + input.getMouseX(), WindowCombat.playerY + input.getMouseY());
    }

    public void efecto(Partida p) {

    }
}
