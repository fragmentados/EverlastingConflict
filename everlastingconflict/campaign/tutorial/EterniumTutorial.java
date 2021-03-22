/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.relojes.RelojEternium;
import everlastingconflict.ventanas.VentanaCombate;
import everlastingconflict.ventanas.VentanaPrincipal;

import java.util.ArrayList;

import static everlastingconflict.ventanas.VentanaCombate.*;

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
                p.getMainPlayer().unidades.get(0).seleccionar();
            }
        });
        pasos.add(new Paso("En la primera fase, las capacidades ofensivas y defensivas de las unidades y de los edificios Eternium se encuentran al 75%") {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.eterniumWatch().ndivision = 2;
            }
        });
        pasos.add(new Paso("En la segunda fase, se encuentran al 100%") {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.eterniumWatch().ndivision = 3;
            }
        });
        pasos.add(new Paso("En la tercera fase, alcanzan su máximo ya que se encuentran al 150%") {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.eterniumWatch().ndivision = 4;
                VentanaCombate.eterniumWatch().detencion_temporal();
            }
        });
        pasos.add(new Paso("La cuarta fase es la más peligrosa ya que, en ella, las unidades Eternium son incapaces de moverse y de atacar. La única ventaja de esta fase, es que las capacidades defensivas aumentan hasta el 500% de su valor base.") {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.eterniumWatch().ndivision = 1;
                VentanaCombate.eterniumWatch().liberacion_temporal();
            }
        });
        pasos.add(new Paso("El sistema de recolección de recursos Eternium sigue el mismo patrón de alta inversión, alto beneficio. La recolección de hierro y transformación del mismo en energía sigue tres pasos.") {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = false;
                getMainPlayer().unidades.get(0).seleccionar();
                VentanaPrincipal.ventanaCombate.movimiento_pantalla(200, 0);
            }
        });
        pasos.add(new Paso("Primero, el hierro se extrae de vetas mediante refinerías. Cualquier unidad militar Eternium puede construir refinerías sobre una veta de hierro. Prueba a hacerlo ahora", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return getMainPlayer().edificios.stream().map(e -> e.nombre).anyMatch(s -> "Refinería".equals(s));
            }

            @Override
            public void efecto(Partida p) {
                getMainPlayer().unidades.get(0).deseleccionar();
                getMainPlayer().edificios.get(0).seleccionar();
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("En segundo lugar, el hierro debe ser transformado en la cámara de asimilación. Construye una cámara de asimilación utilizando la sede en cualquier lugar dentro del área permitida.", false) {
            @Override
            public void efecto(Partida p) {
                getMainPlayer().edificios.get(0).seleccionar();
                VentanaCombate.continuar.canBeUsed = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                return getMainPlayer().edificios.stream().map(e -> e.nombre).anyMatch(s -> "Cámara de asimilación".equals(s));
            }
        });
        pasos.add(new Paso("El último paso para la conversión requiere de tecnología de la que no disponemos en el campo de batalla con lo que se debe transportar a una de las naves que orbitan el planeta. Construye un teletransportador para terminar el sistema.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return getMainPlayer().edificios.stream().map(e -> e.nombre).anyMatch(s -> "Teletransportador".equals(s));
            }
        });
        pasos.add(new Paso("Como puedes comprobar, a partir de ahora, las refinerías empezarán a generar recursos de manera continua.Cada nueva refinería que crees empezará a generar recursos de la misma forma. Pero debes recordar esto, la cámara de asimilación y el teletransportador son los dos edificio primordiales ya que, si alguno es destruido, todas tus refinerías dejarán de funcionar hasta que se reconstruya."));
    }

    @Override
    public void initElements() {
        super.initElements();
        VentanaCombate.WORLD_SIZE_X = map.getWidth();
        VentanaCombate.WORLD_SIZE_Y = map.getHeight();
        VentanaCombate.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_WIDTH;
        VentanaCombate.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_HEIGHT;
        getMainPlayer().x_inicial = 200;
        getMainPlayer().y_inicial = 200;
        getMainPlayer().initElements(this);
        recursos.add(new Recurso("Hierro", 600, 200));
        VentanaCombate.createWatch(new RelojEternium(getMainPlayer()));
        //VentanaCombate.eterniumWatch().detener_reloj(1000);
    }

    public EterniumTutorial() {
        super(RaceEnum.ETERNIUM.getName(), RaceEnum.CLARK.getName());
    }

}
