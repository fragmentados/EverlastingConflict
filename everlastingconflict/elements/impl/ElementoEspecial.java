/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements.impl;

import everlastingconflict.elements.ElementoCoordenadas;
import everlastingconflict.gestion.Game;
import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static everlastingconflict.RTS.DEBUG_MODE;


public class ElementoEspecial extends ElementoCoordenadas {

    public final void iniciar_imagenes() {
        try {
            animation = new Animation(new Image[]{new Image("media/ElementosEspeciales/" + nombre + ".png")}, new int[]{300}, false);
            icono = new Image("media/Iconos/" + nombre + ".png");
            anchura = animation.getWidth();
            altura = animation.getHeight();
        } catch (SlickException ex) {
            Logger.getLogger(ElementoEspecial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ElementoEspecial(String string, float x, float y) {
        this.nombre = string;
        iniciar_imagenes();
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(Game p, Color c, Input input, Graphics g) {
        animation.draw(x - anchura / 2, y - altura / 2);
        if (DEBUG_MODE) {
            g.setColor(Color.black);
            g.drawRect(x - anchura / 2, y - altura / 2, anchura, altura);
        }
        g.setColor(Color.white);
    }

}
