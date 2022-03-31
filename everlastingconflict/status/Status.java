/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.status;

import everlastingconflict.watches.Reloj;
import org.newdawn.slick.Image;


public class Status {

    public StatusNameEnum name;
    public float time, timeCounter;
    //Variable para el uso del estado: Por ejemplo, porcentaje de ralentización de movimiento.
    public float value;
    public Image icon;

    public Status(StatusNameEnum name) {
        //Constructor de estados permanentes
        this.name = name;
        try {
            this.icon = new Image("media/Stats/" + name.getName() + ".png");
        } catch (Exception e) {
        }
    }

    public Status(StatusNameEnum name, float t, float c) {
        this(name, t);
        this.value = c;
    }

    public Status(StatusNameEnum name, float t) {
        this(name);
        timeCounter = time = t;
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
