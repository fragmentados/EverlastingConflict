package everlastingconflict.ai;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Taller;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;

public class AIGuardianesFacil extends AI {



    public AIGuardianesFacil(SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super("AIGuardianes", RaceEnum.GUARDIANES, subRaceEnum, t, isLeader, isJuggernaut);
    }

    @Override
    public final void initElements(Game p) {
        super.initElements(p);
        npushear = 5;
    }

    @Override
    public void decisiones_unidades(Game p) {
        for (Unidad u : unidades) {
            switch (u.nombre) {
                case "Activador":
                    comportamientoActivador(p, u);
                    break;
                case "Artillero":
                    comportamientoArtillero(p, u);
                    break;
            }
        }
    }

    private void comportamientoArtillero(Game p, Unidad u) {
        this.unidades
                .stream().filter(patrulla -> "Patrulla".equals(patrulla.nombre) && !patrulla.movil
                        && this.unidades
                .stream().filter(artilleros -> "Artillero".equals(artilleros.nombre))
                .noneMatch(activador -> patrulla.equals(activador.objetivo)))
                .findFirst().ifPresent(unidad -> u.embarcar(unidad));
    }

    @Override
    public void decisiones_edificios(Game p) {
        for (Edificio e : edificios) {
            if (e.activo) {
                if (!BehaviourEnum.CONSTRUYENDOSE.equals(e.behaviour)) {
                    switch (e.nombre) {
                        case "Ayuntamiento":
                            comportamientoAyuntamiento(p, e);
                            break;
                        case "Taller bélico":
                            comportamientoTaller(p, (Taller) e);
                            break;
                        case "Academia de pilotos":
                            comportamientoAcademia(p, e);
                            break;
                    }
                }
            }
        }
    }

    private void comportamientoAcademia(Game p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            Unidad u = new Unidad(this,"Artillero");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.createUnit(p, this, u);
            }
        }
    }

    private void comportamientoTaller(Game p, Taller t) {
        boolean isAnexusUnderConstruction = t.anexos.stream()
                .anyMatch(a -> BehaviourEnum.CONSTRUYENDOSE.equals(a.behaviour));
        if (!isAnexusUnderConstruction) {
            t.construir_anexo(p);
        }
        if (t.cola_construccion.isEmpty()) {
            Unidad u = new Unidad(this,"Patrulla");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                t.createUnit(p, this, u);
            }
        }
    }

    public void comportamientoActivador(Game p, Unidad u) {
        if (!BehaviourEnum.EMBARCANDO.equals(u.behaviour)) {
            edificios.stream().filter(b -> !b.activo).findFirst().ifPresent(b -> u.embarcar(b));
        }
    }

    public void comportamientoAyuntamiento(Game p, Edificio e) {
        boolean allBuildingsEnabled = edificios.stream().allMatch(b -> b.activo);
        if (!allBuildingsEnabled && e.cola_construccion.isEmpty()) {
            //Reclutamiento de Unidades
            Unidad u = new Unidad(this,"Activador");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.createUnit(p, this, u);
            }
        }
    }
}
