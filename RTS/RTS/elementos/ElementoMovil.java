/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.elementos;

import RTS.elementos.implementacion.Edificio;
import RTS.estados.Estado;
import RTS.gestion.Partida;
import RTS.mapas.MapaCampo;
import RTS.movimientos.Mover;
import RTS.movimientos.Movimiento;
import RTS.movimientos.Recolectar;
import java.awt.geom.Point2D;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

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
        estado = "Parado";
        movimiento = null;
        objetivo = null;
    }

    public boolean puede_mover() {
        if (!movil) {
            return false;
        }
        if (estados.existe_estado(Estado.nombre_stun)) {
            return false;
        }
        if (estados.existe_estado(Estado.nombre_snare)) {
            return false;
        }
        if (estados.existe_estado(Estado.nombre_meditacion)) {
            return false;
        }
        if (estado.equals("Emergiendo")) {
            return false;
        }
        return true;
    }

    public void anadir_recoleccion(Partida partida, float x,float y){
        movimiento = new Recolectar(partida, this, x, y);
    }
    
    public void anadir_movimiento(float x, float y) {
        if (!MapaCampo.mayus) {
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
        if (puede_mover()) {            
            estado = "Mover";
            anadir_movimiento(x, y);
        }
    }

    public void atacarmover(Partida p, float x, float y) {
        if (movil && !hostil) {
            mover(p, x, y);
        } else {
            if (movil && hostil) {
                estado = "AtacarMover";
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
                if (this.estado.equals("Mover") || this.estado.equals("AtacarMover")) {
                    parar();
                } else {
                    movimiento = null;
                }
            }
        }
    }

    @Override
    public void construir(Partida p, Edificio edificio, float x, float y) {

    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
        if (movimiento != null) {
            movimiento.dibujar_fin_movimiento(g);
        }
    }

}
