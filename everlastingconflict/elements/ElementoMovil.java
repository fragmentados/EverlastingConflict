/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.movement.Mover;
import everlastingconflict.movement.Movimiento;
import everlastingconflict.movement.Recolectar;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.awt.geom.Point2D;


public class ElementoMovil extends ElementoAtacante {

    public Movimiento movimiento = null;
    public boolean movil = true;
    public float x_atmov, y_atmov;
    public float velocidad;
    public Animation izquierda, derecha, arriba, abajo;

    @Override
    public void destruir(Game p, ElementoAtacante atacante) {

    }

    public void parar() {
        behaviour = BehaviourEnum.PARADO;
        movimiento = null;
        objetivo = null;
    }

    public boolean canMove(Game game) {
        Jugador ally = game.getPlayerFromElement(this);
        return movil && statusCollection.allowsMove() && BehaviourEnum.allowsMove(behaviour)
                && (ally == null || !RaceEnum.ETERNIUM.equals(ally.raza)
                || WindowCombat.eterniumWatch().ndivision != 4 || nombre.equals("Protector") || nombre.equals(
                        "Erradicador"));
    }

    public void anadir_recoleccion(Game game, float x, float y) {
        movimiento = new Recolectar(game, this, x, y);
    }

    public void anadir_movimiento(float x, float y) {
        if (!WindowCombat.mayus) {
            movimiento = new Mover(this, x, y);
        } else {
            if (movimiento != null) {
                movimiento.puntos.add(new Point2D.Float(x, y));
            } else {
                movimiento = new Mover(this, x, y);
            }
        }
    }

    public void mover(Game p, float x, float y) {
        if (canMove(p)) {
            behaviour = BehaviourEnum.MOVER;
            anadir_movimiento(x, y);
        }
    }

    public void atacarmover(Game p, float x, float y) {
        if (movil && !hostil) {
            mover(p, x, y);
        } else {
            if (movil && hostil) {
                behaviour = BehaviourEnum.ATACAR_MOVER;
                x_atmov = x;
                y_atmov = y;
            }
        }
    }

    @Override
    public void comportamiento(Game p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        if (movimiento != null) {
            if (movimiento.resolucion(p, delta)) {
                if (this.behaviour.equals(BehaviourEnum.MOVER) || this.behaviour.equals(BehaviourEnum.ATACAR_MOVER)) {
                    parar();
                } else {
                    movimiento = null;
                }
            }
        }
    }

    @Override
    public boolean construir(Game p, Edificio edificio, float x, float y) {
        this.disableBuildingButtons();
        return true;
    }

    @Override
    public void render(Game p, Color c, Input input, Graphics g) {
        super.render(p, c, input, g);
        if (movimiento != null && p.getMainPlayer().equals(p.getPlayerFromElement(this))) {
            movimiento.dibujar_fin_movimiento(g);
        }
    }

    public boolean isMoving() {
        return movimiento != null;
    }
}
