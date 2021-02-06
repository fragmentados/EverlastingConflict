/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElementosSlick2D;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Elías
 */
public class BotonSimple {

    public Image sprite;
    public float x, y, altura, anchura;
    public boolean activado;
    public String texto;

    public BotonSimple() {

    }

    public BotonSimple(Image im) {
        sprite = im;
        anchura = im.getWidth();
        altura = im.getHeight();
        activado = true;
    }

    public BotonSimple(String t) {
        texto = t;
        anchura = t.length() * 10;
        altura = 20;
        activado = true;
    }

    public BotonSimple(String t, float x, float y) {
        this(t);
        this.x = x;
        this.y = y;
    }

    public BotonSimple(String t, float x, float y, float anchura, float altura) {
        this(t, x, y);
        this.anchura = anchura;
        this.altura = altura;
    }

    public boolean presionado(float x, float y) {
        if (activado) {
            if ((x >= this.x) && (x <= (this.x + anchura))) {
                if ((y >= this.y) && (y <= (this.y + altura))) {
                    if (activado) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void dibujar(Graphics g) {
        if (activado) {
            if (sprite != null) {
                sprite.draw(x, y, anchura, altura);
            } else {
                float x_contador = this.x + this.anchura / 2 - this.texto.length() * 9 / 2;
                float y_contador = this.y + this.altura / 2 - 7;
                g.drawString(texto, x_contador, y_contador);
            }
            g.setColor(new Color(0f, 0.6f, 0.8f, 0.5f));
            g.drawRect(x, y, anchura, altura);
            g.fillRect(x, y, anchura, altura);
            g.setColor(Color.white);
        }
    }

    public void efecto() {

    }
}
