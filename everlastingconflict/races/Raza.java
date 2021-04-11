/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.races;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Bestia;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import org.newdawn.slick.Image;


public class Raza {


    public static void obtener_experiencia_bestia(Bestia b) {
        b.experiencia_al_morir = b.recompensa;
    }

    public static void obtener_experiencia_unidad(Unidad u) {
        float exp_ataque;
        float exp_vida;
        if (u.vida >= 300) {
            exp_vida = 15;
        } else {
            exp_vida = (u.vida / 300) * 15;
        }
        if (u.ataque >= 20) {
            exp_ataque = 35;
        } else {
            exp_ataque = (u.ataque / 20) * 35;
        }
        u.experiencia_al_morir = exp_ataque + exp_vida;
    }

    public static void obtener_experiencia_edificio(Edificio e) {
        float exp_vida;
        if (e.vida >= 2000) {
            exp_vida = 100;
        } else {
            exp_vida = (e.vida / 2000) * 100;
        }
        e.experiencia_al_morir = exp_vida;
    }

    public static void bestia(Bestia b) {
        switch (b.nombre) {
            case "Alpha":
                Bestias.Alpha(b);
                break;
            case "Beta":
                Bestias.Beta(b);
                break;
            case "Gamma":
                Bestias.Gamma(b);
                break;
            case "Ommega":
                Bestias.Ommega(b);
                break;
        }
        Raza.obtener_experiencia_bestia(b);
    }

    public static void unidad(Jugador aliado, Unidad u) {
        if (aliado != null) {
            switch (aliado.raza) {
                case FENIX:
                    Fenix.unidad(aliado, u);
                    break;
                case ETERNIUM:
                    Eternium.unidad(aliado, u);
                    break;
                case CLARK:
                    Clark.unidad(aliado, u);
                    break;
                case GUARDIANES:
                    Guardianes.unidad(aliado, u);
                    break;
                case MAESTROS:
                    Maestros.unidad(aliado, u);
                    break;
                case ALIANZA:
                    Alianza.unidad(aliado, u);
            }
            Raza.obtener_experiencia_unidad(u);
        }
    }

    public static void edificio(Jugador aliado, Edificio e) {
        switch (aliado.raza) {
            case FENIX:
                Fenix.edificio(aliado, e);
                break;
            case ETERNIUM:
                Eternium.edificio(aliado, e);
                break;
            case CLARK:
                Clark.edificio(aliado, e);
                break;
            case ALIANZA:
                Alianza.edificio(aliado, e);
                break;
            case GUARDIANES:
                Guardianes.edificio(aliado, e);
                break;
        }
        Raza.obtener_experiencia_edificio(e);
    }

    public static void iniciar_botones_edificio(Game p, Edificio e) {
        Jugador aliado = p.getPlayerFromElement(e);
        switch (aliado.raza) {
            case FENIX:
                Fenix.iniciar_botones_edificio(aliado, e);
                break;
            case CLARK:
                Clark.iniciar_botones_edificio(aliado, e);
                break;
            case ETERNIUM:
                Eternium.iniciar_botones_edificio(aliado, e);
                break;
            case GUARDIANES:
                Guardianes.iniciar_botones_edificio(aliado, e);
                break;
            case ALIANZA:
                Alianza.iniciar_botones_edificio(aliado, e);
                break;
        }
        e.initButtonKeys();
        e.checkButtonResources(aliado);
    }

    public static void iniciar_botones_unidad(Game p, Unidad u) {
        Jugador aliado = p.getPlayerFromElement(u);
        u.botones.add(new BotonComplejo("Detener"));
        switch (aliado.raza) {
            case FENIX:
                Fenix.iniciar_botones_unidad(aliado, u);
                break;
            case CLARK:
                Clark.iniciar_botones_unidad(u);
                break;
            case ETERNIUM:
                Eternium.iniciar_botones_unidad(u);
                break;
            case MAESTROS:
                Maestros.iniciar_botones_unidad(u);
                break;
            case GUARDIANES:
                Guardianes.iniciar_botones_unidad(u);
                break;
        }
        u.initButtonKeys();
        u.checkButtonResources(aliado);
    }

    public static Image getResourceImage(RaceEnum race) {
        Image image = null;
        try {
            image = new Image("media/Recursos/" + race.getName() + ".png");
        } catch (Exception e) {
        }
        return image;
    }
}
