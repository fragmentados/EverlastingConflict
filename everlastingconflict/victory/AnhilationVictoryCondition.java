package everlastingconflict.victory;

import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;

public class AnhilationVictoryCondition implements VictoryCondition {

    @Override
    public boolean isDefeated(Game game, Jugador jugador) {
        return jugador.isDefeated();
    }

}
