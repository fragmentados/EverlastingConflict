package everlastingconflict.victory;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;

public class LeaderVictoryCondition implements VictoryCondition {

    @Override
    public boolean isDefeated(Partida partida, Jugador jugador) {
        return partida.getAlliesFromPlayer(jugador).stream().anyMatch(j -> j.isLeader && j.isDefeated());
    }
}
