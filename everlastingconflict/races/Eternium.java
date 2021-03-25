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
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Eternium {

    public static int ataque_guerrero = Unidad.ataque_estandar + 14;
    public static int ataque_ancestro = Unidad.ataque_estandar + 34;
    public static final int MAX_UNIT_PER_QUEUE = 5;
    public static int recursos_refineria = 70;

    public static void dibujar_detencion(Unidad u, Color c, Graphics g) {
        WindowCombat.detencion.draw(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura, u.altura);
        g.setColor(Color.black);
        g.drawRect(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura, u.altura);
        g.setColor(Color.white);
        g.setColor(Color.white);
        //Dibujar barra de vida
        g.drawRect(u.x - u.anchura / 2, u.y + u.altura / 2, u.anchura_barra_vida, u.altura_barra_vida);
        g.setColor(c);
        g.fillRect(u.x - u.anchura / 2, u.y + u.altura / 2, u.anchura_barra_vida * (u.vida / u.vida_max),
                u.altura_barra_vida);
        g.setColor(Color.white);
        //g.setColor(Color.red);
        //g.fillRect(x + anchura_barra_vida * (vida / vida_max), y + altura, anchura_barra_vida - (anchura_barra_vida
        // * (vida / vida_max)), altura_barra_vida);
        if (WindowCombat.ui.elementos.indexOf(u) != -1) {
            u.circulo(g, c);
        }
    }

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
        u.botones.add(new BotonComplejo(new Edificio("Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad básica de ataque a distancia.";
    }

    public static void Guerrero(Unidad u) {
        u.ataque = Eternium.ataque_guerrero;
        u.defensa = Unidad.defensa_estandar + 3;
        u.vida_max = Unidad.vida_estandar + 80;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar - 0.1f;
        u.vision = Unidad.vision_estandar;
        u.coste = 125;
        u.tiempo = Unidad.tiempo_estandar + 5;
        u.botones.add(new BotonComplejo(new Edificio("Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad capaz de recibir grandes daños antes de ser destruida.";
    }

    public static void Ancestro(Unidad u) {
        u.ataque = Eternium.ataque_ancestro;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar + 50;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar - 0.2f;
        u.vision = Unidad.vision_estandar - 50;
        u.coste = 200;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.botones.add(new BotonComplejo(new Edificio("Refinería")));
        u.constructor = true;
        u.descripcion = "Unidad con gran capacidad ofensiva y con la habilidad para alterar el tiempo.";
    }

    public static void Protector(Unidad u) {
        u.ataque = 0;
        u.defensa = 2;
        u.vida_max = Unidad.vida_estandar + 100;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar + 0.2f;
        u.velocidad = Unidad.velocidad_estandar - 0.3f;
        u.vision = Unidad.vision_estandar;
        u.coste = 100;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.botones.add(new BotonComplejo(new Edificio("Refinería")));
        u.constructor = true;
        u.hostil = false;
        u.descripcion = "Unidad defensiva que atrae los proyectiles enemigos durante la detención temporal.";
    }

    public static void Erradicador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 30;
        u.defensa = 2;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.5f;
        u.vision = Unidad.vision_estandar;
        u.coste = 80;
        u.tiempo = Unidad.tiempo_estandar + 2;
        u.botones.add(new BotonComplejo(new Edificio("Refinería")));
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

    public static void iniciar_botones_edificio(Edificio e, Jugador aliado) {
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
                e.botones.add(new BotonComplejo(new Edificio("Cámara de asimilación")));
                e.botones.add(new BotonComplejo(new Edificio("Teletransportador")));
                e.botones.add(new BotonComplejo(new Edificio("Altar de los ancestros")));
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
}