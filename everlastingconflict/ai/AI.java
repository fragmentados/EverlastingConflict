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
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 * @author Elías
 */
public abstract class AI extends Jugador {

    public int npushear;

    public AI(String n, String r, Integer t, boolean isLeader) {
        super(n, r, t, isLeader);
    }

    public static AI crearAI(String r, Integer t, String dificultad, boolean isLeader) {
        if (RaceEnum.CLARK.getName().equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIClarkFacil(t, isLeader);
            }
        } else if (RaceEnum.ETERNIUM.getName().equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIEterniumFacil(t, isLeader);
            }
        } else if (RaceEnum.FENIX.getName().equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                    return new AIFenixNormal(t, isLeader);
                case "Difícil":
                    return new AIFenixFacil(t, isLeader);
            }
        } else if (RaceEnum.GUARDIANES.getName().equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIGuardianesFacil(t, isLeader);
            }
        } else if (RaceEnum.MAESTROS.getName().equals(r)) {
            switch(dificultad) {
                case "Fácil":
                case "Normal":
                case "Difícil":
                    return new AIMaestrosFacil(t, isLeader);
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
