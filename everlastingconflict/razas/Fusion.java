/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.razas;

import everlastingconflict.relojes.Reloj;
import everlastingconflict.elementos.implementacion.Unidad;

import java.util.List;

/**
 *
 * @author Elías
 */
public class Fusion {

    public List<Unidad> contenido;
    public static float tiempo_fusion = 2f;
    public Unidad resultado = null;

    public Fusion(List<Unidad> c) {
        contenido = c;
    }

    public boolean comprobacion() {
        if (resultado != null) {
            return true;
        } else {
            float x = 0, y = 0;
            for (int i = 0; i < contenido.size(); i++) {
                if (i == 0) {
                    //Cojo las coordenadas de la primera unidad
                    x = contenido.get(0).x;
                    y = contenido.get(0).y;
                } else {
                    //Comprobar que todas las unidades están en la misma posición
                    if (contenido.get(i).x != x || contenido.get(i).y != y) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean comportamiento(int delta) {
        if (resultado.vida + (resultado.vida_max / resultado.tiempo) * Reloj.velocidad_reloj * delta >= resultado.vida_max) {
            //Acaba la construccion
            resultado.vida = resultado.vida_max;
            return true;
        } else {
            resultado.vida += (resultado.vida_max / resultado.tiempo) * Reloj.velocidad_reloj * delta;
        }
        return false;
    }
}
