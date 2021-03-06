/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.windows;

import everlastingconflict.watches.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Mensaje {

    public String mensaje;
    public Color color;
    public float x, y;
    public float tiempo, tiempo_contador;
    public boolean error;

    public Mensaje(String m) {
        //Constructor de error
        this.mensaje = m;
        this.color = Color.red;
        this.x = WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH / 2 - (mensaje.length() * 10) / 2;
        this.y = WindowCombat.playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - 20;
        tiempo = 5;
        error = true;
    }

    public Mensaje(String m, float x, float y) {
        mensaje = m;
        this.x = x;
        this.y = y;
        tiempo_contador = 0;
        tiempo = 5;
    }

    public Mensaje(String m, Color c, float x, float y, float t) {
        this(m, x, y);
        color = c;
        tiempo = t;
    }

    public boolean comprobar_mensaje(int delta) {
        tiempo_contador += Reloj.TIME_REGULAR_SPEED * delta;
        return (tiempo_contador >= tiempo);
    }

    public void dibujar(Graphics g) {
        g.setColor(color);
        g.drawString(mensaje, x, y);
        g.setColor(Color.white);
    }

}
