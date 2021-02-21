/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.movimientos.Mover;
import everlastingconflict.movimientos.Movimiento;
import everlastingconflict.movimientos.Recolectar;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.awt.geom.Point2D;

/**
 *
 * @author Elías
 */
public class ElementoMovil extends ElementoAtacante {

    public Movimiento movimiento = null;
    public boolean movil = true;
    public float x_atmov, y_atmov;
    public float velocidad;
    public Animation izquierda, derecha, arriba, abajo;

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {

    }

    public void parar() {
        statusBehaviour = StatusBehaviour.PARADO;
        movimiento = null;
        objetivo = null;
    }

    public boolean canMove() {
        return movil && statusEffectCollection.allowsMove() && StatusBehaviour.allowsMove(statusBehaviour);
    }

    public void anadir_recoleccion(Partida partida, float x,float y){
        movimiento = new Recolectar(partida, this, x, y);
    }
    
    public void anadir_movimiento(float x, float y) {
        if (!VentanaCombate.mayus) {
            movimiento = new Mover(this, x, y);
        } else {
            if (movimiento != null) {
                movimiento.puntos.add(new Point2D.Float(x, y));
            } else {
                movimiento = new Mover(this, x, y);
            }
        }
    }

    public void mover(Partida p, float x, float y) {        
        if (canMove()) {
            statusBehaviour = StatusBehaviour.MOVER;
            anadir_movimiento(x, y);
        }
    }

    public void atacarmover(Partida p, float x, float y) {
        if (movil && !hostil) {
            mover(p, x, y);
        } else {
            if (movil && hostil) {
                statusBehaviour = StatusBehaviour.ATACAR_MOVER;
                x_atmov = x;
                y_atmov = y;
            }
        }
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        if (movimiento != null) {
            if (movimiento.resolucion(p, delta)) {
                if (this.statusBehaviour.equals(StatusBehaviour.MOVER) || this.statusBehaviour.equals(StatusBehaviour.ATACAR_MOVER)) {
                    parar();
                } else {
                    movimiento = null;
                }
            }
        }
    }

    @Override
    public void construir(Partida p, Edificio edificio, float x, float y) {
        this.disableBuildingButtons();
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
        if (movimiento != null) {
            movimiento.dibujar_fin_movimiento(g);
        }
    }

    public boolean isMoving() {
        return movimiento != null;
    }
}
