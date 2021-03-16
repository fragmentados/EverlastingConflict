/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Tecnologia;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;

/**
 * @author Elías
 */
public class AIFenixNormal extends AIFenixFacil {

    public AIFenixNormal(Integer t, boolean isLeader) {
        super(t, isLeader);
    }

    @Override
    public void initElements(Partida p) {
        super.initElements(p);
        npushear = 5;
    }

    @Override
    public void decisiones_edificios(Partida p) {
        for (Edificio e : edificios) {
            if (!StatusBehaviour.CONSTRUYENDOSE.equals(e.statusBehaviour)) {
                switch (e.nombre) {
                    case "Cuartel Fénix":
                        comportamiento_cuartel(p, e);
                        break;
                    case "Academia":
                        comportamiento_academia(p, e);
                        break;
                    case "Centro tecnológico":
                        comportamiento_centro_tecnologico(p, e);
                        break;
                    case "Sede":
                        comportamiento_sede(p, e);
                        break;

                }
            }
        }
    }

    public void comportamiento_centro_tecnologico(Partida p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            if (tecnologias.stream().noneMatch(t -> "Aumentar límite 1".equals(t.nombre))) {
                Tecnologia t = new Tecnologia("Aumentar límite 1");
                if (e.researchTechnology(p, this, t)) {
                    return;
                }
            } else if (tecnologias.stream().noneMatch(t -> "Aumentar límite 2".equals(t.nombre))) {
                Tecnologia t = new Tecnologia("Aumentar límite 2");
                if (e.researchTechnology(p, this, t)) {
                    return;
                }
            } else if (tecnologias.stream().noneMatch(t -> "Aumentar límite 3".equals(t.nombre))) {
                Tecnologia t = new Tecnologia("Aumentar límite 3");
                if (e.researchTechnology(p, this, t)) {
                    return;
                }
            } else {
                for (BotonComplejo b : e.botones) {
                    Tecnologia t = new Tecnologia(b.elemento_nombre);
                    if (e.researchTechnology(p, this, t)) {
                        break;
                    }
                }
            }
        }
    }

}