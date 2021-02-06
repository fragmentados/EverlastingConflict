/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.RTS;
import everlastingconflict.relojes.RelojEternium;
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
public class EterniumTutorial extends Tutorial {

    @Override
    public final void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("Los Eternium son una raza dominada por el tiempo. En la parte superior de la pantalla puedes ver un reloj con cuatro esferas. Esas esferas representan las distintas fases por las que pasa el reloj.") {
            @Override
            public void efecto(Partida p) {
                p.j1.unidades.get(0).seleccionar();
            }
        });
        pasos.add(new Paso("En la primera fase, las capacidades ofensivas y defensivas de las unidades y de los edificios Eternium se encuentran al 75%") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.reloj_eternium.ndivision = 2;
            }
        });
        pasos.add(new Paso("En la segunda fase, se encuentran al 100%") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.reloj_eternium.ndivision = 3;
            }
        });
        pasos.add(new Paso("En la tercera fase, alcanzan su máximo ya que se encuentran al 150%") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.reloj_eternium.ndivision = 4;
                MapaCampo.reloj_eternium.detencion_temporal(j1);
            }
        });
        pasos.add(new Paso("La cuarta fase es la más peligrosa ya que, en ella, las unidades Eternium son incapaces de moverse y de atacar. La única ventaja de esta fase, es que las capacidades defensivas aumentan hasta el 500% de su valor base.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.reloj_eternium.ndivision = 1;
                MapaCampo.reloj_eternium.liberacion_temporal(j1);
            }
        });
        pasos.add(new Paso("El sistema de recolección de recursos Eternium sigue el mismo patrón de alta inversión, alto beneficio. La recolección de hierro y transformación del mismo en energía sigue tres pasos.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
                j1.unidades.get(0).seleccionar();
                MapaEjemplo.mapac.movimiento_pantalla(200, 0);
            }
        });
        pasos.add(new Paso("Primero, el hierro se extrae de vetas mediante refinerías. Cualquier unidad militar Eternium puede construir refinerías sobre una veta de hierro. Prueba a hacerlo ahora") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return j1.unidades.get(0).botones.get(0).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }

            @Override
            public void efecto(Partida p) {
                j1.unidades.get(0).deseleccionar();
                j1.edificios.get(0).seleccionar();
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("En segundo lugar, el hierro debe ser transformado en la cámara de asimilación. Construye una cámara de asimilación utilizando la sede en cualquier lugar dentro del área permitida.") {
            @Override
            public void efecto(Partida p) {
                j1.edificios.get(0).seleccionar();
                MapaCampo.continuar.activado = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return j1.edificios.get(0).botones.get(3).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("El último paso para la conversión requiere de tecnología de la que no disponemos en el campo de batalla con lo que se debe transportar a una de las naves que orbitan el planeta. Construye un transportador para terminar el sistema.") {
            @Override
            public boolean comprobacion(Partida p) {
                Input input = RTS.canvas.getContainer().getInput();
                return j1.edificios.get(0).botones.get(4).presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
            }
        });
        pasos.add(new Paso("Como puedes comprobar, a partir de ahora, las refinerías empezarán a generar recursos de manera continua.Cada nueva refinería que crees empezará a generar recursos de la misma forma. Pero debes recordar esto, la cámara de asimilación y el teletransportador son los dos edificio primordiales ya que, si alguno es destruido, todas tus refinerías dejarán de funcionar hasta que se reconstruya."));

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
        recursos.add(new Recurso("Hierro", 600, 200));
        MapaCampo.reloj_eternium = new RelojEternium();
        MapaCampo.reloj_eternium.detener_reloj(1000);
    }

    public EterniumTutorial() {
        iniciar_pasos();
        j1 = new Jugador("Prueba", "Eternium");
        j2 = new Jugador("Prueba", "Fénix");
        j1.color = Color.green;
        j2.color = Color.red;
    }

}
