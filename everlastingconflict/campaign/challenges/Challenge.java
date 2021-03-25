package everlastingconflict.campaign.challenges;

import everlastingconflict.campaign.tutorial.GameStep;
import everlastingconflict.campaign.tutorial.GuidedGame;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.enums.RaceEnum;

import static everlastingconflict.windows.WindowCombat.continuar;

public abstract class Challenge extends GuidedGame {

    @Override
    public void initSteps() {
        // The only step needed is to kill all enemy units
        steps.add(new GameStep() {
            @Override
            public boolean check(Game p) {
                return p.players.get(1).unidades.isEmpty();
            }
        });
    }

    public static Game createChallengeByRace(RaceEnum race) {
        switch (race) {
            case CLARK:
                return new ClarkChallenge();
            case FENIX:
                return new FenixChallenge();
            case ETERNIUM:
                return new EterniumChallenge();
            case MAESTROS:
                return new MaestrosChallenge();
            case GUARDIANES:
                return new GuardianesChallenge();
        }
        return null;
    }

    public Challenge(RaceEnum r1, RaceEnum r2) {
        super(r1, r2);
        continuar.canBeUsed = false;
        this.players.get(0).isDefeated = player -> player.unidades.stream().noneMatch(u -> u.hostil);
        this.players.get(1).isDefeated = player -> player.unidades.isEmpty();
    }
}
