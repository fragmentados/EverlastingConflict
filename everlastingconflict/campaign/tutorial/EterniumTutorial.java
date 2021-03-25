/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Recurso;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.watches.RelojEternium;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;


public class EterniumTutorial extends GuidedGame {

    @Override
    public final void initSteps() {
        steps.add(new GameStep("Los Eternium son una raza dominada por el tiempo. En la parte superior de la pantalla" +
                " " +
                "puedes ver un reloj con cuatro esferas. Esas esferas representan las distintas fases por las que " +
                "pasa el reloj.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(0).seleccionar();
            }
        });
        steps.add(new GameStep("En la primera fase, las capacidades ofensivas y defensivas de las unidades y de los " +
                "edificios Eternium se encuentran al 75%") {
            @Override
            public void effect(Game p) {
                WindowCombat.eterniumWatch().ndivision = 2;
            }
        });
        steps.add(new GameStep("En la segunda fase, se encuentran al 100%") {
            @Override
            public void effect(Game p) {
                WindowCombat.eterniumWatch().ndivision = 3;
            }
        });
        steps.add(new GameStep("En la tercera fase, alcanzan su máximo ya que se encuentran al 150%") {
            @Override
            public void effect(Game p) {
                WindowCombat.eterniumWatch().ndivision = 4;
                WindowCombat.eterniumWatch().temporalStop();
            }
        });
        steps.add(new GameStep("La cuarta fase es la más peligrosa ya que, en ella, las unidades Eternium son " +
                "incapaces " +
                "de moverse y de atacar. La única ventaja de esta fase, es que las capacidades defensivas aumentan " +
                "hasta el 500% de su valor base.") {
            @Override
            public void effect(Game p) {
                WindowCombat.eterniumWatch().ndivision = 1;
                WindowCombat.eterniumWatch().temporalRelease();
            }
        });
        steps.add(new GameStep("El sistema de recolección de recursos Eternium sigue el mismo patrón de alta " +
                "inversión, " +
                "alto beneficio. La recolección de hierro y transformación del mismo en energía sigue tres pasos.") {
            @Override
            public void effect(Game p) {
                getMainPlayer().unidades.get(0).seleccionar();
                WindowMain.combatWindow.movimiento_pantalla(200, 0);
            }
        });
        steps.add(new GameStep("Primero, el hierro se extrae de vetas mediante refinerías. Cualquier unidad militar " +
                "Eternium puede construir refinerías sobre una veta de hierro. Prueba a hacerlo ahora", false) {
            @Override
            public boolean check(Game p) {
                return getMainPlayer().edificios.stream().map(e -> e.nombre).anyMatch(s -> "Refinería".equals(s));
            }

            @Override
            public void effect(Game p) {
                getMainPlayer().unidades.get(0).deseleccionar();
                getMainPlayer().edificios.get(0).seleccionar();
                WindowMain.combatWindow.movimiento_pantalla(0, 0);
            }
        });
        steps.add(new GameStep("En segundo lugar, el hierro debe ser transformado en la cámara de asimilación. " +
                "Construye " +
                "una cámara de asimilación utilizando la sede en cualquier lugar dentro del área permitida.", false) {
            @Override
            public void effect(Game p) {
                getMainPlayer().edificios.get(0).seleccionar();
            }

            @Override
            public boolean check(Game p) {
                return getMainPlayer().edificios.stream().map(e -> e.nombre).anyMatch(s -> "Cámara de asimilación".equals(s));
            }
        });
        steps.add(new GameStep("El último paso para la conversión requiere de tecnología de la que no disponemos en " +
                "el " +
                "campo de batalla con lo que se debe transportar a una de las naves que orbitan el planeta. Construye" +
                " un teletransportador para terminar el sistema.", false) {
            @Override
            public boolean check(Game p) {
                return getMainPlayer().edificios.stream().map(e -> e.nombre).anyMatch(s -> "Teletransportador".equals(s));
            }
        });
        steps.add(new GameStep("Como puedes comprobar, a partir de ahora, las refinerías empezarán a generar recursos" +
                " de " +
                "manera continua.Cada nueva refinería que crees empezará a generar recursos de la misma forma. Pero " +
                "debes recordar esto, la cámara de asimilación y el teletransportador son los dos edificio " +
                "primordiales ya que, si alguno es destruido, todas tus refinerías dejarán de funcionar hasta que se " +
                "reconstruya.") {
            @Override
            public void effect(Game p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 400);
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Erradicador")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Eternium tienen tres subfacciones: Los Erradicadores, especializados en la " +
                "ofensiva " +
                "cuya unidad particular no es afectada por la cuarta fase del reloj Eternium") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Protector")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Protectores, cuya unidad particular no es afectada por la cuarta fase del " +
                "Eternium y " +
                "atrae proyectiles enemigos en esta fase") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().edificios.stream().filter(e -> e.nombre.equals("Altar de los ancestros")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Eruditos, especializados en la tecnología. No disponen de una unidad propia pero " +
                "sí " +
                "dos tecnologías propias desarrolladas en el altar de los ancestros : Aprendizaje económico (Reduce " +
                "el coste de los adeptos) y Control temporal (Mejora las características de las unidades en las " +
                "tres primeras fases del reloj)"));
        steps.add(new GameStep("Enhorabuena, has completado con éxito el tutorial Eternium. Ya posees los " +
                "conocimientos " +
                "básicos para llevar a tu ejército a la victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.subRace = SubRaceEnum.ERUDITOS;
        recursos.add(new Recurso("Hierro", 600, 200));
        WindowCombat.createWatch(new RelojEternium(getMainPlayer()));
        jugador.unidades.add(new Unidad(jugador, "Erradicador", 800, 600));
        jugador.unidades.add(new Unidad(jugador, "Protector", 900, 600));
        jugador.edificios.add(new Edificio("Altar de los ancestros", 1000, 600));
        jugador.unidades.get(1).iniciarbotones(this);
        jugador.unidades.get(2).iniciarbotones(this);
        jugador.edificios.get(1).iniciarbotones(this);
    }

    public EterniumTutorial() {
        super(RaceEnum.ETERNIUM, RaceEnum.CLARK);
    }

}
