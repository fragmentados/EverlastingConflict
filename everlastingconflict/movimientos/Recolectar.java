/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.movimientos;

import everlastingconflict.gestion.Partida;
import everlastingconflict.elementos.ElementoMovil;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Elías
 */
public class Recolectar extends Movimiento {

    public Partida partida;

    public Recolectar(Partida p, ElementoMovil unidad, float x, float y) {
        this.unidad = unidad;
        this.partida = p;
        puntos = new ArrayList<>();
        puntos.add(new Point2D.Float(x, y));
    }

    @Override
    public boolean resolucion(Partida p, int delta) {
        return comprobar_coordenadas(p, delta);        
    }
}
