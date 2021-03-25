/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.elements.ElementoCoordenadas;
import everlastingconflict.watches.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Vision extends ElementoCoordenadas {

    public int diameter;
    public float tiempo_contador;
    public boolean fullVision = true;

    public Vision(float x, float y, int diameter, float t) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        tiempo_contador = t;
    }

    public boolean comportamiento(int delta) {
        if (tiempo_contador > 0) {
            if (tiempo_contador - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                return true;
            } else {
                tiempo_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        }
        return false;
    }

    public void dibujar(Graphics g, Color color, int desplazamiento) {
        g.setColor(color);
        float alcance = (float) (diameter + desplazamiento);
        g.fillOval(x - alcance / 2, y - alcance / 2, alcance, alcance);
    }

}
