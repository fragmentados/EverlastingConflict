/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.elementos.implementacion;

import RTS.elementos.ElementoCoordenadas;
import RTS.gestion.Partida;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class ElementoEspecial extends ElementoCoordenadas {

    public final void iniciar_imagenes() {
        try {
            sprite = new Animation(new Image[]{new Image("datar/ElementosEspeciales/" + nombre + ".png")}, new int[]{300}, false);            
            icono = new Image("datar/Iconos/" + nombre + ".png");            
            anchura = sprite.getWidth();
            altura = sprite.getHeight();
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
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        sprite.draw(x - anchura / 2, y - altura / 2);
        g.setColor(Color.black);
        g.drawRect(x - anchura / 2, y - altura / 2, anchura, altura);
        g.setColor(Color.white);
    }

}
