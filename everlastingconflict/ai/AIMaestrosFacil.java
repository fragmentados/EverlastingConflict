package everlastingconflict.ai;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;

public class AIMaestrosFacil extends AI {

    public AIMaestrosFacil(Integer t, boolean isLeader) {
        super("AIMaestros", RaceEnum.MAESTROS.getName(), t, isLeader);
    }

    @Override
    public final void initElements(Partida p) {
        super.initElements(p);
        npushear = 5;
    }

    @Override
    public void decisiones_unidades(Partida p) {
        for (Unidad u : unidades) {
            switch (u.nombre) {
                /*case "Activador":
                    comportamientoActivador(p, u);
                    break;*/
            }
        }
    }

    @Override
    public void decisiones_edificios(Partida p) {
        for (Edificio e : edificios) {
            switch (e.nombre) {
                /*case "Ayuntamiento":
                    comportamientoAyuntamiento(p, e);
                    break;*/
            }
        }
    }

}
