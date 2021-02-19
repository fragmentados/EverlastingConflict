/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.razas;

import everlastingconflict.elementos.implementacion.Unidad;

/**
 *
 * @author Elías
 */
public class Maestros {

    public static float cadencia_pugnator = Unidad.cadencia_estandar - 0.1f;
    public static int ataque_saggitarius = Unidad.ataque_estandar + 5;
    public static float velocidad_exterminatore = Unidad.velocidad_estandar + 0.2f;
    public static int area_magum = Unidad.area_estandar + 50;
    public static int ataque_medicum = Unidad.ataque_estandar + 5;
    public static boolean habilidades = false;

    public static void Pugnator(Unidad u) {
        u.ataque = Unidad.ataque_estandar;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 10;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Maestros.cadencia_pugnator;
        u.velocidad = Unidad.velocidad_estandar + 0.2f;
        u.vision = Unidad.vision_estandar + 50;
        u.descripcion = "Soldado cuerpo a cuerpo básico. Alta velocidad y velocidad de ataque.";
    }

    public static void Saggitarius(Unidad u) {
        u.ataque = Maestros.ataque_saggitarius;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 20;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar + 0.3f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar + 60;
        u.descripcion = "Unidad provista de arco y flechas de energía. Alta precisión y daño de ataque pero igual de alta fragilidad.";
    }

    public static void Exterminatore(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 15;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar + 0.2f;
        u.velocidad = Maestros.velocidad_exterminatore;
        u.vision = Unidad.vision_estandar + 50;
        u.descripcion = "Unidad con la mayor potencia de ataque de las invocaciones y fragilidad media.";
    }

    public static void Magum(Unidad u) {
        u.ataque = Unidad.ataque_estandar;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 25;
        u.alcance = Unidad.alcance_estandar + 100;
        u.cadencia = Unidad.cadencia_estandar + 0.5f;
        u.velocidad = Unidad.velocidad_estandar - 0.5f;
        u.vision = Unidad.vision_estandar;
        u.area = Maestros.area_magum;
        u.descripcion = "Unidad con capacidad ofensiva a distancia de área. Especialmente efectivo contra grandes grupos de unidades pequeñas.";
    }

    public static void Medicum(Unidad u) {
        u.ataque = Maestros.ataque_medicum;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 10;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar - 0.2f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar + 50;
        u.descripcion = "Unidad de apoyo sin capacidades ofensivas capaz de curar a unidades aliadas.";
        u.hostil = false;
        u.healer = true;
    }

    public static void Manipulador(Unidad u) {
        u.ataque = Unidad.ataque_estandar;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 900;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar - 0.1f;
        u.vision = Unidad.vision_estandar + 100;
        u.descripcion = "Unidad principal.";
    }

    public static void iniciar_botones_unidad(Unidad u) {
        switch (u.nombre) {

        }
    }

}
