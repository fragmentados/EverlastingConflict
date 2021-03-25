/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.movement;

import everlastingconflict.elements.ElementoMovil;
import everlastingconflict.gestion.Game;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Recolectar extends Movimiento {

    public Game game;

    public Recolectar(Game p, ElementoMovil unidad, float x, float y) {
        this.unidad = unidad;
        this.game = p;
        puntos = new ArrayList<>();
        puntos.add(new Point2D.Float(x, y));
    }

    @Override
    public boolean resolucion(Game p, int delta) {
        return comprobar_coordenadas(p, delta);        
    }
}
