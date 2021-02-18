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
public class StatusEffect {

    public StatusEffectName tipo;
    public float tiempo, tiempo_contador;
    //Variable para el uso del estado: Por ejemplo, porcentaje de ralentización de movimiento.
    public float contador;

    public StatusEffect(StatusEffectName status, float t, float c) {
        this(status, t);
        this.contador = c;
    }

    public StatusEffect(StatusEffectName status, float t) {
        this(status);
        tiempo_contador = tiempo = t;
    }

    public StatusEffect(StatusEffectName status) {
        //Constructor de estados permanentes
        tipo = status;
    }

    public boolean comportamiento(int delta) {
        if (tiempo > 0) {
            if (tiempo_contador - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                return true;
            } else {
                tiempo_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        }
        return false;
    }

}
