/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.movimientos;

import RTS.elementos.ElementoMovil;
import RTS.gestion.Partida;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Elías
 */
public class Mover extends Movimiento {

    public Mover(ElementoMovil unidad, float x, float y) {
        this.unidad = unidad;
        puntos = new ArrayList<>();
        puntos.add(new Point2D.Float(x, y));
    }

    @Override
    public boolean resolucion(Partida p, int delta) {
        return comprobar_coordenadas(p, delta);
    }
}
