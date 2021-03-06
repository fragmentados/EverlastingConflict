package everlastingconflict.victory;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;

public interface VictoryCondition {

    static VictoryCondition getFromName(String name) {
        switch (name) {
            case "Jugador Lider":
                return new LeaderVictoryCondition();
        }
        return new AnhilationVictoryCondition();
    }

    boolean isDefeated(Partida partida, Jugador jugador);
}
