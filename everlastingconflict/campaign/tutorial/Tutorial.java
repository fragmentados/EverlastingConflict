/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.MapEnum;
import everlastingconflict.razas.RaceNameEnum;

import java.util.List;

/**
 *
 * @author Elías
 */
public abstract class Tutorial extends Partida {

    public List<Paso> pasos;

    public abstract void iniciar_pasos();

    public void initElements(int njugador) {
        this.map = MapEnum.SMALL;
    }

    public static Partida createTutorialByRace(String race) {
        if(RaceNameEnum.FENIX.getName().equals(race)) {
            return new FenixTutorial();
        } else if(RaceNameEnum.CLARK.getName().equals(race)) {
            return new ClarkTutorial();
        } else if(RaceNameEnum.ETERNIUM.getName().equals(race)) {
            return new EterniumTutorial();
        } else if(RaceNameEnum.GUARDIANES.getName().equals(race)) {
            return new GuardianesTutorial();
        } else {
            return new MaestrosTutorial();
        }
    }
}
