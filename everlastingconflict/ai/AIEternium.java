/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.elementos.implementacion.Tecnologia;
import everlastingconflict.elementos.implementacion.Unidad;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Elías
 */
// TODO Comentado funcionamiento para poder testear bien
public class AIEternium extends AI {

    public float x_transporte, y_transporte;
    public float x_asimilacion, y_asimilacion;
    public float x_altar, y_altar;

    public AIEternium() {
        super("AIEternium", "Eternium");
    }

    @Override
    public void iniciar_elementos(Partida p) {
        super.iniciar_elementos(p);
        x_asimilacion = x_inicial;
        y_asimilacion = y_inicial + 210;

        x_transporte = x_inicial + 210;
        y_transporte = y_inicial;

        x_altar = x_inicial + 210;
        y_altar = y_inicial + 210;

        npushear = 5;
    }

    @Override
    public void comportamiento_unidades(Partida p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
        pushear(p);
        for (Unidad u : unidades) {
            switch (u.nombre) {
                case "Adepto":
                    comportamiento_adepto(p, u);
                    break;
            }
        }
    }

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);

        for (Edificio e : edificios) {
            switch (e.nombre) {
                case "Mando Central":
                    comportamiento_mando(p, e);
                    break;
                case "Altar de los ancestros":
                    comportamiento_altar(p, e);
                    break;
            }
        }
    }

    public void comportamiento_adepto(Partida p, Unidad u) {
        if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
            Recurso r = p.recurso_mas_cercano(null, this.nombre, "Hierro", u.x, u.y);
            if (r != null) {
                Edificio contador = new Edificio("Refinería");
                contador.vida = 0;
                u.construir(p, contador, r.x, r.y);
            }
        }
    }

    public void comportamiento_altar(Partida p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            for (BotonComplejo b : e.botones) {
                Tecnologia t = new Tecnologia(b.elemento_nombre);
                e.investigar_tecnologia(p, this, t);
                break;
            }
        }
    }

    public void comportamiento_mando(Partida p, Edificio e) {
        //Construcción de Edificios
        if (e.edificio_construccion == null) {
            if (this.cantidad_edificio("Cámara de asimilación") == 0) {
                Edificio contador = new Edificio("Cámara de asimilación");
                contador.vida = 0;
                e.construir(p, contador, x_asimilacion, y_asimilacion);
                return;
            }
            if (this.cantidad_edificio("Teletransportador") == 0) {
                Edificio contador = new Edificio("Teletransportador");
                contador.vida = 0;
                e.construir(p, contador, x_transporte, y_transporte);
                return;
            }
            if (this.cantidad_edificio("Altar de los ancestros") == 0) {
                Edificio contador = new Edificio("Altar de los ancestros");
                contador.vida = 0;
                e.construir(p, contador, x_altar, y_altar);
                return;
            }
        }
        Edificio altar = null;
        for (Edificio ed : edificios) {
            if (e.nombre.equals("Altar de los ancestros")) {
                altar = ed;
                break;
            }
        }
        if (this.perforacion && (altar == null || !altar.cola_construccion.isEmpty())) {
            //Reclutamiento de Unidades
            Unidad u = new Unidad("Ancestro");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.crear_unidad(p, this, u);
            }
            u = new Unidad("Guerrero");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.crear_unidad(p, this, u);
            }
            u = new Unidad("Adepto");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.crear_unidad(p, this, u);
            }
        }
    }

}
