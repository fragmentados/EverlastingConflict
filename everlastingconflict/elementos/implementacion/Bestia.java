/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.Mensaje;
import everlastingconflict.mapas.VentanaPrincipal;
import everlastingconflict.razas.RaceNameEnum;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.*;

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
            sonido_combate = new Sound("media/Sonidos/" + nombre + "Ataque.ogg");
            miniatura = new Image("media/Miniaturas/Prueba.png");
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
        // We use this check in case of several concurrent destructions
        if (!StatusBehaviour.DESTRUIDO.equals(this.statusBehaviour)
                && p.bestias.stream().filter(b -> b.contenido.contains(this)).findFirst().isPresent()) {
            this.statusBehaviour = StatusBehaviour.DESTRUIDO;
            Jugador aliado = p.jugador_aliado(atacante);
            if (aliado.raza.equals("Clark")) {
                VentanaPrincipal.mapac.anadir_mensaje(new Mensaje("+" + this.recompensa, Color.green, x, y - altura / 2 - 20, 2f));
                aliado.addResources(this.recompensa);
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
            Manipulador.checkToGainExperience(p, atacante, recompensa, x, y, altura);
        }
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        Jugador mastersPlayer = p.getPlayerByRace(RaceNameEnum.MAESTROS.getName());
        if (mastersPlayer != null) {
            for (ElementoEspecial e : mastersPlayer.elementos_especiales) {
                if (e.nombre.equals("Agujero negro")) {
                    if (this.alcance(e.x, e.y, e.anchura / 2)) {
                        this.destruir(p, mastersPlayer.unidades.stream().filter(m -> m.nombre.equals("Manipulador")).findFirst().orElse(null));
                    }
                }
            }
        }
        if (movimiento != null) {
            if (movimiento.resolucion(p, delta)) {
                if (statusBehaviour.equals(StatusBehaviour.MOVER)) {
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
        switch (statusBehaviour) {
            case ATACANDO:
                float distancia = calcular_distancia();
                if (distancia >= Bestia.distancia_maxima) {
                    if (canMove()) {
                        this.mover(p, x_inicial, y_inicial);
                    }
                } else {
                    if (alcance(this.alcance, objetivo)) {
                        movimiento = null;
                        ataque(p);                                                    
                    } else {
                        if (canMove()) {
                            anadir_movimiento(objetivo.x, objetivo.y);
                        }
                    }
                }
                break;
            case PARADO:
                if (mastersPlayer != null) {
                    for (ElementoEspecial e : p.getPlayerByRace(RaceNameEnum.MAESTROS.getName()).elementos_especiales) {
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
        if (statusEffectCollection.existe_estado(StatusEffectName.EROSION)) {
            disminuir_vida(p, Reloj.TIME_REGULAR_SPEED * delta * vida_max / 5);
        }
        statusEffectCollection.comportamiento(delta);
    }
}
