/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.razas.SubRaceEnum;

/**
 *
 * @author Elías
 */
public class AIClarkFacil extends AI {

    public AIClarkFacil(SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super("AIClark", RaceEnum.CLARK, subRaceEnum, t, isLeader, isJuggernaut);
    }

    @Override
    public void decisiones_unidades(Partida p) {

    }

    @Override
    public void decisiones_edificios(Partida p) {

    }

}
