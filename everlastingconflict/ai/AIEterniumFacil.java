/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.elementos.implementacion.Tecnologia;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;

public class AIEterniumFacil extends AI {

    public float x_transporte, y_transporte;
    public float x_asimilacion, y_asimilacion;
    public float x_altar, y_altar;

    public AIEterniumFacil(Integer t, boolean isLeader) {
        super("AIEternium", "Eternium", t, isLeader);
    }

    @Override
    public void initElements(Partida p) {
        super.initElements(p);
        x_asimilacion = x_inicial;
        y_asimilacion = this.verticalOffset(y_inicial, 210);

        x_transporte = this.horizontalOffset(x_inicial, 210);
        y_transporte = y_inicial;

        x_altar = this.horizontalOffset(x_inicial, 210);
        y_altar = this.verticalOffset(y_inicial, 210);

        npushear = 5;
    }

    @Override
    public void decisiones_unidades(Partida p) {
        for (Unidad u : unidades) {
            switch (u.nombre) {
                case "Adepto":
                    comportamiento_adepto(p, u);
                    break;
            }
        }
    }

    @Override
    public void decisiones_edificios(Partida p) {
        for (Edificio e : edificios) {
            if (!StatusBehaviour.CONSTRUYENDOSE.equals(e.statusBehaviour)) {
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
    }

    public void comportamiento_adepto(Partida p, Unidad u) {
        if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
            Recurso r = p.closestResource(null, this.nombre, "Hierro", u.x, u.y);
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
                e.researchTechnology(p, this, t);
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
                e.createUnit(p, this, u);
            }
            u = new Unidad("Guerrero");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.createUnit(p, this, u);
            }
            u = new Unidad("Adepto");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.createUnit(p, this, u);
            }
        }
    }

}
