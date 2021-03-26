package everlastingconflict.elements.impl;

import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Vision;

public class TorreVision extends Recurso {

    public TorreVision(String n, float x, float y) {
        super(n, x, y);
    }

    @Override
    public void capturar(Game p, Jugador j) {
        j.lista_recursos.add(this);
        p.recursos.remove(this);
        capturador = j.nombre;
        j.visiones.add(new Vision(this.x, this.y, 1000, 0));
        this.animation.setAutoUpdate(true);
    }
}
