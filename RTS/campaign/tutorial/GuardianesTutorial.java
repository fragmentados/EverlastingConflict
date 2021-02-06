/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.campaign.tutorial;

import RTS.elementos.implementacion.Unidad;
import RTS.gestion.Jugador;
import RTS.gestion.Partida;
import RTS.mapas.MapaCampo;
import static RTS.mapas.MapaCampo.VIEWPORT_SIZE_X;
import static RTS.mapas.MapaCampo.VIEWPORT_SIZE_Y;
import static RTS.mapas.MapaCampo.WORLD_SIZE_X;
import static RTS.mapas.MapaCampo.WORLD_SIZE_Y;
import static RTS.mapas.MapaCampo.playerX;
import static RTS.mapas.MapaCampo.playerY;
import RTS.razas.Eternium;
import RTS.razas.Guardianes;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public class GuardianesTutorial extends Tutorial {

    @Override
    public void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("Cuando los Eternium llegaron a la Tierra, la humanidad se vio dividida en dos facciones. Los Guardianes de la Paz representan a los humanos que se aliaron con los Eternium con tal de obtener paz y prosperidad."));
        pasos.add(new Paso("La primera característica de los Guardianes es que, a diferencia de otras razas, no tiene que construir edificios sino que empiezan la partida con todos los edificios construidos en una posición predeterminada.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Sin embargo, sólo el edificio Central, el Ayuntamiento, puede ser usado nada más empezar, el resto deben ser activados. Para ello, debemos crear una unidad especial en el Ayuntamiento, el Activador. Prueba a hacerlo ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(0).botones.get(0).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Para activar un edificio, selecciona el Activador recién creado y pulsa el botón derecho sobre el edificio a activar. Prueba a activar el Taller bélico ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(3).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("La siguiente característica de la raza son los vehíuculos: es un tipo de unidad más potente que el resto pero que requieren de un piloto para poder moverse y atacar. Pero antes de ello, debemos crear un anexo en el taller bélico para que guarde el vehículo.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(3).botones.get(0).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("A continuación, prueba a crear una Patrulla y observa como se moviliza hacia el anexo.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(3).botones.get(1).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
                p.j1.edificios.get(1).iniciarbotones(p);
            }

        });
        pasos.add(new Paso("Como puedes comprobar, por ahora el vehículo no se puede mover ni atacar. Eso es porque debe ser tripulado. Para ello, debes crear un piloto en la academia de pilotos. Generalmente, deberías crear otro Activador para que la Academia pudiera ser usable, pero en este Tutorial vamos a activar el edificio por ti. Prueba a construir un Artillero.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(1).botones.get(0).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Para embarcar un vehículo, selecciona el piloto y pulsa el botón derecho sobre el vehículo a embarcar. Prueba a hacerlo ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.unidades.get(0).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("A continuación, veamos como los Guardianes obtienen recursos. Existen tres recursos que se muestran de izquierda a derecha en la parte de arriba de la pantalla: el primero es el que se usa para crear todas las unidades y los edificios, el segundo es el porcentaje de felicidad de la población, cuanto mayor sea más rápido se obtendrá el primer recurso, el útlimo recurso representa el nivel de amenaza. Cuanto mayor sea, más piezas del arsenal de los Guardianes se desbloquearán pero peores eventos podrán aparecer."));
        pasos.add(new Paso("Por último, explicaremos los eventos que acabamos de mencionar. Existen dos tipos de eventos: los positivos y los negativos. Los positivos se obtienen investigándolos en el ayuntamiento y aumentan la felicidad de la población. Los eventos negativos aparecen cada cierto tiempo y, de no ser evitados, disminuyen la felicidad de la población."));
        pasos.add(new Paso("Para solucionar un evento negativo se deben sacrificar unidades. Para hacerlo debes moverlas al ayuntamiento.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
                Unidad u = new Unidad("Patrulla", p.j1.unidades.get(0).x - 50, p.j1.unidades.get(0).y);
                u.movil = true;
                p.j1.unidades.add(u);
            }
        });
        pasos.add(new Paso("Veamos un ejemplo. Se ha creado una Patrulla automáticamente. Dos patrullas son las tropas necesarias para solucionar el evento 'Una racha criminal asola las calles'. Prueba a enviar la primera Patrulla al ayuntamiento ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(0).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Muy bien. Hazlo una vez más para solucionar el evento negativo.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.RTS.canvas.getContainer().getInput();
                return p.j1.edificios.get(0).hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Enhorabuena. Ahora ya sabes todo lo que necesitas saber para llevar a tu ejército a la victoria."));
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
    }

    public GuardianesTutorial() {
        iniciar_pasos();
        j1 = new Jugador("Prueba", Guardianes.nombre_raza);
        j2 = new Jugador("Prueba", Eternium.nombre_raza);
        j1.color = Color.green;
        j2.color = Color.red;
    }
}
