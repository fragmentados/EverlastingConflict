/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.gestion.Partida;
import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Elías
 */
public class MensajeChat {

    float x, y;
    private final String nombre_usuario;
    final String texto;
    private float tiempo, tiempo_contador;

    public MensajeChat(String usuario, String texto) {
        this.nombre_usuario = usuario;
        this.texto = texto;
        tiempo_contador = 0;
    }

    public boolean comportamiento(int delta) {
        tiempo_contador += Reloj.TIME_REGULAR_SPEED * delta;
        return (tiempo_contador >= tiempo);
    }

    public void dibujar(Partida p, Graphics g) {
        if (p.j1.nombre.equals(nombre_usuario)) {
            g.setColor(p.j1.color);
        } else {
            g.setColor(p.j2.color);
        }
        g.drawString(nombre_usuario + ": ", x, y);
        g.setColor(Color.white);
        g.drawString(texto, x + nombre_usuario.length() * 10, y);
    }
}
