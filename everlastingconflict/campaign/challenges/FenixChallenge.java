package everlastingconflict.campaign.challenges;

import everlastingconflict.elements.impl.Recurso;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;

import java.util.ArrayList;

public class FenixChallenge extends Challenge {
    public FenixChallenge() {
        super(RaceEnum.FENIX, RaceEnum.GUARDIANES);
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.unidades = new ArrayList<>();
        jugador.edificios = new ArrayList<>();
        jugador.unidades.add(new Unidad(jugador, "Cuervo", 200, 200));
        jugador.unidades.add(new Unidad(jugador, "Halcón", 300, 200));
        jugador.unidades.add(new Unidad(jugador, "Halcón", 400, 200));
        Recurso civiles = new Recurso("Civiles", 600, 400);
        civiles.vida = Recurso.vida_civiles;
        jugador.lista_recursos.add(civiles);
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Patrulla", 600, 600));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Patrulla", 650, 600));
        for (Unidad unidad : this.players.get(1).unidades) {
            unidad.movil = unidad.hostil = true;
        }
        for (Unidad unidad : jugador.unidades) {
            unidad.iniciarbotones(this);
        }
    }
}
