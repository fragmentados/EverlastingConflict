/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.elementos.implementacion.Bestia;
import everlastingconflict.elementos.implementacion.Bestias;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Clark;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.ventanas.WindowCombat;
import everlastingconflict.ventanas.WindowMain;
import org.newdawn.slick.Input;

import java.util.ArrayList;

import static everlastingconflict.ventanas.WindowCombat.*;

/**
 * @author Elías
 */
public class ClarkTutorial extends Tutorial {

    @Override
    public final void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("Los Clark son una raza muy peculiar. No poseen edificios a parte de el Primarca y su " +
                "forma de crear unidades es única. No las crean en el sentido ordinario de la palabra. Poseen " +
                "unidades básicas capaces de fusionarse para crear unidades más avanzadas"));
        pasos.add(new Paso("Veamos las unidades básicas con más detenimiento:") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.get(0).seleccionar();
            }
        });
        pasos.add(new Paso("El depredador es la unidad básica especializada en el combate cuerpo a cuerpo.") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.get(0).deseleccionar();
                p.getMainPlayer().unidades.get(1).seleccionar();
            }
        });
        pasos.add(new Paso("El devorador es la unidad básica especializada en el combate a distancia.") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.get(1).deseleccionar();
                p.getMainPlayer().unidades.get(2).seleccionar();
            }
        });
        pasos.add(new Paso("El cazador es la unidad básica sin capacidad ofensiva con poderosas habilidades.") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.get(2).deseleccionar();
            }
        });
        pasos.add(new Paso("Cuando dos unidades básicas se fusionan, se destruyen para crear una unidad que posee las" +
                " características de las dos unidades que se usaron para crearla.") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().edificios.get(0).seleccionar();
                WindowCombat.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("En el Primarca tienes una ayuda con la que se pueden ver las combinaciones posibles y las" +
                " unidades resultado de esas fusiones. Prueba a activarla", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return getMainPlayer().edificios.get(0).mostrarAyudaFusion;
            }

            @Override
            public void efecto(Partida p) {
                WindowCombat.continuar.canBeUsed = true;
            }
        });
        pasos.add(new Paso("Como puedes ver cualquier combinación de dos unidades básicas generará una unidad más " +
                "poderosa y fusionar una unidad básica de cada tipo generará la unidad más poderosa de los clark") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().edificios.get(0).deseleccionar();
                p.getMainPlayer().unidades.get(0).seleccionar();
                p.getMainPlayer().unidades.get(1).seleccionar();
                WindowCombat.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Veamoslo con un ejemplo: Prueba a fusionar ahora el depredador y el devorador", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return !Clark.fusiones.isEmpty();
            }
        });
        pasos.add(new Paso("Como has podido observar, las dos unidades han desaparecido, y, en su lugar ha aparecido " +
                "una nueva unidad más poderosa. El Moldeador es una unidad capaz de cambiar entre ataque cuerpo a " +
                "cuerpo y ataque a distancia."));
        pasos.add(new Paso("La siguiente peculiaridad de la raza Clark es su forma de obtener recursos. Los Clark no " +
                "necesitan materiales para construir grandes armas o vehículos, sólo necesitan masa y para " +
                "conseguirla deben acabar con la fauna local.") {
            @Override
            public void efecto(Partida p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 0);
                WindowCombat.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Aquí tenemos un grupo de bestias. Éstas en concreto son las denominadas Alphas, las " +
                "bestias más inofensivas con lo que tu recién creado Moldeador podrá encargarse de ellas sin problema" +
                ". Ordénale que acabe con esos seres inferiores.") {
            @Override
            public boolean comprobacion(Partida p) {
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

            @Override
            public void efecto(Partida p) {
                WindowCombat.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Como puedes ver, cada vez que un Alpha muere, tus recursos aumentan. En una partida " +
                "normal de Everlasting Conflict, hay varios tipos de bestias por todo el mapa y cada una da un número" +
                " distinto de recursos. Sin embargo, a mayor recompensa, mayor será la capacidad ofensiva de la " +
                "bestia.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return p.bestias.get(0).muerte;
            }
        });
        pasos.add(new Paso("Ahora que todas las bestias han sido destruidas pasará un tiempo hasta que esas bestias " +
                "reaparezcan en el mapa y puedas volver a matarlas. Como norma general, no conviene esperar hasta que" +
                " reaparezca un grupo de bestias. Es mejor buscar otro grupo para obtener recursos de forma continua" +
                ".") {
            @Override
            public void efecto(Partida p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 400);
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Despedazador")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        pasos.add(new Paso("Los Clark tienen tres subfacciones: Los Despedazadores, especializados en la ofensiva " +
                "cuya unidad particular puede cegar unidades enemigas para reducir su visión al mínimo") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Regurgitador")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        pasos.add(new Paso("Los Regurgitadores, cuya unidad particular tiene una gran capacidad de ataque a distancia" +
                " y puede entrar en modo de ataque definitivo que, al terminar, destruye a la propia unidad") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.stream().filter(e -> e.nombre.equals("Rumiante")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        pasos.add(new Paso("Los Rumiantes, cuya unidad particular puede convertir bestias al bando de los Clark"));
        pasos.add(new Paso("Ahora ya tienes los conocimientos básicos para llevar a tu ejército Clark a la victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        WindowCombat.WORLD_SIZE_X = map.getWidth();
        WindowCombat.WORLD_SIZE_Y = map.getHeight();
        WindowCombat.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_WIDTH;
        WindowCombat.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_HEIGHT;
        players.get(0).x_inicial = 200;
        players.get(0).y_inicial = 200;
        players.get(0).initElements(this);
        bestias.add(new Bestias("Grupo1", 600, 200));
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
