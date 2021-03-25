package everlastingconflict.ai;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.SubRaceEnum;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import static everlastingconflict.races.enums.RaceEnum.GUARDIANES;
import static everlastingconflict.watches.Reloj.TIME_REGULAR_SPEED;

public class AIGuardianesChallenge extends Jugador {

    float pushDelay = 0;

    public AIGuardianesChallenge() {
        super("AIGuardianesChallenge", GUARDIANES, SubRaceEnum.POLICIA, 2, false, false);
        this.color = Color.red;
    }

    @Override
    public void comportamiento_unidades(Game p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
        if (pushDelay >= 10) {
            pushear(p, 400, 10);
        } else {
            pushDelay += TIME_REGULAR_SPEED * delta;
        }
    }

    public void pushear(Game p, float x, float y) {
        for (Unidad u : unidades) {
            if (u.behaviour.equals(BehaviourEnum.PARADO)) {
                u.atacarmover(p, x, y);
            }
        }
    }
}
