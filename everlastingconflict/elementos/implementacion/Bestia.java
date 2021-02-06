/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.elementos.ElementoAtacante;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Elías
 */
public class Bestia extends Unidad {

    public float x_inicial, y_inicial;
    public int recompensa;
    public static final int distancia_maxima = 300;

    public void cambiar_coordenadas(float x, float y) {
        this.x = x;
        this.y = y;
        this.x_inicial = x;
        this.y_inicial = y;
    }

    public final void iniciar_imagenes_bestias() {
        try {
            icono = new Image("media/Iconos/" + nombre + ".png");
            sprite = new Animation(new Image[]{new Image("media/Unidades/" + nombre + ".png")}, new int[]{300}, false);
            sonido_combate = new Sound("media/Sonidos/" + nombre + ".ogg");
            miniatura = new Image("media/Miniaturas/Prueba.png");
            //miniatura = new Image("media/Miniaturas/" + nombre + ".png");
        } catch (SlickException e) {

        }
    }

    public Bestia(String n) {
        super("No hay");
        nombre = n;
        Raza.bestia(this);
        this.iniciar_imagenes_bestias();
        anchura = sprite.getWidth();
        altura = sprite.getHeight();
        anchura_barra_vida = anchura;
        vida = vida_max;
    }

    public Bestia(String n, float x, float y) {
        this(n);
        this.x = x;
        this.y = y;
        x_inicial = x;
        y_inicial = y;
    }

    public void movimiento_aleatorio() {

    }

    public float calcular_distancia() {
        float distancia_x, distancia_y;
        distancia_x = Math.abs(x_inicial - x);
        distancia_y = Math.abs(y_inicial - y);
        return (distancia_x + distancia_y);
    }

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {
        Jugador aliado = p.jugador_aliado(atacante);
        if (aliado.raza.equals("Clark")) {
            aliado.recursos += this.recompensa;
        }
        if (this.seleccionada()) {
            this.deseleccionar();
        }
        for (Bestias b : p.bestias) {
            if (b.contenido.indexOf(this) != -1) {
                b.contenido.remove(this);
                if (b.contenido.isEmpty()) {
                    b.muerte = true;
                }
                break;
            }
        }
        if (atacante instanceof Manipulador) {
            ((Manipulador) atacante).aumentar_experiencia(experiencia_al_morir);
        }
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        if (movimiento != null) {
            if (movimiento.resolucion(p, delta)) {
                if (this.estado.equals("Mover")) {
                    parar();
                } else {
                    movimiento = null;
                }
            }
        }
        if (cadencia_contador > 0) {
            if (cadencia_contador - (Reloj.velocidad_reloj * delta) <= 0) {
                cadencia_contador = 0;
            } else {
                cadencia_contador -= (Reloj.velocidad_reloj * delta);
            }
        }
        switch (estado) {
            case "Atacando":
                float distancia = calcular_distancia();
                if (distancia >= Bestia.distancia_maxima) {
                    this.mover(p, x_inicial, y_inicial);
                } else {
                    if (alcance(this.alcance, objetivo)) {
                        movimiento = null;
                        ataque(p);                                                    
                    } else {
                        anadir_movimiento(objetivo.x, objetivo.y);
                    }
                }
                break;
            case "Parado":
                if ((x != x_inicial) || (y != y_inicial)) {
                    this.mover(p, x_inicial, y_inicial);
                } else {
                    if (vida < vida_max) {
                        vida += 0.1 * delta;
                    }
                }
                break;
        }
    }
}
