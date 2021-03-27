/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Tecnologia;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.enums.SubRaceEnum;


public class AIFenixNormal extends AIFenixFacil {

    public AIFenixNormal(SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super(subRaceEnum, t, isLeader, isJuggernaut);
    }

    @Override
    public void initElements(Game p) {
        super.initElements(p);
        npushear = 5;
    }

    @Override
    public void decisiones_unidades(Game p, int delta) {
        for (Unidad u : unidades) {
            switch (u.nombre) {
                case "Recolector":
                    comportamiento_recolector(p, u);
                    break;
                case "Constructor":
                    comportamiento_constructor(p, u);
                    break;
                default:
                    //Unidad Militar
                    //comportamiento_militar(p, u);
            }
        }
    }

    @Override
    public void decisiones_edificios(Game p, int delta) {
        for (Edificio e : edificios) {
            if (!BehaviourEnum.CONSTRUYENDOSE.equals(e.behaviour)) {
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

    public void comportamiento_centro_tecnologico(Game p, Edificio e) {
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
