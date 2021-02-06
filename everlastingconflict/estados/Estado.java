/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.estados;

import everlastingconflict.relojes.Reloj;

/**
 *
 * @author Elías
 */
public class Estado {

    public String tipo;
    public float tiempo, tiempo_contador;
    //Variable para el uso del estado: Por ejemplo, porcentaje de ralentización de movimiento.
    public float contador;

    public static final String nombre_stun = "Stun";
    public static final String nombre_silencio = "Silencio";
    public static final String nombre_pacifismo = "Pacifismo";
    public static final String nombre_snare = "Snare";
    public static final String nombre_inhabilitacion = "Inhabilitación";
    public static final String nombre_ralentizacion = "Ralentización";
    public static final String nombre_ataque_potenciado = "Ataque potenciado";
    public static final String nombre_amnesia = "Amnesia";
    public static final String nombre_regeneracion = "Regeneración";
    public static final String nombre_necesidad = "Tiempos de necesidad";
    public static final String nombre_erosion = "Erosión";
    public static final String nombre_meditacion = "Meditación";
    public static final String nombre_supervivencia  = "Supervivencia";
    public static final String nombre_drenantes  = "Impactos drenantes";
    public static final String nombre_defensa  = "Defensa férrea";

    public Estado(String ti, float t, float c) {
        this(ti, t);
        this.contador = c;
    }

    public Estado(String ti, float t) {
        this(ti);
        tiempo_contador = tiempo = t;
    }

    public Estado(String ti) {
        //Constructor de estados permanentes
        tipo = ti;
    }

    public boolean comportamiento(int delta) {
        if (tiempo > 0) {
            if (tiempo_contador - Reloj.velocidad_reloj * delta <= 0) {
                return true;
            } else {
                tiempo_contador -= Reloj.velocidad_reloj * delta;
            }
        }
        return false;
    }

}
