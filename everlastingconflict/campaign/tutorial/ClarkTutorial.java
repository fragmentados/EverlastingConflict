/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.elementos.implementacion.Bestia;
import everlastingconflict.elementos.implementacion.Bestias;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.mapas.VentanaPrincipal;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import java.util.ArrayList;

import static everlastingconflict.mapas.VentanaCombate.*;

/**
 *
 * @author Elías
 */
public class ClarkTutorial extends Tutorial {

    @Override
    public final void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("Los Clark son una raza muy peculiar. No poseen edificios a parte de el Primarca y su forma de crear unidades es única. No las crean en el sentido ordinario de la palabra. Poseen unidades básicas capaces de fusionarse para crear unidades más avanzadas"));
        pasos.add(new Paso("Veamos las unidades básicas con más detenimiento:") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(0).seleccionar();
            }
        });
        pasos.add(new Paso("El depredador es la unidad básica especializada en el combate cuerpo a cuerpo.") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(0).deseleccionar();
                p.j1.unidades.get(1).seleccionar();
            }
        });
        pasos.add(new Paso("El devorador es la unidad básica especializada en el combate a distancia.") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(1).deseleccionar();
                p.j1.unidades.get(2).seleccionar();
            }
        });
        pasos.add(new Paso("El cazador es la unidad básica sin capacidad ofensiva con poderosas habilidades.") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(2).deseleccionar();
            }
        });
        pasos.add(new Paso("Cuando dos unidades básicas se fusionan, se destruyen para crear una unidad que posee las características de las dos unidades que se usaron para crearla.") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(0).seleccionar();
                p.j1.unidades.get(1).seleccionar();
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Veamos un ejemplo: Prueba a fusionar ahora el depredador y el devorador") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return VentanaCombate.ui.seleccion_actual.get(0).botones.get(1).presionado(VentanaCombate.playerX + input.getMouseX(), VentanaCombate.playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("Como has podido observar, las dos unidades han desaparecido, y, en su lugar ha aparecido una nueva unidad más poderosa. El Moldeador es una unidad capaz de cambiar entre ataque cuerpo a cuerpo y ataque a distancia."));
        pasos.add(new Paso("La siguiente peculiaridad de la raza Clark es su forma de obtener recursos. Los Clark no necesitan materiales para construir grandes armas o vehículos, sólo necesitan masa y para conseguirla deben acabar con la fauna local.") {
            @Override
            public void efecto(Partida p) {
                VentanaPrincipal.mapac.movimiento_pantalla(200, 0);
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Aquí tenemos un grupo de bestias. Éstas en concreto son las denominadas Alphas, las bestias más inofensivas con lo que tu recién creado Moldeador podrá encargarse de ellas sin problema. Ordénale que acabe con esos seres inferiores.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                boolean resultado = false;
                for (Bestias be : p.bestias) {
                    for (Bestia b : be.contenido) {
                        if (b.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                            return true;
                        }
                    }
                }
                return resultado;
            }

            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Como puedes ver, cada vez que un Alpha muere, tus recursos aumentan. En una partida normal de Everlasting Conflict, hay varios tipos de bestias por todo el mapa y cada una da un número distinto de recursos. Sin embargo, a mayor recompensa, mayor será la capacidad ofensiva de la bestia.") {
            @Override
            public boolean comprobacion(Partida p) {
                return p.bestias.get(0).muerte;
            }
        });
        pasos.add(new Paso("Ahora que todas las bestias han sido destruidas pasará un tiempo hasta que esas bestias reaparezcan en el mapa y puedas volver a matarlas. Como norma general, no conviene esperar hasta que reaparezca un grupo de bestias. Es mejor buscar otro grupo para obtener recursos de forma continua."));
        pasos.add(new Paso("Ahora ya tienes los conocimientos básicos para llevar a tu ejército Clark a la victoria."));
    }

    @Override
    public void initElements(int njugador) {
        VentanaCombate.WORLD_SIZE_X = map.getWidth();
        VentanaCombate.WORLD_SIZE_Y = map.getHeight();
        VentanaCombate.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
        VentanaCombate.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
        j1.x_inicial = 200;
        j1.y_inicial = 200;
        j1.initElements(this);
        bestias.add(new Bestias("Grupo1", 600, 200));
    }

    public ClarkTutorial() {
        iniciar_pasos();
        j1 = new Jugador("Prueba", "Clark");
        j2 = new Jugador("Prueba", "Eternium");
        j1.color = Color.green;
        j2.color = Color.red;
    }
}
