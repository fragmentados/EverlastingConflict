package everlastingconflict.ai;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Taller;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceNameEnum;
import org.newdawn.slick.Graphics;

public class AIGuardianesFacil extends AI {



    public AIGuardianesFacil(Integer t, boolean isLeader) {
        super("AIGuardianes", RaceNameEnum.GUARDIANES.getName(), t, isLeader);
    }

    @Override
    public final void initElements(Partida p) {
        super.initElements(p);
        npushear = 5;
    }

    @Override
    public void comportamiento_unidades(Partida p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
        pushear(p);
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

    private void comportamientoArtillero(Partida p, Unidad u) {
        this.unidades
                .stream().filter(patrulla -> "Patrulla".equals(patrulla.nombre) && !patrulla.movil
                        && this.unidades
                .stream().filter(artilleros -> "Artillero".equals(artilleros.nombre))
                .noneMatch(activador -> patrulla.equals(activador.objetivo)))
                .findFirst().ifPresent(unidad -> u.embarcar(unidad));
    }

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);
        for (Edificio e : edificios) {
            if (e.activo) {
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

    private void comportamientoAcademia(Partida p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            Unidad u = new Unidad("Artillero");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.createUnit(p, this, u);
            }
        }
    }

    private void comportamientoTaller(Partida p, Taller t) {
        boolean isAnexusUnderConstruction = t.anexos.stream()
                .anyMatch(a -> StatusBehaviour.CONSTRUYENDOSE.equals(a.statusBehaviour));
        if (!isAnexusUnderConstruction) {
            t.construir_anexo(p);
        }
        if (t.cola_construccion.isEmpty()) {
            Unidad u = new Unidad("Patrulla");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                t.createUnit(p, this, u);
            }
        }
    }

    public void comportamientoActivador(Partida p, Unidad u) {
        if (!StatusBehaviour.EMBARCANDO.equals(u.statusBehaviour)) {
            edificios.stream().filter(b -> !b.activo).findFirst().ifPresent(b -> u.embarcar(b));
        }
    }

    public void comportamientoAyuntamiento(Partida p, Edificio e) {
        boolean allBuildingsEnabled = edificios.stream().allMatch(b -> b.activo);
        if (!allBuildingsEnabled && e.cola_construccion.isEmpty()) {
            //Reclutamiento de Unidades
            Unidad u = new Unidad("Activador");
            if (this.poblacion_max - this.poblacion >= u.coste_poblacion) {
                e.createUnit(p, this, u);
            }
        }
    }
}
