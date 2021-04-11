package everlastingconflict.campaign.challenges;

import everlastingconflict.ai.AIGuardianesChallenge;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;

import java.util.ArrayList;

public class GuardianesChallenge extends Challenge {
    public GuardianesChallenge() {
        super(RaceEnum.GUARDIANES, RaceEnum.FENIX);
        this.players.get(0).isDefeated = player -> player.edificios.isEmpty();
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.unidades = new ArrayList<>();
        jugador.edificios = new ArrayList<>();
        jugador.unidades.add(new Unidad(jugador, "Artillero", 200, 100));
        jugador.unidades.add(new Unidad(jugador, "Explorador", 300, 100));
        jugador.unidades.add(new Unidad(jugador, "Corredor", 400, 100));
        jugador.unidades.add(new Unidad(jugador, "Ingeniero", 500, 100));
        jugador.unidades.add(new Unidad(jugador, "Armero", 600, 100));
        jugador.unidades.add(new Unidad(jugador, "Amparador", 700, 100));
        jugador.unidades.add(new Unidad(jugador, "Oteador", 800, 100));
        jugador.eventos.desactivacion_permanente = true;
        jugador.edificios.add(new Edificio(jugador, "Torreta defensiva", 400, 200));
        for (int i = 0; i < 2; i++) {
            jugador.edificios.add(new Edificio(jugador, "Torreta defensiva", 400 + 80 * i, 280));
        }
        this.players.remove(1);
        this.players.add(new AIGuardianesChallenge());
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Fénix", 600, 600));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Fénix", 675, 600));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Fénix", 750, 600));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Halcón", 600, 700));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Halcón", 650, 700));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Halcón", 700, 700));
        this.players.get(1).unidades.add(new Unidad(this.players.get(1), "Halcón", 750, 700));
        for (Unidad u : jugador.unidades) {
            u.iniciarbotones(this);
        }
    }
}
