/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.awt.geom.Ellipse2D;

import static everlastingconflict.elementos.util.ElementosComunes.FULL_VISIBLE_COLOR;
import static everlastingconflict.elementos.util.ElementosComunes.HALF_VISIBLE_COLOR;

/**
 *
 * @author Elías
 */
public class Vision {

    public Ellipse2D forma;
    public float tiempo_contador;
    public boolean fullVision = true;

    public Vision(float x, float y, float an, float al, float t) {
        forma = new Ellipse2D.Float(x, y, an, al);
        tiempo_contador = t;
    }

    public Vision(float x, float y, float an, float al, float t, boolean fullVision) {
        this(x, y, an, al, t);
        this.fullVision = fullVision;
    }

    public boolean comportamiento(int delta) {
        if (tiempo_contador > 0) {
            if (tiempo_contador - Reloj.velocidad_reloj * delta <= 0) {
                return true;
            } else {
                tiempo_contador -= Reloj.velocidad_reloj * delta;
            }
        }
        return false;
    }

    public void dibujar(Graphics g, int desplazamiento) {
        g.setColor(fullVision ? FULL_VISIBLE_COLOR : HALF_VISIBLE_COLOR);
        float alcance = (float) (this.forma.getWidth() + desplazamiento);
        g.fillOval((float) (forma.getX() - alcance / 2), (float) (forma.getY() - alcance / 2), alcance, alcance);
    }

}
