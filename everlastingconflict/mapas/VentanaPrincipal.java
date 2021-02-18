/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.gestion.Partida;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class VentanaPrincipal extends BasicGame {

    public static VentanaCombate mapac = new VentanaCombate();
    public static VentanaIntro mapai = new VentanaIntro();
    public static VentanaMenu mapam = new VentanaMenu();
    public static Ventana ventanaActual;

    public static void cambio_de_mapa(GameContainer container, Partida p, String t) throws SlickException {
        Ventana contador = null;
        switch (t) {
            case "Menu":
                mapam.partida = p;
                contador = mapam;
                break;
            case "Campo":
                mapac.partida = p;                
                contador = mapac;
                break;
            case "Intro":
                mapai.partida = p;
                contador = mapai;
                break;
        }
        if (contador != null) {
            contador.init(container);
            ventanaActual = contador;
        }

    }

    public VentanaPrincipal() {
        super("VentanaPrincipal");
        //mapac = new MapaCampo(p);
        //mapai = new MapaIntro(p);
        //mapam = new MapaMenu(p);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        cambio_de_mapa(container, new Partida(), "Menu");
        //cambio_de_mapa(container, new Partida(new Jugador("Elias", "Clark"), new AIEternium()), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("Elias", "Clark"), new AIFenix()), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("Elias", RaceNameEnum.MAESTROS.getName()), new AIFenix()), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("Elias", "Eternium"), new Jugador("H", "Clark")), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("Elias", RaceNameEnum.GUARDIANES.getName()), new AIFenix()), "Campo");

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        ventanaActual.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        ventanaActual.render(container, g);
    }
}
