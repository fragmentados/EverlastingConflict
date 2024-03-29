/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.watches;

import everlastingconflict.elements.ElementoCoordenadas;
import everlastingconflict.gestion.Game;
import org.newdawn.slick.*;

import static everlastingconflict.elements.util.ElementosComunes.UI_COLOR;


public abstract class Reloj extends ElementoCoordenadas {

    //Variable para comprobar con comodidad el cuarto en el que se encuentra el reloj
    public int ndivision;
    public float contador_reloj;
    public float detener, detener_contador;
    public static final float TIME_REGULAR_SPEED = 0.001f;
    public Image sprite;
    float hintBoxWidth = 565;
    float hintBoxHeight = 200;
    protected String hintBoxText;

    public void detener_reloj(float d) {
        detener_contador = detener = d;
    }

    public static final String NO_TIME_REMAINING = "00:00";

    public static String tiempo_a_string(float n) {
        String minutos = Integer.toString((int) n / 60);
        String segundos = Integer.toString((int) n % 60);
        if (minutos.length() == 1) {
            minutos = "0" + minutos;
        }
        if (segundos.length() == 1) {
            segundos = "0" + segundos;
        }
        return minutos + ":" + segundos;
    }

    public abstract float tiempo_restante();
    
    public abstract void avanzar_reloj(Game game, int delta) throws SlickException;
    
    public abstract void dibujar(Input input, Graphics g);

    public void drawHint(Graphics g) {
        g.setColor(UI_COLOR);
        g.fillRect(this.x + this.anchura + 10, this.y, hintBoxWidth, hintBoxHeight);
        g.setColor(Color.white);
        g.drawRect(this.x + this.anchura + 10, this.y, hintBoxWidth, hintBoxHeight);
        g.drawString(this.hintBoxText, this.x + this.anchura + 10 + 1, this.y);
    }

    public boolean hitbox(float x, float y) {
        if ((x >= (this.x - (anchura / 2) + 30)) && (x <= (this.x + anchura))) {
            if ((y >= (this.y - altura / 2)) && (y <= (this.y + altura))) {
                return true;
            }
        }
        return false;
    }

    public abstract void handleLeftClick();
}
