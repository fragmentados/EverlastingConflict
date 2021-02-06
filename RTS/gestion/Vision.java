/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.gestion;

import RTS.relojes.Reloj;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Elías
 */
public class Vision {

    public Ellipse2D forma;
    public float tiempo_contador;

    public Vision(float x, float y, float an, float al, float t) {
        forma = new Ellipse2D.Float(x, y, an, al);
        tiempo_contador = t;
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

}
