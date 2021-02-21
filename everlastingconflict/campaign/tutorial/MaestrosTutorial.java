/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.elementos.implementacion.Bestias;
import everlastingconflict.elementos.implementacion.Habilidad;
import everlastingconflict.elementos.implementacion.Manipulador;
import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.razas.RaceNameEnum;
import everlastingconflict.relojes.RelojMaestros;
import org.newdawn.slick.Color;

import java.util.ArrayList;

import static everlastingconflict.elementos.implementacion.Manipulador.ATTRIBUTES_BUTTON;
import static everlastingconflict.elementos.implementacion.Manipulador.SKILL_BUTTON;
import static everlastingconflict.mapas.VentanaCombate.*;

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
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Intentémoslo. Selecciona la habilidad Deflagración y luego haz objetivo al Alpha que acaba de aparecer y mira lo que ocurre.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return p.bestias.get(0).contenido.size() == 2;
            }
            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = true;
            }
        });
        pasos.add(new Paso("Como puedes ver, la barra de experiencia en la parte superior de la pantalla ha aumentado. Cuando llegue al final, tu Manipulador subirá de nivel y sus capacidades aumentarán."));
        pasos.add(new Paso("Generalmente, sólo obtienes experiencia destruyendo unidades y edificios. Sin embargo, en el nivel uno es especial el Manipulador puede utilizar la habilidad Meditación para obtener experiencia.") {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Prueba a utilizar la habilidad Meditación ahora.", false) {
            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = false;
            }

            @Override
            public boolean comprobacion(Partida p) {
                return p.j1.unidades.get(0).statusEffectCollection.existe_estado(StatusEffectName.MEDITACION);
            }
        });
        pasos.add(new Paso("Como podrás comprobar, tu Manipulador es ahora incapaz de moverse, pero su experiencia aumenta continuamente hasta subir al nivel dos. Espera hasta que ocurra esto.", false) {

            @Override
            public boolean comprobacion(Partida p) {
                return ((Manipulador)p.j1.unidades.get(0)).nivel == 2;
            }
            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Enhorabuena! Tu Manipulador ha subido de nivel. Sigues disponiendo de la habilidad Meditación pero no aumentará la experiencia sino que mejorará tu regeneración de maná. Además, han aparecido dos botones nuevos: Habilidades y Atributos. Prueba a pulsar Habilidades ahora.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return !((Manipulador) p.j1.unidades.get(0)).enhancementButtons.isEmpty();
            }

            @Override
            public void efecto(Partida p) {
                ((Manipulador) p.j1.unidades.get(0)).enhancementButtons.removeIf(b -> "Deflagración".equals(b.elemento_nombre));
                VentanaCombate.continuar.canBeUsed = false;
            }
        });
        pasos.add(new Paso("Ahora podrás elegir hasta dos habildiades para que tu Manipulador las aprenda. Prueba a hacerlo ahora.", false) {
            @Override
            public boolean comprobacion(Partida p) {
                return ((Manipulador) p.j1.unidades.get(0)).enhancementButtons.isEmpty();
            }
            @Override
            public void efecto(Partida p) {
                VentanaCombate.continuar.canBeUsed = true;
            }
        });
        pasos.add(new Paso("El botón atributos funciona más o menos igual salvo que puedes elegir hasta cinco veces y puedes repetir varias veces la misma elección."));
        pasos.add(new Paso("El último aspecto relevante de la raza Maestros del Universo es la división entre día y noche. El reloj que aparece a la izquierda de la vida del Manipulador determina si el mapa se encuentra en día o en noche."));
        pasos.add(new Paso("El estado del mapa determina que habilidades podrá usar el Manipulador: hay habilidades que sólo se pueden usar de día, como deflagración, otras que solo se pueden usar de noche, y otras que se pueden usar en cualquier momento."));
        pasos.add(new Paso("Ahora ya tienes los conocimientos básicos para llevar a tu ejército Maestros del Universo a la victoria."));
    }

    @Override
    public void initElements(int njugador) {
        super.initElements(njugador);
        VentanaCombate.WORLD_SIZE_X = map.getWidth();
        VentanaCombate.WORLD_SIZE_Y = map.getHeight();
        VentanaCombate.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_WIDTH;
        VentanaCombate.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_HEIGHT;
        j1.x_inicial = 200;
        j1.y_inicial = 200;
        j1.initElements(this);
        j1.unidades.get(0).botones = new ArrayList<>();
        j1.unidades.get(0).botones.add(new BotonManipulador(new Habilidad("Deflagración"), RelojMaestros.nombre_dia));
        j1.unidades.get(0).botones.add(new BotonManipulador(new Habilidad("Meditar")));
        j1.unidades.get(0).botones.removeIf(b -> b.equals(SKILL_BUTTON) || b.equals(ATTRIBUTES_BUTTON));
        SKILL_BUTTON.remainingClicks = 0;
        ATTRIBUTES_BUTTON.remainingClicks = 0;
        j1.unidades.get(0).initButtonKeys();
        bestias = new ArrayList<>();
        bestias.add(new Bestias("Grupo1", 500, 200));
        VentanaCombate.crearReloj(new RelojMaestros(j1));
    }

    public MaestrosTutorial() {
        iniciar_pasos();
        j1 = new Jugador("Prueba", RaceNameEnum.MAESTROS.getName());
        j2 = new Jugador("Prueba", RaceNameEnum.ETERNIUM.getName());
        j1.color = Color.green;
        j2.color = Color.red;
    }

}
