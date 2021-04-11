/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.elements.impl.Bestia;
import everlastingconflict.elements.impl.Bestias;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Clark;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.Input;

import static everlastingconflict.windows.WindowCombat.playerX;
import static everlastingconflict.windows.WindowCombat.playerY;


public class ClarkTutorial extends GuidedGame {

    @Override
    public final void initSteps() {
        steps.add(new GameStep("Los Clark son una raza muy peculiar. No poseen edificios a parte de el Primarca y su " +
                "forma de crear unidades es única. No las crean en el sentido ordinario de la palabra. Poseen " +
                "unidades básicas capaces de fusionarse para crear unidades más avanzadas"));
        steps.add(new GameStep("Veamos las unidades básicas con más detenimiento:") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(0).seleccionar();
            }
        });
        steps.add(new GameStep("El depredador es la unidad básica especializada en el combate cuerpo a cuerpo.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(0).deseleccionar();
                p.getMainPlayer().unidades.get(1).seleccionar();
            }
        });
        steps.add(new GameStep("El devorador es la unidad básica especializada en el combate a distancia.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(1).deseleccionar();
                p.getMainPlayer().unidades.get(2).seleccionar();
            }
        });
        steps.add(new GameStep("El cazador es la unidad básica sin capacidad ofensiva con poderosas habilidades.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(2).deseleccionar();
            }
        });
        steps.add(new GameStep("Cuando dos unidades básicas se fusionan, se destruyen para crear una unidad que posee" +
                " las" +
                " características de las dos unidades que se usaron para crearla.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().edificios.get(0).seleccionar();
            }
        });
        steps.add(new GameStep("En el Primarca tienes una ayuda con la que se pueden ver las combinaciones posibles y" +
                " las" +
                " unidades resultado de esas fusiones. Prueba a activarla", false) {
            @Override
            public boolean check(Game p) {
                return getMainPlayer().edificios.get(0).mostrarAyudaFusion;
            }
        });
        steps.add(new GameStep("Como puedes ver cualquier combinación de dos unidades básicas generará una unidad más" +
                " " +
                "poderosa y fusionar una unidad básica de cada tipo generará la unidad más poderosa de los clark") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().edificios.get(0).deseleccionar();
                p.getMainPlayer().unidades.get(0).seleccionar();
                p.getMainPlayer().unidades.get(1).seleccionar();
            }
        });
        steps.add(new GameStep("Veamoslo con un ejemplo: Prueba a fusionar ahora el depredador y el devorador", false) {
            @Override
            public boolean check(Game p) {
                return !Clark.fusiones.isEmpty();
            }
        });
        steps.add(new GameStep("Como has podido observar, las dos unidades han desaparecido, y, en su lugar ha " +
                "aparecido " +
                "una nueva unidad más poderosa. El Moldeador es una unidad capaz de cambiar entre ataque cuerpo a " +
                "cuerpo y ataque a distancia."));
        steps.add(new GameStep("La siguiente peculiaridad de la raza Clark es su forma de obtener recursos. Los Clark" +
                " no " +
                "necesitan materiales para construir grandes armas o vehículos, sólo necesitan masa y para " +
                "conseguirla deben acabar con la fauna local.") {
            @Override
            public void effect(Game p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 0);
            }
        });
        steps.add(new GameStep("Aquí tenemos un grupo de bestias. Éstas en concreto son las denominadas Alphas, las " +
                "bestias más inofensivas con lo que tu recién creado Moldeador podrá encargarse de ellas sin problema" +
                ". Ordénale que acabe con esos seres inferiores.") {
            @Override
            public boolean check(Game p) {
                Input input = RTS.canvas.getContainer().getInput();
                for (Bestias be : p.bestias) {
                    for (Bestia b : be.contenido) {
                        if (b.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        steps.add(new GameStep("Como puedes ver, cada vez que un Alpha muere, tus recursos aumentan. En una partida " +
                "normal de Everlasting Conflict, hay varios tipos de bestias por todo el mapa y cada una da un número" +
                " distinto de recursos. Sin embargo, a mayor recompensa, mayor será la capacidad ofensiva de la " +
                "bestia.", false) {
            @Override
            public boolean check(Game p) {
                return p.bestias.get(0).muerte;
            }
        });
        steps.add(new GameStep("Ahora que todas las bestias han sido destruidas pasará un tiempo hasta que esas " +
                "bestias " +
                "reaparezcan en el mapa y puedas volver a matarlas. Como norma general, no conviene esperar hasta que" +
                " reaparezca un grupo de bestias. Es mejor buscar otro grupo para obtener recursos de forma continua" +
                ".") {
            @Override
            public void effect(Game p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 400);
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Despedazador")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Clark tienen tres subfacciones: Los Despedazadores, especializados en la ofensiva" +
                " " +
                "cuya unidad particular puede cegar unidades enemigas para reducir su visión al mínimo") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Regurgitador")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Regurgitadores, cuya unidad particular tiene una gran capacidad de ataque a " +
                "distancia" +
                " y puede entrar en modo de ataque definitivo que, al terminar, destruye a la propia unidad") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.stream().filter(e -> e.nombre.equals("Rumiante")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Rumiantes, cuya unidad particular puede convertir bestias al bando de los Clark"));
        steps.add(new GameStep("Ahora ya tienes los conocimientos básicos para llevar a tu ejército Clark a la " +
                "victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        bestias.add(new Bestias("Alphas", 600, 200));
        jugador.unidades.add(new Unidad(jugador, "Despedazador", 800, 600));
        jugador.unidades.add(new Unidad(jugador, "Regurgitador", 900, 600));
        jugador.unidades.add(new Unidad(jugador, "Rumiante", 1000, 600));
        jugador.unidades.get(3).iniciarbotones(this);
        jugador.unidades.get(4).iniciarbotones(this);
        jugador.unidades.get(5).iniciarbotones(this);
    }

    public ClarkTutorial() {
        super(RaceEnum.CLARK, RaceEnum.ETERNIUM);
    }
}
