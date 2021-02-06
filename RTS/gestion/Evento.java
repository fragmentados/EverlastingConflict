/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.gestion;

import ElementosSlick2D.BotonComplejo;
import RTS.elementos.ElementoCoordenadas;
import RTS.relojes.Reloj;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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

    public final void iniciar_datos() {
        switch (nombre) {
            //Negativos
            case "Una racha criminal asola las calles":
                efecto = 20;
                cantidad_elemento = 2;
                nombre_elemento = "Patrulla";
                tiempo_evento = tiempo_contador = 45f;
                break;
            case "Manifestaciones en las plazas":
                efecto = 35;
                nombre_elemento = "Pacificador";
                cantidad_elemento = 1;
                tiempo_evento = tiempo_contador = 60f;
                break;
            case "Una revolución se acerca":
                efecto = 50;
                cantidad_elemento = 1;
                nombre_elemento = "Silenciador";
                tiempo_evento = tiempo_contador = 90f;
                break;
            //Positivos
            case "Decorar los parques":
                efecto = 5;
                coste = 20;
                positivo = true;
                coste_alternativo = 1;
                break;
            case "Construir colegios":
                efecto = 20;
                coste = 100;
                positivo = true;
                coste_alternativo = 2;
                break;
            case "Construir bloques de viviendas":
                efecto = 35;
                coste = 300;
                positivo = true;
                coste_alternativo = 3;
                break;
            case "Sufragio universal":
                efecto = 100;
                coste = 500;
                positivo = true;
                coste_alternativo = 3;
                break;
        }
        try {
            icono = new Image("datar/Eventos/" + nombre + ".png");
            sprite = new Animation(new Image[]{icono}, 300, false);
            anchura = icono.getWidth();
            altura = icono.getHeight();
        } catch (SlickException ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Evento(String n) {
        this.nombre = n;
        iniciar_datos();
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
            if (tiempo_contador - Reloj.velocidad_reloj * delta <= 0) {
                efecto(j);
                return true;
            } else {
                tiempo_contador -= Reloj.velocidad_reloj * delta;
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
