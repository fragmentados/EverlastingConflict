/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.elementos;

import RTS.estados.Estados;
import RTS.gestion.Partida;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public abstract class ElementoEstado extends ElementoComplejo {

    public String estado;
    public Estados estados;

    @Override
    public abstract void destruir(Partida p, ElementoAtacante atacante);   

    public void comportamiento(Partida p, Graphics g, int delta) {
        estados.comportamiento(delta);        
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
    }
}
