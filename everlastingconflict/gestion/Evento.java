/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.elementos.ElementoCoordenadas;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.razas.SubRaceEnum;
import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elías
 */
public class Evento extends ElementoCoordenadas {

    public float tiempo_evento, tiempo_contador;
    public int efecto;
    public int cantidad_elemento;
    public String nombre_elemento;
    public boolean positivo;

    public final void initData(Jugador aliado) {
        switch (nombre) {
            //Negativos
            case "Una racha criminal asola las calles":
                efecto = 20;
                cantidad_elemento = (SubRaceEnum.POLICIA.equals(aliado.subRace)) ? 1 : 2;
                nombre_elemento = "Patrulla";
                tiempo_evento = tiempo_contador = 45f;
                break;
            case "Manifestaciones en las plazas":
                efecto = 35;
                nombre_elemento = "Pacificador";
                cantidad_elemento = 1;
                tiempo_evento = tiempo_contador = (SubRaceEnum.POLICIA.equals(aliado.subRace)) ? 75f : 60f;
                break;
            case "Una revolución se acerca":
                efecto = 50;
                cantidad_elemento = 1;
                nombre_elemento = "Silenciador";
                tiempo_evento = tiempo_contador = (SubRaceEnum.POLICIA.equals(aliado.subRace)) ? 120f : 90f;
                break;
            //Positivos
            case "Decorar los parques":
                efecto = 5;
                coste = 20;
                positivo = true;
                guardiansThreatLevelNeeded = 1;
                break;
            case "Construir colegios":
                efecto = 20;
                coste = 100;
                positivo = true;
                guardiansThreatLevelNeeded = 2;
                break;
            case "Construir bloques de viviendas":
                efecto = 35;
                coste = 300;
                positivo = true;
                guardiansThreatLevelNeeded = 3;
                break;
            case "Sufragio universal":
                efecto = 100;
                coste = 500;
                positivo = true;
                guardiansThreatLevelNeeded = 3;
                break;
        }
        try {
            icono = new Image("media/Eventos/" + nombre + ".png");
            sprite = new Animation(new Image[]{icono}, 300, false);
            anchura = icono.getWidth();
            altura = icono.getHeight();
        } catch (SlickException ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Evento(Jugador aliado, String n) {
        this.nombre = n;
        initData(aliado);
    }

    public void efecto(Jugador j) {
        if (positivo) {
            j.aumentar_porcentaje(efecto);
        } else {
            j.disminuir_porcentaje(efecto);
        }
    }

    public boolean comportamiento(Jugador j, int delta) {
        if (!positivo) {
            if (tiempo_contador - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                efecto(j);
                return true;
            } else {
                tiempo_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        }
        return false;
    }

    public void dibujar(Graphics g) {
        sprite.draw(x, y);
        if (positivo) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.red);
        }
        g.drawRect(x, y, anchura, altura);
        g.setColor(Color.white);
        if (!positivo) {
            BotonComplejo.dibujar_aguja(g, x, y, tiempo_contador, tiempo_evento);
        }
    }

}
