/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.ventanas.WindowCombat;
import everlastingconflict.ventanas.WindowMain;
import org.newdawn.slick.Input;

import java.util.ArrayList;

import static everlastingconflict.ventanas.WindowCombat.*;

/**
 * @author Elías
 */
public class FenixTutorial extends Tutorial {

    @Override
    public final void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("La característica principal de los Fénix es que sus recursos no representan algo material" +
                " sino que representan el porcentaje de aceptación de la población local."));
        pasos.add(new Paso("Para aumentar tus recursos como Fénix debes mandar tus recolectores a las ciudades " +
                "civiles para que se esparza tu ideología y la ciudad se te alíe.") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.get(1).seleccionar();
                WindowCombat.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Prueba a hacerlo ahora haciendo click derecho sobre la ciudad con el recolector " +
                "seleccionado") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.recursos.get(0).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                WindowCombat.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Como puedes ver, mientras el recolector captura la ciudad, no puede moverse por lo que " +
                "debes evaluar con cuidado si es seguro o no capturar una ciudad antes de intentar hacerlo.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return !p.getMainPlayer().lista_recursos.isEmpty() && p.getMainPlayer().lista_recursos.get(0).vida == p.getMainPlayer().lista_recursos.get(0).vida_max;
            }
        });
        pasos.add(new Paso("Otra característica de los recursos Fénix es que, a diferencia de los de otras razas, " +
                "nunca disminuyen. A medida que aumentan tus recursos, se van desbloqueando nuevas unidades y " +
                "edificios pero, al crearlos, no pierdes recursos."));
        pasos.add(new Paso("Su última peculiaridad es a la hora de crear unidades. En vez de especificar cada unidad " +
                "que se quiere producir, el cuartel Fénix crea un tipo de unidad constantemente. El jugador puede " +
                "seleccionar que unidad crea cada cuartel.") {
            @Override
            public void efecto(Partida p) {
                WindowCombat.continuar.canBeUsed = false;
                p.getMainPlayer().unidades.get(1).deseleccionar();
                p.getMainPlayer().edificios.get(1).seleccionar();
                WindowMain.combatWindow.movimiento_pantalla(0, 400);
            }
        });
        pasos.add(new Paso("Como ejemplo, hemos creado ya un Cuartel y hemos desbloqueado las dos primeras unidades " +
                "Fénix: el Tigre y el Halcón. Por ahora, el Cuartel no produce ninguna unidad. Haz click en el boton " +
                "de Tigre para que empiece a producir esta unidad. Debes saber que, en una partida real, además de " +
                "construir el cuartel deberías desbloquear las unidades en la academia antes de poder entrenarlas.",
                false) {
            @Override
            public void efecto(Partida p) {
                WindowCombat.continuar.canBeUsed = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                return "Tigre".equals(p.getMainPlayer().edificios.get(1).unidad_actual);
            }
        });
        pasos.add(new Paso("A partir de este momento, el Cuartel creará Tigres hasta que se le indique que cree otra " +
                "unidad o hasta que se llegue a la poblacion máxima. Prueba a ordenarle que cree Halcones ahora.",
                false) {
            @Override
            public void efecto(Partida p) {
                WindowCombat.continuar.canBeUsed = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                return "Halcón".equals(p.getMainPlayer().edificios.get(1).unidad_actual);
            }
        });
        pasos.add(new Paso("Por último, prueba a ordenar al Cuartel que pare de crear unidades.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return p.getMainPlayer().edificios.get(1).statusBehaviour == StatusBehaviour.DETENIDO;
            }

            @Override
            public void efecto(Partida p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 400);
                p.getMainPlayer().edificios.get(1).deseleccionar();
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Oso")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        pasos.add(new Paso("Los Fénix tienen tres subfacciones: Los Osos, especializados en el combate cuerpo a " +
                "cuerpo y la ofensiva") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Tortuga")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        pasos.add(new Paso("Las Tortugas, especializados en la defensa cuya unidad puede atraer proyectiles enemigos " +
                "automáticamente") {
            @Override
            public void efecto(Partida p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Cuervo")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        pasos.add(new Paso("Los Cuervos, especializados en la tecnología cuya unidad tiene habilidades que pueden " +
                "cambiar el resultado de una batalla"));
        pasos.add(new Paso("Enhorabuena, has completado con éxito el tutorial Fénix. Ya posees los conocimientos " +
                "básicos para llevar a tu ejército a la victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        WindowCombat.WORLD_SIZE_X = map.getWidth();
        WindowCombat.WORLD_SIZE_Y = map.getHeight();
        WindowCombat.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_WIDTH;
        WindowCombat.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_HEIGHT;
        jugador.x_inicial = 200;
        jugador.y_inicial = 200;
        jugador.initElements(this);
        jugador.edificios.add(new Edificio("Cuartel Fénix", 200, 600));
        jugador.edificios.get(1).iniciarbotones(this);
        jugador.edificios.get(1).botones.get(1).canBeUsed = true;
        jugador.edificios.get(1).botones.get(2).canBeUsed = true;
        jugador.unidades.add(new Unidad(jugador, "Oso", 800, 600));
        jugador.unidades.add(new Unidad(jugador, "Tortuga", 850, 600));
        jugador.unidades.add(new Unidad(jugador, "Cuervo", 900, 600));
        jugador.unidades.get(2).iniciarbotones(this);
        jugador.unidades.get(3).iniciarbotones(this);
        jugador.unidades.get(4).iniciarbotones(this);
        recursos.add(new Recurso("Civiles", 600, 200));
    }

    public FenixTutorial() {
        super(RaceEnum.FENIX, RaceEnum.ETERNIUM);
    }

}
