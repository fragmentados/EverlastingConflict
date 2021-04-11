/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.elements.impl.Bestias;
import everlastingconflict.elements.impl.Habilidad;
import everlastingconflict.elements.impl.Manipulador;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.status.StatusNameEnum;
import everlastingconflict.watches.RelojMaestros;
import everlastingconflict.windows.WindowCombat;

import java.util.ArrayList;

import static everlastingconflict.elements.impl.Manipulador.ATTRIBUTES_BUTTON;
import static everlastingconflict.elements.impl.Manipulador.SKILL_BUTTON;


public class MaestrosTutorial extends GuidedGame {

    @Override
    public void initSteps() {
        steps.add(new GameStep("La raza de los Maestros del Universo debe ser sin duda la más peculiar de Everlasting" +
                " Conflict."));
        steps.add(new GameStep("La forma de jugar de los jugadores Maestros no se parece a la de los RTS " +
                "tradicionales sino que asemeja a la de un RPG.") {
            @Override
            public void effect(Game p) {
                WindowCombat.elementHighlighted = p.getMainPlayer().unidades.get(0);
            }
        });
        steps.add(new GameStep("La unidad que ves a la izquierda de la pantalla es el Manipulador, el eje principal " +
                "de la raza Maestros del Universo."));
        steps.add(new GameStep("Empiezas la partida con un Manipulador y, si en cualquier momento tu Manipulador " +
                "muere, pierdes la partida automáticamente."));
        steps.add(new GameStep("Esto puede parecer un handicap muy fuerte frente al resto de razas que pueden " +
                "producir gran cantidad de unidades pero como, pronto verás, el Manipulador puede utilizar poderosas " +
                "habilidades que compensan esta debilidad.") {
            @Override
            public void effect(Game p) {
                WindowCombat.elementHighlighted = p.bestias.get(0).contenido.get(1);
            }
        });
        steps.add(new GameStep("Intentémoslo. Selecciona la habilidad Deflagración y luego haz objetivo al Alpha que " +
                "acaba de aparecer y mira lo que ocurre.", false) {
            @Override
            public boolean check(Game p) {
                return p.bestias.get(0).contenido.size() == 2;
            }

            @Override
            public void effect(Game p) {
                WindowCombat.elementHighlighted = null;
            }
        });
        steps.add(new GameStep("Como puedes ver, la barra de experiencia en la parte superior de la pantalla ha " +
                "aumentado. Cuando llegue al final, tu Manipulador subirá de nivel y sus capacidades aumentarán."));
        steps.add(new GameStep("Generalmente, sólo obtienes experiencia destruyendo unidades y edificios. Sin " +
                "embargo, en el nivel uno es especial el Manipulador puede utilizar la habilidad Meditación para " +
                "obtener experiencia."));
        steps.add(new GameStep("Prueba a utilizar la habilidad Meditación ahora.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().unidades.get(0).statusCollection.containsStatus(StatusNameEnum.MEDITACION);
            }
        });
        steps.add(new GameStep("Como podrás comprobar, tu Manipulador es ahora incapaz de moverse, pero su " +
                "experiencia aumenta continuamente hasta subir al nivel dos. Espera hasta que ocurra esto.", false) {
            @Override
            public boolean check(Game p) {
                return ((Manipulador) p.getMainPlayer().unidades.get(0)).nivel == 2;
            }
        });
        steps.add(new GameStep("Enhorabuena! Tu Manipulador ha subido de nivel. Sigues disponiendo de la habilidad " +
                "Meditación pero no aumentará la experiencia sino que mejorará tu regeneración de maná. Además, han " +
                "aparecido dos botones nuevos: Habilidades y Atributos. Prueba a pulsar Habilidades ahora.", false) {
            @Override
            public boolean check(Game p) {
                return !((Manipulador) p.getMainPlayer().unidades.get(0)).enhancementButtons.isEmpty();
            }

            @Override
            public void effect(Game p) {
                ((Manipulador) p.getMainPlayer().unidades.get(0)).enhancementButtons.removeIf(b -> "Deflagración".equals(b.elemento_nombre));
            }
        });
        steps.add(new GameStep("Ahora podrás elegir hasta dos habildiades para que tu Manipulador las aprenda. Prueba" +
                " a hacerlo ahora.", false) {
            @Override
            public boolean check(Game p) {
                return ((Manipulador) p.getMainPlayer().unidades.get(0)).enhancementButtons.isEmpty();
            }
        });
        steps.add(new GameStep("El botón atributos funciona más o menos igual salvo que puedes elegir hasta cinco " +
                "veces y puedes repetir varias veces la misma elección.") {
            @Override
            public void effect(Game p) {
                WindowCombat.elementHighlighted = WindowCombat.masterWatch();
            }
        });
        steps.add(new GameStep("El último aspecto relevante de la raza Maestros del Universo es la división entre día" +
                " y noche. El reloj que aparece a la izquierda de la vida del Manipulador determina si el mapa se " +
                "encuentra en día o en noche."));
        steps.add(new GameStep("El estado del mapa determina que habilidades podrá usar el Manipulador: hay " +
                "habilidades que sólo se pueden usar de día, como deflagración, otras que solo se pueden usar de " +
                "noche, y otras que se pueden usar en cualquier momento.") {
            @Override
            public void effect(Game p) {
                WindowCombat.elementHighlighted = null;
            }
        });
        steps.add(new GameStep("Los Manipuladores tienen tres subfacciones: Los Invocadores, especializados en las " +
                "habilidades que invocan unidades y cuyo manipulador tiene una mayor cantidad de maná inicial"));
        steps.add(new GameStep("Los Hechiceros, especializados en las habilidades que infligen daño y cuyo " +
                "manipulador tiene una mayor cantidad de poder mágico inicial"));
        steps.add(new GameStep("Los Luchadores, especializados en el combate directo y cuyo manipulador tiene un " +
                "mayor ataque y una mayor velocidad de ataque iniciales"));
        steps.add(new GameStep("Ahora ya tienes los conocimientos básicos para llevar a tu ejército Maestros del " +
                "Universo a la victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        getMainPlayer().unidades.get(0).botones = new ArrayList<>();
        getMainPlayer().unidades.get(0).botones.add(new BotonManipulador(new Habilidad("Deflagración"),
                RelojMaestros.nombre_dia));
        getMainPlayer().unidades.get(0).botones.add(new BotonManipulador(new Habilidad("Meditar")));
        getMainPlayer().unidades.get(0).botones.removeIf(b -> b.equals(SKILL_BUTTON) || b.equals(ATTRIBUTES_BUTTON));
        SKILL_BUTTON.remainingClicks = 0;
        ATTRIBUTES_BUTTON.remainingClicks = 0;
        getMainPlayer().unidades.get(0).initButtonKeys();
        bestias = new ArrayList<>();
        bestias.add(new Bestias("Alphas", 500, 200));
        WindowCombat.createWatch(new RelojMaestros(getMainPlayer()));
    }

    public MaestrosTutorial() {
        super(RaceEnum.MAESTROS, RaceEnum.ETERNIUM);
    }

}
