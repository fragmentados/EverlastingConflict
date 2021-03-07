package everlastingconflict.ai;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceNameEnum;
import org.newdawn.slick.Graphics;

public class AIMaestrosFacil extends AI {

    public AIMaestrosFacil(Integer t, boolean isLeader) {
        super("AIMaestros", RaceNameEnum.MAESTROS.getName(), t, isLeader);
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
                /*case "Activador":
                    comportamientoActivador(p, u);
                    break;*/
            }
        }
    }

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);
        for (Edificio e : edificios) {
            switch (e.nombre) {
                /*case "Ayuntamiento":
                    comportamientoAyuntamiento(p, e);
                    break;*/
            }
        }
    }

}
