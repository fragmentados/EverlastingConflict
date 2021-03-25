package everlastingconflict.victory;

import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;

public class LeaderVictoryCondition implements VictoryCondition {

    @Override
    public boolean isDefeated(Game game, Jugador jugador) {
        return game.getAlliesFromPlayer(jugador).stream().anyMatch(j -> j.isLeader && j.isDefeated());
    }
}
