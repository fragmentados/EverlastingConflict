/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.razas.SubRaceEnum;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * @author Elías
 */
public abstract class AI extends Jugador {

    public int npushear;

    public AI(String n, RaceEnum r, SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super(n, r, subRaceEnum, t, isLeader, isJuggernaut);
    }

    public static AI crearAI(RaceEnum r, SubRaceEnum subRaceEnum, Integer t, String dificultad, boolean isLeader, boolean isJuggernaut) {
        if (RaceEnum.CLARK.equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIClarkFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.ETERNIUM.equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIEterniumFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.FENIX.equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                    return new AIFenixNormal(subRaceEnum, t, isLeader, isJuggernaut);
                case "Difícil":
                    return new AIFenixFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.GUARDIANES.equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIGuardianesFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        } else if (RaceEnum.MAESTROS.equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIMaestrosFacil(subRaceEnum, t, isLeader, isJuggernaut);
            }
        }
        return null;
    }

    public void pushear(Partida p) {
        List<Unidad> militares = cantidad_militar();
        if (militares.size() >= npushear) {
            for (Unidad u : militares) {
                if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
                    List<Jugador> enemigos = p.enemyPlayers(this);
                    if (!enemigos.isEmpty()) {
                        Jugador enemigo = enemigos.get(0);
                        u.atacarmover(p, enemigo.x_inicial, enemigo.y_inicial);
                    }
                }
            }
            if (npushear * 2 <= 50) {
                npushear *= 2;
            }
        }
    }

    @Override
    public void initElements(Partida p) {
        super.initElements(p);
    }

    @Override
    public void comportamiento_unidades(Partida p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
        pushear(p);
        this.decisiones_unidades(p);
    }

    public abstract void decisiones_unidades(Partida p);

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);
        this.decisiones_edificios(p);
    }

    public abstract void decisiones_edificios(Partida p);

    @Override
    public void avisar_ataque(Partida p, ElementoAtacante atacante) {
        List<Unidad> militares = cantidad_militar();
        for (Unidad u : militares) {
            if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
                u.atacarmover(p, atacante.x, atacante.y);
            }
        }
    }
}
