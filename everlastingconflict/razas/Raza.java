/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.razas;

import everlastingconflict.elementos.implementacion.Bestia;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Elías
 */
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

    public static void unidad(Unidad u) {
        switch (u.nombre) {
            //Fenix
            case "Recolector":
                Fenix.Recolector(u);
                break;
            case "Constructor":
                Fenix.Constructor(u);
                break;
            case "Soldado":
                Fenix.Soldado(u);
                break;
            case "Minero":
                Fenix.Minero(u);
            case "Sniper":
                Fenix.Sniper(u);
                break;
            case "Tigre":
                Fenix.Tigre(u);
                break;
            case "Halcón":
                Fenix.Halcon(u);
                break;
            case "Fénix":
                Fenix.Fenix(u);
                break;
            //Guardianes
            case "Patrulla":
                Guardianes.Patrulla(u);
                break;
            case "Tanque":
                Guardianes.Tanque(u);
                break;
            case "Destructor":
                Guardianes.Destructor(u);
                break;
            case "Artillero":
                Guardianes.Artillero(u);
                break;
            case "Corredor":
                Guardianes.Corredor(u);
                break;
            case "Amparador":
                Guardianes.Amparador(u);
                break;
            case "Ingeniero":
                Guardianes.Ingeniero(u);
                break;
            case "Armero":
                Guardianes.Armero(u);
                break;
            case "Oteador":
                Guardianes.Oteador(u);
                break;
            case "Explorador":
                Guardianes.Explorador(u);
                break;
            case "Pacificador":
                Guardianes.Pacificador(u);
                break;
            case "Silenciador":
                Guardianes.Silenciador(u);
                break;
            case "Inquisidor":
                Guardianes.Inquisidor(u);
                break;
            case "Activador":
                Guardianes.Activador(u);
                break;
            case "Acribillador":
                Guardianes.Acribillador(u);
                break;
            case "Enviado celeste":
                Guardianes.Enviado(u);
                break;
            //Clark   
            case "Depredador":
                Clark.Depredador(u);
                break;
            case "Devorador":
                Clark.Devorador(u);
                break;
            case "Cazador":
                Clark.Cazador(u);
                break;
            case "Moldeador":
                Clark.Moldeador(u);
                break;
            case "Defensor":
                Clark.Defensor(u);
                break;
            case "Inspirador":
                Clark.Inspirador(u);
                break;
            case "Desmembrador":
                Clark.Desmembrador(u);
                break;
            case "Regurgitador":
                Clark.Regurgitador(u);
                break;
            case "Amaestrador":
                Clark.Amaestrador(u);
                break;
            case "Matriarca":
                Clark.Matriarca(u);
                break;
            //Eternium
            case "Adepto":
                Eternium.Adepto(u);
                break;
            case "Guerrero":
                Eternium.Guerrero(u);
                break;
            case "Ancestro":
                Eternium.Ancestro(u);
                break;
            case "Protector":
                Eternium.Protector(u);
                break;
            //Maestros
            case "Manipulador":
                Maestros.Manipulador(u);
                break;
            case "Pugnator":
                Maestros.Pugnator(u);
                break;
            case "Sagittarius":
                Maestros.Saggitarius(u);
                break;
            case "Exterminatore":
                Maestros.Exterminatore(u);
                break;
            case "Medicum":
                Maestros.Medicum(u);
                break;
            case "Magum":
                Maestros.Magum(u);
                break;
        }
        Raza.obtener_experiencia_unidad(u);
    }

    public static void edificio(Edificio e) {
        switch (e.nombre) {
            //Fénix
            case "Centro de restauración":
                Fenix.Centro(e);
                break;
            case "Cuartel Fénix":
                Fenix.CuartelFenix(e);
                break;
            case "Centro tecnológico":
                Fenix.CentroTecnologico(e);
                break;
            case "Academia":
                Fenix.Academia(e);
                break;
            case "Sede":
                Fenix.Sede(e);
                break;
            //Guardianes
            case "Taller bélico":
                Guardianes.Taller(e);
                break;
            case "Anexo":
                Guardianes.Anexo(e);
                break;
            case "Academia de pilotos":
                Guardianes.Pilotos(e);
                break;
            case "Ayuntamiento":
                Guardianes.Ayuntamiento(e);
                break;
            case "Templo":
                Guardianes.Templo(e);
                break;
            case "Laboratorio de I+D":
                Guardianes.Laboratorio(e);
                break;
            case "Vaticano":
                Guardianes.Vaticano(e);
                break;
            case "Edificio gubernamental":
                Guardianes.Gubernamental(e);
                break;
            case "Torreta defensiva":
                Guardianes.Torreta(e);
                break;
            //Eternium   
            case "Mando Central":
                Eternium.MandoCentral(e);
                break;
            case "Refinería":
                Eternium.Refineria(e);
                break;
            case "Altar de los ancestros":
                Eternium.Altar(e);
                break;
            case "Cámara de asimilación":
                Eternium.Camara(e);
                break;
            case "Teletransportador":
                Eternium.Teletransportador(e);
                break;
            //Clark
            case "Primarca":
                Clark.Primarca(e);
                break;
        }
        Raza.obtener_experiencia_edificio(e);
    }

    public static void iniciar_botones_edificio(Partida p, Edificio e) {
        Jugador aliado = p.jugador_aliado(e);
        switch (aliado.raza) {
            case "Fénix":
                Fenix.iniciar_botones_edificio(e);
                break;
            case "Clark":
                Clark.iniciar_botones_edificio(e);
                break;
            case "Eternium":
                Eternium.iniciar_botones_edificio(e);
                break;
            case "Guardianes":
                Guardianes.iniciar_botones_edificio(e);
                break;
        }
        e.initButtonKeys();
        e.checkButtonResources(aliado);
    }

    public static void iniciar_botones_unidad(Partida p, Unidad u) {
        Jugador aliado = p.jugador_aliado(u);
        u.botones.add(new BotonComplejo("Detener"));
        switch (aliado.raza) {
            case "Fénix":
                Fenix.iniciar_botones_unidad(u);
                break;
            case "Clark":
                Clark.iniciar_botones_unidad(u);
                break;
            case "Eternium":
                Eternium.iniciar_botones_unidad(u);
                break;
            case "Maestros":
                Maestros.iniciar_botones_unidad(u);
                break;
            case "Guardianes":
                Guardianes.iniciar_botones_unidad(u);
                break;
        }
        u.initButtonKeys();
        u.checkButtonResources(aliado);
    }

    public static Image getResourceImage(String raceName) {
        Image image = null;
        try {
            image = new Image("media/Recursos/" + raceName + ".png");
        } catch (Exception e) {
        }
        return image;
    }

    public static List<String> getAllRaceNames() {
        List<String> raceNames = new ArrayList<>();
        raceNames.addAll(Arrays.asList(RaceNameEnum.FENIX.getName(), RaceNameEnum.CLARK.getName(), RaceNameEnum.ETERNIUM.getName(), RaceNameEnum.MAESTROS.getName(), RaceNameEnum.GUARDIANES.getName()));
        return raceNames;
    }
}
