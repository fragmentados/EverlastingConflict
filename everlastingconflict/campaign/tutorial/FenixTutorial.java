/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Recurso;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.Input;

import static everlastingconflict.windows.WindowCombat.playerX;
import static everlastingconflict.windows.WindowCombat.playerY;


public class FenixTutorial extends GuidedGame {

    @Override
    public final void initSteps() {
        steps.add(new GameStep("La característica principal de los Fénix es que sus recursos no representan algo " +
                "material" +
                " sino que representan el porcentaje de aceptación de la población local."));
        steps.add(new GameStep("Para aumentar tus recursos como Fénix debes mandar tus recolectores a las ciudades " +
                "civiles para que se esparza tu ideología y la ciudad se te alíe.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(1).seleccionar();
            }
        });
        steps.add(new GameStep("Prueba a hacerlo ahora haciendo click derecho sobre la ciudad con el recolector " +
                "seleccionado") {
            @Override
            public boolean check(Game p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.recursos.get(0).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        steps.add(new GameStep("Como puedes ver, mientras el recolector captura la ciudad, no puede moverse por lo " +
                "que " +
                "debes evaluar con cuidado si es seguro o no capturar una ciudad antes de intentar hacerlo.", false) {
            @Override
            public boolean check(Game p) {
                return !p.getMainPlayer().lista_recursos.isEmpty() && p.getMainPlayer().lista_recursos.get(0).vida == p.getMainPlayer().lista_recursos.get(0).vida_max;
            }
        });
        steps.add(new GameStep("Otra característica de los recursos Fénix es que, a diferencia de los de otras razas," +
                " " +
                "nunca disminuyen. A medida que aumentan tus recursos, se van desbloqueando nuevas unidades y " +
                "edificios pero, al crearlos, no pierdes recursos."));
        steps.add(new GameStep("Su última peculiaridad es a la hora de crear unidades. En vez de especificar cada " +
                "unidad " +
                "que se quiere producir, el cuartel Fénix crea un tipo de unidad constantemente. El jugador puede " +
                "seleccionar que unidad crea cada cuartel.") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.get(1).deseleccionar();
                p.getMainPlayer().edificios.get(1).seleccionar();
                WindowMain.combatWindow.movimiento_pantalla(0, 400);
            }
        });
        steps.add(new GameStep("Como ejemplo, hemos creado ya un Cuartel y hemos desbloqueado las dos primeras " +
                "unidades " +
                "Fénix: el Tigre y el Halcón. Por ahora, el Cuartel no produce ninguna unidad. Haz click en el boton " +
                "de Tigre para que empiece a producir esta unidad. Debes saber que, en una partida real, además de " +
                "construir el cuartel deberías desbloquear las unidades en la academia antes de poder entrenarlas.",
                false) {
            @Override
            public boolean check(Game p) {
                return "Tigre".equals(p.getMainPlayer().edificios.get(1).unidad_actual);
            }
        });
        steps.add(new GameStep("A partir de este momento, el Cuartel creará Tigres hasta que se le indique que cree " +
                "otra " +
                "unidad o hasta que se llegue a la poblacion máxima. Prueba a ordenarle que cree Halcones ahora.",
                false) {
            @Override
            public boolean check(Game p) {
                return "Halcón".equals(p.getMainPlayer().edificios.get(1).unidad_actual);
            }
        });
        steps.add(new GameStep("Por último, prueba a ordenar al Cuartel que pare de crear unidades.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().edificios.get(1).behaviour == BehaviourEnum.DETENIDO;
            }

            @Override
            public void effect(Game p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 400);
                p.getMainPlayer().edificios.get(1).deseleccionar();
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Oso")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Fénix tienen tres subfacciones: Los Osos, especializados en el combate cuerpo a " +
                "cuerpo y la ofensiva") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Tortuga")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Las Tortugas, especializados en la defensa cuya unidad puede atraer proyectiles " +
                "enemigos " +
                "automáticamente") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Cuervo")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Cuervos, especializados en la tecnología cuya unidad tiene habilidades que pueden" +
                " " +
                "cambiar el resultado de una batalla"));
        steps.add(new GameStep("Enhorabuena, has completado con éxito el tutorial Fénix. Ya posees los conocimientos " +
                "básicos para llevar a tu ejército a la victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.edificios.add(new Edificio(jugador, "Cuartel Fénix", 200, 600));
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
