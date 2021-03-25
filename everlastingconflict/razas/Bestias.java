/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.razas;

import everlastingconflict.elementos.implementacion.Bestia;
import everlastingconflict.elementos.implementacion.Unidad;

/**
 *
 * @author Elías
 */
public class Bestias {

    public static final void Alpha(Bestia u) {
        u.ataque = Unidad.ataque_estandar - 4;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar - 90;
        u.cadencia = Unidad.cadencia_estandar + 0.5f;
        u.velocidad = Unidad.velocidad_estandar - 0.1f;
        u.recompensa = 50;
        u.vision = Unidad.vision_estandar + 100;
    }

    public static final void Beta(Bestia u) {
        u.ataque = Unidad.ataque_estandar + 20;
        u.vida_max = Unidad.vida_estandar + 200;
        u.alcance = Unidad.alcance_estandar - 90;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.recompensa = 100;
        u.vision = Unidad.vision_estandar;
    }

    public static final void Gamma(Bestia u) {
        u.ataque = Unidad.ataque_estandar + 40;
        u.vida_max = Unidad.vida_estandar + 400;
        u.alcance = Unidad.alcance_estandar - 90;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.recompensa = 200;
        u.vision = Unidad.vision_estandar;
    }

    public static final void Ommega(Bestia u) {
        u.ataque = Unidad.ataque_estandar + 60;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar + 0.5f;
        u.velocidad = Unidad.velocidad_estandar;
        u.recompensa = 300;
        u.vision = Unidad.vision_estandar;
    }
}
