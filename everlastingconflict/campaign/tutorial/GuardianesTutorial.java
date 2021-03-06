/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Evento;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.Input;

import java.util.ArrayList;

import static everlastingconflict.windows.WindowCombat.playerX;
import static everlastingconflict.windows.WindowCombat.playerY;


public class GuardianesTutorial extends GuidedGame {

    @Override
    public void initSteps() {
        steps.add(new GameStep("Cuando los Eternium llegaron a la Tierra, la humanidad se vio dividida en dos " +
                "facciones. Los Guardianes de la Paz representan a los humanos que se aliaron con los Eternium con " +
                "tal de obtener paz y prosperidad."));
        steps.add(new GameStep("La primera característica de los Guardianes es que, a diferencia de otras razas, no " +
                "tiene que construir edificios sino que empiezan la partida con todos los edificios construidos en " +
                "una posición predeterminada."));
        steps.add(new GameStep("Sin embargo, sólo el edificio Central, el Ayuntamiento, puede ser usado nada más " +
                "empezar, el resto deben ser activados. Para ello, debemos crear una unidad especial en el " +
                "Ayuntamiento, el Activador. Prueba a hacerlo ahora.", false) {
            @Override
            public boolean check(Game p) {
                return !p.getMainPlayer().edificios.get(0).cola_construccion.isEmpty() && p.getMainPlayer().edificios.get(0).cola_construccion.get(0).nombre.equals("Activador");
            }
        });
        steps.add(new GameStep("Para activar un edificio, selecciona el Activador recién creado y pulsa el botón " +
                "derecho sobre el edificio a activar. Prueba a activar el Taller bélico ahora.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().edificios.stream().anyMatch(e -> e.nombre.equals("Taller bélico") && e.activo);
            }
        });
        steps.add(new GameStep("La siguiente característica de la raza son los vehículos: es un tipo de unidad más " +
                "potente que el resto pero que requieren de un piloto para poder moverse y atacar. Pero antes de " +
                "ello, debemos crear un anexo en el taller bélico para que guarde el vehículo.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().edificios.stream().anyMatch(e -> e.nombre.equals("Anexo"));
            }
        });
        steps.add(new GameStep("A continuación, prueba a crear una Patrulla y observa como se moviliza hacia el anexo" +
                ".", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().unidades.stream().anyMatch(u -> u.nombre.equals("Patrulla"));
            }

            @Override
            public void effect(Game p) {
                Edificio academiaPilotos = p.getMainPlayer().edificios.stream().filter(e -> e.nombre.equals("Academia" +
                        " de pilotos")).findFirst().get();
                academiaPilotos.activo = true;
                academiaPilotos.iniciarbotones(p);
                academiaPilotos.vida = academiaPilotos.vida_max;
            }

        });
        steps.add(new GameStep("Como puedes comprobar, por ahora el vehículo no se puede mover ni atacar. Eso es " +
                "porque debe ser tripulado. Para ello, debes crear un piloto en la academia de pilotos. Generalmente," +
                " deberías crear otro Activador para que la Academia pudiera ser usable, pero en este Tutorial vamos " +
                "a activar el edificio por ti. Prueba a construir un Artillero.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().unidades.stream().anyMatch(u -> u.nombre.equals("Artillero"));
            }
        });
        steps.add(new GameStep("Para embarcar un vehículo, selecciona el piloto y pulsa el botón derecho sobre el " +
                "vehículo a embarcar. Prueba a hacerlo ahora.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().unidades.stream().anyMatch(u -> u.nombre.equals("Patrulla") && u.movil && u.hostil);
            }
        });
        steps.add(new GameStep("A continuación, veamos como los Guardianes obtienen recursos. Existen tres recursos " +
                "que se muestran de izquierda a derecha en la parte de arriba de la pantalla: el primero es el que se" +
                " usa para crear todas las unidades y los edificios, el segundo es el porcentaje de felicidad de la " +
                "población, cuanto mayor sea más rápido se obtendrá el primer recurso, el último recurso representa " +
                "el nivel de amenaza. Cuanto mayor sea, más piezas del arsenal de los Guardianes se desbloquearán " +
                "pero peores eventos podrán aparecer."));
        steps.add(new GameStep("Por último, explicaremos los eventos que acabamos de mencionar. Existen dos tipos de " +
                "eventos: los positivos y los negativos. Los positivos se obtienen investigándolos en el ayuntamiento" +
                " y aumentan la felicidad de la población. Los eventos negativos aparecen cada cierto tiempo y, de no" +
                " ser evitados, disminuyen la felicidad de la población."));
        steps.add(new GameStep("Para solucionar un evento negativo se deben sacrificar unidades. Para hacerlo debes " +
                "moverlas al ayuntamiento.") {
            @Override
            public void effect(Game p) {
                getMainPlayer().eventos.activo = true;
                p.getMainPlayer().eventos.contenido = new ArrayList<>();
                p.getMainPlayer().eventos.contenido.add(new Evento(p.getMainPlayer(), "Una racha criminal asola las " +
                        "calles"));
                Unidad u = new Unidad(p.getMainPlayer(), "Patrulla", p.getMainPlayer().unidades.get(0).x - 50,
                        p.getMainPlayer().unidades.get(0).y);
                u.movil = true;
                p.getMainPlayer().unidades.add(u);
            }
        });
        steps.add(new GameStep("Veamos un ejemplo. Se ha creado una Patrulla automáticamente. Dos patrullas son las " +
                "tropas necesarias para solucionar el evento 'Una racha criminal asola las calles'. Prueba a enviar " +
                "la primera Patrulla al ayuntamiento ahora.") {
            @Override
            public boolean check(Game p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.getMainPlayer().edificios.get(0).hitbox((int) playerX + input.getMouseX(),
                        (int) playerY + input.getMouseY());
            }
        });
        steps.add(new GameStep("Muy bien. Hazlo una vez más para solucionar el evento negativo.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().eventos.contenido.isEmpty();
            }

            @Override
            public void effect(Game p) {
                WindowMain.combatWindow.movimiento_pantalla(200, 400);
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Destructor")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("Los Guardianes tienen tres subfacciones: El ejército, especializados en los tanques " +
                "cuya unidad particular es el vehículo más poderoso") {
            @Override
            public void effect(Game p) {
                p.getMainPlayer().unidades.stream().filter(u -> u.nombre.equals("Enviado celeste")).findFirst().ifPresent(u -> WindowCombat.ui.resetAndSelect(u));
            }
        });
        steps.add(new GameStep("La Iglesia, cuya unidad particular es una unidad celeste con habilidades curativos y " +
                "que se centran en las unidades del vaticano"));
        steps.add(new GameStep("La Policía, que no tienen unidad característica pero cuyos eventos negativos son más " +
                "permisivos y disponen de más eventos positivos para desbloquear"));
        steps.add(new GameStep("Enhorabuena. Ahora ya sabes todo lo que necesitas saber para llevar a tu ejército a " +
                "la victoria."));
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.subRace = SubRaceEnum.EJERCITO;
        getMainPlayer().eventos.activo = false;
        jugador.unidades.add(new Unidad(jugador, "Destructor", 800, 600));
        jugador.unidades.add(new Unidad(jugador, "Enviado celeste", 900, 600));
        jugador.unidades.get(0).iniciarbotones(this);
        jugador.unidades.get(1).iniciarbotones(this);
    }

    public GuardianesTutorial() {
        super(RaceEnum.GUARDIANES, RaceEnum.ETERNIUM);
    }
}
