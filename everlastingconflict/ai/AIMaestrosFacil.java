package everlastingconflict.ai;

import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;

public class AIMaestrosFacil extends AI {

    public AIMaestrosFacil(SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super("AIMaestros", RaceEnum.MAESTROS, subRaceEnum, t, isLeader, isJuggernaut);
    }

    @Override
    public final void initElements(Game p) {
        super.initElements(p);
        npushear = 5;
    }

    @Override
    public void decisiones_unidades(Game p, int delta) {
        for (Unidad u : unidades) {
            switch (u.nombre) {
                /*case "Activador":
                    comportamientoActivador(p, u);
                    break;*/
            }
        }
    }

    @Override
    public void decisiones_edificios(Game p, int delta) {
        for (Edificio e : edificios) {
            switch (e.nombre) {
                /*case "Ayuntamiento":
                    comportamientoAyuntamiento(p, e);
                    break;*/
            }
        }
    }

}
