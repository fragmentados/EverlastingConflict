package everlastingconflict.victory;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;

public class AnhilationVictoryCondition implements VictoryCondition {

    @Override
    public boolean isDefeated(Partida partida, Jugador jugador) {
        return jugador.isDefeated();
    }

}
