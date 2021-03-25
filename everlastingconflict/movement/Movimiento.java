/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.movement;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.ElementoMovil;
import everlastingconflict.gestion.Game;
import everlastingconflict.status.StatusName;
import everlastingconflict.watches.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


//Implementación pendiente de colisiones en el movimiento
public abstract class Movimiento {

    public ElementoMovil unidad;
    public List<Point2D.Float> puntos = new ArrayList<>();
    public static final boolean desviaciones = false;

    public boolean comprobar_coordenadas(Game game, int delta) {
        if (!puntos.isEmpty()) {
            float x_final = puntos.get(0).x;
            float y_final = puntos.get(0).y;
            float movimiento = 100 * Reloj.TIME_REGULAR_SPEED * unidad.velocidad * delta;
            if (unidad.statusCollection.existe_estado(StatusName.RALENTIZACION)) {
                movimiento *= (100 - unidad.statusCollection.obtener_estado(StatusName.RALENTIZACION).value) / 100;
            }
            float distancia_x = Math.abs(x_final - unidad.x);
            float distancia_y = Math.abs(y_final - unidad.y);
            float distancia_total = distancia_x + distancia_y;
            float movimientox = movimiento * (distancia_x / distancia_total);
            if (movimientox > movimiento) {
                movimientox = movimiento;
            }
            float movimientoy = movimiento * (distancia_y / distancia_total);
            if (movimientoy > movimiento) {
                movimientoy = movimiento;
            }
            boolean horizontal = (movimientox > movimientoy);
            unidad.sprite.update(delta);
            if (unidad.x < x_final) {
                if (unidad.x + movimientox >= x_final) {
                    unidad.x = x_final;
                } else {
                    unidad.x += movimientox;
                }
                if (horizontal) {
                    if (unidad.derecha != null) {
                        unidad.sprite = unidad.derecha;
                    }
                }
            } else {
                if (unidad.x > x_final) {
                    if (unidad.x - movimientox <= x_final) {
                        unidad.x = x_final;
                    } else {
                        unidad.x -= movimientox;
                    }
                }
                if (horizontal) {
                    if (unidad.izquierda != null) {
                        unidad.sprite = unidad.izquierda;
                    }
                }
            }
            if (unidad.y < y_final) {
                if (unidad.y + movimientoy >= y_final) {
                    unidad.y = y_final;
                } else {
                    unidad.y += movimientoy;
                }
                if (!horizontal) {
                    if (unidad.abajo != null) {
                        unidad.sprite = unidad.abajo;
                    }
                }
            } else {
                if (unidad.y > y_final) {
                    if (unidad.y - movimientoy <= y_final) {
                        unidad.y = y_final;
                    } else {
                        unidad.y -= movimientoy;
                    }
                }
                if (!horizontal) {
                    if (unidad.arriba != null) {
                        unidad.sprite = unidad.arriba;
                    }
                }
            }
            if (unidad.x == x_final && unidad.y == y_final) {
                puntos.remove(0);
            }
        }
        return puntos.isEmpty();
    }

    public void dibujar_fin_movimiento(Graphics g) {
        if (unidad.behaviour.equals(BehaviourEnum.ATACAR_MOVER)) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.green);
        }
        for (Point2D.Float p : puntos) {
            float x_final = p.x;
            float y_final = p.y;
            g.drawOval(x_final - 10, y_final - 10, 20, 20);
            g.drawLine(x_final, y_final, x_final, y_final);
        }
        g.setColor(Color.white);
    }

    public abstract boolean resolucion(Game p, int delta);

}
