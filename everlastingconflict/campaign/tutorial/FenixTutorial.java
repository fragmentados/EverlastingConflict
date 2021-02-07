/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.MapaCampo;
import static everlastingconflict.mapas.MapaCampo.VIEWPORT_SIZE_X;
import static everlastingconflict.mapas.MapaCampo.VIEWPORT_SIZE_Y;
import static everlastingconflict.mapas.MapaCampo.WORLD_SIZE_X;
import static everlastingconflict.mapas.MapaCampo.WORLD_SIZE_Y;
import static everlastingconflict.mapas.MapaCampo.playerX;
import static everlastingconflict.mapas.MapaCampo.playerY;
import everlastingconflict.mapas.MapaEjemplo;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public class FenixTutorial extends Tutorial {

    @Override
    public final void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("La característica principal de los Fénix es que sus recursos no representan algo material sino que representan el porcentaje de aceptación de la población local."));
        pasos.add(new Paso("Para aumentar tus recursos como Fénix debes mandar tus recolectores a las ciudades civiles para que se esparza tu ideología y la ciudad se te alíe.") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(1).seleccionar();
                MapaEjemplo.mapac.movimiento_pantalla(200, 0);
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Prueba a hacerlo ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.recursos.get(0).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Como puedes ver, mientras el recolector captura la ciudad, no puede moverse por lo que debes evaluar con cuidado si es seguro o no capturar una ciudad antes de intentar hacerlo.") {
            @Override
            public boolean comprobacion(Partida p) {
                return p.recursos.get(0).vida == p.recursos.get(0).vida_max;
            }
        });
        pasos.add(new Paso("Otra característica de los recursos Fénix es que, a diferencia de los de otras razas, nunca disminuyen. A medida que aumentan tus recursos, se van desbloqueando nuevas unidades y edificios pero, al crearlos, no pierdes recursos."));
        pasos.add(new Paso("Su última peculiaridad es a la hora de crear unidades. En vez de especificar cada unidad que se quiere producir, el cuartel Fénix crea un tipo de unidad constantemente. El jugador puede seleccionar que unidad crea cada cuartel.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
                p.j1.edificios.get(1).seleccionar();
                MapaEjemplo.mapac.movimiento_pantalla(0, 400);
            }
        });
        pasos.add(new Paso("Como ejemplo, hemos creado ya un Cuartel y hemos desbloqueado las dos primeras unidades Fénix: el Tigre y el Halcón. Por ahora, el Cuartel no produce ninguna unidad. Haz click en el boton de Tigre para que empiece a producir esta unidad. Debes saber que, en una partida real, además de construir el cuartel deberías desbloquear las unidades en la academia antes de poder entrenarlas.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(1).botones.get(1).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("A partir de este momento, el Cuartel creará Tigres hasta que se le indique que cree otra unidad o hasta que se llegue a la poblacion máxima. Prueba a ordenarle que cree Halcones ahora.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(1).botones.get(2).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("Por último, prueba a ordenar al Cuartel que pare de crear unidades.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(1).botones.get(0).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("Enhorabuena, has completado con éxito el tutorial Fénix. Ya posees los conocimientos básicos para llevar a tu ejército a la victoria."));
    }

    @Override
    public void iniciar_elementos(float anchura, float altura, int njugador) {
        MapaCampo.WORLD_SIZE_X = anchura;
        MapaCampo.WORLD_SIZE_Y = altura;
        MapaCampo.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
        MapaCampo.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
        j1.x_inicial = 200;
        j1.y_inicial = 200;
        j1.iniciar_elementos(this);
        j1.edificios.add(new Edificio("Cuartel Fénix", 200, 600));
        j1.edificios.get(1).iniciarbotones(this);
        j1.edificios.get(1).botones.get(1).activado = true;
        j1.edificios.get(1).botones.get(2).activado = true;
        recursos.add(new Recurso("Civiles", 600, 200));
    }

    public FenixTutorial() {
        iniciar_pasos();
        j1 = new Jugador("Prueba", "Fénix");
        j2 = new Jugador("Prueba", "Eternium");
        j1.color = Color.green;
        j2.color = Color.red;
    }

}