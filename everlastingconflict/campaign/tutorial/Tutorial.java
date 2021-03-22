/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.ventanas.MapEnum;
import everlastingconflict.ventanas.VentanaCombate;
import org.newdawn.slick.Color;

import java.util.List;

/**
 *
 * @author Elías
 */
public abstract class Tutorial extends Partida {

    public List<Paso> pasos;

    public abstract void iniciar_pasos();

    @Override
    public void initElements() {
        this.map = MapEnum.SMALL;
        VentanaCombate.initWatches();
    }

    public static Partida createTutorialByRace(String race) {
        if(RaceEnum.FENIX.getName().equals(race)) {
            return new FenixTutorial();
        } else if(RaceEnum.CLARK.getName().equals(race)) {
            return new ClarkTutorial();
        } else if(RaceEnum.ETERNIUM.getName().equals(race)) {
            return new EterniumTutorial();
        } else if(RaceEnum.GUARDIANES.getName().equals(race)) {
            return new GuardianesTutorial();
        } else {
            return new MaestrosTutorial();
        }
    }

    public Tutorial(String r1, String r2) {
        iniciar_pasos();
        Jugador j1 = new Jugador("Jugador", r1, 1, false);
        j1.color = Color.green;
        j1.isMainPlayer = true;
        j1.team = 1;
        players.add(j1);
        Jugador j2 = new Jugador("IA", r2, 2, false);
        j2.color = Color.red;
        j2.team = 2;
        players.add(j2);
    }
}
