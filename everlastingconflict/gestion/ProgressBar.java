/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.relojes.Reloj;
import everlastingconflict.elementos.implementacion.Edificio;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Elías
 */
public class ProgressBar {

    public int anchura;
    public int altura = 5;
    public float progreso;
    private float maximo;
    private float x, y;
    private boolean activo;

    public ProgressBar(Edificio e) {
        this.anchura = e.anchura_barra_vida;
        this.x = e.x - e.anchura / 2;
        this.y = e.y + e.altura / 2 + e.altura_barra_vida;
        activo = false;
    }

    public ProgressBar(int x, int y, float m) {
        this.x = x;
        this.y = y;
        this.maximo = m;
        this.progreso = 0;
        activo = false;
    }

    public void activar(float m) {
        activo = true;
        progreso = 0;
        maximo = m;
    }

    public boolean isActive() {
        return activo;
    }

    public void desactivar() {
        activo = false;
    }

    public void aumentar_progreso(int delta) {
        if (activo) {
            if (progreso < maximo) {
                if ((progreso + (Reloj.velocidad_reloj * delta)) >= maximo) {
                    progreso = maximo;
                    activo = false;
                } else {
                    progreso += (Reloj.velocidad_reloj * delta);
                }
            }

        }
    }

    public boolean terminado() {
        return progreso == maximo;
    }

    public float getMaximo() {
        return maximo;
    }

    public void dibujar(Graphics g) {
        if (activo) {
            g.drawRect(x, y, anchura, altura);
            g.setColor(Color.blue);
            g.fillRect(x, y, anchura * (progreso / maximo), altura);
            g.setColor(Color.white);
            g.fillRect(x + anchura * (progreso / maximo), y, anchura - (anchura * (progreso / maximo)), altura);
        }
    }
}
