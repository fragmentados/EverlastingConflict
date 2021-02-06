/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.campaign.tutorial;

import RTS.gestion.Partida;
import RTS.mapas.MapaCampo;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public class Paso {

    public String texto;

    public Paso(String t) {
        texto = t;
        this.texto = Partida.anadir_saltos_de_linea(this.texto, MapaCampo.VIEWPORT_SIZE_X);
    }

    public boolean comprobacion(Partida p) {
        Input input = RTS.RTS.canvas.getContainer().getInput();
        return MapaCampo.continuar.presionado(MapaCampo.playerX + input.getMouseX(), MapaCampo.playerY + input.getMouseY());
    }

    public void efecto(Partida p) {

    }
}
