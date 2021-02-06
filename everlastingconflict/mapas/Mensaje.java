/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Elías
 */
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
        this.x = MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X / 2 - (mensaje.length() * 10) / 2;
        this.y = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y - 20;
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
        tiempo_contador += Reloj.velocidad_reloj * delta;
        return (tiempo_contador >= tiempo);
    }

    public void dibujar(Graphics g) {
        g.setColor(color);
        g.drawString(mensaje, x, y);
        g.setColor(Color.white);
    }

}
