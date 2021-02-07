/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.relojes.RelojMaestros;
import everlastingconflict.elementos.implementacion.Bestias;
import everlastingconflict.elementos.implementacion.Habilidad;
import everlastingconflict.elementos.implementacion.Manipulador;
import everlastingconflict.estados.Estado;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.MapaCampo;
import static everlastingconflict.mapas.MapaCampo.VIEWPORT_SIZE_X;
import static everlastingconflict.mapas.MapaCampo.VIEWPORT_SIZE_Y;
import static everlastingconflict.mapas.MapaCampo.WORLD_SIZE_X;
import static everlastingconflict.mapas.MapaCampo.WORLD_SIZE_Y;

import everlastingconflict.razas.Eternium;
import everlastingconflict.razas.Maestros;

import java.util.ArrayList;

import org.newdawn.slick.Color;

/**
 *
 * @author Elías
 */
public class MaestrosTutorial extends Tutorial {

    @Override
    public void iniciar_pasos() {
        pasos = new ArrayList<>();
        pasos.add(new Paso("La raza de los Maestros del Universo debe ser sin duda la más peculiar de Everlasting Conflict."));
        pasos.add(new Paso("La forma de jugar de los jugadores Maestros no se parece a la de los RTS tradicionales sino que asemeja a la de un RPG."));
        pasos.add(new Paso("La unidad que ves a la izquierda de la pantalla es el Manipulador, el eje principal de la raza Maestros del Universo."));
        pasos.add(new Paso("Empiezas la partida con un Manipulador y, si en cualquier momento tu Manipulador muere, pierdes la partida automáticamente."));
        pasos.add(new Paso("Esto puede parecer un handicap muy fuerte frente al resto de razas que pueden producir gran cantidad de unidades pero como, pronto verás, el Manipulador puede utilizar poderosas habilidades que compensan esta debilidad.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Intentémoslo. Selecciona la habilidad Deflagración y luego haz objetivo al Alpha que acaba de aparecer y mira lo que ocurre.") {
            @Override
            public boolean comprobacion(Partida p) {
                return p.bestias.get(0).contenido.size() == 2;
            }
        });
        pasos.add(new Paso("Como puedes ver, la barra de experiencia en la parte superior de la pantalla ha aumentado. Cuando llegue al final, tu Manipulador subirá de nivel y sus capacidades aumentarán."));
        pasos.add(new Paso("Generalmente, sólo obtienes experiencia destruyendo unidades y edificios. Sin embargo, el nivel uno es especial ya que el Manipulador posee la habilidad Meditación.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Prueba a utilizarla habilidad Meditación ahora.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                return p.j1.unidades.get(0).estados.existe_estado(Estado.nombre_meditacion);
            }
        });
        pasos.add(new Paso("Como podrás comprobar, tu Manipulador es ahora incapaz de moverse, pero su experiencia aumenta continuamente hasta subir al nivel dos. Espera hasta que ocurra esto.") {
            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                return p.j1.unidades.get(0).estados.contenido.isEmpty();
            }
        });
        pasos.add(new Paso("Enhorabuena! Tu Manipulador ha subido de nivel. Como habrás observado, la habilidad Meditación ha desaparecido ya que solo se puede usar en el nivel 1. Pero, además, han aparecido dos botones nuevos: Habilidades y Atributos. Prueba a pulsar Habilidades ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                return !((Manipulador) p.j1.unidades.get(0)).botones_mejora.isEmpty();
            }

            @Override
            public void efecto(Partida p) {
                MapaCampo.continuar.activado = false;
            }
        });
        pasos.add(new Paso("Ahora podrás elegir hasta dos habildiades para que tu Manipulador las aprenda. Prueba a hacerlo ahora.") {
            @Override
            public boolean comprobacion(Partida p) {
                return ((Manipulador) p.j1.unidades.get(0)).botones_mejora.isEmpty();
            }
        });
        pasos.add(new Paso("El botón atributos funciona más o menos igual salvo que puedes elegir hasta cinco veces y puedes repetir varias veces la misma elección."));
        pasos.add(new Paso("El último aspecto relevante de la raza Maestros del Universo es la división entre día y noche. El reloj que aparece a la izquierda del nivel del Manipulador determina si el mapa se encuentra en día o en noche."));
        pasos.add(new Paso("El estado del mapa determina que habilidades podrá usar el Manipulador: hay habilidades que sólo se pueden usar de día, como deflagración, otras que solo se pueden usar de noche, y otras que se pueden usar en cualquier momento."));
        pasos.add(new Paso("Ahora ya tienes los conocimientos básicos para llevar a tu ejército Maestros del Universo a la victoria."));
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
        j1.unidades.get(0).botones = new ArrayList<>();
        j1.unidades.get(0).botones.add(new BotonManipulador(new Habilidad("Deflagración"), RelojMaestros.nombre_dia));
        j1.unidades.get(0).botones.add(new BotonManipulador(new Habilidad("Meditar"), "Cualquiera"));
        j1.unidades.get(0).inicializar_teclas_botones(j1.unidades.get(0).botones);
        bestias.add(new Bestias("Grupo1", 500, 200));
        MapaCampo.crearReloj(new RelojMaestros(j1));
    }

    public MaestrosTutorial() {
        iniciar_pasos();
        j1 = new Jugador("Prueba", Maestros.nombre_raza);
        j2 = new Jugador("Prueba", Eternium.nombre_raza);
        j1.color = Color.green;
        j2.color = Color.red;
    }

}
