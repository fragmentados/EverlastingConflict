/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.races;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Habilidad;
import everlastingconflict.elements.impl.Tecnologia;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;

import static everlastingconflict.gestion.Game.getPlayersByRace;


public class Eternium {

    public static final int tiempo_mineria = 10;
    public static int ataque_guerrero = Unidad.ataque_estandar + 14;
    public static int ataque_ancestro = Unidad.ataque_estandar + 34;
    public static final int MAX_UNIT_PER_QUEUE = 5;
    public static int recursos_refineria = 70;

    //Unidades
    public static void Adepto(Jugador aliado, Unidad u) {
        u.ataque = Unidad.ataque_estandar + 6;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 10;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar - 0.2f;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.vision = Unidad.vision_estandar + 100;
        u.coste = aliado.hasTecnologyResearched("Aprendizaje económico") ? 25 : 50;
        u.tiempo = Unidad.tiempo_estandar;
        u.botones.add(new BotonComplejo(new Edificio(aliado, "Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad básica de ataque a distancia.";
    }

    public static void Guerrero(Jugador aliado, Unidad u) {
        u.ataque = Eternium.ataque_guerrero;
        u.defensa = Unidad.defensa_estandar + 3;
        u.vida_max = Unidad.vida_estandar + 80;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar - 0.1f;
        u.vision = Unidad.vision_estandar;
        u.coste = 125;
        u.tiempo = Unidad.tiempo_estandar + 5;
        u.botones.add(new BotonComplejo(new Edificio(aliado, "Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad capaz de recibir grandes daños antes de ser destruida.";
    }

    public static void Ancestro(Jugador aliado, Unidad u) {
        u.ataque = Eternium.ataque_ancestro;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar + 50;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar - 0.2f;
        u.vision = Unidad.vision_estandar - 50;
        u.coste = 200;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.botones.add(new BotonComplejo(new Edificio(aliado, "Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad con gran capacidad ofensiva y con la habilidad para alterar el tiempo.";
    }

    public static void Protector(Jugador aliado, Unidad u) {
        u.ataque = 0;
        u.defensa = 2;
        u.vida_max = Unidad.vida_estandar + 100;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar + 0.2f;
        u.velocidad = Unidad.velocidad_estandar - 0.3f;
        u.vision = Unidad.vision_estandar;
        u.coste = 100;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.botones.add(new BotonComplejo(new Edificio(aliado, "Refinería")));
        u.constructor = true;
        u.hostil = false;
        u.descripcion = "Unidad defensiva que atrae los proyectiles enemigos durante la detención temporal.";
    }

    public static void Erradicador(Jugador aliado, Unidad u) {
        u.ataque = Unidad.ataque_estandar + 30;
        u.defensa = 2;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.5f;
        u.vision = Unidad.vision_estandar;
        u.coste = 80;
        u.tiempo = Unidad.tiempo_estandar + 2;
        u.botones.add(new BotonComplejo(new Edificio(aliado, "Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad especializada en el combate cuerpo a cuerpo inmune a la detención temporal.";
    }

    //Edificios
    public static void MandoCentral(Edificio e) {
        e.vida_max = 2000;
        e.vision = Unidad.vision_estandar + 200;
        e.descripcion = "Edificio central que produce unidades, construye edificios e investiga tecnologías.";
        e.constructor = true;
        e.radio_construccion = 1500f;
        e.main = true;
        e.unitCreator = true;
    }

    public static final void Refineria(Edificio e) {
        e.coste = 100;
        e.vida_max = Edificio.vida_estandar / 2;
        e.vision = Edificio.vision_estandar - 100;
        e.tiempo = Edificio.tiempo_estandar;
        e.descripcion = "Edificio básico para la explotación de las vetas de hierro.";
    }

    public static final void Teletransportador(Edificio e) {
        e.coste = 100;
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar - 200;
        e.tiempo = Edificio.tiempo_estandar;
        e.descripcion = "Edificio capaz de enviar el mineral procesado a la nave nodriza para el último paso del " +
                "procesamiento.";
    }

    public static final void Camara(Edificio e) {
        e.coste = 100;
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar - 200;
        e.tiempo = Edificio.tiempo_estandar;
        e.descripcion = "Edificio encargado de procesar el mineral extraido por las refinerías.";
    }

    public static final void Altar(Edificio e) {
        e.coste = 150;
        e.vida_max = Edificio.vida_estandar + 500;
        e.vision = Edificio.vision_estandar - 200;
        e.tiempo = Edificio.tiempo_estandar;
        e.descripcion = "Edificio encargado de investigar las nuevas tecnologías.";
    }

    public static void iniciar_botones_edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            case "Mando Central":
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Adepto")));
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Guerrero")));
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Ancestro")));
                if (SubRaceEnum.PROTECTORES.equals(aliado.subRace)) {
                    e.botones.add(new BotonComplejo(new Unidad(aliado, "Protector")));
                } else if (SubRaceEnum.ERRADICARDORES.equals(aliado.subRace)) {
                    e.botones.add(new BotonComplejo(new Unidad(aliado, "Erradicador")));
                }
                e.botones.add(new BotonComplejo(new Edificio(aliado, "Cámara de asimilación")));
                e.botones.add(new BotonComplejo(new Edificio(aliado, "Teletransportador")));
                e.botones.add(new BotonComplejo(new Edificio(aliado, "Altar de los ancestros")));
                break;
            case "Altar de los ancestros":
                e.botones.add(new BotonComplejo(new Tecnologia("Catalización extrema")));
                e.botones.add(new BotonComplejo(new Tecnologia("Arco de plasma")));
                e.botones.add(new BotonComplejo(new Tecnologia("Espada desintegradora")));
                e.botones.add(new BotonComplejo(new Tecnologia("Lanza de Longinus")));
                if (SubRaceEnum.ERUDITOS.equals(aliado.subRace)) {
                    e.botones.add(new BotonComplejo(new Tecnologia("Aprendizaje económico")));
                    e.botones.add(new BotonComplejo(new Tecnologia("Control temporal")));
                }
                break;
        }
    }

    static void iniciar_botones_unidad(Unidad u) {
        switch (u.nombre) {
            case "Ancestro":
                u.botones.add(new BotonComplejo(new Habilidad("Parálisis temporal")));
                break;
        }
    }

    public static void unidad(Jugador aliado, Unidad u) {
        switch (u.nombre) {
            //Eternium
            case "Adepto":
                Eternium.Adepto(aliado, u);
                break;
            case "Guerrero":
                Eternium.Guerrero(aliado, u);
                break;
            case "Ancestro":
                Eternium.Ancestro(aliado, u);
                break;
            case "Protector":
                Eternium.Protector(aliado, u);
                break;
            case "Erradicador":
                Eternium.Erradicador(aliado, u);
                break;
        }
    }

    public static void edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
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
        }
    }

    public static void temporalRelease() {
        getPlayersByRace(RaceEnum.ETERNIUM).forEach(p -> p.unidades.stream().filter(u -> "Protector".equals(u.nombre)).forEach(u -> u.isProyectileAttraction = false));
    }

    public static void temporalStop() {
        getPlayersByRace(RaceEnum.ETERNIUM).forEach(p -> p.unidades.stream().filter(u -> "Protector".equals(u.nombre)).forEach(u -> u.isProyectileAttraction = true));
        getPlayersByRace(RaceEnum.ETERNIUM).forEach(p -> p.unidades.stream().filter(u -> !"Protector".equals(u.nombre)).forEach(u -> u.parar()));
    }
}
