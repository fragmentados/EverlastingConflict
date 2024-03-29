/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements.impl;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.ElementoAtacante;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Raza;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.status.StatusNameEnum;
import everlastingconflict.watches.Reloj;
import everlastingconflict.windows.Mensaje;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;


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
            Integer imageIndex = 1;
            List<Image> imageList = new ArrayList<>();
            try {
                do {
                    imageList.add(new Image("media/Unidades/" + nombre + imageIndex + ".png"));
                    imageIndex++;
                } while (imageIndex < 4);
            } catch (Exception e) {
            }
            Image[] images = new Image[imageList.size()];
            imageList.toArray(images);
            animation = new Animation(images, 450, false);
            icono = new Image("media/Iconos/" + nombre + ".png");
            sonido_combate = new Sound("media/Sonidos/" + nombre + "Ataque.ogg");
        } catch (Exception e) {

        }
    }

    public Bestia(String n) {
        super(null, "No hay");
        nombre = n;
        Raza.bestia(this);
        this.iniciar_imagenes_bestias();
        anchura = animation.getWidth();
        altura = animation.getHeight();
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
    public void destruir(Game p, ElementoAtacante atacante) {
        // We use this check in case of several concurrent destructions
        if (!BehaviourEnum.DESTRUIDO.equals(this.behaviour)
                && p.bestias.stream().filter(b -> b.contenido.contains(this)).findFirst().isPresent()) {
            this.behaviour = BehaviourEnum.DESTRUIDO;
            Jugador aliado = p.getPlayerFromElement(atacante);
            if (aliado.raza.equals(RaceEnum.CLARK)) {
                WindowMain.combatWindow.anadir_mensaje(new Mensaje("+" + this.recompensa, Color.green, x, y - altura / 2 - 20, 2f));
                aliado.addResources(this.recompensa);
            }
            if (this.isSelected()) {
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
            Manipulador.checkToGainExperience(p, atacante, recompensa, x, y, altura);
        }
    }

    @Override
    public void comportamiento(Game p, Graphics g, int delta) {
        List<Jugador> mastersPlayers = p.getPlayersByRace(RaceEnum.MAESTROS);
        for (Jugador masterPlayer : mastersPlayers) {
            for (ElementoEspecial e : masterPlayer.elementos_especiales) {
                if (e.nombre.equals("Agujero negro")) {
                    if (this.alcance(e.x, e.y, e.anchura / 2)) {
                        this.destruir(p,
                                masterPlayer.unidades.stream().filter(m -> m.nombre.equals("Manipulador")).findFirst().orElse(null));
                    }
                }
            }
        }
        if (movimiento != null) {
            if (movimiento.resolucion(p, delta)) {
                if (behaviour.equals(BehaviourEnum.MOVER)) {
                    parar();
                } else {
                    movimiento = null;
                }
            }
        }
        if (cadencia_contador > 0) {
            if (cadencia_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                cadencia_contador = 0;
            } else {
                cadencia_contador -= (Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        switch (behaviour) {
            case ATACANDO:
                float distancia = calcular_distancia();
                if (distancia >= Bestia.distancia_maxima) {
                    // Go back to origin place
                    if (canMove(p)) {
                        this.objetivo = null;
                        this.mover(p, x_inicial, y_inicial);
                    }
                } else {
                    if (alcance(this.alcance, objetivo)) {
                        movimiento = null;
                        ataque(p);                                                    
                    } else {
                        if (canMove(p)) {
                            anadir_movimiento(objetivo.x, objetivo.y);
                        }
                    }
                }
                break;
            case PARADO:
                for (Jugador masterPlayer : mastersPlayers) {
                    for (ElementoEspecial e : masterPlayer.elementos_especiales) {
                        if (e.nombre.equals("Agujero negro")) {
                            if (this.alcance(300, e)) {
                                this.mover(p, e.x, e.y);
                                break;
                            }
                        }
                    }
                    if (isMoving()) {
                        break;
                    }
                }
                if ((x != x_inicial) || (y != y_inicial)) {
                    this.mover(p, x_inicial, y_inicial);
                } else {
                    if (vida < vida_max) {
                        vida += 0.1 * delta;
                    }
                }
                break;
        }
        if (statusCollection.containsStatus(StatusNameEnum.EROSION)) {
            disminuir_vida(p, Reloj.TIME_REGULAR_SPEED * delta * vida_max / 5);
        }
        statusCollection.comportamiento(p, this, delta);
    }
}
