package everlastingconflict.victory;

import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;

public interface VictoryCondition {

    static VictoryCondition getFromName(String name) {
        switch (name) {
            case "Jugador Lider":
                return new LeaderVictoryCondition();
        }
        return new AnhilationVictoryCondition();
    }

    boolean isDefeated(Game game, Jugador jugador);
}
