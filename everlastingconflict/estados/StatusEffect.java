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

    public StatusEffectName name;
    public float time, timeCounter;
    //Variable para el uso del estado: Por ejemplo, porcentaje de ralentización de movimiento.
    public float value;

    public StatusEffect(StatusEffectName status, float t, float c) {
        this(status, t);
        this.value = c;
    }

    public StatusEffect(StatusEffectName status, float t) {
        this(status);
        timeCounter = time = t;
    }

    public StatusEffect(StatusEffectName status) {
        //Constructor de estados permanentes
        name = status;
    }

    public boolean comportamiento(int delta) {
        if (time > 0) {
            if (timeCounter - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                return true;
            } else {
                timeCounter -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        }
        return false;
    }

}
