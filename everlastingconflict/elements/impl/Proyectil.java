/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements.impl;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.ElementoAtacante;
import everlastingconflict.elements.ElementoEstado;
import everlastingconflict.elements.ElementoMovil;
import everlastingconflict.elements.ElementoVulnerable;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.status.StatusCollection;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Proyectil extends ElementoMovil {

    public ElementoAtacante origen;
    private static final int ATTRACTION_RADIUS = 200;

    public final void iniciar_datos(ElementoAtacante origen) {
        try {
            this.animation = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_derecha.png")}, 300,
                    true);
            this.izquierda = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_izquierda.png")},
                    300, true);
            this.derecha = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_derecha.png")}, 300,
                    true);
            this.arriba = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_arriba.png")}, 300,
                    true);
            this.abajo = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_abajo.png")}, 300,
                    true);
        } catch (SlickException ex) {
            Logger.getLogger(Proyectil.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (origen.nombre) {
            case "":
                velocidad = 0;
                break;
        }
    }

    public Proyectil(ElementoAtacante origen, ElementoVulnerable objetivo, int ataque) {
        this.nombre = "Proyectil";
        this.objetivo = objetivo;
        this.origen = origen;
        this.ataque = ataque;
        statusCollection = new StatusCollection();
        behaviour = BehaviourEnum.PROYECTIL;
        this.x = this.origen.x;
        this.y = this.origen.y;
        this.velocidad = 3.0f;
        iniciar_datos(origen);
        this.anchura = this.animation.getWidth();
        this.altura = this.animation.getHeight();
    }

    @Override
    public void destruir(Game p, ElementoAtacante atacante) {
        p.proyectiles.remove(this);
        this.behaviour = BehaviourEnum.DESTRUIDO;
    }

    @Override
    public void comportamiento(Game p, Graphics g, int delta) {
        if (!BehaviourEnum.DESTRUIDO.equals(behaviour)) {
            if (objetivo instanceof ElementoEstado && ((ElementoEstado) objetivo).behaviour.equals(BehaviourEnum.DESTRUIDO)) {
                this.destruir(p, null);
            } else {
                if (movimiento != null) {
                    movimiento.resolucion(p, delta);
                    checkTortuceAttraction(p);
                }
                if (objetivo.hitbox(this.x, this.y)) {
                    if (origen.healer && !origen.hostil) {
                        boolean isObjectiveFullHealed = origen.heal(ataque, objetivo);
                        if (isObjectiveFullHealed) {
                            ((ElementoMovil) origen).parar();
                        }
                    } else {
                        boolean isObjectiveDestroyed = origen.dealDamage(p, "Físico", ataque, objetivo);
                        if (isObjectiveDestroyed && origen instanceof ElementoMovil) {
                            ((ElementoMovil) origen).parar();
                        }
                    }
                    this.destruir(p, null);
                } else {
                    anadir_movimiento(objetivo.x, objetivo.y);
                }
            }
        }
    }

    public void checkTortuceAttraction(Game p) {
        if (!objetivo.isProyectileAttraction) {
            List<Jugador> enemies = p.getEnemyPlayersFromElement(this.origen);
            for (Jugador enemy : enemies) {
                List<Unidad> proyectileAttractors =
                        enemy.unidades.stream().filter(u -> u.isProyectileAttraction).collect(Collectors.toList());
                for (Unidad attractor : proyectileAttractors) {
                    if (Math.abs(this.x - attractor.x) <= ATTRACTION_RADIUS && Math.abs(this.y - attractor.y) <= ATTRACTION_RADIUS) {
                        this.objetivo = attractor;
                        this.movimiento = null;
                        return;
                    }
                }
            }
        }
    }

}
