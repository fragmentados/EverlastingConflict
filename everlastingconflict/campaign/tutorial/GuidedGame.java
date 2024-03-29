/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Vision;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.MapEnum;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.List;

import static everlastingconflict.windows.WindowCombat.*;


public abstract class GuidedGame extends Game {

    public List<GameStep> steps = new ArrayList<>();
    public List<Sound> sounds = new ArrayList<>();

    public abstract void initSteps();

    @Override
    public void initElements() {
        this.map = MapEnum.SMALL;
        WindowCombat.initWatches();
        WindowCombat.WORLD_SIZE_WIDTH = map.getWidth();
        WindowCombat.WORLD_SIZE_HEIGHT = map.getHeight();
        WindowCombat.offsetMaxX = WORLD_SIZE_WIDTH - VIEWPORT_SIZE_WIDTH;
        WindowCombat.offsetMaxY = WORLD_SIZE_HEIGHT - VIEWPORT_SIZE_HEIGHT;
    }

    public static Game createTutorialByRace(String race) {
        if (RaceEnum.FENIX.getName().equals(race)) {
            return new FenixTutorial();
        } else if (RaceEnum.CLARK.getName().equals(race)) {
            return new ClarkTutorial();
        } else if (RaceEnum.ETERNIUM.getName().equals(race)) {
            return new EterniumTutorial();
        } else if (RaceEnum.GUARDIANES.getName().equals(race)) {
            return new GuardianesTutorial();
        } else if (RaceEnum.ALIANZA.getName().equals(race)) {
            return new AlianzaTutorial();
        } else {
            return new MaestrosTutorial();
        }
    }

    public GuidedGame(RaceEnum r1, RaceEnum r2) {
        initSteps();
        for (int i = 0; i < steps.size(); i++) {
            try {
                this.sounds.add(new Sound("media/Sonidos/Tutoriales/" + r1.getName() + i + ".wav"));
            } catch (SlickException e) { }
        }
        sounds.get(0).playAt(1.0f, 10.0f, 0f, 0f, 0f);
        Jugador j1 = new Jugador("Jugador", r1, 1, false);
        j1.color = Color.green;
        j1.isMainPlayer = true;
        j1.team = 1;
        j1.x_inicial = 200;
        j1.y_inicial = 200;
        players.add(j1);
        Jugador j2 = new Jugador("IA", r2, 2, false);
        j2.color = Color.red;
        j2.team = 2;
        players.add(j2);
        j1.initElements(this);
        initElements();
        j2.isDefeated = player -> this.steps.isEmpty();
        j1.visiones.add(new Vision(j1.x_inicial, j1.y_inicial, 2000, 1000));
    }
}
