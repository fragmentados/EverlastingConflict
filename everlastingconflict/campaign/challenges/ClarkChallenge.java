package everlastingconflict.campaign.challenges;

import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.watches.RelojEternium;
import everlastingconflict.windows.WindowCombat;

import java.util.ArrayList;

public class ClarkChallenge extends Challenge {

    public ClarkChallenge() {
        super(RaceEnum.CLARK, RaceEnum.ETERNIUM);
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.unidades = new ArrayList<>();
        jugador.edificios = new ArrayList<>();
        Unidad despedazador = new Unidad(jugador, "Despedazador", 200, 200);
        despedazador.ataque = 0;
        despedazador.hostil = false;
        jugador.unidades.add(despedazador);
        jugador.unidades.add(new Unidad(jugador, "Devorador", 300, 200));
        jugador.unidades.add(new Unidad(jugador, "Devorador", 380, 200));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Ancestro", 600, 600));
        RelojEternium reloj = new RelojEternium();
        reloj.contador_reloj = reloj.fin_segundo_cuarto;
        reloj.ndivision = 3;
        WindowCombat.createWatch(reloj);
        for (Unidad unidad : jugador.unidades) {
            unidad.iniciarbotones(this);
        }
    }
}
