/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.relojes;

import everlastingconflict.gestion.Jugador;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Elías
 */
public abstract class Reloj {

    //Variable para comprobar con comodidad el cuarto en el que se encuentra el reloj
    public int ndivision;
    public float contador_reloj;
    public float detener, detener_contador;
    public static float velocidad_reloj = 0.001f;
    public Image sprite;
    public Jugador jugadorAsociado;

    public void detener_reloj(float d) {
        detener_contador = detener = d;
    }

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
    
    public abstract void avanzar_reloj(int delta);
    
    public abstract void dibujar(Graphics g);
    
}
