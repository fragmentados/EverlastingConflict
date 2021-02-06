/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.ai.AIEternium;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class MapaEjemplo extends BasicGame {

    public static MapaCampo mapac = new MapaCampo();
    public static MapaIntro mapai = new MapaIntro();
    public static MapaMenu mapam = new MapaMenu();
    public static Mapa mapa_actual;

    public void cambio_de_mapa(GameContainer container, Partida p, String t) throws SlickException {
        Mapa contador = null;
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
            mapa_actual = contador;
        }

    }

    public MapaEjemplo() {
        super("MapaEjemplo");
        //mapac = new MapaCampo(p);
        //mapai = new MapaIntro(p);
        //mapam = new MapaMenu(p);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        //cambio_de_mapa(container, new Partida(), "Menu");
        cambio_de_mapa(container, new Partida(new Jugador("H", "Fénix"), new AIEternium()), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("H", "Clark"), new AIFenix()), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("H", Maestros.nombre_raza), new AIFenix()), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("H", "Eternium"), new Jugador("H", "Clark")), "Campo");
        //cambio_de_mapa(container, new Partida(new Jugador("H", Guardianes.nombre_raza), new AIFenix()), "Campo");

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        mapa_actual.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        mapa_actual.render(container, g);
    }
}
