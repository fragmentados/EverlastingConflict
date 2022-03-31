/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.ElementoAtacante;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import org.newdawn.slick.Graphics;

import java.util.List;

import static everlastingconflict.gestion.Game.NUM_AI_PLAYERS;


public abstract class AI extends Jugador {

    public int npushear;

    public AI(String n, RaceEnum r, SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super(n + NUM_AI_PLAYERS++, r, subRaceEnum, t, isLeader, isJuggernaut);
    }

    public static AI crearAI(RaceEnum r, SubRaceEnum subRaceEnum, Integer t, String dificultad, boolean isLeader,
                             boolean isJuggernaut) {
        if (RaceEnum.CLARK.equals(r)) {
            switch (dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIClarkFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.ETERNIUM.equals(r)) {
            switch (dificultad) {
                case "Fácil":
                    return new AIEterniumFacil(subRaceEnum, t, isLeader, isJuggernaut);
                case "Normal":
                case "Difícil":
                    return new AIEterniumNormal(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.FENIX.equals(r)) {
            switch (dificultad) {
                case "Fácil":
                    return new AIFenixFacil(subRaceEnum, t, isLeader, isJuggernaut);
                case "Normal":
                case "Difícil":
                    return new AIFenixNormal(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.GUARDIANES.equals(r)) {
            switch (dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIGuardianesFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.MAESTROS.equals(r)) {
            switch (dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIMaestrosFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        }
        return null;
    }

    public void pushear(Game p) {
        List<Jugador> enemigos = p.enemyPlayers(this);
        if (!enemigos.isEmpty()) {
            this.pushear(p, enemigos.get(0).x_inicial, enemigos.get(0).y_inicial);
        }
    }

    public void pushear(Game p, float x, float y) {
        List<Unidad> militares = cantidad_militar();
        if (militares.size() >= npushear) {
            for (Unidad u : militares) {
                if (u.behaviour.equals(BehaviourEnum.PARADO)) {
                    u.atacarmover(p, x, y);
                }
            }
            if (npushear * 2 <= 50) {
                npushear *= 2;
            }
        }
    }

    @Override
    public void initElements(Game p) {
        super.initElements(p);
    }

    @Override
    public void comportamiento_unidades(Game p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
        pushear(p);
        this.decisiones_unidades(p, delta);
    }

    public abstract void decisiones_unidades(Game p, int delta);

    @Override
    public void comportamiento_edificios(Game p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);
        this.decisiones_edificios(p, delta);
    }

    public abstract void decisiones_edificios(Game p, int delta);

    @Override
    public void avisar_ataque(Game p, ElementoAtacante atacante) {
        List<Unidad> militares = cantidad_militar();
        for (Unidad u : militares) {
            if (u.behaviour.equals(BehaviourEnum.PARADO)) {
                u.atacarmover(p, atacante.x, atacante.y);
            }
        }
    }
}
