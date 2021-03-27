/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementosvisuales;

import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.*;


public class BotonSimple {

    public Image sprite;
    public float x, y, altura, anchura;
    public boolean canBeUsed = true;
    public String texto;
    public String tag;

    public BotonSimple() {

    }

    public BotonSimple(Image im) {
        sprite = im;
        anchura = im.getWidth();
        altura = im.getHeight();
    }

    public BotonSimple(String t) {
        texto = t;
        anchura = t.length() * 10;
        altura = 20;
    }

    public BotonSimple(String t, float x, float y) {
        this(t);
        this.x = x;
        this.y = y;
    }

    public BotonSimple(String t, String tag, float x, float y) {
        this(t, x, y);
        this.tag = tag;
    }

    public boolean isHovered(float x, float y) {
        if (canBeUsed) {
            if ((x >= this.x) && (x <= (this.x + anchura))) {
                if ((y >= this.y) && (y <= (this.y + altura))) {
                    if (canBeUsed) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void render(Graphics g) {
        if (canBeUsed) {
            if (sprite != null) {
                sprite.draw(x, y, anchura, altura);
            } else {
                float x_contador = x + this.anchura / 2 - this.texto.length() * 9 / 2;
                float y_contador = y + this.altura / 2 - 7;
                g.drawString(texto, x_contador, y_contador);
            }
            if (tag != null) {
                g.setColor(Color.green);
                g.drawString(tag, x + anchura - tag.length() * 8, y - 16);
            }
            g.setColor(new Color(0f, 0.6f, 0.8f, 0.5f));
            g.drawRect(x, y, anchura, altura);
            g.fillRect(x, y, anchura, altura);
            g.setColor(Color.white);
        }
    }

    public void checkIfItsClicked(Input input) throws SlickException {
        if (isHovered(input.getMouseX(),
                input.getMouseY())) {
            effect();
        }
    }

    public void checkIfItsClickedInCombat(Input input) throws SlickException {
        if (isHovered((int) WindowCombat.playerX + input.getMouseX(),
                (int) WindowCombat.playerY + input.getMouseY())) {
            effect();
        }
    }

    public void effect() throws SlickException {

    }
}
